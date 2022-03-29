package core.game.interaction.`object`.sorceress

import core.plugin.Initializable
import core.plugin.Plugin
import core.game.component.Component
import core.game.content.dialogue.DialoguePlugin
import core.game.content.dialogue.FacialExpression
import core.game.interaction.NodeUsageEvent
import core.game.interaction.UseWithHandler
import core.game.node.scenery.Scenery
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.net.packet.PacketRepository
import core.net.packet.context.MinimapStateContext
import core.net.packet.out.MinimapState
import core.tools.RandomFunction
import rs09.game.interaction.InteractionListener
import rs09.game.world.GameWorld
import rs09.plugin.ClassScanner


class GardenObjectsPlugin : InteractionListener() {

    val SQIRK_TREES = intArrayOf(21767, 21768, 21769, 21766)
    val FOUNTAIN = 21764
    val HERBS = HerbDefinition.values().map { it.id }.toIntArray()
    val SHELVES = 21794
    private val HERBS_ITEMS = intArrayOf(199, 201, 203, 205, 207, 209, 211, 213, 215, 217, 219, 2485, 3049, 3051, 199, 201, 203, 205)

    override fun defineListeners() {
        SqirkJuicePlugin().newInstance(null)
        SqirkMakingDialogue().init()
        ClassScanner.definePlugin(SorceressGardenObject())

        on(SQIRK_TREES, SCENERY, "pick-fruit"){player, node ->
            val def = SeasonDefinitions.forTreeId(node.id)
            if (def != null) {
                player.lock()
                player.logoutListeners["garden"] = {p -> p.location = def.respawn}
                player.animate(PICK_FRUIT)
                player.skills.addExperience(Skills.THIEVING, def.exp, true)
                player.skills.addExperience(Skills.FARMING, def.farmExp, true)
                GameWorld.Pulser.submit(object : Pulse(2, player) {
                    var counter = 0
                    override fun pulse(): Boolean {
                        if (counter == 1) {
                            player.inventory.add(Item(def.fruitId))
                            player.interfaceManager.openOverlay(Component(115))
                            PacketRepository.send(MinimapState::class.java, MinimapStateContext(player, 2))
                        } else if (counter == 3) player.properties.teleportLocation = def.respawn else if (counter == 4) {
                            player.unlock()
                            player.logoutListeners.remove("garden")
                            player.packetDispatch.sendMessage("An elemental force emanating from the garden teleports you away.")
                            PacketRepository.send(MinimapState::class.java, MinimapStateContext(player, 0))
                            player.interfaceManager.close()
                            player.interfaceManager.closeOverlay()
                            player.unlock()
                            return true
                        }
                        counter++
                        return false
                    }
                })
            }
            return@on true
        }


        on(FOUNTAIN, SCENERY, "drink-from"){player, _ ->
            player.lock()
            GameWorld.Pulser.submit(object : Pulse(1, player) {
                var counter = 0
                override fun pulse(): Boolean {
                    when (counter++) {
                        1 -> player.animate(DRINK_ANIM)
                        4 -> player.graphics(GRAPHICS)
                        5 -> player.animate(TELE)
                        6 -> player.interfaceManager.openOverlay(Component(115))
                        7 -> PacketRepository.send(MinimapState::class.java, MinimapStateContext(player, 2))
                        9 -> player.properties.teleportLocation = Location(3321, 3141, 0)
                        11 -> {
                            player.unlock()
                            player.animate(Animation(-1))
                            player.interfaceManager.close()
                            player.interfaceManager.closeOverlay()
                            PacketRepository.send(MinimapState::class.java, MinimapStateContext(player, 0))
                            return true
                        }
                    }
                    return false
                }
            })

            return@on true
        }

        on(SHELVES, SCENERY, "search"){player, node ->
            if (player.inventory.freeSlots() < 1) {
                player.sendMessage("You don't have enough space in your inventory to take a beer glass.")
            } else {
                player.sendMessage("You take an empty beer glass off the shelves.")
                player.inventory.add(Item(1919, 1))
            }

            return@on true
        }

        on(HERBS, SCENERY, "pick"){player, node ->
            val herbDef = HerbDefinition.forId(node.getId())
            if (herbDef != null) {
                handleElementalGarden(player, node.asScenery(), herbDef)
            }
            return@on true
        }

    }

