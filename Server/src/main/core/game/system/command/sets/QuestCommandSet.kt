package core.game.system.command.sets

import core.api.log
import core.game.component.Component
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.QuestRepository
import core.plugin.Initializable
import core.game.system.command.Privilege
import core.game.world.repository.Repository
import core.tools.Log

@Initializable
class QuestCommandSet : CommandSet(Privilege.ADMIN){
    override fun defineCommands() {
        /**
         * Completes all quests
         */
        define("allquest", description = "Instantly completes every quest for your account."){player,_->
            for (quest in QuestRepository.getQuests().values) {
                try {
                    quest.finish(player)
                } catch (e: Exception){
                    e.message?.let { log(this.javaClass, Log.WARN, it) }
                    continue
                }
            }
        }

        /**
         * Displays the currently implemented quests with debug information
         */
        define("quest",
            usage = "::quest [-p username] [quest-filter]",
            description = "Shows quest stages for you, or for [username] if -p is used. Add optional text to filter by quest name."
        ) { player, args ->

            var target = player
            var filter: String? = null

            if (args.size > 1) {
                if (args[1].equals("-p", ignoreCase = true)) {
                    // need at least 3 arguments if -p is used: command, -p, username
                    if (args.size < 3) {
                        reject(player, "ERROR: Missing player name. Usage: ::quest -p <username> [quest-filter]")
                        return@define
                    }

                    val playerName = args[2]
                    target = Repository.getPlayerByName(playerName) ?: run {
                        reject(player, "ERROR: Username '$playerName' not found.")
                        return@define
                    }

                    // arguments after the player name are the quest filter
                    if (args.size > 3) {
                        filter = args.drop(3).joinToString(" ")
                    }
                } else {
                    // no -p, so the arguments are the quest filter
                    filter = args.drop(1).joinToString(" ")
                }
            }
            sendQuestsDebug(player, target, filter)
        }

        /**
         * Sets stage of quest
         */
        define("setqueststage", usage = "::setqueststage <lt>quest-index<gt> <lt>stage<gt>", description = "Sets quest <lt>quest-index<gt> to stage <lt>stage<gt>; use ::quest to find indices."){player,args ->
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
                notify(player, "<col=209dff>Setting " + questObject.quest + " to stage $stage</col>")
            }
        }

        /**
         * Displays the currently implemented quests
         */
        define("quests", Privilege.STANDARD, description = "Opens the quests interface listing current implementations."){ player, _ ->
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
            player.packetDispatch.sendString("<col=ecf0f1>" + (if (q.isCompleted(player)) "<str> " else "") + q.quest + " ", 275, lineId++)
        }
    }

    /**
     * Sends the list of quests with debug information
     * @param admin the player.
     * @param lookupUser the target player to view quests for.
     * @param questFilter optional string to filter quests by name.
     */
    private fun sendQuestsDebug(admin: Player?, lookupUser: Player?, questFilter: String? = null) {
        admin!!.interfaceManager.open(Component(275))

        // clear interface lines
        for (i in 0..310) {
            admin.packetDispatch.sendString("", 275, i)
        }

        var lineId = 11

        // title
        admin.packetDispatch.sendString("<col=ecf0f1>${lookupUser!!.username}'s Quest Debug</col>", 275, 2)

        // filtered quests
        val questsToDisplay = QuestRepository.getQuests().values.filter { q ->
            questFilter.isNullOrBlank() || q.quest.toString().contains(questFilter, ignoreCase = true)
        }

        // no quests to filter
        if (questsToDisplay.isEmpty()) {
            admin.packetDispatch.sendString("<col=ff0000>No quests found matching: $questFilter</col>", 275, lineId++)
            return
        }

        for (q in questsToDisplay) {
            // can't display more than 308 lines
            if (lineId >= 308) {
                admin.packetDispatch.sendString("<col=ff0000>List truncated (too many results).</col>", 275, lineId)
                break
            }

            val stage = lookupUser.questRepository.getStage(q)
            val statusColor = when {
                stage >= 100 -> "80ff00"
                stage in 1..99 -> "ff8400"
                else -> "ff0000"
            }

            admin.packetDispatch.sendString("<col=ecf0f1>${q.quest}</col>", 275, lineId++)
            admin.packetDispatch.sendString("<col=ecf0f1>Index: </col><col=ff1f1f><shad=2>${q.index}</shad></col> | <col=ecf0f1>Stage:</col> <col=$statusColor><shad=2>${stage}</shad></col>", 275, lineId++)
            admin.packetDispatch.sendString("<str>          ", 275, lineId++)
        }
    }
}
