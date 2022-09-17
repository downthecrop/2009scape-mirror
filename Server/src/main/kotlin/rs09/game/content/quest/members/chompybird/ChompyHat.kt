package rs09.game.content.quest.members.chompybird

import api.*
import rs09.tools.stringtools.colorize

import org.rs09.consts.Items

import rs09.game.interaction.InteractionListener
import rs09.game.interaction.IntType
import core.game.node.entity.player.Player

import java.util.*

enum class ChompyHat(val id: Int, val kills: Int, val rankName: String) { 
  O_BOWMAN(Items.CHOMPY_BIRD_HAT_2978, 30, "an Ogre Bowman"),
  BOWMAN(Items.CHOMPY_BIRD_HAT_2979, 40, "a Bowman"),
  O_YEOMAN(Items.CHOMPY_BIRD_HAT_2980, 50, "an Ogre Yeoman"),
  YEOMAN(Items.CHOMPY_BIRD_HAT_2981, 70, "a Yeoman"),
  O_MARKSMAN(Items.CHOMPY_BIRD_HAT_2982, 95, "an Ogre Marksman"),
  MARKSMAN(Items.CHOMPY_BIRD_HAT_2983, 125, "a Marksman"),
  O_WOODSMAN(Items.CHOMPY_BIRD_HAT_2984, 170, "an Ogre Woodsman"),
  WOODSMAN(Items.CHOMPY_BIRD_HAT_2985, 225, "a Woodsman"),
  O_FORESTER(Items.CHOMPY_BIRD_HAT_2986, 300, "an Ogre Forester"),
  FORESTER(Items.CHOMPY_BIRD_HAT_2987, 400, "a Forester"),
  O_BOWMASTER(Items.CHOMPY_BIRD_HAT_2988, 550, "an Ogre Bowmaster"),
  BOWMASTER(Items.CHOMPY_BIRD_HAT_2989, 700, "a Bowmaster"),
  O_EXPERT(Items.CHOMPY_BIRD_HAT_2990, 1000, "an Ogre Expert"),
  EXPERT(Items.CHOMPY_BIRD_HAT_2991, 1300, "an Expert"),
  O_DA(Items.CHOMPY_BIRD_HAT_2992, 1700, "an Ogre Dragon Archer"),
  DA(Items.CHOMPY_BIRD_HAT_2993, 2250, "a Dragon Archer"),
  EO_DA(Items.CHOMPY_BIRD_HAT_2994, 3000, "an Expert Ogre Dragon Archer"),
  E_DA(Items.CHOMPY_BIRD_HAT_2995, 4000, "an Expert Dragon Archer");

  companion object { 
    val killMap = values().map { it.kills to it }.toMap()
  
    fun checkForNewRank(player: Player) { 
      val kills = getAttribute(player, "chompy-kills", 0)
      val newRank = killMap[kills] ?: return

      sendDialogueLines(player,
        colorize("%B*** Congratulations! $kills Chompies! ***"),
        colorize("%R~ You're ${newRank.rankName} ~")
      )
    }

    fun getApplicableHats(player: Player) : List<Int> {
      val kills = getAttribute(player, "chompy-kills", 0)
      val hats = ArrayList<Int>()
      for (hat in values()) {
        if (hat.kills > kills) break
        if (inInventory(player, hat.id) || inEquipment(player, hat.id) || inBank(player, hat.id)) continue
        hats.add(hat.id)
      }
      return hats
    }
  }
}

class ChompyEquipListener : InteractionListener {
  override fun defineListeners() {
    ChompyHat.values().forEach { hat -> 
      onEquip(hat.id) {player, node -> 
        val kills = getAttribute(player, "chompy-kills", 0)
        if (kills < hat.kills) {
          sendItemDialogue(player, node.id, "You haven't earned this!")
          removeItem(player, node.asItem())
          return@onEquip false
        }
        return@onEquip true
      }
    }
  }
}
