package core.game.content.global.worldevents.shootingstar

import core.game.node.`object`.GameObject
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.GameWorld
import core.game.world.repository.Repository
import core.game.node.entity.skill.SkillPulse
import core.game.node.entity.skill.Skills
import core.game.node.entity.skill.gather.SkillingTool

/**
 * The pulse used to handle mining shooting stars.
 */
class ShootingStarMiningPulse(player: Player?, node: GameObject?, val star: ShootingStar) : SkillPulse<GameObject?>(player, node) {
    /**
     * The amount of ticks it takes to get star dust.
     */
    private var ticks = 0
    override fun start() {
        if(!star.isSpawned) return
        super.start()
    }

    override fun checkRequirements(): Boolean {
        tool = SkillingTool.getPickaxe(player)
        if (!star.starObject.isActive || !star.isSpawned) {
            return false
        }
        //checks if the star has been discovered and if not, awards the bonus xp. Xp can be awarded regardless of mining level as per the wiki.
        if (!star.isDiscovered) {
            player.skills.addExperience(Skills.MINING, 75 * player.skills.getStaticLevel(Skills.MINING).toDouble())
            Repository.sendNews(player.username + " is the discoverer of the crashed star near " + star.location + "!")
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
        player.skills.addExperience(Skills.MINING,star.level.exp.toDouble())
        if (ShootingStarOptionHandler.getStarDust(player) < 200) {
            player.inventory.add(Item(ShootingStarOptionHandler.STAR_DUST, 1))
        }
        return false
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