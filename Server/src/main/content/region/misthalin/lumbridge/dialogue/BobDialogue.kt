package content.region.misthalin.lumbridge.dialogue

import content.data.RepairItem
import content.global.handlers.item.equipment.BarrowsEquipment
import core.cache.def.impl.ItemDefinition
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.AchievementDiary
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.item.Item
import core.plugin.Initializable

/**
 * Represents the dialogue plugin used for the bob npc who repairs items.
 * @author 'Vexia
 * @author Damighty - Kotlin conversion
 * @version 1.0
 */
@Initializable
class BobDialogue(player: Player? = null) : DialoguePlugin(player) {

    /**
     * Represents the item id being repaired.
     */
    private var itemId = 0

    /**
     * Represents the item being repaired.
     */
    private lateinit var item: Item

    /**
     * Represents the item repairing.
     */
    private var repairItem: RepairItem? = null

    /**
     * The achievement diary.
     */
    private val level = 1

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            754 -> {
                options("Yes, please.", "No, thank you.")
                stage = 755
            }
            755 -> when (buttonId) {
                1 -> {
                    player("Yes, please.")
                    stage = 757
                }
                2 -> {
                    player("No, thank you.")
                    stage = 756
                }
            }
            756 -> end()
            757 -> {
                end()
                val repairItemDef = RepairItem.forId(itemId)

                if (repairItemDef != null) {
                    // Standard repairable items
                    if (!player.inventory.contains(995, repairItemDef.cost)) {
                        player.packetDispatch.sendMessage("You don't have enough to pay him.")
                        return true
                    }
                    if (player.inventory.remove(Item(itemId, 1))) {
                        player.inventory.remove(Item(995, repairItemDef.cost))
                        player.inventory.add(repairItemDef.product)
                        val costText = if (repairItemDef.cost > 0) "${repairItemDef.cost}gp" else "free"
                        player.packetDispatch.sendMessage("Bob fixes your ${item.name.lowercase().replace("broken", "").trim()} for $costText.")
                    }
                } else {
                    // Barrows items
                    val barrowsDef = BarrowsEquipment.getDefinition(itemId) ?: return true
                    val repairCost = BarrowsEquipment.getRepairCost(item)

                    if (!player.inventory.contains(995, repairCost)) {
                        player.packetDispatch.sendMessage("You don't have enough to pay him.")
                        return true
                    }
                    if (player.inventory.remove(Item(itemId, 1))) {
                        player.inventory.remove(Item(995, repairCost))
                        player.inventory.add(Item(barrowsDef.repairedId))
                        val costText = if (repairCost > 0) "${repairCost}gp" else "free"
                        player.packetDispatch.sendMessage("Bob fixes your ${barrowsDef.itemName.lowercase()} for $costText.")
                    }
                }
                return true
            }
            678 -> end()
            0 -> when (buttonId) {
                1 -> {
                    player("Give me a quest!")
                    stage = -5
                }
                2 -> {
                    player("Have you anything to sell?")
                    stage = 10
                }
                3 -> {
                    player("Can you repair my items for me?")
                    stage = 20
                }
                4 -> {
                    player("I'd like to talk about Achievement Diaries.")
                    stage = 30
                }
            }
            -5 -> {
                interpreter.sendDialogues(npc, FacialExpression.FURIOUS, "Get yer own!")
                stage = -4
            }
            -4 -> end()
            10 -> {
                npc("Yes! I buy and sell axes! Take your pick (or axe)!")
                stage = 11
            }
            11 -> {
                end()
                npc.openShop(player)
            }
            20 -> {
                npc("Of course I'll repair it, though the materials may cost", "you. Just hand me the item and I'll have a look.")
                stage = 21
            }
            21 -> end()
            30 -> {
                if (AchievementDiary.canClaimLevelRewards(player, DiaryType.LUMBRIDGE, level)) {
                    player("I've done all the medium tasks in my Lumbridge", "Achievement Diary.")
                    stage = 150
                } else if (AchievementDiary.canReplaceReward(player, DiaryType.LUMBRIDGE, level)) {
                    player("I've seemed to have lost my explorer's ring...")
                    stage = 160
                } else {
                    options("What is the Achievement Diary?", "What are the rewards?", "How do I claim the rewards?", "See you later.")
                    stage = 31
                }
            }
            31 -> when (buttonId) {
                1 -> {
                    player("What is the Achievement Diary?")
                    stage = 110
                }
                2 -> {
                    player("What are the rewards?")
                    stage = 120
                }
                3 -> {
                    player("How do I claim the rewards?")
                    stage = 130
                }
                4 -> {
                    player("See you later!")
                    stage = 140
                }
            }
            110 -> {
                npc("Ah, well, it's a diary that helps you keep track of", "particular achievements you've made in the world of", "Gielinor. In Lumbridge and Draynor I can help you", "discover some very useful things indeed.")
                stage++
            }
            111 -> {
                npc("Eventually with enough exploration you will be", "rewarded for your explorative efforts.")
                stage++
            }
            112 -> {
                npc("You can access your Achievement Diary by going to", "the Quest Journal. When you've opened the Quest", "Journal click on the green star icon on the top right", "hand corner. This will open the diary.")
                stage = 30
            }
            120 -> {
                npc("Ah, well there are different rewards for each", "Achievement Diary. For completing the Lumbridge and", "Draynor diary you are presented with an explorer's", "ring.")
                stage++
            }
            121 -> {
                npc("This ring will become increasingly useful with each", "section of the diary that you complete.")
                stage = 30
            }
            130 -> {
                npc("You need to complete the tasks so that they're all ticked", "off, then you can claim your reward. Most of them are", "straightforward although you might find some required", "quests to be started, if not finished.")
                stage++
            }
            131 -> {
                npc("To claim the explorer's ring speak to Explorer Jack", " in Lumbridge, Ned in Draynor Village or myself.")
                stage = 30
            }
            140 -> end()
            150 -> {
                npc("Yes I see that, you'll be wanting your", "reward then I assume?")
                stage++
            }
            151 -> {
                player("Yes please.")
                stage++
            }
            152 -> {
                AchievementDiary.flagRewarded(player, DiaryType.LUMBRIDGE, level)
                npc("This ring is a representation of the adventures you", "went on to complete your tasks.")
                stage++
            }
            153 -> {
                player("Wow, thanks!")
                stage = 30
            }
            160 -> {
                AchievementDiary.grantReplacement(player, DiaryType.LUMBRIDGE, level)
                npc("You better be more careful this time.")
                stage = -1
            }
        }
        return true
    }

    override fun newInstance(player: Player): DialoguePlugin {
        return BobDialogue(player)
    }

    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        var repair = false
        var wrong = false
        
        if (npc.id == 3797 && args.size == 1) {
            player("Can you repair my items for me?")
            stage = 20
            return true
        }
        
        if (args.size == 1) {
            options("Give me a quest!", "Have you anything to sell?", "Can you repair my items for me?", "Talk about Achievement Diaries")
            stage = 0
            return true
        }
        
        if (args.size > 1) repair = args[1] as Boolean
        if (args.size > 2) wrong = args[2] as Boolean
        if (args.size > 3) {
            repairItem = RepairItem.forId(args[3] as Int)
            itemId = args[3] as Int
        }
        if (args.size > 4) item = args[4] as Item
        
        if (repair && !wrong) {
            val cost = RepairItem.forId(itemId)?.cost ?: BarrowsEquipment.getRepairCost(item)
            val costText = if (cost > 0) "${cost}gp" else "free"
            npc("Quite badly damaged, but easy to repair. Would you", "like me to repair it for $costText?")
            stage = 754
            return true
        }
        
        if (repair && wrong) {
            npc("Sorry friend, but I can't do anything with that.")
            stage = 678
            return true
        }
        
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(519, 3797)
    }
}