package com.sparky.remotef1.screens.remote

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.MutableState
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.sparky.remotef1.Cobs
import com.sparky.remotef1.UDPClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.text.toInt

enum class SliderId {
    THROTTLE,
    STEERING
}

data class MessageInfo(
    @SerializedName("t") var t: Float? = null,
    @SerializedName("s") var s: Float? = null,
)

data class SlidersRange(
    var lf: Float = 1f,
    var lb: Float = 1f,
    var rf: Float = 1f,
    var rb: Float = 1f,
)

fun Float.roundTo(places: Int): Float {
    if (places <= 0) { throw IllegalArgumentException("The number of places must be positive") }

    return this.times(10f.pow(places)).roundToInt() / (10f.pow(places))
}

class RemoteScreenViewModel: ViewModel() {
    private var throttleSliderValue = 0f
    private var steeringSliderValue = 0f

    private val _UDPSocket = mutableStateOf(null as UDPClient?)
    val UDPSocket: MutableState<UDPClient?> get() = _UDPSocket

    private val _messageOld = mutableStateOf(MessageInfo())
    val messageOld: MutableState<MessageInfo> get() = _messageOld

    private val _slidersRange = mutableStateOf(SlidersRange())
    val slidersRange: MutableState<SlidersRange> get() = _slidersRange

    private var job: Job? = null

    fun updateSlidersRange(rangeLF: Float, rangeLB: Float, rangeRF: Float, rangeRB: Float)
    {
        _slidersRange.value = SlidersRange(rangeLF, rangeLB, rangeRF, rangeRB)
    }

    fun createSocket(ip: String, port: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _UDPSocket.value = UDPClient(ip, port.toInt())
                Log.d("RemoteViewModel", "UDP Socket created successfully.")
            } catch (e: Exception) {
                Log.e("RemoteViewModel", "Error creating UDP socket: ${e.localizedMessage}")
            }
        }
    }

    fun updateSlidersInfo(sliderId: SliderId, newValue: Float)
    {
        viewModelScope.launch {
            when (sliderId) {
                SliderId.THROTTLE -> throttleSliderValue = newValue
                SliderId.STEERING -> steeringSliderValue = newValue
            }
        }
    }

    fun closeSocket() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _UDPSocket.value?.close()
                Log.d("RemoteViewModel", "UDP Socket closed successfully.")
            } catch (e: Exception) {
                Log.e("RemoteViewModel", "Error closing socket: ${e.localizedMessage}")
            }
        }
    }

    fun startRepeatingJob(timeInterval: Long) {
        job?.cancel()
        job = viewModelScope.launch(Dispatchers.Default) {
            while (isActive) {
                var t = throttleSliderValue.roundTo(3)
                var s = steeringSliderValue.roundTo(3)
                if(t > 0){
                    t = (t * slidersRange.value.lf).roundTo(3)
                } else if(t < 0){
                    t = (t * slidersRange.value.lb).roundTo(3)
                }

                if(s > 0){
                    s = (s * slidersRange.value.rf).roundTo(3)
                } else if(s < 0){
                    s = (s * slidersRange.value.rb).roundTo(3)
                }

                Log.d("Sliders", "Throttle: $t, Steering: $s")

                val messageOut = MessageInfo()

                val messageRaw = MessageInfo(t, s)

                if(messageRaw.t != messageOld.value.t) {
                    messageOut.t = messageRaw.t
                }

                if(messageRaw.s != messageOld.value.s) {
                    messageOut.s = messageRaw.s
                }

                val messageBytes = Gson().toJson(messageOut).toString().toCharArray()
                val encodedMessageBytes = CharArray(Cobs.encodeDstBufMaxLen(messageBytes.size) + 1)
                val res = Cobs.encode(encodedMessageBytes, encodedMessageBytes.size - 1, messageBytes, messageBytes.size)

                encodedMessageBytes[res.outLen] = 0x00.toChar()
                val finalMessage = encodedMessageBytes.copyOfRange(0, res.outLen + 1)

                val finalMessageBytes = finalMessage.map { it.code.toByte() }.toByteArray()

                UDPSocket.value?.send(finalMessageBytes)

                _messageOld.value = messageRaw

                delay(timeInterval)
            }
        }
    }

    fun stopRepeatingJob() {
        viewModelScope.launch(Dispatchers.Default) {
            try {
                job?.cancelAndJoin()
                job = null
                Log.d("RemoteViewModel", "Stopped job successfully.")
            } catch (e: Exception) {
                Log.e("RemoteViewModel", "Error stopping job: ${e.localizedMessage}")
            }
        }
    }
}