package com.example.yp_playlist_maker.playlist.ui

import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.yp_playlist_maker.R
import com.example.yp_playlist_maker.app.gone
import com.example.yp_playlist_maker.app.visible
import com.example.yp_playlist_maker.databinding.FragmentPlaylistBinding
import com.example.yp_playlist_maker.playlist.ui.view_model.PlaylistViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment : Fragment() {

    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!
    private var _playlistNameTextWatcher: TextWatcher? = null
    private val playlistNameTextWatcher get() = _playlistNameTextWatcher
    private val viewModel by viewModel<PlaylistViewModel>()
    private var isCoverSet: Boolean = false
    private var isTextSet: Boolean = false
    private var _imageUri: Uri? = null
    private val imageUri get() = _imageUri!!

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

        requireActivity()
            .onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                checkPlaylistCreation()
            }
        })

        setFragmentElements()
        setEditTextWatchers()
        setBinding()

    }

    private fun setBinding() {
        val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                loadTrackImage(uri)
                _imageUri = uri
                isCoverSet = true
            } else {
                isCoverSet = false
            }
        }

        binding.apply {
            toolbar.setNavigationOnClickListener {
                checkPlaylistCreation()
            }
            imgPlaceholder.setOnClickListener {
                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }
            btnCreatePlaylist.setOnClickListener {
                savePlaylist(isCoverSet)
            }
            etPlaylistName.setOnFocusChangeListener { _, hasFocus ->
                checkOnFocus(
                    editText = etPlaylistName,
                    textView = tvPlaylistName,
                    hasFocus = hasFocus
                )
            }
            etPlaylistDescription.setOnFocusChangeListener { _, hasFocus ->
                checkOnFocus(
                    editText = etPlaylistDescription,
                    textView = tvPlaylistDescription,
                    hasFocus = hasFocus
                )
            }
        }
    }

    private fun setTextWatcher(editText: EditText): TextWatcher {
        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (editText.text.isEmpty()) {
                    checkButtonIsAvailable()
                    isTextSet = false
                } else {
                    checkButtonIsAvailable()
                    isTextSet = true
                }
            }

            override fun afterTextChanged(s: Editable?) {
                //
            }
        }

        return simpleTextWatcher
    }

    private fun setEditTextWatchers() {
        val etPlaylistName = binding.etPlaylistName
        _playlistNameTextWatcher = setTextWatcher(etPlaylistName)
        etPlaylistName.addTextChangedListener(playlistNameTextWatcher)
    }

    private fun setFragmentElements() {
        binding.apply {
            btnCreatePlaylist.isEnabled = false
            tvPlaylistName.gone()
            tvPlaylistDescription.gone()
        }
    }

    private fun checkButtonIsAvailable() {
        if (binding.etPlaylistName.text.isNotEmpty()) {
            binding.btnCreatePlaylist.apply {
                isEnabled = true
                setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.yp_blue))
            }
        } else {
            binding.btnCreatePlaylist.apply {
                isEnabled = false
                setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.yp_gray))
            }
        }
    }

    private fun loadTrackImage(uri: Uri) {
        Glide.with(this)
            .load(uri)
            .transform(
                CenterCrop(),
                RoundedCorners(viewModel.getRoundedCorners(PLAYER_IMAGE_RADIUS))
            )
            .placeholder(R.drawable.img_placeholder_audio_player)
            .into(binding.imgPlaceholder)
    }

    private fun checkPlaylistCreation() {
        if (isCoverSet || isTextSet) {
            showCancelDialog()
        } else {
            findNavController().navigateUp()
        }
    }

    private fun savePlaylist(isCoverSet: Boolean) {
        val playListName = binding.etPlaylistName.text.toString()
        val playListDescription = binding.etPlaylistDescription.text.toString()
        val toastMessage = "Плейлист \"$playListName\" успешно создан"
        if (isCoverSet) {
            viewModel.saveImageToPrivateStorage(imageUri)
            viewModel.createPlaylist(
                playlistName = playListName,
                playlistDescription = playListDescription
            )
        } else {
            viewModel.createPlaylist(
                playlistName = playListName,
                playlistDescription = playListDescription
            )
        }
        findNavController().navigateUp()
        Toast.makeText(activity, toastMessage, Toast.LENGTH_LONG).show()
    }

    private fun showCancelDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.cancel_dialog_title))
            .setMessage(getString(R.string.cancel_dialog_message))
            .setNeutralButton(getString(R.string.cancel_dialog_neutral_button)) { _, _ -> }
            .setPositiveButton(getString(R.string.cancel_dialog_positive_button)) { _, _ ->
                findNavController().navigateUp()
            }
            .show()
    }

    private fun checkOnFocus(editText: EditText, textView: TextView, hasFocus: Boolean) {
        if (hasFocus) {
            editText.setBackgroundResource(R.drawable.playlist_text_drawable)
            editText.hint = EMPTY_STRING
            textView.visible()
            textView.setTextColor(requireContext().getColor(R.color.yp_blue))
        } else {
            editText.setBackgroundResource(R.drawable.playlist_empty_text_drawable)
            if (editText.text.isEmpty()) {
                textView.gone()
                editText.hint = textView.text
            } else {
                textView.setTextColor(requireContext().getColor(R.color.yp_gray))
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _playlistNameTextWatcher = null
    }

    companion object {
        private const val EMPTY_STRING = ""
        private const val PLAYER_IMAGE_RADIUS: Int = 8
    }

}