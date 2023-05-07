package content.global.skill.fishing

import core.api.*
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.tools.RandomFunction

private val fishingSpots = FishingSpot.getAllIds()
class FishingNPC : NPCBehavior(*fishingSpots) {

    override fun onCreation(self: NPC) {
        setAttribute(self, "fishing:spot", FishSpots.forLocation(self.location))
        setAttribute(self, "fishing:switchdelay", 0)
    }

    override fun tick(self: NPC): Boolean {
        if(getSpot(self) == null) {
            return false
        }
        if(getAttribute(self, "fishing:switchdelay", 0) < getWorldTicks()) {
            moveSpot(self)
            return false
        }
        return false
    }

    private fun moveSpot(self: NPC) {
        when (val spot = getSpot(self)) {
            null -> {
                self.isInvisible = !self.isInvisible
                setAttribute(self, "fishing:switchdelay", getWorldTicks() + getRandomDelay())
                return
            }
            FishSpots.TUTORIAL_ISLAND -> {
                return
            }
            else -> {
                val randLoc = spot.locations[RandomFunction.random(spot.locations.size)]
                if(findLocalNPCs(randLoc, 0).isEmpty()) {
                    teleport(self, randLoc)
                }
                setAttribute(self, "fishing:switchdelay", getWorldTicks() + getRandomDelay())
            }
        }
    }

    private fun getRandomDelay(): Int {
        return RandomFunction.random(200, 390)
    }

    private fun getSpot(npc: NPC): FishSpots? {
        return getAttribute(npc, "fishing:spot", null)
    }

}