package content.minigame.allfiredup

import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.world.update.flag.context.Animation
import org.rs09.consts.Items
import core.game.interaction.InteractionListener
import core.game.interaction.IntType
import content.data.Quests
import core.api.delayScript
import core.api.getAttribute
import core.api.getOrStartTimer
import core.api.getQuestStage
import core.api.lock
import core.api.queueScript
import core.api.removeAttribute
import core.api.sendMessage
import core.api.setAttribute
import core.api.setQuestStage
import core.api.stopExecuting
import core.game.interaction.QueueStrength

private val VALID_LOGS = intArrayOf(Items.LOGS_1511, Items.OAK_LOGS_1521,Items.WILLOW_LOGS_1519,Items.MAPLE_LOGS_1517,Items.YEW_LOGS_1515,Items.MAGIC_LOGS_1513)
private val FILL_ANIM = Animation(9136)
private val LIGHT_ANIM = Animation(7307)

/**
 * Handles interactions for beacons
 * @author Ceikry, Player Name
 */
class AFUBeaconListeners : InteractionListener {

    override fun defineListeners() {
        on(IntType.SCENERY,"add-logs","light"){ player, node ->
            val beacon = AFUBeacon.forLocation(node.location)
            val questComplete = player.questRepository.isComplete(Quests.ALL_FIRED_UP)
            val questStage = player.questRepository.getStage(Quests.ALL_FIRED_UP)

            if ((beacon != AFUBeacon.RIVER_SALVE && beacon != AFUBeacon.RAG_AND_BONE && !questComplete)
                || (beacon == AFUBeacon.RIVER_SALVE && questStage < 20 && !questComplete)
                || (beacon == AFUBeacon.RAG_AND_BONE && questStage < 50 && !questComplete)) {
                player.dialogueInterpreter.sendDialogues(player, core.game.dialogue.FacialExpression.THINKING, "I probably shouldn't mess with this.")
                return@on true
            }
            player.debug(beacon.getState(player).name)

            when (beacon.getState(player)) {
                BeaconState.EMPTY -> fillBeacon(player, beacon, questComplete)

                BeaconState.DYING -> restoreBeacon(player, beacon, questComplete)

                BeaconState.FILLED -> lightBeacon(player, beacon, questComplete)

                BeaconState.LIT, BeaconState.WARNING -> {
                    player.debug("INVALID BEACON STATE")
                }
            }
            return@on true
        }
    }

    fun fillBeacon(player: Player, beacon: AFUBeacon, questComplete: Boolean){

        when(beacon){
            AFUBeacon.MONASTERY -> {
                if(player.skills.getLevel(Skills.PRAYER) < 31){
                    player.dialogueInterpreter.sendDialogues(NPC(beacon.keeper).getShownNPC(player), core.game.dialogue.FacialExpression.ANGRY,"You must join the monastery to light this beacon!")
                    return
                }
            }

            AFUBeacon.GWD -> {
                if(!AFURepairClimbHandler.isRepaired(player, beacon)){
                    player.dialogueInterpreter.sendDialogue("You must repair the windbreak before you","can light this beacon.")
                    return
                }
            }

            AFUBeacon.GOBLIN_VILLAGE -> {
                if(!player.questRepository.isComplete(Quests.THE_LOST_TRIBE)){
                    player.dialogueInterpreter.sendDialogues(NPC(beacon.keeper).getShownNPC(player), core.game.dialogue.FacialExpression.THINKING,"We no trust you outsider. You no light our beacon.","(Complete Lost Tribe to use this beacon.)")
                    return
                }
            }

            else -> {}

        }

        if(player.skills.getLevel(Skills.FIREMAKING) < beacon.fmLevel){
            player.dialogueInterpreter.sendDialogue("You need ${beacon.fmLevel} Firemaking to light this beacon.")
            return
        }

        val logs = getLogs(player,20)
        if (logs.id != 0 && player.inventory.remove(logs)) {
            lock(player, FILL_ANIM.duration)
            queueScript(player, 0, QueueStrength.SOFT) { stage ->
                if (stage == 1) {
                    player.animator.animate(FILL_ANIM)
                    return@queueScript delayScript(player, FILL_ANIM.duration)
                }
                setAttribute(player, "/save:beacon:${beacon.ordinal}:logsId", logs.id)
                beacon.fillWithLogs(player)
                return@queueScript stopExecuting(player)
            }
        } else {
            player.dialogueInterpreter.sendDialogue("You need some logs to do this.")
        }
    }

