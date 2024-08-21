package content.region.asgarnia.falador.quest.recruitmentdrive

import core.api.*
import core.game.dialogue.DialogueBuilder
import core.game.dialogue.DialogueBuilderFile
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.interaction.InterfaceListener
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.plugin.Initializable
import org.rs09.consts.Components
import org.rs09.consts.NPCs

@Initializable
class SirRenItchwoodDialogue (player: Player? = null) : DialoguePlugin(player) {
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player, SirRenItchwoodDialogueFile(), npc)
        return true
    }
    override fun newInstance(player: Player): DialoguePlugin {
        return SirRenItchwoodDialogue(player)
    }
    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SIR_REN_ITCHOOD_2287)
    }
}

class SirRenItchwoodDialogueFile(private val dialogueNum: Int = 0) : DialogueBuilderFile() {
    companion object {
        const val attributeClueNumber = "quest:recruitmentdrive-cluenumber"
    }

    override fun create(b: DialogueBuilder) {
        b.onPredicate { player -> dialogueNum in 0..1 && getAttribute(player, RecruitmentDrive.attributeStagePassFailState, 0) != -1 }
                .betweenStage { _, player, _, _ ->
                    if (getAttribute(player, attributeClueNumber, -1) !in 0..5) {
                        setAttribute(player, attributeClueNumber, (0..5).random())
                    }
                }
                .npc("Greetings friend, and welcome here,", "you'll find my puzzle not so clear.", "Hidden amongst my words, it's true,", "the password for the door as a clue.")
                .options().let { optionBuilder ->
                    optionBuilder.option_playerl ("Can I have the clue for the door?")
                            .branch { player -> getAttribute(player, attributeClueNumber, 0) }
                            .let{ branch ->
                                // Note: all the "I" in here are written in small case "i" (sic)
                                branch.onValue(0)
                                        .npc("Better than me, you'll not find", "In rhyming and in puzzles.", "This clue so clear will tax your mind", "Entirely as it confuzzles!")
                                        .end()
                                branch.onValue(1)
                                        .npc("Feel the aching of your mind", "In puzzlement, confused.", "See the clue hidden behind", "His words, as you perused.")
                                        .end()
                                branch.onValue(2)
                                        .npc("Look closely at the words i speak;", "And study closely every part.", "See for yourself the word you seek", "Trapped for you if you're smart.")
                                        .end()
                                branch.onValue(3)
                                        .npc("More than words, i have not for you", "Except the things i say today.", "Aware are you, this is a clue?", "Take note of what i say!")
                                        .end()
                                branch.onValue(4)
                                        .npc("Rare it is that you will see", "A puzzle such as this!", "In many ways it tickles me", "Now, watching you hit and miss!")
                                        .end()
                                branch.onValue(5)
                                        .npc("This riddle of mine may confuse,", "I am quite sure of that.", "Mayhap you should closely peruse", "Every word i have spat?")
                                        .end()
                            }
                    return@let optionBuilder.option("Can I have a different clue?")
                            .player("I don't get that riddle...", "Can I have a different one?")
                            .branch { player -> getAttribute(player, attributeClueNumber, 0) }
                            .let{ branch ->
                                branch.onValue(0)
                                        .npc("Before you hurry through that door", "Inspect the words i spoke.", "There is a simple hidden flaw", "Ere you think my rhyme a joke.")
                                        .end()
                                branch.onValue(1)
                                        .npc("First my clue you did not see,", "I really wish you had.", "Such puzzling wordplay devilry", "Has left you kind of mad!")
                                        .end()
                                branch.onValue(2)
                                        .npc("Last time my puzzle did not help", "Apparently, so you've bidden.", "Study my speech carefully, whelp", "To find the answer, hidden.")
                                        .end()
                                branch.onValue(3)
                                        .npc("Many types have passed through here", "Even such as you amongst their sort.", "And in the end, the puzzles clear;", "The hidden word you saught.")
                                        .end()
                                branch.onValue(4)
                                        .npc("Repetition, once again", "Against good sense it goes.", "In my words, the answers plain", "Now that you see rhyme flows.")
                                        .end()
                                branch.onValue(5)
                                        .npc("Twice it is now, i have stated", "In a rhyme, what is the pass.", "Maybe my words obfuscated", "Entirely beyond your class.")
                                        .end()
                            }
                    /*
                    // I'm too goddamned lazy to implement the final clue dialogue
                    return@let optionBuilder.option("Can I have the final clue?")
                            .branch { player -> getAttribute(player, attributeClueNumber, 0) }
                            .let{ branch ->
                                branch.onValue(0)
                                        .npc("Betrayed by words the answer is", "In that what i say is the key", "There is no more help after this", "Especially no more from me.")
                                        .end()
                                branch.onValue(1)
                                        .npc("For the last time i will state", "In simple words, the clue.", "Such tricky words make you irate", "Having no idea what to do...")
                                        .end()
                                branch.onValue(2)
                                        .npc("Lo! my final speech is now", "Attended to by you.", "Study my words, and find out how", "To understand my clue!")
                                        .end()
                                branch.onValue(3)
                                        .npc("Many types have passed through here", "Even such as you amongst their sort.", "And in the end, the puzzles clear;", "The hidden word you saught.")
                                        .end()
                                branch.onValue(4)
                                        .npc("Repetition, once again", "Against good sense it goes.", "In my words, the answers plain", "Now that you see rhyme flows.")
                                        .end()
                                branch.onValue(5)
                                        .npc("Twice it is now, i have stated", "In a rhyme, what is the pass.", "Maybe my words obfuscated", "Entirely beyond your class.")
                                        .end()
                            }
                    */

                }
        b.onPredicate { player -> dialogueNum == 2 || getAttribute(player, RecruitmentDrive.attributeStagePassFailState, 0) == -1 }
                .betweenStage { _, player, _, _ ->
                    setAttribute(player, RecruitmentDrive.attributeStagePassFailState, -1)
                }
                .npc(FacialExpression.SAD, "It's sad to say,", "this test beat you.", "I'll send you to Tiffy,", "what to do?")
                .endWith { _, player ->
                    removeAttribute(player, attributeClueNumber)
                    removeAttribute(player, RecruitmentDrive.attributeStagePassFailState)
                    RecruitmentDriveListeners.FailTestCutscene(player).start()
                }

    }
}

