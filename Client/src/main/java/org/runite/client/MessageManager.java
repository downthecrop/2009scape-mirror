package org.runite.client;

public final class MessageManager {

    static void sendGameMessage(int var0, int type, RSString message, RSString var3, RSString var5) {
        for (int i = 99; i > 0; --i) {
            ConfigInventoryDefinition.anIntArray3082[i] = ConfigInventoryDefinition.anIntArray3082[i - 1];
            ConfigInventoryDefinition.aClass94Array3226[i] = ConfigInventoryDefinition.aClass94Array3226[i - 1];
            LinkableRSString.aClass94Array2580[i] = LinkableRSString.aClass94Array2580[-1 + i];
            Class163_Sub3.aClass94Array3003[i] = Class163_Sub3.aClass94Array3003[i + -1];
            ConfigInventoryDefinition.anIntArray1835[i] = ConfigInventoryDefinition.anIntArray1835[i - 1];
        }

        ++TextureOperation16.anInt3114;
        ConfigInventoryDefinition.anIntArray3082[0] = type;
        ConfigInventoryDefinition.aClass94Array3226[0] = var5;
        Class24.anInt472 = PacketParser.anInt3213;
        ConfigInventoryDefinition.anIntArray1835[0] = var0;
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
        LinkableRSString.aClass94Array2580[0] = message;
        Class163_Sub3.aClass94Array3003[0] = var3;
        if(!secondaryMsg.equalsString(RSString.parse("null")))
            sendGameMessage(var0,type,secondaryMsg,var3,var5);
    }
}
