package com.UNA.gps.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.UNA.gps.entity.PolygonEntity


@Dao
interface PolygonDAO {
    @Insert
    fun insert(entity: PolygonEntity)

    @Query("SELECT * FROM PolygonEntity")
    fun getAll(): List<PolygonEntity?>?

    @Delete
    fun deletePoint(entity: PolygonEntity)
}