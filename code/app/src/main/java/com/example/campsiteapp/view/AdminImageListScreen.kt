/*
 * Admin list of all uploaded images and who uploaded them.
 */
package com.example.campsiteapp.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.campsiteapp.R
import com.example.campsiteapp.controller.LoggedImage
import com.example.campsiteapp.model.User
import com.example.campsiteapp.view.components.RemoveButton
import com.example.campsiteapp.view.components.ScreenScaffold

/**
 * Screen listing every uploaded image.
 *
 * @param images images to show.
 * @param profiles profiles used to name the uploaders.
 * @param onUploaderClick called when the uploader of an image is tapped.
 * @param onRemoveImage called when the admin confirms removing an image.
 * @param onBack called when Back is pressed.
 */
@Composable
fun AdminImageListScreen(
    images: List<LoggedImage>,
    profiles: List<User>,
    onUploaderClick: (User) -> Unit,
    onRemoveImage: (LoggedImage) -> Unit,
    onBack: () -> Unit,
) {
    ScreenScaffold(title = stringResource(R.string.images_title), onBack = onBack) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            items(images, key = { it.id }) { image ->
                ImageRow(
                    image = image,
                    uploader = profiles.find { it.deviceId == image.uploaderId },
                    onUploaderClick = onUploaderClick,
                    onRemove = { onRemoveImage(image) },
                )
                HorizontalDivider()
            }
        }
    }
}

/**
 * Uploader name on the left and the image with its remove button on the right.
 * The name is only tappable while the uploader still has a profile.
 */
@Composable
private fun ImageRow(image: LoggedImage, uploader: User?, onUploaderClick: (User) -> Unit, onRemove: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .clickable(enabled = uploader != null) { uploader?.let(onUploaderClick) },
        ) {
            Text(stringResource(R.string.uploaded_by))
            Text(uploader?.name?.ifBlank { null } ?: image.uploaderId, fontWeight = FontWeight.Bold)
        }
        Box(contentAlignment = Alignment.BottomEnd) {
            AsyncImage(
                model = image.url,
                contentDescription = stringResource(R.string.uploaded_image),
                modifier = Modifier.size(96.dp),
                contentScale = ContentScale.Crop,
            )
            RemoveButton(
                title = stringResource(R.string.remove_image_title),
                message = stringResource(R.string.remove_image_message),
                onConfirm = onRemove,
            )
        }
    }
}
