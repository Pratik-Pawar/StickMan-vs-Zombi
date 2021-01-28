/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameEntity;

import GameEntity.collisionDetection.HitBox;
import java.awt.Graphics;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author prati
 */
public abstract class GameObject {

    private float x, y;
    private int width;
    private int height;
    private List<HitBox> hitBoxList;
    private ID id;

    public GameObject(float x, float y, ID id) {
        this.id = id;
        this.x = x;
        this.y = y;
        hitBoxList = new LinkedList<>();
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getIntX() {
        return Math.round(x);
    }

    public int getIntY() {
        return Math.round(y);

    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    abstract public void render(Graphics g);

    abstract public void update();

    public ID getId() {
        return id;
    }

    void setId(ID id) {
        this.id = id;
    }

    public List<HitBox> getHitBoxList() {
        return hitBoxList;
    }

    void setHitBoxList(List<HitBox> hitBoxList) {
        this.hitBoxList = hitBoxList;
    }

}
