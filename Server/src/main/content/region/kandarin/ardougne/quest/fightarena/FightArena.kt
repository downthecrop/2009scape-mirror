package content.region.kandarin.ardougne.quest.fightarena

import core.api.addItemOrDrop
import core.api.rewardXP
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import org.rs09.consts.Items.COINS_995

@Initializable
class FightArena : Quest("Fight Arena", 61, 60, 2, 17, 0, 1, 14) {

    //------------------------------------------------------------
    // Version: 1.0.3
    // Source: https://runescape.wiki/w/Fight_Arena?oldid=1360259
    // ------------------------------------------------------------
    override fun newInstance(`object`: Any?): Quest { return this }
    companion object { const val FightArenaQuest = "Fight Arena" }
    override fun drawJournal(player: Player?, stage: Int) {
        super.drawJournal(player, stage)
        var line = 11
        player ?: return
        if (stage == 0) {
            line(player, "I can start this quest by speaking to !!Lady Servil?? just", line++)
            line(player, "!!North-West?? of the !!Khazard Port??.", line++)
            line += 1
            line(player, "I must be able to defeat a !!level 137?? enemy.", line++)
            line++
        }
        if (stage >= 10) {
            line(player, "I encountered a distraught Lady Servil,", line++, stage > 10)
            line(player, "who said that her son and husband", line++, stage > 10)
            line(player, "had been kidnapped by the evil General Khazard,", line++, stage > 10)
            line(player, "and were being forced to fight in his fight arena.", line++, stage > 10)
            line += 1
            line(player, "I headed to the arena to try and find Lady Servil's son and husband.", line++, stage > 10)
            line++
        }
        if (stage >= 20) {
            line(player, "I found some Khazard armour in the armoury, on", line++, stage > 20)
            line(player, "the north-east edge of town.", line++, stage > 20)
            line(player, "I used it to disguise myself as a guard so I could look around", line++, stage > 20)
            line++
        }
        if (stage >= 35) {
            line(player, "I found Lady Servil's son,", line++, stage > 40)
            line(player, "Jeremy Servil, in one of the prison cells.", line++, stage > 40)
            line(player, "He told me that a bald, fat, lazy guard with a goatee", line++, stage > 40)
            line(player, "was in charge of the keys.", line++, stage > 40)
            line++
        }
        if (stage >= 50) {
            line(player, "I found the guard Jeremy mentioned.", line++, stage > 55)
            line(player, "He said that he'd like a drink,", line++, stage > 55)
            line(player, "but too much Khali brew would make him fall asleep.", line++, stage > 55)
            line++
        }
        if (stage >= 60) {
            line(player, "I plied the lazy guard", line++, stage > 67)
            line(player, "with some Khali brew and he passed out.", line++, stage > 67)
            line(player, "I was able to get his keys.", line++, stage > 67)
            line++
        }
        if (stage >= 68) {
            line(player, "I found and freed Jeremy, who told me that", line++, stage > 69)
            line(player, "his father had been taken to fight in the Fight Arena.", line++, stage > 69)
            line(player, "We went there to save him.", line++, stage > 69)
            line++
        }
        if (stage >= 71) {
            line(player, "I had to fight a large ogre to stop it killing Sir Servil.", line++, stage > 72)
            line(player, "When I'd defeated it, General Khazard had locked me up.", line++, stage > 72)
            line++
        }
        if (stage >= 88) {
            line(player, "I was led to the fight arena", line++, stage > 88)
            line(player, "and forced to fight a colossal scorpion,", line++, stage > 89)
            line(player, "followed by Khazard's monstrous pet dog called Bouncer.", line++, stage > 90)
            line++
        }
        if (stage >= 91) {
            line(player, "General Khazard released the Servils, but he was so angry", line++, stage >= 99)
            line(player, "that I'd killed Bouncer that he came to fight me himself.", line++, stage >= 99)
            line++
        }
        if (stage >= 99) {
            line(player, "I escaped from the arena and returned to Lady Servil", line++, stage == 100)
            line(player, "She thanked me profusely and rewarded me for my help.", line++, stage == 100)
        }
        if (stage == 100) {
            line++
            line(player, "!!QUEST COMPLETE??", line, true)
        }
    }

    override fun finish(player: Player?) {
        super.finish(player)
        player ?: return
        var ln = 10

        player.packetDispatch.sendItemZoomOnInterface(COINS_995, 230, 277, 5)

        drawReward(player, "2 Quest Point", ln++)
        drawReward(player, "12,175 Attack XP", ln++)
        drawReward(player, "2,175 Thieving XP", ln++)
        drawReward(player, "1000 gold coins", ln)

        rewardXP(player, Skills.ATTACK, 12175.0)
        rewardXP(player, Skills.THIEVING, 2175.0)
        addItemOrDrop(player, COINS_995, 1000)
    }
}