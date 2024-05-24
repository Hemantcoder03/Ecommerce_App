package com.hemant.e_commerceapp.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hemant.e_commerceapp.api_service.ApiInterface
import com.hemant.e_commerceapp.databinding.ActivityLoginBinding
import com.hemant.e_commerceapp.models.LoginCrediential
import com.hemant.e_commerceapp.models.LoginResponse
import com.hemant.e_commerceapp.models.ProfileResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding
    private lateinit var userEmail : String
    private lateinit var userPassword : String
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor : SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding  = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //initiate the sharedPreference (local database)
        sharedPreferences = getSharedPreferences("My_Pref",0)
        editor = sharedPreferences.edit()

        binding.loginBtn.setOnClickListener {
            userEmail = binding.loginEmail.text.toString()
            userPassword = binding.loginPassword.text.toString()
            validEmailAndPass(userEmail, userPassword)
        }

        binding.guestBtn.setOnClickListener {
            sendToMain()
        }

    }

    private fun validEmailAndPass(userEmail : String, userPassword : String){
        if (userEmail.isEmpty()) {
            binding.loginEmail.error = "Please enter email"
        } else if (userPassword.isEmpty()) {
            binding.loginPassword.error = "Please enter password"
        } else if (!isValidEmail(userEmail)) {
            binding.loginEmail.error = "Please enter valid email\nE.g. Abc@gmail.com"
        }
            //it is not working because of already present password have not in required format
//        else if (!isValidPassword(userPassword)) {
//            binding.loginPassword.error = "Please enter valid password\nE.g. Abc@123"
//        }
        else {
            getToken(userEmail, userPassword)
        }
    }

    private fun getToken(userEmail: String, userPassword: String) {

        //fetch the token using base url
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.escuelajs.co/api/v1/")
            .addConverterFactory(GsonConverterFactory.create()).build()
        val apiService : ApiInterface = retrofit.create(ApiInterface::class.java)
        val call = apiService.doAuthentication(LoginCrediential(userEmail, userPassword))
        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if(response.isSuccessful){
                    Log.d("checkError","s "+response.body())
                    val loginResponse : LoginResponse = response.body()!!
                    setToken("Bearer "+loginResponse.access_token)
                }
                else{
                    Toast.makeText(this@LoginActivity,response.errorBody()!!.charStream().read().toString(),
                        Toast.LENGTH_SHORT).show()
                    Log.d("checkError","e "+response.errorBody()!!.charStream().read())
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(this@LoginActivity,t.message, Toast.LENGTH_SHORT).show()
                Log.d("checkError",t.message.toString())
            }
        })
    }

    override fun onStart() {
        super.onStart()
        if(sharedPreferences.getString("access_token","")!!.isNotEmpty()){
            setToken(access_token = sharedPreferences.getString("access_token","")!!)
        }
    }

    private fun setToken(access_token : String){

        //fetch the token using base url
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.escuelajs.co/api/v1/")
            .addConverterFactory(GsonConverterFactory.create()).build()
        val apiService : ApiInterface = retrofit.create(ApiInterface::class.java)
        val call = apiService.doAuthorization(access_token)
        call.enqueue(object : Callback<ProfileResponse> {
            override fun onResponse(call: Call<ProfileResponse>, response: Response<ProfileResponse>) {
                if(response.isSuccessful){
                    Log.d("checkError","s "+response.body())
                    val profileResponse : ProfileResponse = response.body()!!
                    editor.putString("access_token",access_token)
                    editor.putString("profileImage",response.body()!!.avatar)
                    editor.commit()
                    sendToMain()
                }
                else{
                    Toast.makeText(this@LoginActivity,response.errorBody()!!.charStream().read().toString(),
                        Toast.LENGTH_SHORT).show()
                    Log.d("checkError","e "+response.errorBody()!!.charStream().read())
                }
            }

            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                Toast.makeText(this@LoginActivity,t.message, Toast.LENGTH_SHORT).show()
                Log.d("checkError",t.message.toString())
            }
        })
    }

    private fun sendToMain() {
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

//    private fun isValidPassword(password: String): Boolean {
//        val pattern = Pattern.compile("(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#\\$%^&*+=~]).{8,}")
//        return pattern.matcher(password).matches()
//    }

    private fun isValidEmail(email: String): Boolean {
        //It used default email address validation
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

}