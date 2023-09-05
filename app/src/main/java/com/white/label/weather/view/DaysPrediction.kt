package com.white.label.weather.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.white_label.R
import com.white.label.weather.ui.theme.h2TextSize
import com.white.label.weather.ui.theme.normalTextSize
import com.white.label.weather.ui.theme.textColor
import com.white.label.weather.util.AppUtil
import com.white.label.weather.util.Constants
import com.white.label.weather.viewModel.MainViewModel


@Composable
fun DaysPredictionList(viewModel: MainViewModel, bgColor: Color, bannerTitle: String) {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = bgColor,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(8.dp),


    ) {
        val weatherApi by viewModel.webApiLiveData.observeAsState()
        val appIcon by viewModel.appIconImageResourceLiveData.observeAsState()
        val dimen8 = dimensionResource(id = R.dimen.dp_8)

        val weather = weatherApi
        weather?.let {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = dimen8, end = dimen8)
            ) {
                Text(
                    text = bannerTitle.ifEmpty { stringResource(R.string.banner_days_title) },
                    fontSize = h2TextSize,
                    color = textColor,
                    modifier = Modifier.padding(16.dp)


                )
                Divider(color = Color.Gray, thickness = dimensionResource(id = R.dimen.dp_1))

                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(dimen8)
                ) {
                    val daysPrediction = weather.daily
                    itemsIndexed(daysPrediction.temperature_2m_min) { index, item ->

                        Row(
                            modifier = Modifier.fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Text(
                                text = AppUtil.getDay(daysPrediction.time[index]),
                                fontSize = normalTextSize
                            )
                            Image(
                                painter = if (appIcon!!.type == Constants.IMG_TYPE_DRAWABLE) painterResource(
                                    id = AppUtil.getIconImageResource(
                                        daysPrediction.weathercode[index],
                                        appIcon!!
                                    )
                                ) else rememberAsyncImagePainter(
                                    AppUtil.getIconImageUrl(
                                        daysPrediction.weathercode[index],
                                        appIcon!!.iconImgUrlResponse!!
                                    )
                                ),
                                contentDescription = "imgSrc"
                            )
                            Text(
                                text = "${daysPrediction.temperature_2m_min[index]}°",
                                fontSize = normalTextSize
                            )
                            Text(
                                text = "${daysPrediction.temperature_2m_max[index]}°",
                                fontSize = normalTextSize
                            )


                        }
                        Divider(color = Color.Gray)

                    }
                }
            }
        }
    }

}

