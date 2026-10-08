package com.clinic.appointment;

public class Appointment {
    private String id = "", patientId = "", doctorId = "", date = "", time = "", reason = "", status = "BOOKED";
    private double cancelFee;

    public String getId() { return id; }
    public void setId(String v) { id = v; }
    public String getPatientId() { return patientId; }
    public void setPatientId(String v) { patientId = v; }
    public String getDoctorId() { return doctorId; }
    public void setDoctorId(String v) { doctorId = v; }
    public String getDate() { return date; }
    public void setDate(String v) { date = v; }
    public String getTime() { return time; }
    public void setTime(String v) { time = v; }
    public String getReason() { return reason; }
    public void setReason(String v) { reason = v; }
    public String getStatus() { return status; }
    public void setStatus(String v) { status = v; }
    public double getCancelFee() { return cancelFee; }
    public void setCancelFee(double v) { cancelFee = v; }

    public String[] toRow() {
        return new String[]{id, patientId, doctorId, date, time, reason, status, String.valueOf(cancelFee)};
    }

    public static Appointment fromRow(String[] r) {
        Appointment a = new Appointment();
        a.id = r[0]; a.patientId = r[1]; a.doctorId = r[2]; a.date = r[3]; a.time = r[4]; a.reason = r[5]; a.status = r[6];
        try { a.cancelFee = Double.parseDouble(r[7]); } catch (NumberFormatException e) { a.cancelFee = 0; }
        return a;
    }
}