class DoorLockPuzzleInterfaceListener : InterfaceListener {

    companion object {
        const val temp = Components.RD_COMBOLOCK_285
        const val attributeLock1 = "quest:recruitmentdrive-lock1"
        const val attributeLock2 = "quest:recruitmentdrive-lock2"
        const val attributeLock3 = "quest:recruitmentdrive-lock3"
        const val attributeLock4 = "quest:recruitmentdrive-lock4"
        val lockArray = arrayOf(attributeLock1, attributeLock2, attributeLock3, attributeLock4)
        val answers = arrayOf("BITE", "FISH", "LAST", "MEAT", "RAIN", "TIME")
    }

    override fun defineInterfaceListeners() {

        onOpen(Components.RD_COMBOLOCK_285) { player, _ ->
            setAttribute(player, attributeLock1, 65)
            setAttribute(player, attributeLock2, 65)
            setAttribute(player, attributeLock3, 65)
            setAttribute(player, attributeLock4, 65)
            return@onOpen true
        }

        onClose(Components.RD_COMBOLOCK_285) { player, _ ->
            removeAttribute(player, attributeLock1)
            removeAttribute(player, attributeLock2)
            removeAttribute(player, attributeLock3)
            removeAttribute(player, attributeLock4)
            return@onClose true
        }

        on(Components.RD_COMBOLOCK_285) { player, component, opcode, buttonID, slot, itemID ->
            // Child IDs for respective locks:
            //         6                 7                 8                 9
            //  10 < Lock1 > 11   12 < Lock2 > 13   14 < Lock3 > 15   16 < Lock4 > 17
            if (buttonID in 10..17) {
                val position = (buttonID - 10) / 2
                val backForth = (buttonID - 10) % 2
                var newValue = getAttribute(player, lockArray[position], 65) + (if (backForth == 0) { -1 } else { 1 })
                if (newValue < 65) { newValue = 90 } // If char number is under A(65), loop back to Z(90)
                if (newValue > 90) { newValue = 65 } // If char number is over Z(90), loop back to A(65)
                setAttribute(player, lockArray[position], newValue)
                setInterfaceText(player, newValue.toChar().toString(), Components.RD_COMBOLOCK_285, position + 6)
            }
            // Enter Button
            if (buttonID == 18) {
                val lock1 = getAttribute(player, attributeLock1, 65).toChar()
                val lock2 = getAttribute(player, attributeLock2, 65).toChar()
                val lock3 = getAttribute(player, attributeLock3, 65).toChar()
                val lock4 = getAttribute(player, attributeLock4, 65).toChar()
                val answer = arrayOf(lock1, lock2, lock3, lock4).joinToString("")
                closeInterface(player)
                if (answers[getAttribute(player, SirRenItchwoodDialogueFile.attributeClueNumber, 0)] == answer){
                    removeAttribute(player, SirRenItchwoodDialogueFile.attributeClueNumber)
                    if (getAttribute(player, RecruitmentDrive.attributeStagePassFailState, 0) != -1) {
                        setAttribute(player, RecruitmentDrive.attributeStagePassFailState, 1)
                    }
                    sendNPCDialogue(player, NPCs.SIR_REN_ITCHOOD_2287, "Your wit is sharp, your brains quite clear; You solved my puzzle with no fear. At puzzles I rank you quite the best, now enter the portal for your next test.")
                } else {
                    removeAttribute(player, SirRenItchwoodDialogueFile.attributeClueNumber)
                    if (getAttribute(player, RecruitmentDrive.attributeStagePassFailState, 0) == 0) {
                        setAttribute(player, RecruitmentDrive.attributeStagePassFailState, -1)
                        openDialogue(player, SirRenItchwoodDialogueFile(2), NPC(NPCs.SIR_REN_ITCHOOD_2287))
                    }
                }
            }
            return@on true
        }
    }
}
