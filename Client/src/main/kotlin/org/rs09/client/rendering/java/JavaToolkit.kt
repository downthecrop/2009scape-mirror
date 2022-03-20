package org.rs09.client.rendering.java

import org.rs09.client.rendering.Toolkit

class JavaToolkit : Toolkit() {
    var buffer: IntArray = IntArray(0)

    @JvmField
    var clipLeft: Int = 0

    @JvmField
    var clipTop: Int = 0

    @JvmField
    var clipRight: Int = 0

    @JvmField
    var clipBottom: Int = 0

    @JvmField
    var width: Int = 0

    @JvmField
    var height: Int = 0

    override fun fillRect(x: Int, y: Int, w: Int, h: Int, rgb: Int, alpha: Int) {
        var x = x
        var y = y
        var width = w
        var height = h
        var rgb = rgb

        if (x < this.clipLeft) {
            width -= this.clipLeft - x
            x = this.clipLeft
        }

        if (y < this.clipTop) {
            height -= this.clipTop - y
            y = this.clipTop
        }

        if (x + width > this.clipRight) {
            width = this.clipRight - x
        }

        if (y + height > this.clipBottom) {
            height = this.clipBottom - y
        }

        rgb = ((rgb and 0xff00ff) * alpha shr 8 and 0xff00ff) + ((rgb and 0xff00) * alpha shr 8 and 0xff00)
        val invertedOpacity = 256 - alpha
        val skipPerLine = this.width - width
        var offset = x + y * this.width

        for (lx in 0 until height) {
            for (ly in -width..-1) {
                var old = buffer[offset]
                old = ((old and 0xff00ff) * invertedOpacity shr 8 and 0xff00ff) + ((old and 0xff00) * invertedOpacity shr 8 and 0xff00)
                buffer[offset++] = rgb + old
            }
            offset += skipPerLine
        }
    }

    override fun drawHorizontalLine(x: Int, y: Int, w: Int, rgb: Int) {
        var x = x
        var width = w

        if (y < this.clipTop || y >= this.clipBottom)
            return

        if (x < this.clipLeft) {
            width -= this.clipLeft - x
            x = this.clipLeft
        }
        if (x + width > this.clipRight) {
            width = this.clipRight - x
        }

        val var4 = x + y * this.width
        for (var5 in 0 until width) {
            buffer[var4 + var5] = rgb
        }
    }

    override fun drawVerticalLine(x: Int, y: Int, h: Int, rgb: Int) {
        var y = y
        var height = h

        if (x < this.clipLeft || x >= this.clipRight)
            return

        if (y < this.clipTop) {
            height -= this.clipTop - y
            y = this.clipTop
        }
        if (y + height > this.clipBottom) {
            height = this.clipBottom - y
        }

        val var4 = x + y * this.width
        for (var5 in 0 until height) {
            buffer[var4 + var5 * width] = rgb
        }
    }

    override fun drawRect(x: Int, y: Int, w: Int, h: Int, rgb: Int, alpha: Int) {
        drawHorizontalLine(x, y, w, rgb)
        drawHorizontalLine(x, y + h - 1, w, rgb)
        drawVerticalLine(x, y, h, rgb)
        drawVerticalLine(x + w - 1, y, h, rgb)
    }

    //Renamed from method1323 to match its openGL counterpart
    override fun method934(x: Int, y: Int, w: Int, h: Int, rgb: Int) {
        var x = x
        var y = y
        var width = w
        var height = h
        if (x < this.clipLeft) {
            width -= this.clipLeft - x
            x = this.clipLeft
        }
        if (y < this.clipTop) {
            height -= this.clipTop - y
            y = this.clipTop
        }
        if (x + w > this.clipRight) {
            width = this.clipRight - x
        }
        if (y + h > this.clipBottom) {
            height = this.clipBottom - y
        }
        val var5 = this.width - w
        var var6 = x + y * this.width

        for (var7 in -height until 0) {
            for (var8 in -width..-1) {
                buffer[var6++] = rgb
            }
            var6 += var5;
        }
    }


    fun resetBuffer() {
        buffer = IntArray(0)
    }
}