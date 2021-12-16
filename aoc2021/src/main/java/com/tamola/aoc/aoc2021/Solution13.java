package com.tamola.aoc.aoc2021;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Solution13 {
    //private static String INPUT_URI = "aoc2021/src/main/java/com/tamola/aoc/aoc2021/sampleinput13.txt";
    private static String INPUT_URI = "aoc2021/src/main/java/com/tamola/aoc/aoc2021/input13.txt";
    /**
     * algo:
     *  - for horizontal fold line, compute # of lines below fold until end of sheet;
     *      these lines, for line y until end, lines y + 1, y + 2, ..., sheetLen -1,
     *      will be folded to so that line y + 1 folds on top of line y - 1,
     *      line y + 2 folds on top of line y - 2, line y + 3 folds on top of line y - 3, etc;
     *      if # of lines below fold are same or equal to # of lines above fold, we're OK,
     *      but if # of lines below fold is greater than # of lines above fold, the extra
     *      lines are inserted as lines above the top of sheet; so merge lines y + 1 to line
     *      y - x, where x == # of lines above fold (which is y - y == 0; lines y - 1 to line 0)
     */
    private void run() {
        List<String> inDatz = readInput(INPUT_URI);
        System.out.println("inDatz = " + inDatz.toString());
        List<List<Integer>> dotz = new ArrayList<>();
        List<List<Integer>> foldz = new ArrayList<>(); // 0: 0 or 1, 0 == x, 1 == y; 1: line to fold
        makeDotzAndFoldz(inDatz, dotz, foldz);
        System.out.println("dotz = ");
        for (int i = 0; i < dotz.size(); i++) {
            System.out.println(dotz.get(i).toString());
        }
        System.out.println("foldz = " + foldz.toString());
        dotz = fold(dotz, foldz);
        System.out.println("dotz = ");
        for (int i = 0; i < dotz.size(); i++) {
            System.out.println(dotz.get(i).toString());
        }
        System.out.println(countDots(dotz));
    }
    private void makeDotzAndFoldz(List<String> inDatz, List<List<Integer>> dotz, List<List<Integer>> foldz) {
        int i = 0;
        List<List<Integer>> dotzData = new ArrayList<>();
        int maxX = 0;
        int maxY = 0;
        while (!inDatz.get(i).isEmpty()) {
            List<Integer> d = new ArrayList<>();
            String[] dz = inDatz.get(i).split("\\,");
            maxX = Math.max(maxX, Integer.parseInt(dz[0]));
            d.add(Integer.parseInt(dz[0]));
            maxY = Math.max(maxY, Integer.parseInt(dz[1]));
            d.add(Integer.parseInt(dz[1]));
            dotzData.add(d);
            i++;
        }
        for (int j = 0; j < maxY + 1; j++) {
            dotz.add(new ArrayList<>());
        }
        for (int j = 0; j < dotz.size(); j++) {
            for (int k = 0; k < maxX + 1; k++) {
                dotz.get(j).add(0);
            }
        }
        for (int j = 0; j < dotzData.size(); j++) {
            dotz.get(dotzData.get(j).get(1)).set(dotzData.get(j).get(0), 1);
        }
        i++; // skip the blank line, now process folds
        for (; i < inDatz.size(); i++) {
            List<Integer> f = new ArrayList<>();
            String[] fz = inDatz.get(i).split(" ");
            String fd = fz[2];
            String[] fdz = fd.split("\\=");
            if (fdz[0].equals("x")) {
                f.add(0);
            } else {
                f.add(1);
            }
            f.add(Integer.parseInt(fdz[1]));
            foldz.add(f);
        }
    }
    private List<List<Integer>> fold(List<List<Integer>> dotz, List<List<Integer>> foldz) {
        List<List<Integer>> nDotz = new ArrayList<>();
        for (List<Integer> l : dotz) {
            nDotz.add(new ArrayList<>(l));
        }
        for (int i = 0; i < 1; i++) { // foldz.size(); i++) {
            if (foldz.get(i).get(0) == 0) { // an x (vertical fold, left)
                int linesFolded = nDotz.get(0).size() - foldz.get(i).get(1) - 1;
                int linesFoldedOver = foldz.get(i).get(1);
                System.out.println("linesFolded (x) = " + linesFolded + " linesFoldedOver (x) = " + linesFoldedOver);
                List<List<Integer>> nnDotz = new ArrayList<>();
                if (linesFoldedOver <= linesFolded) {
                    /**
                     * algo:
                     *  - create list of empty lists
                     *  - copy all columns to be folded over into new matrix
                     *  - start from column after fold, copy into last column of new matrix,
                     *      then next column after fold, copy into column before last column of
                     *      new matrix, then third column after fold, copy into column before previous
                     *      column, etc
                     */
                    for (int j = 0; j < nDotz.size(); j++) {
                        nnDotz.add(new ArrayList<>());
                    }
                    int x = foldz.get(i).get(1);
                    for (int j = x + 1; j < nDotz.get(0).size(); j++) {
                        for (int k = 0; k < nDotz.size(); k++) {
                            nnDotz.get(k).add(nDotz.get(k).get(j));
                        }
                    }
                    /*
                    for (int j = x - 1; j >= 0; j--) {
                        // FIX for inserting columns before, not rows...
                        nDotz.add(0, dotz.get(j)); // TODO: add new lists, not reference to existing list
                    }
                     */
                    int a = 1;
                    for (int c = x + a; c < nDotz.get(0).size(); c++) {
                        for (int k = 0; k < nDotz.size(); k++) {
                            nnDotz.get(k).set(x - a, nDotz.get(k).get(c) == 1 || nDotz.get(k).get(x - a) == 1 ? 1 : 0);
                        }
                        a++;
                    }
                    nDotz = nnDotz;
                } else {

                }                
            } else { // a y (horizontal fold, up)
                int linesFolded = nDotz.size() - foldz.get(i).get(1) - 1;
                int linesFoldedOver = foldz.get(i).get(1);
                System.out.println("linesFolded (y) = " + linesFolded + " linesFoldedOver (y) = " + linesFoldedOver);
                List<List<Integer>> nnDotz = new ArrayList<>();
                if (linesFoldedOver <= linesFolded) {
                    int y = foldz.get(i).get(1);
                    /**
                     * algo:
                     *  - copy all rows to be folded over to new matrix
                     *  - read all rows to be folded and merge to new matrix,
                     *      starting from bottom of new matrix; so first row after
                     *      fold gets merged to bottom of new matrix, second row
                     *      after fold gets merged to row above bottom of new matrix,
                     *      third row after fold gets merged to row above last merged
                     *      row, etc
                     */
                    for (int j = y - 1; j >= 0; j--) {
                        nnDotz.add(0, new ArrayList<>(nDotz.get(j))); // TODO: add new lists, not reference to existing list
                    }
                    int a = 1;
                    for (int r = y + a; r < nDotz.size(); r++) {
                        for (int k = 0; k < nDotz.get(0).size(); k++) {
                            nnDotz.get(y - a).set(k, nDotz.get(r).get(k) == 1 || nDotz.get(y - a).get(k) == 1 ? 1 : 0); // TODO: merge checking nDotz?
                        }
                        a++;
                    }
                    nDotz = nnDotz;
                } else {

                }
            }
        }
        return nDotz;
    }
    private int countDots(List<List<Integer>> dotz) {
        int count = 0;
        for (int i = 0; i < dotz.size(); i++) {
            for (int j = 0; j < dotz.get(0).size(); j++) {
                if (dotz.get(i).get(j) == 1) {
                    count++;
                }
            }
        }
        return count;
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
        Solution13 soln = new Solution13();
        soln.run();
    }
}
