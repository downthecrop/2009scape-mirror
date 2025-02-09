package content.region.kandarin.witchhaven.quest.seaslug

import content.data.Quests
import core.api.*
import core.game.dialogue.DialogueBuilder
import core.game.dialogue.DialogueBuilderFile
import core.game.dialogue.FacialExpression
import org.rs09.consts.Items

class BaileyDialogueFile : DialogueBuilderFile() {
    override fun create(b: DialogueBuilder) {
        b.onQuestStages(Quests.SEA_SLUG, 0, 1, 2, 3, 4)
                .playerl(FacialExpression.FRIENDLY, "Hello there.")
                .npcl(FacialExpression.SCARED, "What? Who are you? Come inside quickly!")
                .npcl(FacialExpression.SCARED, "What are you doing here?")
                .playerl("I'm trying to find out what happened to a boy named Kennith.")
                .npcl("Oh you mean Kent's son. He's around somewhere, probably hiding if he knows what's good for him.")
                .playerl(FacialExpression.THINKING, "Hiding from what? What's got you so frightened?")
                .npcl("Haven't you seen all those things out there?")
                .playerl(FacialExpression.THINKING, "The sea slugs?")
                .npcl(FacialExpression.SUSPICIOUS, "It all began about a week ago. We pulled up a haul of deep sea flatfish. Mixed in with them we found these slug things, but thought nothing of it.")
                .npcl(FacialExpression.SUSPICIOUS, "Not long after that my friends began to change, now they spend all day pulling in hauls of fish, only to throw back the fish and keep those nasty sea slugs.")
                .npcl(FacialExpression.SUSPICIOUS, "What am I supposed to do with those? I haven't figured out how to kill one yet. If I put them near the stove they squirm and jump away.")
                .playerl(FacialExpression.THINKING, "I doubt they would taste too good.")
                .npcl(FacialExpression.ANGRY, "This is no time for humour.")
                .playerl("I'm sorry, I didn't mean to upset you.")
                .npcl(FacialExpression.SCARED, "That's okay. I just can't shake the feeling that this is the start of something... Terrible.")
                .end()

        b.onQuestStages(Quests.SEA_SLUG, 5)
                .playerl("Hello.")
                .npcl(FacialExpression.EXTREMELY_SHOCKED, "Oh, thank the gods it's you. They've all gone mad I tell you, one of the fishermen tried to throw me into the sea!")
                .playerl("They're all being controlled by the sea slugs.")
                .npcl("I figured as much.")
                .playerl("I need to get Kennith off this platform, but I can't get past the fishermen.")
                .npcl("The sea slugs are scared of heat... I figured that out when I tried to cook them.")
                .npcl("Here.")
                .betweenStage { _, player, _, _ ->
                    addItemOrDrop(player, Items.UNLIT_TORCH_596)
                }
                .iteml(Items.UNLIT_TORCH_596, "Bailey gives you a torch.")
                .npcl("I doubt the fishermen will come near you if you can get this torch lit. The only problem is all the wood and flint are damp... I can't light a thing!")
                .endWith() { df, player ->
                    if(getQuestStage(player, Quests.SEA_SLUG) == 5) {
                            setQuestStage(player, Quests.SEA_SLUG, 6)
                    }
                }

        // We aren't going to give you a spare torch. Go get an unlit torch somewhere else.
        b.onQuestStages(Quests.SEA_SLUG, 6, 7, 8)
                .playerl("Hello.")
                .npcl("Oh, thank the gods it's you. They've all gone mad I tell you, one of the fishermen tried to throw me into the sea!")
                .playerl("They're all being controlled by the sea slugs.")
                .npcl("I figured as much.")
                .playerl("I need to get Kennith off this platform, but I can't get past the fishermen.")
                .npcl("The sea slugs are scared of heat... I figured that out when I tried to cook them.")
                .npcl("I doubt the fishermen will come near you if you can get this torch lit. The only problem is all the wood and flint are damp... I can't light a thing!")
                .end()

        b.onQuestStages(Quests.SEA_SLUG, 9, 10, 100)
                .playerl("I've managed to light the torch.")
                .npcl("Well done traveller, you'd better get Kennith out of here soon. The fishermen are becoming stranger by the minute, and they keep pulling up those blasted sea slugs.")
                .playerl("Don't worry I'm working on it.")
                .npcl("Just be sure to watch your back. The fishermen seem to have taken notice of you.")
                .end()

    }
}
