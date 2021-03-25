package org.runite.client;

import org.rs09.client.Node;

public final class ConfigInventoryDefinition extends Node {

    static int[] anIntArray1835 = new int[100];
    static int[] anIntArray3082 = new int[100];
    static RSString[] aClass94Array3226 = new RSString[100];
    int size = 0;
    static short[][] aShortArrayArray3654 = new short[][]{{(short) 6798, (short) 107, (short) 10283, (short) 16, (short) 4797, (short) 7744, (short) 5799, (short) 4634, (short) -31839, (short) 22433, (short) 2983, (short) -11343, (short) 8, (short) 5281, (short) 10438, (short) 3650, (short) -27322, (short) -21845, (short) 200, (short) 571, (short) 908, (short) 21830, (short) 28946, (short) -15701, (short) -14010}, {(short) 8741, (short) 12, (short) -1506, (short) -22374, (short) 7735, (short) 8404, (short) 1701, (short) -27106, (short) 24094, (short) 10153, (short) -8915, (short) 4783, (short) 1341, (short) 16578, (short) -30533, (short) 25239, (short) 8, (short) 5281, (short) 10438, (short) 3650, (short) -27322, (short) -21845, (short) 200, (short) 571, (short) 908, (short) 21830, (short) 28946, (short) -15701, (short) -14010}, {(short) 25238, (short) 8742, (short) 12, (short) -1506, (short) -22374, (short) 7735, (short) 8404, (short) 1701, (short) -27106, (short) 24094, (short) 10153, (short) -8915, (short) 4783, (short) 1341, (short) 16578, (short) -30533, (short) 8, (short) 5281, (short) 10438, (short) 3650, (short) -27322, (short) -21845, (short) 200, (short) 571, (short) 908, (short) 21830, (short) 28946, (short) -15701, (short) -14010}, {(short) 4626, (short) 11146, (short) 6439, (short) 12, (short) 4758, (short) 10270}, {(short) 4550, (short) 4537, (short) 5681, (short) 5673, (short) 5790, (short) 6806, (short) 8076, (short) 4574}};
    static int anInt3655 = -1;

    public static ConfigInventoryDefinition retrieveConfigurationInventoryFile(int var0) {
        ConfigInventoryDefinition var2 = (ConfigInventoryDefinition) Class49.aClass47_818.get(var0);
        if (null == var2) {
            byte[] var3 = Class8.configurationReferenceCache.getFile(5, var0);
            var2 = new ConfigInventoryDefinition();
            if (var3 != null) {
                var2.decode(new DataBuffer(var3));
            }

            Class49.aClass47_818.put(var0, var2);
        }
        return var2;
    }

    public final void decode(DataBuffer buffer) {
        while (true) {
            int opcode = buffer.readUnsignedByte();
            if (0 == opcode) {
                return;
            }

            this.decode(buffer, opcode);
        }
    }

    private void decode(DataBuffer buffer, int opcode) {
        if (opcode == 2) {
            this.size = buffer.readUnsignedShort();
        }
    }

}
