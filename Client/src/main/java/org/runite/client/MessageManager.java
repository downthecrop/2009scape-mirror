package org.runite.client;

public final class MessageManager {

    static RSString[] aStringArray3226 = new RSString[100];
    static int[] anIntArray1835 = new int[100];
    static int[] anIntArray3082 = new int[100];

    static void sendGameMessage(int var0, int type, RSString message, RSString var3, RSString var5) {
        for (int i = 99; i > 0; --i) {
            anIntArray3082[i] = anIntArray3082[i - 1];
            aStringArray3226[i] = aStringArray3226[i - 1];
            LinkableRSString.aStringArray2580[i] = LinkableRSString.aStringArray2580[-1 + i];
            Class163_Sub3.aStringArray3003[i] = Class163_Sub3.aStringArray3003[i + -1];
            anIntArray1835[i] = anIntArray1835[i - 1];
        }

        ++TextureOperation16.anInt3114;
        anIntArray3082[0] = type;
        aStringArray3226[0] = var5;
        Class24.anInt472 = PacketParser.anInt3213;
        anIntArray1835[0] = var0;
        RSString primaryMsg = RSString.parse("null");
        RSString secondaryMsg = RSString.parse("null");
        int cutOff = 81 - (var3 != null ? var3.length : 0) - (var5 != null ? var5.length : 0);
        if(message.length > cutOff && type != 0){
            String[] tokens = message.toString().split(" ");
            if(tokens.length > 1) {
                int counter = 0;
                for (String tok : tokens) {
                    if (counter + tok.length() > cutOff) {
                        break;
                    }
                    counter += tok.length() + 1;
                }
                primaryMsg = message.substring(0, counter, 0);
                secondaryMsg = message.substring(counter, message.length, 0);
                message = primaryMsg;
            }
        }
        LinkableRSString.aStringArray2580[0] = message;
        Class163_Sub3.aStringArray3003[0] = var3;
        if(!secondaryMsg.equalsString(RSString.parse("null")))
            sendGameMessage(var0,type,secondaryMsg,var3,var5);
    }
}
