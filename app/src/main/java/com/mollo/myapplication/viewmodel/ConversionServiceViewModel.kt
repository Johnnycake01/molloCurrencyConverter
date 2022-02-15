package com.mollo.myapplication.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mollo.myapplication.repository.repositoryInterface.ConvertionServiceInterface
import com.mollo.myapplication.model.ConvertionRate
import com.mollo.myapplication.utils.ApiCallNetworkResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ConversionServiceViewModel @Inject constructor(
    private val conversionRepository:ConvertionServiceInterface
):ViewModel() {
    private val _conversionRates : MutableLiveData<ApiCallNetworkResource<ConvertionRate>> = MutableLiveData()
    val conversionRates : LiveData<ApiCallNetworkResource<ConvertionRate>> = _conversionRates

    fun getAllRates(accessKey:String){
        viewModelScope.launch(Dispatchers.IO){
            _conversionRates.postValue(ApiCallNetworkResource.Loading())
            try {
                delay(2000)
                Log.d("conversionRate", "getAllRates: before working ")
                val response = conversionRepository.getRates(accessKey)
                Log.d("conversionRate", "getAllRates:after working ")

                if (response.isSuccessful){
                    val responseBody =response.body()
                    _conversionRates.postValue(ApiCallNetworkResource.Success("successful",responseBody))

                }else{
                    _conversionRates.postValue(ApiCallNetworkResource.Error("unsuccessful",response.body()))

                }
            }catch (e: Throwable) {
                e.printStackTrace()
                when(e){
                    is IOException ->{
                        _conversionRates.postValue(ApiCallNetworkResource.Error(message =
                        e.localizedMessage))
                    }
                    else->{
                        _conversionRates.postValue(ApiCallNetworkResource.Error(message =
                        e.localizedMessage))
                    }
                }

        }
    }
}
}