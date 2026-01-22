package core.game.system.command.sets

import content.global.handlers.iface.*
import content.global.skill.farming.timers.*
import content.minigame.fishingtrawler.TrawlerLoot
import content.region.misthalin.draynor.quest.anma.AnmaCutscene
import core.ServerConstants
import core.api.*
import core.api.utils.permadeath
import core.cache.def.impl.NPCDefinition
import core.cache.def.impl.SceneryDefinition
import core.cache.def.impl.VarbitDefinition
import core.game.bots.AIRepository
import core.game.component.Component
import core.game.ge.GrandExchange
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.info.Rights
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.system.command.Command
import core.game.system.command.CommandMapping
import core.game.system.command.Privilege
import core.game.system.communication.CommunicationInfo
import core.game.world.map.RegionManager
import core.game.world.map.build.DynamicRegion
import core.game.world.repository.Repository
import core.plugin.Initializable
import core.tools.Log
import core.tools.StringUtils
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.rs09.consts.Components
import java.awt.HeadlessException
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection

@Initializable
class MiscCommandSet : CommandSet(Privilege.ADMIN){
    override fun defineCommands() {

        /**
         * Toggles debug mode
         */
        define("debug", Privilege.STANDARD, "", "Toggles debug mode."){ player, _ ->
            player.toggleDebug()
        }

        define("calcaccuracy", Privilege.STANDARD, "::calcaccuracy <lt>NPC ID<gt>", "Calculates and prints your current chance to hit a given NPC."){ player, args ->
            val handler = player.getSwingHandler(false)
            player.sendMessage("handler type: ${handler.type}")
            val accuracy = handler.calculateAccuracy(player)
            player.sendMessage("calculateAccuracy: ${accuracy}")

            if (args.size > 1)
            {
                val npcId: Int = args[1].toInt()
                val npc = NPC(npcId)
                npc.initConfig()
                player.sendMessage("npc: ${npc.name}. npc defence: ${npc.skills.getLevel(Skills.DEFENCE)}")
                val defence = handler.calculateDefence(npc, player)
                player.sendMessage("calculateDefence: ${defence}")
                val chance: Double = if (accuracy > defence) {
                    1.0 - ((defence + 2.0) / (2.0 * (accuracy + 1.0)))
                } else {
                    accuracy / (2.0 * (defence + 1.0))
                }
                player.sendMessage("chance to hit: ${chance}")
            }
        }

        define("anmacs", Privilege.ADMIN, description = "Starts the AnmaCutscene sequence for testing.") { player, _ ->
            AnmaCutscene(player).start()
        }

        define("setsaveversion", Privilege.ADMIN, "::setsaveversion <lt>version<gt>", "Overrides your save version to <lt>version<gt> so migration logic can run.") { player, args ->
            try{
                player.version = args[1].toInt()
                notify(player, "Setting save version to ${player.version}")
            }
            catch (nfe: NumberFormatException){
                reject(player, "Save versions can only be an integer")
            }
        }

        /**
         * Prints player's current location
         */
        define("loc", Privilege.STANDARD, "", "Prints quite a lot of information about your current location."){ player, _->
            val l = player.location
            val r = player.viewport.region
            var obj: Scenery? = null
            notify(player,"Absolute: " + l + ", regional: [" + l.localX + ", " + l.localY + "], chunk: [" + l.chunkOffsetX + ", " + l.chunkOffsetY + "], flag: [" + RegionManager.isTeleportPermitted(l) + ", " + RegionManager.getClippingFlag(l) + ", " + RegionManager.isLandscape(l) + "].")
            notify(player,"Region: [id=" + l.regionId + ", active=" + r.isActive + ", instanced=" + (r is DynamicRegion) + "], obj=" + RegionManager.getObject(l) + ".")
            notify(player, "Jagex: ${l.z}_${l.regionId shr 8}_${l.regionId and 0xFF}_${l.localX}_${l.localY}")
            notify(player,"Object: " + RegionManager.getObject(l).also{obj = it} + ".")
            notify(player,"Object Varp: " + obj?.definition?.configFile?.varpId + " offset: " + obj?.definition?.configFile?.startBit + " size: " + (obj?.definition?.configFile?.startBit?.minus(obj?.definition?.configFile?.startBit!!)))
            log(this::class.java, Log.FINE,  "Viewport: " + l.getSceneX(player.playerFlags.lastSceneGraph) + "," + l.getSceneY(player.playerFlags.lastSceneGraph))
            val loc = "Location.create(" + l.x + ", " + l.y + ", " + l.z + ")"
            log(this::class.java, Log.FINE,  loc + "; " + player.playerFlags.lastSceneGraph + ", " + l.localX + ", " + l.localY)
            try {
                val stringSelection = StringSelection(loc)
                val clpbrd = Toolkit.getDefaultToolkit().systemClipboard
                clpbrd.setContents(stringSelection, null)
                notify(player, "Coordinates copied to clipboard.")
            } catch (e: HeadlessException) {
                reject(player, "NOTE: Paste will not be available due to remote server.")
            }
        }

        /**
         * Tells the player to use loc
         */
        define("pos", Privilege.STANDARD){ player, _->
            notify(player, "Do you mean ::loc?")
        }

        /**
         * Tells the player to use loc
         */
        define("coords", Privilege.STANDARD){ player, _->
            notify(player, "Do you mean ::loc?")
        }

        define("calcmaxhit", Privilege.STANDARD, description = "Calculates and prints your current maximum hit.") { player, _ ->
            val swingHandler = player.getSwingHandler(false)
            val hit = swingHandler.calculateHit(player, player, 1.0)
            notify(player, "max hit: ${hit} (${(swingHandler as Object).getClass().getName()})")
        }

        /**
         * Empty a player's inventory
         * ADMIN only (for obvious reasons)
         */
        define("empty", Privilege.ADMIN, "", "Empties/clears your inventory."){player,_->
            player.inventory.clear()
            player.inventory.refresh()
        }

        /**
         * Announces a message in chat (NEWS)
         */
        define("announce", Privilege.ADMIN, "::announce <lt>String<gt>", "Sends the given string as a News message."){_,args ->
            val message = args.slice(1 until args.size).joinToString(" ")
            Repository.sendNews(message)
        }

        /**
         * Lists the players currently online
         */
        define("players", Privilege.MODERATOR, "", "Lists the online players."){ player, _ ->
            val rights = player.rights.ordinal
            if (player!!.interfaceManager.isOpened && player.interfaceManager.opened.id != Components.QUESTJOURNAL_SCROLL_275 || player.locks.isMovementLocked || player.locks.isTeleportLocked) {
                reject(player, "Please finish what you're doing first.")
            }
            player.interfaceManager.open(Component(Components.QUESTJOURNAL_SCROLL_275))
            var i = 0
            while (i < 257) {
                player.packetDispatch.sendString("", 275, i)
                i++
            }
            val red = "<col=8A0808>"
            player.packetDispatch.sendString("<col=8A0808>" + "Players" + "</col>", 275, 2)
            var lineStart = 11
            for(p in Repository.players){
                if(!p.isArtificial)
                    player.packetDispatch.sendString(red + "<img=" + (Rights.getChatIcon(p) - 1) + ">" + p.username + if(rights > 0) " [ip=" + p.details.ipAddress + ", name=" + p.details.compName + "]" else "",275,lineStart++)
            }
        }

        /**
         * Lists information about a bot
         */
        define("botinfo", Privilege.STANDARD, "::botinfo <lt>botname<gt>", "Prints debug information about a bot"){ player, args ->
            val scriptInstances = AIRepository.PulseRepository

            // Find the bot with the given name (non-case sensitive, concat args by space)
            val botName = args.slice(1 until args.size).joinToString(" ").lowercase()
            val bot = scriptInstances[botName]
            if (bot == null) {
                reject(player, "No bot with that name found.")
                return@define
            }
            val botInfo = bot.botScript.toString()
            // Print the bot's information, max 80chars per line
            botInfo.chunked(80).forEach { notify(player, it) }
        }

        /**
         * Opens the credit/voting shop
         */
        define("shop", Privilege.STANDARD, "", "Opens the credit shop."){ player, _ ->
            if (player.locks.isInteractionLocked || player.locks.isMovementLocked) {
                sendMessage(player, "You can't open the shop right now.")
            } else player.interfaceManager.open(Component(Components.CREDIT_SHOP))
        }

        /**
         * Shows the player a list of currently active GE sell offers
         */
        define("ge", Privilege.STANDARD, "::ge <lt>MODE<gt> (Modes: buying, selling, search, bots, botsearch)", "Various commands for viewing GE offers.") { player, args ->
            if(args.size < 2){
                reject(player, "Usage: ::ge mode", "Available modes: buying, selling, search, bots, botsearch")
            }

            val mode = args[1]
            when(mode){
                "buying" -> showGeBuy(player)
                "selling" -> showGeSell(player)
                "search" -> showGeInputDialogue(player, args, ::showOffers)
                "bots" -> showGeBots(player)
                "botsearch" -> showGeInputDialogue(player, args, ::showGeBotsearch)
                else -> reject(player, "Invalid mode used. Available modes are: buying, selling, search")
            }
        }
        /**
         * ==================================================================================
         */


        /**
         * List all commands
         */
        define("commands", Privilege.STANDARD, "::commands <lt>page<gt>", "Lists all the commands (you are here.)"){player, args ->
            val page = if (args.size > 1) (args[1].toIntOrNull() ?: 1) - 1 else 0
            val pages = CommandMapping.getPageIndices(player.rights.ordinal)
            val end = if (page < (pages.size - 1)) pages[page + 1] else CommandMapping.getCommands().size

            if (page < 0) {
                reject(player, "Usage: ::commands <lt>page<gt>")
            }

            if (page > pages.size) {
                reject(player, "That page number is too high, you don't have access to that many.")
            }

            val title = "Commands" + if (pages.size > 1) " (${page + 1}/${pages.size})" else ""
            var lineId = setupScrollInterface(player, title)

            for(i in pages[page] until end) {
                val command = CommandMapping.getCommands()[i]
                lineId = displayCommandInScroll(player, command, player.rights.ordinal, lineId)

                if (lineId > 306) {
                    player.packetDispatch.sendString("To view the next page, use ::commands ${page + 2}", Components.QUESTJOURNAL_SCROLL_275, lineId)
                    break
                }
            }
            player.interfaceManager.open(Component(Components.QUESTJOURNAL_SCROLL_275))
        }

        /*
         * Search for commands
         */
        define("commandsearch", Privilege.STANDARD, "::commandsearch <lt>search term<gt> [--chat]", "Searches for commands containing the given term."){player, args ->
            if (args.size < 2) {
                reject(player, "Usage: ::commandsearch <search term> [--chat]")
                return@define
            }
            val rawArguments = args.copyOfRange(1, args.size)
            val chatMode = rawArguments.any { it.equals("--chat", ignoreCase = true) }
            val searchRaw = rawArguments.filterNot { it.equals("--chat", ignoreCase = true) }
            if (searchRaw.isEmpty()) {
                reject(player, "Usage: ::commandsearch <search term> [--chat]")
                return@define
            }
            val search = searchRaw.joinToString(" ").lowercase()
            if (search.length < 2) {
                reject(player, "Search term must be at least 2 characters long")
                return@define
            }
            val playerRights = player.rights.ordinal
            val allMatchingCommands = CommandMapping.getCommands()
                .filter { command ->
                    command.name.lowercase().contains(search) ||
                    command.usage.lowercase().contains(search) ||
                    command.description.lowercase().contains(search)
                }
            if (allMatchingCommands.isEmpty()) {
                notify(player, "No commands found matching '$search'")
                return@define
            }
            val accessibleCommands = allMatchingCommands
                .filter { it.privilege.ordinal <= playerRights }
                .sortedBy { it.name }
            val inaccessibleCount = allMatchingCommands.count { it.privilege.ordinal > playerRights }
            if (chatMode) {
                for (command in accessibleCommands) {
                    val cmdargs = if (command.usage.isNotEmpty()) {
                        val parts = command.usage.split(" ", limit = 2)
                        if (parts.size > 1) parts[1] else ""
                    } else {
                        ""
                    }
                    val formattedMessage = if (cmdargs.isNotEmpty()) {
                        "CMD: ${command.name} ARGS: $cmdargs DESC: ${command.description}"
                    } else {
                        "CMD: ${command.name} DESC: ${command.description}"
                    }
                    val chunks = chunkText(formattedMessage, 70)
                    chunks.forEach { notify(player, it) }
                }
                if (accessibleCommands.isEmpty()) {
                    notify(player, "Found $inaccessibleCount command(s) matching '$search' but you don't have permission to use any of them")
                } else {
                    val summary = "Found ${accessibleCommands.size} accessible command(s) for '$search'"
                    if (inaccessibleCount > 0) {
                        notify(player, summary)
                        notify(player, "($inaccessibleCount additional command(s) require higher privileges)")
                    } else {
                        notify(player, summary)
                    }
                }
                return@define
            }
            var lineId = setupScrollInterface(player, "Command Search: '$search'")
            if (accessibleCommands.isEmpty()) {
                player.packetDispatch.sendString("No accessible commands found.", Components.QUESTJOURNAL_SCROLL_275, lineId++)
            }
            for (command in accessibleCommands) {
                lineId = displayCommandInScroll(player, command, playerRights, lineId)
                if (lineId > 306) {
                    player.packetDispatch.sendString(
                        "Results truncated. Use ::commandsearch $search --chat",
                        Components.QUESTJOURNAL_SCROLL_275,
                        lineId
                    )
                    break
                }
            }
            if (inaccessibleCount > 0 && lineId <= 306) {
                player.packetDispatch.sendString(
                    "$inaccessibleCount more command(s) need higher privileges.",
                    Components.QUESTJOURNAL_SCROLL_275,
                    lineId
                )
            }
            player.interfaceManager.open(Component(Components.QUESTJOURNAL_SCROLL_275))
        }

        /**
         * Reply to PMs (also enables tab-to-reply)
         */
        define("reply", Privilege.STANDARD, "", "Opens a reply prompt to your last DM. Same as pressing tab."){ player, _ ->
            if(player.interfaceManager.isOpened){
                reject(player, "<col=e74c3c>Please finish what you're doing first.")
            }
            if (player.attributes.containsKey("replyTo")) {
                player.setAttribute("keepDialogueAlive", true)
                val replyTo = player.getAttribute("replyTo", "").replace("_".toRegex(), " ")
                sendInputDialogue(player, InputType.MESSAGE ,StringUtils.formatDisplayName(replyTo)){ value ->
                    CommunicationInfo.sendMessage(player, replyTo.toLowerCase(), value as String)
                    player.removeAttribute("keepDialogueAlive")
                }
            } else {
                reject(player, "<col=3498db>You have not recieved any recent messages to which you can reply.")
            }
        }

        /**
         * Enables client to safety close the currently opened interface or dialogue (esc-to-close plugin)
         */
        define("xface", Privilege.STANDARD, "", "Closes the currently opened interface/dialogue."){player, _ ->
            player.interfaceManager.close()
            player.dialogueInterpreter.close()
        }

        define("completediaries", Privilege.ADMIN, "", "Completes all diaries."){player,_ ->
            player.achievementDiaryManager.diarys.forEach {
                for(level in it.taskCompleted.indices){
                    for(task in it.taskCompleted[level].indices){
                        it.finishTask(player,level,task)
                    }
                }
            }
        }

        /**
         * Permadeaths a player, resetting their save
         */
        define("permadeath", Privilege.ADMIN, "::permadeath <lt>PLAYER<gt>", "Permadeaths PLAYER (self if omitted), completely wiping their save."){player,args ->
            var target = player
            if (args.size > 1) {
                val n = args.slice(1 until args.size).joinToString("_")
                val foundtarget = Repository.getPlayerByName(n)
                if (foundtarget == null) {
                    reject(player, "Invalid player \"${n}\" or player not online")
                }
                target = foundtarget!!
            }
            permadeath(target)
        }

        define("log", Privilege.ADMIN, description = "Adds your current coordinates to the location log buffer."){player,_ ->
            var log: ArrayList<String>? = player.getAttribute("loc-log")
            log = log ?: ArrayList<String>()
            val locString = "{${player.location.x},${player.location.y},${player.location.z},1,0}"
            log.add(locString)
            player.setAttribute("loc-log",log)
        }

        define("logdone", description = "Copies the queued location log to your clipboard and clears it."){player,_ ->
            val log: ArrayList<String>? = player.getAttribute("loc-log")
            log ?: return@define

            val sb = StringBuilder()
            var first = true
            for(entry in log){
                if(!first) sb.append("-")
                sb.append(entry)
                first = false
            }

            val stringSelection = StringSelection(sb.toString())
            val clpbrd = Toolkit.getDefaultToolkit().systemClipboard
            clpbrd.setContents(stringSelection, null)

            log.clear()
            player.setAttribute("loc-log",log)
        }

        define("rolltrawlerloot", Privilege.ADMIN, "::rolltrawlerloot <lt>ROLL COUNT<gt>", "Rolls some trawler loot."){player,args ->
            val rolls = if(args.size < 2){
                100
            } else {
                args[1].toString().toInt()
            }
            TrawlerLoot.addLootAndMessage(player, player.skills.getLevel(Skills.FISHING), rolls, false)
        }

        define("fillbank", Privilege.ADMIN, description = "Fills your bank with sequential item IDs."){player,_ ->
            for(i in 0 until ServerConstants.BANK_SIZE){
                player.bank.add(Item(i))
            }
        }

        define("emptybank", Privilege.ADMIN, description = "Clears your bank contents."){player,_ ->
            player.bank.clear()
            player.bank.update()
        }

        define("setconfig", Privilege.ADMIN, "::setconfig <lt>config-id<gt> <lt>value<gt>", "DEPRECATED: use ::setvarp or ::setvarbit. Sets the raw varp <lt>config-id<gt> to <lt>value<gt>."){player,args ->
            if(args.size < 3){
                reject(player,"Syntax: ::setconfig configID value")
            }
            val configID = args[1].toString().toInt()
            val configValue = args[2].toString().toInt()
            setVarp(player, configID, configValue)
        }

        define("getobjectvarp", usage = "::getobjectvarp <lt>object-id<gt>", description = "Looks up which varp controls the state of <lt>object-id<gt>."){player,args ->
            if(args.size < 2){
                reject(player,"Syntax: ::getobjectvarp objectid")
            }
            val objectID = args[1].toInt()
            notify(player, "${VarbitDefinition.forObjectID(SceneryDefinition.forId(objectID).varbitID).varpId}")
        }

        define("define_varbit", Privilege.ADMIN, "::define_varbit <varbit-id>", "Prints detailed information about <varbit-id>."){ player, args ->
            if(args.size < 2) {
                reject(player, "Syntax: ::define_varbit varbitId")
            }
            val varbitID = args[1].toInt()
            notify(player, "${varbitID}: ${VarbitDefinition.forId(varbitID)}")
        }


        define("setvarbit", Privilege.ADMIN, "::setvarbit <lt>varbit-id<gt> <lt>value<gt>", "Sets <lt>varbit-id<gt> to <lt>value<gt> for your player."){
            player,args ->
            if(args.size != 3){
                reject(player,"Usage: ::setvarbit <varbit-id> <value>")
            }
            val index = args[1].toIntOrNull()
            val value = args[2].toIntOrNull()

            if(index == null || value == null){
                reject(player,"Usage ::setvarbit <varbit-id> <value>")
            }

            setVarbit(player, index!!, value!!)
        }

        define("setvarbits", Privilege.ADMIN, "::setvarbits <lt>FROM VARBIT ID<gt> <lt>TO VARBIT ID<gt> <lt>VALUE<gt>", ""){
            player,args ->
            if(args.size != 4){
                reject(player,"Usage: ::setvarbits <fromvarbit> <tovarbit> <value>")
            }
            val fromIndex = args[1].toIntOrNull()!!
            val toIndex = args[2].toIntOrNull()!!
            val value = args[3].toIntOrNull()!!

            for (index in fromIndex..toIndex) {
                setVarbit(player, index, value)
            }
        }

        define("getvarbit", Privilege.ADMIN, "::getvarbit <lt>VARBIT ID<gt>", "Prints the current value of <lt>VARBIT ID<gt>.") {
            player, args ->
            if (args.size != 2)
                reject(player, "Usage: ::getvarbit <id>")
            val index = args[1].toIntOrNull() ?: return@define
            notify(player, "Varbit $index: Currently ${getVarbit(player, index)}")
        }

        define("setvarp", Privilege.ADMIN, "::setvarp <lt>VARP ID<gt> <lt>BIT OFFSET<gt> <lt>VALUE<gt>", "Sets the value starting at the BIT OFFSET of the varp."){
                player,args ->
            if(args.size < 4){
                reject(player,"Usage: ::setvarp index offset value")
            }
            val index = args[1].toIntOrNull()
            val offset = args[2].toIntOrNull()
            val value = args[3].toIntOrNull()

            if(index == null || offset == null || value == null){
                reject(player,"Usage ::setvarp index offset value")
            }

            setVarp(player, index!!, value!! shl offset!!)
        }

        define("setvarc", Privilege.ADMIN, "::setvarc <lt>VARC ID<gt> <lt>VALUE<gt>") { player, args ->
            if(args.size < 3){
                reject(player,"Usage: ::setvarc index value")
            }
            val index = args[1].toShortOrNull()
            val value = args[2].toIntOrNull()

            if(index == null || value == null) {
                reject(player,"Usage ::setvarc index value")
            }

            player.packetDispatch.sendVarcUpdate(index!!, value!!)
        }

        define("grow", Privilege.ADMIN, "", "Grows all planted crops by 1 stage."){ player, _ ->
            val state = getOrStartTimer <CropGrowth> (player)!!

            for(patch in state.getPatches()){
                patch.nextGrowth = System.currentTimeMillis() - 1
            }

            state.run (player)
        }

        define("finishbins", Privilege.ADMIN, "", "Finishes any in-progress compost bins."){ player, _ ->
            val bins = getOrStartTimer<Compost>(player).getBins()
            for (bin in bins) {
                if (!bin.isFinished && bin.isClosed) bin.finish()
            }
        }

        define("resetbins", Privilege.ADMIN, "", "Resets the player's compost bins to their initial states."){ player, _ ->
            val bins = getOrStartTimer<Compost>(player).getBins()
            for (bin in bins) bin.reset()
        }

        define("diseasecrops", Privilege.ADMIN, "", "Disease all crops"){ player, _ ->
            val state = getOrStartTimer<CropGrowth>(player)
            for (patch in state.getPatches()){
                patch.diseaseMod = -128
                patch.nextGrowth = System.currentTimeMillis() + 1
            }
            state.run(player)
        }

        define("addcredits", Privilege.ADMIN, description = "Adds 100 ::shop credits to your account."){ player, _ ->
            player.details.credits += 100
        }

        define("getnpcparent", usage = "::getnpcparent <lt>npc-id<gt>", description = "Finds the parent NPC whose child list includes <lt>npc-id<gt>."){player,args ->
            if(args.size < 2){
                reject(player,"Usage: ::getnpcparent npcid")
            }

            val npcid = args[1].toIntOrNull() ?: reject(player,"Invalid NPC ID.")

            GlobalScope.launch {
                for(def in NPCDefinition.getDefinitions().values){
                    def ?: continue
                    def.childNPCIds ?: continue
                    for(id in def.childNPCIds){
                        if(id == npcid){
                            notify(player,"Parent NPC: ${def.id}")
                            return@launch
                        }
                    }
                }
                notify(player,"No parent NPC found.")
            }
        }
        define("infinitespecial", Privilege.ADMIN, "::infinitespecial <true|false>", "Turns the infinite special-attack flag on or off for your player."){ player, args ->
            val usageStr = "Usage: ::infinitespecial true|false"
            if(args.size < 2){
                reject(player, usageStr)
            }
            when(args[1]){
                "true" -> player.setAttribute("infinite-special", true)
                "false" -> player.removeAttribute("infinite-special")
                else -> reject(player, usageStr)
            }
        }
        define("allowaggro", Privilege.ADMIN, "allowaggro true | false", "Toggle NPCs aggroing on you") { player, args ->
            val usageStr = "Usage: ::allowaggro true | false"
            if(args.size < 2) {
                notify(player, "Allow admin aggression is currently ${player.getAttribute("/save:allow_admin_aggression", false)}")
                return@define
            }
            when(args[1]) {
                "true" -> player.setAttribute("/save:allow_admin_aggression", true)
                "false" -> player.setAttribute("/save:allow_admin_aggression", false)
                else -> reject(player, usageStr)
            }
            notify(player, "Setting aggro ${args[1]}")
        }

        define("rules", Privilege.STANDARD, "", "Shows the rules."){ player, _ ->
            RulesAndInfo.setBaseRulesAndInfo(player)
            player.packetDispatch.sendInterfaceConfig(384, 17, true)
            openInterface(player, 384)
        }

        define("confirmrules", Privilege.STANDARD) { player, args ->
            if(getAttribute(player,"rules:confirmed", false))
                reject(player, "You have already confirmed the rules.")
            if(args.size < 2)
                reject(player, "Usage: ::confirmrules PIN")
            val pin = args[1].toIntOrNull() ?: (-1).also{ reject(player, "Please enter a valid number.") }
            if(pin == getAttribute(player, "rules:pin", -1))
            {
                player.setAttribute("/save:rules:confirmed", true)
                player.interfaceManager.close()
                sendDialogue(player, "Thank you!")
                player.unlock()
                player.removeAttribute("rules:pin")
                if(ServerConstants.NEW_PLAYER_ANNOUNCEMENT) sendNews("A new player has joined. Welcome ${player.username}!")
            }
            else
            {
                sendDialogue(player, "Wrong pin. Try again.")
            }
        }

        define("setplayerstrong", Privilege.ADMIN, "setplayerstrong <lt>0-4<gt>",
            "Set the player progress through the Stronghold of Player Safety test."){player, args ->
            /*
             * 0 = Not started
             * 1 = Received test
             * 2 = Completed test, needs to be marked
             * 3 = Test marked
             * 4 = Dungeon cleared
             */
            if(args.size < 2){
                notify(player, "Player Stronghold progression currently: ${player.savedData.globalData.testStage}")
                return@define
            }
            val stage = args[1].toIntOrNull() ?: (-1).also { reject(player, "Please enter a valid number") }
            if (stage in 0 .. 4){
                player.savedData.globalData.testStage = stage
                notify(player, "Setting test stage to $stage")
                if (stage in 0..2){
                    setVarp(player, 1203, 0, true)
                    notify(player, "The poster passage is now hidden")
                }
                else{
                    setVarp(player, 1203, 1 shl 29, true)
                    notify(player, "The poster passage is now usable")
                }
                if (stage == 4){
                    setVarbit(player, 4499, 1,true)
                    notify(player, "The loot has been taken already")
                }
                else {
                    setVarbit(player, 4499, 0, true)
                    notify(player, "The loot can be reacquired")
                }
            }
            else{
                reject(player, "Only stages 0-4 are valid")
            }
        }

        define("setplaqueread", Privilege.ADMIN, "setplaqueread <lt>true/false<gt>",
            "Set the plaques in the player safety stronghold to read or not read."){player, args ->
            if (args.size == 1) {
                notify(
                    player,
                    "Currently the plaques ${if (player.savedData.globalData.hasReadPlaques()) "have" else "have not"} been read"
                )
                return@define
            }
            when(args[1]) {
                "true" -> setPlaqueReadStatus(player, true)
                "false" -> setPlaqueReadStatus(player, false)
                else -> reject(player, "Only true or false can be used")

            }
            notify(player, "Setting plaques read to: ${args[1]}")

        }
    }

