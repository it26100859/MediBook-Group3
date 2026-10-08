package com.clinic.doctor;

import com.clinic.common.Util;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/doctor")
public class DoctorServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if ("form".equals(req.getParameter("action"))) {
            if (!Util.requireAdmin(req, resp, "DOCTORS")) return;
            req.setAttribute("d", DoctorDAO.find(Util.param(req, "id")));
            req.getRequestDispatcher("/doctor/form.jsp").forward(req, resp);
            return;
        }
        req.setAttribute("q", Util.param(req, "q"));
        req.setAttribute("doctors", DoctorDAO.search(Util.param(req, "q")));
        req.getRequestDispatcher("/doctor/list.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (!Util.requireAdmin(req, resp, "DOCTORS")) return;
        String a = Util.param(req, "action"), ctx = req.getContextPath(), id = Util.param(req, "id");
        if ("delete".equals(a)) {
            DoctorDAO.delete(id);
            Util.log(req, "Deleted doctor " + id);
            Util.flash(req, "Doctor removed.");
        } else if ("create".equals(a) || "update".equals(a)) {
            String name = Util.param(req, "name"), spec = Util.param(req, "specialization");
            double fee = Util.num(Util.param(req, "baseFee"), -1);
            String[] days = req.getParameterValues("days");
            if (name.isEmpty() || spec.isEmpty() || fee <= 0 || days == null) {
                Util.flash(req, "Name, specialization, a fee above 0 and at least one available day are required.");
                resp.sendRedirect(ctx + "/doctor?action=form" + (id.isEmpty() ? "" : "&id=" + id)); return;
            }
            Doctor d = "SPECIALIST".equals(Util.param(req, "type")) ? new Specialist() : new GeneralPractitioner();
            d.setName(name); d.setSpecialization(spec); d.setBaseFee(fee); d.setAvailability(String.join(",", days));
            if ("create".equals(a)) { DoctorDAO.add(d); Util.log(req, "Added doctor " + d.getId()); }
            else { d.setId(id); DoctorDAO.update(d); Util.log(req, "Updated doctor " + id); }
            Util.flash(req, "Doctor saved.");
        }
        resp.sendRedirect(ctx + "/doctor");
    }
}
