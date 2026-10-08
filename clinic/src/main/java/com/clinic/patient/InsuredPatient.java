package com.clinic.patient;

public class InsuredPatient extends Patient {
    @Override public String getType() { return "INSURED"; }
    @Override public double discountRate() { return 0.30; }
    @Override public double cancellationFee(double fee) { return 0.0; }
}
