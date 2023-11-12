package content.region.morytania.quest.creatureoffenkenstrain

import core.api.*
import core.game.dialogue.*
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import org.rs09.consts.Items
import org.rs09.consts.NPCs

@Initializable
class DrFenkenstrainDialogue (player: Player? = null) : DialoguePlugin(player) {
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player, DrFenkenstrainDialogueFile(), npc)
        return true
    }
    override fun newInstance(player: Player): DialoguePlugin {
        return DrFenkenstrainDialogue(player)
    }
    override fun getIds(): IntArray {
        // THE IDs ARE WRONG. 1668 and 1669 are varp controlled Dr Fenkenstrain
        return intArrayOf(NPCs.WEREWOLF_1668, NPCs.WEREWOLF_1669, NPCs.DR_FENKENSTRAIN_1670)
    }
}

class DrFenkenstrainDialogueFile : DialogueBuilderFile() {

    companion object {
        private fun allPartsSubmitted(player: Player): Boolean {
            return getAttribute(player, CreatureOfFenkenstrain.attributeArms, false) &&
                    getAttribute(player, CreatureOfFenkenstrain.attributeLegs, false) &&
                    getAttribute(player, CreatureOfFenkenstrain.attributeTorso, false) &&
                    getAttribute(player, CreatureOfFenkenstrain.attributeHead, false)
        }
        private fun reqArms(player: Player): Boolean {
            return !getAttribute(player, CreatureOfFenkenstrain.attributeArms, false) && inInventory(player, Items.ARMS_4195)
        }
        private fun reqLegs(player: Player): Boolean {
            return !getAttribute(player, CreatureOfFenkenstrain.attributeLegs, false) && inInventory(player, Items.LEGS_4196)
        }
        private fun reqTorso(player: Player): Boolean {
            return !getAttribute(player, CreatureOfFenkenstrain.attributeTorso, false) && inInventory(player, Items.TORSO_4194)
        }
        private fun reqHead(player: Player): Boolean {
            return !getAttribute(player, CreatureOfFenkenstrain.attributeHead, false) && inInventory(player, Items.DECAPITATED_HEAD_4198)
        }
        private fun hasPart(b: DialogueBuilder, item: Item, attributeToSet: String, successMsg: String): DialogueBuilder {
            return b.branch { player ->
                return@branch if (!getAttribute(player, attributeToSet, false) && inInventory(player, item.id, item.amount)) { 1 } else { 0 }
            }.let{ branch ->
                val returnJoin = b.placeholder()
                branch.onValue(0)
                        .goto(returnJoin)
                branch.onValue(1)
                        .betweenStage { _, player, _, _ ->
                            setAttribute(player, attributeToSet, true)
                            removeItem(player, item)
                        }
                        .npcl(successMsg)
                        .goto(returnJoin)
                return@let returnJoin.builder()
            }
        }
    }

