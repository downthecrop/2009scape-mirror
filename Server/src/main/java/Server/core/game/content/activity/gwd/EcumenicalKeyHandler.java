/**
 * https://oldschool.2009scape.wiki/w/Ecumenical_key
 */
//package core.game.content.activity.gwd;
//
//import core.game.content.global.action.DoorActionHandler;
//import core.game.interaction.NodeUsageEvent;
//import core.game.interaction.UseWithHandler;
//import core.game.node.entity.player.Player;
//import core.game.node.object.GameObject;
//import core.game.world.map.Direction;
//import core.plugin.InitializablePlugin;
//import core.plugin.Plugin;
//
///**
// * Handles the boss room instant-access key.
// * @author Splinter
// */
//@InitializablePlugin
//public class EcumenicalKeyHandler extends UseWithHandler {
//
//	/**
//	 * Constructs a new {@code EcumenicalKeyHandler} {@code Object}
//	 */
//	public EcumenicalKeyHandler() {
//		super(14674);
//	}
//
//	@Override
//	public Plugin<Object> newInstance(Object arg) throws Throwable {
//		addHandler(26425, OBJECT_TYPE, this);
//		addHandler(26426, OBJECT_TYPE, this);
//		addHandler(26427, OBJECT_TYPE, this);
//		addHandler(26428, OBJECT_TYPE, this);
//		return this;
//	}
//
//	@Override
//	public boolean handle(NodeUsageEvent event) {
//		final Player player = event.getPlayer();
//		final GameObject object = event.getUsedWith().asObject();
//		Direction dir = Direction.get((object.getRotation() + 3) % 4);
//		if (dir.getStepX() != 0) {
//			if (player.getLocation().getX() == object.getLocation().transform(dir.getStepX(), 0, 0).getX()) {
//				player.sendMessage("It would be unwise to use the key on this side of the door!");
//				return true;
//			}
//		} else if (player.getLocation().getY() == object.getLocation().transform(0, dir.getStepY(), 0).getY()) {
//			player.sendMessage("It would be unwise to use the key on this side of the door!");
//			return true;
//		}
//		player.lock(2);
//		player.getInventory().remove(event.getUsedItem());
//		player.sendMessage("The key shatters as you insert it into the lock.");
//		DoorActionHandler.handleAutowalkDoor(player, event.getUsedWith().asObject());
//		return true;
//	}
//
//}
/**
 * Function to add key into player inventory
 * Goes in GodwarsMapZone.java 'death' method
 */
//			int count = killer.asPlayer().getBank().getAmount(14674) + killer.asPlayer().getInventory().getAmount(14674);
//			int rand = RandomFunction.random(1, 60 + (count * 10));
//			if(rand == 15 && count < 3){
//				Item item = new Item(14674);
//				GroundItemManager.create(item, e.getLocation(), ((Player) killer));
//				killer.asPlayer().sendMessage("<col=990000>A crystalline key falls to the ground as you slay your opponent.</col>");
//			}