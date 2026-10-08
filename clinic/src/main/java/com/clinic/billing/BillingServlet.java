package com.clinic.billing;

import com.clinic.appointment.*;
import com.clinic.common.*;
import com.clinic.doctor.*;
import com.clinic.patient.*;
import java.io.IOException;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/billing")
public class BillingServlet extends HttpServlet {

    private void show(HttpServletRequest req, HttpServletResponse resp, String page) throws ServletException, IOException {
        req.getRequestDispatcher("/billing/" + page).forward(req, resp);
    }

    /** Amount to bill for an appointment: consultation fee if completed, cancellation fee if cancelled. */
    private double billable(Appointment a) {
        if ("COMPLETED".equals(a.getStatus())) { Doctor d = DoctorDAO.find(a.getDoctorId()); return d == null ? 0 : d.consultationFee(); }
        if ("CANCELLED".equals(a.getStatus())) return a.getCancelFee();
        return 0;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String a = req.getParameter("action") == null ? "list" : req.getParameter("action");
        switch (a) {
            case "generate": {
                if (!Util.requireAdmin(req, resp, "BILLING")) return;
                List<String[]> rows = new ArrayList<>();
                for (Appointment ap : AppointmentDAO.all()) {
                    double amt = billable(ap);
                    if (amt <= 0 || BillingDAO.existsForAppointment(ap.getId())) continue;
                    Patient p = PatientDAO.find(ap.getPatientId());
                    Doctor d = DoctorDAO.find(ap.getDoctorId());
                    rows.add(new String[]{ap.getId(), p == null ? "(removed)" : p.getFullName(), d == null ? "(removed)" : d.getName(),
                            ap.getDate(), ap.getStatus(), Util.money(amt)});
                }
                req.setAttribute("billable", rows);
                show(req, resp, "generate.jsp"); break;
            }
            case "pay": {
                if (!Util.requirePatient(req, resp)) return;
                Payment p = BillingDAO.find(Util.param(req, "id"));
                if (p == null || !p.getPatientId().equals(Util.user(req).getId()) || !"PENDING".equals(p.getStatus())) {
                    Util.flash(req, "That bill cannot be paid."); resp.sendRedirect(req.getContextPath() + "/billing"); return;
                }
                req.setAttribute("b", p); show(req, resp, "pay.jsp"); break;
            }
            case "edit": {
                if (!Util.requireAdmin(req, resp, "BILLING")) return;
                Payment p = BillingDAO.find(Util.param(req, "id"));
                if (p == null || !"PENDING".equals(p.getStatus())) { Util.flash(req, "Only pending bills can be edited."); resp.sendRedirect(req.getContextPath() + "/billing"); return; }
                req.setAttribute("b", p); show(req, resp, "edit.jsp"); break;
            }
            default: {
                if (!Util.requireLogin(req, resp)) return;
                req.setAttribute("bills", Util.isAdmin(req) ? BillingDAO.all() : BillingDAO.byPatient(Util.user(req).getId()));
                show(req, resp, "list.jsp");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (!Util.requireLogin(req, resp)) return;
        String act = Util.param(req, "action"), ctx = req.getContextPath(), id = Util.param(req, "id");
        User me = Util.user(req);
        boolean admin = me.hasPermission("BILLING");
        String back = ctx + "/billing";

        if ("generate".equals(act) && admin) {
            Appointment ap = AppointmentDAO.find(Util.param(req, "appointmentId"));
            double amt = ap == null ? 0 : billable(ap);
            if (ap == null || amt <= 0 || BillingDAO.existsForAppointment(ap.getId())) Util.flash(req, "That appointment cannot be billed.");
            else {
                Patient pt = PatientDAO.find(ap.getPatientId());
                Payment p = new PendingPayment();
                p.setAppointmentId(ap.getId()); p.setPatientId(ap.getPatientId()); p.setBaseAmount(amt);
                p.setDiscount("COMPLETED".equals(ap.getStatus()) && pt != null ? amt * pt.discountRate() : 0);
                p.setDate(Util.now());
                BillingDAO.add(p);
                Util.log(req, "Generated bill " + p.getId());
                Util.flash(req, "Bill " + p.getId() + " generated.");
            }
            resp.sendRedirect(back); return;
        }

        Payment p = BillingDAO.find(id);
        if (p == null) { Util.flash(req, "Bill not found."); resp.sendRedirect(back); return; }

        if ("pay".equals(act) && p.getPatientId().equals(me.getId()) && "PENDING".equals(p.getStatus())) {
            Payment paid = "CARD".equals(Util.param(req, "method")) ? new CardPayment() : new CashPayment();
            paid.setId(p.getId()); paid.setAppointmentId(p.getAppointmentId()); paid.setPatientId(p.getPatientId());
            paid.setBaseAmount(p.getBaseAmount()); paid.setDiscount(p.getDiscount());
            paid.setStatus("PAID"); paid.setDate(Util.now());
            BillingDAO.update(paid);
            Util.flash(req, paid.process());
        } else if ("discount".equals(act) && admin && "PENDING".equals(p.getStatus())) {
            double d = Util.num(Util.param(req, "discount"), -1);
            if (d < 0 || d > p.getBaseAmount()) Util.flash(req, "Discount must be between 0 and the bill amount.");
            else { p.setDiscount(d); BillingDAO.update(p); Util.log(req, "Changed discount on bill " + id); Util.flash(req, "Discount updated."); }
        } else if ("delete".equals(act) && admin && "PAID".equals(p.getStatus())) {
            BillingDAO.delete(id); Util.log(req, "Deleted bill " + id); Util.flash(req, "Settled bill removed.");
        } else Util.flash(req, "That action is not allowed.");
        resp.sendRedirect(back);
    }
}
