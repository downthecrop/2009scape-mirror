package plugin.npc.city.pollnivneach;

import org.crandor.cache.def.impl.NPCDefinition;
import org.crandor.game.content.dialogue.DialoguePlugin;
import org.crandor.game.interaction.OptionHandler;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.npc.NPC;
import org.crandor.game.node.entity.player.Player;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * Stand-in for bandit dialogue until blackjacking is implemented
 * @author ceik
 */

@InitializablePlugin
public class DesertBandit extends OptionHandler {
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        new BanditDialogue().init();
        NPCDefinition.forId(6388).getConfigurations().put("option:talk-to",this);
        return this;
    }
    public boolean handle(Player player, Node node, String options){
        NPC npc = (NPC) node;
        if(npc.getId() == 6388){
            if (options.equals("talk-to")){
                player.getDialogueInterpreter().open(6388);
            }
        }
        return true;
    }

    public class BanditDialogue extends DialoguePlugin {
        public BanditDialogue() {
            /**
             * Empty.
             */
        }
        public BanditDialogue(Player player){super(player);}

        @Override
        public DialoguePlugin newInstance(Player player){return new BanditDialogue(player);}

        @Override
        public boolean open(Object... args){
            npc("Go away.");
            return true;
        }

        @Override
        public boolean handle(int interfaceId, int buttonId){
            end();
            return true;
        }
        @Override
        public int[] getIds() {return new int[] {6388};}
    }
}
