package content.region.asgarnia.falador.quest.recruitmentdrive

import core.api.*
import core.game.dialogue.DialogueBuilder
import core.game.dialogue.DialogueBuilderFile
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.interaction.QueueStrength
import core.game.node.entity.Entity
import core.game.node.entity.impl.Projectile
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import core.game.world.update.flag.context.Graphics
import core.plugin.Initializable
import org.rs09.consts.NPCs
import org.rs09.consts.Sounds

@Initializable
class SirTinleyDialogue (player: Player? = null) : DialoguePlugin(player) {
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player, SirTinleyDialogueFile(), npc)
        return true
    }
    override fun newInstance(player: Player): DialoguePlugin {
        return SirTinleyDialogue(player)
    }
    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SIR_TINLEY_2286)
    }
}

class SirTinleyDialogueFile(private val dialogueNum: Int = 0) : DialogueBuilderFile(), MapArea {
    companion object {
        const val attributeDoNotMove = "quest:recruitmentdrive-donotmove"
    }

    override fun create(b: DialogueBuilder) {
        b.onPredicate {player -> dialogueNum == 0 && !getAttribute(player, attributeDoNotMove, false) && getAttribute(player, RecruitmentDrive.attributeStagePassFailState, 0) == 0 }
                .npc("Ah, welcome @name.", "I have but one clue for you to pass this room's puzzle:", "'Patience'.")
                .endWith { _, player ->
                    setAttribute(player, attributeDoNotMove, true)
                    queueScript(player, 0, QueueStrength.NORMAL) { stage: Int ->
                        when (stage) {
                            0 -> {
                                return@queueScript delayScript(player, 15)
                            }
                            1 -> {
                                if (getAttribute(player, RecruitmentDrive.attributeStagePassFailState, 0) != -1) {
                                    setAttribute(player, RecruitmentDrive.attributeStagePassFailState, 1)
                                    setAttribute(player, attributeDoNotMove, false)
                                    npc(FacialExpression.FRIENDLY, "Excellent work, @name.", "Please step through the portal to meet your next", "challenge.")
                                }
                                return@queueScript stopExecuting(player)
                            }
                            else -> return@queueScript stopExecuting(player)
                        }
                    }
                }

        b.onPredicate {player -> dialogueNum == 0 && !getAttribute(player, attributeDoNotMove, false) && getAttribute(player, RecruitmentDrive.attributeStagePassFailState, 0) == 1 }
                .npc(FacialExpression.FRIENDLY, "Excellent work, @name.", "Please step through the portal to meet your next", "challenge.")
                .end()

        // If you talk to him before time is up, you fail.
        b.onPredicate { player -> dialogueNum == 0 && getAttribute(player, attributeDoNotMove, false) || dialogueNum == 2 || getAttribute(player, RecruitmentDrive.attributeStagePassFailState, 0) == -1 }
                .betweenStage { _, player, _, _ ->
                    setAttribute(player, RecruitmentDrive.attributeStagePassFailState, -1)
                }
                .npc(FacialExpression.SAD, "No... I am very sorry.", "Apparently you are not up to the challenge.", "I will return you where you came from, better luck in the", "future.")
                .endWith { _, player ->
                    removeAttribute(player, attributeDoNotMove)
                    removeAttribute(player, RecruitmentDrive.attributeStagePassFailState)
                    RecruitmentDriveListeners.FailTestCutscene(player).start()
                }

        b.onPredicate { _ -> dialogueNum == 1 }
                .npc("Ah, @name, you have arrived.", "Speak to me to begin your task.")
                .endWith { _, player ->
                    setAttribute(player, attributeDoNotMove, false)
                }
    }


    override fun defineAreaBorders(): Array<ZoneBorders> {
        return arrayOf(ZoneBorders(2474, 4959, 2478, 4957))
    }

    override fun entityStep(entity: Entity, location: Location, lastLocation: Location) {
        if (entity is Player) {
            if(getAttribute(entity, attributeDoNotMove, false)) {
                setAttribute(entity, attributeDoNotMove, false)
                setAttribute(entity, RecruitmentDrive.attributeStagePassFailState, -1)
                openDialogue(entity, SirTinleyDialogueFile(2), NPC(NPCs.SIR_TINLEY_2286))
            }
        }
    }
}
