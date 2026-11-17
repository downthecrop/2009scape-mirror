package content.region.misthalin.varrock.quest.gardenoftranquility

import content.data.Quests
import content.global.dialogue.GardenerDialoguePlugin
import content.global.skill.farming.Farmers
import content.global.skill.farming.PatchType
import core.api.getQuestStage
import core.api.getVarbit
import core.api.inEquipment
import core.api.setVarbit
import core.game.dialogue.ChatAnim
import core.game.dialogue.DialogueLabeller
import core.game.dialogue.DialogueOption
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.npc.NPC
import org.rs09.consts.Items

/**
 * Alain, the tree patch farmer near Taverley.
 */

class AlainDialogue : InteractionListener {
    override fun defineListeners() {
        on(intArrayOf(Farmers.ALAIN.id), IntType.NPC, "talk-to") { player, node ->
            DialogueLabeller.open(player, AlainDialogueFile(), node as NPC)
            return@on true
        }
    }
}

class AlainDialogueFile : DialogueLabeller() {

    override fun addConversation() {
        // Initial Topics
        options(
            // garden of tranquility quest
            DialogueOption("quest_cure", "I need to ask you about strong plant cures.") { player, _ ->
                val stage = getQuestStage(player, Quests.GARDEN_OF_TRANQUILITY)
                val varbit = getVarbit(player, GardenOfTranquility.VB_BURTHORPE_VINES)
                stage <= 40
                        && varbit == 3
            },

            // normal farming options
            DialogueOption("chop_tree", "Would you chop my tree down for me?", expression = ChatAnim.ASKING) { player, _ ->
                val patch = Farmers.ALAIN.patches[0].getPatchFor(player)
                patch.patch.type == PatchType.TREE_PATCH && patch.plantable != null && patch.isGrown()
            },
            DialogueOption("look_after", "Would you look after my crops for me?", expression = ChatAnim.ASKING) { player, _ ->
                val patch = Farmers.ALAIN.patches[0].getPatchFor(player)
                !(patch.patch.type == PatchType.TREE_PATCH && patch.plantable != null && patch.isGrown())
            },
            DialogueOption("advice", "Can you give me any farming advice?", expression = ChatAnim.ASKING),
            DialogueOption("sell", "Can you sell me something?", expression = ChatAnim.ASKING),
            DialogueOption("end", "That's all, thanks.")
        )

        // Standard Farming Topics
        label("chop_tree")
        exec { player, npc ->
            open(player, GardenerDialoguePlugin(player, 1000), npc)
        }

        label("look_after")
        exec { player, npc ->
            open(player, GardenerDialoguePlugin(player, 10), npc)
        }

        label("advice")
        exec { player, npc ->
            open(player, GardenerDialoguePlugin(player, 2000), npc)
        }

        label("sell")
        exec { player, npc ->
            open(player, GardenerDialoguePlugin(player, 30), npc)
        }

        // Garden of Tranquility
        label("quest_cure")
        npc("Well, then, you've come to the right person - I researched exotic plant diseases as part of my druidic training. What is the affliction that you need to cure?")
        player("Bernald has got some seriously diseased grapevines. I tried plant cure on them, but it didn't seem to have any positive effect.")
        npc(ChatAnim.ASKING, "Ah, so is this the famous Burthorpe Vine?")
        player("The very one.")
        npc(ChatAnim.SAD, "I'm afraid that my research did not extend to Burthorpe vine. Bernald and his ancestors have always closely guarded their heritage. In fact, I'm surprised that he has involved you.")
        player(ChatAnim.SUSPICIOUS, "Er, I did have some...assistance with persuading him.")
        npc("I see. Well, if it involves some unnatural power I would rather not know. I prefer the use of power that can be brought forth from the plants around us.")
        exec { player, _ ->
            if (inEquipment(player, Items.RING_OF_CHAROSA_6465)) {
                loadLabel(player, "charos_options")
            } else {
                loadLabel(player, "no_charos")
            }
        }

        label("charos_options")
        options(
            DialogueOption("suggest_solution", "Are you sure there's nothing you can suggest?", skipPlayer = true),
            DialogueOption("charm_reaction", "[Charm] You're hiding something from me.", 
                spokenText = "You're hiding something from me.")
        )

        label("no_charos")
        player("Are you sure there's nothing you can suggest?")
        goto("suggest_solution")

        label("suggest_solution")
        npc("Well...")
        npc("There is one thing you could try. One of the most potent cures I have discovered involves using the natural power held within a very special mineral, but it is a very closely guarded secret.")
        player("Do you mean the rune essence?")
        npc(ChatAnim.AMAZED, "You know of that?!? Then I can do no further harm here. You can make a potent plant cure by mixing some crumbled rune essence into a normal plant cure. This may possibly cure Bernald's vines.")
        player(ChatAnim.HAPPY, "Thank you Alain, I will try it out immediately!")
        exec { player, _ ->
            setVarbit(player, GardenOfTranquility.VB_ALAIN, 1, true)
        }
        goto("end")

        label("charm_reaction")
        npc("This is the power you are abusing!! I feel it now - it emanates from the ring you wear, a festering fountain of corruption that appalls my very soul!!!")
        player("Oh, sorry. I forgot that it only works on the weak-minded.")
        npc("You, @name, are the one who must be weak-minded, if you consider using such a dreadful weapon. Beware - it may one day be your downfall.")
        goto("end")
    }
}