package com.mirzaali.qweatherapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


fun Modifier.roundedContainer(
    backgroundColor: Color,
    borderColor: Color = Color.LightGray,
    shape: RoundedCornerShape = RoundedCornerShape(24.dp)
): Modifier = this
    .border(1.dp, borderColor, shape)
    .background(backgroundColor, shape)
