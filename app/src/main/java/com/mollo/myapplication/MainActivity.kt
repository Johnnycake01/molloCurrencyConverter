package com.mollo.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.activity.viewModels
import com.mollo.myapplication.databinding.ActivityMainBinding
import com.mollo.myapplication.model.Rates
import com.mollo.myapplication.utils.*
import com.mollo.myapplication.viewmodel.ConversionServiceViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel:ConversionServiceViewModel by viewModels()
    private var listOfRates:Rates? = null
    private var currency1 = "EUR"
    private var currency2 = "NGN"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpCurrency(currency1,currency2)
        setUpAutoCompleteTextView()
        binding.convertButton.setOnClickListener {
            binding.textinputError.visibility= View.GONE
            if (listOfRates == null){
                viewModel.getAllRates(ACCESS_KEY)
                Toast.makeText(this, "network error!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val firstCurrency = binding.textFieldFirstCurrency.editText?.text
            if(!firstCurrency.isNullOrBlank()){
                val resultOfConversion = Convert(binding.textFieldFirstCurrency.suffixText.toString()
                    ,firstCurrency.toString().toDouble(), currency2
                )
                "$resultOfConversion $currency2".also { binding.textFieldSecondCurrency.text = it }
            }else{
                Toast.makeText(this, "please enter the amount you want to Convert",
                    Toast.LENGTH_SHORT).show()
                binding.textinputError.visibility= View.VISIBLE

            }
        }

        viewModel.getAllRates(ACCESS_KEY)
        viewModel.conversionRates.observe(this) {
            when (it) {
                is ApiCallNetworkResource.Loading ->{
                    Toast.makeText(this,"loading...",Toast.LENGTH_LONG).show()

                }
                is ApiCallNetworkResource.Success ->{
                    listOfRates = it.data?.rates!!
                }
                is ApiCallNetworkResource.Error ->{

                }

            }
        }
    }

    private fun setUpAutoCompleteTextView() {

        val adapter = ArrayAdapter(this, R.layout.list_item, ListOfCountry)
        val dropDown1 = (binding.dropDownFirstCurrency.editText as? AutoCompleteTextView)
        val dropDown2 = (binding.dropDownSecondCurrency.editText as? AutoCompleteTextView)
        dropDown1?.setText(currency1)
        dropDown2?.setText(currency2)
        dropDown1?.setAdapter(adapter)
        dropDown2?.setAdapter(adapter)


        dropDown1?.setOnItemClickListener {
                adapterView, _, i, _ ->
            currency1 = adapterView.getItemAtPosition(i).toString()
            setUpCurrency(firstCurrency = adapterView.getItemAtPosition(i).toString())
        }
        dropDown2?.setOnItemClickListener {
                adapterView, _, i, _ ->
            currency2 = adapterView.getItemAtPosition(i).toString()
            setUpCurrency(secondCurrency = adapterView.getItemAtPosition(i).toString())

        }
    }

    private fun setUpCurrency(firstCurrency: String? = null, secondCurrency: String?= null) {
        if (firstCurrency != null){
            binding.textFieldFirstCurrency.suffixText = firstCurrency
            binding.textFieldFirstCurrency.hint = firstCurrency
        }
        secondCurrency?.also { binding.textFieldSecondCurrency.text = it }
    }
    private fun Convert(fromCurrency:String,fromCurrencyAmount:Double,toCurrencyType:String):Double{

       val intermediate = convertAnyToEuro(fromCurrencyAmount, getRate(fromCurrency, listOfRates!!))

        return convertEuroToAny(intermediate, getRate(toCurrencyType,listOfRates!!))
    }
}