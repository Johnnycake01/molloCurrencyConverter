package com.mollo.myapplication.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.broooapps.graphview.CurveGraphConfig
import com.broooapps.graphview.models.GraphData
import com.broooapps.graphview.models.PointMap
import com.mollo.myapplication.R
import com.mollo.myapplication.databinding.ActivityMainBinding
import com.mollo.myapplication.model.Rates
import com.mollo.myapplication.services.GetCurrentTime
import com.mollo.myapplication.utils.*
import com.mollo.myapplication.viewmodel.ConversionServiceViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), GetCurrentTime {
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
        toggleColorsTheme()
        submitButtonClickedToConvertCurrency()
        viewModel.getAllRates(ACCESS_KEY)
        observeNetworkCallResult()
        drawGraph()
        setUpWorkManager()
    }

    private fun setUpWorkManager() {
        val getCurrentTimeWorkRequest: WorkRequest =
            PeriodicWorkRequestBuilder<ConverterWorkManagerUpdateTime>(1,TimeUnit.MINUTES)
                .build()

        val workManager = WorkManager.getInstance(this)
        workManager.enqueue(getCurrentTimeWorkRequest)


    }

    private fun drawGraph() {
        binding.cgv.configure(
            CurveGraphConfig.Builder(this)
                .setAxisColor(R.color.appColorBlue)
                .setIntervalDisplayCount(10)
                .setNoDataMsg(" No Data ") // Message when no data is provided to the view.
                .setxAxisScaleTextColor(R.color.white) // Set X axis scale text color.
                .setyAxisScaleTextColor(R.color.white) // Set Y axis scale text color
                .setAnimationDuration(2000) // Set Animation Duration
                .build())

        val p2 =  PointMap()
        p2.addPoint(0, 350)
        p2.addPoint(1, 500)
        p2.addPoint(2, 750)
        p2.addPoint(3, 600)


        val gd2 = GraphData.builder(this)
            .setPointMap(p2)
            .setGraphStroke(R.color.appColorDarkBlue)
            .setGraphGradient(R.color.appColorDarkBlue, R.color.lightblue)
            .build();

         Handler(Looper.getMainLooper()).postDelayed({
                binding.cgv.setData(3, 1000, gd2);
        }, 250)
    }

    private fun observeNetworkCallResult() {
        viewModel.conversionRates.observe(this) {
            when (it) {
                is ApiCallNetworkResource.Loading ->{}
                is ApiCallNetworkResource.Success -> {
                    if (listOfRates == null){
                        Toast.makeText(this, "server not found", Toast.LENGTH_SHORT).show()
                    }else{
                        listOfRates = it.data?.rates!!
                    }
                }
                is ApiCallNetworkResource.Error ->{}
            }
        }
    }

    private fun submitButtonClickedToConvertCurrency() {
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

    }

    private fun toggleColorsTheme() {
        val colorTheme30days = Color(binding.txtPast30days,binding.dotPast30days,this)
        val colorTheme90days = Color(binding.txtPast90days,binding.dotPast90days,this)
        colorTheme30days.resetColor()
        colorTheme90days.faintColor()
        binding.L30days.setOnClickListener {
            colorTheme30days.resetColor()
            colorTheme90days.faintColor()
        }
        binding.L90days.setOnClickListener {
            colorTheme90days.resetColor()
            colorTheme30days.faintColor()
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

        binding.linkToViewCurrentExchangeRate.text = getString(R.string.link_to_view_exchange_rate,
            SimpleDateFormat("h:mm a").format(Date())
        )
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

    override fun getCurrentTime() {
        binding.linkToViewCurrentExchangeRate.text = getString(R.string.link_to_view_exchange_rate,
            SimpleDateFormat("h:mm a").format(Date())
        )
    }
}