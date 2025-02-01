package content.region.kandarin.witchhaven.quest.seaslug

import content.region.asgarnia.falador.quest.recruitmentdrive.RecruitmentDrive
import core.api.*
import core.game.dialogue.DialogueBuilder
import core.game.dialogue.DialogueBuilderFile
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.world.map.Location
import core.plugin.Initializable
import org.rs09.consts.Components
import org.rs09.consts.Items
import org.rs09.consts.NPCs

class HolgartDialogueFile : DialogueBuilderFile() {
    override fun create(b: DialogueBuilder) {
        b.onQuestStages(SeaSlug.questName, 0)
                .playerl(FacialExpression.FRIENDLY, "Hello.")
                .npcl(FacialExpression.FRIENDLY, "Well hello @g[m'lad,m'laddy]. Beautiful day isn't it?")
                .playerl("Not bad I suppose.")
                .npcl("Just smell that sea air... beautiful.")
                .playerl(FacialExpression.THINKING, "Hmm... lovely...")
                .end()

        b.onQuestStages(SeaSlug.questName, 1)
                .npcl(FacialExpression.FRIENDLY, "Hello, m'hearty.")
                .playerl("I would like a ride on your boat to the fishing platform.")
                .npcl(FacialExpression.SAD, "I'm afraid it isn't sea worthy, it's full of holes. To fill the holes I'll need some swamp paste.")
                .playerl(FacialExpression.THINKING, "Swamp paste?")
                .npcl("Yes, swamp tar mixed with flour and heated over a fire.")
                .branch { player ->
                    return@branch if (inInventory(player, Items.SWAMP_PASTE_1941)) { 1 } else { 0 }
                }.let { branch ->
                    branch.onValue(0)
                            .playerl("Where can I find swamp tar?")
                            .npcl("Unfortunately the only supply of swamp tar is in the swamps below Lumbridge. It's too far for an old man like me to travel.")
                            .npcl("If you make me some swamp paste I'll give you a ride in my boat.")
                            .playerl("I'll see what I can do.")
                            .endWith() { df, player ->
                                if(getQuestStage(player, SeaSlug.questName) == 1) {
                                    setQuestStage(player, SeaSlug.questName, 2)
                                }
                            }
                    branch.onValue(1)
                            .npcl("In fact, unless me nose be mistaken, you've got some in yer pack.")
                            .playerl("Oh yes, I forgot about that stuff. Can you use it?")
                            .npcl("Aye @g[lad,lass]. That be perfect.")
                            .betweenStage { _, player, _, _ ->
                                removeItem(player, Items.SWAMP_PASTE_1941)
                            }
                            .iteml(Items.SWAMP_PASTE_1941, "You give Holgart the swamp paste.")
                            // Cutscene
                            .endWith() { df, player ->
                                if(getQuestStage(player, SeaSlug.questName) == 1) {
                                    setQuestStage(player, SeaSlug.questName, 3)
                                }
                            }
                }

        b.onQuestStages(SeaSlug.questName, 2)
                .playerl(FacialExpression.FRIENDLY, "Hello.")
                .npcl("Hello, m'hearty. Did you manage to make some swamp paste?")
                .branch { player ->
                    return@branch if (inInventory(player, Items.SWAMP_PASTE_1941)) { 1 } else { 0 }
                }.let { branch ->
                    branch.onValue(0)
                            .playerl("I'm afraid not.")
                            .npcl("It's simply swamp tar mixed with flour heated over a fire. Unfortunately the only supply of swamp tar is in the swamps below Lumbridge.")
                            .npcl("I can't fix my row boat without it.")
                            .playerl("Ok, I'll try to find some.")
                            .end()
                    branch.onValue(1)
                            .playerl("Yes, I have some here.")
                            .betweenStage { _, player, _, _ ->
                                removeItem(player, Items.SWAMP_PASTE_1941)
                            }
                            .iteml(Items.SWAMP_PASTE_1941, "You give Holgart the swamp paste.")
                            // Cutscene
                            .endWith() { df, player ->
                                if(getQuestStage(player, SeaSlug.questName) == 2) {
                                    setQuestStage(player, SeaSlug.questName, 3)
                                }
                            }

                }


        b.onQuestStages(SeaSlug.questName, 3,4,5,6,7,8,9,10,11,100)
                .playerl(FacialExpression.FRIENDLY, "Hello, Holgart.")
                .npcl("Hello again land lover. There's some strange goings on, on that platform, I tell you.")
                .options().let { optionBuilder ->
                    optionBuilder.option("Will you take me there?")
                            .playerl(FacialExpression.THINKING, "Will you take me there?")
                            .npcl("Of course m'hearty. If that's what you want.")
                            .endWith() { df, player ->
                                SeaSlugListeners.seaslugBoatTravel(player, 0)
                            }

                    optionBuilder.option_playerl("I'm keeping away from there.")
                            .npcl("Fair enough m'hearty.")
                            .end()
                }


    }
}
