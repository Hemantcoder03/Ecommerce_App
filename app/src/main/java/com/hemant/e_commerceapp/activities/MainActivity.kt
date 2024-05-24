package com.hemant.e_commerceapp.activities

import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.hemant.e_commerceapp.R
import com.hemant.e_commerceapp.databinding.ActivityMainBinding
import com.squareup.picasso.Picasso

class MainActivity : AppCompatActivity() {

    //used binding0 to ignore the used of findViewById
    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor : SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //initialize the binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        //set as an content view as visual UI
        setContentView(binding.root)

        //initiate the sharedPreference (local database)
        sharedPreferences = getSharedPreferences("My_Pref",0)
        editor = sharedPreferences.edit()

        //set the profile image
        if(!sharedPreferences.getString("profileImage","")!!.isEmpty())
            Picasso.get().load(Uri.parse(sharedPreferences.getString("profileImage",""))).into(binding.profileBtn)

        //set the click listener on menu button
        binding.menuBtn.setOnClickListener {
            //show the toast when click on it
            Toast.makeText(this,"Menu Button", Toast.LENGTH_SHORT).show()
        }
        //set the click listener on profile button
        binding.profileBtn.setOnClickListener {
            //show the toast when click on it
            Toast.makeText(this,"Profile Button", Toast.LENGTH_SHORT).show()
        }

        init()
    }

    private fun init() {

        // Get a reference to the NavController using the root view of the NavHostFragment
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        // Set up bottom navigation with NavController
        binding.bottomNavigationView.setupWithNavController(navController)
    }

}