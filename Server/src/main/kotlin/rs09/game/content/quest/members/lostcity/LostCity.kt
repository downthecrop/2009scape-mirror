package rs09.game.content.quest.members.lostcity

import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.plugin.Initializable
import org.rs09.consts.Items

/**
 * LostCity class for the Lost City quest
 * @author lila
 * @author Vexia
 * @author Aero
 */
@Initializable
class LostCity : Quest("Lost City", 83, 82, 3, 147, 0, 1, 6) {

    class SkillRequirement(val skill: Int?, val level: Int?)

    val requirements = arrayListOf<LostCity.SkillRequirement>()

    override fun finish(player: Player?) {
        player ?: return
        super.finish(player)
        var line = 10
        player.packetDispatch.sendItemZoomOnInterface(Items.DRAMEN_STAFF_772, 235, 277, 3 + 2)
        drawReward(player,"3 Quest points",line++)
        drawReward(player,"Access to Zanaris",line++)
        player.questRepository.syncronizeTab(player)
    }

    override fun drawJournal(player: Player, stage: Int) {
        super.drawJournal(player, stage)
        when (stage) {
            0 -> {
                line(player, BLUE + "I can start this quest by speaking to the Adventurers in", 4 + 7)
                line(player, BLUE + "the Swamp just south of Lumbridge.", 5 + 7)
                drawQuestRequirements(player)
            }
            10 -> line(player, "<str>According to one of the adventurers in Lumbridge Swamp<br><br><str>the entrance to Zanaris is somewhere around there.<br><br><blue>Apparently there is a <red>leprechaun<blue> hiding in a <red>tree<blue> nearby<br><br><blue>who can tell me how to enter the <red>Lost City of Zanaris", 4 + 7)
            20 -> line(player, "<str>According to one of the adventurers in Lumbridge Swamp<br><br><str>the entrance to Zanaris is somewhere around there.<br><br><str>I found the Leprechaun hiding in a nearby tree.<br><br><blue>He told me that the entrance to <red>Zanaris<blue> is in the <red>shed<blue> in<br><br><red>Lumbridge swamp<blue> but only if I am carrying a <red>Dramen Staff<br><br><blue>I can find a <red>Dramen Tree <blue>in a cave on <red>Entrana <blue>somewhere", 4 + 7)
            21 -> if (player.hasItem(Item(Items.DRAMEN_STAFF_772,1))) {
                line(player, "<str>According to one of the adventurers in Lumbridge Swamp<br><br><str>the entrance to Zanaris is somewhere around there.<br><br><str>I found the Leprechaun hiding in a nearby tree.<br><br><str>He told me that the entrance to Zanaris is in the shed in<br><br><str>Lumbridge swamp but only if I am carrying a Dramen Staff.<br><br><str>The Dramen Tree was guarded by a powerful Tree Spirit.<br><br><str>I cut a branch from the tree and crafted a Dramen Staff.<br><br><blue>I should enter <red>Zanaris <blue>by going to the <red>shed <blue>in <red>Lumbridge<br><br><red>Swamp <blue>while keeping the <red>Dramen staff<blue> with me", 4 + 7)
            } else {
                line(player, "<str>According to one of the adventurers in Lumbridge Swamp<br><br><str>the entrance to Zanaris is somewhere around there.<br><br><str>I found the Leprechaun hiding in a nearby tree.<br><br><str>He told me that the entrance to Zanaris is in the shed in<br><br><str>Lumbridge swamp but only if I am carrying a Dramen Staff.<br><br><str>The Dramen Tree was guarded by a powerful Tree Spirit.<br><br><blue>With the <red>Spirit <blue>defeated I can cut a <red>branch <blue>from the tree", 4 + 7)
            }
            100 -> line(player, "<str>According to one of the adventurers in Lumbridge Swamp<br><br><str>the entrance to Zanaris is somewhere around there.<br><br><str>I found the Leprechaun hiding in a nearby tree.<br><br><str>He told me that the entrance to Zanaris is in the shed in<br><br><str>Lumbridge swamp but only if I am carrying a Dramen Staff.<br><br><str>The Dramen Tree was guarded by a powerful Tree Spirit.<br><br><str>I cut a branch from the tree and crafted a Dramen Staff.<br><br><str>With the mystical Dramen Staff in my possession I was<br><br><str>able to enter Zanaris through the shed in Lumbridge<br><br><str>swamp.<br><br><br><col=FF0000>QUEST COMPLETE!", 4 + 7)
        }
    }

    private fun drawQuestRequirements(player: Player) {
        var line = 7 + 7
        val questRequirements = arrayOf<String>(
            "Level 31 Crafting",
            "Level 36 Woodcutting"
        )
        val hasRequirements = booleanArrayOf(
            player.getSkills().hasLevel(Skills.CRAFTING, 31),
            player.getSkills().hasLevel(Skills.WOODCUTTING,35)
        )
        line(player, BLUE + "To complete this quest I will need:", 6)
        for (i in 0..1) {
            line(player, questRequirements[i], line++, hasRequirements[i])
        }
        line(player, BLUE + "and be able to defeat a " + RED + "Level 101 Spirit without weapons", line++)
    }

    override fun newInstance(`object`: Any?): Quest {
        requirements.add(SkillRequirement(Skills.WOODCUTTING, 36))
        requirements.add(SkillRequirement(Skills.CRAFTING, 31))
        return this
    }
}