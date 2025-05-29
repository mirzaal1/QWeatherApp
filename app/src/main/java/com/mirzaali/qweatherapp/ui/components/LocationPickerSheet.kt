package com.mirzaali.qweatherapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mirzaali.qweatherapp.R
import com.mirzaali.qweatherapp.domain.model.City

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationPickerBottomSheet(
    cities: List<City>,
    selectedCityId: Int? = null,
    onDismiss: () -> Unit,
    onCitySelected: (City) -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        containerColor = Color(0xFFFCFBFA)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {

            Text(
                text = stringResource(R.string.back),
                color = Color(0xFF9D1C38),
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .clickable { onDismiss() }
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_location),
                    contentDescription = null,
                    tint = Color.Red,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = stringResource(R.string.current_location),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Text(
                        text = cities.firstOrNull { it.id == selectedCityId }?.let {
                            "${it.name}, ${it.countryName}"
                        } ?: stringResource(R.string.selectcity),
                        fontSize = 14.sp,
                        color = Color.Gray
                    )

                }
            }

            Divider(color = Color.LightGray)

            LazyColumn {
                items(cities) { city ->
                    val isSelected = city.id == selectedCityId
                    val background = if (isSelected) Color(0xFFFFF3CD) else Color.Transparent

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(background)
                            .clickable { onCitySelected(city) }
                    ) {
                        Text(
                            text = city.name,
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp),
                            fontSize = 16.sp
                        )
                    }

                    Divider(color = Color.LightGray)
                }
            }
        }
    }
}
