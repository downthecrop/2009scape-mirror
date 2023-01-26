package content.global.skill.gather

import core.game.node.Node
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import content.global.skill.fishing.FishingSpot
import content.global.skill.gather.fishing.FishingPulse
import content.global.skill.gather.mining.MiningSkillPulse
import content.global.skill.gather.woodcutting.WoodcuttingSkillPulse
import org.rs09.consts.NPCs
import content.region.misc.miscellania.dialogue.KjallakOnChopDialogue
import core.game.interaction.InteractionListener
import core.game.interaction.IntType

class GatheringSkillOptionListeners : InteractionListener {

    val ETCETERIA_REGION = 10300

    override fun defineListeners() {
        on(IntType.SCENERY,"chop-down","chop down","cut down","chop"){ player, node ->
            if(player.location.regionId == ETCETERIA_REGION){
                player.dialogueInterpreter.open(KjallakOnChopDialogue(), NPC(NPCs.CARPENTER_KJALLAK_3916))
                return@on true
            }
            player.pulseManager.run(WoodcuttingSkillPulse(player, node.asScenery()))
            return@on true
        }

        on(IntType.SCENERY,"mine"){ player, node ->
            player.pulseManager.run(MiningSkillPulse(player, node.asScenery()))
            return@on true
        }

        on(IntType.NPC,"net"){ player, node -> return@on fish(player,node,"net")}
        on(IntType.NPC,"lure"){ player, node -> return@on fish(player,node,"lure")}
        on(IntType.NPC,"bait"){ player, node -> return@on fish(player,node,"bait")}
        on(IntType.NPC,"harpoon"){ player, node -> return@on fish(player,node,"harpoon")}
        on(IntType.NPC,"cage"){ player, node -> return@on fish(player,node,"cage")}
        on(IntType.NPC,"fish"){ player, node -> return@on fish(player,node,"fish") }
    }

    fun fish(player: Player, node: Node, opt: String): Boolean{
        val npc = node as NPC
        val spot = FishingSpot.forId(npc.id) ?: return false
        val op = spot.getOptionByName(opt) ?: return false
        player.pulseManager.run(FishingPulse(player, npc, op))
        return true
    }
}
