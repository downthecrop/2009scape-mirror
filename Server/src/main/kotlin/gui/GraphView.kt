package gui

import java.awt.*
import javax.swing.JPanel


/**
 * @author Rodrigo, Maritaria
 * @author Ceikry (rewrote into Kotlin, adapted for specific use case)
 */
class GraphView() : JPanel() {
    private val values: MutableList<Int> = ArrayList(10)
    fun setValues(newValues: Collection<Int>) {
        values.clear()
        addValues(newValues)
    }

    fun addValues(newValues: Collection<Int>) {
        if(values.size > 200) values.removeFirst()
        values.addAll(newValues)
        updateUI()
    }

    override fun paintComponent(graphics: Graphics) {
        super.paintComponent(graphics)
        if (graphics !is Graphics2D) {
            graphics.drawString("Graphics is not Graphics2D, unable to render", 0, 0)
            return
        }
        val g: Graphics2D = graphics
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        val length: Int = values.size
        val width: Int = width
        val height: Int = height
        val maxScore: Double = maxScore
        val minScore: Double = minScore
        val scoreRange: Double = maxScore - minScore

        // draw white background
        g.color = Color.WHITE
        g.fillRect(
            padding + labelPadding,
            padding,
            width - (2 * padding) - labelPadding,
            height - (2 * padding) - labelPadding
        )
        g.color = Color.BLACK
        val fontMetrics: FontMetrics = g.fontMetrics
        val fontHeight: Int = fontMetrics.height

        // create hatch marks and grid lines for y axis.
        for (i in 0 until numberYDivisions + 1) {
            val x1: Int = padding + labelPadding
            val x2: Int = pointWidth + padding + labelPadding
            val y: Int =
                height - (((i * (height - (padding * 2) - labelPadding)) / numberYDivisions) + padding + labelPadding)
            if (length > 0) {
                g.color = gridColor
                g.drawLine(padding + labelPadding + 1 + pointWidth, y, width - padding, y)
                g.color = Color.BLACK
                val tickValue: Int = (minScore + ((scoreRange * i) / numberYDivisions)).toInt()
                val yLabel: String = tickValue.toString() + ""
                val labelWidth: Int = fontMetrics.stringWidth(yLabel)
                g.drawString(yLabel, x1 - labelWidth - 5, y + (fontHeight / 2) - 3)
            }
            g.drawLine(x1, y, x2, y)
        }

        // create x and y axes
        g.drawLine(padding + labelPadding, height - padding - labelPadding, padding + labelPadding, padding)
        g.drawLine(
            padding + labelPadding,
            height - padding - labelPadding,
            width - padding,
            height - padding - labelPadding
        )
        val oldStroke: Stroke = g.stroke
        g.color = lineColor
        g.stroke = graphStroke
        val xScale: Double = (width.toDouble() - (2 * padding) - labelPadding) / (length - 1)
        val yScale: Double = (height.toDouble() - (2 * padding) - labelPadding) / scoreRange
        val graphPoints: MutableList<Point> = ArrayList(length)
        for (i in 0 until length) {
            val x1: Int = ((i * xScale) + padding + labelPadding).toInt()
            val y1: Int = ((maxScore - values.get(i)) * yScale + padding).toInt()
            graphPoints.add(Point(x1, y1))
        }
        for (i in 0 until graphPoints.size - 1) {
            val x1: Int = graphPoints[i].x
            val y1: Int = graphPoints[i].y
            val x2: Int = graphPoints[i + 1].x
            val y2: Int = graphPoints[i + 1].y
            if(y2 != 0) {
                g.drawLine(x1, y1, x2, y2)
            }
        }
        val drawDots: Boolean = width > (length * pointWidth)
        if (drawDots) {
            g.stroke = oldStroke
            g.color = pointColor
            for (graphPoint: Point in graphPoints) {
                val x: Int = graphPoint.x - pointWidth / 2
                val y: Int = graphPoint.y - pointWidth / 2
                g.fillOval(x, y, pointWidth, pointWidth)
            }
        }
    }

    private val minScore: Double
        get() = values.stream().min { obj: Int, integer: Int? -> obj.compareTo((integer)!!) }.orElse(0)
            .toDouble()
    private val maxScore: Double
        get() {
            return values.stream().max { obj: Int, integer: Int? -> obj.compareTo((integer)!!) }.orElse(0)
                .toDouble()
        }

    companion object {
        private const val padding: Int = 25
        private const val labelPadding: Int = 25
        private const val pointWidth: Int = 4
        private const val numberYDivisions: Int = 10
        private val lineColor: Color = Color(44, 102, 230, 180)
        private val pointColor: Color = Color(100, 100, 100, 180)
        private val gridColor: Color = Color(200, 200, 200, 200)
        private val graphStroke: Stroke = BasicStroke(2f)
    }

    init {
        preferredSize = Dimension(padding * 2 + 300, padding * 2 + 200)
    }
}