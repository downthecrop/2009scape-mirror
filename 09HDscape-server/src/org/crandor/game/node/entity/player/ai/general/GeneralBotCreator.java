package org.crandor.game.node.entity.player.ai.general;

import org.crandor.game.node.entity.player.ai.AIPBuilder;
import org.crandor.game.node.entity.player.ai.AIPlayer;
import org.crandor.game.node.entity.player.ai.general.scriptrepository.Script;
import org.crandor.game.system.task.Pulse;
import org.crandor.game.world.GameWorld;
import org.crandor.game.world.map.Location;
import org.crandor.game.world.repository.Repository;

public class GeneralBotCreator {

    public GeneralBotCreator(String botName, Location loc, Script botScript)
    {
        AIPlayer bot = AIPBuilder.create(botName, loc);
        Repository.getPlayers().add(bot);
        bot.init();
        botScript.setPlayer(bot);

        GameWorld.submit(new Pulse(1, bot) {
            int ticks = 0;
            @Override
            public boolean pulse() {
                if (ticks ++ == 5000)
                {
                    AIPlayer.deregister(bot.getUid());
                    return true;
                }

                bot.runScript(botScript);
                return false;
            }
        });
    }
}
