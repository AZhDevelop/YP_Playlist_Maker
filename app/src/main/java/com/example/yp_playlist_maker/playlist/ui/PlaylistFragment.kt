package com.example.yp_playlist_maker.playlist.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
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
import com.example.yp_playlist_maker.database.domain.models.Playlist
import com.example.yp_playlist_maker.databinding.FragmentPlaylistBinding
import com.example.yp_playlist_maker.playlist.view_model.PlaylistViewModel
import com.example.yp_playlist_maker.util.Converter
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream

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
                viewModel.saveImageToPrivateStorage(uri)
            } else {
                Toast.makeText(activity, "Изображение не выбрано", Toast.LENGTH_LONG).show()
            }
        }

        binding.imgPlaceholder.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.btnCreatePlaylist.setOnClickListener {
            Log.d("log", "Button clicked")
            viewModel.createPlaylist()
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
            .transform(RoundedCorners(Converter.dpToPx(PLAYER_IMAGE_RADIUS)))
            .placeholder(R.drawable.img_placeholder_audio_player)
            .into(binding.imgPlaceholder)
    }

//    private fun saveImageToPrivateStorage(uri: Uri) {
//
//        val filePath = File(requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "playlists_covers")
//        if (!filePath.exists()){
//            filePath.mkdirs()
//        }
//        val fileCount = filePath.listFiles()?.size
//        val fileName = fileCount?.plus(1)
//        val file = File(filePath, "${fileName}.jpg")
//        val inputStream = requireActivity().contentResolver.openInputStream(uri)
//        val outputStream = FileOutputStream(file)
//        BitmapFactory
//            .decodeStream(inputStream)
//            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
//    }

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