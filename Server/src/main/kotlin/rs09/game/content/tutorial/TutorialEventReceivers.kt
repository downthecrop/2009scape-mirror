package rs09.game.content.tutorial

import api.events.*
import api.getAttribute
import api.setAttribute
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.node.entity.skill.fishing.FishingSpot
import core.game.node.entity.skill.gather.mining.MiningNode
import core.game.node.entity.skill.gather.woodcutting.WoodcuttingNode
import org.rs09.consts.Items
import org.rs09.consts.NPCs

/**
 * Event receivers for tutorial island
 * @author Ceikry
 */
object TutorialButtonReceiver : EventHook<ButtonClickedEvent>
{
    override fun process(entity: Entity, event: ButtonClickedEvent) {
        if(entity !is Player) return

        when(getAttribute(entity, "tutorial:stage", 0))
        {
            //character design interface, confirm button
            0 -> if(event.iface == 771 && event.buttonId == 362) {
                setAttribute(entity, "/save:tutorial:stage", 1)
                TutorialStage.load(entity, 1)
            }

            //click settings tab SD: 548,24 HD: 746,52
            1 -> if((event.iface == 548 && event.buttonId == 24) || (event.iface == 746 && event.buttonId == 52)) {
                setAttribute(entity, "/save:tutorial:stage", 2)
                TutorialStage.load(entity, 2)
            }

            //click inventory tab SD: 548,41 HD: 746,44
            5 -> if((event.iface == 548 && event.buttonId == 41) || (event.iface == 746 && event.buttonId == 44)) {
                setAttribute(entity, "tutorial:stage", 6)
                TutorialStage.load(entity, 6)
            }

            //Click skills tab SD: 548,39 HD: 746,42
            10 -> if((event.iface == 548 && event.buttonId == 39) || (event.iface == 746 && event.buttonId == 42)) {
                setAttribute(entity, "tutorial:stage", 11)
                TutorialStage.load(entity, 11)
            }

            //Click music tab SD: 548,26 HD: 746,54
            21 -> if((event.iface == 548 && event.buttonId == 26) || (event.iface == 746 && event.buttonId == 54)) {
                setAttribute(entity, "tutorial:stage", 22)
                TutorialStage.load(entity, 22)
            }

            //Click emote tab SD: 548,25 HD: 746,53
            23 -> if((event.iface == 548 && event.buttonId == 25) || (event.iface == 746 && event.buttonId == 53)){
                setAttribute(entity, "tutorial:stage", 24)
                TutorialStage.load(entity, 24)
            }

            //Click any emote
            24 -> if(event.iface == 464){
                setAttribute(entity, "tutorial:stage", 25)
                TutorialStage.load(entity, 25)
            }

            //click run button
            25 -> if(event.iface == 261 && event.buttonId == 3){
                setAttribute(entity, "tutorial:stage", 26)
                TutorialStage.load(entity, 26)
            }

            //Open quest journal tab SD:548,40 HD:746,43
            27 -> if((event.iface == 548 && event.buttonId == 40) || (event.iface == 746 && event.buttonId == 43)){
                setAttribute(entity, "tutorial:stage", 28)
                TutorialStage.load(entity, 28)
            }

            //Open equipment tab SD:548,42 HD:746,45
            45 -> if((event.iface == 548 && event.buttonId == 42) || (event.iface == 746 && event.buttonId == 45)){
                setAttribute(entity, "tutorial:stage", 46)
                TutorialStage.load(entity, 46)
            }

            //Open weapon interface SD:548,38 HD:746,41
            49 -> if((event.iface == 548 && event.buttonId == 38) || (event.iface == 746 && event.buttonId == 41)) {
                setAttribute(entity, "tutorial:stage", 50)
                TutorialStage.load(entity, 50)
            }

            //Open prayer interface SD:548,43 HD:746,46
            61 -> if((event.iface == 548 && event.buttonId == 43) || (event.iface == 746 && event.buttonId == 46)){
                setAttribute(entity, "tutorial:stage", 62)
                TutorialStage.load(entity, 62)
            }

            //Open friends tab SD:548,21 HD:746,49
            63 -> if((event.iface == 548 && event.buttonId == 21) || (event.iface == 746 && event.buttonId == 49)){
                setAttribute(entity, "tutorial:stage", 64)
                TutorialStage.load(entity, 64)
            }

            //Open ignore list tab SD:548,22 HD:746,50
            64 -> if((event.iface == 548 && event.buttonId == 22) || (event.iface == 746 && event.buttonId == 50)){
                setAttribute(entity, "tutorial:stage", 65)
                TutorialStage.load(entity, 65)
            }

            //Open magic tab SD:548,44 HD:746,47
            68 -> if((event.iface == 548 && event.buttonId == 44) || (event.iface == 746 && event.buttonId == 47)){
                setAttribute(entity, "tutorial:stage", 69)
                TutorialStage.load(entity, 69)
            }
        }
    }
}

