package content.region.asgarnia.falador.quest.recruitmentdrive

import core.api.*
import core.game.container.impl.EquipmentContainer
import core.game.dialogue.DialogueBuilder
import core.game.dialogue.DialogueBuilderFile
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.interaction.InteractionListener
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.map.zone.ZoneBorders
import core.plugin.Initializable
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import org.rs09.consts.Scenery

@Initializable
class SirSpishyusDialogue (player: Player? = null) : DialoguePlugin(player) {
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player, SirSpishyusDialogueFile(), npc)
        return true
    }
    override fun newInstance(player: Player): DialoguePlugin {
        return SirSpishyusDialogue(player)
    }
    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SIR_SPISHYUS_2282)
    }
}

class SirSpishyusDialogueFile(private val dialogueNum: Int = 0) : DialogueBuilderFile() {
    override fun create(b: DialogueBuilder) {
        b.onPredicate { player -> getAttribute(player, RecruitmentDrive.attributeStagePassFailState, 0) == 1 }
                .npc(FacialExpression.FRIENDLY, "Excellent work, @name.", "Please step through the portal to meet your next", "challenge.")
                .end()

        b.onPredicate { player -> dialogueNum == 2 || getAttribute(player, RecruitmentDrive.attributeStagePassFailState, 0) == -1 }
                .betweenStage { _, player, _, _ ->
                    setAttribute(player, RecruitmentDrive.attributeStagePassFailState, -1)
                }
                .npc(FacialExpression.SAD, "No... I am very sorry.", "Apparently you are not up to the challenge.", "I will return you where you came from, better luck in the", "future.")
                .endWith { _, player ->
                    removeAttribute(player, SirTinleyDialogueFile.attributeDoNotMove)
                    removeAttribute(player, RecruitmentDrive.attributeStagePassFailState)
                    RecruitmentDriveListeners.FailTestCutscene(player).start()
                }
        b.onPredicate { _ -> true }
                .npcl(FacialExpression.FRIENDLY, "Ah, welcome @name.")
                .playerl(FacialExpression.FRIENDLY, "Hello there." + " What am I supposed to be doing in this room?")
                .npcl(FacialExpression.FRIENDLY, "Well, your task is to take this fox, this chicken and this bag of grain across that bridge there to the other side of the room.")
                .npcl(FacialExpression.FRIENDLY, "When you have done that, your task is complete.")
                .playerl(FacialExpression.FRIENDLY, "Is that it?")
                .npcl(FacialExpression.FRIENDLY, "Well, it is not quite as simple as that may sound.")
                .npcl(FacialExpression.FRIENDLY, "Firstly, you may only carry one of the objects across the room at a time, for the bridge is old and fragile.")
                .npcl(FacialExpression.FRIENDLY, "Secondly, the fox wants to eat the chicken, and the chicken wants to eat the grain. Should you ever leave the fox unattended with the chicken, or the grain unattended with the chicken, then")
                .npcl(FacialExpression.FRIENDLY, "one of them will be eaten, and you will be unable to complete the test.")
                .playerl(FacialExpression.FRIENDLY, "Okay, I'll see what I can do.")
                .end()
    }
}
class SirSpishyusRoomListeners : InteractionListener {
    companion object {
        const val foxFromVarbit = 680
        const val foxToVarbit = 681
        const val chickenFromVarbit = 682
        const val chickenToVarbit = 683
        const val grainFromVarbit = 684
        const val grainToVarbit = 685

        val fromZoneBorder = ZoneBorders(2479, 4967, 2490, 4977)
        val toZoneBorder = ZoneBorders(2471, 4967, 2478, 4977)

        fun countEquipmentItems(player: Player): Int {
            var count = 0
            if(inEquipment(player, Items.GRAIN_5607)) { count++ }
            if(inEquipment(player, Items.FOX_5608)) { count++ }
            if(inEquipment(player, Items.CHICKEN_5609)) { count++ }
            return count
        }

        fun checkFinished(player: Player) {
            if (getVarbit(player, foxToVarbit) == 1 && getVarbit(player, chickenToVarbit) == 1 && getVarbit(player, grainToVarbit) == 1) {
                sendMessage(player, "Congratulations! You have solved this room's puzzle!")
                setAttribute(player, RecruitmentDrive.attributeStagePassFailState, 1)
            }
        }

        fun checkFail(player: Player): Boolean {
            return ((getVarbit(player, foxFromVarbit) == 0 && getVarbit(player, chickenFromVarbit) == 0 && getVarbit(player, grainFromVarbit) == 1) ||
                    (getVarbit(player, foxFromVarbit) == 1 && getVarbit(player, chickenFromVarbit) == 0 && getVarbit(player, grainFromVarbit) == 0) ||
                    (getVarbit(player, foxToVarbit) == 1 && getVarbit(player, chickenToVarbit) == 1 && getVarbit(player, grainToVarbit) == 0) ||
                    (getVarbit(player, foxToVarbit) == 0 && getVarbit(player, chickenToVarbit) == 1 && getVarbit(player, grainToVarbit) == 1))
        }

        fun resetStage(player: Player) {
            setVarbit(player, foxFromVarbit, 0)
            setVarbit(player, chickenFromVarbit, 0)
            setVarbit(player, grainFromVarbit, 0)
            setVarbit(player, foxToVarbit, 0)
            setVarbit(player, chickenToVarbit, 0)
            setVarbit(player, grainToVarbit, 0)
            removeItem(player, Items.GRAIN_5607, Container.EQUIPMENT)
            removeItem(player, Items.FOX_5608, Container.EQUIPMENT)
            removeItem(player, Items.CHICKEN_5609, Container.EQUIPMENT)
        }
    }

