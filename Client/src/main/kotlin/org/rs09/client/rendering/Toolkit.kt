package org.rs09.client.rendering

import org.rs09.client.rendering.java.JavaToolkit
import org.rs09.client.rendering.opengl.OpenGlToolkit

/**
 * Represents an abstract rendering toolkit. The toolkit should support all raw rendering operations including
 * rasterization.
 *
 * By abstracting rendering through toolkits, additional rendering backends can be implemented in the future.
 */
abstract class Toolkit {

    companion object {
        @JvmField
        val OPENGL_TOOLKIT = OpenGlToolkit()
        @JvmField
        val JAVA_TOOLKIT = JavaToolkit()

        @JvmStatic
        fun getActiveToolkit(): Toolkit {
            return if (RenderingUtils.hd) OPENGL_TOOLKIT else JAVA_TOOLKIT
        }
    }

    abstract fun fillRect(x: Int, y: Int, w: Int, h: Int, rgb: Int, alpha: Int)

    abstract fun drawHorizontalLine(x: Int, y: Int, w: Int, rgb: Int)

    abstract fun drawVerticalLine(x: Int, y: Int, h: Int, rgb: Int)

    abstract fun drawRect(x: Int, y: Int, w: Int, h: Int, rgb: Int, alpha: Int)

    abstract fun method934(x: Int, y: Int, w: Int, h: Int, rgb: Int)



}