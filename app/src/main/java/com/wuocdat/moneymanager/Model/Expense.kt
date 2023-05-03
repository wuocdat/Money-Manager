package com.wuocdat.moneymanager.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expense_table")
class Expense(
    val expenseTitle: String,
    val description: String,
    val createdTime: Long, //TimeStamp
    val money: Long,
    val category: String
) {

    @PrimaryKey(autoGenerate = true)
    var id = 0

}