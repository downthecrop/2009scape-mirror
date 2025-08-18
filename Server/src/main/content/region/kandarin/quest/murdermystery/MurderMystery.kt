package content.region.kandarin.quest.murdermystery

import content.data.Quests
import core.api.*
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import org.rs09.consts.Items

// https://www.youtube.com/watch?v=f1Eou5i5hBo at 2:29
// https://www.youtube.com/watch?v=BwO4JhZdQKo at 25:20
// https://www.youtube.com/watch?v=u5Osw_jas4A at 26:35
// https://www.youtube.com/watch?v=f1Eou5i5hBo
// https://www.youtube.com/watch?v=9Q2aLUg44SQ at 10:02
// https://www.youtube.com/watch?v=_lTUessdfOM 6:18

/**
 * Murder Mystery Quest
 */
@Initializable
class MurderMystery : Quest(Quests.MURDER_MYSTERY, 93, 92, 3, 192, 0, 1, 2) {

    companion object {
        const val questName = "Murder Mystery"

        const val attributeRandomMurderer = "/save:quest:murdermystery-randommurderer" //Alphabetical, 0 for Anna, 1 for Bob, 2 for Carol, 3 for David, 4 for Elizabeth, 5 for Frank
        const val attributeTakenThread = "/save:quest:murdermystery-takenthread" //true after taking thread for the first time from window
        const val attributeNoiseClue = "/save:quest:murdermystery-noiseclue" //true on learning of the barking dog for the first time
        const val attributePoisonClue = "/save:quest:murdermystery-poisonclue" //1 after learning poison was bought, 2 after finding liar
        const val attributeAskPoisonAnna = "/save:quest:murdermystery-askpoisonanna" //true after asking Anna about poison
        const val attributeAskPoisonBob = "/save:quest:murdermystery-askpoisonbob" //true after asking Bob about poison
        const val attributeAskPoisonCarol = "/save:quest:murdermystery-askpoisoncarol" //true after asking Carol about poison
        const val attributeAskPoisonDavid = "/save:quest:murdermystery-askpoisondavid" //true after asking David about poison
        const val attributeAskPoisonElizabeth = "/save:quest:murdermystery-askpoisonelizabeth" //true after asking Elizabeth about poison
        const val attributeAskPoisonFrank = "/save:quest:murdermystery-askpoisonfrank" //true after asking Anna about poison

        fun clueCount(player: Player) : Int {
            var count = 0
            if (inInventory(player, Items.CRIMINALS_THREAD_1808) || inInventory(player, Items.CRIMINALS_THREAD_1809) || inInventory(player, Items.CRIMINALS_THREAD_1810)) {
                count++
            }
            if (inInventory(player, Items.KILLERS_PRINT_1815)) {
                count ++
            }
            if (getAttribute(player, attributePoisonClue,0) == 2) {
                count ++
            }
            return count
        }

        fun solvedMystery(player: Player) : Boolean {
            return (
                ( inInventory(player, Items.CRIMINALS_THREAD_1808) || inInventory(player, Items.CRIMINALS_THREAD_1809) || inInventory(player, Items.CRIMINALS_THREAD_1810) )
                && inInventory(player, Items.KILLERS_PRINT_1815)
                && getAttribute(player, attributePoisonClue,0) == 2
                && getAttribute(player, attributeNoiseClue, false)
            )
        }
    }
    override fun drawJournal(player: Player, stage: Int) {
        super.drawJournal(player, stage)
        var line = 12
        var stage = getStage(player)

        var started = getQuestStage(player, Quests.MURDER_MYSTERY) > 0

        if (!started) {
            line(player, "I can start this quest by speaking to one of the !!Guards?? at", line++, false)
            line(player, "the !!Sinclair Mansion??, North of the !!Seer's Village??.", line++, false)
        } else if (stage < 100) {
            line(player, "Lord Sinclair, a prominent nobleman, had been horribly", line++, true)
            line(player, "murdered at his mansion. The guards had been sent to", line++, true)
            line(player, "investigate his murder, but have been completely stuck.", line++, true)

            if (solvedMystery(player)) {
                line(player, "One of the guards asked me for my help in solving the", line++, true)
                line(player, "murder. After careful examination of the crime scene and", line++, true)
                line(player, "interrogating all suspects, I worked out who was guilty.", line++, true)
            } else {
                line(player, "One of the !!guards?? has asked me for my help in solving the", line++, false)
                line(player, "murder. I should !!examine the crime scene?? very closely for", line++, false)
                line(player, "evidence, and !!interrogate everybody?? in the area carefully.", line++, false)
            }

            // This may not be a stage but an attribute when all the evidence is collected.
            if (solvedMystery(player)) {
                line(player, "I have !!indisputable evidence?? of who the murderer must be.", line++, false)
                line(player, "I should take it to one of the !!Guards?? immediately.", line++, false)
            } else {
                line++
                if (inInventory(player, Items.CRIMINALS_THREAD_1808) || inInventory(player, Items.CRIMINALS_THREAD_1809) || inInventory(player, Items.CRIMINALS_THREAD_1810)) {
                    line(player, "I have found some !!coloured thread??. It might be useful.", line++, false)
                }
                if (inInventory(player, Items.CRIMINALS_DAGGER_1813) || inInventory(player, Items.CRIMINALS_DAGGER_1814)) {
                    line(player, "I have taken the !!murder weapon??. I think it might help me.", line++, false)
                }
                if (inInventory(player, Items.PUNGENT_POT_1812)) {
                    line(player, "I have a !!strange smelling pot??. It seems like a clue.", line++, false)
                }
            }
        } else {
            line(player, "One of the guards asked me for my help in solving the", line++, true)
            line(player, "murder. After careful examination of the crime scene and", line++, true)
            line(player, "interrogating all suspects, I worked out who was guilty.", line++, true)
            line(player, "I took the evidence I had collected to the Guards and", line++, true)
            line(player, "explained how it could identify the killer. Impressed", line++, true)
            line(player, "with my deductions, the killer was arrested and I was", line++, true)
            line(player, "given a fair reward for my help in solving the crime.", line++, true)
            line++
            line(player,"<col=FF0000>QUEST COMPLETE!</col>", line)
        }

    }

