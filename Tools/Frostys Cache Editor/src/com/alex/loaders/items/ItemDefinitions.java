package com.alex.loaders.items;

import com.alex.io.InputStream;
import com.alex.io.OutputStream;
import com.alex.store.Store;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

public class ItemDefinitions implements Cloneable {
   public int id;
   public boolean loaded;
   public int modelId;
   public String name;
   public int modelZoom;
   public int modelRotation1;
   public int modelRotation2;
   public int modelOffset1;
   public int modelOffset2;
   public int opcode1;
   public int opcode2;
   public int opcode4;
   public int opcode5;
   public int opcode6;
   public int opcode7;
   public int opcode8;
   public int opcode11;
   public int opcode12;
   public int opcode14;
   public int opcode16;
   public int opcode22;
   public int opcode23;
   public int opcode24;
   public int opcode25;
   public int opcode26;
   public int opcode29;
   public int opcode30;
   public int opcode31;
   public int opcode32;
   public int opcode33;
   public int opcode34;
   public int opcode35;
   public int opcode36;
   public int opcode37;
   public int opcode38;
   public int opcode39;
   public int opcode40;
   public int opcode41;
   public int opcode42;
   public int opcode65;
   public int opcode78;
   public int opcode79;
   public int opcode90;
   public int opcode91;
   public int opcode92;
   public int opcode93;
   public int opcode95;
   public int opcode96;
   public int opcode97;
   public int opcode98;
   public int opcode100;
   public int opcode101;
   public int opcode102;
   public int opcode103;
   public int opcode104;
   public int opcode105;
   public int opcode106;
   public int opcode107;
   public int opcode108;
   public int opcode109;
   public int opcode110;
   public int opcode111;
   public int opcode112;
   public int opcode113;
   public int opcode114;
   public int opcode115;
   public int opcode121;
   public int opcode122;
   public int opcode125;
   public int opcode126;
   public int opcode127;
   public int opcode128;
   public int opcode129;
   public int opcode130;
   public int opcode132;
   public int opcode139;
   public int opcode140;
   public int opcode191;
   public int opcode249;
   public int equipSlot;
   public int equipType;
   public int stackable;
   public int value;
   public boolean membersOnly;
   public int maleEquip1;
   public int femaleEquip1;
   public int maleEquip2;
   public int femaleEquip2;
   public int maleEquipModelId3;
   public int femaleEquipModelId3;
   public String[] groundOptions;
   public String[] inventoryOptions;
   public int[] originalModelColors;
   public int[] modifiedModelColors;
   public short[] originalTextureColors;
   public short[] modifiedTextureColors;
   public byte[] unknownArray1;
   public int[] unknownArray2;
   public int[] unknownArray4;
   public int[] unknownArray5;
   public byte[] unknownArray6;
   public byte[] unknownArray3;
   public boolean unnoted;
   public int unknownInt1;
   public int unknownInt2;
   public int unknownInt3;
   public int unknownInt4;
   public int unknownInt5;
   public int unknownInt6;
   public int switchNoteItemId;
   public int notedItemId;
   public int[] stackIds;
   public int[] stackAmounts;
   public int unknownInt7;
   public int unknownInt8;
   public int unknownInt9;
   public int unknownInt10;
   public int unknownInt11;
   public int teamId;
   public int switchLendItemId;
   public int lendedItemId;
   public int unknownInt12;
   public int unknownInt13;
   public int unknownInt14;
   public int unknownInt15;
   public int unknownInt16;
   public int unknownInt17;
   public int unknownInt18;
   public int unknownInt19;
   public int unknownInt20;
   public int unknownInt21;
   public int unknownInt22;
   public int unknownInt23;
   public int unknownValue1;
   public int unknownValue2;
	private int opcode218;
	private int opcode219;
   public HashMap clientScriptData;

   public static ItemDefinitions getItemDefinition(Store cache, int itemId) {
      return getItemDefinition(cache, itemId, true);
   }

   public static ItemDefinitions getItemDefinition(Store cache, int itemId, boolean load) {
      return new ItemDefinitions(cache, itemId, load);
   }

