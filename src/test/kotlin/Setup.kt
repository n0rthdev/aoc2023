import org.junit.jupiter.api.Test
import java.io.File
import java.net.HttpURLConnection
import java.net.URI


class Setup {

    val DAY_TO_SETUP = 13

    @Test
    fun startDayPart1() {
        startDayPart1(DAY_TO_SETUP)
    }

    @Test
    fun startDayPart2() {
        startDayPart2(DAY_TO_SETUP)
    }

    fun startDayPart1(dayNumber: Int) {
        val stringDayNumber = "%02d".format(dayNumber)

        val dir = File("puzzles/" + stringDayNumber)
        if (!dir.exists()) {
            dir.mkdir()
        }

        createIfNotExists(dir.absolutePath + "/example.in1")
        createIfNotExists(dir.absolutePath + "/example.check1")

        copyCodeFromSampleToPart1(stringDayNumber)

        downloadMyInput(stringDayNumber, AOC_YEAR)
    }

    fun startDayPart2(dayNumber: Int) {
        val stringDayNumber = "%02d".format(dayNumber)
        val dir = File("puzzles/" + stringDayNumber)

        createIfNotExists(dir.absolutePath + "/example.in2")
        createIfNotExists(dir.absolutePath + "/example.check2")
        copyCodeFromPart1ToPart2(stringDayNumber)
    }

    fun copyCodeFromSampleToPart1(stringDayNumber: String): File {
        val srcClass = "Day00Part1"
        val targetClass = "Day${stringDayNumber}Part1"
        return copyClassSource(srcClass, targetClass)
    }

    fun copyCodeFromPart1ToPart2(stringDayNumber: String): File {
        val srcClass = "Day${stringDayNumber}Part1"
        val targetClass = "Day${stringDayNumber}Part2"
        return copyClassSource(srcClass, targetClass)
    }

    private fun copyClassSource(srcClass: String, targetClass: String): File {
        val srcFileName = "src/main/kotlin/$srcClass.kt"
        val targetFileName = "src/main/kotlin/$targetClass.kt"

        val targetFile = File(targetFileName)
        if (!targetFile.exists()) {
            val lines = File(srcFileName).readText().replace(srcClass, targetClass)
            targetFile.writeText(lines)
        }
        return targetFile
    }

    fun createIfNotExists(filepath: String): File {
        val file = File(filepath)
        if (!file.exists()) {
            file.createNewFile();
        }
        return file
    }

    fun downloadMyInput(day: String, aocYear: String) {
        downloadInput(day, aocYear, "puzzles/" + day + "/my.in", "/input")
    }

    fun downloadInput(
        day: String,
        aocYear: String,
        fileName: String,
        subPath: String,
        extractor: (String) -> String = { it }
    ) {
        val inputFile = File(fileName)
        if (!inputFile.exists()) {
            val sessionIdFile = File("sessionid")
            if (sessionIdFile.exists()) {
                val url = "https://adventofcode.com/$aocYear/day/${day.toInt()}$subPath"
                try {
                    val sessionId = sessionIdFile.readLines()[0].trim()
                    val con: HttpURLConnection = URI(url).toURL().openConnection() as HttpURLConnection
                    con.setRequestMethod("GET")
                    con.addRequestProperty(
                        "Cookie",
                        "session=$sessionId"
                    )
                    val lines = con.inputStream.bufferedReader().lines().toList().joinToString("\n")
                    val exampleInput = extractor(lines)
                    inputFile.writeText(exampleInput)
                } catch (ex: Exception) {
                    throw RuntimeException("Could not download $url file for ${day.toInt()} of $aocYear", ex)
                }
            } else {
                System.err.println("Skipping input download, because no sessionid file is found")
            }
        }
    }
}

