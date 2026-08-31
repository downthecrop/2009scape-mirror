package content.region.misc.entrana.dialogue

import content.data.Quests
import content.region.kandarin.yanille.quest.thehandinthesand.quest.TheHandintheSand
import core.api.addItem
import core.api.freeSlots
import core.api.getQuestStage
import core.api.hasAnItem
import core.api.openDialogue
import core.api.setQuestStage
import core.game.dialogue.DialogueLabeller
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import org.rs09.consts.Items
import org.rs09.consts.NPCs

/**
 * Dialogue between Mazion and the player for The Hand in the Sand quest.
 * @author Edith
 */

@Initializable
class MazionDialogue(player: Player? = null) : DialoguePlugin(player) {
    override fun newInstance(player: Player): DialoguePlugin {
        return MazionDialogue(player)
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player, MazionDialogueFile(), npc)
        return false
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.MAZION_3114)
    }
}

class MazionDialogueFile : DialogueLabeller() {
    override fun addConversation() {

        exec { player, _ ->
            when (getQuestStage(player, Quests.THE_HAND_IN_THE_SAND)) {
                TheHandintheSand.STAGE_FIND_CLARENCE_70 -> loadLabel(player, "quest_stage_70")
                TheHandintheSand.STAGE_RETURN_HEAD_75 -> loadLabel(player, "quest_stage_75")
                else -> loadLabel(player, "default_dialogue")
            }
        }

        label("default_dialogue")
            exec { player, _ ->
                when ((1..3).random()) {
                    1 -> loadLabel(player, "nice_weather")
                    2 -> loadLabel(player, "fine_day")
                    else -> loadLabel(player, "parrot_banana")
                }
            }

        label("nice_weather")
            npc("Nice weather we're having today!")

        label("fine_day")
            npc("Hello, @name, fine day today!")

        label("parrot_banana")
            npc("Please leave me alone, a parrot stole my banana.")

        label("quest_stage_70")
            player(FacialExpression.HAPPY, "Hello there!")
            npc("Uh...greetings @name!")
            player(FacialExpression.WORRIED, "Uhh... How do you know my name?")
            npc(FacialExpression.HAPPY, "Oh, I like to keep ahead of things.")
            player(FacialExpression.ASKING, "Er.. ok. Well, I've been sent from the Wizards' Guild in Yanille. There's been an... incident... Do you have any body parts?")
            npc(FacialExpression.AMAZED_TALKING, "How did you know! I found the most awful thing in my sandpit - a head!")
            player(FacialExpression.HAPPY, "Ahhh good! I need to take it back to be buried!")
            npc(FacialExpression.AMAZED_TALKING, "You're very strange, but if it means I get rid of the horrid thing...")
            exec { player, _ ->
                if (freeSlots(player) < 1) {
                    loadLabel(player, "no_space_head")
                }
            }
            exec { player, _ ->
                addItem(player, Items.WIZARDS_HEAD_6957)
                setQuestStage(player, Quests.THE_HAND_IN_THE_SAND, TheHandintheSand.STAGE_RETURN_HEAD_75)
            }
            item(Item(Items.WIZARDS_HEAD_6957), "Mazion gives you the head.")

        label("no_space_head")
            npc("Or I would if you had any room in your bag!")

        label("quest_stage_75")
            exec { player, _ ->
                if (hasAnItem(player, Items.WIZARDS_HEAD_6957).exists()) {
                    loadLabel(player, "has_wizards_head")
                } else {
                    loadLabel(player, "lost_wizards_head")
                }
            }

        label("has_wizards_head")
            npc("I see you still have that head! Take it back to the Wizards in Yanille!")

        label("lost_wizards_head")
            player("I've lost my head!")
            exec { player, _ ->
                if (freeSlots(player) < 1) {
                    loadLabel(player, "no_space_lost_head")
                }
            }
            npc("Keep your hair on! You dropped it! Make sure you take it straight back to the wizards else you won't have a leg to stand on.")
            exec { player, _ ->
                addItem(player, Items.WIZARDS_HEAD_6957)
            }

        label("no_space_lost_head")
            npc("You dropped it and you don't have room to carry it now. Come back when you do.")

    }
}
