@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.theveloper.pixelplay.presentation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.theveloper.pixelplay.data.model.Song
import com.theveloper.pixelplay.presentation.components.SmartImage
import com.theveloper.pixelplay.presentation.components.SongInfoBottomSheet
import com.theveloper.pixelplay.presentation.viewmodel.PlayerViewModel
import com.theveloper.pixelplay.utils.formatDuration

/**
 * Trending screen displaying trending songs from JioSaavn
 * Material 3 Expressive design with cloud music support
 */
@OptIn(UnstableApi::class)
@Composable
fun TrendingScreen(
    paddingValues: PaddingValues,
    playerViewModel: PlayerViewModel,
    navController: NavController,
    viewModel: TrendingViewModel = hiltViewModel()
) {
    val trendingSongs by viewModel.trendingSongs.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    
    var selectedSong by remember { mutableStateOf<Song?>(null) }
    var showSongInfo by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Trending",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(paddingValues)
        ) {
            when {
                isLoading -> {
                    // Loading state
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            CircularProgressIndicator()
                            Text(
                                text = "Loading trending songs...",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
                errorMessage != null -> {
                    // Error state
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier.padding(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.ErrorOutline,
                                contentDescription = "Error",
                                modifier = Modifier.size(64.dp),
                                tint = MaterialTheme.colorScheme.error
                            )
                            Text(
                                text = errorMessage ?: "Unknown error",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.error
                            )
                            Button(onClick = { viewModel.loadTrendingSongs() }) {
                                Text("Retry")
                            }
                        }
                    }
                }
                trendingSongs.isEmpty() -> {
                    // Empty state
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier.padding(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.MusicNote,
                                contentDescription = "No songs",
                                modifier = Modifier.size(64.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "No trending songs available",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
                else -> {
                    // Songs list
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(trendingSongs, key = { it.id }) { song ->
                            TrendingSongItem(
                                song = song,
                                onPlayNow = {
                                    playerViewModel.playSongs(listOf(song), 0)
                                },
                                onPlayNext = {
                                    playerViewModel.addToQueueNext(song)
                                },
                                onAddToQueue = {
                                    playerViewModel.addToQueue(song)
                                },
                                onAddToLibrary = {
                                    playerViewModel.toggleFavorite(song.id)
                                },
                                onMoreClick = {
                                    selectedSong = song
                                    showSongInfo = true
                                }
                            )
                        }
                    }
                }
            }
        }
    }
    
    // Song info bottom sheet
    if (showSongInfo && selectedSong != null) {
        SongInfoBottomSheet(
            song = selectedSong!!,
            onDismiss = { showSongInfo = false },
            playerViewModel = playerViewModel,
            navController = navController
        )
    }
}

@Composable
private fun TrendingSongItem(
    song: Song,
    onPlayNow: () -> Unit,
    onPlayNext: () -> Unit,
    onAddToQueue: () -> Unit,
    onAddToLibrary: () -> Unit,
    onMoreClick: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onPlayNow() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Album artwork
            SmartImage(
                albumArtUri = song.albumArtUriString,
                contentDescription = song.title,
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
            
            // Song info
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = song.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = song.displayArtist,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = formatDuration(song.duration),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            // More options button
            Box {
                IconButton(onClick = { showMenu = true }) {
                    Icon(
                        imageVector = Icons.Rounded.MoreVert,
                        contentDescription = "More options"
                    )
                }
                
                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Play now") },
                        leadingIcon = {
                            Icon(Icons.Rounded.PlayArrow, contentDescription = null)
                        },
                        onClick = {
                            showMenu = false
                            onPlayNow()
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Play next") },
                        leadingIcon = {
                            Icon(Icons.Rounded.SkipNext, contentDescription = null)
                        },
                        onClick = {
                            showMenu = false
                            onPlayNext()
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Add to queue") },
                        leadingIcon = {
                            Icon(Icons.Rounded.PlaylistAdd, contentDescription = null)
                        },
                        onClick = {
                            showMenu = false
                            onAddToQueue()
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Add to library") },
                        leadingIcon = {
                            Icon(Icons.Rounded.Favorite, contentDescription = null)
                        },
                        onClick = {
                            showMenu = false
                            onAddToLibrary()
                        }
                    )
                    Divider()
                    DropdownMenuItem(
                        text = { Text("Song info") },
                        leadingIcon = {
                            Icon(Icons.Rounded.Info, contentDescription = null)
                        },
                        onClick = {
                            showMenu = false
                            onMoreClick()
                        }
                    )
                }
            }
        }
    }
}
