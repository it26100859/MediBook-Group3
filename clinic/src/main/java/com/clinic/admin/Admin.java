package com.clinic.admin;

import com.clinic.common.User;

public class Admin extends User {
    public static final String[] PERMISSIONS = {"PATIENTS", "DOCTORS", "APPOINTMENTS", "BILLING", "REVIEWS", "ADMINS"};
    private String fullName = "", permissions = "";

    public String getFullName() { return fullName; }
    public void setFullName(String v) { fullName = v; }
    public String getPermissions() { return permissions; }
    public void setPermissions(String v) { permissions = v; }

    @Override public String getRole() { return "ADMIN"; }
    @Override public String getDisplayName() { return fullName; }
    @Override public boolean hasPermission(String p) { return permissions.contains(p); }

    /** Admin accounts with no permissions are treated as disabled (different rule from patients). */
    @Override public boolean authenticate(String user, String plain) {
        return super.authenticate(user, plain) && !permissions.isEmpty();
    }

    @Override public String[] toRow() { return new String[]{id, username, password, email, fullName, permissions}; }

    public static Admin fromRow(String[] r) {
        Admin a = new Admin();
        a.id = r[0]; a.username = r[1]; a.password = r[2]; a.email = r[3]; a.fullName = r[4]; a.permissions = r[5];
        return a;
    }
}
