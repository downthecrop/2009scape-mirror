package core.game.node.entity.npc.city.nardah;

import core.cache.def.impl.NPCDefinition;
import core.game.content.dialogue.DialoguePlugin;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Handles Kazemde, who runs the general store in Nardah.
 * @author ceik
 */

@Initializable
public class Kazemde extends OptionHandler {
    public final int NPC_ID = 3039;

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        new KazemdeDialogue().init();
        NPCDefinition.forId(NPC_ID).getHandlers().put("option:trade",this);
        NPCDefinition.forId(NPC_ID).getHandlers().put("option:talk-to",this);
        return this;
    }

    @Override
    public final boolean handle(Player player, Node node, String string){
        NPC npc = node.asNpc();
        switch(string){
            case "trade":
                npc.openShop(player);
                break;
            case "talk-to":
                player.getDialogueInterpreter().open(NPC_ID);
                break;
        }
        return true;
    }

    final class KazemdeDialogue extends DialoguePlugin {
        public KazemdeDialogue(){
            /**
             * Empty
             */
        }
        public KazemdeDialogue(Player player){
            super(player);
        }

        @Override
        public DialoguePlugin newInstance(Player player){return new KazemdeDialogue(player);}

        @Override
        public boolean open(Object... args){
            npc("Hello, and welcome to my shop.");
            return true;
        }

        @Override
        public boolean handle(int interfaceId, int buttonId){
            switch(stage){
                case 0:
                    interpreter.sendOptions("Select one","Let me see what you have.","Goodbye.");
                    stage++;
                    break;
                case 1:
                    switch(buttonId){
                        case 1:
                            end();
                            NPC Kazemde = new NPC(NPC_ID);
                            Kazemde.openShop(player);
                            break;
                        case 2:
                            end();
                            break;
                    }
            }
            return true;
        }
        public int[] getIds() {return new int[] {NPC_ID};}
    }
}
