package com.sparky.remotef1.screens.settings

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.sparky.remotef1.ConfigContent
import com.sparky.remotef1.ConfigType
import com.sparky.remotef1.FileManager
import com.sparky.remotef1.R
import com.sparky.remotef1.screens.configs.ConfigsScreen
import com.sparky.remotef1.screens.remote.RemoteScreen
import com.sparky.remotef1.ui.theme.BlueSexy
import kotlinx.serialization.Serializable

@Composable
fun SettingsScreen(
    navController: NavHostController,
    fileManager: FileManager
) {
    val ipAddressInput = remember { mutableStateOf("") }
    val portInput = remember { mutableStateOf("") }
    val configsList = remember { mutableStateOf(fileManager.getConfigs()) }
    val selectedConfig = remember { mutableStateOf(ConfigContent("","",1f,1f,1f,1f)) }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Image(
            painter = painterResource(R.drawable.backgroundapp),
            contentScale = ContentScale.Crop,
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .fillMaxHeight(0.75f)
                .background(BlueSexy, CircleShape)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                OutlinedTextField(
                    value = ipAddressInput.value,
                    onValueChange = { newText: String -> ipAddressInput.value = newText },
                    singleLine = true,
                    label = { Text("Indirizzo IP") }
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = portInput.value,
                    onValueChange = { newText: String -> portInput.value = newText },
                    singleLine = true,
                    label = { Text("Porta") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = {
                        navController.navigate(
                            ConfigsScreen
                        )
                    },
                    shape = RoundedCornerShape(20),
                ) {
                    Text(
                        text = "Configura",
                        fontSize = 14.sp
                    )
                }

                Button(
                    onClick = {
                        navController.navigate(
                            RemoteScreen(
                                ip = ipAddressInput.value,
                                port = portInput.value,
                                rangeLF = selectedConfig.value.rangeLF,
                                rangeLB = selectedConfig.value.rangeLB,
                                rangeRF = selectedConfig.value.rangeRF,
                                rangeRB = selectedConfig.value.rangeRB
                            )
                        )
                    },
                    shape = RoundedCornerShape(20),
                ) {
                    Text(
                        text = "Conferma",
                        fontSize = 24.sp
                    )
                }
            }
        }
    }

    Box(){
        LazyColumn(
            modifier = Modifier.padding(5.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp),
        ){
            items(configsList.value.size) { index ->
                val config = fileManager.getConfigs()[index]
                val configContent = Gson().fromJson(config.config, ConfigContent::class.java)

                Column(
                    modifier = Modifier
                        .background(BlueSexy, CircleShape)
                        .padding(10.dp)
                        .clickable(onClick = {
                            ipAddressInput.value = configContent.ip
                            portInput.value = configContent.port
                            selectedConfig.value = configContent
                        }),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                )   {
                    Text(text = config.fileName)

                    Image(
                        painter = painterResource(R.drawable.heroicons_trash),
                        contentDescription = null,
                        modifier = Modifier
                            .clickable {
                                configsList.value = fileManager.deleteConfig(config.fileName)
                            }
                    )
                }
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopEnd
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Configurazione selezionata",
                fontSize = 24.sp
            )
            Row(){
                Text(
                    text = "Range LF: ${selectedConfig.value.rangeLF}",
                    fontSize = 24.sp
                )
            }
            Row(){
                Text(
                    text = "Range LB: ${selectedConfig.value.rangeLB}",
                    fontSize = 24.sp
                )
            }
            Row(){
                Text(
                    text = "Range RF: ${selectedConfig.value.rangeRF}",
                    fontSize = 24.sp
                )
            }
            Row(){
                Text(
                    text = "Range RB: ${selectedConfig.value.rangeRB}",
                    fontSize = 24.sp
                )
            }
        }
    }
}

@Serializable
object SettingsScreen