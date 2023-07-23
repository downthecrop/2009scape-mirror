package content.region.misthalin.varrock.quest.dragonslayer

import content.global.skill.agility.AgilityHandler
import content.region.misthalin.lumbridge.dialogue.DukeHoracioDialogue
import core.api.Event
import core.api.LoginListener
import core.api.getQuestStage
import core.game.component.Component
import core.game.event.EventHook
import core.game.event.PickUpEvent
import core.game.event.SpellCastEvent
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.map.Location
import core.game.world.map.RegionManager.getObject
import core.game.world.update.flag.context.Animation
import core.integrations.discord.Discord
import core.plugin.ClassScanner.definePlugins
import core.plugin.Initializable
import org.rs09.consts.Items

/**
 * Represents the dragon slayer quest.
 * @author Vexia - Converted to Kotlin with use of event hooks by not-Vexia
 */
@Initializable
class DragonSlayer : Quest("Dragon Slayer", 18, 17, 2, 176, 0, 1, 10), LoginListener {
    override fun newInstance(`object`: Any?): Quest {
        definePlugins(
            CrandorMapPlugin(),
            DragonSlayerPlugin(),
            DSMagicDoorPlugin(),
            DragonSlayerCutscene(),
            MazeDemonNPC(),
            MazeGhostNPC(),
            MazeSkeletonNPC(),
            MazeZombieNPC(),
            MeldarMadNPC(),
            WormbrainNPC(),
            ZombieRatNPC(),
            DSChestDialogue(),
            GuildmasterDialogue(),
            ElvargNPC(),
            WormbrainDialogue(),
            OziachDialogue(),
            NedDialogue(),
            DukeHoracioDialogue()
        )
        return this
    }

