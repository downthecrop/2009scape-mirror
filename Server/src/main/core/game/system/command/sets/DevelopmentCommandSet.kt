package core.game.system.command.sets

import content.global.activity.jobs.JobManager
import core.api.*
import core.cache.Cache
import core.cache.def.impl.DataMap
import core.cache.def.impl.NPCDefinition
import core.cache.def.impl.VarbitDefinition
import core.cache.def.impl.Struct
import core.game.node.entity.combat.ImpactHandler.HitsplatType
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.SpellBookManager
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.item.Item
import core.net.packet.context.PlayerContext
import core.net.packet.out.ResetInterface
import core.plugin.Initializable
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.rs09.consts.Items
import core.game.system.command.Privilege
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.util.Arrays
import core.net.packet.PacketWriteQueue
import core.tools.Log
import core.game.node.entity.player.info.Rights
import core.game.node.entity.skill.Skills
import core.game.world.map.Location
import core.game.world.repository.Repository

@Initializable
class DevelopmentCommandSet : CommandSet(Privilege.ADMIN) {
    val farmKitItems = arrayListOf(Items.RAKE_5341, Items.SPADE_952, Items.SEED_DIBBER_5343, Items.WATERING_CAN8_5340, Items.SECATEURS_5329, Items.GARDENING_TROWEL_5325,Items.COMPOST_6032, Items.SUPERCOMPOST_6034, Items.PLANT_CURE_6036)
    val runeKitItems = arrayListOf(Items.AIR_RUNE_556, Items.EARTH_RUNE_557, Items.FIRE_RUNE_554, Items.WATER_RUNE_555, Items.MIND_RUNE_558, Items.BODY_RUNE_559, Items.DEATH_RUNE_560, Items.NATURE_RUNE_561, Items.CHAOS_RUNE_562, Items.LAW_RUNE_563, Items.COSMIC_RUNE_564, Items.BLOOD_RUNE_565, Items.SOUL_RUNE_566, Items.ASTRAL_RUNE_9075)

    fun getPlayerFromArgs(player: Player, args: Array<String>, startindex: Int = 1): Player? {
        val n = args.slice(startindex until args.size).joinToString("_")
        if (n == "") { //no argument given -> return self player
            return player
        }
        val target = Repository.getPlayerByName(n)
        if (target == null) {
            reject(player, "Could not find a player named '$n'")
        }
        return target
    }

