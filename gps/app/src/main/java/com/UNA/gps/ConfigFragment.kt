package com.UNA.gps

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

/**
 * A simple [Fragment] subclass.
 * Use the [ConfigFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ConfigFragment : Fragment() {

    ///val message
    private var message: String? = "Mi marcador por default"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            message = it.getString("message")
        }
    }

    interface OnMessageSendListener {
        fun onMessageSent(message: String)
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

        sendButton.setOnClickListener {
            Toast.makeText(requireContext(), "Se cambio exitosamente el nombre del marcador!", Toast.LENGTH_SHORT).show()
            val message = messageEditText.text.toString()
            listener?.onMessageSent(message)
        }

        return view
    }


}