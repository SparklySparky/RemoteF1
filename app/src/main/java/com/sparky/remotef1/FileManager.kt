package com.sparky.remotef1

import android.content.Context
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import java.io.File

private const val DIRECTORY = "Configs"

class FileManager(private val context: Context) {

    fun saveConfig(config: ConfigType): List<ConfigType> {
        val directory = context.createDirectory()
        val file = File(directory, config.fileName)
        file.writeText(config.config)

        return getConfigs()
    }

    fun deleteConfig(fileName: String): List<ConfigType> {
        val directory = context.createDirectory()
        val file = File(directory, fileName)
        if (file.exists()) {
            file.delete()
        }

        return getConfigs()
    }

    fun updateConfig(config: ConfigType): List<ConfigType> {
        val directory = context.createDirectory()
        val file = File(directory, config.fileName)
        if (file.exists()) {
            file.writeText(config.config)
        }

        return getConfigs()
    }

    fun getConfigs(): List<ConfigType> {
        val directory = context.createDirectory()
        val files = directory.listFiles()
        val configs = mutableListOf<ConfigType>()
        files?.forEach {
            val config = ConfigType(it.name, it.readText())
            configs.add(config)
        }
        return configs
    }

    fun getConfigFlow() = callbackFlow<List<ConfigType>> {
        trySend(getConfigs())
        awaitClose {  }
    }

    fun Context.createDirectory(): File{
        val directory = getExternalFilesDir(null)
        val file = File(directory, DIRECTORY)
        if (!file.exists()) {
            file.mkdir()
        }
        return file
    }
}