package org.rs09.client.rendering.opengl.enums

/**
 * Source: https://opensource.apple.com/source/X11server/X11server-85/libGL/AppleSGLX-57/specs/enum.spec
 */
object GLBeginMode {
    const val POINTS = 0x0000
    const val LINES = 0x0001
    const val LINE_LOOP = 0x0002
    const val LINE_STRIP = 0x0003
    const val TRIANGLES = 0x0004
    const val TRIANGLE_STRIP = 0x0005
    const val TRIANGLE_FAN = 0x0006
    const val QUADS = 0x0007
    const val QUAD_STRIP = 0x0008
    const val POLYGON = 0x0009
}