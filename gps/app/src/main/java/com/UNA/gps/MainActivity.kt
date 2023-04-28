package com.UNA.gps

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import java.util.Date

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, ConfigFragment.OnMessageSendListener {
    lateinit var drawerLayout: DrawerLayout
    lateinit var messageDefault : String
    lateinit var fechaDefault : Date
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

    fun showDialog() {
        val builder = AlertDialog.Builder(this) // Reemplaza 'requireContext()' con la referencia al contexto del Fragment
        builder.setTitle("Integrantes")
        builder.setMessage("Jeff Vargas Barrantes y Caleb Sanchez Solorzano")
        builder.setPositiveButton("Cerrar") { dialog, which ->
            dialog.dismiss()
        }
        builder.show()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        lateinit var fragment : Fragment

        //if messageBundle is empty then set default message
        if(!::messageBundle.isInitialized){

            messageDefault = "Mi marcador por default"
            fechaDefault = Date()

            val bundle = Bundle()
            bundle.putString("message", messageDefault)
            bundle.putSerializable("fecha", fechaDefault)
            messageBundle = bundle

        }

        when (item.itemId){
            R.id.home -> {
                showDialog()
                return true
            }
            R.id.maps -> {
                fragment = MapsFragment()
                fragment.arguments = messageBundle
            }
            R.id.conf -> {
                fragment = ConfigFragment()
                fragment.arguments = messageBundle
            }
            R.id.polygon -> {
                fragment = PolygonFragment()
            }
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.home_content, fragment)
            .commit()
        drawerLayout.closeDrawer(GravityCompat.START)
        //cuando seleccionamos el menu maps, se carga un fragmento con el contenido MapsFragment
        return true
    }

    override fun onMessageSent(message: String, fecha: Date) {
        val bundle = Bundle()
        bundle.putString("message", message)
        bundle.putSerializable("fecha", fecha)
        println(fecha)
        messageBundle = bundle
    }

}