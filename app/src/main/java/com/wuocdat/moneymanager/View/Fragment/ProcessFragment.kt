package com.wuocdat.moneymanager.View.Fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.wuocdat.moneymanager.Data.TotalAmountByMonth
import com.wuocdat.moneymanager.Model.Expense
import com.wuocdat.moneymanager.MoneyManagerApplication
import com.wuocdat.moneymanager.Utils.StringUtils
import com.wuocdat.moneymanager.Utils.TimeUtils
import com.wuocdat.moneymanager.ViewModel.ExpenseViewModel
import com.wuocdat.moneymanager.ViewModel.ExpenseViewModelFactory
import com.wuocdat.roomdatabase.R
import java.util.*
import kotlin.collections.ArrayList


class ProcessFragment : Fragment() {

    private lateinit var monthTextView: TextView
    private lateinit var yearTextView: TextView

    private lateinit var pieChart: PieChart
    private lateinit var barChart: BarChart
    lateinit var expenseViewModel: ExpenseViewModel

    var selectedMonth = TimeUtils.getCurrentMonth()
    var selectedYear = TimeUtils.getCurrentYear()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_proccess, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val viewModelFactory =
            ExpenseViewModelFactory((requireActivity().application as MoneyManagerApplication).repository)
        expenseViewModel =
            ViewModelProvider(this, viewModelFactory).get(ExpenseViewModel::class.java)

        pieChart = view.findViewById(R.id.pieChart)
        barChart = view.findViewById(R.id.barChart)
        monthTextView = view.findViewById(R.id.proccess_fragment_month_textView)
        yearTextView = view.findViewById(R.id.proccess_fragment_year_textView)

        monthTextView.text = selectedMonth
        yearTextView.text = selectedYear

        monthTextView.setOnClickListener { showMonthPickerDialog() }
        yearTextView.setOnClickListener { showYearPickerDialog() }

        //pieChart
        val l: Legend = pieChart.legend
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        pieChart.centerText = "This Month"
        pieChart.description.text = "Amount spent this month"
        pieChart.setCenterTextColor(R.color.primary_color)
        expenseViewModel.getExpensesOfXMonth("$selectedYear-$selectedMonth")
            .observe(requireActivity(), Observer { expenses ->
                setDataToPieChart(expenses)
            })

        //barChart
        val yAxisLeft: YAxis = barChart.getAxis(YAxis.AxisDependency.LEFT)
        val yAxisRight: YAxis = barChart.getAxis(YAxis.AxisDependency.RIGHT)
        yAxisLeft.axisMinimum = 0f
        yAxisRight.axisMinimum = 0f
        val xAxisLabels = StringUtils.MONTHS_OF_YEAR
        val xAxis = barChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f
        xAxis.labelCount = xAxisLabels.size
        xAxis.valueFormatter = IndexAxisValueFormatter(xAxisLabels)
        barChart.description.text = "Spending of the months"
        barChart.setDrawBorders(true)

        expenseViewModel.getTotalAmountByMonthInSpecialYear(selectedYear)
            .observe(requireActivity(), Observer { totalAmounts ->
                setDataToBarChart(totalAmounts)
            })

    }

    private fun setDataToBarChart(totalAmountsByMonth: List<TotalAmountByMonth>) {
        val entries: MutableList<BarEntry> = ArrayList()

        for (i in 1..12) {
            val currentEntry = BarEntry(i.toFloat(), 0f)
            totalAmountsByMonth.find { item -> item.month == i }?.let { it ->
                currentEntry.y = it.totalAmount.toFloat()
            }
            entries.add(currentEntry)
        }
        val set = BarDataSet(entries, "").apply {
            colors = ColorTemplate.MATERIAL_COLORS.toList()
                .plus(ColorTemplate.VORDIPLOM_COLORS.toList())
        }

        val data = BarData(set)
        data.barWidth = 0.9f // set custom bar width

        barChart.data = data
        barChart.setFitBars(true) // make the x-axis fit exactly all bars
        barChart.invalidate() // refresh
    }

    private fun setDataToPieChart(expenses: List<Expense>) {
        val entries: MutableList<PieEntry> = ArrayList()

        for (item in StringUtils.categories) {
            val filteredExpenses =
                expenses.filter { expense -> expense.category == item.categoryName }
            if (filteredExpenses.isNotEmpty()) {
                entries.add(PieEntry(filteredExpenses.map { it.money }
                    .reduce { sum, money -> sum + money }.toFloat(), item.categoryName))
            }
        }

        val set = PieDataSet(entries, "").apply {
            colors = ColorTemplate.MATERIAL_COLORS.toList()
                .plus(ColorTemplate.VORDIPLOM_COLORS.toList())
        }
        set.valueTextColor = R.color.white
        val data = PieData(set)
        pieChart.data = data
        pieChart.invalidate() // refresh
    }

    private fun showYearPickerDialog() {
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)

        val yearPicker = NumberPicker(requireContext()).apply {
            minValue = 1900
            maxValue = currentYear
            value = selectedYear.toInt()
            wrapSelectorWheel = false
        }

        val alertDialog = AlertDialog.Builder(requireContext())
            .setTitle("Choose year")
            .setView(yearPicker)
            .setPositiveButton("OK") { _, _ ->
                val year = yearPicker.value
                selectedYear = year.toString()
                yearTextView.text = selectedYear
                expenseViewModel.setYearString(selectedYear)

                val formattedMonth = "$selectedYear-$selectedMonth"
                expenseViewModel.setMonthAndYearString(formattedMonth)
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }
            .create()

        alertDialog.show()
    }

    private fun showMonthPickerDialog() {
        val monthPicker = NumberPicker(requireContext()).apply {
            minValue = 1
            maxValue = 12
            value = selectedMonth.toInt()
            wrapSelectorWheel = false
        }

        val alertDialog = AlertDialog.Builder(requireContext())
            .setTitle("Choose month")
            .setView(monthPicker)
            .setPositiveButton("OK") { _, _ ->
                val month = monthPicker.value
                selectedMonth = String.format("%02d", month)
                val formattedMonth = "$selectedYear-$selectedMonth"
                monthTextView.text = selectedMonth
                expenseViewModel.setMonthAndYearString(formattedMonth)
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }
            .create()

        alertDialog.show()
    }

}