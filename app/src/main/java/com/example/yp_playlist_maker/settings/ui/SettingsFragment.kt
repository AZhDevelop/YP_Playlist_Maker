package com.example.yp_playlist_maker.settings.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.yp_playlist_maker.R
import com.example.yp_playlist_maker.app.App
import com.example.yp_playlist_maker.databinding.FragmentSettingsBinding
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

        val rootView = requireView()

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        // Достаем значение true или false из памяти и меняем состояние Switch
        binding.themeSwitcher.isChecked = viewModel.getAppTheme()

        // Переключаем тему с помощью Switch и сохраняем значение в памяти
        binding.themeSwitcher.setOnCheckedChangeListener { _, checked ->
            (requireContext().applicationContext as App).switchTheme(checked)
            viewModel.saveAppTheme(checked)
        }

        // Поделиться приложением
        binding.mtvShare.setOnClickListener {
            setFragmentBackgroundOnPause()
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

    override fun onResume() {
        super.onResume()
        setBaseFragmentBackground()
    }

    private fun setFragmentBackgroundOnPause() {
        if (compareBackground(requireView())) {
            binding.fragmentSettings.setBackgroundColor(TRANSPARENT_BACKGROUND)
        } else {
            binding.fragmentSettings.background.alpha = ALPHA_BACKGROUND
        }
    }

    private fun setBaseFragmentBackground() {
        binding.fragmentSettings.setBackgroundColor(requireContext().getColor(R.color.main_background))
    }

    private fun compareBackground(view: View) : Boolean {
        val background = view.background
        if (background is ColorDrawable) {
            val currentValue = background.color
            return currentValue == LIGHT_BACKGROUND
        }
        return false
    }

    companion object {
        private val TRANSPARENT_BACKGROUND = Color.argb(0.5F, 26F, 27F, 34F)
        private val LIGHT_BACKGROUND = R.color.white
        private const val ALPHA_BACKGROUND = 127
    }

}