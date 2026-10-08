package com.clinic.review;

public class PublicReview extends Review {
    @Override public String getType() { return "PUBLIC"; }
    @Override public String displayFor(boolean admin) {
        return admin ? patientName + " (" + patientId + ", unverified): " + comment : "Anonymous: " + comment;
    }
}
