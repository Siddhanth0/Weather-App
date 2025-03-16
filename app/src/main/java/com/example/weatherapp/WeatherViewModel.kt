package com.example.weatherapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.api.Constants.API_KEY
import com.example.weatherapp.api.NetworkResponse
import com.example.weatherapp.api.RetrofitInstance
import com.example.weatherapp.api.WeatherModel
import kotlinx.coroutines.launch

class WeatherViewModel: ViewModel() {
    private val weatherApi = RetrofitInstance.weatherApi
    private val _weatherResult = MutableLiveData<NetworkResponse<WeatherModel>>()
    val weatherResult : LiveData<NetworkResponse<WeatherModel>> = _weatherResult

    fun getData(city: String) {
        viewModelScope.launch {
            _weatherResult.value = NetworkResponse.Loading
            try {
                val response = weatherApi.getWeatherData(API_KEY, city)
                if (response.isSuccessful) {
                    response.body()?.let {
                        _weatherResult.value = NetworkResponse.Success(it)
                    }
                } else {
                    _weatherResult.value = NetworkResponse.Error("Failed to Load Data")
                }
            } catch (e: Exception) {
                _weatherResult.value = NetworkResponse.Error("Failed to Load Data")
            }
        }
    }

}