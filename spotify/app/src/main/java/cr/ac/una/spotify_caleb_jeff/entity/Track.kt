package cr.ac.una.spotify_caleb_jeff.entity

data class Track(
    val name: String,
    val album: Album,
    val artists: ArrayList<Artist>,
    val uri: String
)