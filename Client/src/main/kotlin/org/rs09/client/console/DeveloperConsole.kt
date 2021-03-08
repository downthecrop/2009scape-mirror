package org.rs09.client.console

import org.rs09.client.LinkableInt
import org.rs09.client.config.GameConfig
import org.rs09.client.filestore.resources.configs.enums.EnumDefinitionProvider
import org.rs09.client.rendering.RenderingUtils
import org.rs09.client.rendering.Toolkit
import org.runite.client.*
import java.awt.event.KeyEvent
import java.util.*


// TODO Escape characters in the string rendering - is this something we can do using RSString / the text renders?
object DeveloperConsole {

    var ENABLE_PACKETS = false

    private val CONSOLE_FONT
        get() = Class126.aClass3_Sub28_Sub17_1669

    private val LOCK = Any()

    private const val HEIGHT = 300
    private const val BACKGROUND_COLOR = 0x332277
    private const val MAX_LINES = 100
    private const val SCROLL_SPEED = 25

    private val calendar = Calendar.getInstance()
    private var tempBuffer: IntArray? = null
    private val lines = LinkedList<RSString>()
    private var scrollOffset = 0
    private var str: String = ""

    var selectedCompletion = 0
    var autocompletions: AutocompletionHints? = null
        set(t) {
            field = t
            selectedCompletion = 0
        }

    var open = false

    fun toggle() {
        open = !open
    }

    fun draw() {
        if (!open) return

        val tk = Toolkit.getActiveToolkit()
        tk.fillRect(0, 0, RenderingUtils.width, HEIGHT, BACKGROUND_COLOR, 128)
        tk.drawHorizontalLine(0, HEIGHT - 14 - 2, RenderingUtils.width, -1)
        RenderingUtils.drawText(GameConfig.CLIENT_BUILD.toString(), RenderingUtils.width - 27, HEIGHT - 2, -1)
        RenderingUtils.drawText("-> $str", 3, HEIGHT - 2, -1)

        RenderingUtils.setClipping(0, 0, RenderingUtils.width, HEIGHT - 16)
        synchronized(LOCK) {
            lines.forEachIndexed { i, line -> RenderingUtils.drawText(line, 3, scrollOffset + HEIGHT - 20 - i * 14, -1) }
        }
        RenderingUtils.resetClipping()

        // text height = 16
        synchronized(LOCK) {
            autocompletions?.apply {
                val startX = CONSOLE_FONT.method682(RSString.of("-> $str"))
                val boxHeight = 24 + 8 + completions.size * 14
                val boxWidth = 8 + 8 + (completions.map { CONSOLE_FONT.method682(RSString.of(it)) }.maxOrNull() ?: 0)

                tk.fillRect(startX, HEIGHT - 16 - boxHeight, boxWidth, boxHeight, 0x323232, 255)
                RenderingUtils.drawRect(startX + 3, HEIGHT - 16 - boxHeight + 6, boxWidth - 6, boxHeight - 9 - 14, 0x646464, 200)
                tk.drawHorizontalLine(startX + 8, HEIGHT - 16 - boxHeight + 6, 75, 0x323232)
                RenderingUtils.drawText(RSString.parse("Completions"), startX + 12, HEIGHT - 17 - boxHeight + 12, 0xffffff)
                RenderingUtils.drawText(RSString.parse("<col=ee2222>${completions.size}</col>/<col=ee2222>$totalSize</col> sent"), startX + 4, HEIGHT - 20, 0xffffff)

//                tk.fillRect(startX + 4, HEIGHT - 16 - boxHeight + 14, boxWidth - 8, boxHeight - 9 - 14 - 8 - 1, 0xff0000, 255)
//                RenderingUtils.setClipping(startX + 4, HEIGHT - 16 - boxHeight + 14, boxWidth - 8, boxHeight - 9 - 14 - 8 - 1)
                completions.forEachIndexed { i, completion ->
                    if (selectedCompletion == i) {
                        tk.fillRect(startX + 4, HEIGHT - 6 - boxHeight + 4 + i * 14, boxWidth - 8, 14, 0x2a58a8, 255)
                    }
                    RenderingUtils.drawText(RSString.of(completion), startX + 6, HEIGHT - 6 - boxHeight + 14 + i * 14, 0xffffff)
                }
//                RenderingUtils.resetClipping()
            }
        }
    }

    fun println(line: String, timestamp: Boolean = false) {
        calendar.time = Date(TimeUtils.time())
        val h: Int = calendar.get(Calendar.HOUR_OF_DAY)
        val m: Int = calendar.get(Calendar.MINUTE)
        val s: Int = calendar.get(Calendar.SECOND)

        synchronized(LOCK) {
            if (timestamp)
                lines.addFirst(RSString.of("<col=8888cc>[$h:$m:$s]</col> $line"))
            else
                lines.addFirst(RSString.of(line))

            if (lines.size >= MAX_LINES) lines.removeLast()

            if (scrollOffset != 0) {
                val room = HEIGHT - 20
                val max = lines.size * 14
                val diff = max - room
                if (scrollOffset < diff) {
                    scrollOffset += 14
                    if (scrollOffset > diff) scrollOffset = diff
                }
            }
        }
    }

