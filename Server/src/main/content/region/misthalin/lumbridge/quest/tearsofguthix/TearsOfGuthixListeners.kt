package content.region.misthalin.lumbridge.quest.tearsofguthix

import core.api.*
import core.game.dialogue.DialogueFile
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.impl.ForceMovement
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.tools.END_DIALOGUE
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import org.rs09.consts.Scenery

class TearsOfGuthixListeners : InteractionListener {

    companion object {
        fun crossTheChasm(player: Player, with: NPC) {
            // Unless you have time to animate this, this fucking thing is waaay too complicated.
            // THIS IS JUST AESTHETICS
            // You have to do the following:
            // 1 - Get the light creature to your location.
            // 2 - Animate both you and the light creature to float up.
            // 3 - Walk both YOU AND THE LIGHT CREATURE to the other side.
            // 4 - Float both you and the light creature to the ground.
            //
            // 2046 - Magically float into the air
            // 2047 - Magically float back to the ground
            // 2048 - Keep floating in the air

            // Instead you will just get fucking thrown to that other side.
            val lightCreature = with as NPC
            sendMessage(player, "The light-creature is attracted to your beam and comes towards you...")
            LightCreatureBehavior.moveLightCreature(lightCreature, player.location)
            // Could also do player.appearance.setAnimations(Animation(913)) which is the group animation for floating.
            if (player.location.y > 9516) {
                forceMove(player, player.location, Location.create(3229, 9504, 2), 0, 400, null, 2048)
            } else {
                forceMove(player, player.location, Location.create(3228, 9527, 2), 0, 400, null, 2048)
            }
        }
    }

    override fun defineListeners() {
        // Similar to RockClimbShortcut.kt
        on(Scenery.ROCKS_6673, SCENERY, "climb") { player, _ ->
            if (player.location.x > 3240) {
                ForceMovement.run(player, player.location, Location.create(player.location).transform(-2, 0, 0), Animation(1148), Animation(1148), Direction.WEST, 13).endAnimation = Animation.RESET
            } else {
                ForceMovement.run(player, player.location, Location.create(player.location).transform(2, 0, 0), Animation(1148), Animation(1148), Direction.WEST, 13).endAnimation = Animation.RESET
            }
            return@on true
        }
        // Similar to RockClimbShortcut.kt
        on(Scenery.ROCKS_6672, SCENERY, "climb") { player, _ ->
            if (player.location.x > 3239) {
                sendMessage(player, "You could climb down here, but it is too uneven to climb up.")
            } else {
                ForceMovement.run(player, player.location, Location.create(player.location).transform(2, 0, 0), Animation(1148), Animation(1148), Direction.WEST, 13).endAnimation = Animation.RESET
            }
            return@on true
        }

        // Please note: part of this is already done in craftBullseyeLantern() except for the swapping out which is here.
        onUseWith(ITEM, Items.BULLSEYE_LANTERN_4548, Items.SAPPHIRE_1607) { player, used, with ->
            sendMessage(player, "You swap the lantern's lens for a sapphire.")
            if(removeItem(player, with) && removeItem(player, used)) {
                addItemOrDrop(player, Items.SAPPHIRE_LANTERN_4701)
                addItemOrDrop(player, Items.LANTERN_LENS_4542)
            }
            return@onUseWith true
        }
        onUseWith(ITEM, Items.SAPPHIRE_LANTERN_4701, Items.LANTERN_LENS_4542) { player, used, with ->
            sendMessage(player, "You swap the lantern's sapphire for a lens.")
            if(removeItem(player, with) && removeItem(player, used)) {
                addItemOrDrop(player, Items.BULLSEYE_LANTERN_4548)
                addItemOrDrop(player, Items.SAPPHIRE_1607)
            }
            return@onUseWith true
        }
        onUseWith(ITEM, Items.BULLSEYE_LANTERN_4549, Items.SAPPHIRE_1607) { player, used, with ->
            sendMessage(player, "The lantern is too hot to do that while it is lit.")
            return@onUseWith true
        }
        onUseWith(ITEM, Items.SAPPHIRE_LANTERN_4702, Items.LANTERN_LENS_4542) { player, used, with ->
            sendMessage(player, "The lantern is too hot to do that while it is lit.")
            return@onUseWith true
        }

        // MAGIC_STONE are set in MiningNode, but I can't change the messages without screwing up
        // When examining ore: sendMessage(player, "This rock contains a magical kind of stone.")
        // When mining: sendMessage(player, "You manage to mine some stone.")
        // If you have stone in your inventory: sendMessage(player, "You have already mined some stone. You don't need any more.")

        // Note: The construction MAGIC_STONE is MAGIC_STONE_8788 NOT MAGIC_STONE_4703 WHICH IS FOR TEARS OF GUTHIX
        onUseWith(ITEM, Items.MAGIC_STONE_4703, Items.CHISEL_1755) { player, used, with ->
            sendMessage(player, "You make a stone bowl.")
            if(removeItem(player, used)) {
                addItemOrDrop(player, Items.STONE_BOWL_4704)
            }
            return@onUseWith true
        }

        onUseWith(NPC, Items.SAPPHIRE_LANTERN_4702, NPCs.LIGHT_CREATURE_2021) { player, used, with ->
            if (hasRequirement(player, "While Guthix Sleeps")) {
                // Options when you have WGS - B6KHH7AQc2Q
                openDialogue(player, object : DialogueFile(){
                    override fun handle(componentID: Int, buttonID: Int) {
                        when(stage){
                            0 -> interpreter!!.sendOptions("Select an Option", "Across the Chasm.", "Into the Chasm.").also { stage++ }
                            1 -> when(buttonID){
                                1 -> {
                                    crossTheChasm(player, with as NPC)
                                    end()
                                }
                                2 -> {
                                    // This was old.
                                    player.lock(2)
                                    player.teleport(Location.create(2538, 5881, 0))
                                    end()
                                }
                            }
                        }
                    }
                })
            } else {
                crossTheChasm(player, with as NPC)
            }
            return@onUseWith true
        }

    }

    override fun defineDestinationOverrides() {
        setDest(IntType.NPC, intArrayOf(NPCs.LIGHT_CREATURE_2021),"use"){ player, node ->
            return@setDest player.location
        }
    }
}