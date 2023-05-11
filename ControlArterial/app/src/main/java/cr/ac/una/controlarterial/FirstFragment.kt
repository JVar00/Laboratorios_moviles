package cr.ac.una.controlarterial

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.navigation.fragment.findNavController
import cr.ac.una.controlarterial.DAO.TomaArterialDAO
import cr.ac.una.controlarterial.adapter.ListAdapter
import cr.ac.una.controlarterial.databinding.FragmentFirstBinding
import cr.ac.una.controlarterial.entity.TomaArterial
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)

        return binding.root

    }

    //



    //

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ///

        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor("8PsQ4uLLRz5t816BXG_APX322Z5k1vTPZQvm1Yi6MH-0bT5e5w"))
            // .addInterceptor(interceptor)
                
            .build()

        //val gson = GsonBuilder().setPrettyPrinting().create()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://crudapi.co.uk/api/v1/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(TomaArterialDAO::class.java)

        GlobalScope.launch(Dispatchers.IO) {
            val item = TomaArterial(_uuid = null, distolica = 100, sistolica = 200, ritmo = 300)
            var items = ArrayList<TomaArterial>()
            items.add(item)
            val createdItem = apiService.createItem(items)
            val loadItems = apiService.getItems()

            withContext(Dispatchers.Main) {

                // Procesar la respuesta del API
                val listView = view.findViewById<ListView>(R.id.listview_first)
                val adapter = context?.let{ loadItems.items?.let { it1 -> ListAdapter(it, it1) } }
                listView.adapter = adapter

            }

        }

        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}