    fun setupScrollInterface(player: Player, title: String): Int {
        player.interfaceManager.close()
        for (i in 0..310) {
            player.packetDispatch.sendString("", Components.QUESTJOURNAL_SCROLL_275, i)
        }
        player.packetDispatch.sendString(title, Components.QUESTJOURNAL_SCROLL_275, 2)
        return 11
    }

    fun displayCommandInScroll(player: Player, command: Command, playerRights: Int, lineId: Int): Int {
        val privilegeLevel = command.privilege.ordinal
        if (privilegeLevel > playerRights) return lineId
        val titleIcon = privilegeLevel - 1
        var title = command.name
        if (privilegeLevel > 0) {
            title = "(<img=$titleIcon>) $title"
        }
        var currentLineId = lineId
        player.packetDispatch.sendString(title, Components.QUESTJOURNAL_SCROLL_275, currentLineId++)
        if (command.usage.isNotEmpty()) {
            val usageText = "Usage: ${command.usage}"
            val usageChunks = chunkText(usageText, 70)
            for (chunk in usageChunks) {
                player.packetDispatch.sendString(chunk, Components.QUESTJOURNAL_SCROLL_275, currentLineId++)
            }
        }
        if (command.description.isNotEmpty()) {
            val descChunks = chunkText(command.description, 70)
            for (chunk in descChunks) {
                player.packetDispatch.sendString(chunk, Components.QUESTJOURNAL_SCROLL_275, currentLineId++)
            }
        }
        player.packetDispatch.sendString("<str>-------------------------------</str>", Components.QUESTJOURNAL_SCROLL_275, currentLineId++)
        return currentLineId
    }

