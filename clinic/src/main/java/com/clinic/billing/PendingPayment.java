package com.clinic.billing;

public class PendingPayment extends Payment {
    @Override public String getMethod() { return "NONE"; }
    @Override public double surcharge() { return 0; }
    @Override public String process() { return "Bill " + id + " is awaiting payment."; }
}
