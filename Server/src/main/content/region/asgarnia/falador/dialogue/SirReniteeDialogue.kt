package content.region.asgarnia.falador.dialogue

import content.global.skill.construction.CrestType
import content.global.skill.construction.PaintingType
import content.global.skill.crafting.HeraldicProduct
import core.ServerConstants
import core.Util
import core.api.*
import core.game.dialogue.ChatAnim
import core.game.dialogue.DialogueLabeller
import core.game.dialogue.DialogueOption
import core.game.diary.DiaryLevel
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.tools.RandomFunction
import org.rs09.consts.Items
import org.rs09.consts.NPCs

/**
 * Dialogue for Sir Renitee, who manages the player's personal crest for the Construction skill, and sells paintings.
 * @author Bishop
 */

class SirReniteeDialogue : InteractionListener {
    override fun defineListeners() {
        on(NPCs.SIR_RENITEE_4249, IntType.NPC, "talk-to") { player, node ->
            DialogueLabeller.open(player, SirReniteeDialogueFile(), node as NPC)
            return@on true
        }
    }
}

class SirReniteeDialogueFile : DialogueLabeller() {

    companion object {
        const val ATTRIBUTE_CREST                 = "/save:sir-renitee-assigned-crest"
        private const val ATTRIBUTE_CREST_TEMP    = "sir-renitee-temp-crest"
        private const val ATTRIBUTE_PAINTING_TEMP = "sir-renitee-temp-painting"
    }

    private fun selectCrest(crestType: CrestType, labelName: String) {
        label(labelName)
            exec { player, _ ->
                setAttribute(player, ATTRIBUTE_CREST_TEMP, crestType)
                loadLabel(player, "crest_buy")
            }
    }

    private fun selectPainting(painting: PaintingType, labelName: String) {
        label(labelName)
            exec { player, _ ->
                setAttribute(player, ATTRIBUTE_PAINTING_TEMP, painting)
                loadLabel(player, "painting_buy")
            }
    }

