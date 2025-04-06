package com.securelabs.viruschecker;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button btnScanNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnScanNow = findViewById(R.id.btnScanNow);
        TextView tvAuthor = findViewById(R.id.tvAuthor);

        btnScanNow.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ScanActivity.class);
            startActivity(intent);
        });

        // Aktifkan klik link pada TextView
        tvAuthor.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
