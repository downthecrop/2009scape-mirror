package content.region.kandarin.quest.observatoryquest

import content.data.Quests
import content.region.kandarin.quest.observatoryquest.ObservatoryQuest.Companion.attributeFinishedCutscene
import content.region.kandarin.quest.observatoryquest.ObservatoryQuest.Companion.attributeRandomChest
import core.ServerConstants
import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.FacialExpression
import core.game.global.action.DoorActionHandler
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.npc.NPC
import core.game.node.entity.skill.Skills
import core.game.world.map.Location
import core.tools.END_DIALOGUE
import core.tools.RandomFunction
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import org.rs09.consts.Scenery

class ObservatoryQuestListeners : InteractionListener {

    companion object {
        val chestMap = mapOf(
            Scenery.CHEST_2191 to Scenery.CHEST_2194,
            Scenery.CHEST_25385 to Scenery.CHEST_25386,
            Scenery.CHEST_25387 to Scenery.CHEST_25388,
            Scenery.CHEST_25389 to Scenery.CHEST_25390,
            Scenery.CHEST_25391 to Scenery.CHEST_25392
        )

        val reverseChestMap = chestMap.map { (k, v) -> v to k }.toMap()

        /* Comments show only the default order BEFORE randomization. A randomized
           number (0-10) is calculated the first time the player goes down the stairs,
           and is then added to the map value modulo 11, rotating the map.
         */

        val chestLocations = mapOf(
            Location(2333, 9405) to 0, // key
            Location(2312, 9400) to 1, // spider
            Location(2310, 9374) to 2, // spider
            Location(2348, 9383) to 3, // empty
            Location(2356, 9380) to 4, // spider
            Location(2359, 9376) to 5, // empty
            Location(2360, 9366) to 6, // empty
            Location(2351, 9361) to 7, // spider
            Location(2364, 9355) to 8, // antipoison
            Location(2335, 9374) to 9, // spider
            Location(2326, 9360) to 10) // empty
    }

