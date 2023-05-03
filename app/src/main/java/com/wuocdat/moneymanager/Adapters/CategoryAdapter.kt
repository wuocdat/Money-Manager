package com.wuocdat.moneymanager.Adapters

import android.content.Context
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.wuocdat.moneymanager.Data.CategoryItem
import com.wuocdat.moneymanager.Interfaces.OnItemSelectedListener
import com.wuocdat.roomdatabase.R

class CategoryAdapter(
    private val listData: List<CategoryItem>,
    val context: Context,
    val selectable: Boolean,
    private val listener: OnItemSelectedListener
) :
    RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    private var selectedItemPosition = -1

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val imageView: ImageView = itemView.findViewById(R.id.item_category_imageView)
        val textView: TextView = itemView.findViewById(R.id.item_category_textView)
        val container: LinearLayout = itemView.findViewById(R.id.item_category_container)
        val cardView: CardView = itemView.findViewById(R.id.item_category_card_view)
        val checkImage: ImageView = itemView.findViewById(R.id.item_category_check_image)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)

        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {

        val currentItem = listData[position]

        holder.imageView.setImageResource(currentItem.imageResId)
        holder.textView.text = currentItem.categoryName
        holder.cardView.setCardBackgroundColor(context.getColor(currentItem.primaryColor))
        holder.container.setBackgroundColor(context.getColor(currentItem.secondaryColor))

        val container = holder.container

        if (position == selectedItemPosition && selectable) {
            holder.textView.visibility = View.GONE
            holder.cardView.visibility = View.GONE
            holder.checkImage.visibility = View.VISIBLE
        } else {
            holder.textView.visibility = View.VISIBLE
            holder.cardView.visibility = View.VISIBLE
            holder.checkImage.visibility = View.GONE

        }
        container.setOnClickListener {
            val previousSelectedPosition = selectedItemPosition
            selectedItemPosition = holder.adapterPosition
            notifyItemChanged(previousSelectedPosition)
            notifyItemChanged(position)
            listener.onItemSelected(position)
        }

    }

    override fun getItemCount(): Int {
        return listData.size
    }
}