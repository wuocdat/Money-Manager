package com.wuocdat.moneymanager.Repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.wuocdat.moneymanager.Data.CategoryStatistic
import com.wuocdat.moneymanager.Data.TotalAmountByMonth
import com.wuocdat.moneymanager.Model.Expense
import com.wuocdat.moneymanager.Room.ExpenseDAO
import com.wuocdat.moneymanager.Utils.TimeUtils
import kotlinx.coroutines.flow.Flow

class ExpenseRepository(private val expenseDAO: ExpenseDAO) {

    val myAllExpenses: Flow<List<Expense>> = expenseDAO.getAllExpenses()

    @WorkerThread
    suspend fun insert(expense: Expense) {
        expenseDAO.insert(expense)
    }

    @WorkerThread
    suspend fun update(expense: Expense) {
        expenseDAO.update(expense)
    }

    @WorkerThread
    suspend fun delete(expense: Expense) {
        expenseDAO.delete(expense)
    }

    @WorkerThread
    suspend fun deleteById(id: Int) {
        expenseDAO.deleteById(id)
    }


    @WorkerThread
    fun getExpensesById(id: Int): LiveData<Expense> {
        return expenseDAO.getExpensesById(id)
    }

    @WorkerThread
    fun getRecentExpenses(number: Int): LiveData<List<Expense>> {
        return expenseDAO.getRecentExpenses(number)
    }

    @WorkerThread
    fun getExpensesOfXMonth(timeString: String): LiveData<List<Expense>> {
        return expenseDAO.getAllExpensesOfXMonth(timeString)
    }

    @WorkerThread
    fun getTotalAmountByMonthInCurrentYear(): LiveData<List<TotalAmountByMonth>> {
        return expenseDAO.getTotalAmountByMonthInCurrentYear()
    }

    @WorkerThread
    fun getTotalAmountByMonthInSpecialYear(yearString: String): LiveData<List<TotalAmountByMonth>> {
        return expenseDAO.getTotalAmountByMonthInSpecialYear(yearString)
    }

    @WorkerThread
    suspend fun getTotalAmountOfMonth(monthAndYearStr: String): TotalAmountByMonth {
        return expenseDAO.getTotalAmountByMonthAndYear(monthAndYearStr);
    }

    @WorkerThread
    fun getExpensesByDateRange(startTime: Long, endTime: Long): LiveData<List<Expense>> {
        return expenseDAO.getExpensesByDateRange(startTime, endTime)
    }

    @WorkerThread
    fun getExpensesByCategoryAndCreatedTime(
        startTime: Long,
        endTime: Long,
        desiredCategory: String
    ): LiveData<List<Expense>> {

        val start = TimeUtils.startOfDayTimestamp(startTime)
        val end = TimeUtils.endOfDayTimestamp(endTime)

        return if (desiredCategory == "all") expenseDAO.getExpensesByDateRange(
            start,
            end
        ) else
            expenseDAO.getExpensesByCategoryAndCreatedTime(start, end, desiredCategory)
    }

    @WorkerThread
    fun getCategoryStatisticByMonthAndYear(monthAndYearStr: String): LiveData<List<CategoryStatistic>> {
        return expenseDAO.getCategoryStatisticByMonthAndYear(monthAndYearStr)
    }
}