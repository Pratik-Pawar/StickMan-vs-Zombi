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
import java.awt.Image;
import java.util.List;
import mainComponent.Controls.SliderControl;
import mainComponent.Key;

/**
 *
 * @author prati
 */
public class StickMan extends MovingObject {

    private STWalking walking;
    private DirectionalAnimation currAnima, fighting, hurting, death, deadBody, jumping, inAir, landing;
    private State currState;
    private boolean hurtReq = false, deathReq = false;
    private HitBox handHB, fullBodyHB, pushHB, faceHB;
    private boolean attcking;
    private static final int DAMAGE = 10;
    private float Z_PUSH_FORCE = 0.13f;
    private HealthBar health;
    private float dMotionSpeed = 10.7f;
    private int prvFNo;
    private float baseY, jumpV = -20f, currV = 0f, grvAcc = 0.8f;

    public StickMan(int x, int y) {
        super(3.3f, Direction.RIGHT, x, y, STICK_MAN);

        this.attcking = false;

        health = new HealthBar(300, 30, 400, 25, 3, 100,
                new Color(20, 20, 200));

        SliderControl.createAndShow("dMotionSpeed", dMotionSpeed, -15, 15, 0.1f).
                SetChangeListener((source, newValue) -> {
                    dMotionSpeed = newValue;
                });
        SliderControl.createAndShow("jumpV", jumpV, -50, 0, 0.1f).
                SetChangeListener((source, newValue) -> {
                    jumpV = newValue;
                });
        SliderControl.createAndShow("grvAcc", grvAcc, 0, 20, 0.1f).
                SetChangeListener((source, newValue) -> {
                    grvAcc = newValue;
                });

        walking = new STWalking();
        fighting = new DirectionalAnimation(0.55f, "/Animaction/", "ST Fighting", 15, Direction.RIGHT);
        hurting = new DirectionalAnimation(1.3f, "/Animaction/", "ST Hurting", 25, Direction.RIGHT);
        death = new DirectionalAnimation(0.5f, "/Animaction/", "ST Death", 100, Direction.RIGHT);
        jumping = new DirectionalAnimation(0.5f, "/Animaction/", "ST Jump", 6, Direction.RIGHT);
        inAir = new DirectionalAnimation(0.5f, "/Animaction/", "ST In Air", 18, Direction.RIGHT);
        landing = new DirectionalAnimation(0.5f, "/Animaction/", "ST Landing", 6, Direction.RIGHT);
        deadBody = new DirectionalAnimation(1.0f, "ST Death", Direction.LEFT) {

            @Override
            public Image getFrame() {
                return currFrames[currFrames.length - 1];
            }

        };
        currAnima = walking;

        setX(x);
        setY(y);
        baseY = y;
        setWidth(walking.getWidth());
        setHeight(walking.getHeight());

        initHitBox();

        setCurrState(State.REST);

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

            case FIGHT:
                if (nextState == State.FIGHT) {
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
                        setCurrState(State.REST);
                        attcking = false;
                    }
                } else {
                    setCurrState(nextState);
                    if (attcking) {
                        getHitBoxList().remove(handHB);
                        attcking = false;
                    }

                }
                break;
            case WALK_LEFT:
                if (nextState == State.REST) {
                    walking.goRest();
                    if (walking.isFinshed()) {
                        setCurrState(State.REST);
                    }
                } else if (nextState == State.WALK_LEFT) {
                    walking.goNext();
                    setX(getX() - motationSpeed);
                } else {
                    setCurrState(nextState);
                }
                break;
            case WALK_RIGHT:
                if (nextState == State.REST) {
                    walking.goRest();
                    if (walking.isFinshed()) {
                        setCurrState(State.REST);
                    }
                } else if (nextState == State.WALK_RIGHT) {
                    walking.goNext();
                    setX(getX() + motationSpeed);
                } else {
                    setCurrState(nextState);
                }
                break;

            case JUMP:

                if (nextState == State.JUMP) {
                    jumping.goNext();

                    if (Key.left) {
                        setX(getX() - motationSpeed);
                        setDirection(Direction.LEFT);
                    } else if (Key.right) {
                        setX(getX() + motationSpeed);
                        setDirection(Direction.RIGHT);
                    }

                    if (jumping.isFinshed()) {
                        currV = jumpV;
                        setCurrState(State.IN_AIR);
                        System.out.println(baseY + " " + getY() + " " + currV + " " + grvAcc);
                    }
                } else {
                    setCurrState(nextState);
                }
                break;

