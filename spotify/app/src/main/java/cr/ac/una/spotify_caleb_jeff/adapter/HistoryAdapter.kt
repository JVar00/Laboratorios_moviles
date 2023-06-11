package cr.ac.una.spotify_caleb_jeff.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cr.ac.una.spotify_caleb_jeff.R
import cr.ac.una.spotify_caleb_jeff.entity.History
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter

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
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val trackNameView = itemView.findViewById<TextView>(R.id.history_text)
        val trackDateView = itemView.findViewById<TextView>(R.id.history_date)


        fun bind(track: History) {

            val trackName = track.song_name
            val trackDate = track.date
            val formatter = SimpleDateFormat("yyyy-MM-dd")

            trackNameView.text = trackName
            trackDateView.text = formatter.format(trackDate)

        }
    }
}