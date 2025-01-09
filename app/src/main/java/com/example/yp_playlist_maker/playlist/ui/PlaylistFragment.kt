package com.example.yp_playlist_maker.playlist.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.yp_playlist_maker.R
import com.example.yp_playlist_maker.databinding.FragmentPlaylistBinding

class PlaylistFragment: Fragment() {

    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!
    private var _playlistNameTextWatcher: TextWatcher? = null
    private val playlistNameTextWatcher get() = _playlistNameTextWatcher
    private var _playlistDescriptionTextWatcher: TextWatcher? = null
    private val playlistDescriptionTextWatcher get() = _playlistDescriptionTextWatcher

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        setEditTextWatchers()

    }

    private fun setTextWatcher(editText: EditText) : TextWatcher {
        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (editText.text.isEmpty()) {
                    editText.setBackgroundResource(R.drawable.playlist_empty_text_drawable)
                } else {
                    editText.setBackgroundResource(R.drawable.playlist_text_drawable)
                }
            }

            override fun afterTextChanged(s: Editable?) {
                //
            }
        }

        return simpleTextWatcher
    }

    private fun setEditTextWatchers() {
        val playlistName = binding.etPlaylistName
        val playlistDescription = binding.etPlaylistDescription
        _playlistNameTextWatcher = setTextWatcher(playlistName)
        _playlistDescriptionTextWatcher = setTextWatcher(playlistDescription)
        playlistName.addTextChangedListener(playlistNameTextWatcher)
        playlistDescription.addTextChangedListener(playlistDescriptionTextWatcher)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _playlistNameTextWatcher = null
        _playlistDescriptionTextWatcher = null
    }

}