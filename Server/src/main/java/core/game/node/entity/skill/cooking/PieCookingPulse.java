package core.game.node.entity.skill.cooking;

import core.cache.def.impl.ItemDefinition;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.game.node.scenery.Scenery;

public class PieCookingPulse extends StandardCookingPulse {
    private Scenery object;
    private Player player;
    public PieCookingPulse(Player player, Scenery object, int initial, int product, int amount){
        super(player,object,initial,product,amount);
        this.object = object;
        this.player = player;
    }

    @Override
    public boolean checkRequirements() {
        if(!object.getName().toLowerCase().contains("range")){
            player.getPacketDispatch().sendMessage("This can only be cooked on a range.");
            return false;
        }
        return super.checkRequirements();
    }

    @Override
    public String getMessage(Item food, Item product, boolean burned) {
        if(burned){
            return "You accidentally burn the pie.";
        } else {
            return "You successfully bake a delicious " + ItemDefinition.forId(product.getId()).getName().toLowerCase() + ".";
        }
    }
}
