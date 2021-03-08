package com.alex.tools.itemsDefsEditor;

import com.alex.loaders.items.ItemDefinitions;
import com.alex.store.Store;
import com.alex.tools.itemsDefsEditor.GeneratedUkeys;
import com.alex.tools.itemsDefsEditor.ItemDefsEditor;
import com.alex.utils.Utils;

import javax.swing.UIManager.LookAndFeelInfo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class Application {
   public static Store STORE;
   private JFrame frmCacheEditorV;
   private JList itemsList;
   private DefaultListModel itemsListmodel;

   public static void main(String[] args) throws IOException {
      STORE = new Store("cache/", false);
      EventQueue.invokeLater(new Runnable() {
         public void run() {
            try {
               Application var2 = new Application();
               var2.frmCacheEditorV.setVisible(true);
            } catch (Exception var21) {
               var21.printStackTrace();
            }

         }
      });
   }

   public Application() {
      this.initialize();
   }

   private void setLook() {
      boolean found = false;
      LookAndFeelInfo[] e = UIManager.getInstalledLookAndFeels();
      int len$ = e.length;

      for(int var7 = 0; var7 < len$; ++var7) {
         LookAndFeelInfo info = e[var7];
         if(info.getName().equals("Nimbus")) {
            try {
               UIManager.setLookAndFeel(info.getClassName());
               found = true;
            } catch (Exception var8) {
               var8.printStackTrace();
            }
         }
      }

      if(!found) {
         try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
         } catch (Exception var71) {
            var71.printStackTrace();
         }
      }

   }

   private void initialize() {
      this.setLook();
      this.frmCacheEditorV = new JFrame();
      this.frmCacheEditorV.setTitle("Cache Editor V0.1");
      this.frmCacheEditorV.setBounds(100, 100, 352, 435);
      this.frmCacheEditorV.setDefaultCloseOperation(3);
      JTabbedPane tabbedPane = new JTabbedPane(1);
      this.frmCacheEditorV.getContentPane().add(tabbedPane, "Center");
      JPanel panel = new JPanel();
      tabbedPane.addTab("Main", (Icon)null, panel, (String)null);
      panel.setLayout((LayoutManager)null);
      JButton btnGenerateUkeys = new JButton("Generate Ukeys (614- Client Builts)");
      btnGenerateUkeys.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            byte[] ukeys = Utils.getArchivePacketData(255, 255, Application.STORE.generateIndex255Archive255Outdated());
            new GeneratedUkeys(Application.this.getFrame(), ukeys);
         }
      });
      btnGenerateUkeys.setBounds(33, 64, 257, 28);
      panel.add(btnGenerateUkeys);
      JLabel lblCreatedByAlexalso = new JLabel("Created By Alex(Also named Dragonkk)");
      lblCreatedByAlexalso.setFont(new Font("Tekton Pro Ext", 0, 15));
      lblCreatedByAlexalso.setBounds(6, 290, 322, 46);
      panel.add(lblCreatedByAlexalso);
      JPanel panel_1 = new JPanel();
      tabbedPane.addTab("Items", (Icon)null, panel_1, (String)null);
      panel_1.setLayout((LayoutManager)null);
      this.itemsListmodel = new DefaultListModel();
      this.itemsList = new JList(this.itemsListmodel);
      this.itemsList.setSelectionMode(1);
      this.itemsList.setLayoutOrientation(0);
      this.itemsList.setVisibleRowCount(-1);
      JScrollPane itemListscrollPane = new JScrollPane(this.itemsList);
      itemListscrollPane.setBounds(34, 49, 155, 254);
      panel_1.add(itemListscrollPane);
      JButton btnEdit = new JButton("Edit");
      btnEdit.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            ItemDefinitions defs = (ItemDefinitions)Application.this.itemsList.getSelectedValue();
            if(defs != null) {
               new ItemDefsEditor(Application.this, defs);
            }

         }
      });
      btnEdit.setBounds(201, 48, 90, 28);
      panel_1.add(btnEdit);
      JButton btnAdd = new JButton("Add");
      btnAdd.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            new ItemDefsEditor(Application.this, new ItemDefinitions(Application.STORE, Utils.getItemDefinitionsSize(Application.STORE), false));
         }
      });
      btnAdd.setBounds(201, 88, 90, 28);
      panel_1.add(btnAdd);
      JButton btnRemove = new JButton("Remove");
      btnRemove.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            ItemDefinitions defs = (ItemDefinitions)Application.this.itemsList.getSelectedValue();
            if(defs != null) {
               Application.STORE.getIndexes()[19].removeFile(defs.getArchiveId(), defs.getFileId());
               Application.this.removeItemDefs(defs);
            }

         }
      });
      btnRemove.setBounds(201, 128, 90, 28);
      panel_1.add(btnRemove);
      JLabel label = new JLabel("Cached Items:");
      label.setFont(new Font("Comic Sans MS", 0, 18));
      label.setBounds(34, 18, 155, 21);
      panel_1.add(label);
      JButton btnDuplicate = new JButton("Clone");
      btnDuplicate.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            ItemDefinitions defs = (ItemDefinitions)Application.this.itemsList.getSelectedValue();
            if(defs != null) {
               defs = (ItemDefinitions)defs.clone();
               if(defs != null) {
                  defs.id = Utils.getItemDefinitionsSize(Application.STORE);
                  new ItemDefsEditor(Application.this, defs);
               }
            }

         }
      });
      btnDuplicate.setBounds(201, 168, 90, 28);
      panel_1.add(btnDuplicate);
      this.addAllItems();
   }

   public void addAllItems() {
      for(int id = 0; id < Utils.getItemDefinitionsSize(STORE) - 22314; ++id) {
         this.addItemDefs(ItemDefinitions.getItemDefinition(STORE, id));
      }

   }

   public void addItemDefs(final ItemDefinitions defs) {
      EventQueue.invokeLater(new Runnable() {
         public void run() {
            Application.this.itemsListmodel.addElement(defs);
         }
      });
   }

   public void updateItemDefs(final ItemDefinitions defs) {
      EventQueue.invokeLater(new Runnable() {
         public void run() {
            int index = Application.this.itemsListmodel.indexOf(defs);
            if(index == -1) {
               Application.this.itemsListmodel.addElement(defs);
            } else {
               Application.this.itemsListmodel.setElementAt(defs, index);
            }

         }
      });
   }

   public void removeItemDefs(final ItemDefinitions defs) {
      EventQueue.invokeLater(new Runnable() {
         public void run() {
            Application.this.itemsListmodel.removeElement(defs);
         }
      });
   }

   public JFrame getFrame() {
      return this.frmCacheEditorV;
   }
}
