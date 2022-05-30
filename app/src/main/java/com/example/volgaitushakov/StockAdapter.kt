package com.example.volgaitushakov

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.volgaitushakov.domain.api.FinnhubApi
import com.example.volgaitushakov.models.StockCost
import com.example.volgaitushakov.models.StockList
import kotlinx.android.synthetic.main.show_stock.view.*
import org.koin.java.KoinJavaComponent

class StockAdapter (val context: Context,  private val stockCostList : MutableList<StockCost>, val fragment: StockFragment) : RecyclerView.Adapter<StockAdapter.StockViewHolder>() {

    @SuppressLint("CheckResult")
    override fun onBindViewHolder(holder: StockViewHolder, position: Int) {
        val currentSymbol = stockCostList[position]
        val v = holder.itemView
            v.stock_name.text = currentSymbol.description
            v.stock_cost.text = currentSymbol.c.toString()
        holder.bind(stockCostList[position])
    }

    override fun getItemCount(): Int {
        return if (stockCostList.size > 58) 58
        else stockCostList.size
    }

    fun setItemPrice(c: Double, symbol: String) {
        val item = stockCostList.firstOrNull {
            it.symbol == symbol
        }
        item?.c = c
        val position = stockCostList.indexOf(item)
        notifyItemChanged(position)
    }

    fun getItems(): MutableList<StockCost> {
        return stockCostList
    }

    fun getItemBySymbol(symbol: String): StockCost? {
        return stockCostList.firstOrNull {
            it.symbol == symbol
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.show_stock, parent, false)
        return StockViewHolder(view)
    }

    inner class StockViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val stockName: TextView = itemView.findViewById(R.id.stock_name)
        private val stockPrice: TextView = itemView.findViewById(R.id.stock_cost)
        fun bind(stock: StockCost) {
            val oldValue: Double = stockPrice.text.toString().toDouble()
            val newValue: Double = stock.c
            if (oldValue > newValue) {
                stockPrice.setTextColor(Color.parseColor("#ff0000"))
            } else if (oldValue < newValue) {
                stockPrice.setTextColor(Color.parseColor("#0000ff"))
            }
            stockPrice.text = newValue.toString()
            stockName.text = stock.description
        }
    }
}