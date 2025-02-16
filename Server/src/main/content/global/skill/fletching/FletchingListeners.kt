package content.global.skill.fletching

import content.data.Quests
import content.global.skill.fletching.items.arrow.ArrowHeadPulse
import content.global.skill.fletching.items.arrow.HeadlessArrowPulse
import content.global.skill.fletching.items.arrow.HeadlessOgreArrowPulse
import content.global.skill.fletching.items.bow.StringPulse
import content.global.skill.fletching.items.crossbow.LimbPulse
import core.api.*
import core.game.node.entity.skill.Skills
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
import core.game.dialogue.SkillDialogueHandler
import core.game.interaction.InteractionListener
import core.game.interaction.IntType
import core.game.node.entity.player.Player

class FletchingListeners : InteractionListener {

    val LIMBIDs = Fletching.Limb.values().map(Fletching.Limb::limb).toIntArray()
    val STOCKIDs = Fletching.Limb.values().map(Fletching.Limb::stock).toIntArray()
    val MITHRIL_BOLT = Items.MITHRIL_BOLTS_9142
    val MITH_GRAPPLE_TIP = Items.MITH_GRAPPLE_TIP_9416
    val ROPE = Items.ROPE_954
    val MITH_GRAPPLE = Items.MITH_GRAPPLE_9418
    val ROPE_GRAPPLE = Items.MITH_GRAPPLE_9419
    val ARROW_SHAFT = Items.ARROW_SHAFT_52
    val OGRE_ARROW_SHAFT = Items.OGRE_ARROW_SHAFT_2864
    val FLETCHED_SHAFT = Items.HEADLESS_ARROW_53
    val FLIGHTED_OGRE_ARROW = Items.FLIGHTED_OGRE_ARROW_2865
    val UNFINISHED_ARROWS = Fletching.ArrowHeads.values().map(Fletching.ArrowHeads::unfinished).toIntArray()
    val FEATHERS = intArrayOf(FEATHER_314,STRIPY_FEATHER_10087,RED_FEATHER_10088,BLUE_FEATHER_10089,YELLOW_FEATHER_10090,ORANGE_FEATHER_10091)
    val UNSTRUNG_BOWS = Fletching.String.values().map(Fletching.String::unfinished).toIntArray()
    val STRINGS = intArrayOf(Items.BOW_STRING_1777,Items.CROSSBOW_STRING_9438)

