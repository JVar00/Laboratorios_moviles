package cr.ac.una.spotify_caleb_jeff


import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cr.ac.una.spotify_caleb_jeff.adapter.HistoryAdapter
import cr.ac.una.spotify_caleb_jeff.adapter.SearchAdapter
import cr.ac.una.spotify_caleb_jeff.databinding.FragmentSearchBinding
import cr.ac.una.spotify_caleb_jeff.entity.History
import cr.ac.una.spotify_caleb_jeff.entity.Track
import cr.ac.una.spotify_caleb_jeff.viewmodel.SpotifySearchViewmodel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
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
    private lateinit var history: List<History>

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
        history = mutableListOf<History>()

        val viewModel = ViewModelProvider(this).get(SpotifySearchViewmodel::class.java)
        val searchField = view.findViewById<SearchView>(R.id.search_bar)

        val listView = view.findViewById<RecyclerView>(R.id.list_view)
        val historyView = view.findViewById<RecyclerView>(R.id.history_view)

        val adapter = SearchAdapter(tracks as ArrayList<Track>)
        val historyAdapter = HistoryAdapter(history as ArrayList<History>) { selectedItem ->
            searchField.setQuery(selectedItem, false)
        }

        listView.adapter = adapter
        listView.layoutManager = LinearLayoutManager(requireContext())

        historyView.adapter = historyAdapter
        historyView.layoutManager = LinearLayoutManager(requireContext())

        viewModel.tracks.observe(viewLifecycleOwner) { elementos ->
            adapter.updateData(elementos as ArrayList<Track>)
            tracks = elementos
        }

        viewModel.history.observe(viewLifecycleOwner) { elementos ->
            //order the elemenst from the most recent to the oldest
            elementos.sortedByDescending { it.id }
            historyAdapter.updateData(elementos as ArrayList<History>)
            history = elementos
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
        }

        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {

            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                val position = viewHolder.adapterPosition
                val entity = (history as MutableList<History>).get(position)
                searchField.setQuery(entity.song_name, false)

                return false
            }


            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val entity = (history as MutableList<History>).get(position)

                lifecycleScope.launch {
                    withContext(Dispatchers.IO) {
                        viewModel.deleteHistoryItem(requireContext(), entity)
                    }
                }

                // Elimina el elemento cuando se detecta el deslizamiento hacia la derecha
                (history as MutableList<History>).removeAt(position)
                historyAdapter.updateData(history as ArrayList<History>)
            }

            // Sobrescribe el método para dibujar la etiqueta al deslizar
            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                if (viewHolder is HistoryAdapter.ViewHolder) {
                    super.onChildDraw(
                        c,
                        recyclerView,
                        viewHolder,
                        dX,
                        dY,
                        actionState,
                        isCurrentlyActive
                    )
                    if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                        val itemView = viewHolder.itemView
                        val paint = Paint()
                        paint.color = Color.RED
                        val deleteIcon = ContextCompat.getDrawable(
                            requireContext(),
                            android.R.drawable.ic_menu_delete
                        )
                        val iconMargin = (itemView.height - deleteIcon!!.intrinsicHeight) / 2
                        val iconTop =
                            itemView.top + (itemView.height - deleteIcon.intrinsicHeight) / 2
                        val iconBottom = iconTop + deleteIcon.intrinsicHeight

                        // Dibuja el fondo rojo
                        c.drawRect(
                            itemView.left.toFloat(),
                            itemView.top.toFloat(),
                            itemView.right.toFloat(),
                            itemView.bottom.toFloat(),
                            paint
                        )

                        // Calcula las posiciones del icono de eliminar
                        val iconLeft = itemView.right - iconMargin - deleteIcon.intrinsicWidth
                        val iconRight = itemView.right - iconMargin
                        deleteIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom)

                        // Dibuja el icono de eliminar
                        deleteIcon.draw(c)
                    }
                }
            }
        })

        // Adjunta el ItemTouchHelper al RecyclerView
        itemTouchHelper.attachToRecyclerView(historyView)

        //searchField on touch listener
        searchField.setOnQueryTextFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                println("Hi the history is visible")
                lifecycleScope.launch {
                    withContext(Dispatchers.IO) {
                        viewModel.getHistory(requireContext(), "")
                    }
                }
                historyView.visibility = View.VISIBLE
            } else {
                println("Hi the history is gone")
                val query = searchField.query.toString()
                if (query != "") {
                    lifecycleScope.launch {
                        withContext(Dispatchers.IO) {
                            viewModel.addHistory(requireContext(), query)
                        }
                    }
                }
                historyView.visibility = View.GONE
            }
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
                    }
                }

                if (newText != "") {
                    viewModel.search(newText)
                } else {
                    //clean the list
                    adapter.updateData(arrayListOf<Track>())
                }

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