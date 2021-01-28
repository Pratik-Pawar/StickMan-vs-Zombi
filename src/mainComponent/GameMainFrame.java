/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mainComponent;

import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author prati
 */
public class GameMainFrame extends javax.swing.JFrame {

    private Key key;
    public static JPanel controlPanel;
    private static JScrollPane sp;
    private int canvasWidth, canvasHeight;

    public GameMainFrame(int width, int height) {
        this.canvasHeight = height;
        this.canvasWidth = canvasWidth;
        controlPanel = new JPanel();
        controlPanel.setPreferredSize(new Dimension(width - 20, 1500));
        controlPanel.setVisible(true);
        controlPanel.setLocation(0, 0);
        controlPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 1, 1));

        sp = new JScrollPane();
        sp.setLocation(0, height);
        sp.setSize(width - 20, 100);
        sp.setViewportView(controlPanel);
        sp.getVerticalScrollBar().setUnitIncrement(5);
        setMinimumSize(new Dimension(width + 20, height + 200));

        add(sp);

        initComponents();

        key = new Key();
        addKeyListener(key);

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });
        addWindowStateListener(new java.awt.event.WindowStateListener() {
            public void windowStateChanged(java.awt.event.WindowEvent evt) {
                formWindowStateChanged(evt);
            }
        });
        getContentPane().setLayout(null);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
        controlPanel.setSize(getSize().width - 20, controlPanel.getSize().height);
        sp.setSize(getSize().width - 20, getSize().height - canvasHeight);
    }//GEN-LAST:event_formComponentResized
    private void formWindowStateChanged(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowStateChanged
        formComponentResized(null);
    }//GEN-LAST:event_formWindowStateChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
