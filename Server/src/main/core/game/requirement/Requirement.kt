package core.game.requirement

import core.api.*
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.QuestRepository
import core.game.node.entity.skill.Skills
import kotlin.math.min

import java.util.ArrayList

interface Requirement {
    abstract fun evaluate (player: Player) : Pair<Boolean, List<Requirement>>
}

open class SkillReq (val skillId: Int, val level: Int, val soft: Boolean = false) : Requirement {
    override fun evaluate (player: Player) : Pair<Boolean, List<Requirement>> {
        val hasLevelRequirement = getStatLevel(player, skillId) >= level //if (soft) getDynLevel(player, skillId) >= level else getStatLevel(player, skillId) >= level
        if (hasLevelRequirement) return Pair (true, listOf())
        else return Pair(false, listOf(this))
    }
}

open class QuestReq (val questReq: QuestRequirements, val stageRequired: Int = 100) : Requirement {
    override fun evaluate (player: Player) : Pair<Boolean, List<Requirement>> {
        val quest = QuestRepository.getQuests()[questReq.questName]
        val unmetRequirements = ArrayList<Requirement>()
        var isMet = true
        if (quest == null) {
            for (req in questReq.requirements) {
                val (met, reqs) = req.evaluate(player)
                isMet = isMet && met
                unmetRequirements.addAll(reqs)
            }
            unmetRequirements.add (QPCumulative(5))
        } else {
            isMet = quest.getStage(player) >= stageRequired
            if (!isMet) unmetRequirements.add(this)
            unmetRequirements.add (QPCumulative(quest.getQuestPoints()))
        }

        return Pair (isMet, unmetRequirements)
    }
}

open class QPReq (val amount: Int) : Requirement {
    override fun evaluate (player: Player) : Pair<Boolean, List<Requirement>> {
        val needed = min(amount, player.questRepository.getAvailablePoints())
        val hasNeeded = player.questRepository.getPoints() >= needed
        
        return Pair (hasNeeded, if (hasNeeded) listOf() else listOf(this))
    }
}

open class QPCumulative (val amount: Int) : Requirement {
    override fun evaluate (player: Player) : Pair<Boolean, List<Requirement>> {
        return Pair (false, listOf(this))
    }
}

