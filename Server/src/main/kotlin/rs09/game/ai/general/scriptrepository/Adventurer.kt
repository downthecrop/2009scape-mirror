package rs09.game.ai.general.scriptrepository

import core.game.interaction.DestinationFlag
import core.game.interaction.MovementPulse
import core.game.node.scenery.Scenery
import core.game.node.entity.combat.CombatStyle
import rs09.game.node.entity.combat.CombatSwingHandler
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.system.task.Pulse
import rs09.game.world.GameWorld
import core.game.world.map.Location
import core.game.world.map.RegionManager
import core.game.world.map.zone.ZoneBorders
import rs09.game.world.repository.Repository.sendNews
import core.game.world.update.flag.*
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.ChatMessage
import core.game.world.update.flag.context.Graphics
import core.game.world.update.flag.player.ChatFlag
import core.tools.RandomFunction
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import rs09.ServerConstants
import rs09.game.ai.AIRepository
import rs09.game.ai.pvmbots.CombatBotAssembler
import rs09.game.interaction.InteractionListeners
import java.io.File
import java.io.FileReader
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.random.Random


/**
 * A bot script for Adventurers who explore the world!
 * @param counter used in the bots random idling function.
 * @param random is used to generate random number.
 * @param city determines the home city of the bot.
 * @param freshspawn determines if the bot has just been spawned.
 * @param random_city is the list of cities that can be randomly chosen as the home city.
 * @param tree_list is the list of trees that a bot can start cutting randomly.
 * @author Sir Kermit
 * @author Ceikry
 */

//Adventure Bots v4.0.0 -Expansion Edition-
class Adventurer(val style: CombatStyle): Script() {

    var city: Location = lumbridge
    var ticks = 0
    var freshspawn = true
    var sold = false
    private val geloc: Location = if (Random.nextBoolean()){
        Location.create(3165, 3487, 0)
    }else{
        Location.create(3164, 3492, 0)
    }

    var counter: Int = 0
    var random: Int = (5..30).random()

    val type = when(style){
        CombatStyle.MELEE -> CombatBotAssembler.Type.MELEE
        CombatStyle.MAGIC -> CombatBotAssembler.Type.MAGE
        CombatStyle.RANGE -> CombatBotAssembler.Type.RANGE
    }

    init {
        skills[Skills.AGILITY] = 99
        inventory.add(Item(1359))//Rune Axe
        skills[Skills.WOODCUTTING] = 95
        inventory.add(Item(590))//Tinderbox
        skills[Skills.FISHING] = 90
        inventory.add(Item(1271))//Addy Pickaxe
        skills[Skills.MINING] = 90
        skills[Skills.SLAYER] = 90
    }

    private var state = State.START

    fun getRandomCity(): Location{
        return cities.random()
    }

    fun getRandomPoi(): Location{
        return pois.random()
    }

    //TODO: Optimise and adjust how bots handle picking up ground items further.
    fun immerse() {
        if (counter++ == 180) {state = State.TELEPORTING}
        val items = AIRepository.groundItems[bot]
        if (Random.nextBoolean()) {
            if (items.isNullOrEmpty()) {
                scriptAPI.attackNpcsInRadius(bot, 8)
                state = State.LOOT_DELAY
            }
            if (bot.inventory.isFull) {
                if(bankMap[city] == null){
                    scriptAPI.teleport(getRandomCity().also { city = it })
                } else {
                    if(bankMap[city]?.insideBorder(bot) == true){
                        state = State.FIND_BANK
                    } else {
                        scriptAPI.walkTo(bankMap[city]?.randomLoc ?: Location(0, 0, 0))
                    }
                }
            }

        } else {
            if (bot.inventory.isFull){
                if(bankMap[city] == null){
                    scriptAPI.teleport(getRandomCity().also { city = it })
                } else {
                    if(bankMap[city]?.insideBorder(bot) == true){
                        state = State.FIND_BANK
                    } else {
                        scriptAPI.walkTo(bankMap[city]?.randomLoc ?: Location(0, 0, 0))
                    }
                }
            } else {
                val resources = listOf(
                    "Rocks","Tree","Oak","Willow",
                    "Maple tree","Yew","Magic tree",
                    "Teak","Mahogany")
                val resource = scriptAPI.getNearestNodeFromList(resources,true)
                if(resource != null){
                    if(resource.name.contains("ocks")) InteractionListeners.run(resource.id,1,"mine",bot,resource)
                    else InteractionListeners.run(resource.id,1,"chop down",bot,resource)
                }
            }
        }
        return
    }

    fun refresh() {
        scriptAPI.teleport(lumbridge)
        freshspawn = true
        state = State.START
    }

