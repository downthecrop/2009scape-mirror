package core.game.system.command.sets

import content.global.handlers.item.SpadeDigListener
import content.region.misc.tutisland.handlers.iface.CharacterDesign
import core.api.*
import core.ServerStore
import core.game.dialogue.DialogueFile
import core.game.node.entity.combat.ImpactHandler
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.info.login.PlayerSaver
import core.game.node.item.Item
import core.game.system.command.Privilege
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.Location
import core.game.world.map.RegionManager
import core.game.world.repository.Repository.getPlayerByName
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.plugin.Initializable
import core.tools.RandomFunction
import core.tools.Log
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.rs09.consts.Sounds
import java.awt.HeadlessException
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection
import kotlin.streams.toList

@Initializable
class FunCommandSet : CommandSet(Privilege.ADMIN) {

    var npcs: List<NPC> = ArrayList()
    private val TREASURE_KEY = "fun_command_buried_treasures"
    private val treasures = ArrayList<BuriedTreasure>()

    init {
        loadTreasures()
    }

    /**
     * I hope ceikry doesn't punish me for this ~ used SkillcapePerks for a base idea tried to make it modular.
     * Supports persistence across reboots for bury, buryitem, and burymessage supporting all command arguments.
     */
    private fun loadTreasures() {

        val archive = ServerStore.getArchive(TREASURE_KEY)
        if (!archive.containsKey("data")) return

        try {
            val array = archive["data"] as JSONArray

            for (obj in array) {

                val json = obj as JSONObject
                val x = (json["x"] as Long).toInt()
                val y = (json["y"] as Long).toInt()
                val z = (json["z"] as Long).toInt()
                val loc = Location.create(x, y, z)

                val type = TreasureType.valueOf(json["type"] as String)
                val hiderName = json["hiderName"] as? String ?: "Unknown"
                val hideName = json["hideName"] as? Boolean ?: false
                val customMessage = json["customMessage"] as? String
                val message = json["message"] as? String ?: ""

                val items = ArrayList<Item>()
                if (json.containsKey("items")) {
                    val itemsJson = json["items"] as JSONArray
                    for (i in itemsJson) {
                        val itemObj = i as JSONObject
                        val id = (itemObj["id"] as Long).toInt()
                        val amount = (itemObj["amount"] as Long).toInt()
                        items.add(Item(id, amount))
                    }
                }

                val treasure = BuriedTreasure(loc, type, items, message, hiderName, hideName, customMessage)
                treasures.add(treasure)
                registerTreasureListener(treasure)
            }
            log(FunCommandSet::class.java, Log.DEBUG, "Loaded ${treasures.size} buried treasures.")
        } catch (e: Exception) {
            log(FunCommandSet::class.java, Log.ERR, "Failed to load buried treasures: ${e.message}")
            e.printStackTrace()
        }
    }


