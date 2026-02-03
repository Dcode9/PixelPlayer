package com.theveloper.pixelplay.data.network.jiosaavn

import com.theveloper.pixelplay.data.model.ArtistRef
import com.theveloper.pixelplay.data.model.Song

/**
 * Mapper to convert JioSaavn API responses to domain Song models
 */
object JioSaavnMapper {
    
    /**
     * Converts a JioSaavn song to domain Song model
     * Cloud songs are marked with:
     * - id: "saavn:{songId}"
     * - path: "" (empty, not local)
     * - contentUriString: stream URL
     * - duration: converted from seconds to milliseconds
     */
    fun mapToSong(saavnSong: JioSaavnSong): Song? {
        val songId = saavnSong.id ?: return null
        val title = saavnSong.name ?: return null
        
        // Get best stream URL from downloadUrl or mediaUrl
        val streamUrl = getBestStreamUrl(saavnSong) ?: return null
        
        // Get primary artist info
        val primaryArtists = saavnSong.primaryArtists ?: "Unknown Artist"
        val artistsList = parseArtists(saavnSong)
        
        // Get album info
        val albumName = saavnSong.album?.name ?: "Unknown Album"
        val albumId = saavnSong.album?.id?.hashCode()?.toLong() ?: 0L
        
        // Get artwork URL (choose highest quality)
        val artworkUrl = getBestImageUrl(saavnSong.image)
        
        // Convert duration from seconds to milliseconds (safe from overflow)
        val durationMs = (saavnSong.duration?.toLong() ?: 0L) * 1000L
        
        // Year
        val year = saavnSong.year?.toIntOrNull() ?: 0
        
        return Song(
            id = "saavn:$songId", // Prefix to distinguish cloud songs
            title = title,
            artist = primaryArtists,
            artistId = primaryArtists.hashCode().toLong(),
            artists = artistsList,
            album = albumName,
            albumId = albumId,
            albumArtist = primaryArtists,
            path = "", // Empty path for cloud songs
            contentUriString = streamUrl, // Stream URL
            albumArtUriString = artworkUrl,
            duration = durationMs,
            genre = saavnSong.language,
            lyrics = null,
            isFavorite = false,
            trackNumber = 0,
            year = year,
            dateAdded = System.currentTimeMillis(),
            dateModified = System.currentTimeMillis(),
            mimeType = "audio/mpeg",
            bitrate = null,
            sampleRate = null
        )
    }
    
    /**
     * Gets the best available stream URL
     * Priority: highest quality downloadUrl > mediaUrl
     */
    private fun getBestStreamUrl(song: JioSaavnSong): String? {
        // Try downloadUrl first (choose highest quality)
        val downloadUrls = song.downloadUrl
        if (!downloadUrls.isNullOrEmpty()) {
            // Quality order: 320kbps > 160kbps > 96kbps > 48kbps > 12kbps
            val qualityOrder = listOf("320kbps", "160kbps", "96kbps", "48kbps", "12kbps")
            for (quality in qualityOrder) {
                val url = downloadUrls.find { it.quality == quality }
                if (url != null) {
                    return url.link ?: url.url
                }
            }
            // Fallback to first available
            val firstUrl = downloadUrls.firstOrNull()
            if (firstUrl != null) {
                return firstUrl.link ?: firstUrl.url
            }
        }
        
        // Fallback to mediaUrl
        return song.mediaUrl
    }
    
    /**
     * Gets the best image URL (highest quality)
     */
    private fun getBestImageUrl(images: List<JioSaavnImage>?): String? {
        if (images.isNullOrEmpty()) return null
        
        // Quality order: 500x500 > 150x150 > 50x50
        val qualityOrder = listOf("500x500", "150x150", "50x50")
        for (quality in qualityOrder) {
            val image = images.find { it.quality == quality }
            if (image != null) {
                return image.link ?: image.url
            }
        }
        
        // Fallback to first available
        val firstImage = images.firstOrNull()
        return firstImage?.link ?: firstImage?.url
    }
    
    /**
     * Parses artists from JioSaavn song data
     */
    private fun parseArtists(song: JioSaavnSong): List<ArtistRef> {
        val artistRefs = mutableListOf<ArtistRef>()
        
        // Use structured artists list if available
        song.artists?.forEach { artist ->
            val name = artist.name ?: return@forEach
            val id = artist.id?.hashCode()?.toLong() ?: name.hashCode().toLong()
            val isPrimary = artist.role?.contains("primary", ignoreCase = true) ?: false
            artistRefs.add(
                ArtistRef(
                    id = id,
                    name = name,
                    isPrimary = isPrimary
                )
            )
        }
        
        // Fallback to primaryArtists string if no structured data
        if (artistRefs.isEmpty() && !song.primaryArtists.isNullOrEmpty()) {
            val artistNames = song.primaryArtists.split(",").map { it.trim() }
            artistNames.forEachIndexed { index, name ->
                if (name.isNotEmpty()) {
                    artistRefs.add(
                        ArtistRef(
                            id = name.hashCode().toLong(),
                            name = name,
                            isPrimary = index == 0 // First artist is primary
                        )
                    )
                }
            }
        }
        
        return artistRefs
    }
    
    /**
     * Maps a list of JioSaavn songs to domain Song models
     */
    fun mapToSongs(saavnSongs: List<JioSaavnSong>?): List<Song> {
        if (saavnSongs == null) return emptyList()
        return saavnSongs.mapNotNull { mapToSong(it) }
    }
}
