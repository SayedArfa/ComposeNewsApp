package com.example.core.common.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TitleBar(title: String, modifier: Modifier = Modifier, onBackPressed: () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.Start,
        modifier = modifier
            .padding(horizontal = 10.dp)
            .clickable {
                onBackPressed()
            }
    ) {
        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
        Spacer(modifier = Modifier.width(5.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge
        )
    }
}