    fun chunkText(text: String, maxLength: Int = 70): List<String> {
        if (text.length <= maxLength) return listOf(text)
        val chunks = mutableListOf<String>()
        var remaining = text
        while (remaining.length > maxLength) {
            var breakPoint = maxLength
            for (i in maxLength downTo 1) {
                if (remaining[i - 1] == ' ') {
                    breakPoint = i
                    break
                }
            }
            chunks.add(remaining.take(breakPoint).trim())
            remaining = remaining.substring(breakPoint).trim()
        }
        if (remaining.isNotEmpty()) {
            chunks.add(remaining)
        }
        return chunks
    }

    fun setPlaqueReadStatus(player: Player, status: Boolean){
        // For some reason the loop has to be this way to have read write access
        for (i in 0 until player.savedData.globalData.readPlaques.size){
            player.savedData.globalData.readPlaques[i] = status
        }

    }

    fun showGeBotsearch(player: Player, searchTerm: String)
    {
        val offerAmounts = HashMap<Int,Int>()
        val offerPrice = HashMap<Int,Int>()

        val offers = GrandExchange.getBotOffers().filter { getItemName(it.itemID).contains(searchTerm, true) || getItemName(it.itemID).equals(searchTerm, true) }

        for(offer in offers)
        {
            offerAmounts[offer.itemID] = offer.amount
            offerPrice[offer.itemID] = offer.offeredValue
        }

        val entries    = offerAmounts.entries.sortedBy({ e -> getItemName(e.key) })
        val leftLines  = ArrayList<String>(entries.size)
        val rightLines = ArrayList<String>(entries.size)
        for (entry in entries) {
            leftLines.add("${getItemName(entry.key)} (<col=357f44>x${entry.value}</col>)")
            rightLines.add("Price: <col=3a3414>${offerPrice[entry.key]}</col>gp")
        }
        showGeBook(player, "Bot Stock - \"$searchTerm\"", leftLines, rightLines)
    }

