package com.example.firebase

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage

@Composable
fun CustomInAppMessageDialog(onActionClicked: ((actionUrl: String?) -> Unit)? = null) {
    val activeMessage by CustomInAppMessagingManager.currentMessage.collectAsStateWithLifecycle()

    activeMessage?.let { messageData ->
        AlertDialog(
            onDismissRequest = {
                CustomInAppMessagingManager.dismissMessage(messageData)
            },
            title = {
                messageData.title?.let {
                    Text(it, style = MaterialTheme.typography.titleLarge)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    messageData.imageUrl?.let { url ->
                        AsyncImage(
                            model = url,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp),
                        )
                    }
                    messageData.body?.let {
                        Text(it, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val actionUrl = messageData.actionUrl
                        CustomInAppMessagingManager.handleActionClick(messageData)
                        onActionClicked?.invoke(actionUrl)
                    },
                ) {
                    Text("View WatchList")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        CustomInAppMessagingManager.dismissMessage(messageData)
                    },
                ) {
                    Text("Close")
                }
            },
        )
    }
}
