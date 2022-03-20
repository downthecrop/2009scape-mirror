package org.runite.client;

final class Class133 {

    static int[] anIntArray1743 = new int[25];
    static int anInt1748;
    static int anInt1753;
    static int anInt1754;
    static int[] inputTextCodeArray = new int[128];
    int anInt1746;
    int anInt1747;
    int anInt1750;
    int anInt1752;
    int anInt1757;
    private byte aByte1742;


    public Class133() {
    }

    Class133(DataBuffer var1) {
        try {
            this.aByte1742 = var1.readSignedByte();
            this.anInt1752 = var1.readUnsignedShort();
            this.anInt1757 = var1.readInt();
            this.anInt1747 = var1.readInt();
            this.anInt1746 = var1.readInt();
            this.anInt1750 = var1.readInt();
        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "sg.<init>(" + (var1 != null ? "{...}" : "null") + ')');
        }
    }

    static void method1803() {
        try {
            Texture.aReferenceCache_1146.clearSoftReferences();
            Class159.aReferenceCache_2016.clearSoftReferences();
        } catch (RuntimeException var2) {
            throw ClientErrorException.clientError(var2, "sg.D(" + (byte) 22 + ')');
        }
    }

    final int method1804() {
        try {

            return this.aByte1742 & 7;
        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "sg.B(" + false + ')');
        }
    }

    final int method1805() {
        try {
            return 8 != (this.aByte1742 & 8) ? 0 : 1;
        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "sg.C(" + (byte) -33 + ')');
        }
    }

}
