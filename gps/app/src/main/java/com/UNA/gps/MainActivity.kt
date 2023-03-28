package com.UNA.gps

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout

class MainActivity : AppCompatActivity() {

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

}