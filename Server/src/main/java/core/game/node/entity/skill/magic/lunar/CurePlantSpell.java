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
import rs09.game.node.entity.skill.farming.FarmingPatch;
import rs09.game.node.entity.skill.farming.Patch;

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
		FarmingPatch fPatch = FarmingPatch.forObject(object);
		if(fPatch == null){
			player.sendMessage("This spell is for plants... not whatever the heckies that is.");
			return false;
		}
		Patch patch = fPatch.getPatchFor(player);
		if(!patch.isDiseased() && !patch.isWeedy()){
			player.sendMessage("It seems to be growing fine already, lad.");
			return false;
		}
		if(patch.isWeedy()){
			player.sendMessage("Trust me lad, the weeds are healthy enough as is.");
			return false;
		}
		if(patch.isDead()){
			player.sendMessage("It says 'Cure' not 'Resurrect'. Although death may arise from disease, it is not in itself a disease and hence cannot be cured. So there.");
			return false;
		}
		if (!super.meetsRequirements(player, true, true)) {
			return false;
		}
		patch.cureDisease();
		player.animate(ANIMATION);
		player.graphics(GRAPHIC);
		player.getSkills().addExperience(Skills.FARMING, 91.5, true);
		return true;
	}
}
