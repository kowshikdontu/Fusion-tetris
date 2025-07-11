public class EmptyBlock extends Block {
    public EmptyBlock(Position position) {
        super(position);
    }

    public boolean interact(GameBoard board) {
        return false;
    }
}