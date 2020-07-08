package com.example.singlemeal.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.singlemeal.CellClickListener
import com.example.singlemeal.FoodClickListener
import com.example.singlemeal.R
import com.example.singlemeal.database.Food
import com.example.singlemeal.database.MealWithFoods

class MealFoodAdapter internal constructor (context: Context, private val foodClickListener: FoodClickListener<Food>
): RecyclerView.Adapter<MealFoodAdapter.MealFoodViewHolder>() {
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var mealFoods = emptyList<Food>()

    inner class MealFoodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val nameView: TextView = itemView.findViewById(R.id.mealFoodName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealFoodAdapter.MealFoodViewHolder {
        val itemView = inflater.inflate(R.layout.meal_food_item, parent, false)
        return MealFoodViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MealFoodAdapter.MealFoodViewHolder, position: Int) {
        val current = mealFoods[position]
        holder.nameView.text = current.name

        holder.itemView.setOnLongClickListener {
            foodClickListener.onFoodClickListener(current)
            true
        }
    }

    internal fun setMealFoods(mealFoods: List<Food>) {
        this.mealFoods = mealFoods
        notifyDataSetChanged()
    }

    override fun getItemCount() = mealFoods.size
}