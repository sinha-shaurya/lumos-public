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
import com.example.lumos.screens.appbar.AboutUsFragment
import com.example.lumos.screens.appbar.DevelopersFragment
import com.example.lumos.screens.appbar.ThemeBottomSheetFragment
import com.example.lumos.utils.setupWithNavController
import com.example.lumos.utils.viewmodelfactory.UserPreferencesViewModelFactory
import com.example.lumos.viewmodel.ToolbarTitleViewModel
import com.example.lumos.viewmodel.UserPreferencesViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

/**
 *[MainActivity] is the host activity containing all the fragments
 */
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

    private val toolbarTitleViewModel: ToolbarTitleViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)

        setSupportActionBar(binding.appBar)

        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        }



        userPreferencesViewModel.userPreferences.observe(this) {
            Log.d(TAG, it.toString())
            AppCompatDelegate.setDefaultNightMode(it)
        }

        supportActionBar?.setDisplayShowTitleEnabled(false)


        //observe value of title in ToolbarTitleViewModel
        toolbarTitleViewModel.title.observe(this) {
            //for initialisation condition
            Log.i(TAG, it)
            val splitSpaceTransform = it.split(" ") //split by whitespace
            Log.i(TAG, splitSpaceTransform.toString())

            //wrap everything around a try-catch block to prevent crashes
            try {
                if (splitSpaceTransform.size > 1) {
                    binding.appBarTitle1.text = splitSpaceTransform[0]
                    var i = 1;
                    var text = ""
                    while (i < splitSpaceTransform.size) {
                        text += splitSpaceTransform[i]
                        i++
                    }
                    binding.appBarTitle2.text = text
                } else
                    binding.appBarTitle1.text = it
            } catch (e: Exception) {
                binding.appBarTitle1.text = it
            }

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
            val id = navController.currentDestination?.id
            Log.d(TAG, R.id.blogFragment.toString())
            Log.d(TAG, id.toString())

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
            R.id.about_us -> {
                val fragment = AboutUsFragment.newInstance(Bundle())
                fragment.show(supportFragmentManager, FRAGMENT_TAG_ABOUT_US)
                return true
            }
            R.id.developer_info -> {
                val fragment = DevelopersFragment.newInstance(Bundle())
                fragment.show(supportFragmentManager, FRAGMENT_DEVELOPERS)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
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
        const val FRAGMENT_TAG_ABOUT_US = "AboutUsFragment"
        const val FRAGMENT_DEVELOPERS = "DevelopersFragment"
    }
}