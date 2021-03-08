package org.runite.client;

import org.rs09.client.Node;

public final class Class3_Sub28_Sub12 extends Node {

    static int[] anIntArray1835 = new int[100];
    static int[] anIntArray3082 = new int[100];
    static RSString[] aClass94Array3226 = new RSString[100];
    int size = 0;
    static int anInt3652;
    static short[][] aShortArrayArray3654 = new short[][]{{(short) 6798, (short) 107, (short) 10283, (short) 16, (short) 4797, (short) 7744, (short) 5799, (short) 4634, (short) -31839, (short) 22433, (short) 2983, (short) -11343, (short) 8, (short) 5281, (short) 10438, (short) 3650, (short) -27322, (short) -21845, (short) 200, (short) 571, (short) 908, (short) 21830, (short) 28946, (short) -15701, (short) -14010}, {(short) 8741, (short) 12, (short) -1506, (short) -22374, (short) 7735, (short) 8404, (short) 1701, (short) -27106, (short) 24094, (short) 10153, (short) -8915, (short) 4783, (short) 1341, (short) 16578, (short) -30533, (short) 25239, (short) 8, (short) 5281, (short) 10438, (short) 3650, (short) -27322, (short) -21845, (short) 200, (short) 571, (short) 908, (short) 21830, (short) 28946, (short) -15701, (short) -14010}, {(short) 25238, (short) 8742, (short) 12, (short) -1506, (short) -22374, (short) 7735, (short) 8404, (short) 1701, (short) -27106, (short) 24094, (short) 10153, (short) -8915, (short) 4783, (short) 1341, (short) 16578, (short) -30533, (short) 8, (short) 5281, (short) 10438, (short) 3650, (short) -27322, (short) -21845, (short) 200, (short) 571, (short) 908, (short) 21830, (short) 28946, (short) -15701, (short) -14010}, {(short) 4626, (short) 11146, (short) 6439, (short) 12, (short) 4758, (short) 10270}, {(short) 4550, (short) 4537, (short) 5681, (short) 5673, (short) 5790, (short) 6806, (short) 8076, (short) 4574}};
    static int anInt3655 = -1;

    static boolean method609(RSInterface var0, int var1) {
        if (null == var0.anIntArray275) {
            return false;
        } else {
            int var2 = 0;
            if (var1 <= 20) {
                TextCore.COMMAND_MEMORY_MANAGEMENT = (RSString) null;
            }

            for (; var0.anIntArray275.length > var2; ++var2) {
                int var3 = Class164_Sub2.method2247((byte) 119, var2, var0);
                int var4 = var0.anIntArray307[var2];
                if (var0.anIntArray275[var2] != 2) {
                    if (var0.anIntArray275[var2] != 3) {
                        if (4 == var0.anIntArray275[var2]) {
                            if (var4 == var3) {
                                return false;
                            }
                        } else if (var3 != var4) {
                            return false;
                        }
                    } else if (var3 <= var4) {
                        return false;
                    }
                } else if (var3 >= var4) {
                    return false;
                }
            }

            return true;
        }
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

    static void sendGameMessage(int var0, int type, RSString message, RSString var3, RSString var5) {
        for (int i = 99; i > 0; --i) {
            anIntArray3082[i] = anIntArray3082[i - 1];
            aClass94Array3226[i] = aClass94Array3226[i - 1];
            LinkableRSString.aClass94Array2580[i] = LinkableRSString.aClass94Array2580[-1 + i];
            Class163_Sub3.aClass94Array3003[i] = Class163_Sub3.aClass94Array3003[i + -1];
            anIntArray1835[i] = anIntArray1835[i - 1];
        }

        ++Class3_Sub13_Sub9.anInt3114;
        anIntArray3082[0] = type;
        aClass94Array3226[0] = var5;
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
        LinkableRSString.aClass94Array2580[0] = message;
        Class163_Sub3.aClass94Array3003[0] = var3;
        if(!secondaryMsg.equalsString(RSString.parse("null")))
            sendGameMessage(var0,type,secondaryMsg,var3,var5);
    }

    static RSString method612(long var0) {
        return Class3_Sub13_Sub8.method207(10, false, 116, var0);
    }

    private void decode(DataBuffer buffer, int opcode) {
        if (opcode == 2) {
            this.size = buffer.readUnsignedShort();
        }
    }

}