    override fun defineCommands() {

        /**
         * Force animation + messages on all NPCs in a radius of 10 from the player.
         * Add an optional 3rd argument if cycling through a range of animation ids.
         */
        define("npcanim", Privilege.ADMIN, "::npcanim <lt>animation-id<gt> [end-animation-id]", "Makes nearby NPCs play <lt>animation-id<gt>, or loop that id up to <lt>end-animation-id<gt>.") { player, args ->
            if (args.size < 2) {
                reject(player, "Syntax error: ::npcanim <Animation ID>")
            }
            npcs = RegionManager.getLocalNpcs(player.location, 10)
            for (n in npcs) {
                n.lock(6)
                n.faceTemporary(player, 6)
                if (args.size == 2) {
                    n.sendChat(args.slice(1 until args.size).joinToString(" "))
                    n.animate(Animation(args[1].toInt()))
                    n.animate(Animation.create(-1), 6)
                }
                if (args.size == 3) {
                    var count = 0
                    for(animNum in args[1].toInt()..args[2].toInt()) {
                        count++
                        n.animate(Animation(animNum), count*6)
                    }
                }
            }
        }


        /**
         * Transform a player's appearance into that of an NPC.
         */
        define("pnpc", Privilege.MODERATOR, "::pnpc <lt>NPC ID<gt>", "Transforms the player into the given NPC."){ player, args ->
            if(args.size < 2){
                reject(player, "Usage: ::pnpc <npcid>")
                return@define
            }

            val pnpc_id = args[1].toIntOrNull()
            if(pnpc_id == null){
                reject(player, "<npcid> must be a valid integer.")
            }

            player.appearance.transformNPC(pnpc_id!!)
            notify(player,"Transformed into NPC $pnpc_id")
        }


        /**
         * Open bank
         */
        define("bank", Privilege.ADMIN, "", "Opens your bank."){ player, _ ->
            player.getBank().open()
        }

        /**
         * Toggle invisibility
         */
        define("invis", Privilege.ADMIN, "", "Makes you invisible to others."){ player, _ ->
            player.isInvisible = !player.isInvisible
            notify(player,"You are now ${if (player.isInvisible) "invisible" else "visible"} to others.")
        }


        /**
         * Toggle 1-hit kills
         */
        define("1hit", Privilege.ADMIN, "", "Makes you kill things in 1 hit."){ player, _ ->
            player.setAttribute("1hko", !player.getAttribute("1hko", false))
            notify(player,"1-hit KO mode " + if (player.getAttribute("1hko", false)) "on." else "off.")
        }


        /**
         * Toggle god mode
         */
        define("god", Privilege.ADMIN, "", "Makes you invulnerable to damage."){ player, _ ->
            player.setAttribute("godMode", !player.getAttribute("godMode", false))
            notify(player,"God mode ${if (player.getAttribute("godMode", false)) "enabled." else "disabled."}")
        }


        /**
         * Go on Mr Bones' Wild Ride
         */
        define("mrboneswildride"){ player, args ->
            val p : Player = if(args.size > 2){
                reject(player, "Usage: ::mrboneswildride <username>")
                return@define
            } else if(args.size == 1) {
                player
            } else if(getPlayerByName(args[1]) == null) {
                reject(player, "ERROR: Username not found. Usage: ::mrboneswildride <username>")
                return@define
            } else {
                getPlayerByName(args[1]) ?: return@define
            }
            val boneMode = !p.getAttribute("boneMode",false)
            p.setAttribute("boneMode", boneMode)
            notify(p,"Bone Mode ${if (boneMode) "<col=00ff00>ENGAGED</col>." else "<col=ff0000>POWERING DOWN</col>."}")
            p.appearance.rideCart(boneMode)
            if (p.appearance.isRidingMinecart) {
                var i = 0
                GameWorld.Pulser.submit(object : Pulse(1, player) {
                    override fun pulse(): Boolean {
                        if (i++ % 12 == 0) p.sendChat("I want to get off Mr. Bones Wild Ride.")
                        p.moveStep()
                        return !p.appearance.isRidingMinecart
                    }
                })
            }
        }


        /**
         * Opens up the makeover interface
         */
        define("makeover", Privilege.MODERATOR, description = "Opens the makeover interface for your character."){ player, _ ->
            CharacterDesign.open(player)
        }

        /**
         * Copies your current appearance and equipment as JSON to the clipboard
         */
        define("dumpappearance", Privilege.MODERATOR, description = "Copies your appearance and equipment as JSON to the clipboard."){ player, _ ->
            val json = JSONObject()
            PlayerSaver(player).saveAppearance(json)
            val equipJson = PlayerSaver(player).saveContainer(player.equipment)
            json["equipment"] = equipJson
            val jsonString = json.toJSONString()
            try {
                val clpbrd = Toolkit.getDefaultToolkit().systemClipboard
                clpbrd.setContents(StringSelection(jsonString), null)
                notify(player, "Appearance and equipment copied to clipboard.")
            } catch (e: HeadlessException) {
                reject(player, "NOTE: Paste will not be available due to remote server.")
            }
        }

        /**
         * Bury inventory at current location
         */
        define("bury", description = "Buries your entire inventory at your current location for treasure hunters. Required true/false to hide players name and an optional custom message."){player, args ->
            if(player.inventory.isEmpty){
                reject(player, "You have no items to bury.")
            }

            if (args.size < 2) {
                reject(player, "Usage: ::bury <lt>true | false<gt> [custom news message]")
                return@define
            }

            val hideName = when(args[1].lowercase()) {
                "true" -> true
                "false" -> false
                else -> {
                    reject(player, "Invalid argument. Usage: ::bury <lt>true | false<gt> [custom news message]")
                    return@define
                }
            }

            val customMessage = if (args.size > 2) {
                args.slice(2 until args.size).joinToString(" ")
            } else {
                null
            }

            player.dialogueInterpreter.open(object : DialogueFile(){
                override fun handle(componentID: Int, buttonID: Int) {
                    when(stage){
                        0 -> dialogue("This will bury your whole inventory at this spot.","Are you sure?").also { stage++ }
                        1 -> options("Yes","No").also { stage++ }
                        2 -> when(buttonID){
                            1 -> bury(player, hideName, customMessage).also { end() }
                            2 -> end()
                        }
                    }
                }
            })

        }

        /**
         * Bury a secret message at current location for a player to find.
         */
        define("burymessage", description = "Buries a secret message at your current location."){ player, args ->
            if (args.size < 2) {
                reject(player, "Usage: ::burymessage [message]")
                return@define
            }

            val message = args.slice(1 until args.size).joinToString(" ")
            val loc = Location.create(player.location)

            if(SpadeDigListener.listeners.containsKey(loc)){
                reject(player, "There is already something buried here.")
                return@define
            }

            val treasure = BuriedTreasure(loc, TreasureType.MESSAGE, emptyList(), message, player.username, false, null)
            treasures.add(treasure)
            saveTreasures()
            registerTreasureListener(treasure)
            
            notify(player, "You have buried the message '$message' at $loc")
            log(FunCommandSet::class.java, Log.DEBUG, "Message buried by ${player.username} at $loc: \"$message\"")
        }

        /**
         * Bury a specific item at current location
         */
        define("buryitem", description = "Buries a specific item at your current location."){ player, args ->
            if (args.size < 3) {
                reject(player, "Usage: ::buryitem <lt>item id<gt> <lt>true | false<gt> [custom message]")
                return@define
            }

            val itemId = args[1].toIntOrNull()
            if (itemId == null) {
                reject(player, "Invalid item ID.")
                return@define
            }

            val hideName = when(args[2].lowercase()) {
                "true" -> true
                "false" -> false
                else -> {
                    reject(player, "Invalid argument. Usage: ::buryitem <lt>item id<gt> <lt>true | false<gt> [custom message]")
                    return@define
                }
            }

            val customMessage = if (args.size > 3) {
                args.slice(3 until args.size).joinToString(" ")
            } else {
                null
            }

            player.dialogueInterpreter.open(object : DialogueFile(){
                override fun handle(componentID: Int, buttonID: Int) {
                    when(stage){
                        0 -> dialogue("This will bury a ${Item(itemId).name} at this spot.","Are you sure?").also { stage++ }
                        1 -> options("Yes","No").also { stage++ }
                        2 -> when(buttonID){
                            1 -> buryItem(player, itemId, hideName, customMessage).also { end() }
                            2 -> end()
                    }
                }
            }
            })


        }

        define("appearance", Privilege.ADMIN, "", "Allows you to change your appearance."){ player, _ ->
            CharacterDesign.reopen(player)
        }

        /**
         * Cast a weakened version of ice barrage on nearby players within the defined radius.
         * This spell will never kill or freeze a player
         */
        define("barrage", Privilege.ADMIN, "::barrage <lt>radius<gt>", "Cast a weak Ice Barrage on all nearby players. Will never kill players") { player, args ->
            if (args.size != 2)
                reject(player, "Usage: ::barrage radius[max = 50]")
            val radius = if (args[1].toInt() > 50) 50 else args[1].toInt()
            val nearbyPlayers = RegionManager.getLocalPlayers(player, radius).stream().filter { p: Player -> p.username != player.username }.toList()
            animate(player, 1978)
            playGlobalAudio(player.location, Sounds.ICE_CAST_171)
            for (p in nearbyPlayers) {
                playGlobalAudio(p.location, Sounds.ICE_BARRAGE_IMPACT_168, 20)
                val impactAmount = if (p.skills.lifepoints < 10 ) 0 else RandomFunction.getRandom(3)
                impact(p, impactAmount, ImpactHandler.HitsplatType.NORMAL)
                p.graphics(Graphics(369, 0))
            }
        }

        /**
         * Toggles pet hunger to off, normal, or dev
         * 0 = No hunger/growth mode (hunger/growth does not progress)
         * 1 = Normal hunger/growth mode (1x speed)
         * 2 = Dev hunger/growth mode (100x speed)
         */
        define("petrate", Privilege.ADMIN, "::petrate <lt>0-2<gt>", "Sets pet hunger and growth to off, normal, or dev."){ player, args ->
            if(args.size < 2) {
                notify(player, "Pet mode is currently ${player.getAttribute("petrate", 1)}")
                return@define
            }
            val mode = args[1].toIntOrNull() ?: (-1).also { reject(player, "Please enter a valid number") }
            if (mode in 0 .. 2) {
                player.setAttribute("petrate", mode)
                notify(player, "Setting pet mode to $mode")
            }
            else {
                reject(player, "Only modes 0-2 are valid")
            }
        }

        /**
         * Toggle instant woodcutting.
         */
        define("instachop", Privilege.ADMIN, "", "Fells trees after a single log is gathered."){ player, _ ->
            player.setAttribute("instachop", !player.getAttribute("instachop", false))
            notify(player,"Instachop mode " + if (player.getAttribute("instachop", false)) "on." else "off.")
        }

    }

