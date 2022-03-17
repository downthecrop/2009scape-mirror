package rs09.game.content.quest.members.thefremenniktrials

import api.*
import core.game.container.impl.EquipmentContainer.SLOT_WEAPON
import core.game.content.dialogue.FacialExpression
import core.game.content.global.action.ClimbActionHandler
import core.game.content.global.action.DoorActionHandler
import core.game.node.entity.impl.Animator
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.skill.Skills
import core.game.node.entity.skill.gather.SkillingTool
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.map.RegionManager
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.net.packet.PacketRepository
import core.net.packet.context.MusicContext
import core.net.packet.out.MusicPacket
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import org.rs09.primextends.getNext
import org.rs09.primextends.isLast
import rs09.game.interaction.InteractionListener
import rs09.game.system.config.ItemConfigParser
import rs09.game.world.GameWorld
import rs09.game.world.GameWorld.Pulser

class TFTInteractionListeners : InteractionListener(){

    val BEER = Items.BEER_1917
    val WORKER = NPCs.COUNCIL_WORKMAN_1287
    val FISH_ALTAR = 4141
    val LYRE = Items.LYRE_3689
    val FISH = intArrayOf(Items.RAW_SHARK_383,Items.RAW_SEA_TURTLE_395,Items.RAW_MANTA_RAY_389)
    val LOW_ALC_KEG = Items.LOW_ALCOHOL_KEG_3712
    val KEG = Items.KEG_OF_BEER_3711
    val TINDERBOX = Items.TINDERBOX_590
    val CHERRY_BOMB = Items.STRANGE_OBJECT_3713
    val LIT_BOMB = Items.LIT_STRANGE_OBJECT_3714
    val PIPE = 4162
    val PORTALIDs = intArrayOf(2273,2274,2506,2507,2505,2503,2504,5138)
    val SWENSEN_LADDER = 4158
    val SWAYING_TREE = 4142
    val KNIFE = Items.KNIFE_946
    val TREE_BRANCH = Items.BRANCH_3692
    val LYRE_IDs = intArrayOf(14591,14590,6127,6126,6125,3691,3690)
    val THORVALD_LADDER = 34286
    val THORVALD_LADDER_LOWER = 4188
    val LALLIS_STEW = 4149
    val UNSTRUNG_LYRE = Items.UNSTRUNG_LYRE_3688
    val GOLDEN_FLEECE = Items.GOLDEN_FLEECE_3693
    val GOLDEN_WOOL = Items.GOLDEN_WOOL_3694
    val LONGHALL_BACKDOOR = 4148
    val SHOPNPCS = intArrayOf(NPCs.YRSA_1301,NPCs.SKULGRIMEN_1303,NPCs.THORA_THE_BARKEEP_1300,NPCs.SIGMUND_THE_MERCHANT_1282,NPCs.FISH_MONGER_1315)
    val SPINNING_WHEEL_IDS = intArrayOf(2644,4309,8748,20365,21304,25824,26143,34497,36970,37476)
    val STEW_INGREDIENT_IDS = intArrayOf(Items.POTATO_1942,Items.ONION_1957,Items.CABBAGE_1965,Items.PET_ROCK_3695)
    var FREMENNIK_HELMS = intArrayOf(Items.ARCHER_HELM_3749, Items.BERSERKER_HELM_3751, Items.WARRIOR_HELM_3753, Items.FARSEER_HELM_3755/*, Items.HELM_OF_NEITIZNOT_10828 Should this be included?*/)


