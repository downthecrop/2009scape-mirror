package rs09.tools.stringtools

const val RED = "<col=ff0000>"
const val ORANGE = "<col=ff6600>"
const val YELLOW = "<col=ffff00>"
const val GREEN = "<col=66ff33>"
const val BLUE = "<col=3366ff>"
const val PURPLE = "<col=cc00ff>"

fun colorize(line: String): String{
    val new = line.replace("%R", RED)
            .replace("%O", ORANGE)
            .replace("%Y",YELLOW)
            .replace("%G",GREEN)
            .replace("%B",BLUE)
            .replace("%P",PURPLE).append("</col>") + " "
    return new
}

fun colorize(line: String, hexColor: String): String{
    return line.prepend("<col=$hexColor>").append("</col>")
}

fun String.append(line: String): String{
    return this + line
}

fun String.prepend(line: String): String{
    return line + this
}

fun String.shuffle(): String{
    var new = ""
    val old = this.split("").toMutableList()
    for(i in this.indices){
        val c = old.random()
        new += c.toString()
        old.remove(c)
    }
    return new
}
