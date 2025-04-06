package com.securelabs.viruschecker;

import android.content.*;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.*;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class AppAdapter extends RecyclerView.Adapter<AppAdapter.ViewHolder> {
    Context context;
    List<AppInfo> appList;

    public AppAdapter(Context context, List<AppInfo> appList) {
        this.context = context;
        this.appList = appList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_app, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AppInfo app = appList.get(position);
        holder.title.setText(app.name);

        String info = "Permissions: " + app.permissions.size();
        if (app.isRisk) info += " ⚠️ Risk";
        if (app.isBackdoor) info += " | 👿 May Backdoor";
        if (app.isAds) info += " | 📢 Ads";
        holder.subtitle.setText(info);

        holder.installDate.setText(app.installDate != null ? app.installDate : "");

        try {
            Drawable appIcon = context.getPackageManager().getApplicationIcon(app.packageName);
            holder.icon.setImageDrawable(appIcon);
        } catch (PackageManager.NameNotFoundException e) {
            holder.icon.setImageResource(android.R.drawable.sym_def_app_icon);
        }

        holder.itemView.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(app.name);
            builder.setMessage("Package: " + app.packageName + "\n\nPermissions:\n" +
                    (app.permissions.isEmpty() ? "No permissions" : TextUtils.join("\n", app.permissions)));

            builder.setPositiveButton("Open Settings", (dialog, which) -> {
                try {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.setData(Uri.parse("package:" + app.packageName));
                    context.startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(context, "Tidak dapat membuka settings", Toast.LENGTH_SHORT).show();
                }
            });

            builder.setNegativeButton("Close", null);
            builder.show();
        });
    }

    @Override
    public int getItemCount() {
        return appList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView title, subtitle, installDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.appIcon);
            title = itemView.findViewById(R.id.appName);
            subtitle = itemView.findViewById(R.id.appDetails);
            installDate = itemView.findViewById(R.id.installDate);
        }
    }
}
