/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameEntity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import GameEntity.ID.*;
import static GameEntity.ID.SOLID;
import GameEntity.collisionDetection.HitBox;

/**
 *
 * @author Administrator
 */
public class Solid extends StaticObject {

    Color color;
    private static int objCount = 0;
    private int objNo;

    public Solid(float x, float y, int width, int height, Color color) {
        super(x, y, width, height, SOLID);
        objNo = ++objCount;
        this.color = color;
        getHitBoxList().add(
                new HitBox("Wall", 0, 0, width, height, this)
        );
    }

    @Override
    public void render(Graphics g) {
        Color oldColor = g.getColor();
        g.setColor(this.color);

        g.fill3DRect(getIntX(), getIntY(), getWidth(), getHeight(), true);

        g.setColor(oldColor);

    }

    @Override
    public String toString() {
        return "Solid #" + objNo;
    }

}