    fun showGeBots(player: Player)
    {
        val offerAmounts = HashMap<Int,Int>()
        val offerPrice = HashMap<Int,Int>()

        val offers = GrandExchange.getBotOffers()

        for(offer in offers)
        {
            offerAmounts[offer.itemID] = offer.amount
            offerPrice[offer.itemID] = offer.offeredValue
        }

        val entries    = offerAmounts.entries.sortedBy({ e -> getItemName(e.key) })
        val leftLines  = ArrayList<String>(entries.size)
        val rightLines = ArrayList<String>(entries.size)
        for (entry in entries) {
            leftLines.add("${getItemName(entry.key)} (<col=357f44>x${entry.value}</col>)")
            rightLines.add("Price: <col=3a3414>${offerPrice[entry.key]}</col>gp")
        }
        showGeBook(player, "Bot Stock", leftLines, rightLines)
    }

    fun showGeSell(player: Player){
        val offerAmounts = HashMap<Int,Int>()
        val lowestPrice = HashMap<Int,Int>()

        val offers = GrandExchange.getValidOffers()

        for(offer in offers)
        {
            if(!offer.sell) continue
            var amount = offerAmounts[offer.itemID] ?: 0
            amount += offer.amountLeft

            var price = lowestPrice[offer.itemID] ?: Integer.MAX_VALUE
            if(offer.offeredValue < price) price = offer.offeredValue

            offerAmounts[offer.itemID] = amount
            lowestPrice[offer.itemID] = price
        }

        val entries    = offerAmounts.entries.sortedBy({ e -> getItemName(e.key) })
        val leftLines  = ArrayList<String>(entries.size)
        val rightLines = ArrayList<String>(entries.size)
        for (entry in entries) {
            leftLines.add("${getItemName(entry.key)} (<col=357f44>x${entry.value}</col>)")
            rightLines.add("Price: <col=3a3414>${lowestPrice[entry.key]}</col>gp")
        }
        showGeBook(player, "Active Sell Offers", leftLines, rightLines)
    }

