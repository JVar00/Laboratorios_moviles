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
import android.widget.Toast

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
import com.UNA.gps.dao.PolygonDAO
import com.UNA.gps.db.AppDatabase
import com.UNA.gps.entity.LocationEntity
import com.UNA.gps.entity.PolygonEntity
import com.google.android.gms.maps.model.*
import com.google.maps.android.PolyUtil
import kotlinx.coroutines.launch


class MapsFragment : Fragment() {

    private lateinit var locationReceiver: BroadcastReceiver
    private var param1: String? = "Marcador default por maps"
    private lateinit var locationDao: LocationDAO
    private lateinit var polygonDao: PolygonDAO
    private lateinit var polygon: Polygon
    private lateinit var polygonList: List<LatLng>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString("message")
        }
        locationDao = AppDatabase.getInstance(requireContext()).locationDao()
        polygonDao = AppDatabase.getInstance(requireContext()).polygonDao()
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

                /*
                val cameraPosition = CameraPosition.Builder()
                    .target(LatLng(location.latitude, location.longitude))
                    .zoom(15f)
                    .build()
                googleMap.animateCamera(
                    CameraUpdateFactory.newCameraPosition(
                        cameraPosition
                    )
                )*/
                if (isLocationInsidePolygon(location)){
                    googleMap.addMarker(
                    MarkerOptions().position(location).title(param1).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                    )
                    entity.itsInside = true
                }else{
                    googleMap.addMarker(
                        MarkerOptions().position(location).title(param1).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                    )
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

        polygonList.forEach { point ->
            point?.let { location ->
                println(location)
                polygonOptions.add(location)
            }
        }

        //NO SE COMO HACER QUE SE ESPEREEEEEEEEE ESTO ES PARA QUE NO EXPLOTE Y CON AGREGARLE UNO YA LOS CARGA TODOS WTF
        //ALGO ES ALGO LA VERDAD POR AHORA YA SU MAYORIA ESTA
        polygonOptions.add(LatLng(-14.0095923,108.8152324))

        polygonOptions.strokeColor(ContextCompat.getColor(requireContext(), R.color.purple_200))

        return googleMap.addPolygon(polygonOptions)
    }

    private fun extractLocations() {
        lifecycleScope.launch {
            val ubicaciones = withContext(Dispatchers.IO) {
                locationDao.getAll()
            }
            ubicaciones?.forEach { ubicacion ->
                ubicacion?.let { location ->
                    val latLng = LatLng(location.latitude, location.longitude)

                    // OJO CON ESTE SE DEBE DE TESTEAR SI DE VERDAD LO ESTA ACTUALIZANDO O SI ES NECESARIO ES SOLO TEST NO AFECTA
                    if (isLocationInsidePolygon(latLng)){
                        googleMap.addMarker(MarkerOptions().position(latLng).title(param1).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)))
                        location.itsInside = true
                        withContext(Dispatchers.IO) {
                            locationDao.update(location)
                        }
                    } else {
                        googleMap.addMarker(MarkerOptions().position(latLng).title(param1).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)))
                        location.itsInside = false
                        withContext(Dispatchers.IO) {
                            locationDao.update(location)
                        }
                    }
                }
            }
        }
    }
    private fun extractPolygons() {
        polygonList = mutableListOf()
        lifecycleScope.launch {
            val polygonPoints = withContext(Dispatchers.IO) {
                polygonDao.getAll()
            }
            if(!polygonPoints.isNullOrEmpty()){
                polygonPoints.forEach { point ->
                    point?.let { location ->
                        (polygonList as MutableList<LatLng>).add(LatLng(location.latitude, location.longitude))
                    }
                }
            } else {

                (polygonList as MutableList<LatLng>).add(LatLng(-14.0095923,108.8152324))
                (polygonList as MutableList<LatLng>).add(LatLng(-43.3897529,104.2449199))
                (polygonList as MutableList<LatLng>).add(LatLng(-51.8906238,145.7292949))
                (polygonList as MutableList<LatLng>).add(LatLng(-31.7289525,163.3074199))
                (polygonList as MutableList<LatLng>).add(LatLng(-7.4505398,156.2761699))
                (polygonList as MutableList<LatLng>).add(LatLng(-14.0095923,108.8152324))

                Toast.makeText(requireContext(), "No points to create a personalized polygon", Toast.LENGTH_SHORT).show()
            }
        }
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
                        googleMap.addMarker(MarkerOptions().position(currentLatLng).title(param1).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)))
                        entity.itsInside = true
                    } else {
                        googleMap.addMarker(MarkerOptions().position(currentLatLng).title(param1).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)))
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
        //val refreshButton = view.findViewById<Button>(R.id.refreshButton)
        val database = AppDatabase.getInstance(requireContext())

        locationDao = database.locationDao()
        polygonDao = database.polygonDao()

        extractPolygons()
        extractLocations()

        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        // Set an OnClickListener to handle the button click event
        //refreshButton.setOnClickListener {
            // Call a method to update the user's location and add a new marker
            //updateLocationAndMarker()
        //}

        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        locationDao = AppDatabase.getInstance(requireContext()).locationDao()
        polygonDao = AppDatabase.getInstance(requireContext()).polygonDao()

        fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(this.requireContext())

        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        val locationServiceIntent = Intent(requireContext(), LocationService::class.java)
        requireContext().startService(locationServiceIntent)

        initLocationReceiver()
        context?.registerReceiver(locationReceiver, IntentFilter("ubicacionActualizada"))


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
                    Log.d("MapsFragment", "Permission Granted!")
                    //val intent = Intent(context, LocationService::class.java)
                    //context?.startService(intent)
                }
            } else {
                // Permiso denegado, maneja la situación de acuerdo a tus necesidades
                Log.d("MapsFragment", "LocationEntity is null")
                //miami location
                val currentLatLng = LatLng(25.7617, -80.1918)
                googleMap.addMarker(
                    MarkerOptions().position(currentLatLng).title(param1).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                )
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))
            }
        }
    }


}