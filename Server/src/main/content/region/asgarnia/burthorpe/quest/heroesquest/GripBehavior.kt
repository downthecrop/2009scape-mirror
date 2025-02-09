package content.region.asgarnia.burthorpe.quest.heroesquest

import content.data.Quests
import core.api.*
import core.game.dialogue.DialogueFile
import core.game.node.entity.Entity
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.game.node.entity.player.Player
import core.game.node.item.GroundItem
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs09.consts.Items
import org.rs09.consts.NPCs

class GripBehavior : NPCBehavior(NPCs.GRIP_792) {
    // Attacking Grip
    override fun canBeAttackedBy(self: NPC, attacker: Entity, style: CombatStyle, shouldSendMessage: Boolean): Boolean {
        // You cannot attack if you are a black arm gang member.
        if (attacker is Player && HeroesQuest.isBlackArm(attacker)) {
            openDialogue(attacker, object : DialogueFile() {
                override fun handle(componentID: Int, buttonID: Int) {
                    when (stage) {
                        //"I can't attack the head guard here! There are too", "many witnesses around to see me do it! I'd have the", "whole of Brimhaven after me! Besides, if he dies I want", "the promotion!"
                        START_DIALOGUE -> sendPlayerDialogue(attacker, "I can't attack the head guard here! There are too many witnesses around to see me do it! I'd have the whole of Brimhaven after me! Besides, if he dies I want the promotion!") .also { stage++ }
                        1 -> sendDialogueLines(attacker, "Perhaps you need another player's help...?").also {
                            stage = END_DIALOGUE
                        }
                    }
                }
            })
            return false
        }
        return true
    }

    override fun onDeathFinished(self: NPC, killer: Entity) {
        if (killer is Player) {
            if (getQuestStage(killer, Quests.HEROES_QUEST) == 4) {
                setQuestStage(killer, Quests.HEROES_QUEST, 5)
            }

            val gi = GroundItem(
                    Item(Items.GRIPS_KEY_RING_1588),
                    self.location,
                    5000,
                    null,
            )
            gi.forceVisible = true
            gi.isRemainPrivate = false

            val gim = GroundItemManager.create(gi)
            gim.isRemainPrivate = false
        }
    }
}