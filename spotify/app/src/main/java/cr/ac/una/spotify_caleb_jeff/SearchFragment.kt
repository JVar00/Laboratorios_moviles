package cr.ac.una.spotify_caleb_jeff

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cr.ac.una.spotify_caleb_jeff.adapter.SearchAdapter
import cr.ac.una.spotify_caleb_jeff.databinding.FragmentSearchBinding
import cr.ac.una.spotify_caleb_jeff.entity.Track
import cr.ac.una.spotify_caleb_jeff.viewmodel.SpotifySearchViewmodel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * A simple [Fragment] subclass.
 * Use the [SearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private lateinit var tracks: List<Track>

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

        tracks = mutableListOf<Track>()

        val listView = view.findViewById<RecyclerView>(R.id.list_view)
        val adapter = SearchAdapter(tracks as ArrayList<Track>)
        listView.adapter = adapter
        listView.layoutManager = LinearLayoutManager(requireContext())

        val viewModel = ViewModelProvider(this).get(SpotifySearchViewmodel::class.java)
        val searchField = view.findViewById<SearchView>(R.id.search_bar)

        viewModel.tracks.observe(viewLifecycleOwner) { elementos ->
            adapter.updateData(elementos as ArrayList<Track>)
            tracks = elementos
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
        }

        searchField.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {
                lifecycleScope.launch {
                    withContext(Dispatchers.IO) {
                        viewModel.addHistory(requireContext(), query)
                    }
                }
                viewModel.search(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {

                lifecycleScope.launch {
                    withContext(Dispatchers.IO) {
                        viewModel.getHistory(requireContext(), newText)
                        //el getHistory actualizaria la lista del historial
                        //hay que hacer un adapter para el historial de busqueda que se muestra y tambien
                        //un observer para que se actualice la lista conforme escribamos
                    }
                }
                viewModel.search(newText)
                return false
            }
        })

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