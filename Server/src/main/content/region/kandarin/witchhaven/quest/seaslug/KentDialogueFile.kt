package content.region.kandarin.witchhaven.quest.seaslug

import core.api.*
import core.game.dialogue.DialogueBuilder
import core.game.dialogue.DialogueBuilderFile
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.world.map.Location
import core.plugin.Initializable
import org.rs09.consts.Items
import org.rs09.consts.NPCs

class KentDialogueFile : DialogueBuilderFile() {
    override fun create(b: DialogueBuilder) {
        b.onQuestStages(SeaSlug.questName, 0,1,2,3,4,5,6)
                .npcl("Oh thank Saradomin! I thought I'd be left out here forever.")
                .playerl("Your wife sent me out to find you and your boy. Kennith's fine by the way, he's on the platform.")
                .npcl("I knew the row boat wasn't sea worthy. I couldn't risk bringing him along but you must get him off that platform.")
                .playerl("What's going on here?")
                .npcl("Five days ago we pulled in a huge catch. As well as fish we caught small slug like creatures, hundreds of them.")
                .npcl("That's when the fishermen began to act strange.")
                .npcl("It was the sea slugs, they attack themselves to your body and somehow take over the mind of the carrier.")
                .npcl("I told Kennith to hide until I returned but I was washed up here.")
                .npcl("Please go back and get my boy, you can send help for me later.")
                .npcl(FacialExpression.EXTREMELY_SHOCKED, "@name wait!")
                .betweenStage { _, player, _, _ ->
                    visualize(npc!!, 4807, 790)
                    sendMessage(player, "*slooop*")
                    sendMessage(player, "He pulls a sea slug from under your top.")
                }
                .npcl("A few more minutes and that thing would have full control of your body.")
                .playerl(FacialExpression.EXTREMELY_SHOCKED, "Yuck! Thanks Kent.")
                .endWith() { df, player ->
                    if(getQuestStage(player, SeaSlug.questName) == 4) {
                        setQuestStage(player, SeaSlug.questName, 5)
                    }
                }

        b.onQuestStages(SeaSlug.questName, 5,6,7,8,9,10,11,100)
                .playerl("Hello.")
                .npcl("Oh my, I must get back to shore.")
                .end()

    }
}
