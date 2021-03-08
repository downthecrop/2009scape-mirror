package com.alex.tools.itemsDefsEditor;

import com.alex.loaders.items.ItemDefinitions;
import com.alex.tools.itemsDefsEditor.Application;

import javax.swing.border.EmptyBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ItemDefsEditor extends JDialog {
   private static final long serialVersionUID = 779623426244652906L;
   private final JPanel contentPanel = new JPanel();
   private ItemDefinitions defs;
   private Application application;
   private JTextField modelIDField;
   private JTextField nameField;
   private JTextField modelZoomField;
   private JTextField groundOptionsField;
   private JTextField inventoryOptionsField;
   private JTextField femaleModelId2Field;
   private JTextField maleModelId1Field;
   private JTextField maleModelId2Field;
   private JTextField maleModelId3Field;
   private JTextField femaleModelId1Field;
   private JTextField femaleModelId3Field;
   private JTextField teamIdField;
   private JTextField notedItemIdField;
   private JTextField switchNotedItemField;
   private JTextField lendedItemIdField;
   private JTextField switchLendedItemField;
   private JTextField changedModelColorsField;
   private JTextField changedTextureColorsField;
   private JCheckBox membersOnlyCheck;
   private JTextField price;

   public void save() {
      this.defs.setInvModelId(Integer.valueOf(this.modelIDField.getText()).intValue());
      this.defs.setName(this.nameField.getText());
      this.defs.setInvModelZoom(Integer.valueOf(this.modelZoomField.getText()).intValue());
      String[] groundOptions = this.groundOptionsField.getText().split(";");

      for(int var9 = 0; var9 < this.defs.getGroundOptions().length; ++var9) {
         this.defs.getGroundOptions()[var9] = groundOptions[var9].equals("null")?null:groundOptions[var9];
      }

      String[] var91 = this.inventoryOptionsField.getText().split(";");

      for(int t = 0; t < this.defs.getInventoryOptions().length; ++t) {
         this.defs.getInventoryOptions()[t] = var91[t].equals("null")?null:var91[t];
      }

      this.defs.maleEquip1 = Integer.valueOf(this.maleModelId1Field.getText()).intValue();
      this.defs.maleEquip2 = Integer.valueOf(this.maleModelId2Field.getText()).intValue();
      this.defs.maleEquipModelId3 = Integer.valueOf(this.maleModelId3Field.getText()).intValue();
      this.defs.femaleEquip1 = Integer.valueOf(this.femaleModelId1Field.getText()).intValue();
      this.defs.femaleEquip2 = Integer.valueOf(this.femaleModelId2Field.getText()).intValue();
      this.defs.femaleEquipModelId3 = Integer.valueOf(this.femaleModelId3Field.getText()).intValue();
      this.defs.teamId = Integer.valueOf(this.teamIdField.getText()).intValue();
      this.defs.notedItemId = Integer.valueOf(this.notedItemIdField.getText()).intValue();
      this.defs.switchNoteItemId = Integer.valueOf(this.switchNotedItemField.getText()).intValue();
      this.defs.lendedItemId = Integer.valueOf(this.lendedItemIdField.getText()).intValue();
      this.defs.switchLendItemId = Integer.valueOf(this.switchLendedItemField.getText()).intValue();
      this.defs.resetModelColors();
      int var5;
      int var6;
      String[] var7;
      String[] editedColor;
      String[] var10;
      String var101;
      if(!this.changedModelColorsField.getText().equals("")) {
         var10 = this.changedModelColorsField.getText().split(";");
         var7 = var10;
         var6 = var10.length;

         for(var5 = 0; var5 < var6; ++var5) {
            var101 = var7[var5];
            editedColor = var101.split("=");
            this.defs.changeModelColor(Integer.valueOf(editedColor[0]).intValue(), Integer.valueOf(editedColor[1]).intValue());
         }
      }

      this.defs.resetTextureColors();
      if(!this.changedTextureColorsField.getText().equals("")) {
         var10 = this.changedTextureColorsField.getText().split(";");
         var7 = var10;
         var6 = var10.length;

         for(var5 = 0; var5 < var6; ++var5) {
            var101 = var7[var5];
            editedColor = var101.split("=");
            this.defs.changeTextureColor(Short.valueOf(editedColor[0]).shortValue(), Short.valueOf(editedColor[1]).shortValue());
         }
      }

      this.defs.membersOnly = this.membersOnlyCheck.isSelected();
      this.defs.value = Integer.valueOf(this.price.getText()).intValue();
      this.defs.equipType = Integer.valueOf(this.modelIDField.getText()).intValue();
      this.defs.equipSlot = Integer.valueOf(this.modelIDField.getText()).intValue();
      this.defs.write(Application.STORE);
      this.application.updateItemDefs(this.defs);
   }

   public ItemDefsEditor(Application application, ItemDefinitions defs) {
      super(application.getFrame(), "Item Definitions Editor", true);
      this.defs = defs;
      this.application = application;
      this.setBounds(100, 100, 912, 354);
      this.getContentPane().setLayout(new BorderLayout());
      this.contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
      this.getContentPane().add(this.contentPanel, "Center");
      this.contentPanel.setLayout((LayoutManager)null);
      JLabel lblNewLabel = new JLabel("Model ID:");
      lblNewLabel.setFont(new Font("Comic Sans MS", 0, 14));
      lblNewLabel.setBounds(6, 43, 81, 21);
      this.contentPanel.add(lblNewLabel);
      this.modelIDField = new JTextField();
      this.modelIDField.setBounds(139, 40, 122, 28);
      this.contentPanel.add(this.modelIDField);
      this.modelIDField.setColumns(10);
      this.modelIDField.setText("" + defs.getInvModelId());
      JLabel label = new JLabel("Name:");
      label.setFont(new Font("Comic Sans MS", 0, 14));
      label.setBounds(6, 76, 81, 21);
      this.contentPanel.add(label);
      this.nameField = new JTextField();
      this.nameField.setBounds(139, 73, 122, 28);
      this.contentPanel.add(this.nameField);
      this.nameField.setColumns(10);
      this.nameField.setText(defs.getName());
      label = new JLabel("Model Zoom:");
      label.setFont(new Font("Comic Sans MS", 0, 14));
      label.setBounds(6, 109, 95, 21);
      this.contentPanel.add(label);
      this.modelZoomField = new JTextField();
      this.modelZoomField.setBounds(139, 106, 122, 28);
      this.contentPanel.add(this.modelZoomField);
      this.modelZoomField.setColumns(10);
      this.modelZoomField.setText("" + defs.getInvModelZoom());
      label = new JLabel("Ground Options:");
      label.setFont(new Font("Comic Sans MS", 0, 14));
      label.setBounds(6, 142, 108, 21);
      this.contentPanel.add(label);
      this.groundOptionsField = new JTextField();
      this.groundOptionsField.setBounds(139, 139, 122, 28);
      this.contentPanel.add(this.groundOptionsField);
      this.groundOptionsField.setColumns(10);
      String var14 = "";
      String[] label_1 = defs.getGroundOptions();
      int label_2 = label_1.length;

      int label_3;
      String label_4;
      for(label_3 = 0; label_3 < label_2; ++label_3) {
         label_4 = label_1[label_3];
         var14 = var14 + (label_4 == null?"null":label_4) + ";";
      }

      this.groundOptionsField.setText(var14);
      label = new JLabel("Inventory Options:");
      label.setFont(new Font("Comic Sans MS", 0, 14));
      label.setBounds(6, 175, 139, 21);
      this.contentPanel.add(label);
      this.inventoryOptionsField = new JTextField();
      this.inventoryOptionsField.setBounds(139, 172, 122, 28);
      this.contentPanel.add(this.inventoryOptionsField);
      this.inventoryOptionsField.setColumns(10);
      var14 = "";
      label_1 = defs.getInventoryOptions();
      label_2 = label_1.length;

      for(label_3 = 0; label_3 < label_2; ++label_3) {
         label_4 = label_1[label_3];
         var14 = var14 + (label_4 == null?"null":label_4) + ";";
      }

      this.inventoryOptionsField.setText(var14);
      JButton var16 = new JButton("Save");
      var16.setBounds(6, 265, 55, 28);
      this.contentPanel.add(var16);
      var16.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            ItemDefsEditor.this.save();
            ItemDefsEditor.this.dispose();
         }
      });
      this.getRootPane().setDefaultButton(var16);
      var16 = new JButton("Cancel");
      var16.setBounds(73, 265, 67, 28);
      this.contentPanel.add(var16);
      var16.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            ItemDefsEditor.this.dispose();
         }
      });
      var16.setActionCommand("Cancel");
      label = new JLabel("Interface / Droped");
      label.setFont(new Font("Comic Sans MS", 0, 18));
      label.setBounds(6, 6, 205, 21);
      this.contentPanel.add(label);
      JLabel var15 = new JLabel("Wearing");
      var15.setFont(new Font("Comic Sans MS", 0, 18));
      var15.setBounds(273, 6, 205, 21);
      this.contentPanel.add(var15);
      JLabel var17 = new JLabel("Male Model ID 1:");
      var17.setFont(new Font("Comic Sans MS", 0, 14));
      var17.setBounds(273, 43, 131, 21);
      this.contentPanel.add(var17);
      JLabel var18 = new JLabel("Male Model ID 2:");
      var18.setFont(new Font("Comic Sans MS", 0, 14));
      var18.setBounds(273, 76, 131, 21);
      this.contentPanel.add(var18);
      JLabel var19 = new JLabel("Male Model ID 3:");
      var19.setFont(new Font("Comic Sans MS", 0, 14));
      var19.setBounds(273, 112, 131, 21);
      this.contentPanel.add(var19);
      JLabel label_5 = new JLabel("Female Model ID 1:");
      label_5.setFont(new Font("Comic Sans MS", 0, 14));
      label_5.setBounds(273, 145, 131, 21);
      this.contentPanel.add(label_5);
      JLabel label_6 = new JLabel("Female Model ID 2:");
      label_6.setFont(new Font("Comic Sans MS", 0, 14));
      label_6.setBounds(273, 175, 131, 21);
      this.contentPanel.add(label_6);
      JLabel label_7 = new JLabel("Female Model ID 3:");
      label_7.setFont(new Font("Comic Sans MS", 0, 14));
      label_7.setBounds(273, 208, 131, 21);
      this.contentPanel.add(label_7);
      this.femaleModelId2Field = new JTextField();
      this.femaleModelId2Field.setBounds(411, 172, 122, 28);
      this.contentPanel.add(this.femaleModelId2Field);
      this.femaleModelId2Field.setColumns(10);
      this.femaleModelId2Field.setText("" + defs.femaleEquip2);
      this.maleModelId1Field = new JTextField();
      this.maleModelId1Field.setBounds(411, 40, 122, 28);
      this.contentPanel.add(this.maleModelId1Field);
      this.maleModelId1Field.setColumns(10);
      this.maleModelId1Field.setText("" + defs.maleEquip1);
      this.maleModelId2Field = new JTextField();
      this.maleModelId2Field.setBounds(411, 73, 122, 28);
      this.contentPanel.add(this.maleModelId2Field);
      this.maleModelId2Field.setColumns(10);
      this.maleModelId2Field.setText("" + defs.maleEquip2);
      this.maleModelId3Field = new JTextField();
      this.maleModelId3Field.setBounds(411, 106, 122, 28);
      this.contentPanel.add(this.maleModelId3Field);
      this.maleModelId3Field.setColumns(10);
      this.maleModelId3Field.setText("" + defs.maleEquipModelId3);
      this.femaleModelId1Field = new JTextField();
      this.femaleModelId1Field.setBounds(411, 139, 122, 28);
      this.contentPanel.add(this.femaleModelId1Field);
      this.femaleModelId1Field.setColumns(10);
      this.femaleModelId1Field.setText("" + defs.femaleEquip1);
      this.femaleModelId3Field = new JTextField();
      this.femaleModelId3Field.setBounds(411, 205, 122, 28);
      this.contentPanel.add(this.femaleModelId3Field);
      this.femaleModelId3Field.setColumns(10);
      this.femaleModelId3Field.setText("" + defs.femaleEquipModelId3);
      JLabel buttonPane = new JLabel("Team ID:");
      buttonPane.setFont(new Font("Comic Sans MS", 0, 14));
      buttonPane.setBounds(273, 241, 131, 21);
      this.contentPanel.add(buttonPane);
      this.teamIdField = new JTextField();
      this.teamIdField.setBounds(411, 238, 122, 28);
      this.contentPanel.add(this.teamIdField);
      this.teamIdField.setColumns(10);
      this.teamIdField.setText("" + defs.teamId);
      buttonPane = new JLabel("Others");
      buttonPane.setFont(new Font("Comic Sans MS", 0, 18));
      buttonPane.setBounds(539, 6, 205, 21);
      this.contentPanel.add(buttonPane);
      buttonPane = new JLabel("Noted Item ID:");
      buttonPane.setFont(new Font("Comic Sans MS", 0, 14));
      buttonPane.setBounds(545, 43, 131, 21);
      this.contentPanel.add(buttonPane);
      buttonPane = new JLabel("Switch Noted Item Id:");
      buttonPane.setFont(new Font("Comic Sans MS", 0, 14));
      buttonPane.setBounds(545, 76, 160, 21);
      this.contentPanel.add(buttonPane);
      buttonPane = new JLabel("Lended Item ID:");
      buttonPane.setFont(new Font("Comic Sans MS", 0, 14));
      buttonPane.setBounds(545, 109, 160, 21);
      this.contentPanel.add(buttonPane);
      buttonPane = new JLabel("Switch Lended Item Id:");
      buttonPane.setFont(new Font("Comic Sans MS", 0, 14));
      buttonPane.setBounds(545, 145, 160, 21);
      this.contentPanel.add(buttonPane);
      this.notedItemIdField = new JTextField();
      this.notedItemIdField.setBounds(707, 39, 122, 28);
      this.contentPanel.add(this.notedItemIdField);
      this.notedItemIdField.setColumns(10);
      this.notedItemIdField.setText("" + defs.notedItemId);
      this.switchNotedItemField = new JTextField();
      this.switchNotedItemField.setBounds(707, 73, 122, 28);
      this.contentPanel.add(this.switchNotedItemField);
      this.switchNotedItemField.setColumns(10);
      this.switchNotedItemField.setText("" + defs.switchNoteItemId);
      this.lendedItemIdField = new JTextField();
      this.lendedItemIdField.setBounds(707, 106, 122, 28);
      this.contentPanel.add(this.lendedItemIdField);
      this.lendedItemIdField.setColumns(10);
      this.lendedItemIdField.setText("" + defs.lendedItemId);
      this.switchLendedItemField = new JTextField();
      this.switchLendedItemField.setBounds(707, 139, 122, 28);
      this.contentPanel.add(this.switchLendedItemField);
      this.switchLendedItemField.setColumns(10);
      this.switchLendedItemField.setText("" + defs.switchLendItemId);
      buttonPane = new JLabel("Changed Model Colors:");
      buttonPane.setFont(new Font("Comic Sans MS", 0, 14));
      buttonPane.setBounds(545, 175, 160, 21);
      this.contentPanel.add(buttonPane);
      this.changedModelColorsField = new JTextField();
      this.changedModelColorsField.setBounds(707, 172, 122, 28);
      this.contentPanel.add(this.changedModelColorsField);
      this.changedModelColorsField.setColumns(10);
      String var20 = "";
      int i;
      if(defs.originalModelColors != null) {
         for(i = 0; i < defs.originalModelColors.length; ++i) {
            var20 = var20 + defs.originalModelColors[i] + "=" + defs.modifiedModelColors[i] + ";";
         }
      }

      this.changedModelColorsField.setText(var20);
      buttonPane = new JLabel("Changed Texture Colors:");
      buttonPane.setFont(new Font("Comic Sans MS", 0, 14));
      buttonPane.setBounds(545, 205, 160, 21);
      this.contentPanel.add(buttonPane);
      this.changedTextureColorsField = new JTextField();
      this.changedTextureColorsField.setBounds(707, 205, 122, 28);
      this.contentPanel.add(this.changedTextureColorsField);
      this.changedTextureColorsField.setColumns(10);
      var20 = "";
      if(defs.originalTextureColors != null) {
         for(i = 0; i < defs.originalTextureColors.length; ++i) {
            var20 = var20 + defs.originalTextureColors[i] + "=" + defs.modifiedTextureColors[i] + ";";
         }
      }

      this.changedTextureColorsField.setText(var20);
      this.membersOnlyCheck = new JCheckBox("Members Only");
      this.membersOnlyCheck.setFont(new Font("Comic Sans MS", 0, 14));
      this.membersOnlyCheck.setBounds(545, 243, 131, 18);
      this.membersOnlyCheck.setSelected(defs.membersOnly);
      this.contentPanel.add(this.membersOnlyCheck);
      JPanel var21 = new JPanel();
      var21.setLayout(new FlowLayout(2));
      this.getContentPane().add(var21, "South");
      this.setDefaultCloseOperation(2);
      this.setVisible(true);
   }
}
