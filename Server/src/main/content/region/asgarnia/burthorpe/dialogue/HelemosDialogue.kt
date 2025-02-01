package content.region.asgarnia.burthorpe.dialogue

import core.api.*
import core.game.dialogue.DialogueBuilder
import core.game.dialogue.DialogueBuilderFile
import core.game.dialogue.DialoguePlugin
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.NPCs

@Initializable
class HelemosDialogue(player: Player? = null) : DialoguePlugin(player){

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player, HelemosDialogueFile(), npc)
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return HelemosDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.HELEMOS_797)
    }
}
class HelemosDialogueFile : DialogueBuilderFile() {

    override fun create(b: DialogueBuilder) {

        b.onPredicate { _ -> true }
                .npc("Welcome to the Heroes' Guild!")
                .options()
                .let { optionBuilder ->
                    optionBuilder.option("So do you sell anything here?")
                            .playerl("So do you sell anything good here?")
                            .npcl("Why yes! We DO run an exclusive shop for our members!")
                            .endWith { _, player ->
                                openNpcShop(player, NPCs.HELEMOS_797)
                                end()
                            }
                    optionBuilder.option_playerl("So what can I do here?")
                            .npcl("Look around... there are all sorts of things to keep our guild members entertained!")
                            .end()
                }
    }
}