    var poi = false
    var poiloc = karamja

    //Adventure Bots Actual Code STARTS HERE!!!
    override fun tick() {
        ticks++
        if (ticks++ >= 800) {
            ticks = 0
            refresh()
            return
        }

        // zoneborder checker
        if(ticks % 30 == 0){
            for(border in common_stuck_locations){
                if(border.insideBorder(bot)){
                    refresh()
                    ticks = 0
                }
            }
        }

        when(state){

            State.LOOT_DELAY -> {
                bot.pulseManager.run(object : Pulse() {
                    var counter1 = 0
                    override fun pulse(): Boolean {
                        when (counter1++) {
                            7 -> return true.also { state = State.LOOT }
                        }
                        return false
                    }
                })
            }

            State.LOOT -> {
                val items = AIRepository.groundItems[bot]
                if (items?.isNotEmpty() == true && !bot.inventory.isFull) {
                    items.toTypedArray().forEach {
                        scriptAPI.takeNearestGroundItem(it.id)
                    }
                    return
                } else {
                    state = State.EXPLORE
                }
            }

            State.START -> {
                if (freshspawn) {
                    freshspawn = false
                    scriptAPI.randomWalkTo(lumbridge, 20)
                } else {
                    city = getRandomCity()
                    state = State.TELEPORTING
                }
            }

            State.TELEPORTING -> {
                ticks = 0
                counter = 0
                if (bot.location != city) {
                    poi = false
                    scriptAPI.teleport(city)
                } else {
                    poi = false
                    state = State.EXPLORE
                }
            }

            State.EXPLORE -> {
                if (counter++ == 350) {
                    state = State.TELEPORTING
                }

                val chance = if (city == ge || city == ge2) 5000 else 2500
                if (RandomFunction.random(chance) <= 10) {
                    val nearbyPlayers = RegionManager.getLocalPlayers(bot)
                    if (nearbyPlayers.isNotEmpty()) {
                        ticks = 0
                        dialogue()
                    }
                }

                if (RandomFunction.random(1000) <= 150 && !poi) {
                    val roamDistance = if (city != ge && city != ge2) 225 else 7
                    if ((city == ge || city == ge2) && RandomFunction.random(100) < 90) {
                        if (!bot.bank.isEmpty) {
                            state = State.FIND_GE
                        }
                        return
                    }
                    scriptAPI.randomWalkTo(city, roamDistance)
                    return
                }

                if (RandomFunction.random(1000) <= 50 && poi){
                    val roamDistancePoi = when(poiloc){
                        teakfarm,crawlinghands -> 5
                        treegnome -> 50
                        isafdar -> 40
                        eaglespeek -> 40
                        keldagrimout -> 30
                        teak1 -> 30
                        miningguild -> 5
                        magics,coal -> 7
                        gemrocks,chaosnpc,chaosnpc2 -> 1
                        else -> 60
                    }
                    scriptAPI.randomWalkTo(poiloc,roamDistancePoi)
                    return
                }

                if (RandomFunction.random(1000) <= 75) {
                    if (city != ge && city != ge2) {
                        immerse()
                        return
                    } else {
                        return
                    }
                }

                if (RandomFunction.random(20000) <= 60 && !poi) {
                    poiloc = getRandomPoi()
                    city = teak1
                    poi = true
                    scriptAPI.teleport(poiloc)
                    return
                }

                if ((city == ge || city == ge2) && RandomFunction.random(1000) >= 999) {
                    ticks = 0
                    city = getRandomCity()
                    state = State.TELEPORTING
                }

                if (city == ge || city == ge2) {
                    return
                }

                if (city == teak1 && counter++ >= 240){
                    city = getRandomCity()
                    state = State.TELEPORTING
                }

                if (counter++ >= 240 && RandomFunction.random(100) >= 10) {
                    city = getRandomCity()
                    if (RandomFunction.random(100) % 2 == 0) {
                        counter = 0
                        ticks = 0
                        state = State.TELEPORTING
                    } else {
                        if (citygroupA.contains(city)) {
                            city = citygroupA.random()
                        } else {
                            city = citygroupB.random()
                        }
                        counter = 0
                        ticks = 0
                        state = State.FIND_CITY
                    }
                    counter = 0
                    return
                }
                return
            }

            State.GE -> {
                var ge = false
                if (counter++ == 180) {
                    state = State.TELEPORTING
                }
                if (!sold) {
                    if (counter++ >= 15) {
                        sold = true
                        ge = true
                        counter = 0
                        ticks = 0
                        scriptAPI.sellAllOnGeAdv()
                        return
                    }
                } else if (ge && sold) {
                    ge = false
                    city = getRandomCity()
                    state = State.TELEPORTING
                }
                return
            }

            State.FIND_GE -> {
                if (counter++ == 180) {
                    state = State.TELEPORTING
                }
                sold = false
                val ge: Scenery? = scriptAPI.getNearestNode("Desk", true) as Scenery?
                if (ge == null || bot.bank.isEmpty) state = State.EXPLORE
                class GEPulse : MovementPulse(bot, ge, DestinationFlag.OBJECT) {
                    override fun pulse(): Boolean {
                        bot.faceLocation(ge?.location)
                        state = State.GE
                        return true
                    }
                }
                if (ge != null && !bot.bank.isEmpty) {
                    counter = 0
                    scriptAPI.randomWalkTo(geloc, 3)
                    GameWorld.Pulser.submit(GEPulse())
                }
                return
            }

            State.FIND_BANK -> {
                if (counter++ == 300) {
                    state = State.TELEPORTING
                }
                val bank: Scenery? = scriptAPI.getNearestNode("Bank booth", true) as Scenery?
                if (badedge.insideBorder(bot) || bot.location == badedge2 || bot.location == badedge3 || bot.location == badedge4) {
                    bot.randomWalk(5, 5)
                }
                if (bank == null) state = State.EXPLORE
                class BankingPulse : MovementPulse(bot, bank, DestinationFlag.OBJECT) {
                    override fun pulse(): Boolean {
                        bot.faceLocation(bank?.location)
                        state = State.IDLE_BANKS
                        return true
                    }
                }
                if (bank != null) {
                    bot.pulseManager.run(BankingPulse())
                }
                return
            }

            State.IDLE_BANKS -> {
                if (counter++ == 300) {
                    state = State.TELEPORTING
                }
                if (RandomFunction.random(1000) < 100) {
                    for (item in bot.inventory.toArray()) {
                        item ?: continue
                        when (item.id) {
                            1359, 590, 1271, 995 -> continue
                        }
                        bot.bank.add(item)
                        bot.inventory.remove(item)
                    }
                    counter = 0
                    state = State.EXPLORE
                }
                return
            }

            State.FIND_CITY -> {
                if (counter++ >= 600 || (city == ge || city == ge2)) {
                    counter = 0
                    scriptAPI.teleport(getRandomCity().also { city = it })
                    state = State.EXPLORE
                }
                if (bot.location.equals(city)) {
                    state = State.EXPLORE
                } else {
                    scriptAPI.randomWalkTo(city, 5)
                }
                return
            }

            State.IDLE_CITY -> {
                if (counter++ == 300) {
                    state = State.TELEPORTING
                }
                var random = (120..300).random()
                if (counter++ == random && RandomFunction.random(1000) % 33 == 0) {
                    counter = 0
                    state = State.EXPLORE
                }
                return
            }
        }

    }

