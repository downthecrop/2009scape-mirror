package org.runite.client;

import org.rs09.client.Linkable;
import org.rs09.client.data.ReferenceCache;

final class Class3_Sub31 extends Linkable {

   int anInt2602;
   int anInt2603;
   static ReferenceCache aReferenceCache_2604 = new ReferenceCache(64);
   static int[] anIntArray2606;
   static int paramCountryID;

   static RSString[] optionsArrayStringConstructor(RSString[] var1) {
      try {
         RSString[] var2 = new RSString[5];

          for(int var3 = 0; var3 < 5; ++var3) {
            var2[var3] = RSString.stringCombiner(new RSString[]{
                    RSString.stringAnimator(var3), TextCore.aClass94_3577});
            if(var1 != null && null != var1[var3]) {
               var2[var3] = RSString.stringCombiner(new RSString[]{var2[var3], var1[var3]});
            }
         }

         return var2;
      } catch (RuntimeException var4) {
         throw ClientErrorException.clientError(var4, "wk.A(" + 19406 + ',' + (var1 != null?"{...}":"null") + ')');
      }
   }

}
