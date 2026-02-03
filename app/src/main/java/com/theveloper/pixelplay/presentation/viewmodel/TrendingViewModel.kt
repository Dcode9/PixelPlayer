package com.theveloper.pixelplay.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.theveloper.pixelplay.data.model.Song
import com.theveloper.pixelplay.data.repository.MusicRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for Trending screen
 * Manages trending songs from JioSaavn cloud service
 */
@HiltViewModel
class TrendingViewModel @Inject constructor(
    private val musicRepository: MusicRepository
) : ViewModel() {
    
    private val _trendingSongs = MutableStateFlow<List<Song>>(emptyList())
    val trendingSongs: StateFlow<List<Song>> = _trendingSongs.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()
    
    init {
        loadTrendingSongs()
    }
    
    fun loadTrendingSongs() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            
            musicRepository.getTrendingSongs()
                .catch { error ->
                    _errorMessage.value = "Failed to load trending songs: ${error.message}"
                    _isLoading.value = false
                }
                .collect { songs ->
                    _trendingSongs.value = songs
                    _isLoading.value = false
                }
        }
    }
    
    fun refresh() {
        loadTrendingSongs()
    }
}
