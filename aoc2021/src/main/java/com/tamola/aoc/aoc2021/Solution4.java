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
import java.util.Stack;
import java.util.stream.Stream;

public class Solution4 {
    /**
     * algo:
     * - read first line as bingo numbers and put into list of numbers
     * - read subsequent blocks as bingo boards
     *      - read line by line, once reached 5 lines, save to a list,
     *          that'll be a board, save all boards to a list of lists
     *      - boards are separated by a blank line
     * - then iterate through list of bingo numbers, on each one,
     *      iterate through all boards marking that number
     *          - on each board, also check if it's a winner,
     *              if so, then break out, record board number and
     *                  winning number
     * - calculate final score (sum of all unmarked numbers multiplied
     *      by winning number)
     * - bingo board:
     *      - map of board number to matrix coords on a 2d boolean matrix
     * - bingo boards:
     *      - list of maps
     */
    //private static String INPUT_URI = "aoc2021/src/main/java/com/tamola/aoc/aoc2021/sampleinput4.txt";
    private static String INPUT_URI = "aoc2021/src/main/java/com/tamola/aoc/aoc2021/input4.txt";
    public void runPart1() {
        List<String> inDatz = readInput(INPUT_URI);
        List<Integer> bingoDrawNumbers = readBingoDrawNumbers(inDatz.get(0));
        List<Map<Integer, int[]>> bingoBoardNumberLocations = readBingoBoardNumberLocations(inDatz.subList(1, inDatz.size()));
        //System.out.println("bingoBoardNumberLocations = " + bingoBoardNumberLocations.toString() + " size = " + bingoBoardNumberLocations.size());
        List<boolean[][]> bingoBoards = makeBingoBoards(bingoBoardNumberLocations.size());
        int[] winner = new int[2]; // [board number, winning number]
        for (int i = 0; i < bingoDrawNumbers.size(); i++) {
            boolean foundWinner = false;
            for (int j = 0; j < bingoBoards.size(); j++) {
                if (bingoBoardNumberLocations.get(j).containsKey(bingoDrawNumbers.get(i))) {
                    bingoBoards.get(j)[bingoBoardNumberLocations.get(j).get(bingoDrawNumbers.get(i))[0]][bingoBoardNumberLocations.get(j).get(bingoDrawNumbers.get(i))[1]] = true;
                }
                if (isWinner(bingoBoards.get(j))) {
                    winner[0] = j;
                    winner[1] = bingoDrawNumbers.get(i);
                    foundWinner = true;
                    break;
                }
            }
            if (foundWinner) {
                break;
            }
        }
        //System.out.println(Arrays.toString(winner));
        System.out.println(computeBoardScore(bingoBoardNumberLocations.get(winner[0]), bingoBoards.get(winner[0]), winner[1]));
    }
    public void runPart2() {
        List<String> inDatz = readInput(INPUT_URI);
        List<Integer> bingoDrawNumbers = readBingoDrawNumbers(inDatz.get(0));
        List<Map<Integer, int[]>> bingoBoardNumberLocations = readBingoBoardNumberLocations(inDatz.subList(1, inDatz.size()));
        //System.out.println("bingoBoardNumberLocations = " + bingoBoardNumberLocations.toString() + " size = " + bingoBoardNumberLocations.size());
        List<boolean[][]> bingoBoards = makeBingoBoards(bingoBoardNumberLocations.size());
        Stack<int[]> winnerz = new Stack<>();
        Set<Integer> winnerBoardz = new HashSet<>();
        for (int i = 0; i < bingoDrawNumbers.size(); i++) {
            for (int j = 0; j < bingoBoards.size(); j++) {
                if (!winnerBoardz.contains(j)) {
                    if (bingoBoardNumberLocations.get(j).containsKey(bingoDrawNumbers.get(i))) {
                        bingoBoards.get(j)[bingoBoardNumberLocations.get(j).get(bingoDrawNumbers.get(i))[0]][bingoBoardNumberLocations.get(j).get(bingoDrawNumbers.get(i))[1]] = true;
                    }
                    if (isWinner(bingoBoards.get(j))) {
                        int[] winner = new int[2];
                        winner[0] = j;
                        winner[1] = bingoDrawNumbers.get(i);
                        winnerz.push(winner);
                        winnerBoardz.add(j);
                    }
                }
            }
        }
        /*
        System.out.println(Arrays.toString(winnerz.peek()));
        //System.out.println(Arrays.toString(bingoBoards.get(winnerz.peek()[0])));
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                System.out.print(bingoBoards.get(winnerz.peek()[0])[i][j] + " ");
            }
            System.out.println("");
        }
         */
        System.out.println(computeBoardScore(bingoBoardNumberLocations.get(winnerz.peek()[0]), bingoBoards.get(winnerz.peek()[0]), winnerz.peek()[1]));
    }
    private int computeBoardScore(Map<Integer, int[]> numberLocations, boolean[][] board, int winningNumber) {
        int score = 0;
        for (Map.Entry<Integer, int[]> e : numberLocations.entrySet()) {
            if (!board[e.getValue()[0]][e.getValue()[1]]) {
                score += e.getKey();
            }
        }
        return score * winningNumber;
    }
    private boolean isWinner(boolean[][] board) {
        // check rows
        for (int i = 0; i < board.length; i++) {
            int j = 0;
            for (; j < board[i].length; j++) {
                if (!board[i][j]) {
                    break;
                }
            }
            if (j >= board[i].length) {
                return true;
            }
        }
        // check cols
        for (int j = 0; j < board[0].length; j++) {
            int i = 0;
            for (; i < board.length; i++) {
                if (!board[i][j]) {
                    break;
                }
            }
            if (i >= board.length) {
                return true;
            }
        }
        return false;
    }
    private List<boolean[][]> makeBingoBoards(int count) {
        List<boolean[][]> rez = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            rez.add(new boolean[5][5]);
        }
        return rez;
    }
    private List<Map<Integer, int[]>> readBingoBoardNumberLocations(List<String> boardData) {
        List<Map<Integer, int[]>> rez = new ArrayList<>();
        int i = 0;
        for (String l : boardData) {
            if (l.isEmpty()) {
                rez.add(new HashMap<>());
                i = 0;
                continue;
            }
            String[] n = l.split("\\s+");
            //System.out.println("n = " + Arrays.toString(n));
            List<Integer> nl = new ArrayList<>();
            for (int j = 0; j < n.length; j++) {
                if (n[j].isEmpty()) {
                    continue;
                }
                nl.add(Integer.parseInt(n[j]));
            }
            //System.out.println("i = " + i + " nl = " + nl.toString());
            for (int j = 0; j < nl.size(); j++) {
                rez.get(rez.size() - 1).put(nl.get(j), new int[] { i, j });
            }
            i++;
        }
        return rez;
    }
    private List<Integer> readBingoDrawNumbers(String numbersData) {
        String[] splitz = numbersData.split("\\,");
        List<Integer> rez = new ArrayList<>();
        for (String s : splitz) {
            rez.add(Integer.parseInt(s));
        }
        return rez;
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
        Solution4 soln = new Solution4();
        soln.runPart1();
        soln.runPart2();
    }
}
