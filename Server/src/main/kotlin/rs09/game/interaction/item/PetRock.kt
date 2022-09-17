package rs09.game.interaction.item

import api.openDialogue
import core.game.content.dialogue.FacialExpression
import org.rs09.consts.Items
import rs09.game.content.dialogue.DialogueFile
import rs09.game.interaction.InteractionListener
import rs09.game.interaction.IntType
import rs09.tools.END_DIALOGUE

class PetRockPlugin : InteractionListener {
    override fun defineListeners() {
      on(Items.PET_ROCK_3695, IntType.ITEM, "interact"){ player, _ ->
        openDialogue(player, PetRockDialogue())
        return@on true
      }
    }
}

class PetRockDialogue() : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        when(stage) {
          0 -> options("Talk", "Stroke", "Feed", "Fetch", "Stay").also { stage++ }
          1 -> {
            when(buttonID) {
              1 -> {
                  playerl(FacialExpression.FRIENDLY, "Who's a good rock then? Yes you are... You're such a good rock... ooga booga googa.")
                  player!!.sendMessage("Your rock seems a little happier.")
                  stage = END_DIALOGUE
              }
              2 -> {
                  player!!.sendMessage("You stroke your pet rock.")
                  // Missing animation
                  player!!.sendMessage("Your rock seems much happier.")
                  end()
              }
              3 -> {
                  player!!.sendMessage("You try and feed the rock.")
                  player!!.sendMessage("Your rock doesn't seem hungry.")
                  end()
              }
              4 -> {
                  playerl(FacialExpression.FRIENDLY, "Want to fetch the stick, rock? Of course you do...")
                  // Missing animation
                  stage = END_DIALOGUE
              }
              5 -> {
                  playerl(FacialExpression.FRIENDLY, "Be a good rock...")
                  player!!.sendMessage("You wait a few seconds and pick your rock back up and pet it.")
                  // Missing animation
                  stage = END_DIALOGUE
              }
            }
          }
        }
    }
}