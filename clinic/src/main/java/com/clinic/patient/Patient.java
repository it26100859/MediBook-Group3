package com.clinic.patient;

import com.clinic.common.User;

public abstract class Patient extends User {
    private String fullName = "", phone = "", insuranceNo = "";

    public String getFullName() { return fullName; }
    public void setFullName(String v) { fullName = v; }
    public String getPhone() { return phone; }
    public void setPhone(String v) { phone = v; }
    public String getInsuranceNo() { return insuranceNo; }
    public void setInsuranceNo(String v) { insuranceNo = v; }

    public abstract String getType();
    /** Share of the consultation fee covered/discounted for this patient type. */
    public abstract double discountRate();
    /** Fee charged when this patient cancels a booked appointment. */
    public abstract double cancellationFee(double consultationFee);

    @Override public String getRole() { return "PATIENT"; }
    @Override public String getDisplayName() { return fullName; }
    @Override public String[] toRow() {
        return new String[]{id, username, password, email, fullName, phone, getType(), insuranceNo};
    }
}
