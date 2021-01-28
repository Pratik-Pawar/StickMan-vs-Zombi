/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameEntity;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;

/**
 *
 * @author prati
 */
public class HealthBar extends GameObject {

    private int value;
    private Color borderC, fillC;
    private float thickness;
    private int colorAdder = 80;

    public HealthBar(int x, int y, int width, int height, float thickness, int initValue, Color color) {
        super(x, y, ID.HEALTH_BAR);
        this.thickness = thickness;
        setWidth(width);
        setHeight(height);
        setValue(initValue);
        setColor(color);

    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;

        if (this.value > 100) {
            this.value = 100;
        } else if (this.value < 0) {
            this.value = 0;
        }
    }

    public Color getColor() {
        return borderC;
    }

    public void setColor(Color color) {
        fillC = color;
        int r = color.getRed() - colorAdder;
        int g = color.getGreen() - colorAdder;
        int b = color.getBlue() - colorAdder;

        if (r < 0) {
            r = 0;
        }
        if (g < 0) {
            g = 0;
        }
        if (b < 0) {
            b = 0;
        }

        borderC = new Color(r, g, b);

    }

    public float getThickness() {
        return thickness;
    }

    public void setThickness(float thickness) {
        this.thickness = thickness;
    }

    public boolean isDead() {
        return value == 0;
    }

    @Override
    public void render(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics;

        Color oldColor = g.getColor();
        Stroke oldStroke = g.getStroke();
        g.setStroke(new BasicStroke(thickness));

        g.setColor(fillC);
        g.fillRect(getIntX(), getIntY(), getWidth() * value / 100, getHeight());

        g.setColor(borderC);
        g.drawRect(getIntX(), getIntY(), getWidth(), getHeight());

        g.setStroke(oldStroke);
        g.setColor(oldColor);
    }

    public void render(Graphics graphics, int x, int y) {
        Graphics2D g = (Graphics2D) graphics;

        Color oldColor = g.getColor();
        Stroke oldStroke = g.getStroke();
        g.setStroke(new BasicStroke(thickness));

        g.setColor(fillC);
        g.fillRect(getIntX() + x, getIntY() + y, getWidth() * value / 100, getHeight());

        g.setColor(borderC);
        g.drawRect(getIntX() + x, getIntY() + y, getWidth(), getHeight());

        g.setStroke(oldStroke);
        g.setColor(oldColor);
    }

    /**
     *
     * @deprecated This method do nothing
     */
    @Deprecated
    @Override
    public void update() {
    }

}
