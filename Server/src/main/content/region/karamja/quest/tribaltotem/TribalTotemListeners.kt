package content.region.karamja.quest.tribaltotem

import core.api.*
import core.game.node.entity.skill.Skills
import core.game.node.scenery.SceneryBuilder
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import org.rs09.consts.Items
import core.game.interaction.InteractionListener
import core.game.interaction.IntType
import content.data.Quests

class TribalTotemListeners : InteractionListener {

    val frontDoor = 2706
    val wizCrate = 2707
    val realCrate = 2708
    val label = Items.ADDRESS_LABEL_1858
    val lockedDoor = 2705
    val stairs = 2711
    val closedChest = 2709
    val openChest = 2710

    override fun defineListeners() {
        on(frontDoor, IntType.SCENERY, "Open"){ player, door ->
            if(player.questRepository.getStage(Quests.TRIBAL_TOTEM) >= 35){
                core.game.global.action.DoorActionHandler.handleAutowalkDoor(player,door.asScenery())
            }
            else {
                sendMessage(player,"The door is locked shut.")
            }
            return@on true
        }

        on(realCrate, IntType.SCENERY, "Investigate"){ player, node ->
            if(player.questRepository.getStage(Quests.TRIBAL_TOTEM) in 1..19 && !player.inventory.containsAtLeastOneItem(Items.ADDRESS_LABEL_1858)){
                sendDialogue(player,"There is a label on this crate. It says; To Lord Handelmort, Handelmort Mansion Ardogune.You carefully peel it off and take it.")
                addItemOrDrop(player,Items.ADDRESS_LABEL_1858,1)
            }
            else if(player.questRepository.getStage(Quests.TRIBAL_TOTEM) in 1..19 && player.inventory.containsAtLeastOneItem(Items.ADDRESS_LABEL_1858)){
                sendDialogue(player,"There was a label on this crate, but it's gone now since you took it!")
            }
            return@on true
        }

        on(wizCrate, IntType.SCENERY, "Investigate"){ player, node ->
            sendDialogue(player,"There is a label on this crate: Senior Patents Clerk, Chamber of Invention, The Wizards' Tower, Misthalin. The crate is securely fastened shut and ready for delivery.")
            return@on true
        }

        onUseWith(IntType.SCENERY,label,wizCrate){ player, used, with ->
            sendDialogue(player,"You carefully place the delivery address label over the existing label, covering it completely.")
            removeItem(player,label)
            player.questRepository.getQuest(Quests.TRIBAL_TOTEM).setStage(player,20)
            return@onUseWith true
        }

        on(lockedDoor, IntType.SCENERY, "Open"){ player, node ->
            if(player.getAttribute("TT:DoorUnlocked",false) == true){
                core.game.global.action.DoorActionHandler.handleAutowalkDoor(player,node.asScenery())
            }else{
                openInterface(player,369)
            }
            return@on true
        }

        on(stairs, IntType.SCENERY, "Climb-up"){ player, node ->
            if(player.getAttribute("TT:StairsChecked",false)){
                core.game.global.action.ClimbActionHandler.climb(player, Animation(828), Location.create(2629, 3324, 1))
            }
            else{
                sendMessage(player,"You set off a trap and the stairs give way under you, dropping you into the sewers.")
                player.teleport(Location.create(2641, 9721, 0))
            }
            return@on true
        }

        on(stairs, IntType.SCENERY, "Investigate"){ player, node ->
            if(player.getSkills().getStaticLevel(Skills.THIEVING) >= 21){
                sendDialogue(player,"Your trained senses as a thief enable you to see that there is a trap in these stairs. You make a note of its location for future reference when using these stairs")
                player.setAttribute("/save:TT:StairsChecked",true)
            }else{
                sendDialogue(player,"You don't see anything out of place on these stairs.")
            }
            return@on true
        }

        on(closedChest, IntType.SCENERY, "Open"){ player, node ->
            SceneryBuilder.replace(node.asScenery(), node.asScenery().transform(2710))
            return@on true
        }

        on(openChest, IntType.SCENERY, "Close"){ player, node ->
            SceneryBuilder.replace(node.asScenery(), node.asScenery().transform(2709))
            return@on true
        }

        on(openChest, IntType.SCENERY, "Search"){ player, node ->
            if(!player.inventory.containsAtLeastOneItem(Items.TOTEM_1857)){
                sendDialogue(player,"Inside the chest you find the tribal totem.")
                addItemOrDrop(player,Items.TOTEM_1857)
                player.questRepository.getQuest(Quests.TRIBAL_TOTEM).setStage(player,35)
            }
            else{
                sendDialogue(player,"Inside the chest you don't find anything because you already took the totem!")
            }
            return@on true
        }
    }

}