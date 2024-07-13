package content.region.misthalin.barbvillage.stronghold.playersafety

 import core.api.*
 import core.game.activity.Cutscene
 import core.game.component.Component
 import core.game.global.action.DoorActionHandler
 import core.game.interaction.IntType
 import core.game.interaction.InteractionListener
 import core.game.interaction.InterfaceListener
 import core.game.node.Node
 import core.game.node.entity.player.Player
 import core.game.node.entity.player.link.emote.Emotes
 import core.game.node.scenery.Scenery
 import core.game.world.map.Location
 import org.rs09.consts.Items
 import org.rs09.consts.NPCs
 import org.rs09.consts.Scenery as SceneryConst


@Suppress("unused")
 class StrongHoldOfPlayerSafetyListener : InteractionListener{

     companion object{
         private val plaquesToIface = mapOf( // Door to interface
             SceneryConst.JAIL_DOOR_29595 to 701,
             SceneryConst.JAIL_DOOR_29596 to 703,
             SceneryConst.JAIL_DOOR_29597 to 711,
             SceneryConst.JAIL_DOOR_29598 to 695,
             SceneryConst.JAIL_DOOR_29599 to 312,
             SceneryConst.JAIL_DOOR_29600 to 706,
             SceneryConst.JAIL_DOOR_29601 to 698,
         )

         private val creviceClimbedAttribute = "player_strong:crevice_climbed"

     }
     override fun defineListeners() {

         // Test
         on(Items.TEST_PAPER_12626, IntType.ITEM, "take exam") { player, _ ->
             if (player.savedData.globalData.testStage == 2){
                 sendMessage(player, "You have already completed the test. Hand it in to Professor Henry for marking.")
             }
             else{
                 openInterface(player, 697)
             }
             return@on true
         }

         // Students
         on(intArrayOf(NPCs.STUDENT_7151, NPCs.STUDENT_7152, NPCs.STUDENT_7153, NPCs.STUDENT_7154,
             NPCs.STUDENT_7155, NPCs.STUDENT_7156, NPCs.STUDENT_7157), IntType.NPC, "Talk-to") { player, _ ->
             sendMessage(player, "This student is trying to focus on their work.")
             return@on true
         }

         // Jail teleports
         on(SceneryConst.JAIL_ENTRANCE_29603, IntType.SCENERY, "use") { player, _->
             teleport(player, Location.create(3082, 4229, 0))
             return@on true
         }
         on(SceneryConst.JAIL_ENTRANCE_29602, IntType.SCENERY, "leave") { player, _ ->
             teleport(player, Location.create(3074, 3456, 0))
             return@on true
         }
         on(SceneryConst.STAIRS_29589, IntType.SCENERY, "climb-up") { player, _ ->
             if (player.globalData.hasReadPlaques()){
                 teleport(player, Location.create(3083, 3452, 0))
             }
             else{
                 sendMessage(player, "You need to read the jail plaques before the guard will allow you upstairs")
             }

             return@on true
         }
         // Exam room
         on(SceneryConst.DOOR_29732, IntType.SCENERY, "open") { player, node ->
             if (player.globalData.testStage > 0){
                 // The player has talked to the prof
                 DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
             }
             else{
                 sendMessage(player, "The door is locked")
             }
             return@on true
         }
         on(SceneryConst.STAIRS_29592, IntType.SCENERY, "climb-down") { player, _ ->
             teleport(player, Location.create(3086, 4247, 0))
             return@on true
         }

         // crevice (and rope)
         on(SceneryConst.CREVICE_29728, IntType.SCENERY, "enter"){ player, _ ->
             if (getAttribute(player, creviceClimbedAttribute, false)){
                 teleport(player, Location.create(3159, 4279, 3))
             }
             else{
                 // todo find what the crevice should say
                 sendMessage(player, "There's no way down")
             }
             return@on true
         }
         on(SceneryConst.ROPE_29729, IntType.SCENERY, "climb"){ player, _ ->
             if (!getAttribute(player, creviceClimbedAttribute, false)){
                 setAttribute(player, creviceClimbedAttribute, true)
             }
             teleport(player, Location.create(3077, 3462, 0))
             return@on true
         }

         // Plaques
         on(intArrayOf(SceneryConst.JAIL_DOOR_29595, SceneryConst.JAIL_DOOR_29596, SceneryConst.JAIL_DOOR_29597,
             SceneryConst.JAIL_DOOR_29598, SceneryConst.JAIL_DOOR_29599, SceneryConst.JAIL_DOOR_29600,
             SceneryConst.JAIL_DOOR_29601) , IntType.SCENERY, "Read-plaque on") { player, node ->
             read(player, node)
             return@on true
         }

         // The dungeon
         on(SceneryConst.POSTER_29586, IntType.SCENERY, "pull-back") { player, _ ->
             sendDialogue(player, "There appears to be a tunnel behind this poster.")
             teleport(player, Location.create(3140, 4230, 2))
             return@on true
         }
         on(SceneryConst.TUNNEL_29623, IntType.SCENERY, "use") {player, _ ->
             teleport(player, Location.create(3077, 4235, 0))
             return@on true
         }

         on(SceneryConst.AN_OLD_LEVER_29730, IntType.SCENERY, "pull") {player, _ ->
             sendMessage(player, "You hear the cogs and gears moving and a distant unlocking sound.")
             setVarp(player, 1203, (1 shl 29) or (1 shl 26), true)
             return@on true
         }

         on(SceneryConst.AN_OLD_LEVER_29731, IntType.SCENERY, "pull") {player, _ ->
             sendMessage(player, "You hear cogs and gears moving and the sound of heavy locks falling into place.")
             setVarp(player, 1203, 1 shl 29, true)
             return@on true
         }

         // the same jail door is used in 4 different places
         on(SceneryConst.JAIL_DOOR_29624, IntType.SCENERY, "open") { player, _ ->
             if (getVarp(player, 1203) and (1 shl 26) == 0) {
                 // The door is locked
                 sendMessage(player, "The door seems to be locked by some kind of mechanism.")
                 return@on true
             }
             if (player.location.z == 2) {
                 // Floor 2 to hidden tunnel
                 teleport(player, Location.create(3177, 4266, 0))
             }
             else if (player.location.z == 1){
                 // Floor 1 to hidden tunnel
                 teleport(player, Location.create(3143, 4270, 0))
             }
             else {
                 // Leaving the hidden tunnel
                 if (player.location.x < 3150){
                     // Leaving by the west exit (to floor 1)
                     teleport(player, Location.create(3142, 4272, 1))
                 }
                 else{
                     // Must be exiting by the east exit (to floor 2)
                     teleport(player, Location.create(3177, 4269, 2))
                 }
             }
             return@on true
         }

         // the 4 stairs in the middle of the 1st/2nd floor
         // NE
         on(SceneryConst.STAIRS_29667, IntType.SCENERY, "climb-down") { player, _ ->
             teleport(player, Location.create(3160, 4249, 1))
             return@on true
         }
         on(SceneryConst.STAIRS_29668, IntType.SCENERY, "climb-up") { player, _ ->
             teleport(player, Location.create(3158, 4250, 2))
             return@on true
         }

         // SE
         on(SceneryConst.STAIRS_29663, IntType.SCENERY, "climb-down") { player, _ ->
             teleport(player, Location.create(3160, 4246, 1))
             return@on true
         }
         on(SceneryConst.STAIRS_29664, IntType.SCENERY, "climb-up") { player, _ ->
             teleport(player, Location.create(3158, 4245, 2))
             return@on true
         }

         // SW
         on(SceneryConst.STAIRS_29655, IntType.SCENERY, "climb-down") { player, _ ->
             teleport(player, Location.create(3146, 4246, 1))
             return@on true
         }
         on(SceneryConst.STAIRS_29656, IntType.SCENERY, "climb-up") { player, _ ->
             teleport(player, Location.create(3149, 4244, 2))
             return@on true
         }

         // NW
         on(SceneryConst.STAIRS_29659, IntType.SCENERY, "climb-down") { player, _ ->
             teleport(player, Location.create(3146, 4249, 1))
             return@on true
         }
         on(SceneryConst.STAIRS_29660, IntType.SCENERY, "climb-up") { player, _ ->
             teleport(player, Location.create(3148, 4250, 2))
             return@on true
         }


         // rewards chest
         on(SceneryConst.TREASURE_CHEST_29577, IntType.SCENERY, "open"){ player, _ ->
             setVarbit(player, 4499, 1, true)
             return@on true
         }
         on(SceneryConst.TREASURE_CHEST_29578, IntType.SCENERY, "search"){ player, _ ->
             // Give the player rewards
             if (player.globalData.testStage == 3){
                 // Check the player has enough slots
                 if ((freeSlots(player) == 0) or
                     ((freeSlots(player) == 1) and !inInventory(player, Items.COINS_995))){
                     sendDialogue(player, "You do not have enough inventory space!")
                 }
                 else{
                     player.emoteManager.unlock(Emotes.SAFETY_FIRST)
                     addItem(player, Items.COINS_995, 10000)
                     addItem(player, Items.SAFETY_GLOVES_12629)
                     sendItemDialogue(player, Items.SAFETY_GLOVES_12629,
                         "You open the chest to find a large pile of gold, along with a pair of safety gloves. ")

 					 player.globalData.testStage = 4
                 }
             }
             else {
                 // The player may have lost their gloves
                 if (hasAnItem(player, Items.SAFETY_GLOVES_12629).exists()){
                     sendDialogue(player, "The chest is empty")
                 }
                 else{
                     if (freeSlots(player) == 0){
                         sendDialogue(player, "You do not have enough inventory space!")
                     }
                     else {
                         addItem(player, Items.SAFETY_GLOVES_12629)
                         sendItemDialogue(
                             player, Items.SAFETY_GLOVES_12629,
                             "You open the chest to find a pair of safety gloves. "
                         )
                     }
                 }

             }
             return@on true
         }
     }

     fun read(player: Player, plaque: Node){
         if (plaque !is Scenery) return
         player.interfaceManager.openChatbox(plaquesToIface[plaque.id]!!)
     }


    class PlaqueListener : InterfaceListener {

        var scene : PlaqueCutscene? = null

        override fun defineInterfaceListeners() {
            for ((index, iface) in plaquesToIface.values.withIndex()){
                onClose(iface){ player, _ ->
                    scene?.end(fade = false)
                    player.globalData.readPlaques[index] = true
                    return@onClose true
                }

                onOpen(iface) { player, component ->
                    scene = PlaqueCutscene(player, component)
                    scene?.start(hideMiniMap = false)
                    return@onOpen true
                }

                on(iface) { player, _, _, buttonID, _, _ ->
                    // If thumbs up is clicked
                    if (buttonID == 2){
                        scene?.incrementStage()
                        player.interfaceManager.closeChatbox()
                    }
                    return@on true
                }
            }
        }

        class PlaqueCutscene(player: Player, val component: Component): Cutscene(player) {

            // Since the component does not know the door's location
            // there needs to be some translation from player position
            // to the door location. This is component -> rotation (dx, dy)
            private val rotationMapping = mapOf(
                701 to listOf(-1, 0),
                703 to listOf(-1, 0),
                711 to listOf(-1, 0),
                695 to listOf(0, 1),
                312 to listOf(1, 0),
                706 to listOf(1, 0),
                698 to listOf(1, 0),
            )

            override fun setup() {
                setExit(player.location)
            }

            override fun runStage(stage: Int) {
                when (stage){
                    0 -> {
                        // Go to head height
                        moveCamera(player.location.localX, player.location.localY, 245, speed = 50)
                        // Spin in the right direction
                        rotateCamera(player.location.localX + rotationMapping[component.id]!![0],
                            player.location.localY + rotationMapping[component.id]!![1],
                            245, speed = 50)
                    }
                    1 -> {
                        resetCamera()
                    }
                }
            }

        }

    }

 }
