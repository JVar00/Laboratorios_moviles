package cr.ac.una.firebase

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import cr.ac.una.firebase.R
import cr.ac.una.firebase.databinding.FragmentAddBinding
import cr.ac.una.firebase.modelView.PersonaModelView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class AddFragment : Fragment() {

    private var _binding: FragmentAddBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentAddBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel = ViewModelProvider(this).get(PersonaModelView::class.java)
        val buttonSend = view.findViewById<Button>(R.id.button)
        val textName = view.findViewById<EditText>(R.id.name)
        val textLast = view.findViewById<EditText>(R.id.lastName)
        val textAge = view.findViewById<EditText>(R.id.ageText)

        buttonSend.setOnClickListener{

            var name = textName.text.toString()
            var last = textLast.text.toString()
            var age = textAge.text.toString().toInt()

            viewModel.addItem(name, last, age)
            Toast.makeText(context, "Persona agregada", Toast.LENGTH_SHORT).show()

            textName.setText("")
            textLast.setText("")
            textAge.setText("")

        }

        /*
        binding.buttonSecond.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }*/

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}