    fun dialogue() {
        val localPlayer = RegionManager.getLocalPlayers(bot).random()
        val until = 1225 - dateCode
        val lineStd = dialogue.getLines("standard").rand()
        var lineAlt = ""

        when {
            //Celebrates Halloween!
            dateCode == 1031  -> lineAlt = dialogue.getLines("halloween").rand()

            //Celebrates lead up to Christmas!
            until in 2..23 -> lineAlt = dialogue.getLines("approaching_christmas").rand()

            //Celebrates Christmas Day!
            dateCode == 1225 -> lineAlt = dialogue.getLines("christmas_day").rand()

            //Celebrates Christmas Eve
            dateCode == 1224 -> lineAlt = dialogue.getLines("christmas_eve").rand()

            //New years eve
            dateCode == 1231 -> lineAlt = dialogue.getLines("new_years_eve").rand()

            //New years
            dateCode == 101 -> lineAlt = dialogue.getLines("new_years").rand()

            //Valentines
            dateCode == 214 -> lineAlt = dialogue.getLines("valentines").rand()

            //Easter
            dateCode == 404 -> lineAlt = dialogue.getLines("easter").rand()
        }

        val chat = if (lineAlt.isNotEmpty() && Random.nextBoolean()) { lineAlt } else { lineStd }
            .replace("@name", localPlayer.username)
            .replace("@timer", until.toString())

        bot.sendChat(chat)
        bot.updateMasks.register(ChatFlag(ChatMessage(bot, chat, 0, 0)))
    }

    enum class State{
        START,
        EXPLORE,
        FIND_BANK,
        IDLE_BANKS,
        FIND_CITY,
        IDLE_CITY,
        GE,
        TELEPORTING,
        LOOT,
        LOOT_DELAY,
        FIND_GE
    }


