package content.region.asgarnia.burthorpe.dialogue

import content.data.Quests
import content.region.misthalin.varrock.quest.gardenoftranquility.GardenOfTranquility
import core.api.*
import core.game.dialogue.ChatAnim
import core.game.dialogue.DialogueLabeller
import core.game.dialogue.DialogueOption
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.npc.NPC
import core.game.node.item.Item
import org.rs09.consts.Items
import org.rs09.consts.NPCs

/**
 * Bernald, the vine grower in Burthorpe.
 */

class BernaldDialoguePlugin : InteractionListener {
    override fun defineListeners() {
        on(intArrayOf(NPCs.BERNALD_2580), IntType.NPC, "talk-to") { player, node ->
            DialogueLabeller.open(player, BernaldDialogueFile(), node as NPC)
            return@on true
        }
    }
}

class BernaldDialogueFile : DialogueLabeller() {

    override fun addConversation() {
        exec { player, _ ->
            val stage = getQuestStage(player, Quests.GARDEN_OF_TRANQUILITY)
            if (stage > 40) {
                loadLabel(player, "post_quest")
            } else if (stage in 10..40) {
                // 0 before talking, 1 after talking, 2 after trying plant cure, 3 after talking again, 4 when vines cured
                val subStage = getVarbit(player, GardenOfTranquility.VB_BURTHORPE_VINES)
                val alain = getVarbit(player, GardenOfTranquility.VB_ALAIN)

                // check to see if the player has gotten/lost their seeds, and if they haven't planted them yet
                val gotSeeds = getVarbit(player, GardenOfTranquility.VB_VINE_SEEDS) == 1
                val seedsLost = !hasAnItem(player, Items.VINE_SEED_6456).exists()
                val vinePatchNotPlanted = getVarbit(player, GardenOfTranquility.VB_VINE_PATCH) < 4

                when (subStage) {
                    0 -> loadLabel(player, "substage_0")
                    1 -> loadLabel(player, "substage_1")
                    2 -> loadLabel(player, "substage_2")
                    3 -> {
                        if (alain == 0) {
                            loadLabel(player, "substage_3_no")
                        } else {
                            loadLabel(player, "substage_3_yes")
                        }
                    }
                    4 -> {
                        if (!gotSeeds) {
                            loadLabel(player, "substage_4")
                        } else if (seedsLost && vinePatchNotPlanted) {
                            loadLabel(player, "substage_4_lost")
                        } else {
                            loadLabel(player, "post_quest")
                        }
                    }
                }
            } else {
                loadLabel(player, "pre_quest")
            }
        }

        // before Garden of Tranquility
        label("pre_quest")
        npc(ChatAnim.WORRIED, "Do you know anything about grapevine diseases?")
        player(ChatAnim.FRIENDLY, "No, I'm afraid I don't.")
        npc(ChatAnim.GUILTY, "Oh, that's a shame. I hope I find someone soon, otherwise I could lose all of this year's crop.")
        goto("end")

        // after Garden of Tranquility
        label("post_quest")
        player("How are your grapes coming along?")
        npc("Marvellously, thanks to your help, @name.")
        goto("end")

        // during Garden of Tranquility
        label("substage_0")
        npc(ChatAnim.WORRIED, "Do you know anything about grapevine diseases?")
        player("I might - what's the problem?")
        npc("You see those grapevines over there?")
        player("Yes. They don't look too happy at the moment.")
        npc("They've come down with some strange disease that I've never encountered before. I've tried everything with them, but nothing seems to work. If it carries on like this I will lose all this year's crops.")
        player("I could probably help you with that, but I'd need something in return.")
        npc("Anything, you just need to ask!")
        player("Well, I'd like a supply of Burthorpe Vine seeds.")
        npc(ChatAnim.SAD, "I'm afraid I can't do that.")
        options(
            DialogueOption("substage_0_reject", "I thought you said anything!", skipPlayer = true),
            DialogueOption("substage_0_charm", "[Charm] But it is the only way that these vines will be cured.", 
                spokenText = "But it is the only way that these vines will be cured.") { player, _ ->
                inEquipment(player, Items.RING_OF_CHAROSA_6465)
            }
        )

        label("substage_0_reject")
        player("I thought you said anything!")
        npc("I'm afraid that is the one thing I cannot give you. Burthorpe vine has been bred over many years and many generations of my family. I could never give away something so special.")
        goto("end")

        label("substage_0_charm")
        npc("Hmm, you may be right. If I lose these crops what use are more seeds to me, when I have gone out of business? Very well then, this is the deal - cure my vines and I will give you some Burthorpe Vine seeds.")
        options(
            DialogueOption("substage_0_accept", "I accept the deal."),
            DialogueOption("substage_0_decline", "Actually, it seems like a lot of hard work for nothing.")
        )

        label("substage_0_accept")
        npc(ChatAnim.HAPPY, "Oh, thank you! Please speak to me again when you've cured the vines and, hence, saved my livelihood and an important part of Burthorpe's history.")
        exec { player, _ -> 
            setVarbit(player, GardenOfTranquility.VB_BURTHORPE_VINES, 1, true)
        }
        goto("end")

        label("substage_0_decline")
        npc("I wouldn't call the last living example of this rare plant 'nothing' if I were you. Very well, I shall find someone else to assist me.")
        goto("end")

        label("substage_1")
        player("Can you suggest any way that I can cure these grapevines?")
        npc("I thought you were the one with horticultural knowledge here. How would you normally cure plant diseases?")
        goto("end")

        // after unsuccessfully using regular plant cure on the vines
        label("substage_2")
        npc("Plant cure not doing the trick, eh?")
        player(ChatAnim.ASKING, "I don't understand - it's worked with everything else.")
        npc("That grapevine disease must be much stronger than the usual. You'll need something a bit more potent to cut out that infection.")
        player("Do you know of any stronger cures?")
        npc("No, I don't - but I know of a man who might. Not far from here there's a druid gardener who goes by the name of Alain. He's an expert on things like this, so they tell me.")
        player("Why didn't you just go and ask him yourself?")
        npc("These vines are very precious indeed. I could not hazard the risk that someone would steal my grapes while I was away - they may be diseased, but they still contain Burthorpe vine seeds.")
        exec { player, _ ->
            setVarbit(player, GardenOfTranquility.VB_BURTHORPE_VINES, 3, true)
        }
        goto("end")

        label("substage_3_no")
        npc("Have you spoken to Alain yet?")
        player("Not yet.")
        goto("end")

        label("substage_3_yes")
        npc("Have you spoken to Alain yet?")
        player("Yes. He has given me some knowledge so that I can make a more potent plant cure.")
        npc("Let's hope that it does the job, then.")
        goto("end")

        // cured the vines with super plant cure
        label("substage_4")
        npc(ChatAnim.HAPPY, "My vines are cured! You have worked a wonder here, @name! Here, you can have those seeds like I promised. Yes, here you go - I hope they grow for you.")
        item(Item(Items.VINE_SEED_6456), "Bernald gives you some Burthorpe Vine seeds.")
        exec { player, _ ->
            addItemOrDrop(player, Items.VINE_SEED_6456, 4)
            setVarbit(player, GardenOfTranquility.VB_VINE_SEEDS, 1, true)
        }
        goto("end")

        label("substage_4_lost")
        player("Do you have any more Burthorpe Vine seeds?")
        npc("What did you do with the ones I gave you?")
        player("Erm...")
        npc("Yes, here you go - I hope they grow for you.")
        item(Item(Items.VINE_SEED_6456), "Bernald gives you some Burthorpe Vine seeds.")
        exec { player, _ ->
            addItemOrDrop(player, Items.VINE_SEED_6456, 4)
        }
        goto("end")
    }
}