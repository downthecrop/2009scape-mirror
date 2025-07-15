package content.global.handlers.iface.tabs

import core.game.requirement.*
import core.api.*
import core.tools.*
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills

import kotlin.math.*
import java.util.*

import org.rs09.consts.*
import content.data.Quests

object QuestTabUtils {
    @JvmStatic
    fun showRequirementsInterface(player: Player, button: Int) {
        val questName = getNameForButton(button)
        val questReq = QuestRequirements.values().filter { it.quest.questName == questName }.firstOrNull() ?: return
        var (isMet, unmetReqs) = QuestReq(questReq).evaluate(player)

        var messageList = ArrayList<String>()

        val statMap = HashMap<Int, Int>()
        val questList = HashSet<String>()
        var maxQpReq = 0
        var qpPenalty = 0
        closeInterface(player)
        for (req in unmetReqs) {
            if (req is QuestReq)
                questList.add(req.questReq.quest.questName)
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

    private fun getNameForButton(button: Int) : String {
        val quest = when (button) {
            10 -> Quests.MYTHS_OF_THE_WHITE_LANDS.questName
            11 -> Quests.MYTHS_OF_THE_WHITE_LANDS.questName
            12 -> "Free Quests"
            13 -> Quests.BLACK_KNIGHTS_FORTRESS.questName
            14 -> Quests.COOKS_ASSISTANT.questName
            15 -> Quests.DEMON_SLAYER.questName
            16 -> Quests.DORICS_QUEST.questName
            17 -> Quests.DRAGON_SLAYER.questName
            18 -> Quests.ERNEST_THE_CHICKEN.questName
            19 -> Quests.GOBLIN_DIPLOMACY.questName
            20 -> Quests.IMP_CATCHER.questName
            21 -> Quests.THE_KNIGHTS_SWORD.questName
            22 -> Quests.PIRATES_TREASURE.questName
            23 -> Quests.PRINCE_ALI_RESCUE.questName
            24 -> Quests.THE_RESTLESS_GHOST.questName
            25 -> Quests.ROMEO_JULIET.questName
            26 -> Quests.RUNE_MYSTERIES.questName
            27 -> Quests.SHEEP_SHEARER.questName
            28 -> Quests.SHIELD_OF_ARRAV.questName
            29 -> Quests.VAMPIRE_SLAYER.questName
            30 -> Quests.WITCHS_POTION.questName
            31 -> "Members' Quests"
            32 -> Quests.ANIMAL_MAGNETISM.questName
            33 -> Quests.BETWEEN_A_ROCK.questName
            34 -> Quests.BIG_CHOMPY_BIRD_HUNTING.questName
            35 -> Quests.BIOHAZARD.questName
            36 -> Quests.CABIN_FEVER.questName
            37 -> Quests.CLOCK_TOWER.questName
            38 -> Quests.CONTACT.questName
            39 -> Quests.ZOGRE_FLESH_EATERS.questName
            40 -> Quests.CREATURE_OF_FENKENSTRAIN.questName
            41 -> Quests.DARKNESS_OF_HALLOWVALE.questName
            42 -> Quests.DEATH_TO_THE_DORGESHUUN.questName
            43 -> Quests.DEATH_PLATEAU.questName
            44 -> Quests.DESERT_TREASURE.questName
            45 -> Quests.DEVIOUS_MINDS.questName
            46 -> Quests.THE_DIG_SITE.questName
            47 -> Quests.DRUIDIC_RITUAL.questName
            48 -> Quests.DWARF_CANNON.questName
            49 -> Quests.EADGARS_RUSE.questName
            50 -> Quests.EAGLES_PEAK.questName
            51 -> Quests.ELEMENTAL_WORKSHOP_I.questName
            52 -> Quests.ELEMENTAL_WORKSHOP_II.questName
            53 -> Quests.ENAKHRAS_LAMENT.questName
            54 -> Quests.ENLIGHTENED_JOURNEY.questName
            55 -> Quests.THE_EYES_OF_GLOUPHRIE.questName
            56 -> Quests.FAIRYTALE_I_GROWING_PAINS.questName
            57 -> Quests.FAIRYTALE_II_CURE_A_QUEEN.questName
            58 -> Quests.FAMILY_CREST.questName
            59 -> Quests.THE_FEUD.questName
            60 -> Quests.FIGHT_ARENA.questName
            61 -> Quests.FISHING_CONTEST.questName
            62 -> Quests.FORGETTABLE_TALE.questName
            63 -> Quests.THE_FREMENNIK_TRIALS.questName
            64 -> Quests.WATERFALL_QUEST.questName
            65 -> Quests.GARDEN_OF_TRANQUILITY.questName
            66 -> Quests.GERTRUDES_CAT.questName
            67 -> Quests.GHOSTS_AHOY.questName
            68 -> Quests.THE_GIANT_DWARF.questName
            69 -> Quests.THE_GOLEM.questName
            70 -> Quests.THE_GRAND_TREE.questName
            71 -> Quests.THE_HAND_IN_THE_SAND.questName
            72 -> Quests.HAUNTED_MINE.questName
            73 -> Quests.HAZEEL_CULT.questName
            74 -> Quests.HEROES_QUEST.questName
            75 -> Quests.HOLY_GRAIL.questName
            76 -> Quests.HORROR_FROM_THE_DEEP.questName
            77 -> Quests.ICTHLARINS_LITTLE_HELPER.questName
            78 -> Quests.IN_AID_OF_THE_MYREQUE.questName
            79 -> Quests.IN_SEARCH_OF_THE_MYREQUE.questName
            80 -> Quests.JUNGLE_POTION.questName
            81 -> Quests.LEGENDS_QUEST.questName
            82 -> Quests.LOST_CITY.questName
            83 -> Quests.THE_LOST_TRIBE.questName
            84 -> Quests.LUNAR_DIPLOMACY.questName
            85 -> Quests.MAKING_HISTORY.questName
            86 -> Quests.MERLINS_CRYSTAL.questName
            87 -> Quests.MONKEY_MADNESS.questName
            88 -> Quests.MONKS_FRIEND.questName
            89 -> Quests.MOUNTAIN_DAUGHTER.questName
            90 -> Quests.MOURNINGS_END_PART_I.questName
            91 -> Quests.MOURNINGS_END_PART_II.questName
            92 -> Quests.MURDER_MYSTERY.questName
            93 -> Quests.MY_ARMS_BIG_ADVENTURE.questName
            94 -> Quests.NATURE_SPIRIT.questName
            95 -> Quests.OBSERVATORY_QUEST.questName
            96 -> Quests.ONE_SMALL_FAVOUR.questName
            97 -> Quests.PLAGUE_CITY.questName
            98 -> Quests.PRIEST_IN_PERIL.questName
            99 -> Quests.RAG_AND_BONE_MAN.questName
            100 -> Quests.RATCATCHERS.questName
            101 -> Quests.RECIPE_FOR_DISASTER.questName
            102 -> Quests.RECRUITMENT_DRIVE.questName
            103 -> Quests.REGICIDE.questName
            104 -> Quests.ROVING_ELVES.questName
            105 -> Quests.ROYAL_TROUBLE.questName
            106 -> Quests.RUM_DEAL.questName
            107 -> Quests.SCORPION_CATCHER.questName
            108 -> Quests.SEA_SLUG.questName
            109 -> Quests.THE_SLUG_MENACE.questName
            110 -> Quests.SHADES_OF_MORTTON.questName
            111 -> Quests.SHADOW_OF_THE_STORM.questName
            112 -> Quests.SHEEP_HERDER.questName
            113 -> Quests.SHILO_VILLAGE.questName
            114 -> Quests.A_SOULS_BANE.questName
            115 -> Quests.SPIRITS_OF_THE_ELID.questName
            116 -> Quests.SWAN_SONG.questName
            117 -> Quests.TAI_BWO_WANNAI_TRIO.questName
            118 -> Quests.A_TAIL_OF_TWO_CATS.questName
            119 -> Quests.TEARS_OF_GUTHIX.questName
            120 -> Quests.TEMPLE_OF_IKOV.questName
            121 -> Quests.THRONE_OF_MISCELLANIA.questName
            122 -> Quests.THE_TOURIST_TRAP.questName
            123 -> Quests.WITCHS_HOUSE.questName
            124 -> Quests.TREE_GNOME_VILLAGE.questName
            125 -> Quests.TRIBAL_TOTEM.questName
            126 -> Quests.TROLL_ROMANCE.questName
            127 -> Quests.TROLL_STRONGHOLD.questName
            128 -> Quests.UNDERGROUND_PASS.questName
            129 -> Quests.WANTED.questName
            130 -> Quests.WATCHTOWER.questName
            131 -> Quests.COLD_WAR.questName
            132 -> Quests.THE_FREMENNIK_ISLES.questName
            133 -> Quests.TOWER_OF_LIFE.questName
            134 -> Quests.THE_GREAT_BRAIN_ROBBERY.questName
            135 -> Quests.WHAT_LIES_BELOW.questName
            136 -> Quests.OLAFS_QUEST.questName
            137 -> Quests.ANOTHER_SLICE_OF_HAM.questName
            138 -> Quests.DREAM_MENTOR.questName
            139 -> Quests.GRIM_TALES.questName
            140 -> Quests.KINGS_RANSOM.questName
            141 -> Quests.THE_PATH_OF_GLOUPHRIE.questName
            142 -> Quests.BACK_TO_MY_ROOTS.questName
            143 -> Quests.LAND_OF_THE_GOBLINS.questName
            144 -> Quests.DEALING_WITH_SCABARAS.questName
            145 -> Quests.WOLF_WHISTLE.questName
            146 -> Quests.AS_A_FIRST_RESORT.questName
            147 -> Quests.CATAPULT_CONSTRUCTION.questName
            148 -> Quests.KENNITHS_CONCERNS.questName
            149 -> Quests.LEGACY_OF_SEERGAZE.questName
            150 -> Quests.PERILS_OF_ICE_MOUNTAIN.questName
            151 -> Quests.TOKTZ_KET_DILL.questName
            152 -> Quests.SMOKING_KILLS.questName
            153 -> Quests.ROCKING_OUT.questName
            154 -> Quests.SPIRIT_OF_SUMMER.questName
            155 -> Quests.MEETING_HISTORY.questName
            156 -> Quests.ALL_FIRED_UP.questName
            157 -> Quests.SUMMERS_END.questName
            158 -> Quests.DEFENDER_OF_VARROCK.questName
            159 -> Quests.SWEPT_AWAY.questName
            160 -> Quests.WHILE_GUTHIX_SLEEPS.questName
            161 -> Quests.IN_PYRE_NEED.questName
            162 -> Quests.MYTHS_OF_THE_WHITE_LANDS.questName
            else -> ""
        }
        return quest as String
    }
}