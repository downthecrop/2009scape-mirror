package core.game.content.activity.mta;

import core.game.node.entity.skill.magic.MagicSpell;
import core.game.node.entity.skill.magic.Runes;
import core.game.node.Node;
import core.game.node.entity.Entity;
import core.game.node.entity.combat.equipment.SpellType;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.SpellBookManager.SpellBook;
import core.game.node.item.Item;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.plugin.Plugin;

import core.game.content.activity.mta.impl.GraveyardZone;

/**
 * Represents the bones to banana spell.
 * @author Emperor
 * @author 'Vexia
 * @version 1.0
 */
public final class BonesConvertingSpells extends MagicSpell {

	/**
	 * The bone ids.
	 */
	private static final int[] BONES = new int[] { 526, 532, 6904, 6905, 6906, 6907 };

	/**
	 * The graphic.
	 */
	private static final Graphics GRAPHIC = new Graphics(141, 96);

	/**
	 * The animation.
	 */
	private static final Animation ANIMATION = new Animation(722);

	/**
	 * The item to replace the bones with.
	 */
	private Item converted;

	/**
	 * Constructs a new {@code BonesConvertingSpells} {@code Object}.
	 */
	public BonesConvertingSpells() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code BonesConvertingSpells} {@code Object}.
	 * @param level The level required.
	 * @param converted The item to replace the bones with.
	 * @param anim The animation.
	 * @param graphic The graphic.
	 * @param runes The runes.
	 */
	public BonesConvertingSpells(int level, final double experience, Item converted, Animation anim, Graphics graphic, Item... runes) {
		super(SpellBook.MODERN, level, experience, anim, graphic, null, runes);
		this.converted = converted;
	}

	@Override
	public Plugin<SpellType> newInstance(SpellType arg) throws Throwable {
		SpellBook.MODERN.register(9, new BonesConvertingSpells(15, 25, new Item(1963), ANIMATION, GRAPHIC, Runes.EARTH_RUNE.getItem(2), Runes.WATER_RUNE.getItem(2), Runes.NATURE_RUNE.getItem(1)));
		SpellBook.MODERN.register(40, new BonesConvertingSpells(60, 65, new Item(6883), ANIMATION, GRAPHIC, Runes.EARTH_RUNE.getItem(4), Runes.WATER_RUNE.getItem(4), Runes.NATURE_RUNE.getItem(2)));
		return this;
	}

	@Override
	public boolean meetsRequirements(Entity caster, boolean message, boolean remove) {
		if (getSpellId() == 40 && !caster.asPlayer().getSavedData().getActivityData().isBonesToPeaches()) {
			caster.asPlayer().sendMessage("You can only learn this spell from the Mage Training Arena.");
			return false;
		}
		if (!hasBones(caster.asPlayer())) {
			if (message) {
				((Player) caster).getPacketDispatch().sendMessage("You aren't holding any bones!");
			}
			return false;
		}
		return super.meetsRequirements(caster, message, remove);
	}

	@Override
	public boolean cast(Entity entity, Node target) {
		Player p = (Player) entity;
		boolean inGrave = p.getZoneMonitor().isInZone("Creature Graveyard");
		if (p.getAttribute("cbs_tab", false) && inGrave && target.getId() >= 6904 && target.getId() <= 6907) {
			p.sendMessage("This tablet cannot be used on bones from the Mage Training Arena.");
			return false;
		}
		if (!p.getAttribute("cbs_tab", false) && !meetsRequirements(entity, true, true)) {
			return false;
		}
		p.removeAttribute("cbs_tab");
		for (int id : BONES) {
			if (p.getInventory().contains(id, 1)) {
				int amount = p.getInventory().getAmount(id);
				if (inGrave) {
					amount *= (GraveyardZone.BoneType.forItem(new Item(id)).ordinal() + 1);
				}
				p.getInventory().remove(new Item(id, amount));
				p.getInventory().add(new Item(converted.getId(), amount));
			}
		}
		visualize(entity, target);
		return true;
	}

	/**
	 * Checks if the player has bones.
	 * @param player the player.
	 * @return {@code True} if so.
	 */
	private boolean hasBones(Player player) {
		for (int id : BONES) {
			if (player.getInventory().contains(id, 1)) {
				return true;
			}
		}
		return false;
	}

}