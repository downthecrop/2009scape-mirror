package com.editor.model;

import com.alex.store.Index;
import com.alex.store.Store;
import com.editor.Main;

import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.UIManager.LookAndFeelInfo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UniModelDumper extends JFrame {
   private static final long serialVersionUID = 433437706015594462L;
   private static Store STORE;
   private JMenu exit;
   private JLabel jLabel1;
   private JMenuBar jMenuBar1;
   private JMenuItem jMenuItem1;
   private JTextField modelID;
   private JButton submit;

   public UniModelDumper() {
      this.initComponents();
   }

   public UniModelDumper(String cache) {
      try {
         STORE = new Store(cache);
      } catch (Exception var3) {
         Main.log("UniModelDumper", "Cannot find cache directory");
      }

      this.initComponents();
      this.setResizable(false);
      this.setTitle("UniModelDumper");
      this.setDefaultCloseOperation(1);
      this.setLocationRelativeTo((Component)null);
      this.setVisible(true);
   }

   private void initComponents() {
      this.jLabel1 = new JLabel();
      this.modelID = new JTextField();
      this.submit = new JButton();
      this.jMenuBar1 = new JMenuBar();
      this.exit = new JMenu();
      this.jMenuItem1 = new JMenuItem();
      this.setDefaultCloseOperation(3);
      this.jLabel1.setText("Model ID To Dump:");
      this.submit.setText("Submit");
      this.submit.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent evt) {
            UniModelDumper.this.submitActionPerformed(evt);
         }
      });
      this.exit.setText("File");
      this.exit.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent evt) {
            UniModelDumper.this.exitActionPerformed(evt);
         }
      });
      this.jMenuItem1.setText("Exit");
      this.exit.add(this.jMenuItem1);
      this.jMenuBar1.add(this.exit);
      this.setJMenuBar(this.jMenuBar1);
      GroupLayout layout = new GroupLayout(this.getContentPane());
      this.getContentPane().setLayout(layout);
      layout.setHorizontalGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(10, 10, 10).addComponent(this.modelID, -2, 100, -2)).addComponent(this.jLabel1))).addGroup(layout.createSequentialGroup().addGap(32, 32, 32).addComponent(this.submit))).addContainerGap(22, 32767)));
      layout.setVerticalGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(this.jLabel1).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.modelID, -2, -1, -2).addGap(33, 33, 33).addComponent(this.submit).addContainerGap(20, 32767)));
      this.pack();
   }

   private void exitActionPerformed(ActionEvent evt) {
      this.dispose();
   }

   private void submitActionPerformed(ActionEvent evt) {
      File f = new File(System.getProperty("user.home") + "/FCE/models/");
      f.mkdirs();
      Index index = STORE.getIndexes()[7];
      int modelId = Integer.parseInt(this.modelID.getText());

      try {
         byte[] var6 = index.getFile(modelId);
         writeFile(var6, System.getProperty("user.home") + "/FCE/models/" + modelId + ".dat");
         Main.log("UniModelDumper", "Dumped Model ID: " + modelId + " to: " + System.getProperty("user.home") + "/FCE/models/" + modelId + ".dat");
      } catch (IOException var61) {
         Main.log("UniModelDumper", "Cannot Dump Model");
      }

   }

   public static void writeFile(byte[] data, String fileName) throws IOException {
      FileOutputStream out = new FileOutputStream(fileName);
      out.write(data);
      out.close();
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
         Logger.getLogger(UniModelDumper.class.getName()).log(Level.SEVERE, (String)null, var5);
      } catch (InstantiationException var6) {
         Logger.getLogger(UniModelDumper.class.getName()).log(Level.SEVERE, (String)null, var6);
      } catch (IllegalAccessException var7) {
         Logger.getLogger(UniModelDumper.class.getName()).log(Level.SEVERE, (String)null, var7);
      } catch (UnsupportedLookAndFeelException var81) {
         Logger.getLogger(UniModelDumper.class.getName()).log(Level.SEVERE, (String)null, var81);
      }

      EventQueue.invokeLater(new Runnable() {
         public void run() {
            (new UniModelDumper()).setVisible(true);
         }
      });
   }
}