    fun bury(player: Player, hideName: Boolean = false, customMessage: String? = null){

        val loc = Location.create(player.location)
        if(SpadeDigListener.listeners.containsKey(loc)){
            notify(player, "There is already something buried here.")
            return
        }
        
        val inv = player.inventory.toArray().filterNotNull().toList()
        if (inv.isEmpty()) {
            notify(player, "You have nothing to bury.")
            return
        }

        val treasure = BuriedTreasure(loc, TreasureType.ITEM, inv, "", player.username, hideName, customMessage)
        treasures.add(treasure)
        saveTreasures()
        registerTreasureListener(treasure)
        
        player.inventory.clear()
        notify(player, "You have buried your loot at ${loc.toString()}")
        log(FunCommandSet::class.java, Log.DEBUG, "Loot buried by ${player.username} at $loc. Masked: $hideName")
    }

    fun buryItem(player: Player, itemId: Int, hideName: Boolean, customMessage: String? = null) {
        val loc = Location.create(player.location)
        if(SpadeDigListener.listeners.containsKey(loc)){
            notify(player, "There is already something buried here.")
            return
        }

        val treasure = BuriedTreasure(loc, TreasureType.ITEM, listOf(Item(itemId, 1)), "", player.username, hideName, customMessage)
        treasures.add(treasure)
        saveTreasures()
        registerTreasureListener(treasure)
        
        notify(player, "You have buried item ${Item(itemId).name} at $loc")
        log(FunCommandSet::class.java, Log.DEBUG, "Item $itemId buried by ${player.username} at $loc. Masked: $hideName")
    }
    
