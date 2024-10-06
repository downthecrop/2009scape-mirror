package content.global.activity.cchallange

import content.global.activity.cchallange.npc.EarthWarriorChampionNPC.Companion.spawnEarthWarriorChampion
import content.global.activity.cchallange.npc.GhoulChampionNPC.Companion.spawnGhoulChampion
import content.global.activity.cchallange.npc.GiantChampionNPC.Companion.spawnGiantChampion
import content.global.activity.cchallange.npc.GoblinChampionNPC.Companion.spawnGoblinChampion
import content.global.activity.cchallange.npc.HobgoblinChampionNPC.Companion.spawnHobgoblinChampion
import content.global.activity.cchallange.npc.ImpChampionNPC.Companion.spawnImpChampion
import content.global.activity.cchallange.npc.JogreChampionNPC.Companion.spawnJogreChampion
import content.global.activity.cchallange.npc.LesserDemonChampionNPC.Companion.spawnLesserDemonChampion
import content.global.activity.cchallange.npc.SkeletonChampionNPC.Companion.spawnSkeletonChampion
import content.global.activity.cchallange.npc.ZombieChampionNPC.Companion.spawnZombieChampion
import core.api.*
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneRestriction
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import org.rs09.consts.Scenery

/**
 * Represents the Champion challenge.
 */

class ChampionChallengeListener : InteractionListener, MapArea {

    val EARTH_WARRIOR_SCROLL = Items.CHAMPION_SCROLL_6798
    val GHOUL_SCROLL = Items.CHAMPION_SCROLL_6799
    val GIANT_SCROLL = Items.CHAMPION_SCROLL_6800
    val GOBLIN_SCROLL = Items.CHAMPION_SCROLL_6801
    val HOBGOBLIN_SCROLL = Items.CHAMPION_SCROLL_6802
    val IMP_SCROLL = Items.CHAMPION_SCROLL_6803
    val JOGRE_SCROLL = Items.CHAMPION_SCROLL_6804
    val LESSER_DEMON_SCROLL = Items.CHAMPION_SCROLL_6805
    val SKELETON_SCROLL = Items.CHAMPION_SCROLL_6806
    val ZOMBIE_SCROLL = Items.CHAMPION_SCROLL_6807

    val MESSAGE_SCROLL = 222

    private val IMP_SCROLL_TEXT = arrayOf(
        "How about picking on someone your own size? I'll",
        "see you at the Champion's Guild.",
        "",
        "Champion of Imps"
    )

    private val GOBLIN_SCROLL_TEXT = arrayOf(
        "Fight me if you think you can human, I'll wait",
        "for you in the Champion's Guild.",
        "",
        "Champion of Goblins"
    )

    private val SKELETON_SCROLL_TEXT = arrayOf(
        "I'll be waiting at the Champions' Guild to",
        "collect your bones.",
        "",
        "Champion of Skeletons"
    )

    private val ZOMBIE_SCROLL_TEXT = arrayOf(
        "You come to Champions' Guild, you fight me,",
        "I squish you, I get brains!",
        "",
        "Champion of Zombies"
    )

    private val GIANT_SCROLL_TEXT = arrayOf(
        "Get yourself to the Champions' Guild, if you",
        "dare to face me puny human.",
        "",
        "Champion of Giants"
    )

    private val HOBGOBLIN_SCROLL_TEXT = arrayOf(
        "You won't defeat me, though you're welcome to",
        "try at the Champions' Guild.",
        "",
        "Champion of Hobgoblins"
    )

    private val GHOUL_SCROLL_TEXT = arrayOf(
        "Come and duel me at the Champions' Guild, I'll",
        "make sure nothing goes to waste.",
        "",
        "Champion of Ghouls"
    )

    private val EARTH_WARRIOR_TEXT = arrayOf(
        "I challenge you to a duel, come to the arena beneath",
        "the Champion's Guild and fight me if you dare.",
        "",
        "Champion of Earth Warriors"
    )

    private val JOGRE_SCROLL_TEXT = arrayOf(
        "You think you can defeat me? Come to the",
        "Champion's Guild and prove it!",
        "",
        "Champion of Jogres"
    )

    private val LESSER_DEMON_SCROLL_TEXT = arrayOf(
        "Come to the Champion's Guild so I can banish",
        "you mortal!",
        "",
        "Champion of Lesser Demons"
    )

    private val PORTCULLIS = Scenery.PORTCULLIS_10553
    private val CHAMPION_STATUE_CLOSED = Scenery.CHAMPION_STATUE_10556
    private val CHAMPION_STATUE_OPEN = Scenery.CHAMPION_STATUE_10557
    private val TRAPDOOR_CLOSED = Scenery.TRAPDOOR_10558
    private val TRAPDOOR_OPEN = Scenery.TRAPDOOR_10559
    private val LARXUS = NPCs.LARXUS_3050

    private val ARENA_ZONE = 12696

