public class AirBlock extends Block {
    public AirBlock(Position position) {
        super(position);
    }

    @Override
    public boolean interact(GameBoard board) {
        int row = position.getRow();
        int col = position.getCol();

        // Check for 3+ AirBlocks in a row
        int horizontalCount = countConnectedAirBlocks(board, row, col, 0, 1)
                + countConnectedAirBlocks(board, row, col, 0, -1) - 1;

        // Check for 3+ AirBlocks in a column
        int verticalCount = countConnectedAirBlocks(board, row, col, 1, 0)
                + countConnectedAirBlocks(board, row, col, -1, 0) - 1;

        if (horizontalCount >= 3 || verticalCount >= 3) {
            board.shuffleTopLayer();
            return true;
        }
        return false;
    }

    private int countConnectedAirBlocks(GameBoard board, int row, int col, int rowDir, int colDir) {
        int count = 0;
        while (row >= 0 && row < 10 && col >= 0 && col < 10
                && board.getBlockAt(row, col) instanceof AirBlock) {
            count++;
            row += rowDir;
            col += colDir;
        }
        return count;
    }
}