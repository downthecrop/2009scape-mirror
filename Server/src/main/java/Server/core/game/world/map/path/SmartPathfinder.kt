package core.game.world.map.path

import core.game.world.map.Location
import core.game.world.map.Point

class SmartPathfinder
/**
 * Constructs a new `SmartPathfinder` `Object`.
 */
internal constructor() : Pathfinder() {
    /**
     * The x-queue.
     */
    private var queueX: IntArray = intArrayOf(0)

    /**
     * The y-queue.
     */
    private var queueY: IntArray = intArrayOf(0)

    /**
     * The "via" array.
     */
    private var via: Array<IntArray> =  Array(104) { IntArray(104) }

    /**
     * The cost array.
     */
    private var cost: Array<IntArray> =  Array(104) { IntArray(104) }

    /**
     * The current writing position.
     */
    private var writePathPosition = 0

    /**
     * The current x-coordinate.
     */
    private var curX = 0

    /**
     * The current y-coordinate.
     */
    private var curY = 0

    /**
     * The destination x-coordinate.
     */
    private var dstX = 0

    /**
     * The destination y-coordinate.
     */
    private var dstY = 0

    /**
     * If a path was found.
     */
    private var foundPath = false

    /**
     * Resets the pathfinder.
     */
    fun reset() {
        queueX = IntArray(4096)
        queueY = IntArray(4096)
        via = Array(104) { IntArray(104) }
        cost = Array(104) { IntArray(104) }
        writePathPosition = 0
    }

    /**
     * Checks a tile.
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     * @param dir The direction.
     * @param currentCost The current cost.
     */
    fun check(x: Int, y: Int, dir: Int, currentCost: Int) {
        queueX[writePathPosition] = x
        queueY[writePathPosition] = y
        via[x][y] = dir
        cost[x][y] = currentCost
        writePathPosition = writePathPosition + 1 and 0xfff
    }

    override fun find(start: Location?, moverSize: Int, end: Location?, sizeX: Int, sizeY: Int, rotation: Int, type: Int, walkingFlag: Int, near: Boolean, clipMaskSupplier: ClipMaskSupplier?): Path {
        reset()
        assert(start != null && end != null)
        val path = Path()
        foundPath = false
        for (x in 0..103) {
            for (y in 0..103) {
                via[x][y] = 0
                cost[x][y] = 99999999
            }
        }
        val z = start!!.z
        val location = Location.create(start.regionX - 6 shl 3, start.regionY - 6 shl 3, z)
        curX = start.sceneX
        curY = start.sceneY
        dstX = end!!.getSceneX(start)
        dstY = end.getSceneY(start)
        var attempts: Int
        var readPosition: Int
        check(curX, curY, 99, 0)
        try {
            if (moverSize < 2) {
                checkSingleTraversal(end, sizeX, sizeY, type, rotation, walkingFlag, location, clipMaskSupplier!!)
            } else if (moverSize == 2) {
                checkDoubleTraversal(end, sizeX, sizeY, type, rotation, walkingFlag, location, clipMaskSupplier!!)
            } else {
                checkVariableTraversal(end, moverSize, sizeX, sizeY, type, rotation, walkingFlag, location, clipMaskSupplier!!)
            }
        } catch (e: Exception) {}
        if (!foundPath) {
            if (near) {
                var fullCost = 1000
                var thisCost = 100
                val depth = 10
                for (x in dstX - depth..dstX + depth) {
                    for (y in dstY - depth..dstY + depth) {
                        if (x >= 0 && y >= 0 && x < 104 && y < 104 && cost[x][y] < 100) {
                            var diffX = 0
                            if (x < dstX) {
                                diffX = dstX - x
                            } else if (x > dstX + sizeX - 1) {
                                diffX = x - (dstX + sizeX - 1)
                            }
                            var diffY = 0
                            if (y < dstY) {
                                diffY = dstY - y
                            } else if (y > dstY + sizeY - 1) {
                                diffY = y - (dstY + sizeY - 1)
                            }
                            val totalCost = diffX * diffX + diffY * diffY
                            if (totalCost < fullCost || totalCost == fullCost && cost[x][y] < thisCost) {
                                fullCost = totalCost
                                thisCost = cost[x][y]
                                curX = x
                                curY = y
                            }
                        }
                    }
                }
                if (fullCost == 1000) {
                    return path
                }
                path.isMoveNear = true
            }
        }
        readPosition = 0
        queueX[readPosition] = curX
        queueY[readPosition++] = curY
        var previousDirection: Int
        attempts = 0
        var directionFlag = via[curX][curY].also { previousDirection = it }
        while (curX != start.sceneX || curY != start.sceneY) {
            if (++attempts > queueX.size) {
                return path
            }
            if (directionFlag != previousDirection) {
                previousDirection = directionFlag
                queueX[readPosition] = curX
                queueY[readPosition++] = curY
            }
            if (directionFlag and WEST_FLAG != 0) {
                curX++
            } else if (directionFlag and EAST_FLAG != 0) {
                curX--
            }
            if (directionFlag and SOUTH_FLAG != 0) {
                curY++
            } else if (directionFlag and NORTH_FLAG != 0) {
                curY--
            }
            directionFlag = via[curX][curY]
        }
        val size = readPosition--
        var absX = location.x + queueX[readPosition]
        var absY = location.y + queueY[readPosition]
        path.points.add(Point(absX, absY))
        for (i in 1 until size) {
            readPosition--
            absX = location.x + queueX[readPosition]
            absY = location.y + queueY[readPosition]
            path.points.add(Point(absX, absY))
        }
        path.setSuccesful(true)
        return path
    }

    /**
     * Checks possible traversal for a size 1 entity.
     * @param end The destination location.
     * @param sizeX The x-size of the destination.
     * @param sizeY The y-size of the destination.
     * @param type The object type.
     * @param rotation The object rotation.
     * @param walkingFlag The walking flag.
     * @param location The viewport location.
     */
    private fun checkSingleTraversal(end: Location, sizeX: Int, sizeY: Int, type: Int, rotation: Int, walkingFlag: Int, location: Location, clipMaskSupplier: ClipMaskSupplier) {
        var readPosition = 0
        val z = location.z
        while (writePathPosition != readPosition) {
            curX = queueX[readPosition]
            curY = queueY[readPosition]
            readPosition = readPosition + 1 and 0xfff
            if (curX == dstX && curY == dstY) {
                foundPath = true
                break
            }
            try {
                val absX = location.x + curX
                val absY = location.y + curY
                if (type != 0) {
                    if ((type < 5 || type == 10) && canDoorInteract(absX, absY, 1, end.x, end.y, type - 1, rotation, z, clipMaskSupplier)) {
                        foundPath = true
                        break
                    }
                    if (type < 10 && canDecorationInteract(absX, absY, 1, end.x, end.y, type - 1, rotation, z, clipMaskSupplier)) {
                        foundPath = true
                        break
                    }
                }
                if (sizeX != 0 && sizeY != 0 && canInteract(absX, absY, 1, end.x, end.y, sizeX, sizeY, walkingFlag, z, clipMaskSupplier)) {
                    foundPath = true
                    break
                }
                val thisCost = cost[curX][curY] + 1
                if (curY > 0 && via[curX][curY - 1] == 0 && clipMaskSupplier.getClippingFlag(z, absX, absY - 1) and 0x12c0102 == 0) {
                    check(curX, curY - 1, SOUTH_FLAG, thisCost)
                }
                if (curX > 0 && via[curX - 1][curY] == 0 && clipMaskSupplier.getClippingFlag(z, absX - 1, absY) and 0x12c0108 == 0) {
                    check(curX - 1, curY, WEST_FLAG, thisCost)
                }
                if (curY < 103 && via[curX][curY + 1] == 0 && clipMaskSupplier.getClippingFlag(z, absX, absY + 1) and 0x12c0120 == 0) {
                    check(curX, curY + 1, NORTH_FLAG, thisCost)
                }
                if (curX < 103 && via[curX + 1][curY] == 0 && clipMaskSupplier.getClippingFlag(z, absX + 1, absY) and 0x12c0180 == 0) {
                    check(curX + 1, curY, EAST_FLAG, thisCost)
                }
                if (curX > 0 && curY > 0 && via[curX - 1][curY - 1] == 0 && clipMaskSupplier.getClippingFlag(z, absX - 1, absY - 1) and 0x12c010e == 0 && clipMaskSupplier.getClippingFlag(z, absX - 1, absY) and 0x12c0108 == 0 && clipMaskSupplier.getClippingFlag(z, absX, absY - 1) and 0x12c0102 == 0) {
                    check(curX - 1, curY - 1, SOUTH_WEST_FLAG, thisCost)
                }
                if (curX > 0 && curY < 103 && via[curX - 1][curY + 1] == 0 && clipMaskSupplier.getClippingFlag(z, absX - 1, absY + 1) and 0x12c0138 == 0 && clipMaskSupplier.getClippingFlag(z, absX - 1, absY) and 0x12c0108 == 0 && clipMaskSupplier.getClippingFlag(z, absX, absY + 1) and 0x12c0120 == 0) {
                    check(curX - 1, curY + 1, NORTH_WEST_FLAG, thisCost)
                }
                if (curX < 103 && curY > 0 && via[curX + 1][curY - 1] == 0 && clipMaskSupplier.getClippingFlag(z, absX + 1, absY - 1) and 0x12c0183 == 0 && clipMaskSupplier.getClippingFlag(z, absX + 1, absY) and 0x12c0180 == 0 && clipMaskSupplier.getClippingFlag(z, absX, absY - 1) and 0x12c0102 == 0) {
                    check(curX + 1, curY - 1, SOUTH_EAST_FLAG, thisCost)
                }
                if (curX < 103 && curY < 103 && via[curX + 1][curY + 1] == 0 && clipMaskSupplier.getClippingFlag(z, absX + 1, absY + 1) and 0x12c01e0 == 0 && clipMaskSupplier.getClippingFlag(z, absX + 1, absY) and 0x12c0180 == 0 && clipMaskSupplier.getClippingFlag(z, absX, absY + 1) and 0x12c0120 == 0) {
                    check(curX + 1, curY + 1, NORTH_EAST_FLAG, thisCost)
                }
            } catch (e: Exception) {
               // e.printStackTrace()println("curX " + curX + " curY" + curY + " via " + via[curX + 1] + via[curY + 1])
            }
        }
    }

    /**
     * Checks possible traversal for a size 2 entity.
     * @param end The destination location.
     * @param sizeX The x-size of the destination.
     * @param sizeY The y-size of the destination.
     * @param type The object type.
     * @param rotation The object rotation.
     * @param walkingFlag The walking flag.
     * @param location The viewport location.
     */
    private fun checkDoubleTraversal(end: Location, sizeX: Int, sizeY: Int, type: Int, rotation: Int, walkingFlag: Int, location: Location, clipMaskSupplier: ClipMaskSupplier) {
        var readPosition = 0
        val z = location.z
        while (writePathPosition != readPosition) {
            curX = queueX[readPosition]
            curY = queueY[readPosition]
            readPosition = readPosition + 1 and 0xfff
            if (curX == dstX && curY == dstY) {
                foundPath = true
                break
            }
            val absX = location.x + curX
            val absY = location.y + curY
            if (type != 0) {
                if ((type < 5 || type == 10) && canDoorInteract(absX, absY, 2, end.x, end.y, type - 1, rotation, z, clipMaskSupplier)) {
                    foundPath = true
                    break
                }
                if (type < 10 && canDecorationInteract(absX, absY, 2, end.x, end.y, type - 1, rotation, z, clipMaskSupplier)) {
                    foundPath = true
                    break
                }
            }
            if (sizeX != 0 && sizeY != 0 && canInteract(absX, absY, 2, end.x, end.y, sizeX, sizeY, walkingFlag, z, clipMaskSupplier)) {
                foundPath = true
                break
            }
            val thisCost = cost[curX][curY] + 1
            if (curY > 0 && via[curX][curY - 1] == 0 && clipMaskSupplier.getClippingFlag(z, absX, absY - 1) and 0x12c010e == 0 && clipMaskSupplier.getClippingFlag(z, absX + 1, absY - 1) and 0x12c0183 == 0) {
                check(curX, curY - 1, SOUTH_FLAG, thisCost)
            }
            if (curX > 0 && via[curX - 1][curY] == 0 && clipMaskSupplier.getClippingFlag(z, absX - 1, absY) and 0x12c010e == 0 && clipMaskSupplier.getClippingFlag(z, absX - 1, absY + 1) and 0x12c0138 == 0) {
                check(curX - 1, curY, WEST_FLAG, thisCost)
            }
            if (curY < 102 && via[curX][curY + 1] == 0 && clipMaskSupplier.getClippingFlag(z, absX, absY + 2) and 0x12c0138 == 0 && clipMaskSupplier.getClippingFlag(z, absX + 1, absY + 2) and 0x12c01e0 == 0) {
                check(curX, curY + 1, NORTH_FLAG, thisCost)
            }
            if (curX < 102 && via[curX + 1][curY] == 0 && clipMaskSupplier.getClippingFlag(z, absX + 2, absY) and 0x12c0183 == 0 && clipMaskSupplier.getClippingFlag(z, absX + 2, absY + 1) and 0x12c01e0 == 0) {
                check(curX + 1, curY, EAST_FLAG, thisCost)
            }
            if (curX > 0 && curY > 0 && via[curX - 1][curY - 1] == 0 && clipMaskSupplier.getClippingFlag(z, absX - 1, absY - 1) and 0x12c010e == 0 && clipMaskSupplier.getClippingFlag(z, absX - 1, absY) and 0x12c0138 == 0 && clipMaskSupplier.getClippingFlag(z, absX, absY - 1) and 0x12c0183 == 0) {
                check(curX - 1, curY - 1, SOUTH_WEST_FLAG, thisCost)
            }
            if (curX > 0 && curY < 102 && via[curX - 1][curY + 1] == 0 && clipMaskSupplier.getClippingFlag(z, absX - 1, absY + 1) and 0x12c010e == 0 && clipMaskSupplier.getClippingFlag(z, absX - 1, absY + 2) and 0x12c0138 == 0 && clipMaskSupplier.getClippingFlag(z, absX, absY + 2) and 0x12c01e0 == 0) {
                check(curX - 1, curY + 1, NORTH_WEST_FLAG, thisCost)
            }
            if (curX < 102 && curY > 0 && via[curX + 1][curY - 1] == 0 && clipMaskSupplier.getClippingFlag(z, absX + 1, absY - 1) and 0x12c010e == 0 && clipMaskSupplier.getClippingFlag(z, absX + 2, absY) and 0x12c01e0 == 0 && clipMaskSupplier.getClippingFlag(z, absX + 2, absY - 1) and 0x12c0183 == 0) {
                check(curX + 1, curY - 1, SOUTH_EAST_FLAG, thisCost)
            }
            if (curX < 102 && curY < 102 && via[curX + 1][curY + 1] == 0 && clipMaskSupplier.getClippingFlag(z, absX + 1, absY + 2) and 0x12c0138 == 0 && clipMaskSupplier.getClippingFlag(z, absX + 2, absY + 2) and 0x12c01e0 == 0 && clipMaskSupplier.getClippingFlag(z, absX + 2, absY + 1) and 0x12c0183 == 0) {
                check(curX + 1, curY + 1, NORTH_EAST_FLAG, thisCost)
            }
        }
    }

    /**
     * Checks possible traversal for any sized entity.
     * @param end The destination location.
     * @param size The mover size.
     * @param sizeX The x-size of the destination.
     * @param sizeY The y-size of the destination.
     * @param type The object type.
     * @param rotation The object rotation.
     * @param walkingFlag The walking flag.
     * @param location The viewport location.
     */
    private fun checkVariableTraversal(end: Location, size: Int, sizeX: Int, sizeY: Int, type: Int, rotation: Int, walkingFlag: Int, location: Location, clipMaskSupplier: ClipMaskSupplier) {
        var readPosition = 0
        val z = location.z
        main@ while (writePathPosition != readPosition) {
            curX = queueX[readPosition]
            curY = queueY[readPosition]
            readPosition = readPosition + 1 and 0xfff
            if (curX == dstX && curY == dstY) {
                foundPath = true
                break
            }
            val absX = location.x + curX
            val absY = location.y + curY
            if (type != 0) {
                if ((type < 5 || type == 10) && canDoorInteract(absX, absY, size, end.x, end.y, type - 1, rotation, z, clipMaskSupplier)) {
                    foundPath = true
                    break
                }
                if (type < 10 && canDecorationInteract(absX, absY, size, end.x, end.y, type - 1, rotation, z, clipMaskSupplier)) {
                    foundPath = true
                    break
                }
            }
            if (sizeX != 0 && sizeY != 0 && canInteract(absX, absY, size, end.x, end.y, sizeX, sizeY, walkingFlag, z, clipMaskSupplier)) {
                foundPath = true
                break
            }
            val thisCost = cost[curX][curY] + 1
            south@ do {
                if (curY > 0 && via[curX][curY - 1] == 0 && clipMaskSupplier.getClippingFlag(z, absX, absY - 1) and 0x12c010e == 0 && clipMaskSupplier.getClippingFlag(z, absX + (size - 1), absY - 1) and 0x12c0183 == 0) {
                    for (i in 1 until size - 1) {
                        if (clipMaskSupplier.getClippingFlag(z, absX + i, absY - 1) and 0x12c018f != 0) {
                            break@south
                        }
                    }
                    check(curX, curY - 1, SOUTH_FLAG, thisCost)
                }
            } while (false)
            west@ do {
                if (curX > 0 && via[curX - 1][curY] == 0 && clipMaskSupplier.getClippingFlag(z, absX - 1, absY) and 0x12c010e == 0 && clipMaskSupplier.getClippingFlag(z, absX - 1, absY + (size - 1)) and 0x12c0138 == 0) {
                    for (i in 1 until size - 1) {
                        if (clipMaskSupplier.getClippingFlag(z, absX - 1, absY + i) and 0x12c013e != 0) {
                            break@west
                        }
                    }
                    check(curX - 1, curY, WEST_FLAG, thisCost)
                }
            } while (false)
            north@ do {
                if (curY < 102 && via[curX][curY + 1] == 0 && clipMaskSupplier.getClippingFlag(z, absX, absY + size) and 0x12c0138 == 0 && clipMaskSupplier.getClippingFlag(z, absX + (size - 1), absY + size) and 0x12c01e0 == 0) {
                    for (i in 1 until size - 1) {
                        if (clipMaskSupplier.getClippingFlag(z, absX + i, absY + size) and 0x12c01f8 != 0) {
                            break@north
                        }
                    }
                    check(curX, curY + 1, NORTH_FLAG, thisCost)
                }
            } while (false)
            east@ do {
                if (curX < 102 && via[curX + 1][curY] == 0 && clipMaskSupplier.getClippingFlag(z, absX + size, absY) and 0x12c0183 == 0 && clipMaskSupplier.getClippingFlag(z, absX + size, absY + (size - 1)) and 0x12c01e0 == 0) {
                    for (i in 1 until size - 1) {
                        if (clipMaskSupplier.getClippingFlag(z, absX + size, absY + i) and 0x12c01e3 != 0) {
                            break@east
                        }
                    }
                    check(curX + 1, curY, EAST_FLAG, thisCost)
                }
            } while (false)
            southWest@ do {
                if (curX > 0 && curY > 0 && via[curX - 1][curY - 1] == 0 && clipMaskSupplier.getClippingFlag(z, absX - 1, absY + (size - 2)) and 0x12c0138 == 0 && clipMaskSupplier.getClippingFlag(z, absX - 1, absY - 1) and 0x12c010e == 0 && clipMaskSupplier.getClippingFlag(z, absX + (size - 2), absY - 1) and 0x12c0183 == 0) {
                    for (i in 1 until size - 1) {
                        if (clipMaskSupplier.getClippingFlag(z, absX - 1, absY + (i - 1)) and 0x12c013e != 0 || clipMaskSupplier.getClippingFlag(z, absX + (i - 1), absY - 1) and 0x12c018f != 0) {
                            break@southWest
                        }
                    }
                    check(curX - 1, curY - 1, SOUTH_WEST_FLAG, thisCost)
                }
            } while (false)
            northWest@ do {
                if (curX > 0 && curY < 102 && via[curX - 1][curY + 1] == 0 && clipMaskSupplier.getClippingFlag(z, absX - 1, absY + 1) and 0x12c010e == 0 && clipMaskSupplier.getClippingFlag(z, absX - 1, absY + size) and 0x12c0138 == 0 && clipMaskSupplier.getClippingFlag(z, absX, absY + size) and 0x12c01e0 == 0) {
                    for (i in 1 until size - 1) {
                        if (clipMaskSupplier.getClippingFlag(z, absX - 1, absY + (i + 1)) and 0x12c013e != 0 || clipMaskSupplier.getClippingFlag(z, absX + (i - 1), absY + size) and 0x12c01f8 != 0) {
                            break@northWest
                        }
                    }
                    check(curX - 1, curY + 1, NORTH_WEST_FLAG, thisCost)
                }
            } while (false)
            southEast@ do {
                if (curX < 102 && curY > 0 && via[curX + 1][curY - 1] == 0 && clipMaskSupplier.getClippingFlag(z, absX + 1, absY - 1) and 0x12c010e == 0 && clipMaskSupplier.getClippingFlag(z, absX + size, absY - 1) and 0x12c0183 == 0 && clipMaskSupplier.getClippingFlag(z, absX + size, absY + (size - 2)) and 0x12c01e0 == 0) {
                    for (i in 1 until size - 1) {
                        if (clipMaskSupplier.getClippingFlag(z, absX + size, absY + (i - 1)) and 0x12c01e3 != 0 || clipMaskSupplier.getClippingFlag(z, absX + (i + 1), absY - 1) and 0x12c018f != 0) {
                            break@southEast
                        }
                    }
                    check(curX + 1, curY - 1, SOUTH_EAST_FLAG, thisCost)
                }
            } while (false)
            if (curX < 102 && curY < 102 && via[curX + 1][curY + 1] == 0 && clipMaskSupplier.getClippingFlag(z, absX + 1, absY + size) and 0x12c0138 == 0 && clipMaskSupplier.getClippingFlag(z, absX + size, absY + size) and 0x12c01e0 == 0 && clipMaskSupplier.getClippingFlag(z, absX + size, absY + 1) and 0x12c0183 == 0) {
                for (i in 1 until size - 1) {
                    if (clipMaskSupplier.getClippingFlag(z, absX + (i + 1), absY + size) and 0x12c01f8 != 0 || clipMaskSupplier.getClippingFlag(z, absX + size, absY + (i + 1)) and 0x12c01e3 != 0) {
                        continue@main
                    }
                }
                check(curX + 1, curY + 1, NORTH_EAST_FLAG, thisCost)
            }
        }
    }
}