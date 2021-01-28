/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameEntity;

import java.awt.Rectangle;
import mainComponent.Controls.RectControl;

/**
 *
 * @author Administrator
 */
public abstract class StaticObject extends GameObject {

    private Rectangle bond;

    public StaticObject(float x, float y, int width, int height, ID id) {
        super(x, y, id);
        setWidth(width);
        setHeight(height);
        bond = new Rectangle(Math.round(x), Math.round(y), width, height);
        RectControl.createAndShow("Bond: " + getClass().getSimpleName(), bond, 800, 800);
    }

    @Override
    public void update() {
        setX(bond.x);
        setY(bond.y);
        setWidth(bond.width);
        setHeight(bond.height);
    }

}
