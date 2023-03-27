package com.una.pokedex

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ListFragment : Fragment() {


    //en una activity en onCreate ya tenemos creado el XML mientras que en un fragment
    // el XMl se crea hasta este metodo, que se ejecuta un poco despues en la lista del tiempo

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //con data binding:
        //val binding = FragmentListBinding.inflate(inflater)
        //val recycler = binding.pokemon_recycler

        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_list, container, false)
        val recycler = view.findViewById<RecyclerView>(R.id.pokemon_recycler)

        //fragment no tiene context tenemos que tomarlo del activity
        recycler.layoutManager = LinearLayoutManager(requireActivity())

        val adapter = PokemonAdapter()
        recycler.adapter = adapter

        adapter.onItemClickListener = {
            Toast.makeText(requireActivity(), it.name, Toast.LENGTH_SHORT).show()
        }

        val pokemonList = mutableListOf(
            Pokemon(1, "Bulbasaur", 45, 49, 49, 45, Pokemon.Type.GRASS),
            Pokemon(2, "Ivysaur", 60, 62, 63, 60, Pokemon.Type.GRASS),
            Pokemon(3, "Venusaur", 80, 82, 83, 80, Pokemon.Type.GRASS),
            Pokemon(4, "Charmander", 39, 52, 43, 65, Pokemon.Type.FIRE),
            Pokemon(5, "Charmeleon", 58, 64, 58, 80, Pokemon.Type.FIRE),
            Pokemon(6, "Charizard", 78, 84, 78, 100, Pokemon.Type.FIRE),
            Pokemon(7, "Squirtle", 44, 48, 65, 43, Pokemon.Type.WATER),
            Pokemon(8, "Wartortle", 59, 63, 80, 58, Pokemon.Type.WATER),
            Pokemon(9, "Blastoise", 79, 83, 100, 78, Pokemon.Type.WATER)
        )

        adapter.submitList(pokemonList)

        return view
    }

}

//Ciclo de vida de un fragment:
//1. onCreate
//2. onCreateView
//3. onViewCreated
//4. onStart
//5. onResume
//6. onPause
//7. onStop
//8. onDestroyView
//9. onDestroy
//10. onDetach
