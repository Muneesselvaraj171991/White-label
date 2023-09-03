package com.white.label.weather.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.white_label.R

@Composable
fun daysPredictionList() {
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
                LazyColumn (modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ){

                    items(10) { item ->

                        Row(
                            modifier = Modifier.fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            // your composable here
                            Text(text = "Kungalv", fontSize = 24.sp)
                            Image(
                                painter = painterResource(R.drawable.cloudy),
                                contentDescription = "imgSrc"
                            )
                            Text(text = "19", fontSize = 24.sp)
                            Text(text = "19", fontSize = 24.sp)

                        }
                    }}
            }
        }

    }

