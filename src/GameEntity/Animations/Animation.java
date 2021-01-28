/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameEntity.Animations;

import java.awt.Image;

/**
 *
 * @author prati
 */
public abstract class Animation {

    float animeSpeed;
    final float ANIMATION_UNIT_SPEED;

    public Animation(float ANIMATION_UNIT_SPEED) {
        this.ANIMATION_UNIT_SPEED = ANIMATION_UNIT_SPEED;

    }

    public float getAnimeSpeed() {
        return animeSpeed / ANIMATION_UNIT_SPEED;
    }

    public void setAnimeSpeed(float animeSpeed) {
        this.animeSpeed = animeSpeed * ANIMATION_UNIT_SPEED;

    }

    abstract public Image getFrame();

    abstract public void goNext();

    abstract public void goPrevius();

    abstract void loadImages(String path, String name, int frameCount);

}
