package content.global.ame.events.strangeplant

import content.global.ame.RandomEventNPC
import core.api.*
import core.api.utils.WeightBasedTable
import core.game.interaction.QueueStrength
import core.game.node.entity.npc.NPC
import core.game.world.update.flag.context.Animation
import core.tools.minutesToTicks
import core.tools.secondsToTicks
import org.rs09.consts.NPCs

class StrangePlantNPC(override var loot: WeightBasedTable? = null) : RandomEventNPC(NPCs.STRANGE_PLANT_407) {
    private val strangePlantGrowAnim = Animation(348)
    private val strangePlantTransformAnim = Animation(351)
    private val strangePlantPickedAnim = Animation(350)
    private var fruitPicked = false
    private var transformed = false
    private var attacking = false

    override fun init() {
        spawnLocation = getPathableCardinal(player, player.location)
        super.init()
        ticksLeft = minutesToTicks(1)
        animate(this, strangePlantGrowAnim)
        queueScript(this, strangePlantGrowAnim.duration + 2, QueueStrength.SOFT) {
            setAttribute(this, "fruit-grown", true)
            return@queueScript stopExecuting(this)
        }
    }

    override fun tick() {
        super.tick()
        if (getAttribute(this, "fruit-picked", false) && !fruitPicked) {
            fruitPicked = true
            ticksLeft = secondsToTicks(60)
            queueScript(this, 1, QueueStrength.SOFT) { stage: Int ->
                when (stage) {
                    0 -> {
                        this.animate(strangePlantPickedAnim)
                        return@queueScript delayScript(this, strangePlantPickedAnim.duration - 1)
                    }
                    1 -> {
                        this.isInvisible = true
                        terminate()
                        return@queueScript stopExecuting(this)
                    }
                    else -> return@queueScript stopExecuting(this)
                }
            }
        }
        if (ticksLeft <= secondsToTicks(10)) {
            ticksLeft = secondsToTicks(10)
            if (!attacking) {
                attacking = true
                queueScript(this, 1, QueueStrength.SOFT) { stage: Int ->
                    when (stage) {
                        0 -> {
                            animate(this, strangePlantTransformAnim)
                            return@queueScript delayScript(this, strangePlantTransformAnim.duration)
                        }
                        1 -> {
                            this.transform(NPCs.STRANGE_PLANT_408)
                            this.behavior = StrangePlantBehavior()
                            this.attack(player)
                            transformed = true
                            return@queueScript stopExecuting(this)
                        }
                        else -> return@queueScript stopExecuting(this)
                    }
                }
            }
            if (transformed && !this.inCombat())
                this.attack(player)
        }
    }

    override fun follow() {
        if (transformed)
            super.follow()
    }

    override fun talkTo(npc: NPC) {
    }
}