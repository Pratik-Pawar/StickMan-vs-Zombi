/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameEntity.Animations;

import GameEntity.Direction;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import mainComponent.Controls.ChangeListener;
import mainComponent.Controls.SliderControl;
import mainComponent.TimeMonitar;

/**
 *
 * @author Administrator
 */
public class DirectionalAnimation extends Animation implements ChangeListener {

    private Direction direction;
    //BufferedImage[0][i] -> RightFrames
    //BufferedImage[1][i] -> LeftFrames
    private static HashMap<String, BufferedImage[][]> frameMap = new HashMap<>();
    private BufferedImage rightFrames[], leftFrames[], currFrames[];
    private int width, height;

    private float FNo, privFNo;
    final int FRAME_COUNT;
    private SliderControl speedControl;

    public DirectionalAnimation(float ANIMATION_UNIT_SPEED, String name, Direction direction) {
        super(ANIMATION_UNIT_SPEED);
        this.setAnimeSpeed(1.0f);
        speedControl = SliderControl.createAndShow(name, ANIMATION_UNIT_SPEED, 0.01f, 2.0f, 0.01f);
        speedControl.SetChangeListener(this);

        FNo = privFNo = 0;
        BufferedImage image[][] = frameMap.get(name);
        if (image != null) {
            rightFrames = image[0];
            leftFrames = image[1];
            width = rightFrames[0].getWidth();
            height = rightFrames[0].getHeight();
        } else {
            System.err.println("Name does not exist");
            System.err.println("Name does not exist");
            System.err.println("Name does not exist");

            System.exit(0);
        }
        FRAME_COUNT = rightFrames.length;

        setDirection(direction);

    }

    public DirectionalAnimation(float ANIMATION_UNIT_SPEED, String path, String name, int frameCount, Direction direction) {
        super(ANIMATION_UNIT_SPEED);
        this.setAnimeSpeed(1.0f);

        speedControl = SliderControl.createAndShow(name, ANIMATION_UNIT_SPEED, 0.01f, 2.0f, 0.01f);
        speedControl.SetChangeListener(this);

        FNo = privFNo = 0;
        this.FRAME_COUNT = frameCount;
        BufferedImage image[][] = frameMap.get(name);
        if (image != null) {
            rightFrames = image[0];
            leftFrames = image[1];

        } else {
            loadImages(path, name, frameCount);
        }

        width = rightFrames[0].getWidth();
        height = rightFrames[0].getHeight();

        setDirection(direction);

    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
        if (direction.equals(Direction.RIGHT)) {
            currFrames = rightFrames;
        } else {
            currFrames = leftFrames;
        }
    }

    @Override
    void loadImages(String path, String name, int frameCount) {
        TimeMonitar.start(name + "Total Image Loding");
        rightFrames = new BufferedImage[frameCount];
        leftFrames = new BufferedImage[frameCount];
        //Loding Images to rightFrames
        TimeMonitar.start(name + " Image Loding");
        BufferedImage sheet = null;
        try {
            sheet = ImageIO.read(getClass().getResource(path + name + ".png"));
        } catch (Exception ex) {
            Logger.getLogger(DirectionalAnimation.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Error: Image does not loded");
            System.err.println("Error: Image does not loded");
            System.err.println("Error: Image does not loded");
            System.exit(1);
        }
        TimeMonitar.stopAndPrint(name + " Image Loding");
        TimeMonitar.start(name + " Sheet cutting");

        this.height = sheet.getHeight();
        this.width = sheet.getWidth() / frameCount;

        for (int i = 0; i < frameCount; i++) {
            rightFrames[i] = sheet.getSubimage(i * width, 0, width, height);
        }

        TimeMonitar.start(name + " Sheet cutting");
        TimeMonitar.start(name + " Image Mirroring");

        BufferedImage mirrorSheet = convertToMirrorImage(sheet);

        for (int i = 0; i < frameCount; i++) {
            leftFrames[frameCount - i - 1] = mirrorSheet.getSubimage(i * width, 0, width, height);
        }

        TimeMonitar.stopAndPrint(name + " Image Mirroring");
        TimeMonitar.stopAndPrint(name + "Total Image Loding");

        //Adding both frames into frameMap
        //BufferedImage[0][i] -> RightFrames
        //BufferedImage[1][i] -> LeftFrames
        frameMap.put(name, new BufferedImage[][]{rightFrames, leftFrames});
//        System.out.println(name + ": Width:" + leftFrames[0].getWidth() + " Height:" + leftFrames[0].getHeight());
    }

    private BufferedImage convertToMirrorImage(BufferedImage simg) {

        int width = simg.getWidth();
        int height = simg.getHeight();
        // BufferedImage for mirror image
        BufferedImage mimg = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_ARGB);

        // Create mirror image pixel by pixel
        for (int y = 0; y < height; y++) {
            for (int lx = 0, rx = width - 1; lx < width; lx++, rx--) {
                // lx starts from the left side of the image
                // rx starts from the right side of the image
                // lx is used since we are getting pixel from left side
                // rx is used to set from right side
                // get source pixel value
                int p = simg.getRGB(lx, y);

                // set mirror image pixel value
                mimg.setRGB(rx, y, p);
            }
        }
        return mimg;
    }

    @Override
    public Image getFrame() {
        return currFrames[toIndex(getFNo())];
    }

    private int toIndex(float xFNo) {
        return (Math.round(xFNo) % FRAME_COUNT);
    }

    public int getIntFNo() {
        return toIndex(getFNo());
    }

    int FNoToIndex() {
        return toIndex(this.FNo);
    }

    @Override
    public void goNext() {
        setFNo(getFNo() + animeSpeed);

    }

    public float getFNo() {
        return FNo;
    }

    public void setFNo(float nextFNo) {
        nextFNo = nextFNo % FRAME_COUNT;
        if (nextFNo < 0) {
            nextFNo = FRAME_COUNT + nextFNo;
        }
        if (this.FNo != nextFNo) {
            privFNo = this.FNo;
            this.FNo = nextFNo;
        }
    }

    @Override
    public void goPrevius() {
        setFNo(getFNo() - animeSpeed);

    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public void setAnimeSpeed(float animeSpeed) {
        super.setAnimeSpeed(animeSpeed);
    }

    public boolean isFinshed() {
        return (toIndex(privFNo) != 0) && (toIndex(FNo) == 0);
    }

    public void reset() {
        privFNo = FNo = 0.0f;
    }

    @Override
    public void onChange(SliderControl source, float newValue) {

        setAnimeSpeed(newValue / ANIMATION_UNIT_SPEED);

    }
}