    override fun defineCommands() {
        define("farmkit", Privilege.ADMIN, "", "Provides a kit of various farming equipment."){player,_ ->
            for(item in farmKitItems){
                player.inventory.add(Item(item))
            }
        }

        define("cs2", Privilege.ADMIN, "::cs2 id args", "Allows you to call arbitrary cs2 scripts during runtime") {player, args -> 
            var scriptArgs = ArrayList<Any>()
            if (args.size == 2) {
                runcs2(player, args[1].toIntOrNull() ?: return@define)
                return@define
            }
            else if (args.size > 2) {
                for (i in 2 until args.size) {
                    scriptArgs.add(args[i].toIntOrNull() ?: args[i])
                }
                runcs2(player, args[1].toIntOrNull() ?: return@define, *(scriptArgs.toTypedArray().also { player.debug (Arrays.toString(it)) }))
            }
        }

        define("cleardiary", Privilege.ADMIN) { player, _ ->
            for (type in DiaryType.values()) {
                val diary = player.achievementDiaryManager.getDiary(type)
                for (level in 0 until diary.levelStarted.size) {
                    for (task in 0 until diary.taskCompleted[level].size) {
                        diary.resetTask(player, level, task)
                    }
                }
            }
            sendMessage(player, "All achievement diaries cleared successfully.")
        }

        define("clearjob", Privilege.ADMIN) { player, _ ->
            val playerJobManager = JobManager.getInstance(player)
            playerJobManager.job = null
            playerJobManager.jobAmount = -1
            playerJobManager.jobOriginalAmount = -1

            sendMessage(player, "Job cleared successfully.")
        }

        define("region", Privilege.STANDARD, "", "Prints your current Region ID.") {player, args ->
            sendMessage(player, "Region ID: ${player.viewport.region.regionId}")
        }

        define("spellbook", Privilege.ADMIN, "::spellbook <lt>book ID<gt> (0 = MODERN, 1 = ANCIENTS, 2 = LUNARS)", "Swaps your spellbook to the given book ID."){player, args ->
            if(args.size < 2){
                reject(player,"Usage: ::spellbook [int]. 0 = MODERN, 1 = ANCIENTS, 2 = LUNARS")
            }
            val spellBook = SpellBookManager.SpellBook.values()[args[1].toInt()]
            player.spellBookManager.setSpellBook(spellBook)
            player.spellBookManager.update(player)
        }

        define("killme", Privilege.ADMIN, "", "Does exactly what it says on the tin.") { player, _ ->
            player.impactHandler.manualHit(player, player.skills.lifepoints, HitsplatType.NORMAL)
        }

        define("struct") {player, args ->
            val mapId = args[1].toIntOrNull() ?: return@define

            val def = Struct.get(mapId)
            log(this::class.java, Log.FINE,  def.toString())
        }

        define("datamap") {player, args ->
            val mapId = args[1].toIntOrNull() ?: return@define

            val def = DataMap.get(mapId)
            log(this::class.java, Log.FINE,  def.toString())
        }

        define("dumpstructs", Privilege.ADMIN, "", "Dumps all the cache structs to structs.txt") {player, _ ->
            val dump = File("structs.txt")
            val writer = BufferedWriter(FileWriter(dump))
            val index = Cache.getIndexes()[2]
            val containers = index.information.containers[26].filesIndexes
            for(fID in containers)
            {
                val file = index.getFileData(26, fID)
                if(file != null){
                    val def = Struct.parse(fID, file)
                    if(def.dataStore.isEmpty()) continue //no data in struct.
                    writer.write(def.toString())
                    writer.newLine()
                }
            }
            writer.flush()
            writer.close()
        }

        define("dumpdatamaps", Privilege.ADMIN, "", "Dumps all the cache data maps to datamaps.txt") {player, _ ->
            val index = Cache.getIndexes()[17]
            val containers = index.information.containersIndexes

            val dump = File("datamaps.txt")
            val writer = BufferedWriter(FileWriter(dump))

            for(cID in containers)
            {
                val fileIndexes = index.information.containers[cID].filesIndexes
                for(fID in fileIndexes)
                {
                    val file = index.getFileData(cID, fID)
                    if(file != null){
                        val def = DataMap.parse((cID shl 8) or fID, file)
                        if(def.keyType == '?') continue //Empty definition - only a 0 present in the cachefile data.
                        writer.write(def.toString())
                        writer.newLine()
                    }
                }
            }
            writer.flush()
            writer.close()
        }

        define("rolldrops", Privilege.ADMIN, "::rolldrops <lt>NPC ID<gt> <lt>AMOUNT<gt>", "Rolls the given NPC drop table AMOUNT times.") { player: Player, args: Array<String> ->
            if(args.size < 2){
                reject(player,"Usage: ::rolldrops npcid amount")
            }

            val container = player.dropLog
            val npcId = args[1].toInt()
            val amount = args[2].toInt()

            container.clear()
            val drops = NPCDefinition.forId(npcId).dropTables.table.roll(player, amount)
            for(drop in drops) container.add(drop, false)
            container.open(player)
        }

        define("varbits", Privilege.ADMIN, "::varbits <lt>Varp ID<gt>", "Lists all the varbits assigned to the given varp.") { player, args ->
            if(args.size < 2)
                reject(player, "Usage: ::varbits varpIndex")

            val varp = args[1].toIntOrNull() ?: reject(player, "Please use a valid int for the varpIndex.")
            GlobalScope.launch {
                sendMessage(player, "========== Found Varbits for Varp $varp ==========")
                for(id in 0 until 10000)
                {
                    val def = VarbitDefinition.forId(id)
                    if(def.varpId == varp)
                    {
                        sendMessage(player, "${def.id} -> [offset: ${def.startBit}, upperBound: ${def.endBit}]")
                    }
                }
                sendMessage(player, "=========================================")
            }
        }

        define("testpacket") { player, _ ->
            PacketWriteQueue.write(ResetInterface(), PlayerContext(player))
        }

        define("npcsearch", Privilege.STANDARD, "npcsearch name", "Searches for NPCs that match the name either in main or children.") {player, strings ->
            val name = strings.slice(1 until strings.size).joinToString(" ").lowercase()
            for (id in 0 until 9000) {
                val def = NPCDefinition.forId(id)
                if (def.name.isNotBlank() && (def.name.lowercase().contains(name) || name.contains(def.name.lowercase()))) {
                    notify(player, "$id - ${def.name}")
                }
                else {
                    for ((childId,index) in def.childNPCIds?.withIndex() ?: continue) {
                        val childDef = NPCDefinition.forId(childId)
                        if (childDef.name.lowercase().contains(name) || name.contains(childDef.name.lowercase())) {
                            notify(player, "$childId child($id) index $index - ${childDef.name}")
                        }
                    }
                }
            }
        }

        define("itemsearch", Privilege.STANDARD, "itemsearch name", "Searches for items that match the name.") {player, args ->
            val itemName = args.copyOfRange(1, args.size).joinToString(" ").lowercase()
            for (i in 0 until 15000) {
                val name = getItemName(i).lowercase()
                if (name.contains(itemName) || itemName.contains(name))
                    notify(player, "$i: $name")
            }
        }

        define("runekit", Privilege.ADMIN, "", "Gives 1k of each Rune type" ) { player, args ->
                    for(item in runeKitItems) {
                    addItem(player, item, 1000)
            }
        }

        define("drawchunks", Privilege.ADMIN, "", "Draws the border of the chunk you're standing in") {player, _ -> 
            setAttribute (player, "chunkdraw", !getAttribute(player, "chunkdraw", false))
        }

        define("drawclipping", Privilege.ADMIN, "", "Draws the clipping flags of the region you're standing in") {player, _ ->
            setAttribute (player, "clippingdraw", !getAttribute(player, "clippingdraw", false))
        }

        define("drawregions", Privilege.ADMIN, "", "DRaws the border of the region you're standing in") {player, _ -> 
            setAttribute (player, "regiondraw", !getAttribute(player, "regiondraw", false))
        }

        define("drawroute", Privilege.ADMIN, "", "Visualizes the path your player is taking") {player, _ -> 
            setAttribute (player, "routedraw", !getAttribute(player, "routedraw", false))
        }

        define ("fmstart", Privilege.ADMIN, "", "") {player, _ -> 
            setAttribute(player, "fmstart", Location.create(player.location))
        }

        define ("fmend", Privilege.ADMIN, "", "") {player, _ ->
            setAttribute(player, "fmend", Location.create(player.location))
        }

        define ("fmspeed", Privilege.ADMIN, "", "") {player, args ->
            setAttribute(player, "fmspeed", args[1].toIntOrNull() ?: 10)
        }

        define ("fmspeedend", Privilege.ADMIN, "", "") {player, args ->
            setAttribute(player, "fmspeedend", args[1].toIntOrNull() ?: 10)
        }

        define("testfm", Privilege.ADMIN, "", "") { player, _ -> 
            val start = getAttribute(player, "fmstart", Location.create(player.location))
            val end = getAttribute(player, "fmend", Location.create(player.location))
            val speed = getAttribute(player, "fmspeed", 10)
            val speedEnd = getAttribute(player, "fmspeedend", 10)
            val ani = getAttribute(player, "fmanim", -1)
            forceMove(player, start, end, speed, speedEnd, anim = ani)    
        }

        define("fmanim", Privilege.ADMIN, "", "") {player, args ->
            setAttribute(player, "fmanim", args[1].toIntOrNull() ?: -1)
        }

        define("drawintersect", Privilege.ADMIN, "", "Visualizes the predicted intersection point with an NPC") {player, _ ->
            setAttribute(player, "draw-intersect", !getAttribute(player, "draw-intersect", false))
        }

        define("expression", Privilege.ADMIN, "::expression id", "Visualizes chathead animations from ID.") { player, args ->
            if(args.size != 2)
                reject(player, "Usage: ::expression id")
            val id = args[1].toIntOrNull() ?: 9804
            player.dialogueInterpreter.sendDialogues(player, id, "Expression ID: $id")
        }

        define("timers", Privilege.ADMIN, "::timers", "Print out timers") { player, args ->
            player.sendMessage("Active timers:")
            for(timer in player.timers.activeTimers) {
                player.sendMessage("  ${timer.identifier} ${timer.nextExecution}")
            }
            player.sendMessage("New timers:")
            for(timer in player.timers.newTimers) {
                player.sendMessage("  ${timer.identifier}")
            }
        }

        define("setpestpoints", Privilege.ADMIN, "::setpestpoints points player_name", "Sets your (or player_name's) Pest Control points to 'points'") { player, args ->
            val target = getPlayerFromArgs(player, args, 2) ?: return@define
            val points = args[1].toIntOrNull()
            if (points == null) {
                reject(player, "No valid 'points' argument given")
            }
            target.savedData.activityData.pestPoints = points!!
        }

        define("makeadmin", Privilege.ADMIN, "::makeadmin player_name", "Permanently gives admin rights to player_name (or self if empty)") { player, args ->
            val target = getPlayerFromArgs(player, args, 1) ?: return@define
            target.details.rights = Rights.ADMINISTRATOR
            sendMessage(player, "Gave admin rights to ${target.username}.")
            sendMessage(target, "You've been given admin rights by ${player.username}!")
        }

        define("dropadmin", Privilege.ADMIN, "::dropadmin", "Permanently drops admin rights from self") { player, _ ->
            player.details.rights = Rights.REGULAR_PLAYER
            sendMessage(player, "Dropped admin rights.")
        }

        define("max", Privilege.ADMIN, "::max player_name", "Gives all 99s to player_name (or self if empty)") { player, args ->
            val target = getPlayerFromArgs(player, args, 1) ?: return@define
            var index = 0
            Skills.SKILL_NAME.forEach {
                target.skills.setStaticLevel(index,99)
                target.skills.setLevel(index,99)
                index++
            }
            target.skills.updateCombatLevel()
        }

        define("noobme", Privilege.ADMIN, "::noobme player_name", "Sets player_name (or self if empty) back to default stats") { player, args ->
            val target = getPlayerFromArgs(player, args, 1) ?: return@define
            var index = 0
            Skills.SKILL_NAME.forEach {
                val level = if (index == Skills.HITPOINTS) 10 else 1
                target.skills.setStaticLevel(index, level)
                target.skills.setLevel(index, level)
                index++
            }
            target.skills.updateCombatLevel()
        }

        define("setlevel", Privilege.ADMIN, "::setlevel <lt>SKILL NAME<gt> <lt>LEVEL<gt> <lt>PLAYER<gt>", "Sets SKILL NAME to LEVEL for PLAYER (self if omitted)."){player,args ->
            if (args.size < 3) reject(player,"Usage: ::setlevel skillname level")
            val skillname = args[1]
            val desiredLevel: Int? = args[2].toIntOrNull()
            if (desiredLevel == null) {
                reject(player, "Level must be an integer.")
            }
            if (desiredLevel!! > 99) reject(player,"Level must be 99 or lower.")
            val skill = Skills.getSkillByName(skillname)
            if (skill < 0) reject(player, "Must use a valid skill name!")
            val target = getPlayerFromArgs(player, args, 3) ?: return@define
            target.skills.setStaticLevel(skill,desiredLevel)
            target.skills.setLevel(skill,desiredLevel)
            target.skills.updateCombatLevel()
        }

        define("addxp", Privilege.ADMIN, "::addxp <lt>skill name | id<gt> <lt>xp<gt>", "Add xp to skill") { player, args ->
            if (args.size < 3) reject(player, "Usage: ::addxp <lt>skill name | id<gt> <lt>xp<gt> <lt>player(optional)<gt>")
            val target = getPlayerFromArgs(player, args, 3) ?: return@define

            val skill = args[1].toIntOrNull() ?: Skills.getSkillByName(args[1])
            if (skill < 0 || skill >= Skills.NUM_SKILLS) reject(player, "Must use valid skill name or id.")

            val xp = args[2].toDoubleOrNull()
            if (xp == null || xp <= 0) reject(player, "Xp must be a positive number.")

            target.skills.addExperience(skill, xp!!)
        }
    }
}
