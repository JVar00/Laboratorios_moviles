package com.calculadora

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {

    //Numbers
    lateinit var number0 : Button
    lateinit var number1 : Button
    lateinit var number2 : Button
    lateinit var number3 : Button
    lateinit var number4 : Button
    lateinit var number5 : Button
    lateinit var number6 : Button
    lateinit var number7 : Button
    lateinit var number8 : Button
    lateinit var number9 : Button
    lateinit var decimal : Button

    //Functionality Buttons
    lateinit var addition : Button
    lateinit var substract : Button
    lateinit var multiply : Button
    lateinit var divide : Button
    lateinit var clear : Button

    //Result
    lateinit var result : TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Numbers
        number0 = this.findViewById(R.id.Number0)
        number1 = this.findViewById(R.id.Number1)
        number2 = this.findViewById(R.id.Number2)
        number3 = this.findViewById(R.id.Number3)
        number4 = this.findViewById(R.id.Number4)
        number5 = this.findViewById(R.id.Number5)
        number6 = this.findViewById(R.id.Number6)
        number7 = this.findViewById(R.id.Number7)
        number8 = this.findViewById(R.id.Number8)
        number9 = this.findViewById(R.id.Number9)
        decimal = this.findViewById(R.id.Decimal)

        //FuncionalityButtons
        addition = this.findViewById(R.id.AdditionButton)
        substract = this.findViewById(R.id.SubstractButton)
        divide = this.findViewById(R.id.DivideButton)
        multiply = this.findViewById(R.id.MultiplyButton)
        clear = this.findViewById(R.id.ClearButton)

        //Result
        result = this.findViewById(R.id.Result)

        //Listeners

         /*
        text = this.findViewById(R.id.textView1)
        boton = this.findViewById(R.id.button1) //instancia del boton

        //accion del boton
        boton.setOnClickListener{
            text.setText("hola")
        }*/
    }

    //se sobreescribe el metodo para poder utilizar el menu en la pantalla
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //return super.onCreateOptionsMenu(menu)
        val inflater : MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    //detecta cual item fue el seleccionado
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.salir -> {
                //se hace la operacion necesaria
                exitProcess(0)
                true
            }
            R.id.acercade -> {
                //se hace la operacion necesaria
                var intent = Intent(this, Acercade::class.java)
                //le envia this (la misma instancia) y la clase completa
                startActivity(intent)
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}