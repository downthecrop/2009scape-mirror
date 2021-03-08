package com.editor.item;

import com.alex.loaders.items.ItemDefinitions;
import com.alex.store.Store;
import com.alex.utils.Utils;
import com.editor.Main;
import com.editor.item.ItemEditor;

import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ItemSelection extends JFrame {
   private static final long serialVersionUID = -3059309913196024742L;
   public static Store STORE;
   private JButton addButton;
   private JButton duplicateButton;
   private JButton editButton;
   private DefaultListModel itemsListmodel;
   private JList itemsList;
   private JMenu jMenu1;
   private JMenuBar jMenuBar1;
   private JMenuItem exitButton;
   private JButton deleteButton;

   public ItemSelection(String cache) throws IOException {
      STORE = new Store(cache);
      this.setTitle("Item Selection");
      this.setResizable(false);
      this.setDefaultCloseOperation(1);
      this.setLocationRelativeTo((Component)null);
      this.initComponents();
   }

   public ItemSelection() {
      this.initComponents();
   }

   private void initComponents() {
      this.editButton = new JButton();
      this.addButton = new JButton();
      this.duplicateButton = new JButton();
      this.deleteButton = new JButton();
      this.jMenuBar1 = new JMenuBar();
      this.jMenu1 = new JMenu();
      this.exitButton = new JMenuItem();
      this.setDefaultCloseOperation(1);
      this.itemsListmodel = new DefaultListModel();
      this.itemsList = new JList(this.itemsListmodel);
      this.itemsList.setSelectionMode(1);
      this.itemsList.setLayoutOrientation(0);
      this.itemsList.setVisibleRowCount(-1);
      JScrollPane jScrollPane1 = new JScrollPane(this.itemsList);
      this.editButton.setText("Edit");
      this.editButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            ItemDefinitions defs = (ItemDefinitions)ItemSelection.this.itemsList.getSelectedValue();
            if(defs != null) {
               new ItemEditor(ItemSelection.this, defs);
            }

         }
      });
      this.addButton.setText("Add New");
      this.addButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            ItemDefinitions defs = new ItemDefinitions(ItemSelection.STORE, ItemSelection.this.getNewItemID(), false);
            if(defs != null && defs.id != -1) {
               new ItemEditor(ItemSelection.this, defs);
            }

         }
      });
      this.duplicateButton.setText("Duplicate");
      this.duplicateButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            ItemDefinitions defs = (ItemDefinitions)ItemSelection.this.itemsList.getSelectedValue();
            if(defs != null) {
               defs = (ItemDefinitions)defs.clone();
               if(defs != null) {
                  defs.id = ItemSelection.this.getNewItemID();
                  if(defs.id != -1) {
                     new ItemEditor(ItemSelection.this, defs);
                  }
               }
            }

         }
      });
      this.deleteButton.setText("Delete");
      this.deleteButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            ItemDefinitions defs = (ItemDefinitions)ItemSelection.this.itemsList.getSelectedValue();
            JFrame frame = new JFrame();
            int result = JOptionPane.showConfirmDialog(frame, "Do you really want to delete item " + defs.id);
            if(result == 0) {
               if(defs == null) {
                  return;
               }

               ItemSelection.STORE.getIndexes()[19].removeFile(defs.getArchiveId(), defs.getFileId());
               ItemSelection.this.removeItemDefs(defs);
               Main.log("ItemSelection", "Item " + defs.id + " removed.");
            }

         }
      });
      this.jMenu1.setText("File");
      this.exitButton.setText("Close");
      this.exitButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent evt) {
            ItemSelection.this.exitButtonActionPerformed(evt);
         }
      });
      this.jMenu1.add(this.exitButton);
      this.jMenuBar1.add(this.jMenu1);
      this.setJMenuBar(this.jMenuBar1);
      GroupLayout layout = new GroupLayout(this.getContentPane());
      this.getContentPane().setLayout(layout);
      layout.setHorizontalGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(Alignment.LEADING, false).addComponent(jScrollPane1, -2, 200, -2).addGroup(layout.createSequentialGroup().addComponent(this.editButton).addPreferredGap(ComponentPlacement.RELATED, -1, 32767).addComponent(this.addButton))).addGap(0, 0, 32767)).addGroup(layout.createSequentialGroup().addComponent(this.duplicateButton).addPreferredGap(ComponentPlacement.RELATED, -1, 32767).addComponent(this.deleteButton))).addContainerGap(-1, 32767)));
      layout.setVerticalGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jScrollPane1, -2, 279, -2).addPreferredGap(ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(this.editButton).addComponent(this.addButton)).addPreferredGap(ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(this.duplicateButton).addComponent(this.deleteButton)).addContainerGap(-1, 32767)));
      this.pack();
      this.addAllItems();
   }

   public static void main(String[] args) throws IOException {
      STORE = new Store("cache/", false);
      EventQueue.invokeLater(new Runnable() {
         public void run() {
            (new ItemSelection()).setVisible(true);
         }
      });
   }

   private void exitButtonActionPerformed(ActionEvent evt) {
      this.dispose();
   }

   public int getNewItemID() {
      try {
         JFrame var3 = new JFrame();
         String parent1 = JOptionPane.showInputDialog(var3, "Enter new Item ID:");
         return Integer.parseInt(parent1.toString());
      } catch (Exception var31) {
         JFrame parent = new JFrame();
         JOptionPane.showMessageDialog(parent, "Please enter a valid integer!");
         Main.log("ItemSelection", "Non-valid character entered for new Item ID");
         return -1;
      }
   }

   public void addAllItems() {
      int id;
      if(Utils.getItemDefinitionsSize(STORE) > 30000) {
         for(id = 0; id < Utils.getItemDefinitionsSize(STORE) - 22314; ++id) {
            this.addItemDefs(ItemDefinitions.getItemDefinition(STORE, id));
         }
      } else {
         for(id = 0; id < Utils.getItemDefinitionsSize(STORE); ++id) {
            this.addItemDefs(ItemDefinitions.getItemDefinition(STORE, id));
         }
      }

      Main.log("ItemSelection", "All Items Loaded");
   }

   public void addItemDefs(final ItemDefinitions defs) {
      EventQueue.invokeLater(new Runnable() {
         public void run() {
            ItemSelection.this.itemsListmodel.addElement(defs);
         }
      });
   }

   public void updateItemDefs(final ItemDefinitions defs) {
      EventQueue.invokeLater(new Runnable() {
         public void run() {
            int index = ItemSelection.this.itemsListmodel.indexOf(defs);
            if(index == -1) {
               ItemSelection.this.itemsListmodel.addElement(defs);
            } else {
               ItemSelection.this.itemsListmodel.setElementAt(defs, index);
            }

         }
      });
   }

   public void removeItemDefs(final ItemDefinitions defs) {
      EventQueue.invokeLater(new Runnable() {
         public void run() {
            ItemSelection.this.itemsListmodel.removeElement(defs);
         }
      });
   }
}
