package com.editor.item;

import com.alex.loaders.items.ItemDefinitions;
import com.editor.Main;
import com.editor.Utils;
import com.editor.item.ItemSelection;

import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Iterator;

public class ItemEditor extends JFrame {
   private static final long serialVersionUID = -3870086097297420720L;
   private ItemDefinitions defs;
   private ItemSelection is;
   private JTextField ModelOffset1;
   private JTextField ModelRot2;
   private JMenuItem addModelMenuBtn;
   private JTextField array1;
   private JTextField array2;
   private JTextField array3;
   private JTextField array4;
   private JTextField array5;
   private JTextField array6;
   private JTextArea clientScriptOutput;
   private JTextField csk1;
   private JTextField csk2;
   private JTextField csk3;
   private JTextField csk4;
   private JTextField csk5;
   private JTextField csk6;
   private JTextField csk7;
   private JTextField csv1;
   private JTextField csv2;
   private JTextField csv3;
   private JTextField csv4;
   private JTextField csv5;
   private JTextField csv6;
   private JTextField csv7;
   private JLabel currentViewLabel;
   private JTextField equipSlot;
   private JTextField equipType;
   private JMenuItem exitMenuBtn;
   private JMenuItem exportMenuBtn;
   private JTextField femaleEquip1;
   private JTextField femaleEquip2;
   private JTextField femaleEquip3;
   private JTextField groundOptions;
   private JTextField int1;
   private JTextField int10;
   private JTextField int11;
   private JTextField int12;
   private JTextField int13;
   private JTextField int14;
   private JTextField int15;
   private JTextField int16;
   private JTextField int17;
   private JTextField int18;
   private JTextField int19;
   private JTextField int2;
   private JTextField int20;
   private JTextField int21;
   private JTextField int22;
   private JTextField int23;
   private JTextField int3;
   private JTextField int4;
   private JTextField int5;
   private JTextField int6;
   private JTextField int7;
   private JTextField int8;
   private JTextField int9;
   private JTextField invModel;
   private JTextField invModelZoom;
   private JTextField invOptions;
   private JTextField itemName;
   private JLabel jLabel1;
   private JLabel jLabel10;
   private JLabel jLabel11;
   private JLabel jLabel12;
   private JLabel jLabel13;
   private JLabel jLabel14;
   private JLabel jLabel15;
   private JLabel jLabel16;
   private JLabel jLabel17;
   private JLabel jLabel18;
   private JLabel jLabel19;
   private JLabel jLabel2;
   private JLabel jLabel20;
   private JLabel jLabel21;
   private JLabel jLabel22;
   private JLabel jLabel23;
   private JLabel jLabel24;
   private JLabel jLabel25;
   private JLabel jLabel26;
   private JLabel jLabel27;
   private JLabel jLabel28;
   private JLabel jLabel29;
   private JLabel jLabel3;
   private JLabel jLabel30;
   private JLabel jLabel31;
   private JLabel jLabel32;
   private JLabel jLabel33;
   private JLabel jLabel34;
   private JLabel jLabel35;
   private JLabel jLabel36;
   private JLabel jLabel37;
   private JLabel jLabel38;
   private JLabel jLabel39;
   private JLabel jLabel4;
   private JLabel jLabel40;
   private JLabel jLabel41;
   private JLabel jLabel42;
   private JLabel jLabel43;
   private JLabel jLabel44;
   private JLabel jLabel45;
   private JLabel jLabel46;
   private JLabel jLabel47;
   private JLabel jLabel48;
   private JLabel jLabel49;
   private JLabel jLabel5;
   private JLabel jLabel50;
   private JLabel jLabel51;
   private JLabel jLabel52;
   private JLabel jLabel53;
   private JLabel jLabel54;
   private JLabel jLabel55;
   private JLabel jLabel56;
   private JLabel jLabel57;
   private JLabel jLabel58;
   private JLabel jLabel59;
   private JLabel jLabel6;
   private JLabel jLabel60;
   private JLabel jLabel61;
   private JLabel jLabel7;
   private JLabel jLabel8;
   private JLabel jLabel9;
   private JMenu jMenu1;
   private JMenuBar jMenuBar1;
   private JPanel jPanel1;
   private JPanel jPanel2;
   private JPanel jPanel3;
   private JPanel jPanel5;
   private JPanel jPanel6;
   private JPanel jPanel7;
   private JScrollPane jScrollPane1;
   private JTabbedPane jTabbedPane1;
   private JTextField lend;
   private JTextField maleEquip1;
   private JTextField maleEquip2;
   private JTextField maleEquip3;
   private JCheckBox membersOnly;
   private JTextField modelColors;
   private JTextField modelOffset2;
   private JTextField modelRot1;
   private JTextField note;
   private JMenuItem reloadMenuBtn;
   private JMenuItem saveMenuBtn;
   private JTextField stackAmts;
   private JTextField stackIDs;
   private JTextField stackable;
   public JTextField switchLend;
   public JTextField switchNote;
   public JTextField teamId;
   public JTextField textureColors;
   public JCheckBox unnoted;
   public JTextField value;

   public ItemEditor(ItemSelection is, ItemDefinitions defs) {
      this.defs = defs;
      this.is = is;
      this.initComponents();
      this.setResizable(false);
      this.setTitle("Item Editor");
      this.setDefaultCloseOperation(1);
      this.setLocationRelativeTo((Component)null);
      this.setVisible(true);
   }

