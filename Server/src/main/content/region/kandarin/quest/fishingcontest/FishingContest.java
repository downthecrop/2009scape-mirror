package content.region.kandarin.quest.fishingcontest;

import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.quest.Quest;
import core.game.node.item.Item;
import core.plugin.Initializable;
import core.game.node.entity.skill.Skills;
import content.data.Quests;

@Initializable
public class FishingContest extends Quest {
    public FishingContest(){super(Quests.FISHING_CONTEST,62,61,1,11,0,1,5);}
    public static final Item FISHING_ROD = new Item(307);
    public static final Item FISHING_PASS = new Item(27);
    public static final Item RED_VINE_WORM = new Item(25);
    public static final Item RAW_GIANT_CARP = new Item(338);
    public static final Item GIANT_CARP = new Item(337);
    public static final Item FISHING_TROPHY = new Item(26);
    public static final Item GARLIC = new Item(1550);
    public static final Item SPADE = new Item(952);

    // Winner is stranger in black if you did not do the garlic.
    // https://www.youtube.com/watch?v=6zL7M8mCL30

    @Override
    public void drawJournal(Player player, int stage) {
        int line = 12;
        super.drawJournal(player, stage);

        if (stage == 0) {
            line(player, "I can start this quest by speaking to the !!Dwarves?? at the", line++);
            line(player, "tunnel entrances on either side of !!White Wolf Mountain??.", line++);
            line(player,"!!I must have level 10 fishing.??", line++, player.getSkills().getLevel(Skills.FISHING) >= 10);
        } else if ( stage < 100 ) {
            line(player, "The Dwarves will let me use the tunnel through White Wolf", line++, true);
            line(player, "Mountain if I can will the Hemenster Fishing Competition.", line++, true);

            if (stage >= 20) {
                line(player,"I easily won the contest by catching some Giant Carp.", line++, false);
            } else if (stage >= 10) {
                // https://youtu.be/z4MfANC2KqI
                line(player, "They gave me a !!Fishing Contest Pass?? to enter the contest.", line++, false);
                line(player, "I need to bring them back the !!Hemenster Fishing Trophy??.", line++, false);
            }

            if (stage >= 100) {

            } else if (stage >= 20) {
                // https://youtu.be/rysJl-DRihE
                line(player, "I should take back the !!Trophy?? back to the !!Dwarf?? at the side of", line++, false);
                line(player, "!!White Wolf Mountain?? and claim my !!reward??.", line++, false);
            }

        } else {
            // https://youtu.be/u5Osw_jas4A
            line(player, "The Dwarves' wanted me to earn their friendship by winning", line++, true);
            line(player, "the Hemenster Fishing Competition.", line++, true);
            line(player, "I scared away a vampyre with some garlic and easily won the", line++, true);
            line(player, "contest by catching some Giant Carp.", line++, true);
            line++;
            line(player,"%%QUEST COMPLETE!&&", line++);
            line++;
            line(player, "As a reward for getting the Fishing Competition Trophy the", line++, false);
            line(player, "Dwarves will let me use their tunnel to travel quickly and", line++, false);
            line(player, "safely under White Wolf Mountain anytime I wish.", line++, false);
        }
    }

    @Override
    public void finish(Player player) {
        int ln = 10;
        super.finish(player);
        player.getPacketDispatch().sendItemZoomOnInterface(FISHING_TROPHY.getId(), 230, 277, 5);
        // https://youtu.be/8dK362LbYdE
        drawReward(player,"1 Quest Point",ln++);
        drawReward(player,"2437 Fishing XP",ln++);
        drawReward(player,"Access to Tunnel shortcut",ln);
        player.removeAttribute("fishing_contest:garlic");
        player.removeAttribute("fishing_contest:won");
        player.removeAttribute("fishing_contest:pass-shown");
        player.getSkills().addExperience(Skills.FISHING,2437);
    }

    @Override
    public Quest newInstance(Object object) {
        return this;
    }
}