    override fun drawJournal(player: Player, stage: Int) {
        super.drawJournal(player, stage)
        when (getStage(player)) {
            0 -> {
                player.packetDispatch.sendString(
                    BLUE + "I can start this quest by speaking to the " + RED + "Guildmaster " + BLUE + "in",
                    275,
                    4 + 7
                )
                player.packetDispatch.sendString(
                    BLUE + "the " + RED + "Champions' Guild" + BLUE + " , south-west of Varrock.",
                    275,
                    5 + 7
                )
                player.packetDispatch.sendString(
                    BLUE + "I will need to be able to defeat a " + RED + "level 83 dragon.",
                    275,
                    6 + 7
                )
                if (player.questRepository.points < 32) {
                    player.packetDispatch.sendString(
                        BLUE + "To enter the Champions' Guild I need" + RED + " 32 Quest Points.",
                        275,
                        7 + 7
                    )
                } else {
                    player.packetDispatch.sendString(
                        "<str>To enter the Champions' Guild I need 32 Quest Points.",
                        275,
                        7 + 7
                    )
                }
            }

            10 -> {
                line(player, "<str>The Guildmaster of the Champions' Guild said I could earn", 4 + 7)
                line(player, "<str>the right to wear rune armour if I went on a quest for", 5 + 7)
                line(player, "<str>Oziach, who makes the armour.", 6 + 7)
                line(
                    player,
                    BLUE + "I should speak to " + RED + "Oziach" + BLUE + ", who lives by the cliffs to the",
                    7 + 7
                )
                line(player, BLUE + "west of " + RED + "Edgeville.", 8 + 7)
            }

            15 -> {
                line(player, "<str>The Guildmaster of the Champions' Guild said I could earn", 4 + 7)
                line(player, "<str>the right to wear rune armour if I went on a quest for", 5 + 7)
                line(player, "<str>Oziach, who makes the armour.", 6 + 7)
                line(player, "<str>I spoke to Oziach in Edgeville. He told me to slay the", 7 + 7)
                line(player, "<str>dragon of Crandor island.", 8 + 7)
                line(
                    player,
                    BLUE + "I should return to the " + RED + "Champions' Guild Guildmaster " + BLUE + "for",
                    9 + 7
                )
                line(player, BLUE + "more detailed instructions.", 10 + 7)
            }

            20 -> {
                line(player, "<str>The Guildmaster of the Champions' Guild said I could earn", 4 + 7)
                line(player, "<str>the right to wear rune armour if I went on a quest for", 5 + 7)
                line(player, "<str>Oziach, who makes the armour.", 6 + 7)
                line(player, "<str>I spoke to Oziach in Edgeville. He told me to slay the", 7 + 7)
                line(player, "<str>dragon of Crandor island.", 8 + 7)
                line(player, "<str>The Champions' Guild Guildmaster gave me more detailed", 9 + 7)
                line(player, "<str>instructions.", 10 + 7)
                line(
                    player,
                    BLUE + "To defeat the dragon I will need to find a " + RED + "map " + BLUE + "to Crandor, a",
                    11 + 7
                )
                line(
                    player,
                    RED + "ship" + BLUE + ", a " + RED + "captain " + BLUE + "to take me there and some kind of",
                    12 + 7
                )
                line(player, RED + "protection " + BLUE + "against the dragon's breath.", 13 + 7)
                if (!player.inventory.containsItem(MAZE_PIECE) && !player.bank.containsItem(MAZE_PIECE)) {
                    line(player, BLUE + "One-third of the map is in " + RED + "Melzar's Maze" + BLUE + ", near", 14 + 7)
                    line(player, RED + "Rimmington" + ".", 15 + 7)
                } else {
                    line(player, "<str>I found the piece of the map that was hidden in Melzar's", 14 + 7)
                    line(player, "<str>Maze.", 15 + 7)
                }
                if (!player.inventory.containsItem(MAGIC_PIECE) && !player.bank.containsItem(MAGIC_PIECE)) {
                    line(
                        player,
                        BLUE + "One-third of the map is hidden and only the " + RED + "Oracle " + BLUE + "on " + RED + "Ice",
                        16 + 7
                    )
                    line(player, RED + "Mountain" + BLUE + " will know where it is.", 17 + 7)
                } else {
                    line(player, "<str>I found the piece of the map that was hidden beneath Ice", 16 + 7)
                    line(player, "<str>Mountain.", 18 + 7)
                }
                if (!player.inventory.containsItem(WORMBRAIN_PIECE) && !player.bank.containsItem(WORMBRAIN_PIECE)) {
                    line(
                        player,
                        BLUE + "One-third of the map was stolen by a " + RED + "goblin " + BLUE + "from the",
                        18 + 7
                    )
                    line(player, RED + "Goblin Village.", 19 + 7)
                } else {
                    line(player, "<str>I found the piece of the map that the goblin, Wormbrain,", 18 + 7)
                    line(player, "<str>stole.", 19 + 7)
                }
                if (!player.inventory.containsItem(SHIELD) && !player.bank.containsItem(SHIELD)) {
                    line(
                        player,
                        BLUE + "I should ask the " + RED + "Duke of Lumbridge " + BLUE + "for an " + RED + "anti-",
                        20 + 7
                    )
                    line(player, RED + "dragonbreath shield.", 21 + 7)
                } else {
                    line(player, "<str>The Duke of Lumbridge gave me an anti-dragonbreath", 20 + 7)
                    line(player, "<str>shield.", 21 + 7)
                }
                if (!player.savedData.questData.getDragonSlayerAttribute("ship")) {
                    line(
                        player,
                        BLUE + "I should see if there is a " + RED + "ship " + BLUE + "for sale in " + RED + "Port Sarim",
                        22 + 7
                    )
                } else {
                    line(player, "<str>I bought a ship in Port Sarim called the Lady Lumbridge.", 22 + 7)
                    if (!player.savedData.questData.getDragonSlayerAttribute("repaired")) {
                        line(player, "<str>I need to repair the hole in bottom of the ship.", 23 + 7)
                    } else {
                        line(player, "<str>I have repaired my ship using wooden planks and steel", 23 + 7)
                        line(player, "<str>nails.", 24 + 7)
                    }
                }
            }

            30 -> {
                line(player, "<str>The Guildmaster of the Champions' Guild said I could earn", 4 + 7)
                line(player, "<str>the right to wear rune armour if I went on a quest for", 5 + 7)
                line(player, "<str>Oziach, who makes the armour.", 6 + 7)
                line(player, "<str>I spoke to Oziach in Edgeville. He told me to slay the", 7 + 7)
                line(player, "<str>dragon of Crandor island.", 8 + 7)
                line(player, "<str>The Champions' Guild Guildmaster told me I had to find", 9 + 7)
                line(player, "<str>three pieces of a map to Crandor, a ship, a captain to take", 10 + 7)
                line(player, "<str>me there and a shield to protect me from the dragon's", 11 + 7)
                line(player, "<str>breath.", 12 + 7)
                line(player, "<str>I found the piece of the map that was hidden in Melzar's", 13 + 7)
                line(player, "<str>Maze.", 14 + 7)
                line(player, "<str>I found the piece of the map that was hidden beneath Ice", 15 + 7)
                line(player, "<str>Mountain.", 16 + 7)
                line(player, "<str>I found the piece of the map that the goblin, Wormbrain,", 17 + 7)
                line(player, "<str>stole.", 18 + 7)
                line(player, "<str>The Duke of Lumbridge gave me an anti-dragonbreath", 19 + 7)
                line(player, "<str>shield.", 20 + 7)
                line(player, "<str>I bought a ship in Port Sarim called the Lady Lumbridge", 21 + 7)
                line(player, "<str>I have repaired my ship using wooden planks and steel", 22 + 7)
                line(player, "<str>nails.", 23 + 7)
                line(player, "<str>Captain Ned from Draynor Village has agreed to sail the", 24 + 7)
                line(player, "<str>ship to Crandor for me.", 25 + 7)
                line(
                    player,
                    BLUE + "Now I should go to my ship in " + RED + "Port Sarim " + BLUE + "and set sail for",
                    26 + 7
                )
                line(player, RED + "Crandor" + BLUE + "!", 27 + 7)
            }

            40 -> {
                line(player, "<str>The Guildmaster of the Champions' Guild said I could earn", 4 + 7)
                line(player, "<str>the right to wear rune armour if I went on a quest for", 5 + 7)
                line(player, "<str>Oziach, who makes the armour.", 6 + 7)
                line(player, "<str>I spoke to Oziach in Edgeville. He told me to slay the", 7 + 7)
                line(player, "<str>dragon of Crandor island.", 8 + 7)
                line(player, "<str>The Champions' Guild Guildmaster told me I had to find", 9 + 7)
                line(player, "<str>three pieces of a map to Crandor, a ship, a captain to take", 10 + 7)
                line(player, "<str>me there and a shield to protect me from the dragon's", 11 + 7)
                line(player, "<str>breath.", 12 + 7)
                line(player, "<str>I found the piece of the map that was hidden in Melzar's", 13 + 7)
                line(player, "<str>Maze.", 14 + 7)
                line(player, "<str>I found the piece of the map that was hidden beneath Ice", 15 + 7)
                line(player, "<str>Mountain.", 16 + 7)
                line(player, "<str>I found the piece of the map that the goblin, Wormbrain,", 17 + 7)
                line(player, "<str>stole.", 18 + 7)
                line(player, "<str>The Duke of Lumbridge gave me an anti-dragonbreath", 19 + 7)
                line(player, "<str>shield.", 20 + 7)
                if (!player.getAttribute("demon-slayer:memorize", false)) {
                    if (!player.inventory.containsItem(ELVARG_HEAD)) {
                        line(player, BLUE + "Now all I need to do is kill the " + RED + "dragon" + BLUE + "!", 21 + 7)
                    } else {
                        line(
                            player,
                            BLUE + "I have slain the dragon! Now I just need to tell " + RED + "Oziach.",
                            21 + 7
                        )
                    }
                } else {
                    line(player, "<str>I have found a secret passage leading from Karamja to", 21 + 7)
                    line(player, "<str>Crandor, so I no longer need to worry about finding a", 22 + 7)
                    line(player, "<str>seaworthy ship and captain to take me there.", 23 + 7)
                    if (!player.inventory.containsItem(ELVARG_HEAD)) {
                        line(player, BLUE + "Now all I need to do is kill the " + RED + "dragon" + BLUE + "!", 24 + 7)
                    } else {
                        line(
                            player,
                            BLUE + "I have slain the dragon! Now I just need to tell " + RED + "Oziach.",
                            24 + 7
                        )
                    }
                }
            }

            100 -> {
                line(player, "<str>The Guildmaster of the Champions' Guild said I could earn", 4 + 7)
                line(player, "<str>the right to wear rune armour if I went on a quest for", 5 + 7)
                line(player, "<str>Oziach, who makes the armour.", 6 + 7)
                line(player, "<str>I spoke to Oziach in Edgeville. He told me to slay the", 7 + 7)
                line(player, "<str>dragon of Crandor island.", 8 + 7)
                line(player, "<str>The Champions' Guild Guildmaster told me I had to find", 9 + 7)
                line(player, "<str>three pieces of a map to Crandor, a ship, a captain to take", 10 + 7)
                line(player, "<str>me there and a shield to protect me from the dragon's", 11 + 7)
                line(player, "<str>breath.", 12 + 7)
                line(player, "<str>I found the piece of the map that was hidden in Melzar's", 13 + 7)
                line(player, "<str>Maze.", 14 + 7)
                line(player, "<str>I found the piece of the map that was hidden beneath Ice", 15 + 7)
                line(player, "<str>Mountain.", 16 + 7)
                line(player, "<str>I found the piece of the map that the goblin, Wormbrain,", 17 + 7)
                line(player, "<str>stole.", 18 + 7)
                line(player, "<str>The Duke of Lumbridge gave me an anti-dragonbreath", 19 + 7)
                line(player, "<str>shield.", 20 + 7)
                line(player, "<str>I have found a secret passage leading from Karamja to", 21 + 7)
                line(player, "<str>Crandor, so I no longer need to worry about finding a", 22 + 7)
                line(player, "<str>seaworthy ship and captain to take me there.", 23 + 7)
                line(player, "<str>I sailed to Crandor and killed the dragon. I am not a true", 24 + 7)
                line(player, "<str>champion and have proved myself worthy to wear rune", 25 + 7)
                line(player, "<str>platemail!", 26 + 7)
                line(player, "<col=FF0000>QUEST COMPLETE!</col>", 27 + 7)
                line(
                    player,
                    BLUE + "I gained " + RED + "2 Quest Points" + BLUE + ", " + RED + "18,650 Strength XP" + BLUE + ", " + RED + "18,650",
                    28 + 7
                )
                line(player, RED + "Defence XP " + BLUE + "and the right to wear " + RED + "rune platebodies.", 29 + 7)
            }
        }
    }

