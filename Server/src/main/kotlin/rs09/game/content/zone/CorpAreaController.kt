package rs09.game.content.zone

import api.*
import core.game.node.entity.Entity
import core.game.node.entity.npc.bosses.corp.CorporealBeastNPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.summoning.familiar.Familiar
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneRestriction
import rs09.tools.secondsToTicks

class CorpAreaController : MapArea, TickListener {
    companion object {
        var activePlayers = ArrayList<Player>()
        var corpBeast: CorporealBeastNPC? = null
        var borders = ZoneBorders(2974, 4369, 3007, 4400)
    }

    override fun defineAreaBorders(): Array<ZoneBorders> {
        return arrayOf(borders)
    }

    override fun getRestrictions(): Array<ZoneRestriction> {
        return arrayOf(ZoneRestriction.GRAVES, ZoneRestriction.RANDOM_EVENTS)
    }

    override fun areaEnter(entity: Entity) {
        if (entity is Player) {
            activePlayers.add(entity)
        }
        else if (entity is Familiar) {
            entity.setAttribute("corp-time-remaining", secondsToTicks(10)) //Familiars last about 10 seconds, based on https://www.youtube.com/watch?v=kOd6q5Q5ZKI
        }
        else if (entity is CorporealBeastNPC) {
            corpBeast = entity
        }
    }

    override fun areaLeave(entity: Entity, logout: Boolean) {
        if (entity is Player) {
            activePlayers.remove(entity)
        }
    }

    override fun tick() {
        if (activePlayers.size == 0) {
            corpBeast?.let {
                it.skills.lifepoints = it.skills.maximumLifepoints
                if (it.darkEnergyCore != null) {
                    it.darkEnergyCore.clear()
                    it.darkEnergyCore = null
                }
            }
        }

        if (corpBeast?.isActive == true && activePlayers.isNotEmpty()) {
            for (player in activePlayers.toTypedArray()) {
                val familiar = player.familiarManager.familiar ?: continue
                val timeRemaining = getAttribute(familiar, "corp-time-remaining", -1)
                if (timeRemaining == 0 && borders.insideBorder(familiar)) {
                    val healBy = familiar.skills.lifepoints / 4
                    player.familiarManager.dismiss()
                    corpBeast?.skills?.heal(healBy)
                    sendMessage(familiar.owner, "The Beast devoured your familiar!")
                }
                setAttribute(familiar, "corp-time-remaining", timeRemaining - 1)
            }
        }
    }
}