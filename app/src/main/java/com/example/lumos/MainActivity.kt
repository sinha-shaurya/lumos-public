package com.example.lumos

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.lumos.databinding.ActivityMainBinding
import com.example.lumos.utils.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private var currentNavController:LiveData<NavController>?=null
    private lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        //val navController=findNavController(R.id.fragment)

        //binding.bottomNav.setupWithNavController(navController)

        /*
        //setup action bar with navController with appbar configuration
        val appBarConfiguration= AppBarConfiguration(topLevelDestinationIds = setOf(
            R.id.blogFragment,R.id.eventFragment,R.id.categoryEventFragment,R.id.questionFragment,R.id.accountFragment,R.id.loginFragment
        ))

        setupActionBarWithNavController(navController,appBarConfiguration)

         */

        supportActionBar?.hide()
        if(savedInstanceState==null){
            setupBottomNavigationBar()
        }
    }

    /*
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home->{
                onBackPressed()
                return true
            }
            else->super.onOptionsItemSelected(item)
        }
       return true
    }*/

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        setupBottomNavigationBar()
    }
    private fun setupBottomNavigationBar(){
        val navGraphIds=listOf(R.navigation.blog,R.navigation.events,R.navigation.practice,R.navigation.login)

        val bottomNavigationView=findViewById<BottomNavigationView>(R.id.bottom_nav)
        val controller=bottomNavigationView.setupWithNavController(
            navGraphIds=navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = R.id.fragment,
            intent=intent
        )

        //use this to setup Action bar

        controller.observe(this){navController->
            setupActionBarWithNavController(navController)
        }
        currentNavController=controller
    }

    /*
    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp()?:false
    }*/
}