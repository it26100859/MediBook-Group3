package com.clinic.review;

public abstract class Review {
    protected String id = "", patientId = "", patientName = "", doctorId = "", comment = "", date = "";
    protected int rating = 5;

    public String getId() { return id; }
    public void setId(String v) { id = v; }
    public String getPatientId() { return patientId; }
    public void setPatientId(String v) { patientId = v; }
    public String getPatientName() { return patientName; }
    public void setPatientName(String v) { patientName = v; }
    public String getDoctorId() { return doctorId; }
    public void setDoctorId(String v) { doctorId = v; }
    public String getComment() { return comment; }
    public void setComment(String v) { comment = v; }
    public String getDate() { return date; }
    public void setDate(String v) { date = v; }
    public int getRating() { return rating; }
    public void setRating(int v) { rating = Math.max(1, Math.min(5, v)); }

    public abstract String getType();
    /** Different presentation for admins and regular users (polymorphism). */
    public abstract String displayFor(boolean admin);

    public String[] toRow() {
        return new String[]{id, patientId, patientName, doctorId, String.valueOf(rating), comment, date, getType()};
    }
}
