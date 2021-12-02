package com.tamola.aoc.aoc2021;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Solution1 {
    private static String INPUT_URI = "aoc2021/src/main/java/com/tamola/aoc/aoc2021/input1.txt";
    public void runPart1() {
        List<Integer> inDatz = readInput(INPUT_URI);
        //System.out.println(inDatz.toString());
        int incrementCount = 0;
        int current = inDatz.get(0);
        for (int i = 1; i < inDatz.size(); i++) {
            if (inDatz.get(i) > current) {
                incrementCount++;
            }
            current = inDatz.get(i);
        }
        System.out.println(incrementCount);
    }
    public void runPart2() {
        List<Integer> inDatz = readInput(INPUT_URI);
        //System.out.println(inDatz.toString());
        int incrementCount = 0;
        int wStart = 0;
        int wSize = 3;
        int wSum = 0;
        for (int wEnd = wStart; wEnd < inDatz.size(); wEnd++) {
            //System.out.println("wStart = " + wStart + " wEnd = " + wEnd);
            //System.out.println("wSum = " + wSum);
            int i = inDatz.get(wEnd);
            if (wEnd - wStart + 1 <= wSize) {
                wSum += i;
            } else {
                int pwSum = wSum;
                //System.out.println("pwSum = " + pwSum);
                wSum += i;
                wSum -= inDatz.get(wStart++);
                if (wSum > pwSum) {
                    incrementCount++;
                }
            }
        }
        System.out.println(incrementCount);        
    }
    private List<Integer> readInput(String uri) {
        List<Integer> inDatz = new ArrayList<>();
        try (Stream<String> inDatzLinez = Files.lines(Paths.get(uri))) {
            inDatzLinez.forEach(l -> inDatz.add(Integer.parseInt(l)));
        } catch (IOException ex) {
            ex.printStackTrace();
        }    
        return inDatz;
    }
    public static void main(String[] args) {
        Solution1 soln = new Solution1();
        soln.runPart1();
        soln.runPart2();
    }
}
