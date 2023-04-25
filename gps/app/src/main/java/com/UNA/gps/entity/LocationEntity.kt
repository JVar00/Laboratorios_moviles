package com.UNA.gps.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class LocationEntity (@PrimaryKey(autoGenerate = true) val id: Long?,
                           val latitude: Double,
                           val date: Date,
                           val longitude: Double,
                           var itsInside: Boolean,)

