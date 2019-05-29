package plugin.activity.mta;

import org.crandor.game.content.skill.free.magic.MagicSpell;
import org.crandor.game.content.skill.free.magic.Runes;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.Entity;
import org.crandor.game.node.entity.combat.equipment.SpellType;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.entity.player.link.SpellBookManager.SpellBook;
import org.crandor.game.node.item.Item;
import org.crandor.game.world.update.flag.context.Animation;
import org.crandor.game.world.update.flag.context.Graphics;
import org.crandor.plugin.Plugin;

import plugin.activity.mta.impl.AlchemistZone;
import plugin.activity.mta.impl.AlchemistZone.AlchemistItem;

/**
 * Represents the plugin for the magic spell alchemy.
 * @author Emperor
 * @author 'Vexia
 * @version 1.0
 */
public final class AlchemySpell extends MagicSpell {

	/**
	 * If the spell is high alchemy.
	 */
	private boolean highAlchemy;

	/**
	 * Constructs a new {@code AlchemySpell} {@code Object}.
	 */
	public AlchemySpell() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code AlchemySpell} {@code Object}.
	 * @param level The required level.
	 * @param anim the animation.
	 * @param graphic The graphic.
	 * @param highAlchemy If this spell is high alchemy.
	 * @param runes The runes required.
	 */
	public AlchemySpell(int level, final double experience, Animation anim, Graphics graphic, boolean highAlchemy, Item... runes) {
		super(SpellBook.MODERN, level, experience, anim, graphic, null, runes);
		this.highAlchemy = highAlchemy;
	}

	@Override
	public Plugin<SpellType> newInstance(SpellType arg) throws Throwable {
		SpellBook.MODERN.register(13, new AlchemySpell(21, 31, Animation.create(712), new Graphics(112, 96), false, Runes.FIRE_RUNE.getItem(3), Runes.NATURE_RUNE.getItem(1)));
		SpellBook.MODERN.register(34, new AlchemySpell(55, 65, Animation.create(713), new Graphics(113, 96), true, Runes.FIRE_RUNE.getItem(5), Runes.NATURE_RUNE.getItem(1)));
		return this;
	}

	@Override
	public boolean cast(Entity entity, Node target) {
		Item item = (Item) target;
		Player p = (Player) entity;
		p.getInterfaceManager().setViewedTab(6);
		if (p.getAntiMacroHandler().hasEvent()) {
			p.getPacketDispatch().sendMessage("You can't do that right now.");
			return false;
		}
		if (item == null || p.getInventory().get(item.getSlot()) != item) {
			p.sendMessage("Error! Report to admin.");
			return false;
		}
		if (item.getId() == 995 || item.getName().equals("Coins")) {
			p.getPacketDispatch().sendMessage("You can't cast this spell on an object made of gold.");
			return false;
		}
		if (!item.getDefinition().isTradeable()) {
			p.getPacketDispatch().sendMessage("You can't cast this spell on an object like that.");
			return false;
		}
		boolean alchZone = p.getZoneMonitor().isInZone("Alchemists' Playground");
		AlchemistItem alch = null;
		if (alchZone) {
			alch = AlchemistZone.AlchemistItem.forItem(item.getId());
			if (alch == null) {
				p.getDialogueInterpreter().sendDialogue("You can only convert the items you find in the cupboards.");
				return false;
			}
		}
		Item coins = new Item(995, item.getDefinition().getAlchemyValue(highAlchemy));
		if (alchZone) {
			coins = new Item(AlchemistZone.COINS.getId(), alch.getCost());
			if (p.getInventory().getAmount(AlchemistZone.COINS.getId()) + alch.getCost() > 10000) {
				p.getDialogueInterpreter().sendDialogue("Warning: You can't deposit more than 12000 coins at a time.");
			}
		}
		if (coins.getAmount() > 1 && !p.getInventory().hasSpaceFor(coins)) {
			p.getPacketDispatch().sendMessage("Not enough space in your inventory!");
			return false;
		}
		boolean freeAlch = alchZone && alch == AlchemistZone.freeConvert;
		boolean explorerRing = !highAlchemy &&  p.getSavedData().getGlobalData().getLowAlchemyCharges() > 0;
		if (explorerRing && !checkLevelRequirement(p, true)) {
			return false;
		}
		if (!alchZone || !freeAlch) {
			if (!explorerRing && !meetsRequirements(p, true, true)) {
				return false;
			}
		}
		p.lock(3);
		visualize(p, target);
		if (p.getInventory().remove(new Item(item.getId(), 1))) {
			if (explorerRing) {
				p.getSavedData().getGlobalData().setLowAlchemyCharges(p.getSavedData().getGlobalData().getLowAlchemyCharges() - 1);
				int charge = p.getSavedData().getGlobalData().getLowAlchemyCharges();
				p.sendMessages("You use a free cast due to the power of your explorer's ring.", "You have " + charge + " charge" + (charge == 1 ? "" : "s") + " left.");
			}
			p.getAudioManager().send(highAlchemy ? 97 : 98);
			if (coins.getAmount() != 0) {
				p.getInventory().add(coins);
			}
		} else {
			return false;
		}
		return true;
	}

}
