package core.game.interaction.object;

import core.cache.def.impl.SceneryDefinition;
import org.rs09.consts.Items;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.combat.ImpactHandler;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.game.system.task.Pulse;
import rs09.game.world.GameWorld;
import core.game.world.map.Direction;
import core.game.world.map.Location;
import core.plugin.Initializable;
import core.plugin.Plugin;

@Initializable
public class BeehivePlugin extends OptionHandler {
    private static final Item REPELLANT = new Item(Items.INSECT_REPELLENT_28);
    private static final Item BUCKET = new Item(Items.BUCKET_1925);
    private static final Item BUCKET_OF_WAX = new Item(Items.BUCKET_OF_WAX_30);
    private static final Item HONEYCOMB = new Item(12156);

    @Override
    public boolean handle(Player player, Node node, String option) {
        if (!player.getInventory().containsItem(REPELLANT)) {
            player.getPacketDispatch().sendMessage("The bees fly out of the hive and sting you!");
            player.getImpactHandler().manualHit(player, 2, ImpactHandler.HitsplatType.NORMAL, 1);
            GameWorld.getPulser().submit(new Pulse(2, player) {
                @Override
                public boolean pulse() {
                    player.getPacketDispatch().sendMessage("Maybe you can clear them out somehow.");
                    return true;
                }
            });
        } else {
            switch (option) {
                case "take-from":
                    if (!player.getInventory().containsItem(BUCKET)) {
                        player.getPacketDispatch().sendMessage("You need a bucket to do that.");
                    } else {
                        player.getInventory().remove(BUCKET);
                        player.getInventory().add(BUCKET_OF_WAX);
                        player.getPacketDispatch().sendMessage("You fill your bucket with wax from the hive.");
                    }
                    break;
                case "take-honey":
                    if (!player.getInventory().hasSpaceFor(HONEYCOMB)) {
                        player.getPacketDispatch().sendMessage("You don't have enough space in your inventory.");
                    } else {
                        player.getInventory().add(HONEYCOMB);
                        player.getPacketDispatch().sendMessage("You take a chunk of honeycomb from the hive.");
                    }
                    break;
            }
        }
        return true;
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        SceneryDefinition.forId(68).getHandlers().put("option:take-from", this);
        SceneryDefinition.forId(68).getHandlers().put("option:take-honey", this);
        return this;
    }

    @Override
    public Location getDestination(Node mover, Node node) {
        Location west = node.getCenterLocation().transform(Direction.WEST, 1);
        Location east = node.getCenterLocation().transform(Direction.EAST, 1);
        if (mover.getLocation().getDistance(east) <= mover.getLocation().getDistance(west)) {
            return east;
        } else {
            return west;
        }
    }
}
