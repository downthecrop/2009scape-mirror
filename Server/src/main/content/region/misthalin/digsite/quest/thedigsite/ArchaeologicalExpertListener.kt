package content.region.misthalin.digsite.quest.thedigsite

import content.region.misthalin.digsite.dialogue.ArchaeologistcalExpertUsedOnDialogueFile
import core.api.*
import core.game.dialogue.DialogueBuilder
import core.game.dialogue.DialogueBuilderFile
import core.game.dialogue.DialogueFile
import core.game.dialogue.FacialExpression
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import org.rs09.consts.Items
import org.rs09.consts.NPCs

class ArchaeologicalExpertListener : InteractionListener {


    val staff = Items.ANCIENT_STAFF_4675
    val unidentifiedLiquid = Items.UNIDENTIFIED_LIQUID_702
    val nitroglycerin = Items.NITROGLYCERIN_703
    val chemicalPowder = Items.CHEMICAL_POWDER_700
    val ammoniumNitrate = Items.AMMONIUM_NITRATE_701
    val nuggets = Items.NUGGETS_680
    val needle = Items.NEEDLE_1733
    val rottenApple = Items.ROTTEN_APPLE_1984
    val brokenGlass = Items.BROKEN_GLASS_1469
    val brokenArrow = Items.BROKEN_ARROW_687
    val panningTray = Items.PANNING_TRAY_677
    val bones = Items.BONES_526
    val buttons = Items.BUTTONS_688
    val crackedSample = Items.CRACKED_SAMPLE_674
    val oldTooth = Items.OLD_TOOTH_695
    val rustySword = Items.RUSTY_SWORD_686
    val brokenStaff = Items.BROKEN_STAFF_689
    val brokenArmour = Items.BROKEN_ARMOUR_698
    val damagedArmour = Items.DAMAGED_ARMOUR_697
    val ceramicRemains = Items.CERAMIC_REMAINS_694
    val beltBuckle = Items.BELT_BUCKLE_684
    val animalSkull = Items.ANIMAL_SKULL_671
    val specialCup = Items.SPECIAL_CUP_672
    val teddy = Items.TEDDY_673
    val stoneTablet = Items.STONE_TABLET_699

    val items = intArrayOf(staff, unidentifiedLiquid, nitroglycerin, chemicalPowder, ammoniumNitrate, nuggets, needle, rottenApple,
            brokenGlass, brokenArrow, panningTray, bones, buttons, crackedSample, oldTooth, rustySword, brokenStaff, brokenArmour,
            damagedArmour, ceramicRemains, beltBuckle, animalSkull, specialCup, teddy, stoneTablet)

    val archy = NPCs.ARCHAEOLOGICAL_EXPERT_619


    override fun defineListeners() {
        onUseAnyWith(IntType.NPC, NPCs.ARCHAEOLOGICAL_EXPERT_619) { player, used, with ->
            openDialogue(player, ArchaeologicalExpertListenerDialogueFile(used.id), with as NPC)
            return@onUseAnyWith false
        }
    }
}
class ArchaeologicalExpertListenerDialogueFile(val it: Int) : DialogueBuilderFile() {

    companion object {
        fun replaceAll(player: Player, originalItem: Int, newItem: Int) {
            for (a in 0..amountInInventory(player, originalItem)) {
                if (inInventory(player, originalItem)) {
                    replaceSlot(player, player.inventory.getSlot(Item(originalItem)), Item(newItem))
                }
            }
        }
    }

