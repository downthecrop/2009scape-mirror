package rs09.game.content.dialogue.region.dorgeshuun
import api.*
import core.game.content.dialogue.DialoguePlugin
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.plugin.Initializable
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import java.text.NumberFormat

/**
 * @author qmqz
 */

@Initializable
class BarlakDialogue(player: Player? = null) : DialoguePlugin(player){
    private var pask = FacialExpression.ASKING
    private var pfr = FacialExpression.FRIENDLY
    private var pneu = FacialExpression.NEUTRAL
    private var nhap = FacialExpression.OLD_HAPPY
    private var ntalk1 = FacialExpression.OLD_CALM_TALK1
    private var ntalk2 = FacialExpression.OLD_CALM_TALK2

    private var curItem = 0

    val sets = arrayOf(
        intArrayOf(Items.LONG_BONE_10976, 1000, 1500, Skills.CONSTRUCTION),
        intArrayOf(Items.CURVED_BONE_10977, 2000, 2250, Skills.CONSTRUCTION),
        intArrayOf(Items.SNAIL_SHELL_7800, 600, 0, Skills.CONSTRUCTION),
        intArrayOf(Items.PERFECT_SNAIL_SHELL_10996, 600, 500, Skills.CRAFTING),
        intArrayOf(Items.TORTOISE_SHELL_7939, 600, 0, Skills.CRAFTING),
        intArrayOf(Items.PERFECT_SHELL_10995, 600, 500, Skills.CRAFTING),
    )

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        val playerMeetsBoneReqs = getDynLevel(player, Skills.CONSTRUCTION) >= 30 && hasHouse(player)
        if (!playerMeetsBoneReqs) {
            sendMessage(player, "You require at least level 30 construction and a house to speak to Barlak about bones.")
        }
        if (playerMeetsBoneReqs && inInventory(player, Items.LONG_BONE_10976, 1)) {
            curItem = 0
            npcl(nhap, "Those bones! Those are exactly the sort of thing I need! Will you sell them?").also { stage = 0 }
        } else if (playerMeetsBoneReqs && inInventory(player, Items.CURVED_BONE_10977, 1)) {
            curItem = 1
            npcl(nhap, "Those bones! Those are exactly the sort of thing I need! Will you sell them?").also { stage = 0 }
        } else if (inInventory(player, Items.PERFECT_SHELL_10995, 1)) {
            curItem = 5
            npc(ntalk2, "That giant shell... what is it?").also { stage = 120 }
        } else if (inInventory(player, Items.TORTOISE_SHELL_7939, 1)) {
            curItem = 4
            npc(ntalk2, "That giant shell... what is it?").also { stage = 100 }
        } else if (inInventory(player, Items.PERFECT_SNAIL_SHELL_10996, 1)) {
            curItem = 3
            npc(ntalk1, "That giant shell... what is it?").also { stage = 60 }
        } else if (inInventory(player, Items.SNAIL_SHELL_7800, 1)) {
            curItem = 2
            npc(ntalk1, "That giant shell... what is it?").also { stage = 50 }
        } else if (playerMeetsBoneReqs) {
            npc(nhap, "Bones!").also { stage = 150 }
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> npcl(ntalk1, "I'll give you ${getTotalCoinsForCurItem()} gp for the bones you're carrying and for any you have in your bank.").also { stage++ }
            1 -> npcl(ntalk2, "I'll try to teach you something about Construction as well, but it's highly technical so you won't understand if you don't already have level 30 Construction.").also { stage++ }
            2 -> options("Okay.", "No, I'll keep the bones.").also { stage++ }
            3 -> when (buttonId) {
                1 -> player(pfr, "Okay.").also { stage = 10 }
                2 -> player(pneu, "No thanks.").also { stage = 99 }
            }

            10 -> {
                exchange()
                npc(ntalk1, "Thanks! Now, let me explain...").also { stage = 999 }
            }

            50 -> player(pfr, "It's a giant snail shell!").also { stage++ }
            51 -> npcl(ntalk1, "Hmm... I think I might be able to make something out of them.").also { stage++ }
            52 -> npcl(ntalk2, "I'll give you ${getTotalCoinsForCurItem()} gp for the snail shells you're carrying.").also { stage++ }
            53 -> options("Okay.", "No. I'll keep the bones.").also { stage++ }
            54 -> when (buttonId) {
                1 -> player(pfr, "Okay.").also { stage = 55 }
                2 -> player(pneu, "No thanks.").also { stage = 99 }
            }
            55 -> {
                exchange()
                npcl(ntalk1, "Thanks. If you find any more shells like these, please bring them to me!").also { stage = 99 }
            }
            60 -> player(pfr, "It's a giant snail shell!").also { stage++ }
            61 -> {
                when (amountInInventory(player, Items.PERFECT_SNAIL_SHELL_10996)) {
                    1 -> npcl(ntalk2, "Hmm... I think I might be able to make something out of them, especially that perfect one.").also { stage++ }
                    else -> npcl(ntalk1, "Hmm... I think I might be able to make something out of them, especially those perfect ones.").also { stage = 70 }
                }
            }

            62 -> npcl(ntalk2, "I'll give you ${getTotalCoinsForCurItem()} gp for the snail shell you're carrying, and I'll try to give you some advice on Crafting while I'm at it.").also { stage++ }
            63 -> options("Okay.", "No. I'll keep the bones.").also { stage++ }
            64 -> when (buttonId) {
                1 -> player(pfr, "Okay.").also { stage = 65 }
                2 -> player(pneu, "No thanks.").also { stage = 99 }
            }
            65 -> {
                npcl(ntalk2, "Thanks! Now, I don't think I could use that for Construction, but maybe I could make something...").also { stage = 1000 }
                exchange()
            }

            70 -> npcl(ntalk2, "I'll give you ${getTotalCoinsForCurItem()} gp for the snail shells you're carrying, and I'll try to give you some advice on Crafting while I'm at it.").also { stage++ }
            71 -> options("Okay.", "No. I'll keep the bones.").also { stage++ }
            72 -> when (buttonId) {
                1 -> player(pfr, "Okay.").also { stage = 75 }
                2 -> player(pneu, "No thanks.").also { stage = 99 }
            }
            75 -> {
                npcl(ntalk2, "Thanks! Now, I don't think I could use that for Construction, but maybe I could make something...").also { stage = 1000 }
                exchange()
            }

            100 -> player(pfr, "It's a giant battle tortoise shell!").also { stage++ }
            101 -> npcl(ntalk1, "Hmm... I think I might be able to make something out of them.").also { stage++ }
            102 -> npcl(ntalk2, "I'll give you ${getTotalCoinsForCurItem()} gp for the tortoise shells you're carrying.").also { stage++ }
            103 -> options("Okay.", "No. I'll keep the bones.").also { stage++ }
            104 -> when (buttonId) {
                1 -> player(pfr, "Okay.").also { stage = 105 }
                2 -> player(pneu, "No thanks.").also { stage = 99 }
            }
            105 -> {
                npcl(ntalk2, "Thanks. If you find any more shells like these, please bring them to me!").also { stage = 130 }
                exchange()
            }

            120 -> player(pfr, "It's a perfect shell!").also { stage++ }
            121 -> {
                when (amountInInventory(player, Items.PERFECT_SHELL_10995)) {
                    1 -> npcl(ntalk2, "Hmm... I think I might be able to make something out of them, especially that perfect one.").also { stage++ }
                    else -> npcl(ntalk1, "Hmm... I think I might be able to make something out of them, especially those perfect ones.").also { stage++ }
                }
            }
            122 -> npcl(ntalk2, "I'll give you ${getTotalCoinsForCurItem()} gp for the perfect shell you're carrying, and I'll try to give you some advice on Crafting while I'm at it.").also { stage++ }
            123 -> options("Okay.", "No. I'll keep the bones.").also { stage++ }
            124 -> when (buttonId) {
                1 -> player(pfr, "Okay.").also { stage = 125 }
                2 -> player(pneu, "No thanks.").also { stage = 99 }
            }
            125 -> {
                npcl(ntalk2, "Thanks! Now, I don't think I could use that for Construction, but maybe I could make something...").also { stage = 1001 }
                exchange()
            }

            130 -> {
                if (inInventory(player, Items.SNAIL_SHELL_7800, 1)) {
                    curItem = 2
                    npc(ntalk1, "That giant shell... what is it?").also { stage = 50 }
                } else if (inInventory(player, Items.PERFECT_SNAIL_SHELL_10996, 1)) {
                    curItem = 3
                    npc(ntalk1, "That giant shell... what is it?").also { stage = 60 }
                } else {
                    end()
                }
            }

            150 -> player(FacialExpression.THINKING, "What?").also { stage++ }
            151 -> npc(FacialExpression.OLD_DISTRESSED, "I don't have any bones!").also { stage++ }
            152 -> options("Then how do you stand up?", "What do you need bones for?", "Will you buy anything besides bones?", "What kind of bones do you need?", "Goodbye").also { stage++ }
            153 -> when (buttonId) {
                1 -> player(FacialExpression.THINKING, "Then how do you stand up?").also { stage = 200 }
                2 -> player(FacialExpression.THINKING, "What do you need bones for?").also { stage = 220 }
                3 -> player(FacialExpression.THINKING, "Will you buy anything besides bones?").also { stage = 240 }
                4 -> player(FacialExpression.THINKING, "What kind of bones do you need?").also { stage = 260 }
                5 -> player(pfr, "Goodbye.").also { stage = 280 }
            }

            200 -> npc(FacialExpression.OLD_CALM_TALK1, "What?").also { stage++ }
            201 -> playerl(FacialExpression.FRIENDLY, "How do you stand up if you have no bones? Shouldn't you collapse into a gelatinous blob?").also { stage++ }
            202 -> npc(FacialExpression.OLD_LAUGH1, "Ha, ha, ha, ha, ha!").also { stage = 152 }

            220 -> npcl(FacialExpression.OLD_LAUGH1, "To stand up properly. Otherwise I'd collapse into a gelatinous blob! Ha, ha, ha, ha, ha! No, seriously, I need bones to use as a Construction material.").also { stage++ }
            221 -> npcl(ntalk1, "We always need big bones to prop up the mine shafts and to make other temporary structures.").also { stage = 152 }
            240 -> npcl(ntalk2, "Well, I've had a few people bring me some interesting giant shells. They say they came from giant snails and giant tortoises. Of course, like the bones, some shells are better than others.").also { stage++ }
            241 -> npcl(ntalk1, "I'll give you 250gp for ordinary shells, but what I really need are perfect shells.").also { stage++ }
            242 -> npcl(ntalk2, "I'll pay double for them and give you a few tips about Crafting too.").also { stage = 152 }

            260 -> npcl(ntalk1, "Enormous bones, as big as you can get. Not just any big bones will do. Sometimes you might find a particularly long, straight bone. That's the kind of thing I need; I'll give you 1,000gp for one of them.").also { stage++ }
            261 -> npcl(ntalk2, "Occasionally, you might find a long curved bone - these are especially valuable - I'll give you 2,000gp for them. I'll also teach you a bit about how we use the bones in Construction, if you like.").also { stage = 152 }

            280 -> npc(ntalk2, "Goodbye.").also { stage = 99 }

            999 -> sendDialogue("Barlak gives you a short lecture and you learn more about Construction.").also { stage = 99 }
            1000 -> sendDialogue("Barlak gives you a short lecture and you learn more about Crafting.").also { stage = 99 }
            1001 -> sendDialogue("Barlak gives you a short lecture and you learn more about Crafting.").also { stage = 130 }
            99 -> end()
        }
        return true
    }


    private fun exchange() {
        val item = getItemForCurItem()
        val numToExchange = getNumToExchange()
        removeAll(player, item, Container.INVENTORY)
        removeAll(player, item, Container.BANK)
        addItemOrDrop(player, Items.COINS_995, getCoinsForCurItem() * numToExchange)
        rewardXP(player, getSkillForCurItem(), (getXpForCurItem() * numToExchange).toDouble())
    }

    private fun getNumToExchange(): Int {
        val itemId = getItemIdForCurItem()
        return amountInInventory(player, itemId) + amountInBank(player, itemId)
    }

    private fun getTotalCoinsForCurItem(): Int {
        return getCoinsForCurItem() * getNumToExchange()
    }

    private fun getItemForCurItem(): Item {
        return Item(getItemIdForCurItem())
    }

    private fun getItemIdForCurItem(): Int {
        return sets[curItem][0]
    }

    private fun getCoinsForCurItem(): Int {
        return sets[curItem][1]
    }

    private fun getXpForCurItem(): Int {
        return sets[curItem][2]
    }

    private fun getSkillForCurItem(): Int {
        return sets[curItem][3]
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return BarlakDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.BARLAK_5828)
    }
}
