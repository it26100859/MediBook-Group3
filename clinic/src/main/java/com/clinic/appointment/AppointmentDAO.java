package com.clinic.appointment;

import com.clinic.common.FileHandler;
import java.util.*;

public class AppointmentDAO {
    private static final String FILE = "appointments.txt";

    public static List<Appointment> all() {
        List<Appointment> list = new ArrayList<>();
        for (String[] r : FileHandler.readAll(FILE)) if (r.length >= 8) list.add(Appointment.fromRow(r));
        return list;
    }

    public static Appointment find(String id) {
        for (Appointment a : all()) if (a.getId().equals(id)) return a;
        return null;
    }

    public static List<Appointment> byPatient(String patientId) {
        List<Appointment> out = new ArrayList<>();
        for (Appointment a : all()) if (a.getPatientId().equals(patientId)) out.add(a);
        return out;
    }

    public static void add(Appointment a) { a.setId(FileHandler.nextId("A", FILE)); FileHandler.append(FILE, a.toRow()); }
    public static void update(Appointment a) { FileHandler.replaceRow(FILE, a.getId(), a.toRow()); }
    public static void delete(String id) { FileHandler.deleteRow(FILE, id); }
}
