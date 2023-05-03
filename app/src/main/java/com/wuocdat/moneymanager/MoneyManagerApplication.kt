package com.wuocdat.moneymanager

import android.app.Application
import com.wuocdat.moneymanager.Repository.ExpenseRepository
import com.wuocdat.moneymanager.Repository.GoalRepository
import com.wuocdat.moneymanager.Room.ExpenseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class MoneyManagerApplication: Application() {

    val applicationScope = CoroutineScope(SupervisorJob())

    val database by lazy { ExpenseDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { ExpenseRepository(database.getExpenseDao()) }
    val goalRepository by lazy { GoalRepository(database.getGoalDao()) }

}