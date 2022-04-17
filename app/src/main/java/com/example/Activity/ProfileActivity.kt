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
import com.example.Adapters.BookingBuyerAdapter
import com.example.Adapters.BookingSellerAdapter
import com.example.Adapters.ProfileAdapter
import com.example.Adapters.PropertyAdapter
import com.example.R
import com.example.ViewModel.MainSearchActivityViewModel
import com.example.ViewModel.ProfileActivityViewModel
import com.example.data.*
import com.example.databinding.ActivityMainBinding
import com.example.storage.SharedPrefManager

class ProfileActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    lateinit var viewModel: ProfileActivityViewModel
    private var properties : UserProperties? = null
    private lateinit var propertiesListView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_view)


        propertiesListView = findViewById<ListView>(R.id.profile_listView)

        initViewModel()

        fetchUserProperties()



        val deletePropertyButton = findViewById<Button>(R.id.deletePropertyButton)
        var deletePropertyDiv = findViewById<LinearLayout>(R.id.deletePropertyDiv)
        val profileDecisionYesButton = findViewById<Button>(R.id.profileDecisionYesButton)
        val profileDecisionNoButton = findViewById<Button>(R.id.profileDecisionNoButton)

        val bottomNavBooking = findViewById<ImageView>(R.id.bottomNavBooking)
        val bottomNavMainSearch = findViewById<ImageView>(R.id.bottomNavMainSearch)
        val bottomNavFavourites = findViewById<ImageView>(R.id.bottomNavFavourites)

        val changePasswordButton = findViewById<Button>(R.id.changePasswordButton)
        val profileSellPropertyButton = findViewById<Button>(R.id.profileSellPropertyButton)
        val editPropertyButton = findViewById<Button>(R.id.editPropertyButton)
        val logoutButton = findViewById<Button>(R.id.logoutButton)

        bottomNavBooking.setOnClickListener {
            startActivity(Intent(this, BookingActivity::class.java))
        }

        bottomNavMainSearch.setOnClickListener {
            startActivity(Intent(this, MainSearchActivity::class.java))
        }

        bottomNavFavourites.setOnClickListener {
            startActivity(Intent(this, LikedPropertiesActivity::class.java))
        }

        changePasswordButton.setOnClickListener {
            startActivity(Intent(this, ChangePasswordActivity::class.java))
        }

        profileSellPropertyButton.setOnClickListener {
            startActivity(Intent(this, SellPropertyActivity::class.java))
        }



        logoutButton.setOnClickListener {
            //forget user
            SharedPrefManager.getInstance(this).clear()
            startActivity(Intent(this, MainActivity::class.java))
        }

        deletePropertyDiv.visibility = LinearLayout.GONE



        profileDecisionYesButton.setOnClickListener {
            deletePropertyDiv.visibility = LinearLayout.GONE
        }

        profileDecisionNoButton.setOnClickListener {
            deletePropertyDiv.visibility = LinearLayout.GONE
        }
    }


    private fun fetchUserProperties() {
        val token = SharedPrefManager.getInstance(this).user.token.toString()
        viewModel.getUserProperties(token="Bearer "+token)
    }

    private fun onAddField(view: View, properties: List<UserProperty>) {

        var adapter1 = ProfileAdapter(this, properties as ArrayList<UserProperty>)

        adapter1.notifyDataSetInvalidated()
        adapter1.notifyDataSetChanged()


        propertiesListView.adapter = adapter1


        propertiesListView.setOnItemClickListener(){adapterView, view, position, id ->
            val itemAtPos = adapterView.getItemAtPosition(position) as BookingSellerData
            val itemIdAtPos = adapterView.getItemIdAtPosition(position)

            val propertyId = itemAtPos.id

            val intent = (Intent(this, PropertyInfoActivity::class.java))
            intent.putExtra("propertyId", propertyId)
            startActivity(intent)
        }



        val params1: ViewGroup.LayoutParams = propertiesListView.layoutParams
        params1.height = adapter1.dpToPx(220) * properties.size + adapter1.dpToPx(20)
        propertiesListView.layoutParams = params1

/*
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
        listView.layoutParams = params*/
    }


    private fun initViewModel() {

        println("SS")
        viewModel = ViewModelProvider(this).get(ProfileActivityViewModel::class.java)

        viewModel.properties.observe(this) {
            if (it == null) {
                Toast.makeText(this, "0 Propertes found", Toast.LENGTH_LONG).show()
            } else {
                var View = findViewById<ConstraintLayout>(R.id.profile_view)
                onAddField(View, it.properties)
            }
        }






    }

}