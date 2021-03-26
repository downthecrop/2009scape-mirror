package rs09.game.system.command.sets

import core.cache.def.impl.ItemDefinition
import core.cache.def.impl.ObjectDefinition
import core.cache.def.impl.VarbitDefinition
import core.game.component.Component
import core.game.ge.OfferState
import core.game.node.`object`.GameObject
import core.game.node.entity.player.info.Rights
import core.game.node.entity.player.link.RunScript
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.system.communication.CommunicationInfo
import core.game.world.map.RegionManager
import core.game.world.map.build.DynamicRegion
import core.plugin.Initializable
import core.tools.StringUtils
import org.rs09.consts.Components
import rs09.ServerConstants
import rs09.game.content.activity.fishingtrawler.TrawlerLoot
import rs09.game.content.ame.RandomEvents
import rs09.game.ge.OfferManager
import rs09.game.node.entity.state.newsys.states.FarmingState
import rs09.game.system.SystemLogger
import rs09.game.system.command.Command
import rs09.game.system.command.CommandMapping
import rs09.game.world.GameWorld
import rs09.game.world.repository.Repository
import rs09.tools.stringtools.colorize
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection
import java.util.*

@Initializable
class MiscCommandSet : CommandSet(Command.Privilege.ADMIN){
    override fun defineCommands() {

        /**
         * Toggles debug mode
         */
        define("debug", Command.Privilege.STANDARD){ player, _ ->
            player.toggleDebug()
        }

        /**
         * Prints player's current location
         */
        define("loc", Command.Privilege.STANDARD){ player, _->
            val l = player.location
            val r = player.viewport.region
            var obj: GameObject? = null
            player.packetDispatch.sendMessage("Absolute: " + l + ", regional: [" + l.localX + ", " + l.localY + "], chunk: [" + l.chunkOffsetX + ", " + l.chunkOffsetY + "], flag: [" + RegionManager.isTeleportPermitted(l) + ", " + RegionManager.getClippingFlag(l) + ", " + RegionManager.isLandscape(l) + "].")
            player.packetDispatch.sendMessage("Region: [id=" + l.regionId + ", active=" + r.isActive + ", instanced=" + (r is DynamicRegion) + "], obj=" + RegionManager.getObject(l) + ".")
            player.packetDispatch.sendMessage("Object: " + RegionManager.getObject(l).also{obj = it} + ".")
            player.packetDispatch.sendMessage("Object Varp: " + obj?.definition?.configFile?.configId + " offset: " + obj?.definition?.configFile?.bitShift + " size: " + (obj?.definition?.configFile?.bitShift?.minus(obj?.definition?.configFile?.bitShift!!)))
            SystemLogger.logInfo("Viewport: " + l.getSceneX(player.playerFlags.lastSceneGraph) + "," + l.getSceneY(player.playerFlags.lastSceneGraph))
            val loc = "Location.create(" + l.x + ", " + l.y + ", " + l.z + ")"
            SystemLogger.logInfo(loc + "; " + player.playerFlags.lastSceneGraph + ", " + l.localX + ", " + l.localY)
            val stringSelection = StringSelection(loc)
            val clpbrd = Toolkit.getDefaultToolkit().systemClipboard
            clpbrd.setContents(stringSelection, null)
        }

        /**
         * Tells the player to use loc
         */
        define("pos", Command.Privilege.STANDARD){ player, _->
            notify(player, "Do you mean ::loc?")
        }

        /**
         * Tells the player to use loc
         */
        define("coords", Command.Privilege.STANDARD){ player, _->
            notify(player, "Do you mean ::loc?")
        }

        /**
         * Empty a player's inventory
         * ADMIN only (for obvious reasons)
         */
        define("empty"){player,_->
            player.inventory.clear()
            player.inventory.refresh()
        }

        /**
         * Announces a message in chat (NEWS)
         */
        define("announce"){_,args ->
            val message = args.slice(1 until args.size).joinToString(" ")
            Repository.sendNews(message)
        }

        /**
         * Lists the players currently online
         */
        define("players", Command.Privilege.STANDARD){ player, _ ->
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
            val builder = StringBuilder("<br>")
            var count = 0
            for (p in Repository.players) {
                if (count > 45) {
                    builder.append("<br>Max amount we can show on this interface.")
                    break
                }
                if (p == null || p.isAdmin && GameWorld.settings?.isDevMode != true && !player.isAdmin || p.isArtificial) {
                    continue
                }
                builder.append(red + "<img=" + (Rights.getChatIcon(p) - 1) + ">" + p.username + if(rights > 0) " [ip=" + p.details.ipAddress + ", name=" + p.details.compName + "]<br><br>" else "<br><br>")
                count++
            }
            player.packetDispatch.sendString(builder.toString(), 275, 11)
        }
        /**
         * ===================================================================================
         */


        /**
         * Opens the credit/voting shop
         */
        define("shop", Command.Privilege.STANDARD){ player, _ ->
            player.interfaceManager.open(Component(Components.CREDIT_SHOP))
        }

        /**
         * Shows the player a list of currently active GE sell offers
         */
        define("ge", Command.Privilege.STANDARD) { player, _ ->
            val offers = HashMap<Int,Int>()
            for (offerIDs in OfferManager.OFFERS_BY_ITEMID) {
                var totalOffered = 0
                for (offer in offerIDs.value) {
                    if (offer.offerState != OfferState.PENDING && offer.sell) {
                        totalOffered += offer.amountLeft
                    }
                }
                if (totalOffered != 0) {
                    offers[offerIDs.key] = totalOffered
                }
            }
            for (offerIDs in OfferManager.BOT_OFFERS) {
                if (offerIDs.value > 0) {
                    if (offers[offerIDs.key] == null) {
                        offers[offerIDs.key] = offerIDs.value
                    } else {
                        offers[offerIDs.key] = offers[offerIDs.key]!! + offerIDs.value
                    }
                }
            }
            for (i in 0..310) {
                player!!.packetDispatch.sendString("", 275, i)
            }
            val offerList = offers.keys.map { "${ItemDefinition.forId(it).name} - ${offers[it]}" }.sorted()

            var lineId = 11
            player!!.packetDispatch.sendString("Active Sell Offers", 275, 2)
            var counter = 0
            for(i in 0..299) {
                val offer = offerList.elementAtOrNull(i)
                if (offer != null)
                    player.packetDispatch.sendString(offer, 275, lineId++)
                else
                    player.packetDispatch.sendString("", 275, lineId++)
            }
            player.interfaceManager.open(Component(Components.QUESTJOURNAL_SCROLL_275))
        }
        /**
         * ==================================================================================
         */


        /**
         * List all commands
         */
        define("commands"){player,_ ->
            for (i in 0..310) {
                player.packetDispatch.sendString("", Components.QUESTJOURNAL_SCROLL_275, i)
            }
            var lineid = 11
            player.packetDispatch.sendString("Commands",Components.QUESTJOURNAL_SCROLL_275,2)
            for(line in CommandMapping.getNames())
                player.packetDispatch.sendString(line,Components.QUESTJOURNAL_SCROLL_275,lineid++)
            player.interfaceManager.open(Component(Components.QUESTJOURNAL_SCROLL_275))
        }

        /**
         * Reply to PMs (also enables tab-to-reply)
         */
        define("reply", Command.Privilege.STANDARD){ player, _ ->
            if(player.interfaceManager.isOpened){
                reject(player, "<col=e74c3c>Please finish what you're doing first.")
            }
            if (player.attributes.containsKey("replyTo")) {
                player.setAttribute("keepDialogueAlive", true)
                val replyTo = player.getAttribute("replyTo", "").replace("_".toRegex(), " ")
                player.setAttribute("runscript", object : RunScript() {
                    override fun handle(): Boolean {
                        CommunicationInfo.sendMessage(player, replyTo.toLowerCase(), getValue() as String)
                        player.removeAttribute("keepDialogueAlive")
                        return true
                    }
                })
                player.dialogueInterpreter.sendMessageInput(StringUtils.formatDisplayName(replyTo))
            } else {
                reject(player, "<col=3498db>You have not recieved any recent messages to which you can reply.")
            }
        }


        /**
         * Max account stats
         */
        define("max"){player,_ ->
            var index = 0
            Skills.SKILL_NAME.forEach {
                player.skills.setStaticLevel(index,99)
                player.skills.setLevel(index,99)
                index++
            }
            player.skills.updateCombatLevel()
        }

        define("noobme"){ player,_ ->
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
        define("setlevel"){player,args ->
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

        define("completediaries"){player,_ ->
            player.achievementDiaryManager.diarys.forEach {
                for(level in it.taskCompleted.indices){
                    for(task in it.taskCompleted[level].indices){
                        it.finishTask(player,level,task)
                    }
                }
            }
        }

        define("log"){player,_ ->
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

        define("rolltrawlerloot"){player,args ->
            val rolls = if(args.size < 2){
                100
            } else {
                args[1].toString().toInt()
            }
            player.bank.add(*TrawlerLoot.getLoot(rolls,false).toTypedArray())
        }

        define("fillbank"){player,_ ->
            for(i in 0 until ServerConstants.BANK_SIZE){
                player.bank.add(Item(i))
            }
        }

        define("emptybank"){player,_ ->
            player.bank.clear()
            player.bank.update()
        }

        define("setconfig"){player,args ->
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
            notify(player, "${VarbitDefinition.forObjectID(ObjectDefinition.forId(objectID).varbitID).configId}")
        }

        define("togglexp",Command.Privilege.STANDARD){ player, args ->
            val enabled = player.varpManager.get(2501).getVarbit(0)?.value == 1
            player.varpManager.get(2501).setVarbit(0,if(enabled) 0 else 1).send(player)
            notify(player, "XP drops are now " + colorize("%R" + if(!enabled) "ON." else "OFF."))
            player.varpManager.flagSave(2501)
        }

        define("xpconfig",Command.Privilege.STANDARD){player,args ->
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

        define("toggleslayer",Command.Privilege.STANDARD){player,_ ->
            val disabled = player.varpManager.get(2502).getVarbit(0)?.value == 1
            player.varpManager.get(2502).setVarbit(0,if(disabled) 0 else 1).send(player)
            if(!disabled){
                player.varpManager.flagSave(2502)
            } else {
                player.varpManager.get(2502).save = false
            }
            notify(player,"Slayer task tracker is now " + (if(disabled) colorize("%RON") else colorize("%ROFF")) + ".")
        }

        define("setvarbit",Command.Privilege.ADMIN){
            player,args ->
            if(args.size < 4){
                reject(player,"Usage: ::setvarbit index offset value")
            }
            val index = args[1].toIntOrNull()
            val offset = args[2].toIntOrNull()
            val value = args[3].toIntOrNull()

            if(index == null || offset == null || value == null){
                reject(player,"Usage ::setvarbit index offset value")
            }

            player.varpManager.get(index!!).setVarbit(offset!!, value!!).send(player)
        }

        define("grow",Command.Privilege.ADMIN){player,_ ->
            val state: FarmingState = player.states.get("farming") as FarmingState? ?: return@define

            for(patch in state.getPatches()){
                patch.nextGrowth = System.currentTimeMillis()
            }
        }

        define("finishbins",Command.Privilege.ADMIN){player,_ ->
            val state: FarmingState = player.states.get("farming") as FarmingState? ?: return@define

            for(bin in state.getBins()){
                bin.finishedTime = System.currentTimeMillis()
            }
        }

        define("testlady",Command.Privilege.ADMIN){player,_ ->
            player.antiMacroHandler.event = RandomEvents.SURPRISE_EXAM.npc.create(player,null,"sexam")
            player.antiMacroHandler.event!!.init()
        }

        define("addcredits",Command.Privilege.ADMIN){player,_ ->
            player.details.credits += 100
        }

        define("resetgwdropes",Command.Privilege.STANDARD){player,_ ->
            player.varpManager.get(1048).clearBitRange(0,31)
            player.varpManager.get(1048).send(player)
        }

        define("resetmistag",Command.Privilege.STANDARD){player,_ ->
            player.removeAttribute("mistag-greeted")
        }
    }
}