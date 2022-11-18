package com.example.crm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.crm.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var drawerLayout: DrawerLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)


        drawerLayout = binding.myDrawer
        val navController = this.findNavController(R.id.myNavHost)
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)
        NavigationUI.setupWithNavController(binding.myNavView, navController)




    if (SharedPrefManager.getInstance(this).isLoggedIn)
    {
        val user = SharedPrefManager.getInstance(this).user
    }

    else
    {
        val intent = Intent(this@MainActivity, Login::class.java)
        startActivity(intent)
        finish()
    }




    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.myNavHost)

        return NavigationUI.navigateUp(navController, drawerLayout)
    }
}