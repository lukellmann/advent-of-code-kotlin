package aoc2022

import AoCDay
import util.illegalInput
import kotlin.math.min

// https://adventofcode.com/2022/day/7
object Day07 : AoCDay<Int>(
    title = "No Space Left On Device",
    part1ExampleAnswer = 95437,
    part1Answer = 1792222,
    part2ExampleAnswer = 24933642,
    part2Answer = 1112963,
) {
    private data class CommandAndOutput(val command: String, val output: List<String>)

    private fun parseCommandsAndOutputs(input: String) = input
        .splitToSequence("\n$ ")
        .mapIndexed { index, part ->
            val commandAndOutput = if (index == 0) part.removePrefix("$ ") else part
            val lines = commandAndOutput.lines()
            CommandAndOutput(command = lines.first(), output = lines.subList(1, lines.size))
        }

    private sealed interface FileOrDirectory
    private class File(val size: Int) : FileOrDirectory
    private class Directory(val parent: Directory?) : FileOrDirectory {
        val children = HashMap<String, FileOrDirectory>()
    }

    private fun parseFileSystem(input: String): Directory {
        val root = Directory(parent = null)
        var cwd = root
        for ((command, output) in parseCommandsAndOutputs(input)) {
            when {
                command.startsWith("cd ") -> {
                    require(output.isEmpty())
                    cwd = when (val dir = command.removePrefix("cd ")) {
                        "/" -> root
                        ".." -> cwd.parent ?: root
                        else -> cwd.children[dir] as Directory
                    }
                }
                command == "ls" -> {
                    for (child in output) {
                        val (dirOrSize, name) = child.split(' ', limit = 2)
                        val prevDirOrSize = when (val prev = cwd.children[name]) {
                            null -> null
                            is File -> prev.size.toString()
                            is Directory -> "dir"
                        }
                        if (prevDirOrSize == null) {
                            cwd.children[name] = when (dirOrSize) {
                                "dir" -> Directory(parent = cwd)
                                else -> File(size = dirOrSize.toInt())
                            }
                        } else {
                            check(dirOrSize == prevDirOrSize) { "There was a change in the file system" }
                        }
                    }
                }
                else -> illegalInput(command)
            }
        }
        return root
    }

    private data class DirectorySize(val size: Int, val children: List<DirectorySize>)

    private val EMPTY_DIRECTORY_SIZE = DirectorySize(size = 0, children = emptyList())

    private fun Directory.calculateDirectorySize(): DirectorySize =
        children.values.fold(initial = EMPTY_DIRECTORY_SIZE) { acc, child ->
            when (child) {
                is File -> acc.copy(size = acc.size + child.size)
                is Directory -> {
                    val childSize = child.calculateDirectorySize()
                    DirectorySize(size = acc.size + childSize.size, children = acc.children + childSize)
                }
            }
        }

    override fun part1(input: String): Int {
        fun DirectorySize.sumOfSizesOfAtMost(n: Int): Int =
            (if (size <= n) size else 0) + (children.sumOf { it.sumOfSizesOfAtMost(n) })

        return parseFileSystem(input).calculateDirectorySize().sumOfSizesOfAtMost(100000)
    }

    override fun part2(input: String): Int {
        val root = parseFileSystem(input).calculateDirectorySize()

        val availableSpace = 70000000 - root.size
        val missingSpace = 30000000 - availableSpace

        fun DirectorySize.sizeOfSmallestDirectoryToDelete(smallestYet: Int): Int =
            if (size < missingSpace) {
                smallestYet
            } else {
                children.fold(initial = min(size, smallestYet)) { smallest, child ->
                    min(smallest, child.sizeOfSmallestDirectoryToDelete(smallestYet = smallest))
                }
            }

        return root.sizeOfSmallestDirectoryToDelete(smallestYet = root.size)
    }
}
