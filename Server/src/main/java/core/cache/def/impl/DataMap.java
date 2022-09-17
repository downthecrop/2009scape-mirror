package core.cache.def.impl;

import core.cache.Cache;
import core.cache.CacheFileManager;
import core.cache.misc.Container;
import core.cache.misc.buffer.ByteBufferUtils;
import core.tools.CP1252;
import rs09.game.system.SystemLogger;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class DataMap {

    /**
     * The config definitions mapping.
     */
    private static final Map<Integer, core.cache.def.impl.DataMap> definitions = new HashMap<>();

    /**
     * The enum id.
     */
    private final int id;

    public char keyType = '?';

    public char valueType = '?';

    public String defaultString;

    public int defaultInt;

    public HashMap<Integer, Object> dataStore = new HashMap<>();

    public DataMap(int id) {
        this.id = id;
    }

    public int getInt(int key){
        if(!dataStore.containsKey(key)){
            SystemLogger.logErr(this.getClass(), "Invalid value passed for key: " + key + " map: " + id);
            return -1;
        }
        return (int) dataStore.get(key);
    }

    public String getString(int key){
        return (String) dataStore.get(key);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        return "DataMapDefinition{" +
                "id=" + id +
                ", keyType=" + keyType +
                ", valueType=" + (valueType == 'K' ? "Normal" : valueType == 'J' ? "Struct Pointer" : "Unknown") +
                ", defaultString='" + defaultString + '\'' +
                ", defaultInt=" + defaultInt +
                ", dataStore=" + dataStore +
                '}' + "\n";
    }

    public static DataMap get(int id){
        core.cache.def.impl.DataMap def = definitions.get(id);
        if (def != null) {
            return def;
        }
        byte[] data = Cache.getIndexes()[17].getFileData(id >>> 8, id & 0xFF);
        def = parse(id, data);
        definitions.put(id, def);
        return def;
    }

    public static DataMap parse(int id, byte[] data)
    {
        DataMap def = new DataMap(id);
        if (data != null) {
            ByteBuffer buffer = ByteBuffer.wrap(data);
            int opcode;

            while ((opcode = buffer.get() & 0xFF) != 0) {

                if (opcode == 1) {
                    def.keyType = CP1252.getFromByte(buffer.get());
                } else if (opcode == 2) {
                    def.valueType = CP1252.getFromByte(buffer.get());
                } else if (opcode == 3) {
                    def.defaultString = ByteBufferUtils.getString(buffer);
                } else if (opcode == 4) {
                    def.defaultInt = buffer.getInt();
                } else if (opcode == 5 || opcode == 6) {
                    int size = buffer.getShort() & 0xFFFF;

                    for (int i = 0; i < size; i++) {
                        int key = buffer.getInt();

                        Object value;
                        if (opcode == 5) {
                            value = ByteBufferUtils.getString(buffer);
                        } else {
                            value = buffer.getInt();
                        }
                        def.dataStore.put(key, value);
                    }
                }
            }
        }
        return def;
    }

    public int getId() {
        return id;
    }
}
