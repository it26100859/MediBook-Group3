package com.clinic.patient;

public class RegularPatient extends Patient {
    @Override public String getType() { return "REGULAR"; }
    @Override public double discountRate() { return 0.0; }
    @Override public double cancellationFee(double fee) { return fee * 0.20; }
}
