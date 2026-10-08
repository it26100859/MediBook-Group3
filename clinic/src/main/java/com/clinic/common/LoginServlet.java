package com.clinic.common;

import com.clinic.admin.*;
import com.clinic.patient.*;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String u = Util.param(req, "username"), p = Util.param(req, "password");
        User found = null;
        Admin a = AdminDAO.findByUsername(u);
        if (a != null && a.authenticate(u, p)) found = a;
        else {
            Patient pt = PatientDAO.findByUsername(u);
            if (pt != null && pt.authenticate(u, p)) found = pt;
        }
        if (found == null) { Util.flash(req, "Invalid username or password."); resp.sendRedirect(req.getContextPath() + "/login"); return; }
        HttpSession old = req.getSession(false);
        if (old != null) old.invalidate();
        req.getSession(true).setAttribute("user", found);
        resp.sendRedirect(req.getContextPath() + ("ADMIN".equals(found.getRole()) ? "/admin" : "/index.jsp"));
    }
}
