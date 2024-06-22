package com.example.yp_playlist_maker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.FrameLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val shareButton = findViewById<FrameLayout>(R.id.fl_share)
        val contactSupportButton = findViewById<FrameLayout>(R.id.fl_contact_support)
        val licenseAgreementButton = findViewById<FrameLayout>(R.id.fl_license_agreement)

        licenseAgreementButton.setOnClickListener {
            val url = "https://yandex.ru/legal/practicum_offer"
            val licenseAgreementIntent = Intent(Intent.ACTION_VIEW)
            licenseAgreementIntent.data = Uri.parse(url)
            startActivity(licenseAgreementIntent)
        }

    }
}