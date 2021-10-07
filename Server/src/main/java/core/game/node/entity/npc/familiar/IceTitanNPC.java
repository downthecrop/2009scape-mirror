package core.game.node.entity.npc.familiar;

import core.game.node.entity.skill.Skills;
import core.game.node.entity.skill.summoning.familiar.Familiar;
import core.game.node.entity.skill.summoning.familiar.FamiliarSpecial;
import core.game.node.entity.combat.equipment.WeaponInterface;
import core.game.node.entity.player.Player;
import core.game.world.update.flag.context.Animation;
import core.plugin.Initializable;
import core.game.world.update.flag.context.Graphics;

/**
 * Represents the Ice Titan familiar.
 * @author Aero
 */
@Initializable
public class IceTitanNPC extends ElementalTitanNPC {

	/**
	 * Constructs a new {@code IceTitanNPC} {@code Object}.
	 */
	public IceTitanNPC() {
		this(null, 7359);
	}

	/**
	 * Constructs a new {@code IceTitanNPC} {@code Object}.
	 * @param owner The owner.
	 * @param id The id.
	 */
	public IceTitanNPC(Player owner, int id) {
		super(owner, id, 6400, 12806, 20, WeaponInterface.STYLE_ACCURATE);
	}

	@Override
	public Familiar construct(Player owner, int id) {
		return new IceTitanNPC(owner, id);
	}


	@Override
	public void visualizeSpecialMove() {
		owner.visualize(new Animation(7660), new Graphics(1306));
	}

	@Override
	public int[] getIds() {
		return new int[] { 7359, 7360 };
	}

}
