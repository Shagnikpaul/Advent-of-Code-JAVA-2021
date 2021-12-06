/*
--- Part Two ---
On the other hand, it might be wise to try a different strategy: let the giant squid win.

You aren't sure how many bingo boards a giant squid could play at once, so rather than waste time counting its arms, the safe thing to do is to figure out which board will win last and choose that one. That way, no matter which boards it picks, it will win for sure.

In the above example, the second board is the last to win, which happens after 13 is eventually called and its middle column is completely marked. If you were to keep playing until this point, the second board would have a sum of unmarked numbers equal to 148 for a final score of 148 * 13 = 1924.

Figure out which board will win last. Once it wins, what would its final score be?
*/

package day4;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class code2 {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner inFile1 = new Scanner(new File("day4\\input.txt"));
        List<String> numbers = Arrays.asList(inFile1.nextLine().split(","));
        List<String> numberLine = new ArrayList<String>();
        List<Board> boards = new ArrayList<Board>();
        boolean flag = true;
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
        while (boards.size() > 1) {  // picking the last winner ...
            GameManager.marker(boards, Integer.parseInt(numbers.get(k)));
            boards = GameManager.winnerSorter(boards);
            k++;
        }
        winnerBoard = boards.get(0);
        while (flag) { // after thats done now we need to get the last picked number for which this board will win...
            GameManager.marker(boards, Integer.parseInt(numbers.get(k)));
            currentN = Integer.parseInt(numbers.get(k));
            if (GameManager.boardChecker(winnerBoard))
                flag = false;
            else {
                k++;
            }
        }
        winnerBoard.display();
        System.out.println("Game finished ! final score " + GameManager.scoreCalculator(winnerBoard, currentN));

    }

}




class GameManager {
    public static List<Board> winnerSorter(List<Board> boards) {
        List<Board> toRemove = new ArrayList<Board>();
        for (Board b : boards) {
            if (GameManager.boardChecker(b)) {
                toRemove.add(b);
            }
        }
        boards.removeAll(toRemove);
        return boards;
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
        System.out.println("SUum " + s);
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