    override fun create(b: DialogueBuilder) {

        b.onPredicate { _ -> it == Items.UNIDENTIFIED_LIQUID_702 }
                .player(FacialExpression.THINKING, "Do you know what this is?")
                .npcl(FacialExpression.WORRIED, "Where did you get this?")
                .player("From one of the barrels at the digsite.")
                .npcl(FacialExpression.WORRIED, "This is a VERY dangerous liquid called nitroglycerin. Be careful how you handle it. Don't drop it or it will explode!")
                .endWith { _, player ->
                    replaceAll(player, Items.UNIDENTIFIED_LIQUID_702, Items.NITROGLYCERIN_703)
                }

        b.onPredicate { _ -> it == Items.NITROGLYCERIN_703 }
                .player(FacialExpression.THINKING, "Can you tell me any more about this?")
                .npcl(FacialExpression.WORRIED, "Nitroglycerin! This is a dangerous substance. This is normally mixed with other chemicals to produce a potent compound.")
                .npcl(FacialExpression.WORRIED, "Be sure not to drop it! That stuff is highly volatile...")
                .end()

        b.onPredicate { _ -> it == Items.CHEMICAL_POWDER_700 }
                .player(FacialExpression.THINKING, "Do you know what this powder is?")
                .npcl(FacialExpression.WORRIED, "Really, you do find the most unusual items. I know what this is - it's a strong chemical called ammonium nitrate. Why you want this I'll never know...")
                .endWith { _, player ->
                    replaceAll(player, Items.CHEMICAL_POWDER_700, Items.AMMONIUM_NITRATE_701)
                }

        // b.onPredicate { _ -> it == Items.AMMONIUM_NITRATE_701 } From youtu.be/mKTBPLdxRSY at 9:57, once ammonium nitrate is identified, it becomes not of any archaeological significance.

        b.onPredicate { _ -> it == Items.ANCIENT_TALISMAN_681 }
                .npcl(FacialExpression.FRIENDLY, "Unusual... This object doesn't appear right...")
                .npcl(FacialExpression.FRIENDLY, "Hmmm...")
                .npcl(FacialExpression.FRIENDLY, "I wonder... Let me check my guide... Could it be? Surely not!")
                .npcl(FacialExpression.FRIENDLY, "From the markings on it, it seems to be a ceremonial ornament to a god named...")
                .npcl(FacialExpression.FRIENDLY, "...Zaros? I haven't heard much about him before. This is a great discovery; we know very little of the ancient gods that people worshipped.")
                .npcl(FacialExpression.FRIENDLY, "There is some strange writing embossed upon it - it says, 'Zaros will return and wreak his vengeance upon Zamorak the pretender.'")
                .npcl(FacialExpression.FRIENDLY, "I wonder what it means by that. Some silly superstition, probably.")
                .npcl(FacialExpression.FRIENDLY, "Still, I wonder what this is doing around here. I'll tell you what; as you have found this, I will allow you to use the private dig shafts.")
                .npcl(FacialExpression.FRIENDLY, "You obviously have a keen eye. Take this letter and give it to one of the workmen, and they will allow you to use them.")
                .endWith { _, player ->
                    if (removeItem(player, Items.ANCIENT_TALISMAN_681)) {
                        addItemOrDrop(player, Items.INVITATION_LETTER_696)
                    }
                    if(getQuestStage(player, TheDigSite.questName) == 6) {
                        setQuestStage(player, TheDigSite.questName, 7)
                    }
                }

        b.onPredicate { _ -> it == Items.STONE_TABLET_699 }
                .playerl(FacialExpression.FRIENDLY, "I found this in a hidden cavern beneath the site.")
                .npcl(FacialExpression.FRIENDLY, "Incredible!")
                .playerl(FacialExpression.FRIENDLY, "There is an altar down there. The place is crawling with skeletons!")
                .npcl(FacialExpression.FRIENDLY, "Yuck! This is an amazing discovery! All this while we were convinced that no other race had lived here.")
                .npcl("It seems the followers of Saradomin have tried to cover up the evidence of this Zaros altar. This whole city must have been built over it!")
                .npcl("Thanks for your help; your sharp eyes have spotted what many have missed. Here, take this gold as your reward.")
                .item(Items.GOLD_BAR_2357, "The expert gives you two gold bars as payment.")
                .endWith { _, player ->
                    if (removeItem(player, Items.STONE_TABLET_699)) {
                        if(getQuestStage(player, TheDigSite.questName) == 11) {
                            finishQuest(player, TheDigSite.questName)
                        }
                    }
                }

        // Fallback when item isn't recognizable.
        b.onPredicate { _ -> true }
                .npcl(FacialExpression.FRIENDLY, "I don't think that has any archaeological significance.")
                .end()
    }
}