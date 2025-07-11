import java.util.ArrayList;
import java.util.List;

public class FireBlock extends Block {
    public FireBlock(Position position) {
        super(position);
    }

    @Override
    public boolean interact(GameBoard board) {
        if (isPartOfFirePattern(board)) {
            List<Position> toClear = getAffectedPositions();
            for (Position pos : toClear) {
                if (pos.getRow() >= 0 && pos.getRow() < 10 && pos.getCol() >= 0 && pos.getCol() < 10) {
                    board.setBlockAt(pos.getRow(), pos.getCol(), new EmptyBlock(pos));
                }
            }
            return true;
        }
        return false;
    }

    private boolean isPartOfFirePattern(GameBoard board) {
        int row = position.getRow();
        int col = position.getCol();

        // Check horizontal pattern (left-center-right)
        if (col > 0 && col < 9) {
            Block left = board.getBlockAt(row, col-1);
            Block right = board.getBlockAt(row, col+1);
            if (left instanceof FireBlock && right instanceof FireBlock) {
                return true;
            }
        }

        // Check vertical pattern (above-center-below)
        if (row > 0 && row < 9) {
            Block above = board.getBlockAt(row-1, col);
            Block below = board.getBlockAt(row+1, col);
            if (above instanceof FireBlock && below instanceof FireBlock) {
                return true;
            }
        }

        return false;
    }

    private List<Position> getAffectedPositions() {
        List<Position> positions = new ArrayList<>();
        int row = position.getRow();
        int col = position.getCol();

        // Add center and adjacent positions
        positions.add(new Position(row, col));
        positions.add(new Position(row-1, col));
        positions.add(new Position(row+1, col));
        positions.add(new Position(row, col-1));
        positions.add(new Position(row, col+1));

        return positions;
    }
}