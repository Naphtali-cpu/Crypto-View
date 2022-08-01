package com.example.cryptoflow.data.cryptodetailmodel

data class MarketData(
    val circulating_supply: Double,
//    val current_price: CurrentPrice,
    val fdv_to_tvl_ratio: Any,
    val last_updated: String,
    val market_cap: MarketCap,
//    val market_cap_change_24h: Long,
    val market_cap_change_percentage_24h: Double,
    val market_cap_rank: Int,
    val max_supply: Double,
    val mcap_to_tvl_ratio: Any,
//    val price_change_24h: Int,
    val roi: Any,
    val total_supply: Double,
    val total_value_locked: Any,
)