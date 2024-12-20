package com.sparky.remotef1

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.sparky.remotef1.screens.configs.ConfigsScreen
import com.sparky.remotef1.screens.configs.ConfigsScreenViewModel
import com.sparky.remotef1.screens.remote.RemoteScreen
import com.sparky.remotef1.screens.remote.RemoteScreenViewModel
import com.sparky.remotef1.screens.settings.SettingsScreen

@Composable
fun RemoteF1App(
    remoteScreenViewModel: RemoteScreenViewModel,
    fileManager: FileManager
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = SettingsScreen
    ) {
        composable<SettingsScreen> {
            remoteScreenViewModel.stopRepeatingJob()
            remoteScreenViewModel.closeSocket()
            SettingsScreen(navController, fileManager)
        }
        composable<RemoteScreen> {
            val args = it.toRoute<RemoteScreen>()
            RemoteScreen(args, remoteScreenViewModel)
        }
        composable<ConfigsScreen> {
            ConfigsScreen(navController, fileManager)
        }
    }
}