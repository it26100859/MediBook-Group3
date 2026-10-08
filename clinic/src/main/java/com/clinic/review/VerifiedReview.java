package com.clinic.review;

public class VerifiedReview extends Review {
    @Override public String getType() { return "VERIFIED"; }
    @Override public String displayFor(boolean admin) {
        return admin ? patientName + " (" + patientId + ", verified): " + comment : "\u2714 Verified patient: " + comment;
    }
}
