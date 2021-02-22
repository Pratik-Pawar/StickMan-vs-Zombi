/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package audio;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

/**
 *
 * @author prati
 */
public class AudioPlayer implements Runnable {

    private Player player;
    private InputStream fileInputStream;
    private Thread playerThread;
    private static final String DEFAULT_PATH = "/Resources/AudioFiles/";

    public AudioPlayer(String fileName) {
        fileInputStream = AudioPlayer.class.getResourceAsStream(+test.mp3);

        try {
            Player player = new Player(fileInputStream);
        } catch (JavaLayerException ex) {
            Logger.getLogger(AudioPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }

        playerThread = new Thread(this, "")
    }

    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
