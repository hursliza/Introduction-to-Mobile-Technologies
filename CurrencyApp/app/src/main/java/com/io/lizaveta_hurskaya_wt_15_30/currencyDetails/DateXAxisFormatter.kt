package com.io.lizaveta_hurskaya_wt_15_30.currencyDetails

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter

class DateXAxisFormatter(var data: Array<Pair<String, Double>>) : ValueFormatter() {
    val dates = data.map { it.first }

    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        return dates.getOrNull(value.toInt()) ?: value.toString()
    }

}