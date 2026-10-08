package com.clinic.appointment;

import com.clinic.doctor.Doctor;
import java.time.LocalDate;
import java.util.Arrays;

/** Abstraction: hides slot/availability rules from the servlet and JSPs. */
public class AppointmentService {
    public static final String[] SLOTS = {"09:00", "10:00", "11:00", "12:00", "14:00", "15:00", "16:00"};

    public static boolean isSlotAvailable(String doctorId, String date, String time, String ignoreId) {
        for (Appointment a : AppointmentDAO.all())
            if (a.getDoctorId().equals(doctorId) && a.getDate().equals(date) && a.getTime().equals(time)
                    && !"CANCELLED".equals(a.getStatus()) && !a.getId().equals(ignoreId)) return false;
        return true;
    }

    /** Returns an error message, or null when the booking is valid. */
    public static String validate(Doctor d, String date, String time, String ignoreId) {
        if (d == null) return "Please choose a doctor.";
        LocalDate ld;
        try { ld = LocalDate.parse(date); } catch (Exception e) { return "Please enter a valid date."; }
        if (ld.isBefore(LocalDate.now())) return "The date must be today or later.";
        if (!d.worksOn(ld.getDayOfWeek())) return "That doctor is not available on that day (works: " + d.getAvailability() + ").";
        if (!Arrays.asList(SLOTS).contains(time)) return "Please choose a valid time slot.";
        if (!isSlotAvailable(d.getId(), date, time, ignoreId)) return "That slot is already booked. Choose another time.";
        return null;
    }
}
