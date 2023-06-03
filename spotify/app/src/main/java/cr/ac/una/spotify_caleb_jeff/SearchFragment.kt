package cr.ac.una.spotify_caleb_jeff

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import cr.ac.una.spotify_caleb_jeff.databinding.FragmentSearchBinding
import cr.ac.una.spotify_caleb_jeff.viewmodel.SpotifySearchViewmodel

/**
 * A simple [Fragment] subclass.
 * Use the [SearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel = ViewModelProvider(this).get(SpotifySearchViewmodel::class.java)

        /*

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

        */

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}