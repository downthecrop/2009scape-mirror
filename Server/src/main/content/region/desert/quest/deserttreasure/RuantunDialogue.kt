package content.region.desert.quest.deserttreasure

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
class RuantunDialogue(player: Player? = null) : DialoguePlugin(player){
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player!!, RuantunDialogueFile(), npc)
        return false
    }
    override fun newInstance(player: Player?): DialoguePlugin {
        return RuantunDialogue(player)
    }
    override fun getIds(): IntArray {
        return intArrayOf(NPCs.RUANTUN_1916)
    }
}

class RuantunDialogueFile : DialogueBuilderFile() {
    override fun create(b: DialogueBuilder) {

        b.onQuestStages(DesertTreasure.questName, 0,1,2,3,4,5,6,7,8)
                .playerl("Hello.")
                .npcl(FacialExpression.OLD_NORMAL, "You ssshould not be down here...")
                .options()
                .let { optionBuilder ->
                    optionBuilder.option("Who are you?")
                            .playerl("Who are you?")
                            .npcl(FacialExpression.OLD_NORMAL, "My name isss unimportant... I live only to ssserve my massster.")
                            .playerl("Um... Okay then.")
                            .end()

                    optionBuilder.option("Why are you down here?")
                            .playerl("Why are you down here?")
                            .npcl(FacialExpression.OLD_NORMAL, "Thisss isss where I belong... Beingsss sssuch as myssself cannot abide in the light... It is in the darknesss where we find our homesss...")
                            .playerl("Uh... Okay then.")
                            .end()

                    optionBuilder.option("Can I use your anvil?")
                            .playerl("Can I use your anvil?")
                            .npcl(FacialExpression.OLD_NORMAL, "Of courssse you may... I have very little ussse for it nowadaysss...")
                            .playerl("Uh... Thanks, I guess.")
                            .end()
                }

        b.onQuestStages(DesertTreasure.questName, 9, 10, 100)
                // Technically should happen after talking to Malak, but nah.
                .playerl("Hello.")
                .npcl(FacialExpression.OLD_NORMAL, "You sshould not be down here...")
                .playerl("Are you an assistant to Count Draynor?")
                .npc(FacialExpression.OLD_NORMAL, "I usssed to have that honour...", "Why do you ssseek me?")
                .branch { player ->
                    return@branch if (inInventory(player, Items.SILVER_BAR_2355)) { 1 } else { 0 }
                }.let { branch ->
                    branch.onValue(1)
                            .playerl("I have a silver bar with me, I was wondering if you could make it into a 'sacrificial offering pot' for me?")
                            .betweenStage { df, player, _, _ ->
                                if (removeItem(player, Items.SILVER_BAR_2355)) {
                                    addItemOrDrop(player, Items.SILVER_POT_4658)
                                }
                            }
                            .npc(FacialExpression.OLD_NORMAL, "Yesss, of courssse...", "There you are, put it to good usssse...")
                            .end()

                    branch.onValue(0)
                            .playerl("I understand that you can make me a 'sacrificial offering pot' if I bring you a bar of silver?")
                            .npcl(FacialExpression.OLD_NORMAL, "And where did you hear thisss?")
                            .playerl("It was from Malak in Canifis.")
                            .npc(FacialExpression.OLD_NORMAL, "Ah, I sssee...", "Yesss, I know how to make sssuch an item...", "It has been many yearsss sssince I have needed to however...")
                            .npcl(FacialExpression.OLD_NORMAL, "It is not my wisssh to quessstion your desssire for sssuch an item, I wasss merely sssurprisssed that one sssuch as you would make sssuch a requessst...")
                            .npcl(FacialExpression.OLD_NORMAL, "I will happily make you thisss pot, but you mussst bring me a bar of sssilver... Alasss, I can no longer collect my own ingredientsss, and mussst remain here...")
                            .end()
                }
    }
}