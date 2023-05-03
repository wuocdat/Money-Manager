package com.wuocdat.moneymanager.Data

data class CategoryStatistic(
    val category: String,
    val amount: Long,
    val numberTransaction: Int,
    val percentage: Double
)