object TutorialInteractionReceiver : EventHook<InteractionEvent>
{
    override fun process(entity: Entity, event: InteractionEvent) {
        if(entity !is Player) return

        when(getAttribute(entity, "tutorial:stage", 0))
        {
            //Click on tree and start chopping
            6 -> if((WoodcuttingNode.forId(event.target.id)?.identifier ?: -1) == 1.toByte())
            {
                setAttribute(entity, "tutorial:stage", 7)
                TutorialStage.load(entity, 7)
            }

            //Click on fishing spot to start fishing
            12 -> if(FishingSpot.forId(event.target.id) != null)
            {
                setAttribute(entity, "tutorial:stage", 13)
                TutorialStage.load(entity, 13)
            }

            //Prospect rock - Tin
            31 -> if(MiningNode.forId(event.target.id)?.identifier?.equals(2.toByte()) == true && event.option == "prospect"){
                setAttribute(entity, "tutorial:stage", 32)
                TutorialStage.load(entity, 32)
            }

            //Prospect rock- Copper
            33 -> if(MiningNode.forId(event.target.id)?.identifier?.equals(1.toByte()) == true && event.option == "prospect"){
                setAttribute(entity, "tutorial:stage", 34)
                TutorialStage.load(entity, 34)
            }

            //Mine rock - Tin
            35 -> if(MiningNode.forId(event.target.id)?.identifier?.equals(2.toByte()) == true && event.option == "mine"){
                setAttribute(entity, "tutorial:stage", 36)
                TutorialStage.load(entity, 36)
            }

            //Equip bronze dagger
            46 -> if(event.target.id == Items.BRONZE_DAGGER_1205 && event.option == "equip"){
                setAttribute(entity, "tutorial:stage", 47)
                TutorialStage.load(entity, 47)
            }

            //Equip sword and shield
            48 -> {
                if(event.target.id == Items.BRONZE_SWORD_1277 && event.option == "equip"){
                    setAttribute(entity, "/save:tutorial:sword", true)
                }
                else if(event.target.id == Items.WOODEN_SHIELD_1171 && event.option == "equip"){
                    setAttribute(entity, "/save:tutorial:shield", true)
                }
                if(getAttribute(entity, "tutorial:shield",false) && getAttribute(entity, "tutorial:sword",false))
                {
                    setAttribute(entity, "tutorial:stage", 49)
                    TutorialStage.load(entity, 49)
                }
            }

            //Attack rat
            51 -> if(event.target.id == NPCs.GIANT_RAT_86 && event.option == "attack") {
                setAttribute(entity, "tutorial:stage",  52)
                TutorialStage.load(entity, 52)
            }

            //Open bank
            56 -> if(event.target.name.contains("booth", true) && event.option == "use"){
                setAttribute(entity, "tutorial:stage", 57)
                TutorialStage.load(entity, 57)
            }
        }
    }
}

