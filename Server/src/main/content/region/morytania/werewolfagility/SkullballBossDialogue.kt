package content.region.morytania.werewolfagility

import core.api.*
import core.game.dialogue.*
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import org.rs09.consts.Items
import org.rs09.consts.NPCs

@Initializable
class SkullballBossDialogue (player: Player? = null) : DialoguePlugin(player) {
    override fun newInstance(player: Player): DialoguePlugin {
        return SkullballBossDialogue(player)
    }
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player, SkullballBossDialogueFile(), npc)
        return false
    }
    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SKULLBALL_BOSS_1660)
    }
}
class SkullballBossDialogueFile : DialogueLabeller() {
    override fun addConversation() {
        assignToIds(NPCs.SKULLBALL_BOSS_1660)

        exec { player, npc ->
            if(!anyInEquipment(player, Items.RING_OF_CHAROS_4202, Items.RING_OF_CHAROSA_6465)) {
                goto("ishuman")
            } else if (getAttribute<NPC?>(player, SkullballCourse.attributeSkullballInstance, null) != null) {
                goto("skullballinprogress")
            } else {
                goto("noskullball")
            }
        }

        label("ishuman")
        npc(ChatAnim.WEREWOLF_SUSPICIOUS, "Grrr - you don't belong in here, human!")

        label("skullballinprogress")
        options(
            DialogueOption("explainskullball", "What are the instructions for using the skullball course?", expression=ChatAnim.THINKING),
            DialogueOption("lostskullball", "I seem to have lost my ball - can I have another one?", expression=ChatAnim.THINKING),
            DialogueOption("clearskullball", "I give up, I can't do it - take my ball away.", expression=ChatAnim.NEUTRAL),
        )

        label("noskullball")
        options(
            DialogueOption("checkskullball", "I would like to do the skullball course.", expression=ChatAnim.NEUTRAL),
            DialogueOption("explainskullball", "What are the instructions for using the skullball course?", expression=ChatAnim.THINKING),
        )

        label("checkskullball")
        exec { player, npc ->
            if (!hasLevelDyn(player, Skills.AGILITY, 25)) {
                goto("requirementnotmet")
            } else {
                goto("startskullball")
            }
        }

        label("requirementnotmet")
            line("You need an Agility level of at least 25 to do this.")

        label("startskullball")
        exec { player, npc ->
            SkullballCourse.startBall(player)
        }


        label("lostskullball")
        npc(ChatAnim.WEREWOLF_NEUTRAL, "No problem, here's another one. You'll have to start from the beginning again, but the timer will be restarted too.")
        exec { player, npc ->
            SkullballCourse.clearBall(player)
            SkullballCourse.startBall(player)
        }

        label("clearskullball")
        npc(ChatAnim.WEREWOLF_NEUTRAL, "Oh dear, such a defeatist.")
            exec { player, npc ->
            SkullballCourse.clearBall(player)
        }

        label("explainskullball")
        npc(ChatAnim.WEREWOLF_NEUTRAL, "The skullball comes out of one of these four spawnholes. Just kick the ball through the middle of each goal, through the skeleton's feet.")
        npc(ChatAnim.WEREWOLF_NEUTRAL, "There are 10 goals, which you must complete in order, and one final goal.")
        npc(ChatAnim.WEREWOLF_NEUTRAL, "An arrow will point to your ball, just in case lots of people are using the course at the same time as yourself.")
        npc(ChatAnim.WEREWOLF_NEUTRAL, "The better your time, the more agility XP you will be awarded. The timer starts when you score your first goal.")
    }
}
