package content.global.skill.hunter.falconry;

import core.game.container.impl.EquipmentContainer;
import core.game.node.entity.skill.SkillPulse;
import core.game.node.entity.skill.Skills;
import core.game.node.entity.impl.Projectile;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.HintIconManager;
import core.game.node.item.Item;
import core.game.system.task.Pulse;
import core.game.world.GameWorld;
import core.game.world.map.Location;
import core.game.world.repository.Repository;
import core.tools.RandomFunction;
import org.rs09.consts.Sounds;

import static core.api.ContentAPIKt.playAudio;

/**
 * Represents the skill pulse used to catch a kebbit.
 * @author Vexia
 */
public final class FalconryCatchPulse extends SkillPulse<NPC> {

	/**
	 * Represents the falcon catch.
	 */
	private final FalconCatch falconCatch;

	/**
	 * Represents the falcon item.
	 */
	private static final Item FALCON = new Item(10024);

	/**
	 * Represents the falcon glove.
	 */
	private static final Item GLOVE = new Item(10023);

	/**
	 * Represents the original location.
	 */
	private final Location originalLocation;

	/**
	 * If the falcon has been checked.
	 */
	private boolean checked;

	/**
	 * The ticks passed.
	 */
	private int ticks;

	/**
	 * Constructs a new {@code FalconryCatchPulse.java} {@code Object}.
	 * @param player
	 * @param node
	 */
	public FalconryCatchPulse(final Player player, final NPC node, final FalconCatch falconCatch) {
		super(player, node);
		this.falconCatch = falconCatch;
		this.originalLocation = node.getLocation();
	}

	@Override
	public void start() {
		player.faceTemporary(node, 1);
		node.getWalkingQueue().reset();
		player.getWalkingQueue().reset();
		super.start();
	}

	@Override
	public boolean checkRequirements() {
		if (!checked) {
			checked = true;
			if (node.getLocation().getDistance(player.getLocation()) > 15) {
				player.getPacketDispatch().sendMessage("You can't catch a kebbit that far away.");
				return false;
			}
			if (player.getSkills().getLevel(Skills.HUNTER) < falconCatch.getLevel()) {
				player.getPacketDispatch().sendMessage("You need a Hunter level of at least " + falconCatch.getLevel() + " to catch this kebbit.");
				return false;
			}
			if (player.getEquipment().get(EquipmentContainer.SLOT_HANDS) != null || player.getEquipment().get(EquipmentContainer.SLOT_SHIELD) != null) {
				player.getDialogueInterpreter().sendDialogue("Sorry, free your hands, weapon, and shield slot first.");
				return false;
			}
			if (player.getEquipment().get(EquipmentContainer.SLOT_WEAPON) == null || !player.getEquipment().containsItem(FALCON)) {
				player.getPacketDispatch().sendMessage("You need a falcon to catch a kebbit.");
				return false;
			}
			if (player.getEquipment().remove(FALCON)) {
				player.getEquipment().add(GLOVE, true, false);
				sendProjectile();
			}
			node.lock(getDistance()+1);
			player.lock(getDistance()+1);
		}
		return true;
	}

	@Override
	public void stop() {
		super.stop();
		player.unlock();
	}

	@Override
	public void animate() {
	}

	@Override
	public boolean reward() {
		if (++ticks % getDistance() != 0) {
			return false;
		}
		final boolean success = success();
		player.getPacketDispatch().sendMessage(success ? "The falcon successfully swoops down and captures the kebbit." : "The falcon swoops down on the kebbit, but just misses catching it.");
		if (success) {
			node.finalizeDeath(player);
			final NPC falcon = NPC.create(5094, node.getLocation());
			falcon.setAttribute("falcon:owner", player.getUsername());
			falcon.setAttribute("falcon:catch", falconCatch);
			falcon.init();
			HintIconManager.registerHintIcon(player, falcon);
			playAudio(player, Sounds.HUNTING_FALCON_SWOOP_2634, 10, 1, node.getLocation(), 12);
			GameWorld.getPulser().submit(new Pulse(100, falcon) {
				@Override
				public boolean pulse() {
					if (!falcon.isActive()) {
						return true;
					}
					Projectile projectile = Projectile.create(node, Repository.findNPC(5093), 918);
					projectile.setSpeed(80);
					projectile.send();
					player.getPacketDispatch().sendMessage("Your falcon has left its prey. You see it heading back toward the falconer.");
					falcon.clear();
					return true;
				}
			});
		} else {
			if (player.getEquipment().remove(GLOVE)) {
				player.getEquipment().add(FALCON, true, false);
			}
		}
		player.face(null);
		return true;
	}

	/**
	 * Sends the projectile.
	 */
	private void sendProjectile() {
		Projectile projectile = Projectile.create(player, node, 918);
		projectile.setSpeed(80);
		projectile.setStartHeight(26);
		projectile.setEndHeight(1);
		projectile.send();
		playAudio(player, Sounds.HUNTING_FALCON_FLY_2633);
	}

	/**
	 * Gets the distance of the npc.
	 * @return the distance.
	 */
	public int getDistance() {
		return (int) (2 + (player.getLocation().getDistance(node.getLocation())) * 0.5);
	}

	/**
	 * Checks if the catch was successful.
	 * @return {@code True} if so.
	 */
	public boolean success() {
		if (originalLocation != node.getLocation()) {
			return RandomFunction.random(1, 3) == 2;
		}
		return ((RandomFunction.getRandom(3) * player.getSkills().getLevel(Skills.HUNTER)) / 3) > (falconCatch.getLevel() / 2);
	}

}
