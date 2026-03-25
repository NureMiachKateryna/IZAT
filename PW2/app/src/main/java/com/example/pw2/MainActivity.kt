package com.example.pw2

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import org.apache.poi.ss.usermodel.*
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.net.InetSocketAddress
import java.net.Socket

class MainActivity : AppCompatActivity() {

    private lateinit var tvStatus: TextView
    private lateinit var etUrls: EditText
    private lateinit var etCount: EditText
    private lateinit var etInterval: EditText
    private lateinit var rgFormat: RadioGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvStatus = findViewById(R.id.tvStatus)
        etUrls = findViewById(R.id.etUrls)
        etCount = findViewById(R.id.etCount)
        etInterval = findViewById(R.id.etInterval)
        rgFormat = findViewById(R.id.rgFormat)

        findViewById<Button>(R.id.btnCreateExcel).setOnClickListener { createInputExcel() }
        findViewById<Button>(R.id.btnProcessExcel).setOnClickListener { processExcelWithMethodology() }
        findViewById<Button>(R.id.btnStartPing).setOnClickListener { runAutomatedPingWithStats() }
    }

    private fun createInputExcel() {
        try {
            val workbook = XSSFWorkbook()
            val sheet = workbook.createSheet("Data")
            val rows = listOf(
                listOf("10", "20", "30"),
                listOf("Hello", "World", "Test"),
                listOf("ID_100", "v1.2", "User5")
            )
            rows.forEachIndexed { i, data ->
                val row = sheet.createRow(i)
                data.forEachIndexed { j, value -> row.createCell(j).setCellValue(value) }
            }
            val file = File(filesDir, "input.xlsx")
            FileOutputStream(file).use { workbook.write(it) }
            tvStatus.text = "Excel: Створено 'input.xlsx'. Тепер натисніть 'Обробити'."
        } catch (e: Exception) { tvStatus.text = "Excel Error: ${e.message}" }
    }

    private fun processExcelWithMethodology() {
        try {
            val file = File(filesDir, "input.xlsx")
            if (!file.exists()) { tvStatus.text = "Спершу створіть файл!"; return }
            val workbook = XSSFWorkbook(FileInputStream(file))
            val sheet = workbook.getSheetAt(0)

            val green = workbook.createCellStyle().apply { setFont(workbook.createFont().apply { color = IndexedColors.GREEN.getIndex() }) }
            val blue = workbook.createCellStyle().apply { setFont(workbook.createFont().apply { color = IndexedColors.BLUE.getIndex() }) }
            val yellow = workbook.createCellStyle().apply {
                fillForegroundColor = IndexedColors.YELLOW.getIndex()
                fillPattern = FillPatternType.SOLID_FOREGROUND
            }

            val hasLettersRegex = ".*[a-zA-Zа-яА-Я].*".toRegex()
            val isNumberRegex = "-?\\d+".toRegex()

            for (row in sheet) {
                var rowHasLetters = false
                for (cell in row) {
                    if (cell.toString().contains(hasLettersRegex)) {
                        rowHasLetters = true
                        break
                    }
                }
                for (cell in row) {
                    val text = cell.toString()
                    val hasLet = text.contains(hasLettersRegex)
                    val hasDig = text.any { it.isDigit() }

                    if (hasLet && hasDig) cell.cellStyle = yellow
                    else if (text.matches(isNumberRegex)) cell.cellStyle = green
                    else if (rowHasLetters) cell.cellStyle = blue
                }
            }
            val outFile = File(filesDir, "result_colored.xlsx")
            FileOutputStream(outFile).use { workbook.write(it) }
            workbook.close()
            tvStatus.text = "Excel: Успішно! Файл result_colored.xlsx збережено."
        } catch (e: Exception) { tvStatus.text = "Error: ${e.message}" }
    }

    private fun runAutomatedPingWithStats() {
        val urlsStr = etUrls.text.toString()
        val count = etCount.text.toString().toIntOrNull() ?: 3
        val interval = etInterval.text.toString().toLongOrNull() ?: 2

        if (urlsStr.isEmpty()) return
        val urls = urlsStr.split(",").map { it.trim() }.filter { it.isNotEmpty() }
        tvStatus.text = "Ping: Розпочато автоматичний тест..."

        Thread {
            val hostAverages = mutableMapOf<String, Double>()
            var totalSumAllPings = 0.0
            var totalSuccessfulPings = 0

            urls.forEachIndexed { index, host ->
                runOnUiThread { tvStatus.text = "Обробка ($index/${urls.size}): $host" }

                val times = mutableListOf<Double>()
                for (i in 1..count) {
                    val latency = getRealLatency(host)
                    if (latency > 0) {
                        times.add(latency)
                        totalSumAllPings += latency
                        totalSuccessfulPings++
                    }
                }

                val avgForThisHost = if (times.isNotEmpty()) times.average() else -1.0
                hostAverages[host] = avgForThisHost

                if (index < urls.size - 1) Thread.sleep(interval * 1000)
            }

            val globalAverage = if (totalSuccessfulPings > 0) totalSumAllPings / totalSuccessfulPings else 0.0

            val format = when (rgFormat.checkedRadioButtonId) {
                R.id.rbXml -> "XML"; R.id.rbHtml -> "HTML"; else -> "TXT"
            }
            val report = generatePingReport(hostAverages, globalAverage, format)

            runOnUiThread {
                tvStatus.text = "Успішно! Заг. середнє: ${"%.2f".format(globalAverage)} ms\nФайл: ${report.name}"
            }
        }.start()
    }

    private fun getRealLatency(host: String): Double {
        return try {
            val startTime = System.currentTimeMillis()
            val socket = Socket()
            socket.connect(InetSocketAddress(host, 80), 2000)
            val endTime = System.currentTimeMillis()
            socket.close()
            (endTime - startTime).toDouble()
        } catch (e: Exception) {
            -1.0
        }
    }

    private fun generatePingReport(data: Map<String, Double>, globalAvg: Double, format: String): File {
        val file = File(filesDir, "ping_report.${format.lowercase()}")
        val sb = StringBuilder()
        when (format) {
            "HTML" -> {
                sb.append("<html><body><h2>Звіт Ping</h2><table border='1'><tr><th>Хост</th><th>Сер. час (ms)</th></tr>")
                data.forEach { sb.append("<tr><td>${it.key}</td><td>${if(it.value > 0) "%.2f".format(it.value) else "Error"}</td></tr>") }
                sb.append("</table><br><b>Загальне середнє: ${"%.2f".format(globalAvg)} ms</b></body></html>")
            }
            "XML" -> {
                sb.append("<report><results>")
                data.forEach { sb.append("<item><host>${it.key}</host><avg>${it.value}</avg></item>") }
                sb.append("</results><global_avg>$globalAvg</global_avg></report>")
            }
            else -> {
                sb.append("ПРОТОКОЛ ТЕСТУВАННЯ МЕРЕЖІ\n")
                data.forEach { sb.append("Хост: ${it.key} | Сер. час: ${"%.2f".format(it.value)} ms\n") }
                sb.append("---------------------------------\n")
                sb.append("Загальне середнє по всіх запитах: ${"%.2f".format(globalAvg)} ms")
            }
        }
        file.writeText(sb.toString())
        return file
    }
}