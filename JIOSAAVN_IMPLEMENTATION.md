# JioSaavn Cloud Playback Implementation

## Overview
This implementation adds JioSaavn-based cloud music streaming to PixelPlayer, replacing the local-only music playback with a hybrid approach that includes cloud music from JioSaavn's API.

## Base URL
`https://jiosaavn-m02hizdkx-dcode9s-projects.vercel.app`

## Changes Made

### 1. API Integration Layer
**Files Created:**
- `app/src/main/java/com/theveloper/pixelplay/data/network/jiosaavn/JioSaavnModels.kt`
  - Complete data models for JioSaavn API responses
  - Supports trending, search, and song detail endpoints
  - Models: `JioSaavnTrendingResponse`, `JioSaavnSearchResponse`, `JioSaavnSong`, etc.

- `app/src/main/java/com/theveloper/pixelplay/data/network/jiosaavn/JioSaavnApiService.kt`
  - Retrofit service interface with three main endpoints:
    - `getTrending()` - GET /api/modules?category=trending&language=en
    - `searchSongs(query)` - GET /api/search/songs?query=
    - `getSongDetail(id)` - GET /api/songs/{id}

- `app/src/main/java/com/theveloper/pixelplay/data/network/jiosaavn/JioSaavnMapper.kt`
  - Maps JioSaavn API responses to domain `Song` model
  - Cloud songs marked with:
    - ID prefix: `"saavn:{songId}"`
    - Empty `path` field
    - `contentUriString` contains stream URL
  - Selects best quality stream URL (320kbps preferred)
  - Selects best quality artwork (500x500 preferred)
  - Converts duration from seconds to milliseconds

- `app/src/main/java/com/theveloper/pixelplay/data/network/jiosaavn/JioSaavnRepository.kt`
  - Repository with bounded concurrency for detail fetching
  - Implements trending songs, search, and song detail methods
  - Error handling and logging

**Files Modified:**
- `app/src/main/java/com/theveloper/pixelplay/di/AppModule.kt`
  - Added JioSaavn Retrofit instance with custom headers:
    - User-Agent: `PixelPlayer/1.0 (Android)`
    - Referer: `https://www.jiosaavn.com/`
  - Added JioSaavnApiService and JioSaavnRepository providers

- `app/src/main/java/com/theveloper/pixelplay/di/Qualifiers.kt`
  - Added `@JioSaavnRetrofit` qualifier annotation

### 2. Repository & Domain Layer
**Files Modified:**
- `app/src/main/java/com/theveloper/pixelplay/data/repository/MusicRepository.kt`
  - Added new methods:
    - `getTrendingSongs(): Flow<List<Song>>`
    - `searchSongsRemote(query: String, limit: Int): Flow<List<Song>>`

- `app/src/main/java/com/theveloper/pixelplay/data/repository/MusicRepositoryImpl.kt`
  - Injected `JioSaavnRepository`
  - Implemented cloud music methods with proper error handling
  - Returns Flow for reactive updates

### 3. Presentation Layer - Trending Feature
**Files Created:**
- `app/src/main/java/com/theveloper/pixelplay/presentation/screens/TrendingScreen.kt`
  - Material 3 Expressive design
  - Loading, error, and empty states
  - Song list with album art, title, artist, duration
  - Actions: Play now, Play next, Add to queue, Add to library, Song info
  - Integration with SongInfoBottomSheet

- `app/src/main/java/com/theveloper/pixelplay/presentation/viewmodel/TrendingViewModel.kt`
  - State management for trending songs
  - Loading and error state handling
  - Refresh functionality

- `app/src/main/res/drawable/rounded_whatshot_24.xml`
  - Fire/trending icon for navigation

**Files Modified:**
- `app/src/main/java/com/theveloper/pixelplay/presentation/navigation/Screen.kt`
  - Added `Trending` screen route

- `app/src/main/java/com/theveloper/pixelplay/presentation/navigation/AppNavigation.kt`
  - Added Trending screen navigation route
  - Proper transitions and animations

- `app/src/main/java/com/theveloper/pixelplay/MainActivity.kt`
  - Added Trending to bottom navigation bar
  - Position: Between Home and Search tabs

