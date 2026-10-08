package com.clinic.review;

import com.clinic.common.FileHandler;
import java.util.*;

public class ReviewDAO {
    private static final String FILE = "reviews.txt";

    public static Review fromRow(String[] r) {
        Review v = "VERIFIED".equals(r[7]) ? new VerifiedReview() : new PublicReview();
        v.setId(r[0]); v.setPatientId(r[1]); v.setPatientName(r[2]); v.setDoctorId(r[3]);
        try { v.setRating(Integer.parseInt(r[4])); } catch (NumberFormatException e) { }
        v.setComment(r[5]); v.setDate(r[6]);
        return v;
    }

    public static List<Review> all() {
        List<Review> list = new ArrayList<>();
        for (String[] r : FileHandler.readAll(FILE)) if (r.length >= 8) list.add(fromRow(r));
        return list;
    }

    public static Review find(String id) {
        for (Review r : all()) if (r.getId().equals(id)) return r;
        return null;
    }

    public static List<Review> byDoctor(String doctorId) {
        List<Review> out = new ArrayList<>();
        for (Review r : all()) if (r.getDoctorId().equals(doctorId)) out.add(r);
        return out;
    }

    public static void add(Review r) { r.setId(FileHandler.nextId("R", FILE)); FileHandler.append(FILE, r.toRow()); }
    public static void update(String id, String[] row) { FileHandler.replaceRow(FILE, id, row); }
    public static void delete(String id) { FileHandler.deleteRow(FILE, id); }
}
