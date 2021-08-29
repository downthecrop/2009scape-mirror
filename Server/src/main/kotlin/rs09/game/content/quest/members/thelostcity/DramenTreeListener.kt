package rs09.game.content.quest.members.thelostcity

import core.game.content.quest.members.lostcity.TreeSpiritNPC
import core.game.node.scenery.Scenery
import core.game.node.entity.skill.gather.GatheringSkillPulse
import core.game.node.entity.skill.gather.SkillingTool
import core.game.world.map.Location
import rs09.game.interaction.InteractionListener

class DramenTreeListener : InteractionListener() {

    val DRAMEN_TREE = 1292

    override fun defineListeners() {

        on(DRAMEN_TREE,SCENERY,"chop down"){ player, node ->
            val quest = player.questRepository.getQuest("Lost City")
            if (SkillingTool.getHatchet(player) == null) {
                player.getPacketDispatch().sendMessage("You do not have an axe which you have the level to use.")
                return@on true
            }
            if (quest.getStage(player) < 20) {
                return@on true
            }
            if (quest.getStage(player) == 20) {
                if (player.getAttribute("treeSpawned", false)) {
                    return@on true
                }
                val spirit = TreeSpiritNPC.create(655, Location.create(2862, 9734, 0)) as TreeSpiritNPC
                spirit.player = player
                spirit.isRespawn = false
                spirit.init()
                spirit.attack(player)
                player.setAttribute("treeSpawned", true)
                spirit.sendChat("You must defeat me before touching the tree!")
                return@on true
            }
            player.getPulseManager().run(GatheringSkillPulse(player, node as Scenery?))
            return@on true
        }

    }

}