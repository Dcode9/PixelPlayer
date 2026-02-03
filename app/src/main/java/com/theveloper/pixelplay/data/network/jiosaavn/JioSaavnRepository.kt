package com.theveloper.pixelplay.data.network.jiosaavn

import android.util.Log
import com.theveloper.pixelplay.data.model.Song
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for JioSaavn cloud music data
 * Handles fetching trending songs, searching, and getting song details
 */
@Singleton
class JioSaavnRepository @Inject constructor(
    private val apiService: JioSaavnApiService
) {
    
    companion object {
        private const val TAG = "JioSaavnRepository"
        private const val MAX_CONCURRENT_DETAIL_CALLS = 5 // Bounded concurrency
    }
    
    /**
     * Fetches trending songs from JioSaavn
     * If trending items need detail calls for stream URLs, batch them with bounded concurrency
     */
    suspend fun getTrendingSongs(): Result<List<Song>> = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "Fetching trending songs")
            val response = apiService.getTrending()
            
            if (!response.isSuccessful) {
                Log.e(TAG, "Failed to fetch trending: ${response.code()} ${response.message()}")
                return@withContext Result.failure(
                    Exception("Failed to fetch trending: ${response.message()}")
                )
            }
            
            val trendingData = response.body()?.data?.trending?.data
            if (trendingData.isNullOrEmpty()) {
                Log.w(TAG, "No trending data found")
                return@withContext Result.success(emptyList())
            }
            
            Log.d(TAG, "Found ${trendingData.size} trending songs")
            
            // Check if songs have stream URLs, if not fetch details
            val songsNeedingDetails = trendingData.filter { song ->
                song.downloadUrl.isNullOrEmpty() && song.mediaUrl.isNullOrEmpty()
            }
            
            val songs = if (songsNeedingDetails.isNotEmpty()) {
                Log.d(TAG, "${songsNeedingDetails.size} songs need detail calls")
                fetchSongsWithDetails(trendingData)
            } else {
                JioSaavnMapper.mapToSongs(trendingData)
            }
            
            Log.d(TAG, "Successfully mapped ${songs.size} trending songs")
            Result.success(songs)
            
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching trending songs", e)
            Result.failure(e)
        }
    }
    
    /**
     * Searches for songs on JioSaavn
     */
    suspend fun searchSongs(query: String, limit: Int = 20): Result<List<Song>> = 
        withContext(Dispatchers.IO) {
            try {
                if (query.isBlank()) {
                    return@withContext Result.success(emptyList())
                }
                
                Log.d(TAG, "Searching songs: $query")
                val response = apiService.searchSongs(query = query, limit = limit)
                
                if (!response.isSuccessful) {
                    Log.e(TAG, "Failed to search: ${response.code()} ${response.message()}")
                    return@withContext Result.failure(
                        Exception("Failed to search: ${response.message()}")
                    )
                }
                
                val results = response.body()?.data?.results
                if (results.isNullOrEmpty()) {
                    Log.d(TAG, "No search results for: $query")
                    return@withContext Result.success(emptyList())
                }
                
                Log.d(TAG, "Found ${results.size} search results")
                
                // Check if songs have stream URLs, if not fetch details
                val songsNeedingDetails = results.filter { song ->
                    song.downloadUrl.isNullOrEmpty() && song.mediaUrl.isNullOrEmpty()
                }
                
                val songs = if (songsNeedingDetails.isNotEmpty()) {
                    Log.d(TAG, "${songsNeedingDetails.size} songs need detail calls")
                    fetchSongsWithDetails(results)
                } else {
                    JioSaavnMapper.mapToSongs(results)
                }
                
                Log.d(TAG, "Successfully mapped ${songs.size} search results")
                Result.success(songs)
                
            } catch (e: Exception) {
                Log.e(TAG, "Error searching songs: $query", e)
                Result.failure(e)
            }
        }
    
    /**
     * Gets detailed song information by ID
     */
    suspend fun getSongDetail(songId: String): Result<Song?> = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "Fetching song detail: $songId")
            val response = apiService.getSongDetail(songId)
            
            if (!response.isSuccessful) {
                Log.e(TAG, "Failed to fetch song detail: ${response.code()} ${response.message()}")
                return@withContext Result.failure(
                    Exception("Failed to fetch song detail: ${response.message()}")
                )
            }
            
            val songData = response.body()?.data?.firstOrNull()
            if (songData == null) {
                Log.w(TAG, "No song data found for: $songId")
                return@withContext Result.success(null)
            }
            
            val song = JioSaavnMapper.mapToSong(songData)
            Log.d(TAG, "Successfully fetched song detail: ${song?.title}")
            Result.success(song)
            
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching song detail: $songId", e)
            Result.failure(e)
        }
    }
    
    /**
     * Fetches detailed information for songs with bounded concurrency
     * This is used when trending/search results don't include stream URLs
     */
    private suspend fun fetchSongsWithDetails(
        songs: List<JioSaavnSong>
    ): List<Song> = coroutineScope {
        songs.chunked(MAX_CONCURRENT_DETAIL_CALLS).flatMap { chunk ->
            chunk.map { song ->
                async {
                    try {
                        // If song already has stream URL, map directly
                        if (!song.downloadUrl.isNullOrEmpty() || !song.mediaUrl.isNullOrEmpty()) {
                            JioSaavnMapper.mapToSong(song)
                        } else {
                            // Fetch detail for stream URL
                            val songId = song.id ?: return@async null
                            val detailResult = getSongDetail(songId)
                            detailResult.getOrNull()
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "Error fetching detail for song: ${song.id}", e)
                        // Fallback to mapping without detail
                        try {
                            JioSaavnMapper.mapToSong(song)
                        } catch (e2: Exception) {
                            null
                        }
                    }
                }
            }.awaitAll().filterNotNull()
        }
    }
}
