package com.example.yp_playlist_maker.settings.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.yp_playlist_maker.R
import com.example.yp_playlist_maker.creator.Creator
import com.example.yp_playlist_maker.databinding.ActivitySettingsBinding
import com.example.yp_playlist_maker.settings.domain.models.AppThemeParams

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val appTheme = Creator.provideAppThemeInteractor()

        // Достаем значение true или false из памяти и меняем состояние Switch
        binding.themeSwitcher.isChecked = appTheme.getAppTheme()

        // Переключаем тему с помощью Switch и сохраняем значение в памяти
        binding.themeSwitcher.setOnCheckedChangeListener { _, checked ->
            (applicationContext as App).switchTheme(checked)
            appTheme.saveAppTheme(AppThemeParams(checked))
        }

        // Кнопка назад
        binding.iwBack.setOnClickListener {
            finish()
        }

        // Поделиться приложением
        binding.flShare.setOnClickListener {
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, getString(R.string.share_link))
            }
            startActivity(Intent.createChooser(shareIntent, getString(R.string.share_with)))
        }

        // Написать в поддержку
        binding.flContactSupport.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse(getString(R.string.mailto))
                putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.my_email)))
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.contact_support_subject))
                putExtra(Intent.EXTRA_TEXT, getString(R.string.contact_support_message))
            }
            startActivity(shareIntent)
        }

        // Лицензионное соглашение
        binding.flLicenseAgreement.setOnClickListener {
            val licenseAgreementIntent = Intent().apply {
                action = Intent.ACTION_VIEW
                data = Uri.parse(getString(R.string.license_agreement_link))
            }
            startActivity(licenseAgreementIntent)
        }

    }
}