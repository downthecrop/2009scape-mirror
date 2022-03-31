package core.game.node.entity.skill.runecrafting.abyss

import api.*
import rs09.plugin.ClassScanner.definePlugin
import rs09.tools.stringtools.colorize
import rs09.game.system.SystemLogger.logInfo
import core.game.node.scenery.Scenery
import core.game.node.entity.impl.Animator
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.entity.skill.gather.SkillingTool
import core.game.node.entity.skill.runecrafting.Altar
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.tools.RandomFunction
import org.rs09.consts.NPCs
import rs09.game.interaction.InteractionListener
import rs09.game.world.GameWorld

/**
 * A plugin used to handle the abyss.
 * @author cfunny
 */
class AbyssPlugin : InteractionListener() {

    val OBSTACLE = AbbysalObstacle.values().filter { it != AbbysalObstacle.MINE && it != AbbysalObstacle.SQUEEZE && it != AbbysalObstacle.PASSAGE && it != AbbysalObstacle.CHOP }.map { it.option }.toTypedArray()
    val miningObstacle = 7158
    val agilityObstacle = 7164
    val passage = 7154
    val chopObstacle = 7161

    override fun defineListeners() {
        definePlugin(AbyssalNPC())
        definePlugin(DarkMageDialogue())
        definePlugin(ZamorakMageDialogue())
        on(NPCs.MAGE_OF_ZAMORAK_2259, NPC, "teleport"){ player, node ->
            teleport(player, node as NPC)
            return@on true
        }
        on(NPCs.DARK_MAGE_2262, NPC, "repair-pouches"){ player, node ->
            player.dialogueInterpreter.open(node.id, node, true)
            return@on true
        }
        on(SCENERY, "exit-through"){ player, node ->
            val altar = Altar.forObject(node as Scenery)
            altar?.enterRift(player)
            return@on true
        }
        on(SCENERY, *OBSTACLE){ player, node ->
            val obstacle = AbbysalObstacle.forObject(node as Scenery)
            obstacle!!.handle(player, node as Scenery)
            return@on true
        }
        on(miningObstacle, SCENERY, "mine"){ player, node ->
            val obstacle = AbbysalObstacle.forObject(node as Scenery)
            obstacle!!.handle(player, node as Scenery)
            return@on true
        }
        on(agilityObstacle, SCENERY, "squeeze-through"){ player, node ->
            val obstacle = AbbysalObstacle.forObject(node as Scenery)
            obstacle!!.handle(player, node as Scenery)
            return@on true
        }
        on(passage, SCENERY, "go-through",){ player, node ->
            val obstacle = AbbysalObstacle.forObject(node as Scenery)
            obstacle!!.handle(player, node as Scenery)
            return@on true
        }
        on(chopObstacle, SCENERY, "chop"){ player, node ->
            val obstacle = AbbysalObstacle.forObject(node as Scenery)
            obstacle!!.handle(player, node as Scenery)
            return@on true
        }

    }

