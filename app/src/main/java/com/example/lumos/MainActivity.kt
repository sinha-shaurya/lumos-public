package com.example.lumos

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.lumos.databinding.ActivityMainBinding
import com.example.lumos.repository.UserPreferencesRepository
import com.example.lumos.screens.ThemeBottomSheetFragment
import com.example.lumos.utils.setupWithNavController
import com.example.lumos.utils.viewmodelfactory.UserPreferencesViewModelFactory
import com.example.lumos.viewmodel.UserPreferencesViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

private const val PREFERENCE_NAME = "user_preferences"
val Context.datastore by preferencesDataStore(
    name = PREFERENCE_NAME
)

class MainActivity : AppCompatActivity() {

    private var currentNavController: LiveData<NavController>? = null
    private lateinit var binding: ActivityMainBinding

    private val userPreferencesViewModel by viewModels<UserPreferencesViewModel> {
        UserPreferencesViewModelFactory(UserPreferencesRepository(datastore))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)


        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        }


        userPreferencesViewModel.userPreferences.observe(this) {
            Log.d(TAG, it.toString())
            AppCompatDelegate.setDefaultNightMode(it)
        }



    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        setupBottomNavigationBar()
    }

    private fun setupBottomNavigationBar() {
        val navGraphIds = listOf(
            R.navigation.blog,
            R.navigation.events,
            R.navigation.practice,
            R.navigation.login
        )

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav)
        val controller = bottomNavigationView.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = R.id.fragment,
            intent = intent
        )

        //use this to setup Action bar

        controller.observe(this) { navController ->
            setupActionBarWithNavController(navController)
        }
        currentNavController = controller
    }


    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = MenuInflater(this)
        inflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }





    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings -> {
                val fragment = ThemeBottomSheetFragment.newInstance(Bundle())
                fragment.show(supportFragmentManager, FRAGMENT_TAG_THEME)
                return true
            }
            else->return super.onOptionsItemSelected(item)
        }

    }



    /*
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }

     */

    companion object {
        const val TAG = "MainActivity"
        const val FRAGMENT_TAG_THEME = "ThemeBottomSheetDialogFragment"
    }
}