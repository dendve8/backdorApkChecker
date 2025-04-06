package com.securelabs.viruschecker;

import java.util.Arrays;
import java.util.List;

public class BackdoorDetector {

    public static boolean isSuspiciousPermission(String perm) {
        return perm.contains("RECORD_AUDIO") ||
               perm.contains("SEND_SMS") ||
               perm.contains("READ_SMS") ||
               perm.contains("READ_CONTACTS") ||
               perm.contains("ACCESS_FINE_LOCATION") ||
               perm.contains("QUERY_ALL_PACKAGES") ||
               perm.contains("PACKAGE_USAGE_STATS") ||
               perm.contains("SYSTEM_ALERT_WINDOW") ||
               perm.contains("BIND_ACCESSIBILITY_SERVICE");
    }

    public static boolean isAdvancedBackdoor(String[] permissions) {
        if (permissions == null) return false;
        String allPerms = Arrays.toString(permissions);
        return allPerms.contains("RECORD_AUDIO") &&
               allPerms.contains("INTERNET") &&
               allPerms.contains("RECEIVE_BOOT_COMPLETED") ||
               (allPerms.contains("BIND_ACCESSIBILITY_SERVICE") &&
                allPerms.contains("SYSTEM_ALERT_WINDOW")) ||
               (allPerms.contains("QUERY_ALL_PACKAGES") &&
                allPerms.contains("READ_SMS"));
    }

    public static boolean mayUseTelegram(String[] permissions, String packageName) {
        return packageName.toLowerCase().contains("telegram") ||
               (permissions != null &&
                Arrays.toString(permissions).toLowerCase().contains("telegram")) ||
               (permissions != null &&
                Arrays.toString(permissions).toLowerCase().contains("internet"));
    }

    public static boolean isAdware(String packageName, List<String> permissions) {
        boolean hasInternet = false;
        boolean hasNetwork = false;

        for (String perm : permissions) {
            if (perm.contains("INTERNET")) hasInternet = true;
            if (perm.contains("ACCESS_NETWORK_STATE")) hasNetwork = true;
        }

        if (hasInternet && hasNetwork) {
            String lowerPkg = packageName.toLowerCase();
            return lowerPkg.contains("admob") ||
                   lowerPkg.contains("ads") ||
                   lowerPkg.contains("unity") ||
                   lowerPkg.contains("tapjoy") ||
                   lowerPkg.contains("startapp") ||
                   lowerPkg.contains("applovin") ||
                   lowerPkg.contains("mopub") ||
                   lowerPkg.contains("chartboost") ||
                   lowerPkg.contains("vungle") ||
                   lowerPkg.contains("facebook");
        }

        return false;
    }
}