    /**
     * Method used to handle the elemental garden picking.
     * @param player the player.
     * @param object the object.
     * @param herbDef the herbdef.
     */
    private fun handleElementalGarden(player: Player, `object`: Scenery, herbDef: HerbDefinition) {
        player.lock()
        player.logoutListeners["garden"] = {p -> p.location = herbDef.respawn}
        player.animate(ANIMATION)
        player.skills.addExperience(Skills.FARMING, herbDef.exp, true)
        GameWorld.Pulser.submit(object : Pulse(2, player) {
            var counter = 0
            override fun pulse(): Boolean {
                if (counter == 1) {
                    player.inventory.add(Item(HERBS_ITEMS[RandomFunction.random(0, HERBS_ITEMS.size)], +1))
                    player.inventory.add(Item(HERBS_ITEMS[RandomFunction.random(0, HERBS_ITEMS.size)], +1))
                    player.packetDispatch.sendMessage("You pick up a herb.")
                    player.interfaceManager.openOverlay(Component(115))
                    PacketRepository.send(MinimapState::class.java, MinimapStateContext(player, 2))
                } else if (counter == 3) player.properties.teleportLocation =
                    Location.create(herbDef.respawn) else if (counter == 4) {
                    player.unlock()
                    player.packetDispatch.sendMessage("An elemental force emanating from the garden teleports you away.")
                    PacketRepository.send(MinimapState::class.java, MinimapStateContext(player, 0))
                    player.interfaceManager.close()
                    player.interfaceManager.closeOverlay()
                    player.logoutListeners.remove("garden")
                    player.unlock()
                    return true
                }
                counter++
                return false
            }
        })
    }

    /**
     * Represents the herb definitions.
     * @author SonicForce41
     * @version 1.0
     */
    enum class HerbDefinition
    /**
     * Constructs a new `GardenObjectsPlugin` `Object`.
     * @param id the id.
     * @param exp the exp.
     * @param respawn the respawn.
     */(
        /**
         * Represents the id.
         */
        val id: Int,
        /**
         * Represents the experience.
         */
        val exp: Double,
        /**
         * Represents the respawn location.
         */
        val respawn: Location
    ) {
        WINTER(21671, 30.0, Location(2907, 5470, 0)), SPRING(21668, 40.0, Location(2916, 5473, 0)), AUTUMN(
            21670,
            50.0,
            Location(2913, 5467, 0)
        ),
        SUMMER(21669, 60.0, Location(2910, 5476, 0));
        /**
         * Gets the Id
         * @return the Id
         */
        /**
         * Gets the exp
         * @return the Exp
         */
        /**
         * Gets the respawn Location
         * @return the Location.
         */

        companion object {
            /**
             * Gets the herb definition by the id.
             * @param id the objectId
             * @return the definition.
             */
            fun forId(id: Int): HerbDefinition? {
                for (def in values()) {
                    if (def.id == id) {
                        return def
                    }
                }
                return null
            }
        }
    }

    /**
     * Represents the season definitions.
     * @author SonicForce41
     * @version 1.0
     */
    enum class SeasonDefinitions
    /**
     * Constructs a new `GardenObjectsPlugin.java` `Object`.
     * @param treeId the tree id.
     * @param level the level.
     * @param farmExp the farm exp.
     * @param treeExp the tree exp.
     * @param fruitId the fruit id.
     * @param juiceId the juice id.
     * @param fruitAmt the fruit amt.
     * @param boost the boost.
     * @param energy the energy.
     * @param osmanExp the osman exp.
     * @param gateId the gate id.
     * @param respawn the respawn.
     */(
        /**
         * The treeId
         */
        val treeId: Int,
        /**
         * The level
         */
        val level: Int,
        /**
         * The farming experience recieved from picking
         */
        val farmExp: Double,
        /**
         * The thieving experience recieved from picking
         */
        val exp: Double,
        /**
         * The fruitId id
         */
        val fruitId: Int,
        /**
         * The juice id
         */
        val juiceId: Int,
        /**
         * The fruit amt
         */
        val fruitAmt: Int,
        /**
         * The boost
         */
        val boost: Int,
        /**
         * The energy
         */
        val energy: Int,
        /**
         * The experience recieved from osman
         */
        val osmanExp: Double,
        /**
         * The gate id
         */
        val gateId: Int,
        /**
         * The respawn location
         */
        val respawn: Location
    ) {
        WINTER(21769, 1, 30.0, 70.0, 10847, 10851, 5, 0, 10, 350.0, 21709, Location(2907, 5470, 0)), SPRING(
            21767,
            25,
            40.0,
            337.5,
            10844,
            10848,
            4,
            1,
            20,
            1350.0,
            21753,
            Location(2916, 5473, 0)
        ),
        AUTUMN(21768, 45, 50.0, 783.3, 10846, 10850, 3, 2, 30, 2350.0, 21731, Location(2913, 5467, 0)), SUMMER(
            21766,
            65,
            60.0,
            1500.0,
            10845,
            10849,
            2,
            3,
            40,
            3000.0,
            21687,
            Location(2910, 5476, 0)
        );
        /**
         * Gets the treeId
         * @return the treeId
         */
        /**
         * Gets the level
         * @return the Level
         */
        /**
         * Gets the farmExp
         * @return the farmExp
         */
        /**
         * Gets the exp from tree
         * @return the treeExp
         */
        /**
         * Gets the fruit Id
         * @return the fruitId
         */
        /**
         * Gets the juice Id
         * @return the juiceId
         */
        /**
         * Gets the fruid amt
         * @return the fruitAmt
         */
        /**
         * Gets the theiving boost
         * @return the boost
         */
        /**
         * Gets the run energy restoring
         * @return the energy
         */
        /**
         * Gets the experience recieved from osman
         * @return the osmanExp
         */
        /**
         * Gets the gate Id
         * @return the gateId
         */
        /**
         * Gets the respawn location
         * @return the respawn Location
         */

        companion object {
            /**
             * Gets the def by the fruit id.
             * @param fruitId the fruit id.
             * @return the definition.
             */
            fun forFruitId(fruitId: Int): SeasonDefinitions? {
                for (def in values()) {
                    if (def == null) continue
                    if (fruitId == def.fruitId) return def
                }
                return null
            }

            /**
             * Gets the def by the gate Id.
             * @param gateId the gateId
             * @return the def.
             */
            @JvmStatic
            fun forGateId(gateId: Int): SeasonDefinitions? {
                for (def in values()) {
                    if (gateId == def.gateId) return def
                }
                return null
            }

            /**
             * Gets the def by the juice id.
             * @param juiceId the juice id.
             * @return the def.
             */
            fun forJuiceId(juiceId: Int): SeasonDefinitions? {
                for (def in values()) {
                    if (def == null) continue
                    if (juiceId == def.juiceId) return def
                }
                return null
            }

            /**
             * Gets the season def by the tree id.
             * @param treeId the tree id.
             * @return the def.
             */
            fun forTreeId(treeId: Int): SeasonDefinitions? {
                for (def in values()) {
                    if (def == null) continue
                    if (treeId == def.treeId) return def
                }
                return null
            }
        }
    }

