package com.editor;

import com.editor.Main;

import javax.swing.GroupLayout.Alignment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class Console extends JFrame {
   private static final long serialVersionUID = -4693540915136770583L;
   private JMenu jMenu1;
   private JMenuBar jMenuBar1;
   private JMenuItem jMenuItem1;
   private JMenuItem jMenuItem2;
   private JScrollPane jScrollPane1;
   public static JTextArea output;

   public Console() {
      this.setTitle("Console");
      this.setResizable(false);
      this.setDefaultCloseOperation(0);
      this.setLocationRelativeTo((Component)null);
      this.initComponents();
      Main.log("Console", "Console Started.");
   }

   private void initComponents() {
      this.jScrollPane1 = new JScrollPane();
      output = new JTextArea();
      this.jMenuBar1 = new JMenuBar();
      this.jMenu1 = new JMenu();
      this.jMenuItem1 = new JMenuItem();
      this.jMenuItem2 = new JMenuItem();
      this.setDefaultCloseOperation(3);
      output.setEditable(false);
      output.setColumns(20);
      output.setLineWrap(true);
      output.setRows(5);
      this.jScrollPane1.setViewportView(output);
      this.jMenu1.setText("File");
      this.jMenuItem1.setText("Close Console");
      this.jMenuItem1.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent evt) {
            Console.this.jMenuItem1ActionPerformed(evt);
         }
      });
      this.jMenu1.add(this.jMenuItem1);
      this.jMenuItem2.setText("Exit Program");
      this.jMenuItem2.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent evt) {
            Console.this.jMenuItem2ActionPerformed(evt);
         }
      });
      this.jMenu1.add(this.jMenuItem2);
      this.jMenuBar1.add(this.jMenu1);
      this.setJMenuBar(this.jMenuBar1);
      GroupLayout layout = new GroupLayout(this.getContentPane());
      this.getContentPane().setLayout(layout);
      layout.setHorizontalGroup(layout.createParallelGroup(Alignment.LEADING).addComponent(this.jScrollPane1, -1, 618, 32767));
      layout.setVerticalGroup(layout.createParallelGroup(Alignment.LEADING).addComponent(this.jScrollPane1, -1, 240, 32767));
      this.pack();
   }

   private void jMenuItem2ActionPerformed(ActionEvent evt) {
      System.exit(0);
   }

   private void jMenuItem1ActionPerformed(ActionEvent evt) {
      this.dispose();
   }

   public static void main(String[] args) {
      EventQueue.invokeLater(new Runnable() {
         public void run() {
            (new Console()).setVisible(true);
         }
      });
   }

   private static void updateTextArea(final String text) {
      SwingUtilities.invokeLater(new Runnable() {
         public void run() {
            Console.output.append(text);
         }
      });
   }

   public static void redirectSystemStreams() {
      OutputStream out = new OutputStream() {
         public void write(int b) throws IOException {
            Console.updateTextArea(String.valueOf((char)b));
         }

         public void write(byte[] b, int off, int len) throws IOException {
            Console.updateTextArea(new String(b, off, len));
         }

         public void write(byte[] b) throws IOException {
            this.write(b, 0, b.length);
         }
      };
      System.setOut(new PrintStream(out, true));
      System.setErr(new PrintStream(out, true));
   }
}
