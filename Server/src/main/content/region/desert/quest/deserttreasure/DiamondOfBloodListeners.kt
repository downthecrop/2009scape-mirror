package content.region.desert.quest.deserttreasure

import core.api.*
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.Entity
import core.game.node.entity.npc.NPC
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import org.rs09.consts.Scenery

class DiamondOfBloodListeners : InteractionListener {
    override fun defineListeners() {

        // Silver pot conversions
        onUseWith(ITEM, Items.SILVER_POT_4660, Items.SPICE_2007) { player, used, with ->
            sendMessage(player, "You add some spices to the pot.")
            if(removeItem(player, used) && removeItem(player, with)) {
                addItemOrDrop(player, Items.SILVER_POT_4664)
            }
            return@onUseWith true
        }
        onUseWith(ITEM, Items.SILVER_POT_4660, Items.GARLIC_POWDER_4668) { player, used, with ->
            sendMessage(player, "You add some crushed garlic to the pot.")
            if(removeItem(player, used) && removeItem(player, with)) {
                addItemOrDrop(player, Items.SILVER_POT_4662)
            }
            return@onUseWith true
        }
        onUseWith(ITEM, Items.SILVER_POT_4662, Items.SPICE_2007) { player, used, with ->
            sendMessage(player, "You add some spices to the pot.")
            if(removeItem(player, used) && removeItem(player, with)) {
                addItemOrDrop(player, Items.SILVER_POT_4666)
            }
            return@onUseWith true
        }
        onUseWith(ITEM, Items.SILVER_POT_4664, Items.GARLIC_POWDER_4668) { player, used, with ->
            sendMessage(player, "You add some crushed garlic to the pot.")
            if(removeItem(player, used) && removeItem(player, with)) {
                addItemOrDrop(player, Items.SILVER_POT_4666)
            }
            return@onUseWith true
        }
        // Blessed pot conversions
        onUseWith(ITEM, Items.BLESSED_POT_4661, Items.SPICE_2007) { player, used, with ->
            sendMessage(player, "You add some spices to the pot.")
            if(removeItem(player, used) && removeItem(player, with)) {
                addItemOrDrop(player, Items.BLESSED_POT_4665)
            }
            return@onUseWith true
        }
        onUseWith(ITEM, Items.BLESSED_POT_4661, Items.GARLIC_POWDER_4668) { player, used, with ->
            sendMessage(player, "You add some crushed garlic to the pot.")
            if(removeItem(player, used) && removeItem(player, with)) {
                addItemOrDrop(player, Items.BLESSED_POT_4663)
            }
            return@onUseWith true
        }
        onUseWith(ITEM, Items.BLESSED_POT_4663, Items.SPICE_2007) { player, used, with ->
            sendMessage(player, "You add some spices to the pot.")
            if(removeItem(player, used) && removeItem(player, with)) {
                addItemOrDrop(player, Items.BLESSED_POT_4667)
            }
            return@onUseWith true
        }
        onUseWith(ITEM, Items.BLESSED_POT_4665, Items.GARLIC_POWDER_4668) { player, used, with ->
            sendMessage(player, "You add some crushed garlic to the pot.")
            if(removeItem(player, used) && removeItem(player, with)) {
                addItemOrDrop(player, Items.BLESSED_POT_4667)
            }
            return@onUseWith true
        }

        // You need to crush the garlic.
        onUseWith(ITEM, intArrayOf(Items.SILVER_POT_4660, Items.BLESSED_POT_4661, Items.SILVER_POT_4662, Items.BLESSED_POT_4663, Items.SILVER_POT_4664, Items.BLESSED_POT_4665, Items.SILVER_POT_4666, Items.BLESSED_POT_4667 ), Items.GARLIC_1550) { player, used, with ->
            sendMessage(player, "You need to crush the garlic before adding it to the pot.")
            return@onUseWith true
        }

        // Dessous jumps out.
        onUseWith(SCENERY, Items.BLESSED_POT_4667, Scenery.VAMPIRE_TOMB_6437) { player, used, with ->
            val prevNpc = getAttribute<NPC?>(player, DesertTreasure.attributeDessousInstance, null)
            if (prevNpc != null) {
                prevNpc.clear()
            }
            sendMessage(player, "You pour the blood from the pot onto the tomb.")
            removeItem(player, used)
            val scenery = with.asScenery()
            // Swap to a splittable vampire tomb scenery.
            replaceScenery(scenery, Scenery.VAMPIRE_TOMB_6438, Animation(1915).duration)
            // Vampire Tomb breaks open.
            animateScenery(player, scenery, 1915)
            // 8 Bat projectiles
            spawnProjectile(Location(3570, 3402), Location(3570, 3404), 350, 0, 0, 0, 60, 0)
            spawnProjectile(Location(3570, 3402), Location(3570, 3400), 350, 0, 0, 0, 60, 0)
            spawnProjectile(Location(3570, 3402), Location(3568, 3402), 350, 0, 0, 0, 60, 0)
            spawnProjectile(Location(3570, 3402), Location(3572, 3402), 350, 0, 0, 0, 60, 0)
            spawnProjectile(Location(3570, 3402), Location(3568, 3404), 350, 0, 0, 0, 60, 0)
            spawnProjectile(Location(3570, 3402), Location(3572, 3404), 350, 0, 0, 0, 60, 0)
            spawnProjectile(Location(3570, 3402), Location(3568, 3400), 350, 0, 0, 0, 60, 0)
            spawnProjectile(Location(3570, 3402), Location(3572, 3400), 350, 0, 0, 0, 60, 0)
            val npc = NPC(NPCs.DESSOUS_1914)
            queueScript(player, 1, QueueStrength.SOFT) { stage: Int ->
                when (stage) {
                    0 -> {
                        // Projectile gfx for Dessous to jump out.
                        spawnProjectile(Location(3570, 3402), Location(3570, 3405), 351, 0, 0, 0, 40, 0)
                        return@queueScript delayScript(player, 1)
                    }
                    1 -> {
                        npc.isRespawn = false
                        npc.isWalks = false
                        npc.location = Location(3570, 3405, 0)
                        npc.direction = Direction.NORTH
                        setAttribute(player, DesertTreasure.attributeDessousInstance, npc)
                        setAttribute(npc, "target", player)

                        npc.init()
                        npc.attack(player)
                        return@queueScript stopExecuting(player)
                    }
                    else -> return@queueScript stopExecuting(player)
                }
            }

            //sendGraphics(350, Location(3570, 3402))
            //sendGraphics(351, Location(3570, 3402))
            return@onUseWith true
        }
    }
}