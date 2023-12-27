package com.wuocdat.moneymanager.Room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.wuocdat.moneymanager.Model.Goal

@Dao
interface GoalDAO {

    @Insert
    suspend fun insert(goal: Goal)

    @Update
    suspend fun update(goal: Goal)

    @Delete
    suspend fun delete(goal: Goal)

    //queries
    @Query("SELECT * FROM goal_table ORDER BY targetAmount DESC")
    fun getAllGoals(): LiveData<List<Goal>>

    @Query("SELECT * FROM goal_table WHERE month = :month AND year = :year LIMIT 1")
    fun getGoalByMonthAndYear(month: Int, year: Int): LiveData<Goal>

    @Query("UPDATE goal_table SET currentAmount = :amount WHERE month = :month AND year = :year")
    suspend fun updateAmountByMonthAndYear(amount: Long, month: Int, year: Int)

    @Query("UPDATE goal_table SET targetAmount = :amount WHERE month = :month AND year = :year")
    suspend fun updateTargetAmountByMonthAndYear(amount: Long, month: Int, year: Int)
}