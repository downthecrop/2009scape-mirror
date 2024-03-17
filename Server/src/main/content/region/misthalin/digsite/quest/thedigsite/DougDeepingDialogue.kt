package content.region.misthalin.digsite.quest.thedigsite

import core.api.*
import core.game.dialogue.*
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.Items
import org.rs09.consts.NPCs

@Initializable
class DougDeepingDialogue (player: Player? = null) : DialoguePlugin(player) {
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player, DougDeepingDialogueFile(), npc)
        return true
    }
    override fun newInstance(player: Player): DialoguePlugin {
        return DougDeepingDialogue(player)
    }
    override fun getIds(): IntArray {
        return intArrayOf(NPCs.DOUG_DEEPING_614)
    }
}

class DougDeepingDialogueFile : DialogueBuilderFile() {

    override fun create(b: DialogueBuilder) {
        b.onPredicate { _ -> true }
                .playerl(FacialExpression.FRIENDLY, "Hello.")
                .npcl(FacialExpression.FRIENDLY, "Well, well... I have a visitor. What are you doing here?")
                .options().let { optionBuilder ->
                    optionBuilder.option_playerl("I have been invited to research here ")
                            .npcl(FacialExpression.FRIENDLY, "Indeed, you must be someone special to be allowed down here.")
                            .options().let { optionBuilder ->
                                optionBuilder.option_playerl("Do you know where to find a specimen jar?")
                                        .npcl("Hmmm, let me think... Nope, can't help you there, I'm afraid. Try asking at the Exam Centre.")
                                        .end()
                                optionBuilder.option_playerl("I have things to do...")
                                        .npcl("Of course, don't let me keep you.")
                                        .end()
                            }
                    optionBuilder.option_playerl("I'm not really sure.")
                            .npcl("A miner without a clue - how funny!")
                            .end()
                    optionBuilder.option_playerl("I'm here to get rich, rich, rich!")
                            .npcl("Oh, well, don't forget that wealth and riches aren't everything.")
                            .end()
                    optionBuilder.option_playerl("How could I move a large pile of rocks?")
                            .npcl("There used to be this chap that worked in the other shaft. He was working on an explosive chemical mixture to be used for clearing blocked areas underground.")
                            .npcl("He left in a hurry one day. Apparently, something in the shaft scared him to death, but he didn't say what.")
                            .playerl(FacialExpression.FRIENDLY, "Oh?")
                            .npcl(FacialExpression.FRIENDLY, "Rumour has it he'd been writing a book on his chemical mixture. I'm not sure what was in it, but he left in such a hurry, he probably left something behind in the other dig shaft.")
                            .npcl("In fact, I still have a chest key he gave me to look after - perhaps it's more useful to you.")
                            .branch { player ->
                                if (inInventory(player,Items.CHEST_KEY_709)) { 0 } else {
                                    addItemOrDrop(player, Items.CHEST_KEY_709)
                                    1
                                }
                            }
                            .let { branch ->
                                branch.onValue(0)
                                        .playerl("It's okay, I already have one.")
                                        .end()
                                branch.onValue(1)
                                        .iteml(Items.CHEST_KEY_709, "Doug hands you a key.")
                                        .end()
                            }
                }
    }
}