   private void initComponents() {
      this.jTabbedPane1 = new JTabbedPane();
      this.jPanel1 = new JPanel();
      this.jLabel1 = new JLabel();
      this.itemName = new JTextField();
      this.jLabel2 = new JLabel();
      this.value = new JTextField();
      this.teamId = new JTextField();
      this.jLabel3 = new JLabel();
      this.membersOnly = new JCheckBox();
      this.jLabel20 = new JLabel();
      this.equipSlot = new JTextField();
      this.jLabel21 = new JLabel();
      this.equipType = new JTextField();
      this.jLabel22 = new JLabel();
      this.stackIDs = new JTextField();
      this.jLabel23 = new JLabel();
      this.stackAmts = new JTextField();
      this.jLabel24 = new JLabel();
      this.stackable = new JTextField();
      this.jLabel58 = new JLabel();
      this.switchNote = new JTextField();
      this.jLabel59 = new JLabel();
      this.note = new JTextField();
      this.unnoted = new JCheckBox();
      this.jLabel60 = new JLabel();
      this.jLabel61 = new JLabel();
      this.switchLend = new JTextField();
      this.lend = new JTextField();
      this.jPanel2 = new JPanel();
      this.jLabel4 = new JLabel();
      this.invModelZoom = new JTextField();
      this.jLabel5 = new JLabel();
      this.modelRot1 = new JTextField();
      this.jLabel6 = new JLabel();
      this.ModelRot2 = new JTextField();
      this.jLabel7 = new JLabel();
      this.ModelOffset1 = new JTextField();
      this.jLabel8 = new JLabel();
      this.modelOffset2 = new JTextField();
      this.jLabel9 = new JLabel();
      this.invModel = new JTextField();
      this.jLabel10 = new JLabel();
      this.maleEquip1 = new JTextField();
      this.jLabel11 = new JLabel();
      this.femaleEquip1 = new JTextField();
      this.jLabel12 = new JLabel();
      this.maleEquip2 = new JTextField();
      this.jLabel13 = new JLabel();
      this.femaleEquip2 = new JTextField();
      this.jLabel14 = new JLabel();
      this.maleEquip3 = new JTextField();
      this.jLabel15 = new JLabel();
      this.femaleEquip3 = new JTextField();
      this.jPanel3 = new JPanel();
      this.jLabel16 = new JLabel();
      this.invOptions = new JTextField();
      this.jLabel17 = new JLabel();
      this.groundOptions = new JTextField();
      this.jLabel18 = new JLabel();
      this.modelColors = new JTextField();
      this.jLabel19 = new JLabel();
      this.textureColors = new JTextField();
      this.jPanel5 = new JPanel();
      this.jLabel25 = new JLabel();
      this.array1 = new JTextField();
      this.jLabel27 = new JLabel();
      this.jLabel28 = new JLabel();
      this.jLabel29 = new JLabel();
      this.jLabel30 = new JLabel();
      this.jLabel31 = new JLabel();
      this.array2 = new JTextField();
      this.array3 = new JTextField();
      this.array4 = new JTextField();
      this.array5 = new JTextField();
      this.array6 = new JTextField();
      this.jLabel32 = new JLabel();
      this.int1 = new JTextField();
      this.int2 = new JTextField();
      this.jLabel33 = new JLabel();
      this.jLabel34 = new JLabel();
      this.int3 = new JTextField();
      this.jLabel35 = new JLabel();
      this.int4 = new JTextField();
      this.jLabel36 = new JLabel();
      this.int5 = new JTextField();
      this.jLabel37 = new JLabel();
      this.int6 = new JTextField();
      this.jLabel38 = new JLabel();
      this.int7 = new JTextField();
      this.jLabel39 = new JLabel();
      this.int8 = new JTextField();
      this.jLabel40 = new JLabel();
      this.int9 = new JTextField();
      this.jLabel41 = new JLabel();
      this.int10 = new JTextField();
      this.jLabel42 = new JLabel();
      this.int11 = new JTextField();
      this.jLabel43 = new JLabel();
      this.int12 = new JTextField();
      this.jLabel44 = new JLabel();
      this.int13 = new JTextField();
      this.jLabel45 = new JLabel();
      this.int14 = new JTextField();
      this.jPanel6 = new JPanel();
      this.jLabel46 = new JLabel();
      this.int15 = new JTextField();
      this.jLabel47 = new JLabel();
      this.int16 = new JTextField();
      this.jLabel48 = new JLabel();
      this.int17 = new JTextField();
      this.jLabel49 = new JLabel();
      this.int18 = new JTextField();
      this.jLabel50 = new JLabel();
      this.int19 = new JTextField();
      this.jLabel51 = new JLabel();
      this.int20 = new JTextField();
      this.jLabel52 = new JLabel();
      this.int21 = new JTextField();
      this.jLabel53 = new JLabel();
      this.int22 = new JTextField();
      this.jLabel54 = new JLabel();
      this.int23 = new JTextField();
      this.jPanel7 = new JPanel();
      this.jScrollPane1 = new JScrollPane();
      this.clientScriptOutput = new JTextArea();
      this.jLabel26 = new JLabel();
      this.jLabel55 = new JLabel();
      this.jLabel56 = new JLabel();
      this.jLabel57 = new JLabel();
      this.csk1 = new JTextField();
      this.csk2 = new JTextField();
      this.csk3 = new JTextField();
      this.csk4 = new JTextField();
      this.csk5 = new JTextField();
      this.csk6 = new JTextField();
      this.csk7 = new JTextField();
      this.csv1 = new JTextField();
      this.csv2 = new JTextField();
      this.csv3 = new JTextField();
      this.csv4 = new JTextField();
      this.csv5 = new JTextField();
      this.csv6 = new JTextField();
      this.csv7 = new JTextField();
      this.currentViewLabel = new JLabel();
      this.jMenuBar1 = new JMenuBar();
      this.jMenu1 = new JMenu();
      this.reloadMenuBtn = new JMenuItem();
      this.saveMenuBtn = new JMenuItem();
      this.addModelMenuBtn = new JMenuItem();
      this.exportMenuBtn = new JMenuItem();
      this.exitMenuBtn = new JMenuItem();
      this.setDefaultCloseOperation(3);
      this.jLabel1.setText("Name");
      this.itemName.setText(this.defs.getName());
      this.jLabel2.setText("Value");
      this.value.setText("" + this.defs.getValue());
      this.teamId.setText("" + this.defs.getTeamId());
      this.jLabel3.setText("Team");
      this.membersOnly.setSelected(this.defs.isMembersOnly());
      this.membersOnly.setText("Members Only");
      this.jLabel20.setText("Equip Slot");
      this.equipSlot.setText("" + this.defs.getEquipSlot());
      this.jLabel21.setText("Equip Type");
      this.equipType.setText("" + this.defs.getEquipType());
      this.jLabel22.setText("Stack IDs");
      this.stackIDs.setText(this.getStackIDs());
      this.jLabel23.setText("Stack Amounts");
      this.stackAmts.setText(this.getStackAmts());
      this.jLabel24.setText("Stackable");
      this.stackable.setText("" + this.defs.getStackable());
      this.jLabel58.setText("Switch Note Item ID");
      this.switchNote.setText("" + this.defs.switchNoteItemId);
      this.jLabel59.setText("Noted Item ID");
      this.note.setText("" + this.defs.notedItemId);
      this.unnoted.setSelected(this.defs.isUnnoted());
      this.unnoted.setText("Unnoted");
      this.jLabel60.setText("Switch Lend Item ID");
      this.jLabel61.setText("Lent Item ID");
      this.switchLend.setText("" + this.defs.getSwitchLendItemId());
      this.lend.setText("" + this.defs.getLendedItemId());
      GroupLayout jPanel1Layout = new GroupLayout(this.jPanel1);
      this.jPanel1.setLayout(jPanel1Layout);
      jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING).addComponent(this.unnoted).addComponent(this.membersOnly).addGroup(jPanel1Layout.createSequentialGroup().addComponent(this.jLabel20).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(this.equipSlot, -2, 100, -2)).addGroup(jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addComponent(this.jLabel1).addGap(18, 18, 18).addComponent(this.itemName, -2, 100, -2)).addGroup(jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING).addComponent(this.jLabel2).addComponent(this.jLabel3)).addGap(18, 18, 18).addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING).addComponent(this.teamId, -2, 100, -2).addComponent(this.value, -2, 100, -2))).addGroup(jPanel1Layout.createSequentialGroup().addComponent(this.jLabel24).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.stackable, -2, 100, -2))).addGap(126, 126, 126).addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING).addComponent(this.stackAmts, -2, 300, -2).addComponent(this.stackIDs, -2, 300, -2).addComponent(this.jLabel22).addComponent(this.jLabel23))).addGroup(jPanel1Layout.createSequentialGroup().addComponent(this.jLabel21).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.equipType, -2, 100, -2)).addGroup(jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING).addComponent(this.jLabel58).addComponent(this.jLabel59)).addPreferredGap(ComponentPlacement.UNRELATED).addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING, false).addComponent(this.switchNote, -1, 100, 32767).addComponent(this.note)).addGap(18, 18, 18).addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING).addComponent(this.jLabel60).addComponent(this.jLabel61)).addPreferredGap(ComponentPlacement.UNRELATED).addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING, false).addComponent(this.switchLend, -1, 100, 32767).addComponent(this.lend)))).addContainerGap(36, 32767)));
      jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addGroup(jPanel1Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.jLabel1).addComponent(this.itemName, -2, -1, -2).addComponent(this.jLabel22)).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel1Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.value, -2, -1, -2).addComponent(this.jLabel2)).addComponent(this.stackIDs, -2, -1, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.teamId, -2, -1, -2).addComponent(this.jLabel3).addComponent(this.jLabel23)).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING).addComponent(this.stackAmts, -2, -1, -2).addGroup(jPanel1Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.jLabel24).addComponent(this.stackable, -2, -1, -2))).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.membersOnly).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.jLabel20).addComponent(this.equipSlot, -2, -1, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.jLabel21).addComponent(this.equipType, -2, -1, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.jLabel58).addComponent(this.switchNote, -2, -1, -2).addComponent(this.jLabel60).addComponent(this.switchLend, -2, -1, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.jLabel59).addComponent(this.note, -2, -1, -2).addComponent(this.jLabel61).addComponent(this.lend, -2, -1, -2)).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.unnoted).addContainerGap(13, 32767)));
      this.jTabbedPane1.addTab("General", this.jPanel1);
      this.jLabel4.setText("Inventory Model Zoom");
      this.invModelZoom.setText("" + this.defs.getInvModelZoom());
      this.jLabel5.setText("Model Rotation 1");
      this.modelRot1.setText("" + this.defs.getModelRotation1());
      this.jLabel6.setText("Model Rotation 2");
      this.ModelRot2.setText("" + this.defs.getModelRotation2());
      this.jLabel7.setText("Model Offset 1");
      this.ModelOffset1.setText("" + this.defs.getModelOffset1());
      this.jLabel8.setText("Model Offset 2");
      this.modelOffset2.setText("" + this.defs.getModelOffset2());
      this.jLabel9.setText("Inventory Model");
      this.invModel.setText("" + this.defs.getInvModelId());
      this.jLabel10.setText("Male Equip 1");
      this.maleEquip1.setText("" + this.defs.getMaleEquipModelId1());
      this.jLabel11.setText("Female Equip 1");
      this.femaleEquip1.setText("" + this.defs.getFemaleEquipModelId1());
      this.jLabel12.setText("Male Equip 2");
      this.maleEquip2.setText("" + this.defs.getMaleEquipModelId2());
      this.jLabel13.setText("Female Equip 2");
      this.femaleEquip2.setText("" + this.defs.getFemaleEquipModelId2());
      this.jLabel14.setText("Male Equip 3");
      this.maleEquip3.setText("" + this.defs.getMaleEquipModelId3());
      this.jLabel15.setText("Female Equip 3");
      this.femaleEquip3.setText("" + this.defs.getFemaleEquipModelId3());
      GroupLayout jPanel2Layout = new GroupLayout(this.jPanel2);
      this.jPanel2.setLayout(jPanel2Layout);
      jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addContainerGap().addGroup(jPanel2Layout.createParallelGroup(Alignment.LEADING, false).addGroup(jPanel2Layout.createSequentialGroup().addComponent(this.jLabel7).addPreferredGap(ComponentPlacement.RELATED, -1, 32767).addComponent(this.ModelOffset1, -2, 100, -2)).addGroup(jPanel2Layout.createSequentialGroup().addGroup(jPanel2Layout.createParallelGroup(Alignment.LEADING).addComponent(this.jLabel4).addComponent(this.jLabel5).addComponent(this.jLabel6)).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel2Layout.createParallelGroup(Alignment.LEADING).addComponent(this.ModelRot2, -2, 100, -2).addComponent(this.modelRot1, -2, 100, -2).addComponent(this.invModelZoom, -2, 100, -2))).addGroup(Alignment.TRAILING, jPanel2Layout.createSequentialGroup().addComponent(this.jLabel8).addPreferredGap(ComponentPlacement.RELATED, -1, 32767).addComponent(this.modelOffset2, -2, 100, -2))).addGap(114, 114, 114).addGroup(jPanel2Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addComponent(this.jLabel15).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(this.femaleEquip3, -2, 100, -2)).addGroup(jPanel2Layout.createSequentialGroup().addGroup(jPanel2Layout.createParallelGroup(Alignment.LEADING).addComponent(this.jLabel9).addComponent(this.jLabel10)).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel2Layout.createParallelGroup(Alignment.LEADING).addComponent(this.maleEquip1, -2, 100, -2).addComponent(this.invModel, -2, 100, -2))).addGroup(jPanel2Layout.createSequentialGroup().addGroup(jPanel2Layout.createParallelGroup(Alignment.LEADING).addComponent(this.jLabel11).addComponent(this.jLabel12)).addPreferredGap(ComponentPlacement.UNRELATED).addGroup(jPanel2Layout.createParallelGroup(Alignment.LEADING).addComponent(this.maleEquip2, -2, 100, -2).addComponent(this.femaleEquip1, -2, 100, -2))).addGroup(jPanel2Layout.createParallelGroup(Alignment.TRAILING, false).addGroup(jPanel2Layout.createSequentialGroup().addComponent(this.jLabel14).addPreferredGap(ComponentPlacement.RELATED, -1, 32767).addComponent(this.maleEquip3, -2, 100, -2)).addGroup(jPanel2Layout.createSequentialGroup().addComponent(this.jLabel13).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(this.femaleEquip2, -2, 100, -2)))).addContainerGap(103, 32767)));
      jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addContainerGap().addGroup(jPanel2Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.jLabel4).addComponent(this.invModelZoom, -2, -1, -2).addComponent(this.jLabel9).addComponent(this.invModel, -2, -1, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel2Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.jLabel5).addComponent(this.modelRot1, -2, -1, -2).addComponent(this.jLabel10).addComponent(this.maleEquip1, -2, -1, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel2Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.jLabel6).addComponent(this.ModelRot2, -2, -1, -2).addComponent(this.jLabel11).addComponent(this.femaleEquip1, -2, -1, -2)).addGroup(jPanel2Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addGap(9, 9, 9).addGroup(jPanel2Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.jLabel7).addComponent(this.ModelOffset1, -2, -1, -2).addComponent(this.jLabel12)).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel2Layout.createParallelGroup(Alignment.TRAILING).addComponent(this.jLabel8).addComponent(this.modelOffset2, -2, -1, -2).addGroup(Alignment.LEADING, jPanel2Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.jLabel13).addComponent(this.femaleEquip2, -2, -1, -2)))).addGroup(jPanel2Layout.createSequentialGroup().addPreferredGap(ComponentPlacement.RELATED).addComponent(this.maleEquip2, -2, -1, -2))).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel2Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.jLabel14).addComponent(this.maleEquip3, -2, -1, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel2Layout.createParallelGroup(Alignment.LEADING).addComponent(this.jLabel15).addComponent(this.femaleEquip3, -2, -1, -2)).addContainerGap(82, 32767)));
      this.jTabbedPane1.addTab("Model", this.jPanel2);
      this.jLabel16.setText("Inventory Options");
      this.invOptions.setText(this.getInventoryOpts());
      this.jLabel17.setText("Ground Options");
      this.groundOptions.setText(this.getGroundOpts());
      this.jLabel18.setText("Model Colors");
      this.modelColors.setText(this.getChangedModelColors());
      this.jLabel19.setText("Texture Colors");
      this.textureColors.setText(this.getChangedTextureColors());
      GroupLayout jPanel3Layout = new GroupLayout(this.jPanel3);
      this.jPanel3.setLayout(jPanel3Layout);
      jPanel3Layout.setHorizontalGroup(jPanel3Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel3Layout.createSequentialGroup().addContainerGap().addGroup(jPanel3Layout.createParallelGroup(Alignment.LEADING).addComponent(this.jLabel16).addComponent(this.invOptions, -2, 300, -2).addComponent(this.jLabel17).addComponent(this.groundOptions, -2, 300, -2).addComponent(this.jLabel18).addComponent(this.modelColors, -2, 300, -2).addComponent(this.jLabel19).addComponent(this.textureColors, -2, 300, -2)).addContainerGap(312, 32767)));
      jPanel3Layout.setVerticalGroup(jPanel3Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel3Layout.createSequentialGroup().addContainerGap().addComponent(this.jLabel16).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.invOptions, -2, -1, -2).addGap(18, 18, 18).addComponent(this.jLabel17).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.groundOptions, -2, -1, -2).addGap(18, 18, 18).addComponent(this.jLabel18).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.modelColors, -2, -1, -2).addGap(18, 18, 18).addComponent(this.jLabel19).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.textureColors, -2, -1, -2).addContainerGap(47, 32767)));
      this.jTabbedPane1.addTab("Options", this.jPanel3);
      this.jLabel25.setText("unknownArray1");
      this.array1.setText(this.getUnknownArray1());
      this.jLabel27.setText("unknownArray2");
      this.jLabel28.setText("unknownArray3");
      this.jLabel29.setText("unknownArray4");
      this.jLabel30.setText("unknownArray5");
      this.jLabel31.setText("unknownArray6");
      this.array2.setText(this.getUnknownArray2());
      this.array3.setText(this.getUnknownArray3());
      this.array4.setText(this.getUnknownArray4());
      this.array5.setText(this.getUnknownArray5());
      this.array6.setText(this.getUnknownArray6());
      this.jLabel32.setText("unknownInt1");
      this.int1.setText("" + this.defs.unknownInt1);
      this.int2.setText("" + this.defs.unknownInt2);
      this.jLabel33.setText("unknownInt2");
      this.jLabel34.setText("unknownInt3");
      this.int3.setText("" + this.defs.unknownInt3);
      this.jLabel35.setText("unknownInt4");
      this.int4.setText("" + this.defs.unknownInt4);
      this.jLabel36.setText("unknownInt5");
      this.int5.setText("" + this.defs.unknownInt5);
      this.jLabel37.setText("unknownInt6");
      this.int6.setText("" + this.defs.unknownInt6);
      this.jLabel38.setText("unknownInt7");
      this.int7.setText("" + this.defs.unknownInt7);
      this.jLabel39.setText("unknownInt8");
      this.int8.setText("" + this.defs.unknownInt8);
      this.jLabel40.setText("unknownInt9");
      this.int9.setText("" + this.defs.unknownInt9);
      this.jLabel41.setText("unknownInt10");
      this.int10.setText("" + this.defs.unknownInt10);
      this.jLabel42.setText("unknownInt11");
      this.int11.setText("" + this.defs.unknownInt11);
      this.jLabel43.setText("unknownInt12");
      this.int12.setText("" + this.defs.unknownInt12);
      this.jLabel44.setText("unknownInt13");
      this.int13.setText("" + this.defs.unknownInt13);
      this.jLabel45.setText("unknownInt14");
      this.int14.setText("" + this.defs.unknownInt14);
      GroupLayout jPanel5Layout = new GroupLayout(this.jPanel5);
      this.jPanel5.setLayout(jPanel5Layout);
      jPanel5Layout.setHorizontalGroup(jPanel5Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel5Layout.createSequentialGroup().addContainerGap().addGroup(jPanel5Layout.createParallelGroup(Alignment.LEADING, false).addGroup(jPanel5Layout.createSequentialGroup().addGroup(jPanel5Layout.createParallelGroup(Alignment.LEADING).addComponent(this.jLabel31).addComponent(this.jLabel32).addComponent(this.jLabel33).addComponent(this.jLabel34).addComponent(this.jLabel35)).addGap(18, 18, 18).addGroup(jPanel5Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel5Layout.createSequentialGroup().addComponent(this.array6, -2, 200, -2).addPreferredGap(ComponentPlacement.RELATED, -1, 32767).addComponent(this.jLabel41).addGap(18, 18, 18).addComponent(this.int10, -2, 100, -2)).addGroup(jPanel5Layout.createSequentialGroup().addComponent(this.int1, -2, 100, -2).addPreferredGap(ComponentPlacement.RELATED, -1, 32767).addComponent(this.jLabel42).addGap(18, 18, 18).addComponent(this.int11, -2, 100, -2)).addGroup(jPanel5Layout.createSequentialGroup().addComponent(this.int4, -2, 100, -2).addPreferredGap(ComponentPlacement.RELATED, -1, 32767).addComponent(this.jLabel45).addGap(18, 18, 18).addComponent(this.int14, -2, 100, -2)).addGroup(jPanel5Layout.createSequentialGroup().addComponent(this.int2, -2, 100, -2).addPreferredGap(ComponentPlacement.RELATED, -1, 32767).addComponent(this.jLabel43).addGap(18, 18, 18).addComponent(this.int12, -2, 100, -2)).addGroup(jPanel5Layout.createSequentialGroup().addComponent(this.int3, -2, 100, -2).addPreferredGap(ComponentPlacement.RELATED, -1, 32767).addComponent(this.jLabel44).addGap(18, 18, 18).addComponent(this.int13, -2, 100, -2)))).addGroup(jPanel5Layout.createSequentialGroup().addComponent(this.jLabel30).addGap(18, 18, 18).addComponent(this.array5, -2, 200, -2).addPreferredGap(ComponentPlacement.RELATED, -1, 32767).addComponent(this.jLabel40).addGap(18, 18, 18).addComponent(this.int9, -2, 100, -2)).addGroup(jPanel5Layout.createSequentialGroup().addComponent(this.jLabel29).addGap(18, 18, 18).addComponent(this.array4, -2, 200, -2).addPreferredGap(ComponentPlacement.RELATED, -1, 32767).addComponent(this.jLabel39).addGap(18, 18, 18).addComponent(this.int8, -2, 100, -2)).addGroup(jPanel5Layout.createSequentialGroup().addComponent(this.jLabel28).addGap(18, 18, 18).addComponent(this.array3, -2, 200, -2).addPreferredGap(ComponentPlacement.RELATED, -1, 32767).addComponent(this.jLabel38).addGap(18, 18, 18).addComponent(this.int7, -2, 100, -2)).addGroup(jPanel5Layout.createSequentialGroup().addComponent(this.jLabel27).addGap(18, 18, 18).addComponent(this.array2, -2, 200, -2).addPreferredGap(ComponentPlacement.RELATED, -1, 32767).addComponent(this.jLabel37).addGap(18, 18, 18).addComponent(this.int6, -2, 100, -2)).addGroup(jPanel5Layout.createSequentialGroup().addComponent(this.jLabel25).addGap(18, 18, 18).addComponent(this.array1, -2, 200, -2).addGap(73, 73, 73).addComponent(this.jLabel36).addGap(18, 18, 18).addComponent(this.int5, -2, 100, -2))).addContainerGap(64, 32767)));
      jPanel5Layout.setVerticalGroup(jPanel5Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel5Layout.createSequentialGroup().addContainerGap().addGroup(jPanel5Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.jLabel25).addComponent(this.array1, -2, -1, -2).addComponent(this.jLabel36).addComponent(this.int5, -2, -1, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel5Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.jLabel27).addComponent(this.array2, -2, -1, -2).addComponent(this.jLabel37).addComponent(this.int6, -2, -1, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel5Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.jLabel28).addComponent(this.array3, -2, -1, -2).addComponent(this.jLabel38).addComponent(this.int7, -2, -1, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel5Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.jLabel29).addComponent(this.array4, -2, -1, -2).addComponent(this.jLabel39).addComponent(this.int8, -2, -1, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel5Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.jLabel30).addComponent(this.array5, -2, -1, -2).addComponent(this.jLabel40).addComponent(this.int9, -2, -1, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel5Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.jLabel31).addComponent(this.array6, -2, -1, -2).addComponent(this.jLabel41).addComponent(this.int10, -2, -1, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel5Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.jLabel32).addComponent(this.int1, -2, -1, -2).addComponent(this.jLabel42).addComponent(this.int11, -2, -1, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel5Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.jLabel33).addComponent(this.int2, -2, -1, -2).addComponent(this.jLabel43).addComponent(this.int12, -2, -1, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel5Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.jLabel34).addComponent(this.int3, -2, -1, -2).addComponent(this.jLabel44).addComponent(this.int13, -2, -1, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel5Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel5Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.jLabel45).addComponent(this.int14, -2, -1, -2)).addGroup(jPanel5Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.jLabel35).addComponent(this.int4, -2, -1, -2))).addContainerGap(-1, 32767)));
      this.jTabbedPane1.addTab("Unknown Definitions", this.jPanel5);
      this.jLabel46.setText("unknownInt15");
      this.int15.setText("" + this.defs.unknownInt15);
      this.jLabel47.setText("unknownInt16");
      this.int16.setText("" + this.defs.unknownInt16);
      this.jLabel48.setText("unknownInt17");
      this.int17.setText("" + this.defs.unknownInt17);
      this.jLabel49.setText("unknownInt18");
      this.int18.setText("" + this.defs.unknownInt18);
      this.jLabel50.setText("unknownInt19");
      this.int19.setText("" + this.defs.unknownInt19);
      this.jLabel51.setText("unknownInt20");
      this.int20.setText("" + this.defs.unknownInt20);
      this.jLabel52.setText("unknownInt21");
      this.int21.setText("" + this.defs.unknownInt21);
      this.jLabel53.setText("unknownInt22");
      this.int22.setText("" + this.defs.unknownInt22);
      this.jLabel54.setText("unknownInt23");
      this.int23.setText("" + this.defs.unknownInt23);
      GroupLayout jPanel6Layout = new GroupLayout(this.jPanel6);
      this.jPanel6.setLayout(jPanel6Layout);
      jPanel6Layout.setHorizontalGroup(jPanel6Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel6Layout.createSequentialGroup().addContainerGap().addGroup(jPanel6Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel6Layout.createSequentialGroup().addComponent(this.jLabel46).addGap(18, 18, 18).addComponent(this.int15, -2, 100, -2)).addGroup(jPanel6Layout.createSequentialGroup().addComponent(this.jLabel47).addGap(18, 18, 18).addComponent(this.int16, -2, 100, -2)).addGroup(jPanel6Layout.createSequentialGroup().addComponent(this.jLabel48).addGap(18, 18, 18).addComponent(this.int17, -2, 100, -2)).addGroup(jPanel6Layout.createSequentialGroup().addComponent(this.jLabel49).addGap(18, 18, 18).addComponent(this.int18, -2, 100, -2)).addGroup(jPanel6Layout.createSequentialGroup().addComponent(this.jLabel50).addGap(18, 18, 18).addComponent(this.int19, -2, 100, -2)).addGroup(jPanel6Layout.createSequentialGroup().addComponent(this.jLabel51).addGap(18, 18, 18).addComponent(this.int20, -2, 100, -2)).addGroup(jPanel6Layout.createSequentialGroup().addComponent(this.jLabel52).addGap(18, 18, 18).addComponent(this.int21, -2, 100, -2)).addGroup(jPanel6Layout.createSequentialGroup().addComponent(this.jLabel53).addGap(18, 18, 18).addComponent(this.int22, -2, 100, -2)).addGroup(jPanel6Layout.createSequentialGroup().addComponent(this.jLabel54).addGap(18, 18, 18).addComponent(this.int23, -2, 100, -2))).addContainerGap(425, 32767)));
      jPanel6Layout.setVerticalGroup(jPanel6Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel6Layout.createSequentialGroup().addContainerGap().addGroup(jPanel6Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.jLabel46).addComponent(this.int15, -2, -1, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel6Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.jLabel47).addComponent(this.int16, -2, -1, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel6Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.jLabel48).addComponent(this.int17, -2, -1, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel6Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.jLabel49).addComponent(this.int18, -2, -1, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel6Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.jLabel50).addComponent(this.int19, -2, -1, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel6Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.jLabel51).addComponent(this.int20, -2, -1, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel6Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.jLabel52).addComponent(this.int21, -2, -1, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel6Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.jLabel53).addComponent(this.int22, -2, -1, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel6Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.jLabel54).addComponent(this.int23, -2, -1, -2)).addContainerGap(33, 32767)));
      this.jTabbedPane1.addTab("Unknown Definitions(2)", this.jPanel6);
      this.clientScriptOutput.setColumns(20);
      this.clientScriptOutput.setRows(5);
      this.clientScriptOutput.setText(this.getClientScripts());
      this.jScrollPane1.setViewportView(this.clientScriptOutput);
      this.jLabel26.setText("KEY");
      this.jLabel55.setText("VALUE");
      this.jLabel56.setText("Add the keys to the left to the boxes to edit them.");
      this.jLabel57.setText("Add new keys in the boxes to give the item new clientscripts.");
      GroupLayout jPanel7Layout = new GroupLayout(this.jPanel7);
      this.jPanel7.setLayout(jPanel7Layout);
      jPanel7Layout.setHorizontalGroup(jPanel7Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel7Layout.createSequentialGroup().addContainerGap().addComponent(this.jScrollPane1, -2, 305, -2).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel7Layout.createParallelGroup(Alignment.LEADING).addComponent(this.jLabel57).addGroup(jPanel7Layout.createParallelGroup(Alignment.TRAILING).addGroup(jPanel7Layout.createSequentialGroup().addGroup(jPanel7Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel7Layout.createParallelGroup(Alignment.TRAILING).addComponent(this.csk2, -2, 75, -2).addComponent(this.csk1, -2, 75, -2).addComponent(this.csk3, -2, 75, -2).addComponent(this.csk4, -2, 75, -2).addComponent(this.csk5, -2, 75, -2).addComponent(this.csk6, -2, 75, -2).addComponent(this.csk7, -2, 75, -2)).addComponent(this.jLabel26)).addGap(58, 58, 58).addGroup(jPanel7Layout.createParallelGroup(Alignment.LEADING).addComponent(this.csv1, -2, 75, -2).addComponent(this.csv2, -2, 75, -2).addComponent(this.csv3, -2, 75, -2).addComponent(this.csv4, -2, 75, -2).addComponent(this.csv5, -2, 75, -2).addComponent(this.csv6, -2, 75, -2).addComponent(this.csv7, -2, 75, -2).addComponent(this.jLabel55))).addComponent(this.jLabel56))).addContainerGap(-1, 32767)));
      jPanel7Layout.setVerticalGroup(jPanel7Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel7Layout.createSequentialGroup().addContainerGap().addGroup(jPanel7Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel7Layout.createSequentialGroup().addGroup(jPanel7Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.jLabel26).addComponent(this.jLabel55)).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel7Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.csk1, -2, -1, -2).addComponent(this.csv1, -2, -1, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel7Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.csk2, -2, -1, -2).addComponent(this.csv2, -2, -1, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel7Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.csk3, -2, -1, -2).addComponent(this.csv3, -2, -1, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel7Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.csk4, -2, -1, -2).addComponent(this.csv4, -2, -1, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel7Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.csk5, -2, -1, -2).addComponent(this.csv5, -2, -1, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel7Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.csk6, -2, -1, -2).addComponent(this.csv6, -2, -1, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel7Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.csk7, -2, -1, -2).addComponent(this.csv7, -2, -1, -2)).addPreferredGap(ComponentPlacement.RELATED, 20, 32767).addComponent(this.jLabel56).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.jLabel57)).addComponent(this.jScrollPane1)).addContainerGap()));
      this.jTabbedPane1.addTab("Clientscripts", this.jPanel7);
      this.currentViewLabel.setText("Currently Viewing Definitions of Item: " + this.defs.getId() + " - " + this.defs.getName());
      this.jMenu1.setText("File");
      this.reloadMenuBtn.setText("Reload");
      this.reloadMenuBtn.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent evt) {
            ItemEditor.this.reloadMenuBtnActionPerformed(evt);
         }
      });
      this.jMenu1.add(this.reloadMenuBtn);
      this.saveMenuBtn.setText("Save");
      this.saveMenuBtn.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent evt) {
            ItemEditor.this.saveMenuBtnActionPerformed(evt);
         }
      });
      this.jMenu1.add(this.saveMenuBtn);
      this.addModelMenuBtn.setText("Add Model");
      this.addModelMenuBtn.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent evt) {
            ItemEditor.this.addModelMenuBtnActionPerformed(evt);
         }
      });
      this.jMenu1.add(this.addModelMenuBtn);
      this.exportMenuBtn.setText("Export to .txt");
      this.exportMenuBtn.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent evt) {
            ItemEditor.this.exportMenuBtnActionPerformed(evt);
         }
      });
      this.jMenu1.add(this.exportMenuBtn);
      this.exitMenuBtn.setText("Exit");
      this.exitMenuBtn.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent evt) {
            ItemEditor.this.exitMenuBtnActionPerformed(evt);
         }
      });
      this.jMenu1.add(this.exitMenuBtn);
      this.jMenuBar1.add(this.jMenu1);
      this.setJMenuBar(this.jMenuBar1);
      GroupLayout layout = new GroupLayout(this.getContentPane());
      this.getContentPane().setLayout(layout);
      layout.setHorizontalGroup(layout.createParallelGroup(Alignment.LEADING).addComponent(this.jTabbedPane1).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(this.currentViewLabel).addContainerGap(-1, 32767)));
      layout.setVerticalGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, layout.createSequentialGroup().addGap(0, 11, 32767).addComponent(this.currentViewLabel).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.jTabbedPane1, -2, 300, -2)));
      this.pack();
   }

   private void exitMenuBtnActionPerformed(ActionEvent evt) {
      this.dispose();
   }

   private void exportMenuBtnActionPerformed(ActionEvent evt) {
      this.export();
   }

   private void addModelMenuBtnActionPerformed(ActionEvent evt) {
      this.addModel();
   }

   private void saveMenuBtnActionPerformed(ActionEvent evt) {
      this.save();
   }

   private void reloadMenuBtnActionPerformed(ActionEvent evt) {
      this.itemName.setText(this.defs.getName());
      this.value.setText("" + this.defs.getValue());
      this.teamId.setText("" + this.defs.getTeamId());
      this.membersOnly.setSelected(this.defs.isMembersOnly());
      this.equipSlot.setText("" + this.defs.getEquipSlot());
      this.equipType.setText("" + this.defs.getEquipType());
      this.stackIDs.setText(this.getStackIDs());
      this.stackAmts.setText(this.getStackAmts());
      this.stackable.setText("" + this.defs.getStackable());
      this.invModelZoom.setText("" + this.defs.getInvModelZoom());
      this.modelRot1.setText("" + this.defs.getModelRotation1());
      this.ModelRot2.setText("" + this.defs.getModelRotation2());
      this.ModelOffset1.setText("" + this.defs.getModelOffset1());
      this.modelOffset2.setText("" + this.defs.getModelOffset2());
      this.invModel.setText("" + this.defs.getInvModelId());
      this.maleEquip1.setText("" + this.defs.getMaleEquipModelId1());
      this.femaleEquip1.setText("" + this.defs.getFemaleEquipModelId1());
      this.maleEquip2.setText("" + this.defs.getMaleEquipModelId2());
      this.femaleEquip2.setText("" + this.defs.getFemaleEquipModelId2());
      this.maleEquip3.setText("" + this.defs.getMaleEquipModelId3());
      this.femaleEquip3.setText("" + this.defs.getFemaleEquipModelId3());
      this.invOptions.setText(this.getInventoryOpts());
      this.groundOptions.setText(this.getGroundOpts());
      this.modelColors.setText(this.getChangedModelColors());
      this.textureColors.setText(this.getChangedTextureColors());
      this.switchNote.setText("" + this.defs.switchNoteItemId);
      this.note.setText("" + this.defs.notedItemId);
      this.unnoted.setSelected(this.defs.isUnnoted());
      this.switchLend.setText("" + this.defs.getSwitchLendItemId());
      this.lend.setText("" + this.defs.getLendedItemId());
      this.array1.setText(this.getUnknownArray1());
      this.array2.setText(this.getUnknownArray2());
      this.array3.setText(this.getUnknownArray3());
      this.array4.setText(this.getUnknownArray4());
      this.array5.setText(this.getUnknownArray5());
      this.array6.setText(this.getUnknownArray6());
      this.int1.setText("" + this.defs.unknownInt1);
      this.int2.setText("" + this.defs.unknownInt2);
      this.int3.setText("" + this.defs.unknownInt3);
      this.int4.setText("" + this.defs.unknownInt4);
      this.int5.setText("" + this.defs.unknownInt5);
      this.int6.setText("" + this.defs.unknownInt6);
      this.int7.setText("" + this.defs.unknownInt7);
      this.int8.setText("" + this.defs.unknownInt8);
      this.int9.setText("" + this.defs.unknownInt9);
      this.int10.setText("" + this.defs.unknownInt10);
      this.int11.setText("" + this.defs.unknownInt11);
      this.int12.setText("" + this.defs.unknownInt12);
      this.int13.setText("" + this.defs.unknownInt13);
      this.int14.setText("" + this.defs.unknownInt14);
      this.int15.setText("" + this.defs.unknownInt15);
      this.int16.setText("" + this.defs.unknownInt16);
      this.int17.setText("" + this.defs.unknownInt17);
      this.int18.setText("" + this.defs.unknownInt18);
      this.int19.setText("" + this.defs.unknownInt19);
      this.int20.setText("" + this.defs.unknownInt20);
      this.int21.setText("" + this.defs.unknownInt21);
      this.int22.setText("" + this.defs.unknownInt22);
      this.int23.setText("" + this.defs.unknownInt23);
      this.clientScriptOutput.setText(this.getClientScripts());
   }

   private void export() {
      File f = new File(System.getProperty("user.home") + "/FCE/items/");
      f.mkdirs();
      String lineSep = System.getProperty("line.separator");
      BufferedWriter writer = null;

      try {
         writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(System.getProperty("user.home") + "/FCE/items/" + this.defs.id + ".txt"), "utf-8"));
         writer.write("name = " + this.defs.getName());
         writer.write(lineSep);
         writer.write("value = " + this.defs.getValue());
         writer.write(lineSep);
         writer.write("team id = " + this.defs.getTeamId());
         writer.write(lineSep);
         writer.write("members only = " + String.valueOf(this.defs.isMembersOnly()));
         writer.write(lineSep);
         writer.write("equip slot = " + this.defs.getEquipSlot());
         writer.write(lineSep);
         writer.write("equip type = " + this.defs.getEquipType());
         writer.write(lineSep);
         writer.write("stack ids = " + this.getStackIDs());
         writer.write(lineSep);
         writer.write("stack amounts = " + this.getStackAmts());
         writer.write(lineSep);
         writer.write("stackable = " + this.defs.getStackable());
         writer.write(lineSep);
         writer.write("inv model zoom = " + this.defs.getInvModelZoom());
         writer.write(lineSep);
         writer.write("model rotation 1 = " + this.defs.getModelRotation1());
         writer.write(lineSep);
         writer.write("model rotation 2 = " + this.defs.getModelRotation2());
         writer.write(lineSep);
         writer.write("model offset 1 = " + this.defs.getModelOffset1());
         writer.write(lineSep);
         writer.write("model offset 2 = " + this.defs.getModelOffset2());
         writer.write(lineSep);
         writer.write("inv model id = " + this.defs.getInvModelId());
         writer.write(lineSep);
         writer.write("male equip model id 1 = " + this.defs.getMaleEquipModelId1());
         writer.write(lineSep);
         writer.write("female equip model id 1 = " + this.defs.getFemaleEquipModelId1());
         writer.write(lineSep);
         writer.write("male equip model id 2 = " + this.defs.getMaleEquipModelId2());
         writer.write(lineSep);
         writer.write("female equip model id 2 = " + this.defs.getFemaleEquipModelId2());
         writer.write(lineSep);
         writer.write("male equip model id 3 = " + this.defs.getMaleEquipModelId3());
         writer.write(lineSep);
         writer.write("female equip model id 3 = " + this.defs.getFemaleEquipModelId3());
         writer.write(lineSep);
         writer.write("inventory options = " + this.getInventoryOpts());
         writer.write(lineSep);
         writer.write("ground options = " + this.getGroundOpts());
         writer.write(lineSep);
         writer.write("changed model colors = " + this.getChangedModelColors());
         writer.write(lineSep);
         writer.write("changed texture colors = " + this.getChangedTextureColors());
         writer.write(lineSep);
         writer.write("switch note item id = " + this.defs.switchNoteItemId);
         writer.write(lineSep);
         writer.write("noted item id = " + this.defs.notedItemId);
         writer.write(lineSep);
         writer.write("unnoted = " + String.valueOf(this.defs.isUnnoted()));
         writer.write(lineSep);
         writer.write("switch lend item id = " + this.defs.getSwitchLendItemId());
         writer.write(lineSep);
         writer.write("lended item id = " + this.defs.getLendedItemId());
         writer.write(lineSep);
         writer.write("unknownArray1 = " + this.getUnknownArray1());
         writer.write(lineSep);
         writer.write("unknownArray2 = " + this.getUnknownArray2());
         writer.write(lineSep);
         writer.write("unknownArray3 = " + this.getUnknownArray3());
         writer.write(lineSep);
         writer.write("unknownArray4 = " + this.getUnknownArray4());
         writer.write(lineSep);
         writer.write("unknownArray5 = " + this.getUnknownArray5());
         writer.write(lineSep);
         writer.write("unknownArray6 = " + this.getUnknownArray6());
         writer.write(lineSep);
         writer.write("unknownInt1 = " + this.defs.unknownInt1);
         writer.write(lineSep);
         writer.write("unknownInt2 = " + this.defs.unknownInt2);
         writer.write(lineSep);
         writer.write("unknownInt3 = " + this.defs.unknownInt3);
         writer.write(lineSep);
         writer.write("unknownInt4 = " + this.defs.unknownInt4);
         writer.write(lineSep);
         writer.write("unknownInt5 = " + this.defs.unknownInt5);
         writer.write(lineSep);
         writer.write("unknownInt6 = " + this.defs.unknownInt6);
         writer.write(lineSep);
         writer.write("unknownInt7 = " + this.defs.unknownInt7);
         writer.write(lineSep);
         writer.write("unknownInt8 = " + this.defs.unknownInt8);
         writer.write(lineSep);
         writer.write("unknownInt9 = " + this.defs.unknownInt9);
         writer.write(lineSep);
         writer.write("unknownInt10 = " + this.defs.unknownInt10);
         writer.write(lineSep);
         writer.write("unknownInt11 = " + this.defs.unknownInt11);
         writer.write(lineSep);
         writer.write("unknownInt12 = " + this.defs.unknownInt12);
         writer.write(lineSep);
         writer.write("unknownInt13 = " + this.defs.unknownInt13);
         writer.write(lineSep);
         writer.write("unknownInt14 = " + this.defs.unknownInt14);
         writer.write(lineSep);
         writer.write("unknownInt15 = " + this.defs.unknownInt15);
         writer.write(lineSep);
         writer.write("unknownInt16 = " + this.defs.unknownInt16);
         writer.write(lineSep);
         writer.write("unknownInt17 = " + this.defs.unknownInt17);
         writer.write(lineSep);
         writer.write("unknownInt18 = " + this.defs.unknownInt18);
         writer.write(lineSep);
         writer.write("unknownInt19 = " + this.defs.unknownInt19);
         writer.write(lineSep);
         writer.write("unknownInt20 = " + this.defs.unknownInt20);
         writer.write(lineSep);
         writer.write("unknownInt21 = " + this.defs.unknownInt21);
         writer.write(lineSep);
         writer.write("unknownInt22 = " + this.defs.unknownInt22);
         writer.write(lineSep);
         writer.write("unknownInt23 = " + this.defs.unknownInt23);
         writer.write(lineSep);
         writer.write("Clientscripts");
         writer.write(lineSep);
         if(this.defs.clientScriptData != null) {
            Iterator var15 = this.defs.clientScriptData.keySet().iterator();

            while(var15.hasNext()) {
               int key = ((Integer)var15.next()).intValue();
               Object value = this.defs.clientScriptData.get(Integer.valueOf(key));
               writer.write("KEY: " + key + ", VALUE: " + value);
               writer.write(lineSep);
            }
         }
      } catch (IOException var151) {
         Main.log("ItemEditor", "Failed to export Item Defs to .txt");
      } finally {
         try {
            writer.close();
         } catch (Exception var14) {
            ;
         }

      }

   }

   private void save() {
      try {
         this.defs.setName(this.itemName.getText().toString());
         this.defs.setValue(Integer.parseInt(this.value.getText().toString()));
         this.defs.setTeamId(Integer.parseInt(this.teamId.getText().toString()));
         this.defs.setMembersOnly(this.membersOnly.isSelected());
         this.defs.setEquipSlot(Integer.parseInt(this.equipSlot.getText().toString()));
         this.defs.setEquipType(Integer.parseInt(this.equipType.getText().toString()));
         String[] var17;
         int invOpts;
         if(!this.stackIDs.getText().equals("")) {
            var17 = this.stackIDs.getText().split(";");

            for(invOpts = 0; invOpts < this.defs.getStackIds().length; ++invOpts) {
               this.defs.getStackIds()[invOpts] = Integer.parseInt(var17[invOpts]);
            }
         }

         if(!this.stackAmts.getText().equals("")) {
            var17 = this.stackAmts.getText().split(";");

            for(invOpts = 0; invOpts < this.defs.getStackAmounts().length; ++invOpts) {
               this.defs.getStackAmounts()[invOpts] = Integer.parseInt(var17[invOpts]);
            }
         }

         this.defs.setStackable(Integer.parseInt(this.stackable.getText().toString()));
         this.defs.setInvModelZoom(Integer.parseInt(this.invModelZoom.getText().toString()));
         this.defs.setModelRotation1(Integer.parseInt(this.modelRot1.getText().toString()));
         this.defs.setModelRotation2(Integer.parseInt(this.ModelRot2.getText().toString()));
         this.defs.setModelOffset1(Integer.parseInt(this.ModelOffset1.getText().toString()));
         this.defs.setModelOffset2(Integer.parseInt(this.modelOffset2.getText().toString()));
         this.defs.setInvModelId(Integer.parseInt(this.invModel.getText().toString()));
         this.defs.maleEquip1 = Integer.parseInt(this.maleEquip1.getText().toString());
         this.defs.maleEquip2 = Integer.parseInt(this.maleEquip2.getText().toString());
         this.defs.maleEquipModelId3 = Integer.parseInt(this.maleEquip3.getText().toString());
         this.defs.femaleEquip1 = Integer.parseInt(this.femaleEquip1.getText().toString());
         this.defs.femaleEquip2 = Integer.parseInt(this.femaleEquip2.getText().toString());
         this.defs.femaleEquipModelId3 = Integer.parseInt(this.femaleEquip3.getText().toString());
         var17 = this.groundOptions.getText().split(";");

         for(invOpts = 0; invOpts < this.defs.getGroundOptions().length; ++invOpts) {
            this.defs.getGroundOptions()[invOpts] = var17[invOpts].equals("null")?null:var17[invOpts];
         }

         String[] var19 = this.invOptions.getText().split(";");

         for(int i = 0; i < this.defs.getInventoryOptions().length; ++i) {
            this.defs.getInventoryOptions()[i] = var19[i].equals("null")?null:var19[i];
         }

         this.defs.resetModelColors();
         int len$;
         int i$;
         String t;
         String[] editedColor;
         String[] var18;
         String[] var21;
         if(!this.modelColors.getText().equals("")) {
            var18 = this.modelColors.getText().split(";");
            var21 = var18;
            len$ = var18.length;

            for(i$ = 0; i$ < len$; ++i$) {
               t = var21[i$];
               editedColor = t.split("=");
               this.defs.changeModelColor(Integer.valueOf(editedColor[0]).intValue(), Integer.valueOf(editedColor[1]).intValue());
            }
         }

         this.defs.resetTextureColors();
         if(!this.textureColors.getText().equals("")) {
            var18 = this.textureColors.getText().split(";");
            var21 = var18;
            len$ = var18.length;

            for(i$ = 0; i$ < len$; ++i$) {
               t = var21[i$];
               editedColor = t.split("=");
               this.defs.changeTextureColor(Short.valueOf(editedColor[0]).shortValue(), Short.valueOf(editedColor[1]).shortValue());
            }
         }

         this.defs.notedItemId = Integer.valueOf(this.note.getText()).intValue();
         this.defs.switchNoteItemId = Integer.valueOf(this.switchNote.getText()).intValue();
         this.defs.lendedItemId = Integer.valueOf(this.lend.getText()).intValue();
         this.defs.switchLendItemId = Integer.valueOf(this.switchLend.getText()).intValue();
         this.defs.setUnnoted(this.unnoted.isSelected());
         int var20;
         if(!this.array1.getText().equals("")) {
            var18 = this.array1.getText().split(";");

            for(var20 = 0; var20 < this.defs.unknownArray1.length; ++var20) {
               this.defs.unknownArray1[var20] = (byte)Integer.parseInt(var18[var20]);
            }
         }

         if(!this.array2.getText().equals("")) {
            var18 = this.array2.getText().split(";");

            for(var20 = 0; var20 < this.defs.unknownArray2.length; ++var20) {
               this.defs.unknownArray2[var20] = Integer.parseInt(var18[var20]);
            }
         }

         if(!this.array3.getText().equals("")) {
            var18 = this.array3.getText().split(";");

            for(var20 = 0; var20 < this.defs.unknownArray3.length; ++var20) {
               this.defs.unknownArray3[var20] = (byte)Integer.parseInt(var18[var20]);
            }
         }

         if(!this.array4.getText().equals("")) {
            var18 = this.array4.getText().split(";");

            for(var20 = 0; var20 < this.defs.unknownArray4.length; ++var20) {
               this.defs.unknownArray4[var20] = Integer.parseInt(var18[var20]);
            }
         }

         if(!this.array5.getText().equals("")) {
            var18 = this.array5.getText().split(";");

            for(var20 = 0; var20 < this.defs.unknownArray5.length; ++var20) {
               this.defs.unknownArray5[var20] = Integer.parseInt(var18[var20]);
            }
         }

         if(!this.array6.getText().equals("")) {
            var18 = this.array6.getText().split(";");

            for(var20 = 0; var20 < this.defs.unknownArray6.length; ++var20) {
               this.defs.unknownArray6[var20] = (byte)Integer.parseInt(var18[var20]);
            }
         }

         this.defs.unknownInt1 = Integer.parseInt(this.int1.getText());
         this.defs.unknownInt2 = Integer.parseInt(this.int2.getText());
         this.defs.unknownInt3 = Integer.parseInt(this.int3.getText());
         this.defs.unknownInt4 = Integer.parseInt(this.int4.getText());
         this.defs.unknownInt5 = Integer.parseInt(this.int5.getText());
         this.defs.unknownInt6 = Integer.parseInt(this.int6.getText());
         this.defs.unknownInt7 = Integer.parseInt(this.int7.getText());
         this.defs.unknownInt8 = Integer.parseInt(this.int8.getText());
         this.defs.unknownInt9 = Integer.parseInt(this.int9.getText());
         this.defs.unknownInt10 = Integer.parseInt(this.int10.getText());
         this.defs.unknownInt11 = Integer.parseInt(this.int11.getText());
         this.defs.unknownInt12 = Integer.parseInt(this.int12.getText());
         this.defs.unknownInt13 = Integer.parseInt(this.int13.getText());
         this.defs.unknownInt14 = Integer.parseInt(this.int14.getText());
         this.defs.unknownInt15 = Integer.parseInt(this.int15.getText());
         this.defs.unknownInt16 = Integer.parseInt(this.int16.getText());
         this.defs.unknownInt17 = Integer.parseInt(this.int17.getText());
         this.defs.unknownInt18 = Integer.parseInt(this.int18.getText());
         this.defs.unknownInt19 = Integer.parseInt(this.int19.getText());
         this.defs.unknownInt20 = Integer.parseInt(this.int20.getText());
         this.defs.unknownInt21 = Integer.parseInt(this.int21.getText());
         this.defs.unknownInt22 = Integer.parseInt(this.int22.getText());
         this.defs.unknownInt23 = Integer.parseInt(this.int23.getText());

         try {
            if(!this.csk1.getText().equals("") && !this.csv1.getText().equals("")) {
               try {
                  this.defs.clientScriptData.remove(this.csk1);
                  this.defs.clientScriptData.put(Integer.valueOf(Integer.parseInt(this.csk1.getText())), Integer.valueOf(Integer.parseInt(this.csv1.getText())));
               } catch (Exception var181) {
                  this.defs.clientScriptData.remove(this.csk1);
                  this.defs.clientScriptData.put(Integer.valueOf(Integer.parseInt(this.csk1.getText())), this.csv1.getText().toString());
               }
            }

            if(!this.csk2.getText().equals("") && !this.csv2.getText().equals("")) {
               try {
                  this.defs.clientScriptData.remove(this.csk2);
                  this.defs.clientScriptData.put(Integer.valueOf(Integer.parseInt(this.csk2.getText())), Integer.valueOf(Integer.parseInt(this.csv2.getText())));
               } catch (Exception var171) {
                  this.defs.clientScriptData.remove(this.csk2);
                  this.defs.clientScriptData.put(Integer.valueOf(Integer.parseInt(this.csk2.getText())), this.csv2.getText().toString());
               }
            }

            if(!this.csk3.getText().equals("") && !this.csv3.getText().equals("")) {
               try {
                  this.defs.clientScriptData.remove(this.csk3);
                  this.defs.clientScriptData.put(Integer.valueOf(Integer.parseInt(this.csk3.getText())), Integer.valueOf(Integer.parseInt(this.csv3.getText())));
               } catch (Exception var16) {
                  this.defs.clientScriptData.remove(this.csk3);
                  this.defs.clientScriptData.put(Integer.valueOf(Integer.parseInt(this.csk3.getText())), this.csv3.getText().toString());
               }
            }

            if(!this.csk4.getText().equals("") && !this.csv4.getText().equals("")) {
               try {
                  this.defs.clientScriptData.remove(this.csk4);
                  this.defs.clientScriptData.put(Integer.valueOf(Integer.parseInt(this.csk4.getText())), Integer.valueOf(Integer.parseInt(this.csv4.getText())));
               } catch (Exception var15) {
                  this.defs.clientScriptData.remove(this.csk4);
                  this.defs.clientScriptData.put(Integer.valueOf(Integer.parseInt(this.csk4.getText())), this.csv4.getText().toString());
               }
            }

            if(!this.csk5.getText().equals("") && !this.csv5.getText().equals("")) {
               try {
                  this.defs.clientScriptData.remove(this.csk5);
                  this.defs.clientScriptData.put(Integer.valueOf(Integer.parseInt(this.csk5.getText())), Integer.valueOf(Integer.parseInt(this.csv5.getText())));
               } catch (Exception var14) {
                  this.defs.clientScriptData.remove(this.csk5);
                  this.defs.clientScriptData.put(Integer.valueOf(Integer.parseInt(this.csk5.getText())), this.csv5.getText().toString());
               }
            }

            if(!this.csk6.getText().equals("") && !this.csv6.getText().equals("")) {
               try {
                  this.defs.clientScriptData.remove(this.csk6);
                  this.defs.clientScriptData.put(Integer.valueOf(Integer.parseInt(this.csk6.getText())), Integer.valueOf(Integer.parseInt(this.csv6.getText())));
               } catch (Exception var13) {
                  this.defs.clientScriptData.remove(this.csk6);
                  this.defs.clientScriptData.put(Integer.valueOf(Integer.parseInt(this.csk6.getText())), this.csv6.getText().toString());
               }
            }

            if(!this.csk7.getText().equals("") && !this.csv7.getText().equals("")) {
               try {
                  this.defs.clientScriptData.remove(this.csk7);
                  this.defs.clientScriptData.put(Integer.valueOf(Integer.parseInt(this.csk7.getText())), Integer.valueOf(Integer.parseInt(this.csv7.getText())));
               } catch (Exception var12) {
                  this.defs.clientScriptData.remove(this.csk7);
                  this.defs.clientScriptData.put(Integer.valueOf(Integer.parseInt(this.csk7.getText())), this.csv7.getText().toString());
               }
            }
         } catch (Exception var191) {
            this.defs.clientScriptData = new HashMap(1);
         }

         ItemSelection var10001 = this.is;
         this.defs.write(ItemSelection.STORE);
         this.is.updateItemDefs(this.defs);
      } catch (Exception var201) {
         Main.log("ItemEditor", "Cannot save. Please check for mistypes.");
      }

   }

   private void addModel() {
      JFrame frame = new JFrame();
      int result = JOptionPane.showConfirmDialog(frame, "Do you want to specify a model ID?");
      StringBuilder var10001;
      ItemSelection var10002;
      if(result == 0) {
         JFrame fc1 = new JFrame();
         String returnVal1 = JOptionPane.showInputDialog(fc1, "Enter new model ID:");
         if(Integer.parseInt(returnVal1.toString()) != -1) {
            JFileChooser file2 = new JFileChooser();
            file2.setFileSelectionMode(0);
            int var9 = file2.showOpenDialog(this);
            if(var9 == 0) {
               File file1 = file2.getSelectedFile();

               try {
                  var10001 = (new StringBuilder()).append("The model ID of the recently packed model is: ");
                  var10002 = this.is;
                  Main.log("ItemEditor", var10001.append(Utils.packCustomModel(ItemSelection.STORE, Utils.getBytesFromFile(new File(file1.getPath().toString())), Integer.parseInt(returnVal1.toString()))).toString());
               } catch (IOException var12) {
                  Main.log("ItemEditor", "There was an error packing the model.");
               }
            }
         }
      } else if(result == 1) {
         JFileChooser fc11 = new JFileChooser();
         fc11.setFileSelectionMode(0);
         int returnVal11 = fc11.showOpenDialog(this);
         if(returnVal11 == 0) {
            File file21 = fc11.getSelectedFile();

            try {
               var10001 = (new StringBuilder()).append("The model ID of the recently packed model is: ");
               var10002 = this.is;
               Main.log("ItemEditor", var10001.append(Utils.packCustomModel(ItemSelection.STORE, Utils.getBytesFromFile(new File(file21.getPath().toString())))).toString());
            } catch (IOException var11) {
               Main.log("ItemEditor", "There was an error packing the model.");
            }
         }
      }

   }

   public String getInventoryOpts() {
      String text = "";
      String[] arr$ = this.defs.getInventoryOptions();
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         String option = arr$[i$];
         text = text + (option == null?"null":option) + ";";
      }

      return text;
   }

   public String getGroundOpts() {
      String text = "";
      String[] arr$ = this.defs.getGroundOptions();
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         String option = arr$[i$];
         text = text + (option == null?"null":option) + ";";
      }

      return text;
   }

   public String getChangedModelColors() {
      String text = "";
      if(this.defs.originalModelColors != null) {
         for(int i = 0; i < this.defs.originalModelColors.length; ++i) {
            text = text + this.defs.originalModelColors[i] + "=" + this.defs.modifiedModelColors[i] + ";";
         }
      }

      return text;
   }

   public String getChangedTextureColors() {
      String text = "";
      if(this.defs.originalTextureColors != null) {
         for(int i = 0; i < this.defs.originalTextureColors.length; ++i) {
            text = text + this.defs.originalTextureColors[i] + "=" + this.defs.modifiedTextureColors[i] + ";";
         }
      }

      return text;
   }

   public String getStackIDs() {
      String text = "";

      try {
         int[] e = this.defs.getStackIds();
         int len$ = e.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            int index = e[i$];
            text = text + index + ";";
         }
      } catch (Exception var6) {
         ;
      }

      return text;
   }

   public String getClientScripts() {
      String text = "";
      String lineSep = System.getProperty("line.separator");
      if(this.defs.clientScriptData != null) {
         for(Iterator i$ = this.defs.clientScriptData.keySet().iterator(); i$.hasNext(); text = text + lineSep) {
            int key = ((Integer)i$.next()).intValue();
            Object value = this.defs.clientScriptData.get(Integer.valueOf(key));
            text = text + "KEY: " + key + ", VALUE: " + value;
         }
      }

      return text;
   }

   public String getStackAmts() {
      String text = "";

      try {
         int[] e = this.defs.getStackAmounts();
         int len$ = e.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            int index = e[i$];
            text = text + index + ";";
         }
      } catch (Exception var6) {
         ;
      }

      return text;
   }

   public String getUnknownArray1() {
      String text = "";

      try {
         byte[] e = this.defs.unknownArray1;
         int len$ = e.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            byte index = e[i$];
            text = text + index + ";";
         }
      } catch (Exception var6) {
         ;
      }

      return text;
   }

   public String getUnknownArray2() {
      String text = "";

      try {
         int[] e = this.defs.unknownArray2;
         int len$ = e.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            int index = e[i$];
            text = text + index + ";";
         }
      } catch (Exception var6) {
         ;
      }

      return text;
   }

   public String getUnknownArray3() {
      String text = "";

      try {
         byte[] e = this.defs.unknownArray3;
         int len$ = e.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            byte index = e[i$];
            text = text + index + ";";
         }
      } catch (Exception var6) {
         ;
      }

      return text;
   }

   public String getUnknownArray4() {
      String text = "";

      try {
         int[] e = this.defs.unknownArray4;
         int len$ = e.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            int index = e[i$];
            text = text + index + ";";
         }
      } catch (Exception var6) {
         ;
      }

      return text;
   }

   public String getUnknownArray5() {
      String text = "";

      try {
         int[] e = this.defs.unknownArray5;
         int len$ = e.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            int index = e[i$];
            text = text + index + ";";
         }
      } catch (Exception var6) {
         ;
      }

      return text;
   }

   public String getUnknownArray6() {
      String text = "";

      try {
         byte[] e = this.defs.unknownArray6;
         int len$ = e.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            byte index = e[i$];
            text = text + index + ";";
         }
      } catch (Exception var6) {
         ;
      }

      return text;
   }
}
