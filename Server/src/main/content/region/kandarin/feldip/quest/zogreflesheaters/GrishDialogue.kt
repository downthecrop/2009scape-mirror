package content.region.kandarin.feldip.quest.zogreflesheaters

import content.region.kandarin.quest.templeofikov.TempleOfIkov
import core.api.*
import core.game.dialogue.DialogueBuilder
import core.game.dialogue.DialogueBuilderFile
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.Items
import org.rs09.consts.NPCs

@Initializable
class GrishDialogue (player: Player? = null) : DialoguePlugin(player) {
    override fun newInstance(player: Player): DialoguePlugin {
        return GrishDialogue(player)
    }
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player, GrishDialogueFile(), npc)
        return false
    }
    override fun getIds(): IntArray {
        return intArrayOf(NPCs.GRISH_2038)
    }
}
class GrishDialogueFile : DialogueBuilderFile() {
    override fun create(b: DialogueBuilder) {
        b.onQuestStages(ZogreFleshEaters.questName, 0)
                .playerl(FacialExpression.FRIENDLY, "Hello there, what's going on here?")
                .npcl(FacialExpression.OLD_NORMAL, "Hey yous creature...wha's you's doing here? Yous be cleverer to be running so da sickies from da zogres don't dead ya.")

                .let { builder ->
                    val returnJoin = b.placeholder()
                    returnJoin.builder()
                            .options()
                            .let { optionBuilder ->
                                optionBuilder.option_playerl("I'm just looking around thanks.")
                                        .npcl(FacialExpression.OLD_NORMAL, "Yous creature won'ts see muchly in dis place...just da zogries coming wiv da sickies.")
                                        .goto(returnJoin)
                                optionBuilder.option_playerl("What do you mean sickies?")
                                        .npc(FacialExpression.OLD_NORMAL, "Da zogries comin wiv da sickies...yous get bashed by da", "zogries and get da sickies...den you gonna be like da", "zogries.")
                                        .playerl(FacialExpression.FRIENDLY, "Sorry, I just don't understand...")
                                        .betweenStage { df, player, _, _ ->
                                            animate(npc!!, 2090)
                                            setAttribute(player, ZogreFleshEaters.attributeAskedAboutSickies, true)
                                        }
                                        .npc(FacialExpression.OLD_NORMAL, "Da sickies is when yous creature goes like orange till", "green and then goes 'Urggghhhh!'", "<col=00000FF>~ Grish imitates falling down with only the white of his", "<col=00000FF>eyes visible. ~")
                                        .goto(returnJoin);
                                optionBuilder.option_playerl("What are Zogres?")
                                        .npcl(FacialExpression.OLD_NORMAL, "a Zogres are da bigun nasties wiv da sickies, deys old pals of Grish but deys jig in Jiggig when dey's full home is deep in da dirt, dey's is not da same dead'uns like was before.")
                                        .goto(returnJoin);
                                optionBuilder.optionIf("Can I help in any way?") { player -> return@optionIf getAttribute(player, ZogreFleshEaters.attributeAskedAboutSickies, false) }
                                        .playerl(FacialExpression.FRIENDLY, "Can I help in any way?")
                                        .branch { player ->
                                            return@branch if (ZogreFleshEaters.requirements(player)) { 1 } else { 0 }
                                        }.let { branch ->
                                            branch.onValue(0)
                                                    .npcl(FacialExpression.OLD_NORMAL, "Sorry, yous creatures, but yous is too green behind da ears for dis job Grish finks.")
                                                    .playerl(FacialExpression.ANGRY, "No, I'm not!")
                                                    .npcl(FacialExpression.OLD_ANGRY1, "Yes you are!")
                                                    .playerl(FacialExpression.ANGRY, "No, I'm not!")
                                                    .npcl(FacialExpression.OLD_ANGRY1, "Yes you are and that's final!")
                                                    .end()
                                            branch.onValue(1)
                                                    .npcl(FacialExpression.OLD_NORMAL, "Yes creatures...yous does good fings for Grish and learn why Zogries at Jiggig and den get da Zogries back in da ground.")
                                                    .playerl("Oh, so you want me to find out why the Zogres have appeared and then find a way of burying them?")
                                                    .npcl(FacialExpression.OLD_NORMAL, "Is what Grish says! But dis is da biggy danger fing yous creatures...yous be geddin' sickies most surely...yous needs be ready..wiv da foodies un da glug-glugs.")
                                                    .playerl("Right, so you think there's a good chance that I can get ill from this, so I need to get some food and something to drink?")
                                                    .npcl(FacialExpression.OLD_NORMAL, "Yea creatures, yous just say what Grish says...not know own wordies creature?")
                                                    .options()
                                                    .let { optionBuilder ->
                                                        optionBuilder.option_playerl("Hmm, sorry, it sounds a bit too dangerous.")
                                                                .npcl(FacialExpression.OLD_NORMAL, "Yous creature is not a stoopid one...stays out of dere, like clever Grish. Yous can paint circles on chest and be da Shaman too!")
                                                                .playerl("Hmm, is it too late to reconsider?")
                                                                .end()
                                                        optionBuilder.option_playerl("Ok, I'll check things out then and report back.")
                                                                .npcl(FacialExpression.OLD_NORMAL, "Is yous creatures really, really sure yous wanna do dis creatures..we's got no glug-glugs for da sickies? We's knows nuffin for da going of da sickies?")
                                                                .options()
                                                                .let { optionBuilder2 ->
                                                                    optionBuilder2.option_playerl("Yes, I'm really sure!")
                                                                            .npcl(FacialExpression.OLD_NORMAL,"Dats da good fing yous creature...yous does Grish a good fing. But yous know dat yous get sickies and mebe get dead!")
                                                                            .playerl("If that's your idea of a pep talk, I have to say that it leaves a lot to be desired.")
                                                                            .npcl(FacialExpression.OLD_NORMAL,"Yous creatures is alus says funny stuff...speaks proper like Grish!")
                                                                            .manualStage() { df, player, _, _ ->
                                                                                sendDoubleItemDialogue(player, Items.COOKED_CHOMPY_2878, Items.SUPER_RESTORE3_3026, "Grish hands you some food and two potions.")
                                                                            }
                                                                            .npcl(FacialExpression.OLD_NORMAL,"Der's yous go creatures...da best me's do for yous...and be back wivout da sickies.")
                                                                            .endWith { _, player ->
                                                                                if(getQuestStage(player, ZogreFleshEaters.questName) == 0) {
                                                                                    // Trying to prevent players from spamming to get more super restores.
                                                                                    addItemOrDrop(player, Items.COOKED_CHOMPY_2878, 3)
                                                                                    addItemOrDrop(player, Items.SUPER_RESTORE3_3026, 2)
                                                                                    setQuestStage(player, ZogreFleshEaters.questName, 1)
                                                                                }
                                                                            }
                                                                    optionBuilder2.option_playerl("Hmm, sorry, it sounds a bit too dangerous.")
                                                                            .npcl(FacialExpression.OLD_NORMAL, "Yous creature is not a stoopid one...stays out of dere, like clever Grish. Yous can paint circles on chest and be da Shaman too!")
                                                                            .end()
                                                                }
                                                    }

                                        }
                                optionBuilder.option_playerl("Sorry, I have to go.")
                                        .end()
                            }
                }

        b.onQuestStages(ZogreFleshEaters.questName, 1,2,3,4,5,6)
                .npcl(FacialExpression.OLD_NORMAL, "Yous creature dun da fing yet? Da zogries going in da dirt full home?")
                .playerl("Nope, I haven't figured out why the zogres are here yet.")
                .end()

        b.onQuestStages(ZogreFleshEaters.questName, 7)
                .npcl(FacialExpression.OLD_NORMAL,"Yous creature dun da fing yet? Da zogries going in da ground?")
                .playerl("I found who's responsible for the Zogres being here.")
                .npcl(FacialExpression.OLD_NORMAL,"Where is da creature? Me's wants to squeeze him till he's a deadun...")
                .playerl("The person responsible is a wizard named 'Sithik Ints' and he's going to be in serious trouble. He told me that the spell which raised the zogres from the ground will last forever.")
                .playerl("I'm sorry to say, but you'll have to move the site of your ceremonial dancing somewhere else.")
                .npcl(FacialExpression.OLD_NORMAL,"Dat is da bad fing creature...we's needs new Jiggig for da fallin' down jig.")
                .playerl("Yes, that's right, you'll need to create a new ceremonial dance area.")
                .npcl(FacialExpression.OLD_NORMAL,"Urghhh...not good fing creature, yous gotta get da ogrish old fings for da making new jiggig special. You's creature needs da key for getting in da low bury place.")
                .betweenStage { df, player, _, _ ->
                    addItemOrDrop(player, Items.OGRE_GATE_KEY_4839)
                }
                .iteml(Items.OGRE_GATE_KEY_4839, "Grish gives you a crudely crafted key.")
                .playerl("Oh, so you want me to go back in there and look for something for you?")
                .npcl(FacialExpression.OLD_NORMAL,"Yeah creature, yous gotta get da ogrish old fings for da making new jiggig and proper in da special way.")
                .endWith { _, player ->
                    if(getQuestStage(player, ZogreFleshEaters.questName) == 7) {
                        setQuestStage(player, ZogreFleshEaters.questName, 8)
                    }
                }

        b.onQuestStages(ZogreFleshEaters.questName, 8)
                .npcl(FacialExpression.OLD_NORMAL, "Hey, you's creature got da old fings?")
                .branch { player ->
                    return@branch if (inInventory(player, Items.OGRE_GATE_KEY_4839)) { 1 } else { 0 }
                }.let { branch ->
                    branch.onValue(1)
                            .playerl("Nope, not yet.")
                            .npcl(FacialExpression.OLD_NORMAL, "Yous gets 'em quick tho, cos we'ze wonna do da new Jiggig place...")
                            .end()
                    branch.onValue(0)
                            .playerl("I lost the key you gave me.")
                            .betweenStage { df, player, _, _ ->
                                addItemOrDrop(player, Items.OGRE_GATE_KEY_4839)
                            }
                            .iteml(Items.OGRE_GATE_KEY_4839, "Grish gives you a crudely crafted key.")
                            .end()
                }

        b.onQuestStages(ZogreFleshEaters.questName, 9)
                .branch { player ->
                    return@branch if (inInventory(player, Items.OGRE_ARTEFACT_4818)) { 1 } else { 0 }
                }.let { branch ->
                    branch.onValue(0)
                            .npcl(FacialExpression.OLD_NORMAL, "Hey, you's creature got da old fings?")
                            .playerl("No sorry, I don't have them yet.")
                            .npcl(FacialExpression.OLD_NORMAL, "Yous creatures get dem for me soon doh, yes?")
                            .end() // There's all the default dialogue here, but I'm lazy again.

                    branch.onValue(1)
                            .npcl(FacialExpression.OLD_NORMAL, "Hey, you's creature got da old fings?")
                            .playerl("Yeah, I have them here!")
                            .npcl(FacialExpression.OLD_NORMAL, "Dat is da goodly fing yous creature, now's we's can make da new Jiggig place away from zogries! Yous been da big helpy fing yous creature, Grish wishin' yous good stuff for da next fings for creature.")
                            .npcl(FacialExpression.OLD_HAPPY, "<col=08088A>~ Grish seems very pleased about the return of the artefacts. ~")
                            .playerl("Thanks, that's very nice of you!")
                            .endWith { _, player ->
                                if (removeItem(player, Items.OGRE_ARTEFACT_4818)) {
                                    if (getQuestStage(player, ZogreFleshEaters.questName) == 9) {
                                        finishQuest(player, ZogreFleshEaters.questName)
                                    }
                                }
                            }
                }

        b.onQuestStages(ZogreFleshEaters.questName, 100)
                .playerl("How's everything going now?")
                .npcl(FacialExpression.OLD_NORMAL,"All da zogries stayin' in da oldie Jiggig, we's gonna do da new Jiggig someways else. Yous creature da good- un for geddin' da oldie fings...")
                // More default dialogue, but lazy.
                .end()
    }
}