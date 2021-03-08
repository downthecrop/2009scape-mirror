package core.game.node.entity.skill.cooking;

import core.game.interaction.NodeUsageEvent;
import core.game.interaction.UseWithHandler;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Represents the stew plugin.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public class MakeStewPlugin extends UseWithHandler {

	/**
	 * Constructs a new {@code MakeStewPlugin} {@code Object}.
	 */
	public MakeStewPlugin() {
		super(2001);
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		addHandler(114, OBJECT_TYPE, this);
		addHandler(2728, OBJECT_TYPE, this);
		addHandler(2729, OBJECT_TYPE, this);
		addHandler(2730, OBJECT_TYPE, this);
		addHandler(2731, OBJECT_TYPE, this);
		addHandler(2859, OBJECT_TYPE, this);
		addHandler(3039, OBJECT_TYPE, this);
		addHandler(4172, OBJECT_TYPE, this);
		addHandler(5275, OBJECT_TYPE, this);
		addHandler(8750, OBJECT_TYPE, this);
		addHandler(9682, OBJECT_TYPE, this);
		addHandler(12102, OBJECT_TYPE, this);
		addHandler(14919, OBJECT_TYPE, this);
		addHandler(16893, OBJECT_TYPE, this);
		addHandler(21792, OBJECT_TYPE, this);
		addHandler(22154, OBJECT_TYPE, this);
		addHandler(22713, OBJECT_TYPE, this);
		addHandler(22714, OBJECT_TYPE, this);
		addHandler(24283, OBJECT_TYPE, this);
		addHandler(24284, OBJECT_TYPE, this);
		addHandler(25730, OBJECT_TYPE, this);
		addHandler(33500, OBJECT_TYPE, this);
		addHandler(34410, OBJECT_TYPE, this);
		addHandler(34495, OBJECT_TYPE, this);
		addHandler(34546, OBJECT_TYPE, this);
		addHandler(34565, OBJECT_TYPE, this);
		addHandler(36973, OBJECT_TYPE, this);
		addHandler(37629, OBJECT_TYPE, this);
		return this;
	}

	@Override
	public boolean handle(NodeUsageEvent event) {
		event.getPlayer().getDialogueInterpreter().open(43989, event.getUsedItem().getId(), "stew");
		return true;
	}

}
