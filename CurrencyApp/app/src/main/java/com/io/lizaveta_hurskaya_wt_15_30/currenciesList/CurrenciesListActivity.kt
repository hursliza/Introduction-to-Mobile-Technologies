package com.io.lizaveta_hurskaya_wt_15_30.currenciesList

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.io.lizaveta_hurskaya_wt_15_30.R
import com.io.lizaveta_hurskaya_wt_15_30.data.DataHolder
import com.io.lizaveta_hurskaya_wt_15_30.data.DataHolder.getFlag
import com.io.lizaveta_hurskaya_wt_15_30.model.Currency
import org.json.JSONArray

class CurrenciesListActivity : AppCompatActivity() {
    internal lateinit var currenciesRecyclerView: RecyclerView
    internal lateinit var adapter: CurrenciesListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.currencies_list)
        currenciesRecyclerView = findViewById(R.id.currencies_recycler_view)
        currenciesRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter = CurrenciesListAdapter(applicationContext, emptyArray())
        currenciesRecyclerView.adapter = adapter
        updateCurrenciesList()
    }

    fun updateCurrenciesList(){
        val queue = DataHolder.queue

        var url = "http://api.nbp.pl/api/exchangerates/tables/a/last/2?format=json"

        var currencyRatesRequest = JsonArrayRequest(Request.Method.GET, url, null,
            { response ->
                adapter.dataSet = loadData(response)
                adapter.notifyDataSetChanged()
            },

            { error ->
                Toast.makeText(this, "Error: " + error.message, Toast.LENGTH_SHORT).show()
            })

        queue.add(currencyRatesRequest)

        url = "http://api.nbp.pl/api/exchangerates/tables/b/last/2?format=json"

        currencyRatesRequest = JsonArrayRequest(Request.Method.GET, url, null,
            { response ->
                adapter.dataSet += loadData(response)
                adapter.notifyDataSetChanged()
            },

            { error ->
                Toast.makeText(this, "Error: " + error.message, Toast.LENGTH_SHORT).show()
            })

        queue.add(currencyRatesRequest)
    }

    private fun loadData(response: JSONArray?): Array<Currency> {
        response?.let{
            val table = response.getJSONObject(1).getString("table")
            val rates = response.getJSONObject(1).getJSONArray("rates")
            val lastRates = response.getJSONObject(0).getJSONArray("rates")
            val ratesCount = rates.length()
            val tmpData = arrayOfNulls<Currency>(ratesCount)
            for (i in 0 until ratesCount){
                val currencyCode = rates.getJSONObject(i).getString("code")
                val currencyRate = rates.getJSONObject(i).getDouble("mid")
                val currencyDescription = rates.getJSONObject(i).getString("currency")
                val currencyFlag = getFlag(currencyCode)
                var currencyGrowth = true
                if (lastRates.getJSONObject(i).getDouble("mid") > currencyRate){
                    currencyGrowth = false
                }
                tmpData[i] = Currency(table, currencyCode, currencyRate,
                    currencyDescription, currencyFlag, currencyGrowth)
            }
            return tmpData as Array<Currency>
        }

        return emptyArray()
    }
}
