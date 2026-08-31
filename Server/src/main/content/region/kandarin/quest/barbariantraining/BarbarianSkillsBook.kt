package content.region.kandarin.quest.barbariantraining

import content.global.handlers.iface.BookInterface
import content.global.handlers.iface.BookLine
import content.global.handlers.iface.Page
import content.global.handlers.iface.PageSet
import core.api.getAttribute
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import org.rs09.consts.Items

/**
 * Barbarian Skills book given by Otto Godblessed during Barbarian Training.
 * Content dynamically shows based on player's training progress (attributes).
 *
 * Sections are defined as List<List<String>> — a list of raw pages, each page being a list of
 * line strings with no hardcoded interface IDs. buildContents() accumulates raw pages in order
 * and pairs them into left/right spreads dynamically via toPageSets(), so no blank space is
 * left after a single-page section regardless of what precedes it.
 *
 * @source https://runescape.wiki/w/Barbarian_skills
 * @author Damighty
 * @author Bishop
 */

class BarbarianSkillsBook : InteractionListener {
    companion object {
        private const val TITLE = "What Otto told me."
        private const val BLUE = "<col=08088A>"

        private val LEFT_IDS  = intArrayOf(97, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81)
        private val RIGHT_IDS = intArrayOf(82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96)

        private fun List<String>.toLeftPage()  = Page(*mapIndexed { i, s -> BookLine(s, LEFT_IDS[i])  }.toTypedArray())
        private fun List<String>.toRightPage() = Page(*mapIndexed { i, s -> BookLine(s, RIGHT_IDS[i]) }.toTypedArray())

        private fun List<List<String>>.toPageSets(): Array<PageSet> {
            val sets = mutableListOf<PageSet>()
            var i = 0
            while (i < size) {
                val left  = this[i].toLeftPage()
                val right = getOrNull(i + 1)?.toRightPage()
                sets.add(if (right != null) PageSet(left, right) else PageSet(left))
                i += if (right != null) 2 else 1
            }
            return sets.toTypedArray()
        }

        private val INTRODUCTION = listOf(
            listOf(
                "",
                "I have noted down what Otto",
                "has told me in this journal, so",
                "that I may not forget my",
                "tasks. His instructions are",
                "thus faithfully recorded for",
                "posterity.",
            )
        )

        private val ROD_FISHING = listOf(
            listOf(
                "",
                "I have noted down what Otto",
                "has told me in this journal, so",
                "that I may not forget my",
                "tasks. His instructions are",
                "thus faithfully recorded for",
                "posterity.",
                "",
                "",
                "",
                BLUE + "Otto's words on Fishing with",
                BLUE + "rods",
                "",
                "'While you civilised folk use",
                "small, weak fishing rods, we",
            ),
            listOf(
                "barbarians are skilled with",
                "heavier tackle. We fish in the",
                "lake nearby. Take the rod",
                "from under the bed in my",
                "dwelling and fish in the lake.",
                "When you have caught a few",
                "fish I am sure you will be",
                "ready to talk more with me.",
                "You will know when you are",
                "ready, since inspiration will fill",
                "your mind. We do not use",
                "these fish quite as you might",
                "expect. When you return",
                "from Fishing we can speak",
                "more of this matter.'",
            )
        )

        private val ROD_FISHING_DONE = listOf(
            listOf(
                "'Your mind is as clear as the",
                "waters you have fished. This",
                "is good. These are fish which",
                "are fat with eggs rather than",
                "fat of flesh; this is what we",
                "will make use of.'",
            )
        )

        private val BAREHANDED_FISHING = listOf(
            listOf(
                "",
                BLUE + "Otto's words on Fishing",
                BLUE + "without harpoons",
                "",
                "'First you must know more",
                "of harpoons through special",
                "study of fish that are usually",
                "caught with such a device.",
                "You must catch fish which",
                "are usually harpooned,",
                "without a harpoon. You will",
                "be using your skill and",
                "strength. Use your arm as",
                "bait. Wriggle your fingers as",
                "if they are a tasty snack and",
            ),
            listOf(
                "hungry tuna and swordfish",
                "will throng to be caught by",
                "you. This method also works",
                "with shark - though in this",
                "case the action must be more",
                "of a frenzied thrashing of the",
                "arm than a wriggle.'",
            )
        )

        private val BAREHANDED_FISHING_DONE = listOf(
            listOf(
                "",
                BLUE + "Otto's words on Fishing",
                BLUE + "without harpoons",
                "",
                "'First you must know more",
                "of harpoons through special",
                "study of fish that are usually",
                "caught with such a device.",
                "You must catch fish which",
                "are usually harpooned,",
                "without a harpoon. You will",
                "be using your skill and",
                "strength. Use your arm as",
                "bait. Wriggle your fingers as",
                "if they are a tasty snack and",
            ),
            listOf(
                "hungry tuna and swordfish",
                "will throng to be caught by",
                "you. This method also works",
                "with shark - though in this",
                "case the action must be more",
                "of a frenzied thrashing of the",
                "arm than a wriggle.'",
                "'I sense you have more",
                "understanding of spears",
                "through your studies.'",
            )
        )

        private val BOW_FIREMAKING = listOf(
            listOf(
                "",
                BLUE + "Otto's words on Firemaking",
                "",
                "'The first point in your",
                "progression is that of lighting",
                "fires without a tinderbox. For",
                "this process you will require",
                "a strung bow. You use the",
                "bow to quickly rotate pieces",
                "of wood against one another.",
                "As you rub, the wood",
                "becomes hot, eventually",
                "springing into flame. The",
                "spirits will aid you, the power",
                "they supply will guide your",
            ),
            listOf(
                "hands. Go and benefit from",
                "their guidance upon an oaken",
                "log.'",
            )
        )

        private val BOW_FIREMAKING_DONE = listOf(
            listOf(
                "",
                BLUE + "Otto's words on Firemaking",
                "",
                "'The first point in your",
                "progression is that of lighting",
                "fires without a tinderbox. For",
                "this process you will require",
                "a strung bow. You use the",
                "bow to quickly rotate pieces",
                "of wood against one another.",
                "As you rub, the wood",
                "becomes hot, eventually",
                "springing into flame, The",
                "spirits will aid you, the power",
                "they supply will guide your",
            ),
            listOf(
                "hands. Go and benefit from",
                "their guidance upon an oaken",
                "log.'",
                "'Firemaking with your bow",
                "worked - fine news indeed,",
                "secrets of our spirit boats",
                "now await your attention.'",
            )
        )

        private val HERBLORE = listOf(
            listOf(
                "",
                BLUE + "Otto's words on potion",
                BLUE + "enhancement",
                "",
                "'If you use your knife upon",
                "the fat fish, several new fishy",
                "items will be produced. Fish",
                "parts can be used as bait; the",
                "roe or caviar is more useful",
                "for us. Mixing these items",
                "with two dose potions is what",
                "should be performed. This",
                "results in a nutritious slop,",
                "perfect for healing as well as",
                "imparting the effect of the",
            ),
            listOf(
                "potion. Roe can only be used",
                "for some of the more easily",
                "combined mixes, while caviar",
                "may be used for any of the",
                "the mixes of which I am aware.",
                "You will discover that you",
                "are able to decant a four",
                "dose potion into an empty",
                "vial, this giving two potions",
                "of two doses. This will aid",
                "you in the process. In this",
                "case I in fact require a",
                "potion, for my own stocks.",
                "Bring me a lesser attack",
                "potion combined with fish roe.'",
            )
        )

        private val HERBLORE_DONE = listOf(
            listOf(
                "'I see you have my potion. I",
                "will say no more than that I",
                "am eternally grateful.'",
            ),
        )

        private val SPEAR_REJECTED = listOf(
            listOf(
                "",
                BLUE + "Otto's words on Smithing",
                BLUE + "spears",
                "'The next step is to",
                "manufacture a spear, suitable",
                "for combat. Our distant",
                "cousins on Karamja are in",
                "need of help, however, and",
                "you must aid them before I",
                "can aid you. You must go",
                "now and complete the Tai",
                "Bwo Wannai Trio quest.'",
            ),
        )

        private val SPEAR_SMITHING = listOf(
            listOf(
                "",
                BLUE + "Otto's words on Smithing",
                BLUE + "spears",
                "'The next step is to",
                "manufacture a spear, suitable",
                "for combat. Our distant",
                "cousins on Karamja are in",
                "need of help, however, and",
                "you must aid them before I",
                "can aid you. You must go",
                "now and complete the Tai",
                "Bwo Wannai Trio quest.'",
                "",
                "",
                BLUE + "Since I have completed this"
            ),
            listOf(
                BLUE + "quest, he adds",
                "'Many warriors complain that",
                "spears are difficult to find,",
                "we barbarians thus forge our",
                "own. If you use our special",
                "barbarian anvil here, you will",
                "find it ideal. Other forges are",
                "not sturdy enough or shaped",
                "appropriately for the forging",
                "work involved. Make any of",
                "our spears and return.",
                "Note well that you will",
                "require wood for the spear",
                "shafts, the quality of wood",
                "must be similar to that of the",
            ),
            listOf(
                "metal involved.'",
            )
        )

        private val SPEAR_SMITHING_DONE = listOf(
            listOf(
                "",
                BLUE + "Otto's words on Smithing",
                BLUE + "spears",
                "'The next step is to",
                "manufacture a spear, suitable",
                "for combat. Our distant",
                "cousins on Karamja are in",
                "need of help, however, and",
                "you must aid them before I",
                "can aid you. You must go",
                "now and complete the Tai",
                "Bwo Wannai Trio quest.'",
                "",
                "",
                BLUE + "Since I have completed this"
            ),
            listOf(
                BLUE + "quest, he adds",
                "'Many warriors complain that",
                "spears are difficult to find,",
                "we barbarians thus forge our",
                "own. If you use our special",
                "barbarian anvil here, you will",
                "find it ideal. Other forges are",
                "not sturdy enough or shaped",
                "appropriately for the forging",
                "work involved. Make any of",
                "our spears and return.",
                "Note well that you will",
                "require wood for the spear",
                "shafts, the quality of wood",
                "must be similar to that of the",
            ),
            listOf(
                "metal involved.'",
                "The manufacture of spears is",
                "now yours as a speciality.",
                "Use your skill well. In",
                "addition, I am ready to",
                "reveal more spear-related",
                "crafts.'",
            )
        )


        private val PYRE_SHIPS = listOf(
            listOf(
                "",
                BLUE + "Otto's words on Crafting",
                BLUE + "pyre ships",
                "",
                "'The next stage is quite",
                "complex, so listen well. In",
                "order to send our ancestors",
                "into the spirit world, their",
                "mortal remains must be",
                "burned with due ceremony.",
                "This can only be performed",
                "close to the water on the",
                "short of the lake, just to our",
                "north-east. You will recognise",
                "the correct places by the",
            ),
            listOf(
                "ashes to be seen there. You",
                "will need to construct a small",
                "ship by using an axe upon",
                "logs in the area, then add the",
                "bones of a long dead",
                "barbarian hero. From the",
                "caverns beneath this lake.",
                "Many of our ancestors",
                "travelled to these caverns in",
                "order to hunt for glory and",
                "found only death. Their",
                "bones must still lie inside,",
                "their spirits trapped in",
                "torment. The spirit will",
                "ascend to glory, the pyre will",
            ),
            listOf(
                "send the earthly remains to",
                "the depths. You will also",
                "obtain a closer link to the",
                "spirit world. During this",
                "heightened contact, any bones",
                "you bury will have increased",
                "importance to the gods. The",
                "number of bones that may be",
                "buried, before the link fades,",
                "is increased with the difficulty",
                "of obtaining the wood which",
                "you use. I have little",
                "knowledge of the caverns,",
                "they are blocked from the",
                "sight of the spirits with whom",
            ),
            listOf(
                "I commune. I can only",
                "suspect that whatever slew",
                "barbarian heroes is indeed",
                "mighty. I would also suggest",
                "that these bones might well be",
                "very uncommon, since heroes",
                "are not found in vast",
                "numbers. Good luck. Dive",
                "into the whirlpool in the lake",
                "to the east. The spirits will",
                "use their abilities to ensure",
                "you arrive in the correct",
                "location, though their",
                "influence fades so you must",
                "find your own way out.'",
            )
        )

        private val PYRE_SHIPS_DONE = listOf(
            listOf(
                "'Hail to you, savior of my",
                "ancestors. The spirits herald",
                "your presence with a spectral",
                "fanfare. On this great day",
                "you have my thanks, eternally",
                "May you find riches while",
                "rescuing my spiritual",
                "ancestors in the caverns",
                "for many moons to come.'",
            )
        )

        private val HASTA_SMITHING = listOf(
            listOf(
                "",
                BLUE + "Otto's words on one-handed",
                BLUE + "spears",
                "",
                "'The next step is to",
                "manufacture a one handed",
                "version of a spear, suitable",
                "for combat. Such a spear is",
                "known to us as a hasta. As",
                "you might suspect, our ways",
                "require greater",
                "understanding than is gained",
                "by simply looking at a",
                "weapon.",
                "It is also the case that the",
            ),
            listOf(
                "process involves a differently-",
                "balanced spear.",
                "Before you may use such a",
                "weapon in anger, you must",
                "make an example. Only then",
                "will you fully understand the",
                "poise and techniques involved.",
                "You may use our special",
                "anvil for this spear type too.",
                "As the ways of black and",
                "dragon spears are beyond",
                "our knowledge, however, these",
                "spears may not be created.'",
            )
        )

        private val HASTA_SMITHING_DONE = listOf(
            listOf(
                "",
                BLUE + "Otto's words on one-handed",
                BLUE + "spears",
                "",
                "'The next step is to",
                "manufacture a one handed",
                "version of a spear, suitable",
                "for combat. Such a spear is",
                "known to us as a hasta. As",
                "you might suspect, our ways",
                "require greater",
                "understanding than is gained",
                "by simply looking at a",
                "weapon.",
                "It is also the case that the",
            ),
            listOf(
                "process involves a differently-",
                "balanced spear.",
                "Before you may use such a",
                "weapon in anger, you must",
                "make an example. Only then",
                "will you fully understand the",
                "poise and techniques involved.",
                "You may use our special",
                "anvil for this spear type too.",
                "As the ways of black and",
                "dragon spears are beyond",
                "our knowledge, however, these",
                "spears may not be created.'",
                "'I see you have constructed",
                "your hasta, and are",
            ),
            listOf(
                "approaching readiness to live",
                "life to its fullest - that you",
                "may be a peaceful spirit when",
                "your time ends.'",
            )
        )


        private fun buildContents(player: Player): Array<PageSet> {
            val rawPages = mutableListOf<List<String>>()

            val rodStage      = getAttribute(player, BarbarianTraining.attributeRod, 0)
            val fistStage     = getAttribute(player, BarbarianTraining.attributeFist, 0)
            val bowfireStage  = getAttribute(player, BarbarianTraining.attributeBowFire, 0)
            val herbloreStage = getAttribute(player, BarbarianTraining.attributeHerblore, 0)
            val spearRejected = getAttribute(player, BarbarianTraining.attributeSpearRejected, 0)
            val spearStage    = getAttribute(player, BarbarianTraining.attributeSpear, 0)
            val pyreshipStage = getAttribute(player, BarbarianTraining.attributePyreShip, 0)
            val hastaStage    = getAttribute(player, BarbarianTraining.attributeHasta, 0)

            if (rodStage == 0)              rawPages.addAll(INTRODUCTION)
            if (rodStage >= 1)              rawPages.addAll(ROD_FISHING)
            if (rodStage >= 3)              rawPages.addAll(ROD_FISHING_DONE)
            if (fistStage in 1..2)          rawPages.addAll(BAREHANDED_FISHING)
            if (fistStage >= 3)             rawPages.addAll(BAREHANDED_FISHING_DONE)
            if (bowfireStage in 1..2)       rawPages.addAll(BOW_FIREMAKING)
            if (bowfireStage >= 3)          rawPages.addAll(BOW_FIREMAKING_DONE)
            if (herbloreStage >= 1)         rawPages.addAll(HERBLORE)
            if (herbloreStage >= 3)         rawPages.addAll(HERBLORE_DONE)
            if (spearRejected != 0)         rawPages.addAll(SPEAR_REJECTED)
            if (spearStage in 1..2)         rawPages.addAll(SPEAR_SMITHING)
            if (spearStage >= 3)            rawPages.addAll(SPEAR_SMITHING_DONE)
            if (pyreshipStage >= 1)         rawPages.addAll(PYRE_SHIPS)
            if (pyreshipStage >= 3)         rawPages.addAll(PYRE_SHIPS_DONE)
            if (hastaStage in 1..2)         rawPages.addAll(HASTA_SMITHING)
            if (hastaStage >= 3)            rawPages.addAll(HASTA_SMITHING_DONE)

            return rawPages.toPageSets()
        }
    }

    override fun defineListeners() {
        on(Items.BARBARIAN_SKILLS_11340, IntType.ITEM, "read") { player, _ ->
            val contents = buildContents(player)
            BookInterface.openBook(player, BookInterface.FANCY_BOOK_26) { p, _, _ ->
                BookInterface.pageSetup(p, BookInterface.FANCY_BOOK_26, TITLE, contents)
                true
            }
            return@on true
        }
    }
}