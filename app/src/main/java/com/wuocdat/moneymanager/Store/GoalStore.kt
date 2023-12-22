package com.wuocdat.moneymanager.Store

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

class GoalStore {
    companion object {
        private val Context.datastore by preferencesDataStore(name = "goal_store")
        private val GOAL_KEY = intPreferencesKey("goal_key")
        private const val DEFAULT_GOAL_VALUE = 2000

        suspend fun read(context: Context): Int {
            val preferences = context.datastore.data.first()
            val goal = preferences[GOAL_KEY]
            return goal?: DEFAULT_GOAL_VALUE
        }

        suspend fun save(data: Int, context: Context) {
            context.datastore.edit { goalStore ->
                goalStore[GOAL_KEY] = data
            }
        }
    }
}