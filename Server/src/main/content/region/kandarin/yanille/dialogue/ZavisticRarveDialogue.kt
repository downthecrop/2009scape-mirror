package content.region.kandarin.yanille.dialogue

import content.data.Quests
import content.region.kandarin.feldip.quest.zogreflesheaters.ZogreFleshEaters
import content.region.kandarin.yanille.handlers.ZavisticRarveBellSpawn
import content.region.kandarin.yanille.quest.thehandinthesand.cutscene.SandpitCutscene
import content.region.kandarin.yanille.quest.thehandinthesand.quest.TheHandintheSand
import core.api.addItem
import core.api.animate
import core.api.closeDialogue
import core.api.delayScript
import core.api.finishQuest
import core.api.freeSlots
import core.api.getAttribute
import core.api.getQuestStage
import core.api.hasAnItem
import core.api.inInventory
import core.api.isQuestInProgress
import core.api.lock
import core.api.openDialogue
import core.api.playGlobalAudio
import core.api.queueScript
import core.api.removeItem
import core.api.resetAnimator
import core.api.setAttribute
import core.api.setQuestStage
import core.api.setVarbit
import core.api.stopExecuting
import core.api.teleport
import core.api.unlock
import core.api.visualize
import core.game.dialogue.DialogueBuilder
import core.game.dialogue.DialogueLabeller
import core.game.dialogue.DialogueOption
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.interaction.QueueStrength
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.plugin.Initializable
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import org.rs09.consts.Sounds

private const val ATTRIBUTE_MET_ZAVISTIC_RARVE = "/save:met_zavistic_rarve"
private const val CAST_SPELL_ANIM_711 = 711
private const val TELEPORT_SPELL_ANIM_714 = 714
private val TELEPORT_SPELL_GFX_308 = Graphics(308, 100, 50)

/**
 * Dialogue for Zavistic Rarve, including default Wizards Guild dialogue,
 * Zogre Flesh Eaters routing and The Hand in the Sand quest dialogue.
 */

@Initializable
class ZavisticRarveDialogue(player: Player? = null) : DialoguePlugin(player) {
    override fun newInstance(player: Player): DialoguePlugin {
        return ZavisticRarveDialogue(player)
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        ZavisticRarveBellSpawn.resetDespawnTimer(npc, player)
        openDialogue(player, ZavisticRarveDialogueFile(), npc)
        return false
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.ZAVISTIC_RARVE_2059)
    }
}

/**
 * Label-based dialogue router for Zavistic Rarve.
 * The default guild options live here while Zogre Flesh Eaters routes back
 * into its legacy dialogue file without replaying Zavistic's intro.
 */

class ZavisticRarveDialogueFile(private val viaBell: Boolean = false, private val startLabel: String? = null) : DialogueLabeller() {
    companion object {
        fun dialogueInitialTalk(builder: DialogueBuilder): DialogueBuilder {
            return builder
                .npcl("What are you doing...Oh, it's you...sorry...didn't realise...what can I do for you?")
        }

        fun defaultTalk(continueBuilder: DialogueBuilder) {
            continueBuilder.let { builder ->
                val returnJoin = builder.placeholder()

                returnJoin.builder()
                    .options()
                    .let { optionBuilder ->
                        optionBuilder.option_playerl("What is there to do in the Wizards' Guild?")
                            .npcl("This is the finest wizards' establishment in the land.")
                            .npcl("We have magic portals to the other towers of wizardry around Gielinor.")
                            .npcl("We have a particularly wide collection of runes in our rune shop.")
                            .npcl("We sell some of the finest mage robes in the land and we have a training area full of zombies for you to practice your magic on.")
                            .goto(returnJoin)

                        optionBuilder.option_playerl("What are the requirements to get in the Wizards' Guild?")
                            .npcl("You need a magic level of 66, the high magic energy level is too dangerous for anyone below that level.")
                            .goto(returnJoin)

                        optionBuilder.option_playerl("What do you do in the Guild?")
                            .npcl("I'm the Grand Secretary for the Wizards' Guild, I have lots of correspondence to keep up with, as well as attending to the discipline of the more problematic guild members.")
                            .goto(returnJoin)

                        optionBuilder.option_playerl("Ok, thanks.")
                            .end()
                    }

                builder.goto(returnJoin)
            }
        }
    }

