package com.clinic.common;

/** Base class for every account (Patient, Admin). */
public abstract class User {
    protected String id = "", username = "", password = "", email = "";

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public abstract String getRole();
    public abstract String getDisplayName();
    public abstract String[] toRow();

    /** Default authentication: username + SHA-256 password hash. Subclasses may add rules (polymorphism). */
    public boolean authenticate(String user, String plainPassword) {
        return username.equalsIgnoreCase(user) && password.equals(Util.hash(plainPassword));
    }

    public boolean hasPermission(String permission) { return false; }
}
