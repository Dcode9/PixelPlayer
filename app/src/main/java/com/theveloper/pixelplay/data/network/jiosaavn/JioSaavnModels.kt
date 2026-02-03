package com.theveloper.pixelplay.data.network.jiosaavn

import com.google.gson.annotations.SerializedName

/**
 * Response models for JioSaavn API
 * Base URL: https://jiosaavn-m02hizdkx-dcode9s-projects.vercel.app
 * Aligned to saavn.sumit.co endpoints structure
 */

// Trending response from GET /api/modules?category=trending&language=en
data class JioSaavnTrendingResponse(
    @SerializedName("data")
    val data: JioSaavnTrendingData? = null
)

data class JioSaavnTrendingData(
    @SerializedName("trending")
    val trending: JioSaavnTrendingModule? = null
)

data class JioSaavnTrendingModule(
    @SerializedName("position")
    val position: Int? = null,
    @SerializedName("title")
    val title: String? = null,
    @SerializedName("subtitle")
    val subtitle: String? = null,
    @SerializedName("source")
    val source: String? = null,
    @SerializedName("data")
    val data: List<JioSaavnSong>? = null
)

// Search response from GET /api/search/songs?query=
data class JioSaavnSearchResponse(
    @SerializedName("data")
    val data: JioSaavnSearchData? = null
)

data class JioSaavnSearchData(
    @SerializedName("results")
    val results: List<JioSaavnSong>? = null,
    @SerializedName("total")
    val total: Int? = null
)

// Song detail response from GET /api/songs/{id}
data class JioSaavnSongDetailResponse(
    @SerializedName("data")
    val data: List<JioSaavnSong>? = null
)

// Main song data structure
data class JioSaavnSong(
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("type")
    val type: String? = null,
    @SerializedName("year")
    val year: String? = null,
    @SerializedName("releaseDate")
    val releaseDate: String? = null,
    @SerializedName("duration")
    val duration: Int? = null, // Duration in seconds
    @SerializedName("label")
    val label: String? = null,
    @SerializedName("explicitContent")
    val explicitContent: Boolean? = null,
    @SerializedName("playCount")
    val playCount: Int? = null,
    @SerializedName("language")
    val language: String? = null,
    @SerializedName("hasLyrics")
    val hasLyrics: Boolean? = null,
    @SerializedName("lyricsId")
    val lyricsId: String? = null,
    @SerializedName("url")
    val url: String? = null,
    
    // Artist information
    @SerializedName("primaryArtists")
    val primaryArtists: String? = null, // Comma-separated string
    @SerializedName("primaryArtistsId")
    val primaryArtistsId: String? = null,
    @SerializedName("featuredArtists")
    val featuredArtists: String? = null,
    @SerializedName("featuredArtistsId")
    val featuredArtistsId: String? = null,
    @SerializedName("artists")
    val artists: List<JioSaavnArtist>? = null,
    
    // Album information
    @SerializedName("album")
    val album: JioSaavnAlbum? = null,
    
    // Image URLs
    @SerializedName("image")
    val image: List<JioSaavnImage>? = null,
    
    // Download URLs - choose best quality
    @SerializedName("downloadUrl")
    val downloadUrl: List<JioSaavnDownloadUrl>? = null,
    @SerializedName("media_url")
    val mediaUrl: String? = null // Alternative stream URL field
)

data class JioSaavnArtist(
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("role")
    val role: String? = null,
    @SerializedName("type")
    val type: String? = null,
    @SerializedName("image")
    val image: List<JioSaavnImage>? = null,
    @SerializedName("url")
    val url: String? = null
)

data class JioSaavnAlbum(
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("url")
    val url: String? = null
)

data class JioSaavnImage(
    @SerializedName("quality")
    val quality: String? = null, // "50x50", "150x150", "500x500"
    @SerializedName("link")
    val link: String? = null,
    @SerializedName("url")
    val url: String? = null
)

data class JioSaavnDownloadUrl(
    @SerializedName("quality")
    val quality: String? = null, // "12kbps", "48kbps", "96kbps", "160kbps", "320kbps"
    @SerializedName("link")
    val link: String? = null,
    @SerializedName("url")
    val url: String? = null
)
