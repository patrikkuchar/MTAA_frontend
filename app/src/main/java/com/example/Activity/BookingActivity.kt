package com.example.Activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.ui.AppBarConfiguration
import com.example.Adapters.BookingBuyerAdapter
import com.example.Adapters.BookingSellerAdapter
import com.example.R
import com.example.ViewModel.BookingActivityViewModel
import com.example.data.*
import com.example.databinding.ActivityMainBinding
import com.example.storage.SharedPrefManager

class BookingActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    lateinit var viewModel: BookingActivityViewModel
    private var Bookings : BookingData_List? = null
    private lateinit var sellingListView: ListView
    private lateinit var buyingListView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.booking_view)

        val bottomNavProfile = findViewById<ImageView>(R.id.bottomNavProfile)
        val bottomNavMainSearch = findViewById<ImageView>(R.id.bottomNavMainSearch)
        val bottomNavFavourites = findViewById<ImageView>(R.id.bottomNavFavourites)

        sellingListView = findViewById<ListView>(R.id.sellingListView)
        buyingListView = findViewById<ListView>(R.id.buyingListView)

        initViewModel()
        fetchBookings()

        bottomNavProfile.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        bottomNavMainSearch.setOnClickListener {
            startActivity(Intent(this, MainSearchActivity::class.java))
        }

        bottomNavFavourites.setOnClickListener {
            startActivity(Intent(this, LikedPropertiesActivity::class.java))
        }
    }

    private fun fetchBookings() {
        val token = SharedPrefManager.getInstance(this).user.token.toString()
        viewModel.get_booking(token="Bearer "+token)
    }

    private fun onAddField(view: View, sellingBookings: List<BookingSellerData>, buyingBookings: List<BookingBuyerData>) {
        var adapter1 = BookingSellerAdapter(this, sellingBookings as ArrayList<BookingSellerData>)
        adapter1.notifyDataSetInvalidated()
        adapter1.notifyDataSetChanged()

        var adapter2 = BookingBuyerAdapter(this, buyingBookings as ArrayList<BookingBuyerData>)
        adapter2.notifyDataSetInvalidated()
        adapter2.notifyDataSetChanged()

        sellingListView.adapter = adapter1
        buyingListView.adapter = adapter2

        sellingListView.setOnItemClickListener(){adapterView, view, position, id ->
            val itemAtPos = adapterView.getItemAtPosition(position) as BookingSellerData
            val itemIdAtPos = adapterView.getItemIdAtPosition(position)

            val propertyId = itemAtPos.id

            val intent = (Intent(this, PropertyInfoActivity::class.java))
            intent.putExtra("propertyId", propertyId)
            startActivity(intent)
        }

        buyingListView.setOnItemClickListener(){adapterView, view, position, id ->
            val itemAtPos = adapterView.getItemAtPosition(position) as BookingBuyerData
            val itemIdAtPos = adapterView.getItemIdAtPosition(position)

            val propertyId = itemAtPos.id

            val intent = (Intent(this, PropertyInfoActivity::class.java))
            intent.putExtra("propertyId", propertyId)
            startActivity(intent)
        }

        val params1: ViewGroup.LayoutParams = sellingListView.layoutParams
        params1.height = adapter1.dpToPx(210) * sellingBookings.size + adapter1.dpToPx(20)
        sellingListView.layoutParams = params1

        val params2: ViewGroup.LayoutParams = buyingListView.layoutParams
        params2.height = adapter2.dpToPx(210) * buyingBookings.size + adapter2.dpToPx(20)
        buyingListView.layoutParams = params2
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

        viewModel = ViewModelProvider(this).get(BookingActivityViewModel::class.java)

        viewModel.bookings.observe(this) {
            if (it == null) {


                Toast.makeText(this, "Not Found", Toast.LENGTH_LONG).show()
            } else {
                var View = findViewById<ConstraintLayout>(R.id.booking_root)
                Bookings = it
                onAddField(View,it.selling, it.buying)
                println("hre")
            }
        }

        viewModel.response_delete.observe(this) {
            if (it == 0) {

            }
            else {
                if(it == 204){
                    Toast.makeText(this, "Deleted", Toast.LENGTH_LONG).show()
                }
                else if (it == 401) {
                    Toast.makeText(this, "Unauthorized", Toast.LENGTH_LONG).show()
                    startActivity(Intent(this, MainActivity::class.java))
                }
                else if (it ==403) {
                    Toast.makeText(this, "Not your property", Toast.LENGTH_LONG).show()
                    finish()
                }

            }

        }
    }
}