    fun showGeBuy(player: Player){
        val offerAmounts = HashMap<Int,Int>()
        val highestPrice = HashMap<Int,Int>()

        val offers = GrandExchange.getValidOffers()

        for(offer in offers)
        {
            if(offer.sell) continue
            var amount = offerAmounts[offer.itemID] ?: 0
            amount += offer.amountLeft

            var price = highestPrice[offer.itemID] ?: 0
            if(offer.offeredValue > price) price = offer.offeredValue

            offerAmounts[offer.itemID] = amount
            highestPrice[offer.itemID] = price
        }

        val entries    = offerAmounts.entries.sortedBy({ e -> getItemName(e.key) })
        val leftLines  = ArrayList<String>(entries.size)
        val rightLines = ArrayList<String>(entries.size)
        for (entry in entries) {
            leftLines.add("${getItemName(entry.key)} (<col=357f44>x${entry.value}</col>)")
            rightLines.add("Highest: <col=3a3414>${highestPrice[entry.key]}</col>gp")
        }
        showGeBook(player, "Active Buy Offers", leftLines, rightLines)
    }

    fun showOffers(player: Player, searchTerm: String){
        val offers = GrandExchange.getValidOffers().filter { getItemName(it.itemID).contains(searchTerm, true) || getItemName(it.itemID).equals(searchTerm, true) }
        if (offers.isEmpty()) {
            sendMessage(player, "No results.")
            return
        }

        val buyingAmount  = HashMap<Int, Int>()
        val buyingHighest = HashMap<Int, Int>()
        val sellingAmount = HashMap<Int,Int>()
        val sellingLowest = HashMap<Int,Int>()
        for(offer in offers)
        {
            if(offer.sell)
            {
                var price = sellingLowest[offer.itemID] ?: Int.MAX_VALUE
                if(offer.offeredValue < price) price = offer.offeredValue

                var amount = sellingAmount[offer.itemID] ?: 0
                amount += offer.amountLeft

                sellingAmount[offer.itemID] = amount
                sellingLowest[offer.itemID] = price
            }
            else
            {
                var price = buyingHighest[offer.itemID] ?: 0
                if(offer.offeredValue > price) price = offer.offeredValue

                var amount = buyingAmount[offer.itemID] ?: 0
                amount += offer.amountLeft

                buyingAmount[offer.itemID] = amount
                buyingHighest[offer.itemID] = price
            }
        }

        val numLines   = offers.size + 1
        val leftLines  = ArrayList<String>(numLines)
        val rightLines = ArrayList<String>(numLines)
        for (i in 0..numLines) {
            if (i < buyingAmount.keys.size) {
                val offer = buyingAmount.entries.elementAtOrNull(i) ?: continue
                leftLines.add("[BUYING] ${getItemName(offer.key)} (<col=357f44>x${offer.value}</col>)")
                rightLines.add("Highest: <col=3a3414>${buyingHighest[offer.key]}</col>gp")
            } else if (i == buyingAmount.keys.size) {
                leftLines.add("")
                rightLines.add("")
            } else {
                val offer = sellingAmount.entries.elementAtOrNull(i - buyingAmount.keys.size - 1) ?: continue
                leftLines.add("[SELLING] ${getItemName(offer.key)} (<col=357f44>x${offer.value}</col>)")
                rightLines.add("Lowest: <col=3a3414>${sellingLowest[offer.key]}</col>gp")
            }
        }
        showGeBook(player, "Results for \"$searchTerm\"", leftLines, rightLines)
    }

