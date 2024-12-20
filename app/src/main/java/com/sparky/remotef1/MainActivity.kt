package com.sparky.remotef1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.sparky.remotef1.screens.configs.ConfigsScreenViewModel
import com.sparky.remotef1.screens.remote.RemoteScreenViewModel
import com.sparky.remotef1.ui.theme.RemoteF1Theme

class MainActivity : ComponentActivity() {
    private val remoteScreenViewModel = RemoteScreenViewModel();
    private val fileManager by lazy { FileManager(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())

        setContent {
            RemoteF1Theme {
                RemoteF1App(remoteScreenViewModel, fileManager)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        remoteScreenViewModel.stopRepeatingJob()
        remoteScreenViewModel.closeSocket()
    }
}