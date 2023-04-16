package com.UNA.gps

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, ConfigFragment.OnMessageSendListener {
    lateinit var drawerLayout: DrawerLayout
    lateinit var messageDefault : String
    lateinit var messageBundle : Bundle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawerlayout)

        var toogle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.drawer_open,
            R.string.drawer_close
        )
        drawerLayout.addDrawerListener(toogle)
        toogle.syncState()
        val navigationView = findViewById<NavigationView>(R.id.navigation_view)
        navigationView.setNavigationItemSelectedListener(this)

    }

    override fun onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START)
        }else{
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        lateinit var fragment : Fragment

        //if messageBundle is empty then set default message
        if(!::messageBundle.isInitialized){
            messageDefault = "Mi marcador por default"
            val bundle = Bundle()
            bundle.putString("message", messageDefault)
            messageBundle = bundle
        }

        when (item.itemId){
            R.id.home -> {
                //DIALOGO
                //fragment = HomeFragment.newInstance("string1","string2")
            }
            R.id.maps -> {
                fragment = MapsFragment()
                fragment.arguments = messageBundle
            }
            R.id.conf -> {
                fragment = ConfigFragment()
                fragment.arguments = messageBundle
            }
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.home_content, fragment)
            .commit()
        drawerLayout.closeDrawer(GravityCompat.START)
        //cuando seleccionamos el menu maps, se carga un fragmento con el contenido MapsFragment
        return true
    }

    override fun onMessageSent(message: String) {
        val bundle = Bundle()
        bundle.putString("message", message)
        messageBundle = bundle
    }

}