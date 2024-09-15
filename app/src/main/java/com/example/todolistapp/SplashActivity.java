package com.example.todolistapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**
 * Aktivitas ini digunakan untuk menampilkan layar splash ketika aplikasi pertama kali diluncurkan.
 * Layar splash adalah tampilan awal yang muncul selama beberapa detik sebelum aplikasi beralih
 * ke aktivitas utama.
 */
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Mengatur tampilan aktivitas dengan layout yang ditentukan
        setContentView(R.layout.activity_splash);
        // Menyembunyikan action bar jika ada
        getSupportActionBar().hide();

        // Membuat Intent untuk berpindah ke MainActivity
        final Intent i = new Intent(SplashActivity.this, MainActivity.class);

        // Menggunakan Handler untuk menjalankan kode setelah penundaan tertentu
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Memulai MainActivity setelah penundaan
                startActivity(i);
                // Menutup SplashActivity sehingga tidak dapat kembali ke layar splash dengan menekan tombol kembali
                finish();
            }
        }, 1000); // Penundaan selama 1000 milidetik (1 detik)
    }
}
