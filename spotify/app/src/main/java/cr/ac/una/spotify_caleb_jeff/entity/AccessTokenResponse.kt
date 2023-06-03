package cr.ac.una.spotify_caleb_jeff.entity

import com.google.gson.annotations.SerializedName

data class AccessTokenResponse(
    @SerializedName("access_token")
    val accessToken: String?
)