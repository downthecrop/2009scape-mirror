package rs09.game.node.entity.npc

import api.*
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.world.map.Direction
import core.game.world.map.Location
import core.plugin.Initializable
import org.rs09.consts.NPCs
import rs09.game.interaction.InteractionListener
import rs09.game.interaction.IntType
import rs09.game.interaction.`object`.BankBoothListener

/**
 * Provides dialogue tree for all generic banker NPCs as well as
 * handles all the common interactions like 'bank' and 'collect'.
 *
 * @author vddCore
 */
@Initializable
class BankerNPC : AbstractNPC, InteractionListener {
    companion object {
        private const val LUNAR_ISLE_BANK_REGION = 8253

        val SPECIAL_NPC_IDS = intArrayOf(
            NPCs.SIRSAL_BANKER_4519, NPCs.FADLI_958, NPCs.BANK_TUTOR_4907, NPCs.JADE_4296,
            NPCs.OGRESS_BANKER_7049, NPCs.OGRESS_BANKER_7050, NPCs.ARNOLD_LYDSPOR_3824,

            /* Maximillian Sackville - Near Wilderness bounty-hunter area. */
            NPCs.BANKER_6538
        )

        val NPC_IDS = intArrayOf(
            NPCs.BANKER_44, NPCs.BANKER_45, NPCs.BANKER_494, NPCs.BANKER_495, NPCs.BANKER_496, NPCs.BANKER_497,
            NPCs.BANKER_498, NPCs.BANKER_499, NPCs.BANKER_1036, NPCs.BANKER_1360, NPCs.BANKER_2163, NPCs.BANKER_2164,
            NPCs.BANKER_2354, NPCs.BANKER_2355, NPCs.BANKER_2568, NPCs.BANKER_2569, NPCs.BANKER_2570, NPCs.BANKER_3198,
            NPCs.BANKER_3199, NPCs.BANKER_5258, NPCs.BANKER_5259, NPCs.BANKER_5260, NPCs.BANKER_5261, NPCs.BANKER_5776,
            NPCs.BANKER_5777, NPCs.BANKER_5912, NPCs.BANKER_5913, NPCs.BANKER_6200, NPCs.BANKER_6532, NPCs.BANKER_6533,
            NPCs.BANKER_6534, NPCs.BANKER_6535, NPCs.BANKER_7445, NPCs.BANKER_7446, NPCs.BANKER_7605,
            NPCs.GUNDAI_902,

            NPCs.GHOST_BANKER_1702, NPCs.GNOME_BANKER_166, NPCs.NARDAH_BANKER_3046, NPCs.MAGNUS_GRAM_5488, NPCs.TZHAAR_KET_ZUH_2619,
        )

        private val ALL_BANKER_NPC_IDS = intArrayOf(
            *SPECIAL_NPC_IDS,
            *NPC_IDS
        )

        /**
         * This is poorly named, but performs a few checks to see if the player
         * is trying to access the bank on the Lunar Isle and returns a boolean
         * controlling whether or not to pass the quick bank or collection use.
         *
         * TODO
         * The location of this method is shit too. Find a better place for it?
         */
        fun checkLunarIsleRestriction(player: Player, node: Node): Boolean {
            if (node.location.regionId != LUNAR_ISLE_BANK_REGION)
                return false

            if (!hasSealOfPassage(player)) {
                return true
            }

            return false
        }

        fun attemptBank(player: Player, node: Node): Boolean {
            val npc = node as NPC

            if (checkLunarIsleRestriction(player, node)) {
                openDialogue(player, npc.id, npc)
                return true
            }

            npc.faceLocation(null)
            openBankAccount(player)

            return true
        }

        fun attemptCollect(player: Player, node: Node): Boolean {
            val npc = node as NPC

            if (checkLunarIsleRestriction(player, node)) {
                openDialogue(player, npc.id, npc)
                return true
            }

            npc.faceLocation(null)
            openGrandExchangeCollectionBox(player)

            return true
        }
    }

    //Constructor spaghetti because Arios I guess
    constructor() : super(0, null)
    private constructor(id: Int, location: Location) : super(id, location)

    override fun construct(id: Int, location: Location, vararg objects: Any?): AbstractNPC {
        return BankerNPC(id, location)
    }

    private fun findAdjacentBankBoothLocation(): Pair<Direction, Location>? {
        for (side in arrayOf(Direction.SOUTH, Direction.WEST, Direction.NORTH, Direction.EAST)) {
            val boothLocation = location.transform(side)
            val sceneryObject = getScenery(boothLocation)

            if (sceneryObject != null && sceneryObject.id in BankBoothListener.BANK_BOOTHS) {
                return Pair(side, boothLocation.transform(side, 1))
            }
        }

        return null
    }

    private fun provideDestinationOverride(entity: Entity, node: Node): Location {
        val npc = node as NPC

        return when(npc.id) {
            /* Ogress bankers are 2x2 with their spawn being offset to south-western tile. */
            NPCs.OGRESS_BANKER_7049,
            NPCs.OGRESS_BANKER_7050 -> npc.location.transform(3, 1, 0)

            NPCs.BANKER_6532, NPCs.BANKER_6533,
            NPCs.BANKER_6534, NPCs.BANKER_6535 -> npc.location.transform(npc.direction, 1)

            /* Magnus has no bank booth nearby so we need to handle that edge case here. */
            NPCs.MAGNUS_GRAM_5488 -> npc.location.transform(Direction.NORTH, 2)

            else -> {
                if (npc is BankerNPC) {
                    npc.findAdjacentBankBoothLocation()?.let {
                        return it.second
                    }
                }

                return npc.location
            }
        }
    }

    override fun defineListeners() {
        on(ALL_BANKER_NPC_IDS, IntType.NPC, "bank", handler = Companion::attemptBank)
        on(ALL_BANKER_NPC_IDS, IntType.NPC, "collect", handler = Companion::attemptCollect)
    }

    override fun defineDestinationOverrides() {
        setDest(IntType.NPC, ALL_BANKER_NPC_IDS, "bank", "collect", "talk-to", handler = ::provideDestinationOverride)
    }

    override fun init() {
        super.init()

        findAdjacentBankBoothLocation()?.let {
            val (boothDirection, _) = it

            direction = boothDirection
            isWalks = false

            setAttribute("facing_booth", true)
        }
    }

    override fun getIds(): IntArray = ALL_BANKER_NPC_IDS
}
