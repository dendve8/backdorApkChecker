package com.securelabs.viruschecker;

import java.util.List;

public class AppInfo {
    public String name;
    public String packageName;
    public List<String> permissions;

    public boolean isRisk = false;
    public boolean isBackdoor = false;
    public boolean isAds = false;
    public boolean usesTelegram = false;

    public String installDate = null; // tanggal install untuk user apps

    public AppInfo(String name, String packageName, List<String> permissions) {
        this.name = name;
        this.packageName = packageName;
        this.permissions = permissions;
    }
}
