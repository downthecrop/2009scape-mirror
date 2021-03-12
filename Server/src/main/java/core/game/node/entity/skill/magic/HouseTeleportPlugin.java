package core.game.node.entity.skill.magic;

import core.game.node.Node;
import core.game.node.entity.Entity;
import core.game.node.entity.combat.equipment.SpellType;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.SpellBookManager.SpellBook;
import core.game.node.entity.player.link.TeleportManager.TeleportType;
import core.game.node.item.Item;
import rs09.game.world.GameWorld;
import core.game.world.map.Location;
import core.plugin.Plugin;
import core.tools.RandomFunction;

/**
 * 
 * For house teleportation.
 * 
 * @author Jamix77
 *
 */
public class HouseTeleportPlugin extends MagicSpell {
	
	public HouseTeleportPlugin(final int level, final double experience, final Item... items) {
		super(SpellBook.MODERN, level, experience, null, null, null, items);
	}

	@Override
	public Plugin<SpellType> newInstance(SpellType arg) throws Throwable {
		
		return this;
	}

	@Override
	public boolean cast(Entity entity, Node target) {
		if (((Player)entity).getHouseManager().getLocation().getExitLocation() == null) {
			((Player)entity).sendMessage("You must have a house to teleport to before attempting that.");
			return false;
		}
		Location location = ((Player)entity).getHouseManager().getLocation().getExitLocation();
		if (entity.getLocks().isTeleportLocked() || !super.meetsRequirements(entity, true, false)) {
			return false;
		}
		if (entity.getTeleporter().send(location.transform(0, RandomFunction.random(3), 0), TeleportType.NORMAL)) {
			entity.setAttribute("teleport:items", super.runes);
			entity.setAttribute("magic-delay", GameWorld.getTicks() + 5);
			((Player) entity).getInventory().remove(super.runes);
			return true;
		}
		return false;
	}

}
