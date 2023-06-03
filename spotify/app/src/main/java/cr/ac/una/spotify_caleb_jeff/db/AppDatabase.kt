package cr.ac.una.spotify_caleb_jeff.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import cr.ac.una.spotify_caleb_jeff.DAO.HistoryDAO
import cr.ac.una.spotify_caleb_jeff.converters.Converters
import cr.ac.una.spotify_caleb_jeff.entity.History


@Database(entities = [History::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDAO

    companion object {
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            if (instance == null) {
                synchronized(AppDatabase::class) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "history-database"
                    ).build()
                }
            }
            return instance!!
        }
    }
}