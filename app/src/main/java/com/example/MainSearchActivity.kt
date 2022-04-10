package com.example

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        parentLinearLayout = findViewById(R.id.properties)

        listView = findViewById<ListView>(R.id.propertyListView)

        val propertylist = Property.getRecipesFromFile("recipes.json", this)

        val adapter = PropertyAdapter(this, propertylist)

        listView.adapter = adapter



        val sellPropertyButton = findViewById<Button>(R.id.sellPropertyButton)
        val filterButton = findViewById<Button>(R.id.filterButton)
        val filterCloseButton = findViewById<Button>(R.id.filterCloseButton)
        var profileButton = findViewById<ImageView>(R.id.bottomNavProfile)
        var filterDiv = findViewById<LinearLayout>(R.id.filterDiv)
        filterDiv.visibility = LinearLayout.GONE

        val test = SharedPrefManager.getInstance(this).user

        var greeting = findViewById<TextView>(R.id.greetingText)
        greeting.setText(test.name + " " + test.surname)

        profileButton.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }


        var View = findViewById<ConstraintLayout>(R.id.my_root)
        onAddField(View)


        regionSpinner()


        sellPropertyButton.setOnClickListener {
            startActivity(Intent(this, SellPropertyActivity::class.java))
        }

        filterCloseButton.setOnClickListener {
            filterDiv.visibility = LinearLayout.GONE
        }

        filterButton.setOnClickListener {
            filterDiv.visibility = LinearLayout.VISIBLE
        }
    }

    fun regionSpinner() {
        val regionSpinner = findViewById<Spinner>(R.id.regionSpinner)
        val regionList = arrayOf("Bratislavský kraj", "Banskobystrický kraj", "Trnavský kraj")
        val regionAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, regionList)
        regionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        regionSpinner.adapter = regionAdapter
        //on click listener
        regionSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                // Display the selected item text on text view
                val selectedItem = parent.getItemAtPosition(position).toString()
                Toast.makeText(applicationContext, "Selected: $selectedItem", Toast.LENGTH_LONG).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Another interface callback
            }
        }
    }

    fun onAddField(view: View) {
        var oneProperty = Property(4, 3, 90, 230000, "9.8.2009", "daco", "daco", "daco")

        var properties = arrayListOf<Property>()
        properties.add(oneProperty)
        properties.add(oneProperty)
        properties.add(oneProperty)

        val adapter = PropertyAdapter(this, properties)

        var listView = findViewById<ListView>(R.id.propertyListView)

        var propertyDiv = findViewById<LinearLayout>(R.id.property_model)

        listView.adapter = adapter

        val params: ViewGroup.LayoutParams = listView.layoutParams
        val propertyParams: ViewGroup.LayoutParams = propertyDiv.layoutParams
        params.height = (propertyParams.height + adapter.dpToPx(20)) * properties.size
        listView.layoutParams = params

        println("HERE")
        val inflater =
            getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rowView: View = inflater.inflate(R.layout.property_homepage, null)
        parentLinearLayout!!.addView(rowView, parentLinearLayout!!.childCount - 1)
    }
}