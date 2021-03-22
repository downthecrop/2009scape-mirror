package core.game.interaction.city.falador;

import core.game.interaction.Option;
import core.game.interaction.SpecialGroundInteraction;
import core.game.interaction.SpecialGroundItems;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.world.map.RegionManager;

import java.util.List;

public class WineOfZamorakInteraction extends SpecialGroundInteraction {
    @Override
    public void handle(Player player, Option option){
        player.faceLocation(SpecialGroundItems.WINE_OF_ZAMORAK.getLocation());
        final List<NPC> npcs = RegionManager.getLocalNpcs(player);
        for (NPC n : npcs) {
            if (n.getId() == 188) {
                n.sendChat("Hands off zamorak's wine!");
                n.getProperties().getCombatPulse().attack(player);
            }
        }
    }
}
