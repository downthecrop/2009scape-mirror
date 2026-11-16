package content.region.kandarin.seers.quest.elementalworkshop2

import content.data.Quests
import content.region.kandarin.seers.quest.elementalworkshop2.EW2Listeners.Companion.toInstance
import content.region.kandarin.seers.quest.elementalworkshop2.EW2Session.Companion.EW2_REGION
import content.region.kandarin.seers.quest.elementalworkshop2.ElementalWorkshop2.Companion.EW2_EXTRACTOR_GUN
import core.api.*
import core.game.activity.Cutscene
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import org.rs09.consts.Animations
import org.rs09.consts.Graphics
import org.rs09.consts.Sounds

/**
 * The extractor hat cutscene in EW2
 */

class EW2Cutscene(player: Player, good: Boolean) : Cutscene(player) {
    override fun setup() {
        setExit(toInstance(player, Location(1961, 5150, 0)))
        loadRegion(EW2_REGION)
    }

    val successfulExtract = good

    override fun runStage(stage: Int) {
        when(stage) {
            0 -> {
                teleport(player, 41, 30)
                moveCamera(37, 25, 350)
                rotateCamera(42, 29)
                timedUpdate(0)
            }

            1 -> {
                face(player, player.location.transform(1, 0, 0))
                timedUpdate(0)
            }

            2 -> {
                teleport(player, 42, 30)
                if (successfulExtract) {
                    visualize(player, Animations.HUMAN_USE_EXTRACTOR_HAT_CHAIR_4884, Graphics.EXTRACTOR_HAT_CHAIR_807)
                    playAudio(player, Sounds.EW2_EXTRACTOR_OPERATION_3166)
                    val gun = getScenery(base.transform(42, 28, 0))
                    if (gun != null) animateScenery(gun, Animations.EW2_EXTRACTOR_GUN_4897)
                    timedUpdate(Animation(Animations.HUMAN_USE_EXTRACTOR_HAT_CHAIR_4884).duration)
                } else {
                    visualize(player, Animations.HUMAN_IMPROPERLY_USE_EXTRACTOR_HAT_CHAIR_4885, Graphics.IMPROPERLY_EXTRACTOR_HAT_CHAIR_808)
                    playAudio(player, Sounds.EW2_EXTRACTOR_ZAP_3173)
                    timedUpdate(Animation(Animations.HUMAN_IMPROPERLY_USE_EXTRACTOR_HAT_CHAIR_4885).duration)
                }
            }

            3 -> {
                teleport(player, 41, 30)
                if (successfulExtract) {
                    // make a mind bar
                    setVarbit(player, EW2_EXTRACTOR_GUN, 2, true)

                    // drain magic
                    adjustLevel(player, Skills.MAGIC, -20)
                    sendMessage(player, "You feel magically weakened.")

                    // advance quest
                    if (getQuestStage(player, Quests.ELEMENTAL_WORKSHOP_II) <= 9) {
                        setQuestStage(player, Quests.ELEMENTAL_WORKSHOP_II, 10)
                    }
                } else {
                    impact(player, 4)
                    sendMessage(player, "The extractor hat shocks you!")
                }
                timedUpdate(1)
            }

            4 -> {
                endWithoutFade {
                    EW2Session(player).start(Location(1961, 5150, 0))
                    setExit(toInstance(player, Location(1961, 5150, 0)))
                }
            }
        }
    }
}