package content.global.handlers.item.withobject

import core.api.*
import content.region.kandarin.quest.dwarfcannon.DwarfCannon
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.system.task.Pulse
import org.rs09.consts.Items
import core.game.dialogue.SkillDialogueHandler
import core.game.interaction.InteractionListener
import core.game.interaction.IntType
import org.rs09.consts.Sounds

class AmmoMouldOnFurnace : InteractionListener {
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
                        playAudio(player, Sounds.FURNACE_2725)
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
        onUseWith(IntType.SCENERY, Items.STEEL_BAR_2353, *furnaces, handler = ::cannonBallOnUseWithHandler)
    }
}