package content.region.misthalin.varrock.quest.gardenoftranquility

import content.data.Quests
import content.global.dialogue.GardenerDialoguePlugin
import content.global.skill.farming.Farmers
import content.global.skill.farming.PatchType
import content.global.skill.farming.Plantable
import core.api.*
import core.game.dialogue.ChatAnim
import core.game.dialogue.DialogueLabeller
import core.game.dialogue.DialogueOption
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import org.rs09.consts.Items

/**
 * Kragen, the allotment patch farmer near Ardougne.
 */

class KragenDialogue : InteractionListener {
    override fun defineListeners() {
        on(intArrayOf(Farmers.KRAGEN.id), IntType.NPC, "talk-to") { player, node ->
            DialogueLabeller.open(player, KragenDialogueFile(), node as NPC)
            return@on true
        }
    }
}

class KragenDialogueFile : DialogueLabeller() {

    override fun addConversation() {
        // Initial Topics
        options(
            // VB_KRAGEN_STAGE: 0 before agreeing, 1 after agreeing, 2 after planting and before talking, 3 after receiving seeds

            // before agreeing
            DialogueOption("quest_ask_seeds", "Do you have any snowdrop seeds to spare?", expression = ChatAnim.ASKING) { player, _ ->
                val stage = getQuestStage(player, Quests.GARDEN_OF_TRANQUILITY)
                val varbit = getVarbit(player, GardenOfTranquility.VB_KRAGEN_STAGE)
                stage <= 40
                        && varbit == 0
            },

            // after agreeing
            DialogueOption("quest_remind", "What did you ask me to do?", expression = ChatAnim.ASKING) { player, _ ->
                val stage = getQuestStage(player, Quests.GARDEN_OF_TRANQUILITY)
                val varbit = getVarbit(player, GardenOfTranquility.VB_KRAGEN_STAGE)
                stage <= 40
                        && varbit == 1
                        || (varbit == 2 && !hasGrownCabbages(player))
            },

            // after planting but before talking
            DialogueOption("quest_cabbages_grown", "Okay, I've grown those cabbages like you asked.") { player, _ ->
                val stage = getQuestStage(player, Quests.GARDEN_OF_TRANQUILITY)
                val varbit = getVarbit(player, GardenOfTranquility.VB_KRAGEN_STAGE)
                stage <= 40
                        && varbit == 2
                        && hasGrownCabbages(player)
            },

            // after getting the seeds, but having lost them
            DialogueOption("quest_more_seeds", "Can I have some more snowdrop seeds?") { player, _ ->
                val stage = getQuestStage(player, Quests.GARDEN_OF_TRANQUILITY)
                val varbit = getVarbit(player, GardenOfTranquility.VB_KRAGEN_STAGE)
                val snowdropPatch = getVarbit(player, GardenOfTranquility.VB_SNOWDROP_PATCH)
                stage <= 40
                        && varbit == 3
                        && snowdropPatch <= 3 // snowdrops are not planted yet
                        && !hasAnItem(player, Items.SNOWDROP_SEED_6460).exists()
            },

            // normal farming topics
            DialogueOption("chop_tree", "Would you chop my tree down for me?", expression = ChatAnim.ASKING) { player, _ ->
                val patch = Farmers.KRAGEN.patches[0].getPatchFor(player)
                patch.patch.type == PatchType.TREE_PATCH
                        && patch.plantable != null
                        && patch.isGrown()
            },
            DialogueOption("look_after", "Would you look after my crops for me?", expression = ChatAnim.ASKING) { player, _ ->
                val patch = Farmers.KRAGEN.patches[0].getPatchFor(player)
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
        label("quest_ask_seeds")
        npc(ChatAnim.ANGRY, "No I don't, as it happens. Sorry.")
        options(
            DialogueOption("told_wrong", "But I was told that you did!", skipPlayer = true),

            // option only shows if you have ring of charos (a) equipped
            DialogueOption("charm_irritable", "[Charm] You seem to be a little irritable, my friend.",
                spokenText = "You seem to be a little irritable, my friend.") { player, _ ->
                inEquipment(player, Items.RING_OF_CHAROSA_6465)
            }
        )

        label("told_wrong")
        player("But I was told that you did!")
        npc(ChatAnim.ANGRY, "Well, you were told wrong then! Goodbye!")
        goto("end")

        label("charm_irritable")
        npc(ChatAnim.ANGRY, "Oh, you think so?!? And what concern is it of yours?")
        options(
            DialogueOption("not_concerned", "I'm not concerned, I just want you to give me some snowdrop seeds."),
            DialogueOption("charm_upset", "[Charm] I don't like to see a fellow human being so upset.",
                spokenText = "I don't like to see a fellow human being so upset.")
        )

        label("not_concerned")
        npc(ChatAnim.NEUTRAL, "Is that right?!? Well, I do actually have some snowdrop seeds, but you would be the last person I would give them to.")
        goto("end")

        label("charm_upset")
        npc(ChatAnim.NEUTRAL, "I must say, you don't often meet people who show concern the way you have done.")
        options(
            DialogueOption("cant_take_it", "I can't take it anymore - just give me the seeds."),
            DialogueOption("charm_ails", "[Charm] So what ails you, my friend?",
                spokenText = "So what ails you, my friend?")
        )

        label("cant_take_it")
        npc(ChatAnim.NEUTRAL, "And to think I listened to you! I was right after all - there is no kindness between human beings anymore.")
        goto("end")

        label("charm_ails")
        npc(ChatAnim.SAD, "All these people, asking me to look after their crops! And no-one ever asking me how I feel, or if there's something they can do for me!")
        options(
            DialogueOption("stop_whining", "Oh stop whining, it's not like you do it for free."),
            DialogueOption("charm_anything", "[Charm] Well, is there anything I can do for you?",
                spokenText = "Well, is there anything I can do for you?")
        )

        label("stop_whining")
        line("Kragen stares at you in silence, in such a manner that says to you that is probably best to end this conversation right now.")
        goto("end")

        label("charm_anything")
        npc(ChatAnim.HAPPY, "Oh, that's very kind of you! And here I was, thinking of giving it all up and heading off across the seas to find my fortune!")
        options(
            DialogueOption("stop_you", "Don't let me stop you, there are plenty of ships missing cabin boys."),
            DialogueOption("charm_what_can_i_do", "[Charm] So what can I do for you?",
                spokenText = "So what can I do for you?")
        )

        label("stop_you")
        npc(ChatAnim.NEUTRAL, "If you carry on speaking to me like that I might just put this spade to good use, and I'm not talking about digging!")
        goto("end")

        label("charm_what_can_i_do")
        npc(ChatAnim.NEUTRAL, "I'd like just for once for somebody to grow something for me. Just something simple; some vegetables would be nice.")
        player(ChatAnim.NEUTRAL, "How about some potatoes?")
        npc(ChatAnim.NEUTRAL, "Nah, there's a whole field of them over to the west. I don't have any particular use for onions, so how about cabbages, then?")
        player(ChatAnim.NEUTRAL, "Cabbages? Sounds good to me. Where do you want me to grow them?")
        npc(ChatAnim.NEUTRAL, "Oh, in one of these allotments here. Plant them from seed, and when they're ready for harvesting let me know, I'll give you your seeds.")
        goto("deal_options")

        label("deal_options")
        options(
            DialogueOption("deal", "That's a deal - I'll let you know when your cabbages are ready.", expression = ChatAnim.HAPPY),
            DialogueOption("no_time", "I'm not wasting my time growing cabbages."),
            DialogueOption("charm_hate", "[Charm] Aarrrrrghhhh! I hate cabbages!!!! Give me the seeds!!!!!",
                spokenText = "Aarrrrrghhhh! I hate cabbages!!!! Give me the seeds!!!!!")
        )

        label("deal")
        exec { player, _ ->
            setVarbit(player, GardenOfTranquility.VB_KRAGEN_STAGE, 1, true)
        }
        goto("end")

        label("no_time")
        npc(ChatAnim.NEUTRAL, "And I'm not wasting my seeds on an upstart like you!")
        goto("end")

        label("charm_hate")
        npc(ChatAnim.NEUTRAL, "Give ... you ... the ... yes I should ... no I won't ... Not after speaking to me like that you upstart!!! Is it a deal or not?!?")
        goto("deal_options")

        label("quest_remind")
        npc(ChatAnim.NEUTRAL, "You said that you would grow me some cabbages in one of these allotments. When they're ready for harvest come and talk to me, and I'll give you those snowdrop seeds that you're after.")
        goto("end")

        label("quest_cabbages_grown")
        npc(ChatAnim.HAPPY, "Yes, I saw - what fine farming prowess! Nobody has ever done anything like that for me before; here you go, these are the seeds you were after I believe.")
        npc(ChatAnim.NEUTRAL, "Yes, here you go - I hope they grow for you.")
        item(Item(Items.SNOWDROP_SEED_6460), "Kragen gives you some snowdrop seeds.")
        exec { player, _ ->
            addItemOrDrop(player, Items.SNOWDROP_SEED_6460, 4)
            setVarbit(player, GardenOfTranquility.VB_KRAGEN_STAGE, 3, true)
        }
        goto("end")

        label("quest_more_seeds")
        npc(ChatAnim.NEUTRAL, "Yes, here you go - I hope they grow for you.")
        item(Item(Items.SNOWDROP_SEED_6460), "Kragen gives you some snowdrop seeds.")
        exec { player, _ ->
            addItemOrDrop(player, Items.SNOWDROP_SEED_6460, 4)
        }
        goto("end")
    }

    private fun hasGrownCabbages(player: Player): Boolean {
        val patches = Farmers.KRAGEN.patches
        val nPlanted = getVarbit(player, GardenOfTranquility.VB_KRAGEN_NORTH) == 1
        val sPlanted = getVarbit(player, GardenOfTranquility.VB_KRAGEN_SOUTH) == 1

        val nPatch = patches[0].getPatchFor(player)
        val sPatch = patches[1].getPatchFor(player)

        val nGrown = nPatch.plantable == Plantable.CABBAGE_SEED && nPatch.isGrown()
        val sGrown = sPatch.plantable == Plantable.CABBAGE_SEED && sPatch.isGrown()

        return (nPlanted && nGrown) || (sPlanted && sGrown)
    }
}