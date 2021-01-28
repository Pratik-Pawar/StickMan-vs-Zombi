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
public class STWalking extends DirectionalAnimation {

    private int rFNo1, rFNo2;
    // REST_SPEED value must have 0 < REST_SPEED <=1
    private final float REST_SPEED = 0.5f;

    public STWalking() {
        super(1.0f, "/Animaction/", "ST Walking", 48, Direction.RIGHT);

        rFNo1 = 0;
        rFNo2 = FRAME_COUNT / 2;
    }

    @Override
    public boolean isFinshed() {
        int index = FNoToIndex();
        return (index == rFNo1) || (index == rFNo2);
    }

    public void goRest() {
        float FNo = getFNo();
        int x = (int) ((FNo - rFNo1) / FRAME_COUNT);
        int lower = rFNo1 + x * FRAME_COUNT;
        int upper = rFNo1 + (x + 1) * FRAME_COUNT;

        int targetFNo;
        if (Math.abs(FNo - lower) < Math.abs(FNo - upper)) {
            targetFNo = lower;
        } else {
            targetFNo = upper;
        }

        x = (int) ((FNo - rFNo2) / FRAME_COUNT);

        lower = rFNo2 + x * FRAME_COUNT;
        upper = rFNo2 + (x + 1) * FRAME_COUNT;

        if (Math.abs(FNo - lower) < Math.abs(FNo - targetFNo)) {
            targetFNo = lower;
        }

        if (Math.abs(FNo - upper) < Math.abs(FNo - targetFNo)) {
            targetFNo = upper;
        }

        FNo = FNo + ((targetFNo - FNo) * REST_SPEED);
        setFNo(FNo);
    }
}