### 4. Presentation Layer - Search Update
**Files Modified:**
- `app/src/main/java/com/theveloper/pixelplay/presentation/viewmodel/SearchStateHolder.kt`
  - Updated `performSearch()` to use JioSaavn remote search
  - Converts cloud songs to `SearchResultItem.SongItem`
  - Removed local search filter dependency

### 5. UI Cleanup for Cloud Songs
**Files Modified:**
- `app/src/main/java/com/theveloper/pixelplay/presentation/components/SongInfoBottomSheet.kt`
  - Hidden "Share" button for cloud songs
  - Hidden "Delete From Device" button for cloud songs
  - Hidden "Path" field for cloud songs
  - Cloud song detection: `song.path.isEmpty() || song.id.startsWith("saavn:")`

### 6. Build Configuration
**Files Modified:**
- `gradle/libs.versions.toml`
  - Updated AGP version from 8.10.1 to 8.3.0 (valid version)

## Cloud Song Identification
Cloud songs from JioSaavn are identified by:
1. **ID prefix**: `"saavn:{songId}"` (e.g., `"saavn:abc123"`)
2. **Empty path**: `song.path == ""`
3. **Stream URL in contentUriString**: Contains HTTP(S) URL instead of content:// URI

## Playback Support
- **No changes needed to MediaItemBuilder**: Already supports both content URIs and HTTP URLs
- **No changes needed to ExoPlayer**: Media3's ExoPlayer natively supports HTTP streaming
- **Headers**: Added at API level (Retrofit), stream URLs work without additional headers
- **Queue operations**: Work seamlessly with cloud songs (same Song model)

## Favorites/Library
- Cloud songs can be added to favorites
- Stored with same structure as local songs
- Path field empty for cloud songs
- All metadata (title, artist, artwork, duration) preserved

## Features Not Modified
The following features remain unchanged and continue to work with local music:
- Local MediaStore scanning
- File system access for local songs
- Local permission handling (SetupScreen)
- Workers for local sync
- Deezer artist artwork integration (still works)

## API Error Handling
All network operations include:
- Try-catch blocks with logging
- Graceful fallback to empty results
- User-friendly error messages in UI
- Retry functionality in Trending screen

## Performance Optimizations
- Bounded concurrency (max 5) for detail API calls
- Chunked processing for batch song details
- Flow-based reactive updates
- Efficient image loading with Coil (existing)

## Material 3 Expressive Design
- Rounded corners on cards (16dp)
- Smooth shapes with AbsoluteSmoothCornerShape
- Proper spacing and padding
- Consistent with existing app design
- Loading states with CircularProgressIndicator
- Error states with icons and retry buttons

## Testing Recommendations
To test the implementation:

1. **Trending Tab**:
   - Open app → Navigate to Trending tab
   - Verify songs load from JioSaavn
   - Test play, queue operations
   - Test add to favorites

2. **Search**:
   - Navigate to Search tab
   - Search for any song (e.g., "Blinding Lights")
   - Verify JioSaavn results appear
   - Test playback and queue operations

3. **Playback**:
   - Play a cloud song from Trending or Search
   - Verify audio streams correctly
   - Test play/pause, next/previous
   - Verify artwork displays

4. **Song Info Sheet**:
   - Long-press a cloud song → Song Info
   - Verify Share button is hidden
   - Verify Delete button is hidden
   - Verify Path field is hidden

5. **Favorites**:
   - Add cloud song to favorites/library
   - Navigate to Library → Favorites
   - Verify cloud song appears
   - Verify playback from favorites works

## Known Limitations
1. **No offline support** for cloud songs (streaming only)
2. **No lyrics** for cloud songs (JioSaavn API doesn't provide lyrics)
3. **No local edit** capabilities for cloud songs (no file to edit)
4. **Network dependency** for cloud playback

## Future Enhancements
Potential improvements not in scope:
- Caching of stream URLs
- Offline download of cloud songs
- Lyrics fetching from alternative sources
- Playlist sync with JioSaavn
- User authentication for personalized recommendations

## API Compliance
- Base URL: Provided gateway URL
- Headers: User-Agent and Referer as specified
- Endpoints: Aligned with saavn.sumit.co structure
- Rate limiting: Bounded concurrency prevents API overload
