package content.region.desert.quest.deserttreasure

import core.api.*
import core.game.dialogue.DialogueBuilder
import core.game.dialogue.DialogueBuilderFile
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import org.rs09.consts.Items
import org.rs09.consts.NPCs

@Initializable
class EblisMirrorsDialogue(player: Player? = null) : DialoguePlugin(player){
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player!!, EblisMirrorsDialogueFile(), npc)
        return false
    }
    override fun newInstance(player: Player?): DialoguePlugin {
        return EblisMirrorsDialogue(player)
    }
    override fun getIds(): IntArray {
        return intArrayOf(NPCs.EBLIS_1924, NPCs.EBLIS_1925)
    }
}
class EblisMirrorsDialogueFile : DialogueBuilderFile() {
    override fun create(b: DialogueBuilder) {

        b.onQuestStages(DesertTreasure.questName, 8)
                .npcl("Ah, so you got here at last.")
                .npc("As you may noticed, I have made the mirrors for", "the spell, and cast the enchantment upon them.")
                .npc("By simply looking into each mirror, you will be able to", "see the area where the trace magics from the Diamonds", "of Azzanadra are emanating from.")
                .npc("Unfortunately, I cannot narrow the search closer with", "this kind of spell, but if you search the areas shown to", "you, you may be able to find some clues leading you to", "the evil warriors of Zamorak who stole the diamonds in")
                .npc("the first place.")
                .player(FacialExpression.THINKING, "So you can't be anymore specific about where to look", "for these warriors and their diamonds?")
                .npc("I'm afraid not, other than the direction that the mirror", "is facing will be approximately the direction you will", "need to head in.")
                .npc("Make sure to come and speak to me when you have", "retrieved all four diamonds.")
                .endWith { _, player ->
                    if (getQuestStage(player, DesertTreasure.questName) == 8) {
                        setQuestStage(player, DesertTreasure.questName, 9)
                    }
                }

        b.onQuestStages(DesertTreasure.questName, 9, 10)
                .branch { player ->
                    return@branch if (DesertTreasure.completedAllSubstages(player)) {
                        1
                    } else {
                        0
                    }
                }.let { branch ->
                    branch.onValue(1)
                            .player(FacialExpression.THINKING, "So I have all four of these Diamonds of Azzanadra, now", "what?")
                            .npc("Azzanadra was our greatest ever hero.", "He was unkillable, and the cowardly traitors who stole", "our lands did not know what to do with him, for his", "hatred for them was as strong as his magics.")
                            .npc(FacialExpression.SAD, "In the end, they cast a spell upon him, to trap him in", "the stone structure to the South of here.")
                            .npc(FacialExpression.ANNOYED, "They stole his very life force, the essence of his power,", "and trapped it within four crystals - the very same", "Four Diamonds which you have now recovered from", "the brigands who stole from us.")
                            .npc(FacialExpression.ANNOYED, "The four pillars surrounding the structure are keeping", "the containment spell intact.", "By placing a diamond into each, you will breach the", "magical defenses and begin to restore Azzanadra's")
                            .npcl(FacialExpression.ANNOYED, "power, and be able to enter the structure.")
                            .npcl("Go, place the diamonds, and free my lord Azzanadra!")
                            .npc(FacialExpression.FRIENDLY, "The path will be hard, for his prison is full of traps", "and danger to prevent his rescue, but he will reward you", "beyond your wildest dreams when freed!")
                            .npc("Quickly...", "After all these centuries, Lord Azzanadra is nearly free!", "You must spare no time, place the Diamonds upon the", "pillars and enter the pyramid so that you may free him!")
                            .end()

                    branch.onValue(0)
                            .playerl("So can you give me any help on where to find these warriors and their diamonds?")
                            .npcl("No, the magic used in this spell is powerful, but inaccurate. The direction the scrying glass faces is roughly the direction you will find the warrior, but I'm afraid I")
                            .npcl("can't be any more help than that.")
                            .playerl("I don't understand why there are six mirrors when there are only four diamonds...")
                            .npcl("As I say, the enchantment is very inaccurate.")
                            .npcl("I can only focus upon the aura the diamonds have left behind them, so any place where the Diamonds were present for a significant period of time will still be shown - such as the Bandit Camp where I make my home.")
                            .npcl("My apologies, but magic is an inaccurate art in many respects.")
                            .npcl("Don't forget to come back here when you have collected all four diamonds.")
                            .end()
                }

        b.onQuestStages(DesertTreasure.questName, 100)
                .branch { player ->
                    if (inInventory(player, Items.ANCIENT_STAFF_4675)) {
                        1
                    } else {
                        0
                    }
                }.let { branch ->
                    branch.onValue(1)
                            .playerl("Hello again.")
                            .npcl("Greetings. I await the return of my Lord Azzanadra and of our god. I do not know why, but I feel this spot has some significance...")
                            .end()

                    branch.onValue(0)
                            .npcl("So have you spoken to my Lord Azzanadra yet?")
                            .playerl("Yes I have.")
                            .npcl("And what words did he have for his followers?")
                            .playerl("Er... He didn't really mention you at all, but he did teach me some cool new magic spells.")
                            .npcl("It is understandable perhaps... His poor mind must be addled after all of those years of confinement, he would not willingly ignore his followers...")
                            .npcl("Anyway, if he has taught you our ancient magics, you may be interested in purchasing an ancient heirloom that was passed down to me. My ancestor fought in the ancient battles using the")
                            .npcl("magic of our god. This heirloom will help you with the speed of your spell-casting.")
                            .npcl("Normally I could not bear to part with such a priceless relic, but for your help in freeing my Lord Azzanadra, I will be prepared to sell it to you for a mere 80,000 gold.")
                            .npcl("Are you interested?")
                            .options()
                            .let { optionBuilder ->
                                optionBuilder.option("Yes please")
                                        .branch { player ->
                                            if (inInventory(player, Items.COINS_995, 80000)) { 1 } else { 0 }
                                        }
                                        .let { branch2 ->
                                            branch2.onValue(1)
                                                    .npcl("Take care of it, it is the only heirloom from those times I possess, although rumour has it many of our ancient warriors were buried with identical weapons so that they could continue to fight for my Lord in their deaths.")
                                                    .endWith { _, player ->
                                                        if (removeItem(player, Item(Items.COINS_995, 80000))) {
                                                            addItemOrDrop(player, Items.ANCIENT_STAFF_4675)
                                                        }
                                                    }

                                            branch2.onValue(0)
                                                    .linel("You don't have enough money to buy that.")
                                                    .end()
                                        }

                                optionBuilder.option("No thanks")
                                        .playerl("No, not really.")
                                        .npcl("As you wish. Bear my offer in mind should you ever change your decision, I will remain here.")
                                        .end()
                            }
                }
    }
}