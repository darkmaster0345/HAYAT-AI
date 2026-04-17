package com.hayat.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SettingsScreen(
    modelPath: String,
    dbPath: String,
    onPathsChanged: (String, String) -> Unit
) {
    var modelPathState by remember { mutableStateOf(modelPath) }
    var dbPathState by remember { mutableStateOf(dbPath) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Settings",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        TextField(
            value = modelPathState,
            onValueChange = { modelPathState = it },
            label = { Text("Model Path (.gguf)") },
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = dbPathState,
            onValueChange = { dbPathState = it },
            label = { Text("Database Path (.db)") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = { onPathsChanged(modelPathState, dbPathState) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Settings")
        }
    }
}
