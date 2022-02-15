package com.mollo.myapplication.repository.repositoryImplementation

import com.mollo.myapplication.repository.repositoryInterface.ConvertionServiceInterface
import com.mollo.myapplication.model.ConvertionRate
import com.mollo.myapplication.services.ConvertionService
import retrofit2.Response
import javax.inject.Inject

class ConvertionServiceInterfaceImpl @Inject constructor(
    private val conversionService:ConvertionService
):ConvertionServiceInterface {
    override suspend fun getRates(accessKey: String): Response<ConvertionRate> {
        return  conversionService.getRates(accessKey)
    }
}