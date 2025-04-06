// ScanActivity.java
package com.securelabs.viruschecker;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ScanActivity extends AppCompatActivity {

    ProgressBar progressBar;
    TextView scanText;

    String[] loadingMessages = {
        "Scanning apps",
        "Checking permissions",
        "Analyzing behaviors",
        "Detecting backdoors",
        "Compiling report"
    };

    int currentStep = 0;
    boolean alreadyScanned = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        progressBar = findViewById(R.id.progressBar);
        scanText = findViewById(R.id.scanText);

        if (!alreadyScanned) {
            animateScan();
        } else {
            goToResult();
        }
    }

    private void animateScan() {
        Handler handler = new Handler();

        Runnable scanStep = new Runnable() {
            @Override
            public void run() {
                if (currentStep < loadingMessages.length) {
                    scanText.setText(loadingMessages[currentStep]);
                    currentStep++;
                    handler.postDelayed(this, 1200);
                } else {
                    scanText.setText("Scan Completed");
                    progressBar.setVisibility(View.GONE);
                    alreadyScanned = true;

                    handler.postDelayed(() -> goToResult(), 1000);
                }
            }
        };

        handler.post(scanStep);
    }

    private void goToResult() {
        Intent intent = new Intent(ScanActivity.this, InstalledAppsActivity.class);
        startActivity(intent);
        finish();
    }
}
