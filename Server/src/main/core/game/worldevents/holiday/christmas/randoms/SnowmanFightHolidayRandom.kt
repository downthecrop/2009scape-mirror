package core.game.worldevents.holiday.christmas.randoms

import core.api.*
import core.game.node.entity.npc.NPC
import core.game.worldevents.holiday.HolidayRandomEventNPC
import core.tools.minutesToTicks
import org.rs09.consts.NPCs

class SnowmanFightHolidayRandom : HolidayRandomEventNPC(NPCs.BARBARIAN_SNOWMAN_6742) {
    private lateinit var otherSnowman: NPC
    private val snowmen = listOf(NPCs.DRAGON_SNOWMAN_6743, NPCs.PIRATE_SNOWMAN_6745, NPCs.DWARF_SNOWMAN_6744)
    private var snowmenFinalized = false

    override fun init() {
        otherSnowman = create(snowmen.random(), getPathableRandomLocalCoordinate(player, 3, player.location))
        ticksLeft = minutesToTicks(3)
        setAttribute(this, "holidayrandomplayer", player)
        setAttribute(otherSnowman, "holidayrandomplayer", player)
        setAttribute(otherSnowman, "holiday_random_extra_npc", true)
        this.behavior = SnowmanFightBehavior()
        otherSnowman.behavior = SnowmanFightBehavior()
        super.init()
        otherSnowman.init()

        this.attack(otherSnowman)
        otherSnowman.attack(this)
    }

    override fun tick() {
        if (!this.inCombat() && getWorldTicks() % 20 == 0) {
            this.teleport(otherSnowman.location)
            this.attack(otherSnowman)
            otherSnowman.attack(this)
        }
        super.tick()
    }

    override fun terminate() {
        if (!snowmenFinalized) {
            snowmenFinalized = true
            poofClear(otherSnowman)
        }
        super.terminate()
    }

    override fun follow() {
    }

    override fun talkTo(npc: NPC) {
        sendMessage(player, "They seem a little busy.")
    }
}