    override fun defineListeners() {
        onUseWith(NPC,BEER,WORKER){ player, _, _ ->
            player.dialogueInterpreter.open(CouncilWorkerFTDialogue(0,true), NPC(WORKER))
            return@onUseWith true
        }

        onUseWith(SCENERY,FISH_ALTAR,*FISH){ player, _, fish ->
            if(player.inventory.contains(LYRE,1)) {
                Pulser.submit(spiritPulse(player, fish.id))
            } else {
                player.sendMessage("I should probably have my lyre with me.")
            }
            return@onUseWith true
        }

        onUseWith(ITEM,LOW_ALC_KEG,KEG){player,lowKeg,_ ->
            if(!player.getAttribute("fremtrials:keg-mixed",false)!!){
                if(player.getAttribute("fremtrials:cherrybomb",false)) {
                    player.inventory?.remove(lowKeg.asItem())
                    player.setAttribute("/save:fremtrials:keg-mixed", true)
                    player.sendMessage("The cherry bomb in the pipe goes off.")
                    RegionManager.getLocalEntitys(player).stream().forEach { e -> e.sendChat("What was THAT??") }
                    player.sendMessage("You mix the kegs together.")
                    return@onUseWith true
                } else {
                    player.dialogueInterpreter?.sendDialogue("I can't do this right now. I should create","a distraction.")
                    return@onUseWith true
                }
            } else return@onUseWith false
        }

        onUseWith(ITEM,TINDERBOX,CHERRY_BOMB){player,_,bomb ->
            player.inventory.remove(bomb.asItem())
            player.inventory.add(Item(LIT_BOMB))
            player.sendMessage("You light the strange object.")
            return@onUseWith true
        }

        onUseWith(ITEM,KNIFE,TREE_BRANCH){player,knife,_ ->
            if (player.inventory.containsItem(knife.asItem())) {
                GameWorld.submit(BranchFletchingPulse(player))
            } else {
                player.sendMessage("You need a knife to do this.")
            }
            return@onUseWith true
        }

        onUseWith(SCENERY,LALLIS_STEW,*STEW_INGREDIENT_IDS){player,_,stewIngredient ->
            when(stewIngredient.id){
                Items.ONION_1957 -> {
                    sendDialogue(player,"You added an onion to the stew")
                    player.setAttribute("/save:lalliStewOnionAdded",true)
                    removeItem(player,Items.ONION_1957)
                }
                Items.POTATO_1942 -> {
                    sendDialogue(player,"You added a potato to the stew")
                    player.setAttribute("/save:lalliStewPotatoAdded",true)
                    removeItem(player,Items.POTATO_1942)
                }
                Items.CABBAGE_1965 -> {
                    sendDialogue(player,"You added a cabbage to the stew")
                    player.setAttribute("/save:lalliStewCabbageAdded",true)
                    removeItem(player,Items.CABBAGE_1965)
                }
                Items.PET_ROCK_3695 -> {
                    sendDialogue(player,"You added your dear pet rock to the stew")
                    player.setAttribute("/save:lalliStewRockAdded",true)
                    removeItem(player,Items.PET_ROCK_3695)
                }
            }
            return@onUseWith true
        }

        onUseWith(SCENERY,SPINNING_WHEEL_IDS,GOLDEN_FLEECE){player,_,item ->
            animate(player,896)
            sendDialogue(player,"You spin the Golden Fleece into a ball of Golden Wool")
            removeItem(player,GOLDEN_FLEECE)
            addItem(player,Items.GOLDEN_WOOL_3694)
            return@onUseWith true
        }

        onUseWith(ITEM,UNSTRUNG_LYRE,GOLDEN_WOOL){player,lyre,wool ->
            if(player.getSkills().getLevel(Skills.FLETCHING) >= 25){
                animate(player,1248)
                removeItem(player,Items.GOLDEN_WOOL_3694)
                removeItem(player,Items.UNSTRUNG_LYRE_3688)
                addItem(player,Items.LYRE_3689)
                sendDialogue(player,"You string the Lyre with the Golden Wool.")
            }
            else{
                sendDialogue(player,"You need 25 fletching to do this!")
            }
            return@onUseWith true
        }

        on(LONGHALL_BACKDOOR, SCENERY,"open"){player,door ->
            if(player.getAttribute("LyreEnchanted",false)){
                sendNPCDialogue(player,1278,"Yeah you're good to go through. Olaf tells me you're some kind of outerlander bard here on tour. I doubt you're worse than Olaf is.")
                DoorActionHandler.handleAutowalkDoor(player,door.asScenery())
            }
            else if(player.getAttribute("lyreConcertPlayed")){
                DoorActionHandler.handleAutowalkDoor(player,door.asScenery())
            }
            return@on true
        }

        on(LYRE_IDs,ITEM,"play"){player,lyre ->
            if(player.getAttribute("onStage",false) && player.getAttribute("lyreConcertPlayed",false) == false){
                Pulser.submit(LyreConcertPulse(player,lyre.id))
            }
            else if(questStage(player, "Fremennik Trials")!! < 20 || !isQuestComplete(player, "Fremennik Trials")!!){
                player.sendMessage("You lack the knowledge to play this.")
                return@on true
            }
            else if(LYRE_IDs.isLast(lyre.id)){
                player.sendMessage("Your lyre is out of charges!")
            } else {
                player.inventory?.remove(lyre.asItem())
                player.inventory?.add(Item(LYRE_IDs.getNext(lyre.id)))
                player.lock()
                Pulser.submit(telePulse(player))
            }
            return@on true
        }

        on(PIPE,SCENERY,"put-inside"){ player, _ ->
            val bombItem = Item(LIT_BOMB)
            if(player.inventory.containsItem(bombItem)){
                player.sendMessage("You stuff the lit object into the pipe.")
                player.setAttribute("/save:fremtrials:cherrybomb",true)
                player.inventory.remove(bombItem)
            } else {
                player.sendMessage("What am I supposed to put in there? A shoe?")
            }
            return@on true
        }

        on(PORTALIDs,SCENERY,"use"){ player, portal ->
            val toLocation =
            when(portal.id){
                2273 -> destRoom(2639, 10012, 2645, 10018).getCenter()
                2274 -> destRoom(2650, 10034, 2656, 10040).getCenter()
                2506 -> destRoom(2662, 10023, 2669, 10029).getCenter()
                2507 -> destRoom(2626, 10023, 2633, 10029).getCenter()
                2505 -> destRoom(2650, 10001, 2656, 10007).getCenter()
                2503 -> destRoom(2662, 10012, 2668, 10018).getCenter()
                2504 -> {player.setAttribute("/save:fremtrials:maze-complete",true); destRoom(2662, 10034, 2668, 10039).getCenter()}
                else -> getRandomLocation(player)
            }
            player.properties?.teleportLocation = toLocation
            return@on true;
        }

        on(SWENSEN_LADDER,SCENERY,"climb"){ player, _ ->
            if(player.getAttribute("fremtrials:swensen-accepted",false) == false){
                player.dialogueInterpreter?.sendDialogues(1283,FacialExpression.ANGRY,"Where do you think you're going?")
                return@on true
            }
            return@on true
        }

        on(THORVALD_LADDER, SCENERY, "climb-down") { player, _ ->
            if (isQuestComplete(player, "Fremennik Trials") || player.getAttribute("fremtrials:thorvald-vote",false)!!) {
                player.sendMessage("You have no reason to go back down there.")
                return@on true
            } else if (!player.getAttribute("fremtrials:warrior-accepted",false)!!) {
                player.dialogueInterpreter?.sendDialogues(NPCs.THORVALD_THE_WARRIOR_1289, FacialExpression.ANGRY,
                    "Outerlander... do not test my patience. I do not take",
                    "kindly to people wandering in here and acting as though",
                    "they own the place."
                )
                return@on true
            } else if (hasEquippableItems(player)) {
                player.dialogueInterpreter?.sendDialogues(NPCs.THORVALD_THE_WARRIOR_1289, FacialExpression.ANGRY,
                    "You may not enter the battleground with any armour",
                    "or weaponry of any kind."
                )
                player.dialogueInterpreter.addAction { player, buttonId ->
                    player.dialogueInterpreter?.sendDialogues(NPCs.THORVALD_THE_WARRIOR_1289, FacialExpression.ANGRY,
                        "If you need to place your equipment into your bank",
                        "account, I recommend that you speak to the seer. He",
                        "knows a spell that will do that for you."
                    )
                }
                return@on true
            }

            ClimbActionHandler.climb(player, Animation(828), Location.create(2671, 10099, 2))
            player.pulseManager.run(KoscheiPulse(player))
            return@on true
        }

        on(THORVALD_LADDER_LOWER, SCENERY, "climb-up") { player, _ ->
            ClimbActionHandler.climb(player, Animation(828), Location.create(2666, 3694, 0))
            return@on true
        }

        on(SWAYING_TREE,SCENERY,"cut-branch"){ player, _ ->
            SkillingTool.getHatchet(player)?.let { Pulser.submit(ChoppingPulse(player)).also { return@on true } }
            player.sendMessage("You need an axe which you have the woodcutting level to use to do this.")
            return@on true
        }

        on(SHOPNPCS,NPC,"Trade") { player, npc ->
            if(isQuestComplete(player, "Fremennik Trials")){
                npc.asNpc().openShop(player)
            }
            else {
                when(npc.id){
                    NPCs.THORA_THE_BARKEEP_1300 -> sendDialogue(player,"Only Fremenniks may buy drinks here.")
                    NPCs.SKULGRIMEN_1303 -> sendDialogue(player,"Only Fremenniks may purchase weapons and armour here.")
                    NPCs.SIGMUND_THE_MERCHANT_1282 -> sendDialogue(player,"Only Fremenniks may trade with this merchant.")
                    NPCs.YRSA_1301 -> sendDialogue(player,"Only Fremenniks may buy clothes here.")
                    NPCs.FISH_MONGER_1315 -> sendDialogue(player,"Only Fremenniks may purchase fish here.")
                }
            }
            return@on true
        }
    }