    override fun reset(player: Player) {
        removeAttribute(player, attributeRandomMurderer)
        removeAttribute(player, attributeNoiseClue)
        removeAttribute(player, attributePoisonClue)
        removeAttribute(player, attributeTakenThread)
        removeAttribute(player, attributeAskPoisonAnna)
        removeAttribute(player, attributeAskPoisonBob)
        removeAttribute(player, attributeAskPoisonCarol)
        removeAttribute(player, attributeAskPoisonDavid)
        removeAttribute(player, attributeAskPoisonElizabeth)
        removeAttribute(player, attributeAskPoisonFrank)
    }

    override fun finish(player: Player) {
        var ln = 10
        super.finish(player)
        player.packetDispatch.sendString("You have completed the Murder Mystery quest!", 277, 4)
        player.packetDispatch.sendItemZoomOnInterface(Items.COINS_617, 230, 277, 5)

        drawReward(player, "3 Quest Points", ln++)
        drawReward(player, "2000 coins", ln++)
        drawReward(player, "1406 Crafting XP", ln++)

        addItem(player, Items.COINS_995, 2000)
        rewardXP(player, Skills.CRAFTING, 1406.0)

        removeAttribute(player, attributeNoiseClue)
        removeAttribute(player, attributePoisonClue)
        removeAttribute(player, attributeTakenThread)
        removeAttribute(player, attributeAskPoisonAnna)
        removeAttribute(player, attributeAskPoisonBob)
        removeAttribute(player, attributeAskPoisonCarol)
        removeAttribute(player, attributeAskPoisonDavid)
        removeAttribute(player, attributeAskPoisonElizabeth)
        removeAttribute(player, attributeAskPoisonFrank)
    }

    override fun newInstance(`object`: Any?): Quest {
        return this
    }
}