    /**
     * Use with Plugin for Sq'irk Juice making
     * @author SonicForce41
     */
    class SqirkJuicePlugin : UseWithHandler(10844, 10845, 10846, 10847) {
        override fun handle(event: NodeUsageEvent): Boolean {
            val item: Item = event.getUsedItem()
            val with: Item = event.getBaseItem()
            val player: Player = event.getPlayer()
            val def = SeasonDefinitions.forFruitId(item.id)
            if (with == null || player == null || def == null) return true
            val amt = player.inventory.getAmount(item)
            if (!player.inventory.containItems(1919)) {
                player.dialogueInterpreter.open(43382, 0)
                return true
            }
            if (amt < def.fruitAmt) {
                player.dialogueInterpreter.open(43382, 1, item.id)
                return true
            }
            player.animate(CRUSH_ITEM)
            player.skills.addExperience(Skills.COOKING, 5.0, true)
            player.inventory.remove(Item(item.id, def.fruitAmt))
            player.inventory.remove(Item(1919))
            player.inventory.add(Item(def.juiceId))
            player.dialogueInterpreter.sendDialogue("You squeeze " + def.fruitAmt + " sq'irks into an empty glass.")
            return true
        }

        @Throws(Throwable::class)
        override fun newInstance(arg: Any?): Plugin<Any?> {
            UseWithHandler.addHandler(233, UseWithHandler.ITEM_TYPE, this)
            return this
        }

        companion object {
            /**
             * The crushing an item with pestle and mortar animation.
             */
            private val CRUSH_ITEM = Animation(364)
        }
    }

    /**
     * Represents the dialougue of the osman NPC.
     * @author 'Vexia
     * @author SonicForce41
     * @date 31/12/2013
     */
    @Initializable
    class OsmanDialogue : DialoguePlugin {
        /**
         * Represents the quest instance.
         */
        private var quest: Quest? = null

        /**
         * Represents the count of materials you have gathered.
         */
        private var itemCount = 0

        /**
         * Constructs a new `OsmanDialogue` `Object`.
         */
        constructor() {
            /**
             * empty.
             */
        }

        /**
         * Constructs a new `OsmanDialogue` `Object`.
         * @param player the Player
         */
        constructor(player: Player?) : super(player) {}

        override fun newInstance(player: Player): DialoguePlugin {
            return OsmanDialogue(player)
        }

