package com.UNA.gps.entity
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class PolygonEntity(
    @PrimaryKey(autoGenerate = true) val id: Long?,
    val latitude: Double,
    val longitude: Double)