enum class QuestRequirements (val questName: String, vararg val requirements: Requirement) {
    COOK_ASSIST ("Cook's Assistant"),
    DEMON_SLAYER ("Demon Slayer"),
    DORIC_QUEST ("Doric's Quest"),
    DRAGON_SLAYER ("Dragon Slayer", QPReq(32)),
    ERNEST ("Ernest the Chicken"),
    GOBLIN_DIP ("Goblin Diplomacy"),
    IMP_CATCHER ("Imp Catcher"),
    KNIGHT_SWORD ("The Knight's Sword", SkillReq(Skills.MINING, 10, true)),
    PIRATE_T ("Pirate's Treasure"),
    ALI_RESCUE ("Prince Ali Rescue"),
    RESTLESS_GHOST ("The Restless Ghost"),
    ROMEO ("Romeo & Juliet"),
    RUNE_MYST ("Rune Mysteries"),
    SHEEP ("Sheep Shearer"),
    ARRAV ("Shield of Arrav"),
    VAMPIRE ("Vampire Slayer"),
    DORIC ("Doric's Quest"),
    RUNE_MYSTERIES("Rune Mysteries"),
    BLACK_KNIGHT("Black Knights' Fortress", QPReq(12)),
    WITCH_POTION ("Witch's Potion"),
    DRUIDIC_RITUAL ("Druidic Ritual"),
    LOST_CITY ("Lost City", SkillReq(Skills.CRAFTING, 31, true), SkillReq(Skills.WOODCUTTING, 36, true)),
    WITCH_HOUSE ("Witch's House"),
    MERLIN ("Merlin's Crystal"),
    HERO ("Heroes' Quest", QPReq(55), SkillReq(Skills.COOKING, 53, true), SkillReq(Skills.FISHING, 53, true), SkillReq(Skills.HERBLORE, 25, true), SkillReq(Skills.MINING, 50, true), QuestReq(ARRAV), QuestReq(LOST_CITY), QuestReq(MERLIN), QuestReq(DRAGON_SLAYER)),
    SCORP_CATCHER ("Scorpion Catcher", SkillReq(Skills.PRAYER, 31)),
    FAMILY_CREST ("Family Crest", SkillReq(Skills.MINING, 40, true), SkillReq(Skills.SMITHING, 40, true), SkillReq(Skills.MAGIC, 59, true), SkillReq(Skills.CRAFTING, 40, true)),
    FISHING_CONTEST ("Fishing Contest", SkillReq(Skills.FISHING, 10)),
    TOTEM ("Tribal Totem", SkillReq(Skills.THIEVING, 21)),
    MONK ("Monk's Friend"),
    IKOV ("Temple of Ikov", SkillReq(Skills.THIEVING, 42, true), SkillReq(Skills.RANGE, 40)),
    CLOCK_TOWER ("Clock Tower"),
    GRAIL ("Holy Grail", QuestReq (MERLIN), SkillReq (Skills.ATTACK, 20)),
    GNOME_VILLAGE ("Tree Gnome Village"),
    FIGHT_ARENA ("Fight Arena"),
    HAZEEL ("Hazeel Cult"),
    SHEEP_HERDER ("Sheep Herder"),
    PLAGUE_CITY ("Plague City"),
    SEA_SLUG ("Sea Slug", SkillReq(Skills.FIREMAKING, 30, true)),
    WATERFALL ("Waterfall Quest"),
    POTION ("Jungle Potion", SkillReq(Skills.HERBLORE, 3, true), QuestReq(DRUIDIC_RITUAL)),
    GRAND_TREE ("The Grand Tree", SkillReq(Skills.AGILITY, 25, true)),
    BIOHAZARD ("Biohazard", QuestReq(PLAGUE_CITY)),
    UNDERGROUND_PASS ("Underground Pass", SkillReq (Skills.RANGE, 25), QuestReq(BIOHAZARD), QuestReq(PLAGUE_CITY)),
    OBSERVATORY ("Observatory Quest"),
    TOURIST ("The Tourist Trap", SkillReq (Skills.FLETCHING, 10, true), SkillReq(Skills.SMITHING, 20, true)),
    WATCHTOWER ("Watchtower", SkillReq (Skills.MAGIC, 14, true), SkillReq(Skills.THIEVING, 15, true), SkillReq (Skills.AGILITY, 25, true), SkillReq (Skills.HERBLORE, 14, true), SkillReq(Skills.MINING, 40, true)),
    DWARF_CANNON ("Dwarf Cannon"),
    MURDER_MYS ("Murder Mystery"),
    DIG_SITE ("Dig Site", SkillReq(Skills.AGILITY, 10, true), SkillReq (Skills.HERBLORE, 10, true), SkillReq (Skills.THIEVING, 25, true)),
    GERTRUDE ("Gertrude's Cat"),
    SHILO ("Shilo Village", QuestReq(POTION), SkillReq(Skills.CRAFTING, 20, true), SkillReq(Skills.AGILITY, 32, true)),
    LEGEND ("Legend's Quest", QPReq(107), SkillReq(Skills.AGILITY, 50, true), SkillReq(Skills.CRAFTING, 50, true), SkillReq(Skills.HERBLORE, 45, true), SkillReq(Skills.MAGIC, 56, true), SkillReq(Skills.MINING, 52, true), SkillReq(Skills.PRAYER, 42, true), SkillReq(Skills.SMITHING, 50, true), SkillReq(Skills.STRENGTH, 50, true), SkillReq(Skills.THIEVING, 50, true), SkillReq(Skills.WOODCUTTING, 50, true), QuestReq(FAMILY_CREST), QuestReq(HERO), QuestReq(SHILO), QuestReq(UNDERGROUND_PASS), QuestReq(WATERFALL)),
    DEATH_PLATEAU ("Death Plateau"),
    TROLL_STRONGHOLD ("Troll Stronghold", QuestReq(DEATH_PLATEAU), SkillReq(Skills.AGILITY, 15, true)),
    EADGAR ("Eadgar's Ruse", QuestReq (DRUIDIC_RITUAL), QuestReq (TROLL_STRONGHOLD), SkillReq(Skills.HERBLORE, 31, true)),
    CHOMPY ("Big Chompy Bird Hunting", SkillReq (Skills.FLETCHING, 5, true), SkillReq (Skills.COOKING, 30, true), SkillReq(Skills.RANGE, 30, false)),
    ELEMENTAL_W1 ("Elemental Workshop I", SkillReq(Skills.MINING, 20, true), SkillReq(Skills.SMITHING, 20, true), SkillReq(Skills.CRAFTING, 20, true)),
    PRIEST ("Priest in Peril"),
    NATURE_SPIRIT ("Nature Spirit", QuestReq(PRIEST), QuestReq(RESTLESS_GHOST)),
    REGICIDE ("Regicide", QuestReq (UNDERGROUND_PASS), SkillReq (Skills.CRAFTING, 10), SkillReq(Skills.AGILITY, 56, true)),
    TAI_BWO ("Tai Bwo Wannai Trio", SkillReq (Skills.AGILITY, 15, true), SkillReq(Skills.COOKING, 30), SkillReq(Skills.FISHING, 65, true), QuestReq(POTION)),
    SHADES ("Shades of Mort'ton", QuestReq(PRIEST), SkillReq(Skills.CRAFTING, 20, true), SkillReq(Skills.HERBLORE, 15, true), SkillReq(Skills.FIREMAKING, 5, true)),
    FREM_TRIALS ("Fremennik Trials", SkillReq(Skills.FLETCHING, 25, true), SkillReq(Skills.WOODCUTTING, 40, true), SkillReq(Skills.CRAFTING, 40, true)),
    HORROR_DEEP ("Horror from the Deep", SkillReq(Skills.AGILITY, 35, true)),
    THRONE ("Throne of Miscellania", QuestReq(HERO), QuestReq(FREM_TRIALS)),
    MONKEY ("Monkey Madness", QuestReq(GRAND_TREE), QuestReq(GNOME_VILLAGE)),
    MINE ("Haunted Mine", QuestReq(PRIEST), SkillReq(Skills.CRAFTING, 35, true)),
    TROLL_ROMANCE ("Troll Romance", QuestReq(TROLL_STRONGHOLD), SkillReq(Skills.AGILITY, 28, true)),
    SEARCH_MYREQUE ("In Search of the Myreque", QuestReq(NATURE_SPIRIT), SkillReq(Skills.AGILITY, 25, true)),
    FENKENSTRAIN ("Creature of Fenkenstrain", QuestReq(PRIEST), QuestReq(RESTLESS_GHOST), SkillReq(Skills.THIEVING, 25, true), SkillReq(Skills.CRAFTING, 20, true)),
    ROVING_ELVES ("Roving Elves", QuestReq(REGICIDE), QuestReq(WATERFALL), SkillReq(Skills.AGILITY, 56, true)),
    GHOSTS_AHOY ("Ghosts Ahoy", QuestReq(PRIEST), QuestReq(RESTLESS_GHOST), SkillReq(Skills.AGILITY, 25, true), SkillReq(Skills.COOKING, 20, true)),
    FAVOR ("One Small Favor", QuestReq(RUNE_MYSTERIES), QuestReq(SHILO), SkillReq(Skills.AGILITY, 36, true), SkillReq(Skills.CRAFTING, 25, true), SkillReq(Skills.HERBLORE, 18, true), SkillReq(Skills.SMITHING, 30, true)),
    MOUNTAIN_DAUGHTER ("Mountain Daughter", SkillReq(Skills.AGILITY, 20, true)),
    BETWEEN_ROCK ("Between a Rock...", QuestReq(DWARF_CANNON), QuestReq(FISHING_CONTEST), SkillReq(Skills.DEFENCE, 30, true), SkillReq(Skills.MINING, 40, true), SkillReq(Skills.SMITHING, 50, true)),
    FEUD ("The Feud", SkillReq(Skills.THIEVING, 30)),
    GOLEM ("The Golem", SkillReq(Skills.CRAFTING, 20, true), SkillReq(Skills.THIEVING, 25, true)),
    DESERT ("Desert Treasure", QuestReq (DIG_SITE), QuestReq (IKOV), QuestReq(TOURIST), QuestReq(TROLL_STRONGHOLD), QuestReq(PRIEST), QuestReq(WATERFALL), SkillReq(Skills.THIEVING, 53), SkillReq(Skills.MAGIC, 50), SkillReq(Skills.FIREMAKING, 50, true), SkillReq(Skills.SLAYER, 10)),
    ICTHLARIN ("Icthlarin's Little Helper", QuestReq (GERTRUDE)),
    TEARS_OF_GUTHIX ("Tears of Guthix", QPReq(43), SkillReq(Skills.FIREMAKING, 49, true), SkillReq(Skills.CRAFTING, 20, true), SkillReq(Skills.MINING, 20, true)),
    LOST_TRIBE ("Lost Tribe", QuestReq(GOBLIN_DIP), QuestReq(RUNE_MYSTERIES), SkillReq(Skills.AGILITY, 13, true), SkillReq(Skills.THIEVING, 13, true), SkillReq(Skills.MINING, 17, true)),
    GIANT_DWARF ("The Giant Dwarf", SkillReq(Skills.CRAFTING, 12, true), SkillReq(Skills.FIREMAKING, 16, true), SkillReq(Skills.MAGIC, 33, true), SkillReq(Skills.THIEVING, 14, true)),
    RECRUITMENT_DRIVE ("Recruitment Drive", QuestReq (BLACK_KNIGHT), QuestReq(DRUIDIC_RITUAL)),
    MEP_1 ("Mourning's End Part I", SkillReq(Skills.RANGE, 60), SkillReq(Skills.THIEVING, 50), QuestReq(ROVING_ELVES), QuestReq(CHOMPY), QuestReq(SHEEP_HERDER)),
    FORGETTABLE ("Forgettable Tale of a Drunken Dwarf", SkillReq (Skills.COOKING, 22, true), SkillReq(Skills.FARMING, 17, true), QuestReq(GIANT_DWARF), QuestReq(FISHING_CONTEST)),
    GARDEN ("Garden of Tranquility", QuestReq(FENKENSTRAIN), SkillReq(Skills.FARMING, 25)),
    TWO_CATS ("A Tale of Two Cats", QuestReq(ICTHLARIN)),
    WANTED ("Wanted!", QPReq(32), QuestReq(RECRUITMENT_DRIVE), QuestReq(LOST_TRIBE), QuestReq(PRIEST)),
    MEP_2 ("Mourning's End Part II", QuestReq(MEP_1)),
    ZOGRE ("Zogre Flesh Eaters", QuestReq(CHOMPY), QuestReq(POTION), SkillReq(Skills.SMITHING, 4, true), SkillReq(Skills.HERBLORE, 8, true), SkillReq(Skills.RANGE, 30)),
    RUM_DEAL ("Rum Deal", QuestReq(ZOGRE), QuestReq(PRIEST), SkillReq(Skills.CRAFTING, 42, true), SkillReq(Skills.FISHING, 50, true), SkillReq(Skills.FARMING, 40, true), SkillReq(Skills.PRAYER, 47, true), SkillReq(Skills.SLAYER, 42)),
    SHADOW ("Shadow of the Storm", SkillReq(Skills.CRAFTING, 30, true), QuestReq(GOLEM), QuestReq(DEMON_SLAYER)),
    HISTORY ("Making History", QuestReq(PRIEST), QuestReq(RESTLESS_GHOST)),
    RATCATCHERS ("Ratcatchers", QuestReq(ICTHLARIN), QuestReq(GIANT_DWARF)),
    SPIRITS_ELID ("Spirits of the Elid", SkillReq(Skills.MAGIC, 33, true), SkillReq(Skills.RANGE, 37, true), SkillReq(Skills.MINING, 37, true), SkillReq(Skills.THIEVING, 37, true)),
    DEVIOUS ("Devious Minds", SkillReq(Skills.SMITHING, 65, true), SkillReq(Skills.RUNECRAFTING, 50, true), SkillReq(Skills.FLETCHING, 50, true), QuestReq(WANTED), QuestReq(TROLL_STRONGHOLD), QuestReq(DORIC)),
    SAND ("The Hand in the Sand", SkillReq(Skills.THIEVING, 17, true), SkillReq(Skills.CRAFTING, 49, true)),
    ENAKHRA ("Enakhra's Lament", SkillReq(Skills.CRAFTING, 50, true), SkillReq(Skills.FIREMAKING, 45, true), SkillReq(Skills.PRAYER, 43, true), SkillReq(Skills.MAGIC, 39, true)),
    CABIN_FEVER ("Cabin Fever", QuestReq(PIRATE_T), QuestReq(RUM_DEAL), SkillReq(Skills.AGILITY, 42), SkillReq(Skills.CRAFTING, 45), SkillReq(Skills.SMITHING, 50), SkillReq(Skills.RANGE, 40)),
    FAIRYTALE_1 ("Fairytale I - Growing Pains", QuestReq(LOST_CITY), QuestReq(NATURE_SPIRIT)),
    RFD ("Recipe for Disaster", QPReq(175), QuestReq(COOK_ASSIST), SkillReq(Skills.COOKING, 70, true), SkillReq(Skills.AGILITY, 48, true), SkillReq(Skills.MINING, 50, true), SkillReq(Skills.FISHING, 53, true), SkillReq(Skills.THIEVING, 53, true), SkillReq(Skills.HERBLORE, 25, true), SkillReq(Skills.MAGIC, 59, true), SkillReq(Skills.SMITHING, 40, true), SkillReq(Skills.FIREMAKING, 50, true), SkillReq(Skills.RANGE, 40), SkillReq(Skills.CRAFTING, 40, true), SkillReq(Skills.FLETCHING, 10, true), SkillReq(Skills.WOODCUTTING, 36, true), QuestReq(FISHING_CONTEST), QuestReq(GOBLIN_DIP), QuestReq(CHOMPY), QuestReq(MURDER_MYS), QuestReq(NATURE_SPIRIT), QuestReq(WITCH_HOUSE), QuestReq(GERTRUDE), QuestReq(SHADOW), QuestReq(LEGEND), QuestReq(MONKEY), QuestReq(DESERT), QuestReq(HORROR_DEEP)),
    AID_MYREQUE ("In Aid of the Myreque", QuestReq(SEARCH_MYREQUE), SkillReq(Skills.AGILITY, 25, true), SkillReq(Skills.CRAFTING, 25), SkillReq(Skills.MINING, 15), SkillReq(Skills.MAGIC, 7, true)),
    SOUL_BANE ("A Soul's Bane"),
    BONE_MAN_1 ("Rag and Bone Man I"),
    SWAN ("Swan Song", QPReq(100), SkillReq(Skills.MAGIC, 66, true), SkillReq(Skills.COOKING, 62, true), SkillReq(Skills.FISHING, 62, true), SkillReq(Skills.SMITHING, 45, true), SkillReq(Skills.FIREMAKING, 42, true), SkillReq(Skills.CRAFTING, 40, true), QuestReq(FAVOR), QuestReq(GARDEN)),
    ROYAL_TROUBLE ("Royal Trouble", SkillReq(Skills.AGILITY, 40, true), SkillReq(Skills.SLAYER, 40, true), QuestReq(THRONE)),
    DEATH_DORGESHUUN ("Death to the Dorgeshuun", QuestReq(LOST_TRIBE), SkillReq(Skills.AGILITY, 23, true), SkillReq(Skills.THIEVING, 23, true)),
    FAIRYTALE_2 ("Fairytale II - Cure a Queen", QuestReq(FAIRYTALE_1), SkillReq(Skills.THIEVING, 40), SkillReq(Skills.FARMING, 49, true), SkillReq(Skills.HERBLORE, 57, true)),
    LUNAR_DIPLOMACY ("Lunar Diplomacy", QuestReq(FREM_TRIALS), QuestReq(LOST_CITY), QuestReq(RUNE_MYSTERIES), QuestReq(SHILO), SkillReq(Skills.HERBLORE, 5), SkillReq(Skills.CRAFTING, 61), SkillReq(Skills.DEFENCE, 40), SkillReq(Skills.FIREMAKING, 49), SkillReq(Skills.MAGIC, 65), SkillReq(Skills.MINING, 60), SkillReq(Skills.WOODCUTTING, 55)),
    GLOUPHRIE ("The Eyes of Glouphrie", QuestReq(GRAND_TREE), SkillReq(Skills.CONSTRUCTION, 5), SkillReq(Skills.MAGIC, 46)),
    HALLOWVALE ("Darkness of Hallowvale", QuestReq(AID_MYREQUE), SkillReq(Skills.CONSTRUCTION, 5, true), SkillReq(Skills.MINING, 20), SkillReq(Skills.THIEVING, 22), SkillReq(Skills.AGILITY, 26, true), SkillReq(Skills.CRAFTING, 32), SkillReq(Skills.MAGIC, 33, true), SkillReq(Skills.STRENGTH, 40)),
    SLUG_MENACE ("The Slug Menace", QuestReq(WANTED), QuestReq(SEA_SLUG), SkillReq(Skills.CRAFTING, 30), SkillReq(Skills.RUNECRAFTING, 30), SkillReq(Skills.SLAYER, 30), SkillReq(Skills.THIEVING, 30)),
    ELEMENTAL_W2 ("Elemental Workshop II", QuestReq(ELEMENTAL_W1), SkillReq(Skills.MAGIC, 20, true), SkillReq(Skills.SMITHING, 30, true)),
    ARM_ADVENTURE ("My Arm's Big Adventure", SkillReq(Skills.FARMING, 29, true), SkillReq(Skills.WOODCUTTING, 10), QuestReq(EADGAR), QuestReq(FEUD), QuestReq(POTION)),
    ENL_JOURNEY ("Enlightened Journey", QPReq(20), SkillReq(Skills.FIREMAKING, 20, true), SkillReq(Skills.FARMING, 30, true), SkillReq(Skills.CRAFTING, 36, true)),
    EAGLE ("Eagles' Peak", SkillReq(Skills.HUNTER, 27, true)),
    ANMA ("Animal Magnetism", QuestReq(RESTLESS_GHOST), QuestReq(ERNEST), QuestReq(PRIEST), SkillReq(Skills.SLAYER, 18), SkillReq(Skills.CRAFTING, 19), SkillReq(Skills.RANGE, 30), SkillReq(Skills.WOODCUTTING, 35)),
    CONTACT ("Contact!", QuestReq(ALI_RESCUE), QuestReq(ICTHLARIN)),
    COLD_WAR ("Cold War", SkillReq(Skills.HUNTER, 10), SkillReq(Skills.AGILITY, 30, true), SkillReq(Skills.CRAFTING, 30), SkillReq(Skills.CONSTRUCTION, 34), SkillReq(Skills.THIEVING, 15)),
    FREM_ISLES ("The Fremennik Isles", QuestReq(FREM_TRIALS), SkillReq(Skills.CONSTRUCTION, 20, true)),
    BRAIN_ROBBERY ("The Great Brain Robbery", SkillReq(Skills.CRAFTING, 16), SkillReq(Skills.CONSTRUCTION, 30), SkillReq(Skills.PRAYER, 50), QuestReq(FENKENSTRAIN), QuestReq(CABIN_FEVER), QuestReq(RFD)),
    WHAT_LIES_BELOW ("What Lies Below", QuestReq(RUNE_MYSTERIES), SkillReq(Skills.RUNECRAFTING, 35)),
    OLAF ("Olaf's Quest", QuestReq(FREM_TRIALS), SkillReq(Skills.FIREMAKING, 40, true), SkillReq(Skills.WOODCUTTING, 50, true)),
    ANOTHER_SLICE ("Another Slice of H.A.M", SkillReq(Skills.ATTACK, 15), SkillReq(Skills.PRAYER, 25), QuestReq(DEATH_DORGESHUUN), QuestReq(GIANT_DWARF), QuestReq(DIG_SITE)),
    DREAM_MENTOR ("Dream Mentor", QuestReq(LUNAR_DIPLOMACY), QuestReq(EADGAR)),
    GRIM_TALES ("Grim Tales", QuestReq(WITCH_HOUSE), SkillReq(Skills.FARMING, 45, true), SkillReq(Skills.HERBLORE, 52, true), SkillReq(Skills.THIEVING, 58, true), SkillReq(Skills.AGILITY, 59, true), SkillReq(Skills.WOODCUTTING, 71, true)),
    KINGS_RANSOM ("King's Ransom", SkillReq(Skills.MAGIC, 45), SkillReq(Skills.MINING, 45, true), SkillReq(Skills.DEFENCE, 65), QuestReq(BLACK_KNIGHT), QuestReq(GRAIL), QuestReq(MURDER_MYS), QuestReq(FAVOR)),
    TOWER_OF_LIFE ("Tower of Life", SkillReq(Skills.CONSTRUCTION, 10)),
    BONE_MAN_2 ("Rag and Bone Man II", SkillReq(Skills.SLAYER, 40, true), SkillReq(Skills.DEFENCE, 20), QuestReq(BONE_MAN_1), QuestReq(FREM_TRIALS), QuestReq(FENKENSTRAIN), QuestReq(ZOGRE), QuestReq(WATERFALL)),
    LAND_GOBLINS ("Land of the Goblins", QuestReq(ANOTHER_SLICE), QuestReq(FISHING_CONTEST), SkillReq(Skills.AGILITY, 38), SkillReq(Skills.FISHING, 40), SkillReq(Skills.THIEVING, 45), SkillReq(Skills.HERBLORE, 48)),
    PATH_GLOUPHRIE ("The Path of Glouphrie", QuestReq(GLOUPHRIE), QuestReq(GNOME_VILLAGE), QuestReq(WATERFALL), SkillReq(Skills.AGILITY, 45), SkillReq(Skills.RANGE, 47), SkillReq(Skills.SLAYER, 56), SkillReq(Skills.STRENGTH, 60), SkillReq(Skills.THIEVING, 56)),
    DEFENDER_VARROCK ("Defender of Varrock", QuestReq(ARRAV), QuestReq(KNIGHT_SWORD), QuestReq(DEMON_SLAYER), QuestReq(IKOV), QuestReq(FAMILY_CREST), QuestReq(WHAT_LIES_BELOW), QuestReq(GARDEN), SkillReq(Skills.AGILITY, 51), SkillReq(Skills.HUNTER, 51), SkillReq(Skills.MINING, 59), SkillReq(Skills.SMITHING, 54)),
    SPIRIT_OF_SUMMER ("Spirit of Summer", QuestReq(RESTLESS_GHOST), SkillReq(Skills.CONSTRUCTION, 40), SkillReq(Skills.FARMING, 26), SkillReq(Skills.PRAYER, 35), SkillReq(Skills.SUMMONING, 19)),
    SUMMERS_END ("Summer's End", QuestReq(SPIRIT_OF_SUMMER), SkillReq(Skills.FIREMAKING, 47), SkillReq(Skills.HUNTER, 35), SkillReq(Skills.MINING, 45), SkillReq(Skills.PRAYER, 55), SkillReq(Skills.SUMMONING, 23), SkillReq(Skills.WOODCUTTING, 37)),
    SEERGAZE ("Legacy of Seergaze", QuestReq(HALLOWVALE), SkillReq(Skills.AGILITY, 29), SkillReq(Skills.CONSTRUCTION, 20), SkillReq(Skills.CRAFTING, 47), SkillReq(Skills.FIREMAKING, 40), SkillReq(Skills.MAGIC, 49), SkillReq(Skills.MINING, 35), SkillReq(Skills.SLAYER, 31)),
    SMOKING_KILLS ("Smoking Kills", QuestReq(RESTLESS_GHOST), QuestReq(ICTHLARIN), SkillReq(Skills.CRAFTING, 25), SkillReq(Skills.SLAYER, 35)),
    WHILE_GUTHIX_SLEEPS ("While Guthix Sleeps", SkillReq(Skills.SUMMONING, 23), SkillReq(Skills.HUNTER, 55), SkillReq(Skills.THIEVING, 60), SkillReq(Skills.DEFENCE, 65), SkillReq(Skills.FARMING, 65), SkillReq(Skills.HERBLORE, 65), SkillReq(Skills.MAGIC, 75), QuestReq(DEFENDER_VARROCK), QuestReq(DREAM_MENTOR), QuestReq(SAND), QuestReq(KINGS_RANSOM), QuestReq(LEGEND), QuestReq(MEP_2), QuestReq(PATH_GLOUPHRIE), QuestReq(RFD), QuestReq(SUMMERS_END), QuestReq(SWAN), QuestReq(TEARS_OF_GUTHIX), QuestReq(ZOGRE)),
    ALL_FIRED_UP ("All Fired Up", QuestReq(PRIEST), SkillReq(Skills.FIREMAKING, 43))
}
