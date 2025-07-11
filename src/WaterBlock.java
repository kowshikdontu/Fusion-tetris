public class WaterBlock extends Block {
    public WaterBlock(Position position) {
        super(position);
    }

    @Override
    public boolean interact(GameBoard board) {
        int row = position.getRow();
        int col = position.getCol();
        boolean cleared = false;

        // Check horizontal (left-center-right)
        if (col > 0 && col < 9) {
            Block left = board.getBlockAt(row, col-1);
            Block right = board.getBlockAt(row, col+1);
            if (left instanceof WaterBlock && right instanceof WaterBlock) {
                // Clear entire row
                for (int c = 0; c < 10; c++) {
                    if (!board.getBlockAt(row, c).isEmpty()) {
                        board.setBlockAt(row, c, new EmptyBlock(new Position(row, c)));
                        board.addScore(10); // Add score for each cleared block
                        cleared = true;
                    }
                }
            }
        }

        // Check vertical (above-center-below)
        if (row > 0 && row < 9) {
            Block above = board.getBlockAt(row-1, col);
            Block below = board.getBlockAt(row+1, col);
            if (above instanceof WaterBlock && below instanceof WaterBlock) {
                // Clear entire column
                for (int r = 0; r < 10; r++) {
                    if (!board.getBlockAt(r, col).isEmpty()) {
                        board.setBlockAt(r, col, new EmptyBlock(new Position(r, col)));
                        board.addScore(10); // Add score for each cleared block
                        cleared = true;
                    }
                }
            }
        }

        return cleared;
    }
}