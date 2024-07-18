package content.global.skill.agility.shortcuts.grapple

import core.api.*
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.scenery.Scenery
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import org.rs09.consts.Items

abstract class AbstractGrappleShortcut : InteractionListener {
    /**
     * Make sure that you have flagInstant in your listeners.
     * If you do not the player will run and try to touch the grapple point
     */
    private val VALID_CROSSBOWS = intArrayOf(
        Items.MITH_CROSSBOW_9181,
        Items.ADAMANT_CROSSBOW_9183,
        Items.RUNE_CROSSBOW_9185,
        Items.DORGESHUUN_CBOW_8880
    )

    protected abstract val REQUIREMENTS: HashMap<Int, Int>

    protected abstract val grappleStartLocation: Location
    protected abstract val grappleEndLocation: Location

    // use lazy so that the skill requirements can be populated after the child builds them
    private val requirementString1: String by lazy {
        "You need at least " +
            REQUIREMENTS[Skills.AGILITY] + " " + Skills.SKILL_NAME[Skills.AGILITY] + ", " +
            REQUIREMENTS[Skills.RANGE] + " " + Skills.SKILL_NAME[Skills.RANGE]  + ","
    }
    private val requirementString2: String by lazy {
             "and " +
            REQUIREMENTS[Skills.STRENGTH] + " " + Skills.SKILL_NAME[Skills.STRENGTH] + " to use this shortcut."
    }


    // What needs to have its model changed during a grapple
    protected abstract val grappleScenery: List<Scenery?>

    // What animation to use when getting the player across
    protected abstract val animation: Animation
    // How long should the animation last (in ticks)
    protected abstract val animationDuration: Int

    protected abstract fun animation(animationStage: Int, player: Player): Boolean


    // The message that can appear if there should be one after grappling
    protected val message: String? = null

    private fun getRequirementString(): Array<String> {
        val requirementString = arrayOf(requirementString1, requirementString2)
        return requirementString
    }

    private fun doesPlayerHaveRequiredItemsEquipped(player: Player): Boolean {
        return inEquipment(player, Items.MITH_GRAPPLE_9419) && anyInEquipment(player, *VALID_CROSSBOWS)
    }

    private fun doesPlayerHaveRequiredLevels(player: Player): Boolean {
        for ((skill, requiredLevel) in REQUIREMENTS) {
            if (!hasLevelDyn(player, skill, requiredLevel)) {
                return false
            }
        }
        return true
    }

    protected open fun isPlayerInRangeToGrapple(player: Player, startLoc: Location, range: Int): Boolean {
        return inBorders(player, startLoc.x - range, startLoc.y - range,
            startLoc.x + range, startLoc.y + range)
    }

    /**
     * See if the [player] is close enough to the [startLoc] (based on [range] to
     * try and grapple. This will return false if the [player] is too far away,
     * if the player does not have the right levels or if the player does not have
     * the correct gear equipped.
     */
    protected fun canGrapple(player: Player, startLoc: Location, range: Int): Boolean {
        if (isPlayerInRangeToGrapple(player, startLoc, range)) {
            forceWalk(player, startLoc, "smart")
        } else {
            // todo should this be "you are too far away" or something like that?
            sendMessage(player, "Nothing interesting happens.")
            return false
        }

        if (!doesPlayerHaveRequiredItemsEquipped(player)) {
            sendDialogue(player, "You need a Mithril crossbow and a Mithril grapple in order to do this.")
            return false
        }

        if (!doesPlayerHaveRequiredLevels(player)) {
            sendDialogueLines(player,
                *getRequirementString()
            )
            return false
        }
        return true
    }

    protected fun grapple(player: Player, message: String?): Boolean {
        closeAllInterfaces(player)
        lock(player, animationDuration)
        // TODO is this right? should we force the player to cross?
        queueScript(player, strength = QueueStrength.SOFT) { stage: Int ->
            if (animation(stage, player)){
                // We're done with the animation
                return@queueScript stopExecuting(player)
            }
            else{
                return@queueScript delayScript(player, 1)
            }
        }

        message?.let{
            player.sendMessage(message)
        }
        return true
    }


    /**
     * If an achievement diary can be updated it should be done here
     */
    protected open fun updateDiary(player: Player): Boolean{
        return false
    }

}
