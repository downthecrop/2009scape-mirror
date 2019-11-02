package plugin.combat.spell;

import java.util.ArrayList;
import java.util.List;

import org.crandor.cache.def.impl.ItemDefinition;
import org.crandor.game.container.impl.EquipmentContainer;
import org.crandor.game.content.dialogue.DialogueAction;
import org.crandor.game.content.skill.Skills;
import org.crandor.game.interaction.NodeUsageEvent;
import org.crandor.game.interaction.OptionHandler;
import org.crandor.game.interaction.UseWithHandler;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.Entity;
import org.crandor.game.node.entity.combat.BattleState;
import org.crandor.game.node.entity.combat.CombatSpell;
import org.crandor.game.node.entity.combat.equipment.SpellType;
import org.crandor.game.node.entity.impl.Projectile;
import org.crandor.game.node.entity.impl.Animator.Priority;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.entity.player.link.SpellBookManager.SpellBook;
import org.crandor.game.node.item.Item;
import org.crandor.game.world.update.flag.context.Animation;
import org.crandor.game.world.update.flag.context.Graphics;
import org.crandor.plugin.Plugin;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.PluginManager;

/**
 * Handles the trident of seas.
 * @author Empathy
 *
 */
@InitializablePlugin
public class TridentOfSeas extends CombatSpell {
	
	/**
	 * Constructs a new {@code TridentOfSeas} {@code Object}.
	 */
	public TridentOfSeas() {
		super(SpellType.TRIDENT_OF_SEAS, SpellBook.MODERN, 75, 0, -1, -1, new Animation(1167, Priority.HIGH), new Graphics(2004, 96), Projectile.create((Entity) null, null, 2002, 20, 0, 52, 75, 15, 11), new Graphics(2003));
	}

	@Override
	public void addExperience(Entity entity, int hit) {
		if (!(entity instanceof Player) || hit < 1) {
			return;
		}
		entity.getSkills().addExperience(Skills.HITPOINTS, hit * 1, true);
		entity.getSkills().addExperience(Skills.MAGIC, hit * 2, true);
	}

	@Override
	public boolean cast(Entity entity, Node target) {
		if (target instanceof Player) {
			entity.asPlayer().sendMessage("You can only cast this spell on an npc.");
			return false;
		}
		return super.cast(entity, target);
	}
	
	@Override
	public boolean meetsRequirements(Entity caster, boolean message, boolean remove) {
		Player player = caster.asPlayer();
		Item item = player.getEquipment().get(EquipmentContainer.SLOT_WEAPON);
		if (item.getId() == 14664 || item.getId() == 14666) {
			if (item.getCharge() > 1000) {
				int charges = item.getCharge();
				if (item.getId() == 14664) {
					player.getEquipment().replace(new Item(14666), EquipmentContainer.SLOT_WEAPON);
					item = player.getEquipment().get(EquipmentContainer.SLOT_WEAPON);
				}
				item.setCharge(charges - 1);
				return true;
			}
			player.sendMessage("Your staff has run out of charges.");
			player.getEquipment().replace(new Item(14667), EquipmentContainer.SLOT_WEAPON);
			return false;
		}
		return false;
	}
	
	@Override
	public Plugin<SpellType> newInstance(SpellType arg) throws Throwable {
		PluginManager.definePlugin(new TridentOfSeasHandler(), new TridentOfSeasOptionHandler());
		SpellBook.MODERN.register(126, this);
		return this;
	}

	@Override
	public int getMaximumImpact(Entity entity, Entity victim, BattleState state) {
		return type.getImpactAmount(entity, victim, 20);
	}
	
	/**
	 * Handles the trident of seas.
	 * @author Empathy
	 *
	 */
	public class TridentOfSeasHandler extends UseWithHandler {
		
		/**
		 * The items to charge a trident.
		 */
		private final Item chaosRune = new Item(562), fireRune = new Item(554, 5), deathRune = new Item(560), coins = new Item(995, 10);

		/**
		 * The charging animation.
		 */
		private final Animation charge = new Animation(11296);
		
		/**
		 * Constructs a new @{Code TridentOfSeasHandler} object.
		 */
		public TridentOfSeasHandler() {
			super(14667, 14666);
		}
		
		/**
		 * The staff Ids.
		 */
		public final int[] tridentIds = new int[] { 14664, 14666 };
		
		@Override
		public Plugin<Object> newInstance(Object arg) throws Throwable {
			for (int id : tridentIds) {
				ItemDefinition.forId(id).getConfigurations().put("equipment", this);
			}
			addHandler(coins.getId(), ITEM_TYPE, this);
			addHandler(chaosRune.getId(), ITEM_TYPE, this);
			addHandler(fireRune.getId(), ITEM_TYPE, this);
			addHandler(deathRune.getId(), ITEM_TYPE, this);
			return this;
		}

		@Override
		public Object fireEvent(String identifier, Object... args) {
			final Player player = (Player) args[0];
			switch (identifier) {
			case "equip":
				//player.getProperties().setAutocastSpell((CombatSpell) SpellBook.MODERN.getSpell(126));
				break;
			case "unequip":
				player.getProperties().setAutocastSpell(null);
				break;
			}
			return true;
		}

