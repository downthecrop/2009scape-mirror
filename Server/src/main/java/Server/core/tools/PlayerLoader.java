package core.tools;


import core.game.node.entity.player.Player;
import core.game.node.entity.player.info.PlayerDetails;
import core.game.node.entity.player.info.login.PlayerParser;

/**
 * Represents a class that is used to load a player, or details of it.
 *
 * @author 'Vexia
 */
public final class PlayerLoader {

    /**
     * Method used to load the player file.
     *
     * @param name the name.
     * @return the player.
     */
    public static Player getPlayerFile(String name) {
        final PlayerDetails playerDetails = new PlayerDetails(name, "");
        playerDetails.parse();
        final Player player = new Player(playerDetails);
        PlayerParser.parse(player);
//        GameWorld.getWorld().getAccountService().loadPlayer(player);
        return player;
    }

    /**
     * Method used to load the player details file.
     *
     * @param name the name.
     * @return the details
     */
    public static PlayerDetails getPlayerDetailFile(String name) {
        final PlayerDetails playerDetails = new PlayerDetails(name, "");
        playerDetails.parse();
        return playerDetails;
    }
}
