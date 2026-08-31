package content.region.morytania.quest.hauntedmine

import content.data.LightSource
import core.api.*
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.map.zone.ZoneBorders
import org.rs09.consts.Components
import org.rs09.consts.Items

/**
 * Zones covering the Abandoned Mine (home of the Haunted Mine quest).
 */

// if you leave the abandoned mine, any instances of Glowing Fungus in your inventory should be removed and replaced with ashes.
class AbandonedMineZone : MapArea {

    override fun defineAreaBorders(): Array<ZoneBorders> {
        return arrayOf(
            // mine floor 1
            ZoneBorders(3395, 9609, 3448, 9664),

            // mine floors 2-6
            ZoneBorders(2680, 4415, 2820, 4609)
        )
    }

    override fun areaLeave(entity: Entity, logout: Boolean) {
        if (entity is Player) {

            val loc = entity.location

            // this logic makes it so that if you move between mine floor 1 and the rest of the floors, you will still keep the fungus. also keeps fungus during the points cutscene.
            if (loc.x in 3395..3448 && loc.y in 9609..9664
                || loc.x in 2680..2820 && loc.y in 4415..4609
                || getAttribute(entity, HauntedMine.ATTR_KEEP_FUNGUS, false)
            ) {
                return
            } else {
                // remove any glowing fungus the player is carrying.
                val fungi = amountInInventory(entity, Items.GLOWING_FUNGUS_4075)
                if (removeItem(entity, Item(Items.GLOWING_FUNGUS_4075, fungi), Container.INVENTORY)) {
                    addItemOrDrop(entity, Items.ASHES_592, fungi)
                    sendMessage(entity, "The strange fungus you are carrying crumbles to dust.")
                }
            }
        }
        super.areaLeave(entity, logout)
    }
}

// If you enter level 5 of the abandoned mine, any tinderboxes with you should become damp tinderboxes.
// Light sources should also be extinguished. This is so that when you try to go to mine level 6 and the
// player says "I need a light source", it'll force you to get a glowing fungus.
class AbandonedMineL5Zone : MapArea {

    override fun defineAreaBorders(): Array<ZoneBorders> {
        return arrayOf(
            // this zone actually covers levels 5 and 6, so the tinderbox stays damp and you can't light a lantern for some reason on level 6 either.
            ZoneBorders(2688, 4416, 2815, 4479)
        )
    }

    override fun areaEnter(entity: Entity) {
        if (entity is Player) {
            val tindies = amountInInventory(entity, Items.TINDERBOX_590)
            if (removeItem(entity, Item(Items.TINDERBOX_590, tindies))) {
                addItemOrDrop(entity, Items.DAMP_TINDERBOX_4073, tindies)
                swapLitLightSources(entity)
            }
        }
        super.areaEnter(entity)
    }

    override fun areaLeave(entity: Entity, logout: Boolean) {
        if (entity is Player) {
            val tindies = amountInInventory(entity, Items.DAMP_TINDERBOX_4073)
            if (removeItem(entity, Item(Items.DAMP_TINDERBOX_4073, tindies))) {
                addItemOrDrop(entity, Items.TINDERBOX_590, tindies)
            }
        }
        super.areaLeave(entity, logout)
    }

    // Replaces all lit light sources in inventory + equipment
    fun swapLitLightSources(player: Player) {
        listOf(player.inventory, player.equipment).forEach { container ->
            for (slot in 0 until container.capacity()) {
                val item = container[slot] ?: continue
                val source = LightSource.forProductId(item.id) ?: continue
                removeItem(player, Item(source.product.id, item.amount))
                addItem(player, source.raw.id, item.amount)
                sendMessage(player, "The water puts out your " + source.product.name.lowercase() + "!")
            }
        }
    }
}

// if you go to level 6 without a Glowing Fungus, you get placed in a really dark area. This just applies an extra overlay.
class AbandonedMineL6Zone : MapArea {

    override fun defineAreaBorders(): Array<ZoneBorders> {
        return arrayOf(
            ZoneBorders(2688, 4607, 2751, 4544)
        )
    }

    override fun areaEnter(entity: Entity) {
        if (entity is Player) {
            openOverlay(entity, Components.DARKNESS_DARK_96)
        }
        super.areaEnter(entity)
    }

    override fun areaLeave(entity: Entity, logout: Boolean) {
        if (entity is Player) {
            closeOverlay(entity)
        }
        super.areaLeave(entity, logout)
    }
}