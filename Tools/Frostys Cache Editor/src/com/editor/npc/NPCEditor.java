package com.editor.npc;

import com.alex.loaders.npcs.NPCDefinitions;
import com.editor.Main;
import com.editor.Utils;
import com.editor.npc.NPCSelection;

import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Iterator;

public class NPCEditor extends JFrame {
   private static final long serialVersionUID = -8143046599523114860L;
   private NPCDefinitions defs;
   private NPCSelection ns;
   private JMenuItem addModelButton;
   private JTextArea clientScriptOutput;
   private JTextField combatLevel;
   private JTextField csk1;
   private JTextField csk2;
   private JTextField csk3;
   private JTextField csk4;
   private JTextField csk5;
   private JTextField csk6;
   private JTextField csv1;
   private JTextField csv2;
   private JTextField csv3;
   private JTextField csv4;
   private JTextField csv5;
   private JTextField csv6;
   private JMenuItem exitButton;
   private JMenuItem exportButton;
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
   private JLabel jLabel5;
   private JLabel jLabel6;
   private JLabel jLabel7;
   private JLabel jLabel8;
   private JLabel jLabel9;
   private JMenu jMenu1;
   private JMenuBar jMenuBar1;
   private JPanel jPanel1;
   private JPanel jPanel2;
   private JPanel jPanel3;
   private JPanel jPanel4;
   private JPanel jPanel5;
   private JScrollPane jScrollPane1;
   private JTabbedPane jTabbedPane1;
   private JTextField jTextField2;
   private JTextField modelColorField;
   private JTextField modelIds;
   private JTextField name;
   private JTextField npcHeight;
   private JTextField npcSize;
   private JTextField npcWidth;
   private JTextField optionsField;
   private JMenuItem reloadButton;
   private JTextField renderEmote;
   private JTextField respawnDirection;
   private JMenuItem saveButton;
   private JTextField textureColorField;
   private JTextField unknownArray1;
   private JTextField unknownArray2;
   private JTextField unknownArray3;
   private JTextField unknownArray4;
   private JTextField unknownArray5;
   private JCheckBox unknownBoolean1;
   private JCheckBox unknownBoolean2;
   private JCheckBox unknownBoolean3;
   private JCheckBox unknownBoolean4;
   private JCheckBox unknownBoolean5;
   private JCheckBox unknownBoolean6;
   private JCheckBox unknownBoolean7;
   private JTextField unknownInt1;
   private JTextField unknownInt10;
   private JTextField unknownInt11;
   private JTextField unknownInt12;
   private JTextField unknownInt13;
   private JTextField unknownInt14;
   private JTextField unknownInt15;
   private JTextField unknownInt16;
   private JTextField unknownInt17;
   private JTextField unknownInt18;
   private JTextField unknownInt19;
   private JTextField unknownInt2;
   private JTextField unknownInt20;
   private JTextField unknownInt21;
   private JTextField unknownInt3;
   private JTextField unknownInt4;
   private JTextField unknownInt5;
   private JTextField unknownInt6;
   private JTextField unknownInt7;
   private JTextField unknownInt8;
   private JTextField unknownInt9;
   private JCheckBox visibleOnMap;
   private JTextField walkMask;

   public NPCEditor(NPCSelection ns, NPCDefinitions defs) {
      this.defs = defs;
      this.ns = ns;
      this.initComponents();
      this.setResizable(false);
      this.setTitle("NPC Editor");
      this.setDefaultCloseOperation(1);
      this.setLocationRelativeTo((Component)null);
      this.setVisible(true);
   }

   public NPCEditor() {
      this.initComponents();
   }

