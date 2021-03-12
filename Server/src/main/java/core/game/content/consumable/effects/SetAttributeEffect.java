package core.game.content.consumable.effects;

import core.game.node.entity.player.Player;
import rs09.game.world.GameWorld;
import core.game.content.consumable.ConsumableEffect;

public class SetAttributeEffect extends ConsumableEffect {
    String attrString;
    Object attrValue;
    boolean isTicks;

    public SetAttributeEffect(String attr, Object value, boolean isTicks){
        this.attrString = attr;
        this.attrValue = value;
        this.isTicks = isTicks;
    }

    public SetAttributeEffect(String attr, Object value){
        this.attrString = attr;
        this.attrValue = value;
        if (value instanceof Integer){
            this.isTicks = true;
        } else {
            this.isTicks = false;
        }
    }

    @Override
    public void activate(Player p) {
        if(isTicks){
            int val = (Integer) attrValue + GameWorld.getTicks();
            p.setAttribute(attrString,val);
            return;
        }
        p.setAttribute(attrString,attrValue);
    }
}
