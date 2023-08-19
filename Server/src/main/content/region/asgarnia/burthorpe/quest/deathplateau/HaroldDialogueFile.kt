package content.region.asgarnia.burthorpe.quest.deathplateau

import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.FacialExpression
import core.game.dialogue.Topic
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.update.flag.context.Graphics
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs09.consts.Animations
import org.rs09.consts.Items

/**
 * Harold sub dialogue file for death plateau.
 * Called by HaroldDialogue
 * @author bushtail
 * @author ovenbread
 */
class HaroldDialogueFile : DialogueFile() {

    companion object{
        const val ATTRIBUTE_JUMPSTAGE = "deathplateau:jumpStage"
        const val ATTRIBUTE_HAROLD_MONEY = "/save:deathplateau:haroldMoney"

        /** Resets the NPC's quest attributes */
        fun resetNpc(player: Player) {
            setAttribute(player, ATTRIBUTE_JUMPSTAGE, 0)
            setAttribute(player, ATTRIBUTE_HAROLD_MONEY, 200)
        }
    }
    override fun handle(componentID: Int, buttonID: Int) {
        // This jumpStage is needed to resume the conversation after the dice interface.
        // The dice interface closes the dialogue file.
        if(getAttribute(player!!, ATTRIBUTE_JUMPSTAGE, 0) != 0) {
            stage = getAttribute(player!!, ATTRIBUTE_JUMPSTAGE, 0)
            setAttribute(player!!, ATTRIBUTE_JUMPSTAGE, 0)
        }
        println(getAttribute(player!!, ATTRIBUTE_HAROLD_MONEY, -1))
        when (getQuestStage(player!!, DeathPlateau.questName)) {
            10 -> { // First time meeting.
                when (stage) {
                    START_DIALOGUE -> player(FacialExpression.FRIENDLY, "Hello there.").also { stage++ }
                    1 -> npcl(FacialExpression.FRIENDLY, "Hi.").also { stage++ }
                    2 -> playerl(FacialExpression.FRIENDLY, "You're the guard that was on duty last night?").also { stage++ }
                    3 -> npcl(FacialExpression.FRIENDLY, "Yeah.").also { stage++ }
                    4 -> playerl(FacialExpression.HAPPY, "Denulth said that you lost the combination to the equipment room ?").also { stage++ }
                    5 -> npcl(FacialExpression.FRIENDLY, "I don't want to talk about it!").also {
                        setQuestStage(player!!, "Death Plateau", 11)
                        stage = END_DIALOGUE
                    }
                }
            }
            11 -> { // Asking again.
                when (stage) {
                    START_DIALOGUE -> player(FacialExpression.FRIENDLY, "Hello there.").also { stage++ }
                    1 -> npcl(FacialExpression.ANNOYED, "What?").also { stage++ }
                    2 -> playerl(FacialExpression.FRIENDLY, "You're the guard that was on duty last night?").also { stage++ }
                    3 -> npcl(FacialExpression.FRIENDLY, "I said I didn't want to talk about it!").also {
                        stage = END_DIALOGUE
                    }
                }
            }
            12 -> { // After asking Eohric about opening up.
                when (stage) {
                    START_DIALOGUE -> playerl(FacialExpression.FRIENDLY, "Hello there.").also { stage++ }
                    1 -> npcl(FacialExpression.FRIENDLY, "Hi.").also { stage++ }
                    2 -> playerl(FacialExpression.FRIENDLY, "Can I buy you a drink?").also { stage++ }
                    3 -> npcl(FacialExpression.HAPPY, "Now you're talking! An Asgarnian Ale, please!").also { stage++ }
                    4 -> {
                        if (inInventory(player!!, Items.ASGARNIAN_ALE_1905, 1)) {
                            removeItem(player!!, Items.ASGARNIAN_ALE_1905)
                            setQuestStage(player!!, "Death Plateau", 12)
                            sendMessage(player!!, "You give Harold an Asgarnian Ale.")
                            setAttribute(player!!, ATTRIBUTE_HAROLD_MONEY, 200)
                            sendItemDialogue(player!!, Items.ASGARNIAN_ALE_1905, "You give Harold an Asgarnian Ale.").also { stage++ }
                        } else {
                            playerl(FacialExpression.FRIENDLY, "I'll go and get you one.").also { stage = END_DIALOGUE }
                        }
                    }
                    5 -> {
                        end()
                        setQuestStage(player!!, "Death Plateau", 13)
                        animate(npc!!, Animations.HUMAN_EATTING_829)
                        runTask(npc!!, 5) {
                            npcl(FacialExpression.FRIENDLY, "Arrh. That hit the spot!").also { stage = END_DIALOGUE }
                        }
                    }
                }
            }
            13 -> { // After getting him an Asgarnian Ale.
                when (stage) {
                    START_DIALOGUE -> player(FacialExpression.FRIENDLY, "Hello there.").also { stage++ }
                    1 -> npc(FacialExpression.FRIENDLY, "Hi.").also { stage++ }
                    2 -> showTopics(
                            Topic(FacialExpression.ASKING, "Where were you when you last had the combination?", 10),
                            Topic(FacialExpression.FRIENDLY, "Would you like to gamble?", 30),
                            Topic(FacialExpression.FRIENDLY, "Can I buy you a drink?", 20)
                    )

                    10 -> npcl(FacialExpression.FRIENDLY, "I honestly don't know! I've looked everywhere. I've searched the castle and my room!").also { stage++ }
                    11 -> playerl(FacialExpression.ASKING, "Have you tried looking between here and the castle?").also { stage++ }
                    12 -> npcl(FacialExpression.FRIENDLY, "Yeah, I tried that.").also { stage++ }
                    13 -> npcl(FacialExpression.FRIENDLY, "I need another beer.").also { stage = END_DIALOGUE }

                    20 -> npcl(FacialExpression.FRIENDLY, "Sounds good! I normally drink Asgarnian Ale but you know what?").also { stage++ }
                    21 -> playerl(FacialExpression.ASKING, "What?").also { stage++ }
                    22 -> npcl(FacialExpression.FRIENDLY, "I really fancy one of those Blurberry Specials. I never get over to the Gnome Stronghold so I haven't had one for ages!").also { stage++ }
                    23 -> {
                        if (removeItem(player!!, Items.BLURBERRY_SPECIAL_2064)) {
                            sendMessage(player!!, "You give Harold a Blurberry Special.")
                            sendItemDialogue(player!!, Items.BLURBERRY_SPECIAL_2064, "You give Harold a Blurberry Special.").also { stage++ }
                        } else if (removeItem(player!!, Items.PREMADE_BLURB_SP_2028)) {
                            sendMessage(player!!, "You give Harold a Blurberry Special.")
                            sendItemDialogue(player!!, Items.PREMADE_BLURB_SP_2028, "You give Harold a Blurberry Special.").also { stage++ }
                        } else {
                            player(FacialExpression.FRIENDLY, "I'll go and get you one.").also { stage = END_DIALOGUE }
                        }
                    }
                    24 -> {
                        end()
                        setQuestStage(player!!, DeathPlateau.questName, 14)
                        npc!!.isWalks = false
                        animate(npc!!, Animations.HUMAN_EATTING_829)
                        runTask(npc!!, 4) {
                            npc!!.sendChat("Wow!")
                            runTask(npc!!, 4) {
                                sendGraphics(Graphics(80, 96), npc!!.location)
                                runTask(npc!!, 6) {
                                    npc(FacialExpression.DRUNK, "Now THAT hit the spot!").also {
                                        runTask(npc!!, 6) {
                                            npc!!.isWalks = true
                                            end()
                                            stage = END_DIALOGUE
                                        }
                                    }
                                }
                            }
                        }
                    }

                    30 -> npcl(FacialExpression.FRIENDLY, "Good. Good. I have some dice. How much do you want to offer?").also { stage++ }
                    31 -> {
                        sendInputDialogue(player!!, true, "Enter amount:") { value ->
                            val wagerAmount = value as Int
                            // Check wager amount.
                            if (wagerAmount <= 0) {
                                sendMessage(player!!, "You have to offer some money.").also { stage = END_DIALOGUE }
                            } else if (wagerAmount > 1000) {
                                npcl(FacialExpression.FRIENDLY, "Woah! Do you think I'm made of money? Max bet is 1000 gold.").also { stage = END_DIALOGUE }
                            } else if (!inInventory(player!!, Items.COINS_995) || amountInInventory(player!!, Items.COINS_995) < value) {
                                sendMessage(player!!, "You do not have that much money!").also { stage = END_DIALOGUE }
                            } else if (removeItem(player!!, Item(Items.COINS_995, value))) {
                                player!!.setAttribute("deathplateau:wager", wagerAmount)
                                npc(FacialExpression.FRIENDLY, "OK. I'll roll first!").also { stage++ }
                            }
                            return@sendInputDialogue
                        }
                    }
                    32 -> {
                        npcl(FacialExpression.FRIENDLY, "Don't forget that once I start my roll you can't back out of the bet! If you do you lose your stake!").also { stage++ }
                    }
                    33 -> {
                        end()
                        npc!!.isWalks = false
                        // Dialogue drops when another interface is clicked. So a resume dialogue is needed.
                        val advanceStage : (() -> Unit) = {
                            npc!!.isWalks = true
                            setAttribute(player!!, ATTRIBUTE_JUMPSTAGE, stage + 1)
                            openDialogue(player!!, HaroldDialogueFile(), npc!!)
                        }
                        setAttribute(player!!, "deathplateau:dicegameclose", advanceStage)
                        openInterface(player!!, 99)
                        setInterfaceText(player!!, player!!.name, 99, 6)
                    }
                    34 -> {
                        val wager = getAttribute(player!!, "deathplateau:wager", 0)
                        val haroldAmount = getAttribute(player!!, ATTRIBUTE_HAROLD_MONEY, 0)
                        if (getAttribute(player!!, "deathplateau:winstate", false)) {
                            // Win
                            if (wager > haroldAmount) {
                                npcl(FacialExpression.FRIENDLY, "Oh dear, I seem to have run out of money!").also { stage++ }
                            } else {
                                addItemOrDrop(player!!, Items.COINS_995, wager * 2)
                                sendMessage(player!!, "Harold has given you your winnings!")
                                setAttribute(player!!, ATTRIBUTE_HAROLD_MONEY,haroldAmount - wager)
                                sendItemDialogue(player!!, Items.COINS_995, "Harold has given you your winnings!").also { stage = END_DIALOGUE }
                            }
                        } else {
                            // Lose
                            setAttribute(player!!, ATTRIBUTE_HAROLD_MONEY,haroldAmount + wager)
                            sendItemDialogue(player!!, Items.COINS_995, "You give Harold his winnings.").also { stage = END_DIALOGUE }
                        }
                    }
                    35 -> npcl(FacialExpression.FRIENDLY, "Here's what I have.").also { stage++ }
                    36 -> sendItemDialogue(player!!, Items.COINS_995, "Harold has given you some of your winnings!").also {
                        sendMessage(player!!, "Harold has given you some of your winnings!")
                        val haroldAmount = getAttribute(player!!, ATTRIBUTE_HAROLD_MONEY, 200)
                        setAttribute(player!!, ATTRIBUTE_HAROLD_MONEY,0)
                        addItemOrDrop(player!!, Items.COINS_995, haroldAmount)
                        stage++
                    }
                    37 -> npcl(FacialExpression.FRIENDLY, "I'll write you out an IOU for the rest.").also { stage++ }
                    38 -> {
                        addItemOrDrop(player!!, Items.IOU_3103)
                        setQuestStage(player!!, DeathPlateau.questName, 15)
                        sendMessage(player!!, "Harold has given you an IOU scribbled on some paper.")
                        sendItemDialogue(player!!, Items.IOU_3103, "Harold has given you an IOU scribbled on some paper.").also {stage = END_DIALOGUE}
                    }
                }
            }
            14 -> { // After getting him a Blurberry Special.
                when (stage) {
                    START_DIALOGUE -> player(FacialExpression.FRIENDLY, "Hello there.").also { stage++ }
                    1 -> npc(FacialExpression.DRUNK, "'Ello matey!'").also { stage++ }
                    2 -> showTopics(
                        Topic(FacialExpression.THINKING, "Where were you when you last had the combination?", 10),
                        Topic(FacialExpression.FRIENDLY, "Would you like to gamble?", 20),
                        Topic(FacialExpression.FRIENDLY, "Can I buy you a drink?", 15)
                    )

                    10 -> npc(FacialExpression.DRUNK,"Hmm...!").also { stage++ }
                    11 -> npc(FacialExpression.DRUNK,"Er...!").also { stage++ }
                    12 -> npc(FacialExpression.DRUNK,"What wash the queshtion?").also { stage = END_DIALOGUE }

                    15 -> npcl(FacialExpression.DRUNK, "I fink I've had enough!").also { stage = END_DIALOGUE }

                    20 -> npc(FacialExpression.DRUNK,"Shure!").also { stage++ }
                    21 -> npc(FacialExpression.DRUNK,"Place your betsh pleashe!").also { stage++ }
                    22 -> npc(FacialExpression.DRUNK,"*giggle*!").also { stage++ }

                    23 -> {
                        sendInputDialogue(player!!, true, "Enter amount:") { value ->
                            val wagerAmount = value as Int
                            // Check wager amount.
                            if (wagerAmount <= 0) {
                                sendMessage(player!!, "You have to offer some money.").also { stage = END_DIALOGUE }
                            } else if (wagerAmount > 1000) {
                                npcl(FacialExpression.DRUNK, "Eashy tiger! Max bet ish 1000 coinsh.").also { stage = END_DIALOGUE }
                            } else if (!inInventory(player!!, Items.COINS_995) || amountInInventory(player!!, Items.COINS_995) < value) {
                                sendMessage(player!!, "You do not have that much money!").also { stage = END_DIALOGUE }
                            } else if (removeItem(player!!, Item(Items.COINS_995, value))) {
                                player!!.setAttribute("deathplateau:wager", wagerAmount)
                                npc(FacialExpression.DRUNK, "Right...er...here goes...").also { stage++ }
                            }
                            return@sendInputDialogue
                        }
                    }
                    24 -> {
                        end()
                        npc!!.isWalks = false
                        // Dialogue drops when another interface is clicked. So a resume dialogue is needed.
                        val advanceStage : (() -> Unit) = {
                            setAttribute(player!!, ATTRIBUTE_JUMPSTAGE, stage + (1..4).random())
                            openDialogue(player!!, HaroldDialogueFile(), npc!!)
                        }
                        setAttribute(player!!, "deathplateau:dicegameclose", advanceStage)
                        openInterface(player!!, 99)
                        setInterfaceText(player!!, player!!.name, 99, 6)
                    }
                    25 -> npc(FacialExpression.DRUNK, "Shixteen! How am I shupposhed to beat that!").also { stage = 30 }
                    26 -> npc(FacialExpression.DRUNK, "I didn't know you could ushe four dishe. Oh well.").also { stage = 30 }
                    27 -> npc(FacialExpression.DRUNK, "*hic*").also { stage = 30 }
                    28 -> npc(FacialExpression.DRUNK, "I sheemed to have rolled a one.").also { stage = 30 }
                    30 -> {
                        sendDialogue(player!!, "Harold is so drunk he can hardly see, let alone count!").also { stage++ }
                        npc!!.isWalks = true
                        sendMessage(player!!, "Harold is so drunk he can hardly see, let alone count!")
                    }
                    31 -> {
                        val wager = getAttribute(player!!, "deathplateau:wager", 0)
                        val haroldAmount = getAttribute(player!!, ATTRIBUTE_HAROLD_MONEY, 0)
                        // Always Win
                        if (wager > haroldAmount) {
                            npcl(FacialExpression.DRUNK, " Um...not enough money.").also { stage++ }
                        } else {
                            addItemOrDrop(player!!, Items.COINS_995, wager * 2)
                            sendMessage(player!!, "Harold has given you your winnings!")
                            setAttribute(player!!, ATTRIBUTE_HAROLD_MONEY,haroldAmount - wager)
                            sendItemDialogue(player!!, Items.COINS_995, "Harold has given you your winnings!").also { stage = END_DIALOGUE }
                        }
                    }
                    32 -> npcl(FacialExpression.DRUNK, "Heresh shome of it.").also { stage++ }
                    33 -> sendItemDialogue(player!!, Items.COINS_995, "Harold has given you some of your winnings!").also {
                        sendMessage(player!!, "Harold has given you some of your winnings!")
                        val haroldAmount = getAttribute(player!!, ATTRIBUTE_HAROLD_MONEY, 200)
                        setAttribute(player!!, ATTRIBUTE_HAROLD_MONEY,0)
                        addItemOrDrop(player!!, Items.COINS_995, haroldAmount)
                        stage++
                    }
                    34 -> npcl(FacialExpression.DRUNK, "I owe you the resht!").also { stage++ }
                    35 -> {
                        addItemOrDrop(player!!, Items.IOU_3103)
                        setQuestStage(player!!, DeathPlateau.questName, 15)
                        sendMessage(player!!, "Harold has given you an IOU scribbled on some paper.")
                        sendItemDialogue(player!!, Items.IOU_3103, "Harold has given you an IOU scribbled on some paper.").also {stage = END_DIALOGUE}
                    }
                }
            }
            15 -> { // If you lose your IOU
                when (stage) {
                    START_DIALOGUE -> npcl(FacialExpression.FRIENDLY, "Hi.").also {
                        if (inInventory(player!!, Items.IOU_3103)){
                            stage = 5
                        } else {
                            stage++
                        }
                    }
                    1 -> playerl(FacialExpression.FRIENDLY, "I've lost the IOU you gave me.").also { stage++ }
                    2 -> npcl(FacialExpression.FRIENDLY, "I'll write you another.").also { stage++ }
                    3 -> {
                        addItemOrDrop(player!!, Items.IOU_3103)
                        sendItemDialogue(player!!, Items.IOU_3103, "Harold has given you an IOU scribbled on some paper.").also {stage = END_DIALOGUE}
                    }

                    5 -> npc(FacialExpression.FRIENDLY, "Hi.").also { stage++ }
                    6 -> showTopics(
                            Topic(FacialExpression.ASKING, "Where were you when you last had the combination?", 10),
                            Topic(FacialExpression.FRIENDLY, "Would you like to gamble?", 30),
                            Topic(FacialExpression.FRIENDLY, "Can I buy you a drink?", 20)
                    )

                    10 -> npcl(FacialExpression.FRIENDLY, "I honestly don't know! I've looked everywhere. I've searched the castle and my room!").also { stage++ }
                    11 -> playerl(FacialExpression.ASKING, "Have you tried looking between here and the castle?").also { stage++ }
                    12 -> npcl(FacialExpression.FRIENDLY, "Yeah, I tried that.").also { stage++ }
                    13 -> npcl(FacialExpression.FRIENDLY, "I need another beer.").also { stage = END_DIALOGUE }

                    20 -> npcl(FacialExpression.FRIENDLY, "I've run out of money!").also { stage++ }
                    21 -> npcl(FacialExpression.FRIENDLY, "Oh dear. I need beer.").also { stage = END_DIALOGUE }

                    30 -> npcl(FacialExpression.HAPPY, "Now you're talking! An Asgarnian Ale, please!").also { stage++ }
                    31 -> {
                        if (inInventory(player!!, Items.ASGARNIAN_ALE_1905, 1)) {
                            removeItem(player!!, Items.ASGARNIAN_ALE_1905)
                            sendMessage(player!!, "You give Harold an Asgarnian Ale.")
                            sendItemDialogue(player!!, Items.ASGARNIAN_ALE_1905, "You give Harold an Asgarnian Ale.").also { stage++ }
                        } else {
                            playerl(FacialExpression.FRIENDLY, "I'll go and get you one.").also { stage = END_DIALOGUE }
                        }
                    }
                    32 -> {
                        end()
                        animate(npc!!, Animations.HUMAN_EATTING_829)
                        runTask(npc!!, 5) {
                            npcl(FacialExpression.FRIENDLY, "Arrh. That hit the spot!").also { stage = END_DIALOGUE }
                        }
                    }

                }
            }
            in 16 .. 29 -> { // If you lose your IOU
                when (stage) {
                    START_DIALOGUE -> npcl(FacialExpression.FRIENDLY, "Hi.").also {
                        if (inInventory(player!!, Items.COMBINATION_3102) || inInventory(player!!, Items.IOU_3103)){
                            stage = 5
                        } else {
                            stage++
                        }
                    }
                    1 -> playerl(FacialExpression.FRIENDLY, "I've lost the IOU you gave me.").also { stage++ }
                    2 -> npcl(FacialExpression.FRIENDLY, "I'll write you another.").also { stage++ }
                    3 -> {
                        addItemOrDrop(player!!, Items.COMBINATION_3102)
                        sendItemDialogue(player!!, Items.COMBINATION_3102, "Harold has given you the IOU, which you know is the combination.").also {stage = END_DIALOGUE}
                    }

                    5 -> npc(FacialExpression.FRIENDLY, "Hi.").also { stage++ }
                    6 -> showTopics(
                            Topic(FacialExpression.ASKING, "Where were you when you last had the combination?", 10),
                            Topic(FacialExpression.FRIENDLY, "Would you like to gamble?", 30),
                            Topic(FacialExpression.FRIENDLY, "Can I buy you a drink?", 20)
                    )

                    10 -> npcl(FacialExpression.FRIENDLY, "I honestly don't know! I've looked everywhere. I've searched the castle and my room!").also { stage++ }
                    11 -> playerl(FacialExpression.ASKING, "Have you tried looking between here and the castle?").also { stage++ }
                    12 -> npcl(FacialExpression.FRIENDLY, "Yeah, I tried that.").also { stage++ }
                    13 -> npcl(FacialExpression.FRIENDLY, "I need another beer.").also { stage = END_DIALOGUE }

                    20 -> npcl(FacialExpression.FRIENDLY, "I've run out of money!").also { stage++ }
                    21 -> npcl(FacialExpression.FRIENDLY, "Oh dear. I need beer.").also { stage = END_DIALOGUE }

                    30 -> npcl(FacialExpression.HAPPY, "Now you're talking! An Asgarnian Ale, please!").also { stage++ }
                    31 -> {
                        if (inInventory(player!!, Items.ASGARNIAN_ALE_1905, 1)) {
                            removeItem(player!!, Items.ASGARNIAN_ALE_1905)
                            sendMessage(player!!, "You give Harold an Asgarnian Ale.")
                            sendItemDialogue(player!!, Items.ASGARNIAN_ALE_1905, "You give Harold an Asgarnian Ale.").also { stage++ }
                        } else {
                            playerl(FacialExpression.FRIENDLY, "I'll go and get you one.").also { stage = END_DIALOGUE }
                        }
                    }
                    32 -> {
                        end()
                        animate(npc!!, Animations.HUMAN_EATTING_829)
                        runTask(npc!!, 5) {
                            npcl(FacialExpression.FRIENDLY, "Arrh. That hit the spot!").also { stage = END_DIALOGUE }
                        }
                    }
                }
            }
        }
    }
}