package content.global.skill.farming

import core.api.*
import core.game.dialogue.DialoguePlugin
import core.game.interaction.QueueStrength
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.Sounds

@Initializable
class DigUpPatchDialogue(player: Player? = null) : DialoguePlugin(player) {
    var patch: Patch? = null

    override fun newInstance(player: Player?): DialoguePlugin {
        return DigUpPatchDialogue(player)
    }

    override fun open(vararg args: Any?): Boolean {
        patch = args[0] as Patch
        if (patch?.isWeedy() == true || patch?.isEmptyAndWeeded() == true) {
            sendDialogue(player, "There aren't any crops in this patch to dig up.")
            stage = 1000
            return true
        }
        if (patch?.patch?.type == PatchType.TREE_PATCH) {
            val isTreeStump = patch?.getCurrentState() == patch?.plantable!!.value + patch?.plantable!!.stages + 2
            if (patch!!.isGrown() && !isTreeStump) {
                sendMessage(player, "You need to chop this tree down first.") // this message is not authentic
                stage = 1000
                return true
            }
        }
        sendDialogueOptions(player, "Are you sure you want to dig up this patch?", "Yes, I want to clear it for new crops.", "No, I want to leave it as it is.")
        stage = 0
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> when (buttonId) {
                1 -> {
                    end()
                    val anim = getAnimation(830)
                    sendMessage(player, "You start digging the farming patch...")
                    queueScript(player, 0, QueueStrength.WEAK) { stage: Int ->
                        when (stage) {
                            0 -> {
                                animate(player, anim)
                                playAudio(player, Sounds.DIGSPADE_1470)
                                return@queueScript delayScript(player,anim.duration + 2)
                            }
                            1 -> {
                                animate(player, anim)
                                playAudio(player, Sounds.DIGSPADE_1470)
                                return@queueScript delayScript(player, anim.duration + 1)
                            }
                            2 -> {
                                if (patch?.patch?.type == PatchType.TREE_PATCH) {
                                    if (patch?.getCurrentState() == (patch?.plantable?.value ?: 0) + (patch?.plantable?.stages ?: 0) + 2 && patch?.isWeedy() != true && patch?.isEmptyAndWeeded() != true) {
                                        addItemOrDrop(player, patch?.plantable?.harvestItem ?: 0)
                                    }
                                }
                                if (patch?.plantable == Plantable.SCARECROW) {
                                    addItemOrDrop(player, patch?.plantable?.harvestItem ?: 0)
                                }
                                patch?.clear()
                                sendMessage(player, "You have successfully cleared this patch for new crops.")
                                return@queueScript stopExecuting(player)
                            }
                            else -> return@queueScript stopExecuting(player)
                        }
                    }
                }
                else -> end()
            }

            1000 -> end()
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(67984003)
    }

}