package com.sparky.remotef1.screens.remote

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import com.sparky.remotef1.ConfigContent
import com.sparky.remotef1.R
import com.sparky.remotef1.ui.theme.Blue
import com.sparky.remotef1.ui.theme.Orange
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RemoteScreen(
    args: RemoteScreen,
    remoteScreenViewModel: RemoteScreenViewModel
) {
    val ip = args.ip
    val port = args.port

    var throttleSliderPosition = remember { mutableFloatStateOf(0f) }
    var steeringSliderPosition = remember { mutableFloatStateOf(0f) }

    LaunchedEffect(Unit) {
        remoteScreenViewModel.createSocket(ip, port)
        remoteScreenViewModel.startRepeatingJob(100)
        remoteScreenViewModel.updateSlidersRange(
            args.rangeLF,
            args.rangeLB,
            args.rangeRF,
            args.rangeRB
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.backgroundapp),
            contentScale = ContentScale.Crop,
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .rotate(-90f)
                ){
                    Slider(
                        value = throttleSliderPosition.floatValue,
                        onValueChange = { newVal: Float ->
                            throttleSliderPosition.floatValue = newVal
                            remoteScreenViewModel.updateSlidersInfo(SliderId.THROTTLE, newVal)
                        },
                        onValueChangeFinished = {
                            throttleSliderPosition.floatValue = 0f
                            remoteScreenViewModel.updateSlidersInfo(SliderId.THROTTLE, 0f)
                        },
                        valueRange = -1f..1f,
                        thumb = {
                            Box(
                                modifier = Modifier,
                                contentAlignment = Alignment.Center
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(0.095f)
                                        .fillMaxHeight(0.55f)
                                        .background(Color(0xCC006080), CircleShape)
                                )

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(0.075f)
                                        .fillMaxHeight(0.5f)
                                        .background(Blue, CircleShape)
                                )
                            }
                        },
                        track = {
                            Box(
                                modifier = Modifier,
                                contentAlignment = Alignment.Center
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(0.43f)
                                        .fillMaxHeight(0.35f)
                                        .background(Color.White, CircleShape)
                                )

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(0.4f)
                                        .fillMaxHeight(0.3f)
                                        .background(Orange, CircleShape)
                                )
                            }
                        }
                    )
                }

                Slider(
                    value = steeringSliderPosition.floatValue,
                    onValueChange = { newVal: Float ->
                        steeringSliderPosition.floatValue = newVal
                        remoteScreenViewModel.updateSlidersInfo(SliderId.STEERING, newVal)
                    },
                    onValueChangeFinished = {
                        steeringSliderPosition.floatValue = 0f
                        remoteScreenViewModel.updateSlidersInfo(SliderId.STEERING, 0f)
                    },
                    valueRange = -1f..1f,
                    thumb = {
                        Box(
                            modifier = Modifier,
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(0.17f)
                                    .fillMaxHeight(0.5f)
                                    .background(Color(0xCC006080), CircleShape)
                            )

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(0.13f)
                                    .fillMaxHeight(0.45f)
                                    .background(Blue, CircleShape)
                            )
                        }
                    },
                    track = {
                        Box(
                            modifier = Modifier,
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(0.95f)
                                    .fillMaxHeight(0.32f)
                                    .background(Color.White, CircleShape)
                            )

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(0.9f)
                                    .fillMaxHeight(0.27f)
                                    .background(Orange, CircleShape)
                            )
                        }
                    }
                )
            }
        }
    }
}

@Serializable
data class RemoteScreen(
    val ip: String,
    val port: String,
    val rangeLF: Float,
    val rangeLB: Float,
    val rangeRF: Float,
    val rangeRB: Float
)