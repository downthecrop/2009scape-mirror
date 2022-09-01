package api
import java.util.*
import kotlin.math.ceil

/**
 * Automatically split a single continuous string into multiple comma-separated lines.
 * Should this not work out for any reason, you should fallback to standard npc and player methods for dialogue.
 */
fun splitLines(message: String, perLineLimit: Int = 54) : Array<String> {
    var lines = Array(ceil(message.length / perLineLimit.toFloat()).toInt()) {""}

    //short circuit when possible because it's cheaper.
    if (lines.size == 1) {
        lines[0] = message
        return lines
    }

    val tokenQueue = LinkedList(message.split(" "))
    var index = 0
    val line = StringBuilder()
    var accumulator = 0

    fun pushLine() {
        if (line.isEmpty()) return
        //allow array to be resized - there are specific edgecases where it becomes necessary. (Check the unit test for example)
        if (lines.size == index)
            lines = lines.plus(line.toString())
        else
            lines[index] = line.toString()
        index++
        line.clear()
        accumulator = 0
    }

    while (!tokenQueue.isEmpty()) {
        val shouldSpace = line.isNotEmpty()
        accumulator += tokenQueue.peek().length

        if (shouldSpace) accumulator += 1
        if (accumulator > perLineLimit) {
            pushLine()
            continue
        }

        if (shouldSpace) line.append(" ")
        line.append(tokenQueue.pop())
    }

    pushLine()
    return lines
}