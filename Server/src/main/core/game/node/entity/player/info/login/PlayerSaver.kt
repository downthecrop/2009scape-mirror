package core.game.node.entity.player.info.login

import content.global.skill.summoning.familiar.BurdenBeast
import content.global.skill.summoning.pet.Pet
import core.ServerConstants
import core.api.PersistPlayer
import core.api.log
import core.game.container.Container
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.IronmanMode
import core.game.node.entity.skill.Skills
import core.game.world.map.Location
import core.tools.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import java.io.File
import java.io.FileWriter
import java.io.IOException
import javax.script.ScriptEngineManager
import java.util.*


/**
 * Class used for saving the player's data in JSON format.
 * Files are saved in the directory defined in ServerConstants.PLAYER_SAVE_PATH
 * @param player: the player to save for.
 * @author Ceikry
 */
class PlayerSaver (val player: Player){
    companion object {
        val contentHooks = ArrayList<PersistPlayer>()
    }
    fun populate(): JSONObject {
        val saveFile = JSONObject()
        saveVersion(saveFile)
        saveCoreData(saveFile)
        saveSkills(saveFile)
        saveSettings(saveFile)
        saveQuests(saveFile)
        saveAppearance(saveFile)
        saveSpellbook(saveFile)
        saveSavedData(saveFile)
        saveAutocast(saveFile)
        savePlayerMonitor(saveFile)
        saveMusicPlayer(saveFile)
        saveFamiliarManager(saveFile)
        saveBankPinData(saveFile)
        saveHouseData(saveFile)
        saveAchievementData(saveFile)
        saveIronManData(saveFile)
        saveEmoteData(saveFile)
        saveStatManager(saveFile)
        saveAttributes(saveFile)
        savePouches(saveFile)
        contentHooks.forEach { it.savePlayer(player, saveFile) }
        return saveFile
    }
    fun save() = runBlocking {
        if (!player.details.saveParsed) return@runBlocking
        val json: String
        if (ServerConstants.JAVA_VERSION < 11) {
            val manager = ScriptEngineManager()
            val scriptEngine = manager.getEngineByName("JavaScript")
            if (scriptEngine == null) {
                log(this::class.java, Log.ERR, "Cannot save: Failed to load ScriptEngineManager, this is a known issue on non Java-11 versions. Set your Java version to 11 to avoid further bugs!")
                return@runBlocking
            }
            scriptEngine.put("jsonString", populate().toJSONString())
            scriptEngine.eval("result = JSON.stringify(JSON.parse(jsonString), null, 2)")
            json = scriptEngine["result"] as String
        } else json = populate().toJSONString()

        try {
            if(!File("${ServerConstants.PLAYER_SAVE_PATH}${player.name}.json").exists()){
                File("${ServerConstants.PLAYER_SAVE_PATH}").mkdirs()
                withContext(Dispatchers.IO) {
                    File("${ServerConstants.PLAYER_SAVE_PATH}${player.name}.json").createNewFile()
                }
            }
            withContext(Dispatchers.IO) {
                FileWriter("${ServerConstants.PLAYER_SAVE_PATH}${player.name}.json").use { file ->
                    file.write(json)
                    file.flush()
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun savePouches(root: JSONObject){
        player.pouchManager.save(root)
    }

    fun saveVersion(root: JSONObject){
        root.put("version", player.version)
    }

    fun saveAttributes(root: JSONObject){
        if(player.gameAttributes.savedAttributes.isNotEmpty()){
            val attrs = JSONArray()
            for(key in player.gameAttributes.savedAttributes){
                val value = player.gameAttributes.attributes[key]
                value ?: continue
                val isExpirable = player.gameAttributes.keyExpirations.containsKey(key)
                val attr = JSONObject()
                val type = when(value){
                    is Int -> "int"
                    is Boolean -> "bool"
                    is Long -> "long"
                    is Short -> "short"
                    is String -> "str"
                    is Byte -> "byte"
                    is Location -> "location"
                    else -> "null".also { log(this::class.java, Log.WARN,  "Invalid attribute type for key: $key in PlayerSaver.kt Line 115") }
                }
                attr.put("type",type)
                attr.put("key",key)
                if(value is Byte){
                    val asString = Base64.getEncoder().encodeToString(byteArrayOf(value))
                    attr.put("value",asString)
                } else {
                    attr.put("value", if (value is Boolean) value else value.toString())
                }
                if(isExpirable){
                    attr.put("expirable",true)
                    attr.put("expiration-time",player.gameAttributes.keyExpirations[key].toString())
                }
                attrs.add(attr)
            }
            root.put("attributes",attrs)
        }
    }

    fun saveStatManager(root: JSONObject){
        val statistics = JSONArray()
        var index = 0
        root.put("statistics",statistics)
    }

    fun saveEmoteData(root: JSONObject){
        if(player.emoteManager.isSaveRequired) {
            val emoteData = JSONArray()
            player.emoteManager.emotes.map {
                emoteData.add(it.ordinal.toString())
            }
            root.put("emoteData",emoteData)
        }
    }

    fun saveIronManData(root: JSONObject){
        if(player.ironmanManager.mode != IronmanMode.NONE){
            root.put("ironManMode",player.ironmanManager.mode.ordinal.toString())
        }
    }

    fun saveAchievementData(root: JSONObject){
        val achievementData = JSONArray()
        player.achievementDiaryManager.diarys.map {
            val diary = JSONObject()
            val startedLevels = JSONArray()
            it.levelStarted.map {
                startedLevels.add(it)
            }
            diary.put("startedLevels",startedLevels)
            val completedLevels = JSONArray()
            it.taskCompleted.map {
                val level = JSONArray()
                it.map {
                    level.add(it)
                }
                completedLevels.add(level)
            }
            diary.put("completedLevels",completedLevels)
            val rewardedLevels = JSONArray()
            it.levelRewarded.map {
                rewardedLevels.add(it)
            }
            diary.put("rewardedLevels",rewardedLevels)
            val diaryCollector = JSONObject()
            diaryCollector.put(it.type.name, diary)
            achievementData.add(diaryCollector)
        }
        root.put("achievementDiaries",achievementData)
    }

    fun saveHouseData(root: JSONObject){
        val manager = player.houseManager
        val houseData = JSONObject()
        houseData.put("location",manager.location.ordinal.toString())
        houseData.put("style",manager.style.ordinal.toString())
        if(manager.hasServant()){
            val servant = JSONObject()
            servant.put("type",manager.servant.type.ordinal.toString())
            servant.put("uses",manager.servant.uses.toString())
            if(manager.servant.item != null){
                val item = JSONObject()
                item.put("id",manager.servant.item.id.toString())
                item.put("amount",manager.servant.item.amount.toString())
                servant.put("item",item)
            }
            servant.put("greet",manager.servant.isGreet)
            houseData.put("servant",servant)
        }
        val rooms = JSONArray()
        var z = 0
        for(room in player.houseManager.rooms){
            var x = 0
            for(xr in room){
                var y = 0
                for(yr in xr){
                    if(yr != null) {
                        val r = JSONObject()
                        r.put("z", z.toString())
                        r.put("x", x.toString())
                        r.put("y", y.toString())
                        r.put("properties", yr.properties.ordinal.toString())
                        r.put("rotation", yr.rotation.toInteger().toString())
                        val hotspots = JSONArray()
                        var hotspotIndex = 0
                        yr.hotspots.map {
                            if (it.decorationIndex > -1) {
                                val hotspot = JSONObject()
                                hotspot.put("hotspotIndex", hotspotIndex.toString())
                                hotspot.put("decorationIndex", it.decorationIndex.toString())
                                hotspots.add(hotspot)
                            }
                            hotspotIndex++
                        }
                        r.put("hotspots", hotspots)
                        rooms.add(r)
                    }
                    y++
                }
                x++
            }
            z++
        }
        houseData.put("rooms",rooms)
        root.put("houseData",houseData)
    }

    fun saveBankPinData(root: JSONObject){
        val bankPinManager = JSONObject()
        if(player.bankPinManager.hasPin()){
            bankPinManager.put("pin",player.bankPinManager.pin.toString())
        }
        bankPinManager.put("longRecovery",player.bankPinManager.isLongRecovery)
        if(player.bankPinManager.status.ordinal != 0){
            bankPinManager.put("status",player.bankPinManager.status.ordinal.toString())
        }
        if(player.bankPinManager.pendingDelay != -1L && player.bankPinManager.pendingDelay > System.currentTimeMillis()){
            bankPinManager.put("pendingDelay",player.bankPinManager.pendingDelay.toString())
        }
        if(player.bankPinManager.tryDelay > System.currentTimeMillis()){
            bankPinManager.put("tryDelay",player.bankPinManager.tryDelay.toString())
        }
        root.put("bankPinManager",bankPinManager)
    }

    fun saveFamiliarManager(root: JSONObject){
        val familiarManager = JSONObject()
        val petDetails = JSONObject()
        player.familiarManager.petDetails.map {
            val petId = it.key
            val petData = JSONArray()
            for (v in it.value) {
                val pet = JSONObject()
                pet.put("hunger",v.hunger.toString())
                pet.put("growth",v.growth.toString())
                petData.add(pet)
            }
            petDetails.put(petId.toString(), petData)
        }
        familiarManager.put("petDetails",petDetails)
        if(player.familiarManager.hasPet()){
            familiarManager.put("currentPet",(player.familiarManager.familiar as Pet).getItemId().toString())
        } else if (player.familiarManager.hasFamiliar()){
            val familiar = JSONObject()
            familiar.put("originalId",player.familiarManager.familiar.originalId.toString())
            familiar.put("ticks",player.familiarManager.familiar.ticks.toString())
            familiar.put("specialPoints",player.familiarManager.familiar.specialPoints.toString())
            if(player.familiarManager.familiar.isBurdenBeast && !(player.familiarManager.familiar as BurdenBeast).container.isEmpty){
                val familiarInventory = saveContainer((player.familiarManager.familiar as BurdenBeast).container)
                familiar.put("inventory",familiarInventory)
            }
            familiar.put("lifepoints",player.familiarManager.familiar.skills.lifepoints)
            familiarManager.put("familiar",familiar)

        }
        root.put("familiarManager",familiarManager)
    }

    fun saveMusicPlayer(root: JSONObject){
        val unlockedMusic = JSONArray()
        player.musicPlayer.unlocked.values.map {
            unlockedMusic.add(it.id.toString())
        }
        root.put("unlockedMusic",unlockedMusic)
    }

    fun savePlayerMonitor(root: JSONObject){

    }

    fun saveAutocast(root: JSONObject){
        player.properties.autocastSpell ?: return
        val spell = JSONObject()
        spell.put("book",player.properties.autocastSpell.book.ordinal.toString())
        spell.put("spellId",player.properties.autocastSpell.spellId.toString())
        root.put("autocastSpell",spell)
    }

    fun saveSavedData(root: JSONObject) {
        saveActivityData(root)
        saveQuestData(root)
        saveGlobalData(root)
    }

    fun saveGlobalData(root: JSONObject){
        val globalData = JSONObject()
        globalData.put("tutorialStage",player.savedData.globalData.tutorialStage.toString())
        globalData.put("homeTeleportDelay",player.savedData.globalData.homeTeleportDelay.toString())
        globalData.put("lumbridgeRope",player.savedData.globalData.hasTiedLumbridgeRope())
        globalData.put("apprentice",player.savedData.globalData.hasSpokenToApprentice())
        globalData.put("assistTime",player.savedData.globalData.assistTime.toString())
        val assistExperience = JSONArray()
        player.savedData.globalData.assistExperience.map {
            assistExperience.add(it.toString())
        }
        globalData.put("assistExperience",assistExperience)
        val strongholdRewards = JSONArray()
        player.savedData.globalData.strongHoldRewards.map {
            strongholdRewards.add(it)
        }
        globalData.put("strongHoldRewards",strongholdRewards)
        globalData.put("chatPing",player.savedData.globalData.chatPing.toString())
        globalData.put("tutorClaim",player.savedData.globalData.tutorClaim.toString())
        globalData.put("luthasTask",player.savedData.globalData.isLuthasTask)
        globalData.put("karamjaBananas",player.savedData.globalData.karamjaBananas.toString())
        globalData.put("silkSteal",player.savedData.globalData.silkSteal.toString())
        globalData.put("zafAmount",player.savedData.globalData.zaffAmount.toString())
        globalData.put("zafTime",player.savedData.globalData.zafTime.toString())
        globalData.put("fritzGlass",player.savedData.globalData.isFritzGlass)
        globalData.put("wydinEmployee",player.savedData.globalData.isWydinEmployee)
        globalData.put("draynorRecording",player.savedData.globalData.isDraynorRecording)
        globalData.put("geTutorial",player.savedData.globalData.isGeTutorial)
        globalData.put("essenceTeleporter",player.savedData.globalData.essenceTeleporter.toString())
        globalData.put("recoilDamage",player.savedData.globalData.recoilDamage.toString())
        globalData.put("doubleExpDelay",player.savedData.globalData.doubleExpDelay.toString())
        globalData.put("joinedMonastery",player.savedData.globalData.isJoinedMonastery)
        val readPlaques = JSONArray()
        player.savedData.globalData.readPlaques.map {
            readPlaques.add(it)
        }
        globalData.put("readPlaques",readPlaques)
        globalData.put("forgingUses",player.savedData.globalData.forgingUses.toString())
        globalData.put("ectoCharges",player.savedData.globalData.ectoCharges.toString())
        globalData.put("dropDelay",player.savedData.globalData.dropDelay.toString())
        val abyssData = JSONArray()
        player.savedData.globalData.abyssData.map {
            abyssData.add(it)
        }
        globalData.put("abyssData",abyssData)
        val rcDecays = JSONArray()
        player.savedData.globalData.rcDecays.map {
            rcDecays.add(it.toString())
        }
        globalData.put("rcDecays",rcDecays)
        globalData.put("disableDeathScreen",player.savedData.globalData.isDeathScreenDisabled)
        globalData.put("playerTestStage",player.savedData.globalData.playerTestStage.toString())
        globalData.put("charmingDelay",player.savedData.globalData.charmingDelay.toString())
        val travelLogs = JSONArray()
        player.savedData.globalData.travelLogs.map {
            travelLogs.add(it)
        }
        globalData.put("travelLogs",travelLogs)
        val godBooks = JSONArray()
        player.savedData.globalData.godBooks.map {
            godBooks.add(it)
        }
        globalData.put("godBooks",godBooks)
        globalData.put("disableNews",player.savedData.globalData.isDisableNews)
        val godPages = JSONArray()
        player.savedData.globalData.godPages.map {
            godPages.add(it)
        }
        globalData.put("godPages",godPages)
        globalData.put("overChargeDelay",player.savedData.globalData.overChargeDelay.toString())
        val bossCounters = JSONArray()
        player.savedData.globalData.bossCounters.map {
            bossCounters.add(it.toString())
        }
        globalData.put("bossCounters",bossCounters)
        globalData.put("barrowsLoots",player.savedData.globalData.barrowsLoots.toString())
        globalData.put("lootSharePoints",player.savedData.globalData.lootSharePoints.toString())
        globalData.put("lootShareDelay",player.savedData.globalData.lootShareDelay.toString())
        globalData.put("doubleExp",player.savedData.globalData.doubleExp.toString())
        globalData.put("globalTeleporterDelay",player.savedData.globalData.globalTeleporterDelay.toString())
        globalData.put("starSpriteDelay",player.savedData.globalData.starSpriteDelay.toString())
        globalData.put("runReplenishDelay",player.savedData.globalData.runReplenishDelay.toString())
        globalData.put("runReplenishCharges",player.savedData.globalData.runReplenishCharges.toString())
        globalData.put("lowAlchemyCharges",player.savedData.globalData.lowAlchemyCharges.toString())
        globalData.put("lowAlchemyDelay",player.savedData.globalData.lowAlchemyDelay.toString())
        globalData.put("magicSkillCapeDelay",player.savedData.globalData.magicSkillCapeDelay.toString())
        globalData.put("hunterCapeDelay",player.savedData.globalData.hunterCapeDelay.toString())
        globalData.put("hunterCapeCharges",player.savedData.globalData.hunterCapeCharges.toString())
        globalData.put("taskAmount",player.savedData.globalData.taskAmount.toString())
        globalData.put("taskPoints",player.savedData.globalData.taskPoints.toString())
        globalData.put("macroDisabled",player.savedData.globalData.macroDisabled)
        root.put("globalData",globalData)
    }

    fun saveQuestData(root: JSONObject){
        val questData = JSONObject()
        val draynorLever = JSONArray()
        player.savedData.questData.draynorLever.map {
            draynorLever.add(it)
        }
        questData.put("draynorLever",draynorLever)
        val dslayer = JSONArray()
        player.savedData.questData.dragonSlayer.map {
            dslayer.add(it)
        }
        questData.put("dragonSlayer",dslayer)
        questData.put("dragonSlayerPlanks",player.savedData.questData.dragonSlayerPlanks.toString())
        val demonSlayer = JSONArray()
        player.savedData.questData.demonSlayer.map {
            demonSlayer.add(it)
        }
        questData.put("demonSlayer",demonSlayer)
        val cooksAssistant = JSONArray()
        player.savedData.questData.cooksAssistant.map {
            cooksAssistant.add(it)
        }
        questData.put("cooksAssistant",cooksAssistant)
        questData.put("gardenerAttack",player.savedData.questData.isGardenerAttack)
        questData.put("talkedDrezel",player.savedData.questData.isTalkedDrezel)
        val desertTreasureNode = JSONArray()
        player.savedData.questData.desertTreasure.map {
            val item = JSONObject()
            item.put("id",it.id.toString())
            item.put("amount",it.amount.toString())
            desertTreasureNode.add(item)
        }
        questData.put("desertTreasureNode",desertTreasureNode)
        questData.put("witchsExperimentStage",player.savedData.questData.witchsExperimentStage.toString())
        questData.put("witchsExperimentKilled",player.savedData.questData.isWitchsExperimentKilled)
        root.put("questData",questData)
    }

    fun saveActivityData(root: JSONObject){
        val activityData = JSONObject()
        activityData.put("pestPoints",player.savedData.activityData.pestPoints.toString())
        activityData.put("warriorGuildTokens",player.savedData.activityData.warriorGuildTokens.toString())
        activityData.put("bountyHunterRate",player.savedData.activityData.bountyHunterRate.toString())
        activityData.put("bountyRogueRate",player.savedData.activityData.bountyRogueRate.toString())
        activityData.put("barrowKills",player.savedData.activityData.barrowKills.toString())
        val barrowBrothers = JSONArray()
        player.savedData.activityData.barrowBrothers.map {
            barrowBrothers.add(it)
        }
        activityData.put("barrowBrothers",barrowBrothers)
        activityData.put("barrowTunnelIndex",player.savedData.activityData.barrowTunnelIndex.toString())
        activityData.put("kolodionStage",player.savedData.activityData.kolodionStage.toString())
        val godCasts = JSONArray()
        player.savedData.activityData.godCasts.map {
            godCasts.add(it.toString())
        }
        activityData.put("godCasts",godCasts)
        activityData.put("kolodionBoss",player.savedData.activityData.kolodionBoss.toString())
        activityData.put("elnockSupplies",player.savedData.activityData.isElnockSupplies)
        activityData.put("lastBorkBattle",player.savedData.activityData.lastBorkBattle.toString())
        activityData.put("startedMta",player.savedData.activityData.isStartedMta)
        activityData.put("lostCannon",player.savedData.activityData.isLostCannon)
        val pizazzPoints = JSONArray()
        player.savedData.activityData.pizazzPoints.map {
            pizazzPoints.add(it.toString())
        }
        activityData.put("pizazzPoints",pizazzPoints)
        activityData.put("bonesToPeaches",player.savedData.activityData.isBonesToPeaches)
        activityData.put("solvedMazes",player.savedData.activityData.solvedMazes.toString())
        activityData.put("fogRating",player.savedData.activityData.fogRating.toString())
        activityData.put("borkKills",player.savedData.activityData.borkKills.toString())
        activityData.put("hardcoreDeath",player.savedData.activityData.hardcoreDeath)
        activityData.put("topGrabbed",player.savedData.activityData.isTopGrabbed)
        root.put("activityData",activityData)
    }

    fun saveSpellbook(root: JSONObject){
        root.put("spellbook",player.spellBookManager.spellBook.toString())
    }

    fun saveAppearance(root: JSONObject){
        val appearance = JSONObject()
        appearance.put("gender",player.appearance.gender.toByte().toString())
        val appearanceCache = JSONArray()
        player.appearance.appearanceCache.map {
            val bodyPart = JSONObject()
            bodyPart.put("look",it.look.toString())
            bodyPart.put("color",it.color.toString())
            appearanceCache.add(bodyPart)
        }
        appearance.put("appearance_cache", appearanceCache)
        root.put("appearance",appearance)
    }

    fun saveQuests(root: JSONObject){
        val quests = JSONObject()
        quests.put("points",player.questRepository.points.toString())
        val questStages = JSONArray()
        player.questRepository.questList.map {
            val quest = JSONObject()
            quest.put("questId",it.key.toString())
            quest.put("questStage",it.value.toString())
            questStages.add(quest)
        }
        quests.put("questStages",questStages)
        root.put("quests",quests)
    }

    fun saveSettings(root: JSONObject){
        val settings = JSONObject()
        settings.put("brightness",player.settings.brightness.toString())
        settings.put("musicVolume",player.settings.musicVolume.toString())
        settings.put("soundEffectVolume",player.settings.soundEffectVolume.toString())
        settings.put("areaSoundVolume",player.settings.areaSoundVolume.toString())
        settings.put("publicChatSetting",player.settings.publicChatSetting.toString())
        settings.put("privateChatSetting",player.settings.privateChatSetting.toString())
        settings.put("clanChatSetting",player.settings.clanChatSetting.toString())
        settings.put("tradeSetting",player.settings.tradeSetting.toString())
        settings.put("assistSetting",player.settings.assistSetting.toString())
        settings.put("runEnergy",player.settings.runEnergy.toString())
        settings.put("specialEnergy",player.settings.specialEnergy.toString())
        settings.put("attackStyle",player.settings.attackStyleIndex.toString())
        settings.put("singleMouse",player.settings.isSingleMouseButton)
        settings.put("disableChatEffects",player.settings.isDisableChatEffects)
        settings.put("splitPrivate",player.settings.isSplitPrivateChat)
        settings.put("acceptAid",player.settings.isAcceptAid)
        settings.put("runToggled",player.settings.isRunToggled)
        settings.put("retaliation",player.properties.isRetaliating)
        root.put("settings",settings)
    }

    fun saveSkills(root: JSONObject){
        val skills = JSONArray()
        for(i in 0 until 24){
            val skill = JSONObject()
            skill.put("id",i.toString())
            skill.put("static",player.skills.staticLevels[i].toString())
            skill.put("dynamic",player.skills.dynamicLevels[i].toString())
            if (i == Skills.HITPOINTS) {
                skill.put("lifepoints",player.skills.lifepoints.toString())
            }
            if (i == Skills.PRAYER) {
                skill.put("prayerPoints",player.skills.prayerPoints.toString())
            }
            skill.put("experience",player.skills.getExperience(i).toString())
            skills.add(skill)
        }
        root.put("skills",skills)
        root.put("totalEXP",player.skills.experienceGained.toString())
        root.put("exp_multiplier",player.skills.experienceMultiplier.toString())
        if(player.skills.combatMilestone > 0 || player.skills.skillMilestone > 0){
            val milestone = JSONObject()
            milestone.put("combatMilestone",player.skills.combatMilestone.toString())
            milestone.put("skillMilestone",player.skills.skillMilestone.toString())
            root.put("milestone",milestone)
        }
    }

    fun saveContainer(container: Container): JSONArray {
        val json = JSONArray()
        container.toArray().map{
            if (it != null) {
                val item = JSONObject()
                item.put("slot", it.slot.toString())
                item.put("id", it.id.toString())
                item.put("amount", it.amount.toString())
                item.put("charge", it.charge.toString())
                json.add(item)
            }
        }
        return json
    }

    fun saveCoreData(root: JSONObject){
        val coreData = JSONObject()
        val inventory = saveContainer(player.inventory)
        coreData.put("inventory",inventory)

        val bank = saveContainer(player.bankPrimary)
        coreData.put("bank",bank)

        val bankSecondary = saveContainer(player.bankSecondary)
        coreData.put("bankSecondary",bankSecondary)

        val bankTabs = JSONArray()
        for(i in player.bankPrimary.tabStartSlot.indices){
            val tab = JSONObject()
            tab.put("index",i.toString())
            tab.put("startSlot",player.bankPrimary.tabStartSlot[i].toString())
            bankTabs.add(tab)
        }
        coreData.put("bankTabs",bankTabs)

        val bankTabsSecondary = JSONArray()
        for(i in player.bankSecondary.tabStartSlot.indices){
            val tab = JSONObject()
            tab.put("index",i.toString())
            tab.put("startSlot",player.bankSecondary.tabStartSlot[i].toString())
            bankTabsSecondary.add(tab)
        }
        coreData.put("bankTabsSecondary",bankTabsSecondary)

        coreData.put("useSecondaryBank",player.useSecondaryBank)

        val equipment = saveContainer(player.equipment)
        coreData.put("equipment",equipment)

        val loctemp = player.location
        val locStr = "${loctemp.x},${loctemp.y},${loctemp.z}"
        coreData.put("location",locStr)

        val varpData = JSONArray()
        for ((index, value) in player.varpMap) {
            if (!(player.saveVarp[index] ?: false)) continue
            if (value == 0) continue

            val varpObj = JSONObject()
            varpObj["index"] = index.toString()
            varpObj["value"] = value.toString()
            varpData.add(varpObj)
        }
        coreData.put("varp", varpData)

        val timerData = JSONObject()
        player.timers.saveTimers(timerData)
        coreData.put("timers", timerData)

        root.put("core_data",coreData)
    }
}
