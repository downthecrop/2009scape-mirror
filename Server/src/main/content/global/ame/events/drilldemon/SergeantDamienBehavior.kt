package content.global.ame.events.drilldemon

import core.api.inBorders
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.game.world.map.Location
import org.rs09.consts.NPCs

class SeargentDamienBehavior: NPCBehavior(NPCs.SERGEANT_DAMIEN_2790) {

    override fun onCreation(self: NPC) {

        // Sergeant Damien is supposed to walk around the perimeter of the exercise mats
        if (inBorders(self, DrillDemonUtils.DD_AREA)) {
            val movementPath = arrayOf(Location.create(3167, 4822, 0), Location.create(3167, 4818, 0), Location.create(3159, 4818, 0), Location.create(3159, 4822, 0))
            self.configureMovementPath(*movementPath)
            self.isWalks = true
        }
    }


}