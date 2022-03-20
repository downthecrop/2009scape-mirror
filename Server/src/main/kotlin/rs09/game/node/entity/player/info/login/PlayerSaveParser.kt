package rs09.game.node.entity.player.info.login

import core.game.interaction.item.brawling_gloves.BrawlingGloves
import core.game.node.entity.combat.CombatSpell
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.IronmanMode
import core.game.node.entity.player.link.SpellBookManager
import core.game.node.entity.player.link.emote.Emotes
import core.game.node.entity.player.link.grave.GraveType
import core.game.node.entity.player.link.music.MusicEntry
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import rs09.ServerConstants
import rs09.game.node.entity.skill.farming.CompostBins
import rs09.game.node.entity.skill.farming.FarmingPatch
import rs09.game.system.SystemLogger
import rs09.game.world.GameWorld
import java.io.FileReader
import java.util.*

/**
 * Class used for parsing JSON player saves.
 * @author Ceikry
 * @param player: The player we are parsing.
 */
class PlayerSaveParser(val player: Player) {
    var parser = JSONParser()
    var reader: FileReader? = FileReader(ServerConstants.PLAYER_SAVE_PATH + player.name + ".json")
    var saveFile: JSONObject? = null
    var read = true

    val patch_varps = FarmingPatch.values().map { it.varpIndex }.toIntArray()
    val bin_varps = CompostBins.values().map { it.varpIndex }.toIntArray()

    init {
        reader
                ?: SystemLogger.logWarn("Couldn't find save file for ${player.name}, or save is corrupted.").also { read = false }
        if (read) {
            saveFile = parser.parse(reader) as JSONObject
        }
    }

    fun parse() = GlobalScope.launch {
        if (read) {
            launch {
                parseCore()
                parseAttributes()
                parseSkills()
                parseSettings()
                parseSlayer()
                parseQuests()
                parseAppearance()
                parseGrave()
                parseVarps()
                parseStates()
            }
            launch {
                parseSpellbook()
                parseGrandExchange()
                parseSavedData()
                parseAutocastSpell()
                parseFarming()
                parseConfigs()
                parseMonitor()
            }
            launch {
                parseMusic()
                parseFamiliars()
                parseBarCrawl()
                parseAntiMacro()
                parseTT()
                parseBankPin()
            }
            launch {
                parseHouse()
                parseIronman()
                parseEmoteManager()
                parseStatistics()
                parseBrawlingGloves()
                parseAchievements()
                parsePouches()
            }
            parsePouches()
        }
    }

    fun parseVarps(){
        if(saveFile!!.containsKey("varps"))
            player.varpManager.parse(saveFile!!["varps"] as JSONArray)
    }

    fun parsePouches() {
        if (saveFile!!.containsKey("pouches"))
            player.pouchManager.parse(saveFile!!["pouches"] as JSONArray)
    }

    fun parseAttributes() {
        if (saveFile!!.containsKey("attributes")) {
            val attrData = saveFile!!["attributes"] as JSONArray
            for (a in attrData) {
                val attr = a as JSONObject
                val key = attr["key"].toString()
                val type = attr["type"].toString()
                val isExpirable = attr.getOrDefault("expirable",false) as Boolean
                if(isExpirable){
                    val expireTime = attr["expiration-time"].toString().toLong()
                    if(expireTime < System.currentTimeMillis()) continue

                    player.gameAttributes.keyExpirations[key] = expireTime
                }
                player.gameAttributes.savedAttributes.add(key)
                player.gameAttributes.attributes.put(key, when (type) {
                    "int" -> attr["value"].toString().toInt()
                    "str" -> attr["value"].toString()
                    "short" -> attr["value"].toString().toShort()
                    "long" -> attr["value"].toString().toLong()
                    "bool" -> attr["value"] as Boolean
                    "byte" -> Base64.getDecoder().decode(attr["value"].toString())[0]
                    else -> null.also { SystemLogger.logWarn("Invalid data type for key: $key in PlayerSaveParser.kt Line 115") }
                })
            }
        } else {
            player.gameAttributes.parse(player.name + ".xml")
        }
    }

    fun parseBrawlingGloves() {
        if (saveFile!!.containsKey("brawlingGloves")) {
            val bgData: JSONArray = saveFile!!["brawlingGloves"] as JSONArray
            for (bg in bgData) {
                val glove = bg as JSONObject
                player.brawlingGlovesManager.registerGlove(BrawlingGloves.forIndicator(glove.get("gloveId").toString().toInt()).id, glove.get("charges").toString().toInt())
            }
        }
    }

    fun parseStatistics() {
        if (saveFile!!.containsKey("statistics")) {
            val stats: JSONArray = saveFile!!["statistics"] as JSONArray
            for (stat in stats) {
                val s = stat as JSONObject
                val index = (s.get("index") as String).toInt()
                val value = (s.get("value") as String).toInt()
            }
        }
    }

