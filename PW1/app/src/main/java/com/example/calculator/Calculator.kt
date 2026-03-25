package com.example.calculator

interface ILogger {
    fun log(message: String)
}

class Calculator(private val logger: ILogger) {

    fun add(a: Int, b: Int): Int {
        val res = a + b
        logger.log("Додавання: $a + $b = $res")
        return res
    }

    fun multiply(a: Int, b: Int): Int {
        val res = a * b
        logger.log("Множення: $a * $b = $res")
        return res
    }

    fun divide(a: Int, b: Int): Int {
        if (b == 0) throw IllegalArgumentException("Error")
        val res = a / b
        logger.log("Ділення: $a / $b = $res")
        return res
    }
}