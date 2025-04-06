package com.securelabs.viruschecker;

import android.content.pm.*;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.*;

public class InstalledAppsActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    Button btnSystem, btnUser;
    List<AppInfo> allSystemApps = new ArrayList<>();
    List<AppInfo> allUserApps = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_installed_apps);

        recyclerView = findViewById(R.id.recyclerView);
        btnSystem = findViewById(R.id.btnSystem);
        btnUser = findViewById(R.id.btnUser);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Ambil data foreground/background dari ScanActivity
        String foregroundApp = getIntent().getStringExtra("foregroundApp");
        List<String> suspiciousBackground = getIntent().getStringArrayListExtra("suspiciousBackgroundApps");

        PackageManager pm = getPackageManager();
        List<ApplicationInfo> apps = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo app : apps) {
            try {
                PackageInfo pkgInfo = pm.getPackageInfo(app.packageName, PackageManager.GET_PERMISSIONS);
                String[] permissions = pkgInfo.requestedPermissions;
                List<String> permList = permissions != null ? Arrays.asList(permissions) : new ArrayList<>();

                boolean isRisk = false, isBackdoor = false, isAds = false, usesTelegram = false;
                boolean isSystemApp = (app.flags & ApplicationInfo.FLAG_SYSTEM) != 0;

                if (permissions != null) {
                    for (String perm : permissions) {
                        if (BackdoorDetector.isSuspiciousPermission(perm)) isRisk = true;
                    }
                    if (BackdoorDetector.isAdvancedBackdoor(permissions)) isBackdoor = true;
                    if (BackdoorDetector.mayUseTelegram(permissions, app.packageName)) usesTelegram = true;
                }

                AppInfo appInfo = new AppInfo(app.loadLabel(pm).toString(), app.packageName, permList);
                appInfo.isRisk = isRisk;
                appInfo.isBackdoor = isBackdoor;
                appInfo.usesTelegram = usesTelegram;
                appInfo.isAds = BackdoorDetector.isAdware(app.packageName, permList);

                if (foregroundApp != null && app.packageName.equals(foregroundApp)) {
                    appInfo.name += " 🟢 Foreground";
                } else if (suspiciousBackground != null && suspiciousBackground.contains(app.packageName)) {
                    appInfo.name += " 🔴 Suspicious Background";
                }

                if (!isSystemApp) {
                    PackageInfo info = pm.getPackageInfo(app.packageName, 0);
                    Date installDate = new Date(info.firstInstallTime);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
                    appInfo.installDate = sdf.format(installDate);
                    appInfo.name = "👤 " + appInfo.name;
                    allUserApps.add(appInfo);
                } else {
                    appInfo.name = "⚙️ " + appInfo.name;
                    allSystemApps.add(appInfo);
                }

            } catch (Exception ignored) {}
        }

        // Toggle tombol aktif secara visual
        setActiveButton(true);

        btnUser.setOnClickListener(v -> {
            recyclerView.setAdapter(new AppAdapter(this, allUserApps));
            setActiveButton(true);
        });

        btnSystem.setOnClickListener(v -> {
            recyclerView.setAdapter(new AppAdapter(this, allSystemApps));
            setActiveButton(false);
        });

        recyclerView.setAdapter(new AppAdapter(this, allUserApps)); // default tampil user apps
    }

    private void setActiveButton(boolean isUserActive) {
        btnUser.setBackgroundColor(getResources().getColor(isUserActive ? R.color.blue : R.color.gray));
        btnSystem.setBackgroundColor(getResources().getColor(isUserActive ? R.color.gray : R.color.blue));
    }
}
