import org.apache.commons.io.FileUtils
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Timeout
import java.io.File
import java.util.*


@Timeout(100 * 3600)
class AOC {

    @Timeout(100 * 3600)
    @Test
    fun runExample() {
        runForPuzzle("example", true)
    }

    @Timeout(100 * 3600)
    @Test
    fun runMyInput() {
        runForPuzzle("my", false)
    }

    private fun runForPuzzle(filename: String, checkMandatory: Boolean) {
        runForFile(PUZZLE.day, PUZZLE.part, filename, checkMandatory)
    }

    private fun runForFile(day: String, part: String, filename: String, checkMandatory: Boolean) {
        val root = "puzzles"
        val input = File("$root/$day/$filename.in")
        val output = File("$root/$day/$filename.out$part")
        val check = File("$root/$day/$filename.check$part")

        val computeName = "day: $day part: $part file: $filename"

        val computedSolution = compute(input)
        println("Computed Solution for day: $day part: $part file: $filename is $computedSolution")
        checkSolution(computedSolution, output, check, computeName, checkMandatory)
    }

    private fun checkSolution(
        computedSolution: String,
        output: File,
        check: File,
        computeName: String,
        checkMandatory: Boolean
    ) {
        if (check.exists()) {
            val expectedSolution = check.readLines()[0].trim()
            if (expectedSolution.isBlank()) {
                throw RuntimeException("Check file is empty for $computeName")
            }
            if (expectedSolution != computedSolution) {
                System.err.println("Wrong Solution for ${computeName} with $computedSolution, expected is $expectedSolution")
            } else {
                System.err.println("Correct Solution for ${computeName} is $computedSolution")
            }
        } else {
            if (checkMandatory) {
                throw RuntimeException("Check file not found but mandatory for $computeName")
            }
            System.err.println("Unchecked Solution for ${computedSolution} using ${PUZZLE.name} is $computedSolution")
        }
        checkAndAppendToOutput(output, computedSolution)
    }

    private fun compute(inputFile: File): String {
        if (FileUtils.sizeOf(inputFile) == 0L) {
            throw RuntimeException("Empty Input for ${inputFile.name} using ${PUZZLE.name}")
        }
        val foundSolution = PUZZLE.solveFile(inputFile)
        if (foundSolution.isBlank()) {
            throw RuntimeException("Empty Solution for ${inputFile.name} using ${PUZZLE.name}")
        }
        return foundSolution
    }


    fun checkAndAppendToOutput(outputFile: File, computedSolution: String) {
        if (!outputFile.exists()) {
            outputFile.createNewFile()
        }
        val scanner = Scanner(outputFile)
        var found = false
        scanner.forEachRemaining {
            if (it == computedSolution) {
//                assertTrue("Previous Solution found ${it}", false)
                System.err.println("Previous Solution found ${it}")
                found = true
            }
        }
        if (!found) {
            outputFile.appendText("\n" + computedSolution)
        }
    }

}

