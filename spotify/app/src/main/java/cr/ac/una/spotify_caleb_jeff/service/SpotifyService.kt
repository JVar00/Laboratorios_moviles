package cr.ac.una.spotify_caleb_jeff.service

import cr.ac.una.spotify_caleb_jeff.entity.AccessTokenResponse
import cr.ac.una.spotify_caleb_jeff.entity.TrackResponse
import retrofit2.Call
import retrofit2.http.*

interface SpotifyService {
    @FormUrlEncoded
    @POST("api/token")
    fun getAccessToken(
        @Header("Authorization") authorization: String,
        @Field("grant_type") grantType: String
    ): Call<AccessTokenResponse>

    @GET("v1/search?type=track")
    fun searchTrack(
        @Header("Authorization") authorization: String,
        @Query("q") query: String
    ): Call<TrackResponse>
}