package core.game.node.entity.skill.construction;


import core.game.node.entity.Entity;
import core.game.node.entity.player.Player;
import core.game.world.map.zone.MapZone;
import core.game.world.map.zone.ZoneRestriction;

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
        super("poh-zone" + house, true, ZoneRestriction.RANDOM_EVENTS, ZoneRestriction.FOLLOWERS);
        this.house = house;
    }

    @Override
    public void configure() {
        if (previousRegion != -1) {
            unregisterRegion(previousRegion);
        }
        if (previousDungeon != -1) {
            unregisterRegion(previousDungeon);
        }
        registerRegion(house.getRegion().getId());
        if (house.getDungeonRegion() != null) {
            registerRegion(house.getRegion().getId());
        }
    }

    @Override
    public boolean enter(Entity e) {
        return super.enter(e);
    }

    @Override
    public boolean death(Entity e, Entity killer) {
        if (e instanceof Player) {
            Player p = (Player) e;
            HouseManager.leave(p);
        }
        return true;
    }

    @Override
    public boolean leave(Entity e, boolean logout) {
        if (logout) {
            if (e instanceof Player) {
                Player p = (Player) e;
                HouseManager.leave(p);
                return true;
            }
        }
        return true;
    }
}