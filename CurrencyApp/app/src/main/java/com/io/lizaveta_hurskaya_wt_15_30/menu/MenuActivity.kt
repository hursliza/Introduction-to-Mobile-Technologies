package com.io.lizaveta_hurskaya_wt_15_30.menu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.io.lizaveta_hurskaya_wt_15_30.R
import com.io.lizaveta_hurskaya_wt_15_30.data.DataHolder
import com.io.lizaveta_hurskaya_wt_15_30.data.menu

/*
Lizaveta Hurskaya, gr. wt 15:30
Zrealizowane:
    Obsługa obu tabel
    Lista walut, zawierająca
        flagę (poprawione niektóre błędne flagi z biblioteki WorldCountryData)
        kod waluty
        aktualny kurs
        strzałkę, która symbolizuje wzrost/spadek kursu
    Wybór pozycji z listy powoduje przejście do CurrencyDetailsActivity, które zawiera
        aktualny u poprzedni kursy
        wykresy zmiany kursu z ostatnich 7 i 30 zapisów z api
    Złoto: aktualny i poprzedni kurs, wykres 7 i 3 ostatnich kursów
    Przelicznik walut: konwersja obustronna


*/
class MenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        DataHolder.prepare(applicationContext)
        val menuAdapter = MenuAdapter(menu())
        val menuRecyclerView: RecyclerView = findViewById(R.id.menu_recycler_view)
        val layoutManager = LinearLayoutManager(applicationContext)
        menuRecyclerView.layoutManager = layoutManager
        menuRecyclerView.adapter = menuAdapter
    }
}