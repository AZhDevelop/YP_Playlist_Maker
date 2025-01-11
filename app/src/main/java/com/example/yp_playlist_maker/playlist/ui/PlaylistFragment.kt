package com.example.yp_playlist_maker.playlist.ui

import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.yp_playlist_maker.R
import com.example.yp_playlist_maker.app.gone
import com.example.yp_playlist_maker.app.visible
import com.example.yp_playlist_maker.databinding.FragmentPlaylistBinding
import com.example.yp_playlist_maker.playlist.view_model.PlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment : Fragment() {

    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!
    private var _playlistNameTextWatcher: TextWatcher? = null
    private val playlistNameTextWatcher get() = _playlistNameTextWatcher
    private var _playlistDescriptionTextWatcher: TextWatcher? = null
    private val playlistDescriptionTextWatcher get() = _playlistDescriptionTextWatcher
    private val viewModel by viewModel<PlaylistViewModel>()

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

        setFragmentElements()
        setEditTextWatchers()

        val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                loadTrackImage(uri)
                // Надо пересмотреть возможность сохранения только при нажатии на кнопку,
                // а не каждый раз при выборе изображения
                // Сделать проверку на null
                viewModel.saveImageToPrivateStorage(uri)
            } else {
                Toast.makeText(activity, "Изображение не выбрано", Toast.LENGTH_LONG).show()
            }
        }

        binding.imgPlaceholder.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.btnCreatePlaylist.setOnClickListener {
            viewModel.createPlaylist(
                playlistName = binding.etPlaylistName.text.toString(),
                playlistDescription = binding.etPlaylistDescription.text.toString()
            )
            findNavController().navigateUp()
            Toast.makeText(activity, "Плейлист успешно создан", Toast.LENGTH_LONG).show()
        }

    }

    private fun setTextWatcher(editText: EditText, textView: TextView): TextWatcher {
        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (editText.text.isEmpty()) {
                    editText.setBackgroundResource(R.drawable.playlist_empty_text_drawable)
                    textView.gone()
                    checkButtonIsAvailable()
                } else {
                    editText.setBackgroundResource(R.drawable.playlist_text_drawable)
                    textView.visible()
                    checkButtonIsAvailable()
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
        val etPlaylistDescription = binding.etPlaylistDescription
        val tvPlaylistName = binding.tvPlaylistName
        val tvPlaylistDescription = binding.tvPlaylistDescription
        _playlistNameTextWatcher = setTextWatcher(etPlaylistName, tvPlaylistName)
        _playlistDescriptionTextWatcher =
            setTextWatcher(etPlaylistDescription, tvPlaylistDescription)
        etPlaylistName.addTextChangedListener(playlistNameTextWatcher)
        etPlaylistDescription.addTextChangedListener(playlistDescriptionTextWatcher)
    }

    private fun setFragmentElements() {
        binding.btnCreatePlaylist.isEnabled = false
        binding.tvPlaylistName.gone()
        binding.tvPlaylistDescription.gone()
    }

    private fun checkButtonIsAvailable() {
        if (binding.etPlaylistName.text.isNotEmpty() && binding.etPlaylistDescription.text.isNotEmpty()) {
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
            .centerCrop()
            .transform(RoundedCorners(viewModel.getRoundedCorners(PLAYER_IMAGE_RADIUS)))
            .placeholder(R.drawable.img_placeholder_audio_player)
            .into(binding.imgPlaceholder)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _playlistNameTextWatcher = null
        _playlistDescriptionTextWatcher = null
    }

    companion object {
        private const val PLAYER_IMAGE_RADIUS: Int = 8
    }

}