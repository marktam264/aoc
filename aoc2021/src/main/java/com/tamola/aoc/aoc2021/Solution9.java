package com.tamola.aoc.aoc2021;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.stream.Stream;

public class Solution9 {
    private static String INPUT_URI = "aoc2021/src/main/java/com/tamola/aoc/aoc2021/sampleinput9.txt";
    //private static String INPUT_URI = "aoc2021/src/main/java/com/tamola/aoc/aoc2021/input9.txt";
    /**
     * algo:
     *  - scan each location and check all surrounding locations, and
     *      if all surrounding locations are higher, this is a low point
     */
    public void runPart1() {
        List<String> inDatz = readInput(INPUT_URI);
        List<List<Integer>> lavaTubez = new ArrayList<>();
        for (String data : inDatz) {
            List<Integer> l = new ArrayList<>();
            for (char c : data.toCharArray()) {
                l.add(Integer.parseInt(Character.toString(c)));
            } 
            lavaTubez.add(l);
        }
        int[][] dirz = new int[][] {
            { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 }
        };
        int riskLevelSum = 0;
        for (int i = 0; i < lavaTubez.size(); i++) {
            for (int j = 0; j < lavaTubez.get(0).size(); j++) {
                boolean foundEqualOrLower = false;
                for (int[] dir : dirz) {
                    int adjI = i + dir[0];
                    int adjJ = j + dir[1];
                    if (adjI >= 0 && adjI < lavaTubez.size() && adjJ >= 0 && adjJ < lavaTubez.get(0).size()) {
                        if (lavaTubez.get(adjI).get(adjJ) <= lavaTubez.get(i).get(j)) {
                            foundEqualOrLower = true;
                            break;
                        }
                    } 
                }
                if (!foundEqualOrLower) {
                    riskLevelSum += 1 + lavaTubez.get(i).get(j);
                }
            }
        }
        System.out.println(riskLevelSum);
    }
    /**
     * algo:
     *  - from the low point, do a DFS to find rest of the basin;
     *      the basin is surrounded by 9's; return the size of each basin
     *  - similar to the # of islands problem
     *  - find the 3 largest basins and return sum
     */
    public void runPart2() {
        List<String> inDatz = readInput(INPUT_URI);
        List<List<Integer>> lavaTubez = new ArrayList<>();
        for (String data : inDatz) {
            List<Integer> l = new ArrayList<>();
            for (char c : data.toCharArray()) {
                l.add(Integer.parseInt(Character.toString(c)));
            } 
            lavaTubez.add(l);
        }
        int[][] dirz = new int[][] {
            { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 }
        };
        PriorityQueue<Integer> minHeap = new PriorityQueue<>((a, b) -> Integer.compare(a, b));
        for (int i = 0; i < lavaTubez.size(); i++) {
            for (int j = 0; j < lavaTubez.get(0).size(); j++) {
                boolean foundEqualOrLower = false;
                for (int[] dir : dirz) {
                    int adjI = i + dir[0];
                    int adjJ = j + dir[1];
                    if (adjI >= 0 && adjI < lavaTubez.size() && adjJ >= 0 && adjJ < lavaTubez.get(0).size()) {
                        if (lavaTubez.get(adjI).get(adjJ) <= lavaTubez.get(i).get(j)) {
                            foundEqualOrLower = true;
                            break;
                        }
                    } 
                }
                if (!foundEqualOrLower) {
                    int basinSize = mapBasin(lavaTubez, i, j, new HashMap<>(), dirz);
                    System.out.println("basinSize = " + basinSize);
                    if (minHeap.size() < 3) {
                        minHeap.offer(basinSize);
                    } else {
                        if (basinSize > minHeap.peek()) {
                            minHeap.poll();
                            minHeap.offer(basinSize);
                        }
                    }
                }
            }
        }
        System.out.println("minHeap = " + minHeap);
        int sum = 0;
        while (!minHeap.isEmpty()) {
            int size = minHeap.poll();
            sum += size;
        }
        System.out.println(sum);
    }
    private int mapBasin(List<List<Integer>> lavaTubez, int r, int c, Map<Integer, Set<Integer>> visited, int[][] dirz) {
        System.out.println("r = " + r + " c = " + c + " visited = " + visited.toString());
        if (r < 0 || r >= lavaTubez.size() || c < 0 || c >= lavaTubez.get(0).size()
            || (visited.containsKey(r) && visited.get(r).contains(c))
            || lavaTubez.get(r).get(c) == 9) {
            return 0;
        }
        int size = 0;
        visited.computeIfAbsent(r, k -> new HashSet<>()).add(c);
        for (int[] dir : dirz) {
            int nextR = r + dir[0];
            int nextC = c + dir[1];
            size = 1 + mapBasin(lavaTubez, nextR, nextC, visited, dirz);
        }
        visited.get(r).remove(c);
        if (visited.get(r).isEmpty()) {
            visited.remove(r);
        }
        return size;
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
        Solution9 soln = new Solution9();
        soln.runPart1();
        soln.runPart2();
    }
}
