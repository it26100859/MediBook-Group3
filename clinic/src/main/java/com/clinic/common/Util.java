package com.clinic.common;

import java.io.IOException;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.servlet.http.*;

public final class Util {
    private Util() { }

    public static String clean(String s) {
        return s == null ? "" : s.replace('|', ' ').replace('\r', ' ').replace('\n', ' ').trim();
    }

    public static String esc(Object o) {
        if (o == null) return "";
        return o.toString().replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
                .replace("\"", "&quot;").replace("'", "&#39;");
    }

    public static String hash(String s) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            StringBuilder sb = new StringBuilder();
            for (byte b : md.digest(s.getBytes("UTF-8"))) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) { throw new RuntimeException(e); }
    }

    public static String param(HttpServletRequest r, String name) { return clean(r.getParameter(name)); }
    public static double num(String s, double def) { try { return Double.parseDouble(s.trim()); } catch (Exception e) { return def; } }
    public static String now() { return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")); }
    public static String money(double d) { return String.format("Rs. %.2f", d); }

    public static User user(HttpServletRequest r) {
        HttpSession s = r.getSession(false);
        return s == null ? null : (User) s.getAttribute("user");
    }

    public static void flash(HttpServletRequest r, String msg) { r.getSession().setAttribute("flash", msg); }

    private static boolean deny(HttpServletRequest r, HttpServletResponse resp, String msg) throws IOException {
        flash(r, msg);
        resp.sendRedirect(r.getContextPath() + "/login");
        return false;
    }

    public static boolean requireLogin(HttpServletRequest r, HttpServletResponse resp) throws IOException {
        return user(r) != null || deny(r, resp, "Please log in first.");
    }

    public static boolean requirePatient(HttpServletRequest r, HttpServletResponse resp) throws IOException {
        User u = user(r);
        return (u != null && "PATIENT".equals(u.getRole())) || deny(r, resp, "Please log in as a patient.");
    }

    /** perm == null means any admin. */
    public static boolean requireAdmin(HttpServletRequest r, HttpServletResponse resp, String perm) throws IOException {
        User u = user(r);
        boolean ok = u != null && "ADMIN".equals(u.getRole()) && (perm == null || u.hasPermission(perm));
        return ok || deny(r, resp, "Admin access with the right permission is required.");
    }

    public static boolean isAdmin(HttpServletRequest r) { User u = user(r); return u != null && "ADMIN".equals(u.getRole()); }

    public static void log(HttpServletRequest r, String action) {
        User u = user(r);
        if (u != null && "ADMIN".equals(u.getRole()))
            FileHandler.append("adminlog.txt", new String[]{now(), u.getUsername(), clean(action)});
    }
}
