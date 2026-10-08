package com.clinic.review;

import com.clinic.appointment.*;
import com.clinic.common.*;
import com.clinic.doctor.DoctorDAO;
import com.clinic.patient.Patient;
import java.io.IOException;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/review")
public class ReviewServlet extends HttpServlet {

    private void show(HttpServletRequest req, HttpServletResponse resp, String page) throws ServletException, IOException {
        req.getRequestDispatcher("/review/" + page).forward(req, resp);
    }

    private boolean hasCompletedVisit(String patientId, String doctorId) {
        for (Appointment a : AppointmentDAO.byPatient(patientId))
            if (a.getDoctorId().equals(doctorId) && "COMPLETED".equals(a.getStatus())) return true;
        return false;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String a = req.getParameter("action") == null ? "list" : req.getParameter("action");
        switch (a) {
            case "submit":
                if (!Util.requirePatient(req, resp)) return;
                req.setAttribute("doctors", DoctorDAO.all());
                req.setAttribute("selected", Util.param(req, "doctorId"));
                show(req, resp, "submit.jsp"); break;
            case "edit": {
                if (!Util.requirePatient(req, resp)) return;
                Review r = ReviewDAO.find(Util.param(req, "id"));
                if (r == null || !r.getPatientId().equals(Util.user(req).getId())) { Util.flash(req, "You can only edit your own reviews."); resp.sendRedirect(req.getContextPath() + "/review"); return; }
                req.setAttribute("r", r); req.setAttribute("doctors", DoctorDAO.all());
                req.setAttribute("selected", r.getDoctorId());
                show(req, resp, "submit.jsp"); break;
            }
            case "moderate":
                if (!Util.requireAdmin(req, resp, "REVIEWS")) return;
                req.setAttribute("reviews", ReviewDAO.all());
                show(req, resp, "moderate.jsp"); break;
            default: {
                String did = Util.param(req, "doctorId");
                req.setAttribute("doctors", DoctorDAO.all());
                req.setAttribute("doctorId", did);
                List<Review> list = did.isEmpty() ? new ArrayList<Review>() : ReviewDAO.byDoctor(did);
                double sum = 0; for (Review r : list) sum += r.getRating();
                req.setAttribute("reviews", list);
                req.setAttribute("avg", list.isEmpty() ? 0 : sum / list.size());
                show(req, resp, "list.jsp");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (!Util.requireLogin(req, resp)) return;
        String act = Util.param(req, "action"), ctx = req.getContextPath(), id = Util.param(req, "id");
        User me = Util.user(req);
        boolean admin = me.hasPermission("REVIEWS");

        if ("create".equals(act) && me instanceof Patient) {
            String did = Util.param(req, "doctorId"), comment = Util.param(req, "comment");
            if (DoctorDAO.find(did) == null || comment.isEmpty()) { Util.flash(req, "Choose a doctor and write a comment."); resp.sendRedirect(ctx + "/review?action=submit"); return; }
            Review r = hasCompletedVisit(me.getId(), did) ? new VerifiedReview() : new PublicReview();
            r.setPatientId(me.getId()); r.setPatientName(me.getDisplayName()); r.setDoctorId(did);
            r.setRating((int) Util.num(Util.param(req, "rating"), 5)); r.setComment(comment); r.setDate(Util.now());
            ReviewDAO.add(r);
            Util.flash(req, "Thanks for your feedback!");
            resp.sendRedirect(ctx + "/review?doctorId=" + did); return;
        }
        Review r = ReviewDAO.find(id);
        if (r == null) { Util.flash(req, "Review not found."); resp.sendRedirect(ctx + "/review"); return; }
        boolean owner = r.getPatientId().equals(me.getId());
        if ("update".equals(act) && owner) {
            String comment = Util.param(req, "comment");
            if (comment.isEmpty()) { Util.flash(req, "Comment cannot be empty."); resp.sendRedirect(ctx + "/review?action=edit&id=" + id); return; }
            r.setRating((int) Util.num(Util.param(req, "rating"), r.getRating())); r.setComment(comment);
            ReviewDAO.update(id, r.toRow());
            Util.flash(req, "Review updated.");
            resp.sendRedirect(ctx + "/review?doctorId=" + r.getDoctorId());
        } else if ("delete".equals(act) && (owner || admin)) {
            ReviewDAO.delete(id);
            if (admin) Util.log(req, "Deleted review " + id);
            Util.flash(req, "Review deleted.");
            resp.sendRedirect(ctx + (admin && !owner ? "/review?action=moderate" : "/review?doctorId=" + r.getDoctorId()));
        } else { Util.flash(req, "That action is not allowed."); resp.sendRedirect(ctx + "/review"); }
    }
}
