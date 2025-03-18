package com.trungkien.qlychitieu2

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    private val _soDu = MutableLiveData<Double>()
    val soDu: LiveData<Double> get() = _soDu

    fun updateSoDu(soDu: Double) {
        _soDu.value = soDu
    }
}