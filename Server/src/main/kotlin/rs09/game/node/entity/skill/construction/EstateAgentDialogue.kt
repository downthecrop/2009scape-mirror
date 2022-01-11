package rs09.game.node.entity.skill.construction

import core.game.content.dialogue.DialoguePlugin
import core.game.content.dialogue.FacialExpression
import core.game.content.global.Skillcape
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.skill.Skills
import core.game.node.entity.skill.construction.HouseLocation
import core.game.node.entity.skill.construction.HousingStyle
import core.game.node.item.Item
import core.plugin.Initializable
import org.rs09.consts.Items
import rs09.game.world.GameWorld.settings
import rs09.tools.END_DIALOGUE
import rs09.tools.START_DIALOGUE

/**
 * Represents the estate agent dialogue.
 *
 * @author Woah
 */
@Initializable
class EstateAgentDialogue : DialoguePlugin {

    /**
     * Constructs a new `EstateAgentDialogue` `Object`.
     */
    constructor() {
        /**
         * empty.
         */
    }

    /**
     * Constructs a new `EstateAgentDialogue` `Object`.
     *
     * @param player the player.
     */
    constructor(player: Player?) : super(player)

    override fun newInstance(player: Player): DialoguePlugin {
        return EstateAgentDialogue(player)
    }

    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc("Hello. Welcome to the " + settings!!.name + " Housing Agency! What", "can I do for you?")
        stage = START_DIALOGUE
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {

            START_DIALOGUE -> {
                if (player.houseManager.hasHouse()) {
                    options(
                        "Can you move my house please?",
                        "Can you redecorate my house please?",
                        "Could I have a Construction guidebook?",
                        "Tell me about houses.",
                        "Tell me about that skillcape you're wearing.")
                    stage = 1
                } else {
                    options("How can I get a house?", "Tell me about houses.")
                    stage = 2
                }
            }

            1 -> when (buttonId) {
                1 -> {
                    player("Can you move my house please?")
                    stage = 10
                }
                2 -> {
                    player("Can you redecorate my house please?")
                    stage = 30
                }
                3 -> {
                    player("Could I have a Construction guidebook?")
                    stage = 50
                }
                4 -> {
                    player("Tell me about houses!")
                    stage = 60
                }
                5 -> {
                    player("Tell me about that skillcape you're wearing!")
                    stage = if (Skillcape.isMaster(player, Skills.CONSTRUCTION)) 102 else 100
                }
            }

            2 -> when (buttonId) {
                1 -> {
                    player("How can I get a house?")
                    stage = 3
                }
                2 -> {
                    player("Tell me about houses.")
                    stage = 60
                }
            }

            3 -> {
                npc(
                    "I can sell you a starting house in Rimmington for",
                    "1000 coins. As you increase your construction skill you",
                    "will be able to have your house moved to other areas",
                    "and redecorated in other styles."
                )
                stage++
            }

            4 -> {
                npc("Do you want to buy a starter house?")
                stage++
            }

            5 -> {
                options("Yes please!", "No thanks.")
                stage++
            }

            6 -> when (buttonId) {
                1 -> {
                    player("Yes please!")
                    stage = 7
                }
                2 -> {
                    player("No thanks.")
                    stage = END_DIALOGUE
                }
            }

            7 -> {
                if (player.inventory.contains(995, 1000)) {
                    player.inventory.remove(Item(995, 1000))
                    player.houseManager.create(HouseLocation.RIMMINGTON)
                    npc(
                        "Thank you. Go through the Rimmington house portal",
                        "and you will find your house ready for you to start",
                        "building in it."
                    )
                } else {
                    npc("You don't have enough money to buy a house,", "come back when you can afford one.")
                }
                stage = END_DIALOGUE
            }

            10 -> {
                npc("Certainly. Where would you like it moved to?")
                stage++
            }

            11 -> {
                options(
                    "Rimmington (5,000)",
                    "Taverley (5,000)",
                    "Pollnivneach (7,500)",
                    "Rellekka (10,000)",
                    "More..." )
                stage++
            }

            12 -> when (buttonId) {
                1 -> {
                    player("To Rimmington please!")
                    stage = 15
                }
                2 -> {
                    player("To Taverly please!")
                    stage = 16
                }
                3 -> {
                    player("To Pollnivneach please!")
                    stage = 17
                }
                4 -> {
                    player("To Rellekka please!")
                    stage = 18
                }
                5 -> {
                    options("Brimhaven (15,000)", "Yanille (25,000)", "...Previous", "Back")
                    stage = 13
                }
            }

            13 -> when (buttonId) {
                1 -> {
                    player("To Brimhaven please!")
                    stage = 19
                }
                2 -> {
                    player("To Yanille please!")
                    stage = 20
                }
                3 -> {
                    options(
                        "Rimmington (5,000)",
                        "Taverley (5,000)",
                        "Pollnivneach (7,500)",
                        "Rellekka (10,000)",
                        "More..."
                    )
                    stage = 12
                }
            }

            15 -> configureMove(HouseLocation.RIMMINGTON)
            16 -> configureMove(HouseLocation.TAVERLY)
            17 -> configureMove(HouseLocation.POLLNIVNEACH)
            18 -> configureMove(HouseLocation.RELLEKKA)
            19 -> configureMove(HouseLocation.BRIMHAVEN)
            20 -> configureMove(HouseLocation.YANILLE)
            30 -> {
                npc(
                    "Certainly. My magic can rebuild the house in a",
                    "completely new style! What style would you like?" )
                stage++
            }
            31 -> {
                options(
                    "Basic wood (5,000)",
                    "Basic stone (5,000)",
                    "Whitewashed stone (7,500)",
                    "Fremennik-style wood (10,000)",
                    "More..."
                )
                stage++
            }
            32 -> when (buttonId) {
                1 -> {
                    player("Basic wood please!")
                    stage = 35
                }
                2 -> {
                    player("Basic stone please!")
                    stage = 36
                }
                3 -> {
                    player("Whitewashed stone please!")
                    stage = 37
                }
                4 -> {
                    player("Fremennik-style wood please!")
                    stage = 38
                }
                5 -> {
                    options("Tropical wood (15,000)", "Fancy stone (25,000)", "Previous...", "Back")
                    stage = 33
                }
            }
            33 -> when (buttonId) {
                1 -> {
                    player("Tropical wood please!")
                    stage = 39
                }
                2 -> {
                    player("Fancy stone please!")
                    stage = 40
                }
                3 -> {
                    options(
                        "Basic wood (5,000)",
                        "Basic stone (5,000)",
                        "Whitewashed stone (7,500)",
                        "Fremennik-style wood (10,000)",
                        "More..."
                    )
                    stage = 32
                }
            }

            35 -> redecorate(HousingStyle.BASIC_WOOD)
            36 -> redecorate(HousingStyle.BASIC_STONE)
            37 -> redecorate(HousingStyle.WHITEWASHED_STONE)
            38 -> redecorate(HousingStyle.FREMENNIK_STYLE_WOOD)
            39 -> redecorate(HousingStyle.TROPICAL_WOOD)
            40 -> redecorate(HousingStyle.FANCY_STONE)

            50 -> {
                if (player.hasItem(CONSTRUCTION_GUIDE_8463)) {
                    npc("You've already got one!")
                } else {
                    npc("Certainly.")
                    player.inventory.add(CONSTRUCTION_GUIDE_8463)
                }
                stage = END_DIALOGUE
            }
            60 -> {
                npc(
                    "It all came out of the wizards' experiments. They found",
                    "a way to fold space, so that they could pack many",
                    "acres of land into an area only a foot across."
                )
                stage++
            }
            61 -> {
                npc(
                    "They created several folded-space regions across",
                    "" + settings!!.name + ". Each one contains hundreds of small plots",
                    "where people can build houses."
                )
                stage++
            }
            62 -> {
                player("Ah, so that's how everyone can have a house without", "them cluttering up the world!")
                stage++
            }
            63 -> {
                npc(
                    "Quite. The wizards didn't want to get bogged down",
                    "in the business side of things so they ",
                    "hired me to sell the houses."
                )
                stage++
            }
            64 -> {
                npc(
                    "There are various other people across " + settings!!.name + " who can",
                    "help you furnish your house. You should start buying",
                    "planks from the sawmill operator in Varrock."
                )
                stage = END_DIALOGUE
            }
            100 -> {
                npc(
                    "As you may know, skillcapes are only available to masters",
                    "in a skill. I have spent my entire life building houses and",
                    "now I spend my time selling them! As a sign of my abilites",
                    "I wear this Skillcape of Construction. If you ever have"
                )
                stage = 101
            }
            101 -> {
                npc(
                    "enough skill to build a demonic throne, come and talk to",
                    "me and I'll sell you a skillcape like mine."
                )
                stage = END_DIALOGUE
            }
            102 -> {
                interpreter.sendDialogues(
                    npc,
                    FacialExpression.JOLLY,
                    "I see you have recently achieved 99 construction.",
                    "Would you like to buy a cape for 99,0000 gp?"
                )
                stage++
            }
            103 -> {
                options("Yes, I'll pay the 99k", "No thanks, maybe later.")
                stage++
            }
            104 -> when (buttonId) {
                1 -> {
                    if (Skillcape.purchase(player, Skills.CONSTRUCTION)) {
                        npc("Here you go lad, enjoy!")
                    }
                    stage = END_DIALOGUE
                }
                2 -> {
                    player("No thanks, maybe later.")
                    stage = END_DIALOGUE
                }
            }
            END_DIALOGUE -> end()
        }
        return true
    }

    /**
     * Configures the move.
     *
     * @param location The house location.
     */
    private fun configureMove(location: HouseLocation) {
        //Achievement completion checks
        val completedVarrockHouseMove = player.achievementDiaryManager.hasCompletedTask(DiaryType.VARROCK,0,11)
        when {
            //Player does not have required construction level
            !location.hasLevel(player) -> {
                npc(
                    "I'm afraid you don't have a high enough construction",
                    "level to move there. You need to have level " + location.levelRequirement + "." )
                stage = 11
                return
            }
            //Player's house location is already where they selected
            location == player.houseManager.location -> {
                npc("Your house is already there!")
                stage = 11
                return
            }
            //Player does not have enough coins to buy a house move
            !player.inventory.contains(Items.COINS_995, location.cost) -> {
                npc("Hmph. Come back when you have " + location.cost + " coins.")
                stage = END_DIALOGUE
                return
            }
            //Player meets all above requirements + check for achievement unlocks
            else -> {
                player.inventory.remove(Item(Items.COINS_995, location.cost))
                player.houseManager.location = location
                npc("Your house has been moved to " + location.getName() + ".")

                if (player.location.isInRegion(REGION_VARROCK_NE) && !completedVarrockHouseMove) {

                    player.achievementDiaryManager.finishTask(player, DiaryType.VARROCK, 0, 11)
                }
                stage = END_DIALOGUE
            }
        }
    }

    /**
     * Redecorates the player's house.
     *
     * @param style The house style.
     */
    private fun redecorate(style: HousingStyle) {
        //Achievement completion checks
        val completedVarrockHouseRedecorate = player.achievementDiaryManager.hasCompletedTask(DiaryType.VARROCK,2,7)
        when {
            //Player does not have required construction level
            !style.hasLevel(player) -> {
                npc("You need a Construction level of " + style.level + " to buy this style.")
                stage = 31
                return
            }
            //Player's house location is already where they selected
            style == player.houseManager.style -> {
                npc("Your house is already in that style!")
                stage = 31
                return
            }

            //Player does not have enough coins to buy a house move
            !player.inventory.contains(Items.COINS_995, style.cost) -> {
                npc("Hmph. Come back when you have " + style.cost + " coins.")
                stage = END_DIALOGUE
                return
            }

            //Player meets all above requirements + check for achievement unlocks
            else -> {
                player.inventory.remove(Item(Items.COINS_995, style.cost))
                player.houseManager.redecorate(style)
                npc("Your house has been redecorated.")

                // Give your player-owned house a fancy stone or tropical wood<br><br>finish at the Varrock estate agent's
                if (player.location.isInRegion(REGION_VARROCK_NE) && !completedVarrockHouseRedecorate) {
                    player.achievementDiaryManager.finishTask(player, DiaryType.VARROCK, 2, 7)
                }
                stage = END_DIALOGUE
            }
        }
    }

    override fun getIds(): IntArray {
        return intArrayOf(4247)
    }

    //Items
    private val CONSTRUCTION_GUIDE_8463 = Item(Items.CONSTRUCTION_GUIDE_8463, 1)

    //Region to check
    private val REGION_VARROCK_NE = 12854
}