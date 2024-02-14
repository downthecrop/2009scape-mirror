package core.api
import java.util.*
import kotlin.math.ceil

object DialUtils {
    val tagRegex = "<([A-Za-z0-9=/]+)>".toRegex()

    fun removeMatches(message: String, regex: Regex): String {
        return regex.replace(message, "")
    }
}

/**
 * Automatically split a single continuous string into multiple comma-separated lines.
 * Should this not work out for any reason, you should fallback to standard npc and player methods for dialogue.
 */
fun splitLines(message: String, perLineLimit: Int = 54): Array<String> {
    var lines = Array(ceil(DialUtils.removeMatches(message, DialUtils.tagRegex).length / perLineLimit.toFloat()).toInt()) { "" }

    //short circuit when possible because it's cheaper.
    if (lines.size == 1) {
        lines[0] = message
        return lines
    }

    val tokenQueue = LinkedList(message.split(" "))
    var index = 0
    val line = StringBuilder()
    var accumulator = 0

    val openTags = LinkedList<String>()

    fun pushLine() {
        if (line.isEmpty()) return

        // find all tags that were opened or closed in the line
        for (lineTag in DialUtils.tagRegex.findAll(line)) {
            if (lineTag.value.get(1) == '/') {
                // closing tag encountered; remove it from the list of open tags
                for (openTag in openTags.descendingIterator()) {
                    val lineTagName = lineTag.value.substring(2, lineTag.value.length - 1)
                    val openTagName = openTag.substring(1, lineTag.value.length - 2)
                    if (lineTagName == openTagName) {
                        openTags.remove(openTag)
                        break
                    }
                }
            } else {
                openTags.add(lineTag.value)
            }
        }

        //allow array to be resized - there are specific edgecases where it becomes necessary. (Check the unit test for example)
        if (lines.size == index)
            lines = lines.plus(line.toString())
        else
            lines[index] = line.toString()
        index++
        line.clear()
        accumulator = 0

        // if any unclosed tags remain, add them to the beginning of the new line
        for (tag in openTags) line.append(tag)
        openTags.clear()
    }

    while (!tokenQueue.isEmpty()) {
        val shouldSpace = DialUtils.removeMatches(line.toString(), DialUtils.tagRegex).isNotEmpty()
        accumulator += DialUtils.removeMatches(tokenQueue.peek(), DialUtils.tagRegex).length

        if (shouldSpace) accumulator += 1
        if (accumulator > perLineLimit) {
            pushLine()
            continue
        }

        if (shouldSpace) line.append(" ")
        line.append(tokenQueue.pop())
    }

    pushLine()

    // If there's 5 lines, merge lines 4 and 5 into line 4.
    if (lines.size > 4) {
        lines[3] = lines[3] + "<br>" + lines[4]
        lines = lines.sliceArray(0..3)
    }

    return lines
}
