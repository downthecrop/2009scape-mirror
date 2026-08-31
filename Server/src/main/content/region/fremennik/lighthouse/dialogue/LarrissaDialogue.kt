package content.region.fremennik.lighthouse.dialogue

import content.data.Quests
import content.region.fremennik.lighthouse.quest.horror.HorrorFromTheDeep.Companion.HFTD_BRIDGE_E
import content.region.fremennik.lighthouse.quest.horror.HorrorFromTheDeep.Companion.HFTD_BRIDGE_W
import content.region.fremennik.lighthouse.quest.horror.HorrorFromTheDeep.Companion.HFTD_LIGHTHOUSE_DOOR
import content.region.fremennik.lighthouse.quest.horror.HorrorFromTheDeep.Companion.HFTD_LIGHTHOUSE_KEY
import core.api.*
import core.game.dialogue.ChatAnim
import core.game.dialogue.DialogueLabeller
import core.game.dialogue.DialogueOption
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs09.consts.NPCs

/**
 * Larrissa, in the Lighthouse north of Barbarian Outpost. Horror From The Deep quest.
 */

class LarrissaDialogue : InteractionListener {
    override fun defineListeners() {
        on(intArrayOf(NPCs.LARRISSA_1336, NPCs.LARRISSA_1337), IntType.NPC, "talk-to") { player, node ->
            DialogueLabeller.open(player, LarrissaDialogueFile(), node.asNpc())
            return@on true
        }
    }
}

class LarrissaDialogueFile : DialogueLabeller() {