		@Override
		public boolean handle(NodeUsageEvent event) {
			final Player player = event.getPlayer();
			Item item = event.getUsedItem();
			if (item.getCharge() == 3500) {
				player.sendMessage("This item is fully charged.");
				return false;
			}
			if (hasRequirements(player)) {
				if (player.getEquipment().get(EquipmentContainer.SLOT_WEAPON) != null || player.getEquipment().get(EquipmentContainer.SLOT_SHIELD) != null) {
					player.getDialogueInterpreter().sendItemMessage(14664, "You need your hands free to charge the weapon.");
					return true;
				}
				player.animate(charge);
				int charges = calculateCharges(player, item);
				addCharges(player, item, charges);
				player.getDialogueInterpreter().sendItemMessage(14664, "You add " + charges + " charges to the weapon.", "New total: " + (item.getId() == 14666 ? item.getCharge() - 1000 : charges));
				return true;
			}
			player.getDialogueInterpreter().sendItemMessage(14664, "You need one death rune, one chaos rune, five fire", "runes and ten coins to charge the weapon.");
			return false;
		}
		
		/**
		 * Adds the charges to the trident.
		 * @param player The player.
		 * @param item The item.
		 * @param charges The charges.
		 */
		private void addCharges(Player player, Item item, int charges) {
			Item staff = item;
			if (staff.getId() == 14667) {
				player.getInventory().replace(new Item(14666), staff.getSlot());
				staff = player.getInventory().get(staff.getSlot());
			}
			chaosRune.setAmount(charges);
			fireRune.setAmount(charges * 5);
			deathRune.setAmount(charges);
			coins.setAmount(charges * 10);
			player.getInventory().remove(chaosRune, fireRune, deathRune, coins);
			chaosRune.setAmount(1);
			fireRune.setAmount(5);
			deathRune.setAmount(1);
			coins.setAmount(10);			
			staff.setCharge(staff.getCharge() + charges);	
			if (staff.getCharge() == 3500) {
				player.getInventory().replace(new Item(14664), staff.getSlot());
				staff = player.getInventory().get(staff.getSlot());
				staff.setCharge(3500);
			}
		}
		
		/**
		 * Calculates the charges for trident.
		 * @param player The player.
		 * @return the charges.
		 */
		private int calculateCharges(Player player, Item item) {
			int max = 3500 - item.getCharge();
			List<Integer> requiredItems = new ArrayList<>();
			requiredItems.add(player.getInventory().getAmount(fireRune) / 5);
			requiredItems.add(player.getInventory().getAmount(coins) / 10);
			requiredItems.add(player.getInventory().getAmount(deathRune));
			requiredItems.add(player.getInventory().getAmount(chaosRune));
			int lowestAmount = 3500;
			for (Integer i : requiredItems) {
				if (i < lowestAmount) {
					lowestAmount = i;
				}
			}
			return lowestAmount > max ? max : lowestAmount;
		}
		
		/**
		 * Checks if the player has the requirements to charge the trident.
		 * @param player The player.
		 * @return true if so.
		 */
		private boolean hasRequirements(Player player) {
			if (!player.getInventory().containsItems(deathRune, chaosRune, fireRune, coins)) {
				return false;
			}
			return true;
		}

	}
	
	/**
	 * Handles the check and uncharge option for the trident.
	 * @author Empathy
	 *
	 */
	public class TridentOfSeasOptionHandler extends OptionHandler {

		/**
		 * The trident ids.
		 */
		public final int[] tridentIds = new int[] { 14664, 14666 };
		
		/**
		 * The items.
		 */
		private final Item chaosRune = new Item(562), fireRune = new Item(554, 5), deathRune = new Item(560);
		
		@Override
		public Plugin<Object> newInstance(Object arg) throws Throwable {
			for (int i : tridentIds) {
				ItemDefinition.forId(i).getConfigurations().put("option:check", this);
				ItemDefinition.forId(i).getConfigurations().put("option:uncharge", this);
			}
			return this;
		}

		@Override
		public boolean handle(Player player, Node node, String option) {
			final Item item = node.asItem();
			switch(option) {
			case "check": 
				player.sendMessage("Your trident has " + (item.getCharge() - 1000) + " charges.");
				break;
			case "uncharge":
				player.getDialogueInterpreter().sendOptions("You will NOT get the coins back.", "Okay, uncharge it.", "No, don't uncharge it.");
				player.getDialogueInterpreter().addAction(new DialogueAction() {

					@Override
					public void handle(Player player, int buttonId) {
						switch(buttonId) {
						case 2:
							if (!player.getInventory().hasSpaceFor(chaosRune) || !player.getInventory().hasSpaceFor(deathRune) || !player.getInventory().hasSpaceFor(fireRune)) {
								player.sendMessage("You need more inventory space to do this.");
								break;
							}
							int charges = item.getCharge() - 1000;
							chaosRune.setAmount(charges);
							fireRune.setAmount(charges * 5);
							deathRune.setAmount(charges);
							player.getInventory().replace(new Item(14667), item.getSlot());
							player.getInventory().add(fireRune, deathRune, chaosRune);
							break;
						}						
					}
					
				});
				break;
			}
			return true;
		}
		
	}

}
