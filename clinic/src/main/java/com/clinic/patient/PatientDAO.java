package com.clinic.patient;

import com.clinic.common.FileHandler;
import java.util.*;

public class PatientDAO {
    private static final String FILE = "patients.txt";

    public static Patient fromRow(String[] r) {
        Patient p = "INSURED".equals(r[6]) ? new InsuredPatient() : new RegularPatient();
        p.setId(r[0]); p.setUsername(r[1]); p.setPassword(r[2]); p.setEmail(r[3]);
        p.setFullName(r[4]); p.setPhone(r[5]); p.setInsuranceNo(r[7]);
        return p;
    }

    public static List<Patient> all() {
        List<Patient> list = new ArrayList<>();
        for (String[] r : FileHandler.readAll(FILE)) if (r.length >= 8) list.add(fromRow(r));
        return list;
    }

    public static Patient find(String id) {
        for (Patient p : all()) if (p.getId().equals(id)) return p;
        return null;
    }

    public static Patient findByUsername(String username) {
        for (Patient p : all()) if (p.getUsername().equalsIgnoreCase(username)) return p;
        return null;
    }

    public static List<Patient> search(String q) {
        List<Patient> out = new ArrayList<>();
        String s = q == null ? "" : q.toLowerCase();
        for (Patient p : all())
            if (s.isEmpty() || p.getId().toLowerCase().contains(s) || p.getUsername().toLowerCase().contains(s)
                    || p.getFullName().toLowerCase().contains(s)) out.add(p);
        return out;
    }

    public static void add(Patient p) {
        p.setId(FileHandler.nextId("P", FILE));
        FileHandler.append(FILE, p.toRow());
    }

    public static void update(String id, String[] row) { FileHandler.replaceRow(FILE, id, row); }
    public static void delete(String id) { FileHandler.deleteRow(FILE, id); }
}
