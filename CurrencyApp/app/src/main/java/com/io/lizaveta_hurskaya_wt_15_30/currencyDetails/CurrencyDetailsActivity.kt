package com.io.lizaveta_hurskaya_wt_15_30.currencyDetails

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.github.mikephil.charting.charts.LineChart
import com.io.lizaveta_hurskaya_wt_15_30.R
import com.io.lizaveta_hurskaya_wt_15_30.data.DataHolder


import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import org.json.JSONArray
import org.json.JSONObject

class CurrencyDetailsActivity : AppCompatActivity() {
    internal lateinit var flag: ImageView
    internal lateinit var description: TextView
    internal lateinit var code: TextView
    internal lateinit var todayRate: TextView
    internal lateinit var yesterdayRate: TextView
    internal lateinit var monthlyRatesChart: LineChart
    internal lateinit var weeklyRatesChart: LineChart
    lateinit var currencyCode: String
    lateinit var currencyTable: String
    lateinit var currencyRates: Array<Pair<String, Double>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.currency_details_activity)
        currencyCode = intent.getStringExtra("currencyCode")!!
        currencyTable = intent.getStringExtra("table")!!
        monthlyRatesChart = findViewById(R.id.monthly_chart)
        weeklyRatesChart = findViewById(R.id.weekly_chart)
        todayRate = findViewById(R.id.today_rate)
        yesterdayRate = findViewById(R.id.yesterday_rate)
        flag = findViewById(R.id.details_flag)
        code = findViewById(R.id.details_code)
        description = findViewById(R.id.details_name)
        getCurrencyDetails(currencyCode)
    }

    private fun getCurrencyDetails(code: String){
        val queue = DataHolder.queue
        var url = ""

        if (currencyCode == "gold"){
            url = "http://api.nbp.pl/api/cenyzlota/last/30"

            val request = JsonArrayRequest(Request.Method.GET, url, null,
                { response ->
                    currencyRates = loadDataGold(response)
                    showData()
                },
                { error ->
                    Toast.makeText(this, "Error: " + error.message, Toast.LENGTH_SHORT).show()
                })

            queue.add(request)

        } else {
            url = "http://api.nbp.pl/api/exchangerates/rates/%s/%s/last/30/".format(
                currencyTable,
                currencyCode
            )

            val request = JsonObjectRequest(Request.Method.GET, url, null,
                { response ->
                    currencyRates = loadData(response)
                    showData()
                },
                { error ->
                    Toast.makeText(this, "Error: " + error.message, Toast.LENGTH_SHORT).show()
                })

            queue.add(request)
        }

    }


    private fun showData() {
        if (currencyCode == "gold"){
            code.text = ""
            flag.setImageResource(R.drawable.gold_icon)
        }
        else{
            code.text = currencyCode
            flag.setImageResource(DataHolder.getFlag(currencyCode))
        }
        todayRate.text = getString(R.string.rate_today, currencyRates.last().second)
        yesterdayRate.text = getString(R.string.rate_yesterday, currencyRates[currencyRates.size - 2].second)
        showMonthlyChart()
        showWeeklyChart()
    }

    private fun showMonthlyChart() {
        monthlyRatesChart.legend.isEnabled = false
        monthlyRatesChart.description.isEnabled = false
        monthlyRatesChart.xAxis.labelRotationAngle = -45f
        monthlyRatesChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        monthlyRatesChart.xAxis.axisMinimum = 0f
        monthlyRatesChart.extraBottomOffset = 45f

        var entries = ArrayList<Entry>()
        for ((index, element) in currencyRates.withIndex()){
            entries.add(Entry(index.toFloat(), element.second.toFloat()))
        }

        val lineDataSet = LineDataSet(entries, "Kursy przez ostatnie 30 dni")
        lineDataSet.setColor(R.color.colorPrimary)
        lineDataSet.setDrawValues(false)
        lineDataSet.setDrawCircles(false)
        monthlyRatesChart.data = LineData(lineDataSet)

        monthlyRatesChart.xAxis.valueFormatter = DateXAxisFormatter(currencyRates)
        monthlyRatesChart.invalidate()
    }

    private fun showWeeklyChart() {
        weeklyRatesChart.legend.isEnabled = false
        weeklyRatesChart.description.isEnabled = false
        weeklyRatesChart.xAxis.labelRotationAngle = -45f
        weeklyRatesChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        weeklyRatesChart.xAxis.axisMinimum = 0f
        weeklyRatesChart.extraBottomOffset = 45f

        var entries = ArrayList<Entry>()
        for ((index, element) in currencyRates
            .slice(IntRange(currencyRates.size-7, currencyRates.size-1)).withIndex()){
            entries.add(Entry(index.toFloat(), element.second.toFloat()))
        }

        val lineDataSet = LineDataSet(entries, "Kursy przez ostatnie 30 dni")
        lineDataSet.setColor(R.color.colorPrimary)
        lineDataSet.setDrawValues(false)
        lineDataSet.setDrawCircles(false)
        weeklyRatesChart.data = LineData(lineDataSet)

        weeklyRatesChart.xAxis.valueFormatter = DateXAxisFormatter(currencyRates)
        weeklyRatesChart.invalidate()
    }

    private fun loadData(response: JSONObject?): Array<Pair<String, Double>> {
        response?.let{
            val rates = response.getJSONArray("rates")
            val ratesCount = rates.length()
            val tmpData = arrayOfNulls<Pair<String, Double>>(ratesCount)
            description.text =response.getString("currency")

            for (i in 0 until ratesCount){
                val date = rates.getJSONObject(i).getString("effectiveDate")
                val rate = rates.getJSONObject(i).getDouble("mid")
                tmpData[i] = Pair(date, rate)
            }
            return tmpData as Array<Pair<String, Double>>
        }

        return emptyArray()
    }

    private fun loadDataGold(response: JSONArray?): Array<Pair<String, Double>>{
        response?.let{
            val ratesCount = response.length()
            val tmpData = arrayOfNulls<Pair<String, Double>>(ratesCount)
            description.text = "Złoto"

            for (i in 0 until ratesCount){
                val date = response.getJSONObject(i).getString("data")
                val rate = response.getJSONObject(i).getDouble("cena")
                tmpData[i] = Pair(date, rate)
            }
            return tmpData as Array<Pair<String, Double>>
        }

        return emptyArray()
    }
}