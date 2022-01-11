package core.game.content.global.worldevents.shootingstar;

import core.game.node.entity.player.Player;
import rs09.game.system.SystemLogger;
import rs09.game.world.GameWorld;

import java.util.ArrayList;
import java.util.List;

public class ScoreboardManager {
    public static List<ScoreboardEntry> entries = new ArrayList<>(20);

    public static void submit(Player player){
        if(entries.size() == 5){
            entries.remove(0);
        }
        entries.add(new ScoreboardEntry(player.getUsername(), GameWorld.getTicks()));
    }

    public static List<ScoreboardEntry> getEntries(){
        return entries;
    }
}
