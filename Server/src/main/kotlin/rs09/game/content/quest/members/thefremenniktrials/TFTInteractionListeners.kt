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
import core.game.node.entity.player.link.music.MusicEntry
import core.game.node.entity.skill.Skills
import core.game.node.entity.skill.gather.woodcutting.WoodcuttingSkillPulse
import core.game.node.scenery.Scenery
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.map.RegionManager
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import rs09.game.interaction.InteractionListener
import rs09.game.interaction.IntType
import rs09.game.system.config.ItemConfigParser
import rs09.game.world.GameWorld.Pulser

class TFTInteractionListeners : InteractionListener{

    private val BEER = intArrayOf(Items.BEER_3803,Items.BEER_1917)
    private val WORKER = NPCs.COUNCIL_WORKMAN_1287
    private val FISH_ALTAR = 4141
    private val FISH = intArrayOf(Items.RAW_SHARK_383,Items.RAW_SEA_TURTLE_395,Items.RAW_MANTA_RAY_389)
    private val LOW_ALC_KEG = Items.LOW_ALCOHOL_KEG_3712
    private val KEG = Items.KEG_OF_BEER_3711
    private val TINDERBOX = Items.TINDERBOX_590
    private val CHERRY_BOMB = Items.STRANGE_OBJECT_3713
    private val LIT_BOMB = Items.LIT_STRANGE_OBJECT_3714
    private val PIPE = 4162
    private val PORTALIDs = intArrayOf(2273,2274,2506,2507,2505,2503,2504,5138)
    private val SWENSEN_LADDER = 4158
    private val SWAYING_TREE = 4142
    private val KNIFE = Items.KNIFE_946
    private val TREE_BRANCH = Items.BRANCH_3692
    private val LYRE_IDs = intArrayOf(14591,14590,6127,6126,6125,3691,3690)
    private val THORVALD_LADDER = 34286
    private val THORVALD_LADDER_LOWER = 4188
    private val LALLIS_STEW = 4149
    private val UNSTRUNG_LYRE = Items.UNSTRUNG_LYRE_3688
    private val GOLDEN_FLEECE = Items.GOLDEN_FLEECE_3693
    private val GOLDEN_WOOL = Items.GOLDEN_WOOL_3694
    private val LONGHALL_BACKDOOR = 4148
    private val SHOPNPCS = intArrayOf(NPCs.YRSA_1301,NPCs.SKULGRIMEN_1303,NPCs.THORA_THE_BARKEEP_1300,NPCs.SIGMUND_THE_MERCHANT_1282,NPCs.FISH_MONGER_1315)
    private val SPINNING_WHEEL_IDS = intArrayOf(2644,4309,8748,20365,21304,25824,26143,34497,36970,37476)
    private val STEW_INGREDIENT_IDS = intArrayOf(Items.POTATO_1942,Items.ONION_1957,Items.CABBAGE_1965,Items.PET_ROCK_3695)

