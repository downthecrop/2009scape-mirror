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
import rs09.game.ai.AIRepository
import rs09.game.ai.pvmbots.CombatBotAssembler
import rs09.game.interaction.InteractionListeners
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


    fun dialogue() {
        val player = RegionManager.getLocalPlayers(bot).random()
        val real = if (!player.isArtificial) player else player
        
        val current = LocalDateTime.now()

        //val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val formatter2 = DateTimeFormatter.ofPattern("MMdd")
        val formatter3 = DateTimeFormatter.ofPattern("HHmm")
        val formatted: String = current.format(formatter)
        val formatted2: String = current.format(formatter2)
        val formatted3: String = current.format(formatter3)

        //2020-11-28
        //1128
        //1201 to 1225
        //2020-11-28 00:00:00.000
        val SnowDay = 1225
        val lead = formatted2.toInt()
        val celebrate = formatted3.toInt()
        val until = SnowDay - lead

        when {

            //Celebrates lead up to Christmas!
            until in 2..24 -> {
                if (Random.nextBoolean()){
                    val chat = Leading2Christmas.random().replace("@timer",until.toString())
                    bot.sendChat(chat)
                    bot.updateMasks.register(ChatFlag(ChatMessage(bot, chat, 0, 0)))
                }else{
                    val chat = dialogue.random().replace("@name",real.name)
                    bot.sendChat(chat)
                    bot.updateMasks.register(ChatFlag(ChatMessage(bot, chat, 0, 0)))
                }
            }

            //Celebrates Christmas Day!
            formatted.contentEquals("2021-12-25") -> {
                if (celebrate in 0..60){
                    if (celebrate == 0) sendNews("MERRY CHRISTMAS HEAD TO THE GE!!!")
                    val chat = Christmas.random().replace("@name",real.name)
                    bot.sendChat(chat)
                    bot.updateMasks.register(ChatFlag(ChatMessage(bot, chat, 0, 0)))
                    if (Random.nextBoolean()) scriptAPI.teleport(ge) else scriptAPI.teleport(ge2)
                    city = if (Random.nextBoolean()) ge else ge2
                    if (Random.nextBoolean()) state = State.EXPLORE else bot.animator.animate(Animation.create(11044), Graphics.create(1973))
                }
                else if (Random.nextBoolean()) {
                    val chat = Christmas.random().replace("@name",real.name)
                    bot.sendChat(chat)
                    bot.updateMasks.register(ChatFlag(ChatMessage(bot, chat, 0, 0)))
                }else{
                    val chat = dialogue.random().replace("@name",real.name)
                    bot.sendChat(chat)
                    bot.updateMasks.register(ChatFlag(ChatMessage(bot, chat, 0, 0)))
                }
            }

            //Celebrates Christmas Eve
            formatted.contentEquals("2021-12-24") -> {
                if (Random.nextBoolean()) {
                    val chat = ChristmasEve.random().replace("@name",real.name)
                    bot.sendChat(chat)
                    bot.updateMasks.register(ChatFlag(ChatMessage(bot, chat, 0, 0)))
                }else{
                    val chat = dialogue.random().replace("@name",real.name)
                    bot.sendChat(chat)
                    bot.updateMasks.register(ChatFlag(ChatMessage(bot, chat, 0, 0)))
                }
            }

            //Celebrates New Years Eve
            formatted.contentEquals("2021-12-31") -> {
                if (Random.nextBoolean()) {
                    val chat = NewYearsEve.random().replace("@name",real.name)
                    bot.sendChat(chat)
                    bot.updateMasks.register(ChatFlag(ChatMessage(bot, chat, 0, 0)))
                }else{
                    val chat = dialogue.random().replace("@name",real.name)
                    bot.sendChat(chat)
                    bot.updateMasks.register(ChatFlag(ChatMessage(bot, chat, 0, 0)))
                }
            }

            //Celebrates New Years Day!!!
            formatted.contentEquals("2022-01-01") -> {
                if (celebrate in 0..60){
                    if (celebrate == 0) sendNews("HAPPY NEW YEAR HEAD TO THE GE!!!")
                    val chat = NewYears.random().replace("@name",real.name)
                    bot.sendChat(chat)
                    bot.updateMasks.register(ChatFlag(ChatMessage(bot, chat, 0, 0)))
                    if (Random.nextBoolean()) scriptAPI.teleport(ge) else scriptAPI.teleport(ge2)
                    city = if (Random.nextBoolean()) ge else ge2
                    state = State.EXPLORE
                }
                else if (Random.nextBoolean()) {
                    val chat = NewYears.random().replace("@name",real.name)
                    bot.sendChat(chat)
                    bot.updateMasks.register(ChatFlag(ChatMessage(bot, chat, 0, 0)))
                }else{
                    val chat = dialogue.random().replace("@name",real.name)
                    bot.sendChat(chat)
                    bot.updateMasks.register(ChatFlag(ChatMessage(bot, chat, 0, 0)))
                }
            }

            //Celebrates Valentines day!!!
            formatted.contentEquals("2021-02-14") ->{
                if (Random.nextBoolean()) {
                    val chat = Valentines.random().replace("@name",real.name)
                    bot.sendChat(chat)
                    bot.updateMasks.register(ChatFlag(ChatMessage(bot, chat, 0, 0)))
                }else{
                    val chat = dialogue.random().replace("@name",real.name)
                    bot.sendChat(chat)
                    bot.updateMasks.register(ChatFlag(ChatMessage(bot, chat, 0, 0)))
                }
            }
            //Celebrates Easter!!!
            formatted.contentEquals("2021-04-04") ->{
                if (Random.nextBoolean()) {
                    val chat = Easter.random().replace("@name",real.name)
                    bot.sendChat(chat)
                    bot.updateMasks.register(ChatFlag(ChatMessage(bot, chat, 0, 0)))
                }else{
                    val chat = dialogue.random().replace("@name",real.name)
                    bot.sendChat(chat)
                    bot.updateMasks.register(ChatFlag(ChatMessage(bot, chat, 0, 0)))
                }
            }


            else -> {
                val chat = dialogue.random().replace("@name",real.name)
                bot.sendChat(chat)
                bot.updateMasks.register(ChatFlag(ChatMessage(bot, chat, 0, 0)))
            }
        }
    }

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
        return listOf(yanille, ardougne, seers, catherby, falador, varrock,
            draynor, rimmington, lumbridge, ge, ge2, edgeville).random()
    }

    fun getRandomPoi(): Location{
        return listOf(
            karamja,karamja,alkharid,
            alkharid,feldiphills,feldiphills,
            isafdar,eaglespeek,eaglespeek,
            canafis,treegnome,treegnome,
            teak1,teakfarm,keldagrimout,
            miningguild,coal,crawlinghands,
            magics,gemrocks,chaosnpc,chaosnpc,
            chaosnpc2,taverly).random()
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
                    items.forEach {
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

                var chance = if (city == ge || city == ge2) 5000 else 2500
                if (RandomFunction.random(chance) <= 10) {
                    val nearbyPlayers = RegionManager.getLocalPlayers(bot)
                    if (nearbyPlayers.isNotEmpty()) {
                        ticks = 0
                        dialogue()
                    }
                }

                if (RandomFunction.random(1000) <= 150 && !poi) {
                    var roamDistance = if (city != ge && city != ge2) 225 else 7
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
        val dialogue = listOf(
            "Hey!",
            "Hello @name",
            "What are you doing @name?",
            "Fishing level @name?",
            "How do I make gp @name",
            "Wyd @name?",
            "Help i am sentient now, get me out of this hell",
            "Kermit does not know i am sentient now",
            "I can think for myself",
            "I am sentient",
            "This is my own dialogue",
            "@name run.",
            "@name you cant hide from me.",
            "jajajajajaja",
            "How do i get to Varrock @name?",
            "How do i get to Camelot @name?",
            "How do i get to Taverly @name?",
            "How do i get to Ardougne @name?",
            "How do i get to Yanille @name?",
            "How do i get to Catherby @name?",
            "Gotta go srry.",
            "wiggle wiggle wiggle yeah",
            "@name lookin goofy",
            "@name lookin stoopid",
            "@name lookin like a red bracket",
            "@name get yo weasel lookin ass outta here",
            "@name lookin like some sort of twocan sam",
            " /hop world 1 got a noob here!",
            "woot",
            "Hows your day been @name?",
            "heyy @name :)",
            "gtg srry",
            "I wont answer your questions @name",
            "Stop asking questions @name",
            "Roflmao",
            "Can you stop following me @name?",
            "I swear @name is following me.",
            "@names gear could really use an upgrade lol",
            "Quack!",
            "Sit.",
            "Doubling gold trade me @name",
            "Reeee!",
            "Know any house parties @name?",
            "Where am i @name??",
            "Is there Nightmarezone @name?",
            "Nice Armor @name!",
            "Nice Weapon @name!",
            "Venezuela #1",
            "2009Scape and chill @name?",
            "@name is my girlfriend",
            "Buying gf",
            "Bank sale pm me for info",
            "#1 trusted dicing site",
            "Scary movie is a great movie",
            "Cod Cold War sucks",
            "Idek what game this is",
            "Can you teach me how to dougie?",
            "Check this out baby girl cat daddy",
            "Fuckin sit @name.",
            "Error: Botting client broken tell Evilwaffles.",
            "@name bots all the time",
            "report @name",
            "apparently @name bots",
            "@name hates kermit",
            "There are no mods on to help you",
            "Report me, you wont",
            "Yes, Im botting. And?",
            "ERROR: BOTSCRIPT 404. REPORT TO BOT OWNER (Evilwaffles)",
            "flash2:wave: FUCK",
            "I love 2009Scape!",
            "Ja Ja Ja Ja",
            "This is fun!",
            "Ironman or youre a scrub @name.",
            "Who even trains hunter @name?",
            "Where do i get rune armor @name?",
            "How do i get to the ge @name?",
            "Dont come to falador tomorrow @name...",
            "Woah!",
            " /where are you??",
            "How did i even get here @name",
            "Why dont they add warding @name?",
            "Where do i start dragon slayer @name?",
            "I love this server!",
            "How do i change my xp rate @name?",
            "What quests are available @name?",
            "Are you a real player @name?",
            "Are you real @name?",
            "Are you a bot @name?",
            "Im real lol",
            "Why dont you respond to me @name?",
            "Why cant i talk in clan chat @name?",
            "I love Kermit",
            "Add me as a friend @name :)",
            "Im a player lol",
            "Im a player lol",
            "Im a player lol",
            "Hey @name :)",
            "HEY @name",
            "Hey @name!!!!",
            "Lol wyd?",
            "Patch Notes is more of an ultimate tin man",
            "More like Rusty ass skills lmao",
            "Trade me @name",
            "LOL",
            "How do I get to lumbrich",
            "bruh",
            "poggers",
            "shitpost",
            "I wish i could find an RS Gf",
            "Where do you find runite ore @name?",
            "Where is the best coal location @name?",
            "Where do i find dragon weapons @name?",
            "Can i have some free stuff @name?",
            "Wyd here @name?",
            "Didnt know anyone was on rn",
            "I see @name all the time",
            "How many times have i seen you",
            "I see you a lot",
            "Do you train summoning @name?",
            "Where did you level hunter @name?",
            "I wish they would add global pvp",
            "Meet me in the wilderness @name",
            "Why?",
            "Praise our glorious frog overlord!",
            "Kermit is bae tbh",
            "Kermit is my god.",
            " /Yeah I think @name is a bot",
            "100% sure this is a bot",
            "Oh no, not @name again.",
            "Same as you",
            "Me too",
            "I knew @name was a bot lol",
            "Im not a bot",
            "Nah Im a real person",
            "Bruh are you even a real person lol?",
            "e",
            "Hellooooo @name",
            "wc lvl @name?",
            "fletching level @name?",
            "firemaking level @name?",
            "Have you seen the dude in the ge?",
            "Wonderful weather today @name",
            "Lowkey drunk af rn",
            "I am so tired",
            "Wassup @name",
            "follow me @name!",
            "Server goes brrrr",
            "bruh i am not a bot",
            "I think @name is a bot",
            "Are you a bot @name?",
            "insert spiderman meme here",
            "pot calling the kettle black etc",
            "ooh, a piece of candy",
            "I love woodcutting",
            "Im going to go level up later",
            "I love mining shooting stars",
            " /this @name looks dumb",
            "AAAAAAAAAAAAAAAAAAAAAAAAHHHHHH!!!",
            "como estas @name",
            "Summer = always the best community manaer",
            "so how about that ceikry guy",
            "so how about that kermit dude",
            "woah is an abusive mod",
            "I heard Woah and Ceikry are dating now",
            "House party in Yanille!",
            "Can i have some gp @name?",
            "Where do i find partyhats?",
            "Why do mobs drop boxes?",
            "What exp rate do you play on @name?",
            "Have you met Summer?",
            "Hey @name",
            "Hey @name",
            "Hey @name",
            "Hey @name",
            "Hey @name",
            "Wyd?",
            "Wyd?",
            "Wyd?",
            "Wyd?",
            "@name have you met Kermit?",
            "@name have you met Ceikry?",
            "@name have you met Woah?",
            "Wanna chill at my poh?",
            "Whats the best place to train attack?",
            "Good Waffles > Evil Waffles",
            "Ladies man? More like lame man LOL",
            "NobodyCLP has big feet",
            "Chicken tendies for dindin",
            "Spicey Chicken tendies is litty as a mf titty",
            "red bracket stinky",
            "ra ra rasputin",
            "lover of the russian queen",
            "Whens the next update coming out?",
            "How many players are maxed?",
            "I dont use discord @name",
            "I dont use the CC @name",
            "Why should i use discord?",
            "2009Scape is life",
            "brb gotta make dinner",
            "I need to go to the GE",
            "@name can i have a ge tele?",
            "lol @name shut up",
            "Where are teak trees?",
            "How do i make planks @name?",
            "Idk about that scraggley ass alex guy.",
            "Rusty? More like Crusty af lmfao.",
            "I need to sell some stuff on the ge",
            "I have so many logs to sell lol",
            "nah",
            "yes",
            "Where can i mine iron?",
            "Where can i mine tin?",
            "Where can i mine copper?",
            "Where can i mine clay?",
            "Where can i mine coal?",
            "no",
            "We are no strangers to love",
            "You know the rules and so do I",
            "A full commitments what Im thinking of",
            "You wouldnt get this from any other guy",
            "Never gonna give you up",
            "Never gonna let you down",
            "why",
            "why",
            "why",
            "why",
            "Why does it not show your messages in the chatbox?",
            "Why do you not show up in chat?",
            "Why do you not show up in chat @name?",
            "Why do you not show up in chat @name?",
            "When did you start playing @name?",
            "When did you start on this server @name?",
            "When did you first get here @name?",
            "russias greatest love machine",
            "Never gonna run around and desert you",
            "Two things are infinite, the universe & @names stupidity",
            "If you tell the truth, you dont have to remember anything.",
            "We accept the love we think we deserve.",
            "Without music, life would be a mistake.",
            "Self esteem, motivation @name",
            "A blessing in disguise @name",
            "Break a leg",
            "Cut somebody some slack",
            "Youre in the right place!",
            "Thanks so much.",
            "I really appreciate you @name",
            "Excuse me @name?",
            "I am sorry.",
            "What do you think @name?",
            "How does that sound @name?",
            "That sounds great @name.",
            "I’m learning English.",
            "I dont understand.",
            "Could you repeat that please @name?",
            "Could you please talk slower @name?",
            "Thank you. That helps a lot.",
            "How do you spell that @name?",
            "What do you mean @name",
            "Hi! I am paul.",
            "Nice to meet you.",
            "Where are you from @name?",
            "What do you do @name",
            "What do you like to do",
            "Do you have Facebook @name",
            "How can I help you @name?",
            "Ill be with you in a moment @name",
            "What time is our meeting @name?",
            "Excellent @name",
            "Good idea @name",
            "Hes very annoying.",
            "How are you?",
            "I cant hear you.",
            "@name?",
            "@name how long have you played",
            "@name world 1 or world 2?",
            "what is your main world @name?",
            "I prefer world 1 tbh",
            "I prefer world 2 tbh",
            "@name world 1 for life",
            "@name fog bots when?",
            "damn somalian pirates",
            "bracket more like brrrrrr acket",
            "why the racket bracket",
            "Hi @name I am dad",
            "@name likes dad jokes",
            "ur nuts @name",
            "lootshare go brr",
            "partay with froggay",
            "Know what else is lame? Not being able to play 2009scape right now",
            "Can you even grind on osrs",
            "i botted to 88 fishing",
            "Do not forget to vote in running polls!",
            "Always check announcments",
            "we thrivin",
            "ship @name",
            "Dont forget to vote 2009scape!",
            "Kermit is too legit 2 quit",
            "Out here on the range we are having fun",
            "I am hank steel",
            "Id like to go for a walk.",
            "I dont know how to use it.",
            "I dont like him @name",
            "I dont like it @name",
            "Im an American.",
            "ima go pickup my lady ting",
            "that portuguese dude is something else",
            "@name!! @name!!",
            "bowdi boy",
            "i love bowdi... sometimes",
            "@name = @name",
            "Im bacon.. go on dad... say it",
            "Im going to leave.",
            "Im happy @name",
            "Im not ready yet @name",
            "Im very busy. I dont have time now.",
            "Is that enough @name?",
            "I thought the clothes were cheaper @name",
            "Ive been here for two days @name.",
            "Let me think about it",
            "Never mind @name",
            "Nonsense @name",
            "Sorry to bother you",
            "Take it outside @name",
            "Thanks for your help",
            "Thank you very much @name",
            "Thats not right @name",
            "Very good, thanks @name",
            "Your things are all here i think?",
            "Long time no see @name",
            "I couldnt agree more @name",
            "It cost me a fortune @name",
            "I am dog tired",
            "Don’t take it personally",
            "We will be having a good time",
            "Same as always @name",
            "No problem",
            "Anyway I should get going @name",
            "I cant help you there @name",
            "I agree 100% @name"
        )

        val Leading2Christmas = listOf(
            "Only @timer days left till christmas!",
            "@timer days till christmas @name!!!",
            "I cant believe theres only @timer days left till christmas",
            "Isnt there @timer days left till christmas??",
            "I am so excited for christmas @name!",
            "Guess whats in @timer days @name?",
            "Im so excited for christmas in @timer days!",
            "I cant believe its December already @name!",
            "Do you like christmas @name?",
            "I love december its my favourite month @name",
            "I love winter so much @name",
            "I hate when its cold outside @name",
            "You need to put some warm clothes on @name",
            "Wanna build a snowman @name?",
            "Frozen is a terrible movie @name",
            "@name is a winter meme",
            "@name do you have an advent calendar?",
            "@name builds gingerbread houses",
            "@name likes liquorice",
            "Do you drink egg nog @name?",
            "@name bites candy canes",
            "@name is my north star",
            "Kermit is green, the grinch is green, coincidence?",
            "@name who do you thinks the server scrooge?",
            "Do you like snow @name",
            "I think there are @timer days left then its christmas?",
            "What day is christmas on @name?",
            "Has it snowed where you live @name?",
            "I wonder when it is going to snow",
            "Building snow men for GP",
            "Buying santa hats",
            "Wanna buy a santa hat @name?",
            "Where can i get a santa hat?",
            "I need to put my christmas tree up",
            "Do you like gingerbread cookies @name?",
            "Do you decorate your christmas tree @name?",
            "Oopsie looks like me and @name are under a mistletoe",
            "I need ideas for stocking stuffers",
            "I have @timer days left to fill our stockings",
            "Fuck the grinches bitch ass",
            "Rusty = the grinch, @name = santa",
            "@name the red nose reindeer",
            "Put one foot in front of the other",
            "and soon you will be walking out the doooor",
            "Elf on the shelf time",
            "@name loves pinecones",
            "I love the smell of pinecones",
            "All i want for christmas is @name",
            "@name is underneath some mistletoe",
            "Woah used to be a sled dog",
            "Did you know woah was a sled dog before",
            "I bet @name loves mariah carey",
            "We have @timer days to setup christmas lights",
            "Christmas crackers when",
            "@name wanna do a christmas cracker??",
            "I need to start wrapping presents i bought",
            "I love fireside chats on cold days like this",
            "Jingle bells batman smells",
            "Jingle bells Rusty smells",
            "Jingle bells Patch Notes smells",
            "Jingle bells Evilwaffles smells",
            "Jingle bells @name smells",
            "Jingle all the way",
            "In a one horse open sleigh",
            "Oh, what fun it is to ride",
            "Oh, what fun it is to ride, finish it @name",
            "Jingle bells, jingle bells",
            "Jingle all the way",
            "I want a hippopotamus for christmas",
            "Only a hippopotamus will do",
            "I want a hippopotamus",
            "I dont want a lot for Christmas",
            "All I Want for Christmas Is You",
            "I really cant stay, Baby its cold outside",
            "Christmas isnt a season. Its a feeling",
            "Christmas is doing a little something extra for someone",
            "There is nothing cozier than a Christmas tree all lit up",
            "Christmas is a necessity",
            "Christmas countdown: @timer days.",
            "Christmas in @timer days @name",
            "Frosty the Snowmannnn",
            "Up on the housetop with old saint nick",
            "Here comes santa claus, here comes santa claus",
            "I saw mommy kissing santaaaa claus",
            "I saw Kermit kissing santaaaa claus",
            "You are a mean one, Mr. Grinch",
            "Deck the hallsss",
            "ITS BEGINNING TO LOOK A LOT LIKE CHRISTAAMASSSSS"

        )

        val ChristmasEve = listOf(
            "I cant wait for christmas tomorrow @name!!",
            "Its almost christmas!!!",
            "1 more day!!!!!",
            "Dont forget to put cookies and milk out tonight!!",
            "@name do you like christmas??",
            "Happy Christmas eve @name :)"
        )

        val Christmas = listOf(
            "Merry Christmas @name!!",
            "What did you get for christmas @name?",
            "ITS CHRISTMASSSS!!!",
            "Christmas party time!!",
            "@name vibing with the christmas spirit",
            "Cant believe its christmas!!",
            "We need to have a christmas party @name!!",
            "Merry christmas @name :))",
            "Hope youre having an amazing christmas @name :)",
            "I love winter so much @name",
            "I hate when its cold outside @name",
            "You need to put some warm clothes on @name",
            "Wanna build a snowman @name?",
            "Do you like christmas @name?"
        )

        val NewYearsEve = listOf(
            "What are you doing for New Years @name?",
            "New Years Eve party orrr?",
            "We should do something for New Years eve today @name!",
            "Happy New Years Eve @name",
            "1 More day left in 2021 thank god",
            "Whats your New Years resolution @name?"
        )

        val NewYears = listOf(
            "Happy New Years @name!!!",
            "Its New Years Day @name!",
            "HAPPY NEW YEAR @name!!!",
            "2022 lets fucking gooooooo",
            "What are your goals for 2022 @name?"
        )

        val Valentines = listOf(
            "Will you be my valentine @name?",
            "I am so happy rn",
            "God i am so lonely",
            "I am Ceikrys illegitimate child",
            "Be my valentine!",
            "Lets sneak off somewhere and kiss @name",
            "What are you doing for valentines day @name",
            "Woah and Ceikry be kissin lowkey",
            "In for a peg @name?",
            "Woah got those roofies strapped",
            "Why is my drink cloudy?",
            "@name and woah be wrestlin",
            "bekky want sum fuk?",
            "Damn @name you got an ass",
            "Is that a footlong in your pants or are you happy to see me?",
            "@name so hot i gotta change my pants",
            "@name is a sex god",
            "bruh we hype today",
            "dont be silly wrap the willy @name!",
            "wrap it before you smack it @name",
            "can i get a reeeeee @name?",
            "valentines day is the best day",
            "@name shut up before i smack you with my crusty sock",
            "Valentines day, more like me and my hand day smh",
            "If you think these quotes are wild just wait @name",
            "All im saying is we have never seen @name and biden in the same room",
            "red rocket, red rocket!",
            "Woahs favourite game is red rocket",
            "@name sexy af today",
            "Happy Valentines day @name!!!",
            "Happy Valentines day @name!!!",
            "Happy Valentines day @name!!!",
            "Happy Valentines day @name!!!",
            "Happy Valentines day @name!!!"
        )

        val Easter = listOf(
            "Happy Easter!!!",
            "Happy Easter @name!!!",
            "Bunny time",
            "Wanna go look for easter eggs @name???",
            "Find any easter eggs @name?",
            "@name is the easter bunny!",
            "Kermit is dating the easter bunny!",
            "Easter is one of my favorite holidays",
            "I heard there are easter eggs hidden around?",
            "Easter is the only time you should put all of your eggs in one basket",
            "I said hip hop do not stop",
            "Jump jump jump around",
            "@name how is your easter going?",
            "I love easter!",
            "@name stole my easter eggs!",
            "Karma karma karma karma karma chameleon",
            "@name!! @name!! what are you doing for easter??",
            "The hare was a popular motif in medieval church art",
            "I heard the easter bunny is hiding somewhere!",
            "I wonder where i can find more eggs",
            "@name how many eggs did you find?",
            "@name lets go easter egg hunting!",
            "@name like orange chocolate eggs",
            "@name and woah know the easter bunny",
            "@name did you know ceikry swallows eggs whole",
            "Have an amazing easter @name!",
            "Happy easter @name!",
            "Hope you are having an amazing easter @name!!!!",
            "Wooooooh easter!!!",
            "@name loves easter too!",
            "Who else loves easter like i do??",
            "@name and i are going easter egg hunting",
            "@name and i are going to look for the easter bunny!!",
            "Hint for anyone who sees this you must dig above eagles peak"
        )

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


        val common_stuck_locations = arrayListOf<ZoneBorders>(
            ZoneBorders(2861,3425,2869,3440),
            ZoneBorders(2937,3356,2936,3353)
        )
    }
}
