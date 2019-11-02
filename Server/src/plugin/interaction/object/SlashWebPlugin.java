package plugin.interaction.object;

import org.crandor.cache.def.impl.ObjectDefinition;
import org.crandor.game.container.Container;
import org.crandor.game.container.impl.EquipmentContainer;
import org.crandor.game.interaction.OptionHandler;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.combat.equipment.WeaponInterface;
import org.crandor.game.node.entity.combat.equipment.WeaponInterface.AttackStyle;
import org.crandor.game.node.entity.combat.equipment.WeaponInterface.WeaponInterfaces;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.item.Item;
import org.crandor.game.node.object.GameObject;
import org.crandor.game.node.object.ObjectBuilder;
import org.crandor.game.world.update.flag.context.Animation;
import org.crandor.plugin.Plugin;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.tools.RandomFunction;

/**
 * Handles the slashing of a web object.
 * @author Vexia
 * @version 2.0
 */
@InitializablePlugin
public final class SlashWebPlugin extends OptionHandler {

	/**
	 * Represents the object ids.
	 */
	private static final int[] IDS = new int[] { 733, 1810, 11400, 33237 };

	/**
	 * Represents the animation of slashing a web.
	 */
	private static final Animation ANIMATION = new Animation(451);

	/**
	 * Represents the knife animation.
	 */
	private static final Animation KNIFE_ANIMATION = new Animation(911);

	/**
	 * Represents the knife item.
	 */
	private static final Item KNIFE = new Item(946);

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		for (int objectId : IDS) {
			ObjectDefinition.forId(objectId).getConfigurations().put("option:slash", this);
		}
		ObjectDefinition.forId(27266).getConfigurations().put("option:pass", this);
		ObjectDefinition.forId(29354).getConfigurations().put("option:pass", this);
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		final GameObject object = (GameObject) node;
		Item weapon = getWeapon(player, player.getEquipment());
		if (weapon == null) {
			weapon = getWeapon(player, player.getInventory());
			if (weapon == null) {
				if (!player.getInventory().containsItem(KNIFE)) {
					player.getPacketDispatch().sendMessage("Only a sharp blade can cut through this sticky web.");
					return true;
				}
				weapon = KNIFE;
			}
		}
		final boolean success = RandomFunction.random(2) == 1;
		player.lock(2);
		player.animate(weapon == KNIFE ? KNIFE_ANIMATION : ANIMATION);
		if (success) {
			player.getPacketDispatch().sendMessage("You slash the web apart.");
			ObjectBuilder.replace(object, object.getId() == 27266 || object.getId() == 29354 ? object.transform(734) : object.transform(object.getId() + 1), 100);
		} else {
			player.getPacketDispatch().sendMessage("You fail to cut through it.");
		}
		return true;
	}

	/**
	 * Gets the slashed weapon item.
	 * @param player the player.
	 * @param container the container.
	 * @return the item.
	 */
	private Item getWeapon(final Player player, final Container container) {
		Item item = container instanceof EquipmentContainer ? container.get(EquipmentContainer.SLOT_WEAPON) : null;
		if (container instanceof EquipmentContainer) {
			return checkEquipmentWeapon(player, item);
		}
		for (Item i : container.toArray()) {
			if (i == null) {
				continue;
			}
			if (i.getDefinition().hasAction("wield") || i.getDefinition().hasAction("equip")) {
				item = checkEquipmentWeapon(player, i);
				if (item != null) {
					return item;
				}
			} else {
				continue;
			}
		}
		return item;
	}

	/**
	 * Checks an equipment weapon.
	 * @param player the player.
	 * @param item the item.
	 * @return {@code Item} the item.
	 */
	private Item checkEquipmentWeapon(final Player player, final Item item) {
		if (item != null) {
			WeaponInterfaces inter = WeaponInterface.getWeaponInterface(item);
			if (inter == null) {
				return null;
			}
			boolean success = false;
			for (AttackStyle style : inter.getAttackStyles()) {
				if (style.getBonusType() == WeaponInterface.BONUS_SLASH) {
					return item;
				}
			}
			if (!success) {
				return null;
			}
		}
		return item;
	}

}
