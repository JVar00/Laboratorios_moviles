package cr.ac.una.spotify_caleb_jeff.entity

data class AlbumResponse (
    val name: String,
    val images: ArrayList<Cover>,
    val genres: ArrayList<String>,
    val tracks: ArrayList<Tracks>
    )