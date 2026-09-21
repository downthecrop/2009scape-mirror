package content.region.misthalin.draynor.handlers

import core.api.*
import core.game.dialogue.ChatAnim
import core.game.dialogue.DialogueLabeller
import core.game.dialogue.DialogueOption
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneRestriction
import org.rs09.consts.Components
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import org.rs09.consts.Scenery

/**
 * Describes the Killerwatt Plane and handles entry and egress.
 * @author Bishop
 */

object KillerwattPlane {

    const val ATTRIBUTE_WARNING        = "/save:killerwatt-warning"

    const val RIFT_TO_KILLERWATT_PLANE = Scenery.INTERDIMENSIONAL_RIFT_11354
    const val RIFT_TO_DRAYNOR_MANOR    = Scenery.PORTAL_HOME_11356

    private val planeEntrance          = Location(2677, 5215, 2)
    private val planeExit              = Location(3110, 3363, 2)

    fun enterPlane(player: Player) {
        teleport(player, planeEntrance)
        queueScript(player, 1) {
            face(player, planeEntrance.transform(-1, 0, 0))
            return@queueScript stopExecuting(player)
        }
    }

    fun leavePlane(player: Player) {
        teleport(player, planeExit)
        queueScript(player, 1) {
            face(player, planeExit.transform(0, 1, 0))
            return@queueScript stopExecuting(player)
        }
    }

}

class KillerwattPlaneArea : MapArea {

    override fun defineAreaBorders(): Array<ZoneBorders> {
        return arrayOf(ZoneBorders(2624, 5184, 2687, 5247))
    }

    override fun areaEnter(entity: Entity) {
        if (entity is Player) {
            openOverlay(entity, Components.LIGHTNING_FLASH_180)
        }
    }

    override fun areaLeave(entity: Entity, logout: Boolean) {
        if (entity is Player) {
            closeOverlay(entity)
        }
    }

    override fun getRestrictions(): Array<ZoneRestriction> {
        return arrayOf(ZoneRestriction.CANNON)
    }

}

class KillerwattPlaneListeners : InteractionListener {

    override fun defineListeners() {
        on(KillerwattPlane.RIFT_TO_KILLERWATT_PLANE, IntType.SCENERY, "enter") { player, _ ->
            val npc = findLocalNPC(player, NPCs.PROFESSOR_ODDENSTEIN_286) ?: findNPC(NPCs.PROFESSOR_ODDENSTEIN_286)!!
            if (!getAttribute(player, KillerwattPlane.ATTRIBUTE_WARNING, false)) {
                face(player, npc)
                DialogueLabeller.open(player, KillerwattPlaneDialogueFile(), npc)
            } else {
                KillerwattPlane.enterPlane(player)
            }
            return@on true
        }

        on(KillerwattPlane.RIFT_TO_DRAYNOR_MANOR, IntType.SCENERY, "enter") { player, _ ->
            KillerwattPlane.leavePlane(player)
            return@on true
        }
    }

}

class KillerwattPlaneDialogueFile : DialogueLabeller() {

    override fun addConversation() {
        npc(ChatAnim.THINKING,
            "${player!!.username} before you go through there I must warn",
            "you that there are flashing lights and strobe effects on",
            "the other side. If you are an epilepsy sufferer you",
            "must NOT enter! Do you still want to go through?"
        )
        options(
            DialogueOption("yes", "Yes I still want to go in."),
            DialogueOption("yes_and", "Yes I want to go in and don't show me this message again."),
            DialogueOption("no", "No, I dont "/*sic*/+"want to go in."),
        )

        label("yes")
            npc("All right. Good luck then.")
            exec { player, _ ->
                if (inEquipment(player, Items.INSULATED_BOOTS_7159)) {
                    KillerwattPlane.enterPlane(player)
                } else {
                    loadLabel(player, "boots")
                }
                return@exec
            }

        label("yes_and")
        exec { player, _ ->
            setAttribute(player, KillerwattPlane.ATTRIBUTE_WARNING, true)
            loadLabel(player, "yes")
            return@exec
        }

        label("no")
            npc("That's a good decision, if you ask me.")

        label("boots")
            npc("Errr, just before you go through there...")
            player("What's the problem?")
            npc("That portal opens into a plane populated with some very shocking creatures. You should wear some kind of insulated armour before going there.")
            options(
                DialogueOption("boots_where", "Where can I get insulated armour from?"),
                DialogueOption("end", "Thanks, I think I'll stay here for a while then."),
                DialogueOption("brave", "Thanks for the warning, but I'm not scared of any monster."),
            )

        label("boots_where")
            npc("Well there were some pretty tough people here last week. Said they were Slayer Masters. They were planning on making some protective boots. You should speak to one of them.")
            options(
                DialogueOption("boots_acknowledged", "Thanks, I'll do that."),
                DialogueOption("boots_spurned", "I don't want to run around after Slayer Masters, I'm going through."),
            )

        label("brave")
            npc("Ok. Just don't say I didn't warn you"/*sic*/)
            exec { player, _ ->
                KillerwattPlane.enterPlane(player)
                return@exec
            }

        label("boots_acknowledged")
            npc("No problem. See you later.")

        label("boots_spurned")
            npc("Fair enough, just don't say I didn't warn you"/*sic*/)
            exec { player, _ ->
                KillerwattPlane.enterPlane(player)
                return@exec
            }

        label("end")
    }

}