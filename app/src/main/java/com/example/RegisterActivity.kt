package com.example

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.ui.AppBarConfiguration
import com.example.ViewModel.RegisterActivityViewModel
import com.example.data.RegisterRequestData
import com.example.data.RegisterResponseData
import com.example.databinding.ActivityMainBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    lateinit var viewModel: RegisterActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_view)

        initViewModel()

        val register_registerBtn = findViewById<Button>(R.id.register_registerButton)
        val register_loginBtn = findViewById<Button>(R.id.register_loginButton)
        val register_emailInput = findViewById<EditText>(R.id.register_emailInput)
        val register_passwordInput = findViewById<EditText>(R.id.register_passwordInput)
        val register_comfPasswordInput = findViewById<EditText>(R.id.register_comfPasswordInput)
        val register_nameInput = findViewById<EditText>(R.id.register_nameInput)
        val register_surnameInput = findViewById<EditText>(R.id.register_surnameInput)

        register_loginBtn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        register_registerBtn.setOnClickListener {
            val passwordInput = findViewById<EditText>(R.id.register_passwordInput).text.toString()
            val confpasswordInput = findViewById<EditText>(R.id.register_comfPasswordInput).text.toString()
            if (passwordInput != confpasswordInput) {
                Toast.makeText(this, "Passwords don't match ", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            registerUser()
        }
    }

    private fun registerUser() {
        val emailInput = findViewById<EditText>(R.id.register_emailInput).text.toString()
        val nameInput = findViewById<EditText>(R.id.register_nameInput).text.toString()
        val surnameInput = findViewById<EditText>(R.id.register_surnameInput).text.toString()
        val passwordInput = findViewById<EditText>(R.id.register_passwordInput).text.toString()
        val confpasswordInput = findViewById<EditText>(R.id.register_comfPasswordInput).text.toString()
        val user  = RegisterRequestData(emailInput,nameInput,surnameInput,passwordInput,confpasswordInput)


        viewModel.register_user(user)
    }


    private fun initViewModel() {
        println("SS")
        viewModel = ViewModelProvider(this).get(RegisterActivityViewModel::class.java)
        viewModel.register_user_response.observe(this, Observer <RegisterResponseData?>{

            if(it.code  == 400) {
                Toast.makeText(this@RegisterActivity, "Email already taken", Toast.LENGTH_LONG).show()
            } else if (it.code == 201) {
                Toast.makeText(this, "Succesfully registrated, Please Log in", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))

            } else {
                Toast.makeText(this, "Error occured , please contact administrator ", Toast.LENGTH_SHORT).show()
            }
        })
    }
}