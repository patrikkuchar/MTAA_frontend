package com.example.Activity

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.ui.AppBarConfiguration
import com.example.R
import com.example.ViewModel.PropertyInfoViewModel
import com.example.data.PropertyInfoData
import com.example.databinding.ActivityMainBinding
import com.example.storage.SharedPrefManager

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

        val calendarPicker = findViewById<CalendarView>(R.id.calendarPicker)
        val booking_hoursInput = findViewById<EditText>(R.id.booking_hoursInput)
        val booking_minutesInput = findViewById<EditText>(R.id.booking_minutesInput)
        val dateTimePickerComfirmButton = findViewById<Button>(R.id.dateTimePickerComfirmButton)

        var booking_date = ""

        initViewModel()
        fetch_property()





        bookingDiv.visibility = LinearLayout.GONE

        calendarPicker.setOnDateChangeListener { calendarView, year, month, day ->
            booking_date = "$year-${month+1}-$day"
        }

        dateTimePickerComfirmButton.setOnClickListener {
            if (booking_hoursInput.text.toString().isNotEmpty() && booking_minutesInput.text.toString().isNotEmpty()){
                val hours = booking_hoursInput.text.toString().toInt()
                val minutes = booking_minutesInput.text.toString().toInt()
                if (hours < 0 || hours > 23 || minutes < 0 || minutes > 59){
                    Toast.makeText(this, "Please enter valid time", Toast.LENGTH_SHORT).show()
                }
                else{
                    booking_date += " ${hours.toString()}:${minutes.toString()}:00"

                    val token = SharedPrefManager.getInstance(this).user.token.toString()
                    var property_id = intent.getIntExtra("propertyId", 0)

                    if (property_id != null) {
                        viewModel.add_booking(token="Bearer "+token, propertyId=property_id.toInt(), startDate=booking_date)

                        Toast.makeText(this, "Booking added", Toast.LENGTH_SHORT).show()
                        bookingDiv.visibility = LinearLayout.GONE
                    }
                }
            }
            else
                Toast.makeText(this, "Please enter valid time", Toast.LENGTH_SHORT).show()
        }






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
            finish()
            startActivity(Intent(this, BookingActivity::class.java))
        }

        bottomNavMainSearch.setOnClickListener {
            finish()
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

        var urlka = "http://maps.google.com/maps?q=loc:"+property.address
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(urlka))

        propertyInfoImg.setOnClickListener(View.OnClickListener {
            startActivity(intent, null)
        })

        val propertyInfoInfo = findViewById<TextView>(R.id.propertyInfoInfo)

        propertyInfoRooms.text = property.rooms.toString()
        propertyInfoSize.text = property.area.toString()
        propertyInfoPrice.text = property.price.toString()
        propertyInfoAddress.text = property.address
        propertyInfoOwner.text = property.owner
        propertyInfoInfo.text = property.info

        val imageBytes = Base64.decode(property.images[0], 0)
        val image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        propertyInfoImg.setImageBitmap(image)
    }
}