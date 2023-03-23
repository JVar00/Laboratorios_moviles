package com.calculadora

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import kotlin.system.exitProcess
import org.mariuszgromada.math.mxparser.Expression

class Avanzada : AppCompatActivity() {
    //Numbers
    lateinit var num0 : Button
    lateinit var num1 : Button
    lateinit var num2 : Button
    lateinit var num3 : Button
    lateinit var num4 : Button
    lateinit var num5 : Button
    lateinit var num6 : Button
    lateinit var num7 : Button
    lateinit var num8 : Button
    lateinit var num9 : Button
    lateinit var dec : Button

    //Functionality Buttons
    lateinit var addition : Button
    lateinit var substract : Button
    lateinit var multiply : Button
    lateinit var divide : Button
    lateinit var root : Button
    lateinit var exp : Button
    lateinit var sen : Button
    lateinit var cos : Button
    lateinit var tan : Button
    lateinit var p1 : Button
    lateinit var p2 : Button
    lateinit var clear : Button
    lateinit var submit : Button

    //Result
    lateinit var result : TextView

    //Global Values
    private var expression : String = ""
    private var number : Double = 0.0
    private var operation : Int = 0

    companion object{
        const val SUMA = 1
        const val RESTA = 2
        const val MULTIPLICACION = 3
        const val DIVISION = 4
        const val ROT = 5
        const val EXPONENTIAL = 6
        const val SEN = 7
        const val COS = 8
        const val TAN = 9
        const val P1 = 10
        const val P2 = 11
        const val NO_OPERATION = 0
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_avanzada)

        num0 = this.findViewById(R.id.button0)
        num1 = this.findViewById(R.id.button1)
        num2 = this.findViewById(R.id.button2)
        num3 = this.findViewById(R.id.button3)
        num4 = this.findViewById(R.id.button4)
        num5 = this.findViewById(R.id.button5)
        num6 = this.findViewById(R.id.button6)
        num7 = this.findViewById(R.id.button7)
        num8 = this.findViewById(R.id.button8)
        num9 = this.findViewById(R.id.button9)
        dec = this.findViewById(R.id.buttonDec)

        //FuncionalityButtons
        addition = this.findViewById(R.id.buttonSum)
        substract = this.findViewById(R.id.buttonRest)
        divide = this.findViewById(R.id.buttonDiv)
        multiply = this.findViewById(R.id.buttonX)
        root = this.findViewById(R.id.buttonR)
        exp = this.findViewById(R.id.buttonExp)
        sen = this.findViewById(R.id.buttonSen)
        cos = this.findViewById(R.id.buttonCos)
        tan = this.findViewById(R.id.buttonTan)
        p1 = this.findViewById(R.id.buttonP1)
        p2 = this.findViewById(R.id.buttonP2)
        clear = this.findViewById(R.id.buttonAC)
        submit = this.findViewById(R.id.buttonEq)

        //Result
        result = this.findViewById(R.id.textView1)

        //Listeners
        num0.setOnClickListener() { onNumberSelected("0") }
        num1.setOnClickListener() { onNumberSelected("1") }
        num2.setOnClickListener() { onNumberSelected("2") }
        num3.setOnClickListener() { onNumberSelected("3") }
        num4.setOnClickListener() { onNumberSelected("4") }
        num5.setOnClickListener() { onNumberSelected("5") }
        num6.setOnClickListener() { onNumberSelected("6") }
        num7.setOnClickListener() { onNumberSelected("7") }
        num8.setOnClickListener() { onNumberSelected("8") }
        num9.setOnClickListener() { onNumberSelected("9") }
        dec.setOnClickListener() { onNumberSelected(".") }

        addition.setOnClickListener() { onOperationSelected(SUMA) }
        substract.setOnClickListener() { onOperationSelected(RESTA) }
        multiply.setOnClickListener() { onOperationSelected(MULTIPLICACION) }
        divide.setOnClickListener() { onOperationSelected(DIVISION) }
        root.setOnClickListener() { onAdvancedSelected(ROT) }
        exp.setOnClickListener() { onAdvancedSelected(EXPONENTIAL) }
        sen.setOnClickListener() { onAdvancedSelected(SEN) }
        cos.setOnClickListener() { onAdvancedSelected(COS) }
        tan.setOnClickListener() { onAdvancedSelected(TAN) }
        p1.setOnClickListener() { onParenthesisSelected(P1) }
        p2.setOnClickListener() { onParenthesisSelected(P2) }

        clear.setOnClickListener() {
            expression = ""
            number = 0.0
            operation = NO_OPERATION
            result.text = "0"
        }

        submit.setOnClickListener() {

            if (operation != P2){
                expression = "$expression$number"
            }

            result.text = Expression(expression).calculate().toString().removeSuffix(".0")
            number = result.text.toString().toDouble()
            expression = ""

        }
    }

    private fun onNumberSelected(new_number: String){

        when (result.text){
            "0" -> result.text = new_number
            else -> result.text = "${result.text}$new_number"
        }

        number = result.text.toString().toDouble()

    }

    private fun onOperationSelected(operation: Int){

        this.operation = operation

        if (number != 0.0){
            expression = "$expression$number${when (operation) {
                SUMA -> "+"
                RESTA -> "-"
                MULTIPLICACION -> "*"
                DIVISION -> "/"
                else -> ""
            }}"
        }else{
            expression = "$expression${when (operation) {
                SUMA -> "+"
                RESTA -> "-"
                MULTIPLICACION -> "*"
                DIVISION -> "/"
                else -> ""
            }}"
        }

        Log.d("Expression", expression)

        number = 0.0
        result.text = "0"

    }

    private fun onParenthesisSelected(operation: Int){

        this.operation = operation

        if (number != 0.0){
            expression = when (operation) {
                P1 -> "($number$expression"
                P2 -> "$expression$number)"
                else -> ""
            }
        }else{
            expression = when (operation) {
                P1 -> "($expression"
                P2 -> "$expression)"
                else -> ""
            }
        }

        number = 0.0
        result.text = "0"

        Log.d("Expression", expression)

    }

    private fun onAdvancedSelected(operation: Int){

        this.operation = operation

        expression = "${when (operation) {
            ROT -> "sqrt("
            SEN -> "sin("
            EXPONENTIAL -> "exp("
            COS -> "cos("
            TAN -> "tan("
            else -> ""
        }}$expression$number)"


        Log.d("Expression", expression)

        result.text = Expression(expression).calculate().toString()
        number = result.text.toString().toDouble()
        expression = ""

    }


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
            R.id.avanzada -> {
                var intent = Intent(this, Avanzada::class.java)
                startActivity(intent)
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}