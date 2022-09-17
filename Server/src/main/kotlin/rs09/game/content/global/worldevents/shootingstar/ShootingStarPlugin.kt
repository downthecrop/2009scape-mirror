package rs09.game.content.global.worldevents.shootingstar

import api.*
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.TeleportManager
import core.game.node.entity.skill.Skills
import org.json.simple.JSONObject
import org.rs09.consts.Items
import org.rs09.consts.Scenery
import rs09.ServerStore
import rs09.ServerStore.Companion.getBoolean
import rs09.game.content.dialogue.DialogueFile
import rs09.game.interaction.InteractionListener
import rs09.game.interaction.IntType
import rs09.game.system.SystemLogger
import rs09.game.system.command.Privilege
import rs09.game.world.GameWorld
import rs09.tools.secondsToTicks

class ShootingStarPlugin : LoginListener, InteractionListener, TickListener, Commands, StartupListener {
    override fun login(player: Player) {
        if(star.isSpawned && !star.spriteSpawned)
            sendMessage(player, "<img=12><col=CC6600>News: A shooting star (Level ${star.level.ordinal + 1}) has just crashed near the ${star.location}!")
    }

    override fun tick() {
        ++star.ticks

        val maxDelay = tickDelay + (tickDelay / 3)
        if(star.ticks > maxDelay && star.spriteSpawned){
            star.clearSprite()
        }

        if ((star.ticks >= tickDelay && !star.spriteSpawned) || (!star.isSpawned && !star.spriteSpawned)) {
            star.fire()
        }
    }

    override fun defineListeners() {
        on(Scenery.SHOOTING_STAR_NOTICEBOARD_38669, IntType.SCENERY, "read") { player, _ ->
            var index = 0
            scoreboardEntries.forEach { entry ->
                val timeElapsed = secondsToTicks(GameWorld.ticks - entry.time) / 60
                setInterfaceText(player, "$timeElapsed minutes ago", scoreboardIface, index + 6)
                setInterfaceText(player, entry.player, scoreboardIface, index + 11)
                ++index
            }
            openInterface(player, scoreboardIface)
            return@on true
        }

        on(SHOOTING_STARS, IntType.SCENERY, "mine"){ player, _ ->
            star.mine(player)
            return@on true
        }

        on(SHOOTING_STARS, IntType.SCENERY, "prospect"){ player, _ ->
            star.prospect(player)
            return@on true
        }

        on(RING, IntType.ITEM, "rub", "operate"){player, node ->
            if(getRingStoreFile().getBoolean(player.username.toLowerCase())){
                sendDialogue(player, "The ring is still recharging.")
                return@on true
            }

            val condition: (Player) -> Boolean = when(star.location.toLowerCase()){
                "canifis bank" -> { p -> p.questRepository.isComplete("Priest in Peril")}
                //"Burgh de Rott bank" -> { p -> p.questRepository.isComplete("Priest in Peril")} // for now, require this as it is in Morytania. remove when appropriate quests added - Crash
                "crafting guild" -> {p -> hasLevelStat(p, Skills.CRAFTING, 40) }
                "lletya bank" -> {p -> p.questRepository.isComplete("Roving Elves")}
                "jatizso mine" -> {p -> p.questRepository.isComplete("Fremennik Trials")}
                "south crandor mining site" -> {p -> p.questRepository.isComplete("Dragon Slayer")}
                else -> {_ -> true}
            }

            if(!condition.invoke(player) || player.skullManager.isWilderness){
                sendDialogue(player, "Magical forces prevent your teleportation.")
                return@on true
            }

            val shouldWarn = when(star.location){
                "North Edgeville mining site",
                "Southern wilderness mine",
                "Wilderness Volcano bank",
                "Wilderness hobgoblin mine",
                "Pirates' Hideout mine",
                "Lava Maze mining site",
                "Mage Arena bank" -> true
                else -> false
            }

            openDialogue(player, RingDialogue(shouldWarn, star))

            return@on true
        }
    }

    override fun defineCommands() {
        define("tostar", Privilege.ADMIN) { player, _ ->
            teleport(player, star.starObject.location.transform(1,1,0))
        }

        define("submit", Privilege.ADMIN) { _, _ ->
            star.fire()
        }

        define("resetsprite", Privilege.ADMIN) { player, _ ->
            player.savedData.globalData.starSpriteDelay = 0L
        }
    }

    override fun startup() {
        SystemLogger.logInfo(this::class.java, "Shooting Stars initialized.")
    }

    private data class ScoreboardEntry(val player: String, val time: Int)

    private class RingDialogue(val shouldWarn: Boolean, val star: ShootingStar) : DialogueFile(){
        override fun handle(componentID: Int, buttonID: Int) {
            when (stage) {
                0 -> dialogue(if (star.spriteSpawned) "The star sprite has already been freed." else "The star sprite is still trapped.").also { if (shouldWarn) stage++ else stage += 2 }
                1 -> dialogue("WARNING: The star is located in the wilderness.").also { stage++ }
                2 -> player!!.dialogueInterpreter.sendOptions("Teleport to the star?", "Yes", "No").also { stage++ }
                3 -> when (buttonID) {
                    1 -> teleport(player!!, star).also { end() }
                    2 -> end()
                }
            }
        }

        fun teleport(player: Player, star: ShootingStar){
            teleport(player, star.crash_locations[star.location]!!.transform(0, -1, 0), TeleportManager.TeleportType.MINIGAME)
            getRingStoreFile()[player.username.toLowerCase()] = true
        }
    }

    companion object {
        private val star = ShootingStar()
        private val tickDelay = if(GameWorld.settings?.isDevMode == true) 200 else 25000
        private val scoreboardEntries = ArrayList<ScoreboardEntry>()
        private val scoreboardIface = 787
        val SHOOTING_STARS = ShootingStarType.values().map(ShootingStarType::objectId).toIntArray()
        val STAR_DUST = Items.STARDUST_13727
        val RING = Items.RING_OF_THE_STAR_SPRITE_14652


        @JvmStatic fun submitScoreBoard(player: Player)
        {
            if(scoreboardEntries.size == 5)
                scoreboardEntries.removeAt(0)
            scoreboardEntries.add(ScoreboardEntry(player.username, GameWorld.ticks))
        }

        @JvmStatic fun getStar(): ShootingStar
        {
            return star
        }

        @JvmStatic fun getStoreFile() : JSONObject {
            return ServerStore.getArchive("shooting-star")
        }

        @JvmStatic fun getRingStoreFile() : JSONObject {
            return ServerStore.getArchive("daily-star-ring")
        }

        fun getStarDust(player: Player): Int {
            return player.inventory.getAmount(STAR_DUST) + player.bank.getAmount(STAR_DUST)
        }
    }
}