    private fun registerTreasureListener(t: BuriedTreasure) {

        SpadeDigListener.registerListener(t.location) { p ->
            log(FunCommandSet::class.java, Log.DEBUG, "SpadeDigListener triggered for ${p.username} at ${t.location}")
            
            if (t.type == TreasureType.ITEM) {
                for(item in t.items){
                    addItemOrDrop(p, item.id, item.amount)
                    sendMessage(p, "You dig and find ${if(item.amount > 1) "some" else "a"} ${item.name}")
                }
                val finderName = if (t.hideName) "Someone" else p.username
                val message = t.customMessage ?: "$finderName has found the hidden treasure! Congratulations!!!"
                sendNews(message)
            } else {
                sendMessage(p, t.message)
            }
            
            SpadeDigListener.listeners.remove(t.location)
            treasures.remove(t)
            saveTreasures()
        }
    }

    private fun saveTreasures() {

        val array = JSONArray()

        for (t in treasures) {
            val json = JSONObject()
            json["x"] = t.location.x
            json["y"] = t.location.y
            json["z"] = t.location.z
            json["type"] = t.type.name
            json["hiderName"] = t.hiderName
            json["hideName"] = t.hideName
            if (t.customMessage != null) json["customMessage"] = t.customMessage
            json["message"] = t.message

            val itemsArray = JSONArray()
            for (i in t.items) {
                val itemObj = JSONObject()
                itemObj["id"] = i.id
                itemObj["amount"] = i.amount
                itemsArray.add(itemObj)
            }
            json["items"] = itemsArray

            array.add(json)
        }
        val archive = ServerStore.getArchive(TREASURE_KEY)
        archive["data"] = array
    }

    private data class BuriedTreasure(
        val location: Location,
        val type: TreasureType,
        val items: List<Item>,
        val message: String,
        val hiderName: String,
        val hideName: Boolean,
        val customMessage: String?
    )

    private enum class TreasureType { ITEM, MESSAGE }

}
