package core.game.node.entity.skill.cooking;

import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.game.node.scenery.Scenery;

public class IntentionalBurnPulse extends StandardCookingPulse {
    int initial,product,amount;
    Player player;
    Scenery object;
    IntentionalBurnPulse(Player player, Scenery object, int initial, int product, int amount){
        super(player,object,initial,product,amount);
        this.initial = initial;
        this.product = product;
        this.amount = amount;
        this.player = player;
        this.object = object;
    }

    @Override
    public boolean checkRequirements() {
        return true;
    }

    @Override
    public boolean reward() {
        if (getDelay() == 1) {
            setDelay(object.getName().toLowerCase().equals("range") ? 5 : 4);
            return false;
        }
        if(cook(player,null,false,initial,product)) {
            amount--;
        }
        return amount < 1;
    }

    @Override
    public boolean cook(Player player, Scenery object, boolean burned, int initial, int product) {
        super.animate();
        Item initialItem = new Item(initial);
        Item productItem = new Item(product);

        if (player.getInventory().remove(initialItem)) {
            player.getInventory().add(productItem);
            player.getPacketDispatch().sendMessage(getMessage(initialItem, productItem, burned));
            player.getAudioManager().send(SOUND);
            return true;
        }
        return false;
    }
}
