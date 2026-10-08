package com.clinic.appointment;

import com.clinic.common.*;
import com.clinic.doctor.*;
import com.clinic.patient.*;
import java.io.IOException;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/appointment")
public class AppointmentServlet extends HttpServlet {

    private void show(HttpServletRequest req, HttpServletResponse resp, String page) throws ServletException, IOException {
        req.getRequestDispatcher("/appointment/" + page).forward(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String a = req.getParameter("action") == null ? "list" : req.getParameter("action");
        if ("book".equals(a)) {
            if (!Util.requirePatient(req, resp)) return;
            req.setAttribute("doctors", DoctorDAO.all());
            req.setAttribute("selected", Util.param(req, "doctorId"));
            show(req, resp, "book.jsp");
        } else if ("reschedule".equals(a)) {
            if (!Util.requirePatient(req, resp)) return;
            Appointment ap = AppointmentDAO.find(Util.param(req, "id"));
            if (ap == null || !ap.getPatientId().equals(Util.user(req).getId()) || !"BOOKED".equals(ap.getStatus())) {
                Util.flash(req, "That appointment cannot be rescheduled."); resp.sendRedirect(req.getContextPath() + "/appointment"); return;
            }
            req.setAttribute("a", ap);
            req.setAttribute("doctor", DoctorDAO.find(ap.getDoctorId()));
            show(req, resp, "reschedule.jsp");
        } else {
            if (!Util.requireLogin(req, resp)) return;
            User u = Util.user(req);
            req.setAttribute("appointments", Util.isAdmin(req) ? AppointmentDAO.all() : AppointmentDAO.byPatient(u.getId()));
            show(req, resp, "list.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (!Util.requireLogin(req, resp)) return;
        String act = Util.param(req, "action"), ctx = req.getContextPath(), id = Util.param(req, "id");
        User me = Util.user(req);
        boolean admin = me.hasPermission("APPOINTMENTS");
        String back = ctx + "/appointment";

        if ("create".equals(act) && "PATIENT".equals(me.getRole())) {
            Doctor d = DoctorDAO.find(Util.param(req, "doctorId"));
            String date = Util.param(req, "date"), time = Util.param(req, "time");
            String err = AppointmentService.validate(d, date, time, "");
            if (err != null) { Util.flash(req, err); resp.sendRedirect(ctx + "/appointment?action=book"); return; }
            Appointment ap = new Appointment();
            ap.setPatientId(me.getId()); ap.setDoctorId(d.getId()); ap.setDate(date); ap.setTime(time);
            ap.setReason(Util.param(req, "reason"));
            AppointmentDAO.add(ap);
            Util.flash(req, "Appointment " + ap.getId() + " booked.");
            resp.sendRedirect(back); return;
        }

        Appointment ap = AppointmentDAO.find(id);
        if (ap == null) { Util.flash(req, "Appointment not found."); resp.sendRedirect(back); return; }
        boolean owner = ap.getPatientId().equals(me.getId());

        if ("reschedule".equals(act) && owner && "BOOKED".equals(ap.getStatus())) {
            Doctor d = DoctorDAO.find(ap.getDoctorId());
            String date = Util.param(req, "date"), time = Util.param(req, "time");
            String err = AppointmentService.validate(d, date, time, ap.getId());
            if (err != null) { Util.flash(req, err); resp.sendRedirect(ctx + "/appointment?action=reschedule&id=" + id); return; }
            ap.setDate(date); ap.setTime(time);
            AppointmentDAO.update(ap);
            Util.flash(req, "Appointment rescheduled.");
        } else if ("cancel".equals(act) && (owner || admin) && "BOOKED".equals(ap.getStatus())) {
            Patient p = PatientDAO.find(ap.getPatientId());
            Doctor d = DoctorDAO.find(ap.getDoctorId());
            double fee = (p != null && d != null) ? p.cancellationFee(d.consultationFee()) : 0;
            ap.setStatus("CANCELLED"); ap.setCancelFee(fee);
            AppointmentDAO.update(ap);
            if (admin) Util.log(req, "Cancelled appointment " + id);
            Util.flash(req, "Appointment cancelled. Cancellation fee: " + Util.money(fee));
        } else if ("complete".equals(act) && admin && "BOOKED".equals(ap.getStatus())) {
            ap.setStatus("COMPLETED");
            AppointmentDAO.update(ap);
            Util.log(req, "Completed appointment " + id);
            Util.flash(req, "Appointment marked as completed.");
        } else if ("delete".equals(act) && admin && !"BOOKED".equals(ap.getStatus())) {
            AppointmentDAO.delete(id);
            Util.log(req, "Deleted appointment " + id);
            Util.flash(req, "Appointment record removed.");
        } else Util.flash(req, "That action is not allowed.");
        resp.sendRedirect(back);
    }
}
