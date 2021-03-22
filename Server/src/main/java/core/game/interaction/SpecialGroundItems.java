package core.game.interaction;

import core.game.interaction.city.portsarim.AhabBeerInteraction;
import core.game.node.item.GroundItem;
import core.game.node.item.GroundItemManager;
import core.game.world.map.Location;

/**
 * Handles interactions for special ground items
 * @author ceik
 */
public enum SpecialGroundItems {
    //Ahab's beer
    AHAB_BEER(1917,new Location(3049,3257,0), new AhabBeerInteraction());

    private int itemid;
    private Location location;
    private SpecialGroundInteraction inter;

    SpecialGroundItems(int itemId, Location location, SpecialGroundInteraction inter){
        this.itemid = itemId;
        this.location = location;
        this.inter = inter;
    }

    public SpecialGroundInteraction getInteraction() { return inter;}
    public int getItemid() {return itemid;}
    public Location getLocation() {return location;}
    public GroundItem asGroundItem(){return GroundItemManager.get(itemid,location,null);}
}
