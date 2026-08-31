package content.region.kandarin.quest.barbariantraining

import content.global.handlers.iface.BookInterface
import content.global.handlers.iface.BookLine
import content.global.handlers.iface.Page
import content.global.handlers.iface.PageSet
import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import org.rs09.consts.Items

/**
 * My Notes book given by Otto Godblessed during Barbarian Training.
 * Pages are added by using the "copy to log" option on Ancient Page items.
 *
 * Each page's presence in the book is tracked via [BarbarianTraining.attributeMyNotesPage].
 *
 * @see BarbarianSkillsBook for explanation of dynamic page generation
 * @source https://runescape.wiki/w/My_notes?oldid=565587
 * @author Bishop
 */

class MyNotesBook : InteractionListener {
    companion object {
        private const val TITLE = "My notes"

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

        private val PAGE_1 = listOf(
            "",
            "",
            "I have researched these",
            "accursed creatures of mithril",
            "more, the results are",
            "disturbing. Analysis leads me",
            "to believe that they were",
            "created by a higher power,",
            "what sort of creature could",
            "be so powerful?",
        )

        private val PAGE_2 = listOf(
            "",
            "",
            "...metallic for missiles and",
            "magical energies...green for",
            "close combat and magic...both",
            "have their breath weapon for",
            "which they are infamous...",
            "next I will try reason and",
            "logic, may Guthix watch over",
            "me.",
        )

        private val PAGE_3 = listOf(
            "",
            "",
            "I must caution against",
            "offering the wrong gifts to",
            "the spirits inhabiting the",
            "world beyond this. They seem",
            "quick to anger if the offerer",
            "was in any way responsible",
            "for the offering's death.",
        )

        private val PAGE_4 = listOf(
            "",
            "",
            "Item 1 - mithril present in",
            "large quantities as suspected.",
            "Item 2 - guarded by vast",
            "dragons of the same metal.",
            "Recommendation - this has",
            "potential to undermine our",
            "monopoly, suggest we keep",
            "the location secret.",
        )

        private val PAGE_5 = listOf(
            "",
            "",
            "The combination of each of",
            "these prime ingredients will",
            "result in a fine pie,",
            "heightening the reactions of",
            "the lucky fellow who eats it.",
            "Sadly, most demand in my",
            "establishment is from scruffy",
            "fellows who make the place",
            "look untidy.",
        )

        private val PAGE_6 = listOf(
            "",
            "",
            "...no one I know of has",
            "access to an onyx amulet,",
            "the cost is just too immense.",
            "Enchanted, charged dragonstone",
            "amulets appear to be able to",
            "increase the perceptions of",
            "the wearer, at least when",
            "mining rocks for ore, they",
            "can increase the yield of",
            "precious gems.",
        )

        private val PAGE_7 = listOf(
            "",
            "",
            "...because her coach was a",
            "pumpkin. We laughed so much",
            "I thought my sides would",
            "split. I think that too much",
            "dwarven ale may have clouded",
            "my views, since it no longer",
            "seems a fit fest...",
        )

        private val PAGE_8 = listOf(
            "",
            "",
            "In search of the incarnation",
            "of Scabaras, I probe the",
            "darkest recesses of the",
            "hidden world. If he lies in",
            "wait, I must be the one to",
            "reunite him with his fellow",
            "deities. What would befall us",
            "if he were forever isolated?",
            "His bitterness can only",
            "grow...",
        )

        private val PAGE_9 = listOf(
            "",
            "",
            "I am not sure anyone will",
            "ever read this, as I say",
            "my final farewells. If any",
            "find this pass on my regards",
            "to Annalise, if she still",
            "lives. I cannot see this",
            "being a place visited by",
            "those as doomed as I am.",
        )

        private val PAGE_10 = listOf(
            "",
            "",
            "If I ever get my hands on",
            "Otto he'll be in his grave.",
            "Sending me to this place to",
            "fester and die. 'See wonders'",
            "he says 'Impress the spirits'",
            "he says. The only way I'll",
            "be impressing the spirits",
            "is as one. While he sits out",
            "there living in luxury...",
        )

        private val PAGE_11 = listOf(
            "",
            "",
            "...is the stone here?...was",
            "the stone here?...will the",
            "stone be here?...would that",
            "my sanity were firm enough",
            "that I knew what my questions",
            "meant. Then I might seek",
            "an answer.",
        )

        private val PAGE_12 = listOf(
            "",
            "",
            "A momentary flash but I saw",
            "it! Hornless but the same",
            "deep crimson of its set-mates.",
            "A glorious thing to see,",
            "guarded by the creatures in",
            "the upper galleries. I must",
            "have it, I will have it!",
        )

        private val PAGE_13 = listOf(
            "",
            "",
            "It read 'Laufata ki Glough",
            "ki Ta Quir Priw Undo eso,",
            "Tolly, gnomo kar is Glough",
            "hamo sarko pro Arposandra",
            "Qua!' - If only I had",
            "studied my languages more",
            "diligently.",
        )

        private val PAGE_14 = listOf(
            "",
            "",
            "When Pukkamay first made toad",
            "crunchies, everyone thought",
            "he was mad. 'Chewy toads,",
            "in crunchies? It'll never",
            "work' they said - how wrong",
            "they were...",
        )

        private val PAGE_15 = listOf(
            "",
            "",
            "...I learned my lesson when",
            "I tried to take that",
            "Zamorakian wine. I really",
            "think the monks overreacted.",
            "I always thought they were",
            "supposed to be teetotal",
            "anyway.",
        )

        private val PAGE_16 = listOf(
            "",
            "",
            "The Baxtorian Falls are well",
            "worth investigating, in my",
            "opinion, since water leaves",
            "the lake above this area yet",
            "none appears to flow into it.",
            "What could be the source of",
            "this quantity of water?",
        )

        private val PAGE_17 = listOf(
            "",
            "",
            "My investigations show that",
            "water temperature in the",
            "Baxtorian Falls area are",
            "substantially higher than",
            "might be expected given the",
            "local climate. The source of",
            "this heat will now be the",
            "focus of my studies.",
        )

        private val PAGE_18 = listOf(
            "",
            "",
            "Our best guess is that some",
            "sort of heat generation",
            "process is occurring in the",
            "tunnels we have scryed,",
            "though the denizens are",
            "proving quite a hindrance to",
            "our free travel. Research was",
            "never this dangerous for my",
            "teachers.",
        )

        private val PAGE_19 = listOf(
            "",
            "",
            "Barakur has proved his worth,",
            "as we were forced to slay",
            "several dragons on our way",
            "to inner regions. The caves",
            "are clearly of artificial",
            "manufacture, though the",
            "excavations must have been",
            "thousands of years ago, to",
            "judge by the...",
        )

        private val PAGE_20 = listOf(
            "",
            "",
            "Our theory of artificial",
            "origins has been vindicated!",
            "We have discovered a sturdy",
            "door, which seems to be a",
            "similar age to the original",
            "excavations. I am sure that",
            "there is a magical or",
            "mechanical activity behind",
            "it, though it may simply be",
            "a great rush of water.",
        )

        private val PAGE_21 = listOf(
            "",
            "",
            "The door resists all of our",
            "efforts to penetrate it,",
            "even the Fishing explosives",
            "which Derril suggested were",
            "tried with no success. There",
            "must be some way through",
            "which is linked to magic",
            "though perhaps all who know",
            "the secret are now long dead.",
        )

        private val PAGE_22 = listOf(
            "",
            "",
            "Buy from Bob's Brilliant",
            "Axes - if you see our prices",
            "bettered, let us know and we",
            "will best them. Offer subject",
            "to availability and at Bob's",
            "discretion. Does not affect",
            "your legal rights under",
            "Lumbridge laws.",
        )

        private val PAGE_23 = listOf(
            "",
            "",
            "...barred the magi loyal to",
            "the great lord Zamorak from",
            "entering the tower, keeping",
            "the new-found power of the",
            "runes to themselves. But one",
            "brave soul gathered his",
            "followers and forced his way",
            "into the library...",
        )

        private val PAGE_24 = listOf(
            "",
            "",
            "...created spheres of power",
            "to support those who should",
            "come to that dread place",
            "thereafter. But the well",
            "senses the light of these",
            "spheres, and will not...",
        )

        private val PAGE_25 = listOf(
            "",
            "",
            "...and I have lost my way in",
            "the accursed dungeon. The",
            "air is foul and bugs are",
            "everywhere...a noise from",
            "down the tunnel. There was",
            "a bell on the floor. Grasping",
            "the bell I rang it with all",
            "my might...it attacked me.",
        )

        private val PAGE_26 = listOf(
            "",
            "",
            "...perhaps dragonkin were",
            "involved? A wild theory,",
            "I know, but look at the",
            "evidence...",
        )

        private val PAGE_CONTENT = mapOf(
             1 to PAGE_1,   2 to PAGE_2,   3 to PAGE_3,   4 to PAGE_4,
             5 to PAGE_5,   6 to PAGE_6,   7 to PAGE_7,   8 to PAGE_8,
             9 to PAGE_9,  10 to PAGE_10, 11 to PAGE_11, 12 to PAGE_12,
            13 to PAGE_13, 14 to PAGE_14, 15 to PAGE_15, 16 to PAGE_16,
            17 to PAGE_17, 18 to PAGE_18, 19 to PAGE_19, 20 to PAGE_20,
            21 to PAGE_21, 22 to PAGE_22, 23 to PAGE_23, 24 to PAGE_24,
            25 to PAGE_25, 26 to PAGE_26,
        )

        private val EMPTY_PAGE = listOf(
            "",
        )

        private fun buildContents(player: Player): Array<PageSet> {
            val rawPages = mutableListOf<List<String>>()
            for (i in 1..26) {
                if (getAttribute(player, "${BarbarianTraining.attributeMyNotesPage}$i", false)) {
                    PAGE_CONTENT[i]?.let { rawPages.add(it) }
                }
            }
            if (rawPages.isEmpty()) rawPages.add(EMPTY_PAGE)
            return rawPages.toPageSets()
        }
    }

