package rs09.game.node.entity.skill.magic

import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import org.rs09.consts.Items

// Animations
val BAKE_PIE_ANIM = Animation(4413)
val STATSPY_ANIM = Animation(6293)
val CURE_PLANT_ANIM = Animation(4409)
val NPC_CONTACT_ANIM = Animation(4413)
val PLANK_MAKE_ANIM = Animation(6298)
val STRING_JEWELLERY_ANIM = Animation(4412)
val SUPERGLASS_MAKE_ANIM = Animation(4413)
val FERTILE_SOIL_ANIM = Animation(724)
val CURE_ME_ANIM = Animation(4411)
val CURE_GROUP_ANIM = Animation(4409)
val CURE_OTHER_ANIM = Animation(4411)


// Graphics
val BAKE_PIE_GFX = Graphics(746,75)
val STATSPY_GFX = Graphics(1059)
val CURE_PLANT_GFX = Graphics(742,100)
val NPC_CONTACT_GFX = Graphics(730,130)
val PLANK_MAKE_GFX = Graphics(1063, 120)
val STRING_JEWELLERY_GFX = Graphics(728, 100)
val SUPERGLASS_MAKE_GFX = Graphics(729, 120)
val FERTILE_SOIL_GFX = Graphics(141, 96)
val CURE_ME_GFX = Graphics(731, 90)
val CURE_GROUP_GFX = Graphics(751, 130)
val CURE_OTHER_GFX = Graphics(738, 130)

enum class JewelleryString(val unstrung: Int, val strung: Int) {
    GOLD(Items.GOLD_AMULET_1673, Items.GOLD_AMULET_1692),
    SAPPHIRE(Items.SAPPHIRE_AMULET_1675, Items.SAPPHIRE_AMULET_1694),
    EMERALD(Items.EMERALD_AMULET_1677, Items.EMERALD_AMULET_1696),
    RUBY(Items.RUBY_AMULET_1679, Items.RUBY_AMULET_1698),
    DIAMOND(Items.DIAMOND_AMULET_1681, Items.DIAMOND_AMULET_1700),
    DRAGONSTONE(Items.DRAGONSTONE_AMMY_1683, Items.DRAGONSTONE_AMMY_1702),
    ONYX(Items.ONYX_AMULET_6579, Items.ONYX_AMULET_6581),
    SALVE(Items.SALVE_SHARD_4082, Items.SALVE_AMULET_4081),
    HOLY(Items.UNSTRUNG_SYMBOL_1714, Items.UNBLESSED_SYMBOL_1716),
    UNHOLY(Items.UNSTRUNG_EMBLEM_1720, Items.UNPOWERED_SYMBOL_1722);
    companion object {
        val productOfString = values().associate { it.unstrung to it.strung }
        fun forId(id : Int) : Int {
            return productOfString[id]!!
        }

        fun unstrungContains(id: Int): Boolean {
            return productOfString.contains(id)
        }
    }
}
