package com.mollo.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContentProviderCompat.requireContext
import com.mollo.myapplication.databinding.ActivityMainBinding
import com.mollo.myapplication.utils.ACCESS_KEY
import com.mollo.myapplication.utils.ApiCallNetworkResource
import com.mollo.myapplication.utils.ListOfCountry
import com.mollo.myapplication.viewmodel.ConversionServiceViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel:ConversionServiceViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpCurrency("USD","NGN")
        setUpAutoCompleteTextView()






        Log.d("conversionRate", "onCreate: working")
        viewModel.getAllRates(ACCESS_KEY)
        viewModel.conversionRates.observe(this) {
            when (it) {
                is ApiCallNetworkResource.Loading ->{
                    Toast.makeText(this,"loading...",Toast.LENGTH_LONG).show()

                }
                is ApiCallNetworkResource.Success ->{
                    Toast.makeText(this,it.data?.rates?.AED.toString(),Toast.LENGTH_LONG).show()
                    Log.d("conversionRate", "onCreate: ${it.data?.rates}")
                }
                is ApiCallNetworkResource.Error ->{
                    Log.d("conversionRate", it.message.toString())

                }

            }
        }
    }

    private fun setUpAutoCompleteTextView() {
        val adapter = ArrayAdapter(this, R.layout.list_item, ListOfCountry)
        (binding.dropDownFirstCurrency.editText as? AutoCompleteTextView)?.setAdapter(adapter)
        (binding.dropDownSecondCurrency.editText as? AutoCompleteTextView)?.setAdapter(adapter)

        (binding.dropDownFirstCurrency.editText as? AutoCompleteTextView)?.setOnItemClickListener { adapterView, view, i, l ->
            setUpCurrency(firstCurrency = adapterView.getItemAtPosition(i).toString())
            (binding.textFieldSecondCurrency as? EditText)?.setText("kkkkkk")
        }
        (binding.dropDownSecondCurrency.editText as? AutoCompleteTextView)?.setOnItemClickListener { adapterView, view, i, l ->
            setUpCurrency(secondCurrency = adapterView.getItemAtPosition(i).toString())

        }
    }

    private fun setUpCurrency(firstCurrency: String? = null, secondCurrency: String?= null) {
        if (firstCurrency != null){
            binding.textFieldFirstCurrency.suffixText = firstCurrency
            binding.textFieldFirstCurrency.hint = firstCurrency
        }
       if (secondCurrency != null){
           binding.textFieldSecondCurrency.suffixText = secondCurrency
           binding.textFieldSecondCurrency.hint = secondCurrency
       }
    }
}