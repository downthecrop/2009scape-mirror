package core.game.content.global.worldevents.shootingstar

import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.content.dialogue.DialoguePlugin
import java.util.concurrent.TimeUnit

/**
 * Dialogue for the star sprite.
 */
class StarSpriteDialogue(player: Player? = null) : DialoguePlugin(player) {

    /**
     * The cosmic rune item id.
     */
    val COSMIC_RUNE = 564

    /**
     * The astral rune item id.
     */
    val ASTRAL_RUNE = 9075

    /**
     * The gold ore item id.
     */
    val GOLD_ORE = 445

    /**
     * The coins id.
     */
    val COINS = 995

    /**
     * The amplifier amount.
     */
    val AMPLIFIER = 1.0


    /**
     * Constructs the star sprite dialogue.
     */
    fun StarSpriteDialogue() {
        /*
			 * empty.
			 */
    }

    override fun newInstance(player: Player?): DialoguePlugin? {
        return StarSpriteDialogue(player)
    }

    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        if (player.getSavedData().getGlobalData().getStarSpriteDelay() > System.currentTimeMillis() || !player.getInventory().contains(ShootingStarOptionHandler.STAR_DUST, 1)) {
            npc("Hello, strange creature.")
            stage = 0
        } else {
            npc("Thank you for helping me out of here.")
            stage = 50
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> {
                npc("I'm a star sprite! I Was in my star in the sky, when it", "lost control and crashed into the ground. With half my", "star sticking into the ground, I became stuck.", "Fortunately, I was mined out by the kind creatures of")
                stage++
            }
            1 -> {
                npc("your race.")
                stage++
            }
            2 -> {
                options("What's a star sprite?", "What are you going to do without your star?", "I thought stars were huge balls of burning gas.", "Well, I'm glad you're okay.")
                stage++
            }
            3 -> when (buttonId) {
                1 -> {
                    player("What's a star sprite?")
                    stage = 10
                }
                2 -> {
                    player("What are you going to do without your star?")
                    stage = 20
                }
                3 -> {
                    player("I thought stars were huge balls of burning gas.")
                    stage = 30
                }
                4 -> {
                    player("Well, I'm glad you're okay.")
                    stage = 40
                }
            }
            10 -> {
                npc("We're what makes the stars in the sky shine. I made", "this star shine when it was in the sky.")
                stage++
            }
            11 -> {
                options("What are you going to do without your star?", "I thought stars were huge balls of burning gas.", "Well, I'm glad you're okay.")
                stage++
            }
            12 -> when (buttonId) {
                1 -> {
                    player("What are you going to do without your star?")
                    stage = 20
                }
                2 -> {
                    player("I thought stars were huge balls of burning gas.")
                    stage = 30
                }
                3 -> {
                    player("Well, I'm glad you're okay.")
                    stage = 40
                }
            }
            20 -> {
                npc("Don't worry about me. I'm sure I'll find some good", "rocks around here and get back up into the sky in no", "time.")
                stage++
            }
            21 -> {
                options("What's a star sprite?", "I thought stars were huge balls of burning gas.", "Well, I'm glad you're okay.")
                stage++
            }
            22 -> when (buttonId) {
                1 -> {
                    player("What's a star sprite?")
                    stage = 10
                }
                2 -> {
                    player("I thought stars were huge balls of burning gas.")
                    stage = 30
                }
                3 -> {
                    player("Well, I'm glad you're okay.")
                    stage = 40
                }
            }
            30 -> {
                npc("Most of them are, but a lot of shooting stars on this", "plane of the multiverse are rocks with star sprites in", "them.")
                stage++
            }
            31 -> {
                options("What's a star sprite?", "What are you going to do without your star?", "Well, I'm glad you're okay.")
                stage++
            }
            32 -> when (buttonId) {
                1 -> {
                    player("What's a star sprite?")
                    stage = 10
                }
                2 -> {
                    player("What are you going to do without your star?")
                    stage = 20
                }
                3 -> {
                    player("Well, I'm glad you're okay.")
                    stage = 40
                }
            }
            40 -> {
                npc("Thank you.")
                stage++
            }
            41 -> end()
            50 -> {
                val dust = if (player.getInventory().getAmount(ShootingStarOptionHandler.STAR_DUST) > 200) 200 else player.getInventory().getAmount(ShootingStarOptionHandler.STAR_DUST)
                if (player.getInventory().remove(Item(ShootingStarOptionHandler.STAR_DUST, dust))) {
                    val cosmicRunes = (Math.ceil(0.76 * dust) * AMPLIFIER).toInt()
                    val astralRunes = (Math.ceil(0.26 * dust) * AMPLIFIER).toInt()
                    val goldOre = (Math.ceil(0.1 * dust) * AMPLIFIER).toInt()
                    val coins = (Math.ceil(250.0 * dust) * AMPLIFIER).toInt() // limits the amount of gp per turn-in to 90k, same as 2009 rs, and minimum from all stardust from a level 1 star works out to 50k, same as rs in 2009
                    player.getInventory().add(Item(COSMIC_RUNE, cosmicRunes), player)
                    player.getInventory().add(Item(ASTRAL_RUNE, astralRunes), player)
                    player.getInventory().add(Item(GOLD_ORE, goldOre), player)
                    player.getInventory().add(Item(COINS, coins), player)
                    npc("I have rewarded you by making it so you can mine", "extra ore for the next 15 minutes. Also, have $cosmicRunes", "cosmic runes, $astralRunes astral runes, $goldOre gold ore and $coins", "coins.")
                    player.getSavedData().getGlobalData().setStarSpriteDelay(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1))
                    player.registerState("shooting-star").init()
                }
                stage = 52
            }
            52 -> end()
        }
        return true
    }

    override fun getIds(): IntArray? {
        return intArrayOf(8091)
    }

}