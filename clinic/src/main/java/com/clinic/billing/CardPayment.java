package com.clinic.billing;

public class CardPayment extends Payment {
    @Override public String getMethod() { return "CARD"; }
    @Override public double surcharge() { return (baseAmount - discount) * 0.025; }
    @Override public String process() { return String.format("Card payment of Rs. %.2f (incl. 2.5%% card fee) charged for bill %s.", getTotal(), id); }
}
