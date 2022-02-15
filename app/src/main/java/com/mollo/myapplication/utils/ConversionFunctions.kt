package com.mollo.myapplication.utils

import com.mollo.myapplication.model.Rates

fun getRate(rateType:String,data: Rates):Double{
    val result = when (rateType){
         "AED"-> data.AED
         "AUD" -> data.AUD
         "BTC" -> data.BTC
         "CAD" -> data.CAD
         "CZK" -> data.CZK
         "EGP" ->  data.EGP
         "EUR" -> data.EUR
         "GBP" -> data.GBP
         "NGN" -> data.NGN
         "NZD" -> data.NZD
         "USD" -> data.USD
        else -> 0.0
    }
    return result
}
fun convertAnyToEuro(currencyToConvertToEuro:Double,CurrencyRate:Double):Double{
return currencyToConvertToEuro/CurrencyRate
}
fun convertEuroToAny(resultOfConvertToEuro:Double,CurrencyRate:Double):Double{
    return resultOfConvertToEuro * CurrencyRate
}