            case IN_AIR:
                inAir.goNext();

                if (Key.left) {
                    setX(getX() - motationSpeed);
                    setDirection(Direction.LEFT);
                } else if (Key.right) {
                    setX(getX() + motationSpeed);
                    setDirection(Direction.RIGHT);
                }

                setY(getY() + currV + grvAcc);
                currV = currV + grvAcc;
                if (getY() >= baseY) {
                    setCurrState(State.LAND);
                    getHitBoxList().add(faceHB);
                    setY(baseY);
                }
                break;

            case LAND:

                if (nextState == State.LAND) {
                    landing.goNext();

                    if (Key.left) {
                        setX(getX() - motationSpeed);
                        setDirection(Direction.LEFT);
                    } else if (Key.right) {
                        setX(getX() + motationSpeed);
                        setDirection(Direction.RIGHT);
                    }

                    if (landing.isFinshed()) {
                        setCurrState(State.REST);
                    }
                } else {
                    setCurrState(nextState);
                }

                break;

            case HURT:
                hurting.goNext();
                if (hurting.isFinshed()) {
                    setCurrState(State.REST);
                    hurtReq = false;
                    getHitBoxList().add(faceHB);
                }
                break;

            case DEATH:

                death.goNext();
                if (17 < death.getIntFNo() && death.getIntFNo() < 33) {
                    if (death.getIntFNo() != prvFNo) {
                        int diff = death.getIntFNo() - prvFNo;
                        if (getDirection() == Direction.RIGHT) {
                            setX(getX() + diff * dMotionSpeed);
                        } else if (getDirection() == Direction.LEFT) {
                            setX(getX() - diff * dMotionSpeed);
                        }

                        if (death.getIntFNo() == 32) {
                            getHitBoxList().remove(fullBodyHB);
                        }

                        prvFNo = death.getIntFNo();
                    }
                }

                if (death.isFinshed()) {
                    setCurrState(State.DEAD_BODY);
                    deathReq = false;
                }
                break;

            case DEAD_BODY:
                break;
            case REST:
                setCurrState(nextState);
                break;
            //Rest end

        }
    }

    private State getNextState() {

        if (deathReq) {
            return State.DEATH;
        }
        if (hurtReq) {

            return State.HURT;
        }

        if (Key.up) {
            return State.JUMP;
        }

        if (currState == State.JUMP || currState == State.LAND) {
            return currState;
        }

        if (Key.space || currState == State.FIGHT) {
            return State.FIGHT;
        }

        if (Key.right) {
            return State.WALK_RIGHT;
        }

        if (Key.left) {
            return State.WALK_LEFT;
        }

        return State.REST;
    }

    private void setCurrState(State state) {
        switch (state) {
            case FIGHT:
                currAnima = fighting;
                break;

            case WALK_LEFT:
                setDirection(Direction.LEFT);
                break;
            case WALK_RIGHT:
                setDirection(Direction.RIGHT);
                break;
            case REST:
                currAnima = walking;
                break;
            case HURT:
                currAnima = hurting;
                getHitBoxList().remove(faceHB);
                break;

            case JUMP:
                currAnima = jumping;

                break;

            case IN_AIR:
                getHitBoxList().remove(faceHB);
                currAnima = inAir;
                break;

            case LAND:
                currAnima = landing;
                break;

            case DEATH:
                currAnima = death;
                prvFNo = 17;
                getHitBoxList().remove(faceHB);
                getHitBoxList().remove(pushHB);
                break;
            case DEAD_BODY:
                currAnima = deadBody;
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
            jumping.setDirection(newDirection);
            inAir.setDirection(newDirection);
            landing.setDirection(newDirection);
            death.setDirection(newDirection);
            deadBody.setDirection(newDirection);
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
                if (currState == State.WALK_LEFT || currState == State.WALK_RIGHT) {
                    walking.goPrevius();
                    walking.goRest();
                }

            }
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
                    if (currState == State.WALK_LEFT || currState == State.WALK_RIGHT) {
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
                    health.setValue(health.getValue() - DAMAGE);
                    if (health.isDead()) {
                        deathReq = true;
                    } else {
                        hurtReq = true;
                    }
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
        REST, WALK_LEFT, WALK_RIGHT,
        JUMP, IN_AIR, LAND,
        FIGHT, HURT, DEATH, DEAD_BODY
    }

}
