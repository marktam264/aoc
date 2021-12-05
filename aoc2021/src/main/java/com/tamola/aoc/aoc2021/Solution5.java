package com.tamola.aoc.aoc2021;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Solution5 {
    /**
     * algo:
     *  - plot lines into matrix and as plot, if a
     *      point is already populated, add that
     *      point to the count of overlap points
     *  - how to convert between x,y coords to matrix
     *      x,y has 0,0 at top left, 
     *          increasing x goes to the right
     *          increasing y goes down
     *      so (1,1) in x,y is row 1, col 1 in matrix
     *          y is row and x is col,
     *          so (x_xy, y_xy) == [y_xy][x_xy] in matrix
     */
    //private static String INPUT_URI = "aoc2021/src/main/java/com/tamola/aoc/aoc2021/sampleinput5.txt";
    private static String INPUT_URI = "aoc2021/src/main/java/com/tamola/aoc/aoc2021/input5.txt";
    private void runPart1() {
        List<String> inDatz = readInput(INPUT_URI);
        List<int[]> linePointList = new ArrayList<>(); // 0: x1, 1: y1, 2: x2, 3: y2
        int[] boundzData = new int[2]; // 0: maxX, 1: maxY
        makeLinePointsList(inDatz, linePointList, boundzData);
        int[][] matrix = new int[boundzData[1] + 1][boundzData[0] + 1];
        populateMatrixWithLines1(matrix, linePointList);
        System.out.println(countOverlaps(matrix));
    }
    private void runPart2() {
        List<String> inDatz = readInput(INPUT_URI);
        List<int[]> linePointList = new ArrayList<>(); // 0: x1, 1: y1, 2: x2, 3: y2
        int[] boundzData = new int[2]; // 0: maxX, 1: maxY
        makeLinePointsList(inDatz, linePointList, boundzData);
        int[][] matrix = new int[boundzData[1] + 1][boundzData[0] + 1];
        populateMatrixWithLines2(matrix, linePointList);
        System.out.println(countOverlaps(matrix));
    }
    private int countOverlaps(int[][] matrix) {
        int overlaps = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (matrix[i][j] > 1) {
                    overlaps++;
                }
            }
        }
        return overlaps;       
    }
    /**
     * converts each line point (in x,y) to matrix coords (r,c)
     * and plots it on the matrix
     * 
     * (x_xy, y_xy) == [y_xy][x_xy]
     * 
     * @param matrix
     * @param linePointList
     */
    private void populateMatrixWithLines1(int[][] matrix, List<int[]> linePointList) {
        for (int[] endz : linePointList) {
            if (endz[0] == endz[2]) { // vertical line
                int start = Math.min(endz[1], endz[3]);
                int end = Math.max(endz[1], endz[3]);
                for (int r = start; r <= end; r++) {
                    matrix[r][endz[0]]++;
                }
            } else if (endz[1] == endz[3]) { // horizontal line
                int start = Math.min(endz[0], endz[2]);
                int end = Math.max(endz[0], endz[2]);
                for (int c = start; c <= end; c++) {
                    matrix[endz[1]][c]++;
                }
            }
        }
    }
    private void populateMatrixWithLines2(int[][] matrix, List<int[]> linePointList) {
        for (int[] endz : linePointList) {
            if (endz[0] == endz[2]) { // vertical line
                int start = Math.min(endz[1], endz[3]);
                int end = Math.max(endz[1], endz[3]);
                for (int r = start; r <= end; r++) {
                    matrix[r][endz[0]]++;
                }
            } else if (endz[1] == endz[3]) { // horizontal line
                int start = Math.min(endz[0], endz[2]);
                int end = Math.max(endz[0], endz[2]);
                for (int c = start; c <= end; c++) {
                    matrix[endz[1]][c]++;
                }
            } else { // diagonal line
                // if start_x < end_x && start_y < end_y, it's diagonal from top-left to bottom-right
                if (endz[0] < endz[2] && endz[1] < endz[3]) {
                    int r = endz[1];
                    int c = endz[0];
                    while (r <= endz[3] && c <= endz[2]) {
                        matrix[r++][c++]++;
                    }
                } 
                // if start_x < end_x && start_y > end_y, it's diagonal from bottom-left to top-right
                else if (endz[0] < endz[2] && endz[1] > endz[3]) {
                    int r = endz[1];
                    int c = endz[0];
                    while (r >= endz[3] && c <= endz[2]) {
                        matrix[r--][c++]++;
                    }
                }
                // if start_x > end_x && start_y < end_y, it's diagonal from top-right to bottom-left
                else if (endz[0] > endz[2] && endz[1] < endz[3]) {
                    int r = endz[1];
                    int c = endz[0];
                    while (r <= endz[3] && c >= endz[2]) {
                        matrix[r++][c--]++;
                    }
                }
                // if start_x > end_x && start_y > end_y, it's diagonal from bottom-right to top-left
                else if (endz[0] > endz[2] && endz[1] > endz[3]) {
                    int r = endz[1];
                    int c = endz[0];
                    while (r >= endz[3] && c >= endz[2]) {
                        matrix[r--][c--]++;
                    }
                }
            }
        }
    }
    private void makeLinePointsList(List<String> inDatz, List<int[]> linePointList, int[] boundzData) {
        for (String l : inDatz) {
            String[] endz = l.split("\\ -> ");
            String[] start = endz[0].split("\\,");
            String[] end = endz[1].split("\\,");
            linePointList.add(new int[] { 
                Integer.parseInt(start[0]),
                Integer.parseInt(start[1]), 
                Integer.parseInt(end[0]), 
                Integer.parseInt(end[1]) 
            });
            boundzData[0] = Math.max(boundzData[0], Math.max(Integer.parseInt(start[0]), Integer.parseInt(end[0])));
            boundzData[1] = Math.max(boundzData[1], Math.max(Integer.parseInt(start[1]), Integer.parseInt(end[1])));
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
        Solution5 soln = new Solution5();
        soln.runPart1();
        soln.runPart2();
    }
}
