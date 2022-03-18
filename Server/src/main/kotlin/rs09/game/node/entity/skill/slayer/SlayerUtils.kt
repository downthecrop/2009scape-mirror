package rs09.game.node.entity.skill.slayer

import core.game.node.entity.combat.BattleState
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.SpellBookManager.SpellBook
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.skill.Skills
import core.game.node.entity.skill.slayer.Master
import core.game.node.entity.skill.slayer.Tasks
import core.tools.RandomFunction
import org.rs09.consts.Items
import java.util.ArrayList

object SlayerUtils {
    fun generate(player: Player, master: Master): Tasks?
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
            if (rnd < task!!.weight) return task.task
            rnd -= task.weight
        }
        return null
    }

    fun canBeAssigned(player: Player, task: Tasks): Boolean
    {
        return player.getSkills().getLevel(Skills.SLAYER) >= task.levelReq && !player.slayer.flags.removed.contains(task)
    }

    fun assign(player: Player, task: Tasks, master: Master)
    {
        player.slayer.master = master
        player.slayer.task = task
        player.slayer.amount = RandomFunction.random(master.assignment_range[0], master.assignment_range[1])
        if (master == Master.DURADEL) {
            player.achievementDiaryManager.finishTask(player, DiaryType.KARAMJA, 2, 8)
        } else if (master == Master.VANNAKA) {
            player.achievementDiaryManager.finishTask(player, DiaryType.VARROCK, 1, 14)
        }
        player.varpManager.get(2502).setVarbit(0, player.slayer.flags.taskFlags shr 4).send(player)
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
}