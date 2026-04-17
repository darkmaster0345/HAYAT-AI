package com.hayat.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.hayat.R
import java.io.File

@Composable
fun SettingsScreen(
    modelPath: String,
    dbPath: String,
    onPathsChanged: (String, String) -> Unit
) {
    var modelPathState by remember { mutableStateOf(modelPath) }
    var dbPathState by remember { mutableStateOf(dbPath) }
    var modelPathError by remember { mutableStateOf<String?>(null) }
    var dbPathError by remember { mutableStateOf<String?>(null) }

    // Load string resources
    val errorPathEmpty = stringResource(R.string.error_path_empty)
    val errorInvalidModelExtension = stringResource(R.string.error_invalid_model_extension)
    val errorInvalidDbExtension = stringResource(R.string.error_invalid_db_extension)
    val errorFileNotFound = stringResource(R.string.error_file_not_found)
    val errorNotAFile = stringResource(R.string.error_not_a_file)

    fun validateModelPath(path: String): String? {
        val trimmedPath = path.trim()
        return when {
            trimmedPath.isBlank() -> errorPathEmpty
            !trimmedPath.endsWith(".gguf") -> errorInvalidModelExtension
            !File(trimmedPath).exists() -> errorFileNotFound
            !File(trimmedPath).isFile -> errorNotAFile
            else -> null
        }
    }

    fun validateDbPath(path: String): String? {
        val trimmedPath = path.trim()
        return when {
            trimmedPath.isBlank() -> errorPathEmpty
            !trimmedPath.endsWith(".db") -> errorInvalidDbExtension
            !File(trimmedPath).exists() -> errorFileNotFound
            !File(trimmedPath).isFile -> errorNotAFile
            else -> null
        }
    }

    val isValid = modelPathError == null && dbPathError == null

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(R.string.settings_title),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Column {
            TextField(
                value = modelPathState,
                onValueChange = {
                    modelPathState = it
                    modelPathError = validateModelPath(it)
                },
                label = { Text(stringResource(R.string.model_path_label)) },
                modifier = Modifier.fillMaxWidth(),
                isError = modelPathError != null
            )
            if (modelPathError != null) {
                Text(
                    text = modelPathError!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                )
            }
        }

        Column {
            TextField(
                value = dbPathState,
                onValueChange = {
                    dbPathState = it
                    dbPathError = validateDbPath(it)
                },
                label = { Text(stringResource(R.string.db_path_label)) },
                modifier = Modifier.fillMaxWidth(),
                isError = dbPathError != null
            )
            if (dbPathError != null) {
                Text(
                    text = dbPathError!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                )
            }
        }

        Button(
            onClick = {
                onPathsChanged(modelPathState.trim(), dbPathState.trim())
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = isValid
        ) {
            Text(stringResource(R.string.save_settings))
        }
    }
}