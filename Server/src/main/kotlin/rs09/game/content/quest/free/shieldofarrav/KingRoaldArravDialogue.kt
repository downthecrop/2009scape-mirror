package rs09.game.content.quest.free.shieldofarrav

import core.game.content.quest.free.shieldofarrav.ShieldofArrav
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import rs09.game.content.dialogue.DialogueFile
import rs09.tools.END_DIALOGUE
import rs09.tools.START_DIALOGUE


private val CERTIFICATE = Item(769)


class KingRoaldArravDialogue() : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {

        if (player!!.inventory.containsItem(ShieldofArrav.PHOENIX_SHIELD) || player!!.inventory.containsItem(ShieldofArrav.BLACKARM_SHIELD)) {
            when (stage) {
                START_DIALOGUE -> player("Your majesty, I have recovered the Shield of Arrav; I", "would like to claim the reward.").also { stage++ }
                1 -> npc("The Shield of Arrav, eh? Yes, I do recall my father,", "King Roald, put a reward out for that.").also { stage++ }
                2 -> npc("Very well.").also { stage++ }
                3 -> npc("If you get the authenticity of the shield verified by the", "curator at the museum and then return here with", "authentication, I will grant your reward.").also { stage = END_DIALOGUE }
                END_DIALOGUE -> end()
            }
        }

        else if(player!!.getInventory().containsItem(ShieldofArrav.BLACKARM_CERTIFICATE) || player!!.getInventory().containsItem(ShieldofArrav.PHOENIX_CERTIFICATE)){
            when(stage) {
                START_DIALOGUE -> player("Your majesty, I have come to claim the reward for the", "return of the Shield of Arrav.").also { stage++ }
                1 -> interpreter!!.sendItemMessage(if (player!!.inventory.containsItem(ShieldofArrav.BLACKARM_CERTIFICATE)) ShieldofArrav.BLACKARM_CERTIFICATE.id else ShieldofArrav.PHOENIX_CERTIFICATE.id, "You show the certificate to the king.").also { stage++ }
                2 -> npc("I'm  afraid that's only half the reward certificate. You'll", "have to get the other half and join them together if you", "want to cliam the reward.").also { stage = END_DIALOGUE }
                END_DIALOGUE -> end()
            }
        }

        else if(player!!.inventory.containsItem(CERTIFICATE)){
            when(stage){
                START_DIALOGUE -> player("Your majesty, I have come to claim the reward for the", "return of the Shield of Arrav.").also { stage++ }
                1 -> interpreter!!.sendItemMessage(CERTIFICATE.id, "You show the certificate to the king.").also { stage++ }
                2 -> npc("My goodness! This claim is for the reward offered by", "my father many years ago!").also { stage++ }
                3 -> npc("I never thought I would live to see the day when", "someone came forward to claim this reward!").also { stage++ }
                4 -> npc("I heard that you found half the shield, so I will give", "you half of the bounty. That comes to exactly 600 gp!").also { stage++ }
                5 -> {
                    interpreter!!.sendItemMessage(CERTIFICATE.id, "You hand over a certificate. The king gives you 600 gp.")
                    if (player!!.inventory.remove(CERTIFICATE)) {
                        if (!player!!.inventory.add(Item(995, 600))) {
                            GroundItemManager.create(Item(995, 600), player)
                        }
                        player!!.questRepository.getQuest("Shield of Arrav").finish(player)
                        stage = END_DIALOGUE
                    }
                }
            }
        }

        else {
            abandonFile()
        }
    }
}