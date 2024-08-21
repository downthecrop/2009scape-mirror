package content.region.asgarnia.falador.quest.recruitmentdrive

import core.api.*
import core.game.dialogue.DialogueBuilder
import core.game.dialogue.DialogueBuilderFile
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.plugin.Initializable
import org.rs09.consts.NPCs

@Initializable
class SirKuamFerentseDialogue (player: Player? = null) : DialoguePlugin(player) {
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player, SirKuamFerentseDialogueFile(), npc)
        return true
    }
    override fun newInstance(player: Player): DialoguePlugin {
        return SirKuamFerentseDialogue(player)
    }
    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SIR_KUAM_FERENTSE_2284)
    }
}

class SirKuamFerentseDialogueFile(private val dialogueNum: Int = 0) : DialogueBuilderFile() {
    companion object {
        const val attributeGeneratedSirLeye = "quest:recruitmentdrive-generatedsirleye"
    }

    override fun create(b: DialogueBuilder) {
        b.onPredicate { player -> getAttribute(player, RecruitmentDrive.attributeStagePassFailState, 0) == 1 }
                .npc(FacialExpression.FRIENDLY, "Excellent work, @name.", "Please step through the portal to meet your next", "challenge.")
                .end()

        // You can't fail unless you quit the room.
        b.onPredicate { _ -> true }
                .npc("Ah, @name, you're finally here.", "Your task for this room is to defeat Sir Leye.", "He has been blessed by Saradomin to be undefeatable", "by any man, so it should be quite the challenge for you.")
                .npc("If you are having problems, remember", "A true warrior uses his wits as much as his brawn.", "Fight smarter, not harder.")
                .endWith { _, player ->
                    var boss = getAttribute(player, attributeGeneratedSirLeye, NPC(0))
                    if (boss.id != 0) {
                        boss.clear()
                    }
                    boss = NPC(NPCs.SIR_LEYE_2285, player.location)
                    setAttribute(player, attributeGeneratedSirLeye, boss)
                    boss.isRespawn = false
                    boss.isAggressive = false
                    boss.isWalks = true
                    boss.location = Location(2460, 4963)
                    boss.init()
                    registerHintIcon(player, boss)
                    sendChat(boss, "No man may defeat me!")
                }
    }
}