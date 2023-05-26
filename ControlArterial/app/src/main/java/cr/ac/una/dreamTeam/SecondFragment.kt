package cr.ac.una.dreamTeam

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import cr.ac.una.dreamTeam.databinding.FragmentSecondBinding
import cr.ac.una.dreamTeam.viewModel.TomaArterialViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

                var distolica = textDistolica.text.toString().toInt()
                var sistolica = textSistolica.text.toString().toInt()
                var ritmo = textRitmo.text.toString().toInt()

                viewModel.addItem(distolica, sistolica, ritmo)

                //get back to main thread
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Presion arterial agregada!", Toast.LENGTH_SHORT).show()

                    textDistolica.setText("")
                    textSistolica.setText("")
                    textRitmo.setText("")
                }

            }

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