    override fun addConversation() {

        /**
         * GENERIC DIALOGUE
         */

        npc(ChatAnim.NEUTRAL, "Hmm? What's that, young @g[man,woman]? What can I do for", "you?")
            options(
                DialogueOption("intro", "I don't know, what can you do for me?", expression = ChatAnim.ASKING),
                DialogueOption("nothing", "Nothing, thanks", spokenText = "Nothing, thanks.", expression = ChatAnim.NEUTRAL),
            )

        label("nothing")
            npc(ChatAnim.NEUTRAL, "Mmm, well, see you some other time maybe.")

        /**
         * CREST DIALOGUE
         */

        label("intro")
            npc(ChatAnim.NEUTRAL, "Hmm, well, mmm, do you have a family crest? I keep",
                "track of every ${ServerConstants.SERVER_NAME} family, you know, so I might", "be able to find yours.")
            npc(ChatAnim.NEUTRAL, "I'm also something of an, mmm, a painter. If you've",
                "met any important persons or visited any nice places I", "could paint them for you.")
            options(
                DialogueOption("family_crest", "Can you see if I have a family crest?", expression = ChatAnim.ASKING),
                DialogueOption("painting", "Can I buy a painting?", expression = ChatAnim.NEUTRAL),
            )

        label("family_crest")
            npc(ChatAnim.NEUTRAL_FAST, "What is your name?")
            player(ChatAnim.NEUTRAL, "${player!!.username}.")
            npc(ChatAnim.NEUTRAL_FAST, "Mmm, ${player!!.username}, let me see...")
            exec { player, _ ->
                if (hasLevelStat(player, Skills.CONSTRUCTION, HeraldicProduct.STEEL_HELM.constructionReq)) {
                    loadLabel(player, "has_construction_level")
                } else {
                    loadLabel(player, "no_construction_level")
                }
            }

        label("no_construction_level")
            npc(ChatAnim.NEUTRAL, "First thing's first, young @g[man,woman]! There is not much point",
                "in having a family crest if you cannot display it.")
            npc(ChatAnim.NEUTRAL, "You should train construction until you can build a wall",
                "decoration in your dining room.")

        label("has_construction_level")
            exec { player, _ ->
                if (getAttribute(player, ATTRIBUTE_CREST, false)) {
                    loadLabel(player, "show_existing_crest")
                } else {
                    val crests = arrayOf(CrestType.VARROCK, CrestType.ASGARNIA, CrestType.KANDARIN, CrestType.MISTHALIN)
                    player.houseManager.crest = crests[RandomFunction.random(4)]
                    setAttribute(player, ATTRIBUTE_CREST, true)
                    loadLabel(player, "show_new_crest")
                }
                if (!player.achievementDiaryManager.getDiary(DiaryType.FALADOR).isComplete(0, 4)) {
                    finishTask(player, DiaryType.FALADOR, DiaryLevel.EASY, 4)
                }
            }

        label("show_existing_crest")
            npc(ChatAnim.NEUTRAL, "According to my records, your crest is ${player!!.houseManager.crest.crestName}.")
            goto("crest_options")

        label("show_new_crest")
            npc(ChatAnim.NEUTRAL, "Well, I don't think you have any noble blood,",
                "but I see that your ancestors came from ${Util.enumToString(player!!.houseManager.crest.name)},",
                "so ${if (player!!.houseManager.crest == CrestType.VARROCK) "you can use that city's" else "that can be your"} crest.")
            goto("crest_options")

        label("crest_options")
            options(
                DialogueOption("change_crest", "I don't like that crest. Can I have a different one?", expression = ChatAnim.ASKING),
                DialogueOption("thanks", "Thanks!", expression = ChatAnim.NEUTRAL),
            )

        label("thanks")
            npc(ChatAnim.NEUTRAL, "You're welcome, my @g[boy,girl].")

        label("change_crest")
            npc(ChatAnim.NEUTRAL, "Mmm, very well. Changing your crest will cost 5,000", "coins.")
            exec { player, _ ->
                if (amountInInventory(player, Items.COINS_995) < CrestType.ASGARNIA.cost) {
                    loadLabel(player, "no_money")
                } else {
                    loadLabel(player, "choose_symbol")
                }
            }

        label("no_money")
            player(ChatAnim.NEUTRAL, "I'll have to go and get some money then.")

        label("choose_symbol")
            npc(ChatAnim.NEUTRAL, "There are sixteen different symbols; which one would", "you like?")
            goto("crest_page1")

        label("crest_page1")
            options(
                DialogueOption("crest_arrav", "Shield of Arrav", skipPlayer = true),
                DialogueOption("crest_asgarnia", "Asgarnia", skipPlayer = true),
                DialogueOption("crest_dorgeshuun", "Dorgeshuun Symbol", skipPlayer = true),
                DialogueOption("crest_dragon", "Dragon", skipPlayer = true),
                DialogueOption("crest_page2", "More...", skipPlayer = true),
            )

        label("crest_page2")
            options(
                DialogueOption("crest_fairy", "Fairy", skipPlayer = true),
                DialogueOption("crest_guthix", "Guthix", skipPlayer = true),
                DialogueOption("crest_ham", "HAM", skipPlayer = true),
                DialogueOption("crest_horse", "Horse", skipPlayer = true),
                DialogueOption("crest_page3", "More...", skipPlayer = true),
            )

        label("crest_page3")
            options(
                DialogueOption("crest_jogre", "Jogre", skipPlayer = true),
                DialogueOption("crest_kandarin", "Kandarin", skipPlayer = true),
                DialogueOption("crest_misthalin", "Misthalin", skipPlayer = true),
                DialogueOption("crest_money", "Money", skipPlayer = true),
                DialogueOption("crest_page4", "More...", skipPlayer = true),
            )

        label("crest_page4")
            options(
                DialogueOption("crest_saradomin", "Saradomin", skipPlayer = true),
                DialogueOption("crest_skull", "Skull", skipPlayer = true),
                DialogueOption("crest_varrock", "Varrock", skipPlayer = true),
                DialogueOption("crest_zamorak", "Zamorak", skipPlayer = true),
                DialogueOption("crest_page1", "More...", skipPlayer = true),
            )

        /**
         * CREST CHECKOUT
         */

        selectCrest(CrestType.ARRAV, "crest_arrav")
        selectCrest(CrestType.ASGARNIA, "crest_asgarnia")
        selectCrest(CrestType.DORGESHUUN, "crest_dorgeshuun")
        selectCrest(CrestType.DRAGON, "crest_dragon")
        selectCrest(CrestType.FAIRY, "crest_fairy")
        selectCrest(CrestType.GUTHIX, "crest_guthix")
        selectCrest(CrestType.HAM, "crest_ham")
        selectCrest(CrestType.HORSE, "crest_horse")
        selectCrest(CrestType.JOGRE, "crest_jogre")
        selectCrest(CrestType.KANDARIN, "crest_kandarin")
        selectCrest(CrestType.MISTHALIN, "crest_misthalin")
        selectCrest(CrestType.SARADOMIN, "crest_saradomin")
        selectCrest(CrestType.SKULL, "crest_skull")
        selectCrest(CrestType.VARROCK, "crest_varrock")
        selectCrest(CrestType.ZAMORAK, "crest_zamorak")

        label("crest_buy")
            exec { player, _ ->
                val crest = getAttribute(player, ATTRIBUTE_CREST_TEMP, CrestType.ASGARNIA)
                if (crest.eligible(player) && removeItem(player, Item(Items.COINS_995, crest.cost))) {
                    player.houseManager.crest = crest
                    if (crest == CrestType.SARADOMIN && !player.achievementDiaryManager.getDiary(DiaryType.FALADOR).isComplete(2, 1)) {
                        player.achievementDiaryManager.getDiary(DiaryType.FALADOR).updateTask(player, 2, 1, true)
                    }
                    loadLabel(player, "crest_buy_ok")
                } else {
                    loadLabel(player, "crest_buy_fail")
                }
            }

        label("crest_buy_ok")
            manual { player, _ ->
                val crest = getAttribute(player, ATTRIBUTE_CREST_TEMP, CrestType.ASGARNIA)
                val expression = if (crest == CrestType.SKULL) {
                    ChatAnim.AFRAID
                } else {
                    ChatAnim.HAPPY
                }
                val messages = when (crest) {
                    CrestType.ARRAV ->
                        arrayOf("Ah yes, the shield that you helped to retrieve. You have",
                            "certainly earned the right to wear its symbol.")
                    CrestType.ASGARNIA ->
                        arrayOf("Ah, splendid, splendid. There is no better symbol",
                            "than that of our fair land!")
                    CrestType.DORGESHUUN ->
                        arrayOf("Ah yes, our new neighbours under Lumbridge. I hear",
                            "you were the one who made contact with them, jolly good.")
                    CrestType.DRAGON ->
                        arrayOf("I see you are a mighty dragon-slayer! You have",
                            "certainly earned the right to wear a dragon symbol.")
                    CrestType.FAIRY ->
                        arrayOf("Hmm, mmm, yes, everyone likes pretty fairies.")
                    CrestType.GUTHIX ->
                        arrayOf("Guthix, god of balance! I'm a Saradominist myself,",
                            "you know, but we all find meaning in our own way, what?")
                    CrestType.HAM ->
                        arrayOf("Hmm, I'm not sure I like that HAM group, their",
                            "beliefs are a little extreme for me.", "But if that's what you want.")
                    CrestType.HORSE ->
                        arrayOf("Ah, I see you've brought a toy horse for me to see. An",
                            "interesting beast. Certainly you can use that as your",
                            "crest if you like, although it seems a bit strange to me.")
                    CrestType.JOGRE ->
                        arrayOf("A Jungle Ogre, eh? Odd beast, very odd.")
                    CrestType.KANDARIN ->
                        arrayOf("Our neighbours in the west? Very good, very good.")
                    CrestType.MISTHALIN ->
                        arrayOf("Ah, the fair land of Lumbridge and Varrock.")
                    CrestType.SARADOMIN ->
                        arrayOf("Ah, the great god Saradomin! May he smile on your house",
                            "as you adorn it with his symbol!")
                    CrestType.SKULL ->
                        arrayOf("Of, of course you can have a skull symbol, ${if (player.isMale) "sir" else "madam"}!")
                    CrestType.VARROCK ->
                        arrayOf("Ah, Varrock, a fine city!")
                    CrestType.ZAMORAK ->
                        arrayOf("The god of Chaos? It is a terrible thing to worship",
                            "that evil being. But if that is what you wish...")
                    else ->
                        arrayOf("This shouldn't be happening. Please report this.")
                }
                interpreter!!.sendDialogues(npc, expression, *messages)
            }

        label("crest_buy_fail")
            manual { player, _ ->
                val crest = getAttribute(player, ATTRIBUTE_CREST_TEMP, CrestType.ASGARNIA)
                val messages = when (crest) {
                    CrestType.ARRAV ->
                        arrayOf("But that legendary shield is still lost! I don't think",
                            "it would be proper for you to wear its symbol.")
                    CrestType.DORGESHUUN ->
                        arrayOf("Hmm, have you ever even met the Dorgeshuun? I don't",
                            "think you should wear their symbol until you have",
                            "made contact with that lost tribe.")
                    CrestType.DRAGON ->
                        arrayOf("When the dragon on Crandor Isle remains undefeated? I",
                            "think you should prove yourself a dragon-slayer before",
                            "you can wear a dragon symbol!")
                    CrestType.FAIRY ->
                        arrayOf("A fairy? Fairies are rumoured to exist in a lost city",
                            "somewhere. I don't think I should let you use them as",
                            "a symbol until you have met them in person.")
                    CrestType.GUTHIX, CrestType.SARADOMIN, CrestType.ZAMORAK ->
                        arrayOf("You do not seem to be very devoted to any god.",
                            "I will not let you have a divine symbol",
                            "unless you have level 70 prayer.")
                    CrestType.HORSE ->
                        arrayOf("A horse? I know people talk about them, but I'm not at",
                            "all sure they ever existed. I don't think I could let",
                            "you use as your symbol unless you can fetch me some kind",
                            "of model of one.")
                    CrestType.SKULL ->
                        arrayOf("A symbol of death? You do not seem like a killer to me;",
                            "perhaps some other symbol would suit you better.")
                    else ->
                        arrayOf("This shouldn't be happening. Please report this.")
                }
                interpreter!!.sendDialogues(npc, ChatAnim.NEUTRAL, *messages)
            }
            goto("crest_page1")

        // Money crest has a unique flow since Sir Renitee needs to check the player's cash stack a second time
        label("crest_money")
            npc(ChatAnim.NEUTRAL, "You wish to represent yourself by a moneybag?",
                "I think to make that meaningful I should increase", "the price to 500,000 coins. Do you agree?")
            options(
                DialogueOption("money_accept", "All right.", expression = ChatAnim.NEUTRAL),
                DialogueOption("money_refuse", "No way!", expression = ChatAnim.NEUTRAL),
            )

        label("money_accept")
            exec { player, _ ->
                if (CrestType.MONEY.eligible(player) && removeItem(player, Item(Items.COINS_995, CrestType.MONEY.cost))) {
                    player.houseManager.crest = CrestType.MONEY
                    loadLabel(player, "money_ok")
                } else {
                    loadLabel(player, "no_money")
                }
            }

        label("money_ok")
            npc(ChatAnim.NEUTRAL, "Thank you very much! You may now use a money-bag", "as your symbol.")

        label("money_refuse")
            npc(ChatAnim.NEUTRAL, "Well we can't have just any pauper using a money-bag",
                "as a symbol, can we? You'll have to choose a", "different symbol.")
            goto("crest_page1")

        /**
         * PAINTING DIALOGUE
         */

        label("painting")
            npc(ChatAnim.NEUTRAL, "Would you like a portrait or an, mmm, a landscape? Or", "a map, maybe?")
            goto("painting_options")

        label("painting_options")
            options(
                DialogueOption("portrait", "A portrait", "A portrait please.", expression = ChatAnim.NEUTRAL),
                DialogueOption("landscape", "A landscape", "A landscape please.", expression = ChatAnim.NEUTRAL),
                DialogueOption("map", "A map", "A map please.", expression = ChatAnim.NEUTRAL),
            )

        /**
         * PAINTING MENUS
         */

        label("portrait")
            npc(ChatAnim.NEUTRAL, "Mmm, well, there are a few portraits I can paint. I can",
                "only let you have one if you've got some connection with", "that person though. Who would you like?")
            options(
                DialogueOption("portrait_arthur", "King Arthur", skipPlayer = true),
                DialogueOption("portrait_elena", "Elena of Ardougne", skipPlayer = true),
                DialogueOption("portrait_alvis", "King Alvis of Keldagrim", skipPlayer = true),
                DialogueOption("portrait_misc", "The Prince and Princess of Miscellania", skipPlayer = true),
            )

        label("landscape")
            npc(ChatAnim.NEUTRAL, "Mmm, well, I can paint a few places. Where have you had", "your adventures?")
            options(
                DialogueOption("landscape_lumbridge", "The River Lum", skipPlayer = true),
                DialogueOption("landscape_desert", "The Kharid desert", skipPlayer = true),
                DialogueOption("landscape_morytania", "Morytania", skipPlayer = true),
                DialogueOption("landscape_karamja", "Karamja", skipPlayer = true),
                DialogueOption("landscape_isafdar", "Isafdar", skipPlayer = true),
            )

        label("map")
            npc(ChatAnim.NEUTRAL, "Mmm, yes, ah, I have painted maps of the known world on",
                "several different sizes of parchment. Which size would", "you like?")
            options(
                DialogueOption("map_small", "Small", skipPlayer = true),
                DialogueOption("map_medium", "Medium", skipPlayer = true),
                DialogueOption("map_large", "Large", skipPlayer = true),
            )

        /**
         * PAINTING CHECKOUT
         */

        selectPainting(PaintingType.ARTHUR, "portrait_arthur")
        selectPainting(PaintingType.ELENA, "portrait_elena")
        selectPainting(PaintingType.ALVIS, "portrait_alvis")
        selectPainting(PaintingType.MISC, "portrait_misc")
        selectPainting(PaintingType.DESERT, "landscape_desert")
        selectPainting(PaintingType.ISAFDAR, "landscape_isafdar")
        selectPainting(PaintingType.KARAMJA, "landscape_karamja")
        selectPainting(PaintingType.LUMBRIDGE, "landscape_lumbridge")
        selectPainting(PaintingType.MORYTANIA, "landscape_morytania")
        selectPainting(PaintingType.MAP_SMALL, "map_small")
        selectPainting(PaintingType.MAP_MEDIUM, "map_medium")
        selectPainting(PaintingType.MAP_LARGE, "map_large")

        label("painting_buy")
            exec { player, _ ->
                val painting = getAttribute(player, ATTRIBUTE_PAINTING_TEMP, PaintingType.MAP_SMALL)
                if (painting.eligible(player)) {
                    loadLabel(player, "painting_show_cost")
                } else {
                    loadLabel(player, "painting_buy_fail")
                }
            }

        label("painting_show_cost")
            manual { player, _ ->
                val painting = getAttribute(player, ATTRIBUTE_PAINTING_TEMP, PaintingType.MAP_SMALL)
                interpreter!!.sendDialogues(npc, ChatAnim.NEUTRAL, "That will be, mmm, ${painting.cost} coins please.")
            }
            exec { player, _ ->
                val painting = getAttribute(player, ATTRIBUTE_PAINTING_TEMP, PaintingType.MAP_SMALL)
                if (inInventory(player, Items.COINS_995, painting.cost)) {
                    loadLabel(player, "painting_buy_confirm")
                } else {
                    loadLabel(player, "no_money")
                }
            }

        label("painting_buy_confirm")
            options(
                DialogueOption("painting_accept", "All right", skipPlayer = true),
                DialogueOption("painting_refuse", "No thanks", skipPlayer = true),
            )

        label("painting_buy_fail")
            manual { player, _ ->
                val painting = getAttribute(player, ATTRIBUTE_PAINTING_TEMP, PaintingType.MAP_SMALL)
                val messages = when (painting) {
                    PaintingType.ARTHUR ->
                        arrayOf("Do you have, mmm, a connection with King Arthur? He",
                            "wouldn't like me to just give his picture to anyone.")
                    PaintingType.ELENA ->
                        arrayOf("The last I heard, Elena was, mmm, trapped in West",
                            "Ardougne. I wouldn't feel right selling her portrait",
                            "while she was in danger.")
                    PaintingType.ALVIS ->
                        arrayOf("Have you ever been to Keldagrim? I think I'd need you",
                            "to jog my memory...")
                    PaintingType.MISC ->
                        arrayOf("Do you have some connection with the prince and",
                            "princess? I wouldn't want to give out their picture to",
                            "just anyone?")
                    PaintingType.DESERT ->
                        arrayOf("Mmm, I'm not sure you've had enough adventures in the",
                            "desert to deserve a painting of it.")
                    PaintingType.ISAFDAR ->
                        arrayOf("Mmm, I'm not sure you've had enough adventures in",
                            "Isafdar to deserve a painting of it.")
                    PaintingType.KARAMJA ->
                        arrayOf("Mmm, I'm not sure you've had enough adventures in",
                            "Karamja to deserve a painting of it.")
                    PaintingType.LUMBRIDGE ->
                        arrayOf("Mmm, I'm not sure you've had enough adventures in",
                            "Lumbridge to deserve a painting of it.")
                    PaintingType.MORYTANIA ->
                        arrayOf("Mmm, I'm not sure you've had enough adventures in",
                            "Morytania to deserve a painting of it.")
                    PaintingType.MAP_SMALL, PaintingType.MAP_MEDIUM, PaintingType.MAP_LARGE ->
                        arrayOf("Mmm, I'm not sure you've had enough adventures in the",
                            "world to deserve that map.")
                }
                interpreter!!.sendDialogues(npc, ChatAnim.NEUTRAL, *messages)
            }
            manual { player, _ ->
                val painting = getAttribute(player, ATTRIBUTE_PAINTING_TEMP, PaintingType.MAP_SMALL)
                val messages = when (painting) {
                    PaintingType.ARTHUR ->
                        arrayOf("To buy the portrait of King Arthur you must have",
                            "completed The Holy Grail.")
                    PaintingType.ELENA ->
                        arrayOf("To buy the portrait of Elena you must have completed",
                            "the Plague City quest.")
                    PaintingType.ALVIS ->
                        arrayOf("To buy the portrait of the Giant Dwarf"/*sic*/+" you must have",
                            "completed The Giant Dwarf.")
                    PaintingType.MISC ->
                        arrayOf("To buy the portrait of the Prince and Princess of",
                            "Miscellania you must have completed The Throne of",
                            "Miscellania.")
                    PaintingType.DESERT ->
                        arrayOf("To buy the painting of the desert you must have",
                            "completed Tourist Trap, The Feud, and The Golem.")
                    PaintingType.ISAFDAR ->
                        arrayOf("To buy the painting of Isafdar you must have completed",
                            "Roving Elves.")
                    PaintingType.KARAMJA ->
                        arrayOf("To buy the painting of Karamja you must have completed",
                            "Pirate's Treasure, Tai Bwo Wannai Trio, and Shilo Village.")
                    PaintingType.LUMBRIDGE ->
                        arrayOf("To buy the painting of the Lum you must have completed",
                            "Cook's Assistant, Rune Mysteries, and The Restless Ghost.")
                    PaintingType.MORYTANIA ->
                        arrayOf("To buy the painting of Morytania you must have completed",
                            "Shades of Mort'ton, The Creature of Fenkenstrain,",
                            "Ghosts Ahoy, and The Haunted Mine.")
                    PaintingType.MAP_SMALL ->
                        arrayOf("To buy a small map you must have 51 Quest Points.")
                    PaintingType.MAP_MEDIUM ->
                        arrayOf("To buy a medium map you must have 101 Quest Points.")
                    PaintingType.MAP_LARGE ->
                        arrayOf("To buy a large map you must have 151 Quest Points.")
                }
                interpreter!!.sendDialogue(*messages)
            }
            goto("painting_reset")

        label("painting_accept")
            exec { player, _ ->
                val painting = getAttribute(player, ATTRIBUTE_PAINTING_TEMP, PaintingType.MAP_SMALL)
                if (removeItem(player, Item(Items.COINS_995, painting.cost))) {
                    addItemOrDrop(player, painting.paintingId)
                }
            }
            npc(ChatAnim.NEUTRAL, "There you go. Would you like another painting?")
            goto("painting_options")

        label("painting_refuse")
            npc(ChatAnim.NEUTRAL, "Well, mmm, maybe a different painting, mmm?")
            goto("painting_options")

        label("painting_reset")
            npc(ChatAnim.NEUTRAL, "Would you like a different painting?")
            options(
                DialogueOption("portrait", "Yes - a portrait", "A portrait please.", expression = ChatAnim.NEUTRAL),
                DialogueOption("landscape", "Yes - a landscape", "A landscape please.", expression = ChatAnim.NEUTRAL),
                DialogueOption("map", "Yes - a map", "A map please.", expression = ChatAnim.NEUTRAL),
                DialogueOption("nothing", "No, thanks.", expression = ChatAnim.NEUTRAL),
            )

    }

}