package org.runite.client;

import java.awt.*;
import java.awt.image.*;
import java.util.Hashtable;

public final class Class158_Sub2 extends Class158 {

    private Component aComponent2983;


    public final void method2179(Graphics var3) {
        var3.drawImage(this.anImage2009, 0, 0, this.aComponent2983);
    }

    final void method2185(int var1, int var3, Component var4) {
        this.anIntArray2007 = new int[var3 * var1 + 1];
        this.anInt2011 = var1;
        this.anInt2012 = var3;
        DataBufferInt var5 = new DataBufferInt(this.anIntArray2007, this.anIntArray2007.length);
        DirectColorModel var6 = new DirectColorModel(32, 16711680, 65280, 255);
        WritableRaster var7 = Raster.createWritableRaster(var6.createCompatibleSampleModel(this.anInt2012, this.anInt2011), var5, null);
        this.anImage2009 = new BufferedImage(var6, var7, false, new Hashtable<>());
        this.aComponent2983 = var4;
        this.method2182();
    }

    final void drawGraphics(int width, int x, int height, Graphics var5, int y) {
        Shape var7 = var5.getClip();
        var5.clipRect(x, y, width, height);
        var5.drawImage(this.anImage2009, 0, 0, this.aComponent2983);
        var5.setClip(var7);
    }
}
