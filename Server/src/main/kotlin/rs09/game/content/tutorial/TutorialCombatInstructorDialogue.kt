package rs09.game.content.tutorial

import api.*
import core.game.content.dialogue.DialoguePlugin
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.Items
import org.rs09.consts.NPCs

/**
 * Handles the combat instructor's dialogue
 * @author Ceikry
 */
@Initializable
class TutorialCombatInstructorDialogue(player: Player? = null) : DialoguePlugin(player) {
    override fun newInstance(player: Player?): DialoguePlugin {
        return TutorialCombatInstructorDialogue(player)
    }

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC

        when(getAttribute(player, "tutorial:stage", 0))
        {
            44 -> playerl(FacialExpression.FRIENDLY, "Hi! My name's ${player.username}.")
            47 -> npcl(FacialExpression.FRIENDLY, "Very good, but that little butter knife isn't going to protect you much. Here, take these.")
            53 -> playerl(FacialExpression.FRIENDLY, "I did it! I killed a giant rat!")
            54 -> {
                player.dialogueInterpreter.sendDoubleItemMessage(Items.SHORTBOW_841, Items.BRONZE_ARROW_882, "The Combat Guide gives you some bronze arrows and a shortbow!")
                if(!inInventory(player, Items.SHORTBOW_841) && !inEquipment(player, Items.SHORTBOW_841))
                    addItem(player, Items.SHORTBOW_841)
                if(!inInventory(player, Items.BRONZE_ARROW_882) && !inEquipment(player, Items.BRONZE_ARROW_882))
                    addItem(player, Items.BRONZE_ARROW_882, 30)
            }
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(getAttribute(player, "tutorial:stage", 0))
        {
            44 -> when(stage){
                0 -> npcl(FacialExpression.ANGRY, "Do I look like I care? To me you're just another newcomer who thinks they're ready to fight.").also { stage++ }
                1 -> npcl(FacialExpression.FRIENDLY, "I'm Vannaka, the greatest swordsman alive.").also { stage++ }
                2 -> npcl(FacialExpression.FRIENDLY, "Let's get started by teaching you to wield a weapon.").also { stage++ }
                3 -> {
                    end()
                    setAttribute(player, "tutorial:stage", 45)
                    TutorialStage.load(player, 45)
                }
            }

            47 -> when(stage){
                0 -> {
                    addItemOrDrop(player, Items.BRONZE_SWORD_1277)
                    addItemOrDrop(player, Items.WOODEN_SHIELD_1171)
                    sendDoubleItemDialogue(player, Items.BRONZE_SWORD_1277, Items.WOODEN_SHIELD_1171, "The Combat Guide gives you a bronze sword and a wooden shield!")
                    stage++
                }
                1 -> {
                    end()
                    setAttribute(player, "tutorial:stage", 48)
                    TutorialStage.load(player, 48)
                }
            }

            53 -> when(stage){
                0 -> npcl(FacialExpression.FRIENDLY, "I saw, ${player.username}. You seem better at this than I thought. Now that you have grasped basic swordplay, let's move on.").also { stage++ }
                1 -> npcl(FacialExpression.FRIENDLY, "Let's try some ranged attacking, with this you can kill foes from a distance. Also, foes unable to reach you are as good as dead. You'll be able to attack the rats, without entering the pit.").also { stage++ }
                2 -> {
                    sendDoubleItemDialogue(player, Items.SHORTBOW_841, Items.BRONZE_ARROW_882, "The Combat Guide gives you some bronze arrows and a shortbow!")
                    if(!inInventory(player, Items.SHORTBOW_841) && !inEquipment(player, Items.SHORTBOW_841))
                        addItem(player, Items.SHORTBOW_841)
                    if(!inInventory(player, Items.BRONZE_ARROW_882) && !inEquipment(player, Items.BRONZE_ARROW_882))
                        addItem(player, Items.BRONZE_ARROW_882, 30)
                    stage++
                }
                3 -> {
                    end()
                    setAttribute(player, "tutorial:stage", 54)
                    TutorialStage.load(player, 54)
                }
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.COMBAT_INSTRUCTOR_944)
    }

}