        override fun open(vararg args: Any): Boolean {
            npc = args[0] as NPC
            quest = player.questRepository.getQuest("Prince Ali Rescue")
            when (quest!!.getStage(player)) {
                100 -> {
                    interpreter.sendDialogues(player, null, "I'd like to talk about sq'irks.")
                    stage = 0
                    return true
                }
                60 -> {
                    interpreter.sendDialogues(
                        npc,
                        null,
                        "The prince is safe on his way home with Leela.",
                        "You can pick up your payment from the chancellor."
                    )
                    stage = 0
                    return true
                }
                40, 50 -> {
                    interpreter.sendDialogues(npc, null, "Speak to Leela for any further instructions.")
                    stage = 0
                }
                30 -> {
                    interpreter.sendDialogues(player, null, "Can you tell me what I still need to get?")
                    stage = 0
                }
                20 -> {
                    if (!player.getInventory().containsItem(KEY_PRINT)) {
                        interpreter.sendDialogues(player, null, "Can you tell me what I need to get?")
                    } else if (!player.getInventory().containsItem(BRONZE_BAR) && player.getInventory().containsItem(KEY_PRINT)) {
                        interpreter.sendDialogues(
                            npc,
                            null,
                            "Good, you have the print of the key. Get a bar of",
                            "bronze, too, and I can get the key mad."
                        )
                        stage = 70
                    } else {
                        interpreter.sendDialogues(npc, null, "Well done; we can make the key now.")
                        stage = 80
                    }
                    return true
                }
                10 -> interpreter.sendDialogues(player, null, "The chancellor trusts me. I have come for instructions.")
                0 -> interpreter.sendDialogues(player, null, "I'd like to talk about sq'irks.")
                else -> {
                }
            }
            stage = 0
            return true
        }

