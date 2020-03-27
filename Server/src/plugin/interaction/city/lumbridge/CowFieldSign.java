package plugin.interaction.city.lumbridge;

import org.crandor.cache.def.impl.ObjectDefinition;
import org.crandor.game.content.dialogue.DialoguePlugin;
import org.crandor.game.interaction.OptionHandler;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.player.Player;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * Cow field sign manager
 * @author ceik
 */

@InitializablePlugin
public class CowFieldSign extends OptionHandler {
    private int DIALOGUE_KEY = 87905733;
    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable{
        new SignDialogue().init();
        ObjectDefinition.forId(31297).getConfigurations().put("option:read",this);
        return this;
    }
    @Override
    public boolean handle(Player player, Node node, String option){
        if(node.getId() == 31297){
            player.getDialogueInterpreter().open(DIALOGUE_KEY);
        }
        return true;
    }
    public final class SignDialogue extends DialoguePlugin{
        public SignDialogue(){
            /**
             * Empty
             */
        }
        public SignDialogue(Player player){super(player);}
        @Override
        public DialoguePlugin newInstance(Player player){return new SignDialogue(player);}
        @Override
        public boolean open(Object... args){
            interpreter.sendPlainMessage(false,"Wandering adventurers have killed " + CowPenZone.CowDeaths + " cows in this field.", "Local farmers call it an epidemic.");
            return true;
        }

        @Override
        public boolean handle(int componentId, int buttonId){
            end();
            return true;
        }
        public int[] getIds() {return new int[] {DIALOGUE_KEY};}
    }
}
