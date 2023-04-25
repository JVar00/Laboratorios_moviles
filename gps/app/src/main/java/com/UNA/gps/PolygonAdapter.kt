package com.UNA.gps

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.UNA.gps.entity.PolygonEntity


class PolygonAdapter(
    private val deleteClickListener: (PolygonEntity) -> Unit
) : ListAdapter<PolygonEntity, PolygonAdapter.PolygonViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<PolygonEntity>() {
        override fun areItemsTheSame(oldItem: PolygonEntity, newItem: PolygonEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PolygonEntity, newItem: PolygonEntity): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PolygonViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.polygon_list_item, parent, false)
        return PolygonViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PolygonViewHolder, position: Int) {
        val polygon = getItem(position)
        holder.bind(polygon)
    }

    inner class PolygonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val latitudeTextView = itemView.findViewById<TextView>(R.id.polygon_latitude)
        private val longitudeTextView = itemView.findViewById<TextView>(R.id.polygon_longitude)
        private val deleteButton = itemView.findViewById<ImageView>(R.id.deleteButton)

        fun bind(point: PolygonEntity) {
            longitudeTextView.text = point.longitude.toString()
            latitudeTextView.text = point.latitude.toString()
            deleteButton.setImageResource(R.drawable.img)
            deleteButton.setOnClickListener{ deleteClickListener(point) }
        }

    }

}