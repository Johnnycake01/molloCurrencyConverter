package com.mollo.myapplication.services

import com.mollo.myapplication.model.ConvertionRate
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ConvertionService {
    @GET("latest")
    suspend fun getRates(@Query("access_key") accessKey:String):Response<ConvertionRate>
}