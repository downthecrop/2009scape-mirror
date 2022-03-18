package core.game.node.entity.skill.cooking;

import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.game.node.scenery.Scenery;

/**
 * Fixes the half assed previous implementation of cooking Sinew.
 * @author Woah
 */
public class SinewCookingPulse extends StandardCookingPulse {

    public SinewCookingPulse(Player player, Scenery object, int initial, int product, int amount) {
        super(player, object, initial, product, amount);
    }

    @Override
    public boolean checkRequirements() {
        properties = CookableItems.SINEW;
        return super.checkRequirements();
    }

    @Override
    public boolean isBurned(Player player, Scenery object, int food) {
        return false;
    }

    @Override
    public String getMessage(Item food, Item product, boolean burned) {
        return "You dry a piece of beef and extract the sinew.";
    }
}
