package core.game.node.entity.skill.magic.lunar;

import core.plugin.Initializable;
import core.game.node.entity.skill.Skills;
import core.game.node.entity.skill.magic.MagicSpell;
import core.game.node.entity.skill.magic.Runes;
import core.game.node.Node;
import core.game.node.entity.Entity;
import core.game.node.entity.combat.equipment.SpellType;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.SpellBookManager.SpellBook;
import core.game.node.item.Item;
import core.game.node.object.GameObject;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.plugin.Plugin;

/**
 * Cures a diseased plant.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class CurePlantSpell extends MagicSpell {

	/**
	 * Represents the animation of the spell.
	 */
	private final static Animation ANIMATION = Animation.create(4409);

	/**
	 * Represents the graphics to use.
	 */
	private final static Graphics GRAPHIC = new Graphics(742, 100);

	/**
	 * Constructs a new {@code CurePlantSpell} {@code Object}.
	 */
	public CurePlantSpell() {
		super(SpellBook.LUNAR, 66, 60, ANIMATION, GRAPHIC, null, new Item[] { new Item(Runes.ASTRAL_RUNE.getId(), 1), new Item(Runes.EARTH_RUNE.getId(), 8) });
	}

	@Override
	public Plugin<SpellType> newInstance(SpellType arg) throws Throwable {
		SpellBook.LUNAR.register(32, this);
		return this;
	}

	@Override
	public boolean cast(Entity entity, Node target) {
		final Player player = ((Player) entity);
		if (!(target instanceof GameObject)) {
			return false;
		}
		final GameObject object = ((GameObject) target);
		/*if (FarmingPatch.forObject(object.getWrapper().getId()) == null) {
			return false;
		}*/
		/*final PatchWrapper wrapper = player.getFarmingManager().getPatchWrapper(object.getWrapper().getId());
		if (wrapper == null) {
			player.getPacketDispatch().sendMessage("Umm... this spell won't cure that!");
			return false;
		}
		if (!wrapper.getCycle().getDiseaseHandler().isDiseased() && (wrapper.getCycle().getGrowthHandler().isGrowing() || wrapper.getCycle().getWaterHandler().isWatered())) {
			player.getPacketDispatch().sendMessage("It is growing just fine.");
			return false;
		}
		if (wrapper.isWeedy()) {
			player.getPacketDispatch().sendMessage("The weeds are healthy enough already.");
			return false;
		}*/
		/*if (wrapper.isEmpty()) {
			player.getPacketDispatch().sendMessage("There's nothing there to cure.");
			return false;
		}*/
		/*if (wrapper.getCycle().getDeathHandler().isDead()) {
			player.getPacketDispatch().sendMessage("It says 'Cure' not 'Resurrect'. Although death may arise from disease, it is not in itself a disease and hence cannot be cured. So there.");
			return false;
		}*/
		if (!super.meetsRequirements(player, true, true)) {
			return false;
		}
/*
		wrapper.getCycle().getDiseaseHandler().cure(player, true);
*/
		player.animate(ANIMATION);
		player.graphics(GRAPHIC);
		player.getSkills().addExperience(Skills.FARMING, 91.5, true);
		return true;
	}
}
