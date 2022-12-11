package rs09.game.system.command.sets

import api.*
import api.InputType
import core.cache.def.impl.NPCDefinition
import core.cache.def.impl.SceneryDefinition
import core.cache.def.impl.VarbitDefinition
import core.game.component.Component
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.info.Rights
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.system.communication.CommunicationInfo
import core.game.world.map.RegionManager
import core.game.world.map.build.DynamicRegion
import core.plugin.Initializable
import core.tools.StringUtils
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.rs09.consts.Components
import rs09.ServerConstants
import rs09.game.ai.AIPlayer
import rs09.game.ai.AIRepository
import rs09.game.camerautils.PlayerCamera
import rs09.game.content.activity.fishingtrawler.TrawlerLoot
import rs09.game.content.ame.RandomEventManager
import rs09.game.content.ame.RandomEvents
import rs09.game.content.quest.members.anma.AnmaCutscene
import rs09.game.ge.GrandExchange
import rs09.game.interaction.inter.RulesAndInfo
import rs09.game.node.entity.state.newsys.states.FarmingState
import rs09.game.system.SystemLogger
import rs09.game.system.command.CommandMapping
import rs09.game.system.command.Privilege
import rs09.game.world.repository.Repository
import rs09.tools.stringtools.colorize
import java.awt.HeadlessException
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection
import java.util.*

