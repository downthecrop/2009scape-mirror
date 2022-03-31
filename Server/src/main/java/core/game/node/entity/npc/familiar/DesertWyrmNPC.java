package core.game.node.entity.npc.familiar;

import java.util.ArrayList;
import java.util.List;

import core.cache.def.impl.NPCDefinition;
import core.plugin.Initializable;
import core.game.node.entity.skill.SkillBonus;
import core.game.node.entity.skill.Skills;
import core.game.node.entity.skill.gather.SkillingResource;
import core.game.node.entity.skill.summoning.familiar.Familiar;
import core.game.node.entity.skill.summoning.familiar.FamiliarSpecial;
import core.game.node.entity.skill.summoning.familiar.Forager;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.Entity;
import core.game.node.entity.combat.equipment.WeaponInterface;
import core.game.node.entity.impl.Projectile;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.item.GroundItemManager;
import core.game.node.item.Item;
import core.game.node.scenery.Scenery;
import core.game.system.task.Pulse;
import rs09.game.world.GameWorld;
import core.game.world.map.Direction;
import core.game.world.map.Location;
import core.game.world.map.RegionManager;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.plugin.Plugin;
import rs09.plugin.ClassScanner;

/**
 * Represents the Desert Wyrm familiar.
 * @author Vexia
 * @author Aero
 */
@Initializable
public final class DesertWyrmNPC extends Forager {

	/**
	 * Constructs a new {@code DesertWyrmNPC} {@code Object}.
	 */
	public DesertWyrmNPC() {
		this(null, 6831);
	}

	/**
	 * Constructs a new {@code DesertWyrmNPC} {@code Object}.
	 * @param owner The owner.
	 * @param id The id.
	 */
	public DesertWyrmNPC(Player owner, int id) {
		super(owner, id, 1900, 12049, 6, WeaponInterface.STYLE_AGGRESSIVE);
		boosts.add(new SkillBonus(Skills.MINING, 1));
	}

	@Override
	public Familiar construct(Player owner, int id) {
		return new DesertWyrmNPC(owner, id);
	}

	@Override
	protected boolean specialMove(FamiliarSpecial special) {
		final Entity target = (Entity) special.getNode();
		if (!canCombatSpecial(target)) {
			return false;
		}
		faceTemporary((Entity) special.getNode(), 2);
		visualize(new Animation(7795), new Graphics(1410));
		Projectile.magic(this, target, 1411, 40, 36, 51, 10).send();
		sendFamiliarHit(target, 5);
		return true;
	}

	@Override
	public void configureFamiliar() {
		ClassScanner.definePlugin(new OptionHandler() {
			@Override
			public Plugin<Object> newInstance(Object arg) throws Throwable {
				for (int i : getIds()) {
					NPCDefinition.forId(i).getHandlers().put("option:burrow", this);
				}
				return this;
			}

			@Override
			public boolean handle(final Player player, final Node node, String option) {
				final Scenery rock = getClosestRock(player);
				if (!player.getFamiliarManager().isOwner((Familiar) node)) {
					return true;
				}
				if (((NPC) node).getLocks().isMovementLocked()) {
					return true;
				}
				if (rock == null) {
					player.getPacketDispatch().sendMessage("There are no rocks around here for the desert wyrm to mine from!");
					return true;
				}
				final SkillingResource resource = SkillingResource.forId(rock.getId());
				if (resource == null) {
					player.getPacketDispatch().sendMessage("There are no rocks around here for the desert wyrm to mine from!");
					return true;
				}
				final Familiar familiar = (Familiar) node;
				player.lock(9);
				familiar.lock(8);
				familiar.visualize(new Animation(7800), new Graphics(1412));
				GameWorld.getPulser().submit(new Pulse(1, player, familiar) {
					int counter;

					@Override
					public boolean pulse() {
						switch (++counter) {
						case 4:
							familiar.setInvisible(true);
							break;
						case 8:
							familiar.call();
							GroundItemManager.create(new Item(resource.getReward()), familiar.getLocation(), player);
							return true;
						}
						return false;
					}
				});
				return true;
			}

			/**
			 * Gets the closest combat rock.
			 * @return the object.
			 */
			public Scenery getClosestRock(Player player) {
				List<Scenery> rocks = new ArrayList<>(20);
				for (int k = 0; k < 7; k++) {
					for (int i = 0; i < 4; i++) {
						Direction dir = Direction.get(i);
						Location loc = player.getLocation().transform(dir.getStepX() * k, dir.getStepY() * k, 0);
						Scenery object = RegionManager.getObject(loc);
						if (object != null && object.getName().equals("Rocks")) {
							rocks.add(object);
						}
					}
				}
				int ordinal = 0;
				Scenery o = null;
				for (Scenery r : rocks) {
					SkillingResource resource = SkillingResource.forId(r.getId());
					if (resource != null && resource.ordinal() > ordinal) {
						ordinal = resource.ordinal();
						o = r;
					}
				}
				return o;
			}

		});
	}

	@Override
	public int[] getIds() {
		return new int[] { 6831, 6832 };
	}

}
