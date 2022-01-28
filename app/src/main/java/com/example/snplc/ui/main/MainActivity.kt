package com.example.snplc.ui.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.snplc.R
import com.example.snplc.ui.main.fragments.OthersProfileFragment
import com.example.snplc.ui.main.fragments.SettingsFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navHostFragment: NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment


        bottomNavigationView.apply {
            background = null // make sure it is really transparent
            menu.getItem(2).isEnabled = false // placeholder
            setupWithNavController(navHostFragment.findNavController())

            setOnNavigationItemReselectedListener { Unit }
        }

        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.home, R.id.search, R.id.profile)
        )

        setupActionBarWithNavController(navHostFragment.findNavController(), appBarConfiguration)

        fabNewPost.setOnClickListener {
            navHostFragment.findNavController().navigate(
                R.id.globalActionToCreatePostFragment
            )
        }

        supportFragmentManager.registerFragmentLifecycleCallbacks(object : FragmentManager.FragmentLifecycleCallbacks() {
            override fun onFragmentViewCreated(fm: FragmentManager, f: Fragment, v: View, savedInstanceState: Bundle?) {
//                TransitionManager.beginDelayedTransition(binding.root, Slide(Gravity.BOTTOM).excludeTarget(R.id.nav_host_fragment, true))
                when (f) {
                    is SettingsFragment, is OthersProfileFragment -> {
                        bottomAppBar.visibility = View.GONE
                        fabNewPost.visibility = View.GONE
                    }
                    else -> {
                        bottomAppBar.visibility = View.VISIBLE
                        fabNewPost.visibility = View.VISIBLE
                    }
                }
            }
        }, true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {


        when(item.itemId) {
            android.R.id.home -> {
                navHostFragment.findNavController().navigateUp()
            }
//            R.id.miLogout -> {
//                FirebaseAuth.getInstance().signOut()
//                Intent(this, AuthActivity::class.java).also {
//                    startActivity(it)
//                }
//                finish()
//            }
            R.id.miSettings -> {
                navHostFragment.findNavController().navigate(R.id.globalActionToSettings)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}