package org.crandor.game.node.entity.player.link.statistics;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.entity.player.info.login.SavingModule;

/**
 * Stuff
 * @author jamix77
 *
 */
public class PlayerStatisticsManager implements SavingModule {
	
	/**
	 * Array of statistics.
	 */
	private final ArrayList<Statistic> STATISTICS = new ArrayList<Statistic>();

	
	private Statistic
	AL_KHARID_GATE_PASSES,
	FLAX_PICKED,
	CLUES_COMPLETED,
	ENTITIES_KILLED,
	DEATHS,
	LOGS_OBTAINED;

	
	/**
     * The player instance for this manager.
     */
    private final Player player;
    

	public PlayerStatisticsManager(Player player) {
		this.player = player;
		if (!player.isArtificial())
		initStats();
	}
	
	private void initStats() {
		AL_KHARID_GATE_PASSES = new Statistic(player,this);
		FLAX_PICKED = new Statistic(player,this);
		CLUES_COMPLETED = new Statistic(player,this);
		LOGS_OBTAINED = new Statistic(player,this);
		ENTITIES_KILLED = new Statistic(player,this);
		DEATHS = new Statistic(player,this);
	}

	@Override
	public void save(ByteBuffer buffer) {
		for (int i = 0; i < STATISTICS.size(); i++) {
			Statistic s = STATISTICS.get(i);
			buffer.put((byte) 1).putInt(i).putInt(s.getStatisticalAmount());
		}
		buffer.put((byte)0);
	}

	@Override
	public void parse(ByteBuffer buffer) {
		int opcode;
		while ((opcode = buffer.get() & 0xFF) != 0) {
			switch (opcode) {
			case 1:	
				int index = buffer.getInt();
				int amount = buffer.getInt();
				STATISTICS.get(index).setStatisticalAmount(amount);
				break;
			}
		}
		
	}
	
	/**
	 * Add a statistic to the arraylist.
	 * @param statistic
	 */
	public void addStatistic(Statistic statistic) {
		STATISTICS.add(statistic);
	}
	
	
	public ArrayList<Statistic> getSTATISTICS() {
		return STATISTICS;
	}

	public Statistic getAL_KHARID_GATE_PASSES() {
		return AL_KHARID_GATE_PASSES;
	}

	public Statistic getFLAX_PICKED() {
		return FLAX_PICKED;
	}

	public Statistic getCLUES_COMPLETED() {
		return CLUES_COMPLETED;
	}

	public Statistic getLOGS_OBTAINED() {
		return LOGS_OBTAINED;
	}

	public Statistic getENTITIES_KILLED() {
		return ENTITIES_KILLED;
	}

	public Statistic getDEATHS() {
		return DEATHS;
	}
	
	

}
