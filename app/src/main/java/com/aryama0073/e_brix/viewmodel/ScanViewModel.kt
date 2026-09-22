package com.aryama0073.e_brix.viewmodel

import androidx.lifecycle.ViewModel
import com.aryama0073.e_brix.data.ScanData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ScanViewModel : ViewModel() {

    private val _dataList = MutableStateFlow<List<ScanData>>(emptyList())
    val dataList: StateFlow<List<ScanData>> = _dataList

    fun addData(data: ScanData) {
        _dataList.value = _dataList.value + data
    }

    fun getDataById(id: Int): ScanData? {
        return _dataList.value.find { it.id == id }
    }
}