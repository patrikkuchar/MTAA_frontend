package com.example

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import com.example.databinding.ActivityMainBinding

class ProfileActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_view)

        val deletePropertyButton = findViewById<Button>(R.id.deletePropertyButton)
        var deletePropertyDiv = findViewById<LinearLayout>(R.id.deletePropertyDiv)
        val profileDecisionYesButton = findViewById<Button>(R.id.profileDecisionYesButton)
        val profileDecisionNoButton = findViewById<Button>(R.id.profileDecisionNoButton)

        val bottomNavBooking = findViewById<ImageView>(R.id.bottomNavBooking)
        val bottomNavMainSearch = findViewById<ImageView>(R.id.bottomNavMainSearch)
        val bottomNavFavourites = findViewById<ImageView>(R.id.bottomNavFavourites)

        bottomNavBooking.setOnClickListener {
            startActivity(Intent(this, BookingActivity::class.java))
        }

        bottomNavMainSearch.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        bottomNavFavourites.setOnClickListener {
            startActivity(Intent(this, LikedPropertiesActivity::class.java))
        }

        deletePropertyDiv.visibility = LinearLayout.GONE

        deletePropertyButton.setOnClickListener {
            deletePropertyDiv.visibility = LinearLayout.VISIBLE
        }

        profileDecisionYesButton.setOnClickListener {
            deletePropertyDiv.visibility = LinearLayout.GONE
        }

        profileDecisionNoButton.setOnClickListener {
            deletePropertyDiv.visibility = LinearLayout.GONE
        }
    }
}