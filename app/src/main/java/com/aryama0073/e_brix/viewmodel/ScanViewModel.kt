package com.aryama0073.e_brix.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.aryama0073.e_brix.data.ScanData
import com.aryama0073.e_brix.network.ApiService
import com.aryama0073.e_brix.network.toDomain
import com.aryama0073.e_brix.network.toDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ScanViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = application.getSharedPreferences("ebrix_jenis_tebu_cache", Context.MODE_PRIVATE)
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

    private fun getCachedJenisTebu(id: Int): String? {
        return prefs.getString("jenis_$id", null)
    }

    private fun saveCachedJenisTebu(id: Int, jenis: String) {
        if (id != 0 && jenis.isNotBlank()) {
            prefs.edit().putString("jenis_$id", jenis).apply()
        }
    }

    fun fetchScansFromDatabase() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val response = apiService.getAllScans()
                if (response.isSuccessful) {
                    val dtos = response.body() ?: emptyList()
                    _dataList.value = dtos.map { dto ->
                        val domain = dto.toDomain()
                        val cachedJenis = getCachedJenisTebu(domain.id)
                        if (!cachedJenis.isNullOrBlank()) {
                            domain.copy(jenisTebu = cachedJenis)
                        } else if (domain.jenisTebu.isBlank()) {
                            domain.copy(jenisTebu = "Bululawang (BL)")
                        } else {
                            domain
                        }
                    }
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
                    val savedItem = response.body()?.toDomain() ?: data
                    val finalJenis = if (data.jenisTebu.isNotBlank()) data.jenisTebu else savedItem.jenisTebu
                    val finalItem = savedItem.copy(jenisTebu = finalJenis)

                    saveCachedJenisTebu(finalItem.id, finalJenis)

                    _dataList.value = listOf(finalItem) + _dataList.value.filter { it.id != finalItem.id }
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

                // Simpan jenis tebu pilihan user ke cache
                saveCachedJenisTebu(data.id, data.jenisTebu)

                val response = apiService.updateScan(data.id, dto)

                if (response.isSuccessful) {
                    val updatedItem = response.body()?.toDomain() ?: data
                    val finalItem = updatedItem.copy(jenisTebu = data.jenisTebu)

                    _dataList.value = _dataList.value.map { if (it.id == data.id) finalItem else it }
                    fetchScansFromDatabase()
                    onResult(true)
                } else {
                    _dataList.value = _dataList.value.map { if (it.id == data.id) data else it }
                    _errorMessage.value = "Gagal memperbarui data (${response.code()})"
                    fetchScansFromDatabase()
                    onResult(false)
                }
            } catch (e: Exception) {
                _dataList.value = _dataList.value.map { if (it.id == data.id) data else it }
                _errorMessage.value = "Error koneksi: ${e.localizedMessage}"
                fetchScansFromDatabase()
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
                prefs.edit().remove("jenis_$id").apply()
                val response = apiService.deleteScan(id)
                if (response.isSuccessful) {
                    _dataList.value = _dataList.value.filter { it.id != id }
                    fetchScansFromDatabase()
                    onResult(true)
                } else {
                    _errorMessage.value = "Gagal menghapus data (${response.code()})"
                    fetchScansFromDatabase()
                    onResult(false)
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error koneksi: ${e.localizedMessage}"
                fetchScansFromDatabase()
                onResult(false)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getDataById(id: Int): ScanData? {
        val found = _dataList.value.find { it.id == id } ?: return null
        val cached = getCachedJenisTebu(found.id)
        return if (!cached.isNullOrBlank()) {
            found.copy(jenisTebu = cached)
        } else {
            found
        }
    }
}
