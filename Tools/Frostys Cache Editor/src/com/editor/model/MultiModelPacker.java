package com.editor.model;

import com.alex.store.Store;
import com.editor.Main;
import com.editor.Utils;

import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.UIManager.LookAndFeelInfo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MultiModelPacker extends JFrame {
   private static final long serialVersionUID = -1984870962939167633L;
   private static Store STORE;
   private JMenuItem exit;
   private JLabel jLabel1;
   private JLabel jLabel2;
   private JMenu jMenu1;
   private JMenuBar jMenuBar1;
   private JMenuItem modelDir;
   private JTextField modelDirField;
   private JCheckBox sameId;
   private JButton submit;

   public MultiModelPacker() {
      this.initComponents();
   }

   public MultiModelPacker(String cache) {
      try {
         STORE = new Store(cache);
      } catch (Exception var3) {
         Main.log("MultiModelPacker", "Cannot find cache directory");
      }

      this.initComponents();
      this.setResizable(false);
      this.setTitle("MultiModelPacker");
      this.setDefaultCloseOperation(1);
      this.setLocationRelativeTo((Component)null);
      this.setVisible(true);
   }

   private void initComponents() {
      this.jLabel1 = new JLabel();
      this.jLabel2 = new JLabel();
      this.modelDirField = new JTextField();
      this.sameId = new JCheckBox();
      this.submit = new JButton();
      this.jMenuBar1 = new JMenuBar();
      this.jMenu1 = new JMenu();
      this.modelDir = new JMenuItem();
      this.exit = new JMenuItem();
      this.setDefaultCloseOperation(3);
      this.jLabel1.setText("Multiple Model Packer");
      this.jLabel2.setText("Models Directory");
      this.sameId.setText("Keep Same ID");
      this.sameId.setToolTipText("Keeps same ID as named");
      this.submit.setText("Submit");
      this.submit.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent evt) {
            MultiModelPacker.this.submitActionPerformed(evt);
         }
      });
      this.jMenu1.setText("File");
      this.modelDir.setText("Choose Model Dir");
      this.modelDir.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent evt) {
            MultiModelPacker.this.modelDirActionPerformed(evt);
         }
      });
      this.jMenu1.add(this.modelDir);
      this.exit.setText("Exit");
      this.exit.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent evt) {
            MultiModelPacker.this.exitActionPerformed(evt);
         }
      });
      this.jMenu1.add(this.exit);
      this.jMenuBar1.add(this.jMenu1);
      this.setJMenuBar(this.jMenuBar1);
      GroupLayout layout = new GroupLayout(this.getContentPane());
      this.getContentPane().setLayout(layout);
      layout.setHorizontalGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(this.modelDirField).addContainerGap()).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup().addPreferredGap(ComponentPlacement.RELATED, 4, -2).addComponent(this.jLabel2)).addComponent(this.sameId))).addGroup(layout.createSequentialGroup().addGap(77, 77, 77).addComponent(this.jLabel1)).addGroup(layout.createSequentialGroup().addGap(101, 101, 101).addComponent(this.submit))).addGap(0, 76, 32767)));
      layout.setVerticalGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(this.jLabel1).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(this.jLabel2).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.modelDirField, -2, -1, -2).addGap(18, 18, 18).addComponent(this.sameId).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(this.submit).addContainerGap(20, 32767)));
      this.pack();
   }

   private void submitActionPerformed(ActionEvent evt) {
      String directory = this.modelDirField.getText();
      boolean keepID = this.sameId.isSelected();
      String fileName;
      if(keepID) {
         fileName = "";
         String var12 = "";
         File var13 = new File(directory);
         File[] var14 = var13.listFiles();

         for(int var10 = 0; var10 < var14.length; ++var10) {
            if(var14[var10].isFile()) {
               System.out.println(var14[var10]);
               fileName = var14[var10].getName();
               var12 = var14[var10].getName().replace(".dat", "");

               try {
                  Main.log("MultiModelPacker", "The model ID of " + fileName + " is: " + Utils.packCustomModel(STORE, Utils.getBytesFromFile(var14[var10]), Integer.parseInt(var12)));
               } catch (IOException var11) {
                  Main.log("MultiModelPacker", "There was an error packing the model.");
               }
            }
         }
      } else {
         fileName = "";
         File var121 = new File(directory);
         File[] var131 = var121.listFiles();

         for(int var141 = 0; var141 < var131.length; ++var141) {
            if(var131[var141].isFile()) {
               fileName = var131[var141].getName();

               try {
                  Main.log("MultiModelPacker", "The model ID of " + fileName + " is: " + Utils.packCustomModel(STORE, Utils.getBytesFromFile(var131[var141])));
               } catch (IOException var101) {
                  Main.log("MultiModelPacker", "There was an error packing the model.");
               }
            }
         }
      }

   }

   private void modelDirActionPerformed(ActionEvent evt) {
      JFileChooser fc = new JFileChooser();
      fc.setFileSelectionMode(1);
      if(evt.getSource() == this.modelDir) {
         int returnVal = fc.showOpenDialog(this);
         if(returnVal == 0) {
            File file = fc.getSelectedFile();
            this.modelDirField.setText(file.getPath() + "/");
         }
      }

   }

   private void exitActionPerformed(ActionEvent evt) {
      this.dispose();
   }

   public static void main(String[] args) {
      try {
         LookAndFeelInfo[] var8 = UIManager.getInstalledLookAndFeels();
         int len$ = var8.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            LookAndFeelInfo info = var8[i$];
            if("Nimbus".equals(info.getName())) {
               UIManager.setLookAndFeel(info.getClassName());
               break;
            }
         }
      } catch (ClassNotFoundException var5) {
         Logger.getLogger(MultiModelPacker.class.getName()).log(Level.SEVERE, (String)null, var5);
      } catch (InstantiationException var6) {
         Logger.getLogger(MultiModelPacker.class.getName()).log(Level.SEVERE, (String)null, var6);
      } catch (IllegalAccessException var7) {
         Logger.getLogger(MultiModelPacker.class.getName()).log(Level.SEVERE, (String)null, var7);
      } catch (UnsupportedLookAndFeelException var81) {
         Logger.getLogger(MultiModelPacker.class.getName()).log(Level.SEVERE, (String)null, var81);
      }

      EventQueue.invokeLater(new Runnable() {
         public void run() {
            (new MultiModelPacker()).setVisible(true);
         }
      });
   }
}
