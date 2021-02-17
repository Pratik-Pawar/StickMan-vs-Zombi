/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mainComponent;

import GameEntity.*;
import GameEntity.collisionDetection.HitBox;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import mainComponent.Controls.SliderControl;

/**
 *
 * @author prati
 */
public class Controller implements Runnable {

    Timer gameLoopTimer;
    private GameMainFrame gameMainFrame;
    private Canvas canvas;
    private int width = 1100, height = width * 9 / 16;
    private boolean running;
    private Thread gameThread;
    private BufferStrategy bs;
    private Graphics g;
    private int fps = 60;
    private StickMan stickMan;
    private SliderControl fpsMeter;
    private List gameObjectList;
    private List movingObjectList;
    private List<StaticObject> staticObjectsList;
    private List<Zombi> zombiList;
    private JCheckBox collisionDetectionCheckBox,
            drawCollisionBoxCheckBox, pauseGame;
    private JButton collisionButton, next;

    public Controller() {

        gameMainFrame = new GameMainFrame(width, height);
        drawCollisionBoxCheckBox = new JCheckBox("Draw Collision Box");
        drawCollisionBoxCheckBox.setSelected(false);

        pauseGame = new JCheckBox("Pause Game");
        next = new JButton("Next");
        next.setEnabled(false);
        next.addActionListener((e) -> {
            update();

        });
        pauseGame.addActionListener((e) -> {
            next.setEnabled(pauseGame.isSelected());
        });

        collisionButton = new JButton("Collision");
        collisionDetectionCheckBox = new JCheckBox("Stop collision Detection");

        collisionDetectionCheckBox.addActionListener((e) -> {
            collisionButton.setEnabled(collisionDetectionCheckBox.isSelected());
        });
        collisionButton.addActionListener((e) -> {
            collisionDetection();
        });

        collisionDetectionCheckBox.setSelected(false);

        GameMainFrame.controlPanel.add(pauseGame);
        GameMainFrame.controlPanel.add(next);

        GameMainFrame.controlPanel.add(collisionDetectionCheckBox);
        GameMainFrame.controlPanel.add(collisionButton);

        GameMainFrame.controlPanel.add(drawCollisionBoxCheckBox);
        fpsMeter = SliderControl.createAndShow("FPS", fps, 1, 60, 1);
        fpsMeter.SetChangeListener((SliderControl source, float newValue) -> {
            fps = Math.round(newValue);
        });

        canvas = new Canvas();
        running = false;

        gameObjectList = new LinkedList<>();
        movingObjectList = new LinkedList<>();
        zombiList = new LinkedList<>();
        staticObjectsList = new LinkedList<>();
        gameObjectList.add(movingObjectList);
        gameObjectList.add(staticObjectsList);

        stickMan = new StickMan(width / 3, height - 230);

        movingObjectList.add(stickMan);
        movingObjectList.add(zombiList);

        Zombi z = new Zombi(Direction.RIGHT, 0, height - 230, stickMan);
        zombiList.add(z);

        z = new Zombi(Direction.LEFT, 0, height - 230, stickMan);
        zombiList.add(z);

        z = new Zombi(Direction.RIGHT, 800, height - 230, stickMan);
        zombiList.add(z);

        z = new Zombi(Direction.RIGHT, 200, height - 230, stickMan);
        zombiList.add(z);
        staticObjectsList.add(new Solid(0, 0, 30, 700, new Color(0, 0, 0, 50)));
        staticObjectsList.add(new Solid(1080, 0, 30, 700, new Color(0, 0, 0, 50)));

        canvas.setBackground(Color.WHITE);
        canvas.setSize(new Dimension(width, height));
        canvas.setPreferredSize(new Dimension(width, height));
        canvas.setMinimumSize(new Dimension(width, height));
        canvas.setMaximumSize(new Dimension(width, height));
        canvas.setFocusable(false);

        gameMainFrame.add(canvas);

    }

