package com.example.yp_playlist_maker.main.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.yp_playlist_maker.R
import com.example.yp_playlist_maker.databinding.FragmentMainBinding
import com.example.yp_playlist_maker.media.ui.MediaFragment
import com.example.yp_playlist_maker.search.ui.SearchFragment
import com.example.yp_playlist_maker.settings.ui.SettingsFragment

class MainFragment: Fragment() {

    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSearch.setOnClickListener {
            parentFragmentManager.commit {
                replace(
                    R.id.main,
                    SearchFragment.newInstance(),
                    SearchFragment.TAG
                )
                addToBackStack(SearchFragment.TAG)
            }
        }

        binding.btnMedia.setOnClickListener {
            parentFragmentManager.commit {
                replace(
                    R.id.main,
                    MediaFragment.newInstance(),
                    MediaFragment.TAG
                )
                addToBackStack(MediaFragment.TAG)
            }
        }

        binding.btnSettings.setOnClickListener {
            parentFragmentManager.commit {
                replace(
                    R.id.main,
                    SettingsFragment.newInstance(),
                    SettingsFragment.TAG
                )
                addToBackStack(SettingsFragment.TAG)
            }
        }
    }

}