    override fun defineListeners() {
        // Champion's Guild Basement Ladder to Main Floor
        addClimbDest(Location(3190, 9758, 0), Location(3190, 3356, 0))
        // Champion Statue Ladder to Arena
        addClimbDest(Location(3184, 9758, 0), Location(3182, 9758, 0))
        // Arena Ladder to Champion's Guild Basement
        addClimbDest(Location(3183, 9758, 0), Location(3185, 9758, 0))

        on(LARXUS, IntType.NPC, "talk-to") { player, _ ->
            openDialogue(player, LarxusDialogue(false))
            return@on true
        }

        on(ChampionScrollsDropHandler.SCROLLS, IntType.ITEM, "read") { player, node ->
            updateAndReadScroll(player, node.asItem())
            return@on true
        }

        onUseWith(IntType.NPC, ChampionScrollsDropHandler.SCROLLS, NPCs.LARXUS_3050) { player, _, _ ->
            openDialogue(player, LarxusDialogue(true))
            return@onUseWith true
        }

        on(TRAPDOOR_CLOSED, IntType.SCENERY, "open") { _, node ->
            replaceScenery(node.asScenery(), TRAPDOOR_OPEN, 100, node.location)
            return@on true
        }

        on(TRAPDOOR_OPEN, IntType.SCENERY, "close") { _, node ->
            replaceScenery(node.asScenery(), TRAPDOOR_CLOSED, -1, node.location)
            return@on true
        }

        on(CHAMPION_STATUE_CLOSED, IntType.SCENERY, "open") { _, node ->
            replaceScenery(node.asScenery(), CHAMPION_STATUE_OPEN, 100, node.location)
            return@on true
        }

        on(PORTCULLIS, IntType.SCENERY, "open") { player, node ->
            if (player.getAttribute("championsarena:start", false) == false) {
                sendNPCDialogue(player, NPCs.LARXUS_3050, "You need to arrange a challenge with me before you enter the arena.")
            } else {
                lock(player, 3)
                submitWorldPulse(object : Pulse() {
                    var counter = 0
                    override fun pulse(): Boolean {
                        when (counter++) {
                            1 -> {
                                player.familiarManager.dismiss()
                            }
                            2 -> DoorActionHandler.handleDoor(player, node.asScenery())
                            3 -> when{
                                removeItem(player, IMP_SCROLL) -> spawnImpChampion(player)
                                removeItem(player, GOBLIN_SCROLL) -> spawnGoblinChampion(player)
                                removeItem(player, SKELETON_SCROLL) -> spawnSkeletonChampion(player)
                                removeItem(player, ZOMBIE_SCROLL) -> spawnZombieChampion(player)
                                removeItem(player, GIANT_SCROLL) -> spawnGiantChampion(player)
                                removeItem(player, HOBGOBLIN_SCROLL) -> spawnHobgoblinChampion(player)
                                removeItem(player, GHOUL_SCROLL) -> spawnGhoulChampion(player)
                                removeItem(player, EARTH_WARRIOR_SCROLL) -> {
                                    spawnEarthWarriorChampion(player)
                                    player.prayer.reset()
                                }
                                removeItem(player, JOGRE_SCROLL) -> spawnJogreChampion(player)
                                removeItem(player, LESSER_DEMON_SCROLL) -> spawnLesserDemonChampion(player)
                            }
                        }
                        return false
                    }
                })
            }
            return@on true
        }
    }

    private fun updateAndReadScroll(player: Player, item: Item) {
        val id = item.id
        openInterface(player, MESSAGE_SCROLL).also {
            when (id) {
                IMP_SCROLL -> setInterfaceText(player, IMP_SCROLL_TEXT.joinToString("<br>"), MESSAGE_SCROLL, 4)
                GOBLIN_SCROLL -> setInterfaceText(player, GOBLIN_SCROLL_TEXT.joinToString("<br>"), MESSAGE_SCROLL, 4)
                SKELETON_SCROLL -> setInterfaceText(player, SKELETON_SCROLL_TEXT.joinToString("<br>"), MESSAGE_SCROLL, 4)
                ZOMBIE_SCROLL -> setInterfaceText(player, ZOMBIE_SCROLL_TEXT.joinToString("<br>"), MESSAGE_SCROLL, 4)
                GIANT_SCROLL -> setInterfaceText(player, GIANT_SCROLL_TEXT.joinToString("<br>"), MESSAGE_SCROLL, 4)
                HOBGOBLIN_SCROLL -> setInterfaceText(player, HOBGOBLIN_SCROLL_TEXT.joinToString("<br>"), MESSAGE_SCROLL, 4)
                GHOUL_SCROLL -> setInterfaceText(player, GHOUL_SCROLL_TEXT.joinToString("<br>"), MESSAGE_SCROLL, 4)
                EARTH_WARRIOR_SCROLL -> setInterfaceText(player, EARTH_WARRIOR_TEXT.joinToString("<br>"), MESSAGE_SCROLL, 4)
                JOGRE_SCROLL -> setInterfaceText(player, JOGRE_SCROLL_TEXT.joinToString("<br>"), MESSAGE_SCROLL, 4)
                LESSER_DEMON_SCROLL -> setInterfaceText(player, LESSER_DEMON_SCROLL_TEXT.joinToString("<br>"), MESSAGE_SCROLL, 4)
            }
        }
    }

    override fun defineAreaBorders(): Array<ZoneBorders> {
        return arrayOf(getRegionBorders(ARENA_ZONE))
    }

    override fun getRestrictions(): Array<ZoneRestriction> {
        return arrayOf(
            ZoneRestriction.CANNON,
            ZoneRestriction.FIRES,
            ZoneRestriction.RANDOM_EVENTS
        )
    }
}