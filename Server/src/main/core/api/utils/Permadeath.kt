package core.api.utils

import content.global.skill.construction.HouseLocation
import content.minigame.blastfurnace.BFPlayerState
import content.minigame.blastfurnace.BlastFurnace
import core.ServerConstants
import core.api.isUsingSecondaryBankAccount
import core.api.teleport
import core.api.toggleBankAccount
import core.game.node.entity.player.Player
import core.game.node.entity.player.VarpManager
import core.game.node.entity.player.info.login.PlayerSaver
import core.game.node.entity.player.link.IronmanMode
import core.game.node.entity.player.link.SavedData
import core.game.node.entity.player.link.SpellBookManager
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.player.link.quest.QuestRepository
import core.game.node.entity.skill.Skills
import core.game.node.item.GroundItem
import core.game.node.item.GroundItemManager
import core.game.world.map.Location
import java.util.ArrayList

fun permadeath(target: Player) {
    teleport(target, Location.create(3094, 3107, 0))

    // Core
    target.inventory.clear()
    target.bank.clear()
    target.bankSecondary.clear()
    for (i in target.bankPrimary.tabStartSlot.indices) {
        target.bankPrimary.tabStartSlot[i] = 0
    }
    for (i in target.bankSecondary.tabStartSlot.indices) {
        target.bankSecondary.tabStartSlot[i] = 0
    }
    if (isUsingSecondaryBankAccount(target)) {
        toggleBankAccount(target)
    }
    target.equipment.clear()
    target.varpManager = VarpManager(target)
    target.varpMap.clear()
    target.saveVarp.clear()
    target.timers.clearTimers()

    // Skills
    target.skills = Skills(target)

    // Settings can be kept

    // Quests
    target.questRepository = QuestRepository(target)

    // Appearance doesn't matter because you're going to tutorial island anyway

    // Spellbook
    target.spellBookManager.setSpellBook(SpellBookManager.SpellBook.MODERN)

    // Saved data
    target.savedData = SavedData(target)

    // Autocast
    target.properties.autocastSpell = null

    // Player monitor is a no-op

    // Music player
    target.musicPlayer.clearUnlocked()

    // Familiar manager
    if (target.familiarManager.hasFamiliar()) {
        target.familiarManager.dismiss()
    }
    val petKeys = target.familiarManager.petDetails.keys.toList()
    for (key in petKeys) {
        target.familiarManager.removeDetails(key)
    }

    // Bank pin data
    target.bankPinManager.doCancelPin()

    // House data
    target.houseManager.createNewHouseAt(HouseLocation.NOWHERE)

    // Achievements
    for (type in DiaryType.values()) {
        val diary = target.achievementDiaryManager.getDiary(type)
        for (level in 0 until diary.levelStarted.size) {
            for (task in 0 until diary.taskCompleted[level].size) {
                diary.resetTask(target, level, task)
            }
        }
    }

    // Xp rate
    target.skills.experienceMultiplier = 1.0

    // Ironman data
    target.ironmanManager.mode = IronmanMode.NONE

    // Emote data
    target.emoteManager.emotes.clear()

    // Stat manager is a no-op

    // Attributes
    target.clearAttributes()

    // Pouches
    for (pouch in target.pouchManager.pouches.values) {
        pouch.container.clear()
        pouch.currentCap = pouch.capacity
        pouch.charges = pouch.maxCharges
        pouch.remakeContainer()
    }

    // Destroy any dropped items to prevent droptrading to yourself after death
    val droppedItems = ArrayList<GroundItem>();
    for (item in GroundItemManager.getItems()) {
        if (item.dropperUid == target.details.uid) {
            droppedItems.add(item)
        }
    }
    for (item in droppedItems) {
        GroundItemManager.destroy(item)
    }

    // grep -R savePlayer: jobs, treasure trails, brawling gloves, slayer manager, barcrawl, ge history will simply not get saved if we don't run the hooks
    // Only the Blast Furnace needs to be reset explicitly
    BlastFurnace.playerStates[target.details.uid] = BFPlayerState(target)

    // Sayonara
    PlayerSaver(target).save()
    target.clear()
}
