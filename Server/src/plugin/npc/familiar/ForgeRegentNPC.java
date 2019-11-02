package plugin.npc.familiar;

import org.crandor.game.container.impl.EquipmentContainer;
import org.crandor.game.content.skill.member.summoning.familiar.Familiar;
import org.crandor.game.content.skill.member.summoning.familiar.FamiliarSpecial;
import org.crandor.game.node.entity.combat.equipment.WeaponInterface;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.item.Item;
import org.crandor.game.world.update.flag.context.Graphics;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.tools.RandomFunction;

/**
 * Represents the Forge Regent familiar.
 * @author Aero
 */
@InitializablePlugin
public class ForgeRegentNPC extends Familiar {

	/**
	 * Constructs a new {@code ForgeRegentNPC} {@code Object}.
	 */
	public ForgeRegentNPC() {
		this(null, 7335);
	}

	/**
	 * Constructs a new {@code ForgeRegentNPC} {@code Object}.
	 * @param owner The owner.
	 * @param id The id.
	 */
	public ForgeRegentNPC(Player owner, int id) {
		super(owner, id, 4500, 12782, 6, WeaponInterface.STYLE_RANGE_ACCURATE);
	}

	@Override
	public Familiar construct(Player owner, int id) {
		return new ForgeRegentNPC(owner, id);
	}

	@Override
	protected boolean specialMove(FamiliarSpecial special) {
		if (!(special.getTarget() instanceof Player)) {
			owner.sendMessage("You can't use this special on an npc.");
			return false;
		}
		Player target = special.getTarget().asPlayer();
		if (!canCombatSpecial(target)) {
			return false;
		}
		if (target.getInventory().freeSlots() < 1) {
			owner.sendMessage("The target doesn't have enough inventory space.");
			return false;
		}
		Item weapon = target.getEquipment().get(EquipmentContainer.SLOT_WEAPON);
		Item shield = target.getEquipment().get(EquipmentContainer.SLOT_SHIELD);
		if (weapon == null && shield == null) {
			owner.sendMessage("The target doesn't have a weapon or shield.");
			return false;
		}
		Item remove = null;
		while (remove == null) {
			if (RandomFunction.random(2) == 1) {
				remove = weapon;
			} else {
				remove = shield;
			}
		}
		graphics(Graphics.create(1394));
		target.graphics(Graphics.create(1393));
		if (target.getEquipment().remove(remove)) {
			target.getInventory().add(remove);
		}
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { 7335, 7336 };
	}

}