    fun lightBeacon(player: Player, beacon: AFUBeacon, questComplete: Boolean) {
        if(player.inventory.contains(Items.TINDERBOX_590,1)){
            lock(player, LIGHT_ANIM.duration)
            queueScript(player, 0, QueueStrength.SOFT) { stage ->
                if (stage == 1) {
                    player.animator.animate(LIGHT_ANIM)
                    return@queueScript delayScript(player, LIGHT_ANIM.duration)
                }
                val logsId = getAttribute(player, "/save:beacon:${beacon.ordinal}:logsId", Items.LOGS_1511)
                removeAttribute(player, "/save:beacon:${beacon.ordinal}:logsId")
                beacon.light(player)
                if (!questComplete) {
                    setQuestStage(player, Quests.ALL_FIRED_UP, getQuestStage(player, Quests.ALL_FIRED_UP) + 10)
                    return@queueScript stopExecuting(player)
                }
                val timer = getOrStartTimer<AFUTimer>(player)
                timer.addTime(beacon.ordinal, logsId, 20)
                val lit = timer.getLitBeacons()
                if (lit == 6 && !player.hasFireRing()) {
                    sendMessage(player, "Congratulations on lighting 6 beacons at once! King Roald has something for you.")
                    setAttribute(player, "/save:afu-mini:ring", true)
                }
                if (lit == 10 && !player.hasFlameGloves()) {
                    sendMessage(player, "Congratulations on lighting 10 beacons at once! King Roald has something for you.")
                    setAttribute(player, "/save:afu-mini:gloves", true)
                }
                if (lit == 14 && !player.hasInfernoAdze()) {
                    sendMessage(player, "Congratulations on lighting all 14 beacons! King Roald has something special for you.")
                    setAttribute(player, "/save:afu-mini:adze", true)
                }
                val xp = beacon.experience + timer.getBonusExperience()
                player.skills.addExperience(Skills.FIREMAKING, xp)
                return@queueScript stopExecuting(player)
            }
        } else {
            player.dialogueInterpreter.sendDialogue("You need a tinderbox to light this.")
        }
    }

    fun restoreBeacon(player: Player, beacon: AFUBeacon, questComplete: Boolean){
        val logs = getLogs(player, 5)
        if (logs.id != 0 && player.inventory.remove(logs)) {
            lock(player, FILL_ANIM.duration)
            queueScript(player, 0, QueueStrength.SOFT) { stage ->
                if (stage == 1) {
                    player.animator.animate(LIGHT_ANIM)
                    return@queueScript delayScript(player, LIGHT_ANIM.duration)
                }
                val backupLogsId = getAttribute(player, "/save:beacon:${beacon.ordinal}:backupLogsId", Items.LOGS_1511)
                removeAttribute(player, "/save:beacon:${beacon.ordinal}:backupLogsId")
                beacon.light(player)
                if (!questComplete) {
                    setQuestStage(player, Quests.ALL_FIRED_UP, 80)
                    return@queueScript stopExecuting(player)
                }
                val timer = getOrStartTimer<AFUTimer>(player)
                timer.addTime(beacon.ordinal, backupLogsId, 5)
                return@queueScript stopExecuting(player)
            }
        } else {
            player.dialogueInterpreter.sendDialogue("You need some logs to do this.")
        }
    }

    fun getLogs(player: Player, amount: Int): Item{
        var logId = 0
        for (log in VALID_LOGS) if (player.inventory.getAmount(log) >= amount) {logId = log; break}
        return Item(logId,amount)
    }

    fun Player.hasFireRing(): Boolean{
        return inventory.containsItem(Item(Items.RING_OF_FIRE_13659)) || bank.containsItem(Item(Items.RING_OF_FIRE_13659)) || equipment.containsItem(Item(Items.RING_OF_FIRE_13659))
    }

    fun Player.hasFlameGloves(): Boolean{
        return inventory.containsItem(Item(Items.FLAME_GLOVES_13660)) || bank.containsItem(Item(Items.FLAME_GLOVES_13660)) || equipment.containsItem(Item(Items.FLAME_GLOVES_13660))
    }

    fun Player.hasInfernoAdze(): Boolean{
        return inventory.containsItem(Item(Items.INFERNO_ADZE_13661)) || bank.containsItem(Item(Items.INFERNO_ADZE_13661)) || equipment.containsItem(Item(Items.INFERNO_ADZE_13661))
    }

}
