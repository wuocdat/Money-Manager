package com.wuocdat.moneymanager.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.wuocdat.moneymanager.Model.Goal
import com.wuocdat.moneymanager.Repository.ExpenseRepository
import com.wuocdat.moneymanager.Repository.GoalRepository
import com.wuocdat.moneymanager.Utils.TimeUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GoalViewModel(
    private val repository: GoalRepository,
    private val expenseRepository: ExpenseRepository
) : ViewModel() {

    fun insert(goal: Goal) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(goal)
    }

    fun update(goal: Goal) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(goal)
    }

    fun delete(goal: Goal) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(goal)
    }

    fun getAllGoals(): LiveData<List<Goal>> {
        return repository.getAllGoals()
    }

    fun getGoalByMonthAndYear(month: Int, year: Int): LiveData<Goal> {
        return repository.getGoalByMonthAndYear(month, year)
    }

    fun updateGoalByMonthAndYear(month: Int, year: Int) = viewModelScope.launch(Dispatchers.IO) {
        val goalAmount =
            expenseRepository.getTotalAmountOfMonth(TimeUtils.getMonthAndYearStr(month, year))
        repository.updateAmountByMonthAndYear(goalAmount.totalAmount, month, year)
    }


    fun updateTargetAmountByMonthAndYear(amount: Long, month: Int, year: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateTargetAmountByMonthAndYear(amount, month, year)
        }

}

class GoalViewModelFactory(
    private var repository: GoalRepository,
    private var eRepository: ExpenseRepository
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GoalViewModel::class.java)) {
            return GoalViewModel(repository, eRepository) as T
        } else {
            throw IllegalArgumentException("unknown View Model")
        }
    }

}