package org.runite.client;

import org.rs09.client.Node;
import org.rs09.client.data.NodeCache;

public final class ConfigInventoryDefinition extends Node {

    static NodeCache aClass47_818 = new NodeCache(64);
    int size = 0;
    static int anInt3655 = -1;

    public static ConfigInventoryDefinition retrieveConfigurationInventoryFile(int var0) {
        ConfigInventoryDefinition var2 = (ConfigInventoryDefinition) aClass47_818.get(var0);
        if (null == var2) {
            byte[] var3 = Class8.configurationReferenceCache.getFile(5, var0);
            var2 = new ConfigInventoryDefinition();
            if (var3 != null) {
                var2.decode(new DataBuffer(var3));
            }

            aClass47_818.put(var0, var2);
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
