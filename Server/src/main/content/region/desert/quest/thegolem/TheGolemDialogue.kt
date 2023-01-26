package content.region.desert.quest.thegolem

import core.api.*
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.book.Book
import core.game.dialogue.book.BookLine
import core.game.dialogue.book.Page
import core.game.dialogue.book.PageSet
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import core.game.dialogue.DialogueBuilder
import core.game.dialogue.DialogueBuilderFile

@Initializable
public final class ClayGolemDialoguePlugin(player: Player? = null) : core.game.dialogue.DialoguePlugin(player) {
    override fun newInstance(player: Player): core.game.dialogue.DialoguePlugin {
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

class ClayGolemDialogueFile : core.game.dialogue.DialogueBuilderFile() {
    override fun create(b: core.game.dialogue.DialogueBuilder) {
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

class ClayGolemProgramDialogueFile : core.game.dialogue.DialogueBuilderFile() {
    override fun create(b: core.game.dialogue.DialogueBuilder) {
        b.onQuestStages("The Golem", 8)
            .npc("New instructions...", "Updating program...")
            .npcl("Task complete!")
            .npcl("Thank you. Now my mind is at rest.")
            .endWith() { player -> finishQuest(player, "The Golem") }
    }
}

class CuratorHaigHalenGolemDialogue : core.game.dialogue.DialogueBuilderFile() {
    override fun create(b: core.game.dialogue.DialogueBuilder) {
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
        core.game.dialogue.book.PageSet(
                core.game.dialogue.book.Page(
                        core.game.dialogue.book.BookLine("Septober 19:", 55),
                        core.game.dialogue.book.BookLine("The nomads were right:", 56),
                        core.game.dialogue.book.BookLine("there is a city here,", 57),
                        core.game.dialogue.book.BookLine("probably buried for", 58),
                        core.game.dialogue.book.BookLine("millenia and revealed by", 59),
                        core.game.dialogue.book.BookLine("the random motions of", 60),
                        core.game.dialogue.book.BookLine("the sand. The", 61),
                        core.game.dialogue.book.BookLine("architecture is impressive", 62),
                        core.game.dialogue.book.BookLine("even in ruin, and must", 63),
                        core.game.dialogue.book.BookLine("once have been amazing.", 64),
                        core.game.dialogue.book.BookLine("One puzzling factor is the", 65),
                ),
                core.game.dialogue.book.Page(
                        core.game.dialogue.book.BookLine("pottery -- there are", 66),
                        core.game.dialogue.book.BookLine("fragments all over the", 67),
                        core.game.dialogue.book.BookLine("ruins, surely too much", 68),
                        core.game.dialogue.book.BookLine("for a city even of this", 69),
                        core.game.dialogue.book.BookLine("size. We have set up", 70),
                        core.game.dialogue.book.BookLine("camp and will do a proper", 71),
                        core.game.dialogue.book.BookLine("survey tomorrow.", 72),
                )
        ),
        core.game.dialogue.book.PageSet(
                core.game.dialogue.book.Page(
                        core.game.dialogue.book.BookLine("Septober 20:", 55),
                        core.game.dialogue.book.BookLine("The meaning of the", 56),
                        core.game.dialogue.book.BookLine("pottery was explained", 57),
                        core.game.dialogue.book.BookLine("today in a most", 58),
                        core.game.dialogue.book.BookLine("surprising manner. We", 59),
                        core.game.dialogue.book.BookLine("found a mostly-intact clay", 60),
                        core.game.dialogue.book.BookLine("statue buried up to its", 61),
                        core.game.dialogue.book.BookLine("waist in sand, and as", 62),
                        core.game.dialogue.book.BookLine("soon as we dug it out, it", 63),
                        core.game.dialogue.book.BookLine("started to walk around! It", 64),
                        core.game.dialogue.book.BookLine("is a clay golem, built by", 65),
                ),
                core.game.dialogue.book.Page(
                        core.game.dialogue.book.BookLine("the city's inhabitants and", 66),
                        core.game.dialogue.book.BookLine("dormant all this time. Its", 67),
                        core.game.dialogue.book.BookLine("head is badly damaged", 68),
                        core.game.dialogue.book.BookLine("and it is", 69),
                        core.game.dialogue.book.BookLine("uncommunicative, but its", 70),
                        core.game.dialogue.book.BookLine("existence tells us that the", 71),
                        core.game.dialogue.book.BookLine("city's inhabitants were", 72),
                        core.game.dialogue.book.BookLine("expert magical craftsmen.", 73),
                        core.game.dialogue.book.BookLine("The huge kilns in some", 74),
                        core.game.dialogue.book.BookLine("of the buildings indicate", 75),
                        core.game.dialogue.book.BookLine("that at some point before", 76),
                )
        ),
        core.game.dialogue.book.PageSet(
                core.game.dialogue.book.Page(
                        core.game.dialogue.book.BookLine("its destruction the whole", 55),
                        core.game.dialogue.book.BookLine("city was converted to the", 56),
                        core.game.dialogue.book.BookLine("manufacture of these", 57),
                        core.game.dialogue.book.BookLine("golems.", 58),
                        core.game.dialogue.book.BookLine("", 59),
                        core.game.dialogue.book.BookLine("We have also examined", 60),
                        core.game.dialogue.book.BookLine("the carvings on the large", 61),
                        core.game.dialogue.book.BookLine("building in the centre.", 62),
                        core.game.dialogue.book.BookLine("These are symbols", 63),
                        core.game.dialogue.book.BookLine("depicting several of the", 64),
                        core.game.dialogue.book.BookLine("ancient gods, including", 65),
                ),
                core.game.dialogue.book.Page(
                        core.game.dialogue.book.BookLine("Saradomin, Zamorak, and", 66),
                        core.game.dialogue.book.BookLine("Armadyl, but there is", 67),
                        core.game.dialogue.book.BookLine("another prominent symbol", 68),
                        core.game.dialogue.book.BookLine("that I cannot identify. As", 69),
                        core.game.dialogue.book.BookLine("it seems we will need to", 70),
                        core.game.dialogue.book.BookLine("be here for longer than I", 71),
                        core.game.dialogue.book.BookLine("had thought, I have sent", 72),
                        core.game.dialogue.book.BookLine("to Elissa for books on", 73),
                        core.game.dialogue.book.BookLine("golems and religious", 74),
                        core.game.dialogue.book.BookLine("symbols.", 75),
                )
        ),
        core.game.dialogue.book.PageSet(
                core.game.dialogue.book.Page(
                        core.game.dialogue.book.BookLine("Septober 21:", 55),
                        core.game.dialogue.book.BookLine("As we examine the ruins", 56),
                        core.game.dialogue.book.BookLine("one thing becomes", 57),
                        core.game.dialogue.book.BookLine("increasingly clear: most", 58),
                        core.game.dialogue.book.BookLine("of the damage was not", 59),
                        core.game.dialogue.book.BookLine("due to weathering. The", 60),
                        core.game.dialogue.book.BookLine("buildings were destroyed", 61),
                        core.game.dialogue.book.BookLine("by force, as if torn down", 62),
                        core.game.dialogue.book.BookLine("by giant hands.", 63),
                ),
                core.game.dialogue.book.Page(
                        core.game.dialogue.book.BookLine("Septober 22:", 66),
                        core.game.dialogue.book.BookLine("A breakthrough! We have", 67),
                        core.game.dialogue.book.BookLine("found the staircase into", 68),
                        core.game.dialogue.book.BookLine("the lower levels of the", 69),
                        core.game.dialogue.book.BookLine("temple. This part has", 70),
                        core.game.dialogue.book.BookLine("been untouched by the", 71),
                        core.game.dialogue.book.BookLine("elements, and the", 72),
                        core.game.dialogue.book.BookLine("carvings here are more", 73),
                        core.game.dialogue.book.BookLine("intact, especially four", 74),
                        core.game.dialogue.book.BookLine("beautiful statuettes in", 75),
                        core.game.dialogue.book.BookLine("alcoves framing the large", 76),
                )
        ),
        core.game.dialogue.book.PageSet(
                core.game.dialogue.book.Page(
                        core.game.dialogue.book.BookLine("door. I have removed one", 55),
                        core.game.dialogue.book.BookLine("of them. The door will", 56),
                        core.game.dialogue.book.BookLine("not open. I am glad I", 57),
                        core.game.dialogue.book.BookLine("sent for a book on", 58),
                        core.game.dialogue.book.BookLine("symbols, as the", 59),
                        core.game.dialogue.book.BookLine("unidentified symbol is", 60),
                        core.game.dialogue.book.BookLine("even more prominent", 61),
                        core.game.dialogue.book.BookLine("here, especially on the", 62),
                        core.game.dialogue.book.BookLine("door.", 63),
                ),
                core.game.dialogue.book.Page(
                        core.game.dialogue.book.BookLine("Septober 23:", 66),
                        core.game.dialogue.book.BookLine("Our messenger returned", 67),
                        core.game.dialogue.book.BookLine("with the books I asked for", 68),
                        core.game.dialogue.book.BookLine("and a letter from Elissa.", 69),
                        core.game.dialogue.book.BookLine("It is unfortunate that the", 70),
                        core.game.dialogue.book.BookLine("museum will not be able", 71),
                        core.game.dialogue.book.BookLine("to finance a full-scale", 72),
                        core.game.dialogue.book.BookLine("excavation here as well as", 73),
                        core.game.dialogue.book.BookLine("the one closer to Varrock,", 74),
                        core.game.dialogue.book.BookLine("although I am of course", 75),
                        core.game.dialogue.book.BookLine("pleased that the other city", 76),
                )
        ),
        core.game.dialogue.book.PageSet(
                core.game.dialogue.book.Page(
                        core.game.dialogue.book.BookLine("has been uncovered. But", 55),
                        core.game.dialogue.book.BookLine("with the books I am able", 56),
                        core.game.dialogue.book.BookLine("to piece together more of", 57),
                        core.game.dialogue.book.BookLine("the story of this city.", 58),
                        core.game.dialogue.book.BookLine("", 59),
                        core.game.dialogue.book.BookLine("The unidentified symbol", 60),
                        core.game.dialogue.book.BookLine("in the ruins is that of the", 61),
                        core.game.dialogue.book.BookLine("demon Thammaron, who", 62),
                        core.game.dialogue.book.BookLine("was Zamorak's chief", 63),
                        core.game.dialogue.book.BookLine("lieutenant during the", 64),
                        core.game.dialogue.book.BookLine("godwars of the Third", 65),
                ),
                core.game.dialogue.book.Page(
                        core.game.dialogue.book.BookLine("Age. With that", 66),
                        core.game.dialogue.book.BookLine("information I can say", 67),
                        core.game.dialogue.book.BookLine("with confidence that these", 68),
                        core.game.dialogue.book.BookLine("are the ruins of Uzer, an", 69),
                        core.game.dialogue.book.BookLine("advanced human", 70),
                        core.game.dialogue.book.BookLine("civilization said to have", 71),
                        core.game.dialogue.book.BookLine("been destroyed towards", 72),
                        core.game.dialogue.book.BookLine("the end of the Third Age", 73),
                        core.game.dialogue.book.BookLine("(roughly 2,500 years", 74),
                        core.game.dialogue.book.BookLine("ago). It was allied with", 75),
                        core.game.dialogue.book.BookLine("Saradomin and enjoyed", 76),
                )
        ),
        core.game.dialogue.book.PageSet(
                core.game.dialogue.book.Page(
                        core.game.dialogue.book.BookLine("his protection, as well as", 55),
                        core.game.dialogue.book.BookLine("that of its own mages and", 56),
                        core.game.dialogue.book.BookLine("warriors. Thammaron was", 57),
                        core.game.dialogue.book.BookLine("able to open a portal from", 58),
                        core.game.dialogue.book.BookLine("his own domain straight", 59),
                        core.game.dialogue.book.BookLine("into the heart of the city,", 60),
                        core.game.dialogue.book.BookLine("bypassing its defences.", 61),
                        core.game.dialogue.book.BookLine("With Saradomin's help the", 62),
                        core.game.dialogue.book.BookLine("army of Uzer was able to", 63),
                        core.game.dialogue.book.BookLine("drive Thammaron back,", 64),
                        core.game.dialogue.book.BookLine("but the record ends at", 65),
                ),
                core.game.dialogue.book.Page(
                        core.game.dialogue.book.BookLine("that point and it has", 66),
                        core.game.dialogue.book.BookLine("always been assumed that", 67),
                        core.game.dialogue.book.BookLine("a later attack, either by", 68),
                        core.game.dialogue.book.BookLine("Thammaron or by", 69),
                        core.game.dialogue.book.BookLine("Zamorak's other forces,", 70),
                        core.game.dialogue.book.BookLine("finished the city off.", 71),
                        core.game.dialogue.book.BookLine("", 72),
                        core.game.dialogue.book.BookLine("Examining the door", 73),
                        core.game.dialogue.book.BookLine("again, I now see that it is", 74),
                        core.game.dialogue.book.BookLine("exactly the sort of door", 75),
                        core.game.dialogue.book.BookLine("that could be used to seal", 76),
                )
        ),
        core.game.dialogue.book.PageSet(
                core.game.dialogue.book.Page(
                        core.game.dialogue.book.BookLine("Thammaron's portal. I am", 55),
                        core.game.dialogue.book.BookLine("suddently glad I was not", 56),
                        core.game.dialogue.book.BookLine("able to open it! I surmise", 57),
                        core.game.dialogue.book.BookLine("that the army of golems", 58),
                        core.game.dialogue.book.BookLine("was created in order to", 59),
                        core.game.dialogue.book.BookLine("fight the demon, since", 60),
                        core.game.dialogue.book.BookLine("Uzer's army had been", 61),
                        core.game.dialogue.book.BookLine("wiped out and", 62),
                        core.game.dialogue.book.BookLine("Saradomin's forces were", 63),
                        core.game.dialogue.book.BookLine("increasingly stretched.", 64),
                        core.game.dialogue.book.BookLine("However, this approach", 65),
                ),
                core.game.dialogue.book.Page(
                        core.game.dialogue.book.BookLine("evidently failed, since the", 66),
                        core.game.dialogue.book.BookLine("city was eventually", 67),
                        core.game.dialogue.book.BookLine("destroyed.", 68),
                        core.game.dialogue.book.BookLine("", 69),
                        core.game.dialogue.book.BookLine("The art of the", 70),
                        core.game.dialogue.book.BookLine("construction of golems", 71),
                        core.game.dialogue.book.BookLine("has been lost since the", 72),
                        core.game.dialogue.book.BookLine("Third Age, and, although", 73),
                        core.game.dialogue.book.BookLine("they are sometimes", 74),
                        core.game.dialogue.book.BookLine("discovered lying dormant", 75),
                        core.game.dialogue.book.BookLine("in the ground, no", 76),
                )
        ),
        core.game.dialogue.book.PageSet(
                core.game.dialogue.book.Page(
                        core.game.dialogue.book.BookLine("concerted effort has been", 55),
                        core.game.dialogue.book.BookLine("made to regain it, thanks", 56),
                        core.game.dialogue.book.BookLine("largely to the modern", 57),
                        core.game.dialogue.book.BookLine("Saradomist Church's view", 58),
                        core.game.dialogue.book.BookLine("of them as unnatural.", 59),
                        core.game.dialogue.book.BookLine("This view is without", 60),
                        core.game.dialogue.book.BookLine("foundation, as golems are", 61),
                        core.game.dialogue.book.BookLine("neither good nor evil but", 62),
                        core.game.dialogue.book.BookLine("follow instructions they", 63),
                        core.game.dialogue.book.BookLine("are given to the letter", 64),
                        core.game.dialogue.book.BookLine("and without imagination,", 65),
                ),
                core.game.dialogue.book.Page(
                        core.game.dialogue.book.BookLine("indeed experiencing", 66),
                        core.game.dialogue.book.BookLine("extreme discomfort for as", 67),
                        core.game.dialogue.book.BookLine("long as a task assigned to", 68),
                        core.game.dialogue.book.BookLine("them remains incomplete.", 69),
                        core.game.dialogue.book.BookLine("Some golems were", 70),
                        core.game.dialogue.book.BookLine("constructed to obey", 71),
                        core.game.dialogue.book.BookLine("verbal instructions, but", 72),
                        core.game.dialogue.book.BookLine("the main method of", 73),
                        core.game.dialogue.book.BookLine("instruction was to place", 74),
                        core.game.dialogue.book.BookLine("magical words into the", 75),
                        core.game.dialogue.book.BookLine("golem's skull cavity.", 76),
                )
        ),
        core.game.dialogue.book.PageSet(
                core.game.dialogue.book.Page(
                        core.game.dialogue.book.BookLine("These were written on", 55),
                        core.game.dialogue.book.BookLine("papyrus using a naturally", 56),
                        core.game.dialogue.book.BookLine("occurring source of ink,", 57),
                        core.game.dialogue.book.BookLine("and their magical power", 58),
                        core.game.dialogue.book.BookLine("derived from the use of a", 59),
                        core.game.dialogue.book.BookLine("phoenix tail feather as a", 60),
                        core.game.dialogue.book.BookLine("pen. These would be used", 61),
                        core.game.dialogue.book.BookLine("for long-term or", 62),
                        core.game.dialogue.book.BookLine("important tasks, and", 63),
                        core.game.dialogue.book.BookLine("would override any verbal", 64),
                        core.game.dialogue.book.BookLine("instructions.", 65),
                )
        )
)

@Initializable
class VarmensNotesHandler : core.game.dialogue.book.Book {
    constructor() {}
    constructor(player: Player?) : super(player, "The Ruins of Uzer", Items.VARMENS_NOTES_4616, *VARMEN_NOTES) {}
    override fun finish() {
        player.setAttribute("/save:the-golem:varmen-notes-read", true)
    }

    override fun display(set: Array<core.game.dialogue.book.Page>) {
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

    override fun newInstance(player: Player): core.game.dialogue.DialoguePlugin {
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
