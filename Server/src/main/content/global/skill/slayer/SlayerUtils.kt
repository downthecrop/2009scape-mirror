package content.global.skill.slayer

import core.api.setVarp
import core.game.node.entity.combat.BattleState
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.SpellBookManager.SpellBook
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.skill.Skills
import core.tools.RandomFunction
import org.rs09.consts.Items

object SlayerUtils {
    fun generate(player: Player, master: Master): Master.Task?
    {
        val tasks: MutableList<Master.Task?> = ArrayList(10)
        val taskWeightSum = intArrayOf(0)
        master.tasks.stream().filter { task: Master.Task -> canBeAssigned(player, task.task) && task.task.combatCheck <= player.properties.currentCombatLevel }.forEach { task: Master.Task ->
            taskWeightSum[0] += task.weight
            tasks.add(task)
        }
        tasks.shuffle(RandomFunction.RANDOM)
        var rnd = RandomFunction.random(taskWeightSum[0])
        for (task in tasks) {
            if (rnd < task!!.weight) return task
            rnd -= task.weight
        }
        return null
    }

    fun canBeAssigned(player: Player, task: Tasks): Boolean
    {
        return player.getSkills().getLevel(Skills.SLAYER) >= task.levelReq && !SlayerManager.getInstance(player).flags.removed.contains(task) && task.hasQuestRequirements(player)
    }

    fun assign(player: Player, task: Master.Task, master: Master)
    {
        SlayerManager.getInstance(player).master = master
        SlayerManager.getInstance(player).task = task.task
        if (task.task_range[0] == null)
            SlayerManager.getInstance(player).amount = RandomFunction.random(master.default_assignment_range[0], master.default_assignment_range[1])
        else
            SlayerManager.getInstance(player).amount = RandomFunction.random(task.task_range[0], task.task_range[1])
        if (master == Master.DURADEL) {
            player.achievementDiaryManager.finishTask(player, DiaryType.KARAMJA, 2, 8)
        } else if (master == Master.VANNAKA) {
            player.achievementDiaryManager.finishTask(player, DiaryType.VARROCK, 1, 14)
        }
        setVarp(player, 2502, SlayerManager.getInstance(player).flags.taskFlags shr 4)
    }

    @JvmStatic
    fun hasBroadWeaponEquipped(player: Player, state: BattleState): Boolean {
        return (state.weapon != null && state.weapon.id == Items.LEAF_BLADED_SPEAR_4158 ||
            state.weapon != null && state.weapon.id == Items.LEAF_BLADED_SWORD_13290 ||
            state.ammunition != null && (state.ammunition.itemId == Items.BROAD_ARROW_4160 || state.ammunition.itemId == Items.BROAD_TIPPED_BOLTS_13280) ||
            state.spell != null && state.spell.spellId == 31 && player.spellBookManager
                .spellBook == SpellBook.MODERN.interfaceId
        )
    }

    @JvmStatic
    fun pluralise(str: String): String {
        return when (str) {
            "black bear" -> "bears"
            "cyclops" -> "cyclopes"
            "guard dog" -> "dogs"
            "dwarf" -> "dwarves"
            "elf warrior" -> "elves"
            "jelly" -> "jellies"
            "nechryael" -> str  // the plural and singular is the same
            "turoth" -> str // the plural and singular is the same
            "tzhaar-mej" -> "tzHaar"
            "werewolf" -> "werewolves"
            "wolf" -> "wolves"
            "kalphite worker" -> "kalphites"
            "scarab swarm" -> "scabarites"
            else -> str + "s"
        }
    }
}
