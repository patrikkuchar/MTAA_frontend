package com.example.Activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.demo.retrofithttpmethods.MainActivityViewModel
import com.example.data.LoginRequestData
import com.example.databinding.ActivityMainBinding
import com.example.data.LoginResponseData
import androidx.lifecycle.Observer
import com.example.R
import com.example.data.User
import com.example.storage.SharedPrefManager

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    lateinit var viewModel:MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_main)
        initViewModel()

        val registerBtn = findViewById<Button>(R.id.registerbutton)
        val loginBtn = findViewById<Button>(R.id.register_loginButton)
        val emailInput = findViewById<EditText>(R.id.register_emailInput)
        val passwordInput = findViewById<EditText>(R.id.register_passwordInput)

        loginBtn.setOnClickListener {
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter email/password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            createUser()
        }


        
         

        registerBtn.setOnClickListener {
            startActivity(Intent(this, TestMainActivity::class.java))
        }





    }

    private fun createUser() {
        val emailInput = findViewById<EditText>(R.id.register_emailInput)
        val passwordInput = findViewById<EditText>(R.id.register_passwordInput)
        val user  = LoginRequestData( emailInput.text.toString(), passwordInput.text.toString())
        viewModel.login_user(user)
    }

    private fun initViewModel() {
        println("SS")
        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        viewModel.login_user_response.observe(this, Observer <LoginResponseData?>{

            if(it  == null) {


                Toast.makeText(this@MainActivity, "Bad credentials", Toast.LENGTH_LONG).show()
            } else {
                val user = User((it.id)!!.toInt(),it.email,it.name,it.surname,it.token)
                SharedPrefManager.getInstance(applicationContext).saveUser(user)
                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, SellPropertyActivity::class.java))

            }
        })
    }
}