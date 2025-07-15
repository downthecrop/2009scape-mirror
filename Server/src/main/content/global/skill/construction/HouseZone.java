package content.global.skill.construction;


import core.game.node.entity.Entity;
import core.game.node.entity.player.Player;
import core.game.world.map.zone.MapZone;
import core.game.world.map.RegionManager;
import core.game.world.map.Region;
import core.game.system.task.Pulse;
import core.game.world.map.zone.ZoneType;

import static core.api.ContentAPIKt.*;

/**
 * Handles the player owned house zone.
 *
 * @author Emperor
 */
public final class HouseZone extends MapZone {

    /**
     * The house manager.
     */
    private HouseManager house;

    /**
     * The previous house region id.
     */
    private int previousRegion = -1;

    /**
     * The previous dungeon region id.
     */
    private int previousDungeon = -1;

    /**
     * Constructs the house zone object.
     */
    public HouseZone(HouseManager house) {
        super("poh-zone" + house, true);
        this.house = house;
    }

    @Override
    public void configure() {
        setZoneType(ZoneType.P_O_H.getId());
        unregisterOldRegions();
        registerRegion(house.getHouseRegion().getId());
        if (house.getDungeonRegion() != null) {
            registerRegion(house.getDungeonRegion().getId());
        }
    }

    private void unregisterOldRegions() {
        if (previousRegion != -1) {
            unregisterRegion(previousRegion);
        }
        if (previousDungeon != -1) {
            unregisterRegion(previousDungeon);
        }
    }

    @Override
    public boolean enter(Entity e) {
        if (e instanceof Player) {
            Player pl = (Player) e;
            if (house == pl.getHouseManager()) {
                previousRegion = house.getHouseRegion().getId();
                if (house.getDungeonRegion() != null)
                    previousDungeon = house.getDungeonRegion().getId();
            }
            registerLogoutListener(pl, "houselogout", (p) -> {
                    p.setLocation(house.getLocation().getExitLocation());
                    return kotlin.Unit.INSTANCE;
            });
        }
        return super.enter(e);
    }

    @Override
    public boolean death(Entity e, Entity killer) {
        if (e instanceof Player) {
            Player p = (Player) e;
            HouseManager.leave(p);
	        return true;
        }
        return super.death(e, killer);
    }

    @Override
    public boolean leave(Entity e, boolean logout) {
        if (e instanceof Player) {
            Player p = (Player) e;
            // The below tears down the house if the owner was the one who left
            if (house == p.getHouseManager()) {
                house.expelGuests(p);
                int toRemove = previousRegion;
                int dungRemove = previousDungeon;
                submitWorldPulse(new Pulse(2) {
                    public boolean pulse() {
                        Region r = RegionManager.forId(toRemove);
                        Region dr = dungRemove != -1 ? RegionManager.forId(dungRemove) : null;
                        RegionManager.removeRegion(toRemove);
                        unregisterRegion(toRemove);
                        r.setActive(false);
                        if (dungRemove != -1) {
                            RegionManager.removeRegion(dungRemove);
                            unregisterRegion(dungRemove);
                            dr.setActive(false);
                        }
                        return true;
                    }
                });
            }
            // Clear logout listener and original-loc (if appropriate)
            clearLogoutListener(p, "houselogout");
            if (!getAttribute(p, "kidnapped-by-random", false)) {
                removeAttribute(p, "/save:original-loc");
            }
            return true;
        }
        return true;
    }
}
