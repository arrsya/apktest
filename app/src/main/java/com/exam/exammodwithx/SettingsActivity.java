package com.exam.exammodwithx;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.exam.exammodwithx.databinding.ActivitySettingsBinding;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SettingsActivity extends AppCompatActivity {
    private ActivitySettingsBinding binding;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) supportActionBar.hide();

        sharedPreferences = getSharedPreferences("setting", 0);

        // INIT STATE
        binding.switchDarkMode.setChecked(sharedPreferences.getBoolean("darkmode", false));
        binding.switchSupportZoom.setChecked(sharedPreferences.getBoolean("support_zoom", true));
        String userAgent = sharedPreferences.getString("user_agent", "");
        if (userAgent != null) binding.etUserAgent.setText(userAgent);

        binding.switchDarkMode.setOnCheckedChangeListener((CompoundButton buttonView, boolean isChecked) ->
                sharedPreferences.edit().putBoolean("darkmode", isChecked).apply()
        );

        binding.switchSupportZoom.setOnCheckedChangeListener((CompoundButton buttonView, boolean isChecked) ->
                sharedPreferences.edit().putBoolean("support_zoom", isChecked).apply()
        );

        binding.btnSaveUserAgent.setOnClickListener((View v) -> {
            String agent = binding.etUserAgent.getText().toString();
            sharedPreferences.edit().putString("user_agent", agent).apply();
            Toast.makeText(this, "User Agent tersimpan!", Toast.LENGTH_SHORT).show();
        });

        binding.btnReset.setOnClickListener((View v) -> {
            sharedPreferences.edit()
                    .putBoolean("darkmode", false)
                    .putBoolean("support_zoom", true)
                    .putString("user_agent", "")
                    .apply();
            binding.switchDarkMode.setChecked(false);
            binding.switchSupportZoom.setChecked(true);
            binding.etUserAgent.setText("");
            Toast.makeText(this, "Semua pengaturan di-reset ke default.", Toast.LENGTH_SHORT).show();
        });

        // === FITUR CEK TOKEN (2 URL, auto http/https) ===
        binding.btnCekToken.setOnClickListener(v -> {
            binding.tvHasilToken.setText("Mengambil token...");
            String urlDasar = sharedPreferences.getString("url", null);
            if (urlDasar == null || urlDasar.isEmpty()) {
                binding.tvHasilToken.setText("URL utama belum diisi di halaman utama.");
                return;
            }

            // Hilangkan spasi & trailing slash
            urlDasar = urlDasar.trim();
            if (urlDasar.endsWith("/")) urlDasar = urlDasar.substring(0, urlDasar.length() - 1);

            // Kalau tidak ada http/https, default ke http://
            if (!urlDasar.startsWith("http://") && !urlDasar.startsWith("https://")) {
                urlDasar = "http://" + urlDasar;
            }

            // Cek dua kemungkinan, dengan dan tanpa /cbt
            final String url1 = urlDasar + "/wp-content/themes/unbk/api-18575621/generatetoken.php";
            final String url2 = urlDasar + "/cbt/wp-content/themes/unbk/api-18575621/generatetoken.php";

            ambilTokenDariDuaUrl(url1, url2);
        });
        // === END FITUR CEK TOKEN ===
    }

    private void ambilTokenDariDuaUrl(String url1, String url2) {
        ambilTokenAsync(url1, new TokenCallback() {
            @Override
            public void onSuccess(String hasil) {
                tampilkanHasilToken(hasil);
            }

            @Override
            public void onFailed() {
                // Jika gagal, coba url kedua
                ambilTokenAsync(url2, new TokenCallback() {
                    @Override
                    public void onSuccess(String hasil) {
                        tampilkanHasilToken(hasil);
                    }
                    @Override
                    public void onFailed() {
                        tampilkanHasilToken(null);
                    }
                });
            }
        });
    }

    private void ambilTokenAsync(String urlStr, TokenCallback callback) {
        new Thread(() -> {
            try {
                URL url = new URL(urlStr);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(7000);
                conn.setReadTimeout(7000);
                conn.setRequestMethod("GET");
                StringBuilder response = new StringBuilder();
                try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                    String line;
                    while ((line = in.readLine()) != null) {
                        response.append(line).append("\n");
                    }
                }
                String hasil = response.toString().trim();
                new Handler(Looper.getMainLooper()).post(() -> {
                    if (!hasil.isEmpty()) {
                        callback.onSuccess(hasil);
                    } else {
                        callback.onFailed();
                    }
                });
            } catch (Exception e) {
                new Handler(Looper.getMainLooper()).post(callback::onFailed);
            }
        }).start();
    }

    private void tampilkanHasilToken(String hasil) {
        if (hasil == null || hasil.isEmpty()) {
            binding.tvHasilToken.setText("Gagal mengambil token. Silakan coba lagi.");
        } else {
            binding.tvHasilToken.setText("Token: " + hasil);
        }
    }

    private interface TokenCallback {
        void onSuccess(String hasil);
        void onFailed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
