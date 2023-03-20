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
    lateinit var submit : Button

    //Result
    lateinit var result : TextView

    //Global Values
    private var firstNumber : Double = 0.0
    private var secondNumber : Double = 0.0
    private var operation : Int = 0
    companion object{
        const val SUMA = 1
        const val RESTA = 2
        const val MULTIPLICACION = 3
        const val DIVISION = 4
        const val NO_OPERATION = 0
    }


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
        submit = this.findViewById(R.id.SubmitButton)

        //Result
        result = this.findViewById(R.id.Result)

        //Listeners
        number0.setOnClickListener() { onNumberSelected("0") }
        number1.setOnClickListener() { onNumberSelected("1") }
        number2.setOnClickListener() { onNumberSelected("2") }
        number3.setOnClickListener() { onNumberSelected("3") }
        number4.setOnClickListener() { onNumberSelected("4") }
        number5.setOnClickListener() { onNumberSelected("5") }
        number6.setOnClickListener() { onNumberSelected("6") }
        number7.setOnClickListener() { onNumberSelected("7") }
        number8.setOnClickListener() { onNumberSelected("8") }
        number9.setOnClickListener() { onNumberSelected("9") }
        decimal.setOnClickListener() { onNumberSelected(".") }

        addition.setOnClickListener() { onOperationSelected(SUMA) }
        substract.setOnClickListener() { onOperationSelected(RESTA) }
        multiply.setOnClickListener() { onOperationSelected(MULTIPLICACION) }
        divide.setOnClickListener() { onOperationSelected(DIVISION) }

        clear.setOnClickListener() {
            firstNumber = 0.0
            secondNumber = 0.0
            operation = NO_OPERATION
            result.text = "0"
        }

        submit.setOnClickListener() {

            var finalResult = when (operation) {
                SUMA -> firstNumber + secondNumber
                RESTA -> firstNumber - secondNumber
                MULTIPLICACION -> firstNumber * secondNumber
                DIVISION -> firstNumber / secondNumber
                else -> 0
            }

            result.text = finalResult.toString()
        }


         /*
        text = this.findViewById(R.id.textView1)
        boton = this.findViewById(R.id.button1) //instancia del boton

        //accion del boton
        boton.setOnClickListener{
            text.setText("hola")
        }*/
    }

    private fun onNumberSelected(number: String){
        when (result.text){
            "0" -> result.text = "$number"
            else -> result.text = "${result.text}$number"
        }


        if (operation == NO_OPERATION){
            firstNumber = result.text.toString().toDouble()
        } else {
            secondNumber = result.text.toString().toDouble()
        }
    }

    private fun onOperationSelected(operation: Int){

        this.operation = operation
        firstNumber = result.text.toString().toDouble()
        result.text = "0"

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