    override fun defineListeners() {
        onUseWith(IntType.NPC,BEER,WORKER){ player, beer, _ ->
            player.dialogueInterpreter.open(CouncilWorkerFTDialogue(0,true,beer.id), NPC(WORKER))
            return@onUseWith true
        }

        onUseWith(IntType.SCENERY, FISH, FISH_ALTAR){ player,fish,_ ->
            if(inInventory(player,Items.LYRE_3689) || inInventory(player,Items.ENCHANTED_LYRE_3690)) {
                Pulser.submit(SpiritPulse(player, fish.id))
            } else {
                sendMessage(player,"I should probably have my lyre with me.")
            }
            return@onUseWith true
        }

        onUseWith(IntType.ITEM,LOW_ALC_KEG,KEG){player,_,_ ->
            if(!getAttribute(player,"fremtrials:keg-mixed",false)){
                if(getAttribute(player,"fremtrials:cherrybomb",false)) {
                    removeItem(player,LOW_ALC_KEG)
                    setAttribute(player,"/save:fremtrials:keg-mixed", true)
                    sendMessage(player,"The cherry bomb in the pipe goes off.")
                    RegionManager.getLocalEntitys(player).stream().forEach { e -> e.sendChat("What was THAT??") }
                    sendMessage(player,"You mix the kegs together.")
                } else {
                    player.dialogueInterpreter?.sendDialogue("I can't do this right now. I should create","a distraction.")
                }
            } else return@onUseWith false
            return@onUseWith true
        }

        onUseWith(IntType.ITEM,TINDERBOX,CHERRY_BOMB){player,_,_ ->
            if(removeItem(player,CHERRY_BOMB)){
                addItem(player,LIT_BOMB)
                sendMessage(player,"You light the strange object.")
            }
            return@onUseWith true
        }

        onUseWith(IntType.ITEM,KNIFE,TREE_BRANCH){player,_,_ ->
            if (!player.skills.hasLevel(Skills.CRAFTING,40)) {
                sendDialogue(player, "You need 40 crafting to do this!")
                return@onUseWith true
            }
            if (inInventory(player,KNIFE))
                Pulser.submit(BranchFletchingPulse(player))
            else
                sendMessage(player,"You need a knife to do this.")
            return@onUseWith true
        }

        onUseWith(IntType.SCENERY, STEW_INGREDIENT_IDS, LALLIS_STEW){player,stewIngredient,_ ->
            when(stewIngredient.id){
                Items.ONION_1957 -> {
                    sendDialogue(player,"You added an onion to the stew")
                    setAttribute(player,"/save:lalliStewOnionAdded",true)
                    removeItem(player,stewIngredient)
                }
                Items.POTATO_1942 -> {
                    sendDialogue(player,"You added a potato to the stew")
                    setAttribute(player,"/save:lalliStewPotatoAdded",true)
                    removeItem(player,stewIngredient)
                }
                Items.CABBAGE_1965 -> {
                    sendDialogue(player,"You added a cabbage to the stew")
                    setAttribute(player,"/save:lalliStewCabbageAdded",true)
                    removeItem(player,stewIngredient)
                }
                Items.PET_ROCK_3695 -> {
                    sendDialogue(player,"You added your dear pet rock to the stew")
                    setAttribute(player,"/save:lalliStewRockAdded",true)
                    removeItem(player,stewIngredient)
                }
            }
            return@onUseWith true
        }

        onUseWith(IntType.SCENERY,GOLDEN_FLEECE,*SPINNING_WHEEL_IDS){ player, _, _ ->
            if(removeItem(player,GOLDEN_FLEECE)){
                addItem(player,GOLDEN_WOOL)
                animate(player,896)
                sendDialogue(player,"You spin the Golden Fleece into a ball of Golden Wool")
            }
            return@onUseWith true
        }

        onUseWith(IntType.ITEM,UNSTRUNG_LYRE,GOLDEN_WOOL){ player, _, _ ->
            if(player.getSkills().getLevel(Skills.FLETCHING) >= 25){
                if(removeItem(player,GOLDEN_WOOL) &&
                    removeItem(player,Items.UNSTRUNG_LYRE_3688)){
                    animate(player,1248)
                    addItem(player,Items.LYRE_3689)
                    sendDialogue(player,"You string the Lyre with the Golden Wool.")
                }
            } else sendDialogue(player,"You need 25 fletching to do this!")
            return@onUseWith true
        }

        on(LONGHALL_BACKDOOR, IntType.SCENERY, "open"){player,door ->
            when {
                getAttribute(player,"LyreEnchanted",false) -> {
                    sendNPCDialogue(player,1278,"Yeah you're good to go through. Olaf tells me you're some kind of outerlander bard here on tour. I doubt you're worse than Olaf is.")
                    DoorActionHandler.handleAutowalkDoor(player,door.asScenery())
                }
                getAttribute(player,"lyreConcertPlayed",false) -> {
                    DoorActionHandler.handleAutowalkDoor(player,door.asScenery())
                }
                else -> {
                    sendNPCDialogue(player,1278,"I didn't give you permission to go backstage!")
                }
            }
            return@on true
        }

        on(LYRE_IDs, IntType.ITEM, "play"){player,lyre ->
            if(getAttribute(player,"onStage",false) && !getAttribute(player,"lyreConcertPlayed",false)){
                Pulser.submit(LyreConcertPulse(player,lyre.id))
            } else if(questStage(player, "Fremennik Trials") < 20 || !isQuestComplete(player, "Fremennik Trials")){
                sendMessage(player,"You lack the knowledge to play this.")
            } else if(LYRE_IDs.isLast(lyre.id)){
                sendMessage(player,"Your lyre is out of charges!")
            } else {
                if(removeItem(player,lyre.asItem())){
                    addItem(player,LYRE_IDs.getNext(lyre.id))
                    Pulser.submit(TelePulse(player))
                }
            }
            return@on true
        }

        on(PIPE, IntType.SCENERY, "put-inside"){ player, _ ->
            if(inInventory(player,LIT_BOMB)){
                sendMessage(player,"You stuff the lit object into the pipe.")
                setAttribute(player,"/save:fremtrials:cherrybomb",true)
                removeItem(player,LIT_BOMB)
            } else {
                sendMessage(player,"What am I supposed to put in there? A shoe?")
            }
            return@on true
        }

        on(PORTALIDs, IntType.SCENERY, "use"){ player, portal ->
            player.properties?.teleportLocation = when(portal.id){
                2273 -> DestRoom(2639, 10012, 2645, 10018).getCenter()
                2274 -> DestRoom(2650, 10034, 2656, 10040).getCenter()
                2506 -> DestRoom(2662, 10023, 2669, 10029).getCenter()
                2507 -> DestRoom(2626, 10023, 2633, 10029).getCenter()
                2505 -> DestRoom(2650, 10001, 2656, 10007).getCenter()
                2503 -> DestRoom(2662, 10012, 2668, 10018).getCenter()
                2504 -> {
                    setAttribute(player,"/save:fremtrials:maze-complete",true)
                    DestRoom(2662, 10034, 2668, 10039).getCenter()
                }
                else -> getRandomLocation(player)
            }
            return@on true
        }

        on(SWENSEN_LADDER, IntType.SCENERY, "climb"){ player, _ ->
            if(!getAttribute(player,"fremtrials:swensen-accepted",false)){
                sendNPCDialogue(player,1283,"Where do you think you're going?",FacialExpression.ANGRY)
            }
            return@on true
        }

        on(THORVALD_LADDER, IntType.SCENERY, "climb-down") { player, _ ->
            if (isQuestComplete(player, "Fremennik Trials") || getAttribute(player, "fremtrials:thorvald-vote", false)) {
                sendMessage(player,"You have no reason to go back down there.")
                return@on true
            } else if (!getAttribute(player,"fremtrials:warrior-accepted",false)) {
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
                player.dialogueInterpreter.addAction { _, _ ->
                    player.dialogueInterpreter?.sendDialogues(NPCs.THORVALD_THE_WARRIOR_1289, FacialExpression.ANGRY,
                        "If you need to place your equipment into your bank",
                        "account, I recommend that you speak to the seer. He",
                        "knows a spell that will do that for you."
                    )
                }
                return@on true
            }

            if (player.getExtension<Any?>(KoscheiSession::class.java) != null) {
                KoscheiSession.getSession(player).close()
            }
            ClimbActionHandler.climb(player, Animation(828), Location.create(2671, 10099, 2))
            Pulser.submit(KoscheiPulse(player))
            return@on true
        }

        on(THORVALD_LADDER_LOWER, IntType.SCENERY, "climb-up") { player, _ ->
            if (player.getExtension<Any?>(KoscheiSession::class.java) != null) {
                KoscheiSession.getSession(player).close()
            }
            ClimbActionHandler.climb(player, Animation(828), Location.create(2666, 3694, 0))
            return@on true
        }

        on(SWAYING_TREE, IntType.SCENERY, "cut-branch"){ player, node ->
            player.pulseManager.run(WoodcuttingSkillPulse(player, node as Scenery))
            return@on true
        }

        on(SHOPNPCS, IntType.NPC, "Trade") { player, npc ->
            if(isQuestComplete(player, "Fremennik Trials")){
                npc.asNpc().openShop(player)
            } else when(npc.id){
                NPCs.THORA_THE_BARKEEP_1300 -> sendDialogue(player,"Only Fremenniks may buy drinks here.")
                NPCs.SKULGRIMEN_1303 -> sendDialogue(player,"Only Fremenniks may purchase weapons and armour here.")
                NPCs.SIGMUND_THE_MERCHANT_1282 -> sendDialogue(player,"Only Fremenniks may trade with this merchant.")
                NPCs.YRSA_1301 -> sendDialogue(player,"Only Fremenniks may buy clothes here.")
                NPCs.FISH_MONGER_1315 -> sendDialogue(player,"Only Fremenniks may purchase fish here.")
            }
            return@on true
        }
    }

