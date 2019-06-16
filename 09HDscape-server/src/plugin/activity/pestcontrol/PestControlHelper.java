package plugin.activity.pestcontrol;

import org.crandor.game.node.entity.player.Player;
import org.crandor.game.world.map.Location;
import org.crandor.game.world.map.zone.ZoneBorders;

import java.util.Arrays;
import java.util.List;

public class PestControlHelper {

    public enum BoatInfo
    {
        NOVICE(new ZoneBorders(2660, 2638, 2663, 2643), new ZoneBorders(2657, 2638, 2657, 2641)),
        INTERMEDIATE(new ZoneBorders(2638, 2642, 2641, 2647), new ZoneBorders(2644, 2642, 2644, 2646)),
        VERTERAN(new ZoneBorders(2632, 2649, 2635, 2654), new ZoneBorders(2639, 2652, 2638, 2655));

        BoatInfo(ZoneBorders boatBorder, ZoneBorders outsideBoatBorder)
        {
            this.boatBorder = boatBorder;
            this.outsideBoatBorder = outsideBoatBorder;
        }

        public ZoneBorders boatBorder;
        public ZoneBorders outsideBoatBorder;
    }

    public static final int NOVICE_GANGPLANK = 14315;
    public static List<Integer> GATE_ENTRIES = Arrays.asList(14233, 14235);

    public static final Location PestControlIslandLocation = Location.create(2659, 2649, 0);

    public static boolean isInPestControlInstance(Player p)
    {
        return p.getAttribute("pc_zeal") != null;
    }
    public static boolean landerContainsLoc(Location l)
    {
        for (BoatInfo i : BoatInfo.values())
            if (i.boatBorder.insideBorder(l))
                return true;
        return false;
    }
    public static boolean outsideGangplankContainsLoc(Location l)
    {
        for (BoatInfo i : BoatInfo.values())
            if (i.outsideBoatBorder.insideBorder(l))
                return true;
        return false;
    }


}