    private fun showGeInputDialogue(player: Player, args: Array<String>, op: (Player, String) -> (Unit)) {
        if (args.size > 2) {
            val target = args.copyOfRange(2, args.size).joinToString(" ").lowercase()
            op(player, target)
        } else {
            sendInputDialogue(player, InputType.STRING_LONG, "Enter search term:",) { value ->
                op(player, value as String)
            }
        }
    }

    private fun showGeBook(player: Player, title: String, leftLines: ArrayList<String>, rightLines: ArrayList<String>) {
        if (leftLines.size == 0) {
            sendMessage(player, "No results.")
            return
        }
        val lineIds  = BookInterface.FANCY_BOOK_26_LINE_IDS
        val contents = ArrayList<PageSet>()
        val leftChunks  = leftLines.chunked(15)
        val rightChunks = rightLines.chunked(15)
        for (i in leftChunks.indices) {
            val size = leftChunks[i].size
            val leftPageLines  = ArrayList<BookLine>(size)
            val rightPageLines = ArrayList<BookLine>(size)
            for (j in leftChunks[i].indices) {
                leftPageLines.add(BookLine(leftChunks[i][j], lineIds[j+3])) //+3 to skip title and buttons
                rightPageLines.add(BookLine(rightChunks[i][j], lineIds[j+3+15])) //+15 because right pages have different ids
            }
            val leftPage  = Page(*leftPageLines.toTypedArray())
            val rightPage = Page(*rightPageLines.toTypedArray())
            contents.add(PageSet(leftPage, rightPage))
        }

        closeInterface(player)
        fun display(player: Player, pageNum: Int, buttonID: Int): Boolean {
            BookInterface.pageSetup(player, BookInterface.FANCY_BOOK_26, title, contents.toTypedArray())
            return true
        }
        BookInterface.openBook(player, BookInterface.FANCY_BOOK_26, ::display)
    }
}
