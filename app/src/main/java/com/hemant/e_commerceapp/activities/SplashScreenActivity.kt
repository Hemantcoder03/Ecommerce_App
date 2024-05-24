package com.hemant.e_commerceapp.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hemant.e_commerceapp.R
import com.hemant.e_commerceapp.api_service.ApiInterface
import com.hemant.e_commerceapp.models.ProfileResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SplashScreenActivity : AppCompatActivity() {


    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor : SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val imageView = findViewById<ImageView>(R.id.splashImage)

        //initiate the sharedPreference (local database)
        sharedPreferences = getSharedPreferences("My_Pref",0)
        editor = sharedPreferences.edit()

        val animation: Animation = AlphaAnimation(1f, 0f)
        animation.duration = 1000
        animation.interpolator = LinearInterpolator()
        animation.repeatCount = 3 //repeated for 5 times
        imageView.startAnimation(animation)

        animation.setAnimationListener(object : Animation.AnimationListener{
            override fun onAnimationStart(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                if(sharedPreferences.getString("access_token","")!!.isNotEmpty()){
                    setToken(access_token = sharedPreferences.getString("access_token","")!!)
                }
                else{
                    sendToLogin()
                }
            }

            override fun onAnimationRepeat(animation: Animation?) {
            }

        })
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
                    Toast.makeText(this@SplashScreenActivity,response.errorBody()!!.charStream().read().toString(),
                        Toast.LENGTH_SHORT).show()
                    Log.d("checkError","e "+response.errorBody()!!.charStream().read())
                }
            }

            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                Toast.makeText(this@SplashScreenActivity,t.message, Toast.LENGTH_SHORT).show()
                Log.d("checkError",t.message.toString())
            }
        })
    }


    private fun sendToMain() {
        val intent = Intent(this@SplashScreenActivity, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    private fun sendToLogin() {
        val intent = Intent(this@SplashScreenActivity, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
}