package com.hayat.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ErrorScreen(missingPath: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Warning,
            contentDescription = "Warning",
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Missing Assets",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.error,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Please create the following folder and add the required files (model.gguf and medical.db):",
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(16.dp))
        Surface(
            color = MaterialTheme.colorScheme.surface,
            shape = MaterialTheme.shapes.medium
        ) {
            Text(
                text = missingPath,
                modifier = Modifier.padding(16.dp),
                color = Color.Yellow,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }
    }
}
