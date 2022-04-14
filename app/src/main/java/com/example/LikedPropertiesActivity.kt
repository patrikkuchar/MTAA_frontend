package com.example

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
import com.example.Adapters.PropertyLikedAdapter
import com.example.ViewModel.LikedPropertiesActivityViewModel
import com.example.ViewModel.MainSearchActivityViewModel
import com.example.data.Prop
import com.example.databinding.ActivityMainBinding
import com.example.databinding.LikedPropertiesViewBinding
import com.example.storage.SharedPrefManager

class LikedPropertiesActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: LikedPropertiesViewBinding
    lateinit var viewModel: LikedPropertiesActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.liked_properties_view)
        initViewModel()

        fetch_liked()

        val bottomNavProfile = findViewById<ImageView>(R.id.bottomNavProfile)
        val bottomNavBooking = findViewById<ImageView>(R.id.bottomNavBooking)
        val bottomNavMainSearch = findViewById<ImageView>(R.id.bottomNavMainSearch)

        bottomNavProfile.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        bottomNavBooking.setOnClickListener {
            startActivity(Intent(this, BookingActivity::class.java))
        }

        bottomNavMainSearch.setOnClickListener {
            finish()
            startActivity(Intent(this, MainSearchActivity::class.java))
        }
    }


    fun fetch_liked(){
        val token = SharedPrefManager.getInstance(this).user.token.toString()
        viewModel.get_liked(token="Bearer "+token)
    }

    fun onAddField(view: View, properties: List<Prop>) {

        var adapter1 = PropertyLikedAdapter(this, properties as ArrayList<Prop>)
        adapter1.notifyDataSetInvalidated()
        adapter1.notifyDataSetChanged()


        var listView = findViewById<ListView>(R.id.likedPropertyImageView)
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



    private fun initViewModel() {

        println("SS")
        viewModel = ViewModelProvider(this).get(LikedPropertiesActivityViewModel::class.java)

        viewModel.liked.observe(this) {
            if (it == null) {
                Toast.makeText(this@LikedPropertiesActivity, "Bad credentials", Toast.LENGTH_LONG).show()
            } else {
                var View = findViewById<androidx.constraintlayout.widget.ConstraintLayout>(R.id.my_root_liked)
                onAddField(View,it.properties)
                println("hre")
            }
        }

    }
}