    @Override
    public void run() {

        double delta = 0;
        long now;
        long lastTime = System.nanoTime();
        long timer = 0;
        int ticks = 0;

        while (running) {
            double timePerTick = 1000000000 / fps;

            now = System.nanoTime();
            delta += (now - lastTime) / timePerTick;
            timer += now - lastTime;
            lastTime = now;

            if ((delta >= 1)) {

                if (!pauseGame.isSelected()) {
                    update();
                }
                render();
                ticks++;
                delta--;
            }

            if (timer >= 1000000000) {
                gameMainFrame.setTitle("FPS : " + ticks);
                ticks = 0;
                timer = 0;
            }
        }

        stopGame();

    }

    public synchronized void startGame() {
        if (running) {
            return;
        }
        running = true;
        gameThread = new Thread(this, "Game Thread");
        gameThread.start();
    }

    public synchronized void stopGame() {
        if (!running) {
            return;
        }
        running = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void render() {
        bs = canvas.getBufferStrategy();
        if (bs == null) {
            canvas.createBufferStrategy(3);
            return;
        }
        g = bs.getDrawGraphics();

        //Clear Screen
        g.clearRect(0, 0, width, height);

        //Draw Here!
        drawHitBox(g);

        //drawind all GameObject from gameObjectList
        forEachGameObject(gameObjectList, (obj, itr) -> {
            obj.render(g);
        });
        // drawing lines

        Random rand = new Random(0);

        for (int i = 0; i < 50; i++) {
            Color color = new Color(rand.nextInt(3) * 20, rand.nextInt(3) * 20, rand.nextInt(3) * 20, 100);
            g.setColor(color);
            g.drawLine(i * (width / 50), height, i * (width / 50), height - 10);
        }

        //End Drawing!
        bs.show();
        g.dispose();
    }

    private void update() {
        gameMainFrame.requestFocus();

        if (Key.esc) {
            System.exit(0);
        }
        forEachGameObject(gameObjectList, (obj, itr) -> {
            obj.update();
            if (obj.deleteMe()) {
                itr.remove();
            }
        });
        if (!collisionDetectionCheckBox.isSelected()) {
            collisionDetection();
        }

//finalizeing moving objects
/*        forEachGameObject(movingObjectList, (obj) -> {
            ((MovingObject) obj).finalize();
        });
         */
    }

    private void collisionDetection() {
        forEachGameObject(movingObjectList, (mainObj, mainItr) -> {

            forEachGameObject(gameObjectList, (colliderObj, colliderItr) -> {
                if (mainObj != colliderObj) {
                    for (HitBox mainHitBox : mainObj.getHitBoxList()) {
                        for (HitBox colliderHitBox : colliderObj.getHitBoxList()) {
                            mainHitBox.collisionDetection(colliderHitBox);

                        }
                    }
                }
            });
        });
    }

    private void drawHitBox(Graphics g) {
        if (drawCollisionBoxCheckBox.isSelected()) {
            forEachGameObject(gameObjectList, (gameObject, itr) -> {
                gameObject.getHitBoxList().forEach((t) -> {
                    t.render(g);
                });
            });
        }
    }

    private void forEachGameObject(List list, Task task) {
        if (list != null) {
            for (Iterator iterator = list.iterator(); iterator.hasNext();) {
                Object obj = iterator.next();
                if (obj instanceof List) {
                    forEachGameObject((List) obj, task);
                } else if (obj instanceof GameObject) {

                    task.performTask((GameObject) obj, iterator);
                }

            }
        }
    }

    public static void main(String args[]) {
        TimeMonitar.start("Total Loding");
        Controller controller = new Controller();
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                controller.gameMainFrame.setVisible(true);
                TimeMonitar.stopAndPrint("Total Loding");
            }
        });

        controller.startGame();
    }

    private interface Task {

        public void performTask(GameObject gameObject, Iterator iter);
    }

}
