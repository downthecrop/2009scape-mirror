package content.region.asgarnia.burthorpe.quest.heroesquest

import core.api.*
import core.game.dialogue.*
import core.game.global.action.DoorActionHandler
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.Items
import org.rs09.consts.NPCs

@Initializable
class GarvDialogue(player: Player? = null) : DialoguePlugin(player){

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player, GarvDialogueFile(), npc)
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return GarvDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.GARV_788)
    }
}

class GarvDialogueFile : DialogueBuilderFile() {
    override fun create(b: DialogueBuilder) {
        // Technically this won't happen since you have to get past Grubor.
        b.onQuestStages(HeroesQuest.questName, 0,1,2)
                .npcl("Hello. What do you want?")
                .options()
                .let { optionBuilder ->
                    optionBuilder.option_playerl("Can I go in there?")
                            .npcl("No. In there is private.")
                            .end()
                    optionBuilder.option_playerl("I want for nothing!")
                            .npcl("You're one of a very lucky few then.")
                            .end()
                }

        b.onQuestStages(HeroesQuest.questName, 3,4,5,6,100)
                // .npcl("Oi! Where do you think you're going pal?") - When you click on the door instead of Garv.
                .npcl("Hello. What do you want?")
                .playerl("Hi. I'm Hartigen. I've come to work here.")
                .branch { player ->
                    return@branch if (inEquipment(player, Items.BLACK_FULL_HELM_1165) && inEquipment(player, Items.BLACK_PLATEBODY_1125) && inEquipment(player, Items.BLACK_PLATELEGS_1077)) { 1 } else { 0 }
                }.let { branch ->
                    branch.onValue(1)
                            .npcl("I assume you have your I.D. papers then?")
                            .branch { player ->
                                return@branch if (inInventory(player, Items.ID_PAPERS_1584)) { 1 } else { 0 }
                            }.let { branch2 ->
                                branch2.onValue(1)
                                        .npcl("You'd better come in then, Grip will want to talk to you.")
                                        .endWith { _, player ->
                                            if(getQuestStage(player, HeroesQuest.questName) == 3) {
                                                setQuestStage(player, HeroesQuest.questName, 4)
                                            }
                                        }
                                branch2.onValue(0)
                                        .playerl("Uh... Yeah. About that...I must have left them in my other suit of armour.")
                                        .end()
                            }
                    branch.onValue(0)
                            .npcl("Hartigen the Black Knight? I don't think so. He doesn't dress like that.")
                            .end()
                }

    }
}