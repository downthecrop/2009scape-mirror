package rs09.game.content.quest.members.thefremenniktrials

import api.sendMessage
import core.game.container.access.BitregisterAssembler
import rs09.game.interaction.InterfaceListener

class SeerLockInterfaceListener : InterfaceListener() {

   override fun defineListeners() {
       val LETTERONEBACK = 39
       val LETTERONEFORWARD = 40
       val LETTERTWOBACK = 35
       val LETTERTWOFORWARD = 36
       val LETTERTHREEBACK = 31
       val LETTERTHREEFORWARD = 32
       val LETTERFOURBACK = 27
       val LETTERFOURFORWARD = 28
       val ENTER = 1
       val EXIT = 2
       val DOORLOCKINTERFACE = 298
       val LETTERS = arrayOf("A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z")
//
       onOpen(DOORLOCKINTERFACE){player, component ->
           player.packetDispatch.sendVarcUpdate(618, 0)
           player.packetDispatch.sendVarcUpdate(619, 0)
           player.packetDispatch.sendVarcUpdate(620, 0)
           player.packetDispatch.sendVarcUpdate(621, 0)
           BitregisterAssembler.send(player,298,2,0,1, BitregisterAssembler(0))
           player.setAttribute("riddle-letter-one",0)
           player.setAttribute("riddle-letter-two",0)
           player.setAttribute("riddle-letter-three",0)
           player.setAttribute("riddle-letter-four",0)
           return@onOpen true
       }
//
       onClose(DOORLOCKINTERFACE){player, component ->
           player.removeAttribute("riddle-letter-one")
           player.removeAttribute("riddle-letter-two")
           player.removeAttribute("riddle-letter-three")
           player.removeAttribute("riddle-letter-four")
           return@onClose true
       }
//
       on(DOORLOCKINTERFACE){player, component, opcode, buttonID, slot, itemID ->
           when(buttonID){
               LETTERONEBACK ->{
                   if(player.getAttribute("riddle-letter-one",0) == 0){
                       player.setAttribute("riddle-letter-one",25)
                   }
                   else{
                       (player.incrementAttribute("riddle-letter-one",-1))
                   }
               }
               LETTERONEFORWARD ->{
                   if(player.getAttribute("riddle-letter-one",0) == 25){
                       player.setAttribute("riddle-letter-one",0)
                   }
                   else{
                       (player.incrementAttribute("riddle-letter-one",1))
                   }
               }
               LETTERTWOBACK ->{
                   if(player.getAttribute("riddle-letter-two",0) == 0){
                       player.setAttribute("riddle-letter-two",25)
                   }
                   else{
                       (player.incrementAttribute("riddle-letter-two",-1))
                   }
               }
               LETTERTWOFORWARD ->{
                   if(player.getAttribute("riddle-letter-two",0) == 25){
                       player.setAttribute("riddle-letter-two",0)
                   }
                   else{
                       (player.incrementAttribute("riddle-letter-two",1))
                   }
               }
               LETTERTHREEBACK ->{
                   if(player.getAttribute("riddle-letter-three",0) == 0){
                       player.setAttribute("riddle-letter-three",25)
                   }
                   else{
                       (player.incrementAttribute("riddle-letter-three",-1))
                   }
               }
               LETTERTHREEFORWARD ->{
                   if(player.getAttribute("riddle-letter-three",0) == 25){
                       player.setAttribute("riddle-letter-three",0)
                   }
                   else{
                       (player.incrementAttribute("riddle-letter-three",1))
                   }
               }
               LETTERFOURBACK ->{
                   if(player.getAttribute("riddle-letter-four",0) == 0){
                       player.setAttribute("riddle-letter-four",25)
                   }
                   else{
                       (player.incrementAttribute("riddle-letter-four",-1))
                   }
               }
               LETTERFOURFORWARD ->{
                   if(player.getAttribute("riddle-letter-four",0) == 25){
                       player.setAttribute("riddle-letter-four",0)
                   }
                   else{
                       (player.incrementAttribute("riddle-letter-four",1))
                   }
               }
               ENTER -> {
                   val letterOne = LETTERS[player.getAttribute("riddle-letter-one", 0)]
                   val letterTwo = LETTERS[player.getAttribute("riddle-letter-two", 0)]
                   val letterThree = LETTERS[player.getAttribute("riddle-letter-three", 0)]
                   val letterFour = LETTERS[player.getAttribute("riddle-letter-four", 0)]
                   when (player.getAttribute("PeerRiddle", 0)) {
                       0 -> {
                           if (letterOne == "L" && letterTwo == "I" && letterThree == "F" && letterFour == "E") {
                               sendMessage(player, "You have solved the riddle!")
                               player.removeAttribute("PeerRiddle")
                               player.setAttribute("/save:riddlesolved", true)
                               player.interfaceManager.close()
                           } else {
                               sendMessage(player, "You have failed to solve the riddle.")
                               player.interfaceManager.close()
                           }
                       }
                       1 -> {
                           if (letterOne == "M" && letterTwo == "I" && letterThree == "N" && letterFour == "D") {
                               sendMessage(player, "You have solved the riddle!")
                               player.removeAttribute("PeerRiddle")
                               player.setAttribute("/save:riddlesolved", true)
                               player.interfaceManager.close()
                           } else {
                               sendMessage(player, "You have failed to solve the riddle.")
                               player.interfaceManager.close()
                           }
                       }
                       2 -> {
                           if (letterOne == "T" && letterTwo == "I" && letterThree == "M" && letterFour == "E") {
                               sendMessage(player, "You have solved the riddle!")
                               player.removeAttribute("PeerRiddle")
                               player.setAttribute("/save:riddlesolved", true)
                               player.interfaceManager.close()
                           } else {
                               sendMessage(player, "You have failed to solve the riddle.")
                               player.interfaceManager.close()
                           }
                       }
                       3 -> {
                           if (letterOne == "W" && letterTwo == "I" && letterThree == "N" && letterFour == "D") {
                               sendMessage(player, "You have solved the riddle!")
                               player.removeAttribute("PeerRiddle")
                               player.setAttribute("/save:riddlesolved", true)
                               player.interfaceManager.close()
                           } else {
                               sendMessage(player, "You have failed to solve the riddle.")
                               player.interfaceManager.close()
                           }
                       }
                   }
               }
           }
           return@on true
       }
   }
}