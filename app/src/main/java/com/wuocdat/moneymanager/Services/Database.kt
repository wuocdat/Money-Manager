package com.wuocdat.moneymanager.Services

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.wuocdat.moneymanager.MoneyManagerApplication
import com.wuocdat.moneymanager.ViewModel.ExpenseViewModel
import com.wuocdat.moneymanager.ViewModel.ExpenseViewModelFactory
import com.wuocdat.moneymanager.ViewModel.GoalViewModel
import com.wuocdat.moneymanager.ViewModel.GoalViewModelFactory

class Database {
    companion object {
        fun getGoalViewModel(owner: ViewModelStoreOwner, application: Application): GoalViewModel {
            val goalViewModelFactory =
                GoalViewModelFactory(
                    (application as MoneyManagerApplication).goalRepository,
                    application.repository
                )
            return ViewModelProvider(owner, goalViewModelFactory).get(GoalViewModel::class.java);
        }

        fun getExpenseViewModel(
            owner: ViewModelStoreOwner,
            application: Application
        ): ExpenseViewModel {
            val viewModelFactory =
                ExpenseViewModelFactory((application as MoneyManagerApplication).repository)
            return ViewModelProvider(owner, viewModelFactory).get(ExpenseViewModel::class.java);
        }
    }
}