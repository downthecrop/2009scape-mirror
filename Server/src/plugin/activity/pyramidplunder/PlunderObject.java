package plugin.activity.pyramidplunder;

import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.object.GameObject;
import org.crandor.game.world.map.Location;

/**
 * Handles object instances for pyramid plunder
 * @author ceik
 */

public class PlunderObject extends GameObject {
    public boolean playerOpened;
    public boolean snakeCharmed;
    private transient Player player;
    private int thisId;


    public PlunderObject(int id, Location location, Player player) {
        super(id, location);
        this.player = player;
    }


    public boolean isOpenedBy(Player player){
        try {
            //manager.loadList("plunder.tmp");
            PlunderObjectManager manager = player.getPlunderObjectManager();
            return manager.ObjectList.get(manager.ObjectList.indexOf(this)).playerOpened;
        } catch (Exception e){
            return false;
        }
    }
}
