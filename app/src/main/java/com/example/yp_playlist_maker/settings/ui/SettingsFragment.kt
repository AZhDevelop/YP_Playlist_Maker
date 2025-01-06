package com.example.yp_playlist_maker.settings.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
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

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<SettingsViewModel>()
    private var shareIntentActive: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setSearchFragmentObservers()

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

    private fun setSearchFragmentObservers() {
        viewModel.getShareIntentIsActive().observe(viewLifecycleOwner) { shareIntentStatus ->
            handleShareIntentStatus(shareIntentStatus)
            shareIntentActive = shareIntentStatus
        }
    }

    private fun handleShareIntentStatus(shareIntentStatus: Boolean) {
        if (shareIntentStatus) {
            setFragmentBackgroundOnPause()
        } else {
            setBaseFragmentBackground()
        }
    }

    private fun setFragmentBackgroundOnPause() {
        if (compareBackground()) {
            binding.fragmentSettings.setBackgroundColor(TRANSPARENT_BACKGROUND)
        } else {
            binding.fragmentSettings.background.alpha = ALPHA_BACKGROUND
        }
    }

    private fun setBaseFragmentBackground() {
        binding.fragmentSettings.setBackgroundColor(requireContext().getColor(R.color.main_background))
    }

    private fun compareBackground() : Boolean {
        val background = requireView().background
        if (background is ColorDrawable) {
            val currentValue = background.color
            return currentValue == LIGHT_BACKGROUND
        }
        return false
    }

    override fun onResume() {
        super.onResume()
        if (shareIntentActive) {
            viewModel.clearShareIntentStatus()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding= null
    }

    companion object {
        private val TRANSPARENT_BACKGROUND = Color.argb(0.5F, 26F, 27F, 34F)
        private val LIGHT_BACKGROUND = R.color.white
        private const val ALPHA_BACKGROUND = 127
    }
}