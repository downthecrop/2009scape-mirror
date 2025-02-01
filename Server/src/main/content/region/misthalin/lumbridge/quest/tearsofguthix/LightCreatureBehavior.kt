package content.region.misthalin.lumbridge.quest.tearsofguthix

import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.game.world.GameWorld.ticks
import core.game.world.map.Location
import core.game.world.map.path.Pathfinder
import core.tools.RandomFunction
import org.rs09.consts.NPCs

class LightCreatureBehavior : NPCBehavior(NPCs.LIGHT_CREATURE_2021) {

    companion object {
        fun moveLightCreature(self: NPC, location: Location) {
            self.setNextWalk()
            Pathfinder.find(self, location, true, Pathfinder.PROJECTILE).walk(self)
        }

    }

    override fun tick(self: NPC): Boolean {
        if (!self.locks.isMovementLocked) {
            self.isWalks = true
            self.walkRadius = 20
            if (self.isWalks && !self.pulseManager.hasPulseRunning() && self.nextWalk < ticks) {
                self.setNextWalk()
                val l: Location = self.location.transform(-5 + RandomFunction.random(self.walkRadius), -5 + RandomFunction.random(self.walkRadius), 0)
                if (self.canMove(l)) {
                    Pathfinder.find(self, l, true, Pathfinder.PROJECTILE).walk(self)
                }
            }
        }
        return true
    }

}

/*

	/**
	 * Handles the sapphire lantern on a light creature.
	 * @author Vexia
	 *
	 */
	public class LightCreatureHandler extends UseWithHandler {

		/**
		 * Constructs the {@code LightCreatureHandler}
		 */
		public LightCreatureHandler() {
			super( 4700, 4701, 4702);
		}

		@Override
		public Plugin<Object> newInstance(Object arg) throws Throwable {
			addHandler(2021, NPC_TYPE, this);
			return this;
		}

		@Override
		public boolean handle(NodeUsageEvent event) {
			final Player player = event.getPlayer();
                        if (!hasRequirement(player, "While Guthix Sleeps"))
                            return true;
			player.lock(2);
			player.teleport(Location.create(2538, 5881, 0));
			return true;
		}

		@Override
		public Location getDestination(Player player, Node with) {
			if (player.getLocation().withinDistance(with.getLocation())) {
				return player.getLocation();
			}
			return null;
		}

	}


	/**
	 * Handles the light creature npc.
	 * @author Vexia
	 *
	 */
	public class LightCreatureNPC extends AbstractNPC {

		/**
		 * Constructs the {@code LightCreatureNPC}
		 */
		public LightCreatureNPC() {
			super(0, null);
			this.setWalks(true);
			this.setWalkRadius(10);
		}

		/**
		 * Constructs the {@code LightCreatureNPC}
		 */
		public LightCreatureNPC(int id, Location location) {
			super(id, location);
		}

		@Override
		public AbstractNPC construct(int id, Location location, Object... objects) {
			return new LightCreatureNPC(id, location);
		}

		@Override
		public void handleTickActions() {
			if (!getLocks().isMovementLocked()) {
				if (isWalks() && !getPulseManager().hasPulseRunning() && nextWalk < GameWorld.getTicks()) {
					setNextWalk();
					Location l = getLocation().transform(-5 + RandomFunction.random(getWalkRadius()), -5 + RandomFunction.random(getWalkRadius()), 0);
					if (canMove(l)) {
						Pathfinder.find(this, l, true, Pathfinder.PROJECTILE).walk(this);
					}
				}
			}
		}

		@Override
		public int[] getIds() {
			return new int[] {2021};
		}

	}
}
 */