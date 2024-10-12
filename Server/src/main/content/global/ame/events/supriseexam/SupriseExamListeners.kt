package content.global.ame.events.supriseexam

import core.game.component.Component
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.map.Location
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import core.game.interaction.InteractionListener
import core.game.interaction.IntType
import content.global.handlers.iface.ExperienceInterface
import core.api.MapArea
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneRestriction

class SupriseExamListeners : InteractionListener, MapArea {
    override fun defineListeners() {

        on(NPCs.MR_MORDAUT_6117, IntType.NPC, "talk-to") { player, node ->
            player.faceLocation(Location.create(1886, 5024, 0))
            val examComplete = player.getAttribute(SurpriseExamUtils.SE_KEY_CORRECT, 0) == 3
            player.dialogueInterpreter.open(MordautDialogue(examComplete), node.asNpc())
            return@on true
        }

        on(SurpriseExamUtils.SE_DOORS, IntType.SCENERY, "open"){ player, node ->
            val correctDoor = player.getAttribute(SurpriseExamUtils.SE_DOOR_KEY,-1)

            if(correctDoor == -1){
                player.dialogueInterpreter.open(SEDoorDialogue(true))
                return@on true
            }

            if(node.id == correctDoor){
                SurpriseExamUtils.cleanup(player)
                return@on true
            }

            player.dialogueInterpreter.open(SEDoorDialogue(false))
            return@on true
        }

        on(Items.BOOK_OF_KNOWLEDGE_11640, IntType.ITEM, "read") { player, _ ->
            player.setAttribute("caller") { skill: Int, p: Player ->
                if (p.inventory.remove(Item(Items.BOOK_OF_KNOWLEDGE_11640))) {
                    val level = p.skills.getStaticLevel(skill)
                    val experience = level * 15.0
                    p.skills.addExperience(skill, experience)
                }
            }
            player.interfaceManager.open(Component(ExperienceInterface.COMPONENT_ID))
            return@on true
        }

    }

    override fun defineDestinationOverrides() {
        setDest(IntType.NPC, NPCs.MR_MORDAUT_6117) { _, _ ->
            return@setDest Location.create(1886, 5025, 0)
        }
    }

    override fun defineAreaBorders(): Array<ZoneBorders> {
        return arrayOf(ZoneBorders.forRegion(7502))
    }

    override fun getRestrictions(): Array<ZoneRestriction> {
        return arrayOf(ZoneRestriction.RANDOM_EVENTS, ZoneRestriction.CANNON, ZoneRestriction.FOLLOWERS, ZoneRestriction.OFF_MAP)
    }
}
