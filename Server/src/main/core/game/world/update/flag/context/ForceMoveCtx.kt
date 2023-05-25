package core.game.world.update.flag.context

import core.game.world.map.*

data class ForceMoveCtx (val start: Location, val dest: Location, val startArrive: Int, val destArrive: Int, val direction: Direction)
