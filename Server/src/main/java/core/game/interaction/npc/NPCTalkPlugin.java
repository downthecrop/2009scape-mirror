package core.game.interaction.npc;

import core.cache.def.impl.NPCDefinition;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.game.world.map.Location;
import core.plugin.Initializable;
import core.plugin.Plugin;
import core.game.content.activity.gnomecooking.GnomeCookingJob;
import core.game.content.activity.gnomecooking.GnomeTipper;
import core.game.content.dialogue.FacialExpression;

import static core.game.content.activity.gnomecooking.GnomeCookingConstantsKt.*;
import static core.tools.stringtools.StringToolsKt.colorize;

/**
 * Handles the NPC talk-to option.
 * @author Emperor
 */
@Initializable
public final class NPCTalkPlugin extends OptionHandler {

	@Override
	public Location getDestination(Node n, Node node) {
		NPC npc = (NPC) node;
		if (npc.getAttribute("facing_booth", false)) {
			int offsetX = npc.getDirection().getStepX() << 1;
			int offsetY = npc.getDirection().getStepY() << 1;
			return npc.getLocation().transform(offsetX, offsetY, 0);
		}
		return null;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		final NPC npc = (NPC) node;
		if (!npc.getAttribute("facing_booth", false)) {
			npc.faceLocation(player.getLocation());
		}
		//I'm sorry for this but it was honestly the best way to do this
		if(player.getAttribute(GC_BASE_ATTRIBUTE +":"+ GC_JOB_ORDINAL,-1) != -1){
			GnomeCookingJob job = GnomeCookingJob.values()[player.getAttribute(GC_BASE_ATTRIBUTE + ":" + GC_JOB_ORDINAL,-1)];
			if(node.getId() == job.getNpc_id() && !player.getAttribute(GC_BASE_ATTRIBUTE + ":" + GC_JOB_COMPLETE,false)){
				Item neededItem = player.getAttribute(GC_BASE_ATTRIBUTE + ":" + GC_NEEDED_ITEM,null);
				if(neededItem != null && player.getInventory().containsItem(neededItem)) {
					player.getDialogueInterpreter().sendDialogues(job.getNpc_id(), FacialExpression.OLD_HAPPY, "Thank you!");
					player.getInventory().remove(neededItem);
					player.getInventory().add(GnomeTipper.getTip(job.getLevel()));
					player.removeAttribute(GC_BASE_ATTRIBUTE + ":" + GC_JOB_ORDINAL);
					player.removeAttribute(GC_BASE_ATTRIBUTE + ":" + GC_NEEDED_ITEM);
					int curPoints = player.getAttribute(GC_BASE_ATTRIBUTE + ":" + GC_POINTS,0);
					curPoints += 3;
					if(curPoints == 12){
						player.getInventory().add(new Item(9474));
						player.sendMessage(colorize("%RYou have been granted a food delivery token. Use it to have food delivered."));
					} else if(curPoints % 12 == 0){
						int curRedeems = player.getAttribute(GC_BASE_ATTRIBUTE + ":" + GC_REDEEMABLE_FOOD,0);
						player.setAttribute("/save:" + GC_BASE_ATTRIBUTE + ":" + GC_REDEEMABLE_FOOD,curRedeems != 10 ? ++curRedeems : curRedeems);
						player.sendMessage(colorize("%RYou have been granted a single food delivery charge."));
					}
					player.setAttribute("/save:" + GC_BASE_ATTRIBUTE + ":" + GC_POINTS,curPoints);
				}
				return true;
			}
		}
		return player.getDialogueInterpreter().open(npc.getId(), npc);
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		NPCDefinition.setOptionHandler("talk-to", this);
		NPCDefinition.setOptionHandler("talk", this);
		return this;
	}
}
