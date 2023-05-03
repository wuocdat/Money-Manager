package com.wuocdat.moneymanager.Repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.wuocdat.moneymanager.Model.Goal
import com.wuocdat.moneymanager.Room.GoalDAO

class GoalRepository(private val goalDAO: GoalDAO) {

    @WorkerThread
    suspend fun insert(goal: Goal) {
        goalDAO.insert(goal)
    }

    @WorkerThread
    suspend fun update(goal: Goal) {
        goalDAO.update(goal)
    }

    @WorkerThread
    suspend fun delete(goal: Goal) {
        goalDAO.delete(goal)
    }

    @WorkerThread
    fun getAllGoals(): LiveData<List<Goal>> {
        return goalDAO.getAllGoals()
    }

    @WorkerThread
    fun getGoalByMonthAndYear(month: Int, year: Int): LiveData<Goal> {
        return goalDAO.getGoalByMonthAndYear(month, year)
    }

    @WorkerThread
    suspend fun updateAmountByMonthAndYear(addedAmount: Long, month: Int, year: Int) {
        goalDAO.updateAmountByMonthAndYear(addedAmount, month, year)
    }
}