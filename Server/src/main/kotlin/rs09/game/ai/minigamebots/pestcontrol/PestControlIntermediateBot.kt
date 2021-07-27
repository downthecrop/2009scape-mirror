package rs09.game.ai.minigamebots.pestcontrol

import core.game.content.activity.pestcontrol.PestControlActivityPlugin
import core.game.world.map.Location
import core.tools.RandomFunction
import rs09.game.ai.pvmbots.CombatBotAssembler
import rs09.game.ai.pvmbots.PvMBots
import rs09.game.content.activity.pestcontrol.PestControlHelper
import java.util.*

class PestControlTestBot2(l: Location) : PvMBots(legitimizeLocation(l)) {

        var tick = 0
    var combatMoveTimer = 0
    var justStartedGame = true
    var movetimer = 0
    var openedGate = false
    var myCounter = 0
    val num = Random().nextInt(4)
    val myBoat = PestControlHelper.BoatInfo.INTERMEDIATE
    val combathandler = CombatStateIntermediate(this)

    var time = 0

     enum class State {
        OUTSIDE_GANGPLANK,
        REFRESH,
        WAITING_IN_BOAT,
        PLAY_GAME,
        GET_TO_PC
    }
    //Novice Lander co-ords (2657, 2639, 0)
    //Intermediate lander co-ords (2644, 2644, 0)
    //Veteran lander co-ords (2638, 2653 0)
    init {
        val random100 = Random().nextInt(100)
        if (random100 < 30) {
            setAttribute("pc_role","defend_squire");
        } else
        {
            setAttribute("pc_role","attack_portals")
            this.customState = "Fighting NPCs"
        }
        if (num <= 2) {
            CombatBotAssembler().gearPCiMeleeBot( this)
        } else {
            CombatBotAssembler().gearPCiRangedBot(this, Random().nextInt() % 2 == 0)
        }
    }


    override fun tick() {
        super.tick()
        tick++
        time++
        movetimer--
        if (movetimer <= 0) {
            movetimer = 0
            customState = state.toString() + movetimer
            when (state) {
                State.GET_TO_PC -> toPC()
                State.OUTSIDE_GANGPLANK -> enterBoat()
                State.WAITING_IN_BOAT -> idleInBoat()
                State.PLAY_GAME -> attackNPCs()
                State.REFRESH -> toPC()
            }
        }
    }

    private val state: State
        get() {
            if (PestControlHelper.landerContainsLoc2(this.getLocation())) {
                return State.WAITING_IN_BOAT
            }
            if (PestControlHelper.isInPestControlInstance(this)) {
                return State.PLAY_GAME
            }
            if (PestControlHelper.outsideGangplankContainsLoc2(this.getLocation())) {
                return State.OUTSIDE_GANGPLANK
            }
            if (time == 1200) {
                return State.GET_TO_PC//.also { println("I was stuck ${this.username}") }
            }
            return State.GET_TO_PC
        }


    private fun attackNPCs() {
        if (PestControlHelper.outsideGangplankContainsLoc2(getLocation())){
            PestControlActivityPlugin().leave(this,false)
            val test = getClosestNodeWithEntry(50, myBoat.ladderId)
            test ?: println("PC: Gangplank Null")
            test.interaction.handle(this, test.interaction[0])//.also { println("Intermediate - We think we is in pest control ${this.username}.") }
        }
        walkingQueue.isRunning = true

        if (getAttribute("pc_role","E") == "attack_portals") {
            combathandler.goToPortals()
        } else {
            movetimer = RandomFunction.random(2,10)
            val location = PestControlHelper.getMyPestControlSession2(this)?.squire?.location ?: location
            if(location != null) {
                randomWalkAroundPoint(location, 5)
            }
            combathandler.fightNPCs()
        }
    }

    private var insideBoatWalks = 3
     fun idleInBoat() {
         justStartedGame = true
         openedGate = false
         time = 0
         if (prayer.active.isNotEmpty()) {
             prayer.reset()
         }
         if (PestControlHelper.outsideGangplankContainsLoc2(getLocation())){
             val test = getClosestNodeWithEntry(15, myBoat.ladderId)
             test.interaction.handle(this,test.interaction[0])
             enterBoat()//.also { println("We think we is in boat ${this.username}.") }
         }
        if (Random().nextInt(100) < 40) {
            if (Random().nextInt(insideBoatWalks) <= 1) {
                (insideBoatWalks * 1.5).toInt()

                if (Random().nextInt(4) == 1) {
                    this.walkingQueue.isRunning = !this.walkingQueue.isRunning
                }
                if (Random().nextInt(7) >= 4) {
                    this.walkToPosSmart(myBoat.boatBorder.randomLoc)
                }
            }
            if (Random().nextInt(3) == 1) {
                insideBoatWalks += 2
            }
        }
    }

    private fun enterBoat() {
        if (PestControlHelper.outsideGangplankContainsLoc2(getLocation())){
            movetimer = Random().nextInt(10)
            combathandler.randomWalkTo(PestControlHelper.PestControlLanderIntermediate, 1)
        }
        if (!prayer.active.isEmpty()) {
            prayer.reset()
        }
        if (Random().nextInt(8) >= 3)
        {
            val pclocs = Location.create(2652,2646,0)
            combathandler.randomWalkTo(pclocs,8)
            movetimer = Random().nextInt(180) + 15
        }
        if (Random().nextInt(8) >= 2)
        {
            randomWalk(3,3)
            movetimer = Random().nextInt(10)
        }
        if (Random().nextInt(100) > 50)
        {
            if (Random().nextInt(10) <= 5) {
                this.walkToPosSmart(myBoat.outsideBoatBorder.randomLoc)
                movetimer += RandomFunction.normalPlusWeightRandDist(400, 200)
            }
            movetimer = RandomFunction.normalPlusWeightRandDist(100, 50);
            return;
        }
        val test = getClosestNodeWithEntry(15, myBoat.ladderId)
        test ?: randomWalk(1,1)
        test?.interaction?.handle(this, test.interaction[0])
        insideBoatWalks = 3
    }

    var switch = false
    private fun toPC() {
        time = 0
        if (!switch) {
            this.teleport(PestControlHelper.PestControlLanderIntermediate)//.also { println("I was stuck ${this.username}") }
            switch = true
            return
        }
        if (switch) {
            val test = getClosestNodeWithEntry(30, myBoat.ladderId)
            if (test == null) {
                switch = false
                this.teleport(PestControlHelper.PestControlLanderIntermediate)
                State.OUTSIDE_GANGPLANK
            } else {
                switch = false
                test.interaction.handle(this, test.interaction[0])
                State.OUTSIDE_GANGPLANK
            }
        }
    }


    companion object {
         fun legitimizeLocation(l: Location): Location {
            return if (PestControlHelper.landerContainsLoc(l)) Location(2648, 2648, 0) else l
        }
    }
}