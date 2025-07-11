public class EarthBlock extends Block {
    public EarthBlock(Position position) {
        super(position);
    }

    public boolean interact(GameBoard board) {
        int row = position.getRow();
        int col = position.getCol();

        int rowCount = 1;
        int colCount = 1;

        for (int i = col - 1; i >= 0; i--) {
            Block b = board.getBlockAt(row, i);
            if (b instanceof EarthBlock) rowCount++;
            else break;
        }

        for (int i = col + 1; i < 10; i++) {
            Block b = board.getBlockAt(row, i);
            if (b instanceof EarthBlock) rowCount++;
            else break;
        }

        for (int i = row - 1; i >= 0; i--) {
            Block b = board.getBlockAt(i, col);
            if (b instanceof EarthBlock) colCount++;
            else break;
        }

        for (int i = row + 1; i < 10; i++) {
            Block b = board.getBlockAt(i, col);
            if (b instanceof EarthBlock) colCount++;
            else break;
        }

        if (rowCount >= 3 || colCount >= 3) {
            board.shuffleRow(9);
            return true;
        }
        return false;
    }
}