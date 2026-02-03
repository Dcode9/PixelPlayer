package com.theveloper.pixelplay.data.network.jiosaavn

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * JioSaavn API Service
 * Base URL: https://jiosaavn-m02hizdkx-dcode9s-projects.vercel.app
 * Aligned to saavn.sumit.co endpoints
 */
interface JioSaavnApiService {
    
    /**
     * Get trending songs
     * GET /api/modules?category=trending&language=en
     */
    @GET("api/modules")
    suspend fun getTrending(
        @Query("category") category: String = "trending",
        @Query("language") language: String = "en"
    ): Response<JioSaavnTrendingResponse>
    
    /**
     * Search for songs
     * GET /api/search/songs?query=
     */
    @GET("api/search/songs")
    suspend fun searchSongs(
        @Query("query") query: String,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20
    ): Response<JioSaavnSearchResponse>
    
    /**
     * Get song details by ID
     * GET /api/songs/{id}
     */
    @GET("api/songs/{id}")
    suspend fun getSongDetail(
        @Path("id") songId: String
    ): Response<JioSaavnSongDetailResponse>
}
