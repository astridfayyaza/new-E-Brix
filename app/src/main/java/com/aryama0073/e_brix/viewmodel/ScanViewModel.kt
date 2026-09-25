package com.aryama0073.e_brix.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aryama0073.e_brix.data.ScanData
import com.aryama0073.e_brix.network.ApiService
import com.aryama0073.e_brix.network.toDomain
import com.aryama0073.e_brix.network.toDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ScanViewModel : ViewModel() {

    private val apiService = ApiService.create()

    private val _dataList = MutableStateFlow<List<ScanData>>(emptyList())
    val dataList: StateFlow<List<ScanData>> = _dataList

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    init {
        fetchScansFromDatabase()
    }

    fun fetchScansFromDatabase() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val response = apiService.getAllScans()
                if (response.isSuccessful) {
                    val dtos = response.body() ?: emptyList()
                    _dataList.value = dtos.map { it.toDomain() }
                } else {
                    _errorMessage.value = "Gagal mengambil data dari server (${response.code()})"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error koneksi: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addData(data: ScanData, onResult: (Boolean) -> Unit = {}) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val dto = data.toDto()
                val response = apiService.createScan(dto)
                if (response.isSuccessful) {
                    fetchScansFromDatabase()
                    onResult(true)
                } else {
                    _errorMessage.value = "Gagal menyimpan data (${response.code()})"
                    onResult(false)
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error koneksi: ${e.localizedMessage}"
                onResult(false)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateData(data: ScanData, onResult: (Boolean) -> Unit = {}) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val dto = data.toDto()
                val response = apiService.updateScan(data.id, dto)
                if (response.isSuccessful) {
                    fetchScansFromDatabase()
                    onResult(true)
                } else {
                    _errorMessage.value = "Gagal memperbarui data (${response.code()})"
                    onResult(false)
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error koneksi: ${e.localizedMessage}"
                onResult(false)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteData(id: Int, onResult: (Boolean) -> Unit = {}) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val response = apiService.deleteScan(id)
                if (response.isSuccessful) {
                    fetchScansFromDatabase()
                    onResult(true)
                } else {
                    _errorMessage.value = "Gagal menghapus data (${response.code()})"
                    onResult(false)
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error koneksi: ${e.localizedMessage}"
                onResult(false)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getDataById(id: Int): ScanData? {
        return _dataList.value.find { it.id == id }
    }
}