    class DestRoom(val swx: Int, val swy: Int, val nex: Int, val ney: Int)

    fun DestRoom.getCenter(): Location {
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
                if (i.name.lowercase().contains(" rune")) {
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

    class SpiritPulse(val player: Player, val fish: Int) : Pulse(){
        var counter = 0
        val npc = NPC(1273, player.location)
        override fun pulse(): Boolean {
            when(counter++){
                0 -> {
                    npc.init()
                    player.lock()
                    removeItem(player,fish)
                }
                1 -> npc.moveStep()
                2 -> npc.face(player).also { player.face(npc) }
                3 -> player.dialogueInterpreter?.sendDialogues(npc,
                    FacialExpression.HAPPY,"I will kindly accept this offering, and","bestow upon you a gift in return.")
                4 -> if(!removeItem(player,Items.LYRE_3689)) { removeItem(player,Items.ENCHANTED_LYRE_3690) }
                5 -> when(fish){
                    383 -> addItem(player,Items.ENCHANTED_LYRE2_6125)
                    389 -> addItem(player,Items.ENCHANTED_LYRE3_6126)
                    395 -> addItem(player,Items.ENCHANTED_LYRE4_6127)
                }
                6 -> player.unlock()
                10 -> npc.clear().also {
                    setAttribute(player,"/save:LyreEnchanted",true)
                    return true }
            }
            return false
        }
    }

    class LyreConcertPulse(val player: Player, val Lyre: Int) : Pulse(){
        val GENERIC_LYRICS = arrayOf(
            "${player.name?.capitalize()} is my name,",
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
        var counter = 0
        val questPoints = getQP(player)
        val champGuild = player.achievementDiaryManager?.hasCompletedTask(DiaryType.VARROCK, 1, 1)?: false
        val legGuild = questPoints >= 111
        val heroGuild = questPoints >= 5
        val masteredAmount = player.getSkills()?.masteredSkills!! > 0
        var SKILLNAME = getMasteredSkillNames(player)

        val LYRICS = when {
            masteredAmount -> {
                arrayOf(
                    "When people speak of training,",
                    "Some people think they're fine.",
                    "But they just all seem jealous that",
                    "My ${SKILLNAME.random()}'s ninety-nine!"
                )
            }
            legGuild -> LEGENDS_LYRICS
            heroGuild -> HEROES_LYRICS
            champGuild -> CHAMPS_LYRICS
            else -> GENERIC_LYRICS
        }

        override fun pulse(): Boolean {
            when(counter++){
                0 -> {
                    player.lock()
                    animate(player,1318,true)
                }
                2 -> {
                    animate(player,1320,true)
                    player.musicPlayer.play(MusicEntry.forId(165))
                }
                4 -> {
                    animate(player,1320,true)
                    player.musicPlayer.play(MusicEntry.forId(164))
                    sendChat(player,LYRICS[0])
                }
                6 -> {
                    animate(player,1320,true)
                    player.musicPlayer.play(MusicEntry.forId(164))
                    sendChat(player,LYRICS[1])
                }
                8 -> {
                    animate(player,1320,true)
                    player.musicPlayer.play(MusicEntry.forId(164))
                    sendChat(player,LYRICS[2])
                }
                10 ->{
                    animate(player,1320,true)
                    player.musicPlayer.play(MusicEntry.forId(164))
                    sendChat(player,LYRICS[3])
                }
                14 ->{
                    setAttribute(player,"/save:lyreConcertPlayed",true)
                    player.removeAttribute("LyreEnchanted")
                    if(removeItem(player,Lyre))
                        addItem(player,Items.ENCHANTED_LYRE_3690)
                    player.unlock()
                }
            }
            return false
        }
    }

    class BranchFletchingPulse(val player: Player) : Pulse(){
        var counter = 0
        override fun pulse(): Boolean {
            when(counter++){
                0 -> player.animator?.animate(Animation(1248)).also { player.lock() }
                3 -> {
                    if(removeItem(player,Items.BRANCH_3692))
                        addItem(player,Items.UNSTRUNG_LYRE_3688)
                    player.unlock()
                    return true
                }
            }
            return false
        }
    }

    class TelePulse(val player: Player) : Pulse(){
        var counter = 0
        override fun pulse(): Boolean {
            when(counter++){
                0 -> {
                    player.lock()
                    player.animator.animate(Animation(9600, Animator.Priority.VERY_HIGH), Graphics(1682))
                }
                6 -> player.properties.teleportLocation = Location(2661, 3646, 0)
                7 -> player.unlock().also { return true }
            }
            return false
        }
    }

    class KoscheiPulse(val player: Player) : Pulse() {
        var counter = 0
        override fun pulse(): Boolean {
            when(counter++) {
                0 -> sendMessage(player,"Explore this battleground and find your foe...")
                20 -> {
                    if (player.getExtension<Any?>(KoscheiSession::class.java) != null)
                        return true
                    KoscheiSession.create(player).start().also { return true }
                }
            }
            return false
        }
    }
}