package content.region.asgarnia.burthorpe.quest.deathplateau

import core.api.*
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.world.map.zone.ZoneBorders

class SecretWayLocation : MapArea {
    override fun defineAreaBorders(): Array<ZoneBorders> {
        return arrayOf(ZoneBorders(2866, 3609, 2866, 3609))
    }

    override fun areaEnter(entity: Entity) {
        if (entity is Player && getQuestStage(entity, DeathPlateau.questName) == 25) {
            sendPlayerDialogue(entity, "I think this is far enough, I can see Death Plateau and it looks like the trolls haven't found the path. I'd better go and tell Denulth.")
            setQuestStage(entity, DeathPlateau.questName, 26)
        }
    }
}