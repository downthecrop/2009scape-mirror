package core.game.system.command.sets

import core.game.component.Component
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.QuestRepository
import core.plugin.Initializable
import core.game.system.command.Privilege
import core.game.world.repository.Repository

@Initializable
class QuestCommandSet : CommandSet(Privilege.ADMIN){
    override fun defineCommands() {
        /**
         * Completes all quests
         */
        define("allquest"){player,_->
            for (quest in QuestRepository.getQuests().values) {
                quest.finish(player)
            }
        }

        /**
         * Displays the currently implemented quests with debug information
         */
        define("quest"){player,args ->
            if (args.size < 3) {
                val lookupP = if (args.size == 1) {
                    player
                } else if (Repository.getPlayerByName(args[1]) != null) {
                    Repository.getPlayerByName(args[1]) ?: return@define
                } else {
                    reject(player, "ERROR: Username not found. Usage: ::quest <username>")
                    return@define
                }
                sendQuestsDebug(player, lookupP)
            } else {
                reject(player, "Usage: ::quest || ::quest <username>")
            }
        }

        /**
         * Sets stage of quest
         */
        define("setqueststage"){player,args ->
            if (args.size < 3) {
                reject(player,"You must specify the index# of a quest, and a stage number")
            }
            val quest = args[1].toIntOrNull() ?: reject(player,"INVALID QUEST")
            val stage = args[2].toIntOrNull() ?: reject(player,"INVALID STAGE")
            val questObject = player.questRepository.forIndex(quest as Int)
            if (questObject == null){
                reject(player, "$quest did not match anything in the quest repository")
            }
            else{
                player.questRepository.setStageNonmonotonic(questObject, stage as Int)
                if (stage == 0) {
                    questObject.reset(player)
                }
                questObject.updateVarps(player)
                notify(player, "<col=209dff>Setting " + questObject.name + " to stage $stage</col>")
            }
        }

        /**
         * Displays the currently implemented quests
         */
        define("quests", Privilege.STANDARD){ player, _ ->
            sendQuests(player)
        }
    }

    /**
     * Sends the list of quests.
     * @param player the player.
     */
    private fun sendQuests(player: Player?) {
        player!!.interfaceManager.open(Component(275))
        for (i in 0..310) {
            player.packetDispatch.sendString("", 275, i)
        }
        var lineId = 11
        player.packetDispatch.sendString("<col=ecf0f1>" + "Available Quests" + "</col>", 275, 2)
        for (q in QuestRepository.getQuests().toSortedMap().values) {
            // Add a space to beginning and end of string for the strikethrough
            player.packetDispatch.sendString("<col=ecf0f1>" + (if (q.isCompleted(player)) "<str> " else "") + q.name + " ", 275, lineId++)
        }
    }

    /**
     * Sends the list of quests with debug information
     * @param admin the player.
     */
    private fun sendQuestsDebug(admin: Player?, lookupUser: Player?) {
        admin!!.interfaceManager.open(Component(275))
        for (i in 0..310) {
            admin.packetDispatch.sendString("", 275, i)
        }
        var lineId = 11
        admin.packetDispatch.sendString("<col=ecf0f1>${lookupUser!!.username}'s Quest Debug</col>", 275, 2)
        for (q in QuestRepository.getQuests().values) {
            // Add a space to beginning and end of string for the strikethrough
            val stage = lookupUser.questRepository.getStage(q)
            val statusColor = when {
                stage >= 100 -> "80ff00"
                stage in 1..99 -> "ff8400"
                else -> "ff0000"
            }
            admin.packetDispatch.sendString("<col=ecf0f1>${q.name}</col>", 275, lineId++)
            admin.packetDispatch.sendString("<col=ecf0f1>Index: </col><col=ff1f1f><shad=2>${q.index}</shad></col> | <col=ecf0f1>Stage:</col> <col=$statusColor><shad=2>${lookupUser.questRepository.getStage(q)}</shad></col>", 275, lineId++)
            admin.packetDispatch.sendString("<str>          ", 275, lineId++)
        }
    }
}
