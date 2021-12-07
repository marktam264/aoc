package com.tamola.aoc.aoc2021;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Solution6 {
    /**
     * algo:
     *  - fish spawns new fish every 7 days
     *  - given a list of fish with their starting day
     *  - when a fish's internal timer (days left) is 0, reset it to 6,
     *      and create a new fish with internal timer of 8
     *  - run a sim loop that decrements each fish's timer by 1 each loop
     *  - data structure:
     *      - initial thought is to use a list and keep adding to the list for each new
     *          fish 
     */
    //private static String INPUT_URI = "aoc2021/src/main/java/com/tamola/aoc/aoc2021/sampleinput6_1.txt";
    //private static String INPUT_URI = "aoc2021/src/main/java/com/tamola/aoc/aoc2021/sampleinput6.txt";
    private static String INPUT_URI = "aoc2021/src/main/java/com/tamola/aoc/aoc2021/input6.txt";
    private void runPart1(int days) {
        List<String> inDatz = readInput(INPUT_URI);
        List<Integer> fish = makeFish(inDatz);
        for (int i = 0; i < days; i++) {
            int j = 0;
            int newbz = 0;
            while (j < fish.size()) {
                fish.set(j, fish.get(j) - 1);
                if (fish.get(j) < 0) {
                    fish.set(j, 6);
                    newbz++;
                }
                j++;
            }
            for (int k = 0; k < newbz; k++) {
                fish.add(8);
            }
        }
        System.out.println(fish.size());
    }
    /**
     * algo:
     *  - simulate by weekday
     *      - assign each fish to a weekday, count number of fish for each weekday,
     *          store in a table
     *      - for each weekday, decrement by number of fish on that day, then add
     *          to next day; after day 0, add all back to day 6 and add the same
     *          amount to day 8
     *      - after simulating for x days, add up counts of all fish on each day
     * 
     * @param days
     */
    private void runPart2(int days) {
        List<String> inDatz = readInput(INPUT_URI);
        List<Integer> fish = makeFish(inDatz);
        long[] fishCountPerDay = makeFishCountPerDayTable(fish);
        int dz = 0;
        while (dz < days) {
            for (int i = 0; i < fishCountPerDay.length; i++) {
                long count = fishCountPerDay[i];
                if (i == 0) {
                    fishCountPerDay[i] = 0;
                    fishCountPerDay[9] = count;
                } else if (i == 9) {
                    fishCountPerDay[6] += count;
                    fishCountPerDay[8] += count;
                    fishCountPerDay[i] = 0;
                } else {
                    fishCountPerDay[i - 1] = count;
                    fishCountPerDay[i] = 0;
                }
            }
            dz++;
        }
        long total = 0;
        for (int i = 0; i < fishCountPerDay.length; i++) {
            total += fishCountPerDay[i];
        }
        System.out.println(total);
    }
    private long[] makeFishCountPerDayTable(List<Integer> fish) {
        long[] fishCountPerDay = new long[10];
        for (int f : fish) {
            fishCountPerDay[f]++;
        }
        return fishCountPerDay;
    }
    private List<Integer> makeFish(List<String> inDatz) {
        String fishList = inDatz.get(0);
        String[] fishz = fishList.split("\\,");
        List<Integer> fish = new ArrayList<>();
        for (String f : fishz) {
            fish.add(Integer.parseInt(f));
        }
        return fish;
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
        Solution6 soln = new Solution6();
        /*
        for (int i = 2; i <= 128; i *= 2) {
            soln.runPart1(i);
        }
         */
        soln.runPart1(80);
        soln.runPart2(256);
    }
}
