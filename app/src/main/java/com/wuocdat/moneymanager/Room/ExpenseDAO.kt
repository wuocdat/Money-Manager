package com.wuocdat.moneymanager.Room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.wuocdat.moneymanager.Data.CategoryStatistic
import com.wuocdat.moneymanager.Data.TotalAmountByMonth
import com.wuocdat.moneymanager.Model.Expense
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDAO {

    @Insert
    suspend fun insert(expense: Expense)

    @Update
    suspend fun update(expense: Expense)

    @Delete
    suspend fun delete(expense: Expense)

    //queries
    @Query("DELETE FROM expense_table WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("SELECT * FROM expense_table WHERE id=:id ORDER BY createdTime DESC")
    fun getExpensesById(id: Int): LiveData<Expense>

    @Query("SELECT * FROM expense_table ORDER BY createdTime DESC")
    fun getAllExpenses(): Flow<List<Expense>>

    @Query("SELECT * FROM expense_table ORDER BY createdTime DESC LIMIT :number")
    fun getRecentExpenses(number: Int): LiveData<List<Expense>>

    @Query("SELECT * FROM expense_table WHERE strftime('%Y-%m', datetime(createdTime/1000, 'unixepoch', 'localtime')) = :timeString")
    fun getAllExpensesOfXMonth(timeString: String): LiveData<List<Expense>>

    @Query(
        """
        SELECT strftime('%m', datetime(createdTime/1000, 'unixepoch', 'localtime')) AS month, 
        strftime('%Y', datetime(createdTime/1000, 'unixepoch', 'localtime')) AS year, SUM(money) as totalAmount
        FROM expense_table
        WHERE strftime('%Y', datetime(createdTime/1000, 'unixepoch', 'localtime')) = strftime('%Y', 'now', 'localtime')
        GROUP BY month, year
        ORDER BY year, month
    """
    )
    fun getTotalAmountByMonthInCurrentYear(): LiveData<List<TotalAmountByMonth>>

    @Query(
        """
        SELECT strftime('%m', datetime(createdTime/1000, 'unixepoch', 'localtime')) AS month, 
        strftime('%Y', datetime(createdTime/1000, 'unixepoch', 'localtime')) AS year, SUM(money) as totalAmount
        FROM expense_table
        WHERE strftime('%Y', datetime(createdTime/1000, 'unixepoch', 'localtime')) = :yearString
        GROUP BY month, year
        ORDER BY year, month
    """
    )
    fun getTotalAmountByMonthInSpecialYear(yearString: String): LiveData<List<TotalAmountByMonth>>

    @Query("SELECT * FROM expense_table WHERE createdTime >= :startTime AND createdTime <= :endTime")
    fun getExpensesByDateRange(startTime: Long, endTime: Long): LiveData<List<Expense>>

    @Query(
        """
        SELECT * FROM expense_table 
        WHERE createdTime >= :startTime 
        AND createdTime <= :endTime 
        AND category=:desiredCategory
        """
    )
    fun getExpensesByCategoryAndCreatedTime(
        startTime: Long,
        endTime: Long,
        desiredCategory: String
    ): LiveData<List<Expense>>

    @Query(
        """
        WITH total_amount AS (
                   SELECT SUM(money) AS total
                   FROM expense_table
                   WHERE strftime('%Y-%m', datetime(createdTime/1000, 'unixepoch', 'localtime')) = :monthAndYearStr
        )
        SELECT category,
        SUM(money) as amount,
        COUNT(*) as numberTransaction,
        (SUM(money) * 100.0 / (SELECT total FROM total_amount)) AS percentage
        FROM expense_table
        WHERE strftime('%Y-%m', datetime(createdTime/1000, 'unixepoch', 'localtime')) = :monthAndYearStr
        GROUP BY category
        """
    )
    fun getCategoryStatisticByMonthAndYear(monthAndYearStr: String): LiveData<List<CategoryStatistic>>
}