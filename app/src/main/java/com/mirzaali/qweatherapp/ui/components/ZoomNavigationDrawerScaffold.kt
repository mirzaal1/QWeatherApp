package com.mirzaali.qweatherapp.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun ZoomNavigationDrawerScaffold(
    drawerContent: @Composable () -> Unit,
    content: @Composable (openDrawer: () -> Unit) -> Unit
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val scale by animateFloatAsState(
        targetValue = if (drawerState.isOpen) 0.85f else 1f,
        animationSpec = tween(300),
        label = "scale"
    )

    val offsetX by animateDpAsState(
        targetValue = if (drawerState.isOpen) 180.dp else 0.dp,
        animationSpec = tween(300),
        label = "offsetX"
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                drawerContent()
            }
        }
    ) {
        Box(
            modifier = Modifier
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    translationX = offsetX.toPx()
                }
        ) {
            content {
                scope.launch { drawerState.open() }
            }
        }
    }
}
