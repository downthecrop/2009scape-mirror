package rs09.game.content.quest.members.lostcity

import core.game.node.scenery.Scenery
import core.game.node.entity.skill.gather.SkillingTool
import core.game.node.entity.skill.gather.woodcutting.WoodcuttingSkillPulse
import core.game.world.map.Location
import core.plugin.Initializable
import org.rs09.consts.NPCs
import org.rs09.consts.Scenery as Sceneries
import rs09.game.interaction.InteractionListener
import api.questStage
import api.sendMessage
import rs09.game.interaction.IntType

class DramenTreeListener : InteractionListener {

    override fun defineListeners() {

        on(Sceneries.DRAMEN_TREE_1292, IntType.SCENERY, "chop down"){ player, node ->
            val questStage = questStage(player,"Lost City")
            if (SkillingTool.getHatchet(player) == null) {
                sendMessage(player,"You do not have an axe which you have the level to use.")
                return@on true
            }
            if (questStage < 20) {
                return@on true
            }
            if (questStage == 20) {
                if (player.getAttribute("treeSpawned", false)) {
                    return@on true
                }
                val spirit = TreeSpiritNPC(NPCs.TREE_SPIRIT_655, Location(2862, 9734, 0))
                spirit.target = player
                spirit.init()
                spirit.attack(player)
                player.setAttribute("treeSpawned", true)
                spirit.sendChat("You must defeat me before touching the tree!")
                return@on true
            }

            player.pulseManager.run(WoodcuttingSkillPulse(player, node as Scenery))
            return@on true
        }

    }

}
