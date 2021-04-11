package com.io.calculator

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun parser_isCorrect() {
        val parser: Parser = Parser(arrayOf("\\+", "-", "\\*", "/", "%", "@"))
        val pair = parser.parse("1+2*3+@4-7")
        assertEquals(2.0, parser.convertToList(pair).evaluateExpression())
    }
}