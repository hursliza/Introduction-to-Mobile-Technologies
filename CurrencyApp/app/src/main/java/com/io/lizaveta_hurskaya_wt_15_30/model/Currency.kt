package com.io.lizaveta_hurskaya_wt_15_30.model

data class Currency(
    var table: String,
    var currencyCode: String,
    var rate: Double,
    var description: String,
    var flag: Int = 0,
    var growth: Boolean = true
) {
}