    override fun finish(player: Player) {
        super.finish(player)
        player.packetDispatch.sendString("2 Quests Points", 277, 8 + 2)
        player.packetDispatch.sendString("Ability to wear rune platebody", 277, 9 + 2)
        player.packetDispatch.sendString("18,650 Strength XP", 277, 10 + 2)
        player.packetDispatch.sendString("18,650 Defence XP", 277, 11 + 2)
        player.packetDispatch.sendString("You have completed the Dragon Slayer Quest!", 277, 2 + 2)
        player.packetDispatch.sendItemZoomOnInterface(ELVARG_HEAD.id, 230, 277, 3 + 2)
        player.getSkills().addExperience(Skills.STRENGTH, 18650.0)
        player.getSkills().addExperience(Skills.DEFENCE, 18650.0)
        player.unhook(SpellCastHook)
        player.unhook(PickedUpHook)
    }

    override fun setStage(player: Player, stage: Int) {
        super.setStage(player, stage)
        if (stage == 20) {
            player.hook(Event.SpellCast, SpellCastHook)
            player.hook(Event.PickedUp, PickedUpHook)
        }
    }

    override fun login(player: Player) {
        if (getQuestStage(player, this.name) == 20) {
            player.hook(Event.SpellCast, SpellCastHook)
            player.hook(Event.PickedUp, PickedUpHook)
        }
    }

