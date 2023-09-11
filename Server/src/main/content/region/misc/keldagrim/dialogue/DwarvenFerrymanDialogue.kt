package content.region.misc.keldagrim.dialogue

import core.api.*
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.dialogue.Topic
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.map.Location
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs09.consts.Items
import org.rs09.consts.NPCs

@Initializable
class DwarvenFerrymanForthDialogue(player: Player? = null) : DialoguePlugin(player) {
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            START_DIALOGUE -> {
                if (!getAttribute(player, "/save:keldagrim-river-forth-crossed", false)) {
                    npcl(FacialExpression.OLD_DEFAULT, "Hello there, want a ride?").also { stage++ }
                } else {
                    npcl(FacialExpression.OLD_DEFAULT, "Want to cross the river? It's just 2 gold pieces!").also { stage = 6 }
                }
            }
            1 -> playerl(FacialExpression.FRIENDLY, "A ride, where to?").also { stage++ }
            2 -> npcl(FacialExpression.OLD_DEFAULT, "Across the river, across the river! It's just a short ride!").also { stage++ }
            3 -> playerl(FacialExpression.FRIENDLY, "How much will that cost me?").also { stage++ }
            4 -> npcl(FacialExpression.OLD_DEFAULT, "Haha, you understand us dwarves well, human!").also { stage++ }
            5 -> npcl(FacialExpression.OLD_DEFAULT, "It's just 2 gold pieces! Want to go?").also { stage++ }
            6 -> showTopics(
                    Topic(FacialExpression.FRIENDLY, "Yes please.", 7),
                    Topic(FacialExpression.FRIENDLY, "No thanks.", END_DIALOGUE),
            )
            7 -> {
                if (!inInventory(player, Items.COINS_995, 2)) {
                    npcl(FacialExpression.OLD_DEFAULT, "You don't even have 2 gold coins, humans!").also { stage++ }
                } else {
                    if (removeItem(player, Item(Items.COINS_995, 2))) {
                        setAttribute(player, "/save:keldagrim-river-forth-crossed", true)
                        end()
                        teleport(player, Location.create(2836, 10143, 0))
                    }
                }
            }
            8 -> npcl(FacialExpression.OLD_DEFAULT, "Come back later.").also {
                stage = END_DIALOGUE
            }
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return DwarvenFerrymanForthDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.DWARVEN_FERRYMAN_1843)
    }
}

// https://youtu.be/KI_f24g0Xl0
@Initializable
class DwarvenFerrymanBackDialogue(player: Player? = null) : DialoguePlugin(player) {
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            START_DIALOGUE -> {
                if (!getAttribute(player, "/save:keldagrim-river-back-crossed", false)) {
                    npcl(FacialExpression.OLD_DEFAULT, "Hello, want to cross the river?").also { stage++ }
                } else {
                    npcl(FacialExpression.OLD_DEFAULT, "Want me to take you across? It's just 2 gold pieces!").also { stage = 4 }
                }
            }
            1 -> playerl(FacialExpression.FRIENDLY, "Are you going to charge me too?").also { stage++ }
            2 -> npcl(FacialExpression.OLD_DEFAULT, "Are you kidding?").also { stage++ }
            3 -> npc(FacialExpression.OLD_DEFAULT, "Of course I am! But it's just 2 gold coins, do you", "want to go?").also { stage++ }
            4 -> showTopics(
                    Topic(FacialExpression.FRIENDLY, "Yes please.", 5),
                    Topic(FacialExpression.FRIENDLY, "No thanks.", END_DIALOGUE),
            )
            5 -> {
                if (!inInventory(player, Items.COINS_995, 2)) {
                    npcl(FacialExpression.OLD_DEFAULT, "You don't even have 2 gold coins, humans!").also { stage++ }
                } else {
                    if (removeItem(player, Item(Items.COINS_995, 2))) {
                        end()
                        setAttribute(player, "/save:keldagrim-river-back-crossed", true)
                        teleport(player, Location.create(2864, 10133, 0))
                    }
                }
            }
            6 -> npcl(FacialExpression.OLD_DEFAULT, "Come back later.").also { stage++ }
            7 -> playerl(FacialExpression.FRIENDLY, "But... that means I'm stuck here.").also { stage++ }
            8 -> npcl(FacialExpression.OLD_DEFAULT, "Hmm. I suppose I could make an exception for you this time.").also { stage++ }
            9 -> playerl(FacialExpression.FRIENDLY, "Thanks a lot!").also { stage++ }
            10 -> {
                end()
                setAttribute(player, "/save:keldagrim-river-back-crossed", true)
                teleport(player, Location.create(2864, 10133, 0))
            }
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return DwarvenFerrymanBackDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.DWARVEN_FERRYMAN_1844)
    }
}