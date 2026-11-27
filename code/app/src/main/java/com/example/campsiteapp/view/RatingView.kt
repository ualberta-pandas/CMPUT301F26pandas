package com.example.campsiteapp.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun StarRating(
    rating: Int,
    onRatingChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    maxStars: Int = 5,
    starSize: Dp = 32.dp
) {
    Row(modifier = modifier) {
        for (i in 1..maxStars) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = "$i stars",
                tint = if (i <= rating) Color(0xFFFFC107) else Color.LightGray,
                modifier = Modifier
                    .size(starSize)
                    .clickable { onRatingChange(i) }
            )
        }
    }
}