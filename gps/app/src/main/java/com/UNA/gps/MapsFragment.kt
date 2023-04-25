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

import android.Manifest
import android.annotation.SuppressLint
import android.content.*
import android.content.pm.PackageManager
import android.location.Location
import android.os.IBinder

import android.util.Log
import android.widget.Button

import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import com.google.android.gms.common.api.GoogleApiClient

import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

import androidx.lifecycle.lifecycleScope
import com.UNA.gps.dao.LocationDAO
import com.UNA.gps.db.AppDatabase
import com.UNA.gps.entity.LocationEntity
import com.google.android.gms.maps.model.*
import com.google.maps.android.PolyUtil
import kotlinx.coroutines.launch


class MapsFragment : Fragment() {

    private lateinit var locationReceiver: BroadcastReceiver
    private var param1: String? = "Marcador default por maps"
    private lateinit var locationDao: LocationDAO
    private lateinit var polygon: Polygon

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

    private fun initLocationReceiver() {

        locationReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val latitud = intent?.getDoubleExtra("latitud", 0.0) ?: 0.0
                val longitud = intent?.getDoubleExtra("longitud", 0.0) ?: 0.0
                val location = LatLng(latitud, longitud)
                val entity = LocationEntity(
                    id = null,
                    latitude = location.latitude,
                    longitude = location.longitude,
                    date = Date(),
                    itsInside = false
                )
                googleMap.addMarker(
                    MarkerOptions().position(location).title(param1)
                )
                val cameraPosition = CameraPosition.Builder()
                    .target(LatLng(location.latitude, location.longitude))
                    .zoom(15f)
                    .build()
                googleMap.animateCamera(
                    CameraUpdateFactory.newCameraPosition(
                        cameraPosition
                    )
                )
                if (isLocationInsidePolygon(location)){
                    entity.itsInside = true
                }
                insertEntity(entity)
            }
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

    private fun createPolygon(): Polygon {
        val polygonOptions = PolygonOptions()
        polygonOptions.add(LatLng(-14.0095923,108.8152324))
        polygonOptions.add(LatLng( -43.3897529,104.2449199))
        polygonOptions.add(LatLng( -51.8906238,145.7292949))
        polygonOptions.add(LatLng( -31.7289525,163.3074199))
        polygonOptions.add(LatLng( -7.4505398,156.2761699))
        polygonOptions.add(LatLng( -14.0095923,108.8152324))
        return googleMap.addPolygon(polygonOptions)
    }
    private fun isLocationInsidePolygon(location: LatLng): Boolean {
        return polygon != null && PolyUtil.containsLocation(location, polygon?.points, true)
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

        polygon = createPolygon()

        val intent = Intent(context, LocationService::class.java)
        context?.startService(intent)

        initLocationReceiver()
        context?.registerReceiver(locationReceiver, IntentFilter("ubicacionActualizada"))

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

            val fusedLocationClient =
                LocationServices.getFusedLocationProviderClient(requireContext())
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    Log.d("MapsFragment", "LocationEntity is not null")
                    lastLocation = location
                    val entity = LocationEntity(
                        id = null,
                        latitude = location.latitude,
                        longitude = location.longitude,
                        date = Date(),
                        itsInside = false
                    )

                    val currentLatLng = LatLng(location.latitude, location.longitude)
                    googleMap.addMarker(
                        MarkerOptions().position(currentLatLng).title(param1)
                    )
                    googleMap.moveCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            currentLatLng,
                            12f
                        )
                    )
                    if (isLocationInsidePolygon(currentLatLng)){
                        entity.itsInside = true
                    }
                    insertEntity(entity)

                } else {
                    Log.d("MapsFragment", "LocationEntity is null")
                    //miami location
                    val currentLatLng = LatLng(25.7617, -80.1918)
                    googleMap.addMarker(
                        MarkerOptions().position(currentLatLng).title(param1)
                    )
                    googleMap.moveCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            currentLatLng,
                            12f
                        )
                    )
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
        val database = AppDatabase.getInstance(requireContext())
        locationDao = database.locationDao()

        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync { map ->
            googleMap = map
        }
        lifecycleScope.launch {
            val ubicaciones = withContext(Dispatchers.IO) {
                locationDao.getAll()
            }
            ubicaciones?.forEach { ubicacion ->
                ubicacion?.let { location ->
                    val latLng = LatLng(location.latitude, location.longitude)
                    googleMap.addMarker(MarkerOptions().position(latLng).title(param1))

                }


            }
        }

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
            val fusedLocationClient =
                LocationServices.getFusedLocationProviderClient(requireContext())
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                // Check if the location is not null
                location?.let {
                    val entity = LocationEntity(
                        id = null,
                        latitude = location.latitude,
                        longitude = location.longitude,
                        date = Date(),
                        itsInside = false
                    )
                    val currentLatLng = LatLng(location.latitude, location.longitude)
                    // Update the map camera to the new location
                    val cameraPosition = CameraPosition.Builder()
                        .target(LatLng(location.latitude, location.longitude))
                        .zoom(15f)
                        .build()
                    googleMap.animateCamera(
                        CameraUpdateFactory.newCameraPosition(
                            cameraPosition
                        )
                    )
                    // Add a new marker at the new location
                    val markerOptions = MarkerOptions()
                        .position(LatLng(location.latitude, location.longitude))
                        .title(param1)
                    googleMap.addMarker(markerOptions)
                    if (isLocationInsidePolygon(currentLatLng)){
                        entity.itsInside = true
                    }
                    insertEntity(entity)
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

        locationDao = AppDatabase.getInstance(requireContext()).locationDao()

        //val locationServiceIntent = Intent(requireContext(), LocationService::class.java)
        //requireContext().startService(locationServiceIntent)

        fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(this.requireContext())

        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)


    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    //val intent = Intent(context, LocationService::class.java)
                    //context?.startService(intent)
                }
            } else {
                // Permiso denegado, maneja la situación de acuerdo a tus necesidades
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