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
import rs09.game.interaction.IntType
import rs09.game.content.dialogue.SkillDialogueHandler
import rs09.game.content.dialogue.SkillDialogueHandler.SkillDialogue
import rs09.game.system.config.ItemConfigParser
import rs09.game.system.config.NPCConfigParser
import rs09.game.world.GameWorld

import kotlin.math.min
import java.util.Random

@Initializable
class ChompyBird : Quest("Big Chompy Bird Hunting", 35, 34, 2, Vars.VARP_QUEST_CHOMPY, 0, 1, 65), InteractionListener {
  companion object {
    val CAVE_ENTRANCE = Location.create(2646, 9378, 0)
    val CAVE_EXIT = Location.create(2630, 2997, 0) 
    val TOAD_LOCATION = Location.create(2636, 2966, 0)
    val ATTR_ING_RANTZ = "/save:chompybird:rantz-ingredient"
    val ATTR_ING_BUGS  = "/save:chompybird:bugs-ingredient"
    val ATTR_ING_FYCIE = "/save:chompybird:fycie-ingredient"
    val ATTR_BUGS_ASKED = "/save:chompybird:bugs-asked"
    val ATTR_FYCIE_ASKED = "/save:chompybird:fycie-asked"
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
      if (stage == 10) {
        line(player, "Rantz needs me to make 'stabbers'. To do this I need:", ln++, false)
        line(player, "- !!Achey Logs??", ln++, false)
        line(player, "- !!Wolf Bones??", ln++, false)
        line(player, "- !!Feathers??", ln++, false)
        line(player, "I then must turn the !!achey logs?? into !!ogre shafts??,", ln++, false)
        line(player, "attach !!feathers?? to these !!shafts??, and then tip them", ln++, false)
        line(player, "with !!wolf bones?? chiseled into !!tips??.", ln++, false)
        line(player, "At least, that's what I think he was getting at.", ln++, false)
      } else if (stage > 10) {
        line(player, "I created 'stabbers' for Rantz.", ln++, true)
      }

      if (stage == 20) {
        line(player, "Rantz needs me to obtain a bloated swamp toad.", ln++, false)
        line(player, "To do this, I need to take !!billows?? from the !!locked", ln++, false)
        line(player, "!!chest?? in his cave, and then head to the !!swamp to", ln++, false)
        line(player, "!!the south??. There, I should !!use the billows?? on the", ln++, false)
        line(player, "!!swamp bubbles?? to fill them with swamp gas. Then I can", ln++, false)
        line(player, "use the !!billows?? to fill the !!swamp toads?? with gas.", ln++, false)
      } else if (stage > 20) {
        line(player, "I learned how to collect swamp gas and conduct toad inflation.", ln++, true)
      }

      if (stage == 30) {
        line(player, "Rantz needs me to place the swamp toad to bait out a 'chompy'.", ln++, false)
      } else if (stage > 30) {
        line(player, "I learned how to use the toads to bait chompies.", ln++, true)
      }

      if (stage == 40) {
        line(player, "I should return to Rantz and let him know.", ln++, false)
      }

      if (stage == 50) {
        line(player, "Rantz keeps missing the birds. Perhaps I should try.", ln++, false)
      } else if (stage > 50) {
        line(player, "Rantz gave me his bow so that I could try to catch a chompy.", ln++, true)
      }

      if (stage == 60) {
        line(player, "I should use what I've learned to try to bait and kill a chompy bird.", ln++, false)
      }

      if (stage == 70) {
        line(player, "I managed to hunt and kill a chompy bird, and now Rantz wants", ln++, false)
        line(player, "me to cook the bird for him! And to make it even worse, he and", ln++, false)
        line(player, "his children want special ingredients! Those are listed below:", ln++, false)
        line(player, "- Rantz wants: !!${getItemName(getAttribute(player, ATTR_ING_RANTZ, -1))}??", ln++, false)
        line(player, "- ${if(getAttribute(player, ATTR_BUGS_ASKED, false)) "Bugs wants: !!${getItemName(getAttribute(player, ATTR_ING_BUGS, -1))}??" else "I still need to ask !!Bugs??."}", ln++, false)
        line(player, "- ${if(getAttribute(player, ATTR_FYCIE_ASKED, false)) "Fycie wants: !!${getItemName(getAttribute(player, ATTR_ING_FYCIE, -1))}??" else "I still need to ask !!Fycie??."}", ln++, false)
      } else if (stage > 70) {
        line(player, "I seasoned and cooked the chompy bird for Rantz and his kids.", ln++, true)
        line(player, "!!QUEST COMPLETE!??", ln++, false)
      }
    }
  }

  override fun finish(player: Player?) {
    super.finish(player)
    player ?: return

    var ln = 10
    player.packetDispatch.sendItemZoomOnInterface(Items.OGRE_BOW_2883, 230, 277, 5)
    drawReward(player, "2 Quest Points, 262 Fletching", ln++)
    drawReward(player, "XP, 1470 Cooking XP, 735", ln++)
    drawReward(player, "Ranged XP", ln++)
    drawReward(player, "Ogre Bow", ln++)
    drawReward(player, "Ability to make Ogre Arrows", ln++)
    rewardXP(player, Skills.FLETCHING, 262.0)
    rewardXP(player, Skills.COOKING, 1470.0)
    rewardXP(player, Skills.RANGE, 735.0)
    removeItem(player, Items.SEASONED_CHOMPY_2882)
    removeAttribute(player, ATTR_ING_BUGS)
    removeAttribute(player, ATTR_BUGS_ASKED)
    removeAttribute(player, ATTR_ING_RANTZ)
    removeAttribute(player, ATTR_ING_FYCIE)
    removeAttribute(player, ATTR_FYCIE_ASKED)
  }

  override fun newInstance(`object`: Any?): Quest { return this }

  override fun defineListeners() {
    val filledBellows = intArrayOf(Items.OGRE_BELLOWS_3_2872, Items.OGRE_BELLOWS_2_2873, Items.OGRE_BELLOWS_1_2874)

    on(3379, IntType.SCENERY, "enter") {player, _ -> 
      teleport(player, CAVE_ENTRANCE)
      sendMessage(player, "You walk through the cave entrance into a dimly lit cave.")
      return@on true
    }

    on(intArrayOf(32068, 32069), IntType.SCENERY, "pass-through") {player, _ -> 
      teleport(player, CAVE_EXIT)
      sendMessage(player, "You walk back out of the darkness of the cave into daylight.")
      return@on true
    }
    
    on(3377, IntType.SCENERY, "unlock") {player, node -> 
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

    onUseWith(IntType.SCENERY, Items.RAW_CHOMPY_2876, 3375) {player, used, _ -> 
      val rantzIngredient = getAttribute(player, ATTR_ING_RANTZ, -1)
      val bugsIngredient = getAttribute(player, ATTR_ING_BUGS, -1)
      val fycieIngredient = getAttribute(player, ATTR_ING_FYCIE, -1)

      if (rantzIngredient == -1) {
        sendDialogue(player, "I don't have a reason to do this yet.")
        return@onUseWith true
      }

      if (
        amountInInventory(player, rantzIngredient) > 0
        && amountInInventory(player, bugsIngredient) > 0
        && amountInInventory(player, fycieIngredient) > 0
      ) {
        lock(player, 5)
        setVarbit(player, Vars.VARBIT_QUEST_CHOMPY_SPITROAST, 1)
        animate(player, Animations.HUMAN_COOKING_RANGE_896)
        sendMessage(player, "You carefully place the chompy bird on the spit-roast.")
        sendMessage(player, "You add the other ingredients and cook the food.")
        runTask(player, 4) {
          setVarbit(player, Vars.VARBIT_QUEST_CHOMPY_SPITROAST, 0)
          sendItemDialogue(
            player,
            Items.SEASONED_CHOMPY_2882,
            "You use the ${getItemName(rantzIngredient).lowercase()}, ${getItemName(bugsIngredient).lowercase()} and the ${getItemName(fycieIngredient)} with the chompy bird to make a seasoned chompy."
          )
          if (
            removeItem(player, used.asItem())
            && removeItem(player, rantzIngredient)
            && removeItem(player, bugsIngredient)
            && removeItem(player, fycieIngredient)
          ) addItem(player, Items.SEASONED_CHOMPY_2882)
          sendMessage(player, "Eventually the chompy is cooked")
          sendMessage(player, "It has been deliciously seasoned to taste wonderful for ogres.")
        }
      } else {
        sendDialogue(player, "I don't have all the ingredients I need yet.")
        player.debug("Required Items: $rantzIngredient, $bugsIngredient, $fycieIngredient")
      }

      return@onUseWith true
    }

    onUseWith(IntType.SCENERY, Items.OGRE_BELLOWS_2871, 684) {player, used, _ -> 
      if (removeItem(player, used.asItem())) {
        lock(player, 2)
        visualize(player, Animations.HUMAN_USING_BELLOWS_1026, Gfx(Graphics.USING_BELLOWS, 80))
        addItem(player, Items.OGRE_BELLOWS_3_2872)
        sendMessage(player, "You fill the bellows with swamp gas.")
      }
      return@onUseWith true
    }

    onUseWith(IntType.NPC, filledBellows, NPCs.SWAMP_TOAD_1013) {player, used, with ->
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
          with.asNpc().respawnTick = GameWorld.ticks + with.asNpc().definition.getConfiguration(NPCConfigParser.RESPAWN_DELAY, 34)
        }
      }
      return@onUseWith true
    }

    onUseWith(IntType.ITEM, Items.OGRE_ARROW_SHAFT_2864, Items.FEATHER_314) {player, used, with -> 
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

    onUseWith(IntType.ITEM, Items.WOLF_BONES_2859, Items.CHISEL_1755) {player, used, with -> 
      val maxAmount = amountInInventory(player, used.id)

      if (getStage(player) == 0) {
        sendMessage(player, "You must have started Big Chompy Bird Hunting to make these.")
        return@onUseWith true
      }

      if (getStatLevel(player, Skills.FLETCHING) < 5) {
        sendMessage(player, "You need a Fletching level of 5 to make these.")
        return@onUseWith true
      }

      fun process() {
        if (removeItem(player, Item(used.id))) {
          addItem(player, Items.WOLFBONE_ARROWTIPS_2861, 4)
          rewardXP(player, Skills.FLETCHING, 2.5)
        }
      }

      sendSkillDialogue(player) {
        create { id, amount -> 
          var actualAmount = min(amount, maxAmount)
          runTask(
            player,
            delay = 2,
            repeatTimes = actualAmount,
            task = ::process
          )
        }
        withItems(Item(Items.WOLFBONE_ARROWTIPS_2861, 5))
      }

      return@onUseWith true
    }

    onUseWith(IntType.ITEM, Items.WOLFBONE_ARROWTIPS_2861, Items.FLIGHTED_OGRE_ARROW_2865) {player, used, with -> 
      fun getMaxAmount(_unused: Int = 0): Int {
          val tips = amountInInventory(player, Items.WOLFBONE_ARROWTIPS_2861)
          val shafts = amountInInventory(player, Items.FLIGHTED_OGRE_ARROW_2865)
          return min(tips, shafts)
      }     

      fun process() {
        val amountThisIter = min(6, getMaxAmount())
        if (removeItem(player, Item(used.id, amountThisIter)) && removeItem(player, Item(with.id, amountThisIter))) {
          addItem(player, Items.OGRE_ARROW_2866, amountThisIter)
          rewardXP(player, Skills.FLETCHING, 6.0)
        }
      }

      sendSkillDialogue(player) {
        create { id, amount -> 
          runTask(
            player,
            delay = 2,
            repeatTimes = min(amount, getMaxAmount() / 6 + 1),
            task = ::process
          )
        }
        calculateMaxAmount(::getMaxAmount)
        withItems(Item(Items.OGRE_ARROW_2866, 5))
      }
      return@onUseWith true
    }

  }
}
