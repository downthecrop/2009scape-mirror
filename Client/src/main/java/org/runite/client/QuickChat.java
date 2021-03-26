package org.runite.client;

import org.rs09.client.data.NodeCache;

final class QuickChat {

    static CacheIndex aClass153_1967;
    static CacheIndex aClass153_3490;
    static NodeCache aClass47_3137 = new NodeCache(64);
    static NodeCache aClass47_3776 = new NodeCache(64);
    static int anInt377 = 0;
    static int anInt1156 = 0;

    int anInt149;
    QuickChatDefinition aQuickChatDefinition_151;
    int[] anIntArray153;

    static void method205(CacheIndex quickchatMenusIndex, CacheIndex quickchatMessagesIndex, Interface4 var3) {
        try {
            aClass153_1967 = quickchatMenusIndex;
            Class58.anInterface4_915 = var3;
            aClass153_3490 = quickchatMessagesIndex;
            if (aClass153_3490 != null) {
                anInt1156 = aClass153_3490.getFileAmount(1);
            }

            if (aClass153_1967 != null) {
                anInt377 = aClass153_1967.getFileAmount(1);
            }
        } catch (RuntimeException var5) {
            throw ClientErrorException.clientError(var5, "ej.E(" + (quickchatMenusIndex != null ? "{...}" : "null") + ',' + 115 + ',' + (quickchatMessagesIndex != null ? "{...}" : "null") + ',' + (var3 != null ? "{...}" : "null") + ')');
        }
    }

    static QuickChatDefinition method733(int var1) {
        try {
            QuickChatDefinition var2 = (QuickChatDefinition) aClass47_3776.get(var1);
            if (null == var2) {
                byte[] var3;
                if (var1 < 32768) {
                    var3 = aClass153_3490.getFile(1, var1);
                } else {
                    var3 = aClass153_1967.getFile(1, 32767 & var1);
                }

                var2 = new QuickChatDefinition();

                if (var3 != null) {
                    var2.decode(new DataBuffer(var3));
                }

                if (var1 >= 32768) {
                    var2.method548();
                }

                aClass47_3776.put(var1, var2);
            }
            return var2;
        } catch (RuntimeException var4) {
            throw ClientErrorException.clientError(var4, "sj.Q(" + 12345678 + ',' + var1 + ')');
        }
    }

    static QuickChat method2156(DataBuffer var1) {
        try {
            QuickChat var2 = new QuickChat();
            var2.anInt149 = var1.readUnsignedShort();

            var2.aQuickChatDefinition_151 = method733(var2.anInt149);
            return var2;
        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "vh.M(" + 1024 + ',' + (var1 != null ? "{...}" : "null") + ')');
        }
    }

    static Class3_Sub28_Sub1 getQuickChatMessage(int fileId) {
        try {
            Class3_Sub28_Sub1 var2 = (Class3_Sub28_Sub1) aClass47_3137.get(fileId);
            if (null == var2) {
                byte[] var3;
                if (fileId < 32768) {
                    var3 = Unsorted.quickChatMessages.getFile(0, fileId);
                } else {
                    var3 = Unsorted.aClass153_332.getFile(0, fileId & 32767);
                }

                var2 = new Class3_Sub28_Sub1();
                if (null != var3) {
                    var2.method530(new DataBuffer(var3));
                }

                if (fileId >= 32768) {
                    var2.method525();
                }

                aClass47_3137.put(fileId, var2);
            }
            return var2;
        } catch (RuntimeException var4) {
            throw ClientErrorException.clientError(var4, "tb.B(" + fileId + ',' + (byte) -54 + ')');
        }
    }

    static void method1236(CacheIndex var0, CacheIndex var1) {
        try {
            Unsorted.quickChatMessages = var1;
            Unsorted.aClass153_332 = var0;
        } catch (RuntimeException var4) {
            throw ClientErrorException.clientError(var4, "ja.F(" + (var0 != null ? "{...}" : "null") + ',' + (var1 != null ? "{...}" : "null") + ',' + -117 + ')');
        }
    }
}
