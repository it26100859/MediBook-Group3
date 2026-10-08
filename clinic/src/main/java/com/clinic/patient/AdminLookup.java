package com.clinic.patient;

import com.clinic.common.FileHandler;

/** Checks admins.txt directly so usernames stay unique across both account types. */
class AdminLookup {
    static boolean exists(String username) {
        for (String[] r : FileHandler.readAll("admins.txt"))
            if (r.length > 1 && r[1].equalsIgnoreCase(username)) return true;
        return false;
    }
}
