package com.alex.tools.itemsDefsEditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class GeneratedUkeys extends JDialog {
   private static final long serialVersionUID = -4163755150981048484L;

   public GeneratedUkeys(JFrame frame, byte[] ukeys) {
      super(frame, "Ukeys", true);
      this.setBounds(100, 100, 450, 300);
      this.getContentPane().setLayout((LayoutManager)null);
      final JEditorPane editorPane = new JEditorPane();
      editorPane.setText(Arrays.toString(ukeys));
      editorPane.setBounds(6, 6, 420, 213);
      this.getContentPane().add(editorPane);
      JButton btnClose = new JButton("Close");
      btnClose.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            GeneratedUkeys.this.dispose();
         }
      });
      btnClose.setBounds(101, 221, 90, 28);
      this.getContentPane().add(btnClose);
      JButton btnCopy = new JButton("Copy");
      btnCopy.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            ActionEvent nev = new ActionEvent(editorPane, 1001, "copy");
            editorPane.selectAll();
            editorPane.getActionMap().get(nev.getActionCommand()).actionPerformed(nev);
         }
      });
      btnCopy.setBounds(6, 221, 90, 28);
      this.getContentPane().add(btnCopy);
      this.setDefaultCloseOperation(2);
      this.setVisible(true);
   }
}
