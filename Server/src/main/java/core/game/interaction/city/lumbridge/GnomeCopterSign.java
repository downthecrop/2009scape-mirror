package core.game.interaction.city.lumbridge;

import core.cache.def.impl.ObjectDefinition;
import core.plugin.Initializable;
import core.game.content.dialogue.DialoguePlugin;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.plugin.Plugin;

@Initializable
public class GnomeCopterSign extends OptionHandler {
    public int DIALOGUE_ID = 989769182;
    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        new SignDialogue().init();
        ObjectDefinition.forId(30037).getHandlers().put("option:read",this);
        return this;
    }

    @Override
    public boolean handle(Player player, Node node, String string) {
        if (node.getId() == 30037) {
            player.getDialogueInterpreter().open(DIALOGUE_ID);
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
        public DialoguePlugin newInstance(Player player){ return new SignDialogue(player);}

        @Override
        public boolean open(Object... args){
            interpreter.sendPlainMessage(false,"Come check out our gnome copters up north!","Disclaimer: EXTREMELY WIP");
            stage = 0;
            return true;
        }
        @Override
        public boolean handle(int componentId, int buttonId){
            end();
            return true;
        }
        @Override
        public int[] getIds(){return new int[] {DIALOGUE_ID};}
    }
}
