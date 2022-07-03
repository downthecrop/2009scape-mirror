package rs09.game.content.quest.members.chompybird

import api.*

import org.rs09.consts.Vars

import core.plugin.Initializable
import core.game.world.map.Location
import core.game.node.entity.skill.Skills
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest

@Initializable
class ChompyBird : Quest("Big Chompy Bird Hunting", 35, 34, 1, Vars.VARP_QUEST_CHOMPY, 0, 1, 65) {
  companion object {
    val CAVE_ENTRANCE = Location.create(2646, 9378, 0)
    val CAVE_EXIT = Location.create(2630, 2997, 0) 
    val TOAD_LOCATION = Location.create(2636, 2966, 0)
    val ATTR_ING_RANTZ = "/save:chompybird:rantz-ingredient"
    val ATTR_ING_BUGS  = "/save:chompybird:rantz-ingredient"
    val ATTR_ING_FYCIE = "/save:chompybird:rantz-ingredient"
  }

  override fun drawJournal(player: Player, stage: Int) {
    super.drawJournal(player, stage)
    
    var ln = 11

    if (stage == 0) {
      line(player, "To start this quest I will need:", ln++, false)
      line(player, "Level 5 !!Fletching??", ln++, getStatLevel(player, Skills.FLETCHING) >= 5)
      line(player, "Level 30 !!Cooking??", ln++, getStatLevel(player, Skills.COOKING) >= 30)
      line(player, "Level 30 !!Ranged??", ln++, getStatLevel(player, Skills.RANGE) >= 30)
      line(player, "Ability to defend against !!level 64 wolves?? and !!level 70 ogres<n>??for short periods of time.", ln++, false)
    } else {
      if (stage in 0 until 20) {
        line(player, "Rantz needs me to make 'stabbers'. To do this I need:", ln++, false)
        line(player, "- !!Achey Logs??", ln++, false)
        line(player, "- !!Wolf Bones??", ln++, false)
        line(player, "- !!Feathers??", ln++, false)
        line(player, "I then must turn the !!achey logs?? into !!ogre shafts??,", ln++, false)
        line(player, "attach !!feathers?? to these !!shafts??, and then tip them", ln++, false)
        line(player, "with !!wolf bones?? chiseled into !!tips??.", ln++, false)
        line(player, "At least, that's what I think he was getting at.", ln++, false)
      }
      else if (stage in 20 until 30) {
        line(player, "Rantz needs me to obtain a bloated swamp toad.", ln++, false)
        line(player, "To do this, I need to take !!billows?? from the !!locked", ln++, false)
        line(player, "!!chest?? in his cave, and then head to the !!swamp to", ln++, false)
        line(player, "!!the south??. There, I should !!use the billows?? on the", ln++, false)
        line(player, "!!swamp bubbles?? to fill them with swamp gas. Then I can", ln++, false)
        line(player, "use the !!billows?? to fill the !!swamp toads?? with gas.", ln++, false)
      }
      else if (stage in 30 until 65) {
        line(player, "Rantz needs me to place the swamp toad to bait out a 'chompy'.", ln++, false)
      }
    }
  }

  override fun newInstance(`object`: Any?): Quest { return this }
}
