import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GameBoard {
    private Block[][] board;
    private Random random;
    private int score;

    public GameBoard() {
        board = new Block[10][10];
        random = new Random();
        score = 0;
        resetBoard();
    }

    public int getScore() {
        return score;
    }

    public void addScore(int points) {
        score += points;
    }

    public void resetBoard() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                board[i][j] = new EmptyBlock(new Position(i, j));
            }
        }
    }

    public boolean dropBlock(Block block, int col) {
        if (col < 0 || col >= 10) return false;

        for (int row = 9; row >= 0; row--) {
            if (board[row][col].isEmpty()) {
                setBlockAt(row, col, block);
                return true;
            }
        }
        return false;
    }

    public void setBlockAt(int row, int col, Block block) {
        if (row >= 0 && row < 10 && col >= 0 && col < 10) {
            block.setPosition(new Position(row, col));
            board[row][col] = block;
        }
    }

    public Block getBlockAt(int row, int col) {
        if (row >= 0 && row < 10 && col >= 0 && col < 10) {
            return board[row][col];
        }
        return null;
    }

    public void updateBoard() {
        boolean updated;
        int iterations = 0;
        final int MAX_ITERATIONS = 10;

        do {
            updated = false;
            for (int row = 9; row >= 0; row--) {
                for (int col = 0; col < 10; col++) {
                    Block current = getBlockAt(row, col);
                    if (!current.isEmpty()) {
                        if (current.interact(this)) {
                            updated = true;
                        }
                    }
                }
            }
            if (updated) {
                applyGravity();
            }
            iterations++;
        } while (updated && iterations < MAX_ITERATIONS);
    }

    public void shuffleRow(int row) {
        if (row < 0 || row >= 10) return;

        List<Block> rowBlocks = new ArrayList<>();
        for (int col = 0; col < 10; col++) {
            rowBlocks.add(board[row][col]);
        }

        Collections.shuffle(rowBlocks);

        for (int col = 0; col < 10; col++) {
            Block block = rowBlocks.get(col);
            block.setPosition(new Position(row, col));
            board[row][col] = block;
        }
    }

    public void applyGravity() {
        for (int col = 0; col < 10; col++) {
            int emptyRow = 9;
            for (int row = 9; row >= 0; row--) {
                if (!board[row][col].isEmpty()) {
                    if (row != emptyRow) {
                        Block block = board[row][col];
                        board[row][col] = new EmptyBlock(new Position(row, col));
                        board[emptyRow][col] = block;
                        block.setPosition(new Position(emptyRow, col));
                    }
                    emptyRow--;
                }
            }
        }
    }

    public void shuffleTopLayer() {
        List<Block> topBlocks = new ArrayList<>();
        List<Integer> colsWithBlocks = new ArrayList<>();

        for (int col = 0; col < 10; col++) {
            for (int row = 0; row < 10; row++) {
                if (!board[row][col].isEmpty()) {
                    topBlocks.add(board[row][col]);
                    colsWithBlocks.add(col);
                    break;
                }
            }
        }

        Collections.shuffle(topBlocks);

        for (int i = 0; i < topBlocks.size(); i++) {
            int col = colsWithBlocks.get(i);
            for (int row = 0; row < 10; row++) {
                if (!board[row][col].isEmpty()) {
                    board[row][col] = topBlocks.get(i);
                    topBlocks.get(i).setPosition(new Position(row, col));
                    break;
                }
            }
        }
    }

    public boolean isColumnFull(int col) {
        return !board[0][col].isEmpty();
    }


    public boolean isBoardFull() {
        for (int col = 0; col < 10; col++) {
            if (!isColumnFull(col)) {
                return false;
            }
        }
        return true;
    }
    public void printBoard() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                String symbol = board[i][j] instanceof EmptyBlock ? "." :
                        board[i][j] instanceof FireBlock ? "F" :
                                board[i][j] instanceof WaterBlock ? "W" :
                                        board[i][j] instanceof EarthBlock ? "E" :
                                                board[i][j] instanceof AirBlock ? "A" : "N";
                System.out.print(symbol + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
}