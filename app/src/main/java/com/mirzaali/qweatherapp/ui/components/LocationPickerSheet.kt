package com.mirzaali.qweatherapp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mirzaali.qweatherapp.domain.model.City


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationPickerBottomSheet(
    cities: List<City>,
    onDismiss: () -> Unit,
    onCitySelected: (City) -> Unit
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        LazyColumn {
            items(cities) { city ->
                Text(
                    text = "${city.name}, ${city.countryName}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onCitySelected(city) }
                        .padding(16.dp)
                )
            }
        }
    }
}
