package content.region.kandarin.ardougne.westardougne.handlers

import content.data.Quests
import content.region.kandarin.ardougne.quest.plaguecity.PlagueCity
// import core.api.hasAnItem
import core.api.isQuestComplete
import core.api.openDialogue
import core.api.teleport
import core.game.dialogue.DialogueFile
import core.game.dialogue.FacialExpression
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.npc.NPC
import core.game.world.map.Location
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs09.consts.NPCs
// import org.rs09.consts.Items
import org.rs09.consts.Scenery

class MournerHQDoors : InteractionListener {

    override fun defineListeners() {
        class MournerHQDialogue : DialogueFile(){
            override fun handle(componentID: Int, buttonID: Int) {
                npc = NPC(NPCs.MOURNER_347)
                // todo check only the mourner gear is equipped
                when (stage){
                    START_DIALOGUE -> npcl(FacialExpression.ANNOYED, "Who are you? Go away!").also { stage = END_DIALOGUE }
                    // Wearing extra gear
                    /*
                        Mourner: You should know better than to wear non-regulation gear.
                        Player: Sorry, I'm new around here.
                        Mourner: Well, you know the drill - lose the gear, I will let it pass this time.
                     */
                }

            }
        }

        // Front door
        on(Scenery.DOOR_2036, IntType.SCENERY, "open"){ player, node->
            //todo after Mourning's End I is implemented make this check for wearing mourner gear
            if(isQuestComplete(player, Quests.PLAGUE_CITY)){
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            }
            else{
                openDialogue(player, MournerHQDialogue())
            }
            return@on true
        }

        on(Scenery.TRAPDOOR_8783, IntType.SCENERY, "open"){ player, _->
            // https://youtu.be/P-ns2kyvIGs?si=_DfI-V8KCyNoRtss&t=560
            //todo after Mourning's End II is implemented make this check for a New Key 6104
            // if(hasAnItem(player, Items.NEW_KEY_6104).exists()){
                teleport(player, Location.create(2044,4649, 0))
            //}
            //else{
            //    sendMessage(player, "The trapdoor appears locked")
            //}
            return@on true
        }
    }
}