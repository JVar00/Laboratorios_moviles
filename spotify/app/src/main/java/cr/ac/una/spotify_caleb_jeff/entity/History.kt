package cr.ac.una.spotify_caleb_jeff.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class History (@PrimaryKey(autoGenerate = true) val id: Long?,
                    val song_name: String)