package com.tamola.aoc.aoc2021;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import java.util.stream.Stream;

public class Solution10 {
    //private static String INPUT_URI = "aoc2021/src/main/java/com/tamola/aoc/aoc2021/sampleinput10.txt";
    //private static String INPUT_URI = "aoc2021/src/main/java/com/tamola/aoc/aoc2021/sampleinput8_2.txt";
    private static String INPUT_URI = "aoc2021/src/main/java/com/tamola/aoc/aoc2021/input10.txt";
    /**
     * algo:
     *  - find first error in each line (don't worry about incomplete lines),
     *      then calculate total syntax score
     */
    public void runPart1() {
        List<String> inDatz = readInput(INPUT_URI);
        Stack<Character> lastBracketz = new Stack<>();
        int score = 0;
        for (int i = 0; i < inDatz.size(); i++) {
            for (int j = 0; j < inDatz.get(i).length(); j++) {
                //System.out.println("i = " + i + " j = " + j);
                if (lastBracketz.isEmpty()) {
                    lastBracketz.push(inDatz.get(i).charAt(j));
                } else {
                    char curr = inDatz.get(i).charAt(j);
                    if (curr == ')') {
                        if (lastBracketz.peek() != '(') {
                            score += 3;
                            lastBracketz.pop();
                            break;
                        } else {
                            lastBracketz.pop();
                        }
                    } else if (curr == ']') {
                        if (lastBracketz.peek() != '[') {
                            score += 57;
                            lastBracketz.pop();
                            break;
                        } else {
                            lastBracketz.pop();
                        }
                    } else if (curr == '}') {
                        if (lastBracketz.peek() != '{') {
                            score += 1197;
                            lastBracketz.pop();
                            break;
                        } else {
                            lastBracketz.pop();
                        }
                    } else if (curr == '>') {
                        if (lastBracketz.peek() != '<') {
                            score += 25137;
                            lastBracketz.pop();
                            break;
                        } else {
                            lastBracketz.pop();
                        }
                    } else {
                        lastBracketz.push(curr);
                    }
                }
            }
        }
        System.out.println(score);
    }
    public void runPart2() {
        List<String> inDatz = readInput(INPUT_URI);
        List<Long> scorez = new ArrayList<>();
        for (int i = 0; i < inDatz.size(); i++) {
            boolean lineWasCorrupt = false;
            Stack<Character> lastBracketz = new Stack<>();
            for (int j = 0; j < inDatz.get(i).length(); j++) {
                //System.out.println("i = " + i + " j = " + j);
                if (lastBracketz.isEmpty()) {
                    lastBracketz.push(inDatz.get(i).charAt(j));
                } else {
                    char curr = inDatz.get(i).charAt(j);
                    if (curr == ')') {
                        if (lastBracketz.peek() != '(') {
                            lastBracketz.pop();
                            lineWasCorrupt = true;
                            break;
                        } else {
                            lastBracketz.pop();
                        }
                    } else if (curr == ']') {
                        if (lastBracketz.peek() != '[') {
                            lastBracketz.pop();
                            lineWasCorrupt = true;
                            break;
                        } else {
                            lastBracketz.pop();
                        }
                    } else if (curr == '}') {
                        if (lastBracketz.peek() != '{') {
                            lastBracketz.pop();
                            lineWasCorrupt = true;
                            break;
                        } else {
                            lastBracketz.pop();
                        }
                    } else if (curr == '>') {
                        if (lastBracketz.peek() != '<') {
                            lastBracketz.pop();
                            lineWasCorrupt = true;
                            break;
                        } else {
                            lastBracketz.pop();
                        }
                    } else {
                        lastBracketz.push(curr);
                    }
                }
            }
            if (!lineWasCorrupt) {
                if (!lastBracketz.isEmpty()) {
                    long score = 0;
                    while (!lastBracketz.isEmpty()) {
                        char b = lastBracketz.pop();
                        if (b == '(') {
                            score *= 5;
                            score += 1;
                        } else if (b == '[') {
                            score *= 5;
                            score += 2;
                        } else if (b == '{') {
                            score *= 5;
                            score += 3;
                        } else if (b == '<') {
                            score *= 5;
                            score += 4;
                        }
                    }
                    scorez.add(score);
                }
            }
        }
        //System.out.println("scorez = " + scorez.toString());
        Collections.sort(scorez);
        int scoreIdx = scorez.size() / 2;
        System.out.println(scorez.get(scoreIdx));
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
        Solution10 soln = new Solution10();
        soln.runPart1();
        soln.runPart2();
    }
}
