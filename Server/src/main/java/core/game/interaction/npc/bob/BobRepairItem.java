package core.game.interaction.npc.bob;

import core.game.content.global.RepairItem;
import core.game.interaction.NodeUsageEvent;
import core.game.interaction.UseWithHandler;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.plugin.Plugin;
import core.plugin.Initializable;
import rs09.plugin.ClassScanner;

/**
 * Represents the plugin used to handle an item being used on bob.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class BobRepairItem extends UseWithHandler {

	/**
	 * Constructs a new {@code BobRepairItem} {@code Object}.
	 */
	public BobRepairItem() {
		super(494, 468, 496, 470, 498, 472, 500, 502, 474, 504, 476, 506, 478, 6741, 4856, 4857, 4858, 4859, 4860, 4862, 4863, 4864, 4865, 4866, 4868, 4869, 4870, 4871, 4872, 4874, 4875, 4876, 4877, 4878, 4880, 4881, 4882, 4883, 4884, 4886, 4887, 4888, 4889, 4890, 4892, 4893, 4894, 4895, 4896, 4898, 4899, 4900, 4901, 4902, 4904, 4905, 4906, 4907, 4908, 4910, 4911, 4912, 4913, 4914, 4916, 4917, 4918, 4919, 4920, 4922, 4923, 4924, 4925, 4926, 4928, 4929, 4930, 4931, 4932, 4934, 4935, 4936, 4937, 4938, 4940, 4941, 4942, 4943, 4944, 4946, 4947, 4948, 4949, 4950, 4952, 4953, 4954, 4955, 4956, 4958, 4959, 4960, 4961, 4962, 4964, 4965, 4966, 4967, 4968, 4970, 4971, 4972, 4973, 4974, 4976, 4977, 4978, 4979, 4980, 4982, 4983, 4984, 4985, 4986, 4988, 4989, 4990, 4991, 4992, 4994, 4995, 4996, 4997, 4998);
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		addHandler(519, NPC_TYPE, this);
		addHandler(3797, NPC_TYPE, this);
		ClassScanner.definePlugin(new BobDialogue());
		return this;
	}

	@Override
	public boolean handle(NodeUsageEvent event) {
		final Player player = event.getPlayer();
		final RepairItem repair = RepairItem.forId(event.getUsedItem().getId());
		if (repair == null && !BobDialogue.BarrowsEquipment.isBarrowsItem(event.getUsedItem().getId())) {
			player.getDialogueInterpreter().open(519, ((NPC) event.getUsedWith()), true, true, null);
			return true;
		}
		player.getDialogueInterpreter().open(519, ((NPC) event.getUsedWith()), true, false, event.getUsedItem().getId(), event.getUsedItem());
		return true;
	}

}
