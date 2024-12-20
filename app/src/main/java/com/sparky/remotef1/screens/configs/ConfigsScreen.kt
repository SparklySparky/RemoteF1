package com.sparky.remotef1.screens.configs

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.internal.composableLambdaNInstance
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.NavHostController
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.sparky.remotef1.ConfigContent
import com.sparky.remotef1.ConfigType
import com.sparky.remotef1.FileManager
import com.sparky.remotef1.R
import com.sparky.remotef1.screens.remote.RemoteScreenViewModel
import com.sparky.remotef1.ui.theme.BlueSexy
import kotlinx.coroutines.flow.collectLatest
import kotlinx.serialization.Serializable

@Composable
fun ConfigsScreen(
    navController: NavHostController,
    fileManager: FileManager
) {
    val nameField = remember { mutableStateOf("") }
    val ipAddrField = remember { mutableStateOf("") }
    val portField = remember { mutableStateOf("") }
    val rangeLeftForwardField = remember { mutableStateOf("") }
    val rangeLeftBackwardField = remember { mutableStateOf("") }
    val rangeRightForwardField = remember { mutableStateOf("") }
    val rangeRightBackwardField = remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Image(
            painter = painterResource(R.drawable.backgroundapp),
            contentScale = ContentScale.Crop,
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxWidth(0.75f)
                .fillMaxHeight(0.95f)
                .background(BlueSexy, CircleShape)
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                value = nameField.value,
                onValueChange = { newText: String -> nameField.value = newText },
                singleLine = true,
                label = { Text("Nome") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            OutlinedTextField(
                value = ipAddrField.value,
                onValueChange = { newText: String -> ipAddrField.value = newText },
                singleLine = true,
                label = { Text("Indirizzo IP") }
            )

            OutlinedTextField(
                value = portField.value,
                onValueChange = { newText: String -> portField.value = newText },
                singleLine = true,
                label = { Text("Porta") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Row {
                OutlinedTextField(
                    value = rangeLeftForwardField.value,
                    onValueChange = { newText: String -> rangeLeftForwardField.value = newText },
                    singleLine = true,
                    label = { Text("Range LF") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                OutlinedTextField(
                    value = rangeRightForwardField.value,
                    onValueChange = { newText: String -> rangeRightForwardField.value = newText },
                    singleLine = true,
                    label = { Text("Range RF") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }

            Row {
                OutlinedTextField(
                    value = rangeLeftBackwardField.value,
                    onValueChange = { newText: String -> rangeLeftBackwardField.value = newText },
                    singleLine = true,
                    label = { Text("Range LB") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                OutlinedTextField(
                    value = rangeRightBackwardField.value,
                    onValueChange = { newText: String -> rangeRightBackwardField.value = newText },
                    singleLine = true,
                    label = { Text("Range RB") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }

            Button(
                onClick = {
                    val config = ConfigContent(
                        ipAddrField.value,
                        portField.value,
                        rangeLeftForwardField.value.toFloat(),
                        rangeLeftBackwardField.value.toFloat(),
                        rangeRightForwardField.value.toFloat(),
                        rangeRightBackwardField.value.toFloat()
                    )

                    fileManager.saveConfig(ConfigType(nameField.value, Gson().toJson(config).toString()))

                    navController.popBackStack()
                },
                shape = RoundedCornerShape(20),
            ) {
                Text(
                    text = "Salva Configurazione",
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Serializable
object ConfigsScreen