package com.UNA.gps

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText


class PhoneFragment : Fragment() {
    private lateinit var button: Button

    fun makePhoneCall2(view: View){
        phone_number = view.findViewById<EditText>(R.id.phone_number).text.toString()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        button = view.findViewById(R.id.call_button)
        button.setOnClickListener(View.OnClickListener {
            makePhoneCall2(view)
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_phone, container, false)
    }

    companion object {
        var phone_number : String = ""
    }
}