package org.runite.client;

import org.rs09.client.data.NodeCache;

public class QuickChat {

    static NodeCache aClass47_3137 = new NodeCache(64);

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
}
