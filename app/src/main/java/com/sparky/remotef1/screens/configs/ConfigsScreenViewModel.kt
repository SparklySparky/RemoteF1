package com.sparky.remotef1.screens.configs

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.gson.annotations.SerializedName
import com.sparky.remotef1.ConfigType

class ConfigsScreenViewModel: ViewModel() {
    private val _configsList = mutableStateOf(emptyList<ConfigType>())
    val configsList: MutableState<List<ConfigType>> get() = _configsList
}