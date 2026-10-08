package com.clinic.doctor;

public class Specialist extends Doctor {
    @Override public String getType() { return "SPECIALIST"; }
    @Override public double consultationFee() { return baseFee * 1.5; }
    @Override public String display() { return "Dr. " + name + " (Specialist - " + specialization + ")"; }
}
