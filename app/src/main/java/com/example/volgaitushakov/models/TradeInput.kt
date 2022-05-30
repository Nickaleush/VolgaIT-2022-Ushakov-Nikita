package com.example.volgaitushakov.models

import kotlinx.serialization.Serializable

@Serializable
data class TradeInput(
    val symbol: String,
    val type: String
)
