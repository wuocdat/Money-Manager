package com.wuocdat.moneymanager.Adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.wuocdat.moneymanager.Data.CategoryStatistic
import com.wuocdat.moneymanager.Data.StatisticInterface
import com.wuocdat.moneymanager.Utils.StringUtils
import com.wuocdat.roomdatabase.R

class StatisticAdapter(
    val activity: Activity,
    private val listener: StatisticInterface
) :
    RecyclerView.Adapter<StatisticAdapter.StatisticViewHolder>() {

    private var statistics: List<CategoryStatistic> = ArrayList()

    class StatisticViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryIcon: ImageView = itemView.findViewById(R.id.category_progess_item_imageView)
        val progress: CircularProgressIndicator =
            itemView.findViewById(R.id.category_progress_tem_proccess)
        val titleTV: TextView = itemView.findViewById(R.id.category_progress_item_title)
        val percentTV: TextView = itemView.findViewById(R.id.category_progress_item_percent)
        val amountTV: TextView = itemView.findViewById(R.id.category_progress_item_amount)
        val numberTransactionTV: TextView =
            itemView.findViewById(R.id.category_progress_item_number_transaction)
        val mainLayout: ConstraintLayout =
            itemView.findViewById(R.id.category_progress_item_main_layout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatisticViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category_progress, parent, false)
        return StatisticViewHolder(view)
    }

    override fun onBindViewHolder(holder: StatisticViewHolder, position: Int) {

        val currentStatistic = statistics[position]

        val itemImgRes = getResImg(currentStatistic.category)
        if (itemImgRes !== null) holder.categoryIcon.setImageResource(itemImgRes)

        holder.progress.progress = currentStatistic.percentage.toInt()
        holder.percentTV.text = "${currentStatistic.percentage.toInt()}%"

        val itemColorRes = getItemColor(currentStatistic.category)
        if (itemColorRes !== null) {
            holder.categoryIcon.setColorFilter(
                activity.resources.getColor(
                    itemColorRes,
                    activity.theme
                )
            )
            holder.percentTV.setTextColor(activity.resources.getColor(itemColorRes, activity.theme))
            holder.progress.setIndicatorColor(
                activity.resources.getColor(
                    itemColorRes,
                    activity.theme
                )
            )
        }

        holder.titleTV.text = activity.resources.getString(
            getNameResId(currentStatistic.category)?:R.string.medicine
        )
        holder.amountTV.text = StringUtils.convertToCurrencyFormat(currentStatistic.amount)
        val numberTransaction = currentStatistic.numberTransaction
        holder.numberTransactionTV.text =
            activity.resources.getString(R.string.transaction_times, numberTransaction)
        holder.mainLayout.setOnClickListener {
            listener.onClickItem(currentStatistic.category)
        }
    }

    override fun getItemCount(): Int {
        return statistics.size
    }

    fun setStatistic(statistics: List<CategoryStatistic>) {
        this.statistics = statistics
        notifyDataSetChanged()
    }

    private fun getItemColor(category: String): Int? {
        return StringUtils.categories.find { item -> item.categoryName == category }?.primaryColor
    }

    private fun getResImg(category: String): Int? {
        return StringUtils.categories.find { item -> item.categoryName == category }?.imageResId
    }

    private fun getNameResId(category: String): Int? {
        return StringUtils.categories.find { item -> item.categoryName == category }?.nameResId
    }
}