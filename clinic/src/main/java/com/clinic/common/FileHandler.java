package com.clinic.common;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

/** Reads/writes pipe-delimited .txt files. Column 0 of every file is the record ID. */
public class FileHandler {
    private static String dir = System.getProperty("user.home") + "/clinic-data";

    public static synchronized void init(String d) {
        dir = d;
        try { Files.createDirectories(Paths.get(dir)); } catch (IOException e) { throw new RuntimeException(e); }
    }

    private static Path path(String file) { return Paths.get(dir, file); }

    public static synchronized List<String[]> readAll(String file) {
        List<String[]> rows = new ArrayList<>();
        try {
            Path p = path(file);
            if (!Files.exists(p)) return rows;
            for (String line : Files.readAllLines(p, StandardCharsets.UTF_8))
                if (!line.trim().isEmpty()) rows.add(line.split("\\|", -1));
        } catch (IOException e) { throw new RuntimeException(e); }
        return rows;
    }

    public static synchronized void writeAll(String file, List<String[]> rows) {
        List<String> lines = new ArrayList<>();
        for (String[] r : rows) lines.add(String.join("|", r));
        try {
            Files.createDirectories(Paths.get(dir));
            Files.write(path(file), lines, StandardCharsets.UTF_8);
        } catch (IOException e) { throw new RuntimeException(e); }
    }

    public static synchronized void append(String file, String[] row) {
        List<String[]> rows = readAll(file);
        rows.add(row);
        writeAll(file, rows);
    }

    public static synchronized void replaceRow(String file, String id, String[] newRow) {
        List<String[]> rows = readAll(file);
        for (int i = 0; i < rows.size(); i++)
            if (rows.get(i)[0].equals(id)) { rows.set(i, newRow); break; }
        writeAll(file, rows);
    }

    public static synchronized void deleteRow(String file, String id) {
        List<String[]> rows = readAll(file);
        rows.removeIf(r -> r[0].equals(id));
        writeAll(file, rows);
    }

    /** Next ID such as P001, D002 ... */
    public static synchronized String nextId(String prefix, String file) {
        int max = 0;
        for (String[] r : readAll(file)) {
            try { max = Math.max(max, Integer.parseInt(r[0].substring(prefix.length()))); }
            catch (Exception ignored) { }
        }
        return String.format("%s%03d", prefix, max + 1);
    }
}
