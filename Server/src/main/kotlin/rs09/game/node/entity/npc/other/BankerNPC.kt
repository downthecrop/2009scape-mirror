package rs09.game.node.entity.npc.other

import api.getScenery
import core.game.node.entity.npc.AbstractNPC
import core.game.world.map.Direction
import core.game.world.map.Location
import core.plugin.Initializable
import rs09.game.interaction.npc.BankerNPCListener
import rs09.game.interaction.`object`.BankBoothHandler


@Initializable
class BankerNPC : AbstractNPC {
    //Constructor spaghetti because Arios I guess
    constructor() : super(0, null)
    private constructor(id: Int, location: Location) : super(id, location)

    override fun construct(id: Int, location: Location, vararg objects: Any?): AbstractNPC {
        return BankerNPC(id, location)
    }

    override fun init() {
        super.init()

        for (side in Direction.values()) {
            val boothLocation = location.transform(side)
            val bankObject = getScenery(boothLocation)

            if (bankObject != null && bankObject.id in BankBoothHandler.BANK_BOOTHS) {
                direction = side
                isWalks = false

                setAttribute("facing_booth", true)
                break
            }
        }
    }

    override fun getIds(): IntArray = BankerNPCListener.BANKER_NPCS
}