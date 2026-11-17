package content.region.misthalin.varrock.quest.gardenoftranquility

import content.data.Quests
import content.global.dialogue.GardenerDialoguePlugin
import content.global.skill.farming.Farmers
import content.global.skill.farming.PatchType
import core.api.*
import core.game.dialogue.ChatAnim
import core.game.dialogue.DialogueLabeller
import core.game.dialogue.DialogueOption
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.npc.NPC
import org.rs09.consts.Items

/**
 * Dantaera, the allotment patch farmer near Catherby.
 */

class DantaeraDialogue : InteractionListener {
    override fun defineListeners() {
        on(intArrayOf(Farmers.DANTAERA.id), IntType.NPC, "talk-to") { player, node ->
            DialogueLabeller.open(player, DantaeraDialogueFile(), node as NPC)
            return@on true
        }
    }
}

class DantaeraDialogueFile : DialogueLabeller() {

    override fun addConversation() {
        // Initial Topics
        options(
            // VB_DANTAERA_STAGE: 0 before agreeing, 1 after agreeing, 2 after tree is cut for first time

            // before agreeing
            DialogueOption("quest_grow", "Do you know how I could grow a White Tree?", expression = ChatAnim.ASKING) { player, _ ->
                val stage = getQuestStage(player, Quests.GARDEN_OF_TRANQUILITY)
                val varbit = getVarbit(player, GardenOfTranquility.VB_DANTAERA_STAGE)
                stage <= 40
                        && varbit == 0
            },

            // after agreeing
            DialogueOption("quest_remind", "Can you remind me what we agreed about the White Tree?", expression = ChatAnim.ASKING) { player, _ ->
                val stage = getQuestStage(player, Quests.GARDEN_OF_TRANQUILITY)
                val varbit = getVarbit(player, GardenOfTranquility.VB_DANTAERA_STAGE)
                stage <= 40
                        && varbit == 1
            },

            // after cutting tree (lost the cutting)
            DialogueOption("quest_lost", "I have lost the cutting from the White Tree.") { player, _ ->
                val stage = getQuestStage(player, Quests.GARDEN_OF_TRANQUILITY)
                val varbit = getVarbit(player, GardenOfTranquility.VB_DANTAERA_STAGE)
                val treePatch = getVarbit(player, GardenOfTranquility.VB_WHITE_TREE_PATCH)
                stage <= 40
                        && varbit == 2
                        && treePatch <= 3 // tree is not planted yet

                        // has none of the tree cuttings
                        && !hasAnItem(player, Items.WHITE_TREE_SHOOT_6461).exists()
                        && !hasAnItem(player, Items.WHITE_TREE_SHOOT_6462).exists()
                        && !hasAnItem(player, Items.WHITE_TREE_SHOOT_6463).exists()
                        && !hasAnItem(player, Items.WHITE_TREE_SAPLING_6464).exists()
            },

            // after cutting tree (has the cutting)
            DialogueOption("quest_taken", "I have taken the the cutting from the White Tree.") { player, _ ->
                val stage = getQuestStage(player, Quests.GARDEN_OF_TRANQUILITY)
                val varbit = getVarbit(player, GardenOfTranquility.VB_DANTAERA_STAGE)
                val treePatch = getVarbit(player, GardenOfTranquility.VB_WHITE_TREE_PATCH)
                stage <= 40
                        && varbit == 2
                        && treePatch <= 3 // tree is not planted yet

                        // has one of the tree cuttings
                        && (hasAnItem(player, Items.WHITE_TREE_SHOOT_6461).exists()
                        || hasAnItem(player, Items.WHITE_TREE_SHOOT_6462).exists()
                        || hasAnItem(player, Items.WHITE_TREE_SHOOT_6463).exists()
                        || hasAnItem(player, Items.WHITE_TREE_SAPLING_6464).exists())
            },

            // normal farming options
            DialogueOption("chop_tree", "Would you chop my tree down for me?", expression = ChatAnim.ASKING) { player, _ ->
                val patch = Farmers.DANTAERA.patches[0].getPatchFor(player)
                patch.patch.type == PatchType.TREE_PATCH && patch.plantable != null && patch.isGrown()
            },
            DialogueOption("look_after", "Would you look after my crops for me?", expression = ChatAnim.ASKING) { player, _ ->
                val patch = Farmers.DANTAERA.patches[0].getPatchFor(player)
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
        label("quest_grow")
        npc(ChatAnim.NEUTRAL, "There are no more White Trees left in the world - they have all been chopped down, and their beauty has forever left this world.")
        options(
            DialogueOption("quest_over_end", "Then my quest is over - I cannot grow a White Tree.", skipPlayer = true),

            // option only shows if you have ring of charos (a) equipped
            DialogueOption("charm_transparent", "[Charm] I think that there is something that you are not telling me.",
                spokenText = "I think that there is something that you are not telling me.") { player, _ ->
                inEquipment(player, Items.RING_OF_CHAROSA_6465)
            }
        )

        label("quest_over_end")
        player("Then my quest is over - I cannot grow a White Tree.")
        npc(ChatAnim.NEUTRAL, "Even if there were a White Tree somewhere I would not tell you, for as soon as it would grow then it would be slaughtered in its prime. Man's hunger for fire is greater than his admiration of beauty.")
        goto("end")

        label("charm_transparent")
        npc(ChatAnim.SAD, "Am I so transparent? You have seen through me; there is something I would rather not tell you.")
        options(
            DialogueOption("chill_out", "You need to chill out and stop taking yourself so seriously."),
            DialogueOption("charm_secret", "[Charm] A secret is a dreadful burden to have to keep to yourself.",
                spokenText = "A secret is a dreadful burden to have to keep to yourself.")
        )

        label("chill_out")
        npc(ChatAnim.NEUTRAL, "Leave me in peace then, if you are so unable to understand my grief.")
        goto("end")

        label("charm_secret")
        npc(ChatAnim.SAD, "It is not hard to keep this secret - to give it away would cause me great sorrow.")
        player(ChatAnim.NEUTRAL, "I don't see how. Tell me your secret, and I will keep it as well as you have done.")
        npc(ChatAnim.SAD, "Very well - I feel deep trust in you for some reason. It is the White Tree, last survivor of a race of Trees that have disappeared from our landscape. Once they covered the land; now there is only one.")
        player(ChatAnim.NEUTRAL, "What happened to them?")
        npc(ChatAnim.SAD, "Man desired the wood of the White Tree, because of its wood of pure white. Need I tell you more?")
        player(ChatAnim.NEUTRAL, "I can fill in the rest. So where is the remaining White Tree?")
        npc(ChatAnim.SAD, "She stands atop Ice Mountain, alone and sorrowful; and, I fear, that she is dying.")
        player(ChatAnim.NEUTRAL, "If I took a cutting from ... her ... then I could grow another one.")
        npc(ChatAnim.SAD, "Yes, but she only has life enough to produce one shoot. Cut this shoot, and she will die, last of her kind gone forever. If you failed to grow this shoot, there would be no more White Trees.")
        options(
            DialogueOption("quit_worrying", "You should get out a bit more and quit worrying about trees."),
            DialogueOption("charm_allow", "[Charm] Unless you allow me to do this she will die anyway.",
                spokenText = "Unless you allow me to do this she will die anyway.")
        )

        label("quit_worrying")
        npc(ChatAnim.NEUTRAL, "No, I am afraid I cannot do that. She is the last White Tree, and I am the last to care about her. I will not let her pass away alone and unloved.")
        goto("end")

        label("charm_allow")
        npc(ChatAnim.SAD, "I know, but I try not to think about that. You speak truly - she does not have much time left.")
        player(ChatAnim.ASKING, "Then will you allow me to take this cutting?")
        npc(ChatAnim.NEUTRAL, "Do so with my blessing. I wish you good fortune, @name. Go now, and save the beauty of the White Tree.")
        exec { player, _ ->
            setVarbit(player, GardenOfTranquility.VB_DANTAERA_STAGE, 1, true)
        }
        goto("end")

        label("quest_remind")
        npc(ChatAnim.NEUTRAL, "The last of the White Trees stands dying atop Ice Mountain. I would greatly desire that you take her last cutting - although she is then certain to die - and cultivate her offspring")
        goto("end")

        label("quest_lost")
        npc(ChatAnim.NEUTRAL, "I do not believe what I am hearing, after everything you said to me before!")
        player(ChatAnim.NEUTRAL, "Er, yes I know ... sorry ...")
        npc(ChatAnim.NEUTRAL, "Maybe - just maybe - there might be another shoot somewhere on the Tree that you can take as a cutting. Go and have a look, before she dies without offspring!")
        goto("end")

        label("quest_taken")
        npc(ChatAnim.NEUTRAL, "Then it is done now, whatever happens. Though I am eager for offspring to be planted I am doubly sorrowful that she will now die.")
        player(ChatAnim.NEUTRAL, "Don't worry - this White Tree cutting is safe with me.")
        goto("end")
    }
}