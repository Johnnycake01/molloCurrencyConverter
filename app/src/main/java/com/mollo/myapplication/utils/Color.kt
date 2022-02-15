package com.mollo.myapplication.utils

import android.content.Context

import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.mollo.myapplication.R

class Color(var view:TextView, var view2:CardView, var context:Context) {
    fun faintColor(){
        view.setTextColor(ContextCompat.getColor(context, R.color.translucent))
        view2.setCardBackgroundColor(ContextCompat.getColor(context, R.color.teal_700))
    }
    fun resetColor(){
        view.setTextColor(ContextCompat.getColor(context, R.color.white))
        view2.setCardBackgroundColor(ContextCompat.getColor(context, R.color.appColorGreen))
    }
}