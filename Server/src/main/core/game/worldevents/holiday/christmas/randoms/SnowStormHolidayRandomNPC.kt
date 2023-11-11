package core.game.worldevents.holiday.christmas.randoms

import core.api.*
import core.game.node.entity.npc.NPC
import core.game.world.map.RegionManager
import core.game.world.map.path.Pathfinder
import core.game.worldevents.holiday.HolidayRandomEventNPC
import core.tools.RandomFunction
import core.tools.minutesToTicks
import org.rs09.consts.NPCs
import org.rs09.consts.Sounds

class SnowStormHolidayRandomNPC : HolidayRandomEventNPC(NPCs.SNOW_6740) {
    private lateinit var snowTwoNPC: NPC
    private lateinit var snowThreeNPC: NPC
    private lateinit var snowFourNPC: NPC
    private lateinit var snowFiveNPC: NPC
    private lateinit var snowNPCs: List<NPC>
    private val songs = listOf(189, 208, 209, 532, 547, 592)
    private var snowFinalized = false

    override fun init() {
        snowTwoNPC = create(NPCs.SNOW_6740, getPathableRandomLocalCoordinate(player, 3, player.location))
        snowThreeNPC = create(NPCs.SNOW_6740, getPathableRandomLocalCoordinate(player, 3, player.location))
        snowFourNPC = create(NPCs.SNOW_6740, getPathableRandomLocalCoordinate(player, 3, player.location))
        snowFiveNPC = create(NPCs.SNOW_6740, getPathableRandomLocalCoordinate(player, 3, player.location))
        snowNPCs = listOf(this, snowTwoNPC, snowThreeNPC, snowFourNPC, snowFiveNPC)
        ticksLeft = minutesToTicks(2)
        super.init()
        snowTwoNPC.init()
        snowThreeNPC.init()
        snowFourNPC.init()
        snowFiveNPC.init()
        val nearbyPlayers = RegionManager.getLocalPlayers(player.location, 8)
        for (p in nearbyPlayers) {
            p.musicPlayer.unlock(songs.random(), true)
        }
    }

    override fun tick() {
        if (getWorldTicks() % 8 == 0) {
            moveSnow()
        }
        super.tick()
    }

    private fun moveSnow() {
        for (snow in snowNPCs) {
            val snowPath = Pathfinder.find(snow, getPathableRandomLocalCoordinate(player, 4, player.location))
            snowPath.walk(snow)

            if (RandomFunction.roll(4))
                playGlobalAudio(snow.location, Sounds.ICY_WIND_2078)
        }
    }

    override fun follow() {
    }

    override fun terminate() {
        if (!snowFinalized) {
            snowFinalized = true
            for (snow in snowNPCs) {
                poofClear(snow)
            }
        }
        super.terminate()
    }

    override fun talkTo(npc: NPC) {
    }
}