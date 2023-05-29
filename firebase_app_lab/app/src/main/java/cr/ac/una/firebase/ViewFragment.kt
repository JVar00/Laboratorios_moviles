package cr.ac.una.firebase

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cr.ac.una.firebase.R
import cr.ac.una.firebase.adapter.ListAdapter
import cr.ac.una.firebase.entity.Persona
import cr.ac.una.firebase.modelView.PersonaModelView
import cr.ac.una.firebase.databinding.FragmentListBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ViewFragment : Fragment() {

    private var _binding: FragmentListBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var personas: List<Persona>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        personas = mutableListOf<Persona>()

        val listView = view.findViewById<RecyclerView>(R.id.list_view)
        val adapter = ListAdapter(personas as ArrayList<Persona>)
        listView.adapter = adapter
        listView.layoutManager = LinearLayoutManager(requireContext())

        val viewModel = ViewModelProvider(this).get(PersonaModelView::class.java)

        viewModel.personas.observe(viewLifecycleOwner) { elementos ->
            adapter.updateData(elementos as ArrayList<Persona>)
            personas = elementos
        }

        viewModel.getItems()!!
        Toast.makeText(context, "Personas cargadas", Toast.LENGTH_SHORT).show()

        /*
        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }*/

        val itemTouchHelper =
            ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.adapterPosition
                    if (position != 0) {

                        val entity = (personas as MutableList<Persona>).get(position)
                        println(entity._uuid)
                        viewModel.deleteItem(entity._uuid)

                        // Elimina el elemento cuando se detecta el deslizamiento hacia la derecha
                        (personas as MutableList<Persona>).removeAt(position)
                        adapter.updateData(personas as ArrayList<Persona>)
                    }
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
                    if (viewHolder is ListAdapter.ViewHolder) {
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
        itemTouchHelper.attachToRecyclerView(listView)

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
