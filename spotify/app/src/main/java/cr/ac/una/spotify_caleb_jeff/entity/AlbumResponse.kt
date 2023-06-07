package cr.ac.una.spotify_caleb_jeff.entity

data class AlbumResponse (
    val name: String,
    val release_date: String,
    val images: ArrayList<Cover>,
    val artists: ArrayList<Artist>,
    val genres: ArrayList<String>,
    val tracks: Tracks
    )