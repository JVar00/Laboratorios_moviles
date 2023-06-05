package cr.ac.una.spotify_caleb_jeff.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import cr.ac.una.spotify_caleb_jeff.R
import cr.ac.una.spotify_caleb_jeff.entity.Album
import cr.ac.una.spotify_caleb_jeff.entity.Artist
import cr.ac.una.spotify_caleb_jeff.entity.Cover
import cr.ac.una.spotify_caleb_jeff.entity.History
import cr.ac.una.spotify_caleb_jeff.entity.Track

class HistoryAdapter(var history: ArrayList<History>, var onItemClick: (String) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val VIEW_TYPE_ITEM = 0
    //afectara?

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.history_item, parent, false)
        return ViewHolder(view)

    }
    override fun getItemViewType(position: Int): Int {
        return VIEW_TYPE_ITEM
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
       if (holder is ViewHolder) {
           holder.bind(history[position])
           holder.itemView.setOnClickListener {
               onItemClick(history[position].song_name)
           }
       }
    }

    override fun getItemCount(): Int {
        return history.size
    }
    fun updateData(newData: ArrayList<History>) {
        history = newData

        if (newData.isEmpty())
            newData.add(0, History(null, ""))
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val trackName_View = itemView.findViewById<TextView>(R.id.history_text)

        fun bind(track: History) {

            val trackName = track.song_name
            trackName_View.text = trackName

        }
    }
}