@Initializable
class MiscCommandSet : CommandSet(Privilege.ADMIN){
    override fun defineCommands() {

        /**
         * Toggles debug mode
         */
        define("debug", Privilege.STANDARD, "", "Toggles debug mode."){ player, _ ->
            player.toggleDebug()
        }

        define("calc_accuracy", Privilege.STANDARD, "::calc_accuracy <lt>NPC ID<gt>", "Calculates and prints your current chance to hit a given NPC."){ player, args ->
            val handler = player.getSwingHandler(false)
            player.sendMessage("handler type: ${handler.type}")
            player.sendMessage("calculateAccuracy: ${handler.calculateAccuracy(player)}")

            if (args.size > 1)
            {
                val npcId: Int = args[1].toInt()
                val npc = NPC(npcId)
                npc.initConfig()
                player.sendMessage("npc: ${npc.name}. npc defence: ${npc.skills.getLevel(Skills.DEFENCE)}")
                player.sendMessage("calculateDefence: ${handler.calculateDefence(npc, player)}")
            }
        }

        define("movcam", Privilege.ADMIN, "::movcam <lt>Region X<gt> <lt>Region Y<gt> [<lt>Height<gt> <lt>Speed<gt>]", "Moves the camera to the given region-local coordinates.") {player, args ->
            val regionX = args[1].toIntOrNull() ?: return@define
            val regionY = args[2].toIntOrNull() ?: return@define
            var height = 300
            var speed = 100

            if (args.size > 3)
                height = args[3].toIntOrNull() ?: return@define

            if (args.size > 4)
                speed = args[4].toIntOrNull() ?: return@define

            val region = RegionManager.forId(player.location.regionId)
            val base = region.baseLocation

            val globalLoc = base.transform(regionX, regionY, 0)
            PlayerCamera(player).panTo(globalLoc.x, globalLoc.y, height, speed)
        }

        define("rotcam", Privilege.ADMIN, "::rotcam <lt>Region X<gt> <lt>Region Y<gt> [<lt>Height<gt> <lt>Speed<gt>]", "Rotates the camera to face the given region-local coordinates.") {player, args ->
            val regionX = args[1].toIntOrNull() ?: return@define
            val regionY = args[2].toIntOrNull() ?: return@define
            var height = 300
            var speed = 100

            if (args.size > 3)
                height = args[3].toIntOrNull() ?: return@define

            if (args.size > 4)
                speed = args[4].toIntOrNull() ?: return@define

            val region = RegionManager.forId(player.location.regionId)
            val base = region.baseLocation

            val globalLoc = base.transform(regionX, regionY, 0)
            PlayerCamera(player).rotateTo(globalLoc.x, globalLoc.y, height, speed)
        }

        define("anmacs", Privilege.ADMIN) { player, _ ->
            AnmaCutscene(player).start()
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
            SystemLogger.logInfo(this::class.java, "Viewport: " + l.getSceneX(player.playerFlags.lastSceneGraph) + "," + l.getSceneY(player.playerFlags.lastSceneGraph))
            val loc = "Location.create(" + l.x + ", " + l.y + ", " + l.z + ")"
            SystemLogger.logInfo(this::class.java, loc + "; " + player.playerFlags.lastSceneGraph + ", " + l.localX + ", " + l.localY)
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

        define("calcmaxhit", Privilege.STANDARD, "", "Calculates and shows you your current max hit.") { player, _ ->
            val swingHandler = player.getSwingHandler(false)
            val hit = swingHandler.calculateHit(player, player, 1.0)
            notify(player, "max hit (${(swingHandler as Object).getClass().getName()}): ${hit}")
        }

        /**
         * Empty a player's inventory
         * ADMIN only (for obvious reasons)
         */
        define("empty", Privilege.ADMIN, "", "Empties your inventory."){player,_->
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
            player.interfaceManager.open(Component(Components.CREDIT_SHOP))
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
                "search" -> sendInputDialogue(player, InputType.STRING_LONG, "Enter search term:"){value ->
                    showOffers(player, value as String)
                }
                "bots" -> showGeBots(player)
                "botsearch" -> sendInputDialogue(player, InputType.STRING_LONG, "Enter search term:"){value ->
                    showGeBotsearch(player, value as String)
                }
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
            var lineid = 11
            var pages = CommandMapping.getPageIndices(player.rights.ordinal)
            var end = if (page < (pages.size - 1)) pages[page + 1] else CommandMapping.getCommands().size

            player.interfaceManager.close()

            if (page < 0) {
                reject(player, "Usage: ::commands <lt>page<gt>")
            }

            for (i in 0..310) {
                player.packetDispatch.sendString("", Components.QUESTJOURNAL_SCROLL_275, i)
            }

            player.packetDispatch.sendString(
                "Commands" + if (pages.size > 1) " (${page + 1}/${pages.size})" else "", 
                Components.QUESTJOURNAL_SCROLL_275,
                2
            )

            
            for(i in pages[page] until end) {
                var command = CommandMapping.getCommands()[i]
                var title = "${command.name}"
                var rights = command.privilege.ordinal
                var icon = rights - 1

    
                if (rights > player.rights.ordinal) continue

                if (rights > 0)
                    title = "(<img=$icon>) $title"

                player.packetDispatch.sendString(title, Components.QUESTJOURNAL_SCROLL_275, lineid++)

                if (command.usage.isNotEmpty())
                    player.packetDispatch.sendString("Usage: ${command.usage}", Components.QUESTJOURNAL_SCROLL_275, lineid++)

                if (command.description.isNotEmpty())
                    player.packetDispatch.sendString(command.description, Components.QUESTJOURNAL_SCROLL_275, lineid++)
                
                player.packetDispatch.sendString("<str>-------------------------------</str>", Components.QUESTJOURNAL_SCROLL_275, lineid++)

                if (lineid > 306) {
                    player.packetDispatch.sendString("To view the next page, use ::commands ${page + 2}", Components.QUESTJOURNAL_SCROLL_275, lineid)
                    break
                }

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
         * Max account stats
         */
        define("max", Privilege.ADMIN, "", "Gives you all 99s."){player,_ ->
            var index = 0
            Skills.SKILL_NAME.forEach {
                player.skills.setStaticLevel(index,99)
                player.skills.setLevel(index,99)
                index++
            }
            player.skills.updateCombatLevel()
        }

        define("noobme", Privilege.ADMIN, "", "Sets you back to default stats."){ player,_ ->
            var index = 0
            Skills.SKILL_NAME.forEach {
                if (index == Skills.HITPOINTS) {
                    player.skills.setStaticLevel(index,10)
                    player.skills.setLevel(index,10)
                    index++
                } else {
                    player.skills.setStaticLevel(index,1)
                    player.skills.setLevel(index,1)
                    index++
                }
            }
            player.skills.updateCombatLevel()
        }

        /**
         * Set a specific skill to a specific level
         */
        define("setlevel", Privilege.ADMIN, "::setlevel <lt>SKILL NAME<gt> <lt>LEVEL<gt>", "Sets SKILL NAME to LEVEL."){player,args ->
            if(args.size < 2) reject(player,"Usage: ::setlevel skillname level")
            val skillname = args[1]
            val desiredLevel: Int? = args[2].toIntOrNull()
            if(desiredLevel == null){
                reject(player, "Level must be an integer.")
            }
            if(desiredLevel!! > 99) reject(player,"Level must be 99 or lower.")
            val skill = Skills.getSkillByName(skillname)

            if(skill < 0) reject(player, "Must use a valid skill name!")

            player.skills.setStaticLevel(skill,desiredLevel)
            player.skills.setLevel(skill,desiredLevel)
            player.skills.updateCombatLevel()
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

        define("log", Privilege.ADMIN){player,_ ->
            var log: ArrayList<String>? = player.getAttribute("loc-log")
            log = log ?: ArrayList<String>()
            val locString = "{${player.location.x},${player.location.y},${player.location.z},1,0}"
            log.add(locString)
            player.setAttribute("loc-log",log)
        }

        define("logdone"){player,_ ->
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
            player.bank.add(*TrawlerLoot.getLoot(player.skills.getLevel(Skills.FISHING), rolls, false).toTypedArray())
        }

        define("fillbank", Privilege.ADMIN, "", "Right as it says on the tin."){player,_ ->
            for(i in 0 until ServerConstants.BANK_SIZE){
                player.bank.add(Item(i))
            }
        }

        define("emptybank", Privilege.ADMIN, "", "Right as it says on the tin."){player,_ ->
            player.bank.clear()
            player.bank.update()
        }

        define("setconfig", Privilege.ADMIN, "", "DEPRECATED: Use setvarp or setvarbit."){player,args ->
            if(args.size < 3){
                reject(player,"Syntax: ::setconfig configID value")
            }
            val configID = args[1].toString().toInt()
            val configValue = args[2].toString().toInt()
            player.configManager.forceSet(configID,configValue,false)
        }

        define("getobjectvarp"){player,args ->
            if(args.size < 2){
                reject(player,"Syntax: ::getobjectvarp objectid")
            }
            val objectID = args[1].toInt()
            notify(player, "${VarbitDefinition.forObjectID(SceneryDefinition.forId(objectID).varbitID).varpId}")
        }

        define("define_varbit", Privilege.ADMIN, "::define_varbit <lt>VARBIT ID<gt>", "Prints information about the given varbit."){ player, args ->
            if(args.size < 2) {
                reject(player, "Syntax: ::define_varbit varbitId")
            }
            val varbitID = args[1].toInt()
            notify(player, "${varbitID}: ${VarbitDefinition.forId(varbitID)}")
        }
        define("togglexp", Privilege.STANDARD){ player, args ->
            val enabled = player.varpManager.get(2501).getVarbit(0) == 1
            player.varpManager.get(2501).setVarbit(0,if(enabled) 0 else 1).send(player)
            notify(player, "XP drops are now " + colorize("%R" + if(!enabled) "ON." else "OFF."))
            player.varpManager.flagSave(2501)
        }

        define("xpconfig", Privilege.STANDARD){ player, args ->
            if(args.size < 3){
                reject(player,"Usage: ::xpconfig track|mode type",
                    "Track types: total|recent",
                    "Mode types: instant|increment",
                    "Defaults: track - total, mode - increment")
            }

            when(args[1]){
                "track" -> when(args[2]){
                    "total" -> {
                        player.varpManager.get(2501).setVarbit(2,0).send(player)
                        notify(player,"You are now tracking " + colorize("%RTOTAL") + " experience.")
                    }
                    "recent" -> {
                        player.varpManager.get(2501).setVarbit(2,1).send(player)
                        notify(player,"You are now tracking the " + colorize("%RMOST RECENT") + " skill's experience.")
                    }
                    else -> {
                        reject(player,"Usage: ::xpconfig track|mode type")
                        reject(player,"Track types: total|recent")
                        reject(player, "Mode types: instant|increment")
                        reject(player,"Defaults: track - total, mode - increment")
                    }
                }

                "mode" -> {
                    when(args[2]){
                        "instant" -> {
                            player.varpManager.get(2501).setVarbit(1,1).send(player)
                            notify(player,"Your xp tracker now updates " + colorize("%RINSTANTLY") + ".")
                        }
                        "increment" -> {
                            player.varpManager.get(2501).setVarbit(1,0).send(player)
                            notify(player,"Your xp tracker now updates " + colorize("%RINCREMENTALLY") + ".")
                        }
                        else -> {
                            reject(player,"Usage: ::xpconfig track|mode type")
                            reject(player,"Track types: total|recent")
                            reject(player, "Mode types: instant|increment")
                            reject(player,"Defaults: track - total, mode - increment")
                        }
                    }
                }
                else -> {
                    reject(player,"Usage: ::xpconfig track|mode type")
                    reject(player,"Track types: total|recent")
                    reject(player, "Mode types: instant|increment")
                    reject(player,"Defaults: track - total, mode - increment")
                }
            }
            player.varpManager.flagSave(2501)
        }

        define("toggleslayer", Privilege.STANDARD){ player, _ ->
            val disabled = player.varpManager.get(2502).getVarbit(0) == 1
            player.varpManager.get(2502).setVarbit(0,if(disabled) 0 else 1).send(player)
            if(!disabled){
                player.varpManager.flagSave(2502)
            } else {
                player.varpManager.get(2502).save = false
            }
            notify(player,"Slayer task tracker is now " + (if(disabled) colorize("%RON") else colorize("%ROFF")) + ".")
        }

        define("setvarbit", Privilege.ADMIN, "::setvarbit <lt>VARBIT ID<gt> <lt>VALUE<gt>", ""){
            player,args ->
            if(args.size != 3){
                reject(player,"Usage: ::setvarbit varbit value")
            }
            val index = args[1].toIntOrNull()
            val value = args[2].toIntOrNull()

            if(index == null || value == null){
                reject(player,"Usage ::setvarbit index offset value")
            }

            player.varpManager.setVarbit(index!!, value!!)
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

            player.varpManager.get(index!!).setVarbit(offset!!, value!!).send(player)
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
            val state: FarmingState = player.states.get("farming") as FarmingState? ?: return@define

            for(patch in state.getPatches()){
                patch.nextGrowth = System.currentTimeMillis()
            }
        }

        define("finishbins", Privilege.ADMIN, "", "Finishes any in-progress compost bins."){ player, _ ->
            val state: FarmingState = player.states.get("farming") as FarmingState? ?: return@define

            for(bin in state.getBins()){
                bin.finishedTime = System.currentTimeMillis()
            }
        }

        define("testlady", Privilege.ADMIN){ player, _ ->
            RandomEventManager.getInstance(player)!!.event = RandomEvents.RIVER_TROLL.npc.create(player)
            RandomEventManager.getInstance(player)!!.event!!.init()
        }

        define("revent", Privilege.ADMIN){ player, _ ->
            println(player.pulseManager.current)
            RandomEventManager.getInstance(player)!!.fireEvent()
        }

        define("addcredits", Privilege.ADMIN){ player, _ ->
            player.details.credits += 100
        }

        define("resetgwdropes", Privilege.STANDARD, "", "OLD: Was used for fixing broken data."){ player, _ ->
            player.varpManager.get(1048).clearBitRange(0,31)
            player.varpManager.get(1048).send(player)
        }

        define("resetmistag", Privilege.STANDARD, "", "OLD: Was used for fixing broken data."){ player, _ ->
            player.removeAttribute("mistag-greeted")
        }

        define("getnpcparent"){player,args ->
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
        define("infinitespecial", Privilege.ADMIN){ player, args ->
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
        define("allow_aggro", Privilege.ADMIN) { player, args ->
            val usageStr = "Usage: ::allow_aggro true | false"
            if(args.size < 2) {
                reject(player, usageStr)
            }
            when(args[1]) {
                "true" -> player.setAttribute("allow_admin_aggression", true)
                "false" -> player.removeAttribute("allow_admin_aggression")
                else -> reject(player, usageStr)

            }
        }

        define("rules", Privilege.STANDARD, "", "Shows the rules."){ player, _ ->
            RulesAndInfo.setBaseRulesAndInfo(player);
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
            }
            else
            {
                sendDialogue(player, "Wrong pin. Try again.")
            }
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

        val entries = offerAmounts.entries.sortedBy({ e -> getItemName(e.key) })
        var lineId = 11
        setScrollTitle(player, "Bot Stock - \"$searchTerm\"")
        for(i in 0..299) {
            val offer = entries.elementAtOrNull(i)
            if (offer != null)
                setInterfaceText(player, "${getItemName(offer.key)} (<col=6bff89>x${offer.value}</col>) -> Price: <col=e8d151>${offerPrice[offer.key]}</col>gp", Components.QUESTJOURNAL_SCROLL_275, lineId++)
            else
                setInterfaceText(player, "", Components.QUESTJOURNAL_SCROLL_275, lineId++)
        }
        openInterface(player, Components.QUESTJOURNAL_SCROLL_275)
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

        val entries = offerAmounts.entries.sortedBy({ e -> getItemName(e.key) })
        var lineId = 11
        setScrollTitle(player, "Bot Stock")
        for(i in 0..299) {
            val offer = entries.elementAtOrNull(i)
            if (offer != null)
                setInterfaceText(player, "${getItemName(offer.key)} (<col=6bff89>x${offer.value}</col>) -> Price: <col=e8d151>${offerPrice[offer.key]}</col>gp", Components.QUESTJOURNAL_SCROLL_275, lineId++)
            else
                setInterfaceText(player, "", Components.QUESTJOURNAL_SCROLL_275, lineId++)
        }
        openInterface(player, Components.QUESTJOURNAL_SCROLL_275)
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

        val entries = offerAmounts.entries.sortedBy({ e -> getItemName(e.key) })
        var lineId = 11
        setScrollTitle(player, "Active Sell Offers")
        for(i in 0..299) {
            val offer = entries.elementAtOrNull(i)
            if (offer != null)
                setInterfaceText(player, "${getItemName(offer.key)} (<col=6bff89>x${offer.value}</col>) -> Lowest: <col=e8d151>${lowestPrice[offer.key]}</col>gp", Components.QUESTJOURNAL_SCROLL_275, lineId++)
            else
                setInterfaceText(player, "", Components.QUESTJOURNAL_SCROLL_275, lineId++)
        }
        openInterface(player, Components.QUESTJOURNAL_SCROLL_275)
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

        val entries = offerAmounts.entries.sortedBy({ e -> getItemName(e.key) })
        var lineId = 11
        setScrollTitle(player, "Active Buy Offers")
        for(i in 0..299) {
            val offer = entries.elementAtOrNull(i)
            if (offer != null)
                setInterfaceText(player, "${getItemName(offer.key)} (<col=6bff89>x${offer.value}</col>) -> Highest: <col=e8d151>${highestPrice[offer.key]}</col>gp", Components.QUESTJOURNAL_SCROLL_275, lineId++)
            else
                setInterfaceText(player, "", Components.QUESTJOURNAL_SCROLL_275, lineId++)
        }
        openInterface(player, Components.QUESTJOURNAL_SCROLL_275)
    }

    fun showOffers(player: Player, searchTerm: String){
        val offers = GrandExchange.getValidOffers().filter { getItemName(it.itemID).contains(searchTerm, true) || getItemName(it.itemID).equals(searchTerm, true) }
        val buyingAmount = HashMap<Int, Int>()
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

        setScrollTitle(player, "Results for \"$searchTerm\"")

        var lineId = 11
        for(i in 0..299) {
            if(i > buyingAmount.keys.size)
            {
                val offer = sellingAmount.entries.elementAtOrNull(i - buyingAmount.keys.size)
                if(offer != null) {
                    setInterfaceText(player, "[SELLING] ${getItemName(offer.key)} (<col=6bff89>x${offer.value}</col>) -> Lowest: <col=e8d151>${sellingLowest[offer.key]}</col>gp", Components.QUESTJOURNAL_SCROLL_275, lineId++)
                    continue
                }
            }
            else if(i < buyingAmount.keys.size)
            {
                val offer = buyingAmount.entries.elementAtOrNull(i)
                if(offer != null) {
                    setInterfaceText(player, "[BUYING] ${getItemName(offer.key)} (<col=6bff89>x${offer.value}</col>) -> Highest: <col=e8d151>${buyingHighest[offer.key]}</col>gp", Components.QUESTJOURNAL_SCROLL_275, lineId++)
                    continue
                }
            }
            else {
                setInterfaceText(player, "<str>                                                                                                                  </str>", Components.QUESTJOURNAL_SCROLL_275, lineId++)
                continue
            }

            setInterfaceText(player, "", Components.QUESTJOURNAL_SCROLL_275, lineId++)
        }
        openInterface(player, Components.QUESTJOURNAL_SCROLL_275)
    }

    fun setScrollTitle(player: Player, text: String){
        setInterfaceText(player, text, Components.QUESTJOURNAL_SCROLL_275, 2)
    }

}
