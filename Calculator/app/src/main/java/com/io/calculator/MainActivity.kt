package com.io.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import java.text.DecimalFormat

/*  Lizaveta Hurskaya, gr. wtorek 15:30
*Zrealizowane:
*   Stworzony layout (zgodnie z podpunktami a, b, c, d)
*   Logika związana z obsługą obliczeń (własna implementacja)
*   Obsługa kropki i wielokrotnego użycia przycisków z symbolami operacji (pozostałe ?)
*   Kalkulator działa po zmianie symboli, które reprezentują operacje matematyczne
*   Obsługa zbyt długich liczb
*   Wyświetlanie cząstkowych wyników po wprowadzeniunowych symboli
*   Obsługa pierwiastków
*   Poprawna obsługa kolejności działań
* Braki:
*    Aktualizowanie zawartości wyświetlacza za pomocą settera
*    Obsługa procentów
*/

class MainActivity : AppCompatActivity() {
    private lateinit var calculator: Calculator
    private lateinit var digits: Array<Button>
    private lateinit var operations: Array<Button>

    private lateinit var colon: Button
    private lateinit var clear: Button
    private lateinit var equals: Button

    private lateinit var display: TextView
    private lateinit var resultDisplay: TextView

    internal var currentResult: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initButtons()
        initDisplays()
        initCalculator()

    }

    private fun initButtons() {
        val digitsIDs = arrayOf(
            R.id.b0,
            R.id.b1,
            R.id.b2,
            R.id.b3,
            R.id.b4,
            R.id.b5,
            R.id.b6,
            R.id.b7,
            R.id.b8,
            R.id.b9
        )

        val operationsIDs = arrayOf(
            R.id.plus,
            R.id.minus,
            R.id.multiply,
            R.id.divide,
            R.id.percent,
            R.id.sqrt
        )

        colon = findViewById(R.id.dot)
        clear = findViewById(R.id.backspace)
        equals = findViewById(R.id.equals)

        digits = (digitsIDs.map{ id -> findViewById(id) as Button}).toTypedArray()
        operations = (operationsIDs.map{ id -> findViewById(id) as Button}).toTypedArray()

        digits.forEach { digitButton -> digitButton.setOnClickListener { digitButtonListener(digitButton.text) } }
        operations.forEach { operationButton -> operationButton.setOnClickListener{ operationButtonListener(operationButton.text) } }

        colon.setOnClickListener { view ->
            if (display.text == "0"){
                display.text = "0."
            }
            else if (!display.text[display.text.lastIndex].isDigit()){
                val text = display.text
                display.text = "${text}0."
            }
            else{
                addToDisplay(colon.text[0])
            }
            colon.isEnabled = false
        }

        clear.setOnClickListener { view ->
            if (display.text.length <= 1){
                calculator.removeSymbol()
                display.text = "0"
            }
            else{
                calculator.removeSymbol()
                display.text = display.text.subSequence(0, display.text.lastIndex)
            }
            if (display.text[display.text.lastIndex].isDigit()) {
                operations.forEach { operationButton -> operationButton.isEnabled = true }
            }
            updateDisplays()
        }

        equals.setOnClickListener { view ->
            display.setTextSize(42F)
            resultDisplay.setTextSize(56F)
        }
    }

    private fun operationButtonListener(label: CharSequence?) {
        operations.forEach { operationButton -> operationButton.isEnabled = false }
        if (label != getString(R.string.sqrt)){
            operations[5].isEnabled = true
        }
        addToDisplay(label!![0])
        colon.isEnabled = true
        display.setTextSize(56F)
        resultDisplay.setTextSize(42F)
    }

    private fun digitButtonListener(label: CharSequence?) {
        addToDisplay(label!![0])
        operations.forEach { operationButton -> operationButton.isEnabled = true }
        display.setTextSize(56F)
        resultDisplay.setTextSize(42F)
    }

    private fun initDisplays() {
        display = findViewById(R.id.display)
        resultDisplay = findViewById(R.id.resultDisplay)
        display.text = "0"
        resultDisplay.text = "0"
    }

    private fun addToDisplay(c: Char) {
        val text = display.text
        if (text == "0"){
            display.text =  "$c"
        }
        else{
            display.text =  "$text$c"
        }
        calculator.addSymbol(c)
        updateDisplays()
    }

    private fun initCalculator(){
        //OPERATORS ORDER + - * / % sqrt
        var operationLabels: Array<String> = operations.map { operationButton -> operationButton.text.toString() }.toTypedArray()

        val needsBackslash = "<([{\\^=\$!|]})?*+.>".toCharArray()

        operationLabels = operationLabels.map { if (needsBackslash.contains(it[0])) "\\${it[0]}" else it}.toTypedArray()

        calculator = Calculator(operationLabels)
    }

    private fun updateDisplays(){
        currentResult = calculator.evaluate()!!
        val df: DecimalFormat = DecimalFormat("######.######E0")
        resultDisplay.text = "= ${df.format(currentResult)}"
    }
}