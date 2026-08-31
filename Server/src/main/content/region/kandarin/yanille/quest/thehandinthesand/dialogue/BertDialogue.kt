package content.region.kandarin.yanille.quest.thehandinthesand.dialogue

import content.data.Quests
import content.region.kandarin.yanille.quest.thehandinthesand.quest.TheHandintheSand
import core.ServerStore
import core.api.addItem
import core.api.freeSlots
import core.api.getAttribute
import core.api.getQuestStage
import core.api.hasAnItem
import core.api.hasLevelStat
import core.api.inBank
import core.api.inInventory
import core.api.openDialogue
import core.api.removeItem
import core.api.setAttribute
import core.api.setQuestStage
import core.game.dialogue.DialogueLabeller
import core.game.dialogue.DialogueOption
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.RandomFunction
import org.rs09.consts.Items
import org.rs09.consts.NPCs

/**
 * Dialogue between Bert and the player for The Hand in the Sand quest.
 * @author Edith
 */

@Initializable
class BertDialogue(player: Player? = null) : DialoguePlugin(player) {
    override fun newInstance(player: Player): DialoguePlugin {
        return BertDialogue(player)
    }


    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player, BertDialogueFile(), npc)
        return false
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.BERT_3108)
    }
}

class BertDialogueFile : DialogueLabeller() {
    override fun addConversation() {
        exec { player, _ ->
            loadLabel(player, "quest_stage_" + getQuestStage(player, Quests.THE_HAND_IN_THE_SAND))
        }

        label("quest_stage_0")
            npc(FacialExpression.SCARED, "Eeee, wha' shall I do! I'll mos' certainly lose tha job...")
            player(FacialExpression.ASKING, "Lose your job? What's wrong, why?")
            npc(FacialExpression.SCARED, "I w-w-work... over yon sand pit... and weeell... I found... this... hand! T'were buried in't Sand!")
            options(
                DialogueOption("nowhere", "Oh, you found a hand in the sand - that's nice for you.", "Oh, you found a hand in the sand - that's nice for you."),
                DialogueOption("eew_a_hand", "Eww a hand, in the sand! Why haven't you told the authorities?", "Eww a hand, in the sand! Why haven't you told the authorities?", FacialExpression.AMAZED_TALKING)
            )

        label("eew_a_hand")
            npc(FacialExpression.SAD_TALKING, "They's no' wha' they once was. Tha cap'ain o'the Guard spends near all o'the time drunk in yon pub.")
            player(FacialExpression.ASKING, "Oh? The Guard Captain is drunk in the pub you say? That's not good, what will you do?")
            npc(FacialExpression.HALF_THINKING, "Weeellll... do yer think yer could 'elp me?")
            options(
                DialogueOption("nowhere", "I want no part in this!", "I want no part in this!"),
                DialogueOption("sure_give_hand", "Sure, I'll give you a hand.", "Sure, I'll give you a hand.", FacialExpression.LAUGH)
            )

        label("sure_give_hand")
            exec { player, _ ->
                if (!hasLevelStat(player, Skills.THIEVING, 17) ||
                    !hasLevelStat(player, Skills.CRAFTING, 49)
                ) {
                    loadLabel(player, "missing_skill_requirements")
                }
            }
            npc(FacialExpression.AMAZED_TALKING, "..... Nae, ye can 'ave the 'and as h'evidence.")
            exec { player, _ ->
                if (freeSlots(player) < 1) {
                    loadLabel(player, "no_space_sandyhand")
                }
            }
            item(Item(Items.SANDY_HAND_6945), "Bert gives you a rather smelly, sand covered hand.")
            npc(FacialExpression.SAD_TALKING, "P'raps tha smell will get t'Guard Cap'ain's nose out o'his beer fer 2 seconds!")
            exec { player, _ ->
                if (addItem(player, Items.SANDY_HAND_6945)) {
                    setQuestStage(player, Quests.THE_HAND_IN_THE_SAND, TheHandintheSand.STAGE_QUEST_STARTED_5)
                }
            }

        label("missing_skill_requirements")
            //TODO: Placeholder, find authentic requirement message.
            line("You do not meet the requirements to start 'The Hand in the Sand'.")

        label("no_space_sandyhand")
            npc("I's would be givin' yer tha 'and, but yer can no' be carryin' it. Come back when ye can.")

        label("quest_stage_5")
            npc("Did ye see yon Guard Capt'n 'bout hand?")
            exec { player, _ ->
                if (hasAnItem(player, Items.SANDY_HAND_6945).exists()) {
                    loadLabel(player, "has_sandy_hand")
                } else {
                    loadLabel(player, "lost_sandy_hand")
                }
            }

        label("has_sandy_hand")
            player("Not at the moment, but I will be seeing him soon.")

        label("lost_sandy_hand")
            player("Err, I kind of... lost my grip on it...")
            exec { player, _ ->
                if (freeSlots(player) < 1) {
                    loadLabel(player, "no_space_losthand")
                }
            }
            item(Item(Items.SANDY_HAND_6945), "Bert gives you a rather smelly, sand covered hand.")
            npc("Thank t'gods! I tho' I searched up another when I been pickin' this'un up outside... Take tha' blasted thing to yon Guard Captain quick sharp.")
            player("Thanks Bert, I'll go see the Guard Captain right now.")
            exec { player, _ ->
                addItem(player, Items.SANDY_HAND_6945)
            }

        label("no_space_losthand")
            npc("Tha's no' surprisin' with yer bag so full, come back when s'not.")

        label("quest_stage_10")
            npc("Did ye see yon Guard Capt'n 'bout hand?")
            exec { player, _ ->
                if (!hasAnItem(player, Items.BEER_SOAKED_HAND_6946).exists()) {
                    loadLabel(player, "lost_beersoaked_hand")
                }
            }
            player("Yes, the Guard Captain said to see the wizards in the guild.")
            npc("So why you hangin' abou' 'ere then? Go ring t'bell at t'mage guild just over yonder!")

        label("lost_beersoaked_hand")
            player("It seems to have slipped through my fingers!")
            exec { player, _ ->
                if (freeSlots(player) < 1) {
                    loadLabel(player, "no_space_beersoakedhand")
                }
            }
            item(Item(Items.BEER_SOAKED_HAND_6946), "Player receives beer-soaked hand.")
            exec { player, _ ->
                addItem(player, Items.BEER_SOAKED_HAND_6946)
            }
            npc("Thank t'gods! I tho' I was bein invaded by undead 'ands from t'sand when I found this'un! Take i' to tha mage guild and ring tha' bell to talk ta someone.")
            player("Thanks Bert, I'm off to see the wizards, the wonderful wizards of Yanille!")

        label("no_space_beersoakedhand")
            npc("Tha's no' surprisin' with yer bag so full, come back when s'not.")

        label("quest_stage_15")
            npc(FacialExpression.ASKING, "Wha' info ye find 'bout hand, @name?")
            player("I dug up quite a lot about the hand. Can you tell me about your job?")
            npc(FacialExpression.HAPPY, "Sand! Lots o' sand! Me boss be Sandy o' Sandy's Sand Corp based in Brimhaven on tha isle of Karamja an' I hauls sand fr' there to yon sand pit.")
            npc(FacialExpression.HAPPY, "I's looong harrrrd hours, bu' keeps me busy, y'know what tha say! 'Idle hands'r Zamorak's tools.'")
            player(FacialExpression.ASKING, "So you're employed by Sandy's Sand Corp in Brimhaven. Have you changed your hours recently?")
            exec { player, _ ->
                if (freeSlots(player) < 1) {
                    loadLabel(player, "no_space_bertsrota")
                } else {
                    addItem(player, Items.BERTS_ROTA_6947)
                    setQuestStage(player, Quests.THE_HAND_IN_THE_SAND, TheHandintheSand.STAGE_SANDYS_ROTA_20)
                }
            }
            npc("Nae! See fer yersel', here's a copy o' me rota tha' be held a' head office - yer can looksee iffin ye talk t' Sandy, me boss.")
            player(FacialExpression.HAPPY, "Thanks for the Rota Bert. I'll go check for the original with Sandy in Brimhaven.")

        label("no_space_bertsrota")
            npc("Yer coul' see fer yerself iffen yer had space in yorn invent'ry, come back when yer do.")

        label("quest_stage_20")
            // Split because a long player @name can make the text clip the dialogue box.
            npc("Ey'up @name. Did yer see Sandy in Brimhaven", "'bout me rota?")
            exec { player, _ ->
                if (hasAnItem(player, Items.BERTS_ROTA_6947).exists()) {
                    loadLabel(player, "has_berts_rota")
                } else {
                    loadLabel(player, "lost_berts_rota")
                }
            }

        label("has_berts_rota")
            player("No, I'll fit it in my schedule somewhere soon.")

        label("lost_berts_rota")
            player("Err, no, I kind of... lost it...")
            exec { player, _ ->
                if (freeSlots(player) < 1) {
                    loadLabel(player, "no_space_lostrota")
                } else {
                    addItem(player, Items.BERTS_ROTA_6947)
                }
            }
            npc("Lucky fer yorn tha' I's made a copy then ain't it, 'ere 'ave another.")

        label("no_space_lostrota")
            npc("Lucky fer yorn tha' I's made a copy then ain't it, I's 'ave been given it to yer iffen you 'ad some space in yer invent'ry.")
            player("What did you say?")
            npc("Is you stupid? I said I'd give ye another but yer bags be full!")

        label("quest_stage_25")
            exec { player, _ ->
                when {
                    !inInventory(player, Items.BERTS_ROTA_6947) -> loadLabel(player, "lost_berts_rota")
                    !inInventory(player, Items.SANDYS_ROTA_6948) -> loadLabel(player, "missing_sandys_rota")
                    else -> loadLabel(player, "compare_both_rotas")
                }
            }

        label("missing_sandys_rota")
            line("Maybe you should have the rota from Sandy's desk with you before confronting Bert.")

        label("compare_both_rotas")
            player(FacialExpression.SAD_TALKING, "I managed to get a copy of the original rota. Your hours changed a week ago!")
            npc(FacialExpression.AMAZED, "Nae! Nae! I din't remember tha', bu'... hmmm, aye... tha' migh' be it...")
            player(FacialExpression.ASKING, "What? Give me a hand here, I'm having a hard time understanding how you don't remember changing hours!")
            npc("I's all be tha wizard's fault! Tha magic leaks fr'm yon magic guild I tells yer! Tha's why this weirrrrd scroll appeareded a week ago!")
            player(FacialExpression.ASKING, "A scroll appeared? Can I take a look at it while you look at the rotas?")
            exec { player, _ ->
                removeItem(player, Items.BERTS_ROTA_6947)
                removeItem(player, Items.SANDYS_ROTA_6948)
                addItem(player, Items.A_MAGIC_SCROLL_6949)
                setQuestStage(player, Quests.THE_HAND_IN_THE_SAND, TheHandintheSand.STAGE_MAGIC_SCROLL_30)
            }
            npc(FacialExpression.HAPPY, "O'course @name, le's be 'avin'yon rota and 'ere be tha scroll, yer be takin' it back ta those inferrrnal wizards quick sharp!")

        label(
            "quest_stage_30",
            "quest_stage_35",
            "quest_stage_40",
            "quest_stage_45",
            "quest_stage_50",
            "quest_stage_55",
            "quest_stage_60",
            "quest_stage_65",
            "quest_stage_70",
            "quest_stage_75"
        )
            npc("I be hopin' tha search is goin' well... are tha wizard's owning up ta anythin' yet?")
            exec { player, _ ->
                if (getQuestStage(player, Quests.THE_HAND_IN_THE_SAND) == TheHandintheSand.STAGE_MAGIC_SCROLL_30 &&
                    !hasAnItem(player, Items.A_MAGIC_SCROLL_6949).exists()
                ) {
                    loadLabel(player, "lost_magic_scroll")
                } else {
                    loadLabel(player, "has_magic_scroll")
                }
            }

        label("lost_magic_scroll")
            player("Err, no, I kind of... lost the scroll...")
            exec { player, _ ->
                if (freeSlots(player) < 1) {
                    loadLabel(player, "no_space_scroll")
                } else {
                    addItem(player, Items.A_MAGIC_SCROLL_6949)
                }
            }
            npc("I's be seein' ya drop it on tha' way out, 'ere 'tis.")

        label("no_space_scroll")
            npc("I's be seein' ya drop it on tha' way out, I's 'ave been given it to yer iffen you 'ad some space in yer invent'ry.")

        label("has_magic_scroll")
            player("I've found out a lot and will let you know when it's all over.")

        label("quest_stage_100")
            exec { player, _ ->
                if (getAttribute(player, TheHandintheSand.ATTRIBUTE_BERT_POST_QUEST, false)) {
                    loadLabel(player, "collect_daily_sand")
                }
            }
            player("Bert! Good news!")
            npc("Arrr...Good news be always handy.")
            player("They arrested Sandy for the murder of a wizard and the sand pit now refills itself!")
            npc("ME JOB! I'VE LOSTED ME JOB! 'ow c'n yer say", "tha' be good news?? Me wife'll tear me limb fr'm limb!")
            player("Don't worry, the Wizards are going to pay you a large pension so that you can retire...")
            npc("Bu' wha'll I be doin' wit' me day now! I be lovin' tha sand.")
            player("What will you do with your day? Well.... You could build sand castles with your own two hands!")
            npc("I din't think so... bu' iffen yer ever need someone ta haul buckets o'sand 'round, ye be lettin' me know @name, I's can help yer!")
            player("Wow! That would be great, buckets of sand direct to my bank, everyday you say? That's great!")
            line("Once per day you may ask Bert to help you by carrying 84", "buckets of sand to your bank. Just talk to him!")
            exec { player, _ ->
                setAttribute(player, TheHandintheSand.ATTRIBUTE_BERT_POST_QUEST, true)
            }

        label("collect_daily_sand")
            exec { player, _ ->
                val dailySandAvailable = ServerStore.getNPCItemStock(NPCs.BERT_3108, Items.BUCKET_OF_SAND_1783, 84, player)
                val hasSandBucketStack = inBank(player, Items.BUCKET_OF_SAND_1783)
                val hasFreeBankSlot = player.bank.freeSlots() > 0

                when {
                    dailySandAvailable < 84 -> loadLabel(player, "has_collected_sand")
                    !hasSandBucketStack && !hasFreeBankSlot -> loadLabel(player, "no_bank_space")
                    else -> loadLabel(player, "daily_sand_${RandomFunction.random(1, 5)}")
                }
            }

        label("daily_sand_1")
            npc("Mornin' @name!")
            player("Hello there Bert!")
            npc("I be goin' ou' on a limb and guessin' ye wan's yer sand today!")
            player("Yes, I'd like the sand please Bert.")
            npc("I'll ge' righ' on it!")
            goto("deliver_daily_sand")

        label("daily_sand_2")
            npc("'Ello there @name!")
            player("Hope you're having a good day!")
            npc("Oh arr, no' too bad, though sometoimes I could do wi' a second pair o' hands! I'll be gettin' on movin' them there buckets o' sand to yer bank!")
            player("Thanks Bert, see you tomorrow!")
            goto("deliver_daily_sand")

        label("daily_sand_3")
            npc("I thought i' were you comin' in @name. I sposin' you be wantin' yer sand today?")
            player("Aye...I mean, yes please Bert!")
            npc("I'll ge' on wit' movin' it. Thankee fer makin' sure Sandy go' it in t'neck fer 'is double dealin's!")
            player("Excellent! While you move the sand to my bank, I'll do something else, see you later!")
            goto("deliver_daily_sand")

        label("daily_sand_4")
            npc("'Ello again @name!")
            player("Hello Bert, could I have my sand today please?")
            npc("Aye, give me a minute cause I be rushed off me feet!")
            player("Thanks for the sand Bert!")
            goto("deliver_daily_sand")

        label("deliver_daily_sand")
            exec { player, _ ->
                if (player.bank.add(Item(Items.BUCKET_OF_SAND_1783, 84))) {
                    ServerStore.addNPCItemAmount(NPCs.BERT_3108, Items.BUCKET_OF_SAND_1783, 84, player, 84)
                }
            }

        label("no_bank_space")
            npc("Eeee, ye've go' no free space in yon bank, @name. Make some room 'tween the fishes 'n boots fore ye come back ta me.")
            player("Okay, I'll make some space in my bank and come back.")

        label("has_collected_sand")
            npc("'Ello there, @name! Hope yer be havin' fun. I be a", "wee bi' busy to 'elp wit' yon sand. Come back tomorra.") // Linebreak to fit long player names.
            player("Okay, Bert. I'll come back for my sand a bit later on.")

    }
}

