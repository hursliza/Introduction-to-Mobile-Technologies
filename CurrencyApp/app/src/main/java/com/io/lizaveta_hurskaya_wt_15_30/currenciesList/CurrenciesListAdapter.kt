package com.io.lizaveta_hurskaya_wt_15_30.currenciesList

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.io.lizaveta_hurskaya_wt_15_30.R
import com.io.lizaveta_hurskaya_wt_15_30.currencyDetails.CurrencyDetailsActivity
import com.io.lizaveta_hurskaya_wt_15_30.model.Currency

class CurrenciesListAdapter(var context: Context, var dataSet: Array<Currency>) : RecyclerView.Adapter<CurrenciesListAdapter.ViewHolder>() {
    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val flag: ImageView
        val label: TextView
        val rate: TextView
        val dynamics: ImageView

        init {
            flag = view.findViewById(R.id.flag)
            label = view.findViewById(R.id.label)
            rate = view.findViewById(R.id.rate)
            dynamics = view.findViewById(R.id.dynamics)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.currencies_list_row, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.flag.setImageResource(dataSet[position].flag)
        holder.label.text = dataSet[position].currencyCode
        holder.rate.text = dataSet[position].rate.toString()

        if (dataSet[position].growth)
            holder.dynamics.setImageResource(R.drawable.arrow_up)
        else
            holder.dynamics.setImageResource(R.drawable.arrow_down)

        holder.itemView.setOnClickListener{
            val intent = Intent(holder.itemView.context, CurrencyDetailsActivity::class.java).apply {
                putExtra("currencyCode", dataSet[position].currencyCode)
                putExtra("table", dataSet[position].table)
            }
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount() = dataSet.size
}