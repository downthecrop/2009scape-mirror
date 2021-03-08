package com.editor.object;

import com.alex.loaders.objects.ObjectDefinitions;
import com.alex.store.Store;
import com.editor.Main;
import com.editor.object.ObjectEditor;

import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ObjectSelection extends JFrame {
   private static final long serialVersionUID = -3068337441331467664L;
   public static Store STORE;
   private JButton addButton;
   private JButton duplicateButton;
   private JButton editButton;
   private DefaultListModel objectsListmodel;
   private JList objectsList;
   private JMenu jMenu1;
   private JMenuBar jMenuBar1;
   private JMenuItem exitButton;
   private JButton deleteButton;

   public ObjectSelection(String cache) throws IOException {
      STORE = new Store(cache);
      this.setTitle("Object Selection");
      this.setResizable(false);
      this.setDefaultCloseOperation(1);
      this.setLocationRelativeTo((Component)null);
      this.initComponents();
   }

   public ObjectSelection() {
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
      this.objectsListmodel = new DefaultListModel();
      this.objectsList = new JList(this.objectsListmodel);
      this.objectsList.setSelectionMode(1);
      this.objectsList.setLayoutOrientation(0);
      this.objectsList.setVisibleRowCount(-1);
      JScrollPane jScrollPane1 = new JScrollPane(this.objectsList);
      this.editButton.setText("Edit");
      this.editButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            ObjectDefinitions defs = (ObjectDefinitions)ObjectSelection.this.objectsList.getSelectedValue();
            if(defs != null) {
               new ObjectEditor(ObjectSelection.this, defs);
            }

         }
      });
      this.addButton.setText("Add New");
      this.addButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            ObjectDefinitions defs = new ObjectDefinitions(ObjectSelection.STORE, ObjectSelection.this.getNewObjectID(), false);
            if(defs != null && defs.id != -1) {
               new ObjectEditor(ObjectSelection.this, defs);
            }

         }
      });
      this.duplicateButton.setText("Duplicate");
      this.duplicateButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            ObjectDefinitions defs = (ObjectDefinitions)ObjectSelection.this.objectsList.getSelectedValue();
            if(defs != null) {
              // defs = (ObjectDefinitions)defs.clone();
               if(defs != null) {
                  defs.id = ObjectSelection.this.getNewObjectID();
                  if(defs.id != -1) {
                     new ObjectEditor(ObjectSelection.this, defs);
                  }
               }
            }

         }
      });
      this.deleteButton.setText("Delete");
      this.deleteButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            ObjectDefinitions defs = (ObjectDefinitions)ObjectSelection.this.objectsList.getSelectedValue();
            JFrame frame = new JFrame();
            int result = JOptionPane.showConfirmDialog(frame, "Do you really want to delete Object " + defs.id);
            if(result == 0) {
               if(defs == null) {
                  return;
               }

               ObjectSelection.STORE.getIndexes()[19].removeFile(defs.getArchiveId(), defs.getFileId());
               ObjectSelection.this.removeObjectDefs(defs);
               Main.log("ObjectSelection", "Object " + defs.id + " removed.");
            }

         }
      });
      this.jMenu1.setText("File");
      this.exitButton.setText("Close");
      this.exitButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent evt) {
            ObjectSelection.this.exitButtonActionPerformed(evt);
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
      this.addAllObjects();
   }

   public static void main(String[] args) throws IOException {
      STORE = new Store("cache/", false);
      EventQueue.invokeLater(new Runnable() {
         public void run() {
            (new ObjectSelection()).setVisible(true);
         }
      });
   }

   private void exitButtonActionPerformed(ActionEvent evt) {
      this.dispose();
   }

   public int getNewObjectID() {
      try {
         JFrame var3 = new JFrame();
         String parent1 = JOptionPane.showInputDialog(var3, "Enter new Object ID:");
         return Integer.parseInt(parent1.toString());
      } catch (Exception var31) {
         JFrame parent = new JFrame();
         JOptionPane.showMessageDialog(parent, "Please enter a valid integer!");
         Main.log("ObjectSelection", "Non-valid character entered for new Object ID");
         return -1;
      }
   }

   public void addAllObjects() {
      for(int id = 36770; id < /*Utils.getObjectDefinitionsSize(STORE)*/36773; ++id) {
		 addObjectDefs(ObjectDefinitions.getObjectDefinition(STORE, id));
      }

      Main.log("ObjectSelection", "All Objects Loaded");
   }
   
   /*public void addAllObjects() {
      int id;
      if(Utils.getObjectDefinitionsSize(STORE) > 30000) {
         for(id = 0; id < Utils.getObjectDefinitionsSize(STORE) - 22314; ++id) {
            this.addObjectDefs(ObjectDefinitions.getObjectDefinition(STORE, id));
         }
      } else {
         for(id = 0; id < Utils.getObjectDefinitionsSize(STORE); ++id) {
            this.addObjectDefs(ObjectDefinitions.getObjectDefinition(STORE, id));
         }
      }

      Main.log("ObjectSelection", "All Objects Loaded");
   }*/

   public void addObjectDefs(final ObjectDefinitions defs) {
      EventQueue.invokeLater(new Runnable() {
         public void run() {
            ObjectSelection.this.objectsListmodel.addElement(defs);
         }
      });
   }

   public void updateObjectDefs(final ObjectDefinitions defs) {
      EventQueue.invokeLater(new Runnable() {
         public void run() {
            int index = ObjectSelection.this.objectsListmodel.indexOf(defs);
            if(index == -1) {
               ObjectSelection.this.objectsListmodel.addElement(defs);
            } else {
               ObjectSelection.this.objectsListmodel.setElementAt(defs, index);
            }

         }
      });
   }

   public void removeObjectDefs(final ObjectDefinitions defs) {
      EventQueue.invokeLater(new Runnable() {
         public void run() {
            ObjectSelection.this.objectsListmodel.removeElement(defs);
         }
      });
   }
}
