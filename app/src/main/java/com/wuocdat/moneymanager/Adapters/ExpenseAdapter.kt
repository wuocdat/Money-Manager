package com.wuocdat.moneymanager.Adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.wuocdat.moneymanager.Data.DetailInterface
import com.wuocdat.moneymanager.Model.Expense
import com.wuocdat.moneymanager.Utils.StringUtils
import com.wuocdat.moneymanager.Utils.TimeUtils
import com.wuocdat.roomdatabase.R

class ExpenseAdapter(private val activity: Activity, private val detailI: DetailInterface) :
    RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder>() {

    var expenses: List<Expense> = ArrayList()

    class ExpenseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val imageCategory: ImageView = itemView.findViewById(R.id.item_transation_image_view)
        val textViewTitle: TextView = itemView.findViewById(R.id.item_transation_titile)
        val textViewCategory: TextView = itemView.findViewById(R.id.item_transation_category)
        val textViewMoney: TextView = itemView.findViewById(R.id.item_transation_money)
        val textViewTime: TextView = itemView.findViewById(R.id.item_transation_time)
        val moreImg: ImageView = itemView.findViewById(R.id.item_transation_more_img)
        val card: CardView = itemView.findViewById(R.id.item_transation_card)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_transation, parent, false)

        return ExpenseViewHolder(view)

    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val currentExpense = expenses[position]

        val itemColorRes = getItemColor(currentExpense.category)
        val itemImgRes = getResImg(currentExpense.category)

        holder.textViewTitle.text = StringUtils.capFirstCharacter(currentExpense.expenseTitle)
        holder.textViewCategory.text = currentExpense.category
        holder.textViewMoney.text = StringUtils.convertToCurrencyFormat(currentExpense.money)
        holder.textViewTime.text =
            TimeUtils.timeFormat(currentExpense.createdTime, "dd/MM/yyyy")

        StringUtils.categories.find { categoryItem -> categoryItem.categoryName == currentExpense.category }?.imageResId?.let {
            holder.imageCategory.setImageResource(
                it
            )
        }

        if (itemColorRes !== null && itemImgRes !== null) {
            holder.textViewCategory.setTextColor(
                activity.resources.getColor(
                    itemColorRes,
                    activity.theme
                )
            )
            holder.imageCategory.setColorFilter(
                activity.resources.getColor(
                    itemColorRes,
                    activity.theme
                )
            )
        }

        holder.moreImg.setOnClickListener {
            detailI.onClickMoreItem(position)
        }

        holder.card.setOnClickListener{
            detailI.onClickItem(position)
        }

    }

    override fun getItemCount(): Int {
        return expenses.size
    }

    fun setExpense(expenses: List<Expense>) {
        this.expenses = expenses
        notifyDataSetChanged()
    }

    fun getExpense(position: Int): Expense {
        return expenses[position]
    }

    private fun getItemColor(category: String): Int? {
        return StringUtils.categories.find { item -> item.categoryName == category }?.primaryColor
    }

    private fun getResImg(category: String): Int? {
        return StringUtils.categories.find { item -> item.categoryName == category }?.imageResId
    }


}