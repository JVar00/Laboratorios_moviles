package com.UNA.gps

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var drawableLayout: DrawerLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)

        //assign toolbar to main
        setSupportActionBar(toolbar)

        drawableLayout = findViewById(R.id.drawerlayout)
        var toggle = androidx.appcompat.app.ActionBarDrawerToggle(
            this, drawableLayout, toolbar, R.string.open, R.string.close)
        drawableLayout.addDrawerListener(toggle)
        toggle.syncState()

        val navigationView = findViewById<NavigationView>(R.id.navigation_view)
        navigationView.setNavigationItemSelectedListener(this)

    }

    override fun onBackPressed() {
        if(drawableLayout.isDrawerOpen(GravityCompat.START)){
            drawableLayout.closeDrawer(GravityCompat.START)
        }else{
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        lateinit var fragment: androidx.fragment.app.Fragment
        when(item.itemId){
            R.id.menu_item_home -> {
                fragment = HomeFragment()
            }
            R.id.menu_item_maps -> {
                fragment = MapsFragment()
            }
        }

        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit()
        drawableLayout.closeDrawer(GravityCompat.START)
        return true
    }

}