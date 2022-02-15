package com.mollo.myapplication.model

data class ConvertionRate(
    val base: String,
    val date: String,
    val rates: Rates,
    val success: Boolean,
    val timestamp: Int
)