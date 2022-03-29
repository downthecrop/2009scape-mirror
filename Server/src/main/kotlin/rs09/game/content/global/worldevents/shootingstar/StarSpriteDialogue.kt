package rs09.game.content.global.worldevents.shootingstar

import api.Container
import api.*
import core.game.content.dialogue.DialoguePlugin
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.tools.RandomFunction
import org.json.simple.JSONObject
import org.rs09.consts.Items
import rs09.ServerStore
import rs09.ServerStore.Companion.getBoolean
import rs09.game.node.entity.state.newsys.states.ShootingStarState
import rs09.tools.END_DIALOGUE
import rs09.tools.secondsToTicks
import rs09.tools.stringtools.colorize
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
         if (inInventory(player, Items.ANCIENT_BLUEPRINT_14651) && !getAttribute(player, "star-ring:bp-shown", false)) {
            npcl(FacialExpression.NEUTRAL, "I see you got ahold of a blueprint of those silly old rings we used to make.")
            stage = 1000
        } else if (inInventory(player, Items.ANCIENT_BLUEPRINT_14651) && getAttribute(player, "star-ring:bp-shown", false)) {
             playerl(FacialExpression.HALF_ASKING, "So about those rings...")
             stage = 2000
        } else if (getStoreFile().getBoolean(player.username.toLowerCase()) || !player.getInventory().contains(ShootingStarOptionHandler.STAR_DUST, 1)) {
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
                val wearingRing = inEquipment(player, Items.RING_OF_THE_STAR_SPRITE_14652)
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

                    getStoreFile()[player.username.toLowerCase()] = true //flag daily as completed

                    player.registerState("shooting-star")?.init()

                    if(wearingRing){
                        val item = intArrayOf(Items.COSMIC_RUNE_564, Items.ASTRAL_RUNE_9075, Items.GOLD_ORE_445, Items.COINS_995).random()
                        val amount = when(item){
                            Items.COSMIC_RUNE_564 -> cosmicRunes
                            Items.ASTRAL_RUNE_9075 -> astralRunes
                            Items.GOLD_ORE_445 -> goldOre
                            Items.COINS_995 -> coins
                            else -> 0
                        }
                        rollForRingBonus(player, item, amount)
                    }
                }

                if(!inInventory(player, Items.ANCIENT_BLUEPRINT_14651) && !inBank(player, Items.ANCIENT_BLUEPRINT_14651) && RandomFunction.roll(500)){
                    addItemOrDrop(player, Items.ANCIENT_BLUEPRINT_14651, 1)
                    sendMessage(player, colorize("%RThe Star Sprite dropped what looks like some ancient piece of paper and you pick it up."))
                    sendNews("${player.username} found an Ancient Blueprint while mining a shooting star!")
                }
                stage = 52
            }
            52 -> end()

            //Inauthentic ring-based dialogue
            1000 -> playerl(FacialExpression.ASKING, "Oh, you mean this?").also { stage++ }
            1001 -> player.dialogueInterpreter.sendItemMessage(Items.ANCIENT_BLUEPRINT_14651, "You show the blueprint to the Star Sprite.").also { stage++ }
            1002 -> npcl(FacialExpression.ASKING, "Yeah, that's the one, alright!").also { stage++ }
            1003 -> npcl(FacialExpression.NEUTRAL, "I'll tell you what.. if you can get ahold of the resources needed to make one, I'm sure me or one of my kin would craft it for you.").also { stage++ }
            1004 -> playerl(FacialExpression.ASKING, "You'd just do that for me? For free?").also { stage++ }
            1005 -> npcl(FacialExpression.NEUTRAL, "I don't see why not. We used to make these for fun and hand them out to adventurers all the time.").also { stage++ }
            1006 -> playerl(FacialExpression.ASKING, "Well, thanks! So.. what do we need to make one?").also { stage++ }
            1007 -> npcl(FacialExpression.NEUTRAL, "Looking at the blueprint here...").also { stage++ }
            1008 -> npcl(FacialExpression.NEUTRAL, "Yes, it seems we need a ring mould, a silver bar, a cut dragonstone and 200 stardust. Oh, and make sure to bring this blueprint with you.").also { stage++ }
            1009 -> playerl(FacialExpression.FRIENDLY, "Thanks, I'll get right on it!").also { stage++ }
            1010 -> playerl(FacialExpression.ASKING, "So just to make sure I've got it right, I need a ring mould, a silver bar, a cut dragonstone and 200 stardust, as well as this blueprint?").also { stage++ }
            1011 -> npcl(FacialExpression.NEUTRAL, "Yeah, you've got it, human. Any of my kin should be able to do this for you.").also { stage++; setAttribute(player, "/save:star-ring:bp-shown", true) }
            1012 -> playerl(FacialExpression.FRIENDLY, "Thanks!").also { stage = END_DIALOGUE }

            2000 -> npcl(FacialExpression.NEUTRAL, "Yes, did you bring the components to make it, human?").also { stage++ }
            2001 -> if(inInventory(player, Items.DRAGONSTONE_1615,1) && inInventory(player, Items.RING_MOULD_1592, 1) && inInventory(player, Items.STARDUST_13727, 200) && inInventory(player, Items.SILVER_BAR_2355, 1)){
                playerl(FacialExpression.FRIENDLY, "Yes, I have them right here, friend.").also { stage++ }
            } else {
                playerl(FacialExpression.HALF_GUILTY, "I'm afraid not, what did I need again?").also { stage = 2100 }
            }
            2002 -> npcl(FacialExpression.NEUTRAL, "Excellent, give me just a moment here...").also { stage++ }
            2003 -> sendDialogue("You watch as the Star Sprite casts some strange spell.").also { stage++ }
            2004 -> if(removeItem(player, Items.SILVER_BAR_2355, Container.INVENTORY) && removeItem(player, Items.DRAGONSTONE_1615, Container.INVENTORY) && removeItem(player, Item(Items.STARDUST_13727, 200), Container.INVENTORY)){
                    addItem(player, Items.RING_OF_THE_STAR_SPRITE_14652)
                    player.dialogueInterpreter.sendItemMessage(Items.RING_OF_THE_STAR_SPRITE_14652, "The Star Sprite hands you a strange ring.").also { stage++ }
                } else end()
            2005 -> npcl(FacialExpression.NEUTRAL, "There you go, I hope you enjoy it!").also { stage++ }
            2006 -> playerl(FacialExpression.FRIENDLY, "Thank you!").also { stage = END_DIALOGUE }

            2100 -> npcl(FacialExpression.NEUTRAL, "A ring mould, a cut dragonstone, a silver bar and 200 stardust.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray? {
        return intArrayOf(8091)
    }

    fun rollForRingBonus(player: Player, bonusId: Int, bonusBaseAmt: Int){
        if(RandomFunction.roll(3)){
            val state = player.states["shooting-star"] as? ShootingStarState ?: return
            state.ticksLeft += secondsToTicks(TimeUnit.MINUTES.toSeconds(5).toInt())
            sendMessage(player, colorize("%RYour ring shines dimly as if imbued with energy."))
        } else if(RandomFunction.roll(5)){
            addItem(player, bonusId, bonusBaseAmt)
            sendMessage(player, colorize("%RYour ring shines brightly as if surging with energy and then fades out."))
        } else if(RandomFunction.roll(25)){
            getStoreFile()[player.username.toLowerCase()] = false //flag daily as uncompleted
            sendMessage(player, colorize("%RYour ring vibrates briefly as if surging with power, and then stops."))
        }
    }

    fun getStoreFile(): JSONObject{
        return ServerStore.getArchive("daily-shooting-star")
    }

}