   public ItemDefinitions(Store cache, int id) {
      this(cache, id, true);
   }

   public ItemDefinitions(Store cache, int id, boolean load) {
      this.id = id;
      this.setDefaultsVariableValules();
      this.setDefaultOptions();
      if(load) {
         this.loadItemDefinition(cache);
      }

   }

   public boolean isLoaded() {
      return this.loaded;
   }

   public int getValue() {
      return this.value;
   }

   public int getTeamId() {
      return this.teamId;
   }

   public int getStackable() {
      return this.stackable;
   }

   public boolean isUnnoted() {
      return this.unnoted;
   }

   public int getLendedItemId() {
      return this.lendedItemId;
   }

   public int getModelRotation1() {
      return this.modelRotation1;
   }

   public void setModelRotation1(int modelRotation1) {
      this.modelRotation1 = modelRotation1;
   }

   public int getModelRotation2() {
      return this.modelRotation2;
   }

   public void setModelRotation2(int modelRotation2) {
      this.modelRotation2 = modelRotation2;
   }

   public int getModelOffset1() {
      return this.modelOffset1;
   }

   public void setModelOffset1(int modelOffset1) {
      this.modelOffset1 = modelOffset1;
   }

   public int getModelOffset2() {
      return this.modelOffset2;
   }

   public void setModelOffset2(int modelOffset2) {
      this.modelOffset2 = modelOffset2;
   }

   public int getMaleEquipModelId1() {
      return this.maleEquip1;
   }

   public void setMaleEquipModelId1(int maleEquipModelId1) {
      this.maleEquip1 = maleEquipModelId1;
   }

   public int getFemaleEquipModelId1() {
      return this.femaleEquip1;
   }

   public void setFemaleEquipModelId1(int femaleEquipModelId1) {
      this.femaleEquip1 = femaleEquipModelId1;
   }

   public int getMaleEquipModelId2() {
      return this.maleEquip2;
   }

   public void setMaleEquipModelId2(int maleEquipModelId2) {
      this.maleEquip2 = maleEquipModelId2;
   }

   public int getFemaleEquipModelId2() {
      return this.femaleEquip2;
   }

   public void setFemaleEquipModelId2(int femaleEquipModelId2) {
      this.femaleEquip2 = femaleEquipModelId2;
   }

   public int getMaleEquipModelId3() {
      return this.maleEquipModelId3;
   }

   public void setMaleEquipModelId3(int maleEquipModelId3) {
      this.maleEquipModelId3 = maleEquipModelId3;
   }

   public int getFemaleEquipModelId3() {
      return this.femaleEquipModelId3;
   }

   public void setFemaleEquipModelId3(int femaleEquipModelId3) {
      this.femaleEquipModelId3 = femaleEquipModelId3;
   }

   public int[] getOriginalModelColors() {
      return this.originalModelColors;
   }

   public void setOriginalModelColors(int[] originalModelColors) {
      this.originalModelColors = originalModelColors;
   }

   public int[] getModifiedModelColors() {
      return this.modifiedModelColors;
   }

   public void setModifiedModelColors(int[] modifiedModelColors) {
      this.modifiedModelColors = modifiedModelColors;
   }

   public int[] getStackAmounts() {
      return this.stackAmounts;
   }

   public void setStackAmounts(int[] stackAmounts) {
      this.stackAmounts = stackAmounts;
   }

   public int[] getStackIds() {
      return this.stackIds;
   }

   public void setStackIds(int[] stackIds) {
      this.stackIds = stackIds;
   }

   public void setStackable(int stackable) {
      this.stackable = stackable;
   }

   public void setValue(int value) {
      this.value = value;
   }

   public void setTeamId(int teamId) {
      this.teamId = teamId;
   }

   public void setMembersOnly(boolean membersOnly) {
      this.membersOnly = membersOnly;
   }

   public void setUnnoted(boolean unnoted) {
      this.unnoted = unnoted;
   }

   public void setEquipSlot(int equipSlot) {
      this.equipSlot = equipSlot;
   }

