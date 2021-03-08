package core.game.content.activity.pyramidplunder;

import core.game.node.entity.player.Player;
import core.game.world.map.Location;

import java.util.HashMap;

/**
 * Manages what objects a specific player has interacted with in pyramid plunder.
 * @author ceik
 */

public class PlunderObjectManager{
    public static HashMap<Location,Boolean> openedMap = new HashMap<>();
    public static HashMap<Location, Boolean> charmedMap = new HashMap<>();

    int originalIndex;
    public boolean resetObjectsFor(Player player){
        //Completely clear the mapping and reset objects
        openedMap.clear();
        charmedMap.clear();
        return true;
    }

    public void registerOpened(PlunderObject object){
        openedMap.putIfAbsent(object.getLocation(),true);
    }
    public void registerCharmed(PlunderObject object) { charmedMap.putIfAbsent(object.getLocation(),true);}
}
