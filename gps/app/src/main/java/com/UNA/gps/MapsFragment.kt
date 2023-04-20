package com.UNA.gps

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.location.FusedLocationProviderClient

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

import android.Manifest
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.Location

import android.util.Log
import android.widget.Button

import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.common.api.GoogleApiClient

import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

import androidx.lifecycle.lifecycleScope
import com.UNA.gps.dao.LocationDAO
import com.UNA.gps.db.AppDatabase
import com.UNA.gps.entity.LocationEntity
import kotlinx.coroutines.launch


class MapsFragment : Fragment() {

    private lateinit var locationReceiver: BroadcastReceiver
    private var param1: String? = "Marcador default por maps"
    private lateinit var locationDao: LocationDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString("message")
        }
        locationDao = AppDatabase.getInstance(requireContext()).locationDao()
    }

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location
    private val REQUEST_LOCATION_PERMISSION = 1
    private lateinit var googleMap: GoogleMap
    private var mGoogleApiClient: GoogleApiClient? = null

    private fun insertEntity(entity: LocationEntity) {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                locationDao.insert(entity)
            }
        }
    }

    private fun iniciaServicio() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 1)
        } else {
            val intent = Intent(context, LocationService::class.java)
            context?.startService(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        // Registrar el receptor para recibir actualizaciones de ubicación
        context?.registerReceiver(locationReceiver, IntentFilter("ubicacionActualizada"))
    }

    override fun onPause() {
        super.onPause()
        // Desregistrar el receptor al pausar el fragmento
        context?.unregisterReceiver(locationReceiver)
    }

    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->
        this.googleMap = googleMap
        googleMap.uiSettings.isZoomControlsEnabled = true
        googleMap.uiSettings.isMyLocationButtonEnabled = true
        googleMap.uiSettings.isCompassEnabled = true
        googleMap.uiSettings.isMapToolbarEnabled = true
        googleMap.uiSettings.isIndoorLevelPickerEnabled = true
        googleMap.uiSettings.isRotateGesturesEnabled = true
        googleMap.uiSettings.isScrollGesturesEnabled = true
        googleMap.uiSettings.isTiltGesturesEnabled = true
        googleMap.uiSettings.isZoomGesturesEnabled = true


        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
        } else {
            googleMap.isMyLocationEnabled = true
            val locations = locationDao.getAll()

            // Agrega un marcador para cada ubicación.
            if (locations != null) {
                for (location in locations) {
                    val latLng = LatLng(location?.latitude ?: 0.0, location?.longitude ?: 0.0)
                    googleMap.addMarker(MarkerOptions().position(latLng).title(param1))
                }
            }


            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    Log.d("MapsFragment", "LocationEntity is not null")
                    lastLocation = location
                    val entity = LocationEntity(
                        id = null,
                        latitude = location.latitude,
                        longitude = location.longitude,
                        date = Date()
                    )
                    insertEntity(entity)
                    val currentLatLng = LatLng(location.latitude, location.longitude)
                    googleMap.addMarker(
                        MarkerOptions().position(currentLatLng).title(param1)
                    )
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))
                } else {
                    Log.d("MapsFragment", "LocationEntity is null")
                    //miami location
                    val currentLatLng = LatLng(25.7617, -80.1918)
                    googleMap.addMarker(
                        MarkerOptions().position(currentLatLng).title(param1)
                    )
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))
                }
            }

        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_maps, container, false)
        val refreshButton = view.findViewById<Button>(R.id.refreshButton)

        // Set an OnClickListener to handle the button click event
        refreshButton.setOnClickListener {
            // Call a method to update the user's location and add a new marker
            updateLocationAndMarker()
        }
        return view
    }

    private fun updateLocationAndMarker() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Get the user's current location
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                // Check if the location is not null
                location?.let {
                    val entity = LocationEntity(
                        id = null,
                        latitude = location.latitude,
                        longitude = location.longitude,
                        date = Date()
                    )
                    insertEntity(entity)
                    // Update the map camera to the new location
                    val cameraPosition = CameraPosition.Builder()
                        .target(LatLng(location.latitude, location.longitude))
                        .zoom(15f)
                        .build()
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

                    // Add a new marker at the new location
                    val markerOptions = MarkerOptions()
                        .position(LatLng(location.latitude, location.longitude))
                        .title(param1)
                    googleMap.addMarker(markerOptions)
                }
            }
        } else {
            // Request location permissions if not granted
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
        }
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        iniciaServicio()
        locationReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val latitude = intent?.getDoubleExtra("latitude", 0.0) ?: 0.0
                val longitude = intent?.getDoubleExtra("longitude", 0.0) ?: 0.0
                val entity = LocationEntity(
                    id = null,
                    latitude = latitude,
                    longitude = longitude,
                    date = Date()
                )
                //insertEntity(entity)
                val cameraPosition = CameraPosition.Builder()
                    .target(LatLng(latitude, longitude))
                    .zoom(15f)
                    .build()
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

                // Add a new marker at the new location
                val markerOptions = MarkerOptions()
                    .position(LatLng(latitude, longitude))
                    .title(param1)
                googleMap.addMarker(markerOptions)
                println(latitude.toString() + " " + longitude)
            }
        }
        context?.registerReceiver(locationReceiver, IntentFilter("ubicacionActualizada"))

    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(
                        this.requireContext(),
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this.requireContext(),
                        android.Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return
                }
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    if (location != null) {
                        lastLocation = location
                        val entity = LocationEntity(
                            id = null,
                            latitude = location.latitude,
                            longitude = location.longitude,
                            date = Date()
                        )
                        insertEntity(entity)
                        val currentLatLng = LatLng(location.latitude, location.longitude)
                        googleMap.addMarker(
                            MarkerOptions().position(currentLatLng)
                                .title(param1)
                        )
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))
                    }
                }
            }
        }
    }
}