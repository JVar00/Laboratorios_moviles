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

    }

    override fun onBackPressed() {
        if(drawableLayout.isDrawerOpen(GravityCompat.START)){
            drawableLayout.closeDrawer(GravityCompat.START)
        }else{
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
when(item.itemId){
            R.id.menu_item_home -> {
                Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show()
                //supportFragmentManager.beginTransaction().replace(R.id.fragment_container, HomeFragment()).commit()
            }
            R.id.menu_item_maps -> {
                Toast.makeText(this, "Maps", Toast.LENGTH_SHORT).show()
                //supportFragmentManager.beginTransaction().replace(R.id.fragment_container, MapsFragment()).commit()
            }
        }
        drawableLayout.closeDrawer(GravityCompat.START)
        return true
    }

}