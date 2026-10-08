package com.clinic.doctor;

public class GeneralPractitioner extends Doctor {
    @Override public String getType() { return "GP"; }
    @Override public double consultationFee() { return baseFee; }
    @Override public String display() { return "Dr. " + name + " (General Practitioner)"; }
}
