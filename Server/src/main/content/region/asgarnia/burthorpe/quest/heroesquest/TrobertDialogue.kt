package content.region.asgarnia.burthorpe.quest.heroesquest

import content.data.Quests
import core.api.*
import core.game.dialogue.*
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.Items
import org.rs09.consts.NPCs

@Initializable
class TrobertDialogue(player: Player? = null) : DialoguePlugin(player){

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player, TrobertDialogueFile(), npc)
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return TrobertDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.TROBERT_1884)
    }
}

class TrobertDialogueFile : DialogueBuilderFile() {
    override fun create(b: DialogueBuilder) {
        // Technically this won't happen since you have to get past Grubor.
        b.onQuestStages(Quests.HEROES_QUEST, 0, 1)
                .npcl("Welcome to our Brimhaven headquarters. I'm Trobert and I'm in charge here.")
                .playerl("Pleased to meet you.")
                .npcl("Likewise.")
                .end()

        b.onQuestStages(Quests.HEROES_QUEST, 2)
                .npcl("Welcome to our Brimhaven headquarters. I'm Trobert and I'm in charge here.")
                .options()
                .let { optionBuilder ->
                    val continuePath = b.placeholder()
                    optionBuilder.option("So can you help me get Scarface Pete's candlesticks?")
                            .goto(continuePath)
                    optionBuilder.option_playerl("Pleased to meet you.")
                            .npcl("Likewise.")
                            .goto(continuePath)
                    return@let continuePath.builder()
                }
                .playerl("So can you help me get Scarface Pete's candlesticks?")
                .npcl("Well, we have made some progress there. We know that one of the only keys to Pete's treasure room is carried by Grip, the head guard, so we thought it might be good to get close to him somehow.")
                .npcl("Grip was taking on a new deputy called Hartigen, an Asgarnian Black Knight who was deserting the Black Knight Fortress and seeking new employment here on Brimhaven.")
                .npcl("We managed to waylay him on the journey here, and steal his I.D. papers. Now all we need is to find somebody willing to impersonate him and take the deputy role to get that key for us.")
                .options()
                .let { optionBuilder ->

                    optionBuilder.option_playerl("I volunteer to undertake that mission!")
                            .npcl("Good good. Well, here's the ID papers, take them and introduce yourself to the guards at Scarface Pete's mansion, we'll have that treasure in no time.")
                            .endWith { _, player ->
                                addItemOrDrop(player, Items.ID_PAPERS_1584)
                                if(getQuestStage(player, Quests.HEROES_QUEST) == 2) {
                                    setQuestStage(player, Quests.HEROES_QUEST, 3)
                                }
                            }

                    optionBuilder.option_playerl("Well, good luck then.")
                            .npcl("Someone will show up eventually.")
                            .end()
                }

        b.onQuestStages(Quests.HEROES_QUEST, 3, 4, 5)
                .branch { player ->
                    return@branch if (inInventory(player, Items.ID_PAPERS_1584)) { 1 } else { 0 }
                }.let { branch ->
                    branch.onValue(1)
                            .npcl("How's it going?")
                            .playerl("Fine, thanks.")
                            .end()
                    branch.onValue(0)
                            .playerl("I have lost Hartigen's ID papers.")
                            .npcl("Well, that was careless of you, wasn't it? Fortunately for you, he had a spare. Take this one, but please try to be more careful with this one.")
                            .endWith { _, player ->
                                addItemOrDrop(player, Items.ID_PAPERS_1584)
                            }
                }

        b.onQuestStages(Quests.HEROES_QUEST, 6, 100)
                .npcl("How's it going?")
                .playerl("Fine, thanks.")
                .end()

    }
}