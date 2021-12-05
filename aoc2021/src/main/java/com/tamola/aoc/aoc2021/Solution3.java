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

public class Solution3 {
    /**
     *  algo:
     *      - for each input, store in array of (0,1) freq maps
     *          for that index, then iterate through each index and
     *          take (0,1) value with highest freq to calculate gamma,
     *          then for epsilon, iterate through gamma indexes, flipping
     *          each bit
     */
    //private static String INPUT_URI = "aoc2021/src/main/java/com/tamola/aoc/aoc2021/sampleinput3.txt";
    private static String INPUT_URI = "aoc2021/src/main/java/com/tamola/aoc/aoc2021/input3.txt";
    public void runPart1() {
        List<String> inDatz = readInput(INPUT_URI);
        List<Map<Character, Integer>> indexFreqz = new ArrayList<>();
        for (int i = 0; i < inDatz.get(0).length(); i++) {
            indexFreqz.add(new HashMap<>());
        }
        for (int i = 0; i < inDatz.size(); i++) {
            for (int j = 0; j < inDatz.get(i).length(); j++) {
                indexFreqz.get(j).put(inDatz.get(i).charAt(j), indexFreqz.get(j).getOrDefault(inDatz.get(i).charAt(j), 0) + 1);
            }
        }
        // then use StringBuilder to go through all indexFreqz, pick highest freq char for each index
        // and build gamma bit string, then parse that into an integer
        StringBuilder gammaStr = new StringBuilder();
        for (int i = 0; i < indexFreqz.size(); i++) {
            PriorityQueue<Map.Entry<Character, Integer>> maxHeap = new PriorityQueue<>((a, b) -> {
                return Integer.compare(b.getValue(), a.getValue());
            });
            for (Map.Entry<Character, Integer> e : indexFreqz.get(i).entrySet()) {
                maxHeap.offer(e);
            }
            gammaStr.append(maxHeap.peek().getKey());
        }
        StringBuilder epsilonStr = new StringBuilder();
        for (int i = 0; i < gammaStr.length(); i++) {
            epsilonStr.append(gammaStr.charAt(i) == '0' ? '1' : '0');
        }
        System.out.println(Integer.parseInt(gammaStr.toString(), 2) * Integer.parseInt(epsilonStr.toString(), 2));
    }
    /**
     * algo:
     *     - kind of same thing, but for each index, keep list of original numbers that
     *          correspond with the freq map keys; then can iterate through index freq maps
     *     - iterate through each input number string; start with index 0, make map of
     *          character (0 or 1) to list of corresponding number with that character at that
     *          bit index, then take the longer/shorter list, then use that list as input
     *          to process next index
     */
    public void runPart2() {
        List<String> inDatz = readInput(INPUT_URI);
        int idx = 0;
        int len = inDatz.get(0).length();
        List<String> oxyInDatz = new ArrayList<>(inDatz);
        while (idx < len) {
            if (oxyInDatz.size() <= 1) {
                break;
            }
            Map<Character, List<String>> charListz = new HashMap<>();
            for (int i = 0; i < oxyInDatz.size(); i++) {
                charListz.computeIfAbsent(oxyInDatz.get(i).charAt(idx), k -> new ArrayList<>()).add(oxyInDatz.get(i));
            }
            if (charListz.get('0').size() > charListz.get('1').size()) {
                oxyInDatz = charListz.get('0');
            } else if (charListz.get('0').size() < charListz.get('1').size()) {
                oxyInDatz = charListz.get('1');
            } else {
                oxyInDatz = charListz.get('1');
            }
            idx++;
        }
        int oxyRating = Integer.parseInt(oxyInDatz.get(0), 2);
        idx = 0;
        List<String> co2InDatz = new ArrayList<>(inDatz);
        while (idx < len) {
            if (co2InDatz.size() <= 1) {
                break;
            }
            Map<Character, List<String>> charListz = new HashMap<>();
            for (int i = 0; i < co2InDatz.size(); i++) {
                charListz.computeIfAbsent(co2InDatz.get(i).charAt(idx), k -> new ArrayList<>()).add(co2InDatz.get(i));
            }
            if (charListz.get('0').size() < charListz.get('1').size()) {
                co2InDatz = charListz.get('0');
            } else if (charListz.get('0').size() > charListz.get('1').size()) {
                co2InDatz = charListz.get('1');
            } else {
                co2InDatz = charListz.get('0');
            }
            idx++;
        }
        int co2Rating = Integer.parseInt(co2InDatz.get(0), 2);
        System.out.println(oxyRating * co2Rating);
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
        Solution3 soln = new Solution3();
        soln.runPart1();
        soln.runPart2();
    }
}
