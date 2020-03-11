package plugin.activity.jobs;
 
import org.crandor.cache.def.impl.NPCDefinition;
import org.crandor.game.content.global.jobs.GatheringJob;
import org.crandor.game.content.global.jobs.JobType;
import org.crandor.game.content.global.jobs.impl.MikasiJobs;
import org.crandor.game.content.global.jobs.impl.WilfredJobs;
import org.crandor.game.interaction.OptionHandler;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.npc.NPC;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.item.Item;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;
import org.crandor.tools.RandomFunction;
 
/**
 * Handles the work-for actions for the NPCs
 * @author ceik
 */
@InitializablePlugin
public final class WorkForOptionHandler extends OptionHandler {
    
    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        NPCDefinition.forId(4906).getConfigurations().put("option:work-for",this);
        return this;
    }
   
    @Override
    public boolean handle(Player player, Node node, String option) {
        if (player.getJobsManager().getJob() != null) {
        	if (player.getJobsManager().getJob().reward()) {
        		player.sendMessage("You completed the job");
        		player.getJobsManager().setJob(null);
        		return true;
        	}
        } else {
        	
        	switch (((NPC)node).getId()) {
        	case 4906://wilfred, woodcutting
        		while (true) {
        			int gen = RandomFunction.random(0, WilfredJobs.values().length -1);
        			if (WilfredJobs.values()[gen].getLvlReq() <= player.getSkills().getLevel(player.getSkills().WOODCUTTING)) {
        				if (WilfredJobs.values()[gen].getJobType() == JobType.GATHERING) {
        					player.getJobsManager().setJob(new GatheringJob(  ((NPC)node).getId(), player, WilfredJobs.values()[gen].getAmount(), WilfredJobs.values()[gen].getItemId()));
        				}
        				break;
        			}
        		}
        		break;
        		
        	case 4707:
        		while (true) {
        			int gen = RandomFunction.random(0, MikasiJobs.values().length -1);
        			if (WilfredJobs.values()[gen].getLvlReq() <= player.getSkills().getLevel(player.getSkills().WOODCUTTING)) {
        				player.getJobsManager().setJob(new GatheringJob(  ((NPC)node).getId(), player, WilfredJobs.values()[gen].getAmount(), WilfredJobs.values()[gen].getItemId()));
        				break;
        			}
        		}
        		break;
        	}
        	
        	if (player.getJobsManager().getJob() instanceof GatheringJob) {
        		player.getDialogueInterpreter().sendItemMessage(((GatheringJob)player.getJobsManager().getJob()).getItemId(), "Gather " + ((GatheringJob)player.getJobsManager().getJob()).getAmount() + " " + new Item(((GatheringJob)player.getJobsManager().getJob()).getItemId()).getName() + " for " + ((NPC)node).getName() );
        	}
        
        }
        return true;
    }
    
}