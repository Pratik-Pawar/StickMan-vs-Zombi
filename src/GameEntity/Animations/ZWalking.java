/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameEntity.Animations;

import GameEntity.Direction;

/**
 *
 * @author Administrator
 */
public class ZWalking extends DirectionalAnimation {

    private int rFNo1, rFNo2;
    // REST_SPEED value must have 0 < REST_SPEED <=1
    private final float REST_SPEED = 0.5f;

    public ZWalking(Direction direction) {
        super(0.3f, "/Animaction/", "Z Walking", 25, direction);

        rFNo1 = 0;
        rFNo2 = FRAME_COUNT;
    }

    @Override
    public boolean isFinshed() {
        int index = FNoToIndex();
        return (index == rFNo1) || (index == rFNo2);
    }

    public void goRest() {
        float xFNo = getFNo();
        float targetFNo;
        if (xFNo < (this.FRAME_COUNT / 2)) {
            targetFNo = 0;
        } else {
            targetFNo = FRAME_COUNT;
        }

        xFNo = xFNo + ((targetFNo - xFNo) * REST_SPEED);
        setFNo(xFNo);
    }

}
