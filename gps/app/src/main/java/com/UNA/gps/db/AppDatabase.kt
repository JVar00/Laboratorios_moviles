package com.UNA.gps.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.UNA.gps.MapsFragment

import com.UNA.gps.dao.LocationDAO
import com.UNA.gps.dao.PolygonDAO
import com.UNA.gps.entity.LocationEntity
import com.UNA.gps.converter.Converters
import com.UNA.gps.entity.PolygonEntity

@Database(entities = [LocationEntity::class, PolygonEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun locationDao(): LocationDAO
    abstract fun polygonDao(): PolygonDAO

    companion object {
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            if (instance == null) {
                synchronized(AppDatabase::class) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "locations-database"
                    ).build()
                }
            }
            return instance!!
        }
    }
}