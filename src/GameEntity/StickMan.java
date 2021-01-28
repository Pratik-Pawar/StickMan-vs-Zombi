/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameEntity;

import GameEntity.Animations.DirectionalAnimation;
import GameEntity.Animations.STWalking;
import static GameEntity.ID.STICK_MAN;
import GameEntity.collisionDetection.HitBox;
import java.awt.Color;
import java.awt.Graphics;
import java.util.List;
import mainComponent.Controls.SliderControl;
import mainComponent.Key;

/**
 *
 * @author prati
 */
public class StickMan extends MovingObject {

    private STWalking walking;
    private DirectionalAnimation fighting, currAnima, hurting;
    private State currState;
    private boolean fightReq, hurtReq;
    private HitBox handHB, fullBodyHB, pushHB, faceHB;
    private boolean attcking;
    private float Z_PUSH_FORCE = 0.13f;
    private HealthBar health;

    public StickMan(int x, int y) {
        super(3.3f, Direction.RIGHT, x, y, STICK_MAN);

        this.attcking = false;
        this.fightReq = false;

        health = new HealthBar(300, 30, 400, 25, 3, 100,
                new Color(20, 20, 200));

        walking = new STWalking();
        fighting = new DirectionalAnimation(0.55f, "/Animaction/", "ST Fighting", 15, Direction.RIGHT);
        hurting = new DirectionalAnimation(1.3f, "/Animaction/", "ST Hurting", 25, Direction.RIGHT);
        currAnima = walking;

        setX(x);
        setY(y);
        setWidth(walking.getWidth());
        setHeight(walking.getHeight());

        initHitBox();

        setCurrState(State.Rest);

        setSpeed(1.0f);
        SliderControl.createAndShow("Z_PUSH_FORCE", Z_PUSH_FORCE, -0.5f, 2f, 0.01f)
                .SetChangeListener((source, newValue) -> {
                    Z_PUSH_FORCE = newValue;
                });
        SliderControl.createAndShow("StickMan", 1.0f, 0.05f, 5.0f, 0.05f)
                .SetChangeListener((SliderControl source, float newValue) -> {
                    setSpeed(newValue);
                });

    }

    public boolean isAttcking() {
        return attcking;
    }

    @Override
    public void setSpeed(float speed) {
        this.speed = speed;
        motationSpeed = MOTATION_UNIT_SPEED * speed;
        walking.setAnimeSpeed(speed);
        fighting.setAnimeSpeed(speed);
        hurting.setAnimeSpeed(speed);
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(currAnima.getFrame(), getIntX(), getIntY(), null);
        health.render(g);
    }

    @Override
    public void update() {

        State nextState = getNextState();
        switch (currState) {

            case Fight:
                if (nextState == State.Fight) {
                    fighting.goNext();
                    if (7.5 <= fighting.getFNo() && fighting.getFNo() <= 12) {
                        if (!attcking) {
                            getHitBoxList().add(handHB);
                            attcking = true;
                        }
                    } else {
                        if (attcking) {
                            getHitBoxList().remove(handHB);
                            attcking = false;
                        }

                    }

                    if (fighting.isFinshed()) {
                        setCurrState(State.Rest);
                        fightReq = false;
                        attcking = false;
                    }
                } else {
                    setCurrState(nextState);
                }
                break;
            case Walk_Left:
                if (nextState == State.Rest) {
                    walking.reset();
                    if (walking.isFinshed()) {
                        setCurrState(State.Rest);
                    }
                } else if (nextState == State.Walk_Left) {
                    walking.goNext();
                    setX(getX() - motationSpeed);
                } else {
                    setCurrState(nextState);
                }
                break;
            case Walk_Right:
                if (nextState == State.Rest) {
                    walking.reset();
                    if (walking.isFinshed()) {
                        setCurrState(State.Rest);
                    }
                } else if (nextState == State.Walk_Right) {
                    walking.goNext();
                    setX(getX() + motationSpeed);
                } else {
                    setCurrState(nextState);
                }
                break;

            case Hurt:
                hurting.goNext();
                if (hurting.isFinshed()) {
                    setCurrState(State.Rest);
                    fightReq = false;
                    hurtReq = false;
                }
                break;
            case Rest:
                setCurrState(nextState);
                break;
            //Rest end

        }
    }