    /**
     * Represents an obstacle in an abbsyal.
     * @author cfunny
     * @date 02/11/2013
     */
    enum class AbbysalObstacle(
        /**
         * Represents the option.
         */
        val option: String,
        /**
         * Represents the corssing location.
         */
        val locations: Array<Location>,
        /**
         * Represents the object id.
         */
        vararg val objects: Int
    ) {
        BOIL("burn-down", arrayOf(Location.create(3024, 4833, 0), Location.create(3053, 4830, 0)), 7165) {
            override fun handle(player: Player, `object`: Scenery?) {
                `object` ?: return
                if (!inInventory(player, 590, 1)) {
                    sendMessage(player, "You don't have a tinderbox to burn it.")
                    return
                }
                player.animate(Animation(733))
                player.lock()
                GameWorld.Pulser.submit(object : Pulse(1, player) {
                    var count = 0
                    override fun pulse(): Boolean {
                        when (count) {
                            1 -> sendMessage(player, "You attempt to burn your way through..")
                            4 -> return if (RandomFunction.random(100) < getStatLevel(
                                    player,
                                    Skills.FIREMAKING
                                )
                            ) {
                                sendMessage(
                                    player,
                                    colorize("%G...and manage to burn it down and get past.")
                                )
                                player.properties.teleportLocation = locations[getIndex(`object`)]
                                player.unlock()
                                true
                            } else {
                                sendMessage(player, colorize("%RYou fail to set it on fire."))
                                player.unlock()
                                true
                            }
                        }
                        count++
                        return false
                    }

                    override fun stop() {
                        super.stop()
                        player.animate(Animation(-1, Animator.Priority.HIGH))
                    }
                })
            }
        },
        MINE("mine", arrayOf(Location.create(3030, 4821, 0), Location.create(3048, 4822, 0)), 7158, 7153) {
            override fun handle(player: Player, `object`: Scenery?) {
                `object` ?: return
                logInfo("handled abyss mine")
                val tool: SkillingTool = getTool(player, true) ?: return
                if (tool == null) {
                    sendMessage(player, "You need a pickaxe in order to do that.")
                    return
                }
                player.animate(tool.getAnimation())
                player.lock()
                GameWorld.Pulser.submit(object : Pulse(1, player) {
                    var count = 0
                    override fun pulse(): Boolean {
                        when (count) {
                            1 -> sendMessage(player, "You attempt to mine your way through..")
                            4 -> return if (RandomFunction.random(100) < getStatLevel(
                                    player,
                                    Skills.MINING
                                )
                            ) {
                                sendMessage(player, colorize("%G...and manage to break through the rock."))
                                player.properties.teleportLocation = locations[getIndex(`object`)]
                                player.unlock()
                                true
                            } else {
                                sendMessage(player, colorize("%R...but fail to break-up the rock."))
                                player.unlock()
                                true
                            }
                        }
                        count++
                        return false
                    }

                    override fun stop() {
                        super.stop()
                        player.animate(Animation(-1, Animator.Priority.HIGH))
                    }
                })
            }
        },
        CHOP("chop", arrayOf(Location.create(3050, 4824, 0), Location.create(3028, 4824, 0)), 7161, 7144) {
            override fun handle(player: Player, `object`: Scenery?) {
                `object` ?: return
                val tool: SkillingTool? = setTool(false, player)
                if (tool == null) {
                    player.packetDispatch.sendMessage("You need an axe in order to do that.")
                    return
                }
                player.animate(tool.getAnimation())
                player.lock()
                GameWorld.Pulser.submit(object : Pulse(1, player) {
                    var count = 0
                    override fun pulse(): Boolean {
                        when (count) {
                            1 -> sendMessage(player, "You attempt to chop your way through...")
                            4 -> return if (RandomFunction.random(100) < getStatLevel(
                                    player,
                                    Skills.WOODCUTTING
                                )
                            ) {
                                sendMessage(player, colorize("%G...and manage to chop down the tendrils."))
                                player.properties.teleportLocation = locations[getIndex(`object`)]
                                player.unlock()
                                true
                            } else {
                                sendMessage(player, colorize("%RYou fail to cut through the tendrils."))
                                player.unlock()
                                true
                            }
                        }
                        count++
                        return false
                    }

                    override fun stop() {
                        super.stop()
                        player.animate(Animation(-1, Animator.Priority.HIGH))
                    }
                })
            }
        },
        SQUEEZE(
            "squeeze-through",
            arrayOf(Location.create(3048, 4842, 0), Location.create(3031, 4842, 0)),
            7164,
            7147
        ) {
            override fun handle(player: Player, `object`: Scenery?) {
                `object` ?: return
                player.animate(Animation(1331))
                player.lock()
                GameWorld.Pulser.submit(object : Pulse(1, player) {
                    var count = 0
                    override fun pulse(): Boolean {
                        when (count) {
                            1 -> sendMessage(player, "You attempt to squeeze through the narrow gap...")
                            4 -> return if (RandomFunction.random(100) < getStatLevel(
                                    player,
                                    Skills.AGILITY
                                )
                            ) {
                                sendMessage(player, colorize("%G...and you manage to crawl through."))
                                player.properties.teleportLocation = locations[getIndex(`object`)]
                                player.unlock()
                                true
                            } else {
                                sendMessage(player, colorize("%RYou fail to squeeze through the narrow gap"))
                                player.unlock()
                                true
                            }
                        }
                        count++
                        return false
                    }

                    override fun stop() {
                        super.stop()
                        player.animate(Animation(-1, Animator.Priority.HIGH))
                    }
                })
            }
        },
        DISTRACT("distract", arrayOf(Location.create(3029, 4841, 0), Location.create(3051, 4838, 0)), 7168, 7150) {
            override fun handle(player: Player, `object`: Scenery?) {
                `object` ?: return
                val emotes = intArrayOf(
                    855,
                    856,
                    857,
                    858,
                    859,
                    860,
                    861,
                    862,
                    863,
                    864,
                    865,
                    866,
                    2113,
                    2109,
                    2111,
                    2106,
                    2107,
                    2108,
                    0x558,
                    2105,
                    2110,
                    2112,
                    0x84F,
                    0x850,
                    1131,
                    1130,
                    1129,
                    1128,
                    1745,
                    3544,
                    3543,
                    2836
                )
                val index: Int = RandomFunction.random(emotes.size)
                player.animate(Animation(emotes[index]))
                player.lock()
                GameWorld.Pulser.submit(object : Pulse(1, player) {
                    var count = 0
                    override fun pulse(): Boolean {
                        when (count) {
                            1 -> sendMessage(player, "You use your thieving skills to misdirect the eyes...")
                            4 -> return if (RandomFunction.random(100) < getStatLevel(
                                    player,
                                    Skills.THIEVING
                                )
                            ) {
                                sendMessage(
                                    player,
                                    colorize("%G...and sneak past while they're not looking.")
                                )
                                player.properties.teleportLocation = locations[getIndex(`object`)]
                                player.unlock()
                                true
                            } else {
                                sendMessage(player, colorize("%RYou fail to distract the eyes."))
                                player.unlock()
                                true
                            }
                        }
                        count++
                        return false
                    }

                    override fun stop() {
                        super.stop()
                        player.animate(Animation(-1, Animator.Priority.HIGH))
                    }
                })
            }
        },
        PASSAGE("go-through", arrayOf(Location.create(3040, 4844, 0)), 7154) {
            override fun handle(player: Player, `object`: Scenery?) {
                player.properties.teleportLocation = locations[0]
            }
        };
        /**
         * Gets the option.
         * @return The option.
         */
        /**
         * Gets the locations.
         * @return The locations.
         */
        /**
         * Gets the objects.
         * @return The objects.
         */

        /**
         * Method used to get the index.
         * @param object the object.
         * @return the index.
         */
        fun getIndex(`object`: Scenery): Int {
            for (i in objects.indices) {
                if (objects[i] == `object`.getId()) {
                    return i
                }
            }
            return 0
        }

        /**
         * Method used to handle the obstacle.
         * @param player the player.
         * @param object the object.
         */
        open fun handle(player: Player, `object`: Scenery?) {}

        companion object {
            /**
             * Method used to get the abbysal obstacle.
             * @param object the object.
             * @return the `AbbysalObstacle` or `Null`.
             */
            fun forObject(`object`: Scenery): AbbysalObstacle? {
                for (obstacle in values()) {
                    for (i in obstacle.objects) {
                        if (i == `object`.getId()) {
                            return obstacle
                        }
                    }
                }
                return null
            }

            /**
             * Sets the tool used.
             */
            private fun setTool(mining: Boolean, player: Player): SkillingTool? {
                var tool: SkillingTool? = null
                if (!mining) {
                    if (checkTool(player, SkillingTool.DRAGON_AXE)) {
                        tool = SkillingTool.DRAGON_AXE
                    } else if (checkTool(player, SkillingTool.RUNE_AXE)) {
                        tool = SkillingTool.RUNE_AXE
                    } else if (checkTool(player, SkillingTool.ADAMANT_AXE)) {
                        tool = SkillingTool.ADAMANT_AXE
                    } else if (checkTool(player, SkillingTool.MITHRIL_AXE)) {
                        tool = SkillingTool.MITHRIL_AXE
                    } else if (checkTool(player, SkillingTool.BLACK_AXE)) {
                        tool = SkillingTool.BLACK_AXE
                    } else if (checkTool(player, SkillingTool.STEEL_AXE)) {
                        tool = SkillingTool.STEEL_AXE
                    } else if (checkTool(player, SkillingTool.IRON_AXE)) {
                        tool = SkillingTool.IRON_AXE
                    } else if (checkTool(player, SkillingTool.BRONZE_AXE)) {
                        tool = SkillingTool.BRONZE_AXE
                    }
                } else {
                    if (checkTool(player, SkillingTool.RUNE_PICKAXE)) {
                        tool = SkillingTool.RUNE_PICKAXE
                    } else if (checkTool(player, SkillingTool.ADAMANT_PICKAXE)) {
                        tool = SkillingTool.ADAMANT_PICKAXE
                    } else if (checkTool(player, SkillingTool.MITHRIL_PICKAXE)) {
                        tool = SkillingTool.MITHRIL_PICKAXE
                    } else if (checkTool(player, SkillingTool.STEEL_PICKAXE)) {
                        tool = SkillingTool.STEEL_PICKAXE
                    } else if (checkTool(player, SkillingTool.IRON_PICKAXE)) {
                        tool = SkillingTool.IRON_PICKAXE
                    } else if (checkTool(player, SkillingTool.BRONZE_PICKAXE)) {
                        tool = SkillingTool.BRONZE_PICKAXE
                    }
                }
                return tool
            }

            /**
             * Checks if the player has a tool and if he can use it.
             * @param tool The tool.
             * @return `True` if the tool is usable.
             */
            private fun checkTool(player: Player, tool: SkillingTool): Boolean {
                return if (player.equipment.contains(tool.getId(), 1)) {
                    true
                } else player.inventory.contains(tool.getId(), 1)
            }
        }
    }

    companion object {
        /**
         * Represents the teleporting to the abyss.
         * @param player the player.
         */
        fun teleport(player: Player, npc: NPC) {
            player.lock(3)
            npc.visualize(Animation(1979), Graphics(4))
            npc.sendChat("Veniens! Sallakar! Rinnesset!")
            player.skills.decrementPrayerPoints(100.0)
            player.skullManager.checkSkull(player)
            GameWorld.Pulser.submit(object : Pulse(2, player) {
                override fun pulse(): Boolean {
                    player.properties.teleportLocation = Location.create(3021, 4847, 0)
                    npc.updateMasks.reset()
                    return true
                }
            })
        }
    }
}