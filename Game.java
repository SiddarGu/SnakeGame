package model;

import javax.swing.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;

public class Game {
    protected Cell[][] board;
    private int maxRows, maxCols, score;
    // 1 is up, 2 is right, 3 is down, 4 is left
    public int direction = 2;
    LinkedList<HashMap<Integer, Integer>> list;
    private HashMap<Integer, Integer> food;

    public Game(int maxRows, int maxCols) {
        // initialize the board
        board = new Cell[maxRows][maxCols];
        for (int row = 0; row < maxRows; row++) {
            for (int col = 0; col < maxCols; col++) {
                board[row][col] = Cell.EMPTY;
            }
        }
        list = new LinkedList<>();
        HashMap<Integer, Integer> head = new HashMap<>();
        HashMap<Integer, Integer> tail = new HashMap<>();
        food = new HashMap<>();
        head.put(0,1);
        tail.put(0,0);
        this.maxCols = maxCols;
        this.maxRows = maxRows;
        score = 0;
        list.add(head);
        list.add(tail);
        addFood();
    }

    public int getMaxRows() {
        return maxRows;
    }

    public int getMaxCols() {
        return maxCols;
    }

    public boolean isGameOver() {
        // returns true if indices are out of bounds
        if ((getHead()[0] >= maxRows) || (getHead()[1] >= maxCols) || (getHead()[0] < 0) || (getHead()[1] < 0)) {
            return true;
        }
        return false;
    }

    public int getScore() {
        return score;
    }

    public void process() {
        int headRow = getHead()[0];
        int headCol = getHead()[1];

        // set new head according to moving direction
        HashMap<Integer, Integer> head = new HashMap<>();
        // set new head
        if (direction == 1) {
            head.put(headRow - 1, headCol);
        }
        if (direction == 2) {
            head.put(headRow, headCol + 1);
        }
        if (direction == 3) {
            head.put(headRow + 1, headCol);
        }
        if (direction == 4) {
            head.put(headRow, headCol - 1);
        }
        list.addFirst(head);
        HashMap<Integer, Integer> tail = list.removeLast();

        // if the food is eaten
        if (isFoodEaten()) {
            // put the tail back
            list.add(tail);
            // remove the food reference
            food.clear();
            addFood();

        }
    }

    public void addFood() {
        score++;
        boolean foodSet = false;
        Random random = new Random();

        // add food
        while (!foodSet) {
            int hIndex = random.nextInt(maxRows);
            int vIndex = random.nextInt(maxCols);

            for (int row = 0; row < maxRows; row++) {
                for (int col = 0; col < maxCols; col++) {
                    if (hIndex == row && vIndex == col) {
                        // if the cell is empty
                        if (board[row][col].getColor() == Cell.EMPTY.getColor()) {
                            food.put(row, col);
                            foodSet = true;
                        }
                    }
                }
            }
        }
    }

    // returns true if the food is eaten
    public boolean isFoodEaten() {
        int key = -1;
        int value = -1;

        for (Map.Entry<Integer, Integer> entry : food.entrySet()) {
            key = entry.getKey();
            value = entry.getValue();
        }

        for (HashMap<Integer, Integer> map : list) {
            if (map.containsKey(key) && map.containsValue(value)) {
                return true;
            }
        }
        return false;
    }

    public void nextAnimationStep() {
        // set background
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = Cell.EMPTY;
            }
        }
        // set snake
        for (HashMap<Integer, Integer> map : list) {
            int h = -1;
            int v = -1;

            for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
                h = entry.getKey();
                v = entry.getValue();
            }
            try {
                board[h][v] = Cell.SNAKE;
            } catch (ArrayIndexOutOfBoundsException e) {
                String message = "Score " + score + "\n Game Over!";
                JOptionPane.showMessageDialog(null, message);
                System.exit(0);
            }

            // check for snake collide
            for (int i = 0; i < list.size(); i++) {
                for (int j = i + 1; j < list.size(); j++) {
                    if (list.get(i).entrySet().containsAll(list.get(j).entrySet())) {
                        String message = "Score " + score + "\n Game Over!";
                        JOptionPane.showMessageDialog(null, message);
                        System.exit(0);
                    }
                }
            }

        }
        // set food
        int hIndex = -1;
        int vIndex = -1;

        for (Map.Entry<Integer, Integer> entry : food.entrySet()) {
            hIndex = entry.getKey();
            vIndex = entry.getValue();
        }
        board[hIndex][vIndex] = Cell.FOOD;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    private int[] getHead() {
        int[] ans = new int[2];

        for (Map.Entry<Integer, Integer> entry : list.getFirst().entrySet()) {
            ans[0] = entry.getKey();
            ans[1] = entry.getValue();
        }
        return ans;
    }

    public Cell[][] getBoard() {
        return board;
    }

    public int getDirection() {
        return direction;
    }
}
