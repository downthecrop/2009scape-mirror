package rs09.game.interaction.item.withobject

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
    val levelRequirement = 35

    private fun cannonBallOnUseWithHandler(player: Player, used: Node, with: Node): Boolean {
        player.faceLocation(with.centerLocation)
        if(!player.questRepository.isComplete(DwarfCannon.NAME)) {
            player.dialogueInterpreter.sendDialogue("You need to complete the ${DwarfCannon.NAME} quest in order to do this.")
            return true
        }
        if (player.getSkills().getLevel(Skills.SMITHING) < levelRequirement) {
            player.dialogueInterpreter.sendDialogue("You need a Smithing level of at least $levelRequirement in order to do this.")
            return true
        }
        if (!player.inventory.contains(Items.AMMO_MOULD_4, 1)) {
            player.dialogueInterpreter.sendDialogue("You need an ammo mould in order to make a cannon ball.")
            return true
        }

        val cannonBallPulse = object : Pulse() {
            private var tick = 0
            var amount = 0
            override fun pulse(): Boolean {
                when(tick++){
                    0 -> {
                        player.sendMessage("You heat the steel bar into a liquid state.")
                        player.animator.animate(Animation(3243)) // 899 would be preferable but the arms spaz out
                    }
                    3 -> {
                        player.sendMessage("You pour the molten metal into your cannonball mould.")
                        player.animator.animate(Animation(827))
                    }
                    4 -> {
                        player.sendMessage("The molten metal cools slowly to form 4 cannonballs.")
                    }
                    7 -> {
                        if (player.inventory.remove(used.asItem())) {
                            player.inventory.add(Item(Items.CANNONBALL_2, 4))
                            player.getSkills().addExperience(Skills.SMITHING, 25.6, true)
                        }
                        player.animator.animate(Animation(827))
                    }
                    10 -> {
                        if (--amount == 0 || !player.inventory.containsAtLeastOneItem(Items.STEEL_BAR_2353)) {
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
                player.pulseManager.run(cannonBallPulse)
            }

            override fun getAll(index: Int): Int {
                return player.inventory.getAmount(itemUsed)
            }
        }
        dialogue.open()

        return true
    }

    override fun defineListeners() {
        onUseWith(SCENERY, Items.STEEL_BAR_2353, *furnaces, handler = ::cannonBallOnUseWithHandler)
    }
}