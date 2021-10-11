package com.example.snplc.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.snplc.R
import com.example.snplc.ui.auth.AuthActivity
import com.google.firebase.auth.FirebaseAuth
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