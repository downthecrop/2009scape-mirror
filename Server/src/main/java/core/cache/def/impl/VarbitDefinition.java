package core.cache.def.impl;

import core.cache.Cache;
import core.game.node.entity.player.Player;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles config definition reading.
 * @author Emperor
 */
public final class VarbitDefinition {

	/**
	 * The config definitions mapping.
	 */
	private static final Map<Integer, VarbitDefinition> MAPPING = new HashMap<>();

	/**
	 * The bit size flags.
	 */
	private static final int[] BITS = new int[32];

	/**
	 * The file id.
	 */
	private final int id;

	/**
	 * The config id.
	 */
	private int varpId;

	/**
	 * The bit shift amount.
	 */
	private int startBit;

	/**
	 * The bit amount.
	 */
	private int endBit;

	/**
	 * Constructs a new {@code ConfigFileDefinition} {@code Object}.
	 * @param id The file id.
	 */
	public VarbitDefinition(int id) {
		this.id = id;
	}

	public VarbitDefinition(int varpId, int id, int startBit, int endBit)
	{
		this.varpId = varpId;
		this.id = id;
		this.startBit = startBit;
		this.endBit = endBit;
	}

	/**
	 * Initializes the bit flags.
	 */
	static {
		int flag = 2;
		for (int i = 0; i < 32; i++) {
			BITS[i] = flag - 1;
			flag += flag;
		}
	}

	/**
	 * Gets the config file definitions for the given file id.
	 * @param id The file id.
	 * @return The definition.
	 */
	public static VarbitDefinition forObjectID(int id) {
		return forId(id);
	}

	public static VarbitDefinition forNPCID(int id){
		return forId(id);
	}

	public static VarbitDefinition forItemID(int id){
		return forId(id);
	}

	public static VarbitDefinition forId(int id){
		VarbitDefinition def = MAPPING.get(id);
		if (def != null) {
			return def;
		}

		def = new VarbitDefinition(id);
		byte[] bs = Cache.getIndexes()[22].getFileData(id >>> 10, id & 0x3ff);
		if (bs != null) {
			ByteBuffer buffer = ByteBuffer.wrap(bs);
			int opcode = 0;
			while ((opcode = buffer.get() & 0xFF) != 0) {
				if (opcode == 1) {
					def.varpId = buffer.getShort() & 0xFFFF;
					def.startBit = buffer.get() & 0xFF;
					def.endBit = buffer.get() & 0xFF;
				}
			}
		}
		MAPPING.put(id, def);
		return def;
	}

	public static void create(int varpId, int varbitId, int startBit, int endBit){
		VarbitDefinition def = new VarbitDefinition(
			varpId,
			varbitId,
			startBit,
			endBit
		);
		MAPPING.put(varbitId, def);
	}

	/**
	 * Gets the current config value for this file.
	 * @param player The player.
	 * @return The config value.
	 */
	public int getValue(Player player) {
		int size = BITS[endBit - startBit];
		int bitValue = player.varpManager.get(getVarpId()).getBitRangeValue(getStartBit(), getStartBit() + (endBit - startBit));
		if(bitValue != 0){
			return size & (bitValue >>> startBit);
		}
		return size & (player.getConfigManager().get(varpId) >>> startBit);
	}

	/**
	 * Gets the mapping.
	 * @return The mapping.
	 */
	public static Map<Integer, VarbitDefinition> getMapping() {
		return MAPPING;
	}

	/**
	 * Gets the id.
	 * @return The id.
	 */
	public int getId() {
		return id;
	}

	public int getVarpId() {
		return varpId;
	}

	public int getStartBit() {
		return startBit;
	}

	public int getEndBit() {
		return endBit;
	}

	@Override
	public String toString() {
		return "ConfigFileDefinition [id=" + id + ", configId=" + varpId + ", bitShift=" + startBit + ", bitSize=" + endBit + "]";
	}
}