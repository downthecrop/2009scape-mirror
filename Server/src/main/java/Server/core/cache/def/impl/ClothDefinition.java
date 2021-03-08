package core.cache.def.impl;

import java.nio.ByteBuffer;
import java.util.Arrays;

import core.ServerConstants;
import core.cache.Cache;

/**
 * The definitions for player clothing/look.
 * @author Emperor
 */
public final class ClothDefinition {

	/**
	 * The equipment slot.
	 */
	private int equipmentSlot;

	/**
	 * The model ids.
	 */
	private int[] modelIds;

	/**
	 * Unknown boolean.
	 */
	private boolean unknownBool;

	/**
	 * Original colors.
	 */
	private int[] originalColors;

	/**
	 * The colors to change to.
	 */
	private int[] modifiedColors;

	/**
	 * Original texture colors.
	 */
	private int[] originalTextureColors;

	/**
	 * Texture colors to change to.
	 */
	private int[] modifiedTextureColors;

	/**
	 * Other model ids(?)
	 */
	private int[] models = { -1, -1, -1, -1, -1 };

	/**
	 * Gets the definitions for the given cloth id.
	 * @param clothId The clothing id.
	 * @return The definition.
	 */
	public static ClothDefinition forId(int clothId) {
		ClothDefinition def = new ClothDefinition();
		byte[] bs = Cache.getIndexes()[2].getFileData(3, clothId);
		if (bs != null) {
			def.load(ByteBuffer.wrap(bs));
		}
		return def;
	}

	/**
	 * The main method.
	 * @param args The arguments cast on runtime.
	 */
	public static void main(String... args) {
		try {
			Cache.init(ServerConstants.CACHE_PATH);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		int length = Cache.getIndexes()[2].getFilesSize(3);
		System.out.println("Definition size: " + length + ".");
		for (int i = 0; i < length; i++) {
			ClothDefinition def = forId(i);
			if (def.unknownBool)
				System.out.println("Clothing " + i + ": " + def.equipmentSlot + ", " + def.unknownBool + ", " + Arrays.toString(def.modelIds) + ", " + Arrays.toString(def.models));
		}
	}

	/**
	 * Loads the definitions.
	 * @param buffer The buffer.
	 */
	public void load(ByteBuffer buffer) {
		int opcode;
		while ((opcode = buffer.get() & 0xFF) != 0) {
			parse(opcode, buffer);
		}
	}

	/**
	 * Parses an opcode.
	 * @param opcode The opcode.
	 * @param buffer The buffer to read the data from.
	 */
	private void parse(int opcode, ByteBuffer buffer) {
		switch (opcode) {
		case 1:
			equipmentSlot = buffer.get() & 0xFF;
			break;
		case 2:
			int length = buffer.get() & 0xFF;
			modelIds = new int[length];
			for (int i = 0; i < length; i++) {
				modelIds[i] = buffer.getShort() & 0xFFFF;
			}
			break;
		case 3:
			unknownBool = true;
			break;
		case 40:
			length = buffer.get() & 0xFF;
			originalColors = new int[length];
			modifiedColors = new int[length];
			for (int i = 0; i < length; i++) {
				originalColors[i] = buffer.getShort();
				modifiedColors[i] = buffer.getShort();
			}
			break;
		case 41:
			length = buffer.get() & 0xFF;
			originalTextureColors = new int[length];
			modifiedTextureColors = new int[length];
			for (int i = 0; i < length; i++) {
				originalTextureColors[i] = buffer.getShort();
				modifiedTextureColors[i] = buffer.getShort();
			}
			break;
		default:
			if (opcode >= 60 && opcode < 70) {
				models[opcode - 60] = buffer.getShort() & 0xFFFF;
			}
			break;
		}
	}

	/**
	 * Gets the unknown.
	 * @return The unknown.
	 */
	public int getUnknown() {
		return equipmentSlot;
	}

	/**
	 * Gets the modelIds.
	 * @return The modelIds.
	 */
	public int[] getModelIds() {
		return modelIds;
	}

	/**
	 * Gets the unknownBool.
	 * @return The unknownBool.
	 */
	public boolean isUnknownBool() {
		return unknownBool;
	}

	/**
	 * Gets the originalColors.
	 * @return The originalColors.
	 */
	public int[] getOriginalColors() {
		return originalColors;
	}

	/**
	 * Gets the modifiedColors.
	 * @return The modifiedColors.
	 */
	public int[] getModifiedColors() {
		return modifiedColors;
	}

	/**
	 * Gets the originalTextureColors.
	 * @return The originalTextureColors.
	 */
	public int[] getOriginalTextureColors() {
		return originalTextureColors;
	}

	/**
	 * Gets the modifiedTextureColors.
	 * @return The modifiedTextureColors.
	 */
	public int[] getModifiedTextureColors() {
		return modifiedTextureColors;
	}

	/**
	 * Gets the models.
	 * @return The models.
	 */
	public int[] getModels() {
		return models;
	}
}