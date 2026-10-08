package com.clinic.admin;

import com.clinic.appointment.AppointmentDAO;
import com.clinic.billing.BillingDAO;
import com.clinic.common.*;
import com.clinic.doctor.DoctorDAO;
import com.clinic.patient.PatientDAO;
import com.clinic.review.ReviewDAO;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/admin")
public class AdminServlet extends HttpServlet {

    private void show(HttpServletRequest req, HttpServletResponse resp, String page) throws ServletException, IOException {
        req.getRequestDispatcher("/admin/" + page).forward(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String a = req.getParameter("action") == null ? "dashboard" : req.getParameter("action");
        if ("dashboard".equals(a)) {
            if (!Util.requireAdmin(req, resp, null)) return;
            req.setAttribute("nPatients", PatientDAO.all().size());
            req.setAttribute("nDoctors", DoctorDAO.all().size());
            req.setAttribute("nAppointments", AppointmentDAO.all().size());
            req.setAttribute("nPending", BillingDAO.pendingCount());
            req.setAttribute("nReviews", ReviewDAO.all().size());
            show(req, resp, "dashboard.jsp");
            return;
        }
        if (!Util.requireAdmin(req, resp, "ADMINS")) return;
        switch (a) {
            case "form": req.setAttribute("ad", AdminDAO.find(Util.param(req, "id"))); show(req, resp, "form.jsp"); break;
            case "logs": req.setAttribute("logs", AdminDAO.logs()); show(req, resp, "logs.jsp"); break;
            default: req.setAttribute("admins", AdminDAO.all()); show(req, resp, "list.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (!Util.requireAdmin(req, resp, "ADMINS")) return;
        String a = Util.param(req, "action"), ctx = req.getContextPath(), id = Util.param(req, "id");
        String back = ctx + "/admin?action=list";
        if ("delete".equals(a)) {
            long withAdminRight = AdminDAO.all().stream().filter(x -> x.hasPermission("ADMINS")).count();
            Admin target = AdminDAO.find(id);
            if (id.equals(Util.user(req).getId())) Util.flash(req, "You cannot delete your own account.");
            else if (target != null && target.hasPermission("ADMINS") && withAdminRight <= 1) Util.flash(req, "At least one admin must keep the ADMINS permission.");
            else { AdminDAO.delete(id); Util.log(req, "Deleted admin " + id); Util.flash(req, "Admin removed."); }
        } else if ("create".equals(a) || "update".equals(a)) {
            String user = Util.param(req, "username"), name = Util.param(req, "fullName"), pw = Util.param(req, "password");
            String[] perms = req.getParameterValues("permissions");
            boolean create = "create".equals(a);
            Admin existing = AdminDAO.findByUsername(user);
            boolean taken = existing != null && !existing.getId().equals(id);
            if (user.isEmpty() || name.isEmpty() || (create && pw.length() < 6) || (!pw.isEmpty() && pw.length() < 6)) {
                Util.flash(req, "Username, full name and a 6+ character password are required.");
                resp.sendRedirect(ctx + "/admin?action=form" + (create ? "" : "&id=" + id)); return;
            }
            if (taken || (create && PatientDAO.findByUsername(user) != null)) {
                Util.flash(req, "That username is already taken.");
                resp.sendRedirect(ctx + "/admin?action=form" + (create ? "" : "&id=" + id)); return;
            }
            Admin ad = create ? new Admin() : AdminDAO.find(id);
            if (ad == null) { resp.sendRedirect(back); return; }
            ad.setUsername(user); ad.setFullName(name); ad.setEmail(Util.param(req, "email"));
            ad.setPermissions(perms == null ? "" : String.join(",", perms));
            if (!pw.isEmpty()) ad.setPassword(Util.hash(pw));
            if (create) { AdminDAO.add(ad); Util.log(req, "Created admin " + ad.getId()); }
            else {
                AdminDAO.update(ad); Util.log(req, "Updated admin " + id);
                if (id.equals(Util.user(req).getId())) req.getSession().setAttribute("user", ad);
            }
            Util.flash(req, "Admin saved.");
        }
        resp.sendRedirect(back);
    }
}
