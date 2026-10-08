package com.clinic.doctor;

import com.clinic.common.FileHandler;
import java.util.*;

public class DoctorDAO {
    private static final String FILE = "doctors.txt";

    public static Doctor fromRow(String[] r) {
        Doctor d = "SPECIALIST".equals(r[5]) ? new Specialist() : new GeneralPractitioner();
        d.setId(r[0]); d.setName(r[1]); d.setSpecialization(r[2]); d.setAvailability(r[3]);
        try { d.setBaseFee(Double.parseDouble(r[4])); } catch (NumberFormatException e) { d.setBaseFee(0); }
        return d;
    }

    public static List<Doctor> all() {
        List<Doctor> list = new ArrayList<>();
        for (String[] r : FileHandler.readAll(FILE)) if (r.length >= 6) list.add(fromRow(r));
        return list;
    }

    public static Doctor find(String id) {
        for (Doctor d : all()) if (d.getId().equals(id)) return d;
        return null;
    }

    public static List<Doctor> search(String q) {
        List<Doctor> out = new ArrayList<>();
        String s = q == null ? "" : q.toLowerCase();
        for (Doctor d : all())
            if (s.isEmpty() || d.getName().toLowerCase().contains(s) || d.getSpecialization().toLowerCase().contains(s)) out.add(d);
        return out;
    }

    public static void add(Doctor d) { d.setId(FileHandler.nextId("D", FILE)); FileHandler.append(FILE, d.toRow()); }
    public static void update(Doctor d) { FileHandler.replaceRow(FILE, d.getId(), d.toRow()); }
    public static void delete(String id) { FileHandler.deleteRow(FILE, id); }
}
