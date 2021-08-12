package org.runite.client;

import org.rs09.CustomVars;
import org.rs09.SystemLogger;
import org.rs09.client.data.ReferenceCache;

import java.util.Objects;

public class VarpHelpers {

    static ReferenceCache varbitLookup = new ReferenceCache(64);

    static void setVarbit(byte var0, int valueToSet, int varbitID) {
        try {
            if (var0 >= -99) {
                setVarbit((byte) 57, -14, 120);
            }

            CSConfigCachefile cacheFile = CSConfigCachefile.getCSConfigFileFromVarbitID(varbitID);
            int parentVarp = Objects.requireNonNull(cacheFile).parentVarpIndex;
            int upperBound = cacheFile.upperBound;
            int lowerBound = cacheFile.lowerBound;
            int varbitSize = upperBound - lowerBound;
            SystemLogger.logInfo(parentVarp + " - bitStart: " + lowerBound + " bitEnd: " + upperBound + " bitSize = " + (varbitSize + 1));
            int expectedMinimumValue = Class3_Sub6.expectedMinimumValues[varbitSize];
            SystemLogger.logInfo("emv: " + expectedMinimumValue + " || vs: " + (127 + valueToSet));
            if (valueToSet < 0|| expectedMinimumValue < valueToSet) {
                SystemLogger.logInfo(expectedMinimumValue + " < " + valueToSet);
                valueToSet = 0;
            }

            expectedMinimumValue <<= lowerBound;
            int modifiedVarpValue = valueToSet << lowerBound & expectedMinimumValue | ~expectedMinimumValue & Class57.varpArray[parentVarp];
            setVarp(modifiedVarpValue, parentVarp);
        } catch (RuntimeException var8) {
            throw ClientErrorException.clientError(var8, "wd.K(" + var0 + ',' + valueToSet + ',' + varbitID + ')');
        }
    }

    static void setVarp(int valueToSet, int varpIndex) {
       try {
          Class57.varpArray[varpIndex] = valueToSet;
          if(CustomVars.parse(varpIndex,valueToSet)){
             return;
          }
          Class3_Sub7 var3 = (Class3_Sub7) AtmosphereParser.aHashTable_3679.get(varpIndex);
          if(null == var3) {
             var3 = new Class3_Sub7(4611686018427387905L);
             AtmosphereParser.aHashTable_3679.put(varpIndex, var3);
          } else if (var3.aLong2295 != 4611686018427387905L) {
             var3.aLong2295 = TimeUtils.time() + 500L | 4611686018427387904L;
          }

       } catch (RuntimeException var4) {
          throw ClientErrorException.clientError(var4, "nh.W(" + (byte) 99 + ',' + valueToSet + ',' + varpIndex + ')');
       }
    }
}