        override fun handle(interfaceId: Int, buttonId: Int): Boolean {
            when (quest!!.getStage(player)) {
                100 -> when (stage) {
                    else -> handleSqirks(buttonId)
                }
                60 -> end()
                40, 50 -> end()
                30 -> when (stage) {
                    82 -> end()
                    0 -> {
                        interpreter.sendDialogues(npc, null, "You can collect the key from Leela.")
                        stage = 11
                    }
                    11 -> {
                        if (player.getInventory().containsItem(YELLOW_WIG)) {
                            interpreter.sendDialogues(npc, null, "The wig you have got, well done.")
                            itemCount++
                        } else {
                            interpreter.sendDialogues(
                                npc,
                                null,
                                "You need a wig, maybe made from wool. If you find",
                                "someone who can work with wool ask them about it.",
                                "There's a witch nearby may be able to help you dye it."
                            )
                        }
                        stage = 12
                    }
                    12 -> {
                        if (player.getInventory().containsItem(SKIRT)) {
                            interpreter.sendDialogues(npc, null, "You have got the skirt, good.")
                            itemCount++
                        } else {
                            interpreter.sendDialogues(npc, null, "You will need to get a pink skirt, same as Keli's.")
                        }
                        stage = 13
                    }
                    13 -> {
                        if (player.getInventory().containsItem(PASTE)) {
                            interpreter.sendDialogues(
                                npc,
                                null,
                                "You have the skin paint, well done. I thought you would",
                                "struggle to make that."
                            )
                            itemCount++
                        } else {
                            interpreter.sendDialogues(
                                npc,
                                null,
                                "We still need something to colour the Prince's skin",
                                "lighter. There's a witch close to here. She knows about",
                                "many things. She may know some way to make the",
                                "skin lighter."
                            )
                        }
                        stage = if (itemCount == 3) 14 else 15
                    }
                    14 -> {
                        interpreter.sendDialogues(npc, null, "You do have everything for the disguise.")
                        stage = 15
                    }
                    15 -> if (player.getInventory().containsItem(ROPE)) {
                        interpreter.sendDialogues(
                            npc,
                            null,
                            "You have the rope I see, to tie up Keli. That will be the",
                            "most dangerous part of the plan."
                        )
                        stage = 16
                    } else {
                        interpreter.sendDialogues(
                            npc,
                            null,
                            "You will still need some rope to tie up Keli, of course. I",
                            "heard that there's a good rope maker around here."
                        )
                        stage = 16
                    }
                    16 -> end()
                    20 -> {
                        interpreter.sendDialogues(
                            npc,
                            null,
                            "Yes, that is most important. There is no way you can",
                            "get the real key. It is on a chain around Keli's neck.",
                            "Almost impossible to steal."
                        )
                        stage = 21
                    }
                    21 -> end()
                }
                0 -> when (stage) {
                    else -> handleSqirks(buttonId)
                }
                20 -> when (stage) {
                    80 -> {
                        interpreter.sendDialogue("Osman takes the key imprint and the bronze bar.")
                        stage = 81
                    }
                    81 -> if (player.getInventory().remove(BRONZE_BAR) && player.getInventory().remove(KEY_PRINT)) {
                        interpreter.sendDialogues(npc, null, "Pick the key up from Leela.")
                        quest!!.setStage(player, 30)
                        stage = 82
                    }
                    82 -> end()
                    70 -> {
                        interpreter.sendDialogues(player, null, "I will get one and come back.")
                        stage = 71
                    }
                    71 -> end()
                    0 -> {
                        interpreter.sendDialogues(
                            npc,
                            null,
                            "A print of the key in soft clay and a bronze bar.",
                            "Then, collect the key from Leela."
                        )
                        stage = 1
                    }
                    1 -> {
                        interpreter.sendDialogues(
                            npc,
                            null,
                            "When you have the full disguise talk to",
                            "Leela and she will help you with the rest."
                        )
                        stage = 2
                    }
                    2 -> end()
                    10 -> {
                        interpreter.sendDialogues(
                            npc,
                            null,
                            "The prince is guarded by some stupid guards and a",
                            "clever woman. The woman is our only way to get the",
                            "prince out. Only she can walk freely about the area."
                        )
                        stage = 11
                    }
                    11 -> {
                        interpreter.sendDialogues(
                            npc,
                            null,
                            "I think you will need to tie her up. One coil of rope",
                            "should do for that. Then, disguise the prince as her to",
                            "get him out without suspicion."
                        )
                        stage = 12
                    }
                    12 -> {
                        interpreter.sendDialogues(player, null, "How good must the disguise be?")
                        stage = 13
                    }
                    13 -> {
                        interpreter.sendDialogues(
                            npc,
                            null,
                            "Only enough to fool the guards at a distance. Get a",
                            "skirt like hers. Same colour, same style. We will only",
                            "have a short time."
                        )
                        stage = 14
                    }
                    14 -> {
                        interpreter.sendDialogues(
                            npc,
                            null,
                            "Get a blonde wig, too. that is up to you to make or",
                            "find. Something to colour the skin of the prince."
                        )
                        stage = 15
                    }
                    15 -> {
                        interpreter.sendDialogues(
                            npc,
                            null,
                            "My daughter and top spy, Leela, can help you. She has",
                            "sent word that she has discovered where they are",
                            "keeping the prince."
                        )
                        stage = 16
                    }
                    16 -> {
                        interpreter.sendDialogues(
                            npc,
                            null,
                            "It's near Draynor Village. She is lurking somewhere",
                            "near there now."
                        )
                        stage = 17
                    }
                    17 -> {
                        quest!!.setStage(player, 20)
                        interpreter.sendOptions(
                            "Select an Option",
                            "Explain the first thing again.",
                            "What is the second thing you need?",
                            "Okay, I better go find some things."
                        )
                        stage = 50
                    }
                    20 -> {
                        interpreter.sendDialogues(
                            npc,
                            null,
                            "We need the key, or we need a copy made. If you can",
                            "get some soft clay then you can copy the key..."
                        )
                        stage = 21
                    }
                    21 -> {
                        interpreter.sendDialogues(
                            npc,
                            null,
                            "...If you can convince Lady Keli to show it to you",
                            "for a moment. She is very boastful.",
                            "It should not be too hard."
                        )
                        stage = 22
                    }
                    22 -> {
                        interpreter.sendDialogues(npc, null, "Bring the imprint to me, with a bar of bronze.")
                        stage = 23
                    }
                    23 -> {
                        quest!!.setStage(player, 20)
                        interpreter.sendOptions(
                            "Select an Option",
                            "What is the first thing I must do?",
                            "What exactly is the second thing you need?",
                            "Okay, I better go find some things."
                        )
                        stage = 24
                    }
                    24 -> when (buttonId) {
                        1 -> {
                            interpreter.sendDialogues(player, null, "What is the first thing I must do?")
                            stage = 10
                        }
                        2 -> {
                            interpreter.sendDialogues(player, null, "What exactly is the second thing I must do?")
                            stage = 20
                        }
                        3 -> {
                            interpreter.sendDialogues(player, null, "Okay, I better go find some things.")
                            stage = 25
                        }
                    }
                    25 -> {
                        interpreter.sendDialogues(
                            npc,
                            null,
                            "May good luck travel with you. Don't forget to find",
                            "Leela. It can't be done without her help."
                        )
                        stage = 26
                    }
                    26 -> end()
                    50 -> when (buttonId) {
                        1 -> {
                            interpreter.sendDialogues(player, null, "Explain the first thing again.")
                            stage = 10
                        }
                        2 -> {
                            interpreter.sendDialogues(player, null, "What is th second thing you need?")
                            stage = 20
                        }
                        3 -> {
                            interpreter.sendDialogues(player, null, "Okay, I better go find some things.")
                            stage = 25
                        }
                    }
                }
                10 -> when (stage) {
                    0 -> {
                        interpreter.sendDialogues(
                            npc,
                            null,
                            "Our prince is captive by the Lady Keli. We just need",
                            "to make the rescue. There are two things we need",
                            "you to do."
                        )
                        stage = 1
                    }
                    1 -> {
                        interpreter.sendOptions(
                            "Select an Option",
                            "What is the first thing I must do?",
                            "What is the second thing you need?"
                        )
                        stage = 3
                    }
                    3 -> when (buttonId) {
                        1 -> {
                            interpreter.sendDialogues(player, null, "What is the first thing I must do?")
                            stage = 10
                        }
                        2 -> {
                            interpreter.sendDialogues(player, null, "What is the second thing you need?")
                            stage = 20
                        }
                    }
                    10 -> {
                        interpreter.sendDialogues(
                            npc,
                            null,
                            "The prince is guarded by some stupid guards and a",
                            "clever woman. The woman is our only way to get the",
                            "prince out. Only she can walk freely about the area."
                        )
                        stage = 11
                    }
                    11 -> {
                        interpreter.sendDialogues(
                            npc,
                            null,
                            "I think you will need to tie her up. One coil of rope",
                            "should do for that. Then, disguise the prince as her to",
                            "get him out without suspicion."
                        )
                        stage = 12
                    }
                    12 -> {
                        interpreter.sendDialogues(player, null, "How good must the disguise be?")
                        stage = 13
                    }
                    13 -> {
                        interpreter.sendDialogues(
                            npc,
                            null,
                            "Only enough to fool the guards at a distance. Get a",
                            "skirt like hers. Same colour, same style. We will only",
                            "have a short time."
                        )
                        stage = 14
                    }
                    14 -> {
                        interpreter.sendDialogues(
                            npc,
                            null,
                            "Get a blonde wig, too. that is up to you to make or",
                            "find. Something to colour the skin of the prince."
                        )
                        stage = 15
                    }
                    15 -> {
                        interpreter.sendDialogues(
                            npc,
                            null,
                            "My daughter and top spy, Leela, can help you. She has",
                            "sent word that she has discovered where they are",
                            "keeping the prince."
                        )
                        stage = 16
                    }
                    16 -> {
                        interpreter.sendDialogues(
                            npc,
                            null,
                            "It's near Draynor Village. She is lurking somewhere",
                            "near there now."
                        )
                        stage = 17
                    }
                    17 -> {
                        quest!!.setStage(player, 20)
                        interpreter.sendOptions(
                            "Select an Option",
                            "Explain the first thing again.",
                            "What is the second thing you need?",
                            "Okay, I better go find some things."
                        )
                        stage = 50
                    }
                    20 -> {
                        interpreter.sendDialogues(
                            npc,
                            null,
                            "We need the key, or we need a copy made. If you can",
                            "get some soft clay then you can copy the key..."
                        )
                        stage = 21
                    }
                    21 -> {
                        interpreter.sendDialogues(
                            npc,
                            null,
                            "...If you can convince Lady Keli to show it to you",
                            "for a moment. She is very boastful.",
                            "It should not be too hard."
                        )
                        stage = 22
                    }
                    22 -> {
                        interpreter.sendDialogues(npc, null, "Bring the imprint to me, with a bar of bronze.")
                        stage = 23
                    }
                    23 -> {
                        quest!!.setStage(player, 20)
                        interpreter.sendOptions(
                            "Select an Option",
                            "What is the first thing I must do?",
                            "What exactly is the second thing you need?",
                            "Okay, I better go find some things."
                        )
                        stage = 24
                    }
                    24 -> when (buttonId) {
                        1 -> {
                            interpreter.sendDialogues(player, null, "What is the first thing I must do?")
                            stage = 10
                        }
                        2 -> {
                            interpreter.sendDialogues(player, null, "What exactly is the second thing I must do?")
                            stage = 20
                        }
                        3 -> {
                            interpreter.sendDialogues(player, null, "Okay, I better go find some things.")
                            stage = 25
                        }
                    }
                    25 -> {
                        interpreter.sendDialogues(
                            npc,
                            null,
                            "May good luck travel with you. Don't forget to find",
                            "Leela. It can't be done without her help."
                        )
                        stage = 26
                    }
                    26 -> end()
                }
                else -> {
                }
            }
            return true
        }

