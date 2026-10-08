package com.clinic.billing;

import com.clinic.common.FileHandler;
import java.util.*;

public class BillingDAO {
    private static final String FILE = "payments.txt";

    public static Payment fromRow(String[] r) {
        Payment p;
        switch (r[5]) {
            case "CASH": p = new CashPayment(); break;
            case "CARD": p = new CardPayment(); break;
            default: p = new PendingPayment();
        }
        p.setId(r[0]); p.setAppointmentId(r[1]); p.setPatientId(r[2]);
        try { p.setBaseAmount(Double.parseDouble(r[3])); p.setDiscount(Double.parseDouble(r[4])); } catch (NumberFormatException e) { }
        p.setStatus(r[6]); p.setDate(r[7]);
        return p;
    }

    public static List<Payment> all() {
        List<Payment> list = new ArrayList<>();
        for (String[] r : FileHandler.readAll(FILE)) if (r.length >= 8) list.add(fromRow(r));
        return list;
    }

    public static Payment find(String id) {
        for (Payment p : all()) if (p.getId().equals(id)) return p;
        return null;
    }

    public static boolean existsForAppointment(String appointmentId) {
        for (Payment p : all()) if (p.getAppointmentId().equals(appointmentId)) return true;
        return false;
    }

    public static List<Payment> byPatient(String patientId) {
        List<Payment> out = new ArrayList<>();
        for (Payment p : all()) if (p.getPatientId().equals(patientId)) out.add(p);
        return out;
    }

    public static int pendingCount() {
        int n = 0;
        for (Payment p : all()) if ("PENDING".equals(p.getStatus())) n++;
        return n;
    }

    public static void add(Payment p) { p.setId(FileHandler.nextId("B", FILE)); FileHandler.append(FILE, p.toRow()); }
    public static void update(Payment p) { FileHandler.replaceRow(FILE, p.getId(), p.toRow()); }
    public static void delete(String id) { FileHandler.deleteRow(FILE, id); }
}
