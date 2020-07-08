package com.example.singlemeal.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.singlemeal.CellClickListener
import com.example.singlemeal.FoodClickListener
import com.example.singlemeal.database.Meal
import com.example.singlemeal.R
import com.example.singlemeal.database.Food

class FoodAdapter internal constructor (context: Context): RecyclerView.Adapter<FoodAdapter.FoodViewHolder>() {
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var foods = emptyList<Food>() // Cached copy of words
    var callback: FoodClickListener<Food>? = null

    inner class FoodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val nameView: TextView = itemView.findViewById(R.id.foodName)
        val typeView: ImageView = itemView.findViewById(R.id.foodType)
        val view: View = itemView

        var filterLayout: ConstraintLayout = itemView.findViewById(R.id.food_item_layout)
        init {
            setClickListener(filterLayout)
        }

        private fun setClickListener(view: View) {
            view.setOnClickListener {
                callback?.onFoodClickListener(foods[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val itemView = inflater.inflate(R.layout.food_item, parent, false)
        return FoodViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val current = foods[position]
        holder.nameView.text = current.name

        if (current.type == "Protein")
        {
            holder.typeView.setImageResource(R.drawable.ic_meat)
        }else if (current.type == "Carb")
        {
            holder.typeView.setImageResource(R.drawable.ic_bakery)
        }else
        {
            holder.typeView.setImageResource(R.drawable.ic_vegetable)
        }

    }

    internal fun setFoods(foods: List<Food>) {
        this.foods = foods
        notifyDataSetChanged()
    }

    fun setOnClick(click: FoodClickListener<Food>){
        callback = click
    }

    override fun getItemCount() = foods.size




}