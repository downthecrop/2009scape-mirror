package rs09.game.node.entity.skill.fletching

import core.game.node.entity.skill.Skills
import core.game.node.entity.skill.fletching.Fletching
import core.game.node.entity.skill.fletching.items.arrow.ArrowHeadPulse
import core.game.node.entity.skill.fletching.items.arrow.HeadlessArrowPulse
import core.game.node.entity.skill.fletching.items.bow.StringPulse
import core.game.node.entity.skill.fletching.items.crossbow.LimbPulse
import core.game.node.item.Item
import core.net.packet.PacketRepository
import core.net.packet.context.ChildPositionContext
import core.net.packet.out.RepositionChild
import org.rs09.consts.Components
import org.rs09.consts.Items
import org.rs09.consts.Items.BLUE_FEATHER_10089
import org.rs09.consts.Items.FEATHER_314
import org.rs09.consts.Items.ORANGE_FEATHER_10091
import org.rs09.consts.Items.RED_FEATHER_10088
import org.rs09.consts.Items.STRIPY_FEATHER_10087
import org.rs09.consts.Items.YELLOW_FEATHER_10090
import rs09.game.content.dialogue.SkillDialogueHandler
import rs09.game.interaction.InteractionListener

class FletchingListeners : InteractionListener() {

    val LIMBIDs = Fletching.Limb.values().map(Fletching.Limb::limb).toIntArray()
    val STOCKIDs = Fletching.Limb.values().map(Fletching.Limb::stock).toIntArray()
    val MITHRIL_BOLT = Items.MITHRIL_BOLTS_9142
    val MITH_GRAPPLE_TIP = Items.MITH_GRAPPLE_TIP_9416
    val ROPE = Items.ROPE_954
    val MITH_GRAPPLE = Items.MITH_GRAPPLE_9418
    val ROPE_GRAPPLE = Items.MITH_GRAPPLE_9419
    val ARROW_SHAFT = Items.ARROW_SHAFT_52
    val FLETCHED_SHAFT = Items.HEADLESS_ARROW_53
    val UNFINISHED_ARROWS = Fletching.ArrowHeads.values().map(Fletching.ArrowHeads::unfinished).toIntArray()
    val FEATHERS = intArrayOf(FEATHER_314,STRIPY_FEATHER_10087,RED_FEATHER_10088,BLUE_FEATHER_10089,YELLOW_FEATHER_10090,ORANGE_FEATHER_10091)
    val UNSTRUNG_BOWS = Fletching.String.values().map(Fletching.String::unfinished).toIntArray()
    val STRINGS = intArrayOf(Items.BOW_STRING_1777,Items.CROSSBOW_STRING_9438)

    override fun defineListeners() {

        onUseWith(ITEM,STRINGS,*UNSTRUNG_BOWS){player,string,bow ->
            val enum = Fletching.stringMap[bow.id] ?: return@onUseWith false
            if(enum.string != string.id){
                player.sendMessage("That's not the right kind of string for this.")
                return@onUseWith true
            }
            val handler: SkillDialogueHandler =
                object : SkillDialogueHandler(player, SkillDialogue.ONE_OPTION, Item(enum.product)) {
                    override fun create(amount: Int, index: Int) {
                        player.pulseManager.run(StringPulse(player, string.asItem(), enum, amount))
                    }

                    override fun getAll(index: Int): Int {
                        return player.inventory.getAmount(string.asItem())
                    }
                }
            handler.open()
            PacketRepository.send(RepositionChild::class.java, ChildPositionContext(player, Components.SKILL_MULTI1_309, 2, 215, 10))
            return@onUseWith true
        }

        onUseWith(ITEM,ARROW_SHAFT,*FEATHERS){player,shaft,feather ->
            val handler: SkillDialogueHandler =
                object : SkillDialogueHandler(player, SkillDialogue.ONE_OPTION, Item(FLETCHED_SHAFT)) {
                    override fun create(amount: Int, index: Int) {
                        player.pulseManager.run(HeadlessArrowPulse(player, shaft.asItem(), Item(feather.id), amount))
                    }

                    override fun getAll(index: Int): Int {
                        return player.inventory.getAmount(FLETCHED_SHAFT)
                    }
                }
            handler.open()
            PacketRepository.send(RepositionChild::class.java, ChildPositionContext(player, Components.SKILL_MULTI1_309, 2, 210, 10))
            return@onUseWith true
        }

        onUseWith(ITEM,FLETCHED_SHAFT,*UNFINISHED_ARROWS){player,shaft,unfinished ->
            val head = Fletching.arrowHeadMap[unfinished.id] ?: return@onUseWith false
            val handler: SkillDialogueHandler =
                object : SkillDialogueHandler(player, SkillDialogue.ONE_OPTION, head.getFinished()) {
                    override fun create(amount: Int, index: Int) {
                        player.pulseManager.run(ArrowHeadPulse(player, shaft.asItem(), head, amount))
                    }

                    override fun getAll(index: Int): Int {
                        return player.inventory.getAmount(head.getUnfinished())
                    }
                }
            handler.open()
            PacketRepository.send(RepositionChild::class.java, ChildPositionContext(player, Components.SKILL_MULTI1_309, 2, 210, 10))
            return@onUseWith true
        }

        onUseWith(ITEM,MITHRIL_BOLT,MITH_GRAPPLE_TIP){player,bolt,tip ->
            if(player.skills.getLevel(Skills.FLETCHING) < 59){
                player.sendMessage("You need a fletching level of 59 to make this.")
                return@onUseWith true
            }
            if(player.inventory.remove(bolt.asItem(),tip.asItem())){
                player.inventory.add(Item(MITH_GRAPPLE))
            }
            return@onUseWith true
        }

        onUseWith(ITEM,ROPE,MITH_GRAPPLE){player,rope,grapple ->
            if(player.skills.getLevel(Skills.FLETCHING) < 59){
                player.sendMessage("You need a fletching level of 59 to make this.")
                return@onUseWith true
            }
            if(player.inventory.remove(rope.asItem(),grapple.asItem())){
                player.inventory.add(Item(ROPE_GRAPPLE))
            }
            return@onUseWith true
        }

        onUseWith(ITEM,LIMBIDs,*STOCKIDs){player, limb, stock ->
            val limbEnum = Fletching.limbMap[stock.id] ?: return@onUseWith false
            if(limbEnum.limb != limb.id){
                player.sendMessage("That's not the right limb to attach to that stock.")
                return@onUseWith true
            }
            val handler: SkillDialogueHandler = object : SkillDialogueHandler(player, SkillDialogue.ONE_OPTION, Item(limbEnum.product)){
                override fun create(amount: Int, index: Int) {
                    player.pulseManager.run(LimbPulse(player, stock.asItem(), limbEnum, amount))
                }

                override fun getAll(index: Int): Int {
                    return player.inventory.getAmount(stock.asItem())
                }
            }
            handler.open()
            PacketRepository.send(RepositionChild::class.java, ChildPositionContext(player, Components.SKILL_MULTI1_309, 2, 210, 10))
            return@onUseWith true
        }

    }

}