package plugin.activity.jobs;
 
import org.crandor.cache.def.impl.NPCDefinition;
import org.crandor.game.content.global.jobs.impl.GatheringJob;
import org.crandor.game.content.global.jobs.impl.GatheringJob.GatheringJobs;
import org.crandor.game.content.global.jobs.impl.SlayingJob;
import org.crandor.game.content.global.jobs.impl.SlayingJob.SlayingJobs;
import org.crandor.game.interaction.OptionHandler;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.npc.NPC;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.item.Item;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;
import org.crandor.tools.RandomFunction;
import org.crandor.tools.StringUtils;
 
/**
 * Handles the work-for actions for the NPCs
 * @author jamix77
 */
@InitializablePlugin
public final class WorkForOptionHandler extends OptionHandler {
    
    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        NPCDefinition.forId(4906).getConfigurations().put("option:work-for",this);
        NPCDefinition.forId(4707).getConfigurations().put("option:work-for", this);
        NPCDefinition.forId(4904).getConfigurations().put("option:work-for", this);
        NPCDefinition.forId(4903).getConfigurations().put("option:work-for", this);
        NPCDefinition.forId(4902).getConfigurations().put("option:work-for", this);
        NPCDefinition.forId(4901).getConfigurations().put("option:work-for", this);
        NPCDefinition.forId(4899).getConfigurations().put("option:work-for", this);
        NPCDefinition.forId(3807).getConfigurations().put("option:work-for", this);
        NPCDefinition.forId(1861).getConfigurations().put("option:work-for", this);
        NPCDefinition.forId(922).getConfigurations().put("option:work-for", this);
        NPCDefinition.forId(705).getConfigurations().put("option:work-for", this);
        NPCDefinition.forId(0).getConfigurations().put("option:work-for", this);
        return this;
    }
   
    @Override
    public boolean handle(Player player, Node node, String option) {
        if (player.getJobsManager().getJob() != null) {
        	if ( ((NPC)node).getId() != player.getJobsManager().getJob().getEmployer()) {
        		player.getDialogueInterpreter().sendPlainMessage(false, "You're already working for somebody else.");
        		return true;
        	}
        	if (player.getJobsManager().getJob().reward()) {
        		player.getDialogueInterpreter().sendPlainMessage(false,"You are rewarded for your services.");
        		player.getJobsManager().setJob(null);
        		player.getJobsManager().incrementJobs();
        		if (player.getJobsManager().getJobsDone() % 15 == 0) {
        			player.getInventory().add(new Item(11141,1));
        		}
        		return true;
        	}
        } else {
        	
        	switch (((NPC)node).getId()) {
        	case 0://hans
        	case 705://harlan, melee tutor
        	case 922://aggie, the witch in draynor
        	case 1861://nemarti, ranged tutor
        	case 3807://gillie groats, the milk lady in the cow farm.
        	case 4899://cordero, cooking tutor
        	case 4901://finlay, fishing tutor
        	case 4902://monlum, mining tutor
        	case 4903://yauchomi, prayer tutor
        	case 4904://feoras, smelting tutor
        	case 4906://wilfred, woodcutting tutor  		
        	case 4707://mikasi, mage tutor
        		
        		while (true) {
        			int type = RandomFunction.random(0,2);
        			
        			switch (type) {
        			case 0://gathering
        				int gen = RandomFunction.random(0, GatheringJobs.values().length -1);
            			if (GatheringJobs.values()[gen].getLvlReq() <= player.getSkills().getLevel(GatheringJobs.values()[gen].getSkillId())) {
            				player.getJobsManager().setJob(new GatheringJob(  ((NPC)node).getId(), player, GatheringJobs.values()[gen].getAmount(), GatheringJobs.values()[gen].getid()));
            				break;
            			}
        				break;
        			case 1://slaying
        				int gen1 = RandomFunction.random(0,SlayingJobs.values().length - 1);
        				player.getJobsManager().setJob(new SlayingJob(((NPC)node).getId(), player, SlayingJobs.values()[gen1].getAmount(),SlayingJobs.values()[gen1].getNpcId()));
        				break;
        			}
        			break;
        			
        		}
        	}
        	
        	if (player.getJobsManager().getJob() instanceof GatheringJob) {
        		player.getDialogueInterpreter().sendItemMessage(((GatheringJob)player.getJobsManager().getJob()).getItemId(), "Gather " + ((GatheringJob)player.getJobsManager().getJob()).getAmount() + " " + new Item(((GatheringJob)player.getJobsManager().getJob()).getItemId()).getName() + " for " + ((NPC)node).getName() );
        	} else if (player.getJobsManager().getJob() instanceof SlayingJob) {
        		player.getDialogueInterpreter().sendItemMessage(1321, "Slay " + ((SlayingJob)player.getJobsManager().getJob()).getAmount() + " " + StringUtils.plusS(new NPC(((SlayingJob)player.getJobsManager().getJob()).getNpcId()[0]).getName()));
        	}
        
        }
        return true;
    }
    
}