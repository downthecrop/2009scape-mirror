package org.runite.client;

class Class38 {

    static int anInt660;
    static boolean aBoolean661 = true;
    static int[][] anIntArrayArray663;
    static int[] anIntArray664 = new int[14];
    static Signlink gameSignlink;
    static Class146 aClass146_668;

    static void method1029() {
        try {
            TextureOperation12.outgoingBuffer.putOpcode(177);
            TextureOperation12.outgoingBuffer.writeShort(Class113.interfacePacketCounter);
        } catch (RuntimeException var2) {
            throw ClientErrorException.clientError(var2, "fk.D(" + 0 + ')');
        }
    }

}