    class destRoom(val swx: Int, val swy: Int, val nex: Int, val ney: Int)

    fun destRoom.getCenter(): Location {
        return Location((swx + nex) / 2, (swy + ney) / 2).transform(1,0,0)
    }

    fun getRandomLocation(player: Player?): Location{
        var obj: Scenery? = null

        while(obj?.id != 5138) {
            val objects = player?.viewport?.chunks?.random()?.random()?.objects
            obj = objects?.random()?.random()
            if(obj == null || obj.location?.equals(Location(0,0,0))!!){
                continue
            }
        }
        return obj.location
    }

    fun hasEquippableItems(player: Player?): Boolean {
        val container = arrayOf(player!!.inventory, player.equipment)
        for (c in container) {
            for (i in c.toArray()) {
                if (i == null) {
                    continue
                }
                if (i.name.toLowerCase().contains(" rune")) {
                    return true
                }
                var slot: Int = i.definition.getConfiguration(ItemConfigParser.EQUIP_SLOT, -1)
                if (slot == -1 && i.definition.getConfiguration(ItemConfigParser.WEAPON_INTERFACE, -1) != -1) {
                    slot = SLOT_WEAPON
                }
                if (slot >= 0) {
                    return true
                }
            }
        }
        return false
    }

    class spiritPulse(val player: Player?, val fish: Int) : Pulse(){
        var counter = 0
        val npc = NPC(1273,player?.location)
        override fun pulse(): Boolean {
            when(counter++){
                0 -> npc.init().also { player?.lock() }.also { player?.inventory?.remove(Item(fish)) }
                1 -> npc.moveStep()
                2 -> npc.face(player).also { player?.face(npc) }
                3 -> player?.dialogueInterpreter?.sendDialogues(npc,
                    FacialExpression.HAPPY,"I will kindly accept this offering, and","bestow upon you a gift in return.")
                4 -> player?.inventory?.remove(Item(3689))
                5 -> when(fish){
                    383 -> player?.inventory?.add(Item(6125))
                    389 -> player?.inventory?.add(Item(6126))
                    395 -> player?.inventory?.add(Item(6127))
                }
                6 -> player?.unlock()
                10 -> npc.clear().also {
                    player?.setAttribute("/save:LyreEnchanted",true)
                    return true }
            }
            return false
        }
    }

