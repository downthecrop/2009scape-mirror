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
 * Lyra, the allotment patch farmer in Morytania.
 */

class LyraDialogue : InteractionListener {
    override fun defineListeners() {
        on(intArrayOf(Farmers.LYRA.id), IntType.NPC, "talk-to") { player, node ->
            DialogueLabeller.open(player, LyraDialogueFile(), node as NPC)
            return@on true
        }
    }
}

class LyraDialogueFile : DialogueLabeller() {

    override fun addConversation() {
        // Initial Topics
        options(
            // attributeLyraStage: 0 before agreeing, 1 after agreeing, 2 after planting and before talking, 3 after receiving seeds

            // before agreeing
            DialogueOption("quest_ask_seeds", "Do you have any orchid seeds to spare?", expression = ChatAnim.ASKING) { player, _ ->
                val stage = getQuestStage(player, Quests.GARDEN_OF_TRANQUILITY)
                val varbit = getVarbit(player, GardenOfTranquility.VB_LYRA_STAGE)
                stage <= 40
                        && varbit == 0
            },

            // after agreeing
            DialogueOption("quest_remind", "What did you ask me to do?", expression = ChatAnim.ASKING) { player, _ ->
                val stage = getQuestStage(player, Quests.GARDEN_OF_TRANQUILITY)
                val varbit = getVarbit(player, GardenOfTranquility.VB_LYRA_STAGE)
                stage <= 40
                        && varbit == 1
                        || (varbit == 2 && !hasGrownOnions(player))
            },

            // after planting but before talking
            DialogueOption("quest_onions_grown", "Okay, I've grown those onions like you asked.") { player, _ ->
                val stage = getQuestStage(player, Quests.GARDEN_OF_TRANQUILITY)
                val varbit = getVarbit(player, GardenOfTranquility.VB_LYRA_STAGE)
                stage <= 40
                        && varbit == 2
                        && hasGrownOnions(player)
            },

            // after getting the seeds, but having lost them
            DialogueOption("quest_more_seeds", "Can I have some more orchid seeds?") { player, _ ->
                val stage = getQuestStage(player, Quests.GARDEN_OF_TRANQUILITY)
                val varbit = getVarbit(player, GardenOfTranquility.VB_LYRA_STAGE)
                val orchidPatch1 = getVarbit(player, GardenOfTranquility.VB_PINK_ORCHID_PATCH)
                val orchidPatch2 = getVarbit(player, GardenOfTranquility.VB_YELLOW_ORCHID_PATCH)
                stage <= 40
                        && varbit == 3
                        && (orchidPatch1 <= 3 || orchidPatch2 <= 3) // one of the orchid patches isn't planted yet
                        && (!hasAnItem(player, Items.ORCHID_SEED_6458).exists() || !hasAnItem(player, Items.ORCHID_SEED_6459).exists())
            },

            // normal farming topics
            DialogueOption("chop_tree", "Would you chop my tree down for me?", expression = ChatAnim.ASKING) { player, _ ->
                val patch = Farmers.LYRA.patches[0].getPatchFor(player)
                patch.patch.type == PatchType.TREE_PATCH
                        && patch.plantable != null
                        && patch.isGrown()
            },
            DialogueOption("look_after", "Would you look after my crops for me?", expression = ChatAnim.ASKING) { player, _ ->
                val patch = Farmers.LYRA.patches[0].getPatchFor(player)
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
        npc(ChatAnim.SAD, "Please, do not bother me - I have problems to attend to.")
        options(
            DialogueOption("no_charos", "Oh sorry, I didn't mean to take up your valuable time.", skipPlayer = true),

            // option only shows if you have ring of charos (a) equipped
            DialogueOption("tell_problems", "[Charm] If you tell me your problems, I may be able to help you.",
                spokenText = "If you tell me your problems, I may be able to help you.") { player, npc ->
                inEquipment(player, Items.RING_OF_CHAROSA_6465)
            }
        )

        label("no_charos")
        player("Oh sorry, I didn't mean to take up your valuable time.")
        goto("end")

        label("tell_problems")
        npc(ChatAnim.SAD, "As you ask so nicely I will relate my problems to you, but I very much doubt that you may help me at all.")
        player(ChatAnim.NEUTRAL, "Try me.")
        npc(ChatAnim.NEUTRAL, "You must think it strange that a girl such as myself would choose to ply her trade in a place such as this, this darkest of lands.")
        options(
            DialogueOption("fit_in", "No, not really - you fit in here a treat."),
            DialogueOption("wondering", "[Charm] Yes, I was wondering about that.",
                spokenText = "Yes, I was wondering about that.",
                expression = ChatAnim.SUSPICIOUS)
        )

        label("fit_in")
        npc(ChatAnim.NEUTRAL, "Then begone simple fool and halt your churlish tongue. There are things about me that would leave you with nightmares for the rest of your life.")
        goto("end")

        label("wondering")
        npc(ChatAnim.SAD, "My mother, Alice, tends to our farming shop in yonder farmhouse, while I tend to this small allotment. Passing trade has quite literally died off in recent years, and with it our income.")
        options(
            DialogueOption("move", "I can only say one thing to you - move."),
            DialogueOption("hard_times", "[Charm] Times must be very hard for you.",
                spokenText = "Times must be very hard for you.",
                expression = ChatAnim.HALF_GUILTY)
        )

        label("move")
        npc(ChatAnim.NEUTRAL, "And leave behind all that we possess?!? Never!!!")
        goto("end")

        label("hard_times")
        npc(ChatAnim.SAD, "It is but the smallest of our worries. We get by, we survive, on the miserable harvests that I grow here. The price of our survival is what pains me, as it is a price I have paid with the souls of others.")
        options(
            DialogueOption("end", "Ah, it all makes sense now - you're a psychopath."),
            DialogueOption("just_cause", "[Charm] Whatever you've done, I'm sure you had just cause.",
                spokenText = "Whatever you've done, I'm sure you had just cause.")
        )

        label("just_cause")
        npc(ChatAnim.SAD, "You say this, but you know nothing of my crimes. I cannot tell you all, but this much I will say - that being allowed to exist in this land is a privilege that must be paid for, and the cost is dear.")
        options(
            DialogueOption("yawn", "*Yawn* Could you get to the point please?"),
            DialogueOption("nature_fee", "[Charm] And what is the nature of this fee that you pay?",
                spokenText = "And what is the nature of this fee that you pay?",
                expression = ChatAnim.ASKING)
        )

        label("yawn")
        npc(ChatAnim.NEUTRAL, "If your time is so pressing then leave me be and go about your business!")
        goto("end")

        label("nature_fee")
        npc(ChatAnim.NEUTRAL, "Mother and I must pay a tithe of blood to the vampires.", "Father would have protected us, but he is no longer", "with us, although his presence still walks among us in", "the fields. We pay with the blood of others.")
        options(
            DialogueOption("withdraw_help", "Er, can I withdraw my offer of help now?"),
            DialogueOption("whose_blood", "[Charm] If not yours, then whose blood are you offering?",
                spokenText = "If not yours, then whose blood are you offering?",
                expression = ChatAnim.ASKING)
        )

        label("withdraw_help")
        npc(ChatAnim.NEUTRAL, "I would not talk so if I were you - I know a secret call that will bring the vampires here right now, and then you may live only long enough to feel regret for your impertinence.")
        goto("end")

        label("whose_blood")
        npc(ChatAnim.NEUTRAL, "Sometimes I travel to market fairs in distant lands, and I use my feminine arts to lure young farmers back to this land; then the vampyres come for them and I see them no more.")
        options(
            DialogueOption("end", "Something tells me that you actually enjoy it, really."),
            DialogueOption("deal_guilt", "[Charm] How can you deal with so much guilt?",
                spokenText = "How can you deal with so much guilt?",
                expression = ChatAnim.ASKING)
        )

        label("deal_guilt")
        npc(ChatAnim.SAD, "Please - ask me no more. You said that you were looking for some orchid seeds? I do have some as a matter of fact, and you may as well have them because they do not grow well in this grim land.")
        player(ChatAnim.NEUTRAL, "Okay, can I have them then...")
        goto("test_first")

        label("test_first")
        npc(ChatAnim.NEUTRAL, "You must pass a test first. These are no ordinary flowers, and no ordinary farmer can grow them. You must grow onions here in one of these patches, and when they are fully grown I will give you the seeds.")
        options(
            DialogueOption("deal_agreed", "That's a deal - I'll grow a patch of onions for you."),
            DialogueOption("loitered_long", "I think not - I've loitered too long in this land."),
            DialogueOption("give_me_seeds", "[Charm] Oh for Saradomin's sake, just give me the seeds!",
                spokenText = "Oh for Saradomin's sake, just give me the seeds!")
        )

        label("deal_agreed")
        npc(ChatAnim.NEUTRAL, "Speak to me when they are fully grown. You need not pick them for me, I will be able to assess your skills by simply watching you.")
        exec { player, _ ->
            setVarbit(player, GardenOfTranquility.VB_LYRA_STAGE, 1, true)
        }
        goto("end")

        label("loitered_long")
        npc(ChatAnim.NEUTRAL, "Yes, I agree - far too long.")
        goto("end")

        label("give_me_seeds")
        npc(ChatAnim.NEUTRAL, "This feeling in my head - it is so strange - and it is a feeling I have felt before, although I cannot explain it. If you are trying to use some unnatural power over me, you have failed.")
        goto("test_first")

        label("quest_remind")
        npc(ChatAnim.NEUTRAL, "I need you to grow some onions in one of these vegetable patches, from seed until they are fully grown - then I will give you some orchid seeds.")
        goto("end")

        label("quest_onions_grown")
        npc(ChatAnim.NEUTRAL, "Yes, you have demonstrated that you have some moderate skills as a farmer. Take these seeds, and do with them that you will.")
        item(Item(Items.ORCHID_SEED_6458), "Lyra gives you some orchid seeds.")
        exec { player, _ ->
            addItemOrDrop(player, Items.ORCHID_SEED_6458, 3)
            addItemOrDrop(player, Items.ORCHID_SEED_6459, 3)
            setVarbit(player, GardenOfTranquility.VB_LYRA_STAGE, 3, true)
        }
        goto("end")

        label("quest_more_seeds")
        npc(ChatAnim.NEUTRAL, "Yes, here you go - I hope they grow for you.")
        item(Item(Items.ORCHID_SEED_6458), "Lyra gives you some orchid seeds.")
        exec { player, _ ->
            if (!hasAnItem(player, Items.ORCHID_SEED_6458).exists()) {
                addItemOrDrop(player, Items.ORCHID_SEED_6458, 3)
            }
            if (!hasAnItem(player, Items.ORCHID_SEED_6459).exists()) {
                addItemOrDrop(player, Items.ORCHID_SEED_6459, 3)
            }
        }
        goto("end")
    }

    private fun hasGrownOnions(player: Player): Boolean {
        val patches = Farmers.LYRA.patches
        val nwPlanted = getVarbit(player, GardenOfTranquility.VB_LYRA_NORTH) == 1
        val sePlanted = getVarbit(player, GardenOfTranquility.VB_LYRA_SOUTH) == 1

        val nwPatch = patches[0].getPatchFor(player)
        val sePatch = patches[1].getPatchFor(player)

        val nwGrown = nwPatch.plantable == Plantable.ONION_SEED && nwPatch.isGrown()
        val seGrown = sePatch.plantable == Plantable.ONION_SEED && sePatch.isGrown()

        return (nwPlanted && nwGrown) || (sePlanted && seGrown)
    }
}