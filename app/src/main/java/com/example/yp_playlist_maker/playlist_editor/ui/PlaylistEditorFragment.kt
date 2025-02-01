package com.example.yp_playlist_maker.playlist_editor.ui

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.yp_playlist_maker.R
import com.example.yp_playlist_maker.app.gone
import com.example.yp_playlist_maker.app.visible
import com.example.yp_playlist_maker.database.domain.models.Playlist
import com.example.yp_playlist_maker.databinding.FragmentPlaylistEditorBinding
import com.example.yp_playlist_maker.playlist_editor.ui.view_model.PlaylistEditorViewModel
import com.example.yp_playlist_maker.util.State
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistEditorFragment : Fragment() {

    private var _binding: FragmentPlaylistEditorBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<PlaylistEditorViewModel>()
    private val playlistArgs by navArgs<PlaylistEditorFragmentArgs>()
    private var isCoverSet: Boolean = false
    private var isCoverUpdated: Boolean = false
    private var isTextSet: Boolean = false
    private var _imageUri: Uri? = null
    private val imageUri get() = _imageUri!!
    private var playlist: Playlist? = null
    private var showCancelDialog: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaylistEditorBinding.inflate(inflater, container, false)
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

        playlist = playlistArgs.playlist
        viewModel.checkPlaylist(playlist)

        setPlaylistEditorFragmentObservers()
        setBinding()

        if (playlist != null) {
            binding.etPlaylistName.setText(playlist!!.playlistName)
            binding.etPlaylistDescription.setText(playlist!!.playlistDescription)
            if (playlist!!.playlistCoverPath != "null") {
                Glide.with(this)
                    .load(playlist!!.playlistCoverPath)
                    .transform(
                        CenterCrop(),
                        RoundedCorners(viewModel.getRoundedCorners(PLAYER_IMAGE_RADIUS))
                    )
                    .placeholder(R.drawable.img_placeholder_audio_player)
                    .into(binding.imgPlaceholder)
                isCoverSet = true
                isCoverUpdated = false
            }
        }

    }

    private fun setBinding() {
        val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                loadTrackImage(uri)
                _imageUri = uri
                isCoverSet = true
                isCoverUpdated = true
            } else {
                isCoverUpdated = false
            }
        }
        binding.apply {
            toolbar.setNavigationOnClickListener {
                checkPlaylistCreation()
            }
            imgPlaceholder.setOnClickListener {
                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
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
            etPlaylistName.addTextChangedListener(
                onTextChanged = { _, _, _, _ ->
                    if (etPlaylistName.text.isEmpty()) {
                        checkButtonIsAvailable()
                        isTextSet = false
                    } else {
                        checkButtonIsAvailable()
                        isTextSet = true
                    }
                }
            )
        }
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

    private fun setPlaylistEditorFragmentObservers() {
        viewModel.getPlaylistEditorState().observe(viewLifecycleOwner) { state ->
            handlePlaylistEditorState(state)
        }
    }

    private fun handlePlaylistEditorState(state: State.PlaylistEditorState) {
        when (state) {
            State.PlaylistEditorState.EDITOR -> {
                binding.btnCreatePlaylist.text = "Сохранить"
                binding.toolbar.title = "Редактировать"
                checkTextIsNotEmpty()
                binding.btnCreatePlaylist.setOnClickListener {
                    updatePlaylist()
                }
                showCancelDialog = false
            }
            State.PlaylistEditorState.CREATOR -> {
                setFragmentElements()
                binding.btnCreatePlaylist.setOnClickListener {
                    savePlaylist()
                }
                showCancelDialog = true
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
        if (showCancelDialog) {
            if (isCoverSet || isTextSet) {
                showCancelDialog()
            } else {
                findNavController().navigateUp()
            }
        } else {
            findNavController().navigateUp()
        }
    }

    private fun savePlaylist() {
        val playListName = binding.etPlaylistName.text.toString()
        val playListDescription = binding.etPlaylistDescription.text.toString()
        val toastMessage = "Плейлист \"$playListName\" создан"
        if (isCoverSet) { viewModel.saveImageToPrivateStorage(imageUri) }
        viewModel.createPlaylist(
            playlistName = playListName,
            playlistDescription = playListDescription
        )
        findNavController().navigateUp()
        Toast.makeText(activity, toastMessage, Toast.LENGTH_LONG).show()
    }

    private fun updatePlaylist() {
        val playListName = binding.etPlaylistName.text.toString()
        val playListDescription = binding.etPlaylistDescription.text.toString()
        if (isCoverSet && isCoverUpdated) viewModel.saveImageToPrivateStorage(imageUri)
        viewModel.updatePlaylist(
            playlist = playlist!!,
            playlistName = playListName,
            playlistDescription = playListDescription,
            isCoverUpdated = isCoverUpdated
        )
        findNavController().navigateUp()
    }

    private fun showCancelDialog() {
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.cancel_dialog_title))
            .setMessage(getString(R.string.cancel_dialog_message))
            .setNeutralButton(getString(R.string.cancel_dialog_neutral_button)) { _, _ ->
            }
            .setPositiveButton(getString(R.string.cancel_dialog_positive_button)) { _, _ ->
                findNavController().navigateUp()
            }
            .show()

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(requireContext().getColor(R.color.yp_blue))
        dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(requireContext().getColor(R.color.yp_blue))
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

    private fun checkTextIsNotEmpty() {
        if (binding.etPlaylistName.text.isNotEmpty()) {
            binding.etPlaylistName.hint = EMPTY_STRING
            binding.tvPlaylistName.visible()
            binding.tvPlaylistName.setTextColor(requireContext().getColor(R.color.yp_gray))
        }
        if (binding.etPlaylistDescription.text.isNotEmpty()) {
            binding.etPlaylistDescription.hint = EMPTY_STRING
            binding.tvPlaylistDescription.visible()
            binding.tvPlaylistDescription.setTextColor(requireContext().getColor(R.color.yp_gray))
        } else {
            binding.tvPlaylistDescription.gone()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val EMPTY_STRING = ""
        private const val PLAYER_IMAGE_RADIUS: Int = 8
    }

}