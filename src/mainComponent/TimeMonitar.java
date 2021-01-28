/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mainComponent;

import java.util.HashMap;

/**
 *
 * @author Administrator
 */
public class TimeMonitar {

    static private HashMap<String, Long> map = new HashMap<>(20);

    public static void start(String tag) {
        Long time = System.currentTimeMillis();
        map.put(tag, time);
    }

    public static void stopAndPrint(String tag) {
        Long currTime = System.currentTimeMillis();
        Long lastTime = map.get(tag);
        Long timeDiff = currTime - lastTime;
        System.err.println(tag + " Time: " + (timeDiff / 1000.0));
    }
}