/**
 * NPC Contact dialogue for Bert.
 */

class BertNPCContactDialogueFile : DialogueLabeller() {
    companion object {
        fun canContact(player: Player): Boolean {
            return getAttribute(player, TheHandintheSand.ATTRIBUTE_BERT_POST_QUEST, false)
        }
    }

    private fun isInDragonInn(player: Player): Boolean {
        val loc = player.location

        val downstairs = loc.z == 0 && loc.x in 2548..2557 && loc.y in 3077..3082
        val upstairs = loc.z == 1 && loc.x in 2551..2557 && loc.y in 3077..3082

        return downstairs || upstairs
    }

    override fun addConversation() {

        player("Hi there, Bert.")
        npc(FacialExpression.ASKING, "Huh? What tha' doin' in marn 'ed?")
        player("This is the voice of your conscience. Hand all your money to @name! Wooo wooo!")
        npc(FacialExpression.SCARED, "Be away wit' ya!")
        player("Just kidding, I'm using magic to talk to you. I'm totally 'armless.")
        exec { player, _ ->
            if (isInDragonInn(player)) {
                loadLabel(player, "player_nearby_bert")
            }
        }
        npc("Oh ar. So wha' do thee want?")
        player("Could you hand some sand into my bank?")
        exec { player, _ ->
            val dailySandAvailable =
                ServerStore.getNPCItemStock(NPCs.BERT_3108, Items.BUCKET_OF_SAND_1783, 84, player)
            val hasSandBucketStack = inBank(player, Items.BUCKET_OF_SAND_1783)
            val hasFreeBankSlot = player.bank.freeSlots() > 0

            when {
                dailySandAvailable < 84 -> loadLabel(player, "has_collected_sand")
                !hasSandBucketStack && !hasFreeBankSlot -> loadLabel(player, "no_bank_space")
                else -> loadLabel(player, "deliver_contact_sand")
            }
        }

        label("player_nearby_bert")
            npc("Oh ar. Hey, then why ya standon' so close? Speak in person please!")

        label("deliver_contact_sand")
            npc("Ah, keep yer 'air on, I'll leg I' to tha bank wit' some sand fer ya.")
            player("Thanks!")
            exec { player, _ ->
                if (player.bank.add(Item(Items.BUCKET_OF_SAND_1783, 84))) {
                    ServerStore.addNPCItemAmount(NPCs.BERT_3108, Items.BUCKET_OF_SAND_1783, 84, player, 84)
                }
            }

        label("has_collected_sand")
            npc(FacialExpression.HAPPY, "I be a wee bi' busy to 'elp wit' yon sand, come back tomorra.")
            player(FacialExpression.HAPPY, "Okay, Bert, I'll come back for my sand a bit later on.")

        label("no_bank_space")
            npc("Eeee, ye've go' no free space in yon bank, @name. Make some room 'tween the fishes 'n boots fore ye come back ta me.")
            player("Okay, I'll make some space in my bank and come back.")
    }
}