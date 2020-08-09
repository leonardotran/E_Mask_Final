package com.vietnamese.maskregion.ui.home

import android.bluetooth.BluetoothAdapter
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    init {
        Log.i("ViewModel","HomeViewModel")
    }
    private var bluetoothName = ""
    fun setBluetoothName(name: String) {
        bluetoothName = name
    }

    private val _text = MutableLiveData<String>().apply {
        value = "Stopped"
    }
    var text: LiveData<String> = _text

    fun setStatus(status: String) {
        _text.value = status
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("GameViewModel", "GameViewModel destroyed!")
//        BluetoothAdapter.getDefaultAdapter().name = bluetoothName


    }
}