package content.region.fremennik.lighthouse.quest.horror.handlers

import core.api.clearHintIcon
import core.api.*
import core.api.registerHintIcon
import core.game.activity.Cutscene
import core.game.node.entity.player.Player
import core.game.world.map.Direction
import org.rs09.consts.*

class DagannothBossCutscene(player: Player) : Cutscene(player) {
    override fun setup() {
        setExit(player.location.transform(0, 0, 0))
        addNPC(NPCs.JOSSIK_1335, 22, 25, Direction.NORTH_WEST)
        addNPC(DAG_MOM_HEAD, 13, 34, Direction.EAST)
    }

    override fun runStage(stage: Int) {
        when (stage) {
            0 -> {
                registerHintIcon(player, getNPC(DAG_MOM_HEAD)!!)
                moveCamera(22, 42, 400)
                rotateCamera(5, 30)
                timedUpdate(1)
            }

            1 -> {
                move(getNPC(DAG_MOM_HEAD)!!, 17, 37)
                transformNpc(getNPC(DAG_MOM_HEAD)!!, DAG_MOM_HEAD_BODY, 10)
                timedUpdate(1)
            }

            2 -> {
                transformNpc(getNPC(DAG_MOM_HEAD)!!, DAG_MOM_HEAD_BODY_LEGS, 10)
                timedUpdate(2)
            }

            3 -> {
                transformNpc(getNPC(DAG_MOM_HEAD)!!, DAG_MOM_FULL, 10)
                timedUpdate(2)
            }

            4 -> {
                endWithoutFade {
                    clearNPCs()
                    resetCamera()
                    clearHintIcon(player)
                    startBossFight(player)
                }
            }
        }
    }

    companion object {
        private const val DAG_MOM_HEAD = NPCs.DAGANNOTH_MOTHER_1348
        private const val DAG_MOM_HEAD_BODY = NPCs.DAGANNOTH_MOTHER_1349
        private const val DAG_MOM_HEAD_BODY_LEGS = NPCs.DAGANNOTH_MOTHER_1350
        private const val DAG_MOM_FULL = NPCs.DAGANNOTH_MOTHER_1351
    }
}
