package cr.ac.una.firebase.modelView

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import cr.ac.una.firebase.entity.Persona

class PersonaModelView : ViewModel() {
    private var _personas: MutableLiveData<List<Persona>> = MutableLiveData()
    var personas : LiveData<List<Persona>> = _personas
    private lateinit var personaRef : DatabaseReference

    fun deleteItem(uuid:String?){
        init()
        if (uuid != null) {
            //
        }
    }

    fun getItems(){
        init()

    }

    fun addItem(name : String, last : String, age : Int){
        init()
        var persona = Persona(null, name, last, age)
        val personaId = personaRef.push().key
        personaRef.child(personaId!!).setValue(persona)
    }

    fun init(){


        // Obtener referencia a la base de datos "personas"
        val database = FirebaseDatabase.getInstance()
        personaRef = database.getReference("persona")
    }

}