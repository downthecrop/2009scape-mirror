package core.cache.def.impl;

import core.cache.Cache;
import core.cache.CacheFileManager;
import core.cache.misc.Container;
import core.cache.misc.buffer.ByteBufferUtils;
import rs09.game.system.SystemLogger;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class Struct {

    /**
     * The struct definitions mapping.
     */
    private static final Map<Integer, Struct> definitions = new HashMap<>();

    /**
     * The enum id.
     */
    private final int id;

    public HashMap<Integer, Object> dataStore = new HashMap<>();

    public Struct(int id) {
        this.id = id;
    }

    public int getInt(int key){
        if(!dataStore.containsKey(key)){
            SystemLogger.logErr(this.getClass(), "Invalid value passed for key: " + key + " struct: " + id);
            return -1;
        }
        return (int) dataStore.get(key);
    }

    public String getString(int key){
        return (String) dataStore.get(key);
    }

    @Override
    public String toString() {
        return "Struct{" +
                "id=" + id +
                ", dataStore=" + dataStore +
                '}';
    }

    public static Struct get(int id){
        Struct def = definitions.get(id);
        if (def != null) {
            return def;
        }
        byte[] data = Cache.getIndexes()[2].getFileData(26, id);
        def = parse(id, data);

        definitions.put(id, def);
        return def;
    }

    public static Struct parse(int id, byte[] data)
    {
        Struct def = new Struct(id);
        if (data != null) {
            ByteBuffer buffer = ByteBuffer.wrap(data);
            int opcode;

            while ((opcode = buffer.get() & 0xFF) != 0) {

                if (opcode == 249) {
                    int size = buffer.get() & 0xFF;

                    for (int i = 0; size > i; i++) {
                        boolean bool = (buffer.get() & 0xFF) == 1;
                        int key = ByteBufferUtils.getMedium(buffer);
                        Object value;
                        if (bool) {
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

    public static int getCount() {
        return Cache.getIndexes()[2].getFilesSize(26);
    }

    public int getId() {
        return id;
    }
}
