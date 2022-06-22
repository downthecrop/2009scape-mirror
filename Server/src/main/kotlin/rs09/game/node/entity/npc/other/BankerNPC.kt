package rs09.game.node.entity.npc.other

import api.getScenery
import api.hasSealOfPassage
import core.game.content.dialogue.DialogueInterpreter
import core.game.node.Node
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.world.map.Direction
import core.game.world.map.Location
import core.plugin.Initializable
import rs09.game.interaction.npc.BankerNPCListener
import rs09.game.interaction.`object`.BankBoothHandler

private const val LUNAR_ISLE_BANK_REGION = 8253

@Initializable
class BankerNPC : AbstractNPC {

    companion object {
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
    }

    //Constructor spaghetti because Arios I guess
    constructor() : super(0, null)
    private constructor(id: Int, location: Location) : super(id, location)

    override fun construct(id: Int, location: Location, vararg objects: Any?): AbstractNPC {
        return BankerNPC(id, location)
    }

    fun findAdjacentBankBoothLocation(): Pair<Direction, Location>? {
        for (side in arrayOf(Direction.SOUTH, Direction.WEST, Direction.NORTH, Direction.EAST)) {
            val boothLocation = location.transform(side)
            val sceneryObject = getScenery(boothLocation)

            if (sceneryObject != null && sceneryObject.id in BankBoothHandler.BANK_BOOTHS) {
                return Pair(side, boothLocation.transform(side, 1))
            }
        }

        return null
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

    override fun getIds(): IntArray = BankerNPCListener.BANKER_NPCS
}