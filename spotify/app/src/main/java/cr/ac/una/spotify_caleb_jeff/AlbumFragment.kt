package cr.ac.una.spotify_caleb_jeff

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import cr.ac.una.spotify_caleb_jeff.adapter.SearchAdapter
import cr.ac.una.spotify_caleb_jeff.entity.Track
import cr.ac.una.spotify_caleb_jeff.viewmodel.AlbumSearchViewmodel
import cr.ac.una.spotify_caleb_jeff.viewmodel.SpotifySearchViewmodel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "album"

/**
 * A simple [Fragment] subclass.
 * Use the [AlbumFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AlbumFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var album: String? = null
    private lateinit var tracks: List<Track>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            album = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_album, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tracks = mutableListOf<Track>()

        val viewModel = ViewModelProvider(this).get(AlbumSearchViewmodel::class.java)

        val listView = view.findViewById<RecyclerView>(R.id.recycler_songs)
        val adapter = SearchAdapter(tracks as ArrayList<Track>, requireContext()) { selectedItem ->
            //
        }

        listView.adapter = adapter
        listView.layoutManager = LinearLayoutManager(requireContext())

        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
        }
        viewModel.tracks.observe(viewLifecycleOwner) { elementos ->
            adapter.updateData(elementos as ArrayList<Track>)
            tracks = elementos
        }

        album?.let {
            viewModel.searchAlbum(it) }

        val cover = view.findViewById<ImageView>(R.id.image_album)
        val name = view.findViewById<TextView>(R.id.text_album_name)
        val artist = view.findViewById<TextView>(R.id.text_artist_name)
        val progressBar = view.findViewById<ProgressBar>(R.id.loading_progress)
        val genres = view.findViewById<TextView>(R.id.text_genres)
        val releasedDate = view.findViewById<TextView>(R.id.ReleasedDate)

        progressBar.visibility = View.VISIBLE

        viewModel.album.observe(viewLifecycleOwner) { album ->

            name.text = album.name
            releasedDate.text = album.release_date

            val artistsBuilder = StringBuilder()
            for ((index, _artist) in album.artists.withIndex()) {
                artistsBuilder.append(_artist.name)
                if (index < album.artists.size - 1) {
                    artistsBuilder.append(", ")
                }
            }
            artist.text = artistsBuilder.toString()

            val genreBuilder = StringBuilder()
            for ((index, genre) in album.genres.withIndex()) {
                genreBuilder.append(genre)
                if (index < album.genres.size - 1) {
                    genreBuilder.append(" - ")
                }
            }
            genres.text = genreBuilder.toString()

            if (genres.text == "") {
                genres.text = "Rock, Rock Español, Classic Rock"
            }

            Glide.with(view).load(album.images[0].url).listener(object: RequestListener<Drawable> {

                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    progressBar.visibility = View.GONE
                    return false
                }

                override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                    progressBar.visibility = View.GONE
                    return false
                }

            }).error(R.drawable.ic_launcher_foreground).into(cover)

            cover.contentDescription = album.name


        }

    }
}