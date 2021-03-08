package org.runite.client;

import org.rs09.client.config.GameConfig;
import org.rs09.client.filestore.ReferenceTable;
import org.rs09.client.filestore.ResourceProvider;
import org.rs09.client.filestore.compression.Container;
import org.rs09.client.util.ArrayUtils;
import org.rs09.client.util.CRC;

import java.util.Objects;

public final class CacheIndex {

    /**
     * These Indexes listed below are mostly used for CRC checks
     */
    public static CacheIndex fontsIndex;
    static CacheIndex skeletonsIndex;
    static CacheIndex skinsIndex;
    static CacheIndex configurationsIndex;
    static CacheIndex interfacesIndex;
    static CacheIndex soundFXIndex;
    static CacheIndex landscapesIndex;
    static CacheIndex musicIndex;
    static CacheIndex modelsIndex;
    public static CacheIndex spritesIndex;
    static CacheIndex texturesIndex;
    static CacheIndex huffmanEncodingIndex;
    static CacheIndex music2Index;
    static CacheIndex interfaceScriptsIndex;
    static CacheIndex soundFX2Index;
    static CacheIndex soundFX3Index;
    static CacheIndex objectConfigIndex;
    static CacheIndex clientscriptMaskIndex;
    static CacheIndex npcConfigIndex;
    static CacheIndex itemConfigIndex;
    static CacheIndex animationIndex;
    static CacheIndex graphicFXIndex;
    static CacheIndex clientScriptConfigIndex;
    static CacheIndex worldmapIndex;
    static CacheIndex quickchatMessagesIndex;
    static CacheIndex quickchatMenusIndex;
    static CacheIndex materialsIndex;
    static CacheIndex particlesConfigIndex;
    static CacheIndex libIndex;
    /**
     */


    private boolean discardPacked;
    private final boolean aBoolean1946;
    private final ResourceProvider provider;
    private ReferenceTable referenceTable = null;
    private Object[] packed;
    private Object[][] unpacked;

    private static Object wrap(byte[] data) {
        if (data == null) {
            return null;
        }

        if (data.length > 136 && !GameConfig.CACHE_DEBUG) {
            try {
                Class144 buffer = new Class144_Sub1();
                buffer.setBytes(data);
                return buffer;
            } catch (Throwable var4) {
                GameConfig.CACHE_DEBUG = true;
            }
        }

        return data;
    }

    final boolean method2113() {
        try {
            if (this.isReady()) {
                boolean var2 = true;

                for (int var3 = 0; this.referenceTable.validArchiveIds.length > var3; ++var3) {
                    int var4 = this.referenceTable.validArchiveIds[var3];
                    if (null == this.packed[var4]) {
                        this.load(var4);
                        if (null == this.packed[var4]) {
                            var2 = false;
                        }
                    }
                }

                return var2;
            } else {
                return false;
            }
        } catch (RuntimeException var5) {
            throw ClientErrorException.clientError(var5, "ve.IA()");
        }
    }

    private int method2114(int var1) {
        if (this.isValidArchive(var1)) {
            return this.packed[var1] != null ? 100 : this.provider.percentComplete(var1);
        } else {
            return 0;
        }
    }

    final void method2115(int var1, boolean var2) {
        try {
            if (this.isReady()) {
                if (var2) {
                    this.referenceTable.archiveNameHash = null;
                    this.referenceTable.setALookupTable_949(null);
                }

                this.referenceTable.setALookupTableArray962(null);
                this.referenceTable.setFileNameHashes(null);

            }
        } catch (RuntimeException var5) {
            throw ClientErrorException.clientError(var5, "ve.R(" + var1 + ',' + var2 + ',' + true + ')');
        }
    }

    final int method2116(RSString var2) {
        try {
            if (this.isReady()) {
                var2 = var2.toLowercase();
                int var3 = this.referenceTable.getALookupTable_949().get(var2.method1574());
                return this.method2114(var3);
            } else {
                return 0;
            }
        } catch (RuntimeException var4) {
            throw ClientErrorException.clientError(var4, "ve.P(" + 22813 + ',' + (var2 != null ? "{...}" : "null") + ')');
        }
    }