   public void setEquipType(int equipType) {
      this.equipType = equipType;
   }

   public int getSwitchLendItemId() {
      return this.switchLendItemId;
   }

   public void setSwitchLendItemId(int switchLendItemId) {
      this.switchLendItemId = switchLendItemId;
   }

   public void setLendedItemId(int lendedItemId) {
      this.lendedItemId = lendedItemId;
   }

   public int getId() {
      return this.id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public void write(Store store) {
      store.getIndexes()[19].putFile(this.getArchiveId(), this.getFileId(), this.encode());
   }

   private void loadItemDefinition(Store cache) {
      byte[] data = cache.getIndexes()[19].getFile(this.getArchiveId(), this.getFileId());
      if(data != null) {
         try {
            this.readOpcodeValues(new InputStream(data));
         } catch (RuntimeException var4) {
            var4.printStackTrace();
         }

         if(this.notedItemId != -1) {
            this.toNote(cache);
         }

         if(this.lendedItemId != -1) {
            this.toLend(cache);
         }

         this.loaded = true;
      }
   }

   private void toNote(Store store) {
      ItemDefinitions realItem = getItemDefinition(store, this.switchNoteItemId);
      this.membersOnly = realItem.membersOnly;
      this.value = realItem.value;
      this.name = realItem.name;
      this.stackable = 1;
   }

   private void toLend(Store store) {
      ItemDefinitions realItem = getItemDefinition(store, this.switchLendItemId);
      this.originalModelColors = realItem.originalModelColors;
      this.modifiedModelColors = realItem.modifiedModelColors;
      this.teamId = realItem.teamId;
      this.value = 0;
      this.membersOnly = realItem.membersOnly;
      this.name = realItem.name;
      this.inventoryOptions = new String[5];
      this.groundOptions = realItem.groundOptions;
      if(realItem.inventoryOptions != null) {
         for(int optionIndex = 0; optionIndex < 4; ++optionIndex) {
            this.inventoryOptions[optionIndex] = realItem.inventoryOptions[optionIndex];
         }
      }

      this.inventoryOptions[4] = "Discard";
      this.maleEquip1 = realItem.maleEquip1;
      this.maleEquip2 = realItem.maleEquip2;
      this.femaleEquip1 = realItem.femaleEquip1;
      this.femaleEquip2 = realItem.femaleEquip2;
      this.maleEquipModelId3 = realItem.maleEquipModelId3;
      this.femaleEquipModelId3 = realItem.femaleEquipModelId3;
      this.equipType = realItem.equipType;
      this.equipSlot = realItem.equipSlot;
   }

   public int getArchiveId() {
      return this.id >>> 8;
   }

   public int getFileId() {
      return 0xff & this.id;
   }

   public boolean hasSpecialBar() {
      if(this.clientScriptData == null) {
         return false;
      } else {
         Object specialBar = this.clientScriptData.get(Integer.valueOf(686));
         return specialBar != null && specialBar instanceof Integer && ((Integer)specialBar).intValue() == 1;
      }
   }

   public int getRenderAnimId() {
      if(this.clientScriptData == null) {
         return 1426;
      } else {
         Object animId = this.clientScriptData.get(Integer.valueOf(644));
         return animId != null && animId instanceof Integer?((Integer)animId).intValue():1426;
      }
   }

   public int getQuestId() {
      if(this.clientScriptData == null) {
         return -1;
      } else {
         System.out.println(this.clientScriptData.toString());
         Object questId = this.clientScriptData.get(Integer.valueOf(861));
         return questId != null && questId instanceof Integer?((Integer)questId).intValue():-1;
      }
   }

   public HashMap getWearingSkillRequiriments() {
      if(this.clientScriptData == null) {
         return null;
      } else {
         HashMap skills = new HashMap();
         int nextLevel = -1;
         int nextSkill = -1;
         Iterator var5 = this.clientScriptData.keySet().iterator();

         while(var5.hasNext()) {
            int key = ((Integer)var5.next()).intValue();
            Object value = this.clientScriptData.get(Integer.valueOf(key));
            if(!(value instanceof String)) {
               if(key == 23) {
                  skills.put(Integer.valueOf(4), (Integer)value);
                  skills.put(Integer.valueOf(11), Integer.valueOf(61));
               } else if(key >= 749 && key < 797) {
                  if(key % 2 == 0) {
                     nextLevel = ((Integer)value).intValue();
                  } else {
                     nextSkill = ((Integer)value).intValue();
                  }

                  if(nextLevel != -1 && nextSkill != -1) {
                     skills.put(Integer.valueOf(nextSkill), Integer.valueOf(nextLevel));
                     nextLevel = -1;
                     nextSkill = -1;
                  }
               }
            }
         }

         return skills;
      }
   }

   public void printClientScriptData() {
      Iterator key2 = this.clientScriptData.keySet().iterator();

      while(key2.hasNext()) {
         int requiriments = ((Integer)key2.next()).intValue();
         Object value = this.clientScriptData.get(Integer.valueOf(requiriments));
         System.out.println("KEY: " + requiriments + ", VALUE: " + value);
      }

      HashMap requiriments1 = this.getWearingSkillRequiriments();
      if(requiriments1 == null) {
         System.out.println("null.");
      } else {
         System.out.println(requiriments1.keySet().size());
         Iterator value1 = requiriments1.keySet().iterator();

         while(value1.hasNext()) {
            int key21 = ((Integer)value1.next()).intValue();
            Object value2 = requiriments1.get(Integer.valueOf(key21));
            System.out.println("SKILL: " + key21 + ", LEVEL: " + value2);
         }

      }
   }

   private void setDefaultOptions() {
      this.groundOptions = new String[]{null, null, "take", null, null};
      this.inventoryOptions = new String[]{null, null, null, null, "drop"};
   }

   private void setDefaultsVariableValules() {
      this.name = "null";
      this.maleEquip1 = -1;
      this.maleEquip2 = -1;
      this.femaleEquip1 = -1;
      this.femaleEquip2 = -1;
      this.modelZoom = 2000;
      this.switchLendItemId = -1;
      this.lendedItemId = -1;
      this.switchNoteItemId = -1;
      this.notedItemId = -1;
      this.unknownInt9 = 128;
      this.value = 1;
      this.maleEquipModelId3 = -1;
      this.femaleEquipModelId3 = -1;
      this.teamId = -1;
      this.equipType = -1;
      this.equipSlot = -1;
   }

   public byte[] encode() {
      OutputStream stream = new OutputStream();
      stream.writeByte(1);
      stream.writeBigSmart(this.modelId);
      if(!this.name.equals("null") && this.notedItemId == -1) {
         stream.writeByte(2);
         stream.writeString(this.name);
      }

      if(this.modelZoom != 2000) {
         stream.writeByte(4);
         stream.writeShort(this.modelZoom);
      }

      if(this.modelRotation1 != 0) {
         stream.writeByte(5);
         stream.writeShort(this.modelRotation1);
      }

      if(this.modelRotation2 != 0) {
         stream.writeByte(6);
         stream.writeShort(this.modelRotation2);
      }

      int data;
      int value;
      if(this.modelOffset1 != 0) {
         stream.writeByte(7);
         data = this.modelOffset1 >> 0;
         this.modelOffset1 = data;
         value = data;
         if(data < 0) {
            value = data + 65536;
         }

         stream.writeShort(value);
      }

      if(this.modelOffset2 != 0) {
         stream.writeByte(8);
         data = this.modelOffset2 >> 0;
         this.modelOffset2 = data;
         value = data;
         if(data < 0) {
            value = data + 65536;
         }

         stream.writeShort(value);
      }

      if(this.stackable >= 1 && this.notedItemId == -1) {
         stream.writeByte(11);
      }

      if(this.value != 1 && this.lendedItemId == -1) {
         stream.writeByte(12);
         stream.writeInt(this.value);
      }

      if(this.equipSlot != -1) {
         stream.writeByte(13);
         stream.writeByte(this.equipSlot);
      }

      if(this.equipType != -1) {
         stream.writeByte(14);
         stream.writeByte(this.equipType);
      }

      if(this.membersOnly && this.notedItemId == -1) {
         stream.writeByte(16);
      }

      if(this.maleEquip1 != -1) {
         stream.writeByte(23);
         stream.writeBigSmart(this.maleEquip1);
      }

      if(this.maleEquip2 != -1) {
         stream.writeByte(24);
         stream.writeBigSmart(this.maleEquip2);
      }

      if(this.femaleEquip1 != -1) {
         stream.writeByte(25);
         stream.writeBigSmart(this.femaleEquip1);
      }

      if(this.femaleEquip2 != -1) {
         stream.writeByte(26);
         stream.writeBigSmart(this.femaleEquip2);
      }

      for(data = 0; data < 4; ++data) {
         if(this.groundOptions[data] != null && (data != 2 || !this.groundOptions[data].equals("take"))) {
            stream.writeByte(30 + data);
            stream.writeString(this.groundOptions[data]);
         }
      }

      for(data = 0; data < 4; ++data) {
         if(this.inventoryOptions[data] != null && (data != 4 || !this.inventoryOptions[data].equals("drop"))) {
            stream.writeByte(35 + data);
            stream.writeString(this.inventoryOptions[data]);
         }
      }

      if(this.originalModelColors != null && this.modifiedModelColors != null) {
         stream.writeByte(40);
         stream.writeByte(this.originalModelColors.length);

         for(data = 0; data < this.originalModelColors.length; ++data) {
            stream.writeShort(this.originalModelColors[data]);
            stream.writeShort(this.modifiedModelColors[data]);
         }
      }

      if(this.originalTextureColors != null && this.modifiedTextureColors != null) {
         stream.writeByte(41);
         stream.writeByte(this.originalTextureColors.length);

         for(data = 0; data < this.originalTextureColors.length; ++data) {
            stream.writeShort(this.originalTextureColors[data]);
            stream.writeShort(this.modifiedTextureColors[data]);
         }
      }

      if(this.unknownArray1 != null) {
         stream.writeByte(42);
         stream.writeByte(this.unknownArray1.length);

         for(data = 0; data < this.unknownArray1.length; ++data) {
            stream.writeByte(this.unknownArray1[data]);
         }
      }

      if(this.unnoted) {
         stream.writeByte(65);
      }

      if(this.maleEquipModelId3 != -1) {
         stream.writeByte(78);
         stream.writeBigSmart(this.maleEquipModelId3);
      }

      if(this.femaleEquipModelId3 != -1) {
         stream.writeByte(79);
         stream.writeBigSmart(this.femaleEquipModelId3);
      }

      if(this.switchNoteItemId != -1) {
         stream.writeByte(97);
         stream.writeShort(this.switchNoteItemId);
      }

      if(this.notedItemId != -1) {
         stream.writeByte(98);
         stream.writeShort(this.notedItemId);
      }

      if(this.stackIds != null && this.stackAmounts != null) {
         for(data = 0; data < this.stackIds.length; ++data) {
            if(this.stackIds[data] != 0 || this.stackAmounts[data] != 0) {
               stream.writeByte(100 + data);
               stream.writeShort(this.stackIds[data]);
               stream.writeShort(this.stackAmounts[data]);
            }
         }
      }

      if(this.teamId != 0) {
         stream.writeByte(115);
         stream.writeByte(this.teamId);
      }

      if(this.switchLendItemId != -1) {
         stream.writeByte(121);
         stream.writeShort(this.switchLendItemId);
      }

      if(this.lendedItemId != -1) {
         stream.writeByte(122);
         stream.writeShort(this.lendedItemId);
      }

      if(this.unknownArray2 != null) {
         stream.writeByte(132);
         stream.writeByte(this.unknownArray2.length);

         for(data = 0; data < this.unknownArray2.length; ++data) {
            stream.writeShort(this.unknownArray2[data]);
         }
      }

      if(this.clientScriptData != null) {
         stream.writeByte(249);
         stream.writeByte(this.clientScriptData.size());
         Iterator var5 = this.clientScriptData.keySet().iterator();

         while(var5.hasNext()) {
            data = ((Integer)var5.next()).intValue();
            Object value2 = this.clientScriptData.get(Integer.valueOf(data));
            stream.writeByte(value2 instanceof String?1:0);
            stream.write24BitInt(data);
            if(value2 instanceof String) {
               stream.writeString((String)value2);
            } else {
               stream.writeInt(((Integer)value2).intValue());
            }
         }
      }

      stream.writeByte(0);
      byte[] var6 = new byte[stream.getOffset()];
      stream.setOffset(0);
      stream.getBytes(var6, 0, var6.length);
      return var6;
   }

   public int getInvModelId() {
      return this.modelId;
   }

   public void setInvModelId(int modelId) {
      this.modelId = modelId;
   }

   public int getInvModelZoom() {
      return this.modelZoom;
   }

   public void setInvModelZoom(int modelZoom) {
      this.modelZoom = modelZoom;
   }

   private final void readValues(InputStream stream, int opcode) {
      if(opcode == 1) {
         this.modelId = stream.readUnsignedShort();//stream.readBigSmart();
      } else if(opcode == 2) {
         this.name = stream.readString();
      } else if(opcode == 4) {
         this.modelZoom = stream.readUnsignedShort();
      } else if(opcode == 5) {
         this.modelRotation1 = stream.readUnsignedShort();
      } else if(opcode == 6) {
         this.modelRotation2 = stream.readUnsignedShort();
      } else if(opcode == 7) {
         this.modelOffset1 = stream.readUnsignedShort();
         if(this.modelOffset1 > 52767) {
         //if(this.modelOffset1 > 32767) {
            this.modelOffset1 -= 65536;
         }

         this.modelOffset1 <<= 0;
      } else if(opcode == 8) {
         this.modelOffset2 = stream.readUnsignedShort();
         if(this.modelOffset2 > 52767) {
         //if(this.modelOffset2 > 32767) {
            this.modelOffset2 -= 65536;
         }

         this.modelOffset2 <<= 0;
      } else if(opcode == 11) {
         this.stackable = 1;
      } else if(opcode == 12) {
         this.value = stream.readInt();
      } else if(opcode == 13) {
         this.equipSlot = stream.readUnsignedByte();
      } else if(opcode == 14) {
         this.equipType = stream.readUnsignedByte();
      } else if(opcode == 16) {
         this.membersOnly = true;
      } else if(opcode == 18) {
         stream.readUnsignedShortLE();
      } else if(opcode == 23) {
         this.maleEquip1 = stream.readUnsignedShort();//stream.readBigSmart();
      } else if(opcode == 24) {
         this.maleEquip2 = stream.readUnsignedShort();//stream.readBigSmart();
      } else if(opcode == 25) {
         this.femaleEquip1 = stream.readUnsignedShort();//stream.readBigSmart();
      } else if(opcode == 26) {
         this.femaleEquip2 = stream.readUnsignedShort();//stream.readBigSmart();
      } else if(opcode == 27) {
         stream.readUnsignedByte();
      } else if(opcode >= 30 && opcode < 35) {
         this.groundOptions[opcode - 30] = stream.readString();
      } else if(opcode >= 35 && opcode < 40) {
         this.inventoryOptions[opcode - 35] = stream.readString();
      } else {
         int length;
         int index;
         if(opcode == 40) {
            length = stream.readUnsignedByte();
            this.originalModelColors = new int[length];
            this.modifiedModelColors = new int[length];

            for(index = 0; index < length; ++index) {
               this.originalModelColors[index] = stream.readUnsignedShort();
               this.modifiedModelColors[index] = stream.readUnsignedShort();
            }
         } else if(opcode == 41) {
            length = stream.readUnsignedByte();
            this.originalTextureColors = new short[length];
            this.modifiedTextureColors = new short[length];

            for(index = 0; index < length; ++index) {
               this.originalTextureColors[index] = (short)stream.readUnsignedShort();
               this.modifiedTextureColors[index] = (short)stream.readUnsignedShort();
            }
         } else if(opcode == 42) {
            length = stream.readUnsignedByte();
            this.unknownArray1 = new byte[length];

            for(index = 0; index < length; ++index) {
               this.unknownArray1[index] = (byte)stream.readByte();
            }
         } else if(opcode == 65) {
            this.unnoted = true;
         } else if(opcode == 78) {
            this.maleEquipModelId3 = stream.readUnsignedShort();//stream.readBigSmart();
         } else if(opcode == 79) {
            this.femaleEquipModelId3 = stream.readUnsignedShort();//stream.readBigSmart();
         } else if(opcode == 90) {
            this.unknownInt1 = stream.readUnsignedShort();//stream.readBigSmart();
         } else if(opcode == 91) {
            this.unknownInt2 = stream.readUnsignedShort();//stream.readBigSmart();
         } else if(opcode == 92) {
            this.unknownInt3 = stream.readUnsignedShort();//stream.readBigSmart();
         } else if(opcode == 93) {
            this.unknownInt4 = stream.readUnsignedShort();//stream.readBigSmart();
         } else if(opcode == 95) {
            this.unknownInt5 = stream.readUnsignedShort();
         } else if(opcode == 96) {
            this.unknownInt6 = stream.readUnsignedByte();
         } else if(opcode == 97) {
            this.switchNoteItemId = stream.readUnsignedShort();
         } else if(opcode == 98) {
            this.notedItemId = stream.readUnsignedShort();
         } else if(opcode >= 100 && opcode < 110) {
            if(this.stackIds == null) {
               this.stackIds = new int[10];
               this.stackAmounts = new int[10];
            }

            this.stackIds[opcode - 100] = stream.readUnsignedShort();
            this.stackAmounts[opcode - 100] = stream.readUnsignedShort();
         } else if(opcode == 110) {
            this.unknownInt7 = stream.readUnsignedShort();
         } else if(opcode == 111) {
            this.unknownInt8 = stream.readUnsignedShort();
         } else if(opcode == 112) {
            this.unknownInt9 = stream.readUnsignedShort();
         } else if(opcode == 113) {
            this.unknownInt10 = stream.readByte();
         } else if(opcode == 114) {
            this.unknownInt11 = stream.readByte() * 5;
         } else if(opcode == 115) {
            this.teamId = stream.readUnsignedByte();
         } else if(opcode == 121) {
            this.switchLendItemId = stream.readUnsignedShort();
         } else if(opcode == 122) {
            this.lendedItemId = stream.readUnsignedShort();
         } else if(opcode == 125) {
            this.unknownInt12 = stream.readByte() << 0;
            this.unknownInt13 = stream.readByte() << 0;
            this.unknownInt14 = stream.readByte() << 0;
         } else if(opcode == 126) {
            this.unknownInt15 = stream.readByte() << 0;
            this.unknownInt16 = stream.readByte() << 0;
            this.unknownInt17 = stream.readByte() << 0;
         } else if(opcode == 127) {
            this.unknownInt18 = stream.readUnsignedByte();
            this.unknownInt19 = stream.readUnsignedShort();
         } else if(opcode == 128) {
            this.unknownInt20 = stream.readUnsignedByte();
            this.unknownInt21 = stream.readUnsignedShort();
         } else if(opcode == 129) {
            this.unknownInt20 = stream.readUnsignedByte();
            this.unknownInt21 = stream.readUnsignedShort();
         } else if(opcode == 130) {
            this.unknownInt22 = stream.readUnsignedByte();
            this.unknownInt23 = stream.readUnsignedShort();
         } else if(opcode == 132) {
            length = stream.readUnsignedByte();
            this.unknownArray2 = new int[length];

            for(index = 0; index < length; ++index) {
               this.unknownArray2[index] = stream.readUnsignedShort();
            }
         } else if(opcode == 134) {
            stream.readUnsignedByte();
         } else if(opcode == 139) {
            this.unknownValue2 = stream.readUnsignedShort();
         } else if(opcode == 140) {
            this.unknownValue1 = stream.readUnsignedShort();
		}else if (opcode == 191) {
			return;
			//int opcode191 = 0;
		}else if (opcode == 218) {
			return;
			//int opcode218 = 0;
		}else if (opcode == 219) {
			return;
			//int opcode219 = 0;
         } else if(opcode == 249) {
            length = stream.readUnsignedByte();
            if(this.clientScriptData == null) {
               this.clientScriptData = new HashMap(length);
            }

            for(index = 0; index < length; ++index) {
               boolean stringInstance = stream.readUnsignedByte() == 1;
               int key = stream.read24BitInt();
               Object value = stringInstance?stream.readString():Integer.valueOf(stream.readInt());
               this.clientScriptData.put(Integer.valueOf(key), value);
            }
         //}
         } else {
               throw new RuntimeException("MISSING OPCODE " + opcode + " FOR ITEM " + this.id);
            }
      }

   }

   private void readOpcodeValues(InputStream stream) {
      while(true) {
         int opcode = stream.readUnsignedByte();
         if(opcode == 0) {
            return;
         }

         this.readValues(stream, opcode);
      }
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getName() {
      return this.name;
   }

   public void resetTextureColors() {
      this.originalTextureColors = null;
      this.modifiedTextureColors = null;
   }

   public boolean isWearItem() {
      return this.equipSlot != -1;
   }

   public boolean isMembersOnly() {
      return this.membersOnly;
   }

   public void changeTextureColor(short originalModelColor, short modifiedModelColor) {
      if(this.originalTextureColors != null) {
         for(int newOriginalModelColors = 0; newOriginalModelColors < this.originalTextureColors.length; ++newOriginalModelColors) {
            if(this.originalTextureColors[newOriginalModelColors] == originalModelColor) {
               this.modifiedTextureColors[newOriginalModelColors] = modifiedModelColor;
               return;
            }
         }

         short[] var5 = Arrays.copyOf(this.originalTextureColors, this.originalTextureColors.length + 1);
         short[] newModifiedModelColors = Arrays.copyOf(this.modifiedTextureColors, this.modifiedTextureColors.length + 1);
         var5[var5.length - 1] = originalModelColor;
         newModifiedModelColors[newModifiedModelColors.length - 1] = modifiedModelColor;
         this.originalTextureColors = var5;
         this.modifiedTextureColors = newModifiedModelColors;
      } else {
         this.originalTextureColors = new short[]{originalModelColor};
         this.modifiedTextureColors = new short[]{modifiedModelColor};
      }

   }

   public void resetModelColors() {
      this.originalModelColors = null;
      this.modifiedModelColors = null;
   }

   public void changeModelColor(int originalModelColor, int modifiedModelColor) {
      if(this.originalModelColors != null) {
         for(int newOriginalModelColors = 0; newOriginalModelColors < this.originalModelColors.length; ++newOriginalModelColors) {
            if(this.originalModelColors[newOriginalModelColors] == originalModelColor) {
               this.modifiedModelColors[newOriginalModelColors] = modifiedModelColor;
               return;
            }
         }

         int[] var5 = Arrays.copyOf(this.originalModelColors, this.originalModelColors.length + 1);
         int[] newModifiedModelColors = Arrays.copyOf(this.modifiedModelColors, this.modifiedModelColors.length + 1);
         var5[var5.length - 1] = originalModelColor;
         newModifiedModelColors[newModifiedModelColors.length - 1] = modifiedModelColor;
         this.originalModelColors = var5;
         this.modifiedModelColors = newModifiedModelColors;
      } else {
         this.originalModelColors = new int[]{originalModelColor};
         this.modifiedModelColors = new int[]{modifiedModelColor};
      }

   }

   public String[] getGroundOptions() {
      return this.groundOptions;
   }

   public String[] getInventoryOptions() {
      return this.inventoryOptions;
   }

   public int getEquipSlot() {
      return this.equipSlot;
   }

   public int getEquipType() {
      return this.equipType;
   }

   public Object clone() {
      try {
         return super.clone();
      } catch (CloneNotSupportedException var2) {
         var2.printStackTrace();
         return null;
      }
   }

   public String toString() {
      return String.valueOf(this.id) + " - " + this.name;
   }
}
