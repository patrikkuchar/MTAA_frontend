package com.example.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.ui.AppBarConfiguration
import com.example.R
import com.example.ViewModel.ChangePasswordActivityViewModel
import com.example.data.Edit_Password_Request
import com.example.storage.SharedPrefManager

class ChangePasswordActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ChangePasswordActivity
    lateinit var viewModel: ChangePasswordActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.change_password_view)

        val bottomNavProfile = findViewById<ImageView>(R.id.bottomNavProfile)
        val bottomNavBooking = findViewById<ImageView>(R.id.bottomNavBooking)
        val bottomNavMainSearch = findViewById<ImageView>(R.id.bottomNavMainSearch)
        val bottomNavFavourites = findViewById<ImageView>(R.id.bottomNavFavourites)
        val current_password = findViewById<EditText>(R.id.current_password)
        val new_password = findViewById<EditText>(R.id.new_password)
        val confirm_password = findViewById<EditText>(R.id.new_password_confirm)
        val changePasswordButton = findViewById<Button>(R.id.changePasswordButton)

        initViewModel()


        changePasswordButton.setOnClickListener {

            val token = SharedPrefManager.getInstance(this).user.token.toString()
            var request = Edit_Password_Request(current_password.text.toString(), new_password.text.toString(), confirm_password.text.toString())
            viewModel.change_password(token="Bearer "+token, request)

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

        println("SS")
        viewModel = ViewModelProvider(this).get(ChangePasswordActivityViewModel::class.java)



        viewModel.response_pass.observe(this) {
            if (it == null) {
                Toast.makeText(this@ChangePasswordActivity, "Fill out a form", Toast.LENGTH_LONG).show()
            } else {
                println(it)
                if (it == 204){
                    Toast.makeText(this@ChangePasswordActivity,"Password successfully changed", Toast.LENGTH_LONG).show()
                    //logout user and go to login page
                    SharedPrefManager.getInstance(this).clear()
                    startActivity(Intent(this, MainActivity::class.java))
                }
                else if (it == 400){
                    Toast.makeText(this@ChangePasswordActivity,"Password not changed, try again", Toast.LENGTH_LONG).show()
                }
            }
        }
    }






}