package com.clinic.doctor;

import java.time.DayOfWeek;

public abstract class Doctor {
    protected String id = "", name = "", specialization = "", availability = "";
    protected double baseFee;

    public String getId() { return id; }
    public void setId(String v) { id = v; }
    public String getName() { return name; }
    public void setName(String v) { name = v; }
    public String getSpecialization() { return specialization; }
    public void setSpecialization(String v) { specialization = v; }
    public String getAvailability() { return availability; }
    public void setAvailability(String v) { availability = v; }
    public double getBaseFee() { return baseFee; }
    public void setBaseFee(double v) { baseFee = v; }

    public abstract String getType();
    public abstract double consultationFee();
    public abstract String display();

    public boolean worksOn(DayOfWeek d) {
        return availability.contains(d.name().substring(0, 3));
    }

    public String[] toRow() {
        return new String[]{id, name, specialization, availability, String.valueOf(baseFee), getType()};
    }
}
