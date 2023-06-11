package cr.ac.una.spotify_caleb_jeff.viewmodel

import android.content.Context
import android.util.Base64
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cr.ac.una.spotify_caleb_jeff.DAO.HistoryDAO
import cr.ac.una.spotify_caleb_jeff.db.AppDatabase
import cr.ac.una.spotify_caleb_jeff.entity.Track
import cr.ac.una.spotify_caleb_jeff.entity.AccessTokenResponse
import cr.ac.una.spotify_caleb_jeff.entity.Album
import cr.ac.una.spotify_caleb_jeff.entity.AlbumResponse
import cr.ac.una.spotify_caleb_jeff.entity.Artist
import cr.ac.una.spotify_caleb_jeff.entity.Cover
import cr.ac.una.spotify_caleb_jeff.entity.History
import cr.ac.una.spotify_caleb_jeff.entity.TrackResponse
import cr.ac.una.spotify_caleb_jeff.service.SpotifyService

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SpotifySearchViewmodel: ViewModel() {

    private var _tracks: MutableLiveData<List<Track>> = MutableLiveData()
    var tracks : LiveData<List<Track>> = _tracks

    private var _history: MutableLiveData<List<History>> = MutableLiveData()
    var history : LiveData<List<History>> = _history

    private lateinit var historyDAO : HistoryDAO

    private var _errorMessage: MutableLiveData<String> = MutableLiveData()
    var errorMessage: LiveData<String> = _errorMessage

    private var albumURL = ""

   private fun displayErrorMessage(message: String) {
        _errorMessage.value = message
    }

    private val spotifyServiceToken: SpotifyService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://accounts.spotify.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(SpotifyService::class.java)
    }
    private val spotifyService: SpotifyService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.spotify.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(SpotifyService::class.java)
    }

    private fun getAccessToken(): Call<AccessTokenResponse> {
        val clientId = "f13969da015a4f49bb1f1edef2185d4e"
        val clientSecret = "e3077426f4714315937111d5e82cd918"
        val base64Auth = Base64.encodeToString("$clientId:$clientSecret".toByteArray(), Base64.NO_WRAP)

        return spotifyServiceToken.getAccessToken(
            "Basic $base64Auth",
            "client_credentials"
        )
    }

   fun search(query: String){

        val tokenRequest = getAccessToken()
        tokenRequest.enqueue(object : Callback<AccessTokenResponse> {
            override fun onResponse(call: Call<AccessTokenResponse>, response: Response<AccessTokenResponse>) {
                if (response.isSuccessful) {
                    val accessTokenResponse = response.body()
                    val accessToken = accessTokenResponse?.accessToken

                    if (accessToken != null) {

                        val searchRequest = spotifyService.searchTrack("Bearer $accessToken", query)
                        searchRequest.enqueue(object : Callback<TrackResponse> {
                            override fun onResponse(call: Call<TrackResponse>, response: Response<TrackResponse>) {
                                if (response.isSuccessful) {
                                    val trackResponse = response.body()
                                    val trackList = mutableListOf<Track>()

                                    //println(trackResponse)

                                    if (trackResponse != null && trackResponse.tracks.items.isNotEmpty()) {

                                        for (track in trackResponse.tracks.items){

                                            // Create a Track object and populate its properties
                                            val album = track.album
                                            val artists = track.artists
                                            println(artists)

                                            val albumName = album.name
                                            val imageUrl = album.images[0].url
                                            val albumId = album.id

                                            val cover = ArrayList<Cover>()
                                            cover.add(Cover(imageUrl))

                                            val trackObject = Track(
                                                track.id,
                                                track.name,
                                                Album(albumId, albumName, cover),
                                                artists,
                                                track.uri,
                                                track.preview_url,
                                                track.popularity
                                            )

                                            trackList.add(trackObject)

                                            //println("Track: " + track.name)

                                        }
                                        _tracks.postValue(trackList)

                                    } else {
                                        displayErrorMessage("No se encontraron canciones.")
                                    }

                                } else {
                                    System.out.println("Mensaje:    "+response.raw())
                                    displayErrorMessage("Error en la respuesta del servidor.")
                                }
                            }

                            override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                                println(t)
                                displayErrorMessage(t.message ?: "Error en la solicitud de búsqueda.")
                            }
                        })
                    } else {
                        displayErrorMessage("Error al obtener el accessToken.")
                    }
                } else {
                    System.out.println("Mensaje:    "+response.raw())
                    displayErrorMessage("Error en la respuesta del servidor.")
                }
            }

            override fun onFailure(call: Call<AccessTokenResponse>, t: Throwable) {
                displayErrorMessage("Error en la solicitud de accessToken.")
            }
        })
    }

    fun addHistory(context: Context, query: String) {
        initDatabase(context)
        historyDAO.insert(History(null, query))
    }

    fun deleteHistoryItem(context: Context, entity: History){
        initDatabase(context)
        historyDAO.delete(entity)
    }

    fun getHistory(context: Context, text: String){
        initDatabase(context)
        _history.postValue(historyDAO.typeHistory(text))
    }

    private fun initDatabase(context: Context) {
        historyDAO = AppDatabase.getInstance(context).historyDao()
    }

}

/*

    private fun displayTrackInfo(trackName: String, artistName: String) {
        val message = "Canción encontrada: $trackName - $artistName"

    }



 */