package com.clinic.billing;

public class CashPayment extends Payment {
    @Override public String getMethod() { return "CASH"; }
    @Override public double surcharge() { return 0; }
    @Override public String process() { return String.format("Cash payment of Rs. %.2f received for bill %s.", getTotal(), id); }
}
