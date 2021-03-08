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
 * Handles Rokuh, who runs the Nardah chocolate stall
 * @author ceik
 */

@Initializable
public class Rokuh extends OptionHandler {
    public final int NPC_ID = 3045;

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        new RokuhDialogue().init();
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

    final class RokuhDialogue extends DialoguePlugin {
        public RokuhDialogue(){
            /**
             * Empty
             */
        }
        public RokuhDialogue(Player player){
            super(player);
        }
        @Override
        public DialoguePlugin newInstance(Player player){return new RokuhDialogue(player);}

        @Override
        public boolean open(Object... args){
            npc("Come one, come all, buy my amazing choc ice invention here!","Chocolate on the outside. Iced cream on the inside.","Oh I also have some chocolate left over from making","choc ices which I'm selling too.");
            return true;
        }

        @Override
        public boolean handle(int interfaceId, int buttonId){
            switch(stage) {
                case 0:
                    //TODO: Add more options after Spirit of the Elid.
                    interpreter.sendOptions("Select one", "Cool I'd like to buy some", "No thanks, I'm not interested.", "How do you stop your icy snacks melting?");
                    stage++;
                    break;
                case 1:
                    switch(buttonId){
                        case 1:
                            end();
                            NPC Rokuh = new NPC(NPC_ID);
                            Rokuh.openShop(player);
                            break;
                        case 2:
                            player("No thanks, I'm not interested");
                            stage = 10;
                            break;
                        case 3:
                            player("How do you stop your icy snacks melting?","The middle of the desert is the last place I'd expect to see ice.");
                            stage++;
                            break;
                    }
                    break;
                case 2:
                    npc("It's quite a surprise, isn't it, my friend. I actually have this","special magic box of ice, which I bought for a princely sum","from a strange man while adventuring in lands far away","He said it is imbued with some sort of");
                    stage++;
                    break;
                case 3:
                    npc("powerful ice magic which keeps it cold all the time. I had","never seen any ice magic before, it's clearly very rare","and powerful, so I felt I had to buy it.");
                    stage = 10;
                    break;
                case 10:
                    end();
                    break;
            }
            return true;
        }
        @Override
        public int[] getIds() {return new int[]{NPC_ID};}
    }
}