    override fun defineListeners() {

        onUseWith(IntType.ITEM,STRINGS,*UNSTRUNG_BOWS){ player, string, bow ->
            val enum = Fletching.stringMap[bow.id] ?: return@onUseWith false

            if (bow.id == Items.UNSTRUNG_COMP_BOW_4825) {
                // You shouldn't be able to string a bow
                if (getQuestStage(player, Quests.ZOGRE_FLESH_EATERS) < 8) {
                    player.packetDispatch.sendMessage("You must have started Zogre Flesh Eaters and asked Grish to string this.")
                    return@onUseWith true
                }
            }
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

        onUseWith(IntType.ITEM,ARROW_SHAFT,*FEATHERS){ player, shaft, feather ->
            val handler: SkillDialogueHandler =
                object : SkillDialogueHandler(player, SkillDialogue.MAKE_SET_ONE_OPTION, Item(FLETCHED_SHAFT)) {
                    override fun create(amount: Int, index: Int) {
                        player.pulseManager.run(HeadlessArrowPulse(player, shaft.asItem(), Item(feather.id), amount))
                    }

                    override fun getAll(index: Int): Int {
                        return player.inventory.getAmount(FLETCHED_SHAFT)
                    }
                }
            handler.open()
            return@onUseWith true
        }

        onUseWith(IntType.ITEM,OGRE_ARROW_SHAFT,*FEATHERS){ player, shaft, feather ->
            val handler: SkillDialogueHandler =
                    object : SkillDialogueHandler(player, SkillDialogue.MAKE_SET_ONE_OPTION, Item(FLIGHTED_OGRE_ARROW)) {
                        override fun create(amount: Int, index: Int) {
                            player.pulseManager.run(HeadlessOgreArrowPulse(player, shaft.asItem(), Item(feather.id, 4), amount))
                        }

                        override fun getAll(index: Int): Int {
                            return player.inventory.getAmount(FLIGHTED_OGRE_ARROW)
                        }
                    }
            handler.open()
            return@onUseWith true
        }

        onUseWith(IntType.ITEM,FLETCHED_SHAFT,*UNFINISHED_ARROWS){ player, shaft, unfinished ->
            val head = Fletching.arrowHeadMap[unfinished.id] ?: return@onUseWith false
            val handler: SkillDialogueHandler =
                object : SkillDialogueHandler(player, SkillDialogue.MAKE_SET_ONE_OPTION, head.getFinished()) {
                    override fun create(amount: Int, index: Int) {
                        player.pulseManager.run(ArrowHeadPulse(player, shaft.asItem(), head, amount))
                    }

                    override fun getAll(index: Int): Int {
                        return player.inventory.getAmount(head.getUnfinished())
                    }
                }
            handler.open()
            return@onUseWith true
        }

        onUseWith(IntType.ITEM,MITHRIL_BOLT,MITH_GRAPPLE_TIP){ player, bolt, tip ->
            if(player.skills.getLevel(Skills.FLETCHING) < 59){
                player.sendMessage("You need a fletching level of 59 to make this.")
                return@onUseWith true
            }
            if(player.inventory.remove(Item(MITHRIL_BOLT,1),tip.asItem())){
                player.inventory.add(Item(MITH_GRAPPLE))
            }
            return@onUseWith true
        }

        onUseWith(IntType.ITEM,ROPE,MITH_GRAPPLE){ player, rope, grapple ->
            if(player.skills.getLevel(Skills.FLETCHING) < 59){
                player.sendMessage("You need a fletching level of 59 to make this.")
                return@onUseWith true
            }
            if(player.inventory.remove(rope.asItem(),grapple.asItem())){
                player.inventory.add(Item(ROPE_GRAPPLE))
            }
            return@onUseWith true
        }

        onUseWith(IntType.ITEM,LIMBIDs,*STOCKIDs){ player, limb, stock ->
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

        /**
         * (Long) Kebbit bolts don't need feathers and go 6 at a time so use their own interaction
         */
        fun makeKebbitBolt(player : Player, ingredient : Item) : Boolean{
            val longBolts = when(ingredient.id){
                Items.KEBBIT_SPIKE_10105 -> false
                Items.LONG_KEBBIT_SPIKE_10107 -> true
                else -> return false
            }
            val level = if(longBolts) 42 else 26
            if (getDynLevel(player, Skills.FLETCHING) < level){
                sendMessage(player, "You need a fletching level of $level to create ${if (longBolts) "long " else ""}kebbit bolts.")
                return true
            }
            val finalProduct = if(longBolts) Items.LONG_KEBBIT_BOLTS_10159 else Items.KEBBIT_BOLTS_10158
            val xp = if(longBolts) 47.7 else 28.6 // source https://runescape.wiki/w/Fletching?oldid=1069981#Bolts_2
            if(removeItem(player, ingredient.id)){
                addItem(player, finalProduct, 6)
                player.skills.addExperience(Skills.FLETCHING, xp)
                animate(player, 885)
            }
            return true
        }
        onUseWith(IntType.ITEM, Items.CHISEL_1755, Items.KEBBIT_SPIKE_10105) { player, used, with ->
            return@onUseWith makeKebbitBolt(player, with as Item)
        }

        onUseWith(IntType.ITEM, Items.CHISEL_1755, Items.LONG_KEBBIT_SPIKE_10107) { player, used, with ->
            return@onUseWith makeKebbitBolt(player, with as Item)
        }
    }

}