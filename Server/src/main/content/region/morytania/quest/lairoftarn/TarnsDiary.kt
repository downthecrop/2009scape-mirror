package content.region.morytania.quest.lairoftarn

import content.global.handlers.iface.*
import core.api.storeBookInHouse
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import org.rs09.consts.Items

/**
 * Tarn's Diary
 * Source: https://youtu.be/egdH7BM4WZk?si=ZS5G5rovN0ifGfDk&t=656
 */
class TarnsDiary : InteractionListener {
    companion object {
        private const val TITLE = "The Diary of Tarn Razorlor"
        private val CONTENTS = arrayOf(
            // It's hand-typed! Please be sure to appreciate each page equally.
            // page 1-2
            PageSet(
                Page(
                    BookLine("4th of Bennath", 55),
                    BookLine("", 56),
                    BookLine("All my long hours of", 57),
                    BookLine("research have finally", 58),
                    BookLine("payed off! Today I", 59), // 'payed' is authentic
                    BookLine("discovered an ancient", 60),
                    BookLine("journal in the great", 61),
                    BookLine("library, which detailed a", 62),
                    BookLine("ceremony for giving life", 63),
                    BookLine("back to greater beasts", 64),
                    BookLine("after they have died! I", 65),
                ),
                Page(
                    BookLine("shall finally be able to", 66),
                    BookLine("unravel the secrets of the", 67),
                    BookLine("Skeletal Wyverns!", 68),
                    BookLine("", 69),
                    BookLine("", 70),
                    BookLine("", 71),
                    BookLine("", 72),
                    BookLine("", 73),
                    BookLine("", 74),
                    BookLine("", 75),
                    BookLine("", 76),
                )
            ),
            // page 3-4
            PageSet(
                Page(
                    BookLine("18th of Raktuber", 55),
                    BookLine("", 56),
                    BookLine("I have spent over a", 57),
                    BookLine("month studying the spell", 58),
                    BookLine("of Greater Animation", 59),
                    BookLine("now. My studies have led", 60),
                    BookLine("me to believe that", 61),
                    BookLine("although this spell is more", 62),
                    BookLine("powerful than any I have", 63),
                    BookLine("ever encountered, and", 64),
                    BookLine("will indeed reanimate the", 65),
                ),
                Page(
                    BookLine("skeletons of long dead", 66),
                    BookLine("dragons and such", 67),
                    BookLine("creatures, it does not", 68),
                    BookLine("provide any way of", 69),
                    BookLine("controlling them. Whoever", 70),
                    BookLine("devised this spell must", 71),
                    BookLine("have been insane, because", 72),
                    BookLine("without somebody to", 73),
                    BookLine("control them, these", 74),
                    BookLine("undead monsters would", 75),
                    BookLine("have been free to destroy", 76),
                )
            ),
            // page 5-6
            PageSet(
                Page(
                    BookLine("everything that they", 55),
                    BookLine("encountered! No wonder", 56),
                    BookLine("the spell was hidden away", 57),
                    BookLine("in the great library.", 58),
                    BookLine("There is, however, a", 59),
                    BookLine("reference to an amulet of", 60),
                    BookLine("such power that any", 61),
                    BookLine("undead creature would", 62),
                    BookLine("tremble before someone", 63),
                    BookLine("wielding it. I must return", 64),
                    BookLine("to the library and see if I", 65),
                ),
                Page(
                    BookLine("can find any further", 66),
                    BookLine("reference to this magical", 67),
                    BookLine("amulet. Once I have it", 68),
                    BookLine("and can create and", 69),
                    BookLine("control undead dragons,", 70),
                    BookLine("even Lord Drakan and", 71),
                    BookLine("his minions shall tremble", 72),
                    BookLine("before my might!", 73),
                    BookLine("", 74),
                    BookLine("", 75),
                    BookLine("", 76),
                )
            ),
            // page 7-8
            PageSet(
                Page(
                    BookLine("29th of Raktuber", 55),
                    BookLine("", 56),
                    BookLine("I have spent nearly 2 full", 57),
                    BookLine("weeks searching the great", 58),
                    BookLine("library for a referenc to", 59),
                    BookLine("the writings that I need,", 60),
                    BookLine("with no success. Soon I", 61),
                    BookLine("shall be forced to start", 62),
                    BookLine("searching through the", 63),
                    BookLine("tombs and grimoires page", 64),
                    BookLine("by page.", 65),
                ),
                Page(
                    BookLine("8th of Pentember", 66),
                    BookLine("", 67),
                    BookLine("I have found a reference", 68),
                    BookLine("to the crystal! This", 69),
                    BookLine("morning, while reading", 70),
                    BookLine("the histories of Morytania,", 71),
                    BookLine("I happened upon this", 72),
                    BookLine("extract: 'And so our", 73),
                    BookLine("mighty Ruler ordered the", 74),
                    BookLine("mines to be sealed", 75),
                    BookLine("forever and all those who", 76),
                )
            ),
            // page 9-10
            PageSet(
                Page(
                    BookLine("slaved in the mines to be", 55),
                    BookLine("slain. Only by doing this", 56),
                    BookLine("could we seal away the", 57),
                    BookLine("threat of the Accursed", 58),
                    BookLine("Shard for all time!' I", 59),
                    BookLine("suspect this Accursed", 60),
                    BookLine("Shard to be the crystal", 61),
                    BookLine("that I seek! My next", 62),
                    BookLine("course of action must be", 63),
                    BookLine("to discover where these", 64),
                    BookLine("mines that are mentioned", 65),
                ),
                Page(
                    BookLine("can be found.", 66),
                    BookLine("", 67),
                    BookLine("", 68),
                    BookLine("", 69),
                    BookLine("", 70),
                    BookLine("", 71),
                    BookLine("", 72),
                    BookLine("", 73),
                    BookLine("", 74),
                    BookLine("", 75),
                    BookLine("", 76),
                )
            ),
            // page 11-12
            PageSet(
                Page(
                    BookLine("10th of Pentember", 55),
                    BookLine("", 56),
                    BookLine("Once again, the great", 57),
                    BookLine("library has provided the", 58),
                    BookLine("answer. I have found an", 59),
                    BookLine("ancient map that shows a", 60),
                    BookLine("series of mines in south-", 61),
                    BookLine("western Morytania, on", 62),
                    BookLine("the River Salve. These", 63),
                    BookLine("mines are not shown on", 64),
                    BookLine("modern maps. They must", 65),
                ),
                Page(
                    BookLine("be the place I seek.", 66),
                    BookLine("Tomorrow I shall go and", 67),
                    BookLine("find them!", 68),
                    BookLine("", 69),
                    BookLine("", 70),
                    BookLine("", 71),
                    BookLine("", 72),
                    BookLine("", 73),
                    BookLine("", 74),
                    BookLine("", 75),
                    BookLine("", 76),
                )
            ),
            // page 13-14
            PageSet(
                Page(
                    BookLine("11th of Pentember", 55),
                    BookLine("", 56),
                    BookLine("Today has been a hard", 57),
                    BookLine("day. We travelled south", 58),
                    BookLine("through desolate swamps,", 59),
                    BookLine("which were infested with", 60),
                    BookLine("ghasts. Many of my", 61),
                    BookLine("servants and soldiers fell", 62),
                    BookLine("to these creatures, but I", 63),
                    BookLine("have granted them", 64),
                    BookLine("eternal life as the undead,", 65),
                ),
                Page(
                    BookLine("so that they may", 66),
                    BookLine("continue to serve me.", 67),
                    BookLine("", 68),
                    BookLine("", 69),
                    BookLine("", 70),
                    BookLine("", 71),
                    BookLine("", 72),
                    BookLine("", 73),
                    BookLine("", 74),
                    BookLine("", 75),
                    BookLine("", 76),
                )
            ),
            // page 15-16
            PageSet(
                Page(
                    BookLine("12th of Pentember", 55),
                    BookLine("", 56),
                    BookLine("This morning we found", 57),
                    BookLine("out that all our food had", 58),
                    BookLine("spoiled over-night. We will", 59),
                    BookLine("need to hunt for food", 60),
                    BookLine("today, as we continue", 61),
                    BookLine("our trek southward, to", 62),
                    BookLine("the mines. I will be only", 63),
                    BookLine("too please to see the end", 64),
                    BookLine("of these accursed swamps", 65), // lack of a period here is an authentic typo.
                ),
                Page(
                    BookLine("16th of Pentember", 66),
                    BookLine("", 67),
                    BookLine("At last we have reached", 68),
                    BookLine("the mines! All my human", 69),
                    BookLine("companions have perished,", 70),
                    BookLine("but I have animated their", 71),
                    BookLine("corpses and now have", 72),
                    BookLine("servants aplenty to do", 73),
                    BookLine("my bidding. I just wish", 74),
                    BookLine("they weren't all so stupid!", 75),
                    BookLine("This afternoon we shall", 76),
                )
            ),
            // page 17-18
            PageSet(
                Page(
                    BookLine("search for the entrance", 55),
                    BookLine("to the mines.", 56),
                    BookLine("", 57),
                    BookLine("", 58),
                    BookLine("", 59),
                    BookLine("", 60),
                    BookLine("", 61),
                    BookLine("", 62),
                    BookLine("", 63),
                    BookLine("", 64),
                    BookLine("", 65),
                ),
                Page(
                    BookLine("17th of Pentember", 66),
                    BookLine("", 67),
                    BookLine("It has been an eventful", 68),
                    BookLine("morning. The day started", 69),
                    BookLine("with us finding a madman", 70),
                    BookLine("wandering in the area,", 71),
                    BookLine("also searching for an", 72),
                    BookLine("entrance to the mine. I", 73),
                    BookLine("tricked him into telling me", 74),
                    BookLine("where the original", 75),
                    BookLine("entrance was, and from", 76),
                )
            ),
            // page 19-20
            PageSet(
                Page(
                    BookLine("there my zombie minions", 55),
                    BookLine("soon found a way into", 56),
                    BookLine("the mines themselves. I", 57),
                    BookLine("can feel very powerful", 58),
                    BookLine("magic at work here,", 59),
                    BookLine("possibly even stronger", 60),
                    BookLine("than my own! In an", 61),
                    BookLine("attempt to find the source", 62),
                    BookLine("of this magic, I shall", 63),
                    BookLine("spend this afternoon", 64),
                    BookLine("preparing and casting", 65),
                ),
                Page(
                    BookLine("spells of location and", 66),
                    BookLine("scrying.", 67),
                    BookLine("", 68),
                    BookLine("", 69),
                    BookLine("", 70),
                    BookLine("", 71),
                    BookLine("", 72),
                    BookLine("", 73),
                    BookLine("", 74),
                    BookLine("", 75),
                    BookLine("", 76),
                )
            ),
            // page 21-22
            PageSet(
                Page(
                    BookLine("18th of Pentember", 55),
                    BookLine("", 56),
                    BookLine("I have seen the crystal", 57),
                    BookLine("that I seek, however it is", 58),
                    BookLine("guarded by a spirit of", 59),
                    BookLine("extreme power! I shall", 60),
                    BookLine("have my undead create a", 61),
                    BookLine("small dungeon, where I", 62),
                    BookLine("will be safe from attack", 63),
                    BookLine("and then I shall send", 64),
                    BookLine("them forth to slay this fell", 65),
                ),
                Page(
                    BookLine("spirit. I'm sure even one", 66),
                    BookLine("as powerful as that will", 67),
                    BookLine("fall to the numbers of", 68),
                    BookLine("undead that I can", 69),
                    BookLine("summon.", 70),
                    BookLine("", 71),
                    BookLine("", 72),
                    BookLine("", 73),
                    BookLine("", 74),
                    BookLine("", 75),
                    BookLine("", 76),
                )
            ),
            // page 23-24
            PageSet(
                Page(
                    BookLine("19th of Pentember", 55),
                    BookLine("", 56),
                    BookLine("Work began on the", 57),
                    BookLine("passage to my suite of", 58),
                    BookLine("rooms. My slaves work", 59),
                    BookLine("hard when the work is as", 60),
                    BookLine("simple as this, and we", 61),
                    BookLine("make excellent progress.", 62),
                    BookLine("The only thing of interest", 63),
                    BookLine("is that we found some", 64),
                    BookLine("strange stone blocks, of a", 65),
                ),
                Page(
                    BookLine("design that I have not", 66),
                    BookLine("seen before. I may study", 67),
                    BookLine("them further when I have", 68),
                    BookLine("more time available.", 69),
                    BookLine("", 70),
                    BookLine("", 71),
                    BookLine("", 72),
                    BookLine("", 73),
                    BookLine("", 74),
                    BookLine("", 75),
                    BookLine("", 76),
                )
            ),
            // page 25-26
            PageSet(
                Page(
                    BookLine("20th of Pentember", 55),
                    BookLine("", 56),
                    BookLine("Today we excavated the", 57),
                    BookLine("entrance to a building! I", 58),
                    BookLine("do not recognize the style", 59),
                    BookLine("of masonry used, but the", 60),
                    BookLine("creators must have been", 61),
                    BookLine("about the same height and", 62),
                    BookLine("girth as I am, judging by", 63),
                    BookLine("the size of the doorways and", 64),
                    BookLine("stair-cases. The temple is", 65),
                ),
                Page(
                    BookLine("fraught with traps, but", 66),
                    BookLine("will make a fine base for", 67),
                    BookLine("me to wait whilst my", 68),
                    BookLine("army of undead retrieve", 69),
                    BookLine("a piece of the Salve", 70),
                    BookLine("Crystal. I have several", 71),
                    BookLine("theories pertaining to the", 72),
                    BookLine("origin of this strange", 73),
                    BookLine("temple. If I am correct", 74),
                    BookLine("we are deep", 75),
                    BookLine("underground, directly", 76),
                )
            ),
            // page 27-29
            PageSet(
                Page(
                    BookLine("below the village of Burgh", 55),
                    BookLine("do Rott. It may be that", 56),
                    BookLine("this temple was built as a", 57),
                    BookLine("fortification against an", 58),
                    BookLine("invasion from the desert", 59),
                    BookLine("nomads, who live to the", 60),
                    BookLine("west of here.", 61),
                    BookLine("Alternatively, perhaps it", 62),
                    BookLine("was built at the bequest", 63),
                    BookLine("of one of the lesser", 64),
                    BookLine("vampire lords who ruled", 65),
                ),
                Page(
                    BookLine("this area when Drakan", 66),
                    BookLine("came to power. However,", 67),
                    BookLine("I believe that it was", 68),
                    BookLine("probably built to guard", 69),
                    BookLine("an ancient treasure. My", 70),
                    BookLine("reasoning is thus; there", 71),
                    BookLine("doesn't seem to be any", 72),
                    BookLine("type of altar that we have", 73),
                    BookLine("found yet and the temple", 74),
                    BookLine("is littered with traps.", 75),
                    BookLine("Further research may", 76),
                )
            ),
            // page 29-30
            PageSet(
                Page(
                    BookLine("reveal and answer. In the", 55),
                    BookLine("meantime I have", 56),
                    BookLine("enchanted this diary of", 57),
                    BookLine("mine with the spell needed", 58),
                    BookLine("to enhance the powers of", 59),
                    BookLine("the crystal. I await my", 60),
                    BookLine("minions' return.", 61),
                    BookLine("", 62),
                    BookLine("", 63),
                    BookLine("", 64),
                    BookLine("", 65),
                ),
                Page(
                    BookLine("", 66),
                    BookLine("", 67),
                    BookLine("", 68),
                    BookLine("", 69),
                    BookLine("", 70),
                    BookLine("", 71),
                    BookLine("", 72),
                    BookLine("", 73),
                    BookLine("", 74),
                    BookLine("", 75),
                    BookLine("", 76),
                )
            ),
        )

        private fun display(player: Player, pageNum: Int, buttonID: Int): Boolean {
            BookInterface.pageSetup(player, BookInterface.FANCY_BOOK_3_49, TITLE, CONTENTS)
            return true
        }
    }

    override fun defineListeners() {
        on(Items.TARNS_DIARY_10587, IntType.ITEM, "read") { player, node ->
            BookInterface.openBook(player, BookInterface.FANCY_BOOK_3_49, ::display)
            storeBookInHouse(player, node)
            return@on true
        }
    }
}