    override fun defineListeners() {
        on(Scenery.PRECARIOUS_BRIDGE_7286, SCENERY, "cross") { player, node ->
            if (countEquipmentItems(player) > 1) {
                sendDialogue(player, "I really don't think I should be carrying more than 5Kg across that rickety bridge...")
            } else if (checkFail(player)) {
                openDialogue(player, SirTinleyDialogueFile(2), NPC(NPCs.SIR_SPISHYUS_2282)) // Fail
            } else {
                lock(player, 5)
                sendMessage(player, "You carefully walk across the rickety bridge...")
                player.walkingQueue.reset()
                player.walkingQueue.addPath(2476, 4972)
            }
            return@on true
        }

        on(Scenery.PRECARIOUS_BRIDGE_7287, SCENERY, "cross") { player, node ->
            if (countEquipmentItems(player) > 1) {
                sendDialogue(player, "I really don't think I should be carrying more than 5Kg across that rickety bridge...")
            } else if (checkFail(player)) {
                openDialogue(player, SirTinleyDialogueFile(2), NPC(NPCs.SIR_SPISHYUS_2282)) // Fail
            } else {
                lock(player, 5)
                sendMessage(player, "You carefully walk across the rickety bridge...")
                player.walkingQueue.reset()
                player.walkingQueue.addPath(2484, 4972)
            }
            return@on true
        }

        on(Scenery.GRAIN_7284, SCENERY, "pick-up") { player, _ ->
            if (getAttribute(player, RecruitmentDrive.attributeStagePassFailState, 0) == 0) {
                if (fromZoneBorder.insideBorder(player)) {
                    replaceSlot(player, EquipmentSlot.CAPE.ordinal, Item(Items.GRAIN_5607), null, Container.EQUIPMENT)
                    setVarbit(player, grainFromVarbit, 1)
                }
                if (toZoneBorder.insideBorder(player)) {
                    replaceSlot(player, EquipmentSlot.CAPE.ordinal, Item(Items.GRAIN_5607), null, Container.EQUIPMENT)
                    setVarbit(player, grainToVarbit, 0)
                }
            }
            return@on true
        }
        onUnequip(Items.GRAIN_5607) { player, _ ->
            if (fromZoneBorder.insideBorder(player)) {
                removeItem(player, Items.GRAIN_5607, Container.EQUIPMENT)
                setVarbit(player, grainFromVarbit, 0)
            }
            if (toZoneBorder.insideBorder(player)) {
                removeItem(player, Items.GRAIN_5607, Container.EQUIPMENT)
                setVarbit(player, grainToVarbit, 1)
                checkFinished(player)
            }
            return@onUnequip true
        }


        on(Scenery.FOX_7277, SCENERY, "pick-up") { player, _ ->
            if (getAttribute(player, RecruitmentDrive.attributeStagePassFailState, 0) == 0) {
                if (fromZoneBorder.insideBorder(player)) {
                    replaceSlot(player, EquipmentSlot.WEAPON.ordinal, Item(Items.FOX_5608), null, Container.EQUIPMENT)
                    setVarbit(player, foxFromVarbit, 1)
                }
                if (toZoneBorder.insideBorder(player)) {
                    replaceSlot(player, EquipmentSlot.WEAPON.ordinal, Item(Items.FOX_5608), null, Container.EQUIPMENT)
                    setVarbit(player, foxToVarbit, 0)
                }
            }
            return@on true
        }
        onUnequip(Items.FOX_5608) { player, _ ->
            if (fromZoneBorder.insideBorder(player)) {
                removeItem(player, Items.FOX_5608, Container.EQUIPMENT)
                setVarbit(player, foxFromVarbit, 0)
            }
            if (toZoneBorder.insideBorder(player)) {
                removeItem(player, Items.FOX_5608, Container.EQUIPMENT)
                setVarbit(player, foxToVarbit, 1)
                checkFinished(player)
            }
            return@onUnequip true
        }


        on(Scenery.CHICKEN_7281, SCENERY, "pick-up") { player, _ ->
            if (getAttribute(player, RecruitmentDrive.attributeStagePassFailState, 0) == 0) {
                if (fromZoneBorder.insideBorder(player)) {
                    replaceSlot(player, EquipmentSlot.SHIELD.ordinal, Item(Items.CHICKEN_5609), null, Container.EQUIPMENT)
                    setVarbit(player, chickenFromVarbit, 1)
                }
                if (toZoneBorder.insideBorder(player)) {
                    replaceSlot(player, EquipmentSlot.SHIELD.ordinal, Item(Items.CHICKEN_5609), null, Container.EQUIPMENT)
                    setVarbit(player, chickenToVarbit, 0)
                }
            }
            return@on true
        }
        onUnequip(Items.CHICKEN_5609) { player, _ ->
            if (fromZoneBorder.insideBorder(player)) {
                removeItem(player, Items.CHICKEN_5609, Container.EQUIPMENT)
                setVarbit(player, chickenFromVarbit, 0)
            }
            if (toZoneBorder.insideBorder(player)) {
                removeItem(player, Items.CHICKEN_5609, Container.EQUIPMENT)
                setVarbit(player, chickenToVarbit, 1)
                checkFinished(player)
            }
            return@onUnequip true
        }

    }

}