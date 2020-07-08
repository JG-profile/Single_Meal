package com.example.singlemeal

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.transition.Slide
import android.transition.TransitionManager
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.singlemeal.adapters.FoodAdapter
import com.example.singlemeal.adapters.MealAdapter
import com.example.singlemeal.adapters.MealFoodAdapter
import com.example.singlemeal.database.Food
import com.example.singlemeal.database.Meal
import com.example.singlemeal.database.MealFoodCrossRef
import com.example.singlemeal.database.MealViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlin.properties.Delegates

class AddMealActivity () : AppCompatActivity(), FoodClickListener<Food> {

    private lateinit var mealViewModel: MealViewModel
    private lateinit var editNameView: TextView
    private lateinit var editDateView: TextView
    private lateinit var editProteinView: TextView
    private lateinit var editCarbView: TextView
    private lateinit var editVegView: TextView
    private lateinit var nameSent: String
    private lateinit var dateSent: String
    //private lateinit var mealFood: List<Food>
    private var idSent by Delegates.notNull<Int>()

    private var foodPopup:PopupWindow? = null
    private var numVeg: Int = 0
    private var numCarb: Int = 0
    private var numProt: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.meal_activity)
        editNameView = findViewById(R.id.mealActivityName)
        editDateView = findViewById(R.id.mealActivityDate)
        editProteinView = findViewById(R.id.mealActivityProtein)
        editCarbView = findViewById(R.id.mealActivityCarbs)
        editVegView = findViewById(R.id.mealActivityVeg)

        val extras = intent.extras
        if (extras != null)
        {
            nameSent = extras.getString(MainActivity.EXTRA_NAME).toString()
            dateSent = extras.getString(MainActivity.EXTRA_DATE).toString()
            idSent = extras.getInt(MainActivity.EXTRA_ID)
            editDateView.text = dateSent
            editNameView.text = nameSent
        }


        val recyclerView = findViewById<RecyclerView>(R.id.mealFoodRecyclerView)
        val adapter = MealFoodAdapter(this, this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        mealViewModel = ViewModelProvider(this).get(MealViewModel::class.java)

        mealViewModel.mealWithFoods(idSent).observe(this, Observer { foods ->
            // Update the cached copy of the words in the adapter.
            foods?.let { adapter.setMealFoods(it[0].foods)
            var i = 0
                numVeg = 0
                numCarb = 0
                numProt = 0
                while (i<it[0].foods.size)
                {
                    if (it[0].foods[i].type == "Veg")
                    {
                        numVeg++
                    }else if (it[0].foods[i].type == "Protein")
                    {
                        numProt++
                    }else
                    {
                        numCarb++
                    }
                    i++
                }
                editCarbView.text = (getString(R.string.carbs) + numCarb.toString())
                editVegView.text = (getString(R.string.veg) + numVeg.toString())
                editProteinView.text = (getString(R.string.protein) + numProt.toString())
            }
        })

        val fab = findViewById<FloatingActionButton>(R.id.mealActivityFab)
        fab.setOnClickListener {
            dismissPopup()
            foodPopup = showAllFood()
            foodPopup?.isOutsideTouchable = true
            foodPopup?.isFocusable = true

            foodPopup?.showAtLocation(findViewById(R.id.meal_activity_layout), Gravity.CENTER, 0, 0)
        }

    }

    override fun onStop()
    {
        super.onStop()
        dismissPopup()
    }

    private fun dismissPopup()
    {
        foodPopup?.let {
            if(it.isShowing){
                it.dismiss()
            }
            foodPopup = null
        }
    }

    private fun showAllFood(): PopupWindow
    {
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.food_popup, null)
        val recyclerView = view.findViewById<RecyclerView>(R.id.foodPopupRV)

        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = FoodAdapter(this)
        recyclerView.adapter = adapter

        recyclerView.addItemDecoration(DividerItemDecoration(recyclerView.context, DividerItemDecoration.VERTICAL))


        mealViewModel.allFoods.observe(this, Observer { meals ->
            // Update the cached copy of the words in the adapter.
            meals?.let { adapter.setFoods(it) }
        })

        adapter.setOnClick(object : FoodClickListener<Food>{
            override fun onFoodClickListener(food: Food) {
                val servings = food.servings.toInt() - 1
                val newFood = Food(food.foodId, food.name, food.type, food.date, servings.toString())
                mealViewModel.insert(MealFoodCrossRef(idSent,food.foodId))
                mealViewModel.update(newFood)
                dismissPopup()
            }
        })
        return PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onFoodClickListener(food: Food) {

        lateinit var dialog: AlertDialog

        // Initialize a new instance of alert dialog builder object
        val builder = AlertDialog.Builder(this)

        // Set a title for alert dialog
        builder.setTitle("Delete Food")

        // Set a message for alert dialog
        builder.setMessage("Would you like to delete the " + food.name + "?")


        // On click listener for dialog buttons
        val dialogClickListener = DialogInterface.OnClickListener{ _, which ->
            when(which){
                DialogInterface.BUTTON_POSITIVE ->
                {
                    mealViewModel.deleteSelected(idSent, food.foodId)
                    val servings = food.servings.toInt() + 1
                    var updateFood = Food(food.foodId, food.name, food.type, food.date, servings.toString())
                    mealViewModel.update(updateFood)
                }
            }
        }

        // Set the alert dialog positive/yes button
        builder.setPositiveButton("YES",dialogClickListener)

        // Set the alert dialog negative/no button
        builder.setNegativeButton("NO",dialogClickListener)

        // Set the alert dialog negative/no button
        builder.setNeutralButton("CANCEL",dialogClickListener)

        // Initialize the AlertDialog using builder object
        dialog = builder.create()

        // Finally, display the alert dialog
        dialog.show()
    }
}

