package com.tamola.aoc.aoc2021;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Stream;

public class Solution11 {
    //private static String INPUT_URI = "aoc2021/src/main/java/com/tamola/aoc/aoc2021/sampleinput11.txt";
    //private static String INPUT_URI = "aoc2021/src/main/java/com/tamola/aoc/aoc2021/sampleinput8_2.txt";
    private static String INPUT_URI = "aoc2021/src/main/java/com/tamola/aoc/aoc2021/input11.txt";
    /**
     * algo:
     *  - simulate octopus flashing
     *  - at each step:
     *      - increment each octopus energy level by 1
     *      - if energy level is currently 9, then octopus flashes and energy level goes to 0
     *          - increment each adjacent octopus in the 8 directions by 1, and if an adjacent
     *              octopus energy level is currently 9, it also flashes and its energy level
     *              goes to 0 and all of its adjacent octopuses energy level increases by 1,
     *              and if those adjacent octopuses energy level is currently 9, then it
     *              it also flashes, etc
     *      - scan each cell to see which ones should flash, for each one, add that cell to
     *          a queue; at end of scan, flash each one in queue and then add new cells
     *          that flash to the queue; work off queue until no more to flash, then
     *          can proceed to next step
     * 
     */
    public void run() {
        List<String> inDatz = readInput(INPUT_URI);
        List<List<Integer>> octopuses = makeOctopuses(inDatz);
        Queue<int[]> q = new LinkedList<>();
        int step = 0;
        int flashCount = 0;
        int maxSteps = 100;
        int[][] dirz = new int[][] {
            {-1,0},{-1,1},{0,1},{1,1},{1,0},{1,-1},{0,-1},{-1,-1}
        };
        while (step < maxSteps) {
            System.out.println("(pre) step = " + step + " octopuses = ");
            for (List<Integer> l : octopuses) {
                System.out.println(l.toString());
            }
            for (int i = 0; i < octopuses.size(); i++) {
                for (int j = 0; j < octopuses.get(0).size(); j++) {
                    octopuses.get(i).set(j, octopuses.get(i).get(j) + 1);
                    if (octopuses.get(i).get(j) > 9) {
                        q.offer(new int[] { i, j });
                    }
                }
            }
            // for octopus in the q, flash it and increment all adjacents
            // by 1 (and if those will flash, add those to the q); then
            // set its energy level to 0
            Map<Integer, Set<Integer>> flashedz = new HashMap<>();
            while (!q.isEmpty()) {
                System.out.println("(mid) step = " + step);
                System.out.println("flashedz = " + flashedz.toString());
                System.out.println("octopuses = ");
                for (List<Integer> l : octopuses) {
                    System.out.println(l.toString());
                }    
                printQ(q);
                int sz = q.size();
                for (int i = 0; i < sz; i++) {
                    int[] octo = q.poll();
                    //System.out.println("octo = " + Arrays.toString(octo));
                    if (flashedz.containsKey(octo[0]) && flashedz.get(octo[0]).contains(octo[1])) {
                        continue;
                    }
                    octopuses.get(octo[0]).set(octo[1], 0);
                    flashCount++;
                    flashedz.computeIfAbsent(octo[0], k -> new HashSet<>()).add(octo[1]);
                    for (int[] dir : dirz) {
                        int nextR = octo[0] + dir[0];
                        int nextC = octo[1] + dir[1];
                        if (nextR >= 0 && nextR < octopuses.size() && nextC >= 0 && nextC < octopuses.get(0).size()) {
                            
                            if (flashedz.containsKey(nextR) && flashedz.get(nextR).contains(nextC)) {
                                //System.out.println("got here...");
                                continue;
                            }
                            
                            octopuses.get(nextR).set(nextC, octopuses.get(nextR).get(nextC) + 1);
                            if (octopuses.get(nextR).get(nextC) > 9) {
                                q.offer(new int[] { nextR, nextC });
                            }
                        }
                    }
                }
            }
            step++;
            System.out.println("(post) step = " + step + " octopuses = ");
            for (List<Integer> l : octopuses) {
                System.out.println(l.toString());
            }
        }
        System.out.println(flashCount);
    }
    private List<List<Integer>> makeOctopuses(List<String> inDatz) {
        List<List<Integer>> octopuses = new ArrayList<>();
        for (String s : inDatz) {
            List<Integer> octoz = new ArrayList<>();
            for (char c : s.toCharArray()) {
                octoz.add(Integer.parseInt(Character.toString(c)));
            }
            octopuses.add(octoz);
        }
        return octopuses;
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
    private void printQ(Queue<int[]> q) {
        System.out.println("q = ");
        var it = q.iterator();
        while (it.hasNext()) {
            var e = it.next();
            System.out.print(Arrays.toString(e) + " ");
        }
        System.out.println();
    }
    public static void main(String[] args) {
        Solution11 soln = new Solution11();
        soln.run();
    }
}
