package content.region.kandarin.seers.dialogue

import core.game.dialogue.DialoguePlugin
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.player.link.diary.AchievementDiary
import core.game.node.item.Item
import core.plugin.Initializable
import org.rs09.consts.Items
import core.ServerStore
import core.ServerStore.Companion.getBoolean
import org.json.simple.JSONObject

@Initializable
class GeoffreyDialogue(player: Player? = null) : DialoguePlugin(player) {
    override fun open(vararg args: Any?): Boolean {
        //determine reward level that has been claimed
        var gotoStage = 0
        //want the highest value, so this is checked hardest->easiest
        if (AchievementDiary.hasClaimedLevelRewards(player,DiaryType.SEERS_VILLAGE,2)) {
            gotoStage = 102
        }
        else if (AchievementDiary.hasClaimedLevelRewards(player,DiaryType.SEERS_VILLAGE,1)) {
            gotoStage = 101
        }
        else if (AchievementDiary.hasClaimedLevelRewards(player,DiaryType.SEERS_VILLAGE,0)) {
            gotoStage = 100
        }
        //give reward, or proceed to normal dialogue
        if (gotoStage != 0) {
            player("Hello there. Are you Geoff-erm-Flax? I've been told that", "you'll give me some flax.")
            //Already claimed flax else no room else give correct reward
            stage = if (getStoreFile().getBoolean(player.username.lowercase())) { 98 }
                    else if (!player.inventory.hasSpaceFor(Item(Items.FLAX_1780, 1))) { 99 }
                    else { gotoStage }
        }
        //If the diary has not been completed
        else {
            player("Hello there. You look busy.")
            stage = 0
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return GeoffreyDialogue(player)
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            999 -> end()
            0 -> npc("Yes, I am very busy. Picking GLORIOUS flax.", "The GLORIOUS flax won't pick itself. So I pick it.", "I pick it all day long.").also { stage++ }
            1 -> player("Wow, all that flax must really mount up.", "What do you do with it all?").also { stage++ }
            2 -> npc("I give it away! I love picking the GLORIOUS flax,", "but, if I let it all mount up, I wouldn't have any", "room for more GLORIOUS flax.").also { stage++ }
            3 -> player("So, you're just picking the flax for fun? You must", "really like flax.").also { stage++ }
            4 -> npc("'Like' the flax? I don't just 'like' flax. The", "GLORIOUS flax is my calling, my reason to live.", "I just love the feeling of it in my hands!").also { stage++ }
            5 -> player("Erm, okay. Maybe I can have some of your spare flax?").also { stage++ }
            6 -> npc("No. I don't trust outsiders. Who knows what depraved", "things you would do with the GLORIOUS flax? Only", "locals know how to treat it right.").also { stage++ }
            7 -> player("I know this area! It's, erm, Seers' Village. There's", "a pub and, er, a bank.").also { stage++ }
            8 -> npc("Pah! You call that local knowledge? Perhaps if you", "were wearing some kind of item from one of the", "seers, I might trust you.").also { stage = 999 }

            98 -> npc("Don't be greedy. Other people want GLORIOUS flax too.", "You can have some more tomorrow.").also { stage = 999 } // TODO find accurate dialogue, no source found yet, so I'm using the modern RS3 dialogue from wiki source (https://runescape.wiki/w/Transcript:Geoffrey)
            99 -> npc("Yes, but your inventory is full. Come back", "when you have some space for GLORIOUS flax.").also { stage = 999 } // TODO find accurate dialogue
            100 -> {rewardFlax(30, "Yes. The seers have instructed me to give you an", "allowance of 30 GLORIOUS flax a day. I'm not going", "to argue with them, so here you go.")} // TODO find accurate dialogue
            101 -> {rewardFlax(60, "Yes. Stankers has instructed me to give you an", "allowance of 60 GLORIOUS flax a day. I'm not going", "to argue with a dwarf, so here you go.")} // TODO find accurate dialogue
            102 -> {rewardFlax(120, "Yes. Sir Kay has instructed me to give you an", "allowance of 120 GLORIOUS flax a day. I'm not going", "to argue with a knight, so here you go.")}
        }
        return true
    }

    fun rewardFlax(n: Int, vararg messages: String): Unit {
        npc(*messages)
        player.inventory.add(Item(Items.FLAX_1780, n))
        getStoreFile()[player.username.toLowerCase()] = true
        stage = 999
    }

    override fun getIds(): IntArray {
        return intArrayOf(8590)
    }

    fun getStoreFile(): JSONObject {
        return ServerStore.getArchive("daily-seers-flax")
    }

}