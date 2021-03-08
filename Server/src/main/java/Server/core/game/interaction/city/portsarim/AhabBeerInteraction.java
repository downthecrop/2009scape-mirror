package core.game.interaction.city.portsarim;

import core.game.interaction.Option;
import core.game.interaction.SpecialGroundInteraction;
import core.game.interaction.SpecialGroundItems;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;

/**
 * Handles Ahab's beer in port sarim
 * @author ceik
 */

public class AhabBeerInteraction extends SpecialGroundInteraction {
    //The dialogue key
    public static final int DIALOGUE_KEY = 2692;

    @Override
    public void handle(final Player player, final Option option){
        configure();
        if(option.getName() == "take"){
            player.faceLocation(SpecialGroundItems.AHAB_BEER.getLocation());
            player.getDialogueInterpreter().open(DIALOGUE_KEY, new NPC(2692), false);
        } else {
            player.debug("Unhandled option: " + option.getName());
        }
    }
}
