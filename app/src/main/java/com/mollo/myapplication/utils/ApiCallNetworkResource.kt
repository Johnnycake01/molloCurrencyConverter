package com.mollo.myapplication.utils

sealed class ApiCallNetworkResource<T>(
    val data : T? = null,
    val message : String? = null
){
    class Success<T>(message: String,data: T? = null) : ApiCallNetworkResource<T>(data,message)
    class Error<T>(message: String, data: T? = null) : ApiCallNetworkResource<T>(data,message)
    class Loading<T>: ApiCallNetworkResource<T>()
}
