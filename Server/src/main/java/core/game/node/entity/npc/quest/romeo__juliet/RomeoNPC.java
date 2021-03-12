package core.game.node.entity.npc.quest.romeo__juliet;

import core.game.node.entity.npc.AbstractNPC;
import core.game.world.map.Location;
import core.plugin.Initializable;

/**
 * Represents the abstract romeo npc.
 * @author 'Vexia
 */
@Initializable
public class RomeoNPC extends AbstractNPC {

	/**
	 * The NPC ids of NPCs using this plugin.
	 */
	private static final int[] ID = { 639 };

	/**
	 * Represents the delay of speaking to a random player.
	 */
	private static int speakDelay;

	/**
	 * Constructs a new {@code RomeoNPC} {@code Object}.
	 */
	public RomeoNPC() {
		super(0, null, true);
	}

	/**
	 * Constructs a new {@code RomeoNPC} {@code Object}.
	 * @param id The NPC id.
	 * @param location The location.
	 */
	private RomeoNPC(int id, Location location) {
		super(id, location, true);
	}

	@Override
	public AbstractNPC construct(int id, Location location, Object... objects) {
		return new RomeoNPC(id, location);
	}

	@Override
	public void init() {
		setWalks(true);
		super.init();
		setWalks(true);
	}

	@Override
	public void tick() {
		super.tick();
		/*if (speakDelay < GameWorld.getTicks()) {
			speakDelay = GameWorld.getTicks() + 30;
			for (Player p : RegionManager.getLocalPlayers(this, 2)) {
				if (!p.getInterfaceManager().isOpened() && RandomFunction.random(0, 8) == 2 && p.getQuestRepository().getQuest("Romeo & Juliet").getStage(p) == 0) {
					if (p.getDialogueInterpreter().getDialogue() != null || p.getDialogueInterpreter().getDialogueStage() != null) {
						continue;
					}
					p.getDialogueInterpreter().open(this.getId(), this);
					return;
				}
			}
		}*/
	}

	@Override
	public int[] getIds() {
		return ID;
	}

}
