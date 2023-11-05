package content.global.skill.runecrafting

import content.region.misthalin.varrock.diary.VarrockAchivementDiary.Companion.EasyTasks.ENTER_EARTH_ALTAR
import core.api.*
import core.game.container.impl.EquipmentContainer.SLOT_HAT
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.item.Item
import core.game.requirement.QuestReq
import core.game.requirement.QuestRequirements.*
import core.game.system.task.Pulse
import core.game.world.update.flag.context.Animation

class MysteriousRuinListener : InteractionListener {

    private val animation = Animation(827)
    private val allowedUsed = arrayOf(1438, 1448, 1444, 1440, 1442, 5516, 1446, 1454, 1452, 1462, 1458, 1456, 1450, 1460).toIntArray()
    private val allowedWith = allRuins()
    private val nothingInteresting = "Nothing interesting happens"

    override fun defineListeners() {
        onUseWith(IntType.SCENERY, allowedUsed, *allowedWith) { player, used, with ->
            return@onUseWith handleTalisman(player, used, with)
        }
        on(allowedWith, IntType.SCENERY, "enter") { player, node ->
            return@on handleTiara(player, node)
        }
    }

    private fun allRuins(): IntArray {
        return MysteriousRuin
            .values()
            .flatMap { ruins -> ruins.`object`.asList() }
            .toIntArray()
    }

    private fun handleTalisman(player: Player, used: Node, with: Node): Boolean {
        val ruin = MysteriousRuin.forObject(with.asScenery())
        if (!checkQuestCompletion(player, ruin)) {
            return true
        }

        val talisman = Talisman.forItem(used.asItem())
        if (talisman != ruin.talisman && talisman != Talisman.ELEMENTAL) {
            sendMessage(player, nothingInteresting)
            return false
        }
        if (talisman == Talisman.ELEMENTAL && (ruin.talisman != Talisman.AIR && ruin.talisman != Talisman.WATER && ruin.talisman != Talisman.FIRE && ruin.talisman != Talisman.EARTH)) {
            sendMessage(player, nothingInteresting)
            return false
        }

        teleportToRuinTalisman(player, used.asItem(), ruin)
        return true
    }

    private fun handleTiara(player: Player, node: Node): Boolean {
        val ruin = MysteriousRuin.forObject(node.asScenery())

        if (!checkQuestCompletion(player, ruin)) {
            return true
        }

        val tiara = Tiara.forItem(player.equipment.get(SLOT_HAT))
        if (tiara == null || tiara != ruin.tiara) {
            sendMessage(player, nothingInteresting)
            return false
        }

        submitTeleportPulse(player, ruin, 0)
        return true
    }

    private fun checkQuestCompletion(player: Player, ruin: MysteriousRuin): Boolean {
        return when (ruin) {
            MysteriousRuin.DEATH -> hasRequirement(player, QuestReq(MEP_2), true)
            MysteriousRuin.BLOOD -> hasRequirement(player, QuestReq(SEERGAZE), true)
            else -> hasRequirement(player, QuestReq(RUNE_MYSTERIES), true)
        }
    }

    private fun teleportToRuinTalisman(player: Player, talisman: Item, ruin: MysteriousRuin) {
        lock(player, 4)
        animate(player, animation)
        sendMessage(player, "You hold the ${talisman.name} towards the mysterious ruins.")
        submitTeleportPulse(player, ruin, 3)
    }

    private fun submitTeleportPulse(player: Player, ruin: MysteriousRuin, delay: Int) {
        sendMessage(player, "You feel a powerful force take hold of you.")
        submitWorldPulse(object : Pulse(delay, player) {
            override fun pulse(): Boolean {
                teleport(player, ruin.end)
                if (ruin == MysteriousRuin.EARTH) {
                    player.achievementDiaryManager.finishTask(player, DiaryType.VARROCK, 0, ENTER_EARTH_ALTAR)
                }
                return true
            }
        })
    }

}