package com.example.lumos

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.lumos.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        val navController=findNavController(R.id.fragment)

        binding.bottomNav.setupWithNavController(navController)

        //setup action bar with navController with appbar configuration
        val appBarConfiguration= AppBarConfiguration(topLevelDestinationIds = setOf(
            R.id.blogFragment,R.id.eventFragment,R.id.categoryEventFragment,R.id.questionFragment,R.id.accountFragment,R.id.loginFragment
        ))

        setupActionBarWithNavController(navController,appBarConfiguration)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home->{
                onBackPressed()
                return true
            }
            else->super.onOptionsItemSelected(item)
        }
       return true
    }
}