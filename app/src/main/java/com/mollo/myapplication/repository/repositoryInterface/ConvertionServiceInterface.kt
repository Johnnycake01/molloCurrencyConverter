package com.mollo.myapplication.repository.repositoryInterface

import com.mollo.myapplication.model.ConvertionRate
import retrofit2.Response

interface ConvertionServiceInterface{
     suspend fun getRates(accessKey: String): Response<ConvertionRate>
}