    override fun defineListeners() {

        on(Scenery.SIGNPOST_25397, SCENERY, "read") { player, _ ->
            openDialogue(player, object : DialogueFile(){
                override fun handle(componentID: Int, buttonID: Int) {
                    val servername = ServerConstants.SERVER_NAME
                    when(stage){
                        0 -> sendDialogueLines(player, "~ The Observatory ~", "Step ahead to the reception if you wish to explore $servername's most", "magnificent invention.").also {
                            stage++
                        }
                        1 -> player(FacialExpression.NEUTRAL, "Magnificent invention? I've seen some pretty magnificent", "things in my time. It'll have to be pretty impressive.").also { stage = END_DIALOGUE }
                    }
                }
            })
            return@on true
        }

        on(Scenery.ORRERY_25401, SCENERY, "view") { player, _ ->
            sendChat(player, "Oooooh, bizarre!")
            return@on true
        }

        on(Scenery.STAIRS_25432, SCENERY, "climb-down") { player, _ ->
            openInterface(player, 560)
            if (getAttribute(player, attributeRandomChest, null) == null) {
                setAttribute(player, attributeRandomChest, RandomFunction.random(11))
            }
            return@on true
        }
        // http://youtu.be/Kg84MYXgo-M -> You can always go to the observatory, no matter which stage.
        on(Scenery.STAIRS_25429, SCENERY, "climb up") { player, node ->
            if (node.location == Location(2335, 9351)) {
                if (!getAttribute(player, attributeFinishedCutscene, false) && getQuestStage(player, Quests.OBSERVATORY_QUEST) == 6) {
                    ObservatoryCutscene(player).start()
                }
                else teleport(player, Location(2439, 3164))
            } else {
                teleport(player, Location(2457, 3186))
            }
            return@on true
        }

        on(Scenery.STAIRS_25434, SCENERY, "climb-down") { player, _ ->
            teleport(player, Location(2335, 9350))
            return@on true
        }

        on(Scenery.STAIRS_25431, SCENERY, "climb-up") { player, _ ->
            teleport(player, Location(2443, 3160, 1))
            return@on true
        }

        on(Scenery.STAIRS_25437, SCENERY, "climb-down") { player, _ ->
            teleport(player, Location(2444, 3162, 0))
            return@on true
        }

        // All chests
        on(chestMap.keys.toIntArray(), SCENERY, "open") { player, node ->
            animate(player, 536)
            sendMessage(player, "You open the chest.")
            chestMap[node.id]?.let { replaceScenery(node as core.game.node.scenery.Scenery, it, 240) }
            return@on true
        }
        on(chestMap.values.toIntArray(), SCENERY, "close") { player, node ->
            animate(player, 535)
            reverseChestMap[node.id]?.let { replaceScenery(node as core.game.node.scenery.Scenery, it, -1) }
            return@on true
        }

        on(chestMap.values.toIntArray(), SCENERY, "search") { player, node ->
            val chest = chestLocations[node.location]?.plus(getAttribute(player, attributeRandomChest, 0))?.mod(11)
            when (chest) {
                0 -> {
                    if (inInventory(player, Items.GOBLIN_KITCHEN_KEY_601) || getQuestStage(player, Quests.OBSERVATORY_QUEST) != 4 || getAttribute(player, ObservatoryQuest.attributeUnlockedGate, false)) {
                        sendMessage(player, "You search the chest.")
                        sendMessage(player, "The chest is empty.")
                    } else {
                        lock(player,2)
                        queueScript(player, 2, QueueStrength.STRONG) {
                            if (inInventory(player, Items.GOBLIN_KITCHEN_KEY_601)) {
                                return@queueScript stopExecuting(player)
                            }
                            addItemOrDrop(player, Items.GOBLIN_KITCHEN_KEY_601)
                            sendItemDialogue(player, Items.GOBLIN_KITCHEN_KEY_601,"You find a kitchen key.")
                            return@queueScript stopExecuting(player)
                        }
                    }
                    animate(player, 537)
                    return@on true
                }
                1, 2, 4, 7, 9 -> {
                    sendMessage(player, "You search the chest.")
                    if (findLocalNPC(player, NPCs.POISON_SPIDER_1009) != null) {
                        sendMessage(player, "The chest is empty.")
                        animate(player, 537)
                        return@on true
                    }
                    sendMessage(player, "The chest contains a poisonous spider.")
                    val npc = NPC(NPCs.POISON_SPIDER_1009)
                    npc.location = player.location
                    npc.init()
                    npc.isRespawn = false
                    npc.moveStep()
                    npc.face(player)
                    animate(player, 537)
                    return@on true
                }
                8 -> {
                    sendMessage(player, "You search the chest.")
                    lock(player,2)
                    queueScript(player, 2, QueueStrength.STRONG) {
                        addItemOrDrop(player, Items.ANTIPOISON1_179)
                        sendMessage(player,"This chest contains some antipoison.")
                        return@queueScript stopExecuting(player)
                    }
                    animate(player, 537)
                    return@on true
                }
                else -> {
                    sendMessage(player, "You search the chest.")
                    sendMessage(player, "The chest is empty.")
                    animate(player, 537)
                    return@on true
                }
            }
        }

        on(intArrayOf(Scenery.KITCHEN_GATE_2199, Scenery.KITCHEN_GATE_2200), SCENERY, "open") { player, node ->
            if (getAttribute(player, ObservatoryQuest.attributeUnlockedGate, false)) {
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            } else if (getAttribute(player, ObservatoryQuest.attributeKilledGuard, false)) {
                if (removeItem(player, Items.GOBLIN_KITCHEN_KEY_601)) {
                    sendMessage(player, "The gate unlocks.")
                    sendMessage(player, "The key is useless now. You discard it.")
                    setAttribute(player, ObservatoryQuest.attributeUnlockedGate, true)
                    sendPlayerDialogue(player, "I had better be quick, there may be more guards about.")
                    DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
                } else {
                    // http://youtu.be/ZkUF-0eonls
                    sendMessage(player, "The gate is locked.")
                }
            } else if (getQuestStage(player, Quests.OBSERVATORY_QUEST) >= 4){
                sendMessage(player, "If you open the gate, the guard will hear you. You need to get rid of him.")
            } else sendMessage(player, "The gate is locked.")
            return@on true
        }

        on(NPCs.SLEEPING_GUARD_6122, NPC, "prod") { player, node ->
            if (getQuestStage(player, Quests.OBSERVATORY_QUEST) < 4) {
                return@on true
            }
            sendChat(node as NPC, "Oi, how dare you wake me up!")
            transformNpc(node, NPCs.GOBLIN_GUARD_489, 400)
            node.attack(player)
            return@on true
        }

        on(Scenery.GOBLIN_STOVE_25440, SCENERY, "inspect") { player, _ ->
            openDialogue(player, object : DialogueFile(){
                override fun handle(componentID: Int, buttonID: Int) {
                    when(stage){
                        0 -> sendDialogueLines(player, "The goblins appear to have been using the lens mould to cook their", "stew!").also { stage++ }
                        1 -> sendDialogueLines(player, "You shake out its contents and take it with you.").also { animate(player, 537); stage++ }
                        2 -> end().also {
                            sendChat(player, "Euuuw, that smells awful!")
                            setVarbit(player, ObservatoryQuest.goblinStoveVarbit ,1)
                            addItemOrDrop(player, Items.LENS_MOULD_602)
                        }
                    }
                }
            })
            return@on true
        }

        on(Scenery.GOBLIN_STOVE_25441, SCENERY, "inspect") { player, _ ->
            if (!inInventory(player, Items.LENS_MOULD_602)) addItemOrDrop(player, Items.LENS_MOULD_602)
            return@on true
        }

        onUseWith(ITEM, Items.LENS_MOULD_602, Items.MOLTEN_GLASS_1775) { player, _, with ->
            if (getStatLevel(player, Skills.CRAFTING) < 10) {
                sendMessage(player, "You need a crafting level of 10 to do this.")
                return@onUseWith true
            }

            sendMessage(player, "You pour the molten glass into the mould.")
            sendMessage(player, "You clasp it together.")
            sendItemDialogue(player, Items.OBSERVATORY_LENS_603, "It has produced a small, convex glass disc.")
            if (removeItem(player, with)) {
                addItemOrDrop(player, Items.OBSERVATORY_LENS_603)
            }
            return@onUseWith true
        }

        on(intArrayOf(Scenery.STAR_CHART_25578, Scenery.STAR_CHART_25579, Scenery.STAR_CHART_25580, Scenery.STAR_CHART_25581, Scenery.STAR_CHART_25582, Scenery.STAR_CHART_25583), SCENERY, "look-at") { player, _ ->
            openInterface(player, ObservatoryQuest.starChartsInterface)
            return@on true
        }

        on(intArrayOf(Scenery.TELESCOPE_25438, Scenery.TELESCOPE_25439), SCENERY, "view") { player, _ ->
            if (getQuestStage(player, Quests.OBSERVATORY_QUEST) >= 100) {
                openDialogue(player, TelescopeDialogue(), NPC(NPCs.OBSERVATORY_PROFESSOR_488))
            }
            else if (getQuestStage(player, Quests.OBSERVATORY_QUEST) >= 6) {
                if (getAttribute<Int?>(player, ObservatoryQuest.attributeTelescopeStar, null) == null) {
                    setAttribute(player, ObservatoryQuest.attributeTelescopeStar, (19..30).random())
                }
                val randomStar = getAttribute<Int?>(player, ObservatoryQuest.attributeTelescopeStar, null)
                openInterface(player, ObservatoryQuest.telescopeInterface)
                player.packetDispatch.sendModelOnInterface(ObservatoryQuestInterfaces.buttonToStarObjectMapping[randomStar]!!, ObservatoryQuest.telescopeInterface, 7, 0)
            } else {
                sendMessage(player, "The telescope is broken.")
            }
            return@on true
        }

        on(intArrayOf(Scenery.DOOR_25526, Scenery.DOOR_25527), SCENERY, "open") { player, _ ->
            sendMessage(player, "The door is locked.")
            return@on true
        }

        on(Scenery.GRAVE_OF_SCORPIUS_2211, SCENERY, "read") { player, _ ->
            sendMessage(player, "Here lies Scorpius: Only those who have seen beyond the stars may seek his counsel.")
            return@on true
        }
    }
}

class TelescopeDialogue : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        when(stage){
            0 -> npcl(FacialExpression.ASKING, "What do you see now?").also { stage++ }
            1 -> playerl("I can see a constellation through the telescope. It looks like Scorpio.").also { stage++ }
            2 -> npcl("Scorpio? Interesting. How very fitting.").also { stage++ }
            3 -> playerl(FacialExpression.ASKING, " What do you mean?").also { stage++ }
            4 -> npcl("Scorpius, the founder of all we know relating to astronomy. There's a book about him in the reception. Perhaps you should check it out, you might learn something.").also { stage++ }
            5 -> playerl("Then perhaps I shall.").also { stage = END_DIALOGUE }
        }
    }
}