/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameEntity.collisionDetection;

import GameEntity.Direction;
import GameEntity.GameObject;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;
import mainComponent.Controls.RectControl;

/**
 *
 * @author prati
 */
public class HitBox {

    private static Random rand = new Random(0);
    private Rectangle box;
    private GameObject owner;
    private String name;
    private Direction direction;
    private RectControl rectControl;
    private Trigger trigger;
    private Color color;
    private int rightX, leftX;

    public HitBox(String name, Rectangle hitBox, GameObject owner, Trigger trigger) {
        this.box = hitBox;
        this.owner = owner;
        this.name = name;
        this.trigger = trigger;
        color = new Color(rand.nextInt(4) * 50, rand.nextInt(4) * 50, rand.nextInt(4) * 50, 100);
        rectControl = RectControl.createAndShow("HB: " + owner.getClass().getSimpleName() + ": " + name, box, 500, 500);
        rightX = box.x;
        leftX = owner.getWidth() - (box.x + box.width);
    }

    public HitBox(String name, int x, int y, int width, int hight, GameObject owner, Trigger trigger) {
        this(name, new Rectangle(x, y, width, hight), owner, trigger);

    }

    public HitBox(String name, int x, int y, int width, int hight, GameObject owner) {
        this(name, new Rectangle(x, y, width, hight), owner, null);

    }

    public Rectangle getBox() {
        return new Rectangle(box);
    }

    public Rectangle getGlobalBox() {
        return new Rectangle(owner.getIntX() + box.x, owner.getIntY() + box.y, box.width, box.height);
    }

    public GameObject getOwner() {
        return owner;
    }

    public String getName() {
        return name;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setTrigger(Trigger trigger) {
        this.trigger = trigger;
    }

    public void collisionDetection(HitBox collider) {
        if (trigger != null && this.getGlobalBox().intersects(collider.getGlobalBox())) {
            trigger.oncollision(collider);
        }
    }

    public void setBox(Rectangle box) {
        this.box = new Rectangle(box);
        rectControl.setRectangle(box);
    }

    public void render(Graphics g) {
        Color oldColor = g.getColor();
        g.setColor(color);
        Rectangle r = getGlobalBox();
        g.fillRect(r.x, r.y, r.width, r.height);
        int cX = r.x + (r.width / 2), cY = r.y + (r.height / 2);
        g.setColor(new Color(color.getRGB()));

        g.drawLine(cX, r.y, cX, r.y + r.height);
        g.drawLine(r.x, cY, r.x + r.width, cY);
        g.setColor(oldColor);

    }

    /**
     * This method return X co-ordinate (position of top left corner) of hitBox
     * relative to its owner
     *
     * @return relative X co-ordinate
     */
    public int getX() {
        return box.x;
    }

    /**
     * This method return Y co-ordinate (position of top left corner) of hitBox
     * relative to its owner
     *
     * @return relative Y co-ordinate
     */
    public int getY() {
        return box.y;
    }

    /**
     * This method return X co-ordinate (position of top left corner) of hitBox
     * relative to canvas
     *
     * @return Global X co-ordinate
     */
    public int getGlobalX() {
        return box.x + owner.getIntX();
    }

    /**
     * This method return Y co-ordinate (position of top left corner)of hitBox
     * relative to canvas
     *
     * @return Global Y co-ordinate
     */
    public int getGlobalY() {
        return box.y + owner.getIntY();
    }

    /**
     * This method return X co-ordinate of Center of hitBox relative to its
     * owner
     *
     * @return relative X co-ordinate
     */
    public int getCenterX() {
        return box.x + box.width / 2;
    }

    /**
     * This method return Y co-ordinate of Center of hitBox relative to its
     * owner
     *
     * @return relative Y co-ordinate of Center
     */
    public int getCenterY() {
        return box.y + box.height / 2;
    }

    /**
     * This method return X co-ordinate of Center of hitBox relative to canvas
     *
     * @return Global X co-ordinate of Center
     */
    public int getGlobalCenterX() {
        return box.x + box.width / 2 + owner.getIntX();
    }

    /**
     * This method return Y co-ordinate (position of top left corner)of hitBox
     * relative to canvas
     *
     * @return Global Y co-ordinate of Center
     */
    public int getGlobalCenterY() {
        return box.y + box.height / 2 + owner.getIntY();
    }

    /**
     * This method return X co-ordinate of bottom right corner of hitBox
     * relative to its owner
     *
     * @return relative X co-ordinate of bottom right corner
     */
    public int getX_() {
        return box.x + box.width;
    }

    /**
     * This method return Y co-ordinate of bottom right corner of hitBox
     * relative to its owner
     *
     * @return relative Y co-ordinate of bottom right corner
     */
    public int getY_() {
        return box.y + box.height;
    }

    /**
     * This method return X co-ordinate of bottom right corner of hitBox
     * relative to canvas
     *
     * @return Global X co-ordinate of bottom right corner
     */
    public int getGlobalX_() {
        return box.x + owner.getIntX() + box.width;
    }

    /**
     * This method return Y co-ordinate of bottom right corner of hitBox
     * relative to canvas
     *
     * @return Global Y co-ordinate of bottom right corner
     */
    public int getGlobalY_() {
        return box.y + owner.getIntY() + box.height;
    }

    public Direction getDireRelativeTo(HitBox hb) {
        if (this.getGlobalCenterX() <= hb.getGlobalCenterX()) {
            return Direction.LEFT;
        } else {
            return Direction.RIGHT;
        }
    }

    public void setDirection(Direction newDirection) {
        if (newDirection == Direction.LEFT) {
            this.setBox(new Rectangle(leftX, box.y, box.width, box.height));
        } else if (newDirection == Direction.RIGHT) {
            this.setBox(new Rectangle(rightX, box.y, box.width, box.height));
        }
    }

    @Override
    public String toString() {
        return "HitBox: Owner: " + owner + " Name: " + name; //To change body of generated methods, choose Tools | Templates.
    }

}
