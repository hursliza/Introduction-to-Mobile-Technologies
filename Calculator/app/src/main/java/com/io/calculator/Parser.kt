package com.io.calculator

import java.util.regex.Pattern
import kotlin.math.sqrt

class Parser(var operatorsInit: Array<String>) {
    class ExpressionNode(val op: Char, var leftNumber: Double?, var rightNumber: Double?) {
        var prevNode: ExpressionNode? = null
        var nextNode: ExpressionNode? = null

        fun getOperatorPriority(): Int?{
            when (this.op){
                '*' -> return 1
                '/' -> return 1
                '%' -> return 0
                'V' -> return 0
                '+' -> return 2
                '-' -> return 2
            }
            return null
        }

        fun evaluateOperator() :Double{
            var result: Double = Double.NaN
            when (this.op){
                '*' -> result = leftNumber!! * rightNumber!!
                '/' -> result = leftNumber!! / rightNumber!!
                '%' -> result = rightNumber!!/ 100
                'V' -> result = sqrt(rightNumber!!)
                '+' -> result = leftNumber!! + rightNumber!!
                '-' -> result = leftNumber!! - rightNumber!!
            }
            return result
        }
    }

    class ExpressionList(){
        private var head: ExpressionNode = ExpressionNode('!', null, 0.0)
        private var tail: ExpressionNode = ExpressionNode('!', 0.0, null)

        init {
            tail.prevNode = head
            head.nextNode = tail
        }

        fun appendToList(node: ExpressionNode){
            node.prevNode = tail.prevNode
            tail.prevNode!!.nextNode = node
            node.nextNode = tail
            tail.prevNode = node
        }

        fun evaluateExpression(): Double? {
            try {
                var currentPriority = 0
                while (currentPriority < 3) {
                    var currentNode = head
                    while (currentNode != tail) {
                        if (currentNode.getOperatorPriority() == currentPriority) {
                            val currentResult = currentNode.evaluateOperator()
                            currentNode.prevNode!!.rightNumber = currentResult
                            currentNode.nextNode!!.leftNumber = currentResult
                            currentNode.prevNode!!.nextNode = currentNode.nextNode
                            currentNode.nextNode!!.prevNode = currentNode.prevNode
                        }
                        currentNode = currentNode.nextNode!!
                    }
                    currentPriority++
                }
                return head.rightNumber
            }
            catch (e: ArithmeticException){
                return Double.NaN
            }
        }
    }

    fun operatorsPattern(): Pattern{
        return Pattern.compile(operatorsInit.joinToString("|", "(", ")"))
    }

    fun parse(input: String): Pair<Array<Char>, Array<Double>>  {
        val operators: Array<Char>
        val numbers: Array<Double>

        val pattern = operatorsPattern()

        val delimiters = pattern.split(input)

        numbers = delimiters.filter { i -> i.isNotEmpty() }.map { i -> i.toDouble() }.toTypedArray()

        operators = input.filter { i -> Pattern.matches(pattern.toString(), i.toString()) }.map {mapOperator(it.toString())}.toTypedArray()

        return Pair(operators, numbers)
    }

    private fun mapOperator(op: String): Char {
        when (op) {
            operatorsInit[0][operatorsInit[0].lastIndex].toString() -> return '+'
            operatorsInit[1][operatorsInit[1].lastIndex].toString() -> return '-'
            operatorsInit[2][operatorsInit[2].lastIndex].toString()  -> return '*'
            operatorsInit[3][operatorsInit[3].lastIndex].toString()  -> return  '/'
            operatorsInit[4][operatorsInit[4].lastIndex].toString()  -> return '%'
            operatorsInit[5][operatorsInit[5].lastIndex].toString()  -> return 'V'
        }
        return '!'
    }

    fun convertToList(opsNums: Pair<Array<Char>, Array<Double>> ): ExpressionList {
        val operators = opsNums.first
        val numbers = opsNums.second
        val expressionList = ExpressionList()
        var numIndex = 0

        for (i in operators.indices){
            when(operators[i]){
                'V' -> expressionList.appendToList(ExpressionNode(operators[i], null, numbers[numIndex]))
                '%' -> {
                        expressionList.appendToList(ExpressionNode(operators[i], numbers[numIndex], null))
                        numIndex++
                    }
                else -> {
                        expressionList.appendToList(ExpressionNode(operators[i], numbers[numIndex], numbers[numIndex+1]))
                        numIndex++
                    }
            }
        }
        return expressionList
    }

}

