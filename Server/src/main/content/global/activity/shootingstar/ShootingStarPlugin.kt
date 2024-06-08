package content.global.activity.shootingstar

import core.api.*
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.TeleportManager
import core.game.node.entity.skill.Skills
import org.json.simple.JSONObject
import org.rs09.consts.Items
import org.rs09.consts.Scenery
import core.ServerStore
import core.ServerStore.Companion.getBoolean
import core.game.dialogue.DialogueFile
import core.game.interaction.InteractionListener
import core.game.interaction.IntType
import core.tools.SystemLogger
import core.game.system.command.Privilege
import core.game.world.GameWorld
import core.tools.Log
import core.tools.secondsToTicks

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

        on(RING, IntType.ITEM, "rub", "operate"){ player, node ->
            if(getRingStoreFile().getBoolean(player.username.toLowerCase())){
                sendDialogue(player, "The ring is still recharging.")
                return@on true
            }

            class RingDialogue(val star: ShootingStar) : DialogueFile() {
                val shouldWarn = when (star.location) {
                    "North Edgeville mining site",
                    "Southern wilderness mine",
                    "Wilderness hobgoblin mine",
                    "Pirates' Hideout mine",
                    "Lava Maze mining site",
                    "Mage Arena bank" -> true
                    else -> false
                }

                override fun handle(componentID: Int, buttonID: Int) {
                    fun teleportToStar(player: Player) {
                        val condition: (p: Player) -> Boolean = when (star.location.toLowerCase()) {
                            "canifis bank"              -> {p -> requireQuest(p, "Priest in Peril", "to access this.") }
                            //"burgh de rott bank"        -> {p -> hasRequirement(p, "In Aid of the Myreque") } //disabled: crash
                            "crafting guild"            -> {p -> hasLevelStat(p, Skills.CRAFTING, 40)       }
                            "lletya bank"               -> {p -> hasRequirement(p, "Mourning's End Part I") }
                            "jatizso mine"              -> {p -> hasRequirement(p, "The Fremennik Isles")   }
                            "south crandor mining site" -> {p -> hasRequirement(p, "Dragon Slayer")         }
                            "shilo village mining site" -> {p -> hasRequirement(p, "Shilo Village")         }
                            "mos le'harmless bank"      -> {p -> hasRequirement(p, "Cabin Fever")           } //needs to be updated to check for completion when the quest releases; https://runescape.wiki/w/Mos_Le%27Harmless?oldid=913025
                            "lunar isle mine"           -> {p -> hasRequirement(p, "Lunar Diplomacy")       }
                            "miscellania coal mine"     -> {p -> requireQuest(p, "The Fremennik Trials", "to access this.") }
                            //"neitiznot runite mine"     -> {p -> hasRequirement(p, "The Fremennik Isles") } //disabled: currently not reachable
                            else -> {_ -> true}
                        }
                        if (!condition.invoke(player)) {
                            sendDialogue(player,"Magical forces prevent your teleportation.")
                        } else if (teleport(player, star.crash_locations[star.location]!!.transform(0, -1, 0), TeleportManager.TeleportType.MINIGAME)) {
                            getRingStoreFile()[player.username.toLowerCase()] = true
                        }
                    }
                    when (stage) {
                        0 -> dialogue(if (star.spriteSpawned) "The star sprite has already been freed." else "The star sprite is still trapped.").also { if (shouldWarn) stage++ else stage += 2 }
                        1 -> dialogue("WARNING: The star is located in the wilderness.").also { stage++ }
                        2 -> player.dialogueInterpreter.sendOptions("Teleport to the star?", "Yes", "No").also { stage++ }
                        3 -> when (buttonID) {
                            1 -> end().also { teleportToStar(player) }
                            2 -> end()
                        }
                    }
                }
            }

            openDialogue(player, RingDialogue(star))
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
        log(this::class.java, Log.FINE, "Shooting Stars initialized.")
    }

    private data class ScoreboardEntry(val player: String, val time: Int)

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