    class ChoppingPulse(val player: Player) : Pulse() {
        var counter = 0
        override fun pulse(): Boolean {
            when(counter++){
                0 -> player.animator.animate(SkillingTool.getHatchet(player).animation)
                4 -> player.animator.reset().also { player.inventory.add(Item(3692));return true}
            }
            return false
        }
    }

    class LyreConcertPulse(val player: Player?, val Lyre: Int) : Pulse(){
        val GENERIC_LYRICS = arrayOf(
            "${player?.name?.capitalize()} is my name,",
            "I haven't much to say",
            "But since I have to sing this song.",
            "I'll just go ahead and play."
        )
        val CHAMPS_LYRICS = arrayOf(
            "The thought of lots of questing,",
            "Leaves some people unfulfilled,",
            "But I have done my simple best, in",
            "Entering the Champions Guild."
        )
        val HEROES_LYRICS = arrayOf(
            "When it comes to fighting",
            "I hit my share of zeroes",
            "But I'm well respected at",
            "the Guild reserved for Heroes,"
        )
        val LEGENDS_LYRICS = arrayOf(
            "I cannot even start to list",
            "The amount of foes I've killed.",
            "I will simply tell you this:",
            "I've joined the Legends' Guild!"
        )
        val SKILLS = mutableListOf(Skills.SKILL_NAME)
        var counter = 0
        val questPoints = player?.questRepository?.points
        val champGuild = player?.getAchievementDiaryManager()?.hasCompletedTask(DiaryType.VARROCK, 1, 1)?: false
        val legGuild = questPoints!! >= 111
        val heroGuild = questPoints!! >= 5
        val masteredAmount = player?.getSkills()?.masteredSkills!! > 0
        var SKILLNAME = getMasteredSkillNames(player!!)

