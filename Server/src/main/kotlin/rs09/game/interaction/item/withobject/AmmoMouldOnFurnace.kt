package rs09.game.interaction.item.withobject

import api.*
import core.game.content.quest.members.dwarfcannon.DwarfCannon
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.update.flag.context.Animation
import org.rs09.consts.Items
import rs09.game.content.dialogue.SkillDialogueHandler
import rs09.game.interaction.InteractionListener

class AmmoMouldOnFurnace : InteractionListener(){
    private val furnaces = intArrayOf(4304, 6189, 11010, 11666, 12100, 12809, 18497, 26814, 30021, 30510, 36956, 37651)  // abstract when smelting converted to kotlin
    private val levelRequirement = 35

    private fun cannonBallOnUseWithHandler(player: Player, used: Node, with: Node): Boolean {
        face(player, with.centerLocation)

        if(!isQuestComplete(player, DwarfCannon.NAME)) {
            sendDialogue(player, "You need to complete the ${DwarfCannon.NAME} quest in order to do this.")
            return true
        }
        if (getDynLevel(player, Skills.SMITHING) < levelRequirement) {
            sendDialogue(player,"You need a Smithing level of at least $levelRequirement in order to do this.")
            return true
        }
        if (!inInventory(player, Items.AMMO_MOULD_4)) {
            sendDialogue(player,"You need an ammo mould in order to make a cannon ball.")
            return true
        }

        val cannonBallPulse = object : Pulse() {
            private var tick = 0
            var amount = 0
            override fun pulse(): Boolean {
                when(tick++){
                    0 -> {
                        sendMessage(player,"You heat the steel bar into a liquid state.")
                        animate(player, 3243) // 899 would be preferable but the arms spaz out
                    }
                    3 -> {
                        sendMessage(player,"You pour the molten metal into your cannonball mould.")
                        animate(player, 827)
                    }
                    4 -> {
                        sendMessage(player,"The molten metal cools slowly to form 4 cannonballs.")
                    }
                    7 -> {
                        if (removeItem(player, used.asItem())) {
                            addItem(player, Items.CANNONBALL_2, 4)
                            rewardXP(player, Skills.SMITHING, 25.6)
                        }
                        animate(player, 827)
                    }
                    10 -> {
                        if (--amount == 0 || !inInventory(player, Items.STEEL_BAR_2353)) {
                            return true
                        }
                        tick = 0
                    }
                }
                return false
            }
        }

        val itemUsed = used.asItem()

        val dialogue: SkillDialogueHandler = object : SkillDialogueHandler(player, SkillDialogue.ONE_OPTION, itemUsed) {
            override fun create(amount: Int, index: Int) {
                cannonBallPulse.amount = amount
                submitIndividualPulse(player, cannonBallPulse)
            }

            override fun getAll(index: Int): Int {
                return amountInInventory(player, itemUsed.id)
            }
        }
        openDialogue(player, dialogue)
        return true
    }

    override fun defineListeners() {
        onUseWith(SCENERY, Items.STEEL_BAR_2353, *furnaces, handler = ::cannonBallOnUseWithHandler)
    }
}