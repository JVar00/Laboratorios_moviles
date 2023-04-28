package com.UNA.gps

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import java.util.Date

/**
 * A simple [Fragment] subclass.
 * Use the [ConfigFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ConfigFragment : Fragment() {

    ///val message
    private var message: String? = "Mi marcador por default"
    private var fecha: Date? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            message = it.getString("message")
            fecha = it.getSerializable("fecha") as Date?
        }
    }

    interface OnMessageSendListener {
        fun onMessageSent(message: String, fecha: Date)
    }
    private var listener: OnMessageSendListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        listener = try {
            context as OnMessageSendListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement OnMessageSendListener")
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_config, container, false)
        val messageEditText = view.findViewById<EditText>(R.id.editTextTextPersonName)
        val sendButton = view.findViewById<Button>(R.id.buttonSend)
        messageEditText.setText(message)

        val datePicker = view.findViewById<DatePicker>(R.id.datePicker)
        datePicker.init(fecha!!.year + 1900, fecha!!.month, fecha!!.date, null)

        sendButton.setOnClickListener {

            //Obtener la fecha seleccionada por el usuario
            val year = datePicker.year
            val month = datePicker.month
            val day = datePicker.dayOfMonth

            //Crear un objeto Date con la fecha seleccionada
            val fechaSeleccionada = Date(year - 1900, month, day)
            val message = messageEditText.text.toString()

            listener?.onMessageSent(message, fechaSeleccionada)
            Toast.makeText(requireContext(), "Se guardo exitosamente la configuracion", Toast.LENGTH_SHORT).show()
        }

        return view
    }


}