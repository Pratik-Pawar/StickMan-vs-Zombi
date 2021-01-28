/*
 *ReChanged Zombi
test
 */
package GameEntity;

import GameEntity.Animations.DirectionalAnimation;
import GameEntity.Animations.ZWalking;
import static GameEntity.ID.ZOMBI;
import GameEntity.collisionDetection.HitBox;
import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;
import mainComponent.Controls.SliderControl;
import mainComponent.Key;

/**
 *
 * @author Administrator
 */
public class Zombi extends MovingObject {

    private DirectionalAnimation fighting, hurting, currAnima;
    private ZWalking walking;
    private HitBox faceHB, fullBodyHB, pushHB, handHB;
    private State currState;
    private boolean hurtReq = false, fightReq = false, attcking = false;
    private static int objCount = 0;
    private SliderControl xControl;
    private static int hurtMotion[] = {-0, -19, -35, -39, -43, -47, -50, -53, -56, -53, -61, -63, -63, -58, -53, -48, -44, -40, -40};
    private static int fightMotion[] = {0, 5, 10, 14, 20, 23, 34, 51, 56, 53, 50, 46, 39, 33, 27, 20, 13, 7, 0, 0};
    private float initX = 0;
    private float ST_PUSH_FORCE = 0.3f;
    private int objNo;
    private StickMan stickMan;
    private Random rand;
    private int changeDireCount, changeDireTarget;
    private int min = 180, max = 900;
    private HealthBar health;
    private int healthRX, healthLX;

    public Zombi(Direction direction, float x, float y, StickMan stickMan) {
        super(1f, direction, x, y, ZOMBI);

        this.stickMan = stickMan;
        objNo = ++objCount;
        walking = new ZWalking(direction);
        fighting = new DirectionalAnimation(0.3f, "/Animaction/", "Z Fighting", 20, direction);
        hurting = new DirectionalAnimation(0.4f, "/Animaction/", "Z Hurt", 19, direction);
        rand = new Random(objNo);
        changeDireCount = 0;
        changeDireTarget = rand.nextInt(max - min) + min;
        currAnima = walking;

        if (getDirection() == Direction.LEFT) {
            currState = State.WALK_LEFT;

        } else if (getDirection() == Direction.RIGHT) {
            currState = State.WALK_RIGHT;
        }

        setX(x);
        setY(y);
        setSpeed(1.0f);

        SliderControl.createAndShow("ST_PUSH_FORCE", ST_PUSH_FORCE, 0, 2, 0.1f).
                SetChangeListener((source, newValue) -> {
                    ST_PUSH_FORCE = newValue;
                });
        SliderControl.createAndShow("Zombi", 1.0f, 0.01f, 5f, 0.05f)
                .SetChangeListener((SliderControl source, float newValue) -> {
                    setSpeed(newValue);
                });

        setWidth(walking.getWidth());
        setHeight(walking.getHeight());

        health = new HealthBar(127, 17, 76, 7,
                1.5f, 100, Color.green);
        healthRX = health.getIntX();
        healthLX = this.getWidth() - health.getIntX() - health.getWidth();
        if (getDirection() == Direction.LEFT) {
            health.setX(healthLX);
        }

        initHitBox();
        xControl = SliderControl.createAndShow("Zombi x:", getIntX(), -200, 1000, 1);
        xControl.SetChangeListener((source, newValue) -> {
            setX(newValue);
        });
    }

    @Override
    public void setX(float x) {
        super.setX(x);
        if (xControl != null) {
            xControl.setValue(getIntX());
        }
    }

    @Override
    public void setSpeed(float speed) {
        this.speed = speed;
        motationSpeed = MOTATION_UNIT_SPEED * speed;
        walking.setAnimeSpeed(speed);
        fighting.setAnimeSpeed(speed);
        hurting.setAnimeSpeed(speed);
    }

    private void initHitBox() {

        faceHB = new HitBox("Face", 127, 32, 71, 56, this);
        fullBodyHB = new HitBox("Full_Body", 72, 32, 136, 195, this);
        pushHB = new HitBox("Push", 108, 32, 87, 195, this);
        handHB = new HitBox("Hand", 151, 23, 64, 133, this);

        fullBodyHB.setTrigger((collider) -> {
            if (collider.getOwner().getId() == ID.SOLID) {

                Direction d = fullBodyHB.getDireRelativeTo(collider);
                if (d == Direction.RIGHT) {
                    setX(collider.getGlobalX_() - fullBodyHB.getX());
                } else if (d == Direction.LEFT) {
                    setX(collider.getGlobalX() - fullBodyHB.getX_());
                }

                if (currState == State.WALK_LEFT || currState == State.WALK_RIGHT) {
                    if (d == Direction.LEFT) {
                        currState = State.WALK_LEFT;
                    } else if (d == Direction.RIGHT) {
                        currState = State.WALK_RIGHT;
                    }
                }
            }

        });

        faceHB.setTrigger((collider) -> {
            if (collider.getOwner().getId() == ID.STICK_MAN) {
                if (collider.getName().equals("Hand")) {
                    hurtReq = true;
                    Direction d = ((StickMan) collider.getOwner()).getDirection();
                    if (d == Direction.LEFT) {
                        setDirection(Direction.RIGHT);
                    }
                    if (d == Direction.RIGHT) {
                        setDirection(Direction.LEFT);
                    }
                }
            }
        });
        pushHB.setTrigger((collider) -> {
            if (collider.getOwner().getId() == ID.STICK_MAN) {
                if (collider.getName().equals("Full_Body")) {
                    fightReq = true;

                }
                if (collider.getName().equals("Push")) {
                    Direction d = pushHB.getDireRelativeTo(collider);
                    if (d == d.LEFT) {
                        int diff = pushHB.getGlobalX_() - collider.getGlobalX();
                        setX(getX() - diff * ST_PUSH_FORCE);
                    } else if (d == d.RIGHT) {
                        int diff = collider.getGlobalX_() - pushHB.getGlobalX();
                        setX(getX() + diff * ST_PUSH_FORCE);

                    }

                }
            }
        });

        getHitBoxList().add(faceHB);
        getHitBoxList().add(fullBodyHB);
        getHitBoxList().add(pushHB);
    }

