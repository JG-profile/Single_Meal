package com.example.singlemeal.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Entity (tableName = "meal_table")
data class Meal (@PrimaryKey (autoGenerate = true) val mealId: Int,
                 val name: String,
                 val date: String)

@Entity (tableName = "food_table")
data class Food (@PrimaryKey (autoGenerate = true) val foodId: Int,
                 val name: String,
                 val type: String,
                 val date: String,
                 val servings: String)

@Entity(primaryKeys = ["mealId", "foodId"])
data class MealFoodCrossRef(
                val mealId: Int,
                val foodId: Int)

data class MealWithFoods(
    @Embedded val meal: Meal,
    @Relation(
        parentColumn = "mealId",
        entityColumn = "foodId",
        associateBy = Junction(MealFoodCrossRef::class)
    )
    val foods: List<Food>
)