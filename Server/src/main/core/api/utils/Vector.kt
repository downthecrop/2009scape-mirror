package core.api.utils

import core.game.world.map.Direction
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

    operator fun plus (other: Vector) : Vector {
        return Vector(this.x + other.x, this.y + other.y)
    }

    operator fun minus (other: Vector) : Vector {
        return Vector(this.x - other.x, this.y - other.y)
    }

    override fun toString() : String {
        return "{$x,$y}"
    }
    
    fun invert() : Vector {
        return -this
    }

    fun toLocation (plane: Int = 0) : Location {
        return Location.create(floor(x).toInt(), floor(y).toInt(), plane)
    }

    fun toDirection() : Direction {
        val norm = normalized()

        if (norm.x >= 0.85) return Direction.EAST
        else if (norm.x <= -0.85) return Direction.WEST

        if (norm.y > 0) {
            if (norm.y >= 0.85) return Direction.NORTH
            return if (norm.x > 0) Direction.NORTH_EAST else Direction.NORTH_WEST
        } else {
            if (norm.y <= -0.85) return Direction.SOUTH
            return if (norm.x > 0) Direction.SOUTH_EAST else Direction.SOUTH_WEST
        }
    }

    companion object {
        @JvmStatic fun betweenLocs (from: Location, to: Location) : Vector {
            val xDiff = to.x - from.x
            val yDiff = to.y - from.y
            return Vector (xDiff.toDouble(), yDiff.toDouble())
        }
        @JvmStatic fun deriveWithEqualComponents (magnitude: Double) : Vector {
            var sideLength = sqrt(magnitude.pow(2.0) / 2)
            return Vector(sideLength, sideLength)
        }
    }
}
