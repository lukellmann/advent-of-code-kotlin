import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option
import java.nio.file.Files.createFile
import java.time.Year
import kotlin.io.path.*

private val DAY_MAIN_LINE_REGEX = Regex(""" {4}aoc(\d{4}).Day(\d{1,2}),""")

abstract class GenerateFilesAndBoilerplateForDayTask : DefaultTask() {

    @get:Input
    @get:Optional
    @get:Option(
        option = "year",
        description = "The year to generate files and boilerplate for. Defaults to current year.",
    )
    abstract val year: Property<Int>

    @TaskAction
    fun generateFilesAndBoilerplateForDay() {
        val year = year.getOrElse(Year.now().value)
        val rootDir = project.rootDir.toPath()
        val kotlinDir = rootDir / "src" / "main" / "kotlin"

        // add new day to set in Main.kt
        val mainFile = kotlinDir / "Main.kt"
        val mainLines = mainFile.useLines { lines -> lines.toMutableList() }
        val (day, insertIndex) = getDayAndInsertIndex(year, mainLines)
        mainLines.add(insertIndex, "    aoc$year.Day$day,")
        mainFile.writeLines(mainLines)

        // create file with daily object boilerplate
        val aocDir = kotlinDir / "aoc$year"
        aocDir.createDirectories()
        (aocDir / "Day$day.kt").writeText(dayObjectBoilerplate(year, day))

        // create input files, content will be copied over manually
        val inputDir = rootDir / "input" / "aoc$year"
        inputDir.createDirectories()
        createFile(inputDir / "day$day-example.txt")
        createFile(inputDir / "day$day.txt")
    }


    private fun getDayAndInsertIndex(year: Int, mainLines: Iterable<String>): Pair<Int, Int> {
        class Day(val year: Int, val day: Int, val index: Int)

        val yearToMostCurrentDay = mainLines
            .asSequence()
            .mapIndexedNotNull { index, line ->
                DAY_MAIN_LINE_REGEX.matchEntire(line)?.let { IndexedValue(index, it) }
            }
            .map { (index, match) ->
                val (matchedYear, matchedDay) = match.destructured
                Day(year = matchedYear.toInt(), day = matchedDay.toInt(), index)
            }
            .groupingBy { it.year }
            .reduce { _, acc, day -> if (day.day > acc.day) day else acc }

        val years = yearToMostCurrentDay.keys.sortedDescending()
        val previousDay = years.firstOrNull { it <= year }?.let(yearToMostCurrentDay::get)

        val day: Int
        val insertIndex: Int

        if (previousDay == null) {
            day = 1
            insertIndex = 1 // line at index 0 is `private val days = setOf(`
        } else {
            day = if (previousDay.year == year) previousDay.day + 1 else 1
            insertIndex = previousDay.index + 1
        }

        return Pair(day, insertIndex)
    }


    private fun dayObjectBoilerplate(year: Int, day: Int) = """
        package aoc$year
        
        import AoCDay
        
        // https://adventofcode.com/$year/day/$day
        object Day$day : AoCDay(
            title = "",
            part1ExampleAnswer = ,
            part1Answer = null,
            part2ExampleAnswer = null,
            part2Answer = null,
        ) {
            override fun part1(input: String) = super.part1(input)
        
            override fun part2(input: String) = super.part2(input)
        }
        
    """.trimIndent().replace("\n", System.lineSeparator())
}