    private State getNextState() {

        if (hurtReq) {

            return State.Hurt;
        }

        if (Key.space) {
            fightReq = true;
        }

        if (fightReq) {
            if (currState == State.Walk_Left || currState == State.Walk_Right) {
                return State.Rest;
            }
            return State.Fight;
        }

        if (Key.right) {
            return State.Walk_Right;
        }

        if (Key.left) {
            return State.Walk_Left;
        }

        return State.Rest;
    }

    private void setCurrState(State state) {
        switch (state) {
            case Fight:
                currAnima = fighting;
                break;

            case Walk_Left:
                setDirection(Direction.LEFT);
                break;
            case Walk_Right:
                setDirection(Direction.RIGHT);
                break;
            case Rest:
                currAnima = walking;
                break;
            case Hurt:
                currAnima = hurting;
                if (currState != State.Hurt) {
                    health.setValue(health.getValue() - 5);
                }
                break;

        }
        currAnima.reset();
        currState = state;

    }

    @Override
    public void setDirection(Direction newDirection) {
        if (getDirection() != newDirection) {
            super.setDirection(newDirection);
            fullBodyHB.setDirection(newDirection);
            handHB.setDirection(newDirection);
            pushHB.setDirection(newDirection);
            faceHB.setDirection(newDirection);

            fighting.setDirection(newDirection);
            walking.setDirection(newDirection);
            hurting.setDirection(newDirection);
        }
    }

    private void initHitBox() {
        handHB = new HitBox("Hand", 168, 50, 84, 35, this);
        fullBodyHB = new HitBox("Full_Body", 89, 10, 109, 219, this);
        pushHB = new HitBox("Push", 116, 10, 55, 219, this);
        faceHB = new HitBox("Face", 116, 8, 55, 69, this);

        fullBodyHB.setTrigger((collider) -> {
            GameObject owner = collider.getOwner();
            ID colliderID = owner.getId();
            if (colliderID == ID.SOLID) {

                Direction d = fullBodyHB.getDireRelativeTo(collider);
                if (d == Direction.RIGHT) {
                    setX(collider.getGlobalX_() - fullBodyHB.getX());
                } else if (d == Direction.LEFT) {
                    setX(collider.getGlobalX() - fullBodyHB.getX_());
                }
                if (currState == State.Walk_Left || currState == State.Walk_Right) {
                    walking.goPrevius();
                    walking.goRest();
                }

            }
        });

        handHB.setTrigger((collider) -> {
        });

        pushHB.setTrigger((collider) -> {
            if (collider.getOwner().getId() == ID.ZOMBI) {
                if (collider.getName().equals("Push")) {
                    Direction d = pushHB.getDireRelativeTo(collider);
                    float oldX = getX();
                    if (d == d.LEFT) {
                        int diff = pushHB.getGlobalX_() - collider.getGlobalX();
                        setX(getX() - diff * Z_PUSH_FORCE);
                    } else if (d == d.RIGHT) {
                        int diff = collider.getGlobalX_() - pushHB.getGlobalX();
                        setX(getX() + diff * Z_PUSH_FORCE);
                    }
                    if (currState == State.Walk_Left || currState == State.Walk_Right) {
                        float newspeed = (Math.abs(oldX - getX())) / motationSpeed;
                        float oldSpeed = walking.getAnimeSpeed();
                        walking.setAnimeSpeed(newspeed);
                        walking.goPrevius();
                        walking.setAnimeSpeed(oldSpeed);
                    }
                }
            }
        });

        faceHB.setTrigger((collider) -> {
            if (collider.getOwner().getId() == ID.ZOMBI) {
                if (collider.getName().equals("Hand")) {
                    hurtReq = true;
                    setDirection(collider.getDireRelativeTo(faceHB));
                }
            }
        });

        List<HitBox> list = getHitBoxList();
        list.add(fullBodyHB);
        list.add(pushHB);
        list.add(faceHB);

    }

    public int getCenterX() {
        return fullBodyHB.getGlobalCenterX();
    }

    public int getCenterY() {
        return fullBodyHB.getGlobalCenterY();
    }

    @Override

    public String toString() {
        return "StickMan";
    }

    private static enum State {
        Rest, Walk_Left, Walk_Right,
        Fight, Hurt
    }

}
