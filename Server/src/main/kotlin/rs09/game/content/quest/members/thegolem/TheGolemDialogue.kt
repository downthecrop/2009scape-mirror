package rs09.game.content.quest.members.thegolem

import api.*
import core.game.content.dialogue.DialoguePlugin
import core.game.content.dialogue.book.Book
import core.game.content.dialogue.book.BookLine
import core.game.content.dialogue.book.Page
import core.game.content.dialogue.book.PageSet
import core.game.node.Node
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.world.map.Location
import core.plugin.Initializable
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import rs09.game.content.dialogue.DialogueBuilder
import rs09.game.content.dialogue.DialogueBuilderFile
import rs09.game.content.dialogue.DialogueFile
import rs09.tools.END_DIALOGUE

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
            .endWith(){ player -> if (player.questRepository.getStage("The Golem") < 1 ) { setQuestStage(player, "The Golem", 1) } }
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
            .endWith() { player -> setQuestStage(player, "The Golem", 3) }
        b.onQuestStages("The Golem", 3)
            .npcl("The demon is still wounded. You must open the portal so that I can strike the final blow and complete my task.")
            .end()
        b.onQuestStages("The Golem", 4)
            .npcl("My task is incomplete. You must open the portal so I can defeat the great demon.")
            .playerl("It's ok, the demon is dead!")
            .npcl("The demon must be defeated...")
            .playerl("No, you don't understand. I saw the demon's skeleton. It must have died of its wounds.")
            .npcl("Demon must be defeated! Task incomplete.")
            .endWith() { player -> setQuestStage(player, "The Golem", 5) }
        b.onQuestStages("The Golem", 5)
            .npcl("Task incomplete.")
            .playerl("Oh, how am I going to convince you?")
            .endWith() { player -> setQuestStage(player, "The Golem", 6) }
        b.onQuestStages("The Golem", 6, 7)
            .npcl("My task is incomplete. You must open the portal so I can defeat the great demon.")
            .playerl("I already told you, he's dead!")
            .npcl("Task incomplete.")
            .playerl("Oh, how am I going to convince you?")
            .endWith() { player -> if(player.questRepository.getStage("The Golem") < 7) { setQuestStage(player, "The Golem", 7) } }
    }
}

