package content.region.kandarin.quest.templeofikov

import core.api.*
import core.game.dialogue.DialogueBuilder
import core.game.dialogue.DialogueBuilderFile
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.Items
import org.rs09.consts.NPCs

@Initializable
class LucienDialogue (player: Player? = null) : DialoguePlugin(player) {
    override fun newInstance(player: Player): DialoguePlugin {
        return LucienDialogue(player)
    }
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player, LucienDialogueFile(), npc)
        return false
    }
    override fun getIds(): IntArray {
        return intArrayOf(NPCs.LUCIEN_273)
    }
}

class LucienDialogueFile : DialogueBuilderFile() {
    override fun create(b: DialogueBuilder) {
        b.onQuestStages(TempleOfIkov.questName, 100)
                .endWith { _, player ->
                    sendMessage(player, "You have completed the Temple of Ikov quest.")
                }
        b.onQuestStages(TempleOfIkov.questName, 1,2,3,4,5,6,7)
                .npcl("I told you not to meet me here again!")
                .branch { player ->
                    return@branch if (inInventory(player, Items.PENDANT_OF_LUCIEN_86)) { 1 } else { 0 }
                }.let { branch ->
                    branch.onValue(1)
                            .playerl("Sorry! Can you remind me of my mission?")
                            .npcl("My patience grows thin hero!")
                            .npcl("I need the Staff of Armadyl. It's in the Temple of Ikov, near Hemenster, north east of here.")
                            .playerl("I'm up for it!")
                            .end()
                    branch.onValue(0)
                            .playerl("I've lost the pendant you gave me.")
                            .npcl("Imbecile!")
                            .betweenStage { df, player, _, _ ->
                                addItemOrDrop(player, Items.PENDANT_OF_LUCIEN_86)
                            }
                            .item(Items.PENDANT_OF_LUCIEN_86, "Lucien has given you another pendant!")
                            .end()
                }

        b.onQuestStages(TempleOfIkov.questName, 0)
                .npcl("I seek a hero to go on an important mission!")
                .options().let { optionBuilder ->
                    val returnJoin = b.placeholder()
                    optionBuilder.option("I'm a mighty hero!")
                            .playerl(FacialExpression.ANGRY, "I'm a mighty hero!")
                            .goto(returnJoin)
                    optionBuilder.option_playerl("Yep, lots of heros about these days.")
                            .npcl("Well, if you see any be sure to point them in my direction.")
                            .end()
                    return@let returnJoin.builder()
                }
                .npcl("I require the Staff of Armadyl. It is in the deserted Temple of Ikov, near Hemenster, north east of here.")
                .npcl("Take care hero! There is a dangerous monster somewhere in the temple!")
                .let{ builder ->
                    val returnJoin = b.placeholder()
                    returnJoin.builder()
                            .options()
                            .let { optionBuilder ->
                                optionBuilder.option_playerl("Why can't you get it yourself?")
                                        .npcl("The guardians of the Staff of Armadyl fear me!")
                                        .npcl("They have set up a magical barrier which even my power cannot overcome!")
                                        .goto(returnJoin)
                                optionBuilder.option("That sounds like a laugh!")
                                        .playerl(FacialExpression.FRIENDLY, "That sounds like a laugh!")
                                        .npcl("It's not as easy as it sounds. The monster can only be killed with a weapon of ice. There are many other dangers.")
                                        .playerl(FacialExpression.FRIENDLY, "I'm up for it!")
                                        .npcl("Take this pendant. Without it you will not be enter the Chamber of Fear.")
                                        .betweenStage { df, player, _, _ ->
                                            addItemOrDrop(player, Items.PENDANT_OF_LUCIEN_86)
                                        }
                                        .item(Items.PENDANT_OF_LUCIEN_86, "Lucien has given you a pendant!")
                                        .npcl("I cannot stay here much longer. ")
                                        .npcl("I will be west of the Grand Exchange in Varrock. I have a small holding up there.")
                                        .endWith { _, player ->
                                            if(getQuestStage(player, TempleOfIkov.questName) == 0) {
                                                setQuestStage(player, TempleOfIkov.questName, 1)
                                            }
                                        }
                                optionBuilder.option_playerl("Oh no! Sounds far too dangerous!")
                                        .npcl("Wimp! Call yourself a hero?! My daughter is more a hero than you!")
                                        .end()
                                optionBuilder.option_playerl("What's the reward?!")
                                        .npcl("I see you are the mercenary type.")
                                        .playerl("It's a living.")
                                        .npcl("I will reward you well if you bring me the staff.")
                                        .goto(returnJoin)
                                return@let optionBuilder
                            }
                    return@let builder.goto(returnJoin)
                }
    }
}