package cr.ac.una.spotify_caleb_jeff.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import cr.ac.una.spotify_caleb_jeff.entity.History

@Dao
interface HistoryDAO {
    @Insert
    fun insert(entity: History)

    @Delete
    fun delete(entity: History)

    @Query("SELECT * FROM History")
    fun getAll(): List<History?>?
}