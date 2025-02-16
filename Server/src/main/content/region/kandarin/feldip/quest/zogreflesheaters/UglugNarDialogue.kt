package content.region.kandarin.feldip.quest.zogreflesheaters

import core.api.*
import core.game.dialogue.DialogueBuilder
import core.game.dialogue.DialogueBuilderFile
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.NPCs

@Initializable
class UglugNarDialogue (player: Player? = null) : DialoguePlugin(player) {
    override fun newInstance(player: Player): DialoguePlugin {
        return UglugNarDialogue(player)
    }
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player, UglugNarDialogueFile(), npc)
        return false
    }
    override fun getIds(): IntArray {
        return intArrayOf(NPCs.UGLUG_NAR_2039)
    }
}
class UglugNarDialogueFile : DialogueBuilderFile() {
    override fun create(b: DialogueBuilder) {
        b.onQuestStages(ZogreFleshEaters.questName, 0)
                .options()
                .let { optionBuilder ->
                    optionBuilder.option_playerl("Hey, what's going on here?")
                            .npcl(FacialExpression.OLD_NORMAL, "Dem's dead ogre's come out of the ground...dey's makin' da rest of us into sick-ums ...and dead-uns.")
                            .playerl("That doesn't sound good!")
                            .npcl(FacialExpression.OLD_NORMAL, "Grish want's da person go down der - see what's what!")
                            .end()
                    optionBuilder.option_playerl("What are you selling?")
                            .npcl(FacialExpression.OLD_NORMAL, "Me's not got no glug-glugs to sell, yous bring me da sickies glug-glug den me's open da stufsies for ya.")
                            .end()
                    optionBuilder.option_playerl("Ok, thanks.")
                            .end()
                }

        b.onQuestStages(ZogreFleshEaters.questName, 1,2,3,4,5,6,7,8,9,10,100)
                .options()
                .let { optionBuilder ->
                    optionBuilder.option_playerl("Hello again.")
                            .branch { player ->
                                return@branch if (getAttribute(player, ZogreFleshEaters.attributeOpenUglugNarShop, false)) { 1 } else { 0 }
                            }.let { branch ->
                                branch.onValue(1)
                                        .npcl(FacialExpression.OLD_NORMAL, "Hey yous creature...yous did good fings gedin that glug-glugs for da sickies! All is ogries pepels are not gettin dead cos of you.")
                                        .end()
                                branch.onValue(0)
                                        .npcl(FacialExpression.OLD_NORMAL, "Hey yous creature...yous still here?")
                                        .playerl("Yeah, I'm going to help Grish by figuring out what went on here.")
                                        .npcl(FacialExpression.OLD_NORMAL, "If yous finds somefin for da sickies, yous brings to me...and I's give you bright pretties, den me make more for alls pepels.")
                                        .playerl("Hmm, ok. I'll try to bear that in mind.")
                                        .end()
                            }

                    optionBuilder.option_playerl("What are you selling?")
                            .branch { player ->
                                return@branch if (getAttribute(player, ZogreFleshEaters.attributeOpenUglugNarShop, false)) { 1 } else { 0 }
                            }.let { branch ->
                                branch.onValue(1)
                                        .npcl(FacialExpression.OLD_NORMAL, "Me's showin' you da stufsies for yous creatures!")
                                        .endWith { _, player ->
                                            openNpcShop(player, npc!!.id)
                                        }
                                branch.onValue(0)
                                        .npcl(FacialExpression.OLD_NORMAL, "Me's not got no glug-glugs to sell, yous bring me da sickies glug-glug den me's open da stufsies for ya.")
                                        .end()
                            }

                    optionBuilder.option_playerl("Ok, thanks.")
                            .end()
                }
    }
}