    @Override
    public void setDirection(Direction newDirection) {
        if (getDirection() != newDirection) {
            super.setDirection(newDirection);
            faceHB.setDirection(newDirection);
            handHB.setDirection(newDirection);
            pushHB.setDirection(newDirection);
            fullBodyHB.setDirection(newDirection);

            walking.setDirection(newDirection);
            fighting.setDirection(newDirection);
            hurting.setDirection(newDirection);

            if (getDirection() == Direction.LEFT) {
                health.setX(healthLX);
            } else if (getDirection() == Direction.RIGHT) {
                health.setX(healthRX);
            }
        }

    }

    @Override
    public void render(Graphics g) {
        g.drawImage(currAnima.getFrame(), getIntX(), getIntY(), null);
        health.render(g, getIntX(), getIntY());

    }

    private State getNextState() {
        if (hurtReq || Key.ctrl) {
            return State.HURT;
        }
        if (fightReq || Key.alt) {
            return State.FIGHT;
        }
        return currState;
    }

    @Override
    public void update() {
        State nextState = getNextState();
        switch (currState) {
            case WALK_RIGHT:
                changeDireCount++;

                if (nextState != State.WALK_RIGHT) {
                    setCurrState(nextState);
                } else {

                    if (changeDireCount >= changeDireTarget) {

                        setCurrState(State.WALK_LEFT);

                        changeDireCount = 0;
                        changeDireTarget = rand.nextInt(max - min) + min;
                    }

                    setX(getX() + motationSpeed);
                    setDirection(Direction.RIGHT);
                    walking.goNext();
                }

                break;

            case WALK_LEFT:
                changeDireCount++;
                if (nextState != State.WALK_LEFT) {
                    setCurrState(nextState);
                } else {
                    if (changeDireCount >= changeDireTarget) {
                        setCurrState(State.WALK_RIGHT);
                        changeDireCount = 0;
                        changeDireTarget = rand.nextInt(max - min) + min;
                    }
                    setX(getX() - motationSpeed);
                    setDirection(Direction.LEFT);
                    walking.goNext();
                }
                break;

            case HURT:
                hurting.goNext();
                if (getDirection() == Direction.RIGHT) {
                    setX(initX + hurtMotion[hurting.getIntFNo()]);
                } else if (getDirection() == Direction.LEFT) {
                    setX(initX - hurtMotion[hurting.getIntFNo()]);
                }

                if (hurting.isFinshed()) {
                    if (getDirection() == Direction.RIGHT) {
                        setX(initX + hurtMotion[hurtMotion.length - 1]);
                    } else if (getDirection() == Direction.LEFT) {
                        setX(initX - hurtMotion[hurtMotion.length - 1]);
                    }

                    hurtReq = false;
                    fightReq = false;

                    if (getDirection() == Direction.RIGHT) {
                        setCurrState(State.WALK_RIGHT);

                    } else if (getDirection() == Direction.LEFT) {
                        setCurrState(State.WALK_LEFT);

                    }
                }
                break;

            case FIGHT:
                fighting.goNext();
                Direction d = getDirection();
                int FNo = fighting.getIntFNo();
                if (6 <= FNo && FNo <= 9) {

                    if (!attcking) {
                        attcking = true;
                        getHitBoxList().add(handHB);
                    }
                } else if (attcking) {
                    attcking = false;
                    getHitBoxList().remove(handHB);
                }
                if (d == Direction.RIGHT) {
                    setX(initX + fightMotion[fighting.getIntFNo()]);
                } else if (d == Direction.LEFT) {
                    setX(initX - fightMotion[fighting.getIntFNo()]);
                }

                if (nextState == State.HURT) {
                    fightReq = false;
                    getHitBoxList().remove(handHB);
                    setCurrState(nextState);
                } else if (fighting.isFinshed()) {

                    fightReq = false;
                    getHitBoxList().remove(handHB);

                    if (getDirection() == Direction.LEFT) {
                        setCurrState(State.WALK_LEFT);
                    } else if (getDirection() == Direction.RIGHT) {
                        setCurrState(State.WALK_RIGHT);
                    }
                }

                break;
        }

    }

    public State getCurrState() {
        return currState;
    }

    private void setCurrState(State state) {
        switch (state) {
            case HURT:
                currAnima = hurting;
                initX = getX();
                health.setValue(health.getValue() - 20);
                break;
            case FIGHT:
                if (this.getCenterX() < stickMan.getCenterX()) {
                    setDirection(Direction.RIGHT);
                } else {
                    setDirection(Direction.LEFT);
                }
                initX = getX();
                currAnima = fighting;
                break;

            case WALK_LEFT:
            case WALK_RIGHT:
                currAnima = walking;
                break;

        }
        currAnima.reset();
        currState = state;

    }

    public int getCenterX() {
        return fullBodyHB.getGlobalCenterX();
    }

    public int getCenterY() {
        return fullBodyHB.getGlobalCenterY();
    }

    @Override
    public String toString() {
        return "Zombi #" + objNo;
    }

    public static enum State {
        WALK_LEFT, WALK_RIGHT,
        FIGHT, HURT

    }

}
