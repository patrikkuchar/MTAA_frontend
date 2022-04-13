package com.example

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import com.example.databinding.ActivityMainBinding

class PropertyInfoActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

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
}