class ClayGolemProgramDialogueFile : DialogueBuilderFile() {
    override fun create(b: DialogueBuilder) {
        b.onQuestStages("The Golem", 8)
            .npc("New instructions...", "Updating program...")
            .npcl("Task complete!")
            .npcl("Thank you. Now my mind is at rest.")
            .endWith() { player -> finishQuest(player, "The Golem") }
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

val VARMEN_NOTES = arrayOf(
    PageSet(
        Page(
            BookLine("Septober 19:", 55),
            BookLine("The nomads were right:", 56),
            BookLine("there is a city here,", 57),
            BookLine("probably buried for", 58),
            BookLine("millenia and revealed by", 59),
            BookLine("the random motions of", 60),
            BookLine("the sand. The", 61),
            BookLine("architecture is impressive", 62),
            BookLine("even in ruin, and must", 63),
            BookLine("once have been amazing.", 64),
            BookLine("One puzzling factor is the", 65),
        ),
        Page(
            BookLine("pottery -- there are", 66),
            BookLine("fragments all over the", 67),
            BookLine("ruins, surely too much", 68),
            BookLine("for a city even of this", 69),
            BookLine("size. We have set up", 70),
            BookLine("camp and will do a proper", 71),
            BookLine("survey tomorrow.", 72),
        )
    ),
    PageSet(
        Page(
            BookLine("Septober 20:", 55),
            BookLine("The meaning of the", 56),
            BookLine("pottery was explained", 57),
            BookLine("today in a most", 58),
            BookLine("surprising manner. We", 59),
            BookLine("found a mostly-intact clay", 60),
            BookLine("statue buried up to its", 61),
            BookLine("waist in sand, and as", 62),
            BookLine("soon as we dug it out, it", 63),
            BookLine("started to walk around! It", 64),
            BookLine("is a clay golem, built by", 65),
        ),
        Page(
            BookLine("the city's inhabitants and", 66),
            BookLine("dormant all this time. Its", 67),
            BookLine("head is badly damaged", 68),
            BookLine("and it is", 69),
            BookLine("uncommunicative, but its", 70),
            BookLine("existence tells us that the", 71),
            BookLine("city's inhabitants were", 72),
            BookLine("expert magical craftsmen.", 73),
            BookLine("The huge kilns in some", 74),
            BookLine("of the buildings indicate", 75),
            BookLine("that at some point before", 76),
        )
    ),
    PageSet(
        Page(
            BookLine("its destruction the whole", 55),
            BookLine("city was converted to the", 56),
            BookLine("manufacture of these", 57),
            BookLine("golems.", 58),
            BookLine("", 59),
            BookLine("We have also examined", 60),
            BookLine("the carvings on the large", 61),
            BookLine("building in the centre.", 62),
            BookLine("These are symbols", 63),
            BookLine("depicting several of the", 64),
            BookLine("ancient gods, including", 65),
        ),
        Page(
            BookLine("Saradomin, Zamorak, and", 66),
            BookLine("Armadyl, but there is", 67),
            BookLine("another prominent symbol", 68),
            BookLine("that I cannot identify. As", 69),
            BookLine("it seems we will need to", 70),
            BookLine("be here for longer than I", 71),
            BookLine("had thought, I have sent", 72),
            BookLine("to Elissa for books on", 73),
            BookLine("golems and religious", 74),
            BookLine("symbols.", 75),
        )
    ),
    PageSet(
        Page(
            BookLine("Septober 21:", 55),
            BookLine("As we examine the ruins", 56),
            BookLine("one thing becomes", 57),
            BookLine("increasingly clear: most", 58),
            BookLine("of the damage was not", 59),
            BookLine("due to weathering. The", 60),
            BookLine("buildings were destroyed", 61),
            BookLine("by force, as if torn down", 62),
            BookLine("by giant hands.", 63),
        ),
        Page(
            BookLine("Septober 22:", 66),
            BookLine("A breakthrough! We have", 67),
            BookLine("found the staircase into", 68),
            BookLine("the lower levels of the", 69),
            BookLine("temple. This part has", 70),
            BookLine("been untouched by the", 71),
            BookLine("elements, and the", 72),
            BookLine("carvings here are more", 73),
            BookLine("intact, especially four", 74),
            BookLine("beautiful statuettes in", 75),
            BookLine("alcoves framing the large", 76),
        )
    ),
    PageSet(
        Page(
            BookLine("door. I have removed one", 55),
            BookLine("of them. The door will", 56),
            BookLine("not open. I am glad I", 57),
            BookLine("sent for a book on", 58),
            BookLine("symbols, as the", 59),
            BookLine("unidentified symbol is", 60),
            BookLine("even more prominent", 61),
            BookLine("here, especially on the", 62),
            BookLine("door.", 63),
        ),
        Page(
            BookLine("Septober 23:", 66),
            BookLine("Our messenger returned", 67),
            BookLine("with the books I asked for", 68),
            BookLine("and a letter from Elissa.", 69),
            BookLine("It is unfortunate that the", 70),
            BookLine("museum will not be able", 71),
            BookLine("to finance a full-scale", 72),
            BookLine("excavation here as well as", 73),
            BookLine("the one closer to Varrock,", 74),
            BookLine("although I am of course", 75),
            BookLine("pleased that the other city", 76),
        )
    ),
    PageSet(
        Page(
            BookLine("has been uncovered. But", 55),
            BookLine("with the books I am able", 56),
            BookLine("to piece together more of", 57),
            BookLine("the story of this city.", 58),
            BookLine("", 59),
            BookLine("The unidentified symbol", 60),
            BookLine("in the ruins is that of the", 61),
            BookLine("demon Thammaron, who", 62),
            BookLine("was Zamorak's chief", 63),
            BookLine("lieutenant during the", 64),
            BookLine("godwars of the Third", 65),
        ),
        Page(
            BookLine("Age. With that", 66),
            BookLine("information I can say", 67),
            BookLine("with confidence that these", 68),
            BookLine("are the ruins of Uzer, an", 69),
            BookLine("advanced human", 70),
            BookLine("civilization said to have", 71),
            BookLine("been destroyed towards", 72),
            BookLine("the end of the Third Age", 73),
            BookLine("(roughly 2,500 years", 74),
            BookLine("ago). It was allied with", 75),
            BookLine("Saradomin and enjoyed", 76),
        )
    ),
    PageSet(
        Page(
            BookLine("his protection, as well as", 55),
            BookLine("that of its own mages and", 56),
            BookLine("warriors. Thammaron was", 57),
            BookLine("able to open a portal from", 58),
            BookLine("his own domain straight", 59),
            BookLine("into the heart of the city,", 60),
            BookLine("bypassing its defences.", 61),
            BookLine("With Saradomin's help the", 62),
            BookLine("army of Uzer was able to", 63),
            BookLine("drive Thammaron back,", 64),
            BookLine("but the record ends at", 65),
        ),
        Page(
            BookLine("that point and it has", 66),
            BookLine("always been assumed that", 67),
            BookLine("a later attack, either by", 68),
            BookLine("Thammaron or by", 69),
            BookLine("Zamorak's other forces,", 70),
            BookLine("finished the city off.", 71),
            BookLine("", 72),
            BookLine("Examining the door", 73),
            BookLine("again, I now see that it is", 74),
            BookLine("exactly the sort of door", 75),
            BookLine("that could be used to seal", 76),
        )
    ),
    PageSet(
        Page(
            BookLine("Thammaron's portal. I am", 55),
            BookLine("suddently glad I was not", 56),
            BookLine("able to open it! I surmise", 57),
            BookLine("that the army of golems", 58),
            BookLine("was created in order to", 59),
            BookLine("fight the demon, since", 60),
            BookLine("Uzer's army had been", 61),
            BookLine("wiped out and", 62),
            BookLine("Saradomin's forces were", 63),
            BookLine("increasingly stretched.", 64),
            BookLine("However, this approach", 65),
        ),
        Page(
            BookLine("evidently failed, since the", 66),
            BookLine("city was eventually", 67),
            BookLine("destroyed.", 68),
            BookLine("", 69),
            BookLine("The art of the", 70),
            BookLine("construction of golems", 71),
            BookLine("has been lost since the", 72),
            BookLine("Third Age, and, although", 73),
            BookLine("they are sometimes", 74),
            BookLine("discovered lying dormant", 75),
            BookLine("in the ground, no", 76),
        )
    ),
    PageSet(
        Page(
            BookLine("concerted effort has been", 55),
            BookLine("made to regain it, thanks", 56),
            BookLine("largely to the modern", 57),
            BookLine("Saradomist Church's view", 58),
            BookLine("of them as unnatural.", 59),
            BookLine("This view is without", 60),
            BookLine("foundation, as golems are", 61),
            BookLine("neither good nor evil but", 62),
            BookLine("follow instructions they", 63),
            BookLine("are given to the letter", 64),
            BookLine("and without imagination,", 65),
        ),
        Page(
            BookLine("indeed experiencing", 66),
            BookLine("extreme discomfort for as", 67),
            BookLine("long as a task assigned to", 68),
            BookLine("them remains incomplete.", 69),
            BookLine("Some golems were", 70),
            BookLine("constructed to obey", 71),
            BookLine("verbal instructions, but", 72),
            BookLine("the main method of", 73),
            BookLine("instruction was to place", 74),
            BookLine("magical words into the", 75),
            BookLine("golem's skull cavity.", 76),
        )
    ),
    PageSet(
        Page(
            BookLine("These were written on", 55),
            BookLine("papyrus using a naturally", 56),
            BookLine("occurring source of ink,", 57),
            BookLine("and their magical power", 58),
            BookLine("derived from the use of a", 59),
            BookLine("phoenix tail feather as a", 60),
            BookLine("pen. These would be used", 61),
            BookLine("for long-term or", 62),
            BookLine("important tasks, and", 63),
            BookLine("would override any verbal", 64),
            BookLine("instructions.", 65),
        )
    )
)

@Initializable
class VarmensNotesHandler : Book {
    constructor() {}
    constructor(player: Player?) : super(player, "The Ruins of Uzer", Items.VARMENS_NOTES_4616, *VARMEN_NOTES) {}
    override fun finish() {
        player.setAttribute("/save:the-golem:varmen-notes-read", true)
    }

    override fun display(set: Array<Page>) {
        player.interfaceManager.open(getInterface())

        for (i in 55..76) {
            player.packetDispatch.sendString("", getInterface().id, i)
        }
        player.packetDispatch.sendString("", getInterface().id, 77)
        player.packetDispatch.sendString("", getInterface().id, 78)
        player.packetDispatch.sendString(getName(), getInterface().id, 6)
        for (page in set) {
            for (line in page.lines) {
                player.packetDispatch.sendString(line.message, getInterface().id, line.child)
            }
        }
        player.packetDispatch.sendInterfaceConfig(getInterface().id, 51, index < 1)
        val lastPage = index == sets.size - 1
        player.packetDispatch.sendInterfaceConfig(getInterface().id, 53, lastPage)
        if (lastPage) {
            finish()
        }
    }

    override fun newInstance(player: Player): DialoguePlugin {
        return VarmensNotesHandler(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(Items.VARMENS_NOTES_4616)
    }
}

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
