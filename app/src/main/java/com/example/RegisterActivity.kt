package com.example

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import com.example.databinding.ActivityMainBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_view)

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
            startActivity(Intent(this, MainSearchActivity::class.java))
        }
    }
}