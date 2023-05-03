package com.wuocdat.moneymanager.Room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.wuocdat.moneymanager.Model.Expense
import com.wuocdat.moneymanager.Model.Goal
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Expense::class, Goal::class], version = 2)
abstract class ExpenseDatabase : RoomDatabase() {

    abstract fun getExpenseDao(): ExpenseDAO
    abstract fun getGoalDao(): GoalDAO

    companion object {

        @Volatile
        private var INSTANCE: ExpenseDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): ExpenseDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ExpenseDatabase::class.java,
                    "expense_table"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(ExpenseDatabaseCallback(scope))
                    .build()

                INSTANCE = instance

                instance
            }
        }

    }

    private class ExpenseDatabaseCallback(private val scope: CoroutineScope) :
        RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            INSTANCE?.let { database ->

                scope.launch {
                    val expenseDao = database.getExpenseDao()

                    expenseDao.insert(
                        Expense(
                            "TPlink router",
                            "for home",
                            1669414773L*1000,
                            200000,
                            "Device"
                        )
                    )
                    expenseDao.insert(
                        Expense(
                            "Bomber Jacket",
                            "for me",
                            1668896373L*1000,
                            40000,
                            "Shopping"
                        )
                    )
                    expenseDao.insert(
                        Expense(
                            "Panadol",
                            "for me",
                            1671488373L*1000,
                            50000,
                            "Medicine"
                        )
                    )
                    expenseDao.insert(
                        Expense(
                            "TPlink router",
                            "for home",
                            1672006773L*1000,
                            200000,
                            "Device"
                        )
                    )
                    expenseDao.insert(
                        Expense(
                            "Bomber Jacket",
                            "for me",
                            1674685173L*1000,
                            40000,
                            "Shopping"
                        )
                    )
                    expenseDao.insert(
                        Expense(
                            "Panadol",
                            "for me",
                            1674166773L*1000,
                            50000,
                            "Medicine"
                        )
                    )
                    expenseDao.insert(
                        Expense(
                            "TPlink router",
                            "for home",
                            1676845173L*1000,
                            200000,
                            "Device"
                        )
                    )
                    expenseDao.insert(
                        Expense(
                            "Bomber Jacket",
                            "for me",
                            1677363573L*1000,
                            40000,
                            "Shopping"
                        )
                    )
                    expenseDao.insert(
                        Expense(
                            "Panadol",
                            "for me",
                            1679782773L*1000,
                            50000,
                            "Medicine"
                        )
                    )
                    expenseDao.insert(
                        Expense(
                            "TPlink router",
                            "for home",
                            1679264373L*1000,
                            200000,
                            "Device"
                        )
                    )

                }

            }

        }

    }

}