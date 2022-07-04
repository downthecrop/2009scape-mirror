package rs09.game.content.quest.members.chompybird

import api.*

import org.rs09.consts.Vars
import org.rs09.consts.Items
import org.rs09.consts.Animations
import org.rs09.consts.Graphics
import org.rs09.consts.NPCs

import core.plugin.Initializable
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.node.item.Item
import core.game.node.entity.skill.Skills
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.world.update.flag.context.Graphics as Gfx

import rs09.game.interaction.InteractionListener
import rs09.game.content.dialogue.SkillDialogueHandler
import rs09.game.content.dialogue.SkillDialogueHandler.SkillDialogue

import kotlin.math.min
import java.util.Random

@Initializable
class ChompyBird : Quest("Big Chompy Bird Hunting", 35, 34, 1, Vars.VARP_QUEST_CHOMPY, 0, 1, 65), InteractionListener {
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

  override fun defineListeners() {
    val filledBellows = intArrayOf(Items.OGRE_BELLOWS_3_2872, Items.OGRE_BELLOWS_2_2873, Items.OGRE_BELLOWS_1_2874)

    on(3379, SCENERY, "enter") {player, _ -> 
      teleport(player, CAVE_ENTRANCE)
      sendMessage(player, "You walk through the cave entrance into a dimly lit cave.")
      return@on true
    }

    on(intArrayOf(32068, 32069), SCENERY, "pass-through") {player, _ -> 
      teleport(player, CAVE_EXIT)
      sendMessage(player, "You walk back out of the darkness of the cave into daylight.")
      return@on true
    }
    
    on(3377, SCENERY, "unlock") {player, node -> 
      if (freeSlots(player) == 0) {
        sendMessage(player, "You don't have enough space to do that.")
        return@on true
      }
      if (amountInInventory(player, Items.OGRE_BELLOWS_2871) > 0) {
        sendDialogue(player, "I don't need another set of bellows.")
        return@on true
      }
      sendMessage(player, "You strain to lift the rock off the chest.")
      sendChat(player, "Humph!")
      runTask(player, 2) {
        if (Random().nextBoolean()) {
          sendMessage(player, "But it's just too heavy for you.")
          sendMessage(player, "The experience has left you feeling temporarily weakened.")
          sendChat(player, "ARRRGH")
          adjustLevel(player, Skills.STRENGTH, -1)
        } else {
          replaceScenery(node.asScenery(), node.id + 1, 5)
          sendChat(player, "I guess this is what an ogre would call a locked chest.")
          sendItemDialogue(player, Items.OGRE_BELLOWS_2871, "You take the bellows from the chest.")
          addItem(player, Items.OGRE_BELLOWS_2871)
        }
      }

      return@on true
    }

    onUseWith(SCENERY, Items.OGRE_BELLOWS_2871, 684) {player, used, _ -> 
      if (removeItem(player, used.asItem())) {
        lock(player, 2)
        visualize(player, Animations.HUMAN_USING_BELLOWS_1026, Gfx(Graphics.USING_BELLOWS, 80))
        addItem(player, Items.OGRE_BELLOWS_3_2872)
        sendMessage(player, "You fill the bellows with swamp gas.")
      }
      return@onUseWith true
    }

    onUseWith(NPC, filledBellows, NPCs.SWAMP_TOAD_1013) {player, used, with -> 
      val hasTooManyToads = amountInInventory(player, Items.BLOATED_TOAD_2875) >= 3
      
      if (hasTooManyToads) {
        sendMessage(player, "One of your toads manages to escape.")
        removeItem(player, Items.BLOATED_TOAD_2875)
      }
      
      sendChat(player, "Come here toady!") 
      sendMessage(player, "You manage to catch the toad and inflate it with swamp gas.")
      visualize(player, Animations.HUMAN_USING_BELLOWS_1026, Gfx(Graphics.USING_BELLOWS, 80))
      animate(with.asNpc(), Animations.TOAD_INFLATION_1019) 
      runTask(player, 2) {
        if (removeItem(player, used.asItem())) {
          if (filledBellows.isLast(used.id)) {
            addItem(player, Items.OGRE_BELLOWS_2871)
          } else {
            addItem(player, filledBellows.getNext(used.id))
          }
          addItem(player, Items.BLOATED_TOAD_2875)
          runTask(player) { with.asNpc().clear() }
        }
      }
      return@onUseWith true
    }

    onUseWith(ITEM, Items.OGRE_ARROW_SHAFT_2864, Items.FEATHER_314) {player, used, with -> 
      val shaftAmount = amountInInventory(player, used.id)
      val featherAmount = amountInInventory(player, with.id)
      var maxAmount = min(shaftAmount, featherAmount)
      
      submitIndividualPulse(player, object : Pulse(3) {
        override fun pulse() : Boolean {
          val iterAmount = min(maxAmount, 6)
          if (removeItem(player, Item(Items.OGRE_ARROW_SHAFT_2864, iterAmount)) && removeItem(player, Item(Items.FEATHER_314, iterAmount))) 
          {
            addItem(player, Items.FLIGHTED_OGRE_ARROW_2865, iterAmount)
            rewardXP(player, Skills.FLETCHING, 0.9 * iterAmount)
            maxAmount -= iterAmount
          }
          return maxAmount == 0
        }
      })

      return@onUseWith true
    }

    onUseWith(ITEM, Items.WOLF_BONES_2859, Items.CHISEL_1755) {player, used, with -> 
      val maxAmount = amountInInventory(player, used.id)

      if (getStage(player) == 0) {
        sendMessage(player, "You must have started Big Chompy Bird Hunting to make these.")
        return@onUseWith true
      }

      if (getStatLevel(player, Skills.FLETCHING) < 5) {
        sendMessage(player, "You need a Fletching level of 5 to make these.")
        return@onUseWith true
      }

      object : SkillDialogueHandler(player, SkillDialogue.ONE_OPTION, Item(Items.WOLFBONE_ARROWTIPS_2861, 5)){
        override fun create(amount: Int, index: Int) {
          var actualAmount = min(amount, maxAmount)

          submitIndividualPulse(player, object : Pulse(2) {
            override fun pulse() : Boolean {
              if (removeItem(player, Item(used.id))) {
                addItem(player, Items.WOLFBONE_ARROWTIPS_2861, 4)
                rewardXP(player, Skills.FLETCHING, 2.5)
                actualAmount -= 1
              }
              return actualAmount == 0
            }
          })
        }
      }.open()

      return@onUseWith true
    }

    onUseWith(ITEM, Items.WOLFBONE_ARROWTIPS_2861, Items.FLIGHTED_OGRE_ARROW_2865) {player, used, with -> 
      val tips = amountInInventory(player, Items.WOLFBONE_ARROWTIPS_2861)
      val shafts = amountInInventory(player, Items.FLIGHTED_OGRE_ARROW_2865)
      val maxArrows = min(tips, shafts)

      object : SkillDialogueHandler(player, SkillDialogue.ONE_OPTION, Item(Items.OGRE_ARROW_2866, 5)) {
        override fun create(amount: Int, index: Int) {
          var maxActualAmount = min(6 * amount, maxArrows)

          submitIndividualPulse(player, object : Pulse(2) {
            override fun pulse() : Boolean {
              val amountThisIter = min(6, maxActualAmount)
              if (removeItem(player, Item(used.id, amountThisIter)) && removeItem(player, Item(with.id, amountThisIter))) {
                addItem(player, Items.OGRE_ARROW_2866, amountThisIter)
                maxActualAmount -= amountThisIter
                rewardXP(player, Skills.FLETCHING, 6.0)
              }
              return maxActualAmount == 0
            }
          })
        }
      }.open()

      return@onUseWith true
    }

  }
}
