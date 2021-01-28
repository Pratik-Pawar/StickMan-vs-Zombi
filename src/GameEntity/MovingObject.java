/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameEntity;

import java.awt.Rectangle;
import java.util.List;

/**
 *
 * @author prati
 */
public abstract class MovingObject extends GameObject {

    private Direction direction;
    float motationSpeed, speed;
    final float MOTATION_UNIT_SPEED;

    public MovingObject(float MOTATION_UNIT_SPEED, Direction direction, float x, float y, ID id) {
        super(x, y, id);
        this.MOTATION_UNIT_SPEED = MOTATION_UNIT_SPEED;
        this.direction = Direction.RIGHT;
        this.direction = direction;
    }

    public MovingObject(float MOTATION_UNIT_SPEED, ID id) {
        super(0, 0, id);
        this.MOTATION_UNIT_SPEED = MOTATION_UNIT_SPEED;

    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction newDirection) {
        this.direction = newDirection;

    }

    public float getSpeed() {
        return speed / MOTATION_UNIT_SPEED;
    }

    public abstract void setSpeed(float speed);

    //public abstract void finalize();
}
