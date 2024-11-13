package com.white.label.weather.view.compose

import androidx.compose.foundation.Image
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.white.label.weather.model.Weather
import com.white.label.weather.view.ui.theme.h2TextSize
import com.white.label.weather.view.ui.theme.normalTextSize
import com.white.label.weather.view.ui.theme.textColor
import com.white.label.weather.util.AppUtil
import com.white.label.weather.util.Constants
import com.white.label.weather.viewModel.MainViewModel


@Composable
fun DaysPredictionList(viewModel: MainViewModel,weather: Weather?, bgColor: Color?, bannerTitle: String?) {
    Card(
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.dp_8)),
        colors = CardDefaults.cardColors(
            containerColor = bgColor!!,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(dimensionResource(id = R.dimen.dp_8)),


        ) {
        val appIcon by viewModel.appIconImageResourceFlow.collectAsStateWithLifecycle()
        val dimen8 = dimensionResource(id = R.dimen.dp_8)

        weather?.let {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = dimen8, end = dimen8)
            ) {
                bannerTitle?.ifEmpty { stringResource(R.string.banner_days_title) }?.let { it1 ->
                    Text(
                        text = it1,
                        fontSize = h2TextSize,
                        color = textColor,
                        modifier = Modifier.padding(16.dp)


                    )
                }
                HorizontalDivider(
                    thickness = dimensionResource(id = R.dimen.dp_1),
                    color = Color.Gray
                )

                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(dimen8)
                ) {
                    val daysPrediction = it.daily
                    itemsIndexed(daysPrediction.temperature_2m_min) { index, item ->
                        HorizontalDivider(color = Color.Gray)

                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(1.0f),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                modifier = Modifier.weight(0.2f),
                                text = AppUtil.getDay(daysPrediction.time[index]),
                                fontSize = normalTextSize,
                                color = textColor
                            )
                            Image(
                                modifier = Modifier.weight(0.4f),
                                painter = if (appIcon.type == Constants.IMG_TYPE_DRAWABLE) painterResource(
                                    id = AppUtil.getIconImageResource(
                                        daysPrediction.weathercode[index],
                                        appIcon
                                    )
                                ) else rememberAsyncImagePainter(
                                    AppUtil.getIconImageUrl(
                                        daysPrediction.weathercode[index],
                                        appIcon.iconImgUrlResponse!!
                                    )
                                ),
                                contentDescription = "imgSrc"
                            )
                            Text(
                                modifier = Modifier.weight(0.2f),

                                text = "${daysPrediction.temperature_2m_min[index]}°",
                                fontSize = normalTextSize,
                                color = textColor

                            )
                            Text(
                                modifier = Modifier.weight(0.2f),

                                text = "${daysPrediction.temperature_2m_max[index]}°",
                                fontSize = normalTextSize,
                                color = textColor
                            )


                        }

                    }
                }
            }
        }
    }

}

