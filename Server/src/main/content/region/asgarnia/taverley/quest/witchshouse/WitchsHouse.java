package content.region.asgarnia.taverley.quest.witchshouse;

import core.game.node.entity.skill.Skills;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.quest.Quest;
import core.plugin.Initializable;

/**
 * Created for 2009Scape
 * User: Ethan Kyle Millard
 * https://www.youtube.com/watch?v=-RuHho3NbWg
 * Date: March 15, 2020
 * Time: 9:21 AM
 */
@Initializable
public class WitchsHouse extends Quest {

    /**
     * Constructs a new {@code WitchsHouse} {@code Object}.
     */
    public WitchsHouse() {
        super("Witch's House", 124, 123, 4, 226, 0, 1, 7);
    }

    @Override
    public void drawJournal(Player player, int stage) {
        super.drawJournal(player, stage);
        var line = 12;
        if(stage == 0){
            line(player, "I can start this quest by speaking to the !!little boy??", line++);
            line(player, "standing by the long garden just !!north of Taverly??.", line++);
            line(player, "I must be able to defeat a !!level 53 enemy??.", line++);
        } else {
            line(player, "A small boy kicked his ball over the fence into the nearby", line++, true);
            line(player, "garden, and I have agreed to retrieve it for him.", line++, true);
            if (stage == 10) {
                line(player,  "I should find a way into the !!garden?? where the !!ball?? is.", line++);
            }
            if (stage >= 100) {
                line(player,  "After puzzling through the strangely elaborate security", line++, true);
                line(player,  "system, and defeating a very strange monster, I returned", line++, true);
                line(player,  "the child's ball to him, and he thanked me for my help.", line++, true);
                line++;
                line(player,"<col=FF0000>QUEST COMPLETE!</col>", line);
            }
        }
    }

    @Override
    public void finish(Player player) {
        super.finish(player);
        player.getPacketDispatch().sendString("4 Quest Points", 277, 8 + 2);
        player.getPacketDispatch().sendString("6325 Hitpoints XP", 277, 9 + 2);
        player.getSkills().addExperience(Skills.HITPOINTS, 6325);
        player.getInterfaceManager().closeChatbox();
        player.getPacketDispatch().sendItemZoomOnInterface(2407, 240, 277, 3 + 2);
    }

    @Override
    public Quest newInstance(Object object) {
        return this;
    }
}