    fun parseEmoteManager() {
        if (saveFile!!.containsKey("emoteData")) {
            val emoteData: JSONArray = saveFile!!["emoteData"] as JSONArray
            for (emote in emoteData) {
                val e = Emotes.values()[(emote as String).toInt()]
                if (!player.emoteManager.emotes.contains(e)) {
                    player.emoteManager.emotes.add(e)
                }
            }
        }
    }

    fun parseIronman() {
        if (saveFile!!.containsKey("ironManMode")) {
            val ironmanMode = (saveFile!!["ironManMode"] as String).toInt()
            player.ironmanManager.mode = IronmanMode.values()[ironmanMode]
        }
    }

    fun parseAchievements() {
        if (saveFile!!.containsKey("achievementDiaries")) {
            val achvData = saveFile!!["achievementDiaries"] as JSONArray
            player.achievementDiaryManager.parse(achvData)
        } else {
            player.achievementDiaryManager.resetRewards()
        }
    }

    fun parseHouse() {
        val houseData = saveFile!!["houseData"] as JSONObject
        player.houseManager.parse(houseData)
    }

    fun parseBankPin() {
        val bpData = saveFile!!["bankPinManager"] as JSONObject
        player.bankPinManager.parse(bpData)
    }

    fun parseTT() {
        val ttData = saveFile!!["treasureTrails"] as JSONObject
        player.treasureTrailManager.parse(ttData)
    }

    fun parseAntiMacro() {
    }

    fun parseStates() {
        player.states.clear()
        SystemLogger.logErr("Parsing states")
        if (saveFile!!.containsKey("states")) {
            val states: JSONArray = saveFile!!["states"] as JSONArray
            for (state in states) {
                val s = state as JSONObject
                val stateId = s["stateKey"].toString()
                if(player.states[stateId] != null) continue
                val stateClass = player.registerState(stateId)
                stateClass?.parse(s)
                stateClass?.init()
                player.states.put(stateId,stateClass)
            }
        }
    }

    fun parseBarCrawl() {
        val barCrawlData = saveFile!!["barCrawl"] as JSONObject
        player.barcrawlManager.parse(barCrawlData)
    }

    fun parseFamiliars() {
        val familiarData = saveFile!!["familiarManager"] as JSONObject
        player.familiarManager.parse(familiarData)
    }

    fun parseMusic() {
        val unlockedSongs = saveFile!!["unlockedMusic"] as JSONArray
        for (song in unlockedSongs) {
            val s = (song as String).toInt()
            val entry = MusicEntry.forId(s)
            player.musicPlayer.unlocked.put(entry.index, entry)
        }
    }

    fun parseMonitor() {
        val monitorData = saveFile!!["playerMonitor"] as JSONObject
        if (monitorData.containsKey("duplicationFlag")) {
            val duplicationFlag: Int = (monitorData.get("duplicationFlag") as String).toInt()
            player.monitor.duplicationLog.flag(duplicationFlag)
        }
        if (monitorData.containsKey("macroFlag")) {
            val macroFlag: Int = (monitorData.get("macroFlag") as String).toInt()
            player.monitor.macroFlag = macroFlag
        }
        if (monitorData.containsKey("lastIncreaseFlag")) {
            val lastIncreaseFlag: Long = (monitorData.get("lastIncreaseFlag") as String).toLong()
            player.monitor.duplicationLog.lastIncreaseFlag = lastIncreaseFlag
        }
    }

    fun parseConfigs() {
        val configs = saveFile!!["configs"] as JSONArray
        for (config in configs) {
            val c = config as JSONObject
            val index = (c.get("index") as String).toInt()
            if(index == 1048) continue
            if(patch_varps.contains(index)) continue
            if(bin_varps.contains(index)) continue
            val value = (c.get("value") as String).toInt()
            player.configManager.savedConfigurations[index] = value
        }
    }

    fun parseFarming() {
        /*val farmingData = saveFile!!["farming"] as JSONObject

        if (farmingData.containsKey("equipment")) {
            val equipmentData: JSONArray? = farmingData.get("equipment") as JSONArray
            player.farmingManager.equipment.container.parse(equipmentData)
        }
        if (farmingData.containsKey("bins")) {
            val compostData: JSONArray? = farmingData.get("bins") as JSONArray
            player.farmingManager.compostManager.parse(compostData)
        }
        if (farmingData.containsKey("wrappers")) {
            val wrapperData: JSONArray? = farmingData.get("wrappers") as JSONArray
            player.farmingManager.parseWrappers(wrapperData)
        }
        if(farmingData.containsKey("seedlings")){
            val seedlingData = farmingData.get("seedlings") as JSONArray
            player.farmingManager.seedlingManager.parse(seedlingData)
        }
        if(farmingData.containsKey("farmingAmuletWrapperID")){
            player.farmingManager.amuletBoundWrapper = player.farmingManager.getPatchWrapper(farmingData.get("farmingAmuletWrapperID").toString().toInt())
        }*/
    }

