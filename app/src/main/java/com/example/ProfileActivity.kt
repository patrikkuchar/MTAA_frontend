package com.example

import android.os.Bundle
import android.widget.Button
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