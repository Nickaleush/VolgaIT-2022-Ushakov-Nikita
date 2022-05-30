package com.example.volgaitushakov

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.volgaitushakov.models.TradeInput
import com.example.volgaitushakov.domain.api.FinnhubApi
import com.example.volgaitushakov.models.StockCost
import com.example.volgaitushakov.models.StockList
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.main_fragment.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.*
import org.json.JSONObject
import org.koin.java.KoinJavaComponent
import java.io.Serializable
import java.util.concurrent.TimeUnit

const val limit = 58
class StockFragment : Fragment() {

    private val finnhubApi: FinnhubApi by KoinJavaComponent.inject(FinnhubApi::class.java)
    private val stockCostList = mutableListOf<StockCost>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getStockList()
    }

    private fun getStockList(){
        val stockList = finnhubApi.getStockList()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                fillStockCostList(it)
            },{ })
    }

     fun getStockCost(symbol1: String, description: String){
        val api = finnhubApi.getStockCost(symbol1, "c8qdv2iad3ienapjju20")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                it.description = description
                it.symbol = symbol1
                stockCostList.add(it)
                if (stockCostList.size == limit)
                {
                    val adapter = StockAdapter(requireContext() , stockCostList, this@StockFragment)
                    recyclerView.adapter = adapter
                    val request: Request = Request.Builder()
                        .url("wss://ws.finnhub.io?token=c8qdv2iad3ienapjju20")
                        .build()
                    val stocksWebSocketListener = StocksWebSocketListener()
                    val client = OkHttpClient.Builder()
                        .pingInterval (30, TimeUnit.SECONDS)
                        .build()
                    client.newWebSocket(request, stocksWebSocketListener)
                }
            },{})
    }

    private fun fillStockCostList(stockList: ArrayList<StockList>) {
        for (i in 0..limit){
            getStockCost(stockList[i].symbol, stockList[i].description)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val adapter = StockAdapter(requireContext() , stockCostList, this@StockFragment)
        outState.putSerializable("STOCKS", adapter.getItems() as Serializable)
    }

    private inner class StocksWebSocketListener : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: Response) {
            super.onOpen(webSocket, response)

            stockCostList.forEach { (symbol, _) ->
                run {
                    webSocket.send(Json.encodeToString(TradeInput(symbol, "subscribe")))
                }
            }
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            super.onMessage(webSocket, text)
            if (JSONObject(text).has("data")) {
                val jArr = JSONObject(text).getJSONArray("data")
                for (i in 0 until jArr.length()) {
                    val jsonObject = jArr.getJSONObject(i)
                    val price = jsonObject.getDouble("p")
                    stockCostList[i].c = price
                }
                requireActivity().runOnUiThread {
                    val adapter = StockAdapter(requireContext() , stockCostList, this@StockFragment)
                    stockCostList.forEach {
                        if (it.symbol.let { it1 -> adapter.getItemBySymbol(it1) } != null) {
                            adapter.setItemPrice(it.c, it.symbol)
                        } else {
                            it.symbol.let { it1 -> Log.e("SSS", it1) }
                        }
                    }
                }
            }
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            super.onClosed(webSocket, code, reason)
            Log.e("ERR", "Connection closed: $reason")
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            super.onFailure(webSocket, t, response)
            Log.e("ERR", "Error: " +  t.message.toString())
            Log.e("ERR", "Stack trace: " +  t.stackTraceToString())
        }
    }
}
