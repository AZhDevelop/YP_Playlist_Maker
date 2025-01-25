package com.example.yp_playlist_maker.search.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.yp_playlist_maker.R
import com.example.yp_playlist_maker.app.gone
import com.example.yp_playlist_maker.app.hideKeyboard
import com.example.yp_playlist_maker.app.invisible
import com.example.yp_playlist_maker.app.visible
import com.example.yp_playlist_maker.databinding.FragmentSearchBinding
import com.example.yp_playlist_maker.player.ui.AudioPlayerFragment
import com.example.yp_playlist_maker.search.ui.view_model.SearchViewModel
import com.example.yp_playlist_maker.util.State
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment: Fragment() {

    private var _textWatcher: TextWatcher? = null
    private val textWatcher get() = _textWatcher!!
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<SearchViewModel>()
    private var _adapter: TrackAdapter? = null
    private val adapter get() = _adapter!!
    private var updateTrackHistory: Boolean = false
    private var onRestoreError: String = EMPTY_STRING
    private var isClickAllowed = true
    private var searchDebounceJob: Job? = null
    private var clickDebounceJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setSearchFragmentViews()
        setRecyclerView()
        setSearchFragmentObservers()

        _textWatcher = setTextWatcher()

        binding.etSearch.addTextChangedListener(textWatcher)
        binding.etSearch.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && binding.etSearch.text.isNotEmpty() && onRestoreError == EMPTY_STRING) {
                binding.rvTrack.visible()
            } else if (hasFocus && binding.etSearch.text.isNotEmpty() && onRestoreError != EMPTY_STRING) {
                searchDebounceJob?.cancel()
            } else if (hasFocus && binding.etSearch.text.isEmpty() && viewModel.getTrackList().isNotEmpty()) {
                enableSearchHistoryVisibility(true)
            } else {
                enableSearchHistoryVisibility(false)
            }
        }

        // Логика запуска поиска песен с клавиатуры
        binding.etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                checkPlaceholder()
                search()
            }
            false
        }

        // Кнопка перезапуска поиска песен, если отсутствовал интернет
        binding.btnReload.setOnClickListener {
            checkPlaceholder()
            search()
        }

        // Очищаем строку и список песен при нажатии на кнопку
        binding.iwClear.setOnClickListener {
            viewModel.clearTrackList()
            binding.etSearch.setText(EMPTY_STRING)
            binding.fragmentSearch.hideKeyboard()
        }

        binding.btnClearHistory.setOnClickListener {
            viewModel.clearHistory()
            enableSearchHistoryVisibility(false)
        }

        adapter.onTrackClick = {
            if (clickDebounce()) {
                viewModel.saveClickedTrack(it)
                val action = SearchFragmentDirections.actionSearchFragmentToAudioPlayerFragment(it)
                findNavController().navigate(action)
                if (binding.etSearch.text.isEmpty()) {
                    updateTrackHistory = true
                }
            }
        }
    }

    private fun setSearchFragmentViews() {
        binding.apply {
            iwClear.invisible()
            placeholder.gone()
            btnReload.gone()
            btnClearHistory.gone()
            tvSearchHistory.gone()
            rvTrack.gone()
        }
    }

    private fun setRecyclerView() {
        _adapter = TrackAdapter()
        binding.rvTrack.layoutManager = LinearLayoutManager(requireContext())
        binding.rvTrack.adapter = _adapter
    }

    private fun setTextWatcher() : TextWatcher {
        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.iwClear.visibility = clearButtonVisibility(s)
                enableSearchHistoryVisibility(false)
                if (binding.etSearch.text.isEmpty() && viewModel.getTrackHistoryList().isNotEmpty()) {
                    viewModel.updateTrackList()
                    enableSearchHistoryVisibility(true)
                }
                if (binding.etSearch.text.isEmpty()) {
                    checkPlaceholder()
                }
                if (binding.etSearch.text.isNotEmpty()) {
                    checkPlaceholder()
                    searchDebounce()
                }
            }

            override fun afterTextChanged(s: Editable?) {
                if (binding.etSearch.text.isEmpty()) {
                    viewModel.clearTrackList()
                    if (viewModel.getTrackHistoryList().isNotEmpty()) {
                        viewModel.getTrackHistory()
                    }
                }
            }
        }

        return simpleTextWatcher
    }

    // Инициализация наблюдателей viewModel
    private fun setSearchFragmentObservers() {
        viewModel.getSearchStatus().observe(viewLifecycleOwner) { searchStatus ->
            handleSearchStatus(searchStatus)
        }

        viewModel.getTrackListLiveData().observe(viewLifecycleOwner) { trackListLiveData ->
            adapter.data = trackListLiveData
            adapter.notifyDataSetChanged()
        }
    }

    // Наблюдатель состояние запроса поиска
    private fun handleSearchStatus(searchState: State.SearchState) {
        when (searchState) {
            State.SearchState.LOADING -> {
                binding.progressBar.visible()
            }
            State.SearchState.SUCCESS -> {
                binding.progressBar.gone()
                binding.rvTrack.visible()
            }
            State.SearchState.SEARCH_ERROR -> {
                showError(getString(R.string.nothing_found))
                onRestoreError = getString(R.string.nothing_found)
            }
            State.SearchState.CONNECTION_ERROR -> {
                showError(getString(R.string.connection_error))
                onRestoreError = getString(R.string.connection_error)
            }
            State.SearchState.RESET -> {
                checkPlaceholder()
            }
        }
    }

    // Функция контроля состояния видимости кнопки "Очистить строку" в поисковой строке
    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.INVISIBLE
        } else {
            View.VISIBLE
        }
    }

    // Поиск песен
    private fun search() {
        viewModel.search(binding.etSearch.text.toString())
    }

    private fun showError(error: String) {
        binding.progressBar.gone()
        binding.placeholder.visible()
        when (error) {
            getString(R.string.nothing_found) -> {
                binding.placeholderMessage.text = getString(R.string.nothing_found)
                binding.imgSearchError.setImageResource(R.drawable.img_search_error)
                binding.btnReload.gone()
            }
            getString(R.string.connection_error) -> {
                binding.placeholderMessage.text = getString(R.string.connection_error)
                binding.imgSearchError.setImageResource(R.drawable.img_connection_error)
                binding.btnReload.visible()
            }
        }
    }

    private fun enableSearchHistoryVisibility(enabled: Boolean) {
        binding.apply {
            tvSearchHistory.isVisible = enabled
            btnClearHistory.isVisible = enabled
            rvTrack.isVisible = enabled
        }
    }

    private fun searchDebounce() {
        searchDebounceJob?.cancel()
        searchDebounceJob = lifecycleScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            checkPlaceholder()
            if (binding.etSearch.text.isNotEmpty()) { search() }
        }
    }

    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            clickDebounceJob?.cancel()
            clickDebounceJob = lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }

    private fun checkPlaceholder() {
        if (binding.placeholder.isVisible) {
            binding.placeholder.gone()
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (binding.etSearch.text.isNotEmpty()) {
            searchDebounceJob?.cancel()
        }
        if (binding.etSearch.text.isEmpty()) {
            viewModel.resetSearchState()
        }
    }

    override fun onResume() {
        super.onResume()
        if (updateTrackHistory) {
            viewModel.updateTrackList()
            updateTrackHistory = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        adapter.onTrackClick = null
        _adapter = null
        _textWatcher = null
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val EMPTY_STRING: String = ""
    }

}