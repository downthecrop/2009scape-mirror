package plugin.npc.bosses.kraken;
 
import org.crandor.game.content.global.BossKillCounter;
import org.crandor.game.node.entity.Entity;
import org.crandor.game.node.entity.combat.BattleState;
import org.crandor.game.node.entity.combat.CombatPulse;
import org.crandor.game.node.entity.combat.CombatStyle;
import org.crandor.game.node.entity.combat.DeathTask;
import org.crandor.game.node.entity.combat.ImpactHandler.HitsplatType;
import org.crandor.game.node.entity.combat.ImpactHandler.Impact;
import org.crandor.game.node.entity.npc.AbstractNPC;
import org.crandor.game.node.entity.npc.IdleAbstractNPC;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.item.Item;
import org.crandor.game.system.task.Pulse;
import org.crandor.game.world.map.Location;
import org.crandor.plugin.Plugin;
import org.crandor.plugin.PluginManager;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.tools.RandomFunction;
 
/**
 * Handles the Kraken boss NPC.
 * @author Emperor
 *
 */
@InitializablePlugin
public final class KrakenNPC extends IdleAbstractNPC {
	
	/**
	 * The drop location.
	 */
	private static final Location DROP_LOCATION = Location.create(3696, 5807, 0);
	
    /**
     * The tentacles.
     */
    private EnormousTentacleNPC[] tentacles = new EnormousTentacleNPC[4];
     
    /**
     * Constructs a new {@code KrakenNPC} {@code Object}.
     */
    public KrakenNPC() {
        this(8614, Location.create(3694, 5810, 0));
    }
    
    //Location.create(2486, 9797, 0)
     
    /**
     * Constructs a new {@code KrakenNPC} {@code Object}.
     * @param id The NPC id.
     * @param location The location.
     */
    public KrakenNPC(int id, Location location) {
        super(8616, id, location);
    }
     
    @Override
    public void init() {
        super.init();
        tentacles = new EnormousTentacleNPC[] { 
                new EnormousTentacleNPC(8615, Location.create(3691, 5809, 0)),
                new EnormousTentacleNPC(8615, Location.create(3700, 5809, 0)),
                new EnormousTentacleNPC(8615, Location.create(3700, 5814, 0)),
                new EnormousTentacleNPC(8615, Location.create(3691, 5814, 0))
        };
        for (EnormousTentacleNPC tentacle : tentacles) {
            tentacle.kraken = this;
            tentacle.init();
        }
        configureBossData();
    }
     
    @Override
    public void finalizeDeath(Entity entity) {
        for (EnormousTentacleNPC tentacle : tentacles) {
            if (!tentacle.isIdle()) {
                DeathTask.startDeath(tentacle, entity);
            }
        }
       
        if (entity instanceof Player) {
        	 if (RandomFunction.getRandom(1000) == 1) {
        		 ((Player) entity).asPlayer().getFamiliarManager().summon(new Item(14651), true);
        		 ((Player) entity).asPlayer().sendMessage("You have a funny feeling like you're being followed..");
             }
        	BossKillCounter.addtoKillcount((Player) entity, getId());
        }
        super.finalizeDeath(entity);
    }
    
	@Override
	public boolean inDisturbingRange(final Entity disturber) {
		if (!super.inDisturbingRange(disturber)) {
			return false;
		}
		CombatPulse.taskSwing(disturber, this, false, new Pulse(0) {
			@Override
			public boolean pulse() {
				disturb(disturber);
				return true;
			}
		});
		return true;
	}
 
    @Override
    public void disturb(final Entity disturber) {
        for (EnormousTentacleNPC tentacle : tentacles) {
            if (tentacle.isIdle()) {
                if (disturber instanceof Player) {
                    ((Player) disturber).getPacketDispatch().sendMessage("There was no response.");
                }
                getImpactHandler().getImpactQueue().add(new Impact(disturber, 0, CombatStyle.RANGE, HitsplatType.MISS));
                return;
            }
        }
        getImpactHandler().getImpactQueue().add(new Impact(disturber, 1, disturber.getProperties().getCombatPulse().getStyle(), HitsplatType.NORMAL));
        super.disturb(disturber);
    }
    
    @Override
    public void onRespawn() {
    	reTransform();
        for (EnormousTentacleNPC tentacle : tentacles) {
            tentacle.reTransform();
            tentacle.init();
        }
    }
     
    @Override
    public void goIdle() {
        for (IdleAbstractNPC tentacle : tentacles) {
            if (!tentacle.isIdle() && tentacle.inCombat()) {
                setAttribute("combat-time", System.currentTimeMillis() + 10000);
                return;
            }
        }
        super.goIdle();
        for (IdleAbstractNPC tentacle : tentacles) {
            tentacle.goIdle();
        }
    }
    
    @Override
	public boolean canDisturb(Entity disturber) {
		return false;
	}
	
	@Override
	public void checkImpact(BattleState state) {
		super.checkImpact(state);
		if (state.getStyle() != CombatStyle.MAGIC) {
			if (state.getEstimatedHit() > 0) {
				state.setEstimatedHit(state.getEstimatedHit() / 2);
			}
			if (state.getSecondaryHit() > 0) {
				state.setSecondaryHit(state.getSecondaryHit() / 2);
			}
			if (state.getAttacker() instanceof Player) {
				((Player) state.getAttacker()).sendMessage("Your " + (state.getStyle() == CombatStyle.RANGE ? "ranged" : "melee") + " attack has very little effect on the cave kraken.");
			}
		}
	}
     
    @Override
    public AbstractNPC construct(int id, Location location, Object... objects) {
        return new KrakenNPC(id, location);
    }
     
    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        init();
        PluginManager.definePlugin(new EnormousTentacleNPC());
        return super.newInstance(arg);
    }
    
    @Override
    public Location getDropLocation() {
    	return DROP_LOCATION;
    }
 
    @Override
    public int[] getIds() {
        return new int[] { 8614, 8616 };
    }
 
}
