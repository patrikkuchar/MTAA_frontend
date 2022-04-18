package com.example.Activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.ui.AppBarConfiguration
import com.example.R
import com.example.ViewModel.PropertyInfoViewModel
import com.example.ViewModel.SellPropertyActivityViewModel
import com.example.data.*
import com.example.databinding.ActivityMainBinding
import com.example.storage.SharedPrefManager
import org.w3c.dom.Text
import java.io.ByteArrayOutputStream
import java.io.InputStream


class SellPropertyActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private var Imagelist: ArrayList<String> = ArrayList()
    lateinit var viewModel: SellPropertyActivityViewModel
    private var Region_id: Int = 0
    private var Subregion_id: Int = 0
    private var Property: PropertyInfoData? = null
    private var Property_Images : List<String> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sell_property_view)


        val profileButton = findViewById<ImageView>(R.id.bottomNavProfile)
        val bottomNavBooking = findViewById<ImageView>(R.id.bottomNavBooking)
        val bottomNavMainSearch = findViewById<ImageView>(R.id.bottomNavMainSearch)
        val bottomNavFavourites = findViewById<ImageView>(R.id.bottomNavFavourites)
        val addImagesButton =  findViewById<Button>(R.id.addImagesButton)
        val sellPropertyButton = findViewById<Button>(R.id.SELL_porperty_add)
        val uploaded_images = findViewById<TextView>(R.id.uploaded_images_text)
        val regionSpinner = findViewById<Spinner>(R.id.Sell_region_spinner)
        val subregionSpinner = findViewById<Spinner>(R.id.Sell_subregion_spinner)

        val sellPropertyLoggedUser = findViewById<TextView>(R.id.sellPropertyLoggedUser)

        val user = SharedPrefManager.getInstance(this).user
        sellPropertyLoggedUser.setText("logged as " + user.name + " " + user.surname)


        initViewModel()

        val edit_property = intent.getIntExtra("editProperty", 0)
        val property_id = intent.getIntExtra("propertyId", 0)

        if (edit_property == 1) {
            val token = SharedPrefManager.getInstance(this).user.token.toString()
            viewModel.get_property(token = "Bearer " + token, property_id)

            sellPropertyButton.setText("Edit Property")

            sellPropertyButton.setOnClickListener{ view ->
                println("here1")
                val rooms = findViewById<TextView>(R.id.sell_rooms).text.toString()
                val area = findViewById<TextView>(R.id.sell_area).text.toString()
                val price = findViewById<TextView>(R.id.sell_price).text.toString()
                val address = findViewById<TextView>(R.id.sell_address).text.toString()
                val description = findViewById<TextView>(R.id.sell_description).text.toString()
                var request = EditPropertyRequest(rooms.toInt(), area.toInt(), price.toInt(),Subregion_id, address, description)
                viewModel.edit_property(token = "Bearer " + token,request,property_id)

                val request2 = EditImagesRequest(0,Property_Images)

                viewModel.edit_photos(token = "Bearer " + token,request2,property_id)
            }
        }
        else{
            fetch_regions()

            sellPropertyButton.setOnClickListener { view ->
                println("here2")
                if (Imagelist.size < 3){
                    Toast.makeText(this, "Minimum 3 pictures", Toast.LENGTH_SHORT).show()
                    Imagelist.clear()
                }
                else{
                    val rooms = findViewById<TextView>(R.id.sell_rooms).text.toString()
                    val area = findViewById<TextView>(R.id.sell_area).text.toString()
                    val price = findViewById<TextView>(R.id.sell_price).text.toString()
                    val address = findViewById<TextView>(R.id.sell_address).text.toString()
                    val description = findViewById<TextView>(R.id.sell_description).text.toString()

                    val propertyRequest = SellPropertyRequest(rooms.toInt(), area.toInt(), price.toInt(),Subregion_id, address, description,1, Imagelist)
                    val token = SharedPrefManager.getInstance(this).user.token.toString()
                    viewModel.sell_property(token="Bearer "+token,propertyRequest)

                }
            }

        }



        var context: Context? = null
        var PICK_IMAGE_MULTIPLE = 1
        lateinit var imagePath: String
        var imagesPathList: MutableList<String> = arrayListOf()






        var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data

                var mylist: ArrayList<String> = ArrayList()
                val data1 = result.data


                var velkost = data1?.clipData?.itemCount
                var listttt = data1?.clipData
                if (velkost != null){
                    var count = 0
                    while ( count != velkost){
                        println(count)
                        mylist.add(listttt?.getItemAt(count)?.uri.toString())
                        count++
                    }
                    for (image in mylist) {
                        val inputStream1 = image.let { contentResolver.openInputStream(it.toUri()) }
                        val byteArrayOutputStream = ByteArrayOutputStream()
                        inputStream1?.copyTo(byteArrayOutputStream)
                        val bytes = byteArrayOutputStream.toByteArray()
                        val stringbytes = Base64.encode(bytes, 0)
                        val string = String(stringbytes)
                        val a = string.replace("\n", "")
                        Imagelist.add(a)

                    }
                    var pocet = mylist.size
                    if (pocet < 3){
                        //Create a Toast minimum 3 pictures
                        Toast.makeText(this, "Minimum 3 pictures", Toast.LENGTH_SHORT).show()
                        Imagelist.clear()
                    }
                    else{
                        uploaded_images.setText(Imagelist.size.toString()+"uploaded (min 3)")

                    }
                }
                else{
                    Toast.makeText(this, "minimum is 3", Toast.LENGTH_SHORT).show()
                }

                println("AHOJDASODA")
            }
        }



        addImagesButton.setOnClickListener { view ->
            var intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "image/*"
            resultLauncher.launch(intent)
            var pocet = Imagelist.size
            println(pocet)
        }


        profileButton.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        bottomNavBooking.setOnClickListener {
            startActivity(Intent(this, BookingActivity::class.java))
        }

        bottomNavMainSearch.setOnClickListener {
            startActivity(Intent(this, MainSearchActivity::class.java))
        }

        bottomNavFavourites.setOnClickListener {
            startActivity(Intent(this, LikedPropertiesActivity::class.java))
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
        val regionSpinner = findViewById<Spinner>(R.id.Sell_region_spinner)

        var regionList : MutableList<String> = ArrayList()

        val array: Array<Region> = regions.regions.toTypedArray()

        regionList.add(" - ")
        var i = 0
        for (one in array){
            one.name?.let { regionList.add(it) }
            if (one.name == Property?.region){
                Region_id = one.id.toInt()

            }
        }
        val regionAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item,regionList)
        regionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        regionSpinner.adapter = regionAdapter
        regionSpinner.setSelection(Region_id)


        regionSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                // Display the selected item text on text view

                val selectedItem = parent.getItemAtPosition(position).toString()

                if (selectedItem.toString() == " - "){
                    Toast.makeText(applicationContext, "Selected: All regions", Toast.LENGTH_LONG).show()
                }
                else{
                    for (region in array){
                        if (region.name == selectedItem.toString()){
                            val id = region.id.toInt()
                            Region_id = id
                            fetch_subregions(id)
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

        val regionSpinner = findViewById<Spinner>(R.id.Sell_region_spinner)

        val region_id = regionSpinner.selectedItemId.toString()


        val subregionSpinner = findViewById<Spinner>(R.id.Sell_subregion_spinner)

        var subregionList : MutableList<String> = ArrayList()


        val array: Array<Subregion> = subregions.subregions.toTypedArray()

        subregionList.add(" - ")
        var i = 0
        for (one in array){
            one.name?.let { subregionList.add(it) }
            if (one.name == Property?.subregion){
                Subregion_id = one.id.toInt()
                subregionSpinner.setSelection(Subregion_id)
            }
        }

        if (subregions.subregions.size == 0){
            subregionList.clear()
        }


        val subregionAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item,subregionList)
        subregionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        subregionSpinner.adapter = subregionAdapter

        subregionSpinner.setSelection(Subregion_id)
        //on click listener
        subregionSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                // Display the selected item text on text view
                val selectedItem = parent.getItemAtPosition(position).toString()
                if (selectedItem.toString() == " - "){
                    Region_id = region_id.toInt()

                    Toast.makeText(applicationContext, "Selected: All Subregions", Toast.LENGTH_LONG).show()
                }
                else{
                    for (subregion in array){
                        if (subregion.name == selectedItem.toString()){
                            val subregion_id = subregion.id.toInt()
                            Subregion_id = subregion_id

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


    private fun initViewModel() {

        viewModel = ViewModelProvider(this).get(SellPropertyActivityViewModel::class.java)


        viewModel.response_code.observe(this) {
            if (it == null) {
                Toast.makeText(this@SellPropertyActivity, "Bad credentials", Toast.LENGTH_LONG).show()
            } else {
                if (it.toInt() == 201) {
                    Toast.makeText(this@SellPropertyActivity, "Success", Toast.LENGTH_LONG).show()
                    val intent = Intent(this@SellPropertyActivity, MainSearchActivity::class.java)
                    startActivity(intent)
                } else {
                    println("Error: " + it)
                    Toast.makeText(this@SellPropertyActivity, "Erorr occured", Toast.LENGTH_LONG).show()
                }
            }
        }

        viewModel.regions.observe(this){

            if (it == null){
                Toast.makeText(this@SellPropertyActivity, "Error occured", Toast.LENGTH_LONG).show()
            }
            else {
                regionSpinner(it)
            }


        }

        viewModel.subregions.observe(this){
            if (it == null){
                Toast.makeText(this@SellPropertyActivity, "Error occured", Toast.LENGTH_LONG).show()
            }
            else{
                println("HERE I AM :D")
                subregionSpinner(it)
            }
        }

        viewModel.property.observe(this){
            if (it == null){
                Toast.makeText(this@SellPropertyActivity, "Error occured", Toast.LENGTH_LONG).show()
            }
            else{
                val rooms = findViewById<TextView>(R.id.sell_rooms)
                val area = findViewById<TextView>(R.id.sell_area)
                val price = findViewById<TextView>(R.id.sell_price)
                val address = findViewById<TextView>(R.id.sell_address)
                val description = findViewById<TextView>(R.id.sell_description)
                val image_text = findViewById<TextView>(R.id.uploaded_images_text)
                val regionSpinner = findViewById<Spinner>(R.id.Sell_region_spinner)
                val subregionSpinner = findViewById<Spinner>(R.id.Sell_subregion_spinner)

                Property = it.property

                rooms.setText(it.property.rooms.toString())
                area.setText(it.property.area.toString())
                price.setText(it.property.price.toString())
                address.setText(it.property.address)
                description.setText(it.property.info)
                var images = it.property.images
                Property_Images = it.property.images
                image_text.setText(images.size.toString() + " images uploaded")

                fetch_regions()


            }
        }
    }


}