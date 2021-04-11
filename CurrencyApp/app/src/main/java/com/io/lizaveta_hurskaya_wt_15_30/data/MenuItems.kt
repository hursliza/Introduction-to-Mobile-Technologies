package com.io.lizaveta_hurskaya_wt_15_30.data

import com.io.lizaveta_hurskaya_wt_15_30.R
import com.io.lizaveta_hurskaya_wt_15_30.model.MenuItem

fun menu(): List<MenuItem> {
    return listOf(
        MenuItem(0, "Lista walut", R.drawable.currencies),
        MenuItem(1, "Przelicznik walut", R.drawable.convert),
        MenuItem(2, "Złoto" , R.drawable.gold)
    )
}