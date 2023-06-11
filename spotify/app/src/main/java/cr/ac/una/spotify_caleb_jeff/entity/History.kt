package cr.ac.una.spotify_caleb_jeff.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class History (@PrimaryKey(autoGenerate = true) val id: Long?,
                    val song_name: String, val date: Date
)