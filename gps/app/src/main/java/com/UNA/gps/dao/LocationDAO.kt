package com.UNA.gps.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.UNA.gps.entity.LocationEntity


@Dao
interface LocationDAO {
    @Insert
    fun insert(entity: LocationEntity)

    @Update
    fun update(entity: LocationEntity)

    @Query("SELECT * FROM LocationEntity")
    fun getAll(): List<LocationEntity?>?
}