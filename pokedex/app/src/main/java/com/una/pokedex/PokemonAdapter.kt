package com.una.pokedex

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView.OnChildClickListener
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class PokemonAdapter: ListAdapter<Pokemon, PokemonAdapter.ViewHolder>(DiffCallback) {

        companion object DiffCallback : DiffUtil.ItemCallback<Pokemon>() {
            override fun areItemsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean {
                return oldItem == newItem
            }
        }

    lateinit var onItemClickListener: (Pokemon) -> Unit

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonAdapter.ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.pokemon_list_item, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val pokemon = getItem(position)
            holder.bind(pokemon)
        }

        inner class ViewHolder(private val view : View) : RecyclerView.ViewHolder(view) {
            private val id = view.findViewById<TextView>(R.id.pokemon_id)
            private val name = view.findViewById<TextView>(R.id.pokemon_name)
            private val image = view.findViewById<ImageView>(R.id.pokemon_type_image)

            fun bind(pokemon: Pokemon) {
                id.text = pokemon.id.toString()
                name.text = pokemon.name
                val imageId = when(pokemon.type){
                    Pokemon.Type.GRASS -> R.drawable.grass_icon
                    Pokemon.Type.WATER -> R.drawable.water_icon
                    Pokemon.Type.FIRE -> R.drawable.fire_icon
                    Pokemon.Type.FIGHTER -> R.drawable.fighter_icon
                    Pokemon.Type.ELECTRIC -> R.drawable.electric_icon
                }
                image.setImageResource(imageId)

                view.setOnClickListener{
                    if(::onItemClickListener.isInitialized){
                        onItemClickListener(pokemon)
                    }
                }

            }
        }
}