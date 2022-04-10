package com.example

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
import org.json.JSONObject

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
            startActivity(Intent(this, RegisterActivity::class.java))
        }


    }

    private fun createUser() {
        val emailInput = findViewById<EditText>(R.id.emailInput)
        val passwordInput = findViewById<EditText>(R.id.passwordInput)
        val user  = LoginRequestData( emailInput.text.toString(), passwordInput.text.toString())
        viewModel.login_user(user)
    }

    private fun initViewModel() {
        println("SS")
        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        viewModel.login_user_response.observe(this, Observer <LoginResponseData?>{

            if(it  == null) {
                println("SDA")
                Toast.makeText(this@MainActivity, "Failed to create User", Toast.LENGTH_LONG).show()
            } else {
                println("SD56A")
                //{"code":201,"meta":null,"data":{"id":2877,"name":"xxxxxaaaaabbbbb","email":"xxxxxaaaaabbbbb@gmail.com","gender":"male","status":"active"}}
                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()

            }
        })
    }
}