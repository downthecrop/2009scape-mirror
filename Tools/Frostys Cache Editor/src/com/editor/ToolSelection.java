package com.editor;

import com.editor.Main;
import com.editor.item.ItemDefDump;
import com.editor.item.ItemSelection;
import com.editor.model.MultiModelPacker;
import com.editor.model.UniModelDumper;
import com.editor.npc.NPCDefDump;
import com.editor.npc.NPCSelection;
import com.editor.object.ObjectSelection;

import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class ToolSelection extends JFrame {
   private static final long serialVersionUID = 2024943190858205332L;
   private String cache;
   private JLabel jLabel1;
   private JMenu jMenu1;
   private JMenuBar jMenuBar1;
   private JMenuItem loadCacheButton;
   private JMenuItem exitButton;
   private JComboBox selectionBox;
   private JButton submitButton;

   public ToolSelection() {
      this.setTitle("Tool Selection");
      this.setResizable(false);
      this.setDefaultCloseOperation(3);
      this.setLocationRelativeTo((Component)null);
      this.initComponents();
      Main.log("Main", "ToolSelection Started");
   }

   private void initComponents() {
      this.jLabel1 = new JLabel();
      this.selectionBox = new JComboBox();
      this.submitButton = new JButton();
      this.jMenuBar1 = new JMenuBar();
      this.jMenu1 = new JMenu();
      this.loadCacheButton = new JMenuItem();
      this.exitButton = new JMenuItem();
      this.setDefaultCloseOperation(3);
      this.jLabel1.setText("Select Your Editor:");
      this.selectionBox.setModel(new DefaultComboBoxModel(new String[]{"Items", "NPCs", "Objects", "ItemDefDump", "NPCDefDump", "MultiModelPacker", "MultiItemPacker", "MultiNPCPacker", "UniModelDumper"}));
      this.submitButton.setText("Submit");
      this.submitButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent evt) {
            ToolSelection.this.submitButtonActionPerformed(evt);
         }
      });
      this.jMenu1.setText("File");
      this.loadCacheButton.setText("Load Cache");
      this.loadCacheButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent evt) {
            ToolSelection.this.jMenuItem1ActionPerformed(evt);
         }
      });
      this.jMenu1.add(this.loadCacheButton);
      this.exitButton.setText("Exit Program");
      this.exitButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent evt) {
            ToolSelection.this.exitButtonActionPerformed(evt);
         }
      });
      this.jMenu1.add(this.exitButton);
      this.jMenuBar1.add(this.jMenu1);
      this.setJMenuBar(this.jMenuBar1);
      GroupLayout layout = new GroupLayout(this.getContentPane());
      this.getContentPane().setLayout(layout);
      layout.setHorizontalGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(77, 77, 77).addGroup(layout.createParallelGroup(Alignment.LEADING).addComponent(this.submitButton).addComponent(this.selectionBox, -2, -1, -2)).addContainerGap(-1, 32767)).addGroup(Alignment.TRAILING, layout.createSequentialGroup().addContainerGap(62, 32767).addComponent(this.jLabel1).addGap(58, 58, 58)));
      layout.setVerticalGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap(50, 32767).addComponent(this.jLabel1).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(this.selectionBox, -2, -1, -2).addGap(44, 44, 44).addComponent(this.submitButton).addGap(38, 38, 38)));
      this.pack();
   }

   private void submitButtonActionPerformed(ActionEvent evt) {
      if(this.selectionBox.getSelectedIndex() == 0) {
         try {
            (new ItemSelection(this.cache)).setVisible(true);
            Main.log("ToolSelection", "ItemSelection Started");
         } catch (IOException var4) {
            Main.log("ToolSelection", "No Cache Set!");
         }
      } else if(this.selectionBox.getSelectedIndex() == 1) {
         try {
            (new NPCSelection(this.cache)).setVisible(true);
            Main.log("ToolSelection", "NPCSelection Started");
         } catch (IOException var3) {
            Main.log("ToolSelection", "No Cache Set!");
         }
	  } else if(this.selectionBox.getSelectedIndex() == 2) {
         try {
            (new ObjectSelection(this.cache)).setVisible(true);
            Main.log("ToolSelection", "ObjectSelection Started");
         } catch (IOException var2) {
            Main.log("ToolSelection", "No Cache Set!");
         }
      } else if(this.selectionBox.getSelectedIndex() == 3) {
         Main.log("ToolSelection", "Item Def Dumping Started");
         EventQueue.invokeLater(new Runnable() {
            public void run() {
               ItemDefDump.editorDump(ToolSelection.this.cache);
            }
         });
      } else if(this.selectionBox.getSelectedIndex() == 4) {
         Main.log("ToolSelection", "NPC Def Dumping Started");
         EventQueue.invokeLater(new Runnable() {
            public void run() {
               NPCDefDump.editorDump(ToolSelection.this.cache);
            }
         });
      } else if(this.selectionBox.getSelectedIndex() == 5) {
         Main.log("ToolSelection", "MultiModelPacker Started");
         EventQueue.invokeLater(new Runnable() {
            public void run() {
               new MultiModelPacker(ToolSelection.this.cache);
            }
         });
      } else if(this.selectionBox.getSelectedIndex() == 6) {
         Main.log("ToolSelection", "MultiNPCPacker is not working at the moment.");
      } else if(this.selectionBox.getSelectedIndex() == 8) {
         Main.log("ToolSelection", "UniModelDumper Started");
         EventQueue.invokeLater(new Runnable() {
            public void run() {
               new UniModelDumper(ToolSelection.this.cache);
            }
         });
      } else {
         Main.log("ToolSelection", "No Tool Selected!");
      }

   }

   private void jMenuItem1ActionPerformed(ActionEvent evt) {
      JFileChooser fc = new JFileChooser();
      fc.setFileSelectionMode(1);
      if(evt.getSource() == this.loadCacheButton) {
         int returnVal = fc.showOpenDialog(this);
         if(returnVal == 0) {
            File file = fc.getSelectedFile();
            this.cache = file.getPath() + "/";
         }
      }

   }

   private void exitButtonActionPerformed(ActionEvent evt) {
      JDialog.setDefaultLookAndFeelDecorated(true);
      int response = JOptionPane.showConfirmDialog((Component)null, "Do you want to continue?", "Confirm", 0, 3);
      if(response == 0) {
         System.exit(0);
      }

   }

   public static void main(String[] args) {
      EventQueue.invokeLater(new Runnable() {
         public void run() {
            (new ToolSelection()).setVisible(true);
         }
      });
   }
}
