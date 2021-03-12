package core.game.content.activity.bountyhunter;

import core.game.node.entity.skill.magic.MagicSpell;
import core.game.node.entity.skill.magic.Runes;
import core.game.interaction.MovementPulse;
import core.game.node.Node;
import core.game.node.entity.Entity;
import core.game.node.entity.combat.equipment.SpellType;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.SpellBookManager.SpellBook;
import core.game.node.entity.player.link.TeleportManager.TeleportType;
import core.game.node.entity.state.EntityState;
import core.game.node.item.Item;
import rs09.game.world.GameWorld;
import core.game.world.map.Location;
import core.game.world.map.RegionManager;
import core.plugin.Plugin;

/**
 * Handles the bounty target locate spell.
 * @author Emperor
 */
public final class BountyLocateSpell extends MagicSpell {

	/**
	 * Constructs a new {@code BountyLocateSpell} {@code Object}.
	 */
	public BountyLocateSpell() {
		super(SpellBook.MODERN, 32, 45, null, null, null, new Item[] { Runes.AIR_RUNE.getItem(1), Runes.FIRE_RUNE.getItem(1), Runes.LAW_RUNE.getItem(1) });
	}

	@Override
	public boolean cast(Entity entity, Node target) {
		BountyHunterActivity activity = entity.getExtension(BountyHunterActivity.class);
		Player player = (Player) entity;
		if (activity == null) {
			player.getPacketDispatch().sendMessage("You can only use this spell in the Bounty Hunter craters.");
			return false;
		}
		BountyEntry entry = activity.players.get(player);
		if (entry == null || entry.getTarget() == null) {
			player.getPacketDispatch().sendMessage("You don't have a target to teleport to.");
			return true;
		}
		if (player.getStateManager().hasState(EntityState.FROZEN) || player.getStateManager().hasState(EntityState.STUNNED)) {
			player.getPacketDispatch().sendMessage("You can't use this when " + (player.getStateManager().hasState(EntityState.STUNNED) ? "stunned." : "frozen."));
			return true;
		}
		boolean combat = player.inCombat();
		if (!super.meetsRequirements(entity, true, combat)) {
			return false;
		}
		if (combat) {
			player.getPacketDispatch().sendMessage("You were fighting recently so you'll run instead of teleport.");
			target = entry.getTarget();
			Location location = entry.getTarget().getLocation();
			if (!location.withinDistance(player.getLocation(), 30)) {
				int offsetX = location.getX() - player.getLocation().getX();
				int offsetY = location.getY() - player.getLocation().getY();
				if (offsetX > 30) {
					offsetX = 30;
				} else if (offsetX < -30) {
					offsetX = -30;
				}
				if (offsetY > 30) {
					offsetY = 30;
				} else if (offsetY < -30) {
					offsetY = -30;
				}
				target = player.getLocation().transform(offsetX, offsetY, 0);
			}
			player.getPulseManager().run(new MovementPulse(player, target) {
				@Override
				public boolean pulse() {
					return true;
				}
			}, "movement");
			return true;
		}
		Location destination = RegionManager.getTeleportLocation(entry.getTarget().getLocation(), 5);
		if (entity.getTeleporter().send(destination, TeleportType.NORMAL, -1)) {
			if (!super.meetsRequirements(entity, true, true)) {
				entity.getTeleporter().getCurrentTeleport().stop();
				return false;
			}
			entity.setAttribute("magic-delay", GameWorld.getTicks() + 5);
			return true;
		}
		return false;
	}

	@Override
	public Plugin<SpellType> newInstance(SpellType arg) throws Throwable {
		SpellBook.MODERN.register(60, this);
		return this;
	}

}