    fun preDraw() {
        if (RenderingUtils.hd) return

        val copy = IntArray(Toolkit.JAVA_TOOLKIT.buffer.size)
        System.arraycopy(Toolkit.JAVA_TOOLKIT.buffer, 0, copy, 0, copy.size)
        tempBuffer = copy
    }

    fun postDraw() {
        if (RenderingUtils.hd) return

        if (tempBuffer != null) {
            System.arraycopy(tempBuffer!!, 0, Toolkit.JAVA_TOOLKIT.buffer, 0, Toolkit.JAVA_TOOLKIT.buffer.size)
        }
        tempBuffer = null
    }

    fun onConsoleInput(str: String) {
//        println("<col=8888cc>[$h:$m:$s]</col> <col=ff3333>TODO!</col> Handle '$str'")

        if (ENABLE_PACKETS) {
            Class3_Sub13_Sub1.outgoingBuffer.putOpcode(51)
            Class3_Sub13_Sub1.outgoingBuffer.writeShort(0)
            val index = Class3_Sub13_Sub1.outgoingBuffer.index
            Class3_Sub13_Sub1.outgoingBuffer.writeString(DeveloperConsole.str)
            Class3_Sub13_Sub1.outgoingBuffer.finishVarshortPacket(Class3_Sub13_Sub1.outgoingBuffer.index - index)
        }

        if (str.toLowerCase() == "enableconsolepackets") {
            ENABLE_PACKETS = true
            println("<col=44ff44>Enabled console packets!</col>")
        } else if (str.toLowerCase() == "quests") {
            println("<col=5555ff>~~~~~ MINIQUESTS ~~~~~</col>")
            System.out.println("~~~~~ MINIQUESTS ~~~~~")
            var lookup = EnumDefinitionProvider.provide(208)

            for (i in 0..17) {
                val component = (lookup.values!![i.toLong()]!! as LinkableInt).value

                val rsiface = Class7.getRSInterface(component)
                if (rsiface == null) println("Error: couldnt find component for hash $component", true)

                println("$i: <col=5555ff>${rsiface.text}</col>", true)
                System.out.println("name ${rsiface.text}, lookup id $i")
            }

            println("<col=5555ff>~~~~~ QUESTS ~~~~~</col>")
            System.out.println("~~~~~ QUESTS ~~~~~")
            lookup = EnumDefinitionProvider.provide(209)

            for (i in 0..130) {
                val component = (lookup.values!![i.toLong()]!! as LinkableInt).value

                val rsiface = Class7.getRSInterface(component)
                if (rsiface == null) println("Error: couldnt find component for hash $component", true)

                println("$i: <col=5555ff>${rsiface.text}</col>", true)
                System.out.println("name ${rsiface.text}, lookup id $i")
            }
        }

        println(str)
    }

    fun handleKeyDown(evt: KeyEvent) {
        if (evt.keyCode == KeyEvent.VK_DOWN) {
            if (autocompletions != null) {
                if (selectedCompletion + 1 < autocompletions?.completions?.size ?: 0)
                    selectedCompletion++
                return
            }

            if (scrollOffset > SCROLL_SPEED) scrollOffset -= SCROLL_SPEED else scrollOffset = 0
        } else if (evt.keyCode == KeyEvent.VK_UP) {
            if (autocompletions != null) {
                if (selectedCompletion > 0) selectedCompletion--
                return
            }

            val room = HEIGHT - 20
            val max = lines.size * 14
            val diff = max - room
            if (scrollOffset < diff) {
                scrollOffset += SCROLL_SPEED
                if (scrollOffset > diff) scrollOffset = diff
            }
        } else if (evt.keyCode == KeyEvent.VK_ESCAPE) {
            synchronized(LOCK) {
                autocompletions = null
            }
        }
    }

    fun handleKeyPressed(evt: KeyEvent) {
        if (evt.keyChar == '`') return

        when {
            evt.keyChar == '\t' -> {
                if (str.isEmpty()) return

                if (ENABLE_PACKETS) {
                    Class3_Sub13_Sub1.outgoingBuffer.putOpcode(52)
                    Class3_Sub13_Sub1.outgoingBuffer.writeShort(0)
                    val index = Class3_Sub13_Sub1.outgoingBuffer.index
                    Class3_Sub13_Sub1.outgoingBuffer.writeString(str)
                    Class3_Sub13_Sub1.outgoingBuffer.finishVarshortPacket(Class3_Sub13_Sub1.outgoingBuffer.index - index)
                } else if ("enableconsolepackets".startsWith(str, true)) {
                    autocompletions = AutocompletionHints(str, listOf("enableconsolepackets"), 1)
                }
            }
            evt.keyChar == '\n' -> {
                if (autocompletions != null) {
                    str += autocompletions?.completions?.get(selectedCompletion) ?: ""
                    autocompletions = null
                    return
                }

                if (str.isNotBlank()) onConsoleInput(str.trim())
                str = ""
            }
            evt.keyChar == '\b' && str.isNotEmpty() -> str = str.substring(0, str.length - 1)
            else -> str += evt.keyChar
        }
    }

}