package com.wuocdat.moneymanager.Model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "goal_table", indices = [Index(value = ["month", "year"], unique = true)])
class Goal(
    val targetAmount: Long,
    val currentAmount: Long,
    val month: Int,
    val year: Int
) {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}