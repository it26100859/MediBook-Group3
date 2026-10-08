package com.clinic.admin;

import com.clinic.common.*;
import java.util.*;

public class AdminDAO {
    private static final String FILE = "admins.txt";

    public static List<Admin> all() {
        List<Admin> list = new ArrayList<>();
        for (String[] r : FileHandler.readAll(FILE)) if (r.length >= 6) list.add(Admin.fromRow(r));
        return list;
    }

    public static Admin find(String id) {
        for (Admin a : all()) if (a.getId().equals(id)) return a;
        return null;
    }

    public static Admin findByUsername(String u) {
        for (Admin a : all()) if (a.getUsername().equalsIgnoreCase(u)) return a;
        return null;
    }

    public static void add(Admin a) { a.setId(FileHandler.nextId("AD", FILE)); FileHandler.append(FILE, a.toRow()); }
    public static void update(Admin a) { FileHandler.replaceRow(FILE, a.getId(), a.toRow()); }
    public static void delete(String id) { FileHandler.deleteRow(FILE, id); }

    /** Newest first. Each row: timestamp, admin username, action. */
    public static List<String[]> logs() {
        List<String[]> rows = FileHandler.readAll("adminlog.txt");
        Collections.reverse(rows);
        return rows;
    }

    /** Creates the default administrator (admin / admin123) on first start. */
    public static void seed() {
        if (!all().isEmpty()) return;
        Admin a = new Admin();
        a.setUsername("admin"); a.setPassword(Util.hash("admin123")); a.setEmail("admin@clinic.local");
        a.setFullName("System Administrator"); a.setPermissions(String.join(",", Admin.PERMISSIONS));
        add(a);
    }
}