        /**
         * Method used to handle the sqirk juice dials.
         * @param buttonId the buttonId.
         */
        fun handleSqirks(buttonId: Int) {
            when (stage) {
                0 -> if (!hasSqirks()) {
                    interpreter.sendOptions(
                        "Select an Option",
                        "Where do I get sq'irks?",
                        "Why can't you get the sq'irks yourself?",
                        "How should I squeeze the fruit?",
                        "Is there a reward for getting these sq'irks?",
                        "What's so good about sq'irk juice then?"
                    )
                    stage = 200
                } else {
                    player(if (hasSqirkJuice()) "I have some sq'riks juice for you." else "I have some sq'irks for you.")
                    stage = 300
                }
                300 -> if (hasSqirkJuice()) {
                    val exp = experience
                    player.getSkills().addExperience(Skills.THIEVING, exp, true)
                    interpreter.sendDialogue(
                        "Osman imparts some Thieving advice to",
                        "you ( $exp Thieving experience points )",
                        "as a reward for the sq'irk juice."
                    )
                    stage = 304
                } else {
                    npc("Uh, thanks, but is there any chance that you", "could squeeze the fruit into a glass for me?")
                    stage = 301
                }
                301 -> {
                    interpreter.sendOptions(
                        "Select an Option",
                        "How should I squeeze the fruit?",
                        "Can't you do that yourself?"
                    )
                    stage = 302
                }
                302 -> when (buttonId) {
                    1 -> {
                        player("How should I squeeze the fruit?")
                        stage = 130
                    }
                    2 -> {
                        player("Can't you do that yourself?")
                        stage = 303
                    }
                }
                304 -> end()
                303 -> {
                    player("I only carry knives or other such devices on me", "when I'm on the job.")
                    stage = 119
                }
                200 -> when (buttonId) {
                    1 -> {
                        interpreter.sendDialogues(player, null, "Where do I get sq'irks?")
                        stage = 110
                    }
                    2 -> {
                        interpreter.sendDialogues(player, null, "Why can't you get the sq'irks yourself?")
                        stage = 120
                    }
                    3 -> {
                        interpreter.sendDialogues(player, null, "How should I squeeze the fruit?")
                        stage = 130
                    }
                    4 -> {
                        interpreter.sendDialogues(player, null, "Is there a reward for getting these sq'irks?")
                        stage = 140
                    }
                    5 -> {
                        interpreter.sendDialogues(player, null, "What's so good about sq'irk juice then?")
                        stage = 150
                    }
                }
                110 -> {
                    npc(
                        "There is a sorceress near the south-eastern edge of Al",
                        "Kharid who grows them. Once upon a time, we",
                        "considered each other friends."
                    )
                    stage = 111
                }
                111 -> {
                    player("What happened?")
                    stage = 112
                }
                112 -> {
                    npc("We fell out, and now she won't give me any more", "fruit.")
                    stage = 113
                }
                113 -> {
                    player("So all I have to do is ask her for some fruit for you?")
                    stage = 114
                }
                114 -> {
                    npc(
                        "I doubt it will be that easy. She is not renowned for",
                        "her generosity and is very secretive about her garden's",
                        "location."
                    )
                    stage = 115
                }
                115 -> {
                    player("Oh come on, it should be easy enough to find.")
                    stage = 116
                }
                116 -> {
                    npc(
                        "Her garden has remained hidden even to me - the chief",
                        "spy of Al Kharid. I believe her garden must be hidden",
                        "by magical means."
                    )
                    stage = 117
                }
                117 -> {
                    player("This should be an interesting task. How many sq'irks do", "you want?")
                    stage = 118
                }
                118 -> {
                    npc(
                        "I'll reward you for as many as you can get your",
                        "hands on, but could you please squeeze the fruit into a",
                        "glass first?"
                    )
                    stage = 119
                }
                119 -> {
                    interpreter.sendOptions(
                        "Select an Option",
                        "I've another question about sq'irks.",
                        "Thanks for the information."
                    )
                    stage = 98
                }
                98 -> when (buttonId) {
                    1 -> {
                        interpreter.sendOptions(
                            "Select an Option",
                            "Where do I get sq'irks?",
                            "Why can't you get the sq'irks yourself?",
                            "How should I squeeze the fruit?",
                            "Is there a reward for getting these sq'irks?",
                            "What's so good about sq'irk juice then?"
                        )
                        stage = 200
                    }
                    2 -> {
                        player("Thanks for the information.")
                        stage = 99
                    }
                }
                99 -> end()
                120 -> {
                    npc(
                        "I may have mentioned that I had a falling out with the",
                        "Sorceress. Well, unsurprisingly, she refuses to give me",
                        "any more of her garden's produce."
                    )
                    stage = 119
                }
                130 -> {
                    npc(
                        "Use a pestle and mortar to squeeze the sr'irks. Make",
                        "sure you have an empty glass with you to collect the",
                        "juice."
                    )
                    stage = 119
                }
                140 -> {
                    npc(
                        "Of course there is. I am a generous man. I'll teach",
                        "you the art of Thieving for your troubles."
                    )
                    stage = 141
                }
                141 -> {
                    player("How much training will you give?")
                    stage = 142
                }
                142 -> {
                    npc("That depends on the quantity and ripeness of the", "sq'irks you put into the juice.")
                    stage = 143
                }
                143 -> {
                    player("That sounds fair enough.")
                    stage = 119
                }
                150 -> {
                    npc(
                        "Ah it's sweet, sweet nectar for a thief or spy; it makes",
                        "light fingers lighter, fleet feet flightier and comes in four",
                        "different colours for those who are easily amused."
                    )
                    stage = 151
                }
                151 -> {
                    interpreter.sendDialogue("Osman starts salivating at the thought of sq'irk juice.")
                    stage = 152
                }
                152 -> {
                    player("It wouldn't have addictive properties, would it?")
                    stage = 153
                }
                153 -> {
                    npc(
                        "It only holds power over those with poor self-control,",
                        "something which I have an abundance of."
                    )
                    stage = 154
                }
                154 -> {
                    player("I see.")
                    stage = 119
                }
            }
        }

