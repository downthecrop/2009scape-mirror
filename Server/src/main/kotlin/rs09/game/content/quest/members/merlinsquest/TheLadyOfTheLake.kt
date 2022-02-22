package rs09.game.content.quest.members.merlinsquest

import core.game.content.dialogue.DialoguePlugin
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.item.Item
import org.rs09.consts.Items

/**
 * Handles the LadyOfTheLake dialogue.
 *
 * @author Vexia
 * @author Splinter
 * @author pain
 */
class TheLadyOfTheLake(player: Player? = null) : DialoguePlugin(player) {

    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        stage = if (!player.hasItem(Item(35, 1))) {
            npc("Good day to you, " + (if (player.isMale) "sir" else "madam") + ".")
            0
        } else if(player.achievementDiaryManager.getDiary(DiaryType.SEERS_VILLAGE).isComplete(2)
            && player.equipment.contains(14631,1) && player.equipment.contains(35,1)){
            npcl(FacialExpression.HAPPY,"I am the Lady of the Lake.")
            110
        }
        else {
            npc("Good day to you, " + (if (player.isMale) "sir" else "madam") + ".")
            699
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        val quest = player.questRepository.getQuest("Merlin's Crystal")
        when (stage) {
            0 -> options("Who are you?", "I seek the sword Excalibur.", "Good day.").also { stage = 1 }
            1 -> when (buttonId) {
                1 -> player("Who are you?").also { stage = 100 }
                2 -> player("I seek the sword Excalibur.").also {
                        if (quest.getStage(player) < 50) {
                            stage = 250
                        } else {
                            stage = 161
                        }
                    }
                3 -> player("Good day.").also { stage = 10000 }
            }
            100 -> npcl(FacialExpression.HAPPY,"I am the Lady of the Lake.").also { stage = 145 }
            110 -> player("And I'm-").also { stage++ }
            111 -> npc(
                    "You're " + player.name + ". And I see from the sign you",
                    "wear that you have earned the trust of Sir Kay."
                ).also { stage++ }
            112 -> player("It was nothing.. really...").also { stage++ }
            113 -> npc("You shall be rewarded handsomely!").also { stage++ }
            114 -> {
                interpreter.sendItemMessage(
                    Items.EXCALIBUR_35,
                    "The Lady of the Lake reaches out and touches the",
                    "blade Excalibur which seems to vibrate with new power."
                ).also {
                    if (player.inventory.containsAtLeastOneItem(Items.EXCALIBUR_35)) {
                    player.inventory.remove(Item(Items.EXCALIBUR_35))
                    player.inventory.add(Item(14632))
                }
                    else if (player.equipment.containsAtLeastOneItem(Items.EXCALIBUR_35)) {
                    player.equipment.remove(Item(Items.EXCALIBUR_35))
                    player.equipment.add(Item(14632), true, false)
                }
                    stage++
                }
            }
            115 -> player("What does this do then?").also { stage++ }
            116 -> npc(
                    "I made the blade more powerful, and also gave it a",
                    "rather healthy effect when you use the special."
                ).also { stage++ }
            117 -> player("Thanks!").also { stage = 10000 }
            145 -> options("I seek the sword Excalibur.", "Good day.").also { stage = 150 }
            150 -> when (buttonId) {
                1 -> player("I seek the sword Excalibur.").also { stage = 161 }
                2 -> player("Good day.").also { stage = 10000 }
            }
            161 -> if (quest.getStage(player) == 50 || quest.getStage(player) == 60) { // they haven't proven themselves yet
                    npc("Aye, I have that artefact in my possession.").also { stage = 300 }
            } else if (quest.getStage(player) >= 70) {
                    npc(
                        "... But you have already proved thyself to be worthy",
                        "of wielding it once already. I shall return it to you",
                        "if you can prove yourself to still be worthy.").also { stage = 162 }
            }
            162 -> player("... And how can I do that?").also { stage = 163 }
            163 -> npc("Why, by proving yourself to be above material goods.").also { stage = 164 }

            164 -> npc("500 coins ought to do it.").also { stage = 166 }
            166 -> if (player.inventory.contains(995, 500)) {
                player("Ok, here you go.").also { stage = 168 }
            } else {
                player("I don't have that kind of money...").also { stage = 167 }
            }
            167 -> npc("Well, come back when you do.").also { stage = 10000 }
            168 -> if (player.inventory.freeSlots() == 0) {
                player("Sorry, I don't seem to have enough inventory space.").also { stage = 10000 }
            } else if (player.inventory.contains(995, 500)) {
                player.inventory.remove(Item(995, 500))
                player.inventory.add(Item(35, 1))
                npc(
                    "You are still worthy to wield Excalibur! And thanks",
                    "for the cash! I felt like getting a new haircut!"
                )
                stage = 170
            } else {
                player("I don't have that kind of money...").also { stage = 167 }
            }
            170 -> interpreter.sendDialogue("The lady of the Lake hands you Excalibur.").also { stage = 10000 }

            250 -> npc("I am afraid I do not know what you are talking about.").also { stage = 10000 }

            300 -> npc("'Tis very valuable, and not an artefact to be given", "away lightly.").also { stage = 301 }
            301 -> npc("I would want to give it away only to one who is worthy", "and good.").also { stage = 302 }
            302 -> player("And how am I meant to prove that?").also { stage = 303 }
            303 -> npc("I shall set a test for you.").also { stage = 304 }
            304 -> npc(
                    "First I need you to travel to Port Sarim. Then go to",
                    "the upstairs room of the jeweller's shop there."
                ).also { stage = 305 }
            305 -> player("Ok. That seems easy enough.").also {
                quest.setStage(player, 60)
                stage = 10000
                }
            699 -> options("Who are you?", "Good day.").also { stage = 700 }
            700 -> when (buttonId) {
                1 -> player("Who are you?").also { stage = 720 }
                2 -> player("Good day.").also { stage = 10000 }
            }
            720 -> npc("I am the Lady of the Lake.").also { stage = 10000 }
            10000 -> end()
        }
        return true
    }

    override fun newInstance(player: Player): DialoguePlugin {
        return TheLadyOfTheLake(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(250)
    }
}