    final boolean method2117(int var2) {
        if (this.isValidArchive(var2)) {
            if (null == this.packed[var2]) {
                this.load(var2);
                return null != this.packed[var2];
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    final int getReferenceTableCrc() {
        if (this.isReady()) {
            return this.referenceTable.getCrc();
        } else {
            throw new IllegalStateException("");
        }
    }

    final int getArchiveForName(RSString name) {
        try {
            if (this.isReady()) {
                name = name.toLowercase();
                int var3 = this.referenceTable.getALookupTable_949().get(name.method1574());
                return this.isValidArchive(var3) ? var3 : -1;
            } else {
                return -1;
            }
        } catch (RuntimeException var4) {
            throw ClientErrorException.clientError(var4, "ve.EA(" + (name != null ? "{...}" : "null") + ',' + (byte) -30 + ')');
        }
    }

    final int method2121() {
        try {
            if (this.isReady()) {

                return this.referenceTable.archiveLengths.length;
            } else {
                return -1;
            }
        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "ve.D(" + 0 + ')');
        }
    }

    private boolean isReady() {
        if (this.referenceTable == null) {
            this.referenceTable = this.provider.getReferenceTable();
            if (this.referenceTable == null) {
                return false;
            }

            this.unpacked = new Object[this.referenceTable.getArchiveAmount()][];
            this.packed = new Object[this.referenceTable.getArchiveAmount()];
        }

        return true;
    }

    final byte[] method2123(RSString var2, RSString var3) {
        try {
            if (this.isReady()) {
                var3 = var3.toLowercase();
                var2 = var2.toLowercase();
                int var4 = this.referenceTable.getALookupTable_949().get(var3.method1574());

                if (this.isValidArchive(var4)) {
                    int var5 = this.referenceTable.getALookupTableArray962()[var4].get(var2.method1574());
                    return this.getFile(var4, var5);
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } catch (RuntimeException var6) {
            throw ClientErrorException.clientError(var6, "ve.C(" + 0 + ',' + (var2 != null ? "{...}" : "null") + ',' + (var3 != null ? "{...}" : "null") + ')');
        }
    }

    final void method2124(int var1, RSString var2) {
        try {
            if (this.isReady()) {
                var2 = var2.toLowercase();
                int var3 = this.referenceTable.getALookupTable_949().get(var2.method1574());
                this.request(var3);
            }
        } catch (RuntimeException var5) {
            throw ClientErrorException.clientError(var5, "ve.V(" + var1 + ',' + (var2 != null ? "{...}" : "null") + ')');
        }
    }

    final boolean method2125(RSString var1, RSString var3) {
        try {
            if (this.isReady()) {
                var3 = var3.toLowercase();
                var1 = var1.toLowercase();
                int var4 = this.referenceTable.getALookupTable_949().get(var3.method1574());
                if (this.isValidArchive(var4)) {
                    int var5 = this.referenceTable.getALookupTableArray962()[var4].get(var1.method1574());

                    return this.method2129((byte) 70, var5, var4);
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (RuntimeException var6) {
            throw ClientErrorException.clientError(var6, "ve.DA(" + (var1 != null ? "{...}" : "null") + ',' + (byte) 116 + ',' + (var3 != null ? "{...}" : "null") + ')');
        }
    }

    final byte[] getFile(int archive, int file, int[] xtea) {
        if (!this.isValidFile(archive, file)) {
            return null;
        }

        if (this.unpacked[archive] == null || this.unpacked[archive][file] == null) {
            boolean loaded = this.unpack(archive, xtea);

            if (!loaded) {
                this.load(archive);
                loaded = this.unpack(archive, xtea);
                if (!loaded) {
                    return null;
                }
            }
        }

        byte[] var7 = NPC.method1985(this.unpacked[archive][file], false);
        if (this.aBoolean1946) {
            this.unpacked[archive][file] = null;
            if (this.referenceTable.archiveLengths[archive] == 1) {
                this.unpacked[archive] = null;
            }
        }

        return var7;
    }

    final boolean method2127(RSString var2) {
        try {
            if (this.isReady()) {
                var2 = var2.toLowercase();
                int var3 = this.referenceTable.getALookupTable_949().get(var2.method1574());
                return this.method2117(var3);
            } else {
                return false;
            }
        } catch (RuntimeException var4) {
            throw ClientErrorException.clientError(var4, "ve.O(" + (byte) -83 + ',' + (var2 != null ? "{...}" : "null") + ')');
        }
    }

    final void method2128(int var2) {
        try {
            if (this.isValidArchive(var2)) {
                if (null != this.unpacked) {
                    this.unpacked[var2] = null;
                }

            }
        } catch (RuntimeException var4) {
            throw ClientErrorException.clientError(var4, "ve.B(" + 7561 + ',' + var2 + ')');
        }
    }

    final boolean method2129(byte var1, int var2, int var3) {
        try {
            if (this.isValidFile(var3, var2)) {
                if (this.unpacked[var3] != null && null != this.unpacked[var3][var2]) {
                    return true;
                } else if (this.packed[var3] == null) {
                    this.load(var3);
                    return this.packed[var3] != null;
                } else {
                    return true;
                }
            } else {
                return false;
            }
        } catch (RuntimeException var5) {
            throw ClientErrorException.clientError(var5, "ve.FA(" + var1 + ',' + var2 + ',' + var3 + ')');
        }
    }

    private boolean isValidArchive(int archiveId) {
        if (!this.isReady()) {
            return false;
        }

        if (archiveId >= 0
                && this.referenceTable.archiveLengths.length > archiveId
                && this.referenceTable.archiveLengths[archiveId] != 0)
            return true;

        if (GameConfig.CACHE_DEBUG)
            throw new IllegalArgumentException(Integer.toString(archiveId));
        return false;
    }

    private void request(int archive) {
        this.provider.request(archive);
    }

    private boolean unpack(int archive, int[] xtea) {
        if (!isValidArchive(archive)) return false;
        if (packed[archive] == null) return false;

        int count = this.referenceTable.archiveFileLengths[archive];
        int[] indices = this.referenceTable.validFileIds[archive];
        if (this.unpacked[archive] == null) {
            this.unpacked[archive] = new Object[this.referenceTable.archiveLengths[archive]];
        }

        boolean finished = true;
        Object[] objects = this.unpacked[archive];

        for (int i = 0; i < count; ++i) {
            int index;
            if (indices == null) {
                index = i;
            } else {
                index = indices[i];
            }

            if (objects[index] == null) {
                finished = false;
                break;
            }
        }

        if (finished) return true;

        byte[] decrypted;
        if (xtea != null && (xtea[0] != 0 || xtea[1] != 0 || xtea[2] != 0 || xtea[3] != 0)) {
            decrypted = NPC.method1985(this.packed[archive], true);
            DataBuffer buffer = new DataBuffer(decrypted);
            buffer.method770(xtea, buffer.buffer.length);
        } else {
            decrypted = NPC.method1985(this.packed[archive], false);
        }

        byte[] data = new byte[0];
        try {
            data = Container.INSTANCE.decode(decrypted);//Class3_Sub28_Sub13.decodeContainer(var21);
        } catch (Throwable e) {
            System.out.println("Tried to go into a null area");
            System.out.println("T3 - " + (xtea != null) + "," + archive + "," + Objects.requireNonNull(decrypted).length + "," + CRC.INSTANCE.crc32(decrypted, decrypted.length) + "," + CRC.INSTANCE.crc32(decrypted, decrypted.length - 2) + "," + this.referenceTable.archiveCRCs[archive] + "," + this.referenceTable.getCrc());
            //throw ClientErrorException.clientError(e, "T3 - " + (xtea != null) + "," + archive + "," + Objects.requireNonNull(decrypted).length + "," + CRC.INSTANCE.crc32(decrypted, decrypted.length) + "," + CRC.INSTANCE.crc32(decrypted, decrypted.length - 2) + "," + this.referenceTable.archiveCRCs[archive] + "," + this.referenceTable.getCrc());
        }

        if (this.discardPacked) {
            this.packed[archive] = null;
        }

        int start;
        if (count > 1) {
            start = data.length;
            start--;
            int blockCount = data[start] & 0xff;
            start -= count * blockCount * 4;

            DataBuffer buffer = new DataBuffer(data);
            buffer.index = start;
            int[] var13 = new int[count];

            int var15;
            int var16;
            for (int var14 = 0; blockCount > var14; ++var14) {
                var15 = 0;

                for (var16 = 0; var16 < count; ++var16) {
                    var15 += buffer.readInt();

                    var13[var16] += var15;
                }
            }

            byte[][] var24 = new byte[count][];

            for (var15 = 0; count > var15; ++var15) {
                var24[var15] = new byte[var13[var15]];
                var13[var15] = 0;
            }

            buffer.index = start;
            var15 = 0;

            int var17;
            for (var16 = 0; blockCount > var16; ++var16) {
                var17 = 0;

                for (int var18 = 0; var18 < count; ++var18) {
                    var17 += buffer.readInt();
                    ArrayUtils.arraycopy(data, var15, var24[var18], var13[var18], var17);
                    var15 += var17;
                    var13[var18] += var17;
                }
            }

            for (var16 = 0; var16 < count; ++var16) {
                if (indices == null) {
                    var17 = var16;
                } else {
                    var17 = indices[var16];
                }

                if (this.aBoolean1946) {
                    objects[var17] = var24[var16];
                } else {
                    objects[var17] = wrap(var24[var16]);
                }
            }
        } else {
            if (null == indices) {
                start = 0;
            } else {
                start = indices[0];
            }

            if (this.aBoolean1946) {
                objects[start] = data;
            } else {
                objects[start] = wrap(data);
            }
        }
        return true;
    }

    public final byte[] getFile(int archive, int file) {
        return this.getFile(archive, file, null);
    }

    private void load(int archive) {
        if (this.discardPacked) {
            this.packed[archive] = this.provider.get(archive);
        } else {
            this.packed[archive] = wrap(this.provider.get(archive));
        }
    }

    final boolean method2135(RSString var1) {
        try {
            if (this.isReady()) {
                var1 = var1.toLowercase();
                int var3 = this.referenceTable.getALookupTable_949().get(var1.method1574());
                return var3 >= 0;
            } else {
                return false;
            }
        } catch (RuntimeException var4) {
            throw ClientErrorException.clientError(var4, "ve.S(" + (var1 != null ? "{...}" : "null") + ')');
        }
    }

    final int method2136(byte var1) {
        try {
            if (var1 > -121) {
                Unsorted.anInt1950 = -3;
            }

            if (this.isReady()) {
                int var2 = 0;
                int var3 = 0;

                int var4;
                for (var4 = 0; var4 < this.packed.length; ++var4) {
                    if (0 < this.referenceTable.archiveFileLengths[var4]) {
                        var2 += 100;
                        var3 += this.method2114(var4);
                    }
                }

                if (var2 == 0) {
                    return 100;
                } else {
                    var4 = var3 * 100 / var2;
                    return var4;
                }
            } else {
                return 0;
            }
        } catch (RuntimeException var5) {
            throw ClientErrorException.clientError(var5, "ve.N(" + var1 + ')');
        }
    }

    final void method2137() {
        try {
            if (this.unpacked != null) {
                for (int var2 = 0; this.unpacked.length > var2; ++var2) {
                    this.unpacked[var2] = null;
                }
            }

        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "ve.Q(" + ')');
        }
    }

    final byte[] method2138(int var1) {
        try {
            if (!this.isReady()) {
                return null;
            } else if (this.referenceTable.archiveLengths.length == 1) {
                return this.getFile(0, var1);
            } else if (!this.isValidArchive(var1)) {
                return null;
            } else if (this.referenceTable.archiveLengths[var1] == 1) {
                return this.getFile(var1, 0);
            } else {
                throw new RuntimeException();
            }
        } catch (RuntimeException var4) {
            throw ClientErrorException.clientError(var4, "ve.HA(" + var1 + ',' + 0 + ')');
        }
    }

    private boolean isValidFile(int archive, int file) {
        if (!this.isReady()) {
            return false;
        }
        if (0 <= archive
                && file >= 0 && archive < this.referenceTable.archiveLengths.length
                && this.referenceTable.archiveLengths[archive] > file)
            return true;
        if (GameConfig.CACHE_DEBUG)
            throw new IllegalArgumentException(archive + "," + file);
        return false;
    }

    final byte[] method2140(int file, int archive) {
        if (this.isValidFile(archive, file)) {
            if (this.unpacked[archive] == null || null == this.unpacked[archive][file]) {
                boolean var4 = this.unpack(archive, (int[]) null);
                if (!var4) {
                    this.load(archive);
                    var4 = this.unpack(archive, (int[]) null);
                    if (!var4) {
                        return null;
                    }
                }
            }

            return NPC.method1985(this.unpacked[archive][file], false);
        } else {
            return null;
        }
    }

    final int[] getFileIds(int archiveId) {
        try {

            if (this.isValidArchive(archiveId)) {
                int[] var3 = this.referenceTable.validFileIds[archiveId];
                if (null == var3) {
                    var3 = new int[this.referenceTable.archiveFileLengths[archiveId]];

                    for (int var4 = 0; var3.length > var4; var3[var4] = var4++) {
                    }
                }

                return var3;
            } else {
                return null;
            }
        } catch (RuntimeException var5) {
            throw ClientErrorException.clientError(var5, "ve.G(" + (byte) -128 + ',' + archiveId + ')');
        }
    }

    public CacheIndex(ResourceProvider provider, boolean var2, boolean var3) {
        this.provider = provider;
        this.discardPacked = var2;
        this.aBoolean1946 = var3;
    }

    final int getFileAmount(int archiveId) {
        try {
            if (this.isValidArchive(archiveId)) {
                return this.referenceTable.archiveLengths[archiveId];
            } else {
                return 0;
            }
        } catch (RuntimeException var4) {
            throw ClientErrorException.clientError(var4, "ve.H(" + archiveId + ')');
        }
    }

    public final boolean retrieveSpriteFile(int archiveId) {
        try {
            if (!this.isReady()) {
                return false;
            } else if (this.referenceTable.archiveLengths.length == 1) {
                return this.method2129((byte) 86, archiveId, 0);
            } else if (this.isValidArchive(archiveId)) {
                if (1 == this.referenceTable.archiveLengths[archiveId]) {
                    return this.method2129((byte) 109, 0, archiveId);
                } else {
                    throw new RuntimeException();
                }
            } else {
                return false;
            }
        } catch (RuntimeException var4) {
            throw ClientErrorException.clientError(var4, "ve.A(" + 0 + ',' + archiveId + ')');
        }
    }

}
