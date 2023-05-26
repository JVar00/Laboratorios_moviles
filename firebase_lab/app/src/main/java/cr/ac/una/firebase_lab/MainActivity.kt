package cr.ac.una.firebase_lab

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {

    private lateinit var personasRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializar Firebase
        FirebaseApp.initializeApp(this)

        // Obtener referencia a la base de datos "personas"
        val database = FirebaseDatabase.getInstance()
        personasRef = database.getReference("persona")

        val buttonSend = findViewById<Button>(R.id.button)
        val textName = findViewById<EditText>(R.id.name)
        val textLast = findViewById<EditText>(R.id.lastName)
        val textAge = findViewById<EditText>(R.id.ageText)

        buttonSend.setOnClickListener{

            var name = textName.text.toString()
            var last = textLast.text.toString()
            var age = textAge.text.toString().toInt()

            // Agregar una persona a la base de datos
            val persona = Persona(name, last, age)
            val personaId = personasRef.push().key
            personasRef.child(personaId!!).setValue(persona)

            textName.setText("")
            textLast.setText("")
            textAge.setText("")

        }


    }
}