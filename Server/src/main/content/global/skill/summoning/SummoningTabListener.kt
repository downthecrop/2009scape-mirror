import content.global.skill.summoning.familiar.BurdenBeast
import content.global.skill.summoning.familiar.FamiliarSpecial
import content.global.skill.summoning.pet.Pet
import core.api.sendMessage
import core.game.interaction.InterfaceListener

class SummoningTabListener : InterfaceListener {
    override fun defineInterfaceListeners() {
        on(662) { player, _, opcode, buttonID, _, _ ->
            when(buttonID) {
                51 -> {
                    if (player.familiarManager.hasFamiliar()) {
                        player.familiarManager.familiar.call()
                    } else {
                        player.getPacketDispatch().sendMessage("You don't have a follower.")
                    }
                }
                67 -> {
                    if (player.familiarManager.hasFamiliar()) {
                        if (player.familiarManager.familiar.isInvisible || !player.familiarManager.familiar.location.withinDistance(player.location)) {
                            sendMessage(player, "Your familiar is too far away!")
                            return@on true
                        }
                        if (!player.familiarManager.familiar.isBurdenBeast()) {
                            player.getPacketDispatch().sendMessage("Your familiar is not a beast of burden.")
                            return@on true
                        }
                        val beast = player.familiarManager.familiar as BurdenBeast
                        if (beast.getContainer().isEmpty()) {
                            player.getPacketDispatch().sendMessage("Your familiar is not carrying any items.")
                            return@on true
                        }
                        beast.withdrawAll()
                        return@on true
                    }
                    player.getPacketDispatch().sendMessage("You don't have a follower.")
                }
                53 -> {
                    if (player.familiarManager.hasFamiliar()) {
                        if(opcode == 155) {
                            // Dismiss familiar
                            player.getDialogueInterpreter().open("dismiss_dial")
                        } else if(opcode == 196) {
                            // Dismiss now
                            if (player.getFamiliarManager().getFamiliar() is Pet) {
                                val pet = player.familiarManager.familiar as Pet
                                player.familiarManager.removeDetails(pet.getItemId())
                            }
                            player.familiarManager.dismiss()
                        }
                    } else {
                        player.getPacketDispatch().sendMessage("You don't have a follower.")
                    }
                }
                else -> {
                    if (player.familiarManager.hasFamiliar()) {
                        player.familiarManager.familiar.executeSpecialMove(FamiliarSpecial(player))
                    } else {
                        player.getPacketDispatch().sendMessage("You don't have a follower.")
                    }
                }
            }
            return@on true
        }
    }
}

