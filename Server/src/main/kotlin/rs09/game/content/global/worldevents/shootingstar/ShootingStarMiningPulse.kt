package rs09.game.content.global.worldevents.shootingstar

import api.*
import core.game.content.global.worldevents.shootingstar.ScoreboardManager
import core.game.node.scenery.Scenery
import core.game.node.entity.player.Player
import core.game.node.entity.skill.SkillPulse
import core.game.node.entity.skill.Skills
import core.game.node.entity.skill.gather.SkillingTool
import core.game.node.item.Item
import core.tools.RandomFunction
import org.rs09.consts.Items
import rs09.game.world.GameWorld
import rs09.game.world.repository.Repository
import rs09.tools.stringtools.colorize

/**
 * The pulse used to handle mining shooting stars.
 */
class ShootingStarMiningPulse(player: Player?, node: Scenery?, val star: ShootingStar) : SkillPulse<Scenery?>(player, node) {
    /**
     * The amount of ticks it takes to get star dust.
     */
    private var ticks = 0
    override fun start() {
        if(!star.isSpawned) return

        if(!player.isArtificial) {
            star.notifyNewPlayer(player)
        }
        super.start()
    }

    override fun stop() {
        super.stop()

        if(!player.isArtificial){
            star.notifyPlayerLeave(player)
        }
    }

    override fun checkRequirements(): Boolean {
        tool = SkillingTool.getPickaxe(player)
        if (!star.starObject.isActive || !star.isSpawned) {
            return false
        }
        //checks if the star has been discovered and if not, awards the bonus xp. Xp can be awarded regardless of mining level as per the wiki.
        if (!star.isDiscovered && !player.isArtificial) {
            val bonusXp = 75 * player.skills.getStaticLevel(Skills.MINING)
            player.incrementAttribute("/save:shooting-star:bonus-xp", bonusXp)
            Repository.sendNews(player.username + " is the discoverer of the crashed star near " + star.location + "!")
            player.sendMessage("You have ${player.skills.experienceMutiplier * player.getAttribute("shooting-star:bonus-xp", 0).toDouble()} bonus xp towards mining stardust.")
            ScoreboardManager.submit(player)
            star.isDiscovered = true
            return player.skills.getLevel(Skills.MINING) >= star.miningLevel
        }
        
        if (player.skills.getLevel(Skills.MINING) < star.miningLevel) {
            player.dialogueInterpreter.sendDialogue("You need a Mining level of at least " + star.miningLevel + " in order to mine this layer.")
            return false
        }
        if (tool == null) {
            player.packetDispatch.sendMessage("You do not have a pickaxe to use.")
            return false
        }
        if (player.inventory.freeSlots() < 1 && !player.inventory.contains(ShootingStarOptionHandler.STAR_DUST, 1)) {
            player.dialogueInterpreter.sendDialogue("Your inventory is too full to hold any more stardust.")
            return false
        }
        return true
    }

    override fun animate() {
        player.animate(tool.animation)
    }

    override fun reward(): Boolean {
        if (++ticks % 4 != 0) {
            return false
        }
        if (!checkReward()) {
            return false
        }
        if (GameWorld.settings?.isDevMode == true) {
            star.dustLeft = 1
        }
        star.decDust()

        val bonusXp = player.getAttribute("shooting-star:bonus-xp", 0).toDouble()
        var xp = star.level.exp.toDouble()
        if(bonusXp > 0) {
            val delta = Math.min(bonusXp, xp)
            player.incrementAttribute("/save:shooting-star:bonus-xp", (-delta).toInt())
            xp += delta;
            if(player.getAttribute("shooting-star:bonus-xp", 0) <= 0) {
                player.sendMessage("You have obtained all of your bonus xp from the star.")
            }
        }

        player.skills.addExperience(Skills.MINING, xp)
        if (ShootingStarOptionHandler.getStarDust(player) < 200) {
            player.inventory.add(Item(ShootingStarOptionHandler.STAR_DUST, 1))
        }
        if(!inInventory(player, Items.ANCIENT_BLUEPRINT_14651) && !inBank(player, Items.ANCIENT_BLUEPRINT_14651)){
            rollBlueprint(player)
        }
        return false
    }

    fun rollBlueprint(player: Player){
        val chance = when(star.level){
            ShootingStarType.LEVEL_9 -> 250
            ShootingStarType.LEVEL_8 -> 500
            ShootingStarType.LEVEL_7 -> 750
            ShootingStarType.LEVEL_6 -> 1000
            ShootingStarType.LEVEL_5 -> 2000
            ShootingStarType.LEVEL_4 -> 3000
            ShootingStarType.LEVEL_3 -> 4000
            ShootingStarType.LEVEL_2 -> 5000
            ShootingStarType.LEVEL_1 -> 10000
        }

        if(RandomFunction.roll(chance)){
            addItemOrDrop(player, Items.ANCIENT_BLUEPRINT_14651, 1)
            sendMessage(player, colorize("%RWhile mining the star you find an ancient-looking blueprint."))
            sendNews("${player.username} found an Ancient Blueprint while mining a shooting star!")
        }
    }

    override fun message(type: Int) {
        when (type) {
            0 -> player.packetDispatch.sendMessage("You swing your pickaxe at the rock...")
        }
    }

    /**
     * Checks if the player gets rewarded.
     * @return `True` if so.
     */
    private fun checkReward(): Boolean {
        val skill = Skills.MINING
        val level = 1 + player.skills.getLevel(skill) + player.familiarManager.getBoost(skill)
        val hostRatio: Double = Math.random() * (100.0 * star.level.rate)
        val clientRatio: Double = Math.random() * ((level - star.miningLevel) * (1.0 + tool.ratio))
        return hostRatio < clientRatio
    }
}