object TutorialResourceReceiver : EventHook<ResourceProducedEvent>
{
    override fun process(entity: Entity, event: ResourceProducedEvent) {
        if(entity !is Player) return

        when(getAttribute(entity, "tutorial:stage", 0))
        {
            //Gather some logs
            7 -> if(event.itemId == Items.LOGS_1511)
            {
                setAttribute(entity, "tutorial:stage", 8)
                TutorialStage.load(entity, 8)
            }

            //Catch some raw shrimp
            13 -> if(event.itemId == Items.RAW_SHRIMPS_317)
            {
                setAttribute(entity, "tutorial:stage", 14)
                TutorialStage.load(entity, 14)
            }

            //Cook a shrimp
            14,15 -> if(event.itemId == Items.BURNT_SHRIMP_7954)
            {
                setAttribute(entity, "tutorial:stage", 15)
                TutorialStage.load(entity, 15)
            }
            else if(event.itemId == Items.SHRIMPS_315)
            {
                setAttribute(entity, "tutorial:stage", 16)
                TutorialStage.load(entity, 16)
            }

            //Make some bread dough
            19 -> if(event.itemId == Items.BREAD_DOUGH_2307) {
                setAttribute(entity, "tutorial:stage", 20)
                TutorialStage.load(entity, 20)
            }

            //Bake some bread
            20 -> if(event.itemId == Items.BREAD_2309 || event.itemId == Items.BURNT_BREAD_2311) {
                setAttribute(entity, "tutorial:stage", 21)
                TutorialStage.load(entity, 21)
            }

            //Mine some tin ore
            36 -> if(event.itemId == Items.TIN_ORE_438){
                setAttribute(entity, "tutorial:stage", 37)
                TutorialStage.load(entity, 37)
            }

            //Mine some copper ore
            37 -> if(event.itemId == Items.COPPER_ORE_436){
                setAttribute(entity, "tutorial:stage", 38)
                TutorialStage.load(entity, 38)
            }

            //Make a bronze bar
            38 -> if(event.itemId == Items.BRONZE_BAR_2349){
                setAttribute(entity, "tutorial:stage", 40)
                TutorialStage.load(entity, 40)
            }

            //Make a bronze dagger
            42 -> if(event.itemId == Items.BRONZE_DAGGER_1205){
                setAttribute(entity, "tutorial:stage", 43)
                TutorialStage.load(entity, 43)
            }
        }
    }
}

object TutorialFireReceiver : EventHook<LitFireEvent>
{
    override fun process(entity: Entity, event: LitFireEvent) {
        if(entity !is Player) return

        when(getAttribute(entity, "tutorial:stage", 0))
        {
            9 -> {
                setAttribute(entity, "tutorial:stage", 10)
                TutorialStage.load(entity, 10)
            }
        }
    }
}

object TutorialUseWithReceiver : EventHook<UsedWithEvent>
{
    override fun process(entity: Entity, event: UsedWithEvent) {
        if(entity !is Player) return


        when(getAttribute(entity, "tutorial:stage", 0))
        {
            //Start lighting a fire
            8 -> if(event.used == Items.TINDERBOX_590 && event.with == Items.LOGS_1511) {
                setAttribute(entity, "tutorial:stage", 9)
                TutorialStage.load(entity, 9)
            }

            //Use bar on anvil
            41 -> if(event.used == Items.BRONZE_BAR_2349 && event.with == 2783) {
                setAttribute(entity, "tutorial:stage", 42)
                TutorialStage.load(entity, 42)
            }
        }
    }
}

object TutorialKillReceiver : EventHook<NPCKillEvent>
{
    override fun process(entity: Entity, event: NPCKillEvent) {
        if(entity !is Player) return

        when(getAttribute(entity, "tutorial:stage", 0))
        {
            52 -> if(event.npc.id == NPCs.GIANT_RAT_86){
                setAttribute(entity, "tutorial:stage", 53)
                TutorialStage.load(entity, 53)
            }

            54 -> if(event.npc.id == NPCs.GIANT_RAT_86){
                setAttribute(entity, "tutorial:stage", 55)
                TutorialStage.load(entity, 55)
            }

            70 -> if(event.npc.id == NPCs.CHICKEN_41){
                setAttribute(entity, "tutorial:stage", 71)
                TutorialStage.load(entity, 71)
            }
        }
    }
}