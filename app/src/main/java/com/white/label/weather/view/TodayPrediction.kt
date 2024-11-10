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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.example.white_label.R
import com.white.label.weather.ui.theme.h2TextSize
import com.white.label.weather.ui.theme.normalTextSize
import com.white.label.weather.ui.theme.textColor
import com.white.label.weather.util.AppUtil
import com.white.label.weather.util.Constants
import com.white.label.weather.viewModel.MainViewModel


@Composable
fun DayPredictionScreen(viewModel: MainViewModel, bgColor: Color, bannerTitle: String) {
    Card(
        shape = RoundedCornerShape(
            dimensionResource(id = R.dimen.dp_8)),
        colors = CardDefaults.cardColors(
            containerColor = bgColor,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(dimensionResource(id = R.dimen.dp_8))

    ) {
        val weatherApi by viewModel.webApiFlowData.collectAsStateWithLifecycle()
        val appIcon by viewModel.appIconImageResourceFlow.collectAsStateWithLifecycle()
        weatherApi?.let { it
            val dimen8 = dimensionResource(id = R.dimen.dp_8)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = dimen8, end = dimen8)
            ) {
                Text(
                    text = bannerTitle.ifEmpty { stringResource(R.string.banner_hourly_title) },
                    fontSize = h2TextSize,
                    color = textColor,
                    modifier = Modifier.padding(dimen8)
                )
                Divider(color = Color.Gray, thickness = dimensionResource(id = R.dimen.dp_1))
                Spacer(modifier = Modifier.height(dimen8))

                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(dimen8)
                ) {
                    itemsIndexed(it.hourly.getHourlyPredictions()) { index, item ->

                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {

                            Text(text = item.time, fontSize = h2TextSize ,color = textColor)
                            Image(
                                painter = if (appIcon!!.type == Constants.IMG_TYPE_DRAWABLE) painterResource(
                                    id = AppUtil.getIconImageResource(item.weatherCode, appIcon!!)
                                ) else rememberAsyncImagePainter(
                                    appIcon?.iconImgUrlResponse?.let { it1 ->
                                        AppUtil.getIconImageUrl(
                                            item.weatherCode,
                                            it1
                                        )
                                    }
                                ),
                                contentDescription = "imgSrc",
                            )
                            Text(text = "${item.temp}°", color = textColor, fontSize = normalTextSize)

                        }
                    }
                }
            }
        }
    }
}
