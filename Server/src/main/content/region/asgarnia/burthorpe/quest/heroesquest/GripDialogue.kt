package content.region.asgarnia.burthorpe.quest.heroesquest

import core.api.*
import core.game.dialogue.DialogueBuilder
import core.game.dialogue.DialogueBuilderFile
import core.game.dialogue.DialoguePlugin
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.Items
import org.rs09.consts.NPCs

@Initializable
class GripDialogue(player: Player? = null) : DialoguePlugin(player){

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player, GripDialogueFile(), npc)
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return GripDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.GRIP_792)
    }
}

class GripDialogueFile : DialogueBuilderFile() {

    override fun create(b: DialogueBuilder) {
        b.onPredicate { _ -> true }
                .branch { player ->
                    return@branch if (getAttribute(player, HeroesQuest.attributeGripTookPapers, false)) { 1 } else { 0 }
                }.let { branch ->
                    val continuePath = b.placeholder()
                    branch.onValue(1)
                            .goto(continuePath)
                    branch.onValue(0)
                            .playerl("Hi there. I am Hartigen, reporting for duty as your new deputy sir!")
                            .npcl("Ah good, at last. You took your time getting here! Now let me see...")
                            .npcl("I'll get your hours and duty roster sorted out in a while. Oh, and do you have your I.D. papers with you? Internal security is almost as important as external security for a guard.")
                            .branch { player ->
                                return@branch if (inInventory(player, Items.ID_PAPERS_1584)) { 1 } else { 0 }
                            }.let { branch ->
                                val continuePath2 = b.placeholder()
                                branch.onValue(1)
                                        .playerl("Right here sir!")
                                        .linel("You hand the ID papers over to Grip.")
                                        .betweenStage { df, player, _, _ ->
                                            if (removeItem(player, Items.ID_PAPERS_1584)) {
                                                setAttribute(player, HeroesQuest.attributeGripTookPapers, true)
                                            }
                                        }
                                        .goto(continuePath2)
                                branch.onValue(0)
                                        .playerl("Oh, dear. I don't have that with me any more.")
                                        .npcl("Well, that's no good! Go get them immediately, then report back for duty.")
                                        .end()
                                return@let continuePath2.builder()
                            }
                        .goto(continuePath)
                    return@let continuePath.builder()
                }
                .options()
                .let { optionBuilder ->
                    val returnJoin = b.placeholder()

                    optionBuilder.option_playerl("So can I please guard the treasure room please?")
                            .npcl("Well, I might post you outside it sometimes. I prefer to be the only one allowed inside however.")
                            .npcl("There's some pretty valuable artefacts in there! Those keys stay ONLY with the head guard and Scarface Pete.")
                            .goto(returnJoin)

                    optionBuilder.optionIf("So what do my duties involve?") { player ->
                        return@optionIf !getAttribute(player, HeroesQuest.attributeGripSaidDuties, false)
                    }
                            .betweenStage { _, player, _, _ ->
                                setAttribute(player, HeroesQuest.attributeGripSaidDuties, true)
                            }
                            .playerl("So what do my duties involve?")
                            .npcl("You'll have various guard related duties on various shifts. I'll assign specific duties as they are required as and when they become necessary. Just so you know, if anything happens to me")
                            .npcl("you'll need to take over as head guard here. You'll find important keys to the treasure room and Pete's quarters inside my jacket - although I doubt anything bad's going to happen to")
                            .npcl("me anytime soon!")
                            .linel("Grip laughs to himself at the thought.")
                            .goto(returnJoin)

                    optionBuilder.option_playerl("Well, I'd better sort my new room out.")
                            .npcl("Yeah, I'll give you time to settle in. Better get a good night's sleep, I expect you to report for duty at oh five hundred hours tomorrow on the dot!")
                            .end()


                    optionBuilder.optionIf("Anything I can do now?") { player ->
                        return@optionIf getAttribute(player, HeroesQuest.attributeGripSaidDuties, false)
                    }
                            .playerl("Anything I can do now?")
                            .branch { player ->
                                return@branch if (inInventory(player, Items.MISCELLANEOUS_KEY_1586)) {
                                    1
                                } else {
                                    0
                                }
                            }.let { branch ->
                                branch.onValue(1)
                                        .npcl("Can't think of anything right now.")
                                        .end()

                                branch.onValue(0)
                                        .npcl("Hmm. Well, you could find out what this key opens for me. Apparently it's for something in this building, but for the life of me I can't find what.")
                                        .linel("Grip hands you a key.")
                                        .endWith { _, player ->
                                            addItemOrDrop(player, Items.MISCELLANEOUS_KEY_1586)
                                        }
                            }

                    returnJoin.builder()
                }
    }
}