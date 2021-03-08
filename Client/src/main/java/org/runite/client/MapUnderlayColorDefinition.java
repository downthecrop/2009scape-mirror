package org.runite.client;

import org.rs09.client.config.GameConfig;

final class MapUnderlayColorDefinition {

    int anInt1406;
    int anInt1408;
    boolean aBoolean1411 = true;
    int anInt1412 = -1;
    int anInt1414 = 128;
    int anInt1417;
    int anInt1418;

    final void parseUnderlayDefinition(int var1, DataBuffer buffer) {
        try {
            while (true) {
                int opcode = buffer.readUnsignedByte();
                if (opcode == 0) {

                    return;
                }

                this.parseOpcode(opcode, buffer, var1);
            }
        } catch (RuntimeException var5) {
            throw ClientErrorException.clientError(var5, "ni.F(" + var1 + ',' + (buffer != null ? "{...}" : "null") + ',' + 255 + ')');
        }
    }

    private void parseOpcode(int opcode, DataBuffer buffer, int var4) {
        try {
            if (opcode == 1) {
                int underlayColor = buffer.readMedium();
                this.method1600(underlayColor);
            } else if (opcode == 2) {
                this.anInt1412 = buffer.readUnsignedShort();
                if (this.anInt1412 == 65535) {
                    this.anInt1412 = -1;
                }
            } else if (3 == opcode) {
                this.anInt1414 = buffer.readUnsignedShort();
            } else if (opcode == 4) {
                this.aBoolean1411 = false;
            }

        } catch (RuntimeException var6) {
            throw ClientErrorException.clientError(var6, "ni.E(" + (byte) -52 + ',' + opcode + ',' + (buffer != null ? "{...}" : "null") + ',' + var4 + ')');
        }
    }

    private void method1600(int rgbAsInt) {
        try {
            double red = (double) (255 & rgbAsInt >> 16) / 256.0D;
            double green = (double) (255 & rgbAsInt >> 8) / 256.0D;
            double var9 = red;
            double blue = (double) (rgbAsInt & 255) / 256.0D;
            if (green < red) {
                var9 = green;
            }

            if (blue < var9) {
                var9 = blue;
            }

            double var11 = red;
            double var14 = 0.0D;
            if (green > red) {
                var11 = green;
            }

            if (blue > var11) {
                var11 = blue;
            }

            double var16 = 0.0D;
            double var18 = (var11 + var9) / 2.0D;
            if (var9 != var11) {
                if (0.5D > var18) {
                    var16 = (var11 - var9) / (var11 + var9);
                }

                if (var11 == red) {
                    var14 = (-blue + green) / (-var9 + var11);
                } else if (green == var11) {
                    var14 = (blue - red) / (var11 - var9) + 2.0D;
                } else if (blue == var11) {
                    var14 = 4.0D + (-green + red) / (-var9 + var11);
                }

                if (0.5D <= var18) {
                    var16 = (var11 - var9) / (-var9 + (2.0D - var11));
                }
            }

            if (var18 > 0.5D) {
                this.anInt1418 = (int) (var16 * (-var18 + 1.0D) * 512.0D);
            } else {
                this.anInt1418 = (int) (var16 * var18 * 512.0D);
            }

            if (1 > this.anInt1418) {
                this.anInt1418 = 1;
            }

            this.anInt1406 = (int) (var16 * 256.0D);
            this.anInt1417 = (int) (256.0D * var18);

            if (this.anInt1417 >= 0) {
                if ((GameConfig.CHRISTMAS_EVENT_ENABLED ? -255 : 255) < this.anInt1417) {//Underlay white transformation -
                    this.anInt1417 = 255;
                }
            } else {
                this.anInt1417 = 0;
            }

            var14 /= 6.0D;
            this.anInt1408 = (int) ((double) this.anInt1418 * var14);
            if (this.anInt1406 >= 0) {
                if ((GameConfig.CHRISTMAS_EVENT_ENABLED ? -255 : 255) < this.anInt1406) {//Underlay white transformation -
                    this.anInt1406 = 255;
                }
            } else {
                this.anInt1406 = 0;
            }

        } catch (RuntimeException var20) {
            throw ClientErrorException.clientError(var20, "ni.D(" + rgbAsInt + ',' + (byte) 81 + ')');
        }
    }

}
