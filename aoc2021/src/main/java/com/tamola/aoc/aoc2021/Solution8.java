package com.tamola.aoc.aoc2021;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public class Solution8 {
    //private static String INPUT_URI = "aoc2021/src/main/java/com/tamola/aoc/aoc2021/sampleinput8_1.txt";
    //private static String INPUT_URI = "aoc2021/src/main/java/com/tamola/aoc/aoc2021/sampleinput8_2.txt";
    private static String INPUT_URI = "aoc2021/src/main/java/com/tamola/aoc/aoc2021/input8.txt";
    /**
     * algo:
     *  - a 3-seg pattern is a 7
     *  - a 4-seg pattern is a 4
     *  - 0 uses 6
     *  - 1, 2*
     *  - 2, 5
     *  - 3, 5
     *  - 4, 4*
     *  - 5, 5
     *  - 6, 6
     *  - 7, 3*
     *  - 8, 7*
     *  - 9, 6
     * 
     *  - goal for this part is just to count how many times 1, 4, 7 or 8
     *      appear in the display outputs (data to the right of the |)
     */
    public void runPart1() {
        List<String> inDatz = readInput(INPUT_URI);
        List<List<String>> displayOutputs = makeDisplayOutputz(inDatz);
        Set<Integer> uniqueDigitSizes = makeUniqueDigitSizez();
        int count = 0;
        for (List<String> outz : displayOutputs) {
            for (String d : outz) {
                if (uniqueDigitSizes.contains(d.length())) {
                    count++;
                }
            }
        }
        System.out.println(count);
    }
    /**
     * algo:
     *  - make map of jumbled letter segment to normal letter segment,
     *      then check it against all permutations of normal letter segment
     *      for a digit (because a digit, for example "1", which is made
     *      of normal letter segments c & f, could be represented as
     *      cf or fc); only need to do this against the non-unique-sized
     *      digits, since the unique-sized digts can be mapped directly
     *      to 1, 4, 7 or 8
     *  - want a structure where we can give it a string and it will
     *      return which digit the string represents
     *      - a digit is a permutation of letters that represent the digit's
     *          segments
     *      - function will take the string, turn it to a freq map of normal letter
     *          segments, and match it to a map of freq map to digit
     *  - how to make map of jumbled letter segment to normal letter segment
     *      - start with 1 (mapped to 2 letters), then know that those 2 segments also make up the
     *          the right leg of 7, and so the odd letter from 7 is the top segment;
     *          also from 1, know that the 2 odd letters from 4 are the upper left
     *          and middle segment of 4 (mapped to 2 letters);
     *          also from 1, know the bottom left and bottom segments of 8 (mapped to 2 letters)
     *          - now have (c,f) | (f,c) -> (top right,bottom right) | (bottom right,top right)
     *                     a             -> top
     *                     (b,d) | (d,b) -> (top left,middle) | (middle,top left)
     *                     (e,g) | (g,e) -> (bottom left,bottom) | (bottom,bottom left)
     *      - now get all 6 segment jumbled letters, which will be 0, 6 or 9
     *          - we know middle is either b or d; out of these 3 digits,
     *              if they all have b, then d is the middle segment,
     *              or if they all have d, then b is the middle segment
     *              - now we also know from 4, that 
     *                  if b is middle, then d is top left,
     *                  or d is middle, then b is top left
     *          - now we know top, middle, and top left
     *          - now to determine which is 6 or 9
     *              - check which of the 2 have both c and f, that one would be 9,
     *                  and so the other one would be 6
     *                  - so for 6, we now know that f is bottom right and that
     *                      c is top right, since 6 doesn't have top right
     *          - now we know top, middle, top left, top right and bottom right
     *          - now for 9, the only segment not mapped would be bottom, so
     *              now we know g is bottom
     *          - now for 6, the only segment not mapped would be bottom left, so
     *              no we know e is bottom left
     *          - now we know top, middle, top left, top right, bottom right, bottom and bottom left,
     *              and that is all of them
     *  - now we have map of jumbled letter segments to normal letter segments
     *      - use the map to transform jumbled letter segments to their normal letter segments,
     *          then can pass every normal letter segment string to our function that
     *          maps string to digit and decode each display
     */ 
    public void runPart2() {
        List<String> inDatz = readInput(INPUT_URI);
        List<List<String>> jumbledDigz = new ArrayList<>();
        List<List<String>> jumbledDisplayOutz = new ArrayList<>();
        makeJumbledDigzAndJumbledDisplayOutz(inDatz, jumbledDigz, jumbledDisplayOutz);
        System.out.println("jumbledDigz = " + jumbledDigz.toString());
        System.out.println("jumbledDisplayOutz = " + jumbledDisplayOutz.toString());
        List<Integer> realDisplayOutz = new ArrayList<>();
        for (int i = 0; i < jumbledDisplayOutz.size(); i++) {
            Map<Character, Character> jumbledToNormal = makeJumbledToNormalMapping(jumbledDigz.get(i));
            int realDisplayOut = convert(jumbledDisplayOutz.get(i), jumbledToNormal);
            realDisplayOutz.add(realDisplayOut);
        }
        System.out.println("realDisplayOutz = " + realDisplayOutz.toString());
        int sum = 0;
        for (int i : realDisplayOutz) {
            sum += i;
        }
        System.out.println(sum);
    }
    /**
     * takes the 4 digit jumbled display output (for a single display),
     * converts each digit to normal segment lettering, then converts it
     * to an int
     * 
     * @param jumbledDisplayOut
     * @param jumbledToNormal
     * @return
     */
    private int convert(List<String> jumbledDisplayOut, Map<Character, Character> jumbledToNormal) {
        StringBuilder display = new StringBuilder();
        List<String> dispDigz = new ArrayList<>();
        for (String jd : jumbledDisplayOut) {
            StringBuilder d = new StringBuilder();
            for (int c = 0; c < jd.length(); c++) {
                d.append(jumbledToNormal.get(jd.charAt(c)));
            }
            dispDigz.add(d.toString());
            //display.append(d.toString() + " ");
        }
        for (String d : dispDigz) {
            display.append(digitize(d));
        }
        System.out.println("display = " + display);
        return Integer.parseInt(display.toString());
    }
    private void makeJumbledDigzAndJumbledDisplayOutz(List<String> inDatz, List<List<String>> jumbledDigz, List<List<String>> jumbledDisplayOutz) {
        for (String data : inDatz) {
            String[] dataSplitz = data.split(" \\| ");
            String[] jumbledDigDatz = dataSplitz[0].split(" ");
            List<String> digz = new ArrayList<>();
            for (String d : jumbledDigDatz) {
                digz.add(d);
            }
            jumbledDigz.add(digz);
            String[] displayOut = dataSplitz[1].split(" ");
            List<String> out = new ArrayList<>();
            for (String o : displayOut) {
                out.add(o);
            }
            jumbledDisplayOutz.add(out);
        }
    }
    private Map<Character, Character> makeJumbledToNormalMapping(List<String> jumbledDigz) {
        System.out.println("jumbledDigz = " + jumbledDigz.toString());
        Map<Character, Character> m = new HashMap<>();
        // find 1 and 7
        String one = null;
        String seven = null;
        for (String jd : jumbledDigz) {
            if (jd.length() == 2) {
                one = jd;
            } else if (jd.length() == 3) {
                seven = jd;
            }
        }
        System.out.println("one = " + one + " seven = " + seven);
        // use 1 and 7 to find top segment
        char top = ' ';
        Set<Character> oneSegz = new HashSet<>();
        for (char c : one.toCharArray()) {
            oneSegz.add(c);
        }
        for (char c : seven.toCharArray()) {
            if (!oneSegz.contains(c)) {
                top = c;
            }
        }
        // map jumbled character representing top segment to normal character representation of top segment
        m.put(top, 'a'); // map the jumbled top segment letter to the normal top segment letter
        System.out.println("m = " + m.toString());

        // find 1, 6 and 9; also find 4 for use later
        String four = null;
        List<String> n069z = new ArrayList<>();
        for (String jd : jumbledDigz) {
            if (jd.length() == 6) {
                n069z.add(jd);
            } else if (jd.length() == 4) {
                four = jd;
            }
        }
        System.out.println("n069z = " + n069z.toString() + " four = " + four);
        // use 4 to figure out which jumbled letters correspond to top left and middle
        // segments; 0 is the only one out of 0, 6 and 9 that doesn't have a middle segment;
        // so we pick a char out of what we found are the top left and middle segments,
        // if one of the strings doesn't have this char, then this char must be the one
        // representing the middle segment, and this is the string representing 0
        /*
        Set<Character> fourSegz = new HashSet<>();
        for (char c : four.toCharArray()) {
            fourSegz.add(c);
        }
        System.out.println("fourSegz = " + fourSegz.toString());
        List<Character> topLeftAndMiddle = new ArrayList<>();
        for (char c : one.toCharArray()) {
            if (!fourSegz.contains(c)) {
                topLeftAndMiddle.add(c);
            }
        }
         */
        List<Character> topLeftAndMiddle = new ArrayList<>();
        for (char c : four.toCharArray()) {
            if (!oneSegz.contains(c)) {
                topLeftAndMiddle.add(c);
            }
        }
        System.out.println("topLeftAndMiddle = " + topLeftAndMiddle.toString());

        char middle = ' ';
        char topLeft = ' ';
        boolean foundCandidateMiddleSegment = false;
        int zeroIdx = -1;
        for (int i = 0; i < n069z.size(); i++) {
            int candidateIdx = n069z.get(i).indexOf(topLeftAndMiddle.get(0));
            if (candidateIdx == -1) {
                foundCandidateMiddleSegment = true;
                zeroIdx = i;
                break;
            }
        }
        //System.out.println("zeroIdx = " + zeroIdx);
        if (foundCandidateMiddleSegment) {
            middle = topLeftAndMiddle.get(0); 
            topLeft = topLeftAndMiddle.get(1);
        } else {
            middle = topLeftAndMiddle.get(1);
            topLeft = topLeftAndMiddle.get(0);
        }
        // if we didn't find 0 (because index 0 of our top left and middle segment list
        // wasn't the middle segment), then we find it here, now knowing which
        // one in the top left and middle segment list is the middle segment
        for (int i = 0; i < n069z.size(); i++) {
            if (n069z.get(i).indexOf(middle) == -1) {
                zeroIdx = i;
            }
        }
        System.out.println("zeroIdx = " + zeroIdx);
        m.put(middle, 'd');
        m.put(topLeft, 'b');
        System.out.println("m = " + m.toString());

        // NOTES:
        // use zeroIdx to copy only the string for 6 and 9 to a list
        // then figure out which one is 6 or 9 by checking against 1;
        // whichever string has both segments of 1 is 9 and the other
        // is 6

        // find 6 and 9, knowing the index that we found as 0
        List<String> n69z = new ArrayList<>();
        for (int i = 0; i < n069z.size(); i++) {
            if (i != zeroIdx) {
                n69z.add(n069z.get(i));
            }
        }
        // use 1 to figure out which jumbled letters correspond to top right and bottom right
        // segments; 6 is the only one out 6 and 9 that doesn't have a top right segment;
        // so we pick a char out of what we found are the top right and bottom right segments,
        // if one of the strings doesn't have this char, then this char must be the one
        // representing the top right segment, and this is the string representing 6
        char topRight = ' ';
        char bottomRight = ' ';
        int sixIdx = -1;
        boolean foundCandidateTopRightSegment = false;
        List<Character> topRightAndBottomRight = new ArrayList<>();
        for (char c : one.toCharArray()) {
            topRightAndBottomRight.add(c);
        }
        for (int i = 0; i < n69z.size(); i++) {
            int candidateIdx = n69z.get(i).indexOf(topRightAndBottomRight.get(0));
            if (candidateIdx == -1) {
                foundCandidateTopRightSegment = true;
                sixIdx = i;
                break;
            }
        }
        //System.out.println("sixIdx = " + sixIdx);
        if (foundCandidateTopRightSegment) {
            topRight = topRightAndBottomRight.get(0);
            bottomRight = topRightAndBottomRight.get(1);
        } else {
            topRight = topRightAndBottomRight.get(1);
            bottomRight = topRightAndBottomRight.get(0);
        }
        // if we didn't find 6 (because index 0 of our top right and bottom right segment list
        // wasn't the top right segment), then we find it here, now knowing which
        // one in the top right and bottom right segment list is the top right segment
        for (int i = 0; i < n69z.size(); i++) {
            if (n69z.get(i).indexOf(topRight) == -1) {
                sixIdx = i;
            }
        }
        System.out.println("sixIdx = " + sixIdx);
        m.put(topRight, 'c');
        m.put(bottomRight, 'f');
        System.out.println("m = " + m.toString());

        // use 6 and 9 to find the bottom and bottom left segments
        char bottom = ' ';
        // find 6
        String six = null;
        for (int i = 0; i < n69z.size(); i++) {
            if (i == sixIdx) {
                six = n69z.get(i);
            }
        }     
        // find 9
        String nine = null;
        for (int i = 0; i < n69z.size(); i++) {
            if (i != sixIdx) {
                nine = n69z.get(i);
            }
        }
        System.out.println("six = " + six + " nine = " + nine);
        // bottom segment is the only segement of 9 that isn't already mapped
        for (char c : nine.toCharArray()) {
            if (!m.containsKey(c)) {
                bottom = c;
            }
        }
        m.put(bottom, 'g');
        // bottom left segment is the only segment of 6 that isn't already mapped
        char bottomLeft = ' ';
        for (char c : six.toCharArray()) {
            if (!m.containsKey(c)) {
                bottomLeft = c;
            }
        }
        m.put(bottomLeft, 'e');
        System.out.println("m = " + m.toString());

        return m;
    }
    /**
     * takes a string that describes the segments that make up a digit
     * and returns which digit the string represents
     * 
     * @param s
     * @return
     */
    private int digitize(String s) {
        char[] charz = s.toCharArray();
        Arrays.sort(charz);
        String segz = new String(charz);
        if (segz.equals("abcefg")) {
            return 0;
        } else if (segz.equals("cf")) {
            return 1;
        } else if (segz.equals("acdeg")) {
            return 2;
        } else if (segz.equals("acdfg")) {
            return 3;
        } else if (segz.equals("bcdf")) {
            return 4;
        } else if (segz.equals("abdfg")) {
            return 5;
        } else if (segz.equals("abdefg")) {
            return 6;
        } else if (segz.equals("acf")) {
            return 7;
        } else if (segz.equals("abcdefg")) {
            return 8;
        } else { //if (segz.equals("abcdfg")) {
            return 9;
        }
    }
    private Set<Integer> makeUniqueDigitSizez() {
        Set<Integer> uniqueDigitSizes = new HashSet<>();
        uniqueDigitSizes.add(2); // 1
        uniqueDigitSizes.add(4); // 4
        uniqueDigitSizes.add(3); // 7
        uniqueDigitSizes.add(7); // 8
        return uniqueDigitSizes;
    }
    /**
     * makes a list of lists of the display output strings;
     * each display output list will have size 4, since only
     * 4 digits in display output
     * 
     * @param inDatz
     * @return
     */
    private List<List<String>> makeDisplayOutputz(List<String> inDatz) {
        List<List<String>> displayOutputz = new ArrayList<>();
        for (String data : inDatz) {
            String[] dataSplitz = data.split(" \\| ");
            String[] displayOut = dataSplitz[1].split(" ");
            List<String> out = new ArrayList<>();
            for (String o : displayOut) {
                out.add(o);
            }
            displayOutputz.add(out);
        }
        return displayOutputz;
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
        Solution8 soln = new Solution8();
        soln.runPart1();
        soln.runPart2();
    }
}