    override fun newInstance(): Script {
        val script = Adventurer(style)
        script.state = State.START
        val tier = CombatBotAssembler.Tier.MED
        if (type == CombatBotAssembler.Type.RANGE)
            script.bot = CombatBotAssembler().RangeAdventurer(tier, bot.startLocation)
        else
            script.bot = CombatBotAssembler().MeleeAdventurer(tier, bot.startLocation)
        return script
    }

    companion object {
        val badedge = ZoneBorders(3094, 3494, 3096, 3497)
        val badedge2 = Location.create(3094, 3492, 0)
        val badedge3 = Location.create(3094, 3490, 0)
        val badedge4 = Location.create(3094, 3494, 0)

        val yanille: Location = Location.create(2615, 3104, 0)
        val ardougne: Location = Location.create(2662, 3304, 0)
        val seers: Location = Location.create(2726, 3485, 0)
        val edgeville: Location = Location.create(3088, 3486, 0)
        val ge: Location = Location.create(3168, 3487, 0)
        val ge2: Location = Location.create(3161, 3493, 0)
        val catherby: Location = Location.create(2809, 3435, 0)
        val falador: Location = Location.create(2965, 3380, 0)
        val varrock: Location = Location.create(3213, 3428, 0)
        val draynor: Location = Location.create(3080, 3250, 0)
        val rimmington: Location = Location.create(2977, 3239, 0)
        val lumbridge: Location = Location.create(3222, 3219, 0)
        val karamja = Location.create(2849, 3033, 0)
        val alkharid = Location.create(3297, 3219, 0)
        val feldiphills = Location.create(2535, 2919, 0)
        val isafdar = Location.create(2241, 3217, 0)
        val eaglespeek = Location.create(2333, 3579, 0)
        val canafis = Location.create(3492, 3485, 0)
        val treegnome = Location.create(2437, 3441, 0)
        val teak1 = Location.create(2334, 3048, 0)
        val teakfarm = Location.create(2825, 3085, 0)
        val keldagrimout = Location.create(2724,3692,0)
        val miningguild = Location.create(3046,9740,0)
        val magics = Location.create(2285,3146,0)
        val coal = Location.create(2581,3481,0)
        val crawlinghands = Location.create(3422,3548,0)
        val gemrocks = Location.create(2825,2997,0)
        val chaosnpc = Location.create(2612,9484,0)
        val chaosnpc2 = Location.create(2580,9501,0)
        val taverly = Location.create(2909, 3436, 0)
        var citygroupA = listOf(falador, varrock, draynor, rimmington, lumbridge, edgeville)
        var citygroupB = listOf(yanille, ardougne, seers, catherby)

        var bankMap = mapOf<Location, ZoneBorders>(
            falador to ZoneBorders(2950, 3374, 2943, 3368),
            varrock to ZoneBorders(3182, 3435, 3189, 3446),
            draynor to ZoneBorders(3092, 3240, 3095, 3246),
            edgeville to ZoneBorders(3093, 3498, 3092, 3489),
            yanille to ZoneBorders(2610, 3089, 2613, 3095),
            ardougne to ZoneBorders(2649, 3281, 2655, 3286),
            seers to ZoneBorders(2729, 3493, 2722, 3490),
            catherby to ZoneBorders(2807, 3438, 2811, 3441)
        )

        val cities = listOf(yanille, ardougne, seers, catherby, falador, varrock,
            draynor, rimmington, lumbridge, ge, ge2, edgeville)

        val pois = listOf(
            karamja,karamja,alkharid,
            alkharid,feldiphills,feldiphills,
            isafdar,eaglespeek,eaglespeek,
            canafis,treegnome,treegnome,
            teak1,teakfarm,keldagrimout,
            miningguild,coal,crawlinghands,
            magics,gemrocks,chaosnpc,chaosnpc,
            chaosnpc2,taverly)

        val common_stuck_locations = arrayListOf(
            ZoneBorders(2861,3425,2869,3440),
            ZoneBorders(2937,3356,2936,3353)
        )

        val dialogue: JSONObject
        val dateCode: Int

        init {
            val reader = FileReader(ServerConstants.BOT_DATA_PATH + File.separator + "bot_dialogue.json")
            val parser = org.json.simple.parser.JSONParser()
            val data = parser.parse(reader) as JSONObject

            dialogue = data

            val formatter = DateTimeFormatter.ofPattern("MMdd")
            val current = LocalDateTime.now()
            val formatted: String = current.format(formatter)
            dateCode = formatted.toInt()
        }

        private fun JSONObject.getLines(category: String): JSONArray {
            return this[category] as JSONArray
        }

        private fun JSONArray.rand(): String {
            return this.random() as String
        }
    }
}
