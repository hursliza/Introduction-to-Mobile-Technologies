package com.io.lizaveta_hurskaya_wt_15_30.menu
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.io.lizaveta_hurskaya_wt_15_30.R
import com.io.lizaveta_hurskaya_wt_15_30.converter.ConverterActivity
import com.io.lizaveta_hurskaya_wt_15_30.currenciesList.CurrenciesListActivity
import com.io.lizaveta_hurskaya_wt_15_30.currencyDetails.CurrencyDetailsActivity
import com.io.lizaveta_hurskaya_wt_15_30.model.MenuItem

class MenuAdapter(private val dataSet: List<MenuItem>) :
    RecyclerView.Adapter<MenuAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var label: TextView = view.findViewById(R.id.menu_item_title)
        var image: ImageView = view.findViewById(R.id.menu_item_image)
        var card: CardView = view.findViewById(R.id.menu_card)

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.menu_item, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.label.text = dataSet[position].name
        dataSet[position].image?.let { viewHolder.image.setImageResource(it) }
        if (dataSet[position].id == 0) {
            viewHolder.card.setOnClickListener {
                val intent = Intent(it.context, CurrenciesListActivity::class.java)
                it.context.startActivity(intent)
            }
        }
        if (dataSet[position].id == 1) {
            viewHolder.card.setOnClickListener {
                val intent = Intent(it.context, ConverterActivity::class.java)
                it.context.startActivity(intent)
            }
        }
        if (dataSet[position].id == 2) {
            viewHolder.card.setOnClickListener {
                val intent = Intent(it.context, CurrencyDetailsActivity::class.java).apply {
                    putExtra("currencyCode", "gold")
                    putExtra("table", "")
                }
                it.context.startActivity(intent)
            }
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}