    fun parseAutocastSpell() {
        val autocastRaw = saveFile!!["autocastSpell"]
        autocastRaw ?: return
        val autocast = autocastRaw as JSONObject
        val book = (autocast.get("book") as String).toInt()
        val spellId = (autocast.get("spellId") as String).toInt()
        player.properties.autocastSpell = SpellBookManager.SpellBook.values()[book].getSpell(spellId) as CombatSpell
    }

    fun parseSavedData() {
        val activityData = saveFile!!["activityData"] as JSONObject
        val questData = saveFile!!["questData"] as JSONObject
        val globalData = saveFile!!["globalData"] as JSONObject
        player.savedData.activityData.parse(activityData)
        player.savedData.questData.parse(questData)
        player.savedData.globalData.parse(globalData)
    }

    fun parseGrandExchange() {
        val geData: Any? = saveFile!!["grand_exchange"]
        if (geData != null) {
            player.exchangeRecords.parse(geData as JSONObject)
        }

    }

    fun parseSpellbook() {
        val spellbookData = (saveFile!!["spellbook"] as String).toInt()
        player.spellBookManager.setSpellBook(SpellBookManager.SpellBook.forInterface(spellbookData))
    }

    fun parseGrave() {
        saveFile ?: return
        val graveData = (saveFile!!["grave_type"] as String).toInt()
        player.graveManager.type = GraveType.values()[graveData]
    }

    fun parseAppearance() {
        saveFile ?: return
        val appearanceData = saveFile!!["appearance"] as JSONObject
        player.appearance.parse(appearanceData)
    }

    fun parseQuests() {
        saveFile ?: return
        val questData = saveFile!!["quests"] as JSONObject
        player.questRepository.parse(questData)
    }

    fun parseSlayer() {
        saveFile ?: return
        val slayerData = saveFile!!["slayer"] as JSONObject
        player.slayer.parse(slayerData)
    }

    fun parseCore() {
        saveFile ?: return
        val coreData = saveFile!!["core_data"] as JSONObject
        val inventory = coreData["inventory"] as JSONArray
        val bank = coreData["bank"] as JSONArray
        val bankSecondary = coreData.getOrDefault("bankSecondary", JSONArray()) as JSONArray
        val equipment = coreData["equipment"] as JSONArray
        val bBars = coreData["blastBars"] as? JSONArray
        val bOre = coreData["blastOre"] as? JSONArray
        val bCoal = coreData["blastCoal"] as? JSONArray
        val location = coreData["location"] as String
        val bankTabData = coreData["bankTabs"]
        if (bankTabData != null) {
            val tabData = bankTabData as JSONArray
            for (i in tabData) {
                i ?: continue
                val tab = i as JSONObject
                val index = tab["index"].toString().toInt()
                val startSlot = tab["startSlot"].toString().toInt()
                player.bankPrimary.tabStartSlot[index] = startSlot
            }
        }
        val bankTabSecondaryData = coreData["bankTabsSecondary"]
        if (bankTabSecondaryData != null) {
            val tabData = bankTabSecondaryData as JSONArray
            for (i in tabData) {
                i ?: continue
                val tab = i as JSONObject
                val index = tab["index"].toString().toInt()
                val startSlot = tab["startSlot"].toString().toInt()
                player.bankSecondary.tabStartSlot[index] = startSlot
            }
        }
        player.useSecondaryBank = coreData.getOrDefault("useSecondaryBank", false) as Boolean
        player.inventory.parse(inventory)
        player.bankPrimary.parse(bank)
        player.bankSecondary.parse(bankSecondary)
        player.equipment.parse(equipment)
        bBars?.let{player.blastBars.parse(it)}
        bOre?.let{player.blastOre.parse(bOre)}
        bCoal?.let{player.blastCoal.parse(bCoal)}
        player.location = rs09.JSONUtils.parseLocation(location)
    }

    fun parseSkills() {
        saveFile ?: return
        val skillData = saveFile!!["skills"] as JSONArray
        player.skills.parse(skillData)
        player.skills.experienceGained = saveFile!!["totalEXP"].toString().toDouble()
        player.skills.experienceMutiplier = saveFile!!["exp_multiplier"].toString().toDouble()
        if (GameWorld.settings?.default_xp_rate != 5.0) {
            player.skills.experienceMutiplier = GameWorld.settings?.default_xp_rate!!
        }
        val divisor: Double
        if(player.skills.experienceMutiplier >= 10 && !player.attributes.containsKey("permadeath")){ //exclude permadeath HCIMs from XP squish
            divisor = player.skills.experienceMutiplier / 5.0
            player.skills.correct(divisor)
        }
        if (saveFile!!.containsKey("milestone")) {
            val milestone: JSONObject = saveFile!!["milestone"] as JSONObject
            player.skills.combatMilestone = (milestone.get("combatMilestone")).toString().toInt()
            player.skills.skillMilestone = (milestone.get("skillMilestone")).toString().toInt()
        }
    }

    fun parseSettings() {
        saveFile ?: return
        val settingsData = saveFile!!["settings"] as JSONObject
        player.settings.parse(settingsData)
    }


}