    override fun create(b: DialogueBuilder) {

        b.onQuestStages(CREATURE_OF_FENKENSTRAIN, 0)
                .npcl("Have you come to apply for the job?")
                .playerl(FacialExpression.THINKING, "What job?")
                .npcl("I've posted a note on the signpost in Canifis about it. Go take a look at it first.")
                .end()

        b.onQuestStages(CREATURE_OF_FENKENSTRAIN, 1)
                .npcl("Have you come to apply for the job?")
                .options().let { optionBuilder ->
                    val continuePath = b.placeholder()
                    optionBuilder.option ("Yes")
                            .playerl("Yes, if it pays well.")
                            .goto(continuePath) // Continue down below.
                    optionBuilder.option_playerl("No.")
                            .end()
                    return@let continuePath.builder()
                }
                .npcl("I'll have to ask you some questions first.")
                .playerl("Okay...")
                .npcl("How would you describe yourself in one word?")
                .options().let { optionBuilder ->
                    val continuePath = b.placeholder()
                    optionBuilder.recordAttribute("creature-of-fenkenstrain:first-question")
                    optionBuilder.option_playerl("Stunning.").goto(continuePath)
                    optionBuilder.option_playerl("Awe-inspiring.").goto(continuePath)
                    optionBuilder.option_playerl("Breathtaking.").goto(continuePath)
                    optionBuilder.option_playerl("Braindead.").goto(continuePath)
                    return@let continuePath.builder()
                }
                .npcl("Mmmm, I see.")
                .npcl("Just one more question. What would you say is your greatest skill?")
                .options().let { optionBuilder ->
                    val continuePath = b.placeholder()
                    optionBuilder.recordAttribute("creature-of-fenkenstrain:second-question")
                    optionBuilder.option_playerl("Combat.").goto(continuePath)
                    optionBuilder.option_playerl("Magic.").goto(continuePath)
                    optionBuilder.option_playerl("Cooking.").goto(continuePath)
                    optionBuilder.option_playerl("Grave-digging.").goto(continuePath)
                    return@let continuePath.builder()
                }
                .npcl("Mmmm, I see.")
                .branch { player -> if(player.getAttribute("creature-of-fenkenstrain:first-question", -1) == 3 && player.getAttribute("creature-of-fenkenstrain:second-question", -1) == 3) { 1 } else { 0 } }
                .let{ branch ->
                    // Failure branch
                    branch.onValue(0)
                            .npcl("Looks like you're not the @g[man,woman] for the job.")
                            .end()
                    return@let branch // Return DialogueBranchBuilder instead of DialogueBuilder to forward the success branch.
                }.onValue(1) // Success branch
                .npcl("Looks like you're just the @g[man,woman] for the job! Welcome aboard!")
                .playerl("Is there anything you'd like me to do for you, sir?")
                .npcl("Yes, there is. You're highly skilled at grave-digging, yes?")
                .playerl("Err...yes, that's what I said.")
                .npcl("Excellent. Now listen carefully. I need you to find some...stuff...for me.")
                .playerl("Stuff?")
                .npcl("That's what I said...stuff.")
                .playerl("What kind of stuff?")
                .npcl("Well...dead stuff.")
                .playerl("Go on...")
                .npcl("I need you to get me enough dead body parts for me to stitch together a complete body, which I plan to bring to life.")
                .playerl("Right...okay...if you insist.")
                .endWith { _, player ->
                    if(getQuestStage(player, CreatureOfFenkenstrain.questName) == 1) {
                        setQuestStage(player, CreatureOfFenkenstrain.questName, 2)
                    }
                }

        b.onQuestStages(CreatureOfFenkenstrain.questName, 2)
                .options().let { optionBuilder ->
                    val continuePath = b.placeholder()

                    optionBuilder.optionIf("I have some body parts for you.") { player -> return@optionIf allPartsSubmitted(player) || reqArms(player) || reqLegs(player) || reqTorso(player) || reqHead(player) }
                            .playerl("I have some body parts for you.")
                            .goto(continuePath) // Continue down below.
                    optionBuilder.optionIf("Do you know where I could find body parts?") { player -> return@optionIf !(allPartsSubmitted(player) || reqArms(player) || reqLegs(player) || reqTorso(player) || reqHead(player)) }
                            .playerl("Do you know where I could find body parts?")
                            .npcl("The soil of Morytania is unique in its ability to preserve the bodies of the dead, which is one reason why I have chosen to carry out my experiments here.")
                            .npcl("I recommend digging up some graves in the local area. To the south-east you will find the Haunted Woods; I believe there are many graves there.")
                            .npcl( "There is also a mausoleum on an island west of this castle. I expect the bodies that are buried there to be extremely well preserved, as they were wealthy in life.")
                            .end()
                    optionBuilder.option_playerl("Remind me what you want me to do.")
                            .npcl("I need you to get me enough dead body parts for me to stitch together a complete body, which I plan to bring to life.")
                            .playerl("Right...okay...if you insist.")
                            .end()
                    optionBuilder.option_playerl("Why are you trying to make this creature?")
                            .npcl("I came to the land of Morytania many years ago, to find a safe sanctuary for my experiments. This abandoned castle suited my purposes exactly.")
                            .playerl("What were you experimenting in?")
                            .npcl("Oh, perfectly innocent experiments - for the good of mankind.")
                            .playerl("Then why did you need to come to Morytania?")
                            .npcl("Enough questions, now. Get back to your work.")
                            .end()
                    optionBuilder.option_playerl("Will this creature put me out of a job?")
                            .npcl("No, my friend. I have a very special purpose in mind for this creature.")
                            .end()
                    optionBuilder.option_playerl("I must get back to work, sir.")
                            .end()

                    return@let continuePath.builder()
                }
                // Dialogue path to look for arms.
                .let{ builder -> return@let hasPart(builder, Item(Items.ARMS_4195, 1), CreatureOfFenkenstrain.attributeArms, "Great, you've brought me some arms.") }
                // Dialogue path to look for legs.
                .let{ builder -> return@let hasPart(builder, Item(Items.LEGS_4196, 1), CreatureOfFenkenstrain.attributeLegs, "Excellent, you've brought me some legs.") }
                // Dialogue path to look for torso.
                .let{ builder -> return@let hasPart(builder, Item(Items.TORSO_4194, 1), CreatureOfFenkenstrain.attributeTorso, "Splendid, you've brought me a torso.") }
                // Dialogue path to look for head.
                .let{ builder -> return@let hasPart(builder, Item(Items.DECAPITATED_HEAD_4198, 1), CreatureOfFenkenstrain.attributeHead, "Fantastic, you've brought me a head.") }
                .branch { player ->
                    return@branch if (allPartsSubmitted(player)) { 1 } else { 0 }
                }.let{ branch ->
                    // Failure branch
                    branch.onValue(0)
                            .end()
                    return@let branch // Return DialogueBranchBuilder instead of DialogueBuilder to forward the success branch.
                }.onValue(1) // Success branch
                .npcl("Superb! Those are all the parts I need. Now to sew them together...")
                .npcl("Oh bother! I haven't got a needle or thread!")
                .npcl("Go and get me a needle, and I'll need 5 lots of thread.")
                .endWith { _, player ->
                    if(getQuestStage(player, CreatureOfFenkenstrain.questName) == 2) {
                        setQuestStage(player, CreatureOfFenkenstrain.questName, 3)
                    }
                }

        b.onQuestStages(CreatureOfFenkenstrain.questName, 3)
                .npcl("Where are my needle and thread, @name?")
                // Dialogue path to look for 1 needle.
                .let{ builder -> return@let hasPart(builder, Item(Items.NEEDLE_1733, 1), CreatureOfFenkenstrain.attributeNeedle, "Ah, a needle. Wonderful.") }
                // Dialogue path to look for 5 threads.
                .let{ builder -> return@let hasPart(builder, Item(Items.THREAD_1734, 5), CreatureOfFenkenstrain.attributeThread, "Some thread. Excellent.") }
                // Final branch to only continue conversation when both needle and threads are submitted.
                .branch { player ->
                    return@branch if (getAttribute(player, CreatureOfFenkenstrain.attributeNeedle, false) &&
                            getAttribute(player, CreatureOfFenkenstrain.attributeThread, false)) { 1 } else { 0 }
                }.let{ branch ->
                    // Failure branch
                    branch.onValue(0)
                            .end()
                    return@let branch // Return DialogueBranchBuilder instead of DialogueBuilder to forward the success branch.
                }.onValue(1) // Success branch
                .betweenStage { df, player, _, _ ->
                    setVarp(player, CreatureOfFenkenstrain.fenkenstrainVarp, 3, true)
                }
                .line("Fenkenstrain uses the needle and thread to sew the body parts", "together. Soon, a hideous creature lies inanimate on the ritual table.")
                .npcl("Perfect. But I need one more thing from you - flesh and bones by themselves do not make life.")
                .playerl("Really?")
                .npcl("I have honed to perfection an ancient ritual that will give life to this creature, but for this I must harness the very power of Nature.")
                .playerl("And what power is this?")
                .npcl("The power of lightning.")
                .playerl("Sorry, can't make lightning, you've got the wrong @g[man,woman]-")
                .npcl("Silence your insolent tongue! The storm that brews overhead will create the lightning. What I need you to do is to repair the lightning conductor on the balcony above.")
                .playerl("Repair the lightning conductor, right. Can I have a break, soon? By law I'm entitled to 15 minutes every-")
                .npcl("Repair the conductor and BEGONE!!")
                .endWith { _, player ->
                    if(getQuestStage(player, CreatureOfFenkenstrain.questName) == 3) {
                        setQuestStage(player, CreatureOfFenkenstrain.questName, 4)
                    }
                }


        b.onQuestStages(CreatureOfFenkenstrain.questName, 4)
                .playerl(FacialExpression.THINKING, "How do I repair the lighting conductor?")
                .npcl("Oh, it would be easier to do it myself! If you find a conductor mould you should be able to cast a new one.")
                .npcl("Remember this, @name, my experiment will only work with a conductor made from silver.")
                .end()

        b.onQuestStages(CreatureOfFenkenstrain.questName, 5)
                .playerl("So did it work, then?")
                .npcl("Yes, I'm afraid it did, @name - all too well.")
                .playerl(FacialExpression.SUSPICIOUS, "I can't see it anywhere.")
                .npcl("I tricked it into going up to the Tower, and there it remains, imprisoned.")
                .playerl(FacialExpression.SUSPICIOUS, "So the creature wasn't all you'd hoped then?")
                .npcl("...oh, what have I done...")
                .playerl(FacialExpression.SUSPICIOUS, "Oh, I see, we're developing a sense of right and wrong now are we?")
                .playerl(FacialExpression.SUSPICIOUS, "Bit late for that, I'd say.")
                .npcl("I have no control over it! It's coming to get me!")
                .playerl(FacialExpression.SUSPICIOUS, "What do you want me to do about it?")
                .npcl("Destroy it!!! Take the key to the Tower and take back the life I never should have granted!!!")
                .endWith() { df, player ->
                    if(getQuestStage(player, CreatureOfFenkenstrain.questName) == 5) {
                        setQuestStage(player, CreatureOfFenkenstrain.questName, 6)
                    }
                    addItemOrDrop(player, Items.TOWER_KEY_4185)
                }

        b.onQuestStages(CreatureOfFenkenstrain.questName, 6)
                .npcl("So have you destroyed it?!!?")
                .playerl("Not yet.")
                .npcl("Please, hurry - save me!!!!")
                .end()

        b.onQuestStages(CreatureOfFenkenstrain.questName, 7)
                .npcl("So have you destroyed it?!!?")
                .playerl("Never, now that he has told me the truth!")
                .npcl("Oh my, oh my, this is exactly what I feared!")
                .npcl("Why did you have to pick Rologarth's brain of all brains?!?")
                .playerl("I'm through working for you.")
                .npcl("No! I refuse to release you! You must help me build another creature to destroy this dreadful mistake!!")
                .end()

        b.onQuestStages(CreatureOfFenkenstrain.questName, 8, 100)
                .npcl("theyrecomingtogetme theyrecomingtogetme...")
                .playerl("It is all you deserve. Lord Rologarth is master of this castle once more. Let him protect you - if he wants to.")
                .npcl("theyrecomingtogetme theyrecomingtogetme...")
                .end()
    }
}