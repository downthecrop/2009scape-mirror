package content.global.skill.hunter;

import content.data.skill.SkillingPets;
import core.game.node.entity.skill.SkillPulse;
import core.game.node.entity.skill.Skills;
import core.game.node.entity.player.Player;
import core.game.node.scenery.Scenery;
import org.rs09.consts.Sounds;

import static core.api.ContentAPIKt.*;

/**
 * Handles the dismantling of a trap.
 * @author Vexia
 */
public final class TrapDismantlePulse extends SkillPulse<Scenery> {

	/**
	 * The trap wrapper.
	 */
	private final TrapWrapper wrapper;

	/**
	 * The trap type.
	 */
	private final Traps trap;

	/**
	 * The ticks passed.
	 */
	private int ticks;

	private final HunterManager instance;

	/**
	 * Constructs a new {@code TrapDismantlePulse} {@code Object}.
	 * @param player the player.
	 * @param node the node.
     */
	public TrapDismantlePulse(Player player, Scenery node, final TrapWrapper wrapper) {
		super(player, node);
		this.trap = wrapper.getType();
		this.wrapper = wrapper;
		this.instance = HunterManager.getInstance(player);
		if (checkRequirements()){
		switch(trap) {
			case BIRD_SNARE:
				lock(player,5);
				playAudio(player, Sounds.HUNTING_DISMANTLE_2632, 50);
				break;
			case BOX_TRAP:
				lock(player,4);
				playAudio(player, Sounds.HUNTING_DISMANTLE_2632, 50);
				break;
			case NET_TRAP:
				lock(player, 5);
				playAudio(player, Sounds.HUNTING_DISMANTLE_2632, 20);
				break;
			case RABBIT_SNARE:
			case DEAD_FALL:
				lock(player, 4);
				playAudio(player, Sounds.HUNTING_DISMANTLE_2632, 80);
				break;
			}
		}
	}

	@Override
	public boolean checkRequirements() {
		if (wrapper == null || !instance.isOwner(node)) {
			player.sendMessage("This isn't your trap!");
			return false;
		}
		final int itemCount = wrapper.getItems().size() + (wrapper.getType().getSettings().isObjectTrap() ? 0 : 1);
		final int difference = itemCount - player.getInventory().freeSlots();
		if (player.getInventory().freeSlots() < itemCount) {
			player.getPacketDispatch().sendMessage("You don't have enough inventory space. You need " + difference + " more free slot" + (difference > 1 ? "s" : "") + ".");
			return false;
		}
		return true;
	}

	@Override
	public void animate() {
		if (ticks < 1) {
			player.getAnimator().forceAnimation(trap.getSettings().getDismantleAnimation());
		}
	}

	@Override
	public boolean reward() {
		if (++ticks % (trap.getSettings().getDismantleAnimation().getDefinition().getDurationTicks() + 1) != 0) {
			return false;
		}
		if (wrapper.getType().getSettings().clear(wrapper, 1)) {
			instance.deregister(wrapper);
			if (wrapper.isCaught()) {
				if (wrapper.getType().equals(Traps.BOX_TRAP)) {
					for (int i : wrapper.getReward().getNpcIds()) {
						if (i == 5080 || i == 5079) {
							SkillingPets.checkPetDrop(player, i == 5080 ? SkillingPets.BABY_RED_CHINCHOMPA : SkillingPets.BABY_GREY_CHINCHOMPA);
						}
					}
				}
				player.getSkills().addExperience(Skills.HUNTER, wrapper.getReward().getExperience(), true);
			}			
			player.getPacketDispatch().sendMessage("You dismantle the trap.");
		}
		return true;
	}

	@Override
	public void message(int type) {
		switch (type) {
		case 0:
			int ticks = wrapper.getTicks() + (wrapper.getType().getSettings().getDismantleAnimation().getDefinition().getDurationTicks()) + 1;
			wrapper.setTicks(ticks);
			wrapper.setBusyTicks(ticks);
			break;
		}
	}

	/**
	 * Gets the trap.
	 * @return The trap.
	 */
	public Traps getTrap() {
		return trap;
	}

	/**
	 * Gets the wrapper.
	 * @return The wrapper.
	 */
	public TrapWrapper getWrapper() {
		return wrapper;
	}

}
