package com.wuocdat.moneymanager.ViewModel

import androidx.lifecycle.*
import com.wuocdat.moneymanager.Data.CategoryStatistic
import com.wuocdat.moneymanager.Data.TotalAmountByMonth
import com.wuocdat.moneymanager.Model.Expense
import com.wuocdat.moneymanager.Repository.ExpenseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ExpenseViewModel(private val repository: ExpenseRepository) : ViewModel() {

    val myAllExpense: LiveData<List<Expense>> = repository.myAllExpenses.asLiveData()

    private val _monthAndYearString: MutableLiveData<String> = MutableLiveData()
    val monthAndYearString: LiveData<String> get() = _monthAndYearString

    private val _yearString: MutableLiveData<String> = MutableLiveData()
    val yearString: LiveData<String> get() = _yearString

    private val _startTime = MutableLiveData<Long>()
    val startTime: LiveData<Long> get() = _startTime

    private val _endTime = MutableLiveData<Long>()
    val endTime: LiveData<Long> get() = _endTime

    private val _category = MutableLiveData<String>()
    val category: LiveData<String> get() = _category

    private val _queryParameters = MediatorLiveData<Triple<Long, Long, String>>()

    init {
        _queryParameters.addSource(_startTime) { updateQueryParameters() }
        _queryParameters.addSource(_endTime) { updateQueryParameters() }
        _queryParameters.addSource(_category) { updateQueryParameters() }
    }

    private fun updateQueryParameters() {
        _startTime.value?.let { startTime ->
            _endTime.value?.let { endTime ->
                _category.value?.let { category ->
                    _queryParameters.value = Triple(startTime, endTime, category)
                }
            }
        }
    }


    fun insert(expense: Expense) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(expense)
    }

    fun update(expense: Expense) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(expense)
    }

    fun delete(expense: Expense) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(expense)
    }

    fun deleteById(id: Int) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteById(id)
    }

    fun getExpenseById(id: Int) : LiveData<Expense> {
        return repository.getExpensesById(id)
    }

    fun getRecentExpenses(number: Int): LiveData<List<Expense>> {
        return repository.getRecentExpenses(number)
    }

    fun getExpensesOfXMonth(timeString: String): LiveData<List<Expense>> {
        setMonthAndYearString(timeString)
        return Transformations.switchMap(_monthAndYearString) { timeStr ->
            repository.getExpensesOfXMonth(timeStr)
        }
    }

    fun getTotalAmountByMonthInCurrentYear(): LiveData<List<TotalAmountByMonth>> {
        return repository.getTotalAmountByMonthInCurrentYear()
    }

    fun getTotalAmountByMonthInSpecialYear(yearString: String): LiveData<List<TotalAmountByMonth>> {
        setYearString(yearString)
        return Transformations.switchMap(_yearString) { yearStr ->
            repository.getTotalAmountByMonthInSpecialYear(yearStr)
        }
    }

    fun getExpensesByCreatedTimeAndCategory(
        startTime: Long,
        endTime: Long,
        desiredCategory: String
    ): LiveData<List<Expense>> {
        setStartTime(startTime)
        setEndTime(endTime)
        setCategory(desiredCategory)

        return Transformations.switchMap(_queryParameters) { (startTime, endTime, category) ->
            repository.getExpensesByCategoryAndCreatedTime(startTime, endTime, category)
        }
    }

    fun getCategoryStatisticByMonthAndYear(monthAndYearStr: String) : LiveData<List<CategoryStatistic>> {
        return repository.getCategoryStatisticByMonthAndYear(monthAndYearStr)
    }

    //setters
    fun setStartTime(value: Long) {
        _startTime.value = value
    }

    fun setEndTime(value: Long) {
        _endTime.value = value
    }

    fun setCategory(value: String) {
        _category.value = value
    }

    fun setMonthAndYearString(value: String) {
        _monthAndYearString.value = value
    }

    fun setYearString(value: String) {
        _yearString.value = value
    }

}

class ExpenseViewModelFactory(private var repository: ExpenseRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExpenseViewModel::class.java)) {
            return ExpenseViewModel(repository) as T
        } else {
            throw IllegalArgumentException("unknown View Model")
        }
    }

}