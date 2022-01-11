package core.game.node.entity.npc.city.pollnivneach;

import core.cache.def.impl.NPCDefinition;
import core.plugin.Initializable;
import core.game.content.dialogue.DialoguePlugin;
import core.game.content.dialogue.FacialExpression;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.system.task.Pulse;
import rs09.game.world.GameWorld;
import core.game.world.update.flag.context.Animation;
import core.plugin.Plugin;
import core.tools.RandomFunction;

/**
 * Handles Ali the Camel interaction
 * @author ceik
 */
@Initializable
public class AliTheCamelHandler extends OptionHandler {
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        new AliTheCamelDialogue().init();
        NPCDefinition.forId(1873).getHandlers().put("option:pet",this);
        return this;
    }

    public boolean handle(Player player, Node node, String options){
        NPC npc = (NPC) node;
        if(npc.getId() == 1873){
            if (options.equals("pet")){
                player.getDialogueInterpreter().open(1873);
            }
        }
        return true;
    }

    public class AliTheCamelDialogue extends DialoguePlugin {
        public AliTheCamelDialogue(){
            /**
             * Empty
             */
        }
        public AliTheCamelDialogue(Player player){super(player);}

        @Override
        public DialoguePlugin newInstance(Player player){return new AliTheCamelDialogue(player);}

        @Override
        public boolean open(Object... args){
            int phrase = RandomFunction.random(1,4);
            switch(phrase){
                case 1:
                    player(FacialExpression.AFRAID,"That beast would probably bite my fingers off","if I tried to pet it");
                    break;
                case 2:
                    player(FacialExpression.DISGUSTED,"I'm not going to pet that! I might get fleas","or something else that nasty creature","might have.");
                    break;
                case 3:
                    player(FacialExpression.THINKING,"Mmmm... Won't you make the nicest kebab?");
                    break;
            }
            return true;
        }
        @Override
        public boolean handle(int interfaceId, int buttonId) {
            switch (stage) {
                case 0:
                    player.lock();
                    player.animate(new Animation(7299));
                    player.getImpactHandler().setDisabledTicks(3);
                    GameWorld.getPulser().submit(new Pulse(4, player) {
                        @Override
                        public boolean pulse() {
                            player.unlock();
                            player.getAnimator().reset();
                            return true;
                        }
                    });
                    player.getDialogueInterpreter().sendDialogue("The camel tries to kick you for insulting it.");
                    stage = 1;
                    break;
                case 1:
                    end();
                    break;
            }
            return true;
        }
        @Override
        public int[] getIds(){return new int[] {1873};}
    }
}
