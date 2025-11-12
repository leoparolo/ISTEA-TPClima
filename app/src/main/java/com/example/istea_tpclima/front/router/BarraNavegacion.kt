package com.example.istea_tpclima.front.router

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.example.istea_tpclima.Screen

@Composable
fun BarraNavegacion(
    currentRoute: String,
    onNavigate: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White)
            .shadow(6.dp, RoundedCornerShape(24.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp) // 👈 altura exacta y compacta
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavItem(
                selected = currentRoute == Screen.Ciudades.route,
                icon = Icons.Filled.LocationOn,
                onClick = { onNavigate(Screen.Ciudades.route) }
            )

            NavItem(
                selected = currentRoute == Screen.Clima.route,
                icon = Icons.Filled.Cloud,
                onClick = { onNavigate(Screen.Clima.route) }
            )
        }
    }
}
@Composable
fun NavItem(
    selected: Boolean,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (selected) Color.Black else Color.Transparent,
        label = "backgroundColor"
    )

    val iconColor by animateColorAsState(
        targetValue = if (selected) Color.White else Color.Black,
        label = "iconColor"
    )

    val iconSize by animateDpAsState(
        targetValue = if (selected) 28.dp else 22.dp,
        label = "iconSize"
    )
    val circleSize by animateDpAsState(
        targetValue = if (selected) 30.dp else 24.dp,
        label = "circleSize"
    )

    Box(
        modifier = Modifier
            .size(circleSize)
            .background(backgroundColor, CircleShape)
            .padding(4.dp)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier.size(iconSize)
        )
    }
}