package content.region.fremennik.lighthouse.quest.horror.handlers

import core.api.clearHintIcon
import core.api.registerHintIcon
import core.api.transformNpc
import core.game.activity.Cutscene
import core.game.node.entity.player.Player
import core.game.world.map.Direction
import org.rs09.consts.NPCs

class DagannothCutscene(player: Player) : Cutscene(player) {
    override fun setup() {
        setExit(player.location.transform(0, 0, 0))
        addNPC(NPCs.JOSSIK_1335, 22, 25, Direction.NORTH_WEST)
        addNPC(DAGANNOTH_NECK, 15, 28, Direction.SOUTH_EAST)
    }

    override fun runStage(stage: Int) {
        when (stage) {
            0 -> {
                registerHintIcon(player, getNPC(DAGANNOTH_NECK)!!)
                moveCamera(25, 23, 500)
                rotateCamera(21, 26)
                timedUpdate(1)
            }

            1 -> {
                transformNpc(getNPC(DAGANNOTH_NECK)!!, DAGANNOTH_BODY, 10)
                timedUpdate(1)
            }

            2 -> {
                transformNpc(getNPC(DAGANNOTH_NECK)!!, DAGANNOTH_LEGS, 10)
                timedUpdate(2)
            }

            3 -> {
                transformNpc(getNPC(DAGANNOTH_NECK)!!, DAGANNOTH_FULL, 10)
                timedUpdate(2)
            }

            4 -> {
                move(getNPC(DAGANNOTH_NECK)!!, 19, 27)
                timedUpdate(3)
            }

            5 -> {
                endWithoutFade {
                    clearNPCs()
                    resetCamera()
                    clearHintIcon(player)
                    startBabyBossFight(player)
                }
            }
        }
    }

    companion object {
        private const val DAGANNOTH_NECK = NPCs.DAGANNOTH_1344 // lvl-100 Dagannoth (HFTD), in water, cutscene model, head sticking out
        private const val DAGANNOTH_BODY = NPCs.DAGANNOTH_1345 // lvl-100 Dagannoth (HFTD), in water, cutscene model, body swimming
        private const val DAGANNOTH_LEGS = NPCs.DAGANNOTH_1346 // lvl-100 Dagannoth (HFTD), exiting water, cutscene model, body walking out of water
        private const val DAGANNOTH_FULL = NPCs.DAGANNOTH_1347 // lvl-100 Dagannoth (HFTD), quest fight model, attackable
    }
}
