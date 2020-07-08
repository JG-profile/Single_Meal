package com.example.singlemeal.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.singlemeal.CellClickListener
import com.example.singlemeal.database.Meal
import com.example.singlemeal.R

class MealAdapter internal constructor (context: Context, private val cellClickListener: CellClickListener
): RecyclerView.Adapter<MealAdapter.MealViewHolder>() {
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var meals = emptyList<Meal>() // Cached copy of words

    inner class MealViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val nameView: TextView = itemView.findViewById(R.id.mealName)
        val dateView: TextView = itemView.findViewById(R.id.mealDate)
        val view: View = itemView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        val itemView = inflater.inflate(R.layout.meal_item, parent, false)
        return MealViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        val current = meals[position]
        holder.nameView.text = current.name
        holder.dateView.text = current.date

        holder.itemView.setOnClickListener {
            cellClickListener.onCellClickListener(current)
        }

        holder.itemView.setOnLongClickListener {

            cellClickListener.onCellLongClickListener(current.mealId, current.name)
            true
        }

    }

    internal fun setMeals(meals: List<Meal>) {
        this.meals = meals
        notifyDataSetChanged()
    }

    override fun getItemCount() = meals.size




}