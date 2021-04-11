package com.io.lizaveta_hurskaya_wt_15_30.converter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.io.lizaveta_hurskaya_wt_15_30.R
import com.io.lizaveta_hurskaya_wt_15_30.data.DataHolder
import com.io.lizaveta_hurskaya_wt_15_30.model.Currency
import org.json.JSONArray
import org.json.JSONObject

class ConverterActivity : AppCompatActivity() {
    internal lateinit var spinner: Spinner
    internal lateinit var textField1: TextInputEditText
    internal lateinit var textField2: TextInputEditText
    internal lateinit var currencyCode: String
    internal lateinit var fab: FloatingActionButton
    internal var firstEditedLast: Boolean? = null
    internal var rate: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.converter_layout)
        spinner = findViewById(R.id.spinner)
        initSpinner()
        textField1 = findViewById(R.id.text_field_1)
        textField2 = findViewById(R.id.text_field_2)
        fab = findViewById(R.id.floatingActionButton)
        fab.isEnabled = false
        fab.setOnClickListener(fabOnClickListener())
        textField1.addTextChangedListener(modifiedWatcher("1"))
        textField2.addTextChangedListener(modifiedWatcher("2"))
    }

    private fun modifiedWatcher(id: String): TextWatcher? {
        return object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                }
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    fab.isEnabled = true
                    firstEditedLast = id == "1"
                }
        }
    }

    private fun initSpinner(){
        val spinnerItems = DataHolder.getCurrencyList().map { "${it.first}: ${it.second}" }

        val adapter = ArrayAdapter(this, R.layout.spinner_item, spinnerItems)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) {
                currencyCode = DataHolder.getCurrencyList()[position].first
                getRate(currencyCode, currencyTable = DataHolder.getTable(currencyCode) )
                (view as TextView).setTextColor(resources.getColor(R.color.colorPrimaryDark))
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                fab.isEnabled = false
            }
        }
    }

    private fun fabOnClickListener(): View.OnClickListener? {
        return View.OnClickListener{
            if (firstEditedLast!!){
                textField2.setText((rate * textField1.text.toString().toDouble()).toString())
            } else{
                textField1.setText((textField2.text.toString().toDouble() / rate).toString())
            }

            fab.isEnabled = false
        }
    }

    fun getRate(currencyCode: String, currencyTable: String) {
        val queue = DataHolder.queue

        var url = "http://api.nbp.pl/api/exchangerates/rates/%s/%s?format=json".format(currencyTable, currencyCode)

        var currencyRatesRequest = JsonObjectRequest(Request.Method.GET, url, null,
            { response ->
                rate = loadData(response)
            },

            { error ->
                Toast.makeText(this, "Error: " + error.message, Toast.LENGTH_SHORT).show()
            })

        queue.add(currencyRatesRequest)
    }

    private fun loadData(response: JSONObject): Double{
        response?.let{
            val rates = response.getJSONArray("rates").getJSONObject(0)
            return rates.getDouble("mid")
        }
    }

}