package content.region.asgarnia.mudskipperpoint

import content.global.skill.slayer.MogreNPC
import content.region.asgarnia.mudskipperpoint.dialogue.SkippyBootDialogue
import content.region.asgarnia.mudskipperpoint.dialogue.SkippyBucketDialogue
import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.impl.Projectile
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.plugin.Initializable
import core.tools.RandomFunction
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import org.rs09.consts.Scenery
import org.rs09.consts.Vars

@Initializable
class MudSkipperPointListeners : InteractionListener {

    companion object {
        private val MESSAGES = arrayOf(
            "Da boom-boom kill all da fishies!",
            "I smack you good!",
            "Smash stupid human!",
            "Tasty human!",
            "Human hit me on the head!",
            "I get you!",
            "Human scare all da fishies!"
        )

        private val FISHING_SPOTS = intArrayOf(
            Scenery.OMINOUS_FISHING_SPOT_10087,
            Scenery.OMINOUS_FISHING_SPOT_10088,
            Scenery.OMINOUS_FISHING_SPOT_10089
        )
    }

    private fun handleExplosives(player: Player, explosives: Item, target: Location): Boolean{
        // Check if the player has unlocked the ability to summon Mogres
        if (getVarbit(player, Vars.VARBIT_MINI_QUEST_MOGRE) != SkippyBucketDialogue.DONE_QUEST){
            sendDialogueLines(player, "Sinister as that fishing spot is, why would I want to", "explode it?")
            return false
        }
        val distance = player.location.getDistance(target)
        if (distance < 5){
            //too close
            sendPlayerDialogue(player, "If this thing explodes, I think I should stand a liiiiitle further away.")
            return false
        }
        if (distance > 14){
            // too far
            sendPlayerDialogue(player, "I can't throw that far.")
            return false
        }
        removeItem(player, explosives.id)
        animate(player, Animation(385))  // Throw the vial
        sendMessage(player, "You hurl the shuddering vial into the water...")
        val projectile = Projectile.create(player, null, 49, 30, 20, 30, Projectile.getSpeed(player, target.location))
        projectile.endLocation = target.location
        projectile.send()

        val delay =  (2+ distance * 0.5).toInt()
        queueScript(player, delay){
            val mogre = MogreNPC()
            val xOffset = (player.location.x - target.location.x) % 2
            val yOffset = (player.location.y - target.location.y) % 2
            mogre.location = Location.create(target.location.x + xOffset, target.location.y + yOffset)
            mogre.init()
            mogre.moveStep()
            mogre.isRespawn = false
            mogre.attack(player)
            registerHintIcon(player, mogre)
            mogre.sendChat(MESSAGES[RandomFunction.random(MESSAGES.size)])
            if (explosives.id == Items.SUPER_FISHING_EXPLOSIVE_12633){
                impact(mogre, 15)
            }
            mogre.graphics(Graphics(68))
            sendMessage(player, "...and a Mogre appears!")
            return@queueScript stopExecuting(player)
        }
        return true
    }

    private fun soberSkippy(player: Player): Boolean {
        if (getVarbit(player, Vars.VARBIT_MINI_QUEST_MOGRE) > SkippyBucketDialogue.DRUNK){
            sendDialogue(player, "I think he's sober enough. And I don't want to use another bucket of water.")
            return false
        }
        else{
            if (hasAnItem(player, Items.BUCKET_OF_WATER_1929).exists()) {
                openDialogue(player, SkippyBucketDialogue())
                return true
            }
            else{
                sendDialogue(player, "You know, I could shock him out of it if I could find some cold water...")
                return false
            }
        }
    }

    override fun defineListeners() {

        onUseWith(IntType.SCENERY, Items.FISHING_EXPLOSIVE_6664, *FISHING_SPOTS) { player, used, with ->
            handleExplosives(player, used.asItem(), with.location)
        }

        onUseWith(IntType.SCENERY, Items.SUPER_FISHING_EXPLOSIVE_12633, *FISHING_SPOTS) { player, used, with ->
            handleExplosives(player, used.asItem(), with.location)
        }

        on(FISHING_SPOTS, IntType.SCENERY, "Lure", "Bait") { player, _ ->
            sendMessage(player, "Something seems to have scared all the fishes away...")
            return@on true
        }

        on(Scenery.SIGNPOST_10090, IntType.SCENERY, "read") { player, _ ->
            setInterfaceText(player, "Mudskipper Point.", 220, 2)
            setInterfaceText(player, "WARNING! BEWARE OF THE MUDSKIPPERS!", 220, 4)
            openInterface(player, 220)
            return@on true
        }

        // For some reason the 2795 wrapper does not work for sober-up
        on(intArrayOf(NPCs.SKIPPY_2796, NPCs.SKIPPY_2797, NPCs.SKIPPY_2798, NPCs.SKIPPY_2799),
            IntType.NPC, "sober-up") { player, _ ->
            return@on soberSkippy(player)
        }

        onUseWith(IntType.NPC, Items.BUCKET_OF_WATER_1929, NPCs.SKIPPY_2795) { player, _, _ ->
            return@onUseWith soberSkippy(player)
        }

        onUseWith(IntType.NPC, Items.FORLORN_BOOT_6663, NPCs.SKIPPY_2795){ player, _, _ ->
            openDialogue(player, SkippyBootDialogue())
            return@onUseWith true
        }
    }

    override fun defineDestinationOverrides() {

        // Don't run towards the fishing spots when trying to lure or bait or throw explosives
        setDest(IntType.SCENERY, FISHING_SPOTS, "Lure", "Bait", "use"){ entity, _ ->
            return@setDest entity.location
        }
    }
}
