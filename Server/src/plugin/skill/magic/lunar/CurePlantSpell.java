package plugin.skill.magic.lunar;

import org.crandor.game.content.skill.Skills;
import org.crandor.game.content.skill.free.magic.MagicSpell;
import org.crandor.game.content.skill.free.magic.Runes;
import org.crandor.game.content.skill.member.farming.FarmingPatch;
import org.crandor.game.content.skill.member.farming.wrapper.PatchWrapper;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.Entity;
import org.crandor.game.node.entity.combat.equipment.SpellType;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.entity.player.link.SpellBookManager.SpellBook;
import org.crandor.game.node.item.Item;
import org.crandor.game.node.object.GameObject;
import org.crandor.game.world.update.flag.context.Animation;
import org.crandor.game.world.update.flag.context.Graphics;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * Cures a diseased plant.
 * @author 'Vexia
 * @version 1.0
 */
@InitializablePlugin
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
		if (FarmingPatch.forObject(object.getWrapper().getId()) == null) {
			return false;
		}
		final PatchWrapper wrapper = player.getFarmingManager().getPatchWrapper(object.getWrapper().getId());
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
		}
		if (wrapper.isEmpty()) {
			player.getPacketDispatch().sendMessage("There's nothing there to cure.");
			return false;
		}
		if (wrapper.getCycle().getDeathHandler().isDead()) {
			player.getPacketDispatch().sendMessage("It says 'Cure' not 'Resurrect'. Although death may arise from disease, it is not in itself a disease and hence cannot be cured. So there.");
			return false;
		}
		if (!super.meetsRequirements(player, true, true)) {
			return false;
		}
		wrapper.getCycle().getDiseaseHandler().cure(player, true);
		player.animate(ANIMATION);
		player.graphics(GRAPHIC);
		player.getSkills().addExperience(Skills.FARMING, 91.5, true);
		return true;
	}
}
