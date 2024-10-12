package com.example.yp_playlist_maker.settings.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.yp_playlist_maker.creator.Creator
import com.example.yp_playlist_maker.databinding.ActivitySettingsBinding
import com.example.yp_playlist_maker.settings.domain.models.AppThemeParams

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var viewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val appTheme = Creator.provideAppThemeInteractor()

        viewModel = ViewModelProvider(this, SettingsViewModelFactory())[SettingsViewModel::class.java]

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
            viewModel.share()
        }

        // Написать в поддержку
        binding.flContactSupport.setOnClickListener {
            viewModel.contact()
        }

        // Лицензионное соглашение
        binding.flLicenseAgreement.setOnClickListener {
            viewModel.getLicenseAgreement()
        }

    }
}