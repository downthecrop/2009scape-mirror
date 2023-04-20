package core.api.utils

import core.game.world.map.Location
import kotlin.math.*

class Vector (val x: Double, val y: Double) {
    fun normalized() : Vector {
        val magnitude = magnitude()
        val xComponent = x / magnitude 
        val yComponent = y / magnitude
        return Vector(xComponent, yComponent)
    }

    fun magnitude() : Double {
        return sqrt(x.pow(2.0) + y.pow(2.0))
    }

    operator fun Vector.unaryMinus() = Vector(-x, -y)
    
    operator fun times (other: Double) : Vector {
        return Vector(this.x * other, this.y * other)
    }

    operator fun times (other: Int) : Vector {
        return Vector(this.x * other, this.y * other)
    }

    companion object {
        @JvmStatic fun betweenLocs (from: Location, to: Location) : Vector {
            val xDiff = to.x - from.x
            val yDiff = to.y - from.y
            return Vector (xDiff.toDouble(), yDiff.toDouble())
        }
    }
}
