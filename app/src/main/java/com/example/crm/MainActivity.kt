package com.example.crm


import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.crm.databinding.ActivityMainBinding
import java.util.Calendar


class MainActivity : AppCompatActivity() {
   lateinit var binding : ActivityMainBinding
    lateinit var drawerLayout: DrawerLayout
    var backPressedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        if (SharedPrefManager.getInstance(this).isLoggedIn) {
            val user = SharedPrefManager.getInstance(this).user
        } else {
            val intent = Intent(this@MainActivity, Login::class.java)
            startActivity(intent)
            finish()
        }



        drawerLayout = binding.myDrawer

        val navController = this.findNavController(R.id.myNavHost)
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)
        NavigationUI.setupWithNavController(binding.myNavView, navController)











        binding.myNavView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.logout -> {
                    SharedPrefManager.getInstance(this).logout()
                    finish()
                }
                R.id.dashBoard -> {
                    findNavController(R.id.myNavHost)
                        .navigate(R.id.dashBoard)
                    binding.myDrawer.closeDrawer(GravityCompat.START, true)
                }
                R.id.activities -> {
                    findNavController(R.id.myNavHost).navigate(R.id.activities)
                    binding.myDrawer.closeDrawer(GravityCompat.START, true)
                }
                R.id.myActivityFragment -> {
                    findNavController(R.id.myNavHost).navigate(R.id.myActivityFragment)
                    binding.myDrawer.closeDrawer(GravityCompat.START, true)
                }
                R.id.add_user -> {
                    findNavController(R.id.myNavHost).navigate(R.id.addUserFragment)
                    binding.myDrawer.closeDrawer(GravityCompat.START, true)

                }
                R.id.added_users -> {
                    findNavController(R.id.myNavHost).navigate(R.id.addedUsersFragment)
                    binding.myDrawer.closeDrawer(GravityCompat.START, true)

                }
            }
            true
        }



        // Hide Add User Tab if role of user is Executive.
        val nav_Menu: Menu = binding.myNavView.menu
        if(SharedPrefManager.getInstance(this).isLoggedIn) {
            if (SharedPrefManager.getInstance(this).user.role == "Executive") {
                nav_Menu.findItem(R.id.add_user).isVisible = false
                nav_Menu.findItem(R.id.added_users).isVisible = false

            }
        }

        // user name in the navigation drawer
        val headerView = binding.myNavView.getHeaderView(0)
        val username = headerView.findViewById<TextView>(R.id.username)
     if (SharedPrefManager.getInstance(this).isLoggedIn) {
         val usernames =
             SharedPrefManager.getInstance(this).user.firstName + " " + SharedPrefManager.getInstance(
                 this
             ).user.lastName

         username.text = usernames
     }

    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.myNavHost)

        return NavigationUI.navigateUp(navController, drawerLayout)
    }


}