        /**
         * Gets the experience the player can recieve.
         * @return the experience.
         */
        val experience: Double
            get() {
                var total = 0.0
                for (juiceId in JUICES) {
                    val def = SeasonDefinitions.forJuiceId(juiceId) ?: continue
                    val amount: Int = player.getInventory().getAmount(Item(juiceId))
                    total += amount * def.osmanExp
                    player.getInventory().remove(Item(juiceId, amount))
                }
                player.getInventory().refresh()
                return total
            }

        /**
         * Checks wether the **Player** has sq'irk.
         * @return `True`: Player has sq'irk.
         */
        fun hasSqirkFruit(): Boolean {
            for (i in FRUITS) {
                if (player.getInventory().contains(i, 1)) {
                    return true
                }
            }
            return false
        }

        /**
         * Checks wether the **Player** has sq'irk juice
         * @return `True`: Player has sq'irk juice.
         */
        fun hasSqirkJuice(): Boolean {
            for (i in JUICES) {
                if (player.getInventory().contains(i, 1)) {
                    return true
                }
            }
            return false
        }

        /**
         * Checks wether the **Player** has sq'irks (fruit/juice).
         * @return `True`: Player has either a fruit or squeezed juice.
         */
        fun hasSqirks(): Boolean {
            return hasSqirkFruit() || hasSqirkJuice()
        }

