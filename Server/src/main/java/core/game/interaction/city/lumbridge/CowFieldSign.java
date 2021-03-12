package core.game.interaction.city.lumbridge;

import core.cache.def.impl.ObjectDefinition;
import core.game.content.dialogue.DialoguePlugin;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Cow field sign manager
 * @author ceik
 */

@Initializable
public class CowFieldSign extends OptionHandler {
    private int DIALOGUE_KEY = 87905733;
    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable{
        new SignDialogue().init();
        ObjectDefinition.forId(31297).getHandlers().put("option:read",this);
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
