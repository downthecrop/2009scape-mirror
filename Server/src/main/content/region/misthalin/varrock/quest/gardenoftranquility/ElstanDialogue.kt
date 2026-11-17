package content.region.misthalin.varrock.quest.gardenoftranquility

import content.data.Quests
import content.global.dialogue.GardenerDialoguePlugin
import content.global.skill.farming.Farmers
import content.global.skill.farming.PatchType
import core.api.*
import core.game.dialogue.ChatAnim
import core.game.dialogue.DialogueLabeller
import core.game.dialogue.DialogueOption
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.npc.NPC
import core.game.node.item.Item
import org.rs09.consts.Items

/**
 * ELstan, the allotment patch farmer near Falador.
 */

class ElstanDialogue : InteractionListener {
    override fun defineListeners() {
        on(intArrayOf(Farmers.ELSTAN.id), IntType.NPC, "talk-to") { player, node ->
            DialogueLabeller.open(player, ElstanDialogueFile(), node as NPC)
            return@on true
        }
    }
}

class ElstanDialogueFile : DialogueLabeller() {

    override fun addConversation() {
        // Initial Topics
        options(
            // VB_ELSTAN_STAGE: 0 before agreeing, 1 after agreeing, 2 after planting, 3 after harvesting marigolds, 4 after receiving seeds
            
            // before talking to Elstan
            DialogueOption("quest_ask_seeds", "Do you have any delphinium seeds to spare?", expression = ChatAnim.ASKING) { player, _ ->
                val stage = getQuestStage(player, Quests.GARDEN_OF_TRANQUILITY)
                val varbit = getVarbit(player, GardenOfTranquility.VB_ELSTAN_STAGE)
                stage <= 40 
                        && varbit == 0
            },
            
            // after agreeing and before giving the marigolds
            DialogueOption("quest_remind", "What did I agree to do for delphinium seeds?", expression = ChatAnim.ASKING) { player, _ ->
                val stage = getQuestStage(player, Quests.GARDEN_OF_TRANQUILITY)
                val varbit = getVarbit(player, GardenOfTranquility.VB_ELSTAN_STAGE)
                stage <= 40 
                        && varbit in 1..3
                        && !inInventory(player, Items.MARIGOLDS_6010)
            },
            
            // after harvesting the marigolds
            DialogueOption("quest_marigolds", "I have those marigolds for you.") { player, _ ->
                val stage = getQuestStage(player, Quests.GARDEN_OF_TRANQUILITY)
                val varbit = getVarbit(player, GardenOfTranquility.VB_ELSTAN_STAGE)
                stage <= 40
                        && varbit == 3
                        && inInventory(player, Items.MARIGOLDS_6010)
            },
            
            // after getting the seeds, but having lost them
            DialogueOption("quest_more_seeds", "Can I have some more delphinium seeds?") { player, _ ->
                val stage = getQuestStage(player, Quests.GARDEN_OF_TRANQUILITY)
                val varbit = getVarbit(player, GardenOfTranquility.VB_ELSTAN_STAGE)
                val delphiniumPatch = getVarbit(player, GardenOfTranquility.VB_DELPHINIUM_PATCH)
                stage <= 40
                        && varbit == 4
                        && delphiniumPatch <= 3 // delphiniums not planted yet
                        && !hasAnItem(player, Items.DELPHINIUM_SEED_6457).exists()
            },
            
            // normal farming options
            DialogueOption("chop_tree", "Would you chop my tree down for me?", expression = ChatAnim.ASKING) { player, _ ->
                val patch = Farmers.ELSTAN.patches[0].getPatchFor(player)
                patch.patch.type == PatchType.TREE_PATCH
                        && patch.plantable != null
                        && patch.isGrown()
            },
            DialogueOption("look_after", "Would you look after my crops for me?", expression = ChatAnim.ASKING) { player, _ ->
                val patch = Farmers.ELSTAN.patches[0].getPatchFor(player)
                !(patch.patch.type == PatchType.TREE_PATCH && patch.plantable != null && patch.isGrown())
            },
            DialogueOption("advice", "Can you give me any farming advice?", expression = ChatAnim.ASKING),
            DialogueOption("sell", "Can you sell me something?", expression = ChatAnim.ASKING),
            DialogueOption("end", "That's all, thanks.")
        )

        // Standard Farming Topics
        label("chop_tree")
        exec { player, npc ->
            open(player, GardenerDialoguePlugin(player, 1000), npc)
        }

        label("look_after")
        exec { player, npc ->
            open(player, GardenerDialoguePlugin(player, 10), npc)
        }

        label("advice")
        exec { player, npc ->
            open(player, GardenerDialoguePlugin(player, 2000), npc)
        }

        label("sell")
        exec { player, npc ->
            open(player, GardenerDialoguePlugin(player, 30), npc)
        }

        // Garden of Tranquility
        label("quest_ask_seeds")
        npc("I do have delphinium seeds, yes - but to spare, I'm not so sure. You don't look like that much of a gardener to me.")
        options(
            DialogueOption("no_charos", "I'll have you know I'm a pretty good farmer, actually!", skipPlayer = true),

            // option only shows if you have ring of charos (a) equipped
            DialogueOption("charm_expert1", "[Charm] That is why I have come to an expert for advice.",
                spokenText = "That is why I have come to an expert for advice.") { player, _ ->
                inEquipment(player, Items.RING_OF_CHAROSA_6465)
            }
        )

        label("no_charos")
        player(ChatAnim.ANGRY, "I'll have you know I'm a pretty good farmer, actually!")
        goto("end")

        label("charm_expert1")
        npc(ChatAnim.HAPPY, "Ah, now that I like the sound of. So people are saying I'm an expert, then?")
        options(
            DialogueOption("just_said_that", "No, I just said that to get you to give me some seeds."),
            DialogueOption("carry_on", "Yeah, carry on - milk it for all it's worth."),
            DialogueOption("charm_expert2", "[Charm] Not just AN expert, Elstan - they say you are THE expert.",
                spokenText = "Not just AN expert, Elstan - they say you are THE expert.",
                expression = ChatAnim.HAPPY)
        )

        label("just_said_that")
        npc("Really?!? Well, give me any more of that kind of talk and I'll give you something to remember me by, and I'll tell you now it ain't seeds!!")
        goto("end")

        label("carry_on")
        npc("What?!? Any more of that talk and it'll be curtains for you, mate!")
        goto("end")

        label("charm_expert2")
        npc(ChatAnim.HAPPY, "I can't say that I'm surprised, really - I do seem to have the golden touch with the old gardening lark. Only just the other day I managed to grow a - I'm not boring you, am I?")
        options(
            DialogueOption("please_stop", "Yes, please stop right now."),
            DialogueOption("charm_stories", "[Charm] Oh no, I love listening to gardening stories...",
                spokenText = "Oh no, I love listening to gardening stories...",
                expression = ChatAnim.SUSPICIOUS)
        )

        label("please_stop")
        npc(ChatAnim.HAPPY, "As a matter of fact I will! I'm not wasting my time on the likes of you!")
        goto("end")

        label("charm_stories")
        npc("You do! That's the first person I've ever met has said that! Well, don't you worry mate, I've got millions of 'em!")
        options(
            DialogueOption("kill_me", "Somebody kill me now."), // please lol
            DialogueOption("charm_millions", "[Charm] Millions? Ah, just what I wanted to hear...",
                spokenText = "Millions? Ah, just what I wanted to hear...",
                expression = ChatAnim.SUSPICIOUS)
        )

        label("kill_me")
        goto("end")

        label("charm_millions")
        npc(ChatAnim.HAPPY, "Ah, but there's plenty of time for stories. You wanted delphinium seeds, did you say?")
        player(ChatAnim.ASKING, "Er ... what ... er...")
        player(ChatAnim.HAPPY, "Yes, delphinium seeds!")
        npc("As I'm in an especially good mood I think I will let you have some. But you have to do something for me first.")
        player(ChatAnim.ASKING, "And what's that?")
        npc("I need some marigolds to give to Lyra, one of my fellow gardeners over in Morytania. She's a bit mysterious, and has some - well - sinister ways to be sure, but she's just my type.")
        goto("marigold_explain")

        label("marigold_explain")
        player("So, a bunch of marigolds then?")
        npc("Yes, and while I'm at it I can see how good a gardener you are. Grow those marigolds in this flower patch here from seed to flower, pick them and give them to me, and I'll give you some delphinium seeds.")
        options(
            DialogueOption("agree_grow", "Okay, I'll grow you some marigolds."),
            DialogueOption("refuse_grow", "No, I don't have time to do that."),
            DialogueOption("charm_just_give", "[Charm] Are you sure you don't just want to give me the seeds now?",
                spokenText = "Are you sure you don't just want to give me the seeds now?")
        )

        label("agree_grow")
        exec { player, _ ->
            setVarbit(player, GardenOfTranquility.VB_ELSTAN_STAGE, 1, true)
        }
        goto("end")

        label("refuse_grow")
        npc("Have it your own way, mate. No seeds for you then.")
        goto("end")

        label("charm_just_give")
        npc("Strange - there is this funny buzzing in my head that says that I should - but I don't think I will. I need those marigolds for Lyra.")
        goto("marigold_explain")

        label("quest_remind")
        npc("I need some marigolds so that I can woo Lyra. Grow them from seed to flower in this flower patch here, pick them and give them to me, and I'll give you some delphinium seeds.")
        goto("end")

        label("quest_marigolds")
        npc(ChatAnim.HAPPY, "Ah, fantastic - Lyra will surely be mine now! Here you go mate, here's those delphinium seeds you were after. Yes, here you go - I hope they grow for you.")
        item(Item(Items.DELPHINIUM_SEED_6457), "Elstan gives you some delphinium seeds.")
        exec { player, _ ->
            if (removeItem(player, Items.MARIGOLDS_6010)) {
                addItemOrDrop(player, Items.DELPHINIUM_SEED_6457, 4)
                setVarbit(player, GardenOfTranquility.VB_ELSTAN_STAGE, 4, true)
            }
        }
        goto("end")

        label("quest_more_seeds")
        npc("Yes, here you go - I hope they grow for you.")
        item(Item(Items.DELPHINIUM_SEED_6457), "Elstan gives you some delphinium seeds.")
        exec { player, _ ->
            addItemOrDrop(player, Items.DELPHINIUM_SEED_6457, 4)
        }
        goto("end")
    }
}