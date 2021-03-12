package plugin.ai.general.scriptrepository

import core.game.system.command.CommandSystem
import core.game.world.map.Location
import core.game.world.map.RegionManager
import core.game.world.map.path.Pathfinder
import core.tools.RandomFunction
import plugin.ai.AIPlayer

class Idler : Script(){
    override fun tick() {
    }

    override fun newInstance(): Script {
        return this
    }
}