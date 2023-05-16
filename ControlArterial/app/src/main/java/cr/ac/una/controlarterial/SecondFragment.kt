package cr.ac.una.controlarterial

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import cr.ac.una.controlarterial.databinding.FragmentSecondBinding
import cr.ac.una.controlarterial.viewModel.TomaArterialViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel = ViewModelProvider(this).get(TomaArterialViewModel::class.java)
        val textDistolica = view.findViewById<EditText>(R.id.DistolicaText)
        val textSistolica = view.findViewById<EditText>(R.id.SistolicaText)
        val textRitmo = view.findViewById<EditText>(R.id.RitmoText)
        val buttonSend = view.findViewById<Button>(R.id.button)

        buttonSend.setOnClickListener{
            GlobalScope.launch(Dispatchers.IO) {
                viewModel.addItem(textDistolica.text, textSistolica.text, textRitmo.text)!!
            }
        }



        binding.buttonSecond.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}