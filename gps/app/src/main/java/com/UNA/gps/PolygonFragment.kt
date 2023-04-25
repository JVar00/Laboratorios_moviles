package com.UNA.gps

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.UNA.gps.dao.PolygonDAO
import com.UNA.gps.db.AppDatabase
import com.UNA.gps.entity.LocationEntity
import com.UNA.gps.entity.PolygonEntity
import com.google.android.gms.maps.model.Polygon
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PolygonFragment : Fragment() {


    // Declare DAO and list of polygons
    private lateinit var polygonDao: PolygonDAO
    private lateinit var polygonList: MutableList<PolygonEntity>
    private lateinit var polygonAdapter: PolygonAdapter

    private fun insertEntity(entity: PolygonEntity) {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                polygonDao.insert(entity)
            }
            val polygons = withContext(Dispatchers.IO) {
                polygonDao.getAll()
            } as MutableList<PolygonEntity>
            polygonList.clear()
            polygonList.addAll(polygons)
            polygonAdapter.submitList(polygonList)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate layout
        val view = inflater.inflate(R.layout.fragment_polygon, container, false)
        val recycler = view.findViewById<RecyclerView>(R.id.polygon_recycler)
        recycler.layoutManager = LinearLayoutManager(requireActivity())

        //adapter
        polygonAdapter = PolygonAdapter(this::deletePoint)
        recycler.adapter = polygonAdapter

        // Initialize DAO
        polygonDao = AppDatabase.getInstance(requireContext()).polygonDao()
        polygonList = mutableListOf()

        // Retrieve polygons from DAO
        lifecycleScope.launch {
            val polygons = withContext(Dispatchers.IO) {
                polygonDao.getAll()
            } as MutableList<PolygonEntity>
            polygonList.clear()
            polygonList.addAll(polygons)
            polygonAdapter.submitList(polygonList)
        }

        // Set up button to add a new polygon
        val addButton = view.findViewById<Button>(R.id.addButton)
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_add_point, null, false)
        val latitudeEditText = dialogView.findViewById<EditText>(R.id.latitudeEditText)
        val longitudeEditText = dialogView.findViewById<EditText>(R.id.longitudeEditText)

        addButton.setOnClickListener {
            // Create a dialog to prompt the user to enter the point's latitude and longitude
            val dialog = AlertDialog.Builder(requireContext())
                .setTitle("Add Point")
                .setView(dialogView)
                .setPositiveButton("Add") { dialog, _ ->
                    // Get the latitude and longitude entered by the user

                    val latitude = latitudeEditText.text.toString().toDoubleOrNull()
                    val longitude = longitudeEditText.text.toString().toDoubleOrNull()

                    // If the latitude and longitude are valid, add a new point to the database and the list
                    if (latitude != null && longitude != null) {
                        val point = PolygonEntity(id = null, latitude = latitude, longitude = longitude)
                        insertEntity(point)
                        dialog.dismiss()
                    } else {
                        // Show an error message if the latitude and/or longitude are not valid
                        Toast.makeText(requireContext(), "Please enter a valid latitude and longitude", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.cancel()
                }
                .create()

            dialog.show()
        }

        return view
    }

    private fun deletePoint(point: PolygonEntity) {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                polygonDao.deletePoint(point)
            }
            val polygons = withContext(Dispatchers.IO) {
                polygonDao.getAll()
            } as MutableList<PolygonEntity>
            polygonList.clear()
            polygonList.addAll(polygons)
            polygonAdapter.submitList(polygonList)
        }
    }
}
