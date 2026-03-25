package com.example.calculator

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class AndroidLogger : ILogger {
    override fun log(message: String) {
        Log.d("CalculatorApp", message)
    }
}

class MainActivity : AppCompatActivity() {
    private val calculator = Calculator(AndroidLogger())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val inputA = findViewById<EditText>(R.id.inputA)
        val inputB = findViewById<EditText>(R.id.inputB)
        val tvResult = findViewById<TextView>(R.id.tvResult)

        val prefs = getSharedPreferences("calc_prefs", Context.MODE_PRIVATE)

        tvResult.text = prefs.getString("history", "Історія порожня")

        fun updateHistory(newEntry: String) {
            val currentHistory = prefs.getString("history", "")
            val updatedHistory = if (currentHistory.isNullOrEmpty() || currentHistory == "Історія порожня") {
                newEntry
            } else {
                "$currentHistory\n$newEntry"
            }

            prefs.edit().putString("history", updatedHistory).apply()
            tvResult.text = updatedHistory
        }

        findViewById<Button>(R.id.btnAdd).setOnClickListener {
            val a = inputA.text.toString().toIntOrNull() ?: 0
            val b = inputB.text.toString().toIntOrNull() ?: 0
            val res = calculator.add(a, b)
            updateHistory("$a + $b = $res")
        }

        findViewById<Button>(R.id.btnMul).setOnClickListener {
            val a = inputA.text.toString().toIntOrNull() ?: 0
            val b = inputB.text.toString().toIntOrNull() ?: 0
            val res = calculator.multiply(a, b)
            updateHistory("$a * $b = $res")
        }

        findViewById<Button>(R.id.btnDiv).setOnClickListener {
            val a = inputA.text.toString().toIntOrNull() ?: 0
            val b = inputB.text.toString().toIntOrNull() ?: 0
            try {
                val res = calculator.divide(a, b)
                updateHistory("$a / $b = $res")
            } catch (e: Exception) {
                updateHistory("$a / $b = Error")
            }
        }
    }
}