    private val SpellCastHook = object : EventHook<SpellCastEvent> {
        override fun process(entity: Entity, event: SpellCastEvent) {
            if (event.spellId == 19 && event.target != null && event.target.id == Items.MAP_PART_1536) {//telegrab
                Discord.sendToOpenRSC(entity.name, "Player obtained Wormbrain piece! (Murder-Then-Telegrab)")
                entity.unhook(this)
            }
        }
    }

    private val PickedUpHook = object : EventHook<PickUpEvent> {
        override fun process(entity: Entity, event: PickUpEvent) {
            if (event.itemId == Items.MAP_PART_1536) {
                Discord.sendToOpenRSC(entity.name, "Player obtained Wormbrain piece! (Yoinked-Off-Floor)")
                entity.unhook(this)
            }
        }
    }

    companion object {
        /**
         * Represents the maze key given by the guildmaster.
         */
		@JvmField
		val MAZE_KEY = Item(1542)

        /**
         * Represents the red key item.
         */
		@JvmField
		val RED_KEY = Item(1543)

        /**
         * Represents the orange key item.
         */
		@JvmField
		val ORANGE_KEY = Item(1544)

        /**
         * Represents the yellow key item.
         */
		@JvmField
		val YELLOW_KEY = Item(1545)

        /**
         * Represents the blue key item.
         */
		@JvmField
		val BLUE_KEY = Item(1546)

        /**
         * Represents the purple key item.
         */
		@JvmField
		val PURPLE_KEY = Item(1547)

        /**
         * Represents the green key item.
         */
		@JvmField
		val GREEN_KEY = Item(1548)

        /**
         * Represents the maze map piece.
         */
		@JvmField
		val MAZE_PIECE = Item(1535)

        /**
         * Represents the magic door map piece.
         */
		@JvmField
		val MAGIC_PIECE = Item(1537)

        /**
         * Represents the wormbrain piece.
         */
		@JvmField
		val WORMBRAIN_PIECE = Item(1536)

        /**
         * Represents the anti dragon fire shield.
         */
        val SHIELD = Item(1540)

        /**
         * Represents the crandor map item.
         */
		@JvmField
		val CRANDOR_MAP = Item(1538)

        /**
         * Represents the map component interface.
         */
		@JvmField
		val MAP_COMPONENT = Component(547)

        /**
         * Represents the nails item.
         */
		@JvmField
		val NAILS = Item(1539, 30)

        /**
         * Represents the plank item.
         */
		@JvmField
		val PLANK = Item(960)

        /**
         * Represents the hammer item.
         */
		@JvmField
		val HAMMER = Item(2347)

        /**
         * Represents the elvarg head item.
         */
		@JvmField
		val ELVARG_HEAD = Item(11279)

        /**
         * Method used to handle going through the magic door.
         * @param player the player.
         * @param interaction the interaction.
         * @return `True` if so.
         */
		@JvmStatic
		fun handleMagicDoor(player: Player, interaction: Boolean): Boolean {
            if (!player.savedData.questData.getDragonSlayerItem("lobster") || !player.savedData.questData.getDragonSlayerItem(
                    "bowl"
                ) || !player.savedData.questData.getDragonSlayerItem("silk") || !player.savedData.questData.getDragonSlayerItem(
                    "wizard"
                )
            ) {
                if (interaction) {
                    player.packetDispatch.sendMessage("You can't see any way to open the door.")
                }
                return true
            }
            player.packetDispatch.sendMessage("The door opens...")
            val `object` = getObject(Location(3050, 9839, 0))
            player.faceLocation(`object`!!.location)
            player.packetDispatch.sendSceneryAnimation(`object`, Animation(6636))
            Pulser.submit(object : Pulse(1, player) {
                var counter = 0
                override fun pulse(): Boolean {
                    when (counter++) {
                        4 -> AgilityHandler.walk(
                            player,
                            0,
                            player.location,
                            if (player.location.x == 3051) Location.create(3049, 9840, 0) else Location.create(
                                3051,
                                9840,
                                0
                            ),
                            null,
                            0.0,
                            null
                        )

                        5 -> player.packetDispatch.sendSceneryAnimation(`object`, Animation(6637))
                        6 -> {
                            player.packetDispatch.sendSceneryAnimation(`object`, Animation(6635))
                            return true
                        }
                    }
                    return false
                }
            })
            return true
        }
    }
}