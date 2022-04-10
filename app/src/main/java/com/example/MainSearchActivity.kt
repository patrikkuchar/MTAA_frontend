package com.example

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.ui.AppBarConfiguration
import com.example.Adapters.PropertyAdapter
import com.example.data.Property
import com.example.databinding.ActivityMainBinding
import com.example.storage.SharedPrefManager

class MainSearchActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private var parentLinearLayout: LinearLayout? = null
    private lateinit var listView: ListView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_search_view)

        parentLinearLayout = findViewById(R.id.property)

        listView = findViewById<ListView>(R.id.property_list_view)

        val propertylist = Property.getRecipesFromFile("recipes.json", this)

        val adapter = PropertyAdapter(this, propertylist)

        listView.adapter = adapter



        val sellPropertyButton = findViewById<Button>(R.id.sellPropertyButton)
        val filterButton = findViewById<Button>(R.id.filterButton)
        val filterCloseButton = findViewById<Button>(R.id.filterCloseButton)
        var filterDiv = findViewById<LinearLayout>(R.id.filterDiv)

        val test = SharedPrefManager.getInstance(this).user

        var greeting = findViewById<TextView>(R.id.greetingText)
        greeting.setText(test.name + " " + test.surname)


        var View = findViewById<ConstraintLayout>(R.id.my_root)
        onAddField(View)





        sellPropertyButton.setOnClickListener {
            startActivity(Intent(this, SellPropertyActivity::class.java))
        }

        filterCloseButton.setOnClickListener {
            filterDiv.visibility = LinearLayout.GONE
        }

        filterButton.setOnClickListener {
            filterDiv.visibility = LinearLayout.GONE
        }



    }


    fun onAddField(view: View) {
        println("HERE")
        val inflater =
            getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rowView: View = inflater.inflate(R.layout.property_homepage, null)
        parentLinearLayout!!.addView(rowView, parentLinearLayout!!.childCount - 1)
    }
}