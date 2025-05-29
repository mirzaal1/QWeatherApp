package com.mirzaali.qweatherapp.navigation

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mirzaali.qweatherapp.R
import com.mirzaali.qweatherapp.ui.main.MainScreen
import com.mirzaali.qweatherapp.ui.theme.drawerBg

@Composable
fun NavGraph(
    navController: NavHostController,
) {
    var isDrawerOpen by rememberSaveable { mutableStateOf(false) }
    val scale by animateFloatAsState(targetValue = if (isDrawerOpen) 0.75f else 1f, label = "scale")

    val layoutDirection = LocalLayoutDirection.current
    val offsetX by animateDpAsState(
        targetValue = if (isDrawerOpen) {
            if (layoutDirection == LayoutDirection.Rtl) -200.dp else 200.dp
        } else {
            0.dp
        },
        label = "offset"
    )

    Box(modifier = Modifier.fillMaxSize()) {

        AppDrawerContent()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    translationX = offsetX.toPx()
                    shape = RoundedCornerShape(if (isDrawerOpen) 16.dp else 0.dp)
                    clip = true
                    shadowElevation = if (isDrawerOpen) 12f else 0f
                }
                .background(MaterialTheme.colorScheme.background)
                .clickable(
                    enabled = isDrawerOpen,
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }) {
                    isDrawerOpen = false
                }
        ) {
            NavHost(
                navController = navController,
                startDestination = "main"
            ) {
                composable("main") {
                    MainScreen(
                        onMenuClick = { isDrawerOpen = !isDrawerOpen }
                    )
                }
            }
        }

    }
}


@Composable
fun AppDrawerContent(
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxSize()
            .background(drawerBg)
            .padding(top = 100.dp, start = 20.dp)
    ) {

            Icon(
                painter = painterResource(id = R.drawable.ic_launch),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(200.dp)
            )

        Spacer(modifier = Modifier.width(24.dp))

        // Dummy items
        DrawerItem(stringResource(R.string.menu_forecast), R.drawable.ic_cloud, { })
        DrawerItem(stringResource(R.string.menu_profile), R.drawable.ic_pressure, {})
        DrawerItem(stringResource(R.string.menu_settings), R.drawable.ic_max_temp, {})
        DrawerItem(stringResource(R.string.menu_help), R.drawable.ic_visibility, {})

        DrawerItem(stringResource(R.string.menu_forecast), R.drawable.ic_cloud, { })
        DrawerItem(stringResource(R.string.menu_profile), R.drawable.ic_pressure, {})
        DrawerItem(stringResource(R.string.menu_settings), R.drawable.ic_max_temp, {})
        DrawerItem(stringResource(R.string.menu_help), R.drawable.ic_visibility, {})

    }
}


@Composable
fun DrawerItem(
    label: String,
    iconRes: Int,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge, color = Color.White
        )
    }
}