    override fun defineListeners() {
        on(Items.MY_NOTES_11339, IntType.ITEM, "read") { player, node ->
            val contents = buildContents(player)
            BookInterface.openBook(player, BookInterface.FANCY_BOOK_26) { p, _, _ ->
                BookInterface.pageSetup(p, BookInterface.FANCY_BOOK_26, TITLE, contents)
                true
            }
            storeBookInHouse(player, node)
            return@on true
        }

        on((Items.ANCIENT_PAGE_11341..Items.ANCIENT_PAGE_11366).toIntArray(), IntType.ITEM, "copy to log") { player, node ->
            val pageNum = node.asItem().id - Items.ANCIENT_PAGE_11341 + 1
            val attrKey = "${BarbarianTraining.attributeMyNotesPage}$pageNum"
            if (removeItem(player, node)) {
                openInterface(player, 101)
                val pageText = "<br><br>" + " ".repeat(60) + (PAGE_CONTENT[pageNum]
                    ?.filter { it.isNotEmpty() }
                    ?.joinToString(" ") ?: "")
                player.packetDispatch.sendString(pageText, 101, 1)
                if (getAttribute(player, attrKey, false)) {
                    sendMessage(player, "You already have this information in your logbook.")
                } else {
                    setAttribute(player, attrKey, true)
                    sendMessage(player, "You copy the scrap of information to your logbook.")
                }
            }
            return@on true
        }
    }
}