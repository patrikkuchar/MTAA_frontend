package com.example

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.ui.AppBarConfiguration
import com.example.Adapters.PropertyAdapter
import com.example.ViewModel.MainSearchActivityViewModel
import com.example.data.*
import com.example.databinding.ActivityMainBinding
import com.example.storage.SharedPrefManager
import com.google.android.material.slider.Slider


class MainSearchActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private var parentLinearLayout: LinearLayout? = null
    private lateinit var listView: ListView
    lateinit var viewModel: MainSearchActivityViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_search_view)

       parentLinearLayout = findViewById(R.id.properties)

        listView = findViewById<ListView>(R.id.propertyListView)


        initViewModel()
        val pricerange = findViewById<TextView>(R.id.priceRange)
        val sellPropertyButton = findViewById<Button>(R.id.sellPropertyButton)
        val filterButton = findViewById<Button>(R.id.filterButton)
        val filterCloseButton = findViewById<Button>(R.id.filterCloseButton)
        var profileButton = findViewById<ImageView>(R.id.bottomNavProfile)
        var filterDiv = findViewById<LinearLayout>(R.id.filterDiv)
        val filterSubmitButton = findViewById<Button>(R.id.filterpotvr)
        var region = findViewById<Spinner>(R.id.regionSpinner)
        var subregion = findViewById<Spinner>(R.id.subregionSpinner)

        val bottomNavFavourites = findViewById<ImageView>(R.id.bottomNavFavourites)
        val bottomNavBooking = findViewById<ImageView>(R.id.bottomNavBooking)

        val slider_min = findViewById<Slider>(R.id.slider_min)
        val slider_max = findViewById<Slider>(R.id.slider_max)

        filterDiv.visibility = LinearLayout.GONE

        val test = SharedPrefManager.getInstance(this).user

        var greeting = findViewById<TextView>(R.id.greetingText)
        greeting.setText(test.name + " " + test.surname)

        profileButton.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        bottomNavFavourites.setOnClickListener {
            startActivity(Intent(this, LikedPropertiesActivity::class.java))
        }

        bottomNavBooking.setOnClickListener {
            startActivity(Intent(this, BookingActivity::class.java))
        }

        filterSubmitButton.setOnClickListener {
            val min = slider_min.value.toInt().toString()
            val max = slider_max.value.toInt().toString()
            val region_id = region.selectedItemId
            val subregion_id = subregion.selectedItemId
            println("AHOOOOJ")
        }

        var min_price = 10000
        var max_price = 1000000

        slider_min.addOnChangeListener { slider, value, fromUser ->
            min_price = value.toInt()
            var min = min_price.toString()

            min = java.text.NumberFormat.getIntegerInstance().format(min.toInt()).replace(",", " ")

            var max = max_price.toString()

            max = java.text.NumberFormat.getIntegerInstance().format(max.toInt()).replace(",", " ")
            pricerange.setText("$min"+" € " + " - " + "$max"+ " €")

        }

        slider_max.addOnChangeListener { slider, value, fromUser ->
            max_price = value.toInt()
            var min = min_price.toString()

            min = java.text.NumberFormat.getIntegerInstance().format(min.toInt()).replace(",", " ")


            var max = max_price.toString()

            max = java.text.NumberFormat.getIntegerInstance().format(max.toInt()).replace(",", " ")
            pricerange.setText("$min"+" € " + " - " + "$max"+ " €")

        }


        fetch_regions()

        fetch_properties("0","0")


        sellPropertyButton.setOnClickListener {
            startActivity(Intent(this, SellPropertyActivity::class.java))
        }

        filterCloseButton.setOnClickListener {
            filterDiv.visibility = LinearLayout.GONE
            println("AHOOOJ")

        }

        filterButton.setOnClickListener {
            filterDiv.visibility = LinearLayout.VISIBLE
        }


    }

    private fun fetch_regions() {
        val token = SharedPrefManager.getInstance(this).user.token.toString()
        viewModel.get_regions(token="Bearer "+token)
    }


    private fun fetch_subregions(id: Int) {
        if (id != 0){
            val token = SharedPrefManager.getInstance(this).user.token.toString()
            viewModel.get_subregions(token = "Bearer "+token,id)
        }
        else{


            var myEmptyList = listOf<Subregion>()

            var subregions = Subregion_list(myEmptyList)


            subregionSpinner(subregions)
        }

    }

    fun regionSpinner(regions: Region_List) {
        val regionSpinner = findViewById<Spinner>(R.id.regionSpinner)

        var regionList : MutableList<String> = ArrayList()


        val array: Array<Region> = regions.regions.toTypedArray()

        regionList.add(" - ")
        var i = 0
        for (one in array){
            one.name?.let { regionList.add(it) }
        }

        val regionAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item,regionList)
        regionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        regionSpinner.adapter = regionAdapter
        //on click listener
        regionSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                // Display the selected item text on text view
                val selectedItem = parent.getItemAtPosition(position).toString()
                for (region in array){
                    if (region.name == selectedItem.toString()){
                        val id = region.id.toInt()
                        fetch_properties("0",id.toString())
                        fetch_subregions(id)
                    }
                    else if (selectedItem.toString() == " - "){
                        fetch_subregions(0)

                    }
                }
                Toast.makeText(applicationContext, "Selected: $selectedItem", Toast.LENGTH_LONG).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Another interface callback
            }
        }


    }


    fun subregionSpinner(subregions: Subregion_list){

        val regionSpinner = findViewById<Spinner>(R.id.regionSpinner)

        val region_id = regionSpinner.selectedItemId.toString()


        val subregionSpinner = findViewById<Spinner>(R.id.subregionSpinner)

        var subregionList : MutableList<String> = ArrayList()


        val array: Array<Subregion> = subregions.subregions.toTypedArray()

        subregionList.add(" - ")
        var i = 0
        for (one in array){
            one.name?.let { subregionList.add(it) }
        }

        if (subregions.subregions.size == 0){
            subregionList.clear()
        }


        val subregionAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item,subregionList)
        subregionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        subregionSpinner.adapter = subregionAdapter
        //on click listener
        subregionSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                // Display the selected item text on text view
                val selectedItem = parent.getItemAtPosition(position).toString()
                Toast.makeText(applicationContext, "Selected: $selectedItem", Toast.LENGTH_LONG).show()

                for (subregion in array){
                    if (subregion.name == selectedItem.toString()){
                        val subregion_id = subregion.id.toInt()
                        //fetch_properties(subregion_id.toString(),region_id)
                    }
                    else if (selectedItem.toString() == " - "){
                        //fetch_properties("0",region_id)
                    }
                }


            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Another interface callback
            }
        }

    }

    fun onAddField(view: View,properties: List<Prop>) {





        var adapter1 = PropertyAdapter(this, properties as ArrayList<Prop>)
        adapter1.notifyDataSetInvalidated()
        adapter1.notifyDataSetChanged()


        var listView = findViewById<ListView>(R.id.propertyListView)
        var propertyDiv = findViewById<LinearLayout>(R.id.property_modelll)

        listView.adapter = adapter1

        listView.setOnItemClickListener(){adapterView, view, position, id ->
            val itemAtPos = adapterView.getItemAtPosition(position)
            val itemIdAtPos = adapterView.getItemIdAtPosition(position)
            Toast.makeText(this, "Click on item at $itemAtPos its item id $itemIdAtPos", Toast.LENGTH_LONG).show()
        }

        val params: ViewGroup.LayoutParams = listView.layoutParams
        val propertyParams: ViewGroup.LayoutParams = propertyDiv.layoutParams
        //params.height = (propertyParams.height + adapter1.dpToPx(20)) * properties.size
        params.height = adapter1.dpToPx(140) * properties.size + adapter1.dpToPx(20)
        listView.layoutParams = params


        /*
        println("HERE")
        val inflater =
            getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rowView: View = inflater.inflate(R.layout.property_homepage, null)
        parentLinearLayout!!.addView(rowView, parentLinearLayout!!.childCount - 1)


         */

    }

    fun add_favourite(){

    }

    private fun fetch_properties(subregion: String,region: String) {
        var subregion_id = ""
        var region_id = ""

        if (subregion.toInt() == 0 && region.toInt() != 0){
            subregion_id = ""
            region_id = region.toString()
        }
        else if(subregion.toInt() == 0 && region.toInt() == 0){
            subregion_id = ""
            region_id = ""
        }
        else{
            subregion_id = subregion.toString()
            region_id = region.toString()
        }
        val price_min_max = ""

        val area_min_max = ""

        val rooms = ""

        val token = SharedPrefManager.getInstance(this).user.token.toString()
        viewModel.filter(token="Bearer "+token,region_id,subregion_id,price_min_max,area_min_max,rooms)
    }

    private fun initViewModel() {

        println("SS")
        viewModel = ViewModelProvider(this).get(MainSearchActivityViewModel::class.java)


        viewModel.properties.observe(this) {
            if (it == null) {


                Toast.makeText(this@MainSearchActivity, "Bad credentials", Toast.LENGTH_LONG).show()
            } else {
                var View = findViewById<ConstraintLayout>(R.id.my_root)
                onAddField(View,it.properties)
                println("hre")
            }
        }

        viewModel.regions.observe(this){

            if (it == null){
                Toast.makeText(this@MainSearchActivity, "Error occured", Toast.LENGTH_LONG).show()
            }
            else{
                regionSpinner(it)
            }
        }

        viewModel.subregions.observe(this){
            if (it == null){
                Toast.makeText(this@MainSearchActivity, "Error occured", Toast.LENGTH_LONG).show()
            }
            else{
                println("HERE I AM :D")
                subregionSpinner(it)
            }
        }
    }
}