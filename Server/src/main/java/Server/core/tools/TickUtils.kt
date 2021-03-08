package core.tools

const val tick = 600 //ms
const val second = 1000 //ms

fun secondsToTicks(seconds: Int): Int {
    val seconds = seconds * second //seconds -> ms
    return seconds / tick //returns an int with the closest number of ticks to the desired number of seconds possible
}

fun ticksToSeconds(ticks: Int): Int {
    val ticksMs = ticks * tick
    return ticksMs / 1000
}