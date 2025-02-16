package content.region.desert.bandits.handlers

import content.region.desert.quest.deserttreasure.DesertTreasure
import core.api.getAttribute
import core.api.inInventory
import core.api.openDialogue
import core.game.dialogue.DialogueBuilder
import core.game.dialogue.DialogueBuilderFile
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.NPCs

@Initializable
class BanditDialogue (player: Player? = null) : DialoguePlugin(player) {
    override fun newInstance(player: Player): DialoguePlugin {
        return BanditDialogue(player)
    }
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player, BanditDialogueFile(), npc)
        return false
    }
    override fun getIds(): IntArray {
        return intArrayOf(NPCs.BANDIT_1926)
    }
}
class BanditDialogueFile : DialogueBuilderFile() {

    override fun create(b: DialogueBuilder) {

        b.onQuestStages(DesertTreasure.questName, 100)
                .npcl("So you're the one who freed Azzanadra from his prison? Thank you, kind @g[sir,lady]!")
                .end()

        b.onQuestStages(DesertTreasure.questName, 1,2,3,4,5,6,7,8,9,10,11,12,13,14,15)
                .npcl("What do you want @g[lad,lass]?")
                .playerl("I'm here on an archaeological expedition for the Museum of Varrock. I believe there may be some interesting artefacts in the area.")
                .branch { player ->
                    return@branch (0..4).random()
                }.let { branch ->
                    branch.onValue(0)
                            .npcl("You are a crazy @g[man,woman]. The only thing you will find out here in the desert is your death.")
                            .end()
                    branch.onValue(1)
                            .npcl("I have no interest in the world that betrayed my people. Search where you will, you will find nothing.")
                            .end()
                    branch.onValue(2)
                            .npcl("The gods forsake us, and drove us to this place. Anything of worth has been long gone.")
                            .end()
                    branch.onValue(3)
                            .npcl("I'm sure there are many secrets buried beneath the sands here. The thing about this being a desert, is that they're likely to stay that way.")
                            .end()
                    branch.onValue(4)
                            .npcl("Do I look like I care who you are or where you came from?")
                            .end()
                }

        b.onPredicate { _ -> true }
                .npcl("Get out of this village. You are not welcome here.")
                .end()
    }
}