package com.clinic.patient;

import com.clinic.common.*;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/patient")
public class PatientServlet extends HttpServlet {

    private void show(HttpServletRequest req, HttpServletResponse resp, String page) throws ServletException, IOException {
        req.getRequestDispatcher("/patient/" + page).forward(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String a = req.getParameter("action") == null ? "profile" : req.getParameter("action");
        switch (a) {
            case "register": show(req, resp, "register.jsp"); break;
            case "list":
                if (!Util.requireAdmin(req, resp, "PATIENTS")) return;
                req.setAttribute("q", Util.param(req, "q"));
                req.setAttribute("patients", PatientDAO.search(Util.param(req, "q")));
                show(req, resp, "list.jsp"); break;
            case "edit":
                if (!Util.requireAdmin(req, resp, "PATIENTS")) return;
                req.setAttribute("p", PatientDAO.find(Util.param(req, "id")));
                req.setAttribute("adminEdit", true);
                show(req, resp, "profile.jsp"); break;
            default:
                if (!Util.requirePatient(req, resp)) return;
                req.setAttribute("p", PatientDAO.find(Util.user(req).getId()));
                show(req, resp, "profile.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String a = Util.param(req, "action"), ctx = req.getContextPath();
        if ("create".equals(a)) {
            String user = Util.param(req, "username"), pw = Util.param(req, "password"), name = Util.param(req, "fullName");
            String ins = Util.param(req, "insuranceNo");
            if (user.isEmpty() || name.isEmpty() || pw.length() < 6) {
                Util.flash(req, "Username, full name and a password of 6+ characters are required.");
            } else if (PatientDAO.findByUsername(user) != null || AdminLookup.exists(user)) {
                Util.flash(req, "That username is already taken.");
            } else {
                Patient p = ins.isEmpty() ? new RegularPatient() : new InsuredPatient();
                p.setUsername(user); p.setPassword(Util.hash(pw)); p.setEmail(Util.param(req, "email"));
                p.setFullName(name); p.setPhone(Util.param(req, "phone")); p.setInsuranceNo(ins);
                PatientDAO.add(p);
                Util.flash(req, "Registration successful. Please log in.");
                resp.sendRedirect(ctx + "/login"); return;
            }
            resp.sendRedirect(ctx + "/patient?action=register"); return;
        }
        if (!Util.requireLogin(req, resp)) return;
        User me = Util.user(req);
        boolean admin = me.hasPermission("PATIENTS");
        String id = Util.param(req, "id");
        Patient p = PatientDAO.find(id);
        if (p == null || (!admin && !me.getId().equals(id))) { Util.flash(req, "Not allowed."); resp.sendRedirect(ctx + "/index.jsp"); return; }

        if ("update".equals(a)) {
            String[] row = p.toRow();
            row[3] = Util.param(req, "email"); row[4] = Util.param(req, "fullName");
            row[5] = Util.param(req, "phone"); row[7] = Util.param(req, "insuranceNo");
            if (admin) row[6] = "INSURED".equals(Util.param(req, "type")) ? "INSURED" : "REGULAR";
            if (!Util.param(req, "password").isEmpty()) {
                if (Util.param(req, "password").length() < 6) { Util.flash(req, "Password must be 6+ characters."); resp.sendRedirect(ctx + "/patient"); return; }
                row[2] = Util.hash(Util.param(req, "password"));
            }
            PatientDAO.update(id, row);
            Util.log(req, "Updated patient " + id);
            if (me.getId().equals(id)) req.getSession().setAttribute("user", PatientDAO.find(id));
            Util.flash(req, "Profile updated.");
            resp.sendRedirect(ctx + (admin ? "/patient?action=list" : "/patient"));
        } else if ("delete".equals(a) && admin) {
            PatientDAO.delete(id);
            Util.log(req, "Deleted patient " + id);
            Util.flash(req, "Patient removed.");
            resp.sendRedirect(ctx + "/patient?action=list");
        } else resp.sendRedirect(ctx + "/index.jsp");
    }
}
