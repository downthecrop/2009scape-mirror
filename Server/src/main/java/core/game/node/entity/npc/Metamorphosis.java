package core.game.node.entity.npc;

import core.cache.def.impl.NPCDefinition;
import core.game.content.dialogue.DialoguePlugin;
import core.game.node.entity.skill.summoning.familiar.Familiar;
import core.game.node.entity.skill.summoning.pet.Pets;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.plugin.Plugin;
import rs09.plugin.ClassScanner;
import core.tools.RandomFunction;

/**
 * A superclass plugin for any pets that have a metamorphosis option.
 * @author Empathy
 *
 */
public abstract class Metamorphosis extends OptionHandler {

	/**
	 * The ids of the possible npcs to metamorph into.
	 */
	protected int[] ids;
	
	
	/**
	 * Constructs a new {@code Metamorphosis} {@code Object}.
	 * @param ids the id to transform.
	 */
	public Metamorphosis(int...ids) {
		this.ids = ids;
	}
	
	/**
	 * The dialogue plugin for the pet.
	 * @return the plugin.
	 */
	public abstract DialoguePlugin getDialoguePlugin();
	
	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		for (int id : getIds()) {
			NPCDefinition.forId(id).getHandlers().put("option:metamorphosis", this);
		}
		if (getDialoguePlugin() != null) {
			ClassScanner.definePlugin(getDialoguePlugin());
		}
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		Familiar familiar = (Familiar) node;
		switch (option) {
		case "metamorphosis":
			if (player.getFamiliarManager().isOwner(familiar)) {
				int newNpc = player.getFamiliarManager().getFamiliar().getId();
				while (newNpc == player.getFamiliarManager().getFamiliar().getId()) {
					newNpc = getRandomNpcId();
				}
				for (Pets p : Pets.values()) {
					if (p.getBabyNpcId() == newNpc) {
						player.getFamiliarManager().morphPet(new Item(p.getBabyItemId()), false, player.getFamiliarManager().getFamiliar().getLocation());
						break;
					}
				}
				player.getPacketDispatch().sendMessage("You transform your " + player.getFamiliarManager().getFamiliar().getName() + "!");
			} else {
				player.getPacketDispatch().sendMessage("This is not your familiar.");
			}
			break;
		}
		return true;
	}
	
	/**
	 * Gets a random npc id.
	 * @return
	 */
	public int getRandomNpcId() {
		int i = RandomFunction.getRandom(getIds().length - 1);
		return getIds()[i];
	}

	/**
	 * Gets the npc ids.
	 * @return the id.
	 */
	public int[] getIds() {
		return ids;
	}
}
