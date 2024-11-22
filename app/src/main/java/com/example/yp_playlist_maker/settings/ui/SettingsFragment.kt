package com.example.yp_playlist_maker.settings.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.yp_playlist_maker.app.App
import com.example.yp_playlist_maker.databinding.FragmentSettingsBinding
import com.example.yp_playlist_maker.search.ui.SearchFragment
import com.example.yp_playlist_maker.settings.ui.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment: Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private val viewModel by viewModel<SettingsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Достаем значение true или false из памяти и меняем состояние Switch
        binding.themeSwitcher.isChecked = viewModel.getAppTheme()

        // Переключаем тему с помощью Switch и сохраняем значение в памяти
        binding.themeSwitcher.setOnCheckedChangeListener { _, checked ->
            (requireContext().applicationContext as App).switchTheme(checked)
            viewModel.saveAppTheme(checked)
        }

        // Поделиться приложением
        binding.mtvShare.setOnClickListener {
            viewModel.share(requireContext())
        }

        // Написать в поддержку
        binding.mtvContactSupport.setOnClickListener {
            viewModel.contact(requireContext())
        }

        // Лицензионное соглашение
        binding.mtvLicenseAgreement.setOnClickListener {
            viewModel.getLicenseAgreement(requireContext())
        }
    }

    companion object {
        fun newInstance() = SettingsFragment()
        const val TAG = "SettingsFragment"
    }

}