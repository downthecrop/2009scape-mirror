package content.global.handlers.iface

import core.game.requirement.*
import core.api.*
import core.tools.*
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills

import kotlin.math.*
import java.util.*

import org.rs09.consts.*

object QuestTabUtils {
    @JvmStatic
    fun showRequirementsInterface (player: Player, button: Int) {
        val questName = getNameForButton (button)
        val questReq = QuestRequirements.values().filter { it.questName.equals(questName, true) }.firstOrNull() ?: return
        var (isMet, unmetReqs) = QuestReq(questReq).evaluate(player)

        var messageList = ArrayList<String>()

        val statMap = HashMap<Int, Int>()
        val questList = HashSet<String>()
        var maxQpReq = 0
        var qpPenalty = 0
        closeInterface(player)
        for (req in unmetReqs) {
            if (req is QuestReq)
                questList.add(req.questReq.questName)
            else if (req is SkillReq) {
                if (statMap[req.skillId] == null || (statMap[req.skillId] != null && statMap[req.skillId]!! < req.level))
                    statMap[req.skillId] = req.level
            }
            else if (req is QPReq && req.amount > maxQpReq)
                maxQpReq = req.amount
            else if (req is QPCumulative)
                qpPenalty += req.amount
        }

        messageList.add(colorize("%B[Quests Needed]"))
        messageList.addAll(questList.map { "Completion of $it" })
        
        messageList.add(" ")
        messageList.add(colorize("%B[Skills Needed]"))

        for ((skillId, level) in statMap) {
            val name = Skills.SKILL_NAME[skillId]
            messageList.add("$level $name")
        }

        messageList.add(" ")
        messageList.add(colorize("%B[Other Reqs]"))

        val totalQpRequirement = QPReq(min(max(maxQpReq, qpPenalty), player.questRepository.getAvailablePoints()))
        val (meetsQp, _) = totalQpRequirement.evaluate(player)
        isMet = isMet && meetsQp

        if (isMet)
            messageList.add(colorize("%GCongratulations! You've earned this one."))

        if (!meetsQp) messageList.add("A total of ${totalQpRequirement.amount} Quest Points.")

        messageList.add("")
        messageList.add(colorize("%BDISCLAIMER: If you're seeing this screen, this quest is not"))
        messageList.add(colorize("%Bimplemented yet. These are the requirements that you need in order"))
        messageList.add(colorize("%Bto access implemented content that would normally require this quest"))
        messageList.add("")
        messageList.add("If you want to see more quests enter the game, consider")
        messageList.add("contributing dialogue transcripts!")


        setInterfaceText(player, questName, Components.QUESTJOURNAL_SCROLL_275, 2)
        var lineId = 11
        for(i in 0..299) {
            val entry = messageList.elementAtOrNull(i)
            if (entry != null)
                setInterfaceText(player, entry, Components.QUESTJOURNAL_SCROLL_275, lineId++)
            else
                setInterfaceText(player, "", Components.QUESTJOURNAL_SCROLL_275, lineId++)
        }
        openInterface(player, Components.QUESTJOURNAL_SCROLL_275)
    }

