package com.example.stajtvplus

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.example.stajtvplus.configs.ApiClient
import com.example.stajtvplus.databinding.ActivityLoginBinding
import com.example.stajtvplus.databinding.ActivityMainBinding
import com.example.stajtvplus.models.JWTData
import com.example.stajtvplus.models.JWTUser
import com.example.stajtvplus.services.IDummyService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Login : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var dummyService: IDummyService
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor


    object DataManager {
        var userId: String? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        sharedPreferences = getSharedPreferences("users", MODE_PRIVATE)
        editor = sharedPreferences.edit()

        dummyService = ApiClient.getClient().create(IDummyService::class.java)
        binding.btnLogin.setOnClickListener(btnOnClickListener)


        val username = sharedPreferences.getString("username", "")
        binding.txtUsername.setText(username)
        Log.d("username", username.toString())


    }

    private val btnOnClickListener = View.OnClickListener {
        val username = binding.txtUsername.text.toString()
        val password = binding.txtPassword.text.toString()

        if (username.isNotEmpty() && password.isNotEmpty()) {
            val jwtUser = JWTUser(username, password)
            dummyService.login(jwtUser).enqueue(object : Callback<JWTData> {
                override fun onResponse(call: Call<JWTData>, response: Response<JWTData>) {
                    val jwtData = response.body()
                    if (jwtData != null) {
                        Util.user = jwtData
                        editor.putString("username", jwtData.username)
                        editor.putString("firstName", jwtData.firstName)
                        editor.commit()

                        val intent = Intent(this@Login,MainActivity::class.java)
                        DataManager.userId = Util.user?.id.toString()
                        startActivity(intent)
                        finish()


                    } else {
                        Toast.makeText(this@Login, "Login Error", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<JWTData>, t: Throwable) {
                    Toast.makeText(this@Login, "Login Failure", Toast.LENGTH_LONG).show()
                }
            })
        } else {
            Toast.makeText(this@Login, "Alanlar Boş Bırakılamaz!", Toast.LENGTH_LONG).show()
        }
    }

}