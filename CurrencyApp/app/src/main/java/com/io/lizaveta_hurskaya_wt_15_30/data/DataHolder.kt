package com.io.lizaveta_hurskaya_wt_15_30.data

import android.content.Context
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley.newRequestQueue
import com.blongho.country_data.Country
import com.blongho.country_data.World
import com.io.lizaveta_hurskaya_wt_15_30.R
import com.io.lizaveta_hurskaya_wt_15_30.model.Currency
import org.json.JSONArray

object DataHolder {
    lateinit var queue: RequestQueue
    lateinit var countries: List<Country>
    fun prepare(context: Context){
        queue = newRequestQueue(context)
        World.init(context)
        countries = World.getAllCountries().distinctBy { it.currency.code }
    }

    fun getCurrencyList(): ArrayList<Pair<String, String>>{
        var array: ArrayList<Pair<String, String>> = ArrayList<Pair<String, String>>()

        for ((index, element) in World.getAllCountries().distinctBy { it.currency.code }.withIndex()){
            array.add(Pair(element.currency.code, element.currency.name))
        }

        return array
    }

    fun getFlag(countryCode: String) : Int {

        if (countryCode == "USD") return R.drawable.us
        if (countryCode == "EUR") return R.drawable.eu
        if (countryCode == "HKD") return R.drawable.hk
        if (countryCode == "GBP") return R.drawable.gb
        if (countryCode == "BYN") return R.drawable.by
        if (countryCode == "CHF") return  R.drawable.ch

        return countries.find { it.currency.code == countryCode }?.flagResource ?: World.getWorldFlag()
    }

    fun getTable(currencyCode: String): String{
        var table = arrayOfNulls<String>(1)
        var url = "http://api.nbp.pl/api/exchangerates/tables/a?format=json"

        var currencyRatesRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                table = extractTable(response)
            },

            { error ->
                print(error.message)
            })

        queue.add(currencyRatesRequest)

        return if (currencyCode in table) "a" else "b"
    }

    private fun extractTable(response: JSONArray): Array<String?>{
        response?.let{
            val table = response.getJSONObject(0).getString("table")
            val rates = response.getJSONObject(0).getJSONArray("rates")
            val ratesCount = rates.length()
            val tmpData = arrayOfNulls<String>(ratesCount)
            for (i in 0 until ratesCount){
                val currencyCode = rates.getJSONObject(i).getString("code")
                tmpData[i] = currencyCode
            }
            return tmpData
        }

        return emptyArray()
    }
}