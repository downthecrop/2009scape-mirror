package core.game.node.entity.skill.herblore;

import core.game.interaction.NodeUsageEvent;
import core.game.interaction.UseWithHandler;
import core.net.packet.PacketRepository;
import core.net.packet.context.ChildPositionContext;
import core.net.packet.out.RepositionChild;
import core.plugin.Initializable;
import core.plugin.Plugin;
import rs09.game.content.dialogue.SkillDialogueHandler;

/**
 * Represents the plugin used to create a herb tar.
 * @author  Ceikry, fawk Vexia and his bad design.
 * @version 1.0
 */
@Initializable
public final class HerbTarPlugin extends UseWithHandler {

	/**
	 * Constructs a new {@code HerbTarPlugin} {@code Object}.
	 */
	public HerbTarPlugin() {
		super(1939);
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		for (Tars tar : Tars.values()) {
			addHandler(tar.getIngredient().getId(), ITEM_TYPE, this);
		}
		return this;
	}

	@Override
	public boolean handle(NodeUsageEvent event) {
		//Tars.forItem(event.getUsedItem().getId() == 1939 ? event.getBaseItem() : event.getUsedItem())
		SkillDialogueHandler handler = new SkillDialogueHandler(event.getPlayer(), SkillDialogueHandler.SkillDialogue.ONE_OPTION,Tars.forItem(event.getUsedItem().getId() == 1939 ? event.getBaseItem() : event.getUsedItem()).getTar()){
			@Override
			public void create(int amount, int index) {
				event.getPlayer().getPulseManager().run(new HerbTarPulse(event.getPlayer(), null, Tars.forItem(event.getUsedItem().getId() == 1939 ? event.getBaseItem() : event.getUsedItem()), amount));
			}
			@Override
			public int getAll(int index) {
				return event.getPlayer().getInventory().getAmount(event.getUsedItem());
			}
		};
		handler.open();
		PacketRepository.send(RepositionChild.class, new ChildPositionContext(event.getPlayer(), 309, 2, 210, 15));
		return true;
	}

}