        override fun getIds(): IntArray {
            return intArrayOf(924, 5282)
        }

        companion object {
            /**
             * Represents the key print item.
             */
            private val KEY_PRINT = Item(2423)

            /**
             * Represents the bronze bar item.
             */
            private val BRONZE_BAR = Item(2349)

            /**
             * Represents the rope item.
             */
            private val ROPE = Item(954)

            /**
             * Represents the pink skirt item.
             */
            private val SKIRT = Item(1013)

            /**
             * Represents the yellow wig item.
             */
            private val YELLOW_WIG = Item(2419)

            /**
             * Represents the skin paste item.
             */
            private val PASTE = Item(2424)

            /**
             * Represents the juices.
             */
            private val JUICES = intArrayOf(10848, 10849, 10850, 10851)

            /**
             * Represents the fruits.
             */
            private val FRUITS = intArrayOf(10844, 10845, 10846, 10847)
        }
    }

    companion object {
        /**
         * Represents the animation to use.
         */
        private val ANIMATION = Animation(827)

        /**
         * Represents the herbs items used for the elemental garden picking.
         */
        private val HERBS =
            intArrayOf(199, 201, 203, 205, 207, 209, 211, 213, 215, 217, 219, 2485, 3049, 3051, 199, 201, 203, 205)

        /**
         * Represents the drinking animation.
         */
        private val DRINK_ANIM = Animation(5796)

        /**
         * Represents the teleport anim.
         */
        private val TELE = Animation(714)

        /**
         * Represents the graphics to use.
         */
        private val GRAPHICS = Graphics(111, 100, 1)

        /**
         * Represents the picking fruit anim.
         */
        private val PICK_FRUIT = Animation(2280)
    }
}

    /**
     * Dialogue for Sqirk making
     * @author SonicForce41
     */
    class SqirkMakingDialogue : DialoguePlugin {
        private var dialogueId = 0
        private var definition: GardenObjectsPlugin.SeasonDefinitions? = null

        /**
         * Constructs a new `SqirkMakingDialogue.java` `Object`.
         */
        constructor() {}

        /**
         * Constructs a new `SqirkMakingDialogue.java` `Object`.
         * @param player the Player
         */
        constructor(player: Player?) : super(player) {}

        override fun getIds(): IntArray {
            return intArrayOf(43382)
        }

        override fun handle(interfaceId: Int, buttonId: Int): Boolean {
            when (dialogueId) {
                0 -> end()
                1 -> when (stage) {
                    0 -> {
                        interpreter.sendDialogue("You need " + definition!!.fruitAmt + " sq'irks of this kind to fill a glass of juice.")
                        stage = 1
                    }
                    1 -> end()
                }
            }
            return true
        }

        override fun newInstance(player: Player): DialoguePlugin {
            return SqirkMakingDialogue(player)
        }

        override fun open(vararg args: Any): Boolean {
            dialogueId = args[0] as Int
            when (dialogueId) {
                0 -> interpreter.sendDialogues(
                    player,
                    FacialExpression.THINKING,
                    "I should get an empty beer glass to",
                    "hold the juice before I squeeze the fruit."
                )
                1 -> {
                    definition = GardenObjectsPlugin.SeasonDefinitions.forFruitId(args[1] as Int)
                    if (definition == null) end()
                    interpreter.sendDialogues(
                        player,
                        FacialExpression.THINKING,
                        "I think I should wait till I have",
                        "enough fruits to make a full glass."
                    )
                }
            }
            stage = 0
            return true
        }
    }
