package com.tamola.aoc.aoc2021;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class Solution14 {
    //private static String INPUT_URI = "aoc2021/src/main/java/com/tamola/aoc/aoc2021/sampleinput14.txt";
    private static String INPUT_URI = "aoc2021/src/main/java/com/tamola/aoc/aoc2021/input14.txt";
    /**
     * algo:
     *  - do a sliding window of size 2 over current polymer template,
     *      copy result of insert into window into new list
     *  - or just do sliding window and insert into middle
     */
    private void run() {
        List<String> inDatz = readInput(INPUT_URI);
        StringBuilder template = new StringBuilder();
        Map<String, String> insertionz = new HashMap<>();
        makeTemplateAndInsertionMap(inDatz, template, insertionz);
        System.out.println("template = " + template.toString());
        System.out.println("insertionz = " + insertionz.toString());
        int step = 0;
        int maxSteps = 10;
        while (step < maxSteps) {
            int wStart = 0;
            int len = template.length();
            for (int wEnd = wStart; wEnd < len + 1; wEnd++) {
                //System.out.println("wStart = " + wStart + " wEnd = " + wEnd);
                //System.out.println(template.toString());
                if (wEnd - wStart + 1 > 2) {
                    template.insert(wStart + 1, insertionz.get(template.substring(wStart, wEnd)));
                    wStart = wEnd;
                    len += 1;
                }
            }
            step++;
        }
        Map<Character, Integer> freqz = new HashMap<>();
        for (char c : template.toString().toCharArray()) {
            freqz.put(c, freqz.getOrDefault(c, 0) + 1);
        }
        List<Map.Entry<Character, Integer>> freqzEntries = new ArrayList<>(freqz.entrySet());
        Collections.sort(freqzEntries, (a, b) -> Integer.compare(b.getValue(), a.getValue()));
        System.out.println(freqzEntries.get(0).getValue() - freqzEntries.get(freqzEntries.size() - 1).getValue());
        //System.out.println(template.toString());
    }
    private void makeTemplateAndInsertionMap(List<String> inDatz, StringBuilder template, Map<String, String> insertionz) {
        template.append(inDatz.get(0));
        for (int i = 2; i < inDatz.size(); i++) {
            String[] dz = inDatz.get(i).split(" \\-\\> ");
            insertionz.put(dz[0], dz[1]);
        }
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
        Solution14 soln = new Solution14();
        soln.run();
    }
}