    override fun addConversation() {
        exec { player, _ ->
            // quest stage
            val questStage = getQuestStage(player, Quests.HORROR_FROM_THE_DEEP)

            // stage 1 checks
            val key = getVarbit(player, HFTD_LIGHTHOUSE_KEY) == 1
            val bridgeWest = getVarbit(player, HFTD_BRIDGE_W) == 1
            val bridgeEast = getVarbit(player, HFTD_BRIDGE_E) == 1
            val bridge = bridgeEast && bridgeWest

            // stage 2 checks
            val enteredLighthouse = getVarbit(player, HFTD_LIGHTHOUSE_DOOR) == 1

            // choose label
            when (questStage) {
                // quest unstarted
                0 -> loadLabel(player, "s0")
                // getting shit done
                1 -> {
                    if (key && bridge) { // both key and bridge fixed
                        loadLabel(player, "s1_complete")
                    } else if (!key && bridge) { // only bridge, no key
                        loadLabel(player, "s1_bridge_only")
                    } else if (key && !bridge) { // only key, no bridge
                        loadLabel(player, "s1_key_only")
                    } else if (!key && !bridge) { // no key and no bridge
                        loadLabel(player, "s1_neither")
                    }
                }
                // go inside the lighthouse
                2 -> {
                    if (enteredLighthouse) {
                        loadLabel(player, "s2_inside")
                    } else {
                        loadLabel(player, "s2_go_inside")
                    }
                }
                // start fixing the lamp
                3 -> loadLabel(player, "s3")
                // lamp fixed
                4 -> loadLabel(player, "s4")
                // in dag fight
                5 -> loadLabel(player, "s5")
                // quest complete
                100 -> loadLabel(player, "s100")
                else -> loadLabel(player, "end")
            }
        }

        // ---------------------- STAGE 0 STARTING QUEST ----------------------
        label("s0")
        player(ChatAnim.FRIENDLY, "Hello there.")
        npc(ChatAnim.HALF_GUILTY, "Oh, thank Armadyl! I am in such a worry... please help me!")
        options(
            DialogueOption("s0_with_what", "With what?", expression = ChatAnim.FRIENDLY),
            DialogueOption("end", "Sorry, just passing through", expression = ChatAnim.FRIENDLY)
        )

        label("s0_with_what")
        npc(ChatAnim.HALF_GUILTY, "Oh... it is terrible... horrible... My boyfriend lives here in this lighthouse, but I haven't seen him the last few days! I think something terrible has happened!")
        npc(ChatAnim.HALF_GUILTY, "Look, you can see for yourself that the light has gone out, and the front door is locked up tight! He would NEVER do that!")
        npc(ChatAnim.HALF_GUILTY, "With the light off this coastline is terribly dangerous to ships! And to lock the front door so that nobody can turn the light back on?")
        player(ChatAnim.FRIENDLY, "Maybe he just went on holiday or something? Must be pretty boring living in a lighthouse.")
        npc(ChatAnim.HALF_GUILTY, "That is terribly irresponsible! He is far too thoughtful for that! He would never leave it unattended! He would also never leave without telling me!")
        npc(ChatAnim.HALF_GUILTY, "Please, I know something terrible has happened to him... I can sense it! Please... please help me adventurer!")
        options(
            DialogueOption("s0_how_help", "But how can I help?", expression = ChatAnim.FRIENDLY),
            DialogueOption("end", "Sorry, just passing through.", expression = ChatAnim.FRIENDLY)
        )

        label("s0_how_help")
        npc(ChatAnim.HALF_GUILTY, "Well, we have to do something to get the lighthouse working again! Also, as you may have noticed, the storm that knocked the bridge out")
        npc(ChatAnim.HALF_GUILTY, "has trapped me on this causeway! You seem to have got here okay somehow, so if you could go and visit my cousin and get the spare key I left him,")
        npc(ChatAnim.HALF_GUILTY, "as well as fix the bridge enough so that I can go and speak to my family in Rellekka and tell them whats happened, I will be eternally grateful!")
        options(
            DialogueOption("s0_okay_help", "Okay, I'll help!", expression = ChatAnim.FRIENDLY),
            DialogueOption("end", "Sorry, just passing through.", expression = ChatAnim.FRIENDLY)
        )

        label("s0_okay_help")
        npc(ChatAnim.FRIENDLY, "OH! THANK YOU SO MUCH! I know my darling", "would never have left with the lighthouse lights off", "and without even telling me where he's gone!")
        exec { player, _ ->
            setQuestStage(player, Quests.HORROR_FROM_THE_DEEP, 1)
        }
        goto("s0_main_menu")

        label("s0_main_menu")
        options(
            DialogueOption("s0_where_cousin", "Where is your cousin?", expression = ChatAnim.FRIENDLY),
            DialogueOption("s0_fix_bridge", "How can I fix the bridge?", expression = ChatAnim.FRIENDLY),
            DialogueOption("s0_see_what", "I'll see what I can do.", expression = ChatAnim.FRIENDLY)
        )

        label("s0_cousin_menu")
        options(
            DialogueOption("s0_fix_bridge", "How can I fix the bridge?", expression = ChatAnim.FRIENDLY),
            DialogueOption("s0_see_what", "I'll see what I can do.", expression = ChatAnim.FRIENDLY)
        )

        label("s0_bridge_menu")
        options(
            DialogueOption("s0_where_cousin", "Where is your cousin?", expression = ChatAnim.FRIENDLY),
            DialogueOption("s0_see_what", "I'll see what I can do.", expression = ChatAnim.FRIENDLY)
        )

        label("s0_where_cousin")
        npc(ChatAnim.FRIENDLY, "My cousin was always interested in agility. He left our home in Rellekka many moons ago, so that he could pursue this interest.")
        npc(ChatAnim.FRIENDLY, "I don't exactly know where he has gone, but I am sure he went somewhere to practise his agility. If you see him, his name is Gunnjorn. Mention my name, he will recognise it.")
        goto("s0_cousin_menu")

        label("s0_fix_bridge")
        npc(ChatAnim.FRIENDLY, "Well, I am not just some helpless girl! I have pretty good agility, so you will only need to use two planks to make a ledge that I can balance along.")
        npc(ChatAnim.FRIENDLY, "Just use a plank on each side of the bridge. You will need a hammer, and thirty steel nails for each plank you use as well. I believe there are some planks near here...")
        goto("s0_bridge_menu")

        label("s0_see_what")
        npc(ChatAnim.FRIENDLY, "Thank you so much!")
        goto("end")

        // ---------------------- STAGE 1 FIXING BRIDGE AND GETTING KEY ----------------------
        label("s1_complete")
        player(ChatAnim.FRIENDLY, "I've fixed the bridge for you, and I've got your key!")
        npc(ChatAnim.HALF_GUILTY, "Oh, thank you so much!")
        exec { player, _ ->
            // set quest stage 2
            setQuestStage(player, Quests.HORROR_FROM_THE_DEEP, 2)
        }
        npc(ChatAnim.HALF_GUILTY, "Quickly, we must go inside and find out what has happened to my beloved Jossik!")
        goto("end")

        label("s1_bridge_only")
        player(ChatAnim.FRIENDLY, "I've fixed the bridge for you!")
        npc(ChatAnim.HALF_GUILTY, "Oh, thank you so much! Please find the key to the lighthouse for me though! I cannot bear to think that something bad may have happened to my darling Jossik...")
        goto("end")

        label("s1_key_only")
        player(ChatAnim.FRIENDLY, "I've got your key for you!")
        npc(ChatAnim.HALF_GUILTY, "Thank you adventurer, but I need you to fix the bridge for me. The key to the lighthouse is of little comfort while I am trapped here on this causeway!")
        goto("end")

        label("s1_neither")
        player(ChatAnim.FRIENDLY, "Hello again.")
        npc(ChatAnim.HALF_GUILTY, "Oh, please find my darling! I just know something horrible has happened!")
        goto("end")

        // ---------------------- STAGE 2 GO INSIDE ----------------------
        label("s2_go_inside")
        npc(ChatAnim.HALF_GUILTY, "Quickly, we must go inside and find out what has happened to my beloved Jossik!")
        goto("end")

        label("s2_inside")
        npc(ChatAnim.HALF_GUILTY, "This is terrible... What could have happened here? Please, you must fix the light, we cannot let my darling Jossik take the blame for any shipwrecks!")
        player(ChatAnim.FRIENDLY, "Okay, I will see what I can do.")
        exec { player, _ ->
            setQuestStage(player, Quests.HORROR_FROM_THE_DEEP, 3)
        }
        goto("end")

        // ---------------------- STAGE 3 ENTERED LIGHTHOUSE AND FIXING LIGHT ----------------------
        label("s3")
        player(ChatAnim.FRIENDLY, "What am I supposed to be doing again?")
        npc(ChatAnim.HALF_GUILTY, "You MUST find a way to fix the lighthouse! The ships are in terrible danger as long as the light is not working properly!")
        player(ChatAnim.FRIENDLY, "Okay, I will see what I can do.")
        npc(ChatAnim.HALF_GUILTY, "I'm sorry adventurer, I do not know anything about lighthouses.")
        npc(ChatAnim.HALF_GUILTY, "I'm sure there must be some kind of user's guide around here somewhere though.")
        npc(ChatAnim.HALF_GUILTY, "My darling Jossik knew nothing about Lighthouses when he first came here, so the council must have left him some kind of manual or something.")
        goto("end")

        // ---------------------- STAGE 4 FIXED THE LIGHT ----------------------
        label("s4")
        player(ChatAnim.FRIENDLY, "I have managed to fix the light!")
        npc(ChatAnim.HALF_GUILTY, "Excellent work, adventurer!")
        npc(ChatAnim.HALF_GUILTY, "Now you can devote all of your energies to finding out what has happened to my darling Jossik!")
        goto("end")

        // ---------------------- STAGE 5 DAG FIGHT ----------------------
        label("s5")
        player(ChatAnim.FRIENDLY, "I found Jossik! He's in a cavern underground...")
        npc(ChatAnim.HALF_GUILTY, "That is wonderful news! But why does he not come up out of there?")
        player(ChatAnim.FRIENDLY, "Um... well, there's a big monster down there with him stopping him leaving.")
        npc(ChatAnim.HALF_GUILTY, "What?!?! You MUST save him! I beseech you, please! Do something!")
        player(ChatAnim.FRIENDLY, "Okay, okay, sheesh, keep your hair on...")
        goto("end")

        // ---------------------- STAGE 100 COMPLETED QUEST ----------------------
        label("s100")
        npc(ChatAnim.FRIENDLY, "Oh! Thank you so much for all of your help in rescuing my darling Jossik! I do not know what would have happened to him had you not chanced along this path!")
        player(ChatAnim.FRIENDLY, "Don't worry about it, I was glad to help out.")
        goto("end")
    }
}