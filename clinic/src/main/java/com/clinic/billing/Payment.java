package com.clinic.billing;

public abstract class Payment {
    protected String id = "", appointmentId = "", patientId = "", status = "PENDING", date = "";
    protected double baseAmount, discount;

    public String getId() { return id; }
    public void setId(String v) { id = v; }
    public String getAppointmentId() { return appointmentId; }
    public void setAppointmentId(String v) { appointmentId = v; }
    public String getPatientId() { return patientId; }
    public void setPatientId(String v) { patientId = v; }
    public String getStatus() { return status; }
    public void setStatus(String v) { status = v; }
    public String getDate() { return date; }
    public void setDate(String v) { date = v; }
    public double getBaseAmount() { return baseAmount; }
    public void setBaseAmount(double v) { baseAmount = v; }
    public double getDiscount() { return discount; }
    public void setDiscount(double v) { discount = v; }

    public abstract String getMethod();
    /** Extra charge that depends on the payment method. */
    public abstract double surcharge();
    /** Receipt message produced when the payment is processed. */
    public abstract String process();

    public double getTotal() { return baseAmount - discount + surcharge(); }

    public String[] toRow() {
        return new String[]{id, appointmentId, patientId, String.valueOf(baseAmount), String.valueOf(discount), getMethod(), status, date};
    }
}