        val SKILLER_LYRICS = if(masteredAmount){arrayOf(
            "When people speak of training,",
            "Some people think they're fine.",
            "But they just all seem jealous that",
            "My ${SKILLNAME.random()}'s ninety-nine!"
        )} else arrayOf("Pee pee poo poo pee","I really have to pee","If you sing this song","Let me know that this is what you see")

        val LYRICS = if(masteredAmount){
            println(masteredAmount)
            SKILLER_LYRICS
        } else if(legGuild){
            LEGENDS_LYRICS
        }else if(heroGuild){
            HEROES_LYRICS
        }else if(champGuild){
            CHAMPS_LYRICS
        }else{GENERIC_LYRICS}

        override fun pulse(): Boolean {
            when(counter++){
                0 -> {
                    player?.lock()
                    animate(player!!,1318,true)
                }
                2 -> {
                    animate(player!!,1320,true)
                    PacketRepository.send(MusicPacket::class.java, MusicContext(player, 165, true))
                }
                4 -> {
                    animate(player!!,1320,true)
                    PacketRepository.send(MusicPacket::class.java, MusicContext(player, 164, true))
                    sendChat(player,LYRICS[0])
                }
                6 -> {
                    animate(player!!,1320,true)
                    PacketRepository.send(MusicPacket::class.java, MusicContext(player, 164, true))
                    sendChat(player,LYRICS[1])
                }
                8 -> {
                    animate(player!!,1320,true)
                    PacketRepository.send(MusicPacket::class.java, MusicContext(player, 164, true))
                    sendChat(player,LYRICS[2])
                }
                10 ->{
                    animate(player!!,1320,true)
                    PacketRepository.send(MusicPacket::class.java, MusicContext(player, 163, true))
                    sendChat(player,LYRICS[3])
                }
                12 ->{
                    player?.setAttribute("/save:lyreConcertPlayed",true)
                    player?.removeAttribute("LyreEnchanted")
                    player?.inventory?.remove(Item(Lyre))
                    addItem(player!!,3689)
                    player?.unlock()
                }
            }
            return false
        }
    }

    class BranchFletchingPulse(val player: Player?) : Pulse(){
        var counter = 0
        override fun pulse(): Boolean {
            when(counter++){
                0 -> player?.animator?.animate(Animation(1248)).also { player!!.lock() }
                3 -> player?.inventory?.remove(Item(Items.BRANCH_3692)).also { player?.inventory?.add(Item(Items.UNSTRUNG_LYRE_3688)); player!!.unlock(); return true}
            }
            return false
        }
    }

    class telePulse(val player: Player) : Pulse(){
        var counter = 0
        override fun pulse(): Boolean {
            when(counter++){
                0 -> player.animator.animate(Animation(9600, Animator.Priority.VERY_HIGH), Graphics(1682))
                6 -> player.properties.teleportLocation = Location.create(2661, 3646, 0)
                7 -> player.unlock().also { return true }
            }
            return false
        }
    }

    class KoscheiPulse(val player: Player) : Pulse() {
        var counter = 0
        override fun pulse(): Boolean {
            when(counter++) {
                0 -> player.sendMessage("Explore this battleground and find your foe...")
                20 -> KoscheiSession.create(player).start().also { return true }
            }
            return false
        }
    }
}