package plugin.dialogue;

import org.crandor.game.content.dialogue.DialoguePlugin;
import org.crandor.game.node.entity.npc.NPC;
import org.crandor.game.node.entity.player.Player;
import org.crandor.plugin.InitializablePlugin;

/**
 * Replacement for ArdougneBaker.asc
 * @author ceik
 */

@InitializablePlugin
public class ArdougneBaker extends DialoguePlugin {
    public ArdougneBaker(){
        /**
         * Empty on purpose
         */
    }
    public ArdougneBaker(Player player){super(player);}

    @Override
    public DialoguePlugin newInstance(Player player){return new ArdougneBaker(player);}

    @Override
    public boolean open(Object... args) {
        npc = (NPC)args[0];
        npc("Good day, monsieur. Would you like ze nice", "freshly-baked bread? Or perhaps a nice piece of cake?");
        stage = 1;
        return true;
    }

    @Override
    public final boolean handle(int interfaceId, int buttonId){
        switch(stage){
            case 1:
                interpreter.sendOptions("Choose an option.", "Let's see what you have.", "No thank you.");
                stage = 2;
                break;
            case 2:
                switch(buttonId){
                    case 1:
                        end();
                        npc.openShop(player);
                        stage = 20;
                        break;
                    case 2:
                        end();
                        break;
                }
                break;
        }
        return true;
    }
    @Override
    public int[] getIds() {
        return new int[] { 571 };
    }
}