   private void initComponents() {
      this.jTabbedPane1 = new JTabbedPane();
      this.jPanel1 = new JPanel();
      this.jLabel1 = new JLabel();
      this.name = new JTextField();
      this.jLabel4 = new JLabel();
      this.combatLevel = new JTextField();
      this.jLabel5 = new JLabel();
      this.npcSize = new JTextField();
      this.visibleOnMap = new JCheckBox();
      this.jLabel10 = new JLabel();
      this.npcHeight = new JTextField();
      this.jLabel11 = new JLabel();
      this.npcWidth = new JTextField();
      this.jLabel12 = new JLabel();
      this.walkMask = new JTextField();
      this.jLabel13 = new JLabel();
      this.respawnDirection = new JTextField();
      this.jLabel14 = new JLabel();
      this.renderEmote = new JTextField();
      this.jPanel2 = new JPanel();
      this.jLabel3 = new JLabel();
      this.modelIds = new JTextField();
      this.jLabel6 = new JLabel();
      this.jTextField2 = new JTextField();
      this.jPanel3 = new JPanel();
      this.jLabel7 = new JLabel();
      this.optionsField = new JTextField();
      this.jLabel8 = new JLabel();
      this.modelColorField = new JTextField();
      this.jLabel9 = new JLabel();
      this.textureColorField = new JTextField();
      this.jPanel4 = new JPanel();
      this.jLabel15 = new JLabel();
      this.unknownArray1 = new JTextField();
      this.jLabel16 = new JLabel();
      this.unknownArray2 = new JTextField();
      this.jLabel17 = new JLabel();
      this.unknownArray3 = new JTextField();
      this.jLabel18 = new JLabel();
      this.unknownArray4 = new JTextField();
      this.jLabel19 = new JLabel();
      this.unknownArray5 = new JTextField();
      this.unknownBoolean1 = new JCheckBox();
      this.unknownBoolean2 = new JCheckBox();
      this.unknownBoolean3 = new JCheckBox();
      this.unknownBoolean5 = new JCheckBox();
      this.unknownBoolean4 = new JCheckBox();
      this.unknownBoolean6 = new JCheckBox();
      this.unknownBoolean7 = new JCheckBox();
      this.jLabel20 = new JLabel();
      this.unknownInt1 = new JTextField();
      this.jLabel21 = new JLabel();
      this.unknownInt2 = new JTextField();
      this.jLabel22 = new JLabel();
      this.unknownInt3 = new JTextField();
      this.jLabel23 = new JLabel();
      this.unknownInt4 = new JTextField();
      this.jLabel24 = new JLabel();
      this.unknownInt5 = new JTextField();
      this.jLabel25 = new JLabel();
      this.unknownInt6 = new JTextField();
      this.jLabel26 = new JLabel();
      this.unknownInt7 = new JTextField();
      this.jLabel27 = new JLabel();
      this.unknownInt8 = new JTextField();
      this.jLabel28 = new JLabel();
      this.unknownInt9 = new JTextField();
      this.jLabel29 = new JLabel();
      this.unknownInt10 = new JTextField();
      this.jLabel30 = new JLabel();
      this.unknownInt11 = new JTextField();
      this.jLabel31 = new JLabel();
      this.unknownInt12 = new JTextField();
      this.jLabel32 = new JLabel();
      this.unknownInt13 = new JTextField();
      this.jLabel33 = new JLabel();
      this.unknownInt14 = new JTextField();
      this.jLabel34 = new JLabel();
      this.unknownInt15 = new JTextField();
      this.jLabel35 = new JLabel();
      this.unknownInt16 = new JTextField();
      this.jLabel36 = new JLabel();
      this.unknownInt17 = new JTextField();
      this.jLabel37 = new JLabel();
      this.unknownInt18 = new JTextField();
      this.jLabel38 = new JLabel();
      this.unknownInt19 = new JTextField();
      this.jLabel39 = new JLabel();
      this.unknownInt20 = new JTextField();
      this.jLabel40 = new JLabel();
      this.unknownInt21 = new JTextField();
      this.jPanel5 = new JPanel();
      this.jScrollPane1 = new JScrollPane();
      this.clientScriptOutput = new JTextArea();
      this.jLabel41 = new JLabel();
      this.jLabel42 = new JLabel();
      this.csk1 = new JTextField();
      this.csk2 = new JTextField();
      this.csk3 = new JTextField();
      this.csk4 = new JTextField();
      this.csk5 = new JTextField();
      this.csk6 = new JTextField();
      this.csv1 = new JTextField();
      this.csv2 = new JTextField();
      this.csv3 = new JTextField();
      this.csv4 = new JTextField();
      this.csv5 = new JTextField();
      this.csv6 = new JTextField();
      this.jLabel43 = new JLabel();
      this.jLabel44 = new JLabel();
      this.jLabel2 = new JLabel();
      this.jMenuBar1 = new JMenuBar();
      this.jMenu1 = new JMenu();
      this.reloadButton = new JMenuItem();
      this.saveButton = new JMenuItem();
      this.addModelButton = new JMenuItem();
      this.exportButton = new JMenuItem();
      this.exitButton = new JMenuItem();
      this.setDefaultCloseOperation(3);
      this.jLabel1.setText("Name");
      this.name.setText(this.defs.getName());
      this.jLabel4.setText("Combat Level");
      this.combatLevel.setText("" + this.defs.getCombatLevel());
      this.jLabel5.setText("NPC Size");
      this.npcSize.setText("" + this.defs.getSize());
      this.visibleOnMap.setSelected(this.defs.isVisibleOnMap);
      this.visibleOnMap.setText("Visible On Map");
      this.jLabel10.setText("NPC Height");
      this.npcHeight.setText("" + this.defs.npcHeight);
      this.jLabel11.setText("NPC Width");
      this.npcWidth.setText("" + this.defs.npcWidth);
      this.jLabel12.setText("Walk Mask");
      this.walkMask.setText("" + this.defs.walkMask);
      this.jLabel13.setText("Respawn Direction");
      this.respawnDirection.setText("" + this.defs.respawnDirection);
      this.jLabel14.setText("Render Animation");
      this.renderEmote.setText("" + this.defs.renderEmote);
      GroupLayout jPanel1Layout = new GroupLayout(this.jPanel1);
      this.jPanel1.setLayout(jPanel1Layout);
      jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING).addComponent(this.visibleOnMap).addGroup(jPanel1Layout.createParallelGroup(Alignment.TRAILING, false).addGroup(Alignment.LEADING, jPanel1Layout.createSequentialGroup().addComponent(this.jLabel1).addPreferredGap(ComponentPlacement.RELATED, -1, 32767).addComponent(this.name, -2, 100, -2)).addGroup(Alignment.LEADING, jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING).addComponent(this.jLabel4).addComponent(this.jLabel5)).addGap(18, 18, 18).addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING).addComponent(this.npcSize, -2, 100, -2).addComponent(this.combatLevel, -2, 100, -2))).addGroup(jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING).addComponent(this.jLabel10).addComponent(this.jLabel11).addComponent(this.jLabel12)).addPreferredGap(ComponentPlacement.RELATED, -1, 32767).addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING, false).addComponent(this.npcHeight, -1, 100, 32767).addComponent(this.npcWidth).addComponent(this.walkMask)))).addGroup(jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING).addComponent(this.jLabel13).addComponent(this.jLabel14)).addPreferredGap(ComponentPlacement.UNRELATED).addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING, false).addComponent(this.respawnDirection, -1, 100, 32767).addComponent(this.renderEmote)))).addContainerGap(455, 32767)));
      jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addGroup(jPanel1Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.jLabel1).addComponent(this.name, -2, -1, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.jLabel4).addComponent(this.combatLevel, -2, -1, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.jLabel5).addComponent(this.npcSize, -2, 20, -2)).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.visibleOnMap).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.jLabel10).addComponent(this.npcHeight, -2, -1, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.jLabel11).addComponent(this.npcWidth, -2, -1, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.jLabel12).addComponent(this.walkMask, -2, -1, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.jLabel13).addComponent(this.respawnDirection, -2, -1, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.jLabel14).addComponent(this.renderEmote, -2, -1, -2)).addContainerGap(134, 32767)));
      this.jTabbedPane1.addTab("General", this.jPanel1);
      this.jLabel3.setText("Model IDs");
      this.modelIds.setText(this.getModelIds());
      this.jLabel6.setText("Chat Heads");
      this.jTextField2.setText(this.getChatHeads());
      GroupLayout jPanel2Layout = new GroupLayout(this.jPanel2);
      this.jPanel2.setLayout(jPanel2Layout);
      jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addContainerGap().addGroup(jPanel2Layout.createParallelGroup(Alignment.LEADING, false).addComponent(this.jLabel3).addComponent(this.modelIds, -1, 275, 32767).addComponent(this.jLabel6).addComponent(this.jTextField2)).addContainerGap(379, 32767)));
      jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addContainerGap().addComponent(this.jLabel3).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.modelIds, -2, -1, -2).addGap(18, 18, 18).addComponent(this.jLabel6).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.jTextField2, -2, -1, -2).addContainerGap(259, 32767)));
      this.jTabbedPane1.addTab("Model", this.jPanel2);
      this.jLabel7.setText("Options");
      this.optionsField.setText(this.getOpts());
      this.jLabel8.setText("Model Colors");
      this.modelColorField.setText(this.getChangedModelColors());
      this.jLabel9.setText("Texture Colors");
      this.textureColorField.setText(this.getChangedTextureColors());
      GroupLayout jPanel3Layout = new GroupLayout(this.jPanel3);
      this.jPanel3.setLayout(jPanel3Layout);
      jPanel3Layout.setHorizontalGroup(jPanel3Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel3Layout.createSequentialGroup().addContainerGap().addGroup(jPanel3Layout.createParallelGroup(Alignment.LEADING, false).addComponent(this.jLabel7).addComponent(this.optionsField, -1, 275, 32767).addComponent(this.jLabel8).addComponent(this.modelColorField).addComponent(this.jLabel9).addComponent(this.textureColorField)).addContainerGap(379, 32767)));
      jPanel3Layout.setVerticalGroup(jPanel3Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel3Layout.createSequentialGroup().addContainerGap().addComponent(this.jLabel7).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.optionsField, -2, -1, -2).addGap(18, 18, 18).addComponent(this.jLabel8).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.modelColorField, -2, -1, -2).addGap(18, 18, 18).addComponent(this.jLabel9).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.textureColorField, -2, -1, -2).addContainerGap(201, 32767)));
      this.jTabbedPane1.addTab("Options", this.jPanel3);
      this.jLabel15.setText("unknownArray1");
      this.unknownArray1.setText(this.getUnknownArray1());
      this.jLabel16.setText("unknownArray2");
      this.unknownArray2.setText(this.getUnknownArray2());
      this.jLabel17.setText("unknownArray3");
      this.unknownArray3.setText(this.getUnknownArray3());
      this.jLabel18.setText("unknownArray4");
      this.unknownArray4.setText(this.getUnknownArray4());
      this.jLabel19.setText("unknownArray5");
      this.unknownArray5.setText(this.getUnknownArray5());
      this.unknownBoolean1.setSelected(this.defs.unknownBoolean1);
      this.unknownBoolean1.setText("unknownBoolean1");
      this.unknownBoolean2.setSelected(this.defs.unknownBoolean2);
      this.unknownBoolean2.setText("unknownBoolean2");
      this.unknownBoolean3.setSelected(this.defs.unknownBoolean3);
      this.unknownBoolean3.setText("unknownBoolean3");
      this.unknownBoolean5.setSelected(this.defs.unknownBoolean5);
      this.unknownBoolean5.setText("unknownBoolean5");
      this.unknownBoolean4.setSelected(this.defs.unknownBoolean4);
      this.unknownBoolean4.setText("unknownBoolean4");
      this.unknownBoolean6.setSelected(this.defs.unknownBoolean6);
      this.unknownBoolean6.setText("unknownBoolean6");
      this.unknownBoolean7.setSelected(this.defs.unknownBoolean7);
      this.unknownBoolean7.setText("unknownBoolean7");
      this.jLabel20.setText("unknownInt1");
      this.unknownInt1.setText("" + this.defs.unknownInt1);
      this.jLabel21.setText("unknownInt2");
      this.unknownInt2.setText("" + this.defs.unknownInt2);
      this.jLabel22.setText("unknownInt3");
      this.unknownInt3.setText("" + this.defs.unknownInt3);
      this.jLabel23.setText("unknownInt4");
      this.unknownInt4.setText("" + this.defs.unknownInt4);
      this.jLabel24.setText("unknownInt5");
      this.unknownInt5.setText("" + this.defs.unknownInt5);
      this.jLabel25.setText("unknownInt6");
      this.unknownInt6.setText("" + this.defs.unknownInt6);
      this.jLabel26.setText("unknownInt7");
      this.unknownInt7.setText("" + this.defs.unknownInt7);
      this.jLabel27.setText("unknownInt8");
      this.unknownInt8.setText("" + this.defs.unknownInt8);
      this.jLabel28.setText("unknownInt9");
      this.unknownInt9.setText("" + this.defs.unknownInt9);
      this.jLabel29.setText("unknownInt10");
      this.unknownInt10.setText("" + this.defs.unknownInt10);
      this.jLabel30.setText("unknownInt11");
      this.unknownInt11.setText("" + this.defs.unknownInt11);
      this.jLabel31.setText("unknownInt12");
      this.unknownInt12.setText("" + this.defs.unknownInt12);
      this.jLabel32.setText("unknownInt13");
      this.unknownInt13.setText("" + this.defs.unknownInt13);
      this.jLabel33.setText("unknownInt14");
      this.unknownInt14.setText("" + this.defs.unknownInt14);
      this.jLabel34.setText("unknownInt15");
      this.unknownInt15.setText("" + this.defs.unknownInt15);
      this.jLabel35.setText("unknownInt16");
      this.unknownInt16.setText("" + this.defs.unknownInt16);
      this.jLabel36.setText("unknownInt17");
      this.unknownInt17.setText("" + this.defs.unknownInt17);
      this.jLabel37.setText("unknownInt18");
      this.unknownInt18.setText("" + this.defs.unknownInt18);
      this.jLabel38.setText("unknownInt19");
      this.unknownInt19.setText("" + this.defs.unknownInt19);
      this.jLabel39.setText("unknownInt20");
      this.unknownInt20.setText("" + this.defs.unknownInt20);
      this.jLabel40.setText("unknownInt21");
      this.unknownInt21.setText("" + this.defs.unknownInt21);
      GroupLayout jPanel4Layout = new GroupLayout(this.jPanel4);
      this.jPanel4.setLayout(jPanel4Layout);
      jPanel4Layout.setHorizontalGroup(jPanel4Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel4Layout.createSequentialGroup().addContainerGap().addGroup(jPanel4Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel4Layout.createSequentialGroup().addGroup(jPanel4Layout.createParallelGroup(Alignment.LEADING).addComponent(this.unknownArray2, -2, 175, -2).addComponent(this.jLabel15).addComponent(this.unknownArray1, -2, 175, -2).addComponent(this.jLabel16).addComponent(this.jLabel17)).addGap(18, 18, 18).addGroup(jPanel4Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel4Layout.createSequentialGroup().addComponent(this.jLabel20).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.unknownInt1, -2, 100, -2).addPreferredGap(ComponentPlacement.RELATED, 33, 32767).addComponent(this.jLabel32).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.unknownInt13, -2, 100, -2)).addGroup(jPanel4Layout.createSequentialGroup().addComponent(this.jLabel21).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.unknownInt2, -2, 100, -2).addPreferredGap(ComponentPlacement.RELATED, -1, 32767).addComponent(this.jLabel33).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.unknownInt14, -2, 100, -2)).addGroup(Alignment.TRAILING, jPanel4Layout.createSequentialGroup().addGroup(jPanel4Layout.createParallelGroup(Alignment.LEADING).addComponent(this.unknownBoolean5).addComponent(this.unknownBoolean6).addComponent(this.unknownBoolean7)).addPreferredGap(ComponentPlacement.RELATED, -1, 32767).addGroup(jPanel4Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel4Layout.createSequentialGroup().addComponent(this.jLabel31).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.unknownInt12, -2, 100, -2)).addGroup(jPanel4Layout.createSequentialGroup().addComponent(this.jLabel30).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.unknownInt11, -2, 100, -2)).addGroup(jPanel4Layout.createSequentialGroup().addComponent(this.jLabel29).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.unknownInt10, -2, 100, -2)))))).addGroup(jPanel4Layout.createSequentialGroup().addGroup(jPanel4Layout.createParallelGroup(Alignment.LEADING).addComponent(this.unknownArray3, -2, 175, -2).addComponent(this.jLabel18)).addGap(18, 18, 18).addGroup(jPanel4Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel4Layout.createSequentialGroup().addComponent(this.jLabel23).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.unknownInt4, -2, 100, -2).addPreferredGap(ComponentPlacement.RELATED, -1, 32767).addComponent(this.jLabel35).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.unknownInt16, -2, 100, -2)).addGroup(jPanel4Layout.createSequentialGroup().addComponent(this.jLabel22).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.unknownInt3, -2, 100, -2).addPreferredGap(ComponentPlacement.RELATED, -1, 32767).addComponent(this.jLabel34).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.unknownInt15, -2, 100, -2)))).addGroup(jPanel4Layout.createSequentialGroup().addGroup(jPanel4Layout.createParallelGroup(Alignment.LEADING).addComponent(this.unknownBoolean4).addComponent(this.unknownBoolean3)).addGap(0, 0, 32767)).addGroup(jPanel4Layout.createSequentialGroup().addGroup(jPanel4Layout.createParallelGroup(Alignment.LEADING).addComponent(this.unknownArray4, -2, 175, -2).addComponent(this.jLabel19)).addGap(18, 18, 18).addGroup(jPanel4Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel4Layout.createSequentialGroup().addComponent(this.jLabel25).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.unknownInt6, -2, 100, -2).addPreferredGap(ComponentPlacement.RELATED, -1, 32767).addComponent(this.jLabel37).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.unknownInt18, -2, 100, -2)).addGroup(jPanel4Layout.createSequentialGroup().addComponent(this.jLabel24).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.unknownInt5, -2, 100, -2).addPreferredGap(ComponentPlacement.RELATED, -1, 32767).addComponent(this.jLabel36).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.unknownInt17, -2, 100, -2)))).addGroup(jPanel4Layout.createSequentialGroup().addGroup(jPanel4Layout.createParallelGroup(Alignment.TRAILING, false).addGroup(Alignment.LEADING, jPanel4Layout.createSequentialGroup().addComponent(this.unknownArray5, -2, 175, -2).addGap(18, 18, 18).addComponent(this.jLabel26).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.unknownInt7, -2, 100, -2)).addGroup(jPanel4Layout.createSequentialGroup().addGroup(jPanel4Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel4Layout.createSequentialGroup().addComponent(this.unknownBoolean1).addPreferredGap(ComponentPlacement.RELATED, -1, 32767)).addGroup(Alignment.TRAILING, jPanel4Layout.createSequentialGroup().addComponent(this.unknownBoolean2).addGap(80, 80, 80))).addGroup(jPanel4Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel4Layout.createSequentialGroup().addComponent(this.jLabel28).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.unknownInt9, -2, 100, -2)).addGroup(jPanel4Layout.createSequentialGroup().addComponent(this.jLabel27).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.unknownInt8, -2, 100, -2))))).addPreferredGap(ComponentPlacement.RELATED, -1, 32767).addGroup(jPanel4Layout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, jPanel4Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel4Layout.createSequentialGroup().addComponent(this.jLabel38).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.unknownInt19, -2, 100, -2)).addGroup(jPanel4Layout.createSequentialGroup().addComponent(this.jLabel39).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.unknownInt20, -2, 100, -2))).addGroup(Alignment.TRAILING, jPanel4Layout.createSequentialGroup().addComponent(this.jLabel40).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.unknownInt21, -2, 100, -2))))).addGap(88, 88, 88)));
      jPanel4Layout.setVerticalGroup(jPanel4Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel4Layout.createSequentialGroup().addContainerGap().addGroup(jPanel4Layout.createParallelGroup(Alignment.LEADING, false).addGroup(jPanel4Layout.createSequentialGroup().addComponent(this.jLabel15).addGap(4, 4, 4).addComponent(this.unknownArray1, -2, -1, -2).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.jLabel16).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.unknownArray2, -2, -1, -2)).addGroup(jPanel4Layout.createSequentialGroup().addGroup(jPanel4Layout.createParallelGroup(Alignment.TRAILING).addGroup(jPanel4Layout.createSequentialGroup().addGroup(jPanel4Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.unknownBoolean5).addComponent(this.jLabel29).addComponent(this.unknownInt10, -2, -1, -2)).addGap(22, 22, 22)).addGroup(jPanel4Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.unknownBoolean6).addComponent(this.jLabel30).addComponent(this.unknownInt11, -2, -1, -2))).addGroup(jPanel4Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.unknownBoolean7).addComponent(this.jLabel31).addComponent(this.unknownInt12, -2, -1, -2)).addPreferredGap(ComponentPlacement.RELATED, -1, 32767).addGroup(jPanel4Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.jLabel20).addComponent(this.unknownInt1, -2, -1, -2).addComponent(this.jLabel32).addComponent(this.unknownInt13, -2, -1, -2)))).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel4Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.jLabel17).addComponent(this.jLabel21).addComponent(this.unknownInt2, -2, -1, -2).addComponent(this.jLabel33).addComponent(this.unknownInt14, -2, -1, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel4Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.unknownArray3, -2, -1, -2).addComponent(this.jLabel22).addComponent(this.unknownInt3, -2, -1, -2).addComponent(this.jLabel34).addComponent(this.unknownInt15, -2, -1, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel4Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.jLabel18).addComponent(this.jLabel23).addComponent(this.unknownInt4, -2, -1, -2).addComponent(this.jLabel35).addComponent(this.unknownInt16, -2, -1, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel4Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.unknownArray4, -2, -1, -2).addComponent(this.jLabel24).addComponent(this.unknownInt5, -2, -1, -2).addComponent(this.jLabel36).addComponent(this.unknownInt17, -2, -1, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel4Layout.createParallelGroup(Alignment.LEADING).addComponent(this.unknownInt6, -2, 20, -2).addGroup(jPanel4Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.jLabel19).addComponent(this.jLabel25).addComponent(this.jLabel37).addComponent(this.unknownInt18, -2, -1, -2))).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel4Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.unknownArray5, -2, -1, -2).addComponent(this.jLabel26).addComponent(this.unknownInt7, -2, -1, -2).addComponent(this.jLabel38).addComponent(this.unknownInt19, -2, -1, -2)).addPreferredGap(ComponentPlacement.UNRELATED).addGroup(jPanel4Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.unknownBoolean1).addComponent(this.jLabel27).addComponent(this.unknownInt8, -2, -1, -2).addComponent(this.jLabel39).addComponent(this.unknownInt20, -2, -1, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel4Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.unknownBoolean2).addComponent(this.jLabel28).addComponent(this.unknownInt9, -2, -1, -2).addComponent(this.jLabel40).addComponent(this.unknownInt21, -2, -1, -2)).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.unknownBoolean3).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.unknownBoolean4).addContainerGap(-1, 32767)));
      this.jTabbedPane1.addTab("Unknown Definitions", this.jPanel4);
      this.clientScriptOutput.setColumns(20);
      this.clientScriptOutput.setRows(5);
      this.clientScriptOutput.setText(this.getClientScripts());
      this.jScrollPane1.setViewportView(this.clientScriptOutput);
      this.jLabel41.setText("KEY");
      this.jLabel42.setText("VALUE");
      this.jLabel43.setText("Add keys into the boxes on the left to edit or add them.");
      this.jLabel44.setText("Add values on the right to match the keys.");
      GroupLayout jPanel5Layout = new GroupLayout(this.jPanel5);
      this.jPanel5.setLayout(jPanel5Layout);
      jPanel5Layout.setHorizontalGroup(jPanel5Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel5Layout.createSequentialGroup().addContainerGap().addComponent(this.jScrollPane1, -2, 325, -2).addGroup(jPanel5Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel5Layout.createSequentialGroup().addGap(72, 72, 72).addComponent(this.jLabel41).addPreferredGap(ComponentPlacement.RELATED, -1, 32767).addComponent(this.jLabel42).addGap(81, 81, 81)).addGroup(jPanel5Layout.createSequentialGroup().addGap(18, 18, 18).addGroup(jPanel5Layout.createParallelGroup(Alignment.LEADING).addComponent(this.jLabel44).addComponent(this.jLabel43)).addContainerGap(43, 32767)).addGroup(jPanel5Layout.createSequentialGroup().addGap(53, 53, 53).addGroup(jPanel5Layout.createParallelGroup(Alignment.LEADING, false).addComponent(this.csk4, Alignment.TRAILING).addComponent(this.csk5, Alignment.TRAILING).addComponent(this.csk6, Alignment.TRAILING).addComponent(this.csk1, -1, 80, 32767).addComponent(this.csk2).addComponent(this.csk3)).addPreferredGap(ComponentPlacement.RELATED, -1, 32767).addGroup(jPanel5Layout.createParallelGroup(Alignment.LEADING, false).addComponent(this.csv1, -2, 80, -2).addComponent(this.csv2, -1, 81, 32767).addComponent(this.csv3).addComponent(this.csv4).addComponent(this.csv5).addComponent(this.csv6)).addGap(0, 0, 32767)))));
      jPanel5Layout.setVerticalGroup(jPanel5Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel5Layout.createSequentialGroup().addContainerGap().addGroup(jPanel5Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel5Layout.createSequentialGroup().addComponent(this.jScrollPane1, -1, 346, 32767).addContainerGap()).addGroup(jPanel5Layout.createSequentialGroup().addGroup(jPanel5Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.jLabel41).addComponent(this.jLabel42)).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel5Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.csk1, -2, -1, -2).addComponent(this.csv1, -2, -1, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel5Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.csk2, -2, -1, -2).addComponent(this.csv2, -2, -1, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel5Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.csk3, -2, -1, -2).addComponent(this.csv3, -2, -1, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel5Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.csk4, -2, -1, -2).addComponent(this.csv4, -2, -1, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel5Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.csk5, -2, -1, -2).addComponent(this.csv5, -2, -1, -2)).addPreferredGap(ComponentPlacement.RELATED).addGroup(jPanel5Layout.createParallelGroup(Alignment.BASELINE).addComponent(this.csk6, -2, -1, -2).addComponent(this.csv6, -2, -1, -2)).addPreferredGap(ComponentPlacement.RELATED, -1, 32767).addComponent(this.jLabel43).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.jLabel44).addGap(45, 45, 45)))));
      this.jTabbedPane1.addTab("Clientscripts", this.jPanel5);
      this.jLabel2.setText("Currently Viewing Definitions of NPC: " + this.defs.getId() + " - " + this.defs.getName());
      this.jMenu1.setText("File");
      this.reloadButton.setText("Reload");
      this.reloadButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent evt) {
            NPCEditor.this.reloadButtonActionPerformed(evt);
         }
      });
      this.jMenu1.add(this.reloadButton);
      this.saveButton.setText("Save");
      this.saveButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent evt) {
            NPCEditor.this.saveButtonActionPerformed(evt);
         }
      });
      this.jMenu1.add(this.saveButton);
      this.addModelButton.setText("Add Model");
      this.addModelButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent evt) {
            NPCEditor.this.addModelButtonActionPerformed(evt);
         }
      });
      this.jMenu1.add(this.addModelButton);
      this.exportButton.setText("Export to .txt");
      this.exportButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent evt) {
            NPCEditor.this.exportButtonActionPerformed(evt);
         }
      });
      this.jMenu1.add(this.exportButton);
      this.exitButton.setText("Exit");
      this.exitButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent evt) {
            NPCEditor.this.exitButtonActionPerformed(evt);
         }
      });
      this.jMenu1.add(this.exitButton);
      this.jMenuBar1.add(this.jMenu1);
      this.setJMenuBar(this.jMenuBar1);
      GroupLayout layout = new GroupLayout(this.getContentPane());
      this.getContentPane().setLayout(layout);
      layout.setHorizontalGroup(layout.createParallelGroup(Alignment.LEADING).addComponent(this.jTabbedPane1).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(this.jLabel2).addContainerGap(-1, 32767)));
      layout.setVerticalGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, layout.createSequentialGroup().addGap(0, 16, 32767).addComponent(this.jLabel2).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(this.jTabbedPane1, -2, 396, -2)));
      this.pack();
   }

   private void reloadButtonActionPerformed(ActionEvent evt) {
      this.reload();
   }

   private void saveButtonActionPerformed(ActionEvent evt) {
      this.save();
   }

   private void addModelButtonActionPerformed(ActionEvent evt) {
      this.addModel();
   }

   private void exportButtonActionPerformed(ActionEvent evt) {
      this.export();
   }

   private void exitButtonActionPerformed(ActionEvent evt) {
      this.dispose();
   }

   public static void main(String[] args) {
      EventQueue.invokeLater(new Runnable() {
         public void run() {
            (new NPCEditor()).setVisible(true);
         }
      });
   }

   private void reload() {
      this.name.setText(this.defs.getName());
      this.combatLevel.setText("" + this.defs.getCombatLevel());
      this.visibleOnMap.setSelected(this.defs.isVisibleOnMap);
      this.npcHeight.setText("" + this.defs.npcHeight);
      this.npcWidth.setText("" + this.defs.npcWidth);
      this.walkMask.setText("" + this.defs.walkMask);
      this.respawnDirection.setText("" + this.defs.respawnDirection);
      this.renderEmote.setText("" + this.defs.renderEmote);
      this.modelIds.setText(this.getModelIds());
      this.jTextField2.setText(this.getChatHeads());
      this.optionsField.setText(this.getOpts());
      this.modelColorField.setText(this.getChangedModelColors());
      this.textureColorField.setText(this.getChangedTextureColors());
      this.unknownArray1.setText(this.getUnknownArray1());
      this.unknownArray2.setText(this.getUnknownArray2());
      this.unknownArray3.setText(this.getUnknownArray3());
      this.unknownArray4.setText(this.getUnknownArray4());
      this.unknownArray5.setText(this.getUnknownArray5());
      this.unknownBoolean1.setSelected(this.defs.unknownBoolean1);
      this.unknownBoolean2.setSelected(this.defs.unknownBoolean2);
      this.unknownBoolean3.setSelected(this.defs.unknownBoolean3);
      this.unknownBoolean5.setSelected(this.defs.unknownBoolean5);
      this.unknownBoolean4.setSelected(this.defs.unknownBoolean4);
      this.unknownBoolean6.setSelected(this.defs.unknownBoolean6);
      this.unknownBoolean7.setSelected(this.defs.unknownBoolean7);
      this.unknownInt1.setText("" + this.defs.unknownInt1);
      this.unknownInt2.setText("" + this.defs.unknownInt2);
      this.unknownInt3.setText("" + this.defs.unknownInt3);
      this.unknownInt4.setText("" + this.defs.unknownInt4);
      this.unknownInt5.setText("" + this.defs.unknownInt5);
      this.unknownInt6.setText("" + this.defs.unknownInt6);
      this.unknownInt7.setText("" + this.defs.unknownInt7);
      this.unknownInt8.setText("" + this.defs.unknownInt8);
      this.unknownInt9.setText("" + this.defs.unknownInt9);
      this.unknownInt10.setText("" + this.defs.unknownInt10);
      this.unknownInt11.setText("" + this.defs.unknownInt11);
      this.unknownInt12.setText("" + this.defs.unknownInt12);
      this.unknownInt13.setText("" + this.defs.unknownInt13);
      this.unknownInt14.setText("" + this.defs.unknownInt14);
      this.unknownInt15.setText("" + this.defs.unknownInt15);
      this.unknownInt16.setText("" + this.defs.unknownInt16);
      this.unknownInt17.setText("" + this.defs.unknownInt17);
      this.unknownInt18.setText("" + this.defs.unknownInt18);
      this.unknownInt19.setText("" + this.defs.unknownInt19);
      this.unknownInt20.setText("" + this.defs.unknownInt20);
      this.unknownInt21.setText("" + this.defs.unknownInt21);
      this.clientScriptOutput.setText(this.getClientScripts());
   }

   private void save() {
      try {
         this.defs.name = this.name.getText();
         this.defs.combatLevel = Integer.parseInt(this.combatLevel.getText());
         this.defs.isVisibleOnMap = this.visibleOnMap.isSelected();
         this.defs.npcHeight = Integer.parseInt(this.npcHeight.getText());
         this.defs.npcWidth = Integer.parseInt(this.npcWidth.getText());
         this.defs.walkMask = (byte)Integer.parseInt(this.walkMask.getText());
         this.defs.respawnDirection = (byte)Integer.parseInt(this.respawnDirection.getText());
         this.defs.renderEmote = Integer.parseInt(this.renderEmote.getText());
         String[] var15;
         int e1;
         if(!this.modelIds.getText().equals("")) {
            var15 = this.modelIds.getText().split(";");

            for(e1 = 0; e1 < this.defs.modelIds.length; ++e1) {
               this.defs.modelIds[e1] = Integer.parseInt(var15[e1]);
            }
         }

         if(!this.jTextField2.getText().equals("")) {
            var15 = this.jTextField2.getText().split(";");

            for(e1 = 0; e1 < this.defs.npcChatHeads.length; ++e1) {
               this.defs.npcChatHeads[e1] = Integer.parseInt(var15[e1]);
            }
         }

         var15 = this.optionsField.getText().split(";");

         for(e1 = 0; e1 < this.defs.options.length; ++e1) {
            this.defs.options[e1] = var15[e1].equals("null")?null:var15[e1];
         }

         this.defs.resetModelColors();
         String[] var17;
         String[] i;
         int len$;
         int i$;
         String t;
         String[] editedColor;
         if(!this.modelColorField.getText().equals("")) {
            var17 = this.modelColorField.getText().split(";");
            i = var17;
            len$ = var17.length;

            for(i$ = 0; i$ < len$; ++i$) {
               t = i[i$];
               editedColor = t.split("=");
               this.defs.changeModelColor(Integer.valueOf(editedColor[0]).intValue(), Integer.valueOf(editedColor[1]).intValue());
            }
         }

         this.defs.resetTextureColors();
         if(!this.textureColorField.getText().equals("")) {
            var17 = this.textureColorField.getText().split(";");
            i = var17;
            len$ = var17.length;

            for(i$ = 0; i$ < len$; ++i$) {
               t = i[i$];
               editedColor = t.split("=");
               this.defs.changeTextureColor(Integer.valueOf(editedColor[0]).intValue(), Integer.valueOf(editedColor[1]).intValue());
            }
         }

         int var16;
         if(!this.unknownArray1.getText().equals("")) {
            var17 = this.unknownArray1.getText().split(";");

            for(var16 = 0; var16 < this.defs.unknownArray1.length; ++var16) {
               this.defs.unknownArray1[var16] = (byte)Integer.parseInt(var17[var16]);
            }
         }

         if(!this.unknownArray2.getText().equals("")) {
            var17 = this.unknownArray2.getText().split(";");

            for(var16 = 0; var16 < this.defs.unknownArray2.length; ++var16) {
               this.defs.unknownArray2[var16] = Integer.parseInt(var17[var16]);
            }
         }

         if(!this.unknownArray3.getText().equals("")) {
            var17 = this.unknownArray3.getText().split(";");

            for(var16 = 0; var16 < this.defs.unknownArray3.length; ++var16) {
               this.defs.unknownArray3[0][var16] = Integer.parseInt(var17[var16]);
            }
         }

         if(!this.unknownArray4.getText().equals("")) {
            var17 = this.unknownArray4.getText().split(";");

            for(var16 = 0; var16 < this.defs.unknownArray4.length; ++var16) {
               this.defs.unknownArray4[var16] = Integer.parseInt(var17[var16]);
            }
         }

         if(!this.unknownArray5.getText().equals("")) {
            var17 = this.unknownArray5.getText().split(";");

            for(var16 = 0; var16 < this.defs.unknownArray5.length; ++var16) {
               this.defs.unknownArray5[var16] = Integer.parseInt(var17[var16]);
            }
         }

         this.defs.unknownBoolean1 = this.unknownBoolean1.isSelected();
         this.defs.unknownBoolean2 = this.unknownBoolean2.isSelected();
         this.defs.unknownBoolean3 = this.unknownBoolean3.isSelected();
         this.defs.unknownBoolean4 = this.unknownBoolean4.isSelected();
         this.defs.unknownBoolean5 = this.unknownBoolean5.isSelected();
         this.defs.unknownBoolean6 = this.unknownBoolean6.isSelected();
         this.defs.unknownBoolean7 = this.unknownBoolean7.isSelected();
         if(!this.unknownInt1.getText().equals("")) {
            this.defs.unknownInt1 = Integer.parseInt(this.unknownInt1.getText());
         }

         if(!this.unknownInt2.getText().equals("")) {
            this.defs.unknownInt2 = Integer.parseInt(this.unknownInt2.getText());
         }

         if(!this.unknownInt3.getText().equals("")) {
            this.defs.unknownInt3 = Integer.parseInt(this.unknownInt3.getText());
         }

         if(!this.unknownInt4.getText().equals("")) {
            this.defs.unknownInt4 = Integer.parseInt(this.unknownInt4.getText());
         }

         if(!this.unknownInt5.getText().equals("")) {
            this.defs.unknownInt5 = Integer.parseInt(this.unknownInt5.getText());
         }

         if(!this.unknownInt6.getText().equals("")) {
            this.defs.unknownInt6 = Integer.parseInt(this.unknownInt6.getText());
         }

         if(!this.unknownInt7.getText().equals("")) {
            this.defs.unknownInt7 = Integer.parseInt(this.unknownInt7.getText());
         }

         if(!this.unknownInt8.getText().equals("")) {
            this.defs.unknownInt8 = Integer.parseInt(this.unknownInt8.getText());
         }

         if(!this.unknownInt9.getText().equals("")) {
            this.defs.unknownInt9 = Integer.parseInt(this.unknownInt9.getText());
         }

         if(!this.unknownInt10.getText().equals("")) {
            this.defs.unknownInt10 = Integer.parseInt(this.unknownInt10.getText());
         }

         if(!this.unknownInt11.getText().equals("")) {
            this.defs.unknownInt11 = Integer.parseInt(this.unknownInt11.getText());
         }

         if(!this.unknownInt12.getText().equals("")) {
            this.defs.unknownInt12 = Integer.parseInt(this.unknownInt12.getText());
         }

         if(!this.unknownInt13.getText().equals("")) {
            this.defs.unknownInt13 = Integer.parseInt(this.unknownInt13.getText());
         }

         if(!this.unknownInt14.getText().equals("")) {
            this.defs.unknownInt14 = Integer.parseInt(this.unknownInt14.getText());
         }

         if(!this.unknownInt15.getText().equals("")) {
            this.defs.unknownInt15 = Integer.parseInt(this.unknownInt15.getText());
         }

         if(!this.unknownInt16.getText().equals("")) {
            this.defs.unknownInt16 = Integer.parseInt(this.unknownInt16.getText());
         }

         if(!this.unknownInt17.getText().equals("")) {
            this.defs.unknownInt17 = Integer.parseInt(this.unknownInt17.getText());
         }

         if(!this.unknownInt18.getText().equals("")) {
            this.defs.unknownInt18 = Integer.parseInt(this.unknownInt18.getText());
         }

         if(!this.unknownInt19.getText().equals("")) {
            this.defs.unknownInt19 = Integer.parseInt(this.unknownInt19.getText());
         }

         if(!this.unknownInt20.getText().equals("")) {
            this.defs.unknownInt20 = Integer.parseInt(this.unknownInt20.getText());
         }

         if(!this.unknownInt21.getText().equals("")) {
            this.defs.unknownInt21 = Integer.parseInt(this.unknownInt21.getText());
         }

         try {
            if(!this.csk1.getText().equals("") && !this.csv1.getText().equals("")) {
               try {
                  this.defs.clientScriptData.remove(this.csk1);
                  this.defs.clientScriptData.put(Integer.valueOf(Integer.parseInt(this.csk1.getText())), Integer.valueOf(Integer.parseInt(this.csv1.getText())));
               } catch (Exception var161) {
                  this.defs.clientScriptData.remove(this.csk1);
                  this.defs.clientScriptData.put(Integer.valueOf(Integer.parseInt(this.csk1.getText())), this.csv1.getText().toString());
               }
            }

            if(!this.csk2.getText().equals("") && !this.csv2.getText().equals("")) {
               try {
                  this.defs.clientScriptData.remove(this.csk2);
                  this.defs.clientScriptData.put(Integer.valueOf(Integer.parseInt(this.csk2.getText())), Integer.valueOf(Integer.parseInt(this.csv2.getText())));
               } catch (Exception var151) {
                  this.defs.clientScriptData.remove(this.csk2);
                  this.defs.clientScriptData.put(Integer.valueOf(Integer.parseInt(this.csk2.getText())), this.csv2.getText().toString());
               }
            }

            if(!this.csk3.getText().equals("") && !this.csv3.getText().equals("")) {
               try {
                  this.defs.clientScriptData.remove(this.csk3);
                  this.defs.clientScriptData.put(Integer.valueOf(Integer.parseInt(this.csk3.getText())), Integer.valueOf(Integer.parseInt(this.csv3.getText())));
               } catch (Exception var14) {
                  this.defs.clientScriptData.remove(this.csk3);
                  this.defs.clientScriptData.put(Integer.valueOf(Integer.parseInt(this.csk3.getText())), this.csv3.getText().toString());
               }
            }

            if(!this.csk4.getText().equals("") && !this.csv4.getText().equals("")) {
               try {
                  this.defs.clientScriptData.remove(this.csk4);
                  this.defs.clientScriptData.put(Integer.valueOf(Integer.parseInt(this.csk4.getText())), Integer.valueOf(Integer.parseInt(this.csv4.getText())));
               } catch (Exception var13) {
                  this.defs.clientScriptData.remove(this.csk4);
                  this.defs.clientScriptData.put(Integer.valueOf(Integer.parseInt(this.csk4.getText())), this.csv4.getText().toString());
               }
            }

            if(!this.csk5.getText().equals("") && !this.csv5.getText().equals("")) {
               try {
                  this.defs.clientScriptData.remove(this.csk5);
                  this.defs.clientScriptData.put(Integer.valueOf(Integer.parseInt(this.csk5.getText())), Integer.valueOf(Integer.parseInt(this.csv5.getText())));
               } catch (Exception var12) {
                  this.defs.clientScriptData.remove(this.csk5);
                  this.defs.clientScriptData.put(Integer.valueOf(Integer.parseInt(this.csk5.getText())), this.csv5.getText().toString());
               }
            }

            if(!this.csk6.getText().equals("") && !this.csv6.getText().equals("")) {
               try {
                  this.defs.clientScriptData.remove(this.csk6);
                  this.defs.clientScriptData.put(Integer.valueOf(Integer.parseInt(this.csk6.getText())), Integer.valueOf(Integer.parseInt(this.csv6.getText())));
               } catch (Exception var11) {
                  this.defs.clientScriptData.remove(this.csk6);
                  this.defs.clientScriptData.put(Integer.valueOf(Integer.parseInt(this.csk6.getText())), this.csv6.getText().toString());
               }
            }
         } catch (Exception var171) {
            this.defs.clientScriptData = new HashMap(1);
         }

         NPCSelection var10001 = this.ns;
         this.defs.write(NPCSelection.STORE);
         this.ns.updateNPCDefs(this.defs);
      } catch (Exception var18) {
         Main.log("NPCEditor", "Cannot save. Please check for mistypes.");
         System.out.println(var18);
      }

   }

   private void addModel() {
      JFrame frame = new JFrame();
      int result = JOptionPane.showConfirmDialog(frame, "Do you want to specify a model ID?");
      StringBuilder var10001;
      NPCSelection var10002;
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
                  var10002 = this.ns;
                  Main.log("NPCEditor", var10001.append(Utils.packCustomModel(NPCSelection.STORE, Utils.getBytesFromFile(new File(file1.getPath().toString())), Integer.parseInt(returnVal1.toString()))).toString());
               } catch (IOException var12) {
                  Main.log("NPCEditor", "There was an error packing the model.");
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
               var10002 = this.ns;
               Main.log("NPCEditor", var10001.append(Utils.packCustomModel(NPCSelection.STORE, Utils.getBytesFromFile(new File(file21.getPath().toString())))).toString());
            } catch (IOException var11) {
               Main.log("NPCEditor", "There was an error packing the model.");
            }
         }
      }

   }

   private void export() {
      File f = new File(System.getProperty("user.home") + "/FCE/npcs/");
      f.mkdirs();
      String lineSep = System.getProperty("line.separator");
      BufferedWriter writer = null;

      try {
         writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(System.getProperty("user.home") + "/FCE/npcs/" + this.defs.id + ".txt"), "utf-8"));
         writer.write("name = " + this.defs.getName());
         writer.write(lineSep);
         writer.write("combat level = " + this.defs.getCombatLevel());
         writer.write(lineSep);
         writer.write("isVisibleOnMap = " + this.defs.isVisibleOnMap);
         writer.write(lineSep);
         writer.write("height = " + this.defs.npcHeight);
         writer.write(lineSep);
         writer.write("width = " + this.defs.npcWidth);
         writer.write(lineSep);
         writer.write("walk mask = " + this.defs.walkMask);
         writer.write(lineSep);
         writer.write("respawn direction = " + this.defs.respawnDirection);
         writer.write(lineSep);
         writer.write("render emote = " + this.defs.renderEmote);
         writer.write(lineSep);
         writer.write("model ids = " + this.getModelIds());
         writer.write(lineSep);
         writer.write("chat head model ids = " + this.getChatHeads());
         writer.write(lineSep);
         writer.write("options = " + this.getOpts());
         writer.write(lineSep);
         writer.write("model colors = " + this.getChangedModelColors());
         writer.write(lineSep);
         writer.write("texture colors = " + this.getChangedTextureColors());
         writer.write(lineSep);
         writer.write("unknown array1 = " + this.getUnknownArray1());
         writer.write(lineSep);
         writer.write("unknown array2 = " + this.getUnknownArray2());
         writer.write(lineSep);
         writer.write("unknown array3 = " + this.getUnknownArray3());
         writer.write(lineSep);
         writer.write("unknown array4 = " + this.getUnknownArray4());
         writer.write(lineSep);
         writer.write("unknown array5 = " + this.getUnknownArray5());
         writer.write(lineSep);
         writer.write("unknownBoolean1 = " + this.defs.unknownBoolean1);
         writer.write(lineSep);
         writer.write("unknwonBoolean2 = " + this.defs.unknownBoolean2);
         writer.write(lineSep);
         writer.write("unknownBoolean3 = " + this.defs.unknownBoolean3);
         writer.write(lineSep);
         writer.write("unknownBoolean4 = " + this.defs.unknownBoolean5);
         writer.write(lineSep);
         writer.write("unknownBoolean5 = " + this.defs.unknownBoolean4);
         writer.write(lineSep);
         writer.write("unknownBoolean6 = " + this.defs.unknownBoolean6);
         writer.write(lineSep);
         writer.write("unknownBoolean7 = " + this.defs.unknownBoolean7);
         writer.write(lineSep);
         writer.write("unknown int1 = " + this.defs.unknownInt1);
         writer.write(lineSep);
         writer.write("unknown int2 = " + this.defs.unknownInt2);
         writer.write(lineSep);
         writer.write("unknown int3 = " + this.defs.unknownInt3);
         writer.write(lineSep);
         writer.write("unknown int4 = " + this.defs.unknownInt4);
         writer.write(lineSep);
         writer.write("unknown int5 = " + this.defs.unknownInt5);
         writer.write(lineSep);
         writer.write("unknown int6 = " + this.defs.unknownInt6);
         writer.write(lineSep);
         writer.write("unknown int7 = " + this.defs.unknownInt7);
         writer.write(lineSep);
         writer.write("unknown int8 = " + this.defs.unknownInt8);
         writer.write(lineSep);
         writer.write("unknown int9 = " + this.defs.unknownInt9);
         writer.write(lineSep);
         writer.write("unknown int10 = " + this.defs.unknownInt10);
         writer.write(lineSep);
         writer.write("unknown int11 = " + this.defs.unknownInt11);
         writer.write(lineSep);
         writer.write("unknown int12 = " + this.defs.unknownInt12);
         writer.write(lineSep);
         writer.write("unknown int13 = " + this.defs.unknownInt13);
         writer.write(lineSep);
         writer.write("unknown int14 = " + this.defs.unknownInt14);
         writer.write(lineSep);
         writer.write("unknown int15 = " + this.defs.unknownInt15);
         writer.write(lineSep);
         writer.write("unknown int16 = " + this.defs.unknownInt16);
         writer.write(lineSep);
         writer.write("unknown int17 = " + this.defs.unknownInt17);
         writer.write(lineSep);
         writer.write("unknown int18 = " + this.defs.unknownInt18);
         writer.write(lineSep);
         writer.write("unknown int19 = " + this.defs.unknownInt19);
         writer.write(lineSep);
         writer.write("unknown int20 = " + this.defs.unknownInt20);
         writer.write(lineSep);
         writer.write("unknown int21 = " + this.defs.unknownInt21);
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
         Main.log("NPCEditor", "Failed to export NPC Defs to .txt");
      } finally {
         try {
            writer.close();
         } catch (Exception var14) {
            ;
         }

      }

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

   public String getModelIds() {
      String text = "";

      try {
         int[] e = this.defs.modelIds;
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

   public String getOpts() {
      String text = "";
      String[] arr$ = this.defs.getOptions();
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

   public String getChatHeads() {
      String text = "";

      try {
         int[] e = this.defs.npcChatHeads;
         int len$ = e.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            int id = e[i$];
            text = text + id + ";";
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
         int[] e = this.defs.unknownArray3[0];
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
}
