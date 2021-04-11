package com.io.calculator

interface CalculatorInterface {
    fun addSymbol(c: Char)
    fun removeSymbol()
    fun evaluate(): Double?
}