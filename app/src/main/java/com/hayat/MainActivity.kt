package com.hayat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hayat.data.ChatMessage
import com.hayat.data.ChatRole
import com.hayat.ui.screens.ChatScreen
import com.hayat.ui.screens.ErrorScreen
import com.hayat.ui.screens.SettingsScreen
import com.hayat.ui.theme.HayatTheme
import com.hayat.viewmodel.MainViewModel
import com.hayat.viewmodel.Screen
import java.io.File

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            HayatApp()
        }
    }
}

@Composable
fun HayatApp() {
    val viewModel: MainViewModel = viewModel()
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val currentScreen by viewModel.currentScreen.collectAsState()
    val assetsMissing by viewModel.assetsMissing.collectAsState()
    val modelPath by viewModel.modelPath.collectAsState()
    val dbPath by viewModel.dbPath.collectAsState()
    val messages = viewModel.messages

    // Initialize paths on first launch
    LaunchedEffect(Unit) {
        viewModel.initializePaths(context)
    }

    // Re-check assets when app resumes
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.checkAssets()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    HayatTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            if (assetsMissing) {
                ErrorScreen()
            } else {
                Box {
                    when (currentScreen) {
                        Screen.Chat -> ChatScreen(
                            messages = messages,
                            onSendMessage = { text ->
                                viewModel.addMessage(ChatMessage(text, ChatRole.USER))
                                // Placeholder for AI response
                                viewModel.addMessage(ChatMessage("Checking medical guidelines for: $text", ChatRole.ASSISTANT))
                            }
                        )
                        Screen.Settings -> SettingsScreen(
                            modelPath = modelPath,
                            dbPath = dbPath,
                            onPathsChanged = { newModel, newDb ->
                                viewModel.updatePaths(newModel, newDb)
                                viewModel.setCurrentScreen(Screen.Chat)
                            }
                        )
                    }

                    // Simple floating button to switch screens for demonstration
                    FloatingActionButton(
                        onClick = {
                            val newScreen = if (currentScreen == Screen.Chat) Screen.Settings else Screen.Chat
                            viewModel.setCurrentScreen(newScreen)
                        },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(16.dp),
                        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = stringResource(R.string.toggle_settings),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}