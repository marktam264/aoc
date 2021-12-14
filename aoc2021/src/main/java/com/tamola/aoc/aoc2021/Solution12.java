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

public class Solution12 {
    //private static String INPUT_URI = "aoc2021/src/main/java/com/tamola/aoc/aoc2021/sampleinput12_1.txt";
    //private static String INPUT_URI = "aoc2021/src/main/java/com/tamola/aoc/aoc2021/sampleinput12_2.txt";
    //private static String INPUT_URI = "aoc2021/src/main/java/com/tamola/aoc/aoc2021/sampleinput12_3.txt";
    private static String INPUT_URI = "aoc2021/src/main/java/com/tamola/aoc/aoc2021/input12.txt";
    /**
     * algo:
     *  - create the graph
     *      - use an adjacency list representation
     *      - data is provided as a list of connections, all non-directional
     *          - i.e. in the form "startNode-endNode"
     */
    public void run() {
        List<String> inDatz = readInput(INPUT_URI);
        Map<String, List<String>> cavez = makeCaves(inDatz);
        //System.out.println("cavez = " + cavez.toString());
        System.out.println(countPaths("start", cavez, new HashSet<>()));
    }
    private int countPaths(String root, Map<String, List<String>> cavez, Set<String> smallCavesVisited) {
        //System.out.println("root = " + root + " smallCavesVisited = " + smallCavesVisited);
        if (root.equals("end")) {
            return 1;
        }
        if (isAllLower(root)) {
            smallCavesVisited.add(root);
        }
        int count = 0;
        for (int i = 0; i < cavez.get(root).size(); i++) {
            if (!smallCavesVisited.contains(cavez.get(root).get(i))) {
                count += countPaths(cavez.get(root).get(i), cavez, smallCavesVisited);
            }
        }
        smallCavesVisited.remove(root);
        return count;
    }
    private boolean isAllLower(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (!Character.isLowerCase(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }
    private Map<String, List<String>> makeCaves(List<String> inDatz) {
        Map<String, List<String>> cavez = new HashMap<>();
        for (String d : inDatz) {
            String[] dz = d.split("\\-");
            cavez.computeIfAbsent(dz[0], k -> new ArrayList<>()).add(dz[1]);
            cavez.computeIfAbsent(dz[1], k -> new ArrayList<>()).add(dz[0]);
        }
        return cavez;
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
        Solution12 soln = new Solution12();
        soln.run();
    }
}