    fun getNameForButton (button: Int) : String {
        val name = when (button) {
            10 -> "Myths of the White Lands"
            11 -> "Myths of the White Lands"
            12 -> "Free Quests"
            13 -> "Black Knights' Fortress"
            14 -> "Cook's Assistant"
            15 -> "Demon Slayer"
            16 -> "Doric's Quest"
            17 -> "Dragon Slayer"
            18 -> "Ernest the Chicken"
            19 -> "Goblin Diplomacy"
            20 -> "Imp Catcher"
            21 -> "The Knight's Sword"
            22 -> "Pirate's Treasure"
            23 -> "Prince Ali Rescue"
            24 -> "The Restless Ghost"
            25 -> "Romeo & Juliet"
            26 -> "Rune Mysteries"
            27 -> "Sheep Shearer"
            28 -> "Shield of Arrav"
            29 -> "Vampire Slayer"
            30 -> "Witch's Potion"
            31 -> "Members' Quests"
            32 -> "Animal Magnetism"
            33 -> "Between a Rock..."
            34 -> "Big Chompy Bird Hunting"
            35 -> "Biohazard"
            36 -> "Cabin Fever"
            37 -> "Clock Tower"
            38 -> "Contact!"
            39 -> "Zogre Flesh Eaters"
            40 -> "Creature of Fenkenstrain"
            41 -> "Darkness of Hallowvale"
            42 -> "Death to the Dorgeshuun"
            43 -> "Death Plateau"
            44 -> "Desert Treasure"
            45 -> "Devious Minds"
            46 -> "The Dig Site"
            47 -> "Druidic Ritual"
            48 -> "Dwarf Cannon"
            49 -> "Eadgar's Ruse"
            50 -> "Eagles' Peak"
            51 -> "Elemental Workshop I"
            52 -> "Elemental Workshop II"
            53 -> "Enakhra's Lament"
            54 -> "Enlightened Journey"
            55 -> "The Eyes of Glouphrie"
            56 -> "Fairytale I - Growing Pains"
            57 -> "Fairytale II - Cure a Queen"
            58 -> "Family Crest"
            59 -> "The Feud"
            60 -> "Fight Arena"
            61 -> "Fishing Contest"
            62 -> "Forgettable Tale..."
            63 -> "The Fremennik Trials"
            64 -> "Waterfall Quest"
            65 -> "Garden of Tranquillity"
            66 -> "Gertrude's Cat"
            67 -> "Ghosts Ahoy"
            68 -> "The Giant Dwarf"
            69 -> "The Golem"
            70 -> "The Grand Tree"
            71 -> "The Hand in the Sand"
            72 -> "Haunted Mine"
            73 -> "Hazeel Cult"
            74 -> "Heroes' Quest"
            75 -> "Holy Grail"
            76 -> "Horror from the Deep"
            77 -> "Icthlarin's Little Helper"
            78 -> "In Aid of the Myreque"
            79 -> "In Search of the Myreque"
            80 -> "Jungle Potion"
            81 -> "Legend's Quest"
            82 -> "Lost City"
            83 -> "The Lost Tribe"
            84 -> "Lunar Diplomacy"
            85 -> "Making History"
            86 -> "Merlin's Crystal"
            87 -> "Monkey Madness"
            88 -> "Monk's Friend"
            89 -> "Mountain Daughter"
            90 -> "Mourning's End Part I"
            91 -> "Mourning's End Part II"
            92 -> "Murder Mystery"
            93 -> "My Arm's Big Adventure"
            94 -> "Nature Spirit"
            95 -> "Observatory Quest"
            96 -> "One Small Favour"
            97 -> "Plague City"
            98 -> "Priest in Peril"
            99 -> "Rag and Bone Man"
            100 -> "Ratcatchers"
            101 -> "Recipe for Disaster"
            102 -> "Recruitment Drive"
            103 -> "Regicide"
            104 -> "Roving Elves"
            105 -> "Royal Trouble"
            106 -> "Rum Deal"
            107 -> "Scorpion Catcher"
            108 -> "Sea Slug"
            109 -> "The Slug Menace"
            110 -> "Shades of Mort'ton"
            111 -> "Shadow of the Storm"
            112 -> "Sheep Herder"
            113 -> "Shilo Village"
            114 -> "A Soul's Bane"
            115 -> "Spirits of the Elid"
            116 -> "Swan Song"
            117 -> "Tai Bwo Wannai Trio"
            118 -> "A Tail of Two Cats"
            119 -> "Tears of Guthix"
            120 -> "Temple of Ikov"
            121 -> "Throne of Miscellania"
            122 -> "The Tourist Trap"
            123 -> "Witch's House"
            124 -> "Tree Gnome Village"
            125 -> "Tribal Totem"
            126 -> "Troll Romance"
            127 -> "Troll Stronghold"
            128 -> "Underground Pass"
            129 -> "Wanted!"
            130 -> "Watchtower"
            131 -> "Cold War"
            132 -> "The Fremennik Isles"
            133 -> "Tower of Life"
            134 -> "The Great Brain Robbery"
            135 -> "What Lies Below"
            136 -> "Olaf's Quest"
            137 -> "Another Slice of H.A.M"
            138 -> "Dream Mentor"
            139 -> "Grim Tales"
            140 -> "King's Ransom"
            141 -> "The Path of Glouphrie"
            142 -> "Back to my Roots"
            143 -> "Land of the Goblins"
            144 -> "Dealing with Scabaras"
            145 -> "Wolf Whistle"
            146 -> "As a First Resort..."
            147 -> "Catapult Construction"
            148 -> "Kennith's Concerns"
            149 -> "Legacy of Seergaze"
            150 -> "Perils of Ice Mountain"
            151 -> "TokTz-Ket-Dill"
            152 -> "Smoking Kills"
            153 -> "Rocking Out"
            154 -> "Spirit of Summer"
            155 -> "Meeting History"
            156 -> "All Fired Up"
            157 -> "Summer's End"
            158 -> "Defender of Varrock"
            159 -> "Swept Away"
            160 -> "While Guthix Sleeps"
            161 -> "In Pyre Need"
            162 -> "Myths of the White Lands"
            else -> ""
        }
        return name
    }
}
