package org.crandor.tools;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import org.crandor.cache.ServerStore;
import org.crandor.cache.StoreFile;

public class XTEAKeyParser {

	private static final int[] DEFAULT_XTEAS = new int[] { 14881828, -6662814, 58238456, 146761213 };

	private static final Map<Integer, int[]> xteas = new HashMap<>();

	public boolean parse() throws Throwable {
		StoreFile file = ServerStore.get("xteas");
		if (file == null) {
			return true;
		}
		ByteBuffer buffer = file.data();
		int opcode;

		while ((opcode = buffer.get() & 0xFF) != 0) {

			switch (opcode) {
			case 1:
				int regionId = -1;
				while ((regionId = buffer.getInt()) != 0) {
					xteas.put(regionId, new int[] { buffer.getInt(), buffer.getInt(), buffer.getInt(), buffer.getInt() });

				}
				break;
			}

		}
		return true;
	}

}
