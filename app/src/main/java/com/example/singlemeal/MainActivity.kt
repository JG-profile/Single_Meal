package com.example.singlemeal

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.singlemeal.adapters.MealAdapter
import com.example.singlemeal.database.Meal
import com.example.singlemeal.database.MealViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), CellClickListener {

    private lateinit var mealViewModel: MealViewModel
    private val newFoodActivityRequestCode = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.mealRecyclerview)
        val adapter = MealAdapter(this, this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        mealViewModel = ViewModelProvider(this).get(MealViewModel::class.java)

        mealViewModel.allMealsByDate.observe(this, Observer { meals ->
            // Update the cached copy of the words in the adapter.
            meals?.let { adapter.setMeals(it) }
        })

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val meal = Meal(0, "Lunch", "2020/07/07")
            mealViewModel.insert(meal)
        }

    }

    override fun onCellLongClickListener(id: Int, name: String) {

        lateinit var dialog: AlertDialog

        // Initialize a new instance of alert dialog builder object
        val builder = AlertDialog.Builder(this)

        // Set a title for alert dialog
        builder.setTitle("Delete Food")

        // Set a message for alert dialog
        builder.setMessage("Would you like to delete the " + name + "?")


        // On click listener for dialog buttons
        val dialogClickListener = DialogInterface.OnClickListener{ _, which ->
            when(which){
                DialogInterface.BUTTON_POSITIVE -> mealViewModel.deleteSelected(id)
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

    override fun onCellClickListener(meal: Meal)
    {
        val intent = Intent(this@MainActivity, AddMealActivity::class.java)
        intent.putExtra(EXTRA_NAME, meal.name)
        intent.putExtra(EXTRA_DATE, meal.date)
        intent.putExtra(EXTRA_ID, meal.mealId)
        startActivityForResult(intent, newFoodActivityRequestCode)
    }

    companion object {
        const val EXTRA_NAME = "com.example.singlemeal.NAME"
        const val EXTRA_DATE = "com.example.singlemeal.DATE"
        const val EXTRA_ID = "com.example.singlemeal.ID"
    }
}