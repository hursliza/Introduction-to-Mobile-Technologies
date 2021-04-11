package com.io.calculator

import android.util.Log

class Calculator(operators: Array<String>) : CalculatorInterface{
    var input = ""
    var parser: Parser = Parser(operatorsInit = operators)
    var firstOperationDone = false

    override fun addSymbol(c: Char) {
        input += c
    }

    override fun removeSymbol() {
        input = input.substring(0, input.length - 1)
    }

    override fun evaluate(): Double? {
        try{
            if (!parser.operatorsPattern().matcher(input[input.lastIndex].toString()).matches())
            {
                if (!firstOperationDone){
                    return input.toDouble()
                }
                return parser.convertToList(parser.parse(input)).evaluateExpression()
            }
            else {
                if (!firstOperationDone) {
                    firstOperationDone = true
                    return input.substring(0, input.length - 1).toDouble()
                }
                return parser.convertToList(parser.parse(input.substring(0, input.length - 1))).evaluateExpression()
            }
        }
        catch (e: Exception){
            Log.d("CALCULATOR EXCEPTION", e.message!!)
            return Double.NaN
        }
    }

}