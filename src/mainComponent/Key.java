/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mainComponent;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 *
 * @author prati
 */
public class Key implements KeyListener {

    public static boolean left, right, up, down, esc, space, a, s, d, w, ctrl, alt;

    @Override
    public void keyPressed(KeyEvent evt) {
        switch (evt.getKeyCode()) {

            case KeyEvent.VK_UP:
                up = true;
                break;
            case KeyEvent.VK_DOWN:
                down = true;
                break;
            case KeyEvent.VK_LEFT:
                left = true;
                break;
            case KeyEvent.VK_RIGHT:
                right = true;
                break;

            case KeyEvent.VK_ESCAPE:
                esc = true;
                break;

            case KeyEvent.VK_SPACE:
                space = true;
                break;

            case KeyEvent.VK_A:
                a = true;
                break;
            case KeyEvent.VK_S:
                s = true;
                break;
            case KeyEvent.VK_D:
                d = true;
                break;
            case KeyEvent.VK_W:
                w = true;
                break;

            case KeyEvent.VK_CONTROL:
                ctrl = true;
                break;

            case KeyEvent.VK_ALT:
                alt = true;
                break;

        }
    }

    @Override
    public void keyReleased(KeyEvent evt) {
        switch (evt.getKeyCode()) {
            case KeyEvent.VK_UP:
                up = false;
                break;
            case KeyEvent.VK_DOWN:
                down = false;
                break;
            case KeyEvent.VK_LEFT:
                left = false;
                break;
            case KeyEvent.VK_RIGHT:
                right = false;
                break;
            case KeyEvent.VK_ESCAPE:
                esc = false;
                break;
            case KeyEvent.VK_SPACE:
                space = false;
                break;

            case KeyEvent.VK_A:
                a = false;
                break;
            case KeyEvent.VK_S:
                s = false;
                break;
            case KeyEvent.VK_D:
                d = false;
                break;
            case KeyEvent.VK_W:
                w = false;
                break;
            case KeyEvent.VK_CONTROL:
                ctrl = false;
                break;
            case KeyEvent.VK_ALT:
                alt = false;
                break;

        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

}
