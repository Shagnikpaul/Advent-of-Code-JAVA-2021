/*
--- Day 4: Giant Squid ---
You're already almost 1.5km (almost a mile) below the surface of the ocean, already so deep that you can't see any sunlight. What you can see, however, is a giant squid that has attached itself to the outside of your submarine.

Maybe it wants to play bingo?

Bingo is played on a set of boards each consisting of a 5x5 grid of numbers. Numbers are chosen at random, and the chosen number is marked on all boards on which it appears. (Numbers may not appear on all boards.) If all numbers in any row or any column of a board are marked, that board wins. (Diagonals don't count.)

The submarine has a bingo subsystem to help passengers (currently, you and the giant squid) pass the time. It automatically generates a random order in which to draw numbers and a random set of boards (your puzzle input). For example:

7,4,9,5,11,17,23,2,0,14,21,24,10,16,13,6,15,25,12,22,18,20,8,19,3,26,1

22 13 17 11  0
 8  2 23  4 24
21  9 14 16  7
 6 10  3 18  5
 1 12 20 15 19

 3 15  0  2 22
 9 18 13 17  5
19  8  7 25 23
20 11 10 24  4
14 21 16 12  6

14 21 17 24  4
10 16 15  9 19
18  8 23 26 20
22 11 13  6  5
 2  0 12  3  7
After the first five numbers are drawn (7, 4, 9, 5, and 11), there are no winners, but the boards are marked as follows (shown here adjacent to each other to save space):

22 13 17 11  0         3 15  0  2 22        14 21 17 24  4
 8  2 23  4 24         9 18 13 17  5        10 16 15  9 19
21  9 14 16  7        19  8  7 25 23        18  8 23 26 20
 6 10  3 18  5        20 11 10 24  4        22 11 13  6  5
 1 12 20 15 19        14 21 16 12  6         2  0 12  3  7
After the next six numbers are drawn (17, 23, 2, 0, 14, and 21), there are still no winners:

22 13 17 11  0         3 15  0  2 22        14 21 17 24  4
 8  2 23  4 24         9 18 13 17  5        10 16 15  9 19
21  9 14 16  7        19  8  7 25 23        18  8 23 26 20
 6 10  3 18  5        20 11 10 24  4        22 11 13  6  5
 1 12 20 15 19        14 21 16 12  6         2  0 12  3  7
Finally, 24 is drawn:

22 13 17 11  0         3 15  0  2 22        14 21 17 24  4
 8  2 23  4 24         9 18 13 17  5        10 16 15  9 19
21  9 14 16  7        19  8  7 25 23        18  8 23 26 20
 6 10  3 18  5        20 11 10 24  4        22 11 13  6  5
 1 12 20 15 19        14 21 16 12  6         2  0 12  3  7
 
At this point, the third board wins because it has at least one complete row or column of marked numbers (in this case, the entire top row is marked: 14 21 17 24 4).

The score of the winning board can now be calculated. Start by finding the sum of all unmarked numbers on that board; in this case, the sum is 188. Then, multiply that sum by the number that was just called when the board won, 24, to get the final score, 188 * 24 = 4512.

To guarantee victory against the giant squid, figure out which board will win first. What will your final score be if you choose that board?
*/

package day4;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class code {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner inFile1 = new Scanner(new File("day4\\input.txt"));
        List<String> numbers = Arrays.asList(inFile1.nextLine().split(","));
        List<String> numberLine = new ArrayList<String>();
        List<Board> boards = new ArrayList<Board>();
        inFile1.nextLine();
        while (inFile1.hasNext()) {
            while (inFile1.hasNextLine()) {
                while (inFile1.hasNext()) {
                    if (numberLine.size() == 25)
                        break;
                    numberLine.add(inFile1.next());
                }
                if (numberLine.size() == 25)
                    break;
                if (inFile1.hasNext())
                    inFile1.nextLine();
            }
            boards.add(new Board(numberLine));
            numberLine.clear();
            if (inFile1.hasNext())
                inFile1.nextLine();
        }
        int k = 0, currentN = 0;
        Board winnerBoard = null;
        while (winnerBoard == null) {
            GameManager.marker(boards, Integer.parseInt(numbers.get(k)));
            currentN = Integer.parseInt(numbers.get(k));
            k++;
            winnerBoard = GameManager.winnerAnnouncer(boards);
        }
        System.out.println("Game finished ! final score " + GameManager.scoreCalculator(winnerBoard, currentN));

    }

}

class GameManager {
    public static Board winnerAnnouncer(List<Board> boards) {
        for (Board b : boards) {
            if (GameManager.boardChecker(b)) {
                return b;
            }
        }
        return null;
    }

    public static void marker(List<Board> boards, int pickedNumber) {
        int i, j;
        for (Board b : boards) {
            for (i = 0; i < 5; i++) {
                for (j = 0; j < 5; j++) {
                    if (Character.toString(b.board[i][j].charAt(b.board[i][j].length() - 1)).equals("."))
                        continue;
                    if (Integer.parseInt(b.board[i][j]) == pickedNumber)
                        b.board[i][j] += "."; // to mark the found numbers
                }
            }
        }
    }

    public static int scoreCalculator(Board b, int pickedNumber) {
        int i, j, s = 0;
        for (i = 0; i < 5; i++) {
            for (j = 0; j < 5; j++) {
                if (Character.toString(b.board[i][j].charAt(b.board[i][j].length() - 1)).equals("."))
                    continue;
                else
                    s += Integer.parseInt(b.board[i][j]);
            }
        }
        return (s * pickedNumber);
    }

    public static boolean boardChecker(Board brd) {
        int i, j, c = 0;
        String[] row;
        for (i = 0; i < 5; i++) { // row
            row = brd.board[i];
            for (j = 0; j < 5; j++) {
                if (Character.toString(row[j].charAt(row[j].length() - 1)).equals(".")) {
                    c++;
                }
            }
            if (c == 5) {
                return true;
            }
            c = 0;
        }
        c = 0;
        for (i = 0; i < 5; i++) { // column
            for (j = 0; j < 5; j++) {
                if (Character.toString(brd.board[j][i].charAt(brd.board[j][i].length() - 1)).equals("."))
                    c++;
            }
            if (c == 5) {
                return true;
            }
            c = 0;
        }
        return false;
    }
}

class Board {
    String board[][] = new String[5][5];
    int i, j, k = 0;

    public Board(List<String> numbers) {
        for (i = 0; i < 5; i++) {
            for (j = 0; j < 5; j++) {
                board[i][j] = numbers.get(k);
                k++;
            }
        }
    }

    public void display() {
        for (i = 0; i < 5; i++) {
            for (j = 0; j < 5; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("\n\n");
    }

}