package content.minigame.puropuro

import core.api.*
import core.tools.*
import core.game.node.entity.npc.*
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.RegionManager
import core.game.system.task.Pulse
import content.global.skill.hunter.bnet.BNetTypes

import org.rs09.consts.Items

class ImpDefenderBehavior : NPCBehavior(6074) {
    override fun onCreation (self: NPC) {
        self.isWalks = true
        self.isNeverWalks = false
        self.walkRadius = 30
    }

    override fun tick (self: NPC) : Boolean {
        if (!RandomFunction.roll(10)) return true

        var nextCaptureTick = getAttribute(self, "next-capture-tick", 0)
        if (getWorldTicks() < nextCaptureTick) return true

        var players = RegionManager.getLocalPlayers(self, 2)
        for (player in players) {
            var lowestTierImpling = BNetTypes.getImpling(player)
            if (lowestTierImpling == null) continue
            var jarItem = lowestTierImpling.reward
            setAttribute(self, "capture-target", player)
            setAttribute(self, "capture-item", jarItem)
            setAttribute(self, "next-capture-tick", getWorldTicks() + RandomFunction.random(25, 100))
            submitIndividualPulse(self, TryReleasePulse(self))
            break
        }

        return true
    }

    private class TryReleasePulse(val self: NPC) : Pulse() {
        companion object {
            const val catchPlayerLow = 35.0 //14% chance to avoid catch at level 1
            const val catchPlayerHigh = 280.0 //100% chance to avoid catch at level 90 (level 82 with imp repellent)
            const val impRepellentBonus = 20.0 //10% bonus if player has imp repellent
        }

        var counter = 0
        override fun pulse() : Boolean {
            val player: Player? = getAttribute(self, "capture-target", null)
            val jarItem: Item? = getAttribute(self, "capture-item", null)
            if (player == null || jarItem == null) return true
            when (counter++) {
                0 -> {
                    face (self, player)
                    animate (self, 6628)
                    setDelay(animationDuration(getAnimation(6628)))
                    return false
                }
                1 -> {
                    var hasRepellent = inInventory(player, Items.IMP_REPELLENT_11262)
                    var baseRoll = RandomFunction.randomDouble(100.0)
                    var playerRoll = RandomFunction.getSkillSuccessChance(
                        catchPlayerLow + if (hasRepellent) impRepellentBonus else 0.0, 
                        catchPlayerHigh + if (hasRepellent) impRepellentBonus else 0.0, 
                        getStatLevel(player, Skills.THIEVING)
                    )
                    if (playerRoll < baseRoll) {
                        sendChat (self, "Be free!")
                        animate (self, 6629)
                        removeItem (player, jarItem)
                        var loc = ZoneBorders (
                            self.location.x - 2, 
                            self.location.y - 2, 
                            self.location.x + 2, 
                            self.location.y + 2
                        ).randomLoc
                        GroundItemManager.create(
                            Item(Items.IMPLING_JAR_11260), 
                            loc, player
                        ) 
                    }
                    resetFace (self)
                }
            }
            return true
        }
    }
}
