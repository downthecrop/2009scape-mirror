package content.global.dialogue

import content.global.skill.farming.FarmerPayOptionDialogue
import content.global.skill.farming.Farmers
import content.global.skill.farming.FarmingPatch
import content.global.skill.farming.PatchType
import core.api.*
import core.game.dialogue.FacialExpression
import core.game.dialogue.IfTopic
import core.game.dialogue.Topic
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import org.rs09.consts.Items
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE

@Initializable
class GardenerDialoguePlugin(player: Player? = null) : core.game.dialogue.DialoguePlugin(player) {
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        val patches = Farmers.forId(npc.id)!!.patches
        when (stage) {
            // TODO: Can fruit trees be chopped down by the gardener too?
            START_DIALOGUE -> {
                val patch = patches[0].getPatchFor(player)
                showTopics(
                    IfTopic(
                        FacialExpression.ASKING,
                        "Would you chop my tree down for me?",
                        1000,
                        patch.patch.type == PatchType.TREE_PATCH && patch.plantable != null && patch.isGrown()
                    ),
                    IfTopic(
                        FacialExpression.ASKING,
                        "Would you look after my crops for me?",
                        10,
                        !(patch.patch.type == PatchType.TREE_PATCH && patch.plantable != null && patch.isGrown())
                    ),
                    Topic(FacialExpression.ASKING, "Can you give me any farming advice?", 2000),
                    Topic(FacialExpression.ASKING, "Can you sell me something?", 30),
                    Topic(FacialExpression.NEUTRAL, "That's all, thanks.", END_DIALOGUE)
                )
            }

            10 -> {
                if (patches.size > 1) {
                    npc("I might. Which one were you thinking of?").also { stage = 20 }
                } else {
                    openPayGardenerDialogue(player, patches[0])
                }
            }

            20 -> when (npc.id) {
                Farmers.ELSTAN.id, Farmers.LYRA.id -> showTopics(
                    Topic(FacialExpression.NEUTRAL, "The north-western allotment.", 21),
                    Topic(FacialExpression.NEUTRAL, "The south-eastern allotment.", 22)
                )
                Farmers.DANTAERA.id, Farmers.KRAGEN.id -> showTopics(
                    Topic(FacialExpression.NEUTRAL, "The northern allotment.", 21),
                    Topic(FacialExpression.NEUTRAL, "The southern allotment.", 22)
                )
            }
            21 -> openPayGardenerDialogue(player, patches[0])
            22 -> openPayGardenerDialogue(player, patches[1])

            30 -> npc(FacialExpression.NEUTRAL, "That depends on whether I have it to sell. What is it", "that you're looking for?").also { stage++ }
            31 -> showTopics(
                Topic(FacialExpression.NEUTRAL, "Some plant cure.", 100),
                Topic(FacialExpression.NEUTRAL, "A bucket of compost.", 200),
                Topic(FacialExpression.NEUTRAL, "A rake.", 300),
                Topic("(See more items)", 32, true)
            )
            32 -> showTopics(
                Topic(FacialExpression.NEUTRAL, "A watering can.", 400),
                Topic(FacialExpression.NEUTRAL, "A gardening trowel.", 500),
                Topic(FacialExpression.NEUTRAL, "A seed dibber.", 600),
                Topic("(See previous items)", 31, true),
                Topic(FacialExpression.NEUTRAL, "Forget it.", 40, true)
            )

            40 -> player("Forget it, you don't have anything I need.").also { stage = END_DIALOGUE }

            100 -> npc("Plant cure, eh? I might have some put aside for myself.", "Tell you what. I'll sell you some plant cure for 25 gp if", "you like.").also { stage++ }
            101 -> options("Yes, that sounds like a fair price.", "No thanks, I can get that much cheaper elsewhere.").also { stage++ }
            102 -> when (buttonId) {
                1 -> {
                    player(FacialExpression.HAPPY, "Yes, that sounds like a fair price.").also { stage = END_DIALOGUE }
                    if (removeItem(player, Item(Items.COINS_995, 25))) {
                        addItemOrDrop(player, Items.PLANT_CURE_6036)
                    } else {
                        sendMessage(player, "You need 25 gp to pay for that.")
                    }
                }
                2 -> player("No thanks, I can get that much cheaper elsewhere.").also { stage = END_DIALOGUE }
            }

            200 -> npc("A bucket of compost, eh? I might have one spare...", "tell you what, I'll sell it to you for 35 gp if you like.").also { stage++ }
            201 -> options("Yes, that sounds like a fair price.", "No thanks, I can get that much cheaper elsewhere.").also { stage++ }
            202 -> when (buttonId) {
                1 -> {
                    player("Yes, that sounds like a fair price.").also { stage = END_DIALOGUE }
                    if (removeItem(player, Item(Items.COINS_995, 35))) {
                        addItemOrDrop(player, Items.COMPOST_6032)
                    } else {
                        sendMessage(player, "You need 35 gp to pay for that.")
                    }
                }
                2 -> player("No thanks, I can get that much cheaper elsewhere.").also { stage = END_DIALOGUE }
            }

            300 -> npc("A rake, eh? I might have one spare...", "tell you what, I'll sell it to you for 15 gp if you like.").also { stage++ }
            301 -> options("Yes, that sounds like a fair price.", "No thanks, I can get that much cheaper elsewhere.").also { stage++ }
            302 -> when (buttonId) {
                1 -> {
                    player("Yes, that sounds like a fair price.").also { stage = END_DIALOGUE }
                    if (removeItem(player, Item(Items.COINS_995, 15))) {
                        addItemOrDrop(player, Items.RAKE_5341)
                    } else {
                        sendMessage(player, "You need 15 gp to pay for that.")
                    }
                }
                2 -> player("No thanks, I can get that much cheaper elsewhere.").also { stage = END_DIALOGUE }
            }

            400 -> npc("A watering can, eh? I might have one spare...", "tell you what, I'll sell it to you for 25 gp if you like.").also { stage++ }
            401 -> options("Yes, that sounds like a fair price.", "No thanks, I can get that much cheaper elsewhere.").also { stage++ }
            402 -> when(buttonId){
                1 -> {
                    player("Yes, that sounds like a fair price.").also { stage = END_DIALOGUE }
                    if (removeItem(player, Item(Items.COINS_995, 25))) {
                        addItemOrDrop(player, Items.WATERING_CAN8_5340)
                    } else {
                        sendMessage(player, "You need 25 gp to pay for that.")
                    }
                }
                2 -> player("No thanks, I can get that much cheaper elsewhere.").also { stage = END_DIALOGUE }
            }

            500 -> npc("A gardening trowel, eh? I might have one spare...", "tell you what, I'll sell it to you for 15 gp if you like.").also { stage++ }
            501 -> options("Yes, that sounds like a fair price.", "No thanks, I can get that much cheaper elsewhere.").also { stage++ }
            502 -> when (buttonId) {
                1 -> {
                    player("Yes, that sounds like a fair price.").also { stage = END_DIALOGUE }
                    if (removeItem(player, Item(Items.COINS_995, 15))) {
                        addItemOrDrop(player, Items.GARDENING_TROWEL_5325)
                    } else {
                        sendMessage(player, "You need 15 gp to pay for that.")
                    }
                }
                2 -> player("No thanks, I can get that much cheaper elsewhere.").also { stage = END_DIALOGUE }
            }

            600 -> npc("A seed dibber, eh? I might have one spare...", "tell you what, I'll sell it to you for 15 gp if you like.").also { stage++ }
            601 -> options("Yes, that sounds like a fair price.", "No thanks, I can get that much cheaper elsewhere.").also { stage++ }
            602 -> when (buttonId) {
                1 -> {
                    player("Yes, that sounds like a fair price.").also { stage = END_DIALOGUE }
                    if (removeItem(player, Item(Items.COINS_995, 15))) {
                        addItemOrDrop(player, Items.SEED_DIBBER_5343)
                    } else {
                        sendMessage(player, "You need 15 gp to pay for that.")
                    }
                }
                2 -> player("No thanks, I can get that much cheaper elsewhere.").also { stage = END_DIALOGUE }
            }

            // Note: This dialogue changes slightly in April 2009, and significantly in December 2009
            1000 -> npc(FacialExpression.THINKING, "Why? You look like you could chop it down yourself!").also { stage++ }
            1001 -> showTopics(
                Topic(FacialExpression.NEUTRAL, "Yes, you're right - I'll do it myself.", END_DIALOGUE),
                Topic(FacialExpression.NEUTRAL, "I can't be bothered - I'd rather pay you to do it.", 1020)
            )

            1020 -> npc(FacialExpression.NEUTRAL, "Well, it's a lot of hard work - if you pay me 200 GP", "I'll chop it down for you.").also { stage++ }
            1021 -> {
                if (inInventory(player, Items.COINS_995, 200)) {
                    showTopics(
                        Topic(FacialExpression.NEUTRAL, "Here's 200GP - chop my tree down please.", 1022),
                        Topic(FacialExpression.NEUTRAL, "I don't want to pay that much, sorry.", END_DIALOGUE)
                    )
                } else {
                    player("I don't have that much money on me.").also { stage = END_DIALOGUE } // not authentic
                }
            }
            1022 -> {
                end()
                if (removeItem(player, Item(Items.COINS_995, 200))) {
                    patches[0].getPatchFor(player).clear()
                }
            }

            2000 -> {
                val advice = arrayOf(
                    "There are four main Farming areas - Elstan looks after an area south of Falador, Dantaera has one to the north of Catherby, Kragen has one near Ardougne, and Lyra looks after a place in north Morytania.",
                    "If you want to grow fruit trees you could try a few places: Catherby and Brimhaven have a couple of fruit tree patches, and I hear that the gnomes are big on that sort of thing.",
                    "Bittercap mushrooms can only be grown in a special patch in Morytania, near the Mort Myre swamp. There the ground is especially dank and suited to growing poisonous fungi.",
                    "There is a special patch for growing Belladonna - I believe it's somewhere near Draynor Manor, where the ground is a tad 'unblessed'.",

                    "Don't just throw away your weeds after you've raked a patch - put them in a compost bin and make some compost.",
                    "Applying compost to a patch will not only reduce the chance that your crops will get diseased, but you will also grow more crops to harvest.",
                    "Supercompost is far better than normal compost, but more expensive to make. You need to rot the right type of item; show me an item, and I'll tell you if it's super-compostable or not.",

                    "Tree seeds must be grown in a plantpot of soil into a sapling, and then transferred to a tree patch to continue growing to adulthood.",
                    "You don't have to buy all your plantpots you know, you can make them yourself on a pottery wheel. If you're a good enough ${if (player!!.isMale) "craftsman" else "craftswoman"}, that is.",
                    "You can fill plantpots with soil from Farming patches, if you have a gardening trowel.",

                    "Vegetables, hops and flowers are far more likely to grow healthily if you water them periodically.",
                    "The only way to cure a bush or tree of disease is to prune away the diseased leaves with a pair of secateurs. For all other crops I would just apply some plant-cure.",
                    "If you need to be rid of your fruit trees for any reason, all you have to do is chop them down and then dig up the stump.",

                    "You can put up to ten potatoes, cabbages or onions in vegetable sacks, although you can't have a mix in the same sack.",
                    "You can put up to five tomatoes, strawberries, apples, bananas or oranges into a fruit basket, although you can't have a mix in the same basket.",
                    "If you want to make your own sacks and baskets you'll need to use the loom that's near the Farming shop in Falador. If you're a good enough ${if (player!!.isMale) "craftsman" else "craftswoman"}, that is.",
                    "You can buy all the farming tools from farming shops, which can be found close to the allotments.",

                    "Hops are good for brewing ales. I believe there's a brewery up in Keldagrim somewhere, and I've heard rumours that a place called Phasmatys used to be good for that type of thing. 'Fore they all died, of course.",
                )
                npcl(FacialExpression.NEUTRAL, advice.random()).also { stage = START_DIALOGUE }
            }
        }
        return true
    }

    fun openPayGardenerDialogue(player: Player, fPatch: FarmingPatch) {
        end()
        openDialogue(player, FarmerPayOptionDialogue(fPatch.getPatchFor(player)), npc)
    }

    override fun getIds(): IntArray {
        return Farmers.values().map(Farmers::id).toIntArray()
    }

}