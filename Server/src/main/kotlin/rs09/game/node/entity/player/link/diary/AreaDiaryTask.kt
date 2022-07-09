package rs09.game.node.entity.player.link.diary

import api.inBorders
import core.game.node.entity.player.Player
import core.game.world.map.zone.ZoneBorders

class AreaDiaryTask(
    val zoneBorders: ZoneBorders,
    val diaryLevel: DiaryLevel,
    val taskId: Int,
    private val condition: ((player: Player) -> Boolean)? = null
) {
    fun whenSatisfied(player: Player, then: () -> Unit) {
        var result = inBorders(player, zoneBorders)

        condition?.let {
            result = it(player)
        }

        if (result) then()
    }
}