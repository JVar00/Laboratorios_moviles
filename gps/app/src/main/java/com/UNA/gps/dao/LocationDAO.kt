package com.UNA.gps.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.UNA.gps.entity.Location


@Dao
interface LocationDAO {
    @Insert
    fun insert(entity: Location)

    @Query("SELECT * FROM location")
    fun getAll(): List<Location?>?
}