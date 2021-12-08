package com.tamola.aoc.aoc2021;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.stream.Stream;

public class Solution7 {
    //private static String INPUT_URI = "aoc2021/src/main/java/com/tamola/aoc/aoc2021/sampleinput7.txt";
    private static String INPUT_URI = "aoc2021/src/main/java/com/tamola/aoc/aoc2021/input7.txt";
    /**
     * algo:
     *  - do a greedy algo, sort by most frequent position and calculate score
     *      for that one, then try next most frequent, etc...
     *      - why greedy? because position values can vary over a huge range and so
     *          to try every single position in that range would be wasteful; it's
     *          gonna be O(n), so assume that it makes sense to only try
     *          to move to one of the already occupied positions, instead of
     *          also trying out all the in-between positions?
     */ 
    public void runPart1() {
        List<String> inDatz = readInput(INPUT_URI);
        String crabList = inDatz.get(0);
        List<Integer> crabs = makeCrabs(crabList);
        Map<Integer, Integer> freqz = makePositionFreqMap(crabs);
        PriorityQueue<Map.Entry<Integer, Integer>> maxHeap = new PriorityQueue<>((a, b) -> {
            return Integer.compare(b.getValue(), a.getValue());
        });
        for (Map.Entry<Integer, Integer> e : freqz.entrySet()) {
            maxHeap.offer(e);
        }
        int minCost = Integer.MAX_VALUE;
        while (!maxHeap.isEmpty()) {
            Map.Entry<Integer, Integer> e = maxHeap.poll();
            minCost = Math.min(minCost, computeCost1(e.getKey(), crabs));
        }
        System.out.println(minCost);
    }
    /**
     * algo:
     *  - greedy?
     *      - what parameter to optimize?
     *          - optimize on distance...pick position with smallest average distance from all crabs?
     *              - so, would pick a position, then calculate average distance from this position
     *                  to all points, then pick another position, then calculate average distance from
     *                  this other position to all points, then pick another position, etc
     *                  - this would be O(n^2)
     *  - from example, it looks like take simple average, then round? gonna try that and see what happens...
     *      - didn't work
     *  - do a binary search on crab positions and choose the half that has more crabs in it?
     *      - no
     *  - ???
     *  - just doing brute force, checked data set and doesn't look too big
     *      - O(n^2)
     */
    public void runPart2() {
        List<String> inDatz = readInput(INPUT_URI);
        String crabList = inDatz.get(0);
        List<Integer> crabs = makeCrabs(crabList);
        //int sum = 0;
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (int c : crabs) {
            //sum += c;
            min = Math.min(min, c);
            max = Math.max(max, c);
        }
        //long avgPos = Math.round((double)sum / (double)crabs.size());
        //System.out.println("min = " + min + " max = " + max + " avgPos = " + avgPos);
        int minCost = Integer.MAX_VALUE;
        for (int i = min; i <= max; i++) {
            int cost = computeCost2(i, crabs);
            minCost = Math.min(minCost, cost);
        }
        System.out.println(minCost);
    }
    private int computeCost1(int position, List<Integer> crabs) {
        int cost = 0;
        for (int i = 0; i < crabs.size(); i++) {
            cost += Math.abs(crabs.get(i) - position);
        }
        return cost;
    }
    private int computeCost2(int position, List<Integer> crabs) {
        int cost = 0;
        for (int i = 0; i < crabs.size(); i++) {
            int d = Math.abs(crabs.get(i) - position);
            int c = 0;
            for (int j = 1; j <= d; j++) {
                c += j;
            }
            cost += c;
        }
        return cost;
    }
    private Map<Integer, Integer> makePositionFreqMap(List<Integer> crabs) {
        Map<Integer, Integer> freqz = new HashMap<>();
        for (int c : crabs) {
            freqz.put(c, freqz.getOrDefault(c, 0) + 1);
        }
        return freqz;
    }
    private List<Integer> makeCrabs(String crabList) {
        String[] crabStrz = crabList.split("\\,");
        List<Integer> crabs = new ArrayList<>();
        for (String c : crabStrz) {
            crabs.add(Integer.parseInt(c));
        }
        return crabs;
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
        Solution7 soln = new Solution7();
        soln.runPart1();
        soln.runPart2();
    }
}
