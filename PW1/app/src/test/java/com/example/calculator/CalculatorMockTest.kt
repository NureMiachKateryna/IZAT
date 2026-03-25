package com.example.calculator

import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito.*

class CalculatorMockTest {

    @Test
    fun testCalculatorWithMockLogger() {
        val mockLogger = mock(ILogger::class.java)
        val calculator = Calculator(mockLogger)
        val result = calculator.multiply(4, 5)

        assertEquals(20, result)
        verify(mockLogger, times(1)).log(anyString())
    }
}