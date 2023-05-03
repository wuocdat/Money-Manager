package com.wuocdat.moneymanager.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.wuocdat.moneymanager.Model.Goal
import com.wuocdat.moneymanager.Repository.GoalRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GoalViewModel(private val repository: GoalRepository): ViewModel() {

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

    fun updateAmountByMonthAndYear(addedAmount: Long, month: Int, year: Int) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateAmountByMonthAndYear(addedAmount, month, year)
    }
}

class GoalViewModelFactory(private var repository: GoalRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GoalViewModel::class.java)) {
            return GoalViewModel(repository) as T
        } else {
            throw IllegalArgumentException("unknown View Model")
        }
    }

}