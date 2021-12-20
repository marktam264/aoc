package com.tamola.aoc.aoc2021;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public class Solution15 {
    private static String INPUT_URI = "aoc2021/src/main/java/com/tamola/aoc/aoc2021/sampleinput15.txt";
    //private static String INPUT_URI = "aoc2021/src/main/java/com/tamola/aoc/aoc2021/input15.txt";
    /**
     * algo:
     *  - some kind of greedy algo that looks at each alternative and sums up
     *      risk for each alternative and then picks the one with the lowest
     *      risk
     */
    private void run() {
        List<String> inDatz = readInput(INPUT_URI);
        List<List<Integer>> cavern = makeCavern1(inDatz);
        Integer[][] cache = new Integer[cavern.size()][cavern.get(0).size()];
        System.out.println(search(cache, cavern, 0, 0, new HashMap<>(), new int[][] { {1,0},{0,1} }) - cavern.get(0).get(0));
    }
    private List<List<Integer>> makeCavern1(List<String> inDatz) {
        List<List<Integer>> cavern = new ArrayList<>();
        for (String d : inDatz) {
            List<Integer> l = new ArrayList<>();
            for (char c : d.toCharArray()) {
                l.add(c - '0');
            }
            cavern.add(l);
        }
        return cavern;
    }
    private List<List<Integer>> makeCavern2(List<String> inDatz) {
        List<List<Integer>> incorrectCavern = new ArrayList<>();
        for (String d : inDatz) {
            List<Integer> l = new ArrayList<>();
            for (char c : d.toCharArray()) {
                l.add(c - '0');
            }
            incorrectCavern.add(l);
        }
        List<List<Integer>> entireCavern = new ArrayList<>();
        // TODO
        return entireCavern;
    }
    private int search(Integer[][] cache, List<List<Integer>> cavern, int r, int c, Map<Integer, Set<Integer>> visited, int[][] directions) {
        if (cache[r][c] != null) {
            return cache[r][c];
        }
        if (r == cavern.size() - 1 && c == cavern.get(0).size() - 1) {
            return cavern.get(r).get(c);
        }
        visited.computeIfAbsent(r, k -> new HashSet<>()).add(c);
        int risk = Integer.MAX_VALUE;
        for (int[] d : directions) {
            int nextR = r + d[0];
            int nextC = c + d[1];
            if (nextR >= 0 && nextR < cavern.size() && nextC >= 0 && nextC < cavern.get(0).size()
                && !(visited.containsKey(nextR) && visited.get(nextR).contains(nextC))) {
                risk = Math.min(risk, cavern.get(r).get(c) + search(cache, cavern, nextR, nextC, visited, directions));
            }
        }
        visited.get(r).remove(c);
        if (visited.get(r).isEmpty()) {
            visited.remove(r);
        }
        cache[r][c] = risk;
        return cache[r][c];
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
        Solution15 soln = new Solution15();
        soln.run();
    }
}
