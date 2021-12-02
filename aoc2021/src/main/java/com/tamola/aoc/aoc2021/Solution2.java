package com.tamola.aoc.aoc2021;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Solution2 {
    private String INPUT_URI = "aoc2021/src/main/java/com/tamola/aoc/aoc2021/input2.txt";
    public void runPart1() {
        List<String[]> commandz = readInput(INPUT_URI);
        int hPosition = 0;
        int depth = 0;
        for (String[] c : commandz) {
            //System.out.println("hPosition = " + hPosition + " depth = " + depth);
            if (c[0].equals("forward")) {
                hPosition += Integer.parseInt(c[1]);
            } else if (c[0].equals("down")) {
                depth += Integer.parseInt(c[1]);
            } else if (c[0].equals("up")) {
                depth -= Integer.parseInt(c[1]);
            }
        }
        System.out.println(hPosition * depth);
    }
    public void runPart2() {
        List<String[]> commandz = readInput(INPUT_URI);
        int hPosition = 0;
        int depth = 0;
        int aim = 0;
        for (String[] c : commandz) {
            //System.out.println("hPosition = " + hPosition + " depth = " + depth + " aim = " + aim);
            if (c[0].equals("forward")) {
                hPosition += Integer.parseInt(c[1]);
                depth += aim * Integer.parseInt(c[1]);
            } else if (c[0].equals("down")) {
                aim += Integer.parseInt(c[1]);
            } else if (c[0].equals("up")) {
                aim -= Integer.parseInt(c[1]);
            }
        }
        System.out.println(hPosition * depth);
    }
    private List<String[]> readInput(String uri) {
        List<String[]> commandz = new ArrayList<>(); // store as [command,param]
        try (Stream<String> inLinezStream = Files.lines(Paths.get(uri))) {
            inLinezStream.forEach(l -> commandz.add(l.split(" ")));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return commandz;
    }
    public static void main(String[] args) {
        Solution2 soln = new Solution2();
        soln.runPart1();
        soln.runPart2();
    }
}
