package com.tamola.aoc.aoc2021;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Solution17 {
    private static String INPUT_URI = "aoc2021/src/main/java/com/tamola/aoc/aoc2021/sampleinput17.txt";
    //private static String INPUT_URI = "aoc2021/src/main/java/com/tamola/aoc/aoc2021/input17.txt";
    /**
     * algo:
     *  - come up with algo to determine if probe is within
     *      target area, based on initial x & y velocity inputs
     *  - then do a binary search to find initial velocities, such
     *      that probe will reach highest y position and still hit 
     *      target area (any part of it) within any step
     *  - sim the probe position at each step
     *  - what is the search space for x & y initial velocities???
     */
    private void run(int velX, int velY) {
        List<String> inDatz = readInput(INPUT_URI);
        List<Integer> targetDatz = makeTargetArea(inDatz.get(0)); // 0: x_lo, 1: x_hi, 2: y_lo, 3: y_hi
        // TODO: search for velX and velY that results in highest y position
        //       and still hits target
        System.out.println(launchProbe(velX, velY, targetDatz));
    }
    private List<Integer> makeTargetArea(String targetData) {
        String[] tdz = targetData.split(" ");
        String xDatz = tdz[2];
        String yDatz = tdz[3];
        //System.out.println("xDatz = " + xDatz + " yDatz = " + yDatz);
        List<Integer> targetArea = new ArrayList<>();
        StringBuilder xDatzz = new StringBuilder(xDatz);
        xDatzz.deleteCharAt(xDatz.length() - 1);
        String[] xDatzSplitz = xDatzz.toString().split("\\=");
        String[] yDatzSplitz = yDatz.split("\\=");
        String[] xDatzRangeSplitz = xDatzSplitz[1].split("\\.\\.");
        String[] yDatzRangeSplitz = yDatzSplitz[1].split("\\.\\.");
        targetArea.add(Integer.parseInt(xDatzRangeSplitz[0]));
        targetArea.add(Integer.parseInt(xDatzRangeSplitz[1]));
        targetArea.add(Integer.parseInt(yDatzRangeSplitz[0]));
        targetArea.add(Integer.parseInt(yDatzRangeSplitz[1]));
        //System.out.println("targetArea = " + targetArea.toString());
        return targetArea;
    }
    /**
     * algo:
     *  - how to determine when should stop sim?
     *      - when posX is past right boundary of target
     *      - when posY is past bottom boundary of target
     * 
     * @param velX
     * @param velY
     * @param target
     * @return
     */
    private boolean launchProbe(int velX, int velY, List<Integer> target) {
        int posX = 0;
        int posY = 0;
        while (posX <= target.get(1) && posY >= target.get(2)) {
            //System.out.println("posX = " + posX + " posY = " + posY + " velX = " + velX + " velY = " + velY);
            if (posX >= target.get(0) && posX <= target.get(1) && posY >= target.get(2) && posY <= target.get(3)) {
                return true;
            }
            posX += velX;
            posY += velY;
            velX = velX > 0 ? velX - 1 : velX < 0 ? velX + 1 : 0;
            velY -= 1;
        }
        return false;
    }
    private List<String> readInput(String uri) {
        List<String> inDatz = new ArrayList<>();
        try (Stream<String> inDatzLinez = Files.lines(Paths.get(uri))) {
            inDatzLinez.forEach(l -> inDatz.add(l));
        } catch (IOException ex) {
            ex.printStackTrace();
        }    
        return inDatz;
    }
    public static void main(String[] args) {
        Solution17 soln = new Solution17();
        soln.run(7, 2);
        soln.run(6, 3);
        soln.run(9, 0);
        soln.run(17, -4);
    }
}
