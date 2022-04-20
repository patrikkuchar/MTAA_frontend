package com.example.Activity

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
import com.example.R
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
    private var Properties : Propety_list? = null
    private var Region_id: Int = 0
    private var Subregion_id: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_search_view)

        parentLinearLayout = findViewById(R.id.properties)

        listView = findViewById<ListView>(R.id.propertyListView)



        initViewModel()

        fetch_liked()




        val pricerange = findViewById<TextView>(R.id.priceRange)

        val arearange = findViewById<TextView>(R.id.areaRange)

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

        val slider_min_area = findViewById<Slider>(R.id.slider_min_area)
        val slider_max_area = findViewById<Slider>(R.id.slider_max_area)

        val slider_min = findViewById<Slider>(R.id.slider_min)
        val slider_max = findViewById<Slider>(R.id.slider_max)

        filterDiv.visibility = LinearLayout.GONE

        val user = SharedPrefManager.getInstance(this).user

        var greeting = findViewById<TextView>(R.id.greetingText)
        greeting.setText(user.name + " " + user.surname)

        profileButton.setOnClickListener {
            finish()
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        bottomNavFavourites.setOnClickListener {
            finish()
            startActivity(Intent(this, LikedPropertiesActivity::class.java))
        }

        bottomNavBooking.setOnClickListener {
            finish()
            startActivity(Intent(this, BookingActivity::class.java))
        }

        filterSubmitButton.setOnClickListener {
            filterDiv.visibility = LinearLayout.GONE
            fetch_properties(Properties!!,Subregion_id.toString(),Region_id.toString())

        }

        var min_price = 10000
        var max_price = 1000000

        var min_area = 10
        var max_area = 100

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

        slider_min_area.addOnChangeListener { slider, value, fromUser ->
            min_area = value.toInt()
            var min = min_area.toString()

            min = java.text.NumberFormat.getIntegerInstance().format(min.toInt()).replace(",", " ")


            var max = max_area.toString()

            max = java.text.NumberFormat.getIntegerInstance().format(max.toInt()).replace(",", " ")
            arearange.setText("$min" + " m " + " - " + "$max" + " m")
        }

        slider_max_area.addOnChangeListener { slider, value, fromUser ->
            max_area = value.toInt()
            var min = min_area.toString()

            min = java.text.NumberFormat.getIntegerInstance().format(min.toInt()).replace(",", " ")

            var max = max_area.toString()

            max = java.text.NumberFormat.getIntegerInstance().format(max.toInt()).replace(",", " ")
            arearange.setText("$min" + " m " + " - " + "$max" + " m")
        }







        //fetch_properties("0","0")


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


    private fun fetch_liked(){
        val token = SharedPrefManager.getInstance(this).user.token.toString()
        viewModel.get_liked(token="Bearer "+token)
        Thread.sleep(1_000)
        fetch_regions()

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
        while (Properties == null){
            println("Properties is null")
        }
        regionSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                // Display the selected item text on text view

                val selectedItem = parent.getItemAtPosition(position).toString()

                if (selectedItem.toString() == " - "){
                    fetch_properties(Properties!!,"0","0")
                    Toast.makeText(applicationContext, "Selected: All regions", Toast.LENGTH_LONG).show()
                }
                else{
                    for (region in array){
                        if (region.name == selectedItem.toString()){
                            val id = region.id.toInt()
                            Region_id = id
                            viewModel.liked.value?.let { fetch_properties(it,"0",id.toString()) }
                            fetch_subregions(id)
                        }
                        else if (selectedItem.toString() == " - "){
                            fetch_subregions(0)

                        }
                    }
                    Toast.makeText(applicationContext, "Selected: $selectedItem", Toast.LENGTH_LONG).show()
                }



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


                if (selectedItem.toString() == " - "){
                    Region_id = region_id.toInt()
                    fetch_properties(Properties!!,"0",region_id)
                    Toast.makeText(applicationContext, "Selected: All Subregions", Toast.LENGTH_LONG).show()
                }
                else{
                    for (subregion in array){
                        if (subregion.name == selectedItem.toString()){
                            val subregion_id = subregion.id.toInt()
                            Subregion_id = subregion_id
                            fetch_properties(Properties!!,subregion = subregion_id.toString(),region = region_id)
                        }
                    }
                    Toast.makeText(applicationContext, "Selected: $selectedItem", Toast.LENGTH_LONG).show()
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
            val itemAtPos = adapterView.getItemAtPosition(position) as Prop
            val itemIdAtPos = adapterView.getItemIdAtPosition(position)
            Toast.makeText(this, "Click on item at $itemAtPos its item id $itemIdAtPos", Toast.LENGTH_LONG).show()

            val propertyId = itemAtPos.id.toInt()


            val intent = (Intent(this, PropertyInfoActivity::class.java))
            intent.putExtra("propertyId", propertyId)
            startActivity(intent)

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



    private fun fetch_properties(likedlist:Propety_list, subregion: String,region: String) {
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
        val price_min = findViewById<com.google.android.material.slider.Slider>(R.id.slider_min)
        val price_max = findViewById<com.google.android.material.slider.Slider>(R.id.slider_max)
        val min = price_min.value.toInt()
        val max = price_max.value.toInt()


        //area
        val area_min = findViewById<com.google.android.material.slider.Slider>(R.id.slider_min_area)
        val area_max = findViewById<com.google.android.material.slider.Slider>(R.id.slider_max_area)
        val min_area = area_min.value.toInt()
        val max_area = area_max.value.toInt()


        val price_min_max = "$min"+"-"+"$max"

        val area_min_max = "$min_area"+"-"+"$max_area"

        //checkbox
        val rooms1 = findViewById<CheckBox>(R.id.rooms1).isChecked
        val rooms2 = findViewById<CheckBox>(R.id.rooms2).isChecked
        val rooms3 = findViewById<CheckBox>(R.id.rooms3).isChecked
        val rooms4 = findViewById<CheckBox>(R.id.rooms4).isChecked
        val rooms5 = findViewById<CheckBox>(R.id.rooms5).isChecked
        val rooms6 = findViewById<CheckBox>(R.id.rooms6).isChecked

        //String list of rooms based on checked checkboxes with delimeter -
        var rooms = ""
        if(rooms1){
            rooms += "1-"
        }
        if(rooms2){
            rooms += "2-"
        }
        if(rooms3){
            rooms += "3-"
        }
        if(rooms4){
            rooms += "4-"
        }
        if(rooms5){
            rooms += "5-"
        }
        if(rooms6){
            rooms += "6-"
        }
        if(rooms.length > 0){
            rooms = rooms.substring(0, rooms.length - 1)
        }

        val token = SharedPrefManager.getInstance(this).user.token.toString()
        viewModel.filter(token="Bearer "+token,region_id,subregion_id,price_min_max,area_min_max,rooms,likedlist)
    }

    private fun initViewModel() {

        viewModel = ViewModelProvider(this).get(MainSearchActivityViewModel::class.java)


        viewModel.properties.observe(this) {
            if (it == null) {
                Toast.makeText(this@MainSearchActivity, "Bad credentials", Toast.LENGTH_LONG).show()
            } else {
                var View = findViewById<ConstraintLayout>(R.id.my_root)
                Properties = it
                onAddField(View,it.properties)
            }
        }

        viewModel.regions.observe(this){

            if (it == null){
                Toast.makeText(this@MainSearchActivity, "Error occured", Toast.LENGTH_LONG).show()
            }
            else{
                if (Properties  != null){
                    regionSpinner(it)
                }

            }
        }

        viewModel.subregions.observe(this){
            if (it == null){
                Toast.makeText(this@MainSearchActivity, "Error occured", Toast.LENGTH_LONG).show()
            }
            else{
                subregionSpinner(it)
            }
        }

        viewModel.liked.observe(this){
            if (it == null){
                Toast.makeText(this@MainSearchActivity, "Error occured", Toast.LENGTH_LONG).show()
            }
            else{
                Properties = it
                fetch_properties(it,"0","0")
            }
        }
    }
}