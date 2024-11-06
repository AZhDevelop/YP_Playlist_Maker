package com.example.yp_playlist_maker.settings.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.yp_playlist_maker.app.App
import com.example.yp_playlist_maker.databinding.ActivitySettingsBinding
import com.example.yp_playlist_maker.settings.ui.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private val viewModel by viewModel<SettingsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Достаем значение true или false из памяти и меняем состояние Switch
        binding.themeSwitcher.isChecked = viewModel.getAppTheme()

        // Переключаем тему с помощью Switch и сохраняем значение в памяти
        binding.themeSwitcher.setOnCheckedChangeListener { _, checked ->
            (applicationContext as App).switchTheme(checked)
            viewModel.saveAppTheme(checked)
        }

        // Кнопка назад
        binding.iwBack.setOnClickListener {
            finish()
        }

        // Поделиться приложением
        binding.flShare.setOnClickListener {
            viewModel.share(this)
        }

        // Написать в поддержку
        binding.flContactSupport.setOnClickListener {
            viewModel.contact(this)
        }

        // Лицензионное соглашение
        binding.flLicenseAgreement.setOnClickListener {
            viewModel.getLicenseAgreement(this)
        }

    }
}