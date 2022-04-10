package com.example

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import com.example.databinding.ActivityMainBinding

class MainSearchActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_search_view)

        val sellPropertyButton = findViewById<Button>(R.id.sellPropertyButton)
        val filterButton = findViewById<Button>(R.id.filterButton)
        val filterCloseButton = findViewById<Button>(R.id.filterCloseButton)
        var filterDiv = findViewById<LinearLayout>(R.id.filterDiv)

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
}