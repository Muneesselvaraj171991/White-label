package com.white.label.weather.view

import android.health.connect.datatypes.units.Percentage
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.white_label.R

@Composable
fun dayPredictionScreen() {

        Card(
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(8.dp)

        ) {

        Column (modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp)){
            Text(text = "Headervvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvgffffffffffffffffffffff", fontSize = 24.sp)
            Divider(color = Color.Red, thickness = 1.dp)

            val itemList = listOf("Item1", "Item2")
            LazyRow (modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ){

                items(10) { item ->

                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
    // your composable here
                        Text(text = "Kungalv", fontSize = 24.sp)
                        Image(
                            painter = painterResource(R.drawable.cloudy),
                            contentDescription = "imgSrc"
                        )
                        Text(text = "19", fontSize = 24.sp)

                    }
                }}
            }
        }
    }
