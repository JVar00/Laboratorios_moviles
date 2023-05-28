package cr.ac.una.firebase.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cr.ac.una.firebase.R
import cr.ac.una.firebase.entity.Persona

class ListAdapter(var personas: ArrayList<Persona>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val VIEW_TYPE_HEADER = 0
    private val VIEW_TYPE_ITEM = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_HEADER) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_header, parent, false)
            HeaderViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
            ViewHolder(view)
        }


    }
    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            VIEW_TYPE_HEADER
        } else {
            VIEW_TYPE_ITEM
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = personas[position]

        if (holder is HeaderViewHolder) {
            holder.bind()
        } else if (holder is ViewHolder) {
            val controlArterialItem = item
            holder.bind(controlArterialItem)
        }
    }

    override fun getItemCount(): Int {
        return personas.size
    }
    fun updateData(newData: ArrayList<Persona>) {

        personas = newData
        if (!newData.isEmpty())
            if(newData[0]._uuid !=null)
                newData.add(0,Persona(null, "","",0))
        notifyDataSetChanged()
    }

    inner class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(){
            val nameTextView = itemView.findViewById<TextView>(R.id.Name)
            val lastTextView = itemView.findViewById<TextView>(R.id.Last_name)
            val ageTextView = itemView.findViewById<TextView>(R.id.Age)
            nameTextView.text = "Name"
            lastTextView.text = "Last Name"
            ageTextView.text = "Age"
        }

    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val nameTextView = itemView.findViewById<TextView>(R.id.Name)
        val lastTextView = itemView.findViewById<TextView>(R.id.Last_name)
        val ageTextView = itemView.findViewById<TextView>(R.id.Age)


        fun bind(persona: Persona) {
            nameTextView.text = persona.nombre
            lastTextView.text = persona.apellido
            ageTextView.text = persona.edad.toString()
        }
    }
}