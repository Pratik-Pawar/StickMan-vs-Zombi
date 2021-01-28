/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mainComponent.Controls;

import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import mainComponent.GameMainFrame;

/**
 *
 * @author prati
 */
public class SliderControl extends javax.swing.JPanel implements AdjustmentListener {

    /**
     * Creates new form Control
     */
    private float value;
    private int sliderValue;
    private float step;
    private float max, min;
    private ChangeListener changeListener;

    public SliderControl(String name, float iniValue, float min, float max, float step) {
        initComponents();
        this.max = max;
        this.min = min;
        this.name.setText(name);
        this.setSize(getPreferredSize());
        slider.setMaximum(Math.round((max + 0.1f) * 100));
        slider.setMinimum(Math.round(min * 100));
        slider.setUnitIncrement(Math.round(step * 100));
        this.step = step;
        setValue(iniValue);

        slider.addAdjustmentListener(this);
    }

    public static SliderControl createAndShow(String name, float iniValue, float min, float max, float step) {
        SliderControl sc = new SliderControl(name, iniValue, min, max, step);
        GameMainFrame.controlPanel.add(sc);
        return sc;
    }

    public void setValue(float value) {
        this.value = value;
        sliderValue = Math.round(value * 100);
        slider.setValue(sliderValue);
        valueText.setText("" + this.value);

    }

    public float getValue() {
        return value;
    }

    public int getIntValue() {
        return Math.round(value);
    }

    public void SetChangeListener(ChangeListener changeListener) {
        this.changeListener = changeListener;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        name = new javax.swing.JLabel();
        valueText = new javax.swing.JLabel();
        slider = new javax.swing.JScrollBar();

        setMaximumSize(new java.awt.Dimension(217, 37));
        setMinimumSize(new java.awt.Dimension(217, 37));
        setPreferredSize(new java.awt.Dimension(217, 37));
        setLayout(null);

        name.setText("jLabel1");
        add(name);
        name.setBounds(10, 10, 90, 16);

        valueText.setText("jLabel1");
        valueText.setPreferredSize(new java.awt.Dimension(120, 20));
        add(valueText);
        valueText.setBounds(130, 22, 40, 20);

        slider.setOrientation(javax.swing.JScrollBar.HORIZONTAL);
        slider.setPreferredSize(new java.awt.Dimension(100, 500));
        slider.setValueIsAdjusting(true);
        add(slider);
        slider.setBounds(100, 10, 100, 15);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel name;
    private javax.swing.JScrollBar slider;
    private javax.swing.JLabel valueText;
    // End of variables declaration//GEN-END:variables

    @Override
    public void adjustmentValueChanged(AdjustmentEvent e) {
        sliderValue = slider.getValue();
        value = sliderValue / 100.0f;
        valueText.setText("" + value);
        if (changeListener != null) {
            changeListener.onChange(this, value);
        }
    }
}
