package com.UNA.gps.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.UNA.gps.entity.LocationEntity


@Dao
interface LocationDAO {
    @Insert
    fun insert(entity: LocationEntity)

    @Query("SELECT * FROM LocationEntity")
    fun getAll(): List<LocationEntity?>?
}