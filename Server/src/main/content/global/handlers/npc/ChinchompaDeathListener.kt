package content.global.handlers.npc

import core.api.*
import core.api.playGlobalAudio
import core.api.sendGraphics
import core.game.node.entity.Entity
import core.game.node.entity.npc.NPCBehavior
import org.rs09.consts.NPCs
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.tools.RandomFunction
import core.game.world.update.flag.context.Graphics
import core.game.node.entity.combat.ImpactHandler.HitsplatType;

private val chinchompaIDs = intArrayOf(
    NPCs.CHINCHOMPA_5079,
    NPCs.CARNIVOROUS_CHINCHOMPA_5080
)

/**
 * Minimal evasive approach to the Chinchompas exploding on death cause by combat and not by trapping.
 */
class ChinchompaDeathListener: NPCBehavior(*chinchompaIDs) {
    override fun onDeathFinished(self: NPC, killer: Entity) {

        // If it 'died' from getting trapped an attribute is set of 'hunter'.
        if (!self.attributes.containsKey("hunter") && killer is Player) {
            val explosionLoc = self.location
            val validExplosionDmgTiles = explosionLoc.surroundingTiles

            val chinExplosionGFX = Graphics(157, 96)
            sendGraphics(chinExplosionGFX, explosionLoc)
            playGlobalAudio(explosionLoc, 158)

            if (validExplosionDmgTiles.contains(killer.location)) {
                //As weird this looks, it does allow to have a hit from 0 to 2
                val hitAmount = RandomFunction.random(0, 3)
                impact(killer, hitAmount, HitsplatType.NORMAL)
            }
        }
    }

    /**
     * At content.global.skill.hunter.TrapSetting
     * sets an attribute of 'hunter' during it's getCatchPulse for reward rolling, but it never cleans it up after.
     * These NPCs is a member of NPC class although exists a HunterNPC class, but it's mainly used for imps.
     * Logically you'd do this after death finishes, but it would interfere with the reward rolls and I fear it may affect other hunter NPCs.
     */
    override fun onRespawn(self: NPC) {
        self.removeAttribute("hunter")
    }
}