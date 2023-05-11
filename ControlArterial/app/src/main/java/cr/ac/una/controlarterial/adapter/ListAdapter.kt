package cr.ac.una.controlarterial.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import cr.ac.una.controlarterial.R
import cr.ac.una.controlarterial.entity.TomaArterial
import cr.ac.una.controlarterial.entity.TomasArteriales


class ListAdapter(context: Context, tomasArteriales: List<TomaArterial>) :
    ArrayAdapter<TomaArterial>(context, 0, tomasArteriales) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        }

        val ubicacion = getItem(position)

        val distolicaText = view!!.findViewById<TextView>(R.id.distolica)
        val sistolicaText = view.findViewById<TextView>(R.id.sistolica)
        val ritmoText = view.findViewById<TextView>(R.id.ritmo)

        distolicaText.text = ubicacion!!.distolica.toString()
        sistolicaText.text = ubicacion.sistolica.toString()
        ritmoText.text = ubicacion.ritmo.toString()

        return view
    }
}