    override fun addConversation() {

        exec { player, _ ->
            if (startLabel != null) {
                loadLabel(player, startLabel)
            } else {
                loadLabel(player, "initial_dialogue")
            }
        }

        label("initial_dialogue")
            exec { player, _ ->
                if (getAttribute(player, ATTRIBUTE_MET_ZAVISTIC_RARVE, false)) {
                    loadLabel(player, "quest_intro")
                } else if (viaBell) {
                    loadLabel(player, "bell_intro")
                } else {
                    loadLabel(player, "no_quest_intro")
                }
            }

        label("quest_intro")
            npc("What are you doing...Oh, it's you...sorry...didn't realise...what can I do for you?")
            goto("after_intro_route")

        label("no_quest_intro")
            npc("What are you doing bothering me? Don't you think some of us have work to do?")
            player("I thought you were here to help?")
            npc("Well... I am, I suppose, anyway... we're very busy here, hurry up, what do you want?")
            exec { player, _ ->
                if (getQuestStage(player, ZogreFleshEaters.questName) > 0 ||
                    getQuestStage(player, Quests.THE_HAND_IN_THE_SAND) > 0
                ) {
                    setAttribute(player, ATTRIBUTE_MET_ZAVISTIC_RARVE, true)
                }
            }
            goto("after_intro_route")

        label("bell_intro")
            npc("What are you doing ringing that bell?! Don't you think some of us have work to do?")
            player("But I was told to ring the bell if I wanted some attention.")
            npc("Well...anyway...we're very busy here, hurry up what do you want?")
            exec { player, _ ->
                if (getQuestStage(player, ZogreFleshEaters.questName) > 0 ||
                    getQuestStage(player, Quests.THE_HAND_IN_THE_SAND) > 0
                ) {
                    setAttribute(player, ATTRIBUTE_MET_ZAVISTIC_RARVE, true)
                }
            }
            goto("after_intro_route")

        label("after_intro_route")
            exec { player, _ ->
                val hasZogreQuestDialogue = isQuestInProgress(player, ZogreFleshEaters.questName, 2, 9)
                val questStage = getQuestStage(player, Quests.THE_HAND_IN_THE_SAND)

                val hasHandQuestDialogue =
                    questStage == TheHandintheSand.STAGE_SEE_WIZARD_10 ||
                            questStage == TheHandintheSand.STAGE_MAGIC_SCROLL_30 ||
                            questStage == TheHandintheSand.STAGE_MAGICAL_ORB_35 ||
                            questStage in TheHandintheSand.STAGE_TRUTH_SERUM_40..TheHandintheSand.STAGE_INTERROGATE_SANDY_50 ||
                            questStage == TheHandintheSand.STAGE_SANDY_CONFESSION_55 ||
                            questStage == TheHandintheSand.STAGE_RETRIEVE_ITEMS_60 ||
                            questStage == TheHandintheSand.STAGE_SANDPIT_FILLED_65 ||
                            questStage == TheHandintheSand.STAGE_FIND_CLARENCE_70 ||
                            questStage == TheHandintheSand.STAGE_RETURN_HEAD_75

                when {
                    questStage == TheHandintheSand.STAGE_SANDPIT_FILLED_65 -> loadLabel(player, "hand_in_sand")
                    hasZogreQuestDialogue && hasHandQuestDialogue -> loadLabel(player, "quest_choice_options")
                    questStage == TheHandintheSand.STAGE_MAGICAL_ORB_35 -> loadLabel(player, "main_options")
                    hasZogreQuestDialogue -> loadLabel(player, "zogre_dialogue")
                    hasHandQuestDialogue -> loadLabel(player, "hand_in_sand")
                    else -> loadLabel(player, "main_options")
                }
            }

        label("quest_choice_options")
            options(
                DialogueOption("zogre_dialogue", "I'm here about the sicks...err Zogres", skipPlayer = true),
                DialogueOption("hand_in_sand", "I have a rather sandy problem that I'd like to palm off on you.", skipPlayer = true),
            )

        label("main_options")
            options(
                DialogueOption("default_talk", "What is there to do in the Wizards' Guild?"),
                DialogueOption("guild_requirements", "What are the requirements to get in the Wizards' Guild?"),
                DialogueOption("guild_job", "What do you do in the Guild?"),
                DialogueOption(
                    "quest_stage_35",
                    "Can you help me more?", "Can you help me more?", FacialExpression.ASKING,
                    optionIf = { player, _ ->
                        getQuestStage(player, Quests.THE_HAND_IN_THE_SAND) == TheHandintheSand.STAGE_MAGICAL_ORB_35
                    }
                ),
                DialogueOption(
                    "nowhere",
                    "Ok, thanks.",
                    optionIf = { player, _ ->
                        getQuestStage(player, Quests.THE_HAND_IN_THE_SAND) != TheHandintheSand.STAGE_MAGICAL_ORB_35
                    }
                )
            )

        label("default_talk")
            npc("This is the finest wizards' establishment in the land.")
            npc("We have magic portals to the other towers of wizardry around Gielinor.")
            npc("We have a particularly wide collection of runes in our rune shop.")
            npc("We sell some of the finest mage robes in the land and we have a training area full of zombies for you to practice your magic on.")
            goto("main_options")

        label("guild_requirements")
            npc("You need a magic level of 66, the high magic energy level is too dangerous for anyone below that level.")
            goto("main_options")

        label("guild_job")
            npc("I'm the Grand Secretary for the Wizards' Guild, I have lots of correspondence to keep up with, as well as attending to the discipline of the more problematic guild members.")
            goto("main_options")

        label("zogre_dialogue")
            open(player!!, content.region.kandarin.feldip.quest.zogreflesheaters.ZavisticRarveDialogueFileNoIntro(), npc!!)

        label("hand_in_sand")
            exec { player, _ ->
                loadLabel(player, "quest_stage_" + getQuestStage(player, Quests.THE_HAND_IN_THE_SAND))
            }

        label("quest_stage_10")
            exec { player, _ ->
                if (!inInventory(player, Items.BEER_SOAKED_HAND_6946)) {
                    loadLabel(player, "missing_soaked_hand")
                }
            }
            item(Item(Items.BEER_SOAKED_HAND_6946), "You wave the hand at the wizard.")
            player(FacialExpression.ASKING, "Ummm... Do you have all your wizards?")
            npc(FacialExpression.THINKING, "All my.... whatever do you mean...?")
            player("The Guard Captain asked me to see if you have any... missing... wizards.")
            npc(FacialExpression.AMAZED_TALKING, "That's silly! No one would kill a wizard... would they?")
            player(FacialExpression.SUSPICIOUS, "Erm... no...")
            player("Well.. maybe, you see Bert found this hand and it might belong to.. a wizard!")
            npc(FacialExpression.ASKING, "Bert? Ahh yes, the sandman who seems to have been working very long hours recently. Let's see that hand...")
            exec { player, _ ->
                removeItem(player, Items.BEER_SOAKED_HAND_6946)
            }
            item(Item(Items.BEER_SOAKED_HAND_6946), "You hand it over.")
            npc(FacialExpression.AMAZED_TALKING, "Oh my! This is most definitely Clarence, my most able student! You must find out who did this!")
            player("Do you have any input as to the matter at hand?")
            npc("Well.... Ask Bert about the long hours he's been working, that sounds suspicious to me. Digging things up at all hours of the day isn't natural.")
            exec { player, _ ->
                setQuestStage(player, Quests.THE_HAND_IN_THE_SAND, TheHandintheSand.STAGE_ASK_BERT_15)
            }

        label("missing_soaked_hand")
            line("Maybe you should have the hand with you before speaking to Zavistic.")

        label(
            "quest_stage_15",
            "quest_stage_20",
            "quest_stage_25"
        )
            npc("Did you find out who killed Clarence yet?")
            player("Not yet, but don't lose your head over it.")

        label("quest_stage_30")
            exec { player, _ ->
                if (!inInventory(player, Items.A_MAGIC_SCROLL_6949)) {
                    loadLabel(player, "missing_magic_scroll")
                }
            }
            player("I talked to Bert and found something very strange about his hours.")
            npc(FacialExpression.ASKING, "Oh? Did he kill Clarence?")
            player("No, but he doesn't remember changing his hours, and his rota and the original that his boss Sandy had, are different!")
            player("... oh, and this scroll appeared when they changed - he gave it to me.")
            npc(FacialExpression.AMAZED_TALKING, "I recognise that type of scroll! It's used in a mind altering spell of some sort. Did you speak to this... Sandy guy? Perhaps he has a hand in this.")
            player("I took a look around his office. I don't know about a hand in it, I think he has both hands and feet in it!")
            npc("Even more suspicious! Here, take this magical scrying orb and get some Truth Serum from Betty in Port Sarim, she owes me a favour, just tell her I sent you if she complains.")
            npc("Then you will be equipped to ask Sandy a few questions. Oh Clarence, I will find your murderer!")
            exec { player, _ ->
                removeItem(player, Items.A_MAGIC_SCROLL_6949)
                addItem(player, Items.MAGICAL_ORB_6950)
                setQuestStage(player, Quests.THE_HAND_IN_THE_SAND, TheHandintheSand.STAGE_MAGICAL_ORB_35)
            }
            item(Item(Items.MAGICAL_ORB_6950), "You exchange the scroll for the magical scrying orb. Perhaps Zavistic can give you even more of a hand to find the murderer?")

        label("missing_magic_scroll")
            line("Perhaps you should have the scroll from Bert", "with you before you speak to Zavistic.")

        label("quest_stage_35")
            exec { player, _ ->
                if (!hasAnItem(player, Items.MAGICAL_ORB_6950).exists() &&
                    !hasAnItem(player, Items.MAGICAL_ORB_A_6951).exists()) {
                    loadLabel(player, "lost_magical_orb")
                }
            }
            exec { player, _ ->
                if (getAttribute(player, TheHandintheSand.ATTRIBUTE_ZAVISTIC_TELEPORT, false)) {
                    loadLabel(player, "player_already_teleported")
                }
            }
            npc(FacialExpression.HAPPY, "Bring me a vial and I'll help you a little more.")
            exec { player, _ ->
                if (inInventory(player, Items.VIAL_229)) {
                    loadLabel(player, "player_has_vial")
                }
            }

        label("player_has_vial")
            player(FacialExpression.HAPPY, "I have a vial here for you.")
            npc(FacialExpression.ASKING, "Ok, would you like me to transport you to Port Sarim? I'm sticking my neck out a bit helping you like this and can only do it once though!")
            options(
                DialogueOption("player_teleport_yes", "Yes, that would be great!", "Yes, that would be great!", FacialExpression.HAPPY),
                DialogueOption("player_teleport_no", "No, I prefer using my legs, thanks all the same.", "No, I prefer using my legs, thanks all the same.")
            )

        label("player_teleport_yes")
            npc(FacialExpression.HAPPY, "Off you go then, break a leg!")
            exec { player, zavistic ->
                if (removeItem(player, Items.VIAL_229)) {
                    setAttribute(player, TheHandintheSand.ATTRIBUTE_ZAVISTIC_TELEPORT, true)

                    animate(zavistic, Animation(CAST_SPELL_ANIM_711))

                    queueScript(player, 0, QueueStrength.SOFT) { stage ->
                        when (stage) {
                            0 -> {
                                lock(player, 6)
                                return@queueScript delayScript(player, 2)
                            }

                            1 -> {
                                visualize(player, TELEPORT_SPELL_ANIM_714, TELEPORT_SPELL_GFX_308)
                                playGlobalAudio(player.location, Sounds.TELEPORT_ALL_200)
                                return@queueScript delayScript(player, 3)
                            }

                            2 -> {
                                teleport(player, Location.create(3014, 3259, 0))
                                resetAnimator(player)
                                unlock(player)
                            }
                        }

                        return@queueScript stopExecuting(player)
                    }
                }
            }

        label("player_teleport_no")
            npc("Ok, suit yourself!")

        label("player_already_teleported")
            npc("Unfortunately I've already helped you with one teleport, get some exercise - your legs won't fall off!")

        label("lost_magical_orb")
            player("I've lost my magical scrying orb!")
            exec { player, _ ->
                if (freeSlots(player) < 1) {
                    loadLabel(player, "no_space_orb")
                }
            }
            npc("No matter, here, have another and please hurry, whoever killed Clarence must pay!")
            exec { player, _ ->
                addItem(player, Items.MAGICAL_ORB_6950)
            }

        label("no_space_orb")
            npc("I'd give you another magical scrying orb if you had some space in your inventory.")

        label(
            "quest_stage_40",
            "quest_stage_45",
            "quest_stage_50"
        )
            exec { player, _ ->
                if (getQuestStage(player, Quests.THE_HAND_IN_THE_SAND) in
                    TheHandintheSand.STAGE_MAGICAL_ORB_35..TheHandintheSand.STAGE_INTERROGATE_SANDY_50 &&
                    !hasAnItem(player, Items.MAGICAL_ORB_6950).exists() &&
                    !hasAnItem(player, Items.MAGICAL_ORB_A_6951).exists()
                    ) {
                    loadLabel(player, "lost_magical_orb")
                }
            }
            npc("Have you made the serum and talked to Sandy yet?")
            player("Not yet, but don't bust a gut over it!")
            goto("main_options")

        label("quest_stage_55")
            exec { player, _ ->
                if (!inInventory(player, Items.MAGICAL_ORB_A_6951)) {
                    loadLabel(player, "lost_activated_orb")
                }
            }
            line("You hand the magical scrying orb to the Wizard and watch as the", "recording is played back.")
            npc(FacialExpression.ANNOYED, "Well, well...I think this Sandy needs a lesson, please bring me 5 earth runes and a bucket of sand.")
            exec { player, _ ->
                removeItem(player, Items.MAGICAL_ORB_A_6951)
            }
            goto("teach_sandy_lesson")

        label("lost_activated_orb")
            player("I got the whole story from Sandy... but I lost the orb.")
            npc("It's ok, I saw the whole thing as the orb is connected via magic to me as I enchanted it.")
            npc("I think this Sandy needs a lesson, please bring me 5 earth runes and a bucket of sand.")
            exec { player, _ ->
                hasAnItem(player, Items.MAGICAL_ORB_A_6951, true).remove()
            }
            goto("teach_sandy_lesson")

        label("teach_sandy_lesson")
            player(FacialExpression.AMAZED_TALKING, "Erm, why?")
            npc(FacialExpression.ANGRY, "Don't question me or you'll end up as braindead as that legless Guard Captain!")
            player(FacialExpression.SCARED, "Umm.. ok, I'll get you the 5 earth runes and bucket of sand.")
            exec { player, _ ->
                setQuestStage(player, Quests.THE_HAND_IN_THE_SAND, TheHandintheSand.STAGE_RETRIEVE_ITEMS_60)
            }

        label("quest_stage_60")
            exec { player, _ ->
                if (inInventory(player, Items.BUCKET_OF_SAND_1783, 1) && inInventory(player, Items.EARTH_RUNE_557, 5)) {
                    loadLabel(player, "has_runes_and_sand")
                }
            }
            npc("You really mean you forgot? Bring me 5 earth runes and 1 bucket of sand to help stop that moneygrabbing Sandy!")
            goto("main_options")

        label("has_runes_and_sand")
            player(FacialExpression.ASKING, "I've brought what you wanted, what are you going to do?")
            npc(FacialExpression.HAPPY, "Ahh excellent, let's have those! Watch and learn...")
            manual { player, npc ->
                removeItem(player, Item(Items.BUCKET_OF_SAND_1783, 1))
                removeItem(player, Item(Items.EARTH_RUNE_557, 5))
                closeDialogue(player)
                SandpitCutscene(player, npc).start()
                null
            }

        label("quest_stage_65")
            npc(FacialExpression.HAPPY, "There, the sand pit will now magically refill. No more work for Bert!")
            npc("We must find the rest of Clarence, I've sent some wizards out to some of the sandpits, would you please check the Entrana sandpit?")
            exec { player, _ ->
                setQuestStage(player, Quests.THE_HAND_IN_THE_SAND, TheHandintheSand.STAGE_FIND_CLARENCE_70)
            }

        label("quest_stage_70")
            npc("Did you visit the Entrana sandpit yet? Ask the worker there if he's found an arm or a leg.")
            player("Not yet no. I've been running around like a headless chicken, but I'll get to it!")
            goto("main_options")

        label("quest_stage_75")
            exec { player, _ ->
                if (!inInventory(player, Items.WIZARDS_HEAD_6957)) {
                    loadLabel(player, "missing_wizards_head")
                }
            }
            item(Item(Items.WIZARDS_HEAD_6957), "You give the wizard the head.")
            exec { player, _ ->
                removeItem(player, Items.WIZARDS_HEAD_6957)
            }
            npc(FacialExpression.SAD_TALKING, "Alas poor Clarence, I knew him well.")
            npc("Thank you @name, we shall bury him today. I have sent word for the guards to arrest Sandy, so no one will ever see him again!")
            exec { player, _ ->
                setVarbit(player, TheHandintheSand.VARBIT_SANDY_STATE_1535, TheHandintheSand.SANDY_ARRESTED_2, true)
                setVarbit(player, TheHandintheSand.VARBIT_SANDYS_COFFEE_MUG_1536, TheHandintheSand.SANDYS_COFFEE_REMOVED_1, true)
                finishQuest(player, Quests.THE_HAND_IN_THE_SAND)
            }

        label("missing_wizards_head")
            line("Perhaps you should have the wizard's head with you before speaking to Zavistic.")
    }
}