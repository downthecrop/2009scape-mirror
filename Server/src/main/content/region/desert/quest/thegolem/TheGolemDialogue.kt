package content.region.desert.quest.thegolem

import core.api.*
import core.game.dialogue.DialoguePlugin
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.NPCs
import core.game.dialogue.DialogueBuilder
import core.game.dialogue.DialogueBuilderFile

@Initializable
public final class ClayGolemDialoguePlugin(player: Player? = null) : DialoguePlugin(player) {
    override fun newInstance(player: Player): DialoguePlugin {
        return ClayGolemDialoguePlugin(player)
    }
    override fun open(vararg objects: Any?): Boolean {
        npc = objects[0] as NPC
        player.dialogueInterpreter.open(ClayGolemDialogueFile(), npc)
        return true
    }
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        return false
    }
    override fun getIds(): IntArray {
        return intArrayOf(1907, NPCs.BROKEN_CLAY_GOLEM_1908, NPCs.DAMAGED_CLAY_GOLEM_1909, NPCs.CLAY_GOLEM_1910)
    }
}

class ClayGolemDialogueFile : DialogueBuilderFile() {
    override fun create(b: DialogueBuilder) {
        val opt1 = b.onQuestStages("The Golem", 0)
            .npc("Damage... severe...", "task... incomplete...")
            .options()
        opt1
            .optionIf("Shall I try to repair you?") { player -> return@optionIf player.questRepository.getQuest("The Golem").hasRequirements(player) }
            .playerl("Shall I try to repair you?")
            .npcl("Repairs... needed...")
            .endWith(){ _, player -> if (player.questRepository.getStage("The Golem") < 1 ) { setQuestStage(player, "The Golem", 1) } }
        opt1
            .option("I'm not going to find a conversation here!")
            .playerl("I'm not going to find a conversation here!")
            .end()
        b.onQuestStages("The Golem", 1)
            .npcl("Repairs... needed...")
            .end()
        b.onQuestStages("The Golem", 2)
            .npcl("Damage repaired...")
            .npcl("Thank you. My body and mind are fully healed.")
            .npcl("Now I must complete my task by defeating the great enemy.")
            .playerl("What enemy?")
            .npcl("A great demon. It broke through from its dimension to attack the city.")
            .npcl("The golem army was created to fight it. Many were destroyed, but we drove the demon back!")
            .npcl("The demon is still wounded. You must open the portal so that I can strike the final blow and complete my task.")
            .endWith() { _, player -> setQuestStage(player, "The Golem", 3) }
        b.onQuestStages("The Golem", 3)
            .npcl("The demon is still wounded. You must open the portal so that I can strike the final blow and complete my task.")
            .end()
        b.onQuestStages("The Golem", 4)
            .npcl("My task is incomplete. You must open the portal so I can defeat the great demon.")
            .playerl("It's ok, the demon is dead!")
            .npcl("The demon must be defeated...")
            .playerl("No, you don't understand. I saw the demon's skeleton. It must have died of its wounds.")
            .npcl("Demon must be defeated! Task incomplete.")
            .endWith() { _, player -> setQuestStage(player, "The Golem", 5) }
        b.onQuestStages("The Golem", 5)
            .npcl("Task incomplete.")
            .playerl("Oh, how am I going to convince you?")
            .endWith() { _, player -> setQuestStage(player, "The Golem", 6) }
        b.onQuestStages("The Golem", 6, 7)
            .npcl("My task is incomplete. You must open the portal so I can defeat the great demon.")
            .playerl("I already told you, he's dead!")
            .npcl("Task incomplete.")
            .playerl("Oh, how am I going to convince you?")
            .endWith() { df, player -> if(player.questRepository.getStage("The Golem") < 7) { setQuestStage(player, "The Golem", 7) } }
    }
}

class ClayGolemProgramDialogueFile : DialogueBuilderFile() {
    override fun create(b: DialogueBuilder) {
        b.onQuestStages("The Golem", 8)
            .npc("New instructions...", "Updating program...")
            .npcl("Task complete!")
            .npcl("Thank you. Now my mind is at rest.")
            .endWith() { _, player -> finishQuest(player, "The Golem") }
    }
}

class CuratorHaigHalenGolemDialogue : DialogueBuilderFile() {
    override fun create(b: DialogueBuilder) {
        val opt1 = b.onQuestStages("The Golem", 3)
            .npcl("Ah yes, a very impressive artefact. The people of that city were excellent sculptors.")
            .npcl("It's in the display case upstairs.")
            .playerl("No, I need to take it away with me.")
            .npcl("What do you want it for?")
            .options()
        opt1
            .option("I want to open a portal to the lair of an elder-demon.")
            .playerl("I want to open a portal to the lair of an elder-demon.")
            .npcl("Good heavens! I'd never let you do such a dangerous thing.")
            .end()
        opt1
            .option("Well, I, er, just want it.")
            .playerl("Well, I, er, just want it.")
            .end()
    }
}

val LETTER_LINES = arrayOf(
    "",
    "",
    "Dearest Varmen,",
    "I hope this finds you well. Here are the books you asked for",
    "There has been an exciting development closer to home --",
    "another city from the same period has been discovered east",
    "of Varrock, and we are starting a huge excavation project",
    "here. I don't know if the museum will be able to finance your",
    "expedition as well as this one, so I fear your current trip will be",
    "the last.",
    "May Saradomin grant you a safe journey home",
    "Your loving Elissa.",
    )

val DISPLAY_CASE_TEXT = arrayOf("3rd age - yr 3000-4000",
    "",
    "This statuette was found in an underground",
    "temple in the ruined city of Uzer, which was",
    "destroyed late in the 3rd Age, suddenly, due to",
    "causes unknown. It probably represents one of",
    "the clay golems that the craftsmen of the city",
    "built as warriors and servants. The statuette",
    "was originally part of a mechanism whose",
    "purpose is unknown.")
