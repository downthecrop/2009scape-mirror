package content.global.skill.smithing;

import content.global.skill.smithing.smelting.Bar;
import core.game.component.Component;
import core.game.container.access.InterfaceContainer;
import core.game.node.entity.skill.Skills;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.tools.StringUtils;

/**
 * Represents the builder class to view the correct information, on the
 * interface of smithing, ie, the items ammount, and name.
 * @author 'Vexia
 */
public final class SmithingBuilder {

	/**
	 * The bar type.
	 */
	private BarType type;

	/**
	 * The bar used.
	 */
	private Bar bar;

	/**
	 * Constructs a new {@code SmithingBuilder.java} {@code Object}.
	 * @param item the item.
	 */
	public SmithingBuilder(Item item) {
		this.bar = Bar.forId(item.getId());
		this.type = BarType.getBarTypeForId(item.getId());
	}

	/**
	 * Builds the interface of the smithing.
	 * @param player the player.
	 */
	public void build(Player player) {
		player.getGameAttributes().removeAttribute("smith-type");
		player.getGameAttributes().setAttribute("smith-type", type);
		if (type.name().equals("BLURITE")) {
			// interface 300 spawns with most things already there. Hide everything except what we want
			int[] values = {
					17, // dagger
					25, // axe
					33, // mace
					41, // med helm
					// show this 49, // bolts
					57, // sword
					65, // dart tip
					73, // nail
					105, // arrow tip
					113, // scimmy
					// show this 121, // limbs
					129, // long sword
					137, // throwing knife
					145, // full helm
					153, // square shield
					177, // warhammer
					185, // battleaxe
					193, // chain body
					201, // kiteshield
					217, // 2h
					225, // plate sk
					233, // plate l
					241, // platebody
			};
            for (int childId : values) {
                player.getPacketDispatch().sendInterfaceConfig(300, childId, true);

            }
		}
		else {
			player.getPacketDispatch().sendInterfaceConfig(300, 267, false);// pickaxe
		}
		final Bars bars[] = Bars.getBars(type);
		for (int i = 0; i < bars.length; i++) {
			if (bars[i].getSmithingType() == SmithingType.TYPE_GRAPPLE_TIP) {
				player.getPacketDispatch().sendInterfaceConfig(300, 169, false);
			}
			if (bars[i].getSmithingType() == SmithingType.TYPE_DART_TIP) {
				player.getPacketDispatch().sendInterfaceConfig(300, 65, false);
			}
			if (bars[i].getSmithingType() == SmithingType.TYPE_WIRE){
				player.getPacketDispatch().sendInterfaceConfig(300, 81, false);
			}
			if (bars[i].getSmithingType() == SmithingType.TYPE_SPIT_IRON){
				player.getPacketDispatch().sendInterfaceConfig(300, 89, false);
			}
			if ( bars[i].getSmithingType() == SmithingType.TYPE_BULLSEYE) {
				player.getPacketDispatch().sendInterfaceConfig(300, 161, false);
			}
			if (bars[i].getSmithingType() == SmithingType.TYPE_CLAWS) {
				player.getPacketDispatch().sendInterfaceConfig(300, 209, false);
			}
			if (bars[i].getSmithingType() == SmithingType.TYPE_OIL_LANTERN) {
				player.getPacketDispatch().sendInterfaceConfig(300, 161, false);
			}
			if (bars[i].getSmithingType() == SmithingType.TYPE_STUDS) {
				player.getPacketDispatch().sendInterfaceConfig(300, 97, false);
			}
			String color = "";
			if (player.getSkills().getLevel(Skills.SMITHING) < bars[i].getLevel()) {
			} else {
				color = "<col=FFFFFF>";
			}
			player.getPacketDispatch().sendString(color + StringUtils.formatDisplayName(bars[i].getSmithingType().name().replace("TYPE_", "")), 300, bars[i].getSmithingType().getName());
			if (player.getInventory().contains(bars[i].getBarType().getBarType(), bars[i].getSmithingType().getRequired())) {
				color = "<col=2DE120>";
			} else {
				color = null;
			}
			if (color != null) {
				String amt = bars[i].getSmithingType().getRequired() > 1 ? "s" : "";
				player.getPacketDispatch().sendString(color + String.valueOf(bars[i].getSmithingType().getRequired()) + " Bar" + amt, 300, bars[i].getSmithingType().getName() + 1);
			}
			InterfaceContainer.generateItems(player, new Item[] { new Item(bars[i].getProduct(), bars[i].getSmithingType().getProductAmount()) }, new String[] { "" }, 300, bars[i].getSmithingType().getChild() - 1);
		}
		player.getPacketDispatch().sendString(type.getBarName(), 300, 14);
		player.getInterfaceManager().open(new Component(300));
	}

	/**
	 * Gets the type.
	 * @return the type.
	 */
	public BarType getType() {
		return type;
	}

	/**
	 * Gets the bar.
	 * @return the bar.
	 */
	public Bar getBar() {
		return bar;
	}

}
