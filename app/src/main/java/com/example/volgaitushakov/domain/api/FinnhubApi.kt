package com.example.volgaitushakov.domain.api

import com.example.volgaitushakov.models.StockCost
import com.example.volgaitushakov.models.StockList
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.*

interface FinnhubApi {

    @GET("stock/symbol?exchange=US&token=c8qdv2iad3ienapjju20")
    fun getStockList(): Single<ArrayList<StockList>>

    @GET("quote?")
    fun getStockCost(
        @Query("symbol") symbol: String,
        @Query("token") token: String
    ): Single<StockCost>

}