package com.example

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.ui.AppBarConfiguration
import com.example.ViewModel.MainSearchActivityViewModel
import com.example.ViewModel.PropertyInfoViewModel
import com.example.data.PropertyInfoData
import com.example.databinding.ActivityMainBinding
import com.example.storage.SharedPrefManager
import com.squareup.picasso.Picasso

class PropertyInfoActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    lateinit var viewModel: PropertyInfoViewModel

    private fun fetch_property(){
        val token = SharedPrefManager.getInstance(this).user.token.toString()
        var property_id = intent.getIntExtra("propertyId", 0)

        if (property_id != null) {
            viewModel.get_property_info(token="Bearer "+token, propertyId=property_id.toInt())
        }


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.property_info_view)

        val bottomNavProfile = findViewById<ImageView>(R.id.bottomNavProfile)
        val bottomNavBooking = findViewById<ImageView>(R.id.bottomNavBooking)
        val bottomNavMainSearch = findViewById<ImageView>(R.id.bottomNavMainSearch)
        val bottomNavFavourites = findViewById<ImageView>(R.id.bottomNavFavourites)

        val bookingDiv = findViewById<LinearLayout>(R.id.bookingDiv)
        val bookVideoCallButton = findViewById<Button>(R.id.bookVideoCallButton)
        val dateTimePickerCancelButton = findViewById<Button>(R.id.dateTimePickerCancelButton)

        initViewModel()
        fetch_property()









        bookingDiv.visibility = LinearLayout.GONE

        bookVideoCallButton.setOnClickListener {
            bookingDiv.visibility = LinearLayout.VISIBLE
        }

        dateTimePickerCancelButton.setOnClickListener {
            bookingDiv.visibility = LinearLayout.GONE
        }

        bottomNavProfile.setOnClickListener {
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

    private fun initViewModel() {

        viewModel = ViewModelProvider(this).get(PropertyInfoViewModel::class.java)

        val a = viewModel.property.value

        viewModel.property.observe(this) {
            if (it == null) {


                Toast.makeText(this@PropertyInfoActivity, "Bad credentials", Toast.LENGTH_LONG).show()
            } else {

                property(it)
                println("hre")
            }
        }
    }

    private fun property(property: PropertyInfoData){
        val propertyInfoRooms = findViewById<TextView>(R.id.propertyInfoRooms)
        val propertyInfoSize = findViewById<TextView>(R.id.propertyInfoSize)
        val propertyInfoPrice = findViewById<TextView>(R.id.propertyInfoPrice)
        val propertyInfoAddress = findViewById<TextView>(R.id.propertyInfoAddress)
        val propertyInfoOwner = findViewById<TextView>(R.id.propertyInfoOwner)
        val propertyInfoImg = findViewById<ImageView>(R.id.propertyInfoImg)
        val propertyInfoInfo = findViewById<TextView>(R.id.propertyInfoInfo)

        propertyInfoRooms.text = property.rooms.toString()
        propertyInfoSize.text = property.area.toString()
        propertyInfoPrice.text = property.price.toString()
        propertyInfoAddress.text = property.address
        propertyInfoOwner.text = property.owner
        propertyInfoInfo.text = property.info

       // val imageBytes = Base64.decode(property.images[0], 0)
      //  val image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
     //   propertyInfoImg.setImageBitmap(image)
    }
}