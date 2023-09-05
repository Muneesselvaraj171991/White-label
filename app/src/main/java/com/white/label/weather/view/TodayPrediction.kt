package com.white.label.weather.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.white_label.R
import com.white.label.weather.ui.theme.darkTextColor
import com.white.label.weather.ui.theme.h2TextSize
import com.white.label.weather.ui.theme.normalTextSize
import com.white.label.weather.ui.theme.textColor
import com.white.label.weather.util.AppUtil
import com.white.label.weather.util.Constants
import com.white.label.weather.viewModel.MainViewModel


@Composable
fun DayPredictionScreen(viewModel: MainViewModel, bgColor: Color, bannerTitle: String) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(8.dp)
            .background(bgColor)

    ) {
        val weatherApi by viewModel.webApiLiveData.observeAsState()
        val appIcon by viewModel.appIconImageResourceLiveData.observeAsState()
        val weather = weatherApi
        weather?.let {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp)
            ) {
                Text(
                    text = bannerTitle.ifEmpty { stringResource(R.string.banner_hourly_title) },
                    fontSize = h2TextSize,
                    color = textColor,
                    modifier = Modifier.padding(8.dp)
                )
                Divider(color = Color.Red, thickness = 1.dp)
                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {


                    itemsIndexed(weather.hourly.getHourlyPredictions()) { index, item ->

                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {

                            Text(text = item.time, fontSize = h2TextSize)
                            Image(
                                painter = if (appIcon!!.type == Constants.IMG_TYPE_DRAWABLE) painterResource(
                                    id = AppUtil.getIconImageResource(item.weatherCode, appIcon!!)
                                ) else rememberAsyncImagePainter(
                                    AppUtil.getIconImageUrl(
                                        item.weatherCode,
                                        appIcon!!.iconImgUrlResponse!!
                                    )
                                ),
                                contentDescription = "imgSrc"
                            )
                            Text(text = "${item.temp}°", fontSize = normalTextSize)

                        }
                    }
                }
            }
        }
    }
}
