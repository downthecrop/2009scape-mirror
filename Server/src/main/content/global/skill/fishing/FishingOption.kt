package content.global.skill.fishing

import core.api.*
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.world.update.flag.context.Animation
import core.tools.RandomFunction
import org.rs09.consts.Items

enum class FishingOption(val tool: Int, val level: Int, val animation: Animation, val bait: IntArray?, val option: String, vararg val fish: Fish) {
    CRAYFISH_CAGE(Items.CRAYFISH_CAGE_13431, 1, Animation(10009), null, "cage", Fish.CRAYFISH),
    SMALL_NET(Items.SMALL_FISHING_NET_303, 1, Animation(621), null, "net", Fish.SHRIMP, Fish.ANCHOVIE),
    BAIT(Items.FISHING_ROD_307, 5, Animation(622), intArrayOf(Items.FISHING_BAIT_313), "bait", Fish.SARDINE, Fish.HERRING),
    LURE(Items.FLY_FISHING_ROD_309, 20, Animation(622), intArrayOf(Items.FEATHER_314, Items.STRIPY_FEATHER_10087), "lure", Fish.TROUT, Fish.SALMON, Fish.RAINBOW_FISH),
    PIKE_BAIT(Items.FISHING_ROD_307, 25, Animation(622), intArrayOf(Items.FISHING_BAIT_313), "bait", Fish.PIKE),
    LOBSTER_CAGE(Items.LOBSTER_POT_301, 40, Animation(619), null, "cage", Fish.LOBSTER),
    HARPOON(Items.HARPOON_311, 35, Animation(618), null, "harpoon", Fish.TUNA, Fish.SWORDFISH),
    BARB_HARPOON(Items.BARB_TAIL_HARPOON_10129, 35, Animation(618), null, "harpoon", Fish.TUNA, Fish.SWORDFISH),
    BIG_NET(Items.BIG_FISHING_NET_305, 16, Animation(620), null, "net", Fish.MACKEREL, Fish.COD, Fish.BASS, Fish.SEAWEED),
    SHARK_HARPOON(Items.HARPOON_311, 76, Animation(618), null, "harpoon", Fish.SHARK),
    MONKFISH_NET(Items.SMALL_FISHING_NET_303, 62, Animation(621), null, "net", Fish.MONKFISH),
    MORTMYRE_ROD(Items.FISHING_ROD_307, 5, Animation(622), intArrayOf(Items.FISHING_BAIT_313), "bait", Fish.SLIMY_EEL),
    LUMBDSWAMP_ROD(Items.FISHING_ROD_307, 5, Animation(622), intArrayOf(Items.FISHING_BAIT_313), "bait", Fish.SLIMY_EEL, Fish.CAVE_EEL),
    LUMBDSWAMP_NET(Items.SMALL_FISHING_NET_303, 1, Animation(621), null, "net", Fish.FROG_SPAWN),
    KBWANJI_NET(Items.SMALL_FISHING_NET_303, 5, Animation(621), null, "net", Fish.KARAMBWANJI),
    KARAMBWAN_VES(Items.KARAMBWAN_VESSEL_3157, 65, Animation(1193), intArrayOf((Items.RAW_KARAMBWANJI_3150)), "fish", Fish.KARAMBWAN),
    OILY_FISHING_ROD(Items.OILY_FISHING_ROD_1585, 53, Animation(622), intArrayOf(Items.FISHING_BAIT_313), "bait", Fish.LAVA_EEL);

    companion object {
        private val nameMap: HashMap<String, FishingOption> = HashMap()

        init {
            for(value in values()) {
                nameMap[value.option] = value
            }
        }

        fun forName(opName: String): FishingOption? {
            return nameMap[opName]
        }
    }

    fun rollFish(player: Player): Fish? {
        if(this == BIG_NET) {
            when(RandomFunction.randomize(100)) {
                0 -> return Fish.OYSTER
                50 -> return Fish.CASKET
                90 -> return Fish.SEAWEED
            }
        }
        val vlvl = getDynLevel(player, Skills.FISHING)
        val ilvl = vlvl + player.familiarManager.getBoost(Skills.FISHING)
        for(f in fish) {
            if(f.level > vlvl) {
                continue
            }
            if(this == LURE && inInventory(player, Items.STRIPY_FEATHER_10087) != (f == Fish.RAINBOW_FISH)) {
                continue
            }
            val chance = f.getSuccessChance(ilvl)
            if(RandomFunction.random(0.0, 1.0) < chance) {
                return f
            }
        }
        return null
    }

    fun getBaitName(): String {
        if(bait != null && bait.isNotEmpty()) {
            return getItemName(bait[0])
        }
        return "none"
    }

    fun hasBait(player: Player): Boolean {
        return if(bait == null) true else {
            var anyBait = false
            for(b in bait) {
                anyBait = anyBait || inInventory(player, b)
            }
            anyBait
        }
    }

    fun removeBait(player: Player): Boolean {
        return if (bait == null) {
            true
        } else {
            for (i in bait.size downTo 1) {
                if (removeItem(player, bait[i - 1], Container.INVENTORY)) {
                    return true
                }
            }
            false
        }
    }

    fun getStartMessage(): String {
        return if(option == "net")
            "You cast out